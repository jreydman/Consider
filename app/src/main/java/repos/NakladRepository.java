package repos;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import model.Naklad;

public class NakladRepository extends Dao{
    public double getSkidka(Integer kodSklad, String tipdoc, String startDate, String endDate, Boolean roundBar) {
        String SQL_LINE = String.format("""
            SELECT SUM(a.SUMROUND) as DISCOUNT
            FROM MVC a 
            WHERE
                a.KODSKL = '%s'
                and a.DATEDOC between '%s' and '%s'
                and a.TIPDOC='%s'
                %s
            """, kodSklad, startDate, endDate, tipdoc, (!roundBar ? "and a.SUMROUND<0" : ""));
            
        try {
            QueryRunner runner = new QueryRunner(ds);
            BeanListHandler<Naklad> beanListHandler = new BeanListHandler<>(Naklad.class);

            List<Naklad> list = runner.query(SQL_LINE, beanListHandler, new Object[]{});
            return list.get(0).getDISCOUNT();
        } catch (SQLException ex) {
            Logger.getLogger(NakladRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
}