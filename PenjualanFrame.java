/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mainapp1;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.awt.event.*;

public class PenjualanFrame extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<String> cmbPelanggan, cmbMobil;
    private JTextField txtTotalBiaya, txtQuantity;
    private JButton btnAdd, btnUpdate, btnDelete, btnRefresh, btnCalculate;

    public PenjualanFrame() {
        setTitle("CRUD Data Penjualan");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel lblPelanggan = new JLabel("Pelanggan:");
        JLabel lblMobil = new JLabel("Mobil:");
        JLabel lblQuantity = new JLabel("Quantity:");
        JLabel lblTotalBiaya = new JLabel("Total Biaya:");

        cmbPelanggan = new JComboBox<>();
        cmbMobil = new JComboBox<>();
        txtQuantity = new JTextField(20);
        txtTotalBiaya = new JTextField(20);
        txtTotalBiaya.setEditable(false);  // Total biaya tidak bisa diedit langsung

        btnAdd = new JButton("Tambah");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Hapus");
        btnRefresh = new JButton("Refresh");
        btnCalculate = new JButton("Hitung Total Biaya");

        // Table
        tableModel = new DefaultTableModel(new String[]{"ID Penjualan", "Pelanggan", "Mobil", "Quantity", "Total Biaya"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Layout
        JPanel panelForm = new JPanel();
        GroupLayout layout = new GroupLayout(panelForm);
        panelForm.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(lblPelanggan)
                                .addComponent(lblMobil)
                                .addComponent(lblQuantity)
                                .addComponent(lblTotalBiaya)
                                .addComponent(btnAdd))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(cmbPelanggan)
                                .addComponent(cmbMobil)
                                .addComponent(txtQuantity)
                                .addComponent(txtTotalBiaya)
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(btnUpdate)
                                        .addComponent(btnDelete)
                                        .addComponent(btnRefresh)
                                        .addComponent(btnCalculate)))  // Add the button here
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lblPelanggan)
                                .addComponent(cmbPelanggan))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lblMobil)
                                .addComponent(cmbMobil))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lblQuantity)
                                .addComponent(txtQuantity))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lblTotalBiaya)
                                .addComponent(txtTotalBiaya))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(btnAdd)
                                .addComponent(btnUpdate)
                                .addComponent(btnDelete)
                                .addComponent(btnRefresh)
                                .addComponent(btnCalculate))  // Add the button here
        );

        add(panelForm, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Event Listeners
        btnAdd.addActionListener(e -> addPenjualan());
        btnUpdate.addActionListener(e -> updatePenjualan());
        btnDelete.addActionListener(e -> deletePenjualan());
        btnRefresh.addActionListener(e -> loadData());
        btnCalculate.addActionListener(e -> calculateTotalBiaya());  // ActionListener for calculating total biaya

        // Load Data
        loadData();
        loadPelanggan();
        loadMobil();
    }

    // Method to calculate total biaya
    // Method untuk menghitung total biaya
