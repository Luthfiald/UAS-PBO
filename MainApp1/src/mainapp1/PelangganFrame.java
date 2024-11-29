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

public class PelangganFrame extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtNama, txtNIK, txtNoTelp, txtAlamat;
    private JButton btnAdd, btnUpdate, btnDelete, btnRefresh;

    public PelangganFrame() {
        setTitle("CRUD Data Pelanggan");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel lblNama = new JLabel("Nama:");
        JLabel lblNIK = new JLabel("NIK:");
        JLabel lblNoTelp = new JLabel("No Telp:");
        JLabel lblAlamat = new JLabel("Alamat:");

        txtNama = new JTextField(20);
        txtNIK = new JTextField(20);
        txtNoTelp = new JTextField(20);
        txtAlamat = new JTextField(20);

        btnAdd = new JButton("Tambah");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Hapus");
        btnRefresh = new JButton("Refresh");

        // Table
        tableModel = new DefaultTableModel(new String[]{"ID", "Nama", "NIK", "No Telp", "Alamat"}, 0);
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
                                .addComponent(lblNama)
                                .addComponent(lblNIK)
                                .addComponent(lblNoTelp)
                                .addComponent(lblAlamat)
                                .addComponent(btnAdd))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(txtNama)
                                .addComponent(txtNIK)
                                .addComponent(txtNoTelp)
                                .addComponent(txtAlamat)
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(btnUpdate)
                                        .addComponent(btnDelete)
                                        .addComponent(btnRefresh)))
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lblNama)
                                .addComponent(txtNama))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lblNIK)
                                .addComponent(txtNIK))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lblNoTelp)
                                .addComponent(txtNoTelp))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lblAlamat)
                                .addComponent(txtAlamat))
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
        btnAdd.addActionListener(e -> addPelanggan());
        btnUpdate.addActionListener(e -> updatePelanggan());
        btnDelete.addActionListener(e -> deletePelanggan());
        btnRefresh.addActionListener(e -> loadData());
    }

    private void loadData() {
        tableModel.setRowCount(0);
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM pelanggan";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("idpelanggan"),
                        rs.getString("nama"),
                        rs.getString("nik"),
                        rs.getString("notelp"),
                        rs.getString("alamat")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + ex.getMessage());
        }
    }

    private void addPelanggan() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO pelanggan (nama, nik, notelp, alamat) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, txtNama.getText());
            stmt.setString(2, txtNIK.getText());
            stmt.setString(3, txtNoTelp.getText());
            stmt.setString(4, txtAlamat.getText());
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Data berhasil ditambahkan.");
            loadData();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error adding data: " + ex.getMessage());
        }
    }

    private void updatePelanggan() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih baris yang akan diupdate.");
            return;
        }
        int idPelanggan = (int) tableModel.getValueAt(selectedRow, 0);

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "UPDATE pelanggan SET nama = ?, nik = ?, notelp = ?, alamat = ? WHERE idpelanggan = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, txtNama.getText());
            stmt.setString(2, txtNIK.getText());
            stmt.setString(3, txtNoTelp.getText());
            stmt.setString(4, txtAlamat.getText());
            stmt.setInt(5, idPelanggan);

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data berhasil diupdate.");
            loadData();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error updating data: " + ex.getMessage());
        }
    }

    private void deletePelanggan() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih baris yang akan dihapus.");
            return;
        }
        int idPelanggan = (int) tableModel.getValueAt(selectedRow, 0);

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "DELETE FROM pelanggan WHERE idpelanggan = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idPelanggan);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Data berhasil dihapus.");
            loadData();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error deleting data: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PelangganFrame().setVisible(true));
    }
}
