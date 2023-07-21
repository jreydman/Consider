/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vvu.report.service;

import com.vvu.report.model.Report;
import com.vvu.report.model.Sklad;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.vvu.report.repos.NakladRepository;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.DateFormatConverter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Root
 */
public class ExportService extends Service<String> {

    LocalDate from;
    LocalDate to;
    Map<Sklad, List<Report>> map;

    boolean group = false;

    public void startService(LocalDate from, LocalDate to, Map<Sklad, List<Report>> map, boolean group) {

        if (!isRunning()) {
            this.from = from;
            this.to = to;
            this.map = map;
            this.group = group;
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

    private String getDirName() {
        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        return new StringBuilder("./export/").append(format.format(date)).toString();
    }

    private String getFileName() {
        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("HH_mm");
        return new StringBuilder("/dump_").append(format.format(date)).append(".xls").toString();
    }

    @Override
    protected Task createTask() {
        return new Task<String>() {
            @Override
            protected String call() throws Exception {
                File f = new File(getDirName());
                //System.out.println(dateTime.format(DateTimeFormatter.ofPattern("d::MMM::uuuu HH::mm::ss")));
                String fileName = String.format("report%s_%s.xlsx", from.format(DateTimeFormatter.ofPattern("yyyyMMdd")), to.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
                if (group) {
                    fileName = "gr" + fileName;
                }
                String userHome = "./";//System.getProperty("user.home");
                //File desktop = new File(System.getProperty("user.home"), "Desktop");
                //userHome=desktop.getAbsolutePath()+"//";

                //log.info("Start export..");
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

                int ind = 0;
                int i = 0;
                this.updateProgress(i, map.keySet().size());
                updateMessage("Начало экспорта");
                for (Map.Entry<Sklad, List<Report>> entry : map.entrySet()) {

                    int rowCount = 0;
                    Sklad sklad = entry.getKey();
                    updateMessage(String.format("Формирую  данные по складу %s", sklad.getNAME()));
                    XSSFSheet sheet = workbook.createSheet(String.format("SKLAD %d", sklad.getKOD()));
                    //workbook.setActiveSheet(ind);
                    sheet.createFreezePane( 0, 3, 0, 3 );

                    ind++;
                    XSSFRow row = sheet.createRow(rowCount);
                    sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, 13));
                    XSSFCell cellHeader = row.createCell(0);
                    cellHeader.setCellType(CellType.STRING);

                    XSSFCellStyle s = workbook.createCellStyle();
                    s.setFont(font);
                    s.setAlignment(HorizontalAlignment.CENTER);

                    cellHeader.setCellValue(String.format("Реализация товара по складу %s", sklad.getNAME()));
                    cellHeader.setCellStyle(s);
                    rowCount++;
                    row = sheet.createRow(rowCount);
                    sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, 13));
                    cellHeader = row.createCell(0);
                    cellHeader.setCellType(CellType.STRING);
                    cellHeader.setCellValue(
                            String.format("За период с %s по %s",
                                    from.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                                    to.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))));

                    cellHeader.setCellStyle(s);

                    rowCount++;
                    row = sheet.createRow(rowCount);
                    row.setHeightInPoints(40);
                    rowCount++;
                    cellHeader = row.createCell(0);
                    cellHeader.setCellType(CellType.STRING);
                    cellHeader.setCellValue("Внутренний код (справочник)");
                    cellHeader.setCellStyle(headStyle);
                    cellHeader = row.createCell(1);
                    cellHeader.setCellType(CellType.STRING);
                    cellHeader.setCellValue("Дата прихода  в базу (последняя)");
                    cellHeader.setCellStyle(headStyle);
                    cellHeader = row.createCell(2);
                    cellHeader.setCellType(CellType.STRING);
                    cellHeader.setCellValue("Кол-во последнего прихода");
                    cellHeader.setCellStyle(headStyle);
                    cellHeader = row.createCell(3);
                    cellHeader.setCellType(CellType.STRING);
                    cellHeader.setCellValue("Наименование товара");
                    cellHeader.setCellStyle(headStyle);
                    cellHeader = row.createCell(4);
                    cellHeader.setCellType(CellType.STRING);
                    cellHeader.setCellValue("Производитель");
                    cellHeader.setCellStyle(headStyle);
                    cellHeader = row.createCell(5);
                    cellHeader.setCellType(CellType.STRING);
                    cellHeader.setCellValue("Реализация  Кол-во ");
                    cellHeader.setCellStyle(headStyle);
                    cellHeader = row.createCell(6);
                    cellHeader.setCellType(CellType.STRING);
                    cellHeader.setCellValue("Цена продажи");
                    cellHeader.setCellStyle(headStyle);
                    cellHeader = row.createCell(7);
                    cellHeader.setCellType(CellType.STRING);
                    cellHeader.setCellValue("Сумма по приходу без НДС (Закупка)");
                    cellHeader.setCellStyle(headStyle);
                    cellHeader = row.createCell(8);
                    cellHeader.setCellType(CellType.STRING);
                    cellHeader.setCellValue("Сумма по приходу с НДС (Закупка)");
                    cellHeader.setCellStyle(headStyle);
                    cellHeader = row.createCell(9);
                    cellHeader.setCellType(CellType.STRING);
                    cellHeader.setCellValue("Сумма реализации всего розница");
                    cellHeader.setCellStyle(headStyle);
                    cellHeader = row.createCell(10);
                    cellHeader.setCellType(CellType.STRING);
                    cellHeader.setCellValue("Валовый доход  (наценка розница-закупка с НДС)");
                    cellHeader.setCellStyle(headStyle);
                    cellHeader = row.createCell(11);
                    cellHeader.setCellType(CellType.STRING);
                    cellHeader.setCellValue("Цена розница");
                    cellHeader.setCellStyle(headStyle);
                    cellHeader = row.createCell(12);
                    cellHeader.setCellType(CellType.STRING);
                    cellHeader.setCellValue("Поставщик");
                    cellHeader.setCellStyle(headStyle);

                    cellHeader = row.createCell(13);
                    cellHeader.setCellType(CellType.STRING);
                    cellHeader.setCellValue("Штрих-код (заводской)");
                    cellHeader.setCellStyle(headStyle);

                    cellHeader = row.createCell(14);
                    cellHeader.setCellType(CellType.STRING);
                    cellHeader.setCellValue("Уктзед");
                    cellHeader.setCellStyle(headStyle);

                    cellHeader = row.createCell(15);
                    cellHeader.setCellType(CellType.STRING);
                    cellHeader.setCellValue("Дата/время чека");
                    cellHeader.setCellStyle(headStyle);

                    cellHeader = row.createCell(16);
                    cellHeader.setCellType(CellType.STRING);
                    cellHeader.setCellValue("Дата чека");
                    cellHeader.setCellStyle(headStyle);

                    cellHeader = row.createCell(17);
                    cellHeader.setCellType(CellType.STRING);
                    cellHeader.setCellValue("Тип чека");
                    cellHeader.setCellStyle(headStyle);

                    cellHeader = row.createCell(18);
                    cellHeader.setCellType(CellType.STRING);
                    cellHeader.setCellValue("НДС");
                    cellHeader.setCellStyle(headStyle);

                    cellHeader = row.createCell(19);
                    cellHeader.setCellType(CellType.STRING);
                    cellHeader.setCellValue("Ставка НДС");
                    cellHeader.setCellStyle(headStyle);
                    
                    cellHeader = row.createCell(20);
                    cellHeader.setCellType(CellType.STRING);
                    cellHeader.setCellValue("Кассир");
                    cellHeader.setCellStyle(headStyle);
                    
                    cellHeader = row.createCell(21);
                    cellHeader.setCellType(CellType.STRING);
                    cellHeader.setCellValue("Допинфо");
                    cellHeader.setCellStyle(headStyle);
                    
                    cellHeader = row.createCell(22);
                    cellHeader.setCellType(CellType.STRING);
                    cellHeader.setCellValue("Скидка");
                    cellHeader.setCellStyle(headStyle);
                    
                    cellHeader = row.createCell(23);
                    cellHeader.setCellType(CellType.STRING);
                    cellHeader.setCellValue("Скидка в процентах");
                    cellHeader.setCellStyle(headStyle);

                    XSSFCellStyle dateStyle = workbook.createCellStyle();
                    dateStyle.setDataFormat((short) 14);
                    dateStyle.setBorderBottom(BorderStyle.THIN);
                    dateStyle.setBorderTop(BorderStyle.THIN);
                    dateStyle.setBorderLeft(BorderStyle.THIN);
                    dateStyle.setBorderRight(BorderStyle.THIN);

                    XSSFCellStyle dateTimeStyle = workbook.createCellStyle();
                    String excelFormatPattern = DateFormatConverter.convert(Locale.GERMAN, "dd.mm.yyyy hh:mm:ss");
                    DataFormat poiFormat = workbook.createDataFormat();
                    dateTimeStyle.setDataFormat(poiFormat.getFormat(excelFormatPattern));
                    dateTimeStyle.setBorderBottom(BorderStyle.THIN);
                    dateTimeStyle.setBorderTop(BorderStyle.THIN);
                    dateTimeStyle.setBorderLeft(BorderStyle.THIN);
                    dateTimeStyle.setBorderRight(BorderStyle.THIN);
                    for (Report r : entry.getValue()) {
                        row = sheet.createRow(rowCount);
                        rowCount++;
                        XSSFCell cell = row.createCell(0);
                        cell.setCellStyle(cellStyle);
                        cell.setCellType(CellType.STRING);
                        cell.setCellValue(r.getNAMEKOD());
                        cell = row.createCell(1);
                        cell.setCellStyle(dateStyle);
                        if (r.getLAST_DATE() == null) {
                            cell.setCellValue("");
                        } else {
                            cell.setCellValue(r.getLAST_DATE());
                        }
                        cell = row.createCell(2);
                        cell.setCellType(CellType.NUMERIC);
                        cell.setCellValue(r.getKOLVO_PRIHOD());
                        cell.setCellStyle(cellStyle);

                        cell = row.createCell(3);
                        cell.setCellType(CellType.STRING);
                        cell.setCellValue(r.getNAME());
                        cell.setCellStyle(cellStyle);

                        cell = row.createCell(4);
                        cell.setCellType(CellType.STRING);
                        cell.setCellValue(r.getKLASS_NAME());
                        cell.setCellStyle(cellStyle);

                        cell = row.createCell(5);
                        cell.setCellType(CellType.NUMERIC);
                        cell.setCellValue(r.getKOLVO());
                        cell.setCellStyle(cellStyle);

                        cell = row.createCell(6);
                        cell.setCellType(CellType.NUMERIC);
                        cell.setCellValue(r.getCENARASX());
                        cell.setCellStyle(cellStyle);

                        cell = row.createCell(7);
                        cell.setCellType(CellType.NUMERIC);
                        cell.setCellValue(r.getSUM_BEZ_NDS());
                        cell.setCellStyle(cellStyle);

                        cell = row.createCell(8);
                        cell.setCellType(CellType.NUMERIC);
                        cell.setCellValue(r.getSUM_NDS());
                        cell.setCellStyle(cellStyle);

                        cell = row.createCell(9);
                        cell.setCellType(CellType.NUMERIC);
                        cell.setCellValue(r.getSUM_REAL());
                        cell.setCellStyle(cellStyle);

                        cell = row.createCell(10);
                        cell.setCellType(CellType.NUMERIC);
                        cell.setCellValue(r.getVAL_DOHOD());
                        cell.setCellStyle(cellStyle);
                        cell = row.createCell(11);
                        cell.setCellType(CellType.NUMERIC);
                        cell.setCellValue(r.getCENA_ROZN());
                        cell.setCellStyle(cellStyle);

                        cell = row.createCell(12);
                        cell.setCellType(CellType.STRING);
                        cell.setCellValue(r.getPOSTAVKA_NAME());
                        cell.setCellStyle(cellStyle);

                        cell = row.createCell(13);
                        cell.setCellType(CellType.STRING);
                        cell.setCellValue(r.getSHTRIHKOD());
                        cell.setCellStyle(cellStyle);

                        cell = row.createCell(14);
                        cell.setCellType(CellType.STRING);
                        cell.setCellValue(r.getVEDKODC());
                        cell.setCellStyle(cellStyle);

                        cell = row.createCell(15);
                        cell.setCellStyle(dateTimeStyle);
                        if (r.getDATESROK() == null) {
                            cell.setCellValue("");
                        } else {
                            cell.setCellValue(r.getDATESROK());
                        }
                        cell = row.createCell(16);
                        cell.setCellStyle(dateStyle);
                        if (r.getLAST_DATE() == null) {
                            cell.setCellValue("");
                        } else {
                            cell.setCellValue(r.getDATADOC());
                        }

                        cell = row.createCell(17);
                        cell.setCellType(CellType.STRING);
                        cell.setCellValue(r.getYESPRINT());
                        cell.setCellStyle(cellStyle);

                        cell = row.createCell(18);
                        cell.setCellType(CellType.NUMERIC);
                        cell.setCellValue(r.getNDS());
                        cell.setCellStyle(cellStyle);

                        cell = row.createCell(19);
                        cell.setCellType(CellType.STRING);
                        cell.setCellValue(r.getSTAVKA_NDS());
                        cell.setCellStyle(cellStyle);
                        
                        cell = row.createCell(20);
                        cell.setCellType(CellType.STRING);
                        cell.setCellValue(r.getFULLNAME());
                        cell.setCellStyle(cellStyle);
                        
                        cell = row.createCell(21);
                        cell.setCellType(CellType.STRING);
                        cell.setCellValue(r.getCHECKDOPINFO());
                        cell.setCellStyle(cellStyle);
                        
                        cell = row.createCell(22);
                        cell.setCellType(CellType.NUMERIC);
                        cell.setCellValue(r.getDISCOUNT());
                        cell.setCellStyle(cellStyle);
                        
                        cell = row.createCell(23);
                        cell.setCellType(CellType.NUMERIC);
                        cell.setCellValue(r.getDISCOUNT_PERCENT());
                        cell.setCellStyle(cellStyle);

                    }
                    row = sheet.createRow(rowCount);
                    rowCount++;

                    row.setRowStyle(s);

                    XSSFCell cell = row.createCell(0);
                    cell.setCellType(CellType.STRING);
                    cell.setCellValue("ВСЬОГО");

                    cell = row.createCell(5);
                    cell.setCellType(CellType.NUMERIC);
                    cell.setCellValue(entry.getValue().stream().mapToDouble(r -> r.getKOLVO()).sum());

                    cell = row.createCell(7);
                    cell.setCellType(CellType.NUMERIC);
                    cell.setCellValue(entry.getValue().stream().mapToDouble(r -> r.getSUM_BEZ_NDS()).sum());

                    cell = row.createCell(8);
                    cell.setCellType(CellType.NUMERIC);
                    cell.setCellValue(entry.getValue().stream().mapToDouble(r -> r.getSUM_NDS()).sum());

                    cell = row.createCell(9);
                    cell.setCellType(CellType.NUMERIC);
                    double overReal=entry.getValue().stream().mapToDouble(r -> r.getSUM_REAL()).sum();
                    cell.setCellValue(overReal);

                    cell = row.createCell(10);
                    cell.setCellType(CellType.NUMERIC);
                    cell.setCellValue(entry.getValue().stream().mapToDouble(r -> r.getVAL_DOHOD()).sum());

                    cell = row.createCell(18);
                    cell.setCellType(CellType.NUMERIC);
                    cell.setCellValue(entry.getValue().stream().mapToDouble(r -> r.getNDS()).sum());

                    cell = row.createCell(22);
                    cell.setCellType(CellType.NUMERIC);
                    double overDiscount = entry.getValue().stream().mapToDouble(r -> r.getDISCOUNT()).sum();
                    cell.setCellValue(overDiscount);


                    row = sheet.createRow(rowCount);
                    rowCount++;
                    row.setRowStyle(s);

                    cell = row.createCell(3);
                    cell.setCellType(CellType.STRING);
                    cell.setCellValue("Округлення за чеком");

                    cell = row.createCell(9);
                    cell.setCellType(CellType.NUMERIC);
                    NakladRepository nr = new NakladRepository();
                    double nakladReal = Math.abs(nr.getSkidka(sklad.getKOD(),
                            "8",
                            from.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                            to.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")), true
                    ));
                    cell.setCellValue(nakladReal);

                    cell = row.createCell(22);
                    cell.setCellType(CellType.NUMERIC);
                    double nakladDiscount = Math.abs(nr.getSkidka(sklad.getKOD(),
                            "8",
                            from.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                            to.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")), false
                    ));
                    cell.setCellValue(nakladDiscount);


                    row = sheet.createRow(rowCount);
                    row.setRowStyle(s);
                    rowCount++;

                    cell = row.createCell(0);
                    cell.setCellType(CellType.STRING);
                    cell.setCellValue("ВСЬОГО");

                    cell = row.createCell(9);
                    cell.setCellType(CellType.NUMERIC);
                    cell.setCellValue(nakladReal+overReal);

                    cell = row.createCell(22);
                    cell.setCellType(CellType.NUMERIC);
                    cell.setCellValue(nakladDiscount+overDiscount);


                    //for (int j = 0; j < 13; j++) {    sheet.autoSizeColumn(j);                 }
                    sheet.setColumnWidth(0, 3100);
                    sheet.setColumnWidth(1, 3000);
                    sheet.setColumnWidth(2, 3000);
                    sheet.setColumnWidth(3, 18000);
                    sheet.setColumnWidth(4, 10000);
                    sheet.setColumnWidth(5, 3000);
                    sheet.setColumnWidth(6, 3000);
                    sheet.setColumnWidth(7, 3000);
                    sheet.setColumnWidth(8, 3000);
                    sheet.setColumnWidth(9, 3000);
                    sheet.setColumnWidth(10, 3000);
                    sheet.setColumnWidth(11, 3000);
                    sheet.setColumnWidth(12, 8000);
                    sheet.setColumnWidth(13, 5000);
                    sheet.setColumnWidth(14, 3000);
                    sheet.setColumnWidth(15, 6000);
                    sheet.setColumnWidth(16, 3000);
                    sheet.setColumnWidth(17, 3000);
                    sheet.setColumnWidth(18, 3000);
                    sheet.setColumnWidth(19, 3000);

                    i++;
                    this.updateProgress(i, map.keySet().size());

                }

                try {
                    FileOutputStream outputStream;
                    outputStream = new FileOutputStream(userHome + fileName);
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
