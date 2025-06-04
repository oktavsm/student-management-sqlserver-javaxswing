package temp;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.HashMap;

public class NilaiForm extends JFrame {
    JTextField tfNim = new JTextField();
    JComboBox<String> cbMk = new JComboBox<>();
    JTextField tfNilai = new JTextField();
    JButton btnSimpan = new JButton("Simpan");

    HashMap<String, String> mkMap = new HashMap<>(); // nama → kode

    public NilaiForm() {
        setTitle("Input Nilai");
        setLayout(new GridLayout(4, 2));
        add(new JLabel("NIM")); add(tfNim);
        add(new JLabel("Mata Kuliah")); add(cbMk);
        add(new JLabel("Nilai")); add(tfNilai);
        add(new JLabel("")); add(btnSimpan);
        setSize(300, 200); setVisible(true);

        loadMatakuliah();

        btnSimpan.addActionListener(e -> {
            try (Connection conn = DBConnection.getConnection()) {
                String kode = mkMap.get(cbMk.getSelectedItem().toString());
                String sql = "INSERT INTO nilai (nim, kode_mk, nilai) VALUES (?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, tfNim.getText());
                ps.setString(2, kode);
                ps.setFloat(3, Float.parseFloat(tfNilai.getText()));
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Nilai disimpan");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Gagal simpan");
            }
        });
    }

    void loadMatakuliah() {
        try (Connection conn = DBConnection.getConnection()) {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT kode_mk, nama_mk FROM matakuliah");
            while (rs.next()) {
                String nama = rs.getString("nama_mk");
                String kode = rs.getString("kode_mk");
                cbMk.addItem(nama);
                mkMap.put(nama, kode);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
