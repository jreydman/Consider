package model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Name {
    Integer KOD;
    String NAME;

    public Name() {}

    @Override
    public String toString() {
        return NAME;
    }
}
