/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vvu.report.service;

import com.vvu.report.model.Report;
import com.vvu.report.model.Sklad;
import com.vvu.report.model.YesPrint;
import com.vvu.report.repos.ReportRepository;
import com.vvu.report.repos.SkladRepository;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author Root
 */
public class ReportService extends Service<Map<Sklad, List<Report>>> {

    String filter;
    LocalDate from;
    LocalDate to;
    boolean group = false;
    YesPrint yesPrint = new YesPrint(-1,"");
    LocalTime start;
    LocalTime stop;

    public ReportService() {

    }

    public void startService(LocalDate from, LocalDate to, boolean group, String filter, YesPrint yesPrint,
            LocalTime start,LocalTime stop) {

        if (!isRunning()) {
            this.filter = filter;
            this.from = from;
            this.to = to;
            this.group = group;
            this.yesPrint = yesPrint;
            this.start = start;
            this.stop = stop;
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
    protected Task<Map<Sklad, List<Report>>> createTask() {
        return new Task<Map<Sklad, List<Report>>>() {
            @Override
            protected Map<Sklad, List<Report>> call() throws Exception {
                ReportRepository repository = new ReportRepository();
                SkladRepository skladRepository = new SkladRepository();
                List<Sklad> sklads = skladRepository.getAll();
                Map<Sklad, List<Report>> map = new TreeMap<>();
                long size = 0;
                int i = 0;
                this.updateProgress(i, sklads.size());
                for (Sklad sklad : sklads) {

                    updateMessage(String.format("Читаю данные по складу %s", sklad.getNAME()));
                    List<Report> list = repository.getAll(sklad.getKOD(), from, to, filter,yesPrint,start,stop);
                    size += list.size();
                    updateMessage(String.format("Считал данные по складу %s, %d записей", sklad.getNAME(), size));
                    if (group) {
                        Map<String, List<Report>> groupMap
                                = list.stream().collect(Collectors.groupingBy(Report::getNAMEKOD));
                        ArrayList<Report> total = new ArrayList<>();
                        updateMessage(String.format("Группирую данные по складу %s", sklad.getNAME()));
                        for (Map.Entry<String, List<Report>> entry : groupMap.entrySet()) {
                            List<Report> temp = entry.getValue();
                            Collections.sort(temp);
                            Report r = (Report) temp.get(0).clone();
                            r.setKOLVO(temp.stream().mapToDouble(t -> t.getKOLVO()).sum());
                            r.setSUM_BEZ_NDS(temp.stream().mapToDouble(t -> t.getSUM_BEZ_NDS()).sum());
                            r.setSUM_NDS(temp.stream().mapToDouble(t -> t.getSUM_NDS()).sum());
                            r.setSUM_REAL(temp.stream().mapToDouble(t -> t.getSUM_REAL()).sum());
                            r.setVAL_DOHOD(temp.stream().mapToDouble(t -> t.getVAL_DOHOD()).sum());
                            total.add(r);
                        }
                        Collections.sort(total, new Comparator<Report>() {
                            @Override
                            public int compare(Report u1, Report u2) {
                                return u1.getNAME().compareTo(u2.getNAME());
                            }
                        });
                        map.put(sklad, total);

                    } else {
                       Collections.sort(list, new Comparator<Report>() {
                            @Override
                            public int compare(Report u1, Report u2) {
                                return u1.getNAME().compareTo(u2.getNAME());
                            }
                        });
                        map.put(sklad, list);
                    }

                    i++;
                    this.updateProgress(i, sklads.size());

                }
                updateMessage(String.format("Выполнено! Загружено %d записей", size));
                return map;
            }
        };
    }

}
