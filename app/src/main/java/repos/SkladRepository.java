package repos;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import model.Sklad;

public class SkladRepository extends Dao implements Repository<Sklad>{
    
    private String SQL_ALL = "select KOD,NAME from LISTSKL ORDER BY NAME";

    public SkladRepository(DataSource ds) {
        super(ds);
    }

    public SkladRepository() {}
    
    @Override
    public List<Sklad> getAll() {
        try {
            QueryRunner runner = new QueryRunner(ds);
            BeanListHandler<Sklad> beanListHandler = new BeanListHandler<>(Sklad.class);

            List<Sklad> list = runner.query(SQL_ALL, beanListHandler, new Object[]{});
            return list;
        } catch (SQLException ex) {
            Logger.getLogger(SkladRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<Sklad>();
    }

    @Override
    public List<Sklad> getByName(String name) {
        try {
            QueryRunner runner = new QueryRunner(ds);
            BeanListHandler<Sklad> beanListHandler = new BeanListHandler<>(Sklad.class);

            List<Sklad> list = runner.query(SQL_ALL, beanListHandler, new Object[]{name});
            return list;
        } catch (SQLException ex) {
            Logger.getLogger(SkladRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<Sklad>();
    }
    
}

