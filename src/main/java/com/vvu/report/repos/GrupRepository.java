/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vvu.report.repos;

import com.vvu.report.model.Grup;
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
public class GrupRepository extends Dao implements Repository<Grup>{
    
    private String SQL_ALL = "select KOD,NAME from GRUP ORDER BY NAME";
    private String SQL_BY_NAME = "select KOD,NAME from GRUP WHERE NAME LIKE ? ORDER BY NAME";

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

            List<Grup> list = runner.query(SQL_ALL, new Object[]{}, beanListHandler);
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

            List<Grup> list = runner.query(SQL_ALL, new Object[]{name}, beanListHandler);
            return list;
        } catch (SQLException ex) {
            Logger.getLogger(GrupRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<Grup>();
    }
    
}
