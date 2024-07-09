
import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import model.Grup;
import model.Klass;
import model.Name;
import model.PivotRow;
import model.Postavka;
import model.Report;
import model.Sklad;
import model.Tip;
import model.Vid;
import model.YesPrint;
import repos.GrupRepository;
import repos.KlassRepository;
import repos.NameRepository;
import repos.PostavkaRepository;
import repos.SkladRepository;
import repos.TipRepository;
import repos.VidRepository;
import service.ExportPivotService;
import service.ExportService;
import service.PivotService;
import service.ReportService;

public class FXMLController implements Initializable {

    ObservableList<Name> filterName = FXCollections.observableArrayList();
    ObservableList<Name> nameList = FXCollections.observableArrayList();

    ObservableList<Klass> filterKlass = FXCollections.observableArrayList();
    ObservableList<Klass> klassList = FXCollections.observableArrayList();

    ObservableList<Grup> filterGrup = FXCollections.observableArrayList();
    ObservableList<Grup> grupList = FXCollections.observableArrayList();

    ObservableList<Vid> filterVid = FXCollections.observableArrayList();
    ObservableList<Vid> vidList = FXCollections.observableArrayList();

    ObservableList<Tip> filterTip = FXCollections.observableArrayList();
    ObservableList<Tip> tipList = FXCollections.observableArrayList();

    ObservableList<Postavka> filterPostavka = FXCollections.observableArrayList();
    ObservableList<Postavka> postavkaList = FXCollections.observableArrayList();

    ObservableList<Sklad> allSklad = FXCollections.observableArrayList();
    ObservableList<Report> selectedReport = FXCollections.observableArrayList();

    ObservableList<Sklad> filterSklad = FXCollections.observableArrayList();
    ObservableList<Sklad> skladList = FXCollections.observableArrayList();

    ObservableList<PivotRow> pivotList = FXCollections.observableArrayList();

    NameRepository nameRepos;
    KlassRepository klassRepos;
    SkladRepository skladRepository;
    ExportPivotService exportPivotService;

    @FXML
    private ListView<Name> listName;

    Sklad selectedSklad;

    Map<Sklad, List<Report>> map;

    @FXML
    private DatePicker reportStart;
    @FXML
    private DatePicker reportStop;

    @FXML
    private DatePicker pivotStart;
    @FXML
    private DatePicker pivotStop;

    @FXML
    private CheckBox reportGroup;

    @FXML
    private Button btGetReport;
    @FXML
    private Button btExportReport;
    @FXML
    private Button btApplySklad;

    @FXML
    private Button btGetPivot;
    @FXML
    private Button btExportPivot;

    @FXML
    private Label reportStatus;
    @FXML
    private Label pivotStatus;

    ReportService reportService;
    ExportService export;
    PivotService pivotService;

    @FXML
    ComboBox<String> startHour;
    @FXML
    ComboBox<String> startMinute;
    @FXML
    ComboBox<String> stopHour;
    @FXML
    ComboBox<String> stopMinute;

    @FXML
    ComboBox<YesPrint> chYesPrint;
    @FXML
    ComboBox<YesPrint> chYesPrintPivot;

    @FXML
    ComboBox<String> startHourPivot;
    @FXML
    ComboBox<String> startMinutePivot;
    @FXML
    ComboBox<String> stopHourPivot;
    @FXML
    ComboBox<String> stopMinutePivot;

    @FXML
    private TableView<Report> tReport;

    @FXML
    private TableView<PivotRow> tPivot;

    @FXML
    private TableColumn<Report, String> nameKod;
    @FXML
    private TableColumn<Report, Date> lastDate;

    @FXML
    private TableColumn<Report, String> name;
    @FXML
    private TableColumn<Report, String> klassName;
    @FXML
    private TableColumn<Report, Double> kolvo;

    @FXML
    private TableColumn<Report, Double> kolvoPrihod;

    @FXML
    private TableColumn<Report, Double> cenaRasx;
    @FXML
    private TableColumn<Report, Double> sumBezNds;
    @FXML
    private TableColumn<Report, Double> sumNds;
    @FXML
    private TableColumn<Report, Double> sumReal;
    @FXML
    private TableColumn<Report, Double> valDohod;

    @FXML
    private TableColumn<Report, Double> cenaRozn;

    @FXML
    private TableColumn<Report, String> postavkaName;
    @FXML
    private TableColumn<Report, String> shtrihKod;

    @FXML
    private TableColumn<Report, String> vedKod;
    @FXML
    private TableColumn<Report, Double> nds;
    @FXML
    private TableColumn<Report, String> stavkaNds;
    @FXML
    private TableColumn<Report, Date> dataDoc;

    @FXML
    private TableColumn<Report, Double> skidkaPro;
    @FXML
    private TableColumn<Report, Double> skidka;

    @FXML
    private TableColumn<Report, Date> dateSrok;
    @FXML
    private TableColumn<Report, String> yesPrint;

    @FXML
    private TableColumn<Report, String> kassir;
    @FXML
    private TableColumn<Report, String> dopinfo;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private ProgressBar progressBarPivot;

    @FXML
    private ComboBox<Sklad> chSklad;

    @FXML
    ListView<Name> listSelName;

    @FXML
    private TextField fName;

    @FXML
    private Button btClearName;
    @FXML
    private Button btDelName;
    @FXML
    private Button btAddName;

    @FXML
    ListView<Vid> listVid;
    @FXML
    ListView<Vid> listSelVid;

    @FXML
    ListView<Tip> listTip;
    @FXML
    ListView<Tip> listSelTip;

    @FXML
    ListView<Grup> listGrup;
    @FXML
    ListView<Grup> listSelGrup;

    @FXML
    ListView<Postavka> listPostavka;
    @FXML
    ListView<Postavka> listSelPostavka;

    @FXML
    private TextField fKlass;
    @FXML
    ListView<Klass> listKlass;
    @FXML
    ListView<Klass> listSelKlass;

    @FXML
    ListView<Sklad> listSklad;
    @FXML
    ListView<Sklad> listSelSklad;

