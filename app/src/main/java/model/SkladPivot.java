package model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SkladPivot {
    
    double KOLVO=0.0; //6
    double FIRST_RASX=0.0; //7
    double SUM_BEZ_NDS=0.0; //8
    double SUM_NDS=0.0;//9
    double SUM_REAL=0.0; //10
    double VAL_DOHOD=0.0; //11

    public SkladPivot() {}
    
    public SkladPivot(Report r) {
        this.KOLVO = r.getKOLVO();
        this.FIRST_RASX = r.getFIRST_RASX();
        this.SUM_BEZ_NDS =r.getSUM_BEZ_NDS();
        this.SUM_NDS = r.getSUM_NDS();
        this.SUM_REAL = r.getSUM_REAL();
        this.VAL_DOHOD = r.getVAL_DOHOD();
    }
    public void init (Report r) {
        this.KOLVO = r.getKOLVO();
        this.FIRST_RASX = r.getFIRST_RASX();
        this.SUM_BEZ_NDS =r.getSUM_BEZ_NDS();
        this.SUM_NDS = r.getSUM_NDS();
        this.SUM_REAL = r.getSUM_REAL();
        this.VAL_DOHOD = r.getVAL_DOHOD();
    }
}
