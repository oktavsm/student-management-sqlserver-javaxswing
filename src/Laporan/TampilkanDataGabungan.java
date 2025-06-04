package Laporan;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TampilkanDataGabungan {

   
    static String dbUrl = "jdbc:sqlserver://localhost:1433;databaseName=Bab13DB;integratedSecurity=true;encrypt=true;trustServerCertificate=true;";
    
    public static void main(String[] args) {
        TampilkanDataGabungan app = new TampilkanDataGabungan();
        Connection conn = null;

        try {

            conn = app.buatKoneksi();
            System.out.println("Koneksi ke database berhasil.\n");


            System.out.println("Menampilkan data gabungan mahasiswa, mata kuliah, dan nilai:");
            app.tampilkanData(conn);

        } catch (SQLException e) {
            System.err.println("Terjadi kesalahan SQL: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Driver JDBC tidak ditemukan: " + e.getMessage());
            e.printStackTrace();
        } finally {

            if (conn != null) {
                try {
                    conn.close();
                    System.out.println("\nKoneksi ke database ditutup.");
                } catch (SQLException e) {
                    System.err.println("Gagal menutup koneksi: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    
    public Connection buatKoneksi() throws SQLException, ClassNotFoundException {
       
        return DriverManager.getConnection(dbUrl);
    }

    
    public void tampilkanData(Connection conn) throws SQLException {
        String sql = "SELECT m.nim, m.nama, mk.nama_mk, n.nilai " +
                     "FROM mahasiswa m " +
                     "JOIN nilai n ON m.nim = n.nim " +
                     "JOIN matakuliah mk ON n.kode_mk = mk.kode_mk;";

        Statement st = null;
        ResultSet rs = null;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(sql);


            System.out.println("-----------------------------------------------------------------------------");
            System.out.printf("| %-15s | %-25s | %-20s | %-5s |\n", "NIM", "Nama Mahasiswa", "Mata Kuliah", "Nilai");
            System.out.println("-----------------------------------------------------------------------------");


            if (!rs.isBeforeFirst() ) {
                System.out.println("| Tidak ada data yang dapat ditampilkan.                                  |");
            } else {
                while (rs.next()) {
                    String nim = rs.getString("nim");
                    String nama = rs.getString("nama");
                    String mataKuliah = rs.getString("nama_mk");
                    float nilai = rs.getFloat("nilai");

                    System.out.printf("| %-15s | %-25s | %-20s | %-5.2f |\n", nim, nama, mataKuliah, nilai);
                }
            }
            System.out.println("-----------------------------------------------------------------------------");

        } finally {

            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
