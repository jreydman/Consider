/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vvu.report.repos;

import javax.sql.DataSource;

/**
 *
 * @author Root
 */
public abstract class Dao {
    public DataSource ds;

    public Dao(DataSource ds) {
        this.ds = ds;
    }

    public Dao() {
         ds = DataSourceFactory.getDataSource();

    }
    
    
}
