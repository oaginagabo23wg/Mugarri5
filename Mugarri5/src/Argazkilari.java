import java.sql.*;
public class Argazkilari {
    private int idArgazkilari;
    private String izena;
    private int saritua;

    public Argazkilari(int idArgazkilari, String izena, int saritua) {
        this.idArgazkilari = idArgazkilari;
        this.izena = izena;
        this.saritua = saritua;
    }

    @Override
    public String toString() {
        return this.izena;
    }

    public int getIdArgazkilari() {
        return idArgazkilari;
    }

    public String getIzena() {
        return izena;
    }

    public int getSaritua() {
        return saritua;
    }

    public void setSaritua(int saritua) {
        this.saritua = saritua;
    }
}
