package model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Tip {
    Integer KOD;
    String NAME;

    public Tip() {}

    public Tip(Integer KOD, String MAME) {
        this.KOD = KOD;
        this.NAME = MAME;
    }

    @Override
    public String toString() {
        return NAME;
    }
}
