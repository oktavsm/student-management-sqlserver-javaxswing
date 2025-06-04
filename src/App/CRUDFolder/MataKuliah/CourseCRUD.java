package App.CRUDFolder.MataKuliah;

import java.awt.CardLayout;
import java.awt.Color;
import javax.swing.*;
import javax.swing.table.*;

import App.ComboItem;

import java.sql.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;

/**
 *
 * @author Hype GLK
 */
public class CourseCRUD extends javax.swing.JPanel {

    private Connection conn;
    private CardLayout cl;
    private DefaultTableModel tableModel; 
    private DefaultComboBoxModel<ComboItem> courseComboBoxModel; 

    private javax.swing.JButton previousButton;

    public CourseCRUD(Connection conn) {
        this.conn = conn;
        initComponents(); 

        cl = (CardLayout) crudPanel.getLayout();

        
        tableModel = (DefaultTableModel) courseTable.getModel();
        
        
        
        
        if (courseComboBox.getModel() instanceof DefaultComboBoxModel) {
            courseComboBoxModel = (DefaultComboBoxModel<ComboItem>) courseComboBox.getModel();
        } else {
            courseComboBoxModel = new DefaultComboBoxModel<>();
            courseComboBox.setModel(courseComboBoxModel);
        }
        
        courseComboBox1.setModel(courseComboBoxModel);


        addFocusListenersToInsertFields();
        addItemListenersToCourseComboBoxes();
        
        loadData(); 

        setInsert(); 
    }

    private void addFocusListenersToInsertFields() {
        addPlaceholderFocusListener(insertCourseCode, "Kode Mata Kuliah");
        addPlaceholderFocusListener(insertCourseName, "Nama Mata Kuliah");
    }
    
