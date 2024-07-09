package service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import model.PivotRow;
import model.Sklad;
import model.SkladPivot;

public class ExportPivotService extends Service<String> {

    LocalDate from;
    LocalDate to;
    List<PivotRow> list;
    Sklad[] sklads;

    public void startService(LocalDate from, LocalDate to, List<PivotRow> list, Sklad[] sklads) {
        if (!isRunning()) {
            this.from = from;
            this.to = to;
            this.list = list;
            this.sklads = sklads;
            reset();
            start();
        }
    }

    public boolean stopService() {
        if (isRunning()) {
            return cancel();
        }
        return false;
    }

    @Override
    protected Task<String> createTask() {
        return new Task<String>() {
            @Override
            protected String call() throws Exception {
                String fileName = String.format("pivot%s_%s.xlsx", from.format(DateTimeFormatter.ofPattern("yyyyMMdd")),
                        to.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
                String reportsPath = "./reports/pivot/";
                Path path = Paths.get(reportsPath);

                if (!Files.exists(path)) {
                    try {
                        Files.createDirectories(path);
                    } catch (IOException e) {
                        System.out.println("Failed to create reports directory!" + e.getMessage());
                    }
                }

                XSSFWorkbook workbook = new XSSFWorkbook();
                XSSFCellStyle style = workbook.createCellStyle();
                style.setBorderBottom(BorderStyle.THIN);
                style.setBorderTop(BorderStyle.THIN);
                style.setBorderLeft(BorderStyle.THIN);
                style.setBorderRight(BorderStyle.THIN);
                style.setWrapText(true);

                XSSFFont font = workbook.createFont();
                font.setFontHeightInPoints((short) 10);
                font.setFontName("Arial");
                font.setBold(true);
                style.setFont(font);

                XSSFCellStyle cellStyle = workbook.createCellStyle();
                cellStyle.setBorderBottom(BorderStyle.THIN);
                cellStyle.setBorderTop(BorderStyle.THIN);
                cellStyle.setBorderLeft(BorderStyle.THIN);
                cellStyle.setBorderRight(BorderStyle.THIN);

                XSSFCellStyle headStyle = workbook.createCellStyle();
                headStyle.setBorderBottom(BorderStyle.THIN);
                headStyle.setBorderTop(BorderStyle.THIN);
                headStyle.setBorderLeft(BorderStyle.THIN);
                headStyle.setBorderRight(BorderStyle.THIN);
                headStyle.setWrapText(true);
                headStyle.setAlignment(HorizontalAlignment.CENTER);
                headStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                headStyle.setFont(font);
                int i = 0;
                this.updateProgress(i, list.size());
                updateMessage("Начало экспорта");
                int rowCount = 0;
                updateMessage("Формирую  данные");
                XSSFSheet sheet = workbook.createSheet();

                sheet.createFreezePane(0, 4, 0, 4);

                XSSFCellStyle s = workbook.createCellStyle();
                s.setFont(font);
                s.setAlignment(HorizontalAlignment.CENTER);

                int colRow = 9 + sklads.length * 6;

                XSSFRow row = sheet.createRow(rowCount);
                sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, colRow));
                rowCount++;
                XSSFCell cellHeader = row.createCell(0);
                cellHeader.setCellValue("Сводная реализация товара ");
                cellHeader.setCellStyle(s);
                row = sheet.createRow(rowCount);
                sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, colRow));
                cellHeader = row.createCell(0);
                cellHeader.setCellType(CellType.STRING);
                cellHeader.setCellStyle(s);
                cellHeader.setCellValue(String.format("За период с %s по %s",
                        from.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                        to.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))));
                rowCount++;
                row = sheet.createRow(rowCount);
                for (int j = 0; j < sklads.length; j++) {
                    int start = j + 3 + j * 5;
                    int stop = start + 5;
                    sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, start, stop));
                    cellHeader = row.createCell(start);
                    cellHeader.setCellType(CellType.STRING);
                    cellHeader.setCellStyle(headStyle);
                    cellHeader.setCellValue(String.format("Склад %s ", sklads[j].getNAME()));
                }
                int start = sklads.length + 3 + sklads.length * 5;
                int stop = start + 5;
                sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, start, stop));
                cellHeader = row.createCell(start);
                cellHeader.setCellType(CellType.STRING);
                cellHeader.setCellStyle(headStyle);
                cellHeader.setCellValue("Сума");
                rowCount++;
                row = sheet.createRow(rowCount);
                rowCount++;

                row.setHeight((short) 1000);
                cellHeader.getCellStyle().setVerticalAlignment(VerticalAlignment.TOP);

                cellHeader = row.createCell(0);
                cellHeader.setCellType(CellType.STRING);
                cellHeader.setCellValue("Артикул");
                cellHeader.setCellStyle(headStyle);
                cellHeader = row.createCell(1);
                cellHeader.setCellType(CellType.STRING);
                cellHeader.setCellValue("Наименование");
                cellHeader.setCellStyle(headStyle);
                cellHeader = row.createCell(2);
                cellHeader.setCellType(CellType.STRING);
                cellHeader.setCellValue("Производитель");
                cellHeader.setCellStyle(headStyle);
                int r = 3;
                SkladPivot[] total = new SkladPivot[sklads.length];
                for (int j = 0; j < sklads.length; j++) {
                    total[j] = new SkladPivot();
                }
                for (int j = 0; j < sklads.length; j++) {
                    cellHeader = row.createCell(r);
                    r++;
                    cellHeader.setCellType(CellType.STRING);
                    cellHeader.setCellValue("Реализация (количество)");
                    cellHeader.setCellStyle(headStyle);
                    cellHeader = row.createCell(r);
                    r++;
                    cellHeader.setCellType(CellType.STRING);
                    cellHeader.setCellValue("Продаж у закупівельних цінах, без ПДВ УМНОЖ на КОЛ_ВО РЕАЛИЗ");
                    cellHeader.setCellStyle(headStyle);
                    cellHeader = row.createCell(r);
                    r++;
                    cellHeader.setCellType(CellType.STRING);
                    cellHeader.setCellValue("Продаж у закупівельних цінах з ПДВ УМНОЖ на КОЛ_ВО РЕАЛИЗ");
                    cellHeader.setCellStyle(headStyle);
                    cellHeader = row.createCell(r);
                    r++;
                    cellHeader.setCellType(CellType.STRING);
                    cellHeader.setCellValue("Сумма продажу в цінах реалізації УМНОЖ на КОЛ_ВО РЕАЛИЗ");
                    cellHeader.setCellStyle(headStyle);
                    cellHeader = row.createCell(r);
                    r++;
                    cellHeader.setCellType(CellType.STRING);
                    cellHeader.setCellValue("Сумма продажу в роздрібних цінах УМНОЖ на КОЛ_ВО РЕАЛИЗ");
                    cellHeader.setCellStyle(headStyle);
                    cellHeader = row.createCell(r);
                    r++;
                    cellHeader.setCellType(CellType.STRING);
                    cellHeader.setCellValue("Валовий прибуток");
                    cellHeader.setCellStyle(headStyle);
                }
                cellHeader = row.createCell(r);
                r++;
                cellHeader.setCellType(CellType.STRING);
                cellHeader.setCellValue("Ітого Реализация (количество)");
                cellHeader.setCellStyle(headStyle);
                cellHeader = row.createCell(r);
                r++;
                cellHeader.setCellType(CellType.STRING);
                cellHeader.setCellValue("Ітого продаж у закупівельних цінах, без ПДВ");
                cellHeader.setCellStyle(headStyle);
                cellHeader = row.createCell(r);
                r++;
                cellHeader.setCellType(CellType.STRING);
                cellHeader.setCellValue("Ітого продаж у закупівельних цінах з ПДВ");
                cellHeader.setCellStyle(headStyle);
                cellHeader = row.createCell(r);
                r++;
                cellHeader.setCellType(CellType.STRING);
                cellHeader.setCellValue("Ітого сумма продажу в цінах реалізації");
                cellHeader.setCellStyle(headStyle);
                cellHeader = row.createCell(r);
                r++;
                cellHeader.setCellType(CellType.STRING);
                cellHeader.setCellValue("Ітого сумма продажу в роздрібних цінах");
                cellHeader.setCellStyle(headStyle);
                cellHeader = row.createCell(r);
                r++;
                cellHeader.setCellType(CellType.STRING);
                cellHeader.setCellValue("Ітого валовий прибуток");
                cellHeader.setCellStyle(headStyle);
                cellHeader = row.createCell(r);
                r++;
                cellHeader.setCellType(CellType.STRING);
                cellHeader.setCellValue("Штрих-код(заводской)");
                cellHeader.setCellStyle(headStyle);

                for (PivotRow pivot : list) {
                    r = 0;
                    row = sheet.createRow(rowCount);
                    rowCount++;
                    XSSFCell cell = row.createCell(r);
                    r++;
                    cell.setCellStyle(cellStyle);
                    cell.setCellType(CellType.STRING);
                    cell.setCellValue(pivot.getNAMEKOD());
                    cell = row.createCell(r);
                    r++;
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(pivot.getNAME());
                    cell = row.createCell(r);
                    r++;
                    cell.setCellType(CellType.STRING);
                    cell.setCellValue(pivot.getKLASS_NAME());
                    cell.setCellStyle(cellStyle);

                    for (int j = 0; j < sklads.length; j++) {
                        cell = row.createCell(r);
                        r++;
                        cell.setCellType(CellType.NUMERIC);
                        cell.setCellValue(pivot.getSkladPivots()[j].getKOLVO());
                        cell.setCellStyle(cellStyle);
                        cell = row.createCell(r);
                        total[j].setKOLVO(total[j].getKOLVO() + pivot.getSkladPivots()[j].getKOLVO());

                        r++;
                        cell.setCellType(CellType.NUMERIC);
                        cell.setCellValue(pivot.getSkladPivots()[j].getSUM_BEZ_NDS());
                        cell.setCellStyle(cellStyle);
                        cell = row.createCell(r);
                        total[j].setSUM_BEZ_NDS(total[j].getSUM_BEZ_NDS() + pivot.getSkladPivots()[j].getSUM_BEZ_NDS());
                        r++;
                        cell.setCellType(CellType.NUMERIC);
                        cell.setCellValue(pivot.getSkladPivots()[j].getSUM_NDS());
                        cell.setCellStyle(cellStyle);
                        cell = row.createCell(r);
                        total[j].setSUM_NDS(total[j].getSUM_NDS() + pivot.getSkladPivots()[j].getSUM_NDS());
                        r++;
                        cell.setCellType(CellType.NUMERIC);
                        cell.setCellValue(pivot.getSkladPivots()[j].getSUM_REAL());
                        cell.setCellStyle(cellStyle);
                        cell = row.createCell(r);
                        total[j].setSUM_REAL(total[j].getSUM_REAL() + pivot.getSkladPivots()[j].getSUM_REAL());
                        r++;
                        cell.setCellType(CellType.NUMERIC);
                        cell.setCellValue(pivot.getSkladPivots()[j].getFIRST_RASX());
                        cell.setCellStyle(cellStyle);
                        cell = row.createCell(r);
                        total[j].setFIRST_RASX(total[j].getFIRST_RASX() + pivot.getSkladPivots()[j].getFIRST_RASX());
                        r++;
                        cell.setCellType(CellType.NUMERIC);
                        cell.setCellValue(pivot.getSkladPivots()[j].getVAL_DOHOD());
                        total[j].setVAL_DOHOD(total[j].getVAL_DOHOD() + pivot.getSkladPivots()[j].getVAL_DOHOD());
                        cell.setCellStyle(cellStyle);
                    }

                    cell = row.createCell(r);
                    r++;
                    cell.setCellType(CellType.NUMERIC);

                    cell.setCellValue(pivot.getKOLVO());
                    cell.setCellStyle(cellStyle);
                    cell = row.createCell(r);
                    r++;
                    cell.setCellType(CellType.NUMERIC);
                    cell.setCellValue(pivot.getSUM_BEZ_NDS());
                    cell.setCellStyle(cellStyle);
                    cell = row.createCell(r);
                    r++;
                    cell.setCellType(CellType.NUMERIC);
                    cell.setCellValue(pivot.getSUM_NDS());
                    cell.setCellStyle(cellStyle);
                    cell = row.createCell(r);
                    r++;
                    cell.setCellType(CellType.NUMERIC);
                    cell.setCellValue(pivot.getSUM_REAL());
                    cell.setCellStyle(cellStyle);
                    cell = row.createCell(r);
                    r++;
                    cell.setCellType(CellType.NUMERIC);
                    cell.setCellValue(pivot.getFIRST_RASX());
                    cell.setCellStyle(cellStyle);
                    cell = row.createCell(r);
                    r++;
                    cell.setCellType(CellType.NUMERIC);
                    cell.setCellValue(pivot.getVAL_DOHOD());
                    cell.setCellStyle(cellStyle);
                    cell = row.createCell(r);
                    r++;
                    cell.setCellType(CellType.STRING);
                    cell.setCellValue(pivot.getSHTRIHKOD());
                    cell.setCellStyle(cellStyle);
                    i++;
                    this.updateProgress(i, list.size());

                }
                row = sheet.createRow(rowCount);
                rowCount++;

                XSSFCellStyle totalStyle = row.getRowStyle();
                if (totalStyle == null) {
                    totalStyle = row.getSheet().getWorkbook().createCellStyle();
                }
                totalStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
                totalStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                row.setRowStyle(totalStyle);

                XSSFCell cell = row.createCell(0);
                cell.setCellType(CellType.STRING);
                cell.setCellValue("ВСЕГО");

                r = 3;
                double sumBezNds = 0;
                double sumNds = 0;
                double sumReal = 0;
                double sumRasx = 0;
                double sumDohod = 0;
                double sumKolvo = 0;
                for (int j = 0; j < sklads.length; j++) {
                    cell = row.createCell(r);
                    r++;
                    cell.setCellType(CellType.NUMERIC);
                    cell.setCellValue(total[j].getKOLVO());
                    sumKolvo += total[j].getKOLVO();
                    cell = row.createCell(r);
                    r++;
                    cell.setCellType(CellType.NUMERIC);
                    cell.setCellValue(total[j].getSUM_BEZ_NDS());
                    sumBezNds += total[j].getSUM_BEZ_NDS();
                    cell = row.createCell(r);
                    r++;
                    cell.setCellType(CellType.NUMERIC);
                    cell.setCellValue(total[j].getSUM_NDS());
                    sumNds += total[j].getSUM_NDS();
                    cell = row.createCell(r);
                    r++;
                    cell.setCellType(CellType.NUMERIC);
                    cell.setCellValue(total[j].getSUM_REAL());
                    sumReal += total[j].getSUM_REAL();
                    cell = row.createCell(r);
                    r++;
                    cell.setCellType(CellType.NUMERIC);
                    cell.setCellValue(total[j].getFIRST_RASX());
                    sumRasx += total[j].getFIRST_RASX();
                    cell = row.createCell(r);
                    r++;
                    cell.setCellType(CellType.NUMERIC);
                    cell.setCellValue(total[j].getVAL_DOHOD());
                    sumDohod += total[j].getVAL_DOHOD();
                }
                cell = row.createCell(r);
                r++;
                cell.setCellType(CellType.NUMERIC);
                cell.setCellValue(sumKolvo);

                cell = row.createCell(r);
                r++;
                cell.setCellType(CellType.NUMERIC);
                cell.setCellValue(sumBezNds);

                cell = row.createCell(r);
                r++;
                cell.setCellType(CellType.NUMERIC);
                cell.setCellValue(sumNds);

                cell = row.createCell(r);
                r++;
                cell.setCellType(CellType.NUMERIC);
                cell.setCellValue(sumReal);

                cell = row.createCell(r);
                r++;
                cell.setCellType(CellType.NUMERIC);
                cell.setCellValue(sumRasx);

                cell = row.createCell(r);
                r++;
                cell.setCellType(CellType.NUMERIC);
                cell.setCellValue(sumDohod);
                r++;

                try {
                    FileOutputStream outputStream;
                    outputStream = new FileOutputStream(reportsPath + fileName);
                    workbook.write(outputStream);
                    workbook.close();
                    outputStream.close();
                    workbook = null;
                    updateMessage("Записано в " + fileName);
                } catch (FileNotFoundException ex) {
                    System.out.println(ex);
                    updateMessage(ex.getMessage());
                } catch (IOException ex) {
                    System.out.println(ex);
                    updateMessage(ex.getMessage());
                }

                return fileName;
            }
        };
    }
}
