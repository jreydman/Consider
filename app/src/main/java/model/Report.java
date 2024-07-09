package model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalTime;
import java.util.Date;

@Getter
@Setter
public class Report  implements Cloneable,Comparable<Report>{
    Integer NOMKOD;
    String NAMEKOD; //1
    Date LAST_DATE; //2
    double KOLVO_PRIHOD; //3
    String NAME; //4
    String KLASS_NAME; //5
    double KOLVO; //6
    double CENARASX; //7
    double SUM_BEZ_NDS; //8
    double SUM_NDS;//9
    double SUM_REAL; //10
    double VAL_DOHOD; //11
    String POSTAVKA_NAME; //12
    String SHTRIHKOD; //13
    double FIRST_RASX;
    double CENA_ROZN;
    Date DATADOC;
    String VEDKODC;
    double NDS;
    String STAVKA_NDS;
    int PDV;
    String YESPRINT;
    Date DATESROK;
    LocalTime TIMESROK;
    String FULLNAME;
    String CHECKDOPINFO;
    double DISCOUNT;
    double DISCOUNT_PERCENT;
    
    public Report() {}

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int compareTo(Report o) {
        return o.getNOMKOD()-this.NOMKOD;
    }
    
    public double getKOLVO() {
        BigDecimal a = new BigDecimal(this.KOLVO);
        return a.setScale(2,RoundingMode.HALF_EVEN).doubleValue();
    }

    public double getSUM_BEZ_NDS() {
        BigDecimal a = new BigDecimal(this.SUM_BEZ_NDS);
        return a.setScale(2, RoundingMode.HALF_EVEN).doubleValue();
    }

    public double getSUM_NDS() {
        BigDecimal a = new BigDecimal(this.SUM_NDS);
        return a.setScale(2, RoundingMode.HALF_EVEN).doubleValue();
    }

    public double getSUM_REAL() {
        return this.SUM_REAL;
    }

    public double getNDS() {
        BigDecimal a = new BigDecimal(this.NDS);
        return a.setScale(2, RoundingMode.HALF_EVEN).doubleValue();
    }

    public double getVAL_DOHOD() {
        BigDecimal a = new BigDecimal(this.VAL_DOHOD);
        return a.setScale(2, RoundingMode.HALF_EVEN).doubleValue();
    }

    public double getDISCOUNT(){
        BigDecimal a = BigDecimal.valueOf(CENARASX*KOLVO);
        BigDecimal b = BigDecimal.valueOf(CENA_ROZN*KOLVO);
        a = a.setScale(2, RoundingMode.HALF_UP);
        b = b.setScale(2, RoundingMode.HALF_UP);

        BigDecimal res = b.subtract(a);
        return this.DISCOUNT = res.doubleValue();
    }
    
    public double getDISCOUNT_PERCENT(){
        if (CENA_ROZN==0) return this.DISCOUNT_PERCENT=0;

        BigDecimal a = BigDecimal.valueOf(CENARASX*KOLVO);
        BigDecimal b = BigDecimal.valueOf(CENA_ROZN*KOLVO);
        BigDecimal res = b.subtract(a);

        if (res.equals(BigDecimal.valueOf(0))) return this.DISCOUNT_PERCENT=0;

        res = res.multiply(BigDecimal.valueOf(100));
        res = res.divide(b, 3, RoundingMode.HALF_UP);
        res = res.setScale(3,RoundingMode.HALF_EVEN);
        return this.DISCOUNT_PERCENT = res.doubleValue();
    }
}