    private void addItemListenersToCourseComboBoxes() {
        
        courseComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                if (evt.getStateChange() == ItemEvent.SELECTED) {
                    ComboItem selectedItem = (ComboItem) courseComboBox.getSelectedItem();
                    if (selectedItem != null) {
                        courseNameField.setText(selectedItem.getValue());
                    } else {
                        courseNameField.setText(""); 
                    }
                }
            }
        });

        
        courseComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                if (evt.getStateChange() == ItemEvent.SELECTED) {
                    ComboItem selectedItem = (ComboItem) courseComboBox1.getSelectedItem();
                    if (selectedItem != null) {
                        courseNameField1.setText(selectedItem.getValue());
                    } else {
                        courseNameField1.setText("");
                    }
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
    
    private void loadData() {
        tableModel.setRowCount(0); 
        courseComboBoxModel.removeAllElements(); 
       
        String sql = "SELECT kode_mk, nama_mk FROM matakuliah ORDER BY nama_mk"; 
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                String kodeMk = rs.getString("kode_mk");
                String namaMk = rs.getString("nama_mk");
                
                tableModel.addRow(new Object[]{kodeMk, namaMk});
                courseComboBoxModel.addElement(new ComboItem(kodeMk, namaMk));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal memuat data mata kuliah: " + e.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
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
        
        insertCourseCode.setText("Kode Mata Kuliah");
        insertCourseName.setText("Nama Mata Kuliah");
        addPlaceholderFocusListener(insertCourseCode, "Kode Mata Kuliah"); 
        addPlaceholderFocusListener(insertCourseName, "Nama Mata Kuliah"); 
        
        revalidate();
        repaint();
    }

    void setUpdate() {
        setButtonActive(toUpdatePanelButton);
        cl.show(crudPanel, "updateCard");
        if (courseComboBox.getItemCount() > 0) {
            courseComboBox.setSelectedIndex(0); 
            
        } else {
            courseNameField.setText(""); 
        }
        revalidate();
        repaint();
    }

    void setRemove() {
        setButtonActive(toRemovePanelButton);
        cl.show(crudPanel, "removeCard");
         if (courseComboBox1.getItemCount() > 0) {
            courseComboBox1.setSelectedIndex(0);
            
        } else {
            courseNameField1.setText("");
        }
        
        revalidate();
        repaint();
    }

    void insert(String code, String name) {
        
        String sql = "INSERT INTO matakuliah (kode_mk, nama_mk) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, code);
            pstmt.setString(2, name);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Mata kuliah berhasil ditambahkan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                loadData(); 
                
                
                insertCourseCode.setText("Kode Mata Kuliah");
                insertCourseName.setText("Nama Mata Kuliah");
                addPlaceholderFocusListener(insertCourseCode, "Kode Mata Kuliah");
                addPlaceholderFocusListener(insertCourseName, "Nama Mata Kuliah");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal menambahkan mata kuliah: " + e.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
        }
    }

    void update(String code, String name) {
        String sql = "UPDATE matakuliah SET nama_mk = ? WHERE kode_mk = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, code);
            int affectedRows = pstmt.executeUpdate();
             if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Mata kuliah berhasil diperbarui!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                loadData(); 
                
                 if (courseComboBox.getItemCount() > 0) {
                     
                     
                 } else {
                     courseNameField.setText("");
                 }

            } else {
                JOptionPane.showMessageDialog(this, "Mata kuliah dengan kode " + code + " tidak ditemukan.", "Gagal Update", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal memperbarui mata kuliah: " + e.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
        }
    }

    void remove(String code) {
        String sql = "DELETE FROM matakuliah WHERE kode_mk = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, code);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Mata kuliah berhasil dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                loadData(); 
                
                if (courseComboBox1.getItemCount() > 0) {
                    courseComboBox1.setSelectedIndex(0);
                } else {
                    courseNameField1.setText("");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Mata kuliah dengan kode " + code + " tidak ditemukan.", "Gagal Hapus", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException e) {
             if (e.getMessage().toLowerCase().contains("foreign key constraint") || 
                (e.getSQLState() != null && e.getSQLState().equals("23000"))) { 
                JOptionPane.showMessageDialog(this, "Gagal menghapus mata kuliah: Mata kuliah ini masih digunakan di tabel nilai.", "Error Relasi Data", JOptionPane.ERROR_MESSAGE);
            } else {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Gagal menghapus mata kuliah: " + e.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
            }
        }
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
        insertCourseCode = new javax.swing.JTextField();
        insertCourseName = new javax.swing.JTextField();
        insertButton = new javax.swing.JButton();
        updatePanel = new javax.swing.JPanel();
        updateButton = new javax.swing.JButton();
        courseComboBox = new javax.swing.JComboBox<ComboItem>();
        courseNameField = new javax.swing.JTextField();
        removePanel = new javax.swing.JPanel();
        removeCourseButton = new javax.swing.JButton();
        courseComboBox1 = new javax.swing.JComboBox<ComboItem>();
        courseNameField1 = new javax.swing.JTextField();
        toUpdatePanelButton = new javax.swing.JButton();
        toInsertPanelButton = new javax.swing.JButton();
        toRemovePanelButton = new javax.swing.JButton();
        scrollPanel = new javax.swing.JScrollPane();
        courseTable = new javax.swing.JTable();

        setBackground(new java.awt.Color(51, 51, 51));
        setMaximumSize(new java.awt.Dimension(625, 600));
        setMinimumSize(new java.awt.Dimension(625, 600));
        setPreferredSize(new java.awt.Dimension(625, 600));

        crudPanel.setPreferredSize(new java.awt.Dimension(607, 66));
        crudPanel.setLayout(new java.awt.CardLayout());

        insertPanel.setBackground(new java.awt.Color(51, 51, 51));

        insertCourseCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertCourseCodeActionPerformed(evt);
            }
        });

        insertButton.setText("Insert Course");
        insertButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                String code = insertCourseCode.getText();
                String name = insertCourseName.getText();
                if (code.equals("Kode Mata Kuliah") || name.equals("Nama Mata Kuliah") || code.isEmpty() || name.isEmpty()) {
                    JOptionPane.showMessageDialog(insertPanel, "Kode dan Nama Mata Kuliah tidak boleh kosong atau placeholder", "Error Input", JOptionPane.ERROR_MESSAGE);
                } else {
                    insert(code, name);
                }
            }
        });

        javax.swing.GroupLayout insertPanelLayout = new javax.swing.GroupLayout(insertPanel);
        insertPanel.setLayout(insertPanelLayout);
        insertPanelLayout.setHorizontalGroup(
            insertPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(insertPanelLayout.createSequentialGroup()
                .addComponent(insertCourseCode, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(insertCourseName, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE))
            .addComponent(insertButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        insertPanelLayout.setVerticalGroup(
            insertPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(insertPanelLayout.createSequentialGroup()
                .addGroup(insertPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(insertCourseCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(insertCourseName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(insertButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        crudPanel.add(insertPanel, "insertCard");

        updatePanel.setBackground(new java.awt.Color(51, 51, 51));
        updatePanel.setPreferredSize(new java.awt.Dimension(607, 66));

        updateButton.setText("Update Course");
        updateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ComboItem selectedItem = (ComboItem) courseComboBox.getSelectedItem();
                if (selectedItem != null) {
                    String code = selectedItem.getKey();
                    String name = courseNameField.getText();
                    if (name.isEmpty() || name.equals("Nama Mata Kuliah")) { 
                        JOptionPane.showMessageDialog(updatePanel, "Nama Mata Kuliah tidak boleh kosong", "Error Input", JOptionPane.ERROR_MESSAGE);
                    } else {
                        update(code, name);
                    }
                } else {
                    JOptionPane.showMessageDialog(updatePanel, "Pilih mata kuliah yang akan diupdate", "Error Seleksi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        
        
        courseComboBox.setPreferredSize(new java.awt.Dimension(72, 30));
        

        javax.swing.GroupLayout updatePanelLayout = new javax.swing.GroupLayout(updatePanel);
        updatePanel.setLayout(updatePanelLayout);
        updatePanelLayout.setHorizontalGroup(
            updatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(updatePanelLayout.createSequentialGroup()
                .addComponent(courseComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(courseNameField, javax.swing.GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE))
            .addComponent(updateButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        updatePanelLayout.setVerticalGroup(
            updatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(updatePanelLayout.createSequentialGroup()
                .addGroup(updatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(courseComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(courseNameField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(updateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        crudPanel.add(updatePanel, "updateCard");

        removePanel.setBackground(new java.awt.Color(51, 51, 51));
        removePanel.setPreferredSize(new java.awt.Dimension(607, 66));

        courseNameField1.setEditable(false);
        courseNameField1.setBackground(new java.awt.Color(204, 204, 204)); 

        removeCourseButton.setText("Remove Course");
        removeCourseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ComboItem selectedItem = (ComboItem) courseComboBox1.getSelectedItem();
                if (selectedItem != null) {
                    String code = selectedItem.getKey();
                     int confirm = JOptionPane.showConfirmDialog(removePanel, 
                        "Anda yakin ingin menghapus mata kuliah: " + selectedItem.getValue() + " (" + code + ")?",
                        "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        remove(code);
                    }
                } else {
                    JOptionPane.showMessageDialog(removePanel, "Pilih mata kuliah yang akan dihapus", "Error Seleksi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        
        
        courseComboBox1.setPreferredSize(new java.awt.Dimension(72, 30));
        

        javax.swing.GroupLayout removePanelLayout = new javax.swing.GroupLayout(removePanel);
        removePanel.setLayout(removePanelLayout);
        removePanelLayout.setHorizontalGroup(
            removePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(removePanelLayout.createSequentialGroup()
                .addComponent(courseComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(courseNameField1, javax.swing.GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE))
            .addComponent(removeCourseButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        removePanelLayout.setVerticalGroup(
            removePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(removePanelLayout.createSequentialGroup()
                .addGroup(removePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(courseComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(courseNameField1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(removeCourseButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
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
        toInsertPanelButton.setForeground(Color.WHITE);
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

        courseTable.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true));
        courseTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Kode MK", "Nama"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        
        courseTable.setGridColor(new java.awt.Color(153, 153, 153));
        courseTable.setPreferredSize(new java.awt.Dimension(150, 607));
        courseTable.setSelectionForeground(new java.awt.Color(153, 153, 153));
        courseTable.setShowGrid(true);
        scrollPanel.setViewportView(courseTable);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(crudPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(toInsertPanelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(toUpdatePanelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(toRemovePanelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(scrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 598, Short.MAX_VALUE))
                .addGap(15, 15, 15))
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

    private void insertCourseCodeActionPerformed(java.awt.event.ActionEvent evt) {
        
    }

    private void toInsertPanelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        setInsert();
    }

    private void toUpdatePanelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        setUpdate();
    }

    private void toRemovePanelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        setRemove();
    }

    
    private javax.swing.JComboBox<App.ComboItem> courseComboBox;
    private javax.swing.JComboBox<App.ComboItem> courseComboBox1;
    private javax.swing.JTextField courseNameField;
    private javax.swing.JTextField courseNameField1;
    private javax.swing.JTable courseTable;
    private javax.swing.JPanel crudPanel;
    private javax.swing.JButton insertButton;
    private javax.swing.JTextField insertCourseCode;
    private javax.swing.JTextField insertCourseName;
    private javax.swing.JPanel insertPanel;
    private javax.swing.JButton removeCourseButton;
    private javax.swing.JPanel removePanel;
    private javax.swing.JScrollPane scrollPanel;
    private javax.swing.JButton toInsertPanelButton;
    private javax.swing.JButton toRemovePanelButton;
    private javax.swing.JButton toUpdatePanelButton;
    private javax.swing.JButton updateButton;
    private javax.swing.JPanel updatePanel;
    
}
