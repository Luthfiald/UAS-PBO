/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mainapp1;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class MobilFrame extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtIDMobil, txtMerk, txtTahun, txtHarga;
    private JButton btnAdd, btnUpdate, btnDelete, btnRefresh;

    public MobilFrame() {
        setTitle("CRUD Data Mobil");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel lblIDMobil = new JLabel("ID Mobil:");
        JLabel lblMerk = new JLabel("Merk:");
        JLabel lblTahun = new JLabel("Tahun:");
        JLabel lblHarga = new JLabel("Harga:");

        txtIDMobil = new JTextField(20);
        txtMerk = new JTextField(20);
        txtTahun = new JTextField(20);
        txtHarga = new JTextField(20);

        btnAdd = new JButton("Tambah");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Hapus");
        btnRefresh = new JButton("Refresh");

        // Table
        tableModel = new DefaultTableModel(new String[]{"ID Mobil", "Merk", "Tahun", "Harga"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Layout dengan GroupLayout
        JPanel panelForm = new JPanel();
        GroupLayout layout = new GroupLayout(panelForm);
        panelForm.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(lblIDMobil)
                                .addComponent(lblMerk)
                                .addComponent(lblTahun)
                                .addComponent(lblHarga)
                                .addComponent(btnAdd))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(txtIDMobil)
                                .addComponent(txtMerk)
                                .addComponent(txtTahun)
                                .addComponent(txtHarga)
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(btnUpdate)
                                        .addComponent(btnDelete)
                                        .addComponent(btnRefresh)))
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lblIDMobil)
                                .addComponent(txtIDMobil))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lblMerk)
                                .addComponent(txtMerk))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lblTahun)
                                .addComponent(txtTahun))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lblHarga)
                                .addComponent(txtHarga))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(btnAdd)
                                .addComponent(btnUpdate)
                                .addComponent(btnDelete)
                                .addComponent(btnRefresh))
        );

        // Main layout
        setLayout(new BorderLayout());
        add(panelForm, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Load data saat frame dibuka
        loadData();

        // Event Handlers
        btnAdd.addActionListener(e -> addMobil());
        btnUpdate.addActionListener(e -> updateMobil());
        btnDelete.addActionListener(e -> deleteMobil());
        btnRefresh.addActionListener(e -> loadData());

        // Menambahkan listener untuk memilih baris di tabel
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    // Isi ID Mobil pada text field
                    txtIDMobil.setText(tableModel.getValueAt(selectedRow, 0).toString());
                    txtMerk.setText(tableModel.getValueAt(selectedRow, 1).toString());
                    txtTahun.setText(tableModel.getValueAt(selectedRow, 2).toString());
                    txtHarga.setText(tableModel.getValueAt(selectedRow, 3).toString());
                }
            }
        });
    }

    private void loadData() {
        tableModel.setRowCount(0);
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM mobil";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("idmobil"),
                        rs.getString("merk"),
                        rs.getInt("tahun"),
                        rs.getDouble("harga")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + ex.getMessage());
        }
    }

    private void addMobil() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO mobil (merk, tahun, harga) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, txtMerk.getText());
            stmt.setInt(2, Integer.parseInt(txtTahun.getText()));
            stmt.setDouble(3, Double.parseDouble(txtHarga.getText()));
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Data berhasil ditambahkan.");
            loadData();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error adding data: " + ex.getMessage());
        }
    }

    private void updateMobil() {
        // Ambil ID Mobil dari text field
        int idMobil = Integer.parseInt(txtIDMobil.getText());

        // Pastikan ID tidak kosong dan valid
        if (txtIDMobil.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "ID Mobil tidak boleh kosong.");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            // Query untuk mengupdate data berdasarkan ID
            String sql = "UPDATE mobil SET merk = ?, tahun = ?, harga = ? WHERE idmobil = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, txtMerk.getText());
            stmt.setInt(2, Integer.parseInt(txtTahun.getText()));
            stmt.setDouble(3, Double.parseDouble(txtHarga.getText()));
            stmt.setInt(4, idMobil);  // ID yang akan diupdate
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Data berhasil diupdate.");
            loadData(); // Reload data setelah update
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error updating data: " + ex.getMessage());
        }
    }

    private void deleteMobil() {
        // Ambil ID Mobil dari text field untuk menghapus
        int idMobil = Integer.parseInt(txtIDMobil.getText());

        // Pastikan ID tidak kosong dan valid
        if (txtIDMobil.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "ID Mobil tidak boleh kosong.");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            // Query untuk menghapus data berdasarkan ID
            String sql = "DELETE FROM mobil WHERE idmobil = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idMobil); // ID yang akan dihapus
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Data berhasil dihapus.");
            loadData(); // Reload data setelah delete
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error deleting data: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MobilFrame().setVisible(true));
    }
}
