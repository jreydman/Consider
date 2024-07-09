package model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

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

  public Filter() {}
}
