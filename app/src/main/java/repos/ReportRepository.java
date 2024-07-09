package repos;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.math3.util.Precision;

import model.Report;
import model.YesPrint;

public class ReportRepository extends Dao {

    public String PART1 = """
        WITH a AS (
            SELECT FIRST ? SKIP ? m.NOMKOD , m.NAMEKOD , m.DATADOC ,m.KOLVO , m.CENAROZN, n.NAME ,
                m.KODMAT, n.KODKLASS , k.NAME AS KLASS_NAME, n.PDV, case WHEN v.YESPRINT=1 THEN 'Фискальный' else 'Нефискальный' END  as YESPRINT, v.DATESROK,
                m.KODSKLADA ,cast(round(m.CENARASX,2) as numeric (15,2)) AS CENARASX, CAST(round(c.PRICEPRIXOD,2) AS numeric (15,2)) AS PRICEPRIXOD,
                n.SHTRIHKOD, p.NAME  AS POSTAVKA_NAME_OLD, c.PRICEREAL3,IIF((COALESCE(c.VEDKODC,'')!=''), c.VEDKODC,COALESCE(n.VEDKOD,'')) as VEDKODC, 
                case (n.PDV) when 0 then '7%' when 1 then '20%' when 2 then '0%' else '-' end as STAVKA_NDS, u.FULLNAME,v.CHECKDOPINFO
            FROM mv m
            JOIN NAME n ON n.KOD =m.NAMEKOD
            JOIN MVC v ON v.KROSSNOM  =m.KROSSNOMER
            LEFT JOIN CENNIK c ON C.KOD = M.KODMAT
            LEFT JOIN KLASS k ON k.KOD  =n.KODKLASS
            LEFT JOIN POSTAVKA p  ON p.KOD =n.KODPOST
            LEFT JOIN USERS u  ON u.ID =v.BODYDOC
            WHERE m.TIPDOC = 8
            AND m.DATADOC  BETWEEN ? AND ?
            AND m.KODSKLADA = ? 
        """;

    public String PART2 = """
        ORDER BY m.NOMKOD )
        SELECT a.NOMKOD,a.YESPRINT, a.DATESROK,a.NAMEKOD,c.LAST_DATE,COALESCE(C.LAST_KOLVO,0) AS KOLVO_PRIHOD, a.name,KLASS_NAME,a.kolvo,a.CENARASX,
            round(COALESCE(c.cena_bez_nds,a.PRICEPRIXOD)*a.kolvo,5) AS sum_bez_nds,
            round(COALESCE(c.cena_nds,a.PRICEPRIXOD)*a.kolvo,5) AS sum_nds,
            round(a.kolvo*a.CENARASX,2) AS SUM_REAL, round(COALESCE(a.CENAROZN, a.PRICEREAL3)*a.KOLVO,2) AS FIRST_RASX,
            COALESCE(a.CENAROZN, a.PRICEREAL3) as CENA_ROZN, PDV,
            round(round(a.kolvo*a.CENARASX,2)-round(COALESCE(c.cena_nds,a.PRICEPRIXOD)*a.kolvo,5),5) AS VAL_DOHOD,
            CASE COALESCE(c.perenos,0)  WHEN 0 THEN P.NAME
                                        ELSE 'Перенос с '||l.NAME
            END AS POSTAVKA_NAME,a.SHTRIHKOD,a.DATADOC,a.VEDKODC,a.STAVKA_NDS,a.FULLNAME,a.CHECKDOPINFO
        FROM a  left  JOIN CENA_REPORT2(NOMKOD) c ON NOMKOD=c.KOD_NOM
        LEFT JOIN listskl l ON l.KOD  = c.perenos
        LEFT JOIN POSTAVKA p  ON p.KOD =C.KODPARTNERA
        ORDER BY NAME,LAST_DATE
        """;

    public List<Report> getAll(Integer sklad, LocalDate from, LocalDate to, String filter, YesPrint yesPrint, LocalTime start, LocalTime stop) {
        ArrayList<Report> list = new ArrayList<>();
        int skip = 0;
        int first = 1000;
        try {
            if (filter == null || filter.isEmpty()) {
                filter = " AND m.kolvo>0 ";
            }

            QueryRunner runner = new QueryRunner(ds);
            BeanListHandler<Report> beanListHandler = new BeanListHandler<>(Report.class);
            String filterYes = " ";
            switch (yesPrint.getValue()) {
                case 1:
                    filterYes = " and v.YESPRINT=1 ";
                    break;
                case 0:
                    filterYes = " and v.YESPRINT is NULL ";
                    break;
            }
            while (true) {
                String SQL_ALL = PART1 + filterYes + filter + PART2;
                List<Report> listPage = runner.query(SQL_ALL, beanListHandler, new Object[]{first, skip, java.sql.Date.valueOf(from), java.sql.Date.valueOf(to), sklad});
                skip += first;
                if (listPage == null || listPage.size() == 0) {
                    break;
                }
                listPage.forEach(r -> {
                    LocalTime time = r.getDATESROK().toInstant().atZone(ZoneId.systemDefault()).toLocalTime();

                    if (time.compareTo(start) >= 1 && time.compareTo(stop) <= 0) {
                        double NDS = 0;
                        if (r.getPDV() == 0) {
                            NDS = Precision.round(0.07 * r.getCENA_ROZN() / 1.07, 5);
                        }
                        if (r.getPDV() == 1) {
                            NDS = Precision.round(0.2 * r.getCENA_ROZN() / 1.2, 5);
                        }
                        r.setNDS(NDS);
                        list.add(r);
                    }
                }
                );
            }
        }
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(ReportRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
}