package temp;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class MahasiswaForm extends JFrame {
    JTextField tfNim = new JTextField();
    JTextField tfNama = new JTextField();
    JComboBox<String> cbJK = new JComboBox<>(new String[]{"L", "P"});
    JButton btnSimpan = new JButton("Simpan");

    public MahasiswaForm() {
        setTitle("Mahasiswa");
        setLayout(new GridLayout(4, 2));
        add(new JLabel("NIM")); add(tfNim);
        add(new JLabel("Nama")); add(tfNama);
        add(new JLabel("Jenis Kelamin")); add(cbJK);
        add(new JLabel("")); add(btnSimpan);
        setSize(300, 200); setVisible(true);

        btnSimpan.addActionListener(e -> {
            try (Connection conn = DBConnection.getConnection()) {
                String sql = "INSERT INTO mahasiswa (nim, nama, jenis_kelamin) VALUES (?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, tfNim.getText());
                ps.setString(2, tfNama.getText());
                ps.setString(3, cbJK.getSelectedItem().toString());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Data disimpan");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Gagal simpan");
            }
        });
    }
}
