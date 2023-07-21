package com.vvu.report.repos;

import com.vvu.report.model.Naklad;
import com.vvu.report.model.Report;
import com.vvu.report.model.Sklad;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.poi.ss.formula.functions.Na;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NakladRepository extends Dao{
    public double getSkidka(Integer kodSklad, String tipdoc, String startDate, String endDate, Boolean roundBar) {
        String SQL_LINE = "SELECT SUM(a.SUMROUND) as DISCOUNT\n" +
                          "FROM MVC a \n" +
                          "WHERE\n" +
                          "    a.KODSKL = '"+kodSklad+"'\n" +
                          "    and a.DATEDOC between '"+startDate+"' and '"+endDate+"'\n" +
                          "    and a.TIPDOC='"+tipdoc+"'\n" +
                (!roundBar?"    and a.SUMROUND<0":"");
        try {
            QueryRunner runner = new QueryRunner(ds);
            BeanListHandler<Naklad> beanListHandler = new BeanListHandler<>(Naklad.class);

            List<Naklad> list = runner.query(SQL_LINE, new Object[]{}, beanListHandler);
            return list.get(0).getDISCOUNT();
        } catch (SQLException ex) {
            Logger.getLogger(NakladRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
}
