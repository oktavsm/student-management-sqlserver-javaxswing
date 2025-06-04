package temp;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class MatakuliahForm extends JFrame {
    JTextField tfKode = new JTextField();
    JTextField tfNama = new JTextField();
    JButton btnSimpan = new JButton("Simpan");

    public MatakuliahForm() {
        setTitle("Mata Kuliah");
        setLayout(new GridLayout(3, 2));
        add(new JLabel("Kode MK")); add(tfKode);
        add(new JLabel("Nama MK")); add(tfNama);
        add(new JLabel("")); add(btnSimpan);
        setSize(300, 150); setVisible(true);

        btnSimpan.addActionListener(e -> {
            try (Connection conn = DBConnection.getConnection()) {
                String sql = "INSERT INTO matakuliah (kode_mk, nama_mk) VALUES (?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, tfKode.getText());
                ps.setString(2, tfNama.getText());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Data disimpan");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Gagal simpan");
            }
        });
    }
}
