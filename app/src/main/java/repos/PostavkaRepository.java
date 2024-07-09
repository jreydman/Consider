package repos;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import model.Postavka;

public class PostavkaRepository extends Dao implements Repository<Postavka>{
    
    private String SQL_ALL = "select KOD,NAME from POSTAVKA ORDER BY NAME";
    private String SQL_BY_NAME = "select KOD,NAME from POSTAVKA WHERE NAME LIKE ? ORDER BY NAME";

    @Override
    public List<Postavka> getAll() {
        try {
            QueryRunner runner = new QueryRunner(ds);
            BeanListHandler<Postavka> beanListHandler = new BeanListHandler<>(Postavka.class);

            List<Postavka> list = runner.query(SQL_ALL, beanListHandler, new Object[]{});
            return list;
        } catch (SQLException ex) {
            Logger.getLogger(KlassRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<Postavka>();
    }

    @Override
    public List<Postavka> getByName(String name) {
        try {
            QueryRunner runner = new QueryRunner(ds);
            BeanListHandler<Postavka> beanListHandler = new BeanListHandler<>(Postavka.class);

            List<Postavka> list = runner.query(SQL_BY_NAME, beanListHandler, new Object[]{name});
            return list;
        } catch (SQLException ex) {
            Logger.getLogger(KlassRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<Postavka>();
    }
    
}
