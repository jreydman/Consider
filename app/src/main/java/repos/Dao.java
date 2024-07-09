package repos;

import javax.sql.DataSource;

public abstract class Dao {
    public DataSource ds;

    public Dao(DataSource ds) {
        this.ds = ds;
    }

    public Dao() {
        ds = DataSourceFactory.getDataSource();
    }
}
