/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vvu.report.repos;

import com.vvu.report.model.Tip;
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
public class TipRepository extends Dao implements Repository<Tip>{
    
    private String SQL_ALL = "select KOD,NAME from TIP ORDER BY NAME";
    private String SQL_BY_NAME = "select KOD,NAME from TIP WHERE NAME LIKE ? ORDER BY NAME";

    public TipRepository(DataSource ds) {
        super(ds);
    }

    public TipRepository() {
    }
    

    @Override
    public List<Tip> getAll() {
        try {
            QueryRunner runner = new QueryRunner(ds);
            BeanListHandler<Tip> beanListHandler = new BeanListHandler<>(Tip.class);

            List<Tip> list = runner.query(SQL_ALL, new Object[]{}, beanListHandler);
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

            List<Tip> list = runner.query(SQL_ALL, new Object[]{name}, beanListHandler);
            return list;
        } catch (SQLException ex) {
            Logger.getLogger(TipRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<Tip>();
    }
    
}
