package App.CRUDFolder.Nilai;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import App.ComboItem;

public class NilaiPanel extends javax.swing.JPanel {

    private Connection conn;
    private CardLayout cl;
    private DefaultTableModel tableModel;

    
    private DefaultComboBoxModel<ComboItem> વિદ્યાર્થીInsertStudentModel; 
    private DefaultComboBoxModel<ComboItem> insertCourseModel;
    private DefaultComboBoxModel<ComboItem> updateStudentModel;
    private DefaultComboBoxModel<ComboItem> updateCourseModel;
    private DefaultComboBoxModel<ComboItem> removeStudentModel;
    private DefaultComboBoxModel<ComboItem> removeCourseModel;

    private javax.swing.JButton previousButton;

    public NilaiPanel(Connection conn) {
        this.conn = conn;
        initComponents(); 
        
        
        cl = (CardLayout) crudPanel.getLayout();

        
        tableModel = (DefaultTableModel) takesTable.getModel();

        
        વિદ્યાર્થીInsertStudentModel = new DefaultComboBoxModel<>();
        insertStudentComboBox.setModel(વિદ્યાર્થીInsertStudentModel);

        insertCourseModel = new DefaultComboBoxModel<>();
        insertCourseNameComboBox.setModel(insertCourseModel);

        updateStudentModel = new DefaultComboBoxModel<>();
        updateStudentNameComboBox.setModel(updateStudentModel);

        updateCourseModel = new DefaultComboBoxModel<>();
        updateCourseComboBox.setModel(updateCourseModel);

        removeStudentModel = new DefaultComboBoxModel<>();
        removeStudentNameComboBox.setModel(removeStudentModel);

        removeCourseModel = new DefaultComboBoxModel<>();
        removeCourseNameComboBox.setModel(removeCourseModel);
        
        loadInitialData();
        addFocusListenersToFields();
        addItemListenersToComboBoxes();

        setInsert(); 
    }

    private void loadInitialData() {
        loadTakesTable();
        loadInsertStudentComboBox();
        loadInsertCourseComboBox();
        loadUpdateRemoveStudentComboBox(); 
        
    }

    private void addFocusListenersToFields() {
        addPlaceholderFocusListener(insertScoreField, "Nilai (0-100)");
        addPlaceholderFocusListener(updateScoreField, "Nilai (0-100)");
        
    }
    
    private void addItemListenersToComboBoxes() {
        
        updateStudentNameComboBox.addItemListener(evt -> {
            if (evt.getStateChange() == ItemEvent.SELECTED) {
                ComboItem selectedStudent = (ComboItem) updateStudentNameComboBox.getSelectedItem();
                if (selectedStudent != null) {
                    loadCoursesForStudent(selectedStudent.getKey(), updateCourseModel, updateScoreField, true);
                } else {
                    updateCourseModel.removeAllElements();
                    updateScoreField.setText("Nilai (0-100)");
                     addPlaceholderFocusListener(updateScoreField, "Nilai (0-100)");
                }
            }
        });

        updateCourseComboBox.addItemListener(evt -> {
            if (evt.getStateChange() == ItemEvent.SELECTED) {
                ComboItem selectedStudent = (ComboItem) updateStudentNameComboBox.getSelectedItem();
                ComboItem selectedCourse = (ComboItem) updateCourseComboBox.getSelectedItem();
                if (selectedStudent != null && selectedCourse != null) {
                    loadScoreForStudentCourse(selectedStudent.getKey(), selectedCourse.getKey(), updateScoreField, true);
                } else if (selectedStudent != null && selectedCourse == null){ 
                     updateScoreField.setText("Nilai (0-100)");
                     addPlaceholderFocusListener(updateScoreField, "Nilai (0-100)");
                }
            }
        });

        
        removeStudentNameComboBox.addItemListener(evt -> {
            if (evt.getStateChange() == ItemEvent.SELECTED) {
                ComboItem selectedStudent = (ComboItem) removeStudentNameComboBox.getSelectedItem();
                if (selectedStudent != null) {
                    loadCoursesForStudent(selectedStudent.getKey(), removeCourseModel, removeScoreField, false);
                } else {
                    removeCourseModel.removeAllElements();
                    removeScoreField.setText(""); 
                }
            }
        });

        removeCourseNameComboBox.addItemListener(evt -> {
            if (evt.getStateChange() == ItemEvent.SELECTED) {
                ComboItem selectedStudent = (ComboItem) removeStudentNameComboBox.getSelectedItem();
                ComboItem selectedCourse = (ComboItem) removeCourseNameComboBox.getSelectedItem();
                if (selectedStudent != null && selectedCourse != null) {
                    loadScoreForStudentCourse(selectedStudent.getKey(), selectedCourse.getKey(), removeScoreField, false);
                } else if (selectedStudent != null && selectedCourse == null) {
                    removeScoreField.setText("");
                }
            }
        });
    }


