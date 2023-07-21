/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vvu.report.service;

import com.vvu.report.model.PivotRow;
import com.vvu.report.model.Report;
import com.vvu.report.model.Sklad;
import com.vvu.report.model.YesPrint;
import com.vvu.report.repos.ReportRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 *
 * @author Root
 */
public class PivotService extends  Service<List<PivotRow>>{
    
    String filter;
    LocalDate from;
    LocalDate to;
    LocalTime start;
    LocalTime stop;
    ObservableList<Sklad> filterSklad;
    YesPrint yesPrint = new YesPrint(-1,"");
    

    public PivotService() {
    }
    
    public void startService(LocalDate from, LocalDate to,  String filter,ObservableList<Sklad> filterSklad,YesPrint yesPrint,
            LocalTime start,LocalTime stop) {

        if (!isRunning()) {
            this.filter = filter;
            this.from = from;
            this.to = to;
            this.filterSklad = filterSklad;
            this.yesPrint = yesPrint;
            this.start = start;
            this.stop  = stop;
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
    protected Task<List<PivotRow>> createTask() {
         return new Task<List<PivotRow>>() {
             @Override
             protected List<PivotRow> call() throws Exception {
                 List<PivotRow> rows = new ArrayList<>();
                 Sklad[] listSklad = filterSklad.stream().sorted(Comparator.comparingInt(Sklad::getKOD)).toArray(Sklad[]::new);
                 ReportRepository repository = new ReportRepository();
                 long size = 0;
                 int i = 0;
                 this.updateProgress(i, listSklad.length);
                 Map<String,PivotRow> map = new TreeMap<String, PivotRow>();
                 for (int j=0;j<listSklad.length;j++){
                      updateMessage(String.format("Читаю данные по складу %s", listSklad[j].getNAME()));
                      List<Report> list = repository.getAll(listSklad[j].getKOD(), from, to, filter,yesPrint,start,stop);
                      size += list.size();
                      updateMessage(String.format("Считал данные по складу %s, %d записей", listSklad[j].getNAME(), list.size()));
                      System.out.println(String.format("Считал данные по складу %s, %d записей", listSklad[j].getNAME(), list.size()));
                      updateMessage(String.format("Группирую данные по складу %s", listSklad[j].getNAME()));
                      Map<String, List<Report>> groupMap
                                = list.stream().collect(Collectors.groupingBy(Report::getNAMEKOD));
                      ArrayList<Report> total = new ArrayList<>();
                      
                      for (Map.Entry<String, List<Report>> entry : groupMap.entrySet()) {
                            List<Report> temp = entry.getValue();
                            Collections.sort(temp);
                            Report r = (Report) temp.get(0).clone();
                            r.setKOLVO(temp.stream().mapToDouble(t -> t.getKOLVO()).sum());
                            r.setSUM_BEZ_NDS(temp.stream().mapToDouble(t -> t.getSUM_BEZ_NDS()).sum());
                            r.setSUM_NDS(temp.stream().mapToDouble(t -> t.getSUM_NDS()).sum());
                            r.setSUM_REAL(temp.stream().mapToDouble(t -> t.getSUM_REAL()).sum());
                            r.setFIRST_RASX(temp.stream().mapToDouble(t -> t.getFIRST_RASX()).sum());
                            r.setVAL_DOHOD(temp.stream().mapToDouble(t -> t.getVAL_DOHOD()).sum());
                            total.add(r);
                      }
                      updateMessage(String.format("Группирую данные по складу %s %d" , listSklad[j].getNAME(),total.size()));
                      for (Report report :total){
                          PivotRow row = null;
                          if (map.containsKey(report.getNAMEKOD())){
                              row = map.get(report.getNAMEKOD());
                          }else {
                              row=new PivotRow(listSklad.length);
                              row.init(report);
                              map.put(report.getNAMEKOD(),row);
                         }
                          row.getSkladPivots()[j].init(report);
                      }
                      this.updateProgress(j+1, listSklad.length);
                    }
                    updateMessage("Обробатываю..");
                    rows.addAll(map.values().stream().collect(Collectors.toList()));
                    for (PivotRow row:rows){row.calc();}
                    Collections.sort(rows);
                    updateMessage(String.format("Оброботано %d строк",rows.size()));
                 return rows;
             }
         };
    }
    
   
    
}
