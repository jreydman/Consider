export interface ICennik {
  KOD: string;
  NAME: string;
  PRICEPRIXOD: number;
  PRICEREAL1: number;
  PRICEREAL2: number;
  PRICEREAL3: number;
  IZMERENIE: string;
  UPAKOVKA: string;
  MINZAPAS: number;
  INCNDS: string;
  PROCINCNDS: number;
  DBEFOR: Date;
  KLASSKOD: number;
  GROUPKOD: number;
  NAMEKOD: number;
  DATEINBASE: Date;
  SERIA: string;
  PRICETAM: number;
  TAKECOMMON: string;
  LASTDATEPR: Date;
  LASTDATERS: Date;
  KODFPOST: number;
  ZAPRET: string;
  TOVARSH: string;
  KODKASSA: number;
  PRICEVALUTA: number;
  PRIVAZKA: string;
  PRICEZAVOD: number;
  TIPVALUT: string;
  PRICEREAL4: number;
  PRICEREAL5: number;
  PRICEREAL6: number;
  NOSALE: string;
  NOSALEDATE: null;
  KODCERTF: number;
  GRUP: null;
  NOMSERTF: string;
  DATESERTF: Date;
  NOMMONTHUC: number;
  UCENKAYES: string;
  NOMERPROT: string;
  DATEPROT: Date;
  KODPARENT: string;
  STRIHCOD: string;
  CPOSTNOMER: string;
  CPOSTDATE: Date;
  CNOMER: null;
  CENAFROMPRICE: null;
  AKCIAPRICE: number;
  KODORGF: string;
  VEDKODC: null;
  FORMAP: number;
  AKCIAUSE: number;
  TOVARMASTER: number;
}

export class Cennik {
    public KOD: string;
    public NAME: string;
    public PRICEPRIXOD: number;
    public PRICEREAL1: number;
    public PRICEREAL2: number;
    public PRICEREAL3: number;
    public IZMERENIE: string;
    public UPAKOVKA: string;
    public MINZAPAS: number;
    public INCNDS: string;
    public PROCINCNDS: number;
    public DBEFOR: Date;
    public KLASSKOD: number;
    public GROUPKOD: number;
    public NAMEKOD: number;
    public DATEINBASE: Date;
    public SERIA: string;
    public PRICETAM: number;
    public TAKECOMMON: string;
    public LASTDATEPR: Date;
    public LASTDATERS: Date;
    public KODFPOST: number;
    public ZAPRET: string;
    public TOVARSH: string;
    public KODKASSA: number;
    public PRICEVALUTA: number;
    public PRIVAZKA: string;
    public PRICEZAVOD: number;
    public TIPVALUT: string;
    public PRICEREAL4: number;
    public PRICEREAL5: number;
    public PRICEREAL6: number;
    public NOSALE: string;
    public NOSALEDATE: null;
    public KODCERTF: number;
    public GRUP: null;
    public NOMSERTF: string;
    public DATESERTF: Date;
    public NOMMONTHUC: number;
    public UCENKAYES: string;
    public NOMERPROT: string;
    public DATEPROT: Date;
    public KODPARENT: string;
    public STRIHCOD: string;
    public CPOSTNOMER: string;
    public CPOSTDATE: Date;
    public CNOMER: null;
    public CENAFROMPRICE: null;
    public AKCIAPRICE: number;
    public KODORGF: string;
    public VEDKODC: null;
    public FORMAP: number;
    public AKCIAUSE: number;
    public TOVARMASTER: number;
    constructor(value: Cennik) {
      this.KOD = value.KOD
      this.NAME = value.NAME
      this.PRICEPRIXOD = value.PRICEPRIXOD
      this.PRICEREAL1 = value.PRICEREAL1
      this.PRICEREAL2 = value.PRICEREAL2
      this.PRICEREAL3 = value.PRICEREAL3
      this.IZMERENIE = value.IZMERENIE
      this.UPAKOVKA = value.UPAKOVKA
      this.MINZAPAS = value.MINZAPAS
      this.INCNDS = value.INCNDS
      this.PROCINCNDS = value.PROCINCNDS
      this.DBEFOR = value.DBEFOR
      this.KLASSKOD = value.KLASSKOD
      this.GROUPKOD = value.GROUPKOD
      this.NAMEKOD = value.NAMEKOD
      this.DATEINBASE = value.DATEINBASE
      this.SERIA = value.SERIA
      this.PRICETAM = value.PRICETAM
      this.TAKECOMMON = value.TAKECOMMON
      this.LASTDATEPR = value.LASTDATEPR
      this.LASTDATERS = value.LASTDATERS
      this.KODFPOST = value.KODFPOST
      this.ZAPRET = value.ZAPRET
      this.TOVARSH = value.TOVARSH
      this.KODKASSA = value.KODKASSA
      this.PRICEVALUTA = value.PRICEVALUTA
      this.PRIVAZKA = value.PRIVAZKA
      this.PRICEZAVOD = value.PRICEZAVOD
      this.TIPVALUT = value.TIPVALUT
      this.PRICEREAL4 = value.PRICEREAL4
      this.PRICEREAL5 = value.PRICEREAL5
      this.PRICEREAL6 = value.PRICEREAL6
      this.NOSALE = value.NOSALE
      this.NOSALEDATE = value.NOSALEDATE
      this.KODCERTF = value.KODCERTF
      this.GRUP = value.GRUP
      this.NOMSERTF = value.NOMSERTF
      this.DATESERTF = value.DATESERTF
      this.NOMMONTHUC = value.NOMMONTHUC
      this.UCENKAYES = value.UCENKAYES
      this.NOMERPROT = value.NOMERPROT
      this.DATEPROT = value.DATEPROT
      this.KODPARENT = value.KODPARENT
      this.STRIHCOD = value.STRIHCOD
      this.CPOSTNOMER = value.CPOSTNOMER
      this.CPOSTDATE = value.CPOSTDATE
      this.CNOMER = value.CNOMER
      this.CENAFROMPRICE = value.CENAFROMPRICE
      this.AKCIAPRICE = value.AKCIAPRICE
      this.KODORGF = value.KODORGF
      this.VEDKODC = value.VEDKODC
      this.FORMAP = value.FORMAP
      this.AKCIAUSE = value.AKCIAUSE
      this.TOVARMASTER = value.TOVARMASTER
    }
}