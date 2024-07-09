package model;

public class Sklad  implements Comparable<Sklad>{
    Integer KOD;
    String NAME;

    public Sklad() {}

    public Integer getKOD() {
        return KOD;
    }

    public void setKOD(Integer KOD) {
        this.KOD = KOD;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    @Override
    public int compareTo(Sklad o) {
        return this.NAME.compareTo(o.getNAME());
    }
    
    @Override
    public String toString()  {
        return this.NAME;
    }
    
    
    
    
}
