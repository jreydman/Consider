package model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Postavka {
    Integer KOD;
    String NAME;

    public Postavka() {}

    @Override
    public String toString() {
        return NAME;
    }
    
}
