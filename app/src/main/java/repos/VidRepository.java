package repos;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import model.Vid;

public class VidRepository extends Dao implements Repository<Vid>{
    
    private String SQL_ALL = "select KOD,NAME from VID ORDER BY NAME";
    private String SQL_BY_NAME = "select KOD,NAME from VID WHERE NAME LIKE ? ORDER BY NAME";

    public VidRepository(DataSource ds) {
        super(ds);
    }

    public VidRepository() {}

    @Override
    public List<Vid> getAll() {
        try {
            QueryRunner runner = new QueryRunner(ds);
            BeanListHandler<Vid> beanListHandler = new BeanListHandler<>(Vid.class);

            List<Vid> list = runner.query(SQL_ALL, beanListHandler, new Object[]{});
            return list;
        } catch (SQLException ex) {
            Logger.getLogger(VidRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<Vid>();
    }

    @Override
    public List<Vid> getByName(String name) {
        try {
            QueryRunner runner = new QueryRunner(ds);
            BeanListHandler<Vid> beanListHandler = new BeanListHandler<>(Vid.class);

            List<Vid> list = runner.query(SQL_BY_NAME, beanListHandler,  new Object[]{name});
            return list;
        } catch (SQLException ex) {
            Logger.getLogger(VidRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<Vid>();
    }
    
}
