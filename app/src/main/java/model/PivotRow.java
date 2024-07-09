package model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PivotRow  implements Comparable<PivotRow>{

    String NAMEKOD;
    String NAME;
    String KLASS_NAME; //5
    double KOLVO=0; //6
    double CENARASX=0.0; //7
    double FIRST_RASX=0.0; //7
    double SUM_BEZ_NDS=0.0; //8
    double SUM_NDS=0.0;//9
    double SUM_REAL=0.0; //10
    double VAL_DOHOD=0.0; //11
    String SHTRIHKOD; //13
    SkladPivot skladPivots[];

    public PivotRow(int count) {
        this.skladPivots = new SkladPivot[count];
        for (int i = 0; i < count; i++) {
            this.skladPivots[i] = new SkladPivot();
        }
    }
    public void init(Report r){
        this.NAMEKOD = r.getNAMEKOD();
        this.NAME = r.getNAME();
        this.KLASS_NAME = r.getKLASS_NAME();
        this.SHTRIHKOD = r.getSHTRIHKOD();
        this.CENARASX = r.getCENARASX();
    }

    public double getSkladKolvo() {
        return Arrays.stream(this.skladPivots).mapToDouble(a->a.getKOLVO()).sum();
    }

    public void setKOLVO(double value) {
        final BigDecimal d = new BigDecimal(value);
        this.KOLVO = d.setScale(2,RoundingMode.HALF_EVEN).doubleValue();
    }

    public void setSUM_BEZ_NDS(double value) {
        final BigDecimal d = new BigDecimal(value);
        this.SUM_BEZ_NDS = d.setScale(2,RoundingMode.HALF_EVEN).doubleValue();
    }

    public void setSUM_NDS(double value) {
        final BigDecimal d = new BigDecimal(value);
        this.SUM_NDS = d.setScale(2,RoundingMode.HALF_EVEN).doubleValue();
    }

    public void setSUM_REAL(double value) {
        final BigDecimal d = new BigDecimal(value);
        this.SUM_REAL = d.setScale(2,RoundingMode.HALF_EVEN).doubleValue();
    }

    public void setFIRST_RASX(double value) {
        final BigDecimal d = new BigDecimal(value);
        this.FIRST_RASX = d.setScale(2,RoundingMode.HALF_EVEN).doubleValue();
    }

    public void setVAL_DOHOD(double value) {
        final BigDecimal d = new BigDecimal(value);
        this.VAL_DOHOD = d.setScale(2,RoundingMode.HALF_EVEN).doubleValue();
    }

    public void calc() {
        for (int i = 0; i < this.skladPivots.length; i++) {
            this.FIRST_RASX += this.skladPivots[i].getFIRST_RASX();
            this.SUM_BEZ_NDS += this.skladPivots[i].getSUM_BEZ_NDS();
            this.SUM_NDS += this.skladPivots[i].getSUM_NDS();
            this.SUM_REAL += this.skladPivots[i].getSUM_REAL();
            this.VAL_DOHOD += this.skladPivots[i].getVAL_DOHOD();
            this.KOLVO += this.skladPivots[i].getKOLVO();
        }
    }

    @Override
    public int compareTo(PivotRow o) {
        return NAME.compareTo(o.getNAME());
    }

}
