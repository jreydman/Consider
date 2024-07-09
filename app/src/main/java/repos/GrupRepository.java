package repos;

import model.Grup;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

public class GrupRepository extends Dao implements Repository<Grup>{
    
    private String SQL_ALL = "select KOD,NAME from GRUP ORDER BY NAME";

    public GrupRepository(DataSource ds) {
        super(ds);
    }

    public GrupRepository() {
    }
    

    @Override
    public List<Grup> getAll() {
        try {
            QueryRunner runner = new QueryRunner(ds);
            BeanListHandler<Grup> beanListHandler = new BeanListHandler<>(Grup.class);

            List<Grup> list = runner.query(SQL_ALL, beanListHandler, new Object[]{});
            return list;
        } catch (SQLException ex) {
            Logger.getLogger(GrupRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<Grup>();
    }

    @Override
    public List<Grup> getByName(String name) {
        try {
            QueryRunner runner = new QueryRunner(ds);
            BeanListHandler<Grup> beanListHandler = new BeanListHandler<>(Grup.class);

            List<Grup> list = runner.query(SQL_ALL, beanListHandler, new Object[]{name});
            return list;
        } catch (SQLException ex) {
            Logger.getLogger(GrupRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<Grup>();
    }
    
}
