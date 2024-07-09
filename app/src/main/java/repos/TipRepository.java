package repos;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import model.Tip;

public class TipRepository extends Dao implements Repository<Tip>{
    
    private String SQL_ALL = "select KOD,NAME from TIP ORDER BY NAME";

    public TipRepository(DataSource ds) {
        super(ds);
    }

    public TipRepository() {}

    @Override
    public List<Tip> getAll() {
        try {
            QueryRunner runner = new QueryRunner(ds);
            BeanListHandler<Tip> beanListHandler = new BeanListHandler<>(Tip.class);

            List<Tip> list = runner.query(SQL_ALL, beanListHandler, new Object[]{});
            return list;
        } catch (SQLException ex) {
            Logger.getLogger(TipRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<Tip>();
    }

    @Override
    public List<Tip> getByName(String name) {
        try {
            QueryRunner runner = new QueryRunner(ds);
            BeanListHandler<Tip> beanListHandler = new BeanListHandler<>(Tip.class);

            List<Tip> list = runner.query(SQL_ALL, beanListHandler, new Object[]{name});
            return list;
        } catch (SQLException ex) {
            Logger.getLogger(TipRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<Tip>();
    }
    
}
