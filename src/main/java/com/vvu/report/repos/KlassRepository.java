/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vvu.report.repos;

import com.vvu.report.model.Klass;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

/**
 *
 * @author Root
 */
public class KlassRepository extends Dao implements Repository<Klass>{
    
    private String SQL_ALL = "select KOD,NAME from KLASS ORDER BY NAME";
    private String SQL_BY_NAME = "select KOD,NAME from KLASS WHERE lower(NAME)  LIKE ? ORDER BY NAME";

    public KlassRepository(DataSource ds) {
        super(ds);
    }

    public KlassRepository() {
    }
    

    @Override
    public List<Klass> getAll() {
        try {
            QueryRunner runner = new QueryRunner(ds);
            BeanListHandler<Klass> beanListHandler = new BeanListHandler<>(Klass.class);

            List<Klass> list = runner.query(SQL_ALL, new Object[]{}, beanListHandler);
            return list;
        } catch (SQLException ex) {
            Logger.getLogger(KlassRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<Klass>();
    }

    @Override
    public List<Klass> getByName(String name) {
               try {
            QueryRunner runner = new QueryRunner(ds);
            BeanListHandler<Klass> beanListHandler = new BeanListHandler<>(Klass.class);

            List<Klass> list = runner.query(SQL_BY_NAME, new Object[]{name}, beanListHandler);
            return list;
        } catch (SQLException ex) {
            Logger.getLogger(KlassRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<Klass>();
    }
    
}
