/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vvu.report.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Root
 */
@Getter
@Setter
public class Filter {
  
  LocalDate begin;
  LocalDate end;
  int sklad;
  Set<Name> fName = new HashSet<>();
  Set<Grup> fGrup = new HashSet<>();
  Set<Tip> fTip = new HashSet<>();
  Set<Vid> fVid = new HashSet<>();
  Set<Klass> fKlass = new HashSet<>();

  public Filter() {
  }
  
  
  
          
}