    @FXML
    private void onReportAction(ActionEvent event) {
        if (reportStart.getValue().isAfter(reportStop.getValue()))
            return;
        selectedReport.clear();
        btGetReport.setDisable(true);
        btExportReport.setDisable(true);
        btApplySklad.setDisable(true);
        progressBar.progressProperty().unbind();
        reportStatus.textProperty().unbind();
        progressBar.progressProperty().bind(reportService.progressProperty());
        reportStatus.textProperty().bind(reportService.messageProperty());

        reportStatus.textProperty().bind(reportService.messageProperty());

        StringBuilder filter = new StringBuilder();
        if (filterName.size() > 0) {
            filter.append(" AND n.KOD  IN ( ");
            filter.append(String.join(",",
                    filterName.stream().map(a -> Integer.toString(a.getKOD())).toArray(String[]::new)));
            filter.append(") ");
        }
        if (filterKlass.size() > 0) {
            filter.append(" AND k.KOD  IN ( ");
            filter.append(String.join(",",
                    filterKlass.stream().map(a -> Integer.toString(a.getKOD())).toArray(String[]::new)));
            filter.append(") ");
        }

        if (filterGrup.size() > 0) {
            filter.append(" AND  n.KODGROUP IN ( ");
            filter.append(String.join(",",
                    filterGrup.stream().map(a -> Integer.toString(a.getKOD())).toArray(String[]::new)));
            filter.append(") ");
        }
        if (filterVid.size() > 0) {
            filter.append(" AND  n.KODVIDA IN ( ");
            filter.append(String.join(",", filterVid.stream().map(a -> a.getKOD()).toArray(String[]::new)));
            filter.append(") ");
        }
        if (filterTip.size() > 0) {
            filter.append(" AND  n.TIPTOV IN ( ");
            filter.append(
                    String.join(",", filterTip.stream().map(a -> Integer.toString(a.getKOD())).toArray(String[]::new)));
            filter.append(") ");
        }
        if (filterPostavka.size() > 0) {
            filter.append(" AND  n.KODPOST IN ( ");
            filter.append(String.join(",",
                    filterPostavka.stream().map(a -> Integer.toString(a.getKOD())).toArray(String[]::new)));
            filter.append(") ");
        }

        int beginHour = Integer.parseInt(startHour.getSelectionModel().getSelectedItem());
        int beginMinute = Integer.parseInt(startMinute.getSelectionModel().getSelectedItem());
        int endHour = Integer.parseInt(this.stopHour.getSelectionModel().getSelectedItem());
        int endMinute = Integer.parseInt(stopMinute.getSelectionModel().getSelectedItem());
        int endSec = endMinute == 0 ? 0 : 59;

        LocalTime start = LocalTime.of(beginHour, beginMinute, 0);
        LocalTime stop = LocalTime.of(endHour, endMinute, endSec);

        YesPrint yesPrint = chYesPrint.getSelectionModel().getSelectedItem();
        reportService.startService(reportStart.getValue(), reportStop.getValue(), reportGroup.isSelected(),
                filter.toString(), yesPrint, start, stop);
    }

    @FXML
    private void onPivotAction(ActionEvent event) {
        if (filterSklad.size() == 0) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Помилка");
            alert.setHeaderText("Зверніть увагу:");
            alert.setContentText("Не вибрано склади!");

            alert.showAndWait();
            return;
        }
        pivotList.clear();
        btGetPivot.setDisable(true);
        btExportPivot.setDisable(true);
        progressBarPivot.progressProperty().unbind();
        pivotStatus.textProperty().unbind();
        progressBarPivot.progressProperty().bind(pivotService.progressProperty());
        pivotStatus.textProperty().bind(pivotService.messageProperty());

        StringBuilder filter = new StringBuilder();
        if (filterName.size() > 0) {
            filter.append(" AND n.KOD  IN ( ");
            filter.append(String.join(",",
                    filterName.stream().map(a -> Integer.toString(a.getKOD())).toArray(String[]::new)));
            filter.append(") ");
        }
        if (filterKlass.size() > 0) {
            filter.append(" AND k.KOD  IN ( ");
            filter.append(String.join(",",
                    filterKlass.stream().map(a -> Integer.toString(a.getKOD())).toArray(String[]::new)));
            filter.append(") ");
        }

        if (filterGrup.size() > 0) {
            filter.append(" AND  n.KODGROUP IN ( ");
            filter.append(String.join(",",
                    filterGrup.stream().map(a -> Integer.toString(a.getKOD())).toArray(String[]::new)));
            filter.append(") ");
        }
        if (filterVid.size() > 0) {
            filter.append(" AND  n.KODVIDA IN ( ");
            filter.append(String.join(",", filterVid.stream().map(a -> a.getKOD()).toArray(String[]::new)));
            filter.append(") ");
        }
        if (filterTip.size() > 0) {
            filter.append(" AND  n.TIPTOV IN ( ");
            filter.append(
                    String.join(",", filterTip.stream().map(a -> Integer.toString(a.getKOD())).toArray(String[]::new)));
            filter.append(") ");
        }
        if (filterPostavka.size() > 0) {
            filter.append(" AND  n.KODPOST IN ( ");
            filter.append(String.join(",",
                    filterPostavka.stream().map(a -> Integer.toString(a.getKOD())).toArray(String[]::new)));
            filter.append(") ");
        }

        int beginHour = Integer.parseInt(startHourPivot.getSelectionModel().getSelectedItem());
        int beginMinute = Integer.parseInt(startMinutePivot.getSelectionModel().getSelectedItem());
        int endHour = Integer.parseInt(stopHourPivot.getSelectionModel().getSelectedItem());
        int endMinute = Integer.parseInt(stopMinutePivot.getSelectionModel().getSelectedItem());
        int endSec = endMinute == 0 ? 0 : 59;
        LocalTime start = LocalTime.of(beginHour, beginMinute, 0);
        LocalTime stop = LocalTime.of(endHour, endMinute, endSec);

        YesPrint yesPrint = chYesPrintPivot.getSelectionModel().getSelectedItem();

