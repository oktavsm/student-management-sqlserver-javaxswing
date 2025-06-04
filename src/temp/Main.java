package temp;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("CRUD Aplikasi");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(300, 200);
            JPanel panel = new JPanel();

            JButton btnMahasiswa = new JButton("Form Mahasiswa");
            JButton btnMatkul = new JButton("Form Matakuliah");
            JButton btnNilai = new JButton("Form Nilai");

            btnMahasiswa.addActionListener(e -> new MahasiswaForm());
            btnMatkul.addActionListener(e -> new MatakuliahForm());
            btnNilai.addActionListener(e -> new NilaiForm());

            panel.add(btnMahasiswa);
            panel.add(btnMatkul);
            panel.add(btnNilai);

            frame.add(panel);
            frame.setVisible(true);
        });
    }
}
