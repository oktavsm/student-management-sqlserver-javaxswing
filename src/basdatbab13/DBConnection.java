package basdatbab13;
import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    public static Connection getConnection() {
        try {
            String url = "jdbc:sqlserver://localhost:1433;databaseName=Bab13DB;integratedSecurity=true;encrypt=true;trustServerCertificate=true;";
            return DriverManager.getConnection(url);
        } catch (Exception e) {
            System.out.println("Koneksi gagal: " + e.getMessage());
            return null;
        }
    }
}
