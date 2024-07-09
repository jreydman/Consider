package model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Grup {
    Integer KOD;
    String NAME;

    public Grup() {}

    public Grup(Integer KOD, String NAME) {
        this.KOD = KOD;
        this.NAME = NAME;
    }

    @Override
    public String toString() {
        return NAME;
    }
}