// Method untuk menghitung total biaya
private void calculateTotalBiaya() {
    try {
        // Ambil quantity dari text field
        int quantity = Integer.parseInt(txtQuantity.getText());
        
        // Ambil nama mobil yang dipilih
        String selectedMobil = (String) cmbMobil.getSelectedItem();
        
        double totalBiaya = 0;

        // Ambil harga mobil dari database
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Query untuk mendapatkan harga mobil berdasarkan merk mobil
            String sqlMobil = "SELECT harga FROM mobil WHERE merk = ?";
            PreparedStatement stmtMobil = conn.prepareStatement(sqlMobil);
            stmtMobil.setString(1, selectedMobil);
            ResultSet rsMobil = stmtMobil.executeQuery();
            
            if (rsMobil.next()) {
                // Ambil harga mobil dari hasil query
                double hargaMobil = rsMobil.getDouble("harga");
                
                // Hitung total biaya berdasarkan quantity dan harga mobil
                totalBiaya = hargaMobil * quantity;
            }
        }

        // Set hasil total biaya ke text field
        txtTotalBiaya.setText(String.format("%.2f", totalBiaya));

    } catch (NumberFormatException ex) {
        // Jika quantity tidak valid
        JOptionPane.showMessageDialog(this, "Masukkan quantity yang valid.");
    } catch (SQLException ex) {
        // Jika ada error saat mengambil data dari database
        JOptionPane.showMessageDialog(this, "Error calculating total biaya: " + ex.getMessage());
    }
}


    // Load list pelanggan
    private void loadPelanggan() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT nama FROM pelanggan";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                cmbPelanggan.addItem(rs.getString("nama"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading pelanggan: " + ex.getMessage());
        }
    }

    // Load list mobil
    private void loadMobil() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT merk FROM mobil";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                cmbMobil.addItem(rs.getString("merk"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading mobil: " + ex.getMessage());
        }
    }

    // Load data penjualan ke tabel
    private void loadData() {
        tableModel.setRowCount(0);
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT penjualan.idpenjualan, pelanggan.nama, mobil.merk, penjualan.quantity, penjualan.totalbiaya " +
                    "FROM penjualan " +
                    "JOIN pelanggan ON penjualan.idpelanggan = pelanggan.idpelanggan " +
                    "JOIN mobil ON penjualan.idmobil = mobil.idmobil";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                tableModel.addRow(new Object[] {
                        rs.getInt("idpenjualan"),
                        rs.getString("nama"),
                        rs.getString("merk"),
                        rs.getInt("quantity"),
                        rs.getDouble("totalbiaya")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading data penjualan: " + ex.getMessage());
        }
    }

    // Tambah data penjualan
    private void addPenjualan() {
        String selectedPelanggan = (String) cmbPelanggan.getSelectedItem();
        String selectedMobil = (String) cmbMobil.getSelectedItem();
        int quantity = Integer.parseInt(txtQuantity.getText());
        double totalBiaya = Double.parseDouble(txtTotalBiaya.getText());

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sqlPelanggan = "SELECT idpelanggan FROM pelanggan WHERE nama = ?";
            PreparedStatement stmtPelanggan = conn.prepareStatement(sqlPelanggan);
            stmtPelanggan.setString(1, selectedPelanggan);
            ResultSet rsPelanggan = stmtPelanggan.executeQuery();
            rsPelanggan.next();
            int idPelanggan = rsPelanggan.getInt("idpelanggan");

            String sqlMobil = "SELECT idmobil FROM mobil WHERE merk = ?";
            PreparedStatement stmtMobil = conn.prepareStatement(sqlMobil);
            stmtMobil.setString(1, selectedMobil);
            ResultSet rsMobil = stmtMobil.executeQuery();
            rsMobil.next();
            int idMobil = rsMobil.getInt("idmobil");

            String sqlInsert = "INSERT INTO penjualan (idpelanggan, idmobil, quantity, totalbiaya) VALUES (?, ?, ?, ?)";
            PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert);
            stmtInsert.setInt(1, idPelanggan);
            stmtInsert.setInt(2, idMobil);
            stmtInsert.setInt(3, quantity);
            stmtInsert.setDouble(4, totalBiaya);
            stmtInsert.executeUpdate();

            JOptionPane.showMessageDialog(this, "Data penjualan berhasil ditambahkan.");
            loadData();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error adding data: " + ex.getMessage());
        }
    }

    // Update data penjualan
    private void updatePenjualan() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int idPenjualan = (Integer) tableModel.getValueAt(selectedRow, 0);
            String selectedPelanggan = (String) cmbPelanggan.getSelectedItem();
            String selectedMobil = (String) cmbMobil.getSelectedItem();
            int quantity = Integer.parseInt(txtQuantity.getText());
            double totalBiaya = Double.parseDouble(txtTotalBiaya.getText());

            try (Connection conn = DatabaseConnection.getConnection()) {
                String sqlUpdate = "UPDATE penjualan SET quantity = ?, totalbiaya = ? WHERE idpenjualan = ?";
                PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate);
                stmtUpdate.setInt(1, quantity);
                stmtUpdate.setDouble(2, totalBiaya);
                stmtUpdate.setInt(3, idPenjualan);
                stmtUpdate.executeUpdate();

                JOptionPane.showMessageDialog(this, "Data penjualan berhasil diupdate.");
                loadData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error updating data: " + ex.getMessage());
            }
        }
    }

    // Delete data penjualan
    private void deletePenjualan() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int idPenjualan = (Integer) tableModel.getValueAt(selectedRow, 0);
            try (Connection conn = DatabaseConnection.getConnection()) {
                String sqlDelete = "DELETE FROM penjualan WHERE idpenjualan = ?";
                PreparedStatement stmtDelete = conn.prepareStatement(sqlDelete);
                stmtDelete.setInt(1, idPenjualan);
                stmtDelete.executeUpdate();

                JOptionPane.showMessageDialog(this, "Data penjualan berhasil dihapus.");
                loadData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error deleting data: " + ex.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PenjualanFrame frame = new PenjualanFrame();
            frame.setVisible(true);
        });
    }
}
