package model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Klass {
    Integer KOD;
    String NAME;

    public Klass() {}

    public Klass(Integer KOD, String NAME) {
        this.KOD = KOD;
        this.NAME = NAME;
    }

    @Override
    public String toString() {
        return NAME;
    }
    
    
    
}
