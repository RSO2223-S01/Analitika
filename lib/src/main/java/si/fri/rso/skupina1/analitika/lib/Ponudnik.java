package si.fri.rso.skupina1.analitika.lib;

public class Ponudnik {

    private Integer ponudnikId;
    private String ime;
    private String mesto;
    private Long postnaSt;
    private String ulica;

    public Integer getPonudnikId() {
        return ponudnikId;
    }

    public void setPonudnikId(Integer ponudnikId) {
        this.ponudnikId = ponudnikId;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getMesto() {
        return mesto;
    }

    public void setMesto(String mesto) {
        this.mesto = mesto;
    }

    public Long getPostnaSt() {
        return postnaSt;
    }

    public void setPostnaSt(Long postnaSt) {
        this.postnaSt = postnaSt;
    }

    public String getUlica() {
        return ulica;
    }

    public void setUlica(String ulica) {
        this.ulica = ulica;
    }
}
