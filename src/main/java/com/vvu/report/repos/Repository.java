/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vvu.report.repos;

import java.util.List;

/**
 *
 * @author Root
 */
public interface Repository<T> {

    List<T> getAll();
    List<T> getByName(String name);
    
}
