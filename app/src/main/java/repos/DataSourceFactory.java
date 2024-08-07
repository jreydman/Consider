package repos;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.firebirdsql.pool.FBSimpleDataSource;

public class DataSourceFactory {

    public static DataSource getDataSource() {
        // String driver = "org.firebirdsql.jdbc.FBDriver";
        String url = "//%s:%s/%s";

        Properties props = new Properties();
        FileInputStream fis = null;
        FBSimpleDataSource fbDS = null;
        try {
            fis = new FileInputStream("db.conf");
            props.load(new InputStreamReader(fis, StandardCharsets.UTF_8));
            //props.load(fis);
            fbDS = new FBSimpleDataSource();
            String host = props.getProperty("db.host");
            String port = props.getProperty("db.port");
            String base = props.getProperty("db.base");
            fbDS.setCharSet(props.getProperty("db.charSet"));
            fbDS.setUserName(props.getProperty("db.login"));
            fbDS.setPassword(props.getProperty("db.pass"));
            String URL = String.format(url, host, port, base);
            fbDS.setDatabase(URL);
            System.out.println(URL);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return fbDS;
    }

    public static void createProcedure() {
        String SQL_CALL = """
            CREATE OR ALTER PROCEDURE CENA_REPORT(NOMKOD INTEGER)
            RETURNS (KOD_NOM INTEGER,NOM_KOD INTEGER,CENA_BEZ_NDS NUMERIC(15, 5), CENA_NDS NUMERIC(15, 5),PERENOS SMALLINT,
            FIRST_RASX NUMERIC(15, 2),
            LAST_KOLVO NUMERIC(15, 2), LAST_DATE DATE ) AS
            DECLARE	VARIABLE TIPDOC SMALLINT;
            DECLARE	VARIABLE KODSKLADA SMALLINT;
            DECLARE	VARIABLE PERENOSKLADA SMALLINT;
            DECLARE	VARIABLE KODMAT VARCHAR(15);
            DECLARE	VARIABLE DATADOC DATE;
            DECLARE	VARIABLE KODORGF VARCHAR(15);
            DECLARE	VARIABLE CENAPRIX NUMERIC(15, 5);
            DECLARE	VARIABLE CENARASX NUMERIC(15, 2);
            DECLARE	VARIABLE STAVKANDS NUMERIC(5, 2);
            DECLARE VARIABLE KOD INTEGER;
            DECLARE VARIABLE FLAG INTEGER;
            DECLARE VARIABLE C SMALLINT;
            DECLARE VARIABLE TIPNDS SMALLINT;
            DECLARE	VARIABLE KOLVO NUMERIC(15, 3);
            BEGIN
                CENA_BEZ_NDS = NULL;    CENA_NDS = NULL;    CENAPRIX = NULL;    TIPNDS = NULL;    NOM_KOD=NOMKOD;    PERENOS=0;
                KOD_NOM=NOMKOD;
                LAST_KOLVO=NULL;
                LAST_DATE=NULL;
                FIRST_RASX=NULL;
                SELECT 	M.KODSKLADA,M.DATADOC,	C.KODORGF,	M.KODMAT FROM	MV M JOIN CENNIK C ON 	C.KOD = M.KODMAT 
                WHERE 	NOMKOD =:NOMKOD
                INTO 	:KODSKLADA,	:DATADOC,	:KODORGF,	:KODMAT; 
                IF (ROW_COUNT = 0) THEN BEGIN
                    SUSPEND;
                    EXIT;
                END
                TIPDOC = 0;
                C=0;
                FLAG=1;
                WHILE (FLAG<>0) DO BEGIN
                    IF (FLAG=3) THEN  BEGIN
                        SELECT C.KODORGF
                        FROM MV M
                        JOIN CENNIK C ON C.KOD = M.KODMAT 
                        WHERE NOMKOD =:NOM_KOD
                        INTO :KODMAT;
                        FLAG=1;
                    END	
                    SELECT	FIRST 1 	NOMKOD,	KODSKLADA,	DATADOC,	KODORGF,	CENAPRIX,CENARASX,	TIPNDS,	TIPDOC,	STAVKANDS,	PERENOS,KOLVO
                    FROM (
                        SELECT M.NOMKOD, M.KODSKLADA, M.DATADOC, C.KODORGF, M.CENAPRIX, M.CENARASX, M2.TIPNDS, M.TIPDOC, M2.STAVKANDS, 0 AS PERENOS,M.KOLVO 
                        FROM MV M
                        JOIN CENNIK C ON C.KOD = M.KODMAT
                        JOIN MVC M2 ON M2.KROSSNOM = M.KROSSNOMER
                        WHERE M.KODMAT =:KODMAT AND M.TIPDOC = 1 AND M.DATADOC <= DATADOC AND M.KODSKLADA =:KODSKLADA
                        UNION
                        SELECT M.NOMKOD, M.KODSKLADA, M.DATADOC, C.KODORGF, M.CENAPRIX,M.CENARASX, M2.TIPNDS, M.TIPDOC, M2.STAVKANDS, m.KODPARTNERA AS PERENOS,M.KOLVO
                        FROM MV M	
                        JOIN CENNIK C ON C.KOD = M.KODMAT
                        JOIN MVC M2 ON M2.KROSSNOM = M.KROSSNOMER
                        WHERE M.KODMAT =:KODMAT AND M.TIPDOC = 4 AND M.DATADOC <= DATADOC AND M.KODPARTNERA =:KODSKLADA
                    )
                    ORDER BY NOMKOD
                    INTO    :NOM_KOD,	:KODSKLADA,	:DATADOC,	:KODORGF,	:CENAPRIX, :CENARASX,	:TIPNDS,	:TIPDOC,	:STAVKANDS,	:PERENOSKLADA,:KOLVO;
                    IF (ROW_COUNT=0) THEN BEGIN
                        C=C+1;
                        FLAG=3;
                    END 
                    ELSE BEGIN
                        IF (TIPDOC=1) THEN FLAG=0; ELSE FLAG=1;
                        IF (LAST_KOLVO IS NULL) THEN LAST_KOLVO=KOLVO;
                        IF (LAST_DATE IS NULL) THEN LAST_DATE=DATADOC;
                        IF (FIRST_RASX IS NULL) THEN FIRST_RASX=CENARASX;
                    END 
                    IF (C>2) THEN FLAG=0;
                    IF (PERENOS = 0 AND TIPDOC = 4) THEN PERENOS = KODSKLADA;
                END
                IF (CENAPRIX IS NOT NULL) THEN BEGIN
                    IF (COALESCE(TIPNDS, 2)= 1) THEN CENA_NDS = CAST(ROUND(CENAPRIX*(1 + STAVKANDS / 100), 5) AS NUMERIC (15, 5));
                    ELSE CENA_NDS = ROUND(CENAPRIX, 5);
                    IF (COALESCE(TIPNDS, 2)= 0) THEN CENA_BEZ_NDS = CAST(ROUND(CENAPRIX /(1 + STAVKANDS / 100), 5) AS NUMERIC (15, 5));
                    ELSE CENA_BEZ_NDS = ROUND(CENAPRIX, 5);
                END
                SUSPEND;
            END
            """;

        try {
            getDataSource().getConnection().createStatement().execute(SQL_CALL);
        }
        catch (SQLException ex) {
            Logger.getLogger(DataSourceFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void createProcedure2() {
        String SQL_CALL = """
            CREATE OR ALTER PROCEDURE CENA_REPORT2 (NOMKOD INTEGER) RETURNS 
            ( KOD_NOM INTEGER, NOM_KOD INTEGER, CENA_BEZ_NDS NUMERIC(15, 5), CENA_NDS NUMERIC(15, 5), PERENOS SMALLINT, FIRST_RASX NUMERIC(15, 2), 
            LAST_KOLVO NUMERIC(15, 2), LAST_DATE DATE ,KODPARTNERA INTEGER) AS
            DECLARE VARIABLE TIPDOC SMALLINT;
            DECLARE VARIABLE KODSKLADA SMALLINT;
            DECLARE VARIABLE PERENOSKLADA SMALLINT;
            DECLARE VARIABLE KODMAT VARCHAR(15);
            DECLARE VARIABLE DATADOC DATE;
            DECLARE VARIABLE KODORGF VARCHAR(15);
            DECLARE VARIABLE CENAPRIX NUMERIC(15, 5);
            DECLARE VARIABLE CENARASX NUMERIC(15, 2);
            DECLARE VARIABLE STAVKANDS NUMERIC(5, 2);
            DECLARE VARIABLE KOD INTEGER;
            DECLARE VARIABLE FLAG INTEGER;
            DECLARE VARIABLE C SMALLINT;
            DECLARE VARIABLE TIPNDS SMALLINT;
            DECLARE VARIABLE KOLVO NUMERIC(15, 3);
            BEGIN
                CENA_BEZ_NDS = NULL;
                CENA_NDS = NULL;
                CENAPRIX = NULL;
                TIPNDS = NULL;
                NOM_KOD = NOMKOD;
                PERENOS = 0;
                KOD_NOM = NOMKOD;
                LAST_KOLVO = NULL;
                LAST_DATE = NULL;
                FIRST_RASX = NULL;
                KODPARTNERA=0;
            
                SELECT M.KODSKLADA, M.DATADOC, C.KODORGF, M.KODMAT
                FROM MV M
                JOIN CENNIK C ON C.KOD = M.KODMAT
                WHERE NOMKOD =:NOMKOD
                INTO :KODSKLADA, :DATADOC, :KODORGF, :KODMAT;
                IF (ROW_COUNT = 0) THEN
                BEGIN
                    SUSPEND;
                    EXIT;
                END 
                TIPDOC = 0;
                C = 0; 
                FLAG = 1;
                WHILE (FLAG <> 0) DO BEGIN
                    IF (FLAG = 3) THEN BEGIN
                        SELECT C.KODORGF
                        FROM MV M
                        JOIN CENNIK C ON C.KOD = M.KODMAT
                        WHERE NOMKOD =:NOM_KOD
                        INTO :KODMAT;
                        FLAG = 1;
                    END
                    SELECT FIRST 1 NOMKOD, KODSKLADA, DATADOC, KODORGF, CENAPRIX, CENARASX, TIPNDS, TIPDOC, STAVKANDS, PERENOS, KOLVO, KODPARTNERA
                    FROM (
                        SELECT M.NOMKOD, M.KODSKLADA, M.DATADOC, C.KODORGF, M.CENAPRIX, M.CENARASX, M2.TIPNDS, M.TIPDOC, M2.STAVKANDS, 0 AS PERENOS, M.KOLVO, M2.KODPARTNERA 
                        FROM MV M
                        JOIN CENNIK C ON C.KOD = M.KODMAT
                        JOIN MVC M2 ON M2.KROSSNOM = M.KROSSNOMER
                        WHERE M.KODMAT =:KODMAT AND M.TIPDOC = 1 AND M.DATADOC <= DATADOC AND M.KODSKLADA =:KODSKLADA
                        UNION
                        SELECT M.NOMKOD, M.KODSKLADA, M.DATADOC, C.KODORGF, M.CENAPRIX, M.CENARASX, M2.TIPNDS, M.TIPDOC, M2.STAVKANDS, m.KODPARTNERA AS PERENOS, M.KOLVO, M2.KODPARTNERA 
                        FROM MV M 
                        JOIN CENNIK C ON C.KOD = M.KODMAT
                        JOIN MVC M2 ON M2.KROSSNOM = M.KROSSNOMER
                        WHERE M.KODMAT =:KODMAT AND M.TIPDOC = 4 AND M.DATADOC <= DATADOC AND M.KODPARTNERA =:KODSKLADA
                    )
                    ORDER BY NOMKOD
                    INTO :NOM_KOD, :KODSKLADA, :DATADOC, :KODORGF, :CENAPRIX, :CENARASX, :TIPNDS, :TIPDOC, :STAVKANDS, :PERENOSKLADA, :KOLVO, :KODPARTNERA ;
                    IF (ROW_COUNT = 0) THEN BEGIN
                        C = C + 1;
                        FLAG = 3;
                    END
                    ELSE BEGIN
                        IF (TIPDOC = 1) THEN FLAG = 0;
                        ELSE FLAG = 1;
                        IF (LAST_KOLVO IS NULL) THEN LAST_KOLVO = KOLVO;
                        IF (LAST_DATE IS NULL) THEN LAST_DATE = DATADOC;
                        IF (FIRST_RASX IS NULL) THEN FIRST_RASX = CENARASX;
                    END 
                    IF (C>2) THEN FLAG = 0;
                    IF (PERENOS = 0 AND TIPDOC = 4) THEN PERENOS = KODSKLADA;
                END
                IF (CENAPRIX IS NOT NULL) THEN BEGIN
                    IF (COALESCE(TIPNDS, 2)= 1) THEN CENA_NDS = CAST(ROUND(CENAPRIX*(1 + STAVKANDS / 100), 5) AS NUMERIC (15, 5));
                    ELSE CENA_NDS = ROUND(CENAPRIX, 5);
                    IF (COALESCE(TIPNDS, 2)= 0) THEN CENA_BEZ_NDS = CAST(ROUND(CENAPRIX /(1 + STAVKANDS / 100), 5) AS NUMERIC (15, 5));
                    ELSE CENA_BEZ_NDS = ROUND(CENAPRIX, 5);
                END
                SUSPEND;
            END
            """;

        try {
            getDataSource().getConnection().createStatement().execute(SQL_CALL);
        }
        catch (SQLException ex) {
            Logger.getLogger(DataSourceFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void createProcedure3() {
        String SQL_CALL = """
            CREATE OR ALTER PROCEDURE CENA_REPORT3 (NOMKOD INTEGER) RETURNS 
            ( KOD_NOM INTEGER, NOM_KOD INTEGER, CENA_BEZ_NDS NUMERIC(15, 5), CENA_NDS NUMERIC(15, 5), PERENOS SMALLINT, FIRST_RASX NUMERIC(15, 2), 
            LAST_KOLVO NUMERIC(15, 2), LAST_DATE DATE ,KODPARTNERA INTEGER) AS
            DECLARE VARIABLE TIPDOC SMALLINT;
            DECLARE VARIABLE KODSKLADA SMALLINT;
            DECLARE VARIABLE PERENOSKLADA SMALLINT;
            DECLARE VARIABLE KODMAT VARCHAR(15);
            DECLARE VARIABLE DATADOC DATE;
            DECLARE VARIABLE KODORGF VARCHAR(15);
            DECLARE VARIABLE CENAPRIX NUMERIC(15, 5);
            DECLARE VARIABLE CENARASX NUMERIC(15, 2);
            DECLARE VARIABLE STAVKANDS NUMERIC(5, 2);
            DECLARE VARIABLE KOD INTEGER;
            DECLARE VARIABLE FLAG INTEGER;
            DECLARE VARIABLE C SMALLINT;
            DECLARE VARIABLE TIPNDS SMALLINT;
            DECLARE VARIABLE KOLVO NUMERIC(15, 3);
            BEGIN
                CENA_BEZ_NDS = NULL;
                CENA_NDS = NULL;
                CENAPRIX = NULL;
                TIPNDS = NULL;
                NOM_KOD = NOMKOD;
                PERENOS = 0;
                KOD_NOM = NOMKOD;
                LAST_KOLVO = NULL;
                LAST_DATE = NULL;
                FIRST_RASX = NULL;
                KODPARTNERA=0;
                SELECT M.KODSKLADA, M.DATADOC, C.KODORGF, M.KODMAT
                FROM MV M
                JOIN CENNIK C ON C.KOD = M.KODMAT
                WHERE NOMKOD =:NOMKOD
                INTO :KODSKLADA, :DATADOC, :KODORGF, :KODMAT;
                IF (ROW_COUNT = 0) THEN BEGIN
                    SUSPEND;
                    EXIT;
                END
                TIPDOC = 0;
                C = 0; 
                FLAG = 1;
                WHILE (FLAG <> 0) DO BEGIN
                    IF (FLAG = 3) THEN BEGIN
                        SELECT C.KODORGF
                        FROM MV M
                        JOIN CENNIK C ON C.KOD = M.KODMAT
                        WHERE NOMKOD =:NOM_KOD
                        INTO :KODMAT;
                        FLAG = 1;
                    END
                    SELECT FIRST 1 NOMKOD, KODSKLADA, DATADOC, KODORGF, CENAPRIX, CENARASX, TIPNDS, TIPDOC, STAVKANDS, PERENOS, KOLVO, KODPARTNERA
                    FROM (
                        SELECT M.NOMKOD, M.KODSKLADA, M.DATADOC, C.KODORGF, M.CENAPRIX, iIf(n.kodvida=1,M.CENARASX,c.pricereal3) as CENARASX, M2.TIPNDS, M.TIPDOC, M2.STAVKANDS, 0 AS PERENOS, M.KOLVO, M2.KODPARTNERA 
                        FROM MV M
                        JOIN CENNIK C ON C.KOD = M.KODMAT
                        JOIN name n on C.NAMEKOD=N.KOD
                        JOIN MVC M2 ON M2.KROSSNOM = M.KROSSNOMER
                        WHERE M.KODMAT =:KODMAT AND M.TIPDOC = 1 AND M.DATADOC <= DATADOC AND M.KODSKLADA =:KODSKLADA
                        UNION
                        SELECT M.NOMKOD, M.KODSKLADA, M.DATADOC, C.KODORGF, M.CENAPRIX, M.CENARASX, M2.TIPNDS, M.TIPDOC, M2.STAVKANDS, m.KODPARTNERA AS PERENOS, M.KOLVO, M2.KODPARTNERA 
                        FROM MV M
                        JOIN CENNIK C ON C.KOD = M.KODMAT
                        JOIN MVC M2 ON M2.KROSSNOM = M.KROSSNOMER
                        WHERE M.KODMAT =:KODMAT AND M.TIPDOC = 4 AND M.DATADOC <= DATADOC AND M.KODPARTNERA =:KODSKLADA
                    )
                    ORDER BY NOMKOD
                    INTO :NOM_KOD, :KODSKLADA, :DATADOC, :KODORGF, :CENAPRIX, :CENARASX, :TIPNDS, :TIPDOC, :STAVKANDS, :PERENOSKLADA, :KOLVO, :KODPARTNERA ;
                    IF (ROW_COUNT = 0) THEN BEGIN
                        C = C + 1;
                        FLAG = 3;
                    END
                    ELSE BEGIN
                        IF (TIPDOC = 1) THEN FLAG = 0;
                        ELSE FLAG = 1;
                        IF (LAST_KOLVO IS NULL) THEN LAST_KOLVO = KOLVO;
                        IF (LAST_DATE IS NULL) THEN LAST_DATE = DATADOC;
                        IF (FIRST_RASX IS NULL) THEN FIRST_RASX = CENARASX;
                    END
                    IF (C>2) THEN FLAG = 0;
                    IF (PERENOS = 0 AND TIPDOC = 4) THEN PERENOS = KODSKLADA;
                END
                IF (CENAPRIX IS NOT NULL) THEN BEGIN
                    IF (COALESCE(TIPNDS, 2)= 1) THEN CENA_NDS = CAST(ROUND(CENAPRIX*(1 + STAVKANDS / 100), 5) AS NUMERIC (15, 5));
                    ELSE CENA_NDS = ROUND(CENAPRIX, 5);
                    IF (COALESCE(TIPNDS, 2)= 0) THEN CENA_BEZ_NDS = CAST(ROUND(CENAPRIX /(1 + STAVKANDS / 100), 5) AS NUMERIC (15, 5));
                    ELSE CENA_BEZ_NDS = ROUND(CENAPRIX, 5);
                END
                SUSPEND;
            END
            """;

        try {
            getDataSource().getConnection().createStatement().execute(SQL_CALL);
        }
        catch (SQLException ex) {
            Logger.getLogger(DataSourceFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
