package temp;
import java.sql.*;

public class Koneksi {
    public static Connection getConnection() {
        try {
            String url = "jdbc:sqlserver://localhost:1433;databaseName=Bab13DB;integratedSecurity=true;encrypt=true;trustServerCertificate=true;";

            Connection conn = DriverManager.getConnection(url);
            return conn;
        } catch (SQLException e) {
            System.out.println("Koneksi gagal: " + e.getMessage());
            return null;
        }
    }
}
