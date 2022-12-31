package si.fri.rso.skupina1.analitika.lib;

public class Ponudba {

    private Integer ponudbaId;
    private Integer ponudnikId;
    private String ime;
    private Float cena;

    public Integer getPonudbaId() {
        return ponudbaId;
    }

    public void setPonudbaId(Integer ponudbaId) {
        this.ponudbaId = ponudbaId;
    }

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

    public Float getCena() {
        return cena;
    }

    public void setCena(Float cena) {
        this.cena = cena;
    }
}
