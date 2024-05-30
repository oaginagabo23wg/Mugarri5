import java.sql.*;
import java.util.ArrayList;

public class DatuBaseKudeatzailea {
    static final String url = "jdbc:mysql://localhost/Mugarri4";
    static final String erab = "root";
    static final String pasahitza = "zubiri";
    private Connection conn;
    public DatuBaseKudeatzailea() {
        try {
            conn = DriverManager.getConnection(url, erab, pasahitza);
            System.out.println("Ongi konektatu da!");
        }
        catch (SQLException e){
            e.printStackTrace();
        }

    }
    public void itxi(ArrayList<Argazki> argazkis, Argazkilari[] argazkilaris) {
        try {
            for (Argazki argazki : argazkis){
                PreparedStatement statement = conn.prepareStatement("Update argazkiak set bistaratzeKop = ? where idArgazki = ?");
                statement.setInt(1, argazki.bistaratzeKop);
                statement.setInt(2, argazki.idArgazki);
                statement.executeUpdate();
            }
            for (Argazkilari argazkilari : argazkilaris){
                PreparedStatement statementArg =conn.prepareStatement("update argazkilari set saritua =? where idArgazkilari=?;");
                statementArg.setInt(1,argazkilari.getSaritua());
                statementArg.setInt(2,argazkilari.getIdArgazkilari());
                statementArg.executeUpdate();
            }
            conn.close();

            System.out.println("Datu-basetik deskonektatuta.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Argazkilari[] argazkilariakLortu(){

        try {

            String sql = "SELECT * FROM argazkilari;";
            Statement kontsulta = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = kontsulta.executeQuery(sql);
            int kont = 0;
            while (rs.next()){
                kont++;
            }
            Argazkilari[] argazkilariak = new Argazkilari[kont];
            rs.beforeFirst();
            int i=0;
            while (rs.next()){
                Argazkilari argazkilari = new Argazkilari(rs.getInt(1),rs.getString(2),rs.getInt(3));
                argazkilariak[i]=argazkilari;
                i++;
            }
            return argazkilariak;
        } catch (SQLException e) {
            System.out.println("Konektatzerakoan errorea: " + e.getMessage());
            return null;
        }
    }
    public ArrayList<Argazki> argazkiakLortu(){

        try {

            String sql = "SELECT * FROM argazkiak;";
            Statement kontsulta = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = kontsulta.executeQuery(sql);
            ArrayList<Argazki> argazkiak = new ArrayList<>();
            while (rs.next()){
                Argazki argazki = new Argazki(rs.getInt(1),rs.getString(2),rs.getDate(3), rs.getString(4),rs.getInt(5),rs.getInt(6) );
                argazkiak.add(argazki);
            }
            return argazkiak;
        } catch (SQLException e) {
            System.out.println("Konektatzerakoan errorea: " + e.getMessage());
            return null;
        }
    }
    public void argazkiaEzabatu(Argazki argazki){
        try {
            PreparedStatement kontsulta = conn.prepareStatement("DELETE FROM argazkiak WHERE idArgazki = ?");
            kontsulta.setInt(1, argazki.idArgazki);
            int a = kontsulta.executeUpdate();

            if (a > 0) {
                System.out.println("Argazkia ezabatu da.");
            } else {
                System.out.println("Ez da argazkia ezabatu.");
            }
        } catch (SQLException e){
            System.out.println("Errorea argazkia ezabatzean: " + e.getMessage());
        }
    }

    public int argazkilariArgKop(Argazkilari argazkilari){
        try {
            PreparedStatement statement = conn.prepareStatement("select count(*) from argazkiak where idArgazkilari=?",ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            statement.setInt(1, argazkilari.getIdArgazkilari());
            ResultSet rs = statement.executeQuery();
            return rs.getInt("idArgazki");
        }catch (SQLException e){
            e.printStackTrace();
        }
        return 1;
    }
    public void argazkilariaEzabatu(Argazkilari argazkilari){
        try {
            PreparedStatement statement = conn.prepareStatement("delete from argazkilari where idArgazkilari=?");
            statement.setInt(1, argazkilari.getIdArgazkilari());
            statement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
