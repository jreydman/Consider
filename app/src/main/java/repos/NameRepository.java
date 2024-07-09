package repos;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import model.Name;

public class NameRepository extends Dao implements Repository<Name>{
    
    private String SQL_ALL = "select KOD,NAME from NAME ORDER BY NAME";
    private String SQL_BY_NAME = "select KOD,NAME from NAME WHERE lower(NAME) LIKE ? ORDER BY NAME";

    public NameRepository(DataSource ds) {
        super(ds);
    }

    public NameRepository() {}
    
    @Override
    public List<Name> getAll() {
        try {
            QueryRunner runner = new QueryRunner(ds);
            BeanListHandler<Name> beanListHandler = new BeanListHandler<>(Name.class);

            List<Name> list = runner.query(SQL_ALL, beanListHandler, new Object[]{});
            return list;
        } catch (SQLException ex) {
            Logger.getLogger(NameRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<Name>();
    }

    @Override
    public List<Name> getByName(String name) {
        try {
            QueryRunner runner = new QueryRunner(ds);
            BeanListHandler<Name> beanListHandler = new BeanListHandler<>(Name.class);

            List<Name> list = runner.query(SQL_BY_NAME, beanListHandler, new Object[]{name});
            return list;
        } catch (SQLException ex) {
            Logger.getLogger(NameRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<Name>();
    }
    
}