    private void addPlaceholderFocusListener(JTextField textField, String placeholder) {
        Color originalForeground = textField.getForeground();
        Color placeholderForeground = Color.GRAY;

        if (textField.getText().isEmpty() || textField.getText().equals(placeholder)) {
            textField.setText(placeholder);
            textField.setForeground(placeholderForeground);
        }

        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(originalForeground);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText(placeholder);
                    textField.setForeground(placeholderForeground);
                }
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    
    private void initComponents() {

        crudPanel = new javax.swing.JPanel();
        insertPanel = new javax.swing.JPanel();
        insertButton = new javax.swing.JButton();
        insertScoreField = new javax.swing.JTextField();
        insertCourseNameComboBox = new javax.swing.JComboBox<ComboItem>();
        insertStudentComboBox = new javax.swing.JComboBox<ComboItem>();
        updatePanel = new javax.swing.JPanel();
        updateButton = new javax.swing.JButton();
        updateStudentNameComboBox = new javax.swing.JComboBox<ComboItem>();
        updateScoreField = new javax.swing.JTextField();
        updateCourseComboBox = new javax.swing.JComboBox<ComboItem>();
        removePanel = new javax.swing.JPanel();
        removeButton = new javax.swing.JButton();
        removeStudentNameComboBox = new javax.swing.JComboBox<ComboItem>();
        removeScoreField = new javax.swing.JTextField();
        removeCourseNameComboBox = new javax.swing.JComboBox<ComboItem>();
        toUpdatePanelButton = new javax.swing.JButton();
        toInsertPanelButton = new javax.swing.JButton();
        toRemovePanelButton = new javax.swing.JButton();
        scrollPanel = new javax.swing.JScrollPane();
        takesTable = new javax.swing.JTable();

        setBackground(new java.awt.Color(51, 51, 51));
        setMaximumSize(new java.awt.Dimension(625, 600));
        setMinimumSize(new java.awt.Dimension(625, 600));
        setPreferredSize(new java.awt.Dimension(625, 600));

        crudPanel.setLayout(new java.awt.CardLayout());

        insertPanel.setBackground(new java.awt.Color(51, 51, 51));

        insertButton.setText("Insert Nilai");
        insertButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertButtonActionPerformed(evt);
            }
        });

        insertScoreField.setText("Nilai (0-100)");
        insertScoreField.setPreferredSize(new java.awt.Dimension(180, 22));

        javax.swing.GroupLayout insertPanelLayout = new javax.swing.GroupLayout(insertPanel);
        insertPanel.setLayout(insertPanelLayout);
        insertPanelLayout.setHorizontalGroup(
            insertPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(insertButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(insertPanelLayout.createSequentialGroup()
                .addComponent(insertStudentComboBox, 0, 198, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(insertCourseNameComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(insertScoreField, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        insertPanelLayout.setVerticalGroup(
            insertPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(insertPanelLayout.createSequentialGroup()
                .addGroup(insertPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(insertScoreField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(insertCourseNameComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(insertStudentComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(insertButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        crudPanel.add(insertPanel, "insertCard");

        updatePanel.setBackground(new java.awt.Color(51, 51, 51));
        updatePanel.setPreferredSize(new java.awt.Dimension(607, 66));

        updateButton.setText("Update Nilai");
        updateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateButtonActionPerformed(evt);
            }
        });

        updateStudentNameComboBox.setPreferredSize(new java.awt.Dimension(72, 30));

        updateScoreField.setText("Nilai (0-100)");

        updateCourseComboBox.setPreferredSize(new java.awt.Dimension(72, 30));

        javax.swing.GroupLayout updatePanelLayout = new javax.swing.GroupLayout(updatePanel);
        updatePanel.setLayout(updatePanelLayout);
        updatePanelLayout.setHorizontalGroup(
            updatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(updatePanelLayout.createSequentialGroup()
                .addComponent(updateStudentNameComboBox, 0, 196, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(updateCourseComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(updateScoreField, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(updateButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        updatePanelLayout.setVerticalGroup(
            updatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(updatePanelLayout.createSequentialGroup()
                .addGroup(updatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(updateStudentNameComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(updateScoreField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(updateCourseComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(updateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        crudPanel.add(updatePanel, "updateCard");

        removePanel.setBackground(new java.awt.Color(51, 51, 51));
        removePanel.setPreferredSize(new java.awt.Dimension(607, 66));

        removeButton.setText("Remove Nilai");
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeButtonActionPerformed(evt);
            }
        });

        removeStudentNameComboBox.setPreferredSize(new java.awt.Dimension(72, 30));

        removeScoreField.setEditable(false);
        removeScoreField.setBackground(new java.awt.Color(204, 204, 204));
        removeScoreField.setText("Nilai");

        removeCourseNameComboBox.setPreferredSize(new java.awt.Dimension(72, 30));

        javax.swing.GroupLayout removePanelLayout = new javax.swing.GroupLayout(removePanel);
        removePanel.setLayout(removePanelLayout);
        removePanelLayout.setHorizontalGroup(
            removePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(removePanelLayout.createSequentialGroup()
                .addComponent(removeStudentNameComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(removeCourseNameComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(removeScoreField, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE))
            .addComponent(removeButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        removePanelLayout.setVerticalGroup(
            removePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(removePanelLayout.createSequentialGroup()
                .addGroup(removePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(removeStudentNameComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(removeScoreField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(removeCourseNameComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(removeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        crudPanel.add(removePanel, "removeCard");

        toUpdatePanelButton.setText("Update Data");
        toUpdatePanelButton.setPreferredSize(new java.awt.Dimension(180, 30));
        toUpdatePanelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toUpdatePanelButtonActionPerformed(evt);
            }
        });

        toInsertPanelButton.setBackground(new java.awt.Color(51, 51, 51));
        toInsertPanelButton.setForeground(java.awt.Color.white);
        toInsertPanelButton.setText("Insert Data");
        toInsertPanelButton.setPreferredSize(new java.awt.Dimension(180, 30));
        toInsertPanelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toInsertPanelButtonActionPerformed(evt);
            }
        });

        toRemovePanelButton.setText("Remove Data");
        toRemovePanelButton.setPreferredSize(new java.awt.Dimension(180, 30));
        toRemovePanelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toRemovePanelButtonActionPerformed(evt);
            }
        });

        takesTable.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true));
        takesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nama Mahasiswa", "Mata Kuliah", "Nilai"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        takesTable.setGridColor(new java.awt.Color(153, 153, 153));
        takesTable.setShowGrid(true);
        scrollPanel.setViewportView(takesTable);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(13, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(crudPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(toInsertPanelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(toUpdatePanelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(toRemovePanelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(scrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 598, Short.MAX_VALUE))
                .addGap(14, 14, 14))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(toInsertPanelButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(toUpdatePanelButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(toRemovePanelButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(crudPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
                .addContainerGap())
        );
    }

    private void toUpdatePanelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        setUpdate();
    }

    private void toInsertPanelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        setInsert();
    }

    private void toRemovePanelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        setRemove();
    }

    private void insertButtonActionPerformed(java.awt.event.ActionEvent evt) {
        ComboItem selectedStudent = (ComboItem) insertStudentComboBox.getSelectedItem();
        ComboItem selectedCourse = (ComboItem) insertCourseNameComboBox.getSelectedItem();
        String nilaiStr = insertScoreField.getText();

        if (selectedStudent == null) {
            JOptionPane.showMessageDialog(this, "Pilih mahasiswa terlebih dahulu.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (selectedCourse == null) {
            JOptionPane.showMessageDialog(this, "Pilih mata kuliah terlebih dahulu.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (nilaiStr.isEmpty() || nilaiStr.equals("Nilai (0-100)")) {
            JOptionPane.showMessageDialog(this, "Nilai tidak boleh kosong.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        float nilai;
        try {
            nilai = Float.parseFloat(nilaiStr);
            if (nilai < 0 || nilai > 100) {
                JOptionPane.showMessageDialog(this, "Nilai harus antara 0 dan 100.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Nilai harus berupa angka.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        insertNilai(selectedStudent.getKey(), selectedCourse.getKey(), nilai);
    }

    private void updateButtonActionPerformed(java.awt.event.ActionEvent evt) {
        ComboItem selectedStudent = (ComboItem) updateStudentNameComboBox.getSelectedItem();
        ComboItem selectedCourse = (ComboItem) updateCourseComboBox.getSelectedItem();
        String nilaiStr = updateScoreField.getText();

        if (selectedStudent == null) {
            JOptionPane.showMessageDialog(this, "Pilih mahasiswa untuk update.", "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (selectedCourse == null) {
            JOptionPane.showMessageDialog(this, "Pilih mata kuliah untuk update.", "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
         if (nilaiStr.isEmpty() || nilaiStr.equals("Nilai (0-100)")) {
            JOptionPane.showMessageDialog(this, "Nilai tidak boleh kosong.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        float nilai;
        try {
            nilai = Float.parseFloat(nilaiStr);
            if (nilai < 0 || nilai > 100) {
                JOptionPane.showMessageDialog(this, "Nilai harus antara 0 dan 100.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Nilai harus berupa angka.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        updateNilai(selectedStudent.getKey(), selectedCourse.getKey(), nilai);
    }

    private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {
        ComboItem selectedStudent = (ComboItem) removeStudentNameComboBox.getSelectedItem();
        ComboItem selectedCourse = (ComboItem) removeCourseNameComboBox.getSelectedItem();

        if (selectedStudent == null) {
            JOptionPane.showMessageDialog(this, "Pilih mahasiswa untuk menghapus nilai.", "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (selectedCourse == null) {
            JOptionPane.showMessageDialog(this, "Pilih mata kuliah untuk menghapus nilai.", "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
                "Anda yakin ingin menghapus nilai mata kuliah '" + selectedCourse.getValue() + "' untuk mahasiswa '" + selectedStudent.getValue() + "'?",
                "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            removeNilai(selectedStudent.getKey(), selectedCourse.getKey());
        }
    }
    
    void setButtonActive(JButton currentActiveButton) {
        JButton[] buttons = {toInsertPanelButton, toUpdatePanelButton, toRemovePanelButton};
        for (JButton button : buttons) {
            if (button == currentActiveButton) {
                button.setBackground(new Color(51, 51, 51)); 
                button.setForeground(Color.WHITE); 
            } else {
                button.setBackground(new Color(78, 80, 82)); 
                button.setForeground(Color.BLACK); 
            }
        }
        previousButton = currentActiveButton;
    }

    void setInsert() {
        setButtonActive(toInsertPanelButton);
        cl.show(crudPanel, "insertCard");
        insertStudentComboBox.setSelectedIndex(insertStudentComboBox.getItemCount() > 0 ? 0 : -1);
        insertCourseNameComboBox.setSelectedIndex(insertCourseNameComboBox.getItemCount() > 0 ? 0 : -1);
        insertScoreField.setText("Nilai (0-100)");
        addPlaceholderFocusListener(insertScoreField, "Nilai (0-100)");
        revalidate();
        repaint();
    }

    void setUpdate() {
        setButtonActive(toUpdatePanelButton);
        cl.show(crudPanel, "updateCard");
        if (updateStudentNameComboBox.getItemCount() > 0) {
            updateStudentNameComboBox.setSelectedIndex(0); 
            
        } else {
            updateCourseModel.removeAllElements();
            updateScoreField.setText("Nilai (0-100)");
            addPlaceholderFocusListener(updateScoreField, "Nilai (0-100)");
        }
        revalidate();
        repaint();
    }

    void setRemove() {
        setButtonActive(toRemovePanelButton);
        cl.show(crudPanel, "removeCard");
         if (removeStudentNameComboBox.getItemCount() > 0) {
            removeStudentNameComboBox.setSelectedIndex(0);
            
        } else {
            removeCourseModel.removeAllElements();
            removeScoreField.setText("");
        }
        revalidate();
        repaint();
    }

    private void loadTakesTable() {
        tableModel.setRowCount(0); 
        String sql = "SELECT m.nama AS nama_mahasiswa, mk.nama_mk, n.nilai " +
                     "FROM nilai n " +
                     "JOIN mahasiswa m ON n.nim = m.nim " +
                     "JOIN matakuliah mk ON n.kode_mk = mk.kode_mk";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getString("nama_mahasiswa"),
                    rs.getString("nama_mk"),
                    rs.getFloat("nilai")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal memuat data nilai: " + e.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadInsertStudentComboBox() {
        વિદ્યાર્થીInsertStudentModel.removeAllElements();
        String sql = "SELECT nim, nama FROM mahasiswa ORDER BY nama";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                વિદ્યાર્થીInsertStudentModel.addElement(new ComboItem(rs.getString("nim"), rs.getString("nama")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal memuat data mahasiswa: " + e.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadInsertCourseComboBox() {
        insertCourseModel.removeAllElements();
        String sql = "SELECT kode_mk, nama_mk FROM matakuliah ORDER BY nama_mk";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                insertCourseModel.addElement(new ComboItem(rs.getString("kode_mk"), rs.getString("nama_mk")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal memuat data mata kuliah: " + e.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadUpdateRemoveStudentComboBox() {
        updateStudentModel.removeAllElements();
        removeStudentModel.removeAllElements();
        String sql = "SELECT DISTINCT m.nim, m.nama FROM nilai n JOIN mahasiswa m ON n.nim = m.nim ORDER BY m.nama";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ComboItem item = new ComboItem(rs.getString("nim"), rs.getString("nama"));
                updateStudentModel.addElement(item);
                removeStudentModel.addElement(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal memuat mahasiswa untuk update/remove: " + e.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadCoursesForStudent(String nim, DefaultComboBoxModel<ComboItem> courseModel, JTextField scoreField, boolean isUpdate) {
        courseModel.removeAllElements();
        if (isUpdate) {
            scoreField.setText("Nilai (0-100)"); 
            addPlaceholderFocusListener(scoreField, "Nilai (0-100)");
        } else {
            scoreField.setText(""); 
        }


        String sql = "SELECT mk.kode_mk, mk.nama_mk " +
                     "FROM nilai n " +
                     "JOIN matakuliah mk ON n.kode_mk = mk.kode_mk " +
                     "WHERE n.nim = ? ORDER BY mk.nama_mk";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nim);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    courseModel.addElement(new ComboItem(rs.getString("kode_mk"), rs.getString("nama_mk")));
                }
                if (courseModel.getSize() > 0) {
                    
                     if (courseModel.getElementAt(0) instanceof ComboItem) { 
                        ComboItem firstCourse = (ComboItem) courseModel.getElementAt(0);
                        loadScoreForStudentCourse(nim, firstCourse.getKey(), scoreField, isUpdate);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal memuat mata kuliah untuk mahasiswa: " + e.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadScoreForStudentCourse(String nim, String kodeMk, JTextField scoreField, boolean isUpdate) {
        String sql = "SELECT nilai FROM nilai WHERE nim = ? AND kode_mk = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nim);
            pstmt.setString(2, kodeMk);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    scoreField.setText(String.valueOf(rs.getFloat("nilai")));
                    
                    scoreField.setForeground(Color.BLACK); 
                    for(FocusListener fl : scoreField.getFocusListeners()){
                        if(fl instanceof FocusAdapter){ 
                           
                        }
                    }
                } else {
                     if (isUpdate) {
                        scoreField.setText("Nilai (0-100)");
                        addPlaceholderFocusListener(scoreField, "Nilai (0-100)");
                    } else {
                        scoreField.setText(""); 
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal memuat nilai: " + e.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void insertNilai(String nim, String kodeMk, float nilai) {
        String sql = "INSERT INTO nilai (nim, kode_mk, nilai) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nim);
            pstmt.setString(2, kodeMk);
            pstmt.setFloat(3, nilai);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Nilai berhasil ditambahkan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                loadTakesTable();
                loadUpdateRemoveStudentComboBox(); 
                
                insertStudentComboBox.setSelectedIndex(insertStudentComboBox.getItemCount() > 0 ? 0 : -1);
                insertCourseNameComboBox.setSelectedIndex(insertCourseNameComboBox.getItemCount() > 0 ? 0 : -1);
                insertScoreField.setText("Nilai (0-100)");
                addPlaceholderFocusListener(insertScoreField, "Nilai (0-100)");
            }
        } catch (SQLException e) {
            if (e.getMessage().toLowerCase().contains("primary key constraint") || 
                (e.getSQLState() != null && (e.getSQLState().equals("23000") || e.getSQLState().equals("23505")) )) { 
                 JOptionPane.showMessageDialog(this, "Gagal menambahkan nilai: Mahasiswa ini sudah memiliki nilai untuk mata kuliah tersebut.", "Error Duplikasi", JOptionPane.ERROR_MESSAGE);
            } else {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Gagal menambahkan nilai: " + e.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void updateNilai(String nim, String kodeMk, float nilai) {
        String sql = "UPDATE nilai SET nilai = ? WHERE nim = ? AND kode_mk = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setFloat(1, nilai);
            pstmt.setString(2, nim);
            pstmt.setString(3, kodeMk);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Nilai berhasil diperbarui!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                loadTakesTable();
            } else {
                 JOptionPane.showMessageDialog(this, "Tidak ada data nilai yang cocok untuk diperbarui.", "Gagal Update", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal memperbarui nilai: " + e.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeNilai(String nim, String kodeMk) {
        String sql = "DELETE FROM nilai WHERE nim = ? AND kode_mk = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nim);
            pstmt.setString(2, kodeMk);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Nilai berhasil dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                loadTakesTable();
                loadUpdateRemoveStudentComboBox(); 
                
                if (removeStudentNameComboBox.getItemCount() > 0) {
                    removeStudentNameComboBox.setSelectedIndex(0); 
                } else {
                    removeCourseModel.removeAllElements();
                    removeScoreField.setText("");
                }
            } else {
                 JOptionPane.showMessageDialog(this, "Tidak ada data nilai yang cocok untuk dihapus.", "Gagal Hapus", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal menghapus nilai: " + e.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    private javax.swing.JComboBox<ComboItem> insertCourseNameComboBox;
    private javax.swing.JPanel crudPanel;
    private javax.swing.JButton insertButton;
    private javax.swing.JPanel insertPanel;
    private javax.swing.JTextField insertScoreField;
    private javax.swing.JComboBox<ComboItem> insertStudentComboBox;
    private javax.swing.JButton removeButton;
    private javax.swing.JComboBox<ComboItem> removeCourseNameComboBox;
    private javax.swing.JPanel removePanel;
    private javax.swing.JTextField removeScoreField;
    private javax.swing.JComboBox<ComboItem> removeStudentNameComboBox;
    private javax.swing.JScrollPane scrollPanel;
    private javax.swing.JTable takesTable;
    private javax.swing.JButton toInsertPanelButton;
    private javax.swing.JButton toRemovePanelButton;
    private javax.swing.JButton toUpdatePanelButton;
    private javax.swing.JButton updateButton;
    private javax.swing.JComboBox<ComboItem> updateCourseComboBox;
    private javax.swing.JPanel updatePanel;
    private javax.swing.JTextField updateScoreField;
    private javax.swing.JComboBox<ComboItem> updateStudentNameComboBox;
    
}
