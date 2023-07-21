/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vvu.report.model;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Root
 */
@Getter
@Setter
public class Tip {
    Integer KOD;
    String NAME;

    public Tip() {
    }

    public Tip(Integer KOD, String MAME) {
        this.KOD = KOD;
        this.NAME = MAME;
    }

    @Override
    public String toString() {
        return NAME;
    }
    
}