        pivotService.startService(pivotStart.getValue(), pivotStop.getValue(),
                filter.toString(), filterSklad, yesPrint, start, stop);

    }

    @FXML
    private void onExportPivot(ActionEvent event) {

        if (pivotList.size() == 0) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Помилка");
            alert.setHeaderText("Зверніть увагу:");
            alert.setContentText("Нема що експортувати!");

            alert.showAndWait();
            return;
        }

        Sklad[] listSklad = filterSklad.stream().sorted(Comparator.comparingInt(Sklad::getKOD)).toArray(Sklad[]::new);
        btGetPivot.setDisable(true);
        btExportPivot.setDisable(true);
        progressBarPivot.progressProperty().unbind();
        pivotStatus.textProperty().unbind();
        progressBarPivot.progressProperty().bind(exportPivotService.progressProperty());
        pivotStatus.textProperty().bind(exportPivotService.messageProperty());
        exportPivotService.startService(pivotStart.getValue(), pivotStop.getValue(), pivotList, listSklad);

    }

    private void setColumnText(int columnIndex, String text, double sum) {
        BigDecimal total = new BigDecimal(sum);
        tReport.getColumns().get(columnIndex).setText(text + total.setScale(2, RoundingMode.HALF_EVEN).doubleValue());
    }

    @FXML
    private void applySklad(ActionEvent event) {
        selectedReport.clear();
        selectedSklad = chSklad.getValue();
        selectedReport.clear();
        if (map.get(selectedSklad) != null) {
            selectedReport.addAll(map.get(selectedSklad));
        }

        setColumnText(5, "Реаліизація  Кількість\n-----\n", selectedReport.stream().mapToDouble(o -> o.getKOLVO()).sum());
        setColumnText(7, "Сума по приходу без ПДВ (Закупка)\n-----\n",
                selectedReport.stream().mapToDouble(o -> o.getSUM_BEZ_NDS()).sum());
        setColumnText(8, "Сума по приходу с ПДВ (Закупка)\n-----\n",
                selectedReport.stream().mapToDouble(o -> o.getSUM_NDS()).sum());
        setColumnText(9, "Сума реалізації всього розниця\n-----\n",
                selectedReport.stream().mapToDouble(o -> o.getSUM_REAL()).sum());
        setColumnText(10, "Валовий дохід  (націнка розниця-закупка с ПДВ)\n-----\n",
                selectedReport.stream().mapToDouble(o -> o.getVAL_DOHOD()).sum());
        setColumnText(18, "ПДВ\n-----\n", selectedReport.stream().mapToDouble(o -> o.getNDS()).sum());
        setColumnText(22, "Знижка\n-----\n", selectedReport.stream().mapToDouble(o -> o.getDISCOUNT()).sum());

        tReport.setItems(selectedReport);
    }

    @FXML
    private void onExport(ActionEvent event) {
        int size = map.values().stream().mapToInt(List::size).sum();
        if (size == 0) {
            showAlert("Помилка", "Зверніть увагу:", "Нема що експортувати!");
            return;
        }
        disableButtons(true);
        progressBar.progressProperty().unbind();
        reportStatus.textProperty().unbind();
        progressBar.progressProperty().bind(export.progressProperty());
        reportStatus.textProperty().bind(export.messageProperty());
        export.startService(reportStart.getValue(), reportStop.getValue(), map, reportGroup.isSelected());
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void disableButtons(boolean disable) {
        btGetReport.setDisable(disable);
        btExportReport.setDisable(disable);
        btApplySklad.setDisable(disable);
    }

    @FXML
    private void addName(ActionEvent event) {
        filterName.addAll(listName.getSelectionModel().getSelectedItems());
        filterName = (ObservableList<Name>) filterName.stream().distinct().collect(Collectors.toList());
        filterName.sort(Comparator.comparing(Name::getNAME));
    }

    @FXML
    private void delName(ActionEvent event) {
        ObservableList<Name> selected = listSelName.getSelectionModel().getSelectedItems();
        filterName.removeAll(selected);
    }

    @FXML
    private void clearName(ActionEvent event) {
        filterName.clear();
    }

    @FXML
    private void addVid(ActionEvent event) {
        List<Vid> selected = listVid.getSelectionModel().getSelectedItems();
        filterVid.addAll(selected);
        vidList.removeAll(selected);
        filterVid.sort(Comparator.comparing(Vid::getNAME));
    }

    @FXML
    private void delVid(ActionEvent event) {
        List<Vid> selected = listSelVid.getSelectionModel().getSelectedItems();
        vidList.addAll(selected);
        filterVid.removeAll(selected);
        vidList.sort(Comparator.comparing(Vid::getNAME));
    }

    @FXML
    private void clearVid(ActionEvent event) {
        List<Vid> selected = new ArrayList<>(filterVid);
        vidList.addAll(selected);
        filterVid.removeAll(selected);
        vidList.sort(Comparator.comparing(Vid::getNAME));
    }

    @FXML
    private void addTip(ActionEvent event) {
        final List<Tip> selected = listTip.getSelectionModel().getSelectedItems();
        filterTip.addAll(selected);
        vidList.removeAll((Collection<?>) selected);

        filterTip.sort(new Comparator<Tip>() {
            @Override
            public int compare(Tip u1, Tip u2) {
                return u1.getNAME().compareTo(u2.getNAME());
            }
        });
    }

    @FXML
    private void delTip(ActionEvent event) {
        List<Tip> selected = listSelTip.getSelectionModel().getSelectedItems();
        tipList.addAll(selected);
        filterTip.removeAll(selected);
        tipList.sort(new Comparator<Tip>() {
            @Override
            public int compare(Tip u1, Tip u2) {
                return u1.getNAME().compareTo(u2.getNAME());
            }
        });

    }

    @FXML
    private void clearTip(ActionEvent event) {
        final List<Tip> selected = filterTip.stream().collect(toList());
        tipList.addAll(selected);
        filterTip.removeAll(selected);
        tipList.sort(new Comparator<Tip>() {
            @Override
            public int compare(Tip u1, Tip u2) {
                return u1.getNAME().compareTo(u2.getNAME());
            }
        });
    }

    @FXML
    private void addGrup(ActionEvent event) {

        List<Grup> selected = listGrup.getSelectionModel().getSelectedItems();
        filterGrup.addAll(selected);
        grupList.removeAll(selected);
        filterGrup.sort(new Comparator<Grup>() {
            @Override
            public int compare(Grup u1, Grup u2) {
                return u1.getNAME().compareTo(u2.getNAME());
            }
        });
    }

    @FXML
    private void delGrup(ActionEvent event) {
        List<Grup> selected = listSelGrup.getSelectionModel().getSelectedItems();
        grupList.addAll(selected);
        filterGrup.removeAll(selected);
        grupList.sort(new Comparator<Grup>() {
            @Override
            public int compare(Grup u1, Grup u2) {
                return u1.getNAME().compareTo(u2.getNAME());
            }
        });

    }

    @FXML
    private void clearGrup(ActionEvent event) {
        List<Grup> selected = filterGrup.stream().collect(toList());
        grupList.addAll(selected);
        filterGrup.removeAll(selected);
        grupList.sort(new Comparator<Grup>() {
            @Override
            public int compare(Grup u1, Grup u2) {
                return u1.getNAME().compareTo(u2.getNAME());
            }
        });
    }

    @FXML
    private void addPost(ActionEvent event) {

        List<Postavka> selected = listPostavka.getSelectionModel().getSelectedItems();
        filterPostavka.addAll(selected);
        postavkaList.removeAll(selected);
        filterPostavka.sort(new Comparator<Postavka>() {
            @Override
            public int compare(Postavka u1, Postavka u2) {
                return u1.getNAME().compareTo(u2.getNAME());
            }
        });
    }

    @FXML
    private void delPost(ActionEvent event) {
        List<Postavka> selected = listSelPostavka.getSelectionModel().getSelectedItems();
        postavkaList.addAll(selected);
        filterPostavka.removeAll(selected);
        postavkaList.sort(new Comparator<Postavka>() {
            @Override
            public int compare(Postavka u1, Postavka u2) {
                return u1.getNAME().compareTo(u2.getNAME());
            }
        });

    }

    @FXML
    private void clearPost(ActionEvent event) {
        List<Postavka> selected = filterPostavka.stream().collect(toList());
        postavkaList.addAll(selected);
        filterPostavka.removeAll(selected);
        postavkaList.sort(new Comparator<Postavka>() {
            @Override
            public int compare(Postavka u1, Postavka u2) {
                return u1.getNAME().compareTo(u2.getNAME());
            }
        });
    }

    @FXML
    private void addKlass(ActionEvent event) {

        Set<Klass> set = new HashSet<Klass>();
        set.addAll(filterKlass.stream().collect(toList()));
        set.addAll(listKlass.getSelectionModel().getSelectedItems());
        filterKlass.clear();
        filterKlass.addAll(set);

        filterKlass.sort(new Comparator<Klass>() {
            @Override
            public int compare(Klass u1, Klass u2) {
                return u1.getNAME().compareTo(u2.getNAME());
            }
        });
    }

    @FXML
    private void delKlass(ActionEvent event) {
        final ObservableList<Klass> selected = listSelKlass.getSelectionModel().getSelectedItems();
        filterName.removeAll((Collection<?>) selected);
    }

    @FXML
    private void clearKlass(ActionEvent event) {
        filterKlass.clear();
    }

    @FXML
    private void addSklad(ActionEvent event) {

        List<Sklad> selected = listSklad.getSelectionModel().getSelectedItems();
        if (selected == null || selected.size() == 0) {
            return;
        }
        filterSklad.addAll(selected);
        skladList.removeAll(selected);
        filterSklad.sort(new Comparator<Sklad>() {
            @Override
            public int compare(Sklad u1, Sklad u2) {
                return u1.getNAME().compareTo(u2.getNAME());
            }
        });
    }

    @FXML
    private void delSklad(ActionEvent event) {
        List<Sklad> selected = listSelSklad.getSelectionModel().getSelectedItems();
        if (selected == null || selected.size() == 0) {
            return;
        }
        skladList.addAll(selected);
        filterSklad.removeAll(selected);
        skladList.sort(new Comparator<Sklad>() {
            @Override
            public int compare(Sklad u1, Sklad u2) {
                return u1.getNAME().compareTo(u2.getNAME());
            }
        });

    }

    @FXML
    private void clearSklad(ActionEvent event) {
        List<Sklad> selected = filterSklad.stream().collect(toList());
        if (selected == null || selected.size() == 0) {
            return;
        }
        skladList.addAll(selected);
        filterSklad.removeAll(selected);
        skladList.sort(new Comparator<Sklad>() {
            @Override
            public int compare(Sklad u1, Sklad u2) {
                return u1.getNAME().compareTo(u2.getNAME());
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        ObservableList<String> hours = FXCollections.observableArrayList();
        ObservableList<String> minutes = FXCollections.observableArrayList();
        for (int i = 0; i < 24; i++) {
            hours.add(i, String.format("%02d", i));
        }
        for (int i = 0; i < 60; i++) {
            minutes.add(i, String.format("%02d", i));
        }
        startHour.setItems(hours);
        startHour.getSelectionModel().select("00");
        startMinute.setItems(minutes);
        startMinute.getSelectionModel().select("00");
        stopHour.setItems(hours);
        stopHour.getSelectionModel().select("23");
        stopMinute.setItems(minutes);
        stopMinute.getSelectionModel().select("59");

        startHourPivot.setItems(hours);
        startHourPivot.getSelectionModel().select("00");
        startMinutePivot.setItems(minutes);
        startMinutePivot.getSelectionModel().select("00");
        stopHourPivot.setItems(hours);
        stopHourPivot.getSelectionModel().select("23");
        stopMinutePivot.setItems(minutes);
        stopMinutePivot.getSelectionModel().select("59");

        ObservableList<YesPrint> listYes = FXCollections.observableArrayList();
        listYes.add(new YesPrint(-1, "Все"));
        listYes.add(new YesPrint(1, "Фискальный"));
        listYes.add(new YesPrint(0, "Нефискальный"));

        chYesPrint.setItems(listYes);
        chYesPrint.getSelectionModel().selectFirst();

        chYesPrintPivot.setItems(listYes);
        chYesPrintPivot.getSelectionModel().selectFirst();

        reportStop.setValue(LocalDate.now());
        reportStart.setValue(LocalDate.now().plusMonths(-1));

        pivotStop.setValue(LocalDate.now());
        pivotStart.setValue(LocalDate.now().plusMonths(-1));

        reportService = new ReportService();
        pivotService = new PivotService();
        export = new ExportService();
        exportPivotService = new ExportPivotService();

        skladRepository = new SkladRepository();
        allSklad.addAll(skladRepository.getAll());

        chSklad.setItems(allSklad);
        selectedSklad = allSklad.get(0);
        chSklad.getSelectionModel().select(selectedSklad);

        nameKod.setCellValueFactory(new PropertyValueFactory<Report, String>("NAMEKOD"));
        name.setCellValueFactory(new PropertyValueFactory<Report, String>("NAME"));
        klassName.setCellValueFactory(new PropertyValueFactory<Report, String>("KLASS_NAME"));
        postavkaName.setCellValueFactory(new PropertyValueFactory<Report, String>("POSTAVKA_NAME"));
        shtrihKod.setCellValueFactory(new PropertyValueFactory<Report, String>("SHTRIHKOD"));
        kolvo.setCellValueFactory(new PropertyValueFactory<Report, Double>("KOLVO"));
        cenaRasx.setCellValueFactory(new PropertyValueFactory<Report, Double>("CENARASX"));
        sumBezNds.setCellValueFactory(new PropertyValueFactory<Report, Double>("SUM_BEZ_NDS"));
        sumNds.setCellValueFactory(new PropertyValueFactory<Report, Double>("SUM_NDS"));
        sumReal.setCellValueFactory(new PropertyValueFactory<Report, Double>("SUM_REAL"));
        valDohod.setCellValueFactory(new PropertyValueFactory<Report, Double>("VAL_DOHOD"));
        cenaRozn.setCellValueFactory(new PropertyValueFactory<Report, Double>("CENA_ROZN"));
        lastDate.setCellValueFactory(new PropertyValueFactory<Report, Date>("LAST_DATE"));
        kolvoPrihod.setCellValueFactory(new PropertyValueFactory<Report, Double>("KOLVO_PRIHOD"));
        skidka.setCellValueFactory(new PropertyValueFactory<Report, Double>("DISCOUNT"));
        skidkaPro.setCellValueFactory(new PropertyValueFactory<Report, Double>("DISCOUNT_PERCENT"));

        vedKod.setCellValueFactory(new PropertyValueFactory<Report, String>("VEDKODC"));
        yesPrint.setCellValueFactory(new PropertyValueFactory<Report, String>("YESPRINT"));
        nds.setCellValueFactory(new PropertyValueFactory<Report, Double>("NDS"));

        stavkaNds.setCellValueFactory(new PropertyValueFactory<Report, String>("STAVKA_NDS"));
        kassir.setCellValueFactory(new PropertyValueFactory<Report, String>("FULLNAME"));
        dopinfo.setCellValueFactory(new PropertyValueFactory<Report, String>("CHECKDOPINFO"));

        dataDoc.setCellValueFactory(new PropertyValueFactory<Report, Date>("DATADOC"));
        ;
        dateSrok.setCellValueFactory(new PropertyValueFactory<Report, Date>("DATESROK"));
        ;

        dateSrok.setCellFactory(column -> {
            TableCell<Report, Date> cell = new TableCell<Report, Date>() {
                private SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

                @Override
                protected void updateItem(Date item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        if (item != null)
                            setText(format.format(item));
                        else
                            setText(null);
                    }
                }
            };

            return cell;
        });
        dataDoc.setCellFactory(column -> {
            TableCell<Report, Date> cell = new TableCell<Report, Date>() {
                private SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");

                @Override
                protected void updateItem(Date item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        if (item != null)
                            setText(format.format(item));
                        else
                            setText(null);
                    }
                }
            };

            return cell;
        });

        lastDate.setCellFactory(column -> {
            TableCell<Report, Date> cell = new TableCell<Report, Date>() {
                private SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");

                @Override
                protected void updateItem(Date item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        if (item != null)
                            setText(format.format(item));
                        else
                            setText(null);
                    }
                }
            };

            return cell;
        });

        tReport.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        tReport.setPlaceholder(new Label("Немає даних"));
        tReport.setItems(selectedReport);

        NumberFormat currencyFormat = NumberFormat.getNumberInstance();
        currencyFormat.setMaximumFractionDigits(2);
        currencyFormat.setMinimumFractionDigits(2);
        cenaRasx.setCellFactory(tc -> new TableCell<Report, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(price));
                }
            }
        });
        cenaRozn.setCellFactory(tc -> new TableCell<Report, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(price));
                }
            }
        });
        sumBezNds.setCellFactory(tc -> new TableCell<Report, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(price));
                }
            }
        });
        sumNds.setCellFactory(tc -> new TableCell<Report, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(price));
                }
            }
        });
        sumReal.setCellFactory(tc -> new TableCell<Report, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(price));
                }
            }
        });
        valDohod.setCellFactory(tc -> new TableCell<Report, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(price));
                }
            }
        });

        reportService.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @SuppressWarnings("unchecked")
            @Override
            public void handle(WorkerStateEvent event) {
                progressBar.progressProperty().unbind();
                reportStatus.textProperty().unbind();
                map = (Map<Sklad, List<Report>>) event.getSource().getValue(); // Add generic type declaration to the
                                                                               // cast statement
                selectedReport.clear();
                selectedReport.addAll(map.get(selectedSklad));
                tReport.setItems(selectedReport);
                btGetReport.setDisable(false);
                btExportReport.setDisable(false);
                btApplySklad.setDisable(false);

            }
        });

        export.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                progressBar.progressProperty().unbind();
                reportStatus.textProperty().unbind();
                btGetReport.setDisable(false);
                btExportReport.setDisable(false);
                btApplySklad.setDisable(false);

            }
        });

        export.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                progressBar.progressProperty().unbind();
                reportStatus.textProperty().unbind();
                btGetReport.setDisable(false);
                btExportReport.setDisable(false);
                btApplySklad.setDisable(false);
                reportStatus.setText("EXPORT FAIL");
                Throwable throwable = export.getException();
                throwable.printStackTrace();

            }
        });
        export.setOnCancelled(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                progressBar.progressProperty().unbind();
                reportStatus.textProperty().unbind();
                btGetReport.setDisable(false);
                btExportReport.setDisable(false);
                btApplySklad.setDisable(false);
                reportStatus.setText("EXPORT CANCEL");
            }
        });

        pivotService.setOnCancelled(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                progressBarPivot.progressProperty().unbind();
                pivotStatus.textProperty().unbind();
                btGetPivot.setDisable(false);
                btExportPivot.setDisable(false);
                reportStatus.setText("EXPORT CANCEL");
            }
        });

        pivotService.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                progressBarPivot.progressProperty().unbind();
                pivotStatus.textProperty().unbind();
                btGetPivot.setDisable(false);
                btExportPivot.setDisable(false);
                pivotList.clear();
                @SuppressWarnings("unchecked")
                final List<PivotRow> list = (List<PivotRow>) event.getSource().getValue();
                pivotList.addAll(list);
                tPivot.getItems().clear();
                tPivot.getColumns().clear();
                TableColumn<PivotRow, String> namekod_ = new TableColumn<>("Артикул\n-------------\nВСЕГО");
                namekod_.setCellValueFactory(new PropertyValueFactory<PivotRow, String>("NAMEKOD"));
                tPivot.getColumns().add(namekod_);
                TableColumn<PivotRow, String> name_ = new TableColumn<>("Найменування");
                name_.setCellValueFactory(new PropertyValueFactory<PivotRow, String>("NAME"));
                tPivot.getColumns().add(name_);
                TableColumn<PivotRow, String> klass_ = new TableColumn<>("Виробник");
                klass_.setCellValueFactory(new PropertyValueFactory<PivotRow, String>("KLASS_NAME"));
                tPivot.getColumns().add(klass_);

                NumberFormat currencyFormat = NumberFormat.getNumberInstance();
                currencyFormat.setMaximumFractionDigits(2);
                currencyFormat.setMinimumFractionDigits(2);

                Sklad[] listSklad = filterSklad.stream().sorted(Comparator.comparingInt(Sklad::getKOD))
                        .toArray(Sklad[]::new);
                for (int i = 0; i < listSklad.length; i++) {
                    final int j = i;
                    final BigDecimal total_kolvo = new BigDecimal(
                            list.stream().mapToDouble(o -> o.getSkladPivots()[j].getKOLVO()).sum());
                    TableColumn<PivotRow, Double> kol = new TableColumn<PivotRow, Double>(
                            "Кількість (" + listSklad[i].getNAME() + ")\n-------------\n"
                                    + total_kolvo.setScale(2, RoundingMode.HALF_EVEN).doubleValue());
                    kol.setCellValueFactory(
                            new Callback<CellDataFeatures<PivotRow, Double>, ObservableValue<Double>>() {
                                @Override
                                public ObservableValue<Double> call(CellDataFeatures<PivotRow, Double> cd) {
                                    return new SimpleObjectProperty<>(cd.getValue().getSkladPivots()[j].getKOLVO());
                                }
                            });
                    kol.setCellFactory(tc -> new TableCell<PivotRow, Double>() {
                        @Override
                        protected void updateItem(Double price, boolean empty) {
                            super.updateItem(price, empty);
                            if (empty) {
                                setText(null);
                            } else {
                                setText(currencyFormat.format(price));
                            }
                        }
                    });
                    tPivot.getColumns().add(kol);
                    final BigDecimal total_sum_bez_nds = new BigDecimal(
                            list.stream().mapToDouble(o -> o.getSkladPivots()[j].getSUM_BEZ_NDS()).sum());
                    TableColumn<PivotRow, Double> sumBezNds = new TableColumn<PivotRow, Double>(
                            "Продаж у закупівельних цінах, без ПДВ\n-------------\n"
                                    + total_sum_bez_nds.setScale(2, RoundingMode.HALF_EVEN).doubleValue());
                    sumBezNds.setCellValueFactory(
                            new Callback<CellDataFeatures<PivotRow, Double>, ObservableValue<Double>>() {
                                @Override
                                public ObservableValue<Double> call(CellDataFeatures<PivotRow, Double> cd) {
                                    return new SimpleObjectProperty<>(
                                            cd.getValue().getSkladPivots()[j].getSUM_BEZ_NDS());
                                }
                            });
                    sumBezNds.setCellFactory(tc -> new TableCell<PivotRow, Double>() {
                        @Override
                        protected void updateItem(Double price, boolean empty) {
                            super.updateItem(price, empty);
                            if (empty) {
                                setText(null);
                            } else {
                                setText(currencyFormat.format(price));
                            }
                        }
                    });
                    tPivot.getColumns().add(sumBezNds);

                    final BigDecimal total_sum_nds = new BigDecimal(
                            list.stream().mapToDouble(o -> o.getSkladPivots()[j].getSUM_NDS()).sum());
                    TableColumn<PivotRow, Double> sumNds = new TableColumn<PivotRow, Double>(
                            "Продаж у закупівельних цінах, з ПДВ\n-------------\n"
                                    + total_sum_nds.setScale(2, RoundingMode.HALF_EVEN).doubleValue());
                    sumNds.setCellValueFactory(
                            new Callback<CellDataFeatures<PivotRow, Double>, ObservableValue<Double>>() {
                                @Override
                                public ObservableValue<Double> call(CellDataFeatures<PivotRow, Double> cd) {
                                    return new SimpleObjectProperty<>(cd.getValue().getSkladPivots()[j].getSUM_NDS());
                                }
                            });
                    sumNds.setCellFactory(tc -> new TableCell<PivotRow, Double>() {
                        @Override
                        protected void updateItem(Double price, boolean empty) {
                            super.updateItem(price, empty);
                            if (empty) {
                                setText(null);
                            } else {
                                setText(currencyFormat.format(price));
                            }
                        }
                    });
                    tPivot.getColumns().add(sumNds);

                    final BigDecimal total_sum_real = new BigDecimal(
                            list.stream().mapToDouble(o -> o.getSkladPivots()[j].getSUM_REAL()).sum());
                    final TableColumn<PivotRow, Double> sumReal = new TableColumn<PivotRow, Double>(
                            "Сума продажу в цінах реалізації\n-------------\n"
                                    + total_sum_real.setScale(2, RoundingMode.HALF_EVEN).doubleValue());
                    sumReal.setCellValueFactory(
                            new Callback<CellDataFeatures<PivotRow, Double>, ObservableValue<Double>>() {
                                @Override
                                public ObservableValue<Double> call(CellDataFeatures<PivotRow, Double> cd) {
                                    return new SimpleObjectProperty<>(cd.getValue().getSkladPivots()[j].getSUM_REAL());
                                }
                            });
                    sumReal.setCellFactory(tc -> new TableCell<PivotRow, Double>() {
                        @Override
                        protected void updateItem(Double price, boolean empty) {
                            super.updateItem(price, empty);
                            if (empty) {
                                setText(null);
                            } else {
                                setText(currencyFormat.format(price));
                            }
                        }
                    });
                    tPivot.getColumns().add(sumReal);

                    final BigDecimal total_first_rasx = new BigDecimal(
                            list.stream().mapToDouble(o -> o.getSkladPivots()[j].getFIRST_RASX()).sum());
                    TableColumn<PivotRow, Double> sumFirst = new TableColumn<PivotRow, Double>(
                            "Сума продажу в роздрібних цінах\n-------------\n"
                                    + total_first_rasx.setScale(2, RoundingMode.HALF_EVEN).doubleValue());
                    sumFirst.setCellValueFactory(
                            new Callback<CellDataFeatures<PivotRow, Double>, ObservableValue<Double>>() {
                                @Override
                                public ObservableValue<Double> call(CellDataFeatures<PivotRow, Double> cd) {
                                    return new SimpleObjectProperty<>(
                                            cd.getValue().getSkladPivots()[j].getFIRST_RASX());
                                }
                            });
                    sumFirst.setCellFactory(tc -> new TableCell<PivotRow, Double>() {
                        @Override
                        protected void updateItem(Double price, boolean empty) {
                            super.updateItem(price, empty);
                            if (empty) {
                                setText(null);
                            } else {
                                setText(currencyFormat.format(price));
                            }
                        }
                    });
                    tPivot.getColumns().add(sumFirst);

                    final BigDecimal total_val_dohod = new BigDecimal(
                            list.stream().mapToDouble(o -> o.getSkladPivots()[j].getVAL_DOHOD()).sum());
                    TableColumn<PivotRow, Double> valDoh = new TableColumn<PivotRow, Double>(
                            "Валовий прибуток\n-------------\n"
                                    + total_val_dohod.setScale(2, RoundingMode.HALF_EVEN).doubleValue());
                    valDoh.setCellValueFactory(
                            new Callback<CellDataFeatures<PivotRow, Double>, ObservableValue<Double>>() {
                                @Override
                                public ObservableValue<Double> call(CellDataFeatures<PivotRow, Double> cd) {
                                    return new SimpleObjectProperty<>(cd.getValue().getSkladPivots()[j].getVAL_DOHOD());
                                }
                            });
                    valDoh.setCellFactory(tc -> new TableCell<PivotRow, Double>() {
                        @Override
                        protected void updateItem(Double price, boolean empty) {
                            super.updateItem(price, empty);
                            if (empty) {
                                setText(null);
                            } else {
                                setText(currencyFormat.format(price));
                            }
                        }
                    });
                    tPivot.getColumns().add(valDoh);
                }

                final BigDecimal total_itog_sum_bez_nds = new BigDecimal(list.stream()
                        .mapToDouble(o -> Arrays.stream(o.getSkladPivots()).mapToDouble(a -> a.getSUM_BEZ_NDS()).sum())
                        .sum());
                TableColumn<PivotRow, Double> SUM_BEZ_NDS = new TableColumn<>(
                        "Ітого продаж у закупівельних цінах, без ПДВ\n-------------\n"
                                + total_itog_sum_bez_nds.setScale(2, RoundingMode.HALF_EVEN).doubleValue());
                SUM_BEZ_NDS.setCellValueFactory(new PropertyValueFactory<PivotRow, Double>("SUM_BEZ_NDS"));
                SUM_BEZ_NDS.setCellFactory(tc -> new TableCell<PivotRow, Double>() {
                    @Override
                    protected void updateItem(Double price, boolean empty) {
                        super.updateItem(price, empty);
                        if (empty) {
                            setText(null);
                        } else {
                            setText(currencyFormat.format(price));
                        }
                    }
                });
                tPivot.getColumns().add(SUM_BEZ_NDS);

                final BigDecimal total_itog_sum_nds = new BigDecimal(list.stream()
                        .mapToDouble(o -> Arrays.stream(o.getSkladPivots()).mapToDouble(a -> a.getSUM_NDS()).sum())
                        .sum());
                TableColumn<PivotRow, Double> SUM_NDS = new TableColumn<>(
                        "Ітого продаж у закупівельних цінах з ПДВ\n-------------\n"
                                + total_itog_sum_nds.setScale(2, RoundingMode.HALF_EVEN).doubleValue());
                SUM_NDS.setCellValueFactory(new PropertyValueFactory<PivotRow, Double>("SUM_NDS"));
                SUM_NDS.setCellFactory(tc -> new TableCell<PivotRow, Double>() {
                    @Override
                    protected void updateItem(Double price, boolean empty) {
                        super.updateItem(price, empty);
                        if (empty) {
                            setText(null);
                        } else {
                            setText(currencyFormat.format(price));
                        }
                    }
                });
                tPivot.getColumns().add(SUM_NDS);

                final BigDecimal total_itog_sum_real = new BigDecimal(list.stream()
                        .mapToDouble(o -> Arrays.stream(o.getSkladPivots()).mapToDouble(a -> a.getSUM_REAL()).sum())
                        .sum());
                TableColumn<PivotRow, Double> SUM_REAL = new TableColumn<>(
                        "Ітого сумма продажу в цінах реалізації\n-------------\n"
                                + total_itog_sum_real.setScale(2, RoundingMode.HALF_EVEN).doubleValue());
                SUM_REAL.setCellValueFactory(new PropertyValueFactory<PivotRow, Double>("SUM_REAL"));

                SUM_REAL.setCellFactory(tc -> new TableCell<PivotRow, Double>() {
                    @Override
                    protected void updateItem(Double price, boolean empty) {
                        super.updateItem(price, empty);
                        if (empty) {
                            setText(null);
                        } else {
                            setText(currencyFormat.format(price));
                        }
                    }
                });
                tPivot.getColumns().add(SUM_REAL);

                final BigDecimal total_itog_first_rasx = new BigDecimal(list.stream()
                        .mapToDouble(o -> Arrays.stream(o.getSkladPivots()).mapToDouble(a -> a.getFIRST_RASX()).sum())
                        .sum());
                TableColumn<PivotRow, Double> FIRST_RASX = new TableColumn<>(
                        "Ітого сумма продажу в роздрібних цінах\n-------------\n"
                                + total_itog_first_rasx.setScale(2, RoundingMode.HALF_EVEN).doubleValue());
                FIRST_RASX.setCellValueFactory(new PropertyValueFactory<PivotRow, Double>("FIRST_RASX"));
                FIRST_RASX.setCellFactory(tc -> new TableCell<PivotRow, Double>() {
                    @Override
                    protected void updateItem(Double price, boolean empty) {
                        super.updateItem(price, empty);
                        if (empty) {
                            setText(null);
                        } else {
                            setText(currencyFormat.format(price));
                        }
                    }
                });
                tPivot.getColumns().add(FIRST_RASX);

                final BigDecimal total_itog_val_dohod = new BigDecimal(list.stream()
                        .mapToDouble(o -> Arrays.stream(o.getSkladPivots()).mapToDouble(a -> a.getVAL_DOHOD()).sum())
                        .sum());
                TableColumn<PivotRow, Double> VAL_DOHOD = new TableColumn<>("Ітого валовий прибуток\n-------------\n"
                        + total_itog_val_dohod.setScale(2, RoundingMode.HALF_EVEN).doubleValue());
                VAL_DOHOD.setCellValueFactory(new PropertyValueFactory<PivotRow, Double>("VAL_DOHOD"));
                VAL_DOHOD.setCellFactory(tc -> new TableCell<PivotRow, Double>() {
                    @Override
                    protected void updateItem(Double price, boolean empty) {
                        super.updateItem(price, empty);
                        if (empty) {
                            setText(null);
                        } else {
                            setText(currencyFormat.format(price));
                        }
                    }
                });
                tPivot.getColumns().add(VAL_DOHOD);

                TableColumn<PivotRow, String> shtrihcod_ = new TableColumn<>("Штрихкод");
                shtrihcod_.setCellValueFactory(new PropertyValueFactory<PivotRow, String>("SHTRIHKOD"));
                tPivot.getColumns().add(shtrihcod_);
                tPivot.setItems(pivotList);
            }
        });

        exportPivotService.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                progressBarPivot.progressProperty().unbind();
                pivotStatus.textProperty().unbind();
                btGetPivot.setDisable(false);
                btExportPivot.setDisable(false);
            }
        });

        pivotService.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                progressBarPivot.progressProperty().unbind();
                pivotStatus.textProperty().unbind();
                btGetPivot.setDisable(false);
                btExportPivot.setDisable(false);
                pivotStatus.setText("EXPORT FAIL");
                Throwable throwable = pivotService.getException();
                throwable.printStackTrace();

            }
        });

        exportPivotService.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                progressBarPivot.progressProperty().unbind();
                pivotStatus.textProperty().unbind();
                btGetPivot.setDisable(false);
                btExportPivot.setDisable(false);
                pivotStatus.setText("EXPORT FAIL");
                Throwable throwable = exportPivotService.getException();
                throwable.printStackTrace();

            }
        });

        nameRepos = new NameRepository();

        fName.setOnKeyReleased(keyEvent -> {
            String str = fName.getText();
            if (str.length() > 2) {
                List<Name> list = nameRepos.getByName(str.toLowerCase() + "%");
                nameList.clear();
                nameList.addAll(list);
                listName.setItems(nameList);
            }

        });
        listSelName.setItems(filterName);
        listName.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listSelName.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        VidRepository vidRep = new VidRepository();
        vidList.addAll(vidRep.getAll());
        listVid.setItems(vidList);
        listSelVid.setItems(filterVid);
        listVid.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listSelVid.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        TipRepository tipRep = new TipRepository();
        tipList.addAll(tipRep.getAll());
        listTip.setItems(tipList);
        listSelTip.setItems(filterTip);
        listTip.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listSelTip.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        GrupRepository grupRep = new GrupRepository();
        grupList.addAll(grupRep.getAll());
        listGrup.setItems(grupList);
        listSelGrup.setItems(filterGrup);
        listGrup.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listSelGrup.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        PostavkaRepository postavkaRep = new PostavkaRepository();
        postavkaList.addAll(postavkaRep.getAll());
        listPostavka.setItems(postavkaList);
        listSelPostavka.setItems(filterPostavka);
        listPostavka.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listSelPostavka.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        klassRepos = new KlassRepository();
        listKlass.setItems(klassList);
        listSelKlass.setItems(filterKlass);
        listSelKlass.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listKlass.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        fKlass.setOnKeyReleased(keyEvent -> {
            String str = fKlass.getText();
            if (str.length() > 2) {
                List<Klass> list = klassRepos.getByName(str.toLowerCase() + "%");
                klassList.clear();
                klassList.addAll(list);
            }

        });
        skladList.addAll(allSklad);
        listSklad.setItems(skladList);
        listSelSklad.setItems(filterSklad);
        listSelSklad.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listSklad.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

}
