package model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Vid {
    String KOD;
    String NAME;    

    public Vid() {}

    public Vid(String KOD, String NAME) {
        this.KOD = KOD;
        this.NAME = NAME;
    }

    @Override
    public String toString() {
        return NAME;
    }
}
