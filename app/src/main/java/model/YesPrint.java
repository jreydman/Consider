package model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class YesPrint {
    int value;
    String name;

    public YesPrint(int value, String name) {
        this.value = value;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
