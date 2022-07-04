package frame;

import helpers.Koneksi;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PakaianInputFrame extends JFrame{
    private JPanel mainPanel;
    private JTextField idTextField;
    private JTextField merkTextField;
    private JTextField warnaTextField;
    private JTextField jenisTextField;
    private JButton batalButton;
    private JButton simpanButton;
    private int id;

    public void setId(int id){
        this.id = id;
    }

    public void isiKomponen(){
        Connection c = Koneksi.getConnection();
        String findSQL = "SELECT*FROM pakaian WHERE id = ?";
        PreparedStatement ps = null;
        try {
            ps = c.prepareStatement(findSQL);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                idTextField.setText(String.valueOf(rs.getInt("id")));
                merkTextField.setText(rs.getString("merk"));
                warnaTextField.setText(rs.getString("warna"));
                jenisTextField.setText(rs.getString("jenis"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public PakaianInputFrame(){
        batalButton.addActionListener(e -> {
            dispose();
        });

        simpanButton.addActionListener(e -> {
            String merk = merkTextField.getText();
            String warna = warnaTextField.getText();
            String jenis = jenisTextField.getText();
            if (merk.equals("")){
                JOptionPane.showMessageDialog(null,
                        "Isi nama Merk",
                        "Validasi data kosong",
                        JOptionPane.WARNING_MESSAGE);
                merkTextField.requestFocus();
            }
            Connection c = Koneksi.getConnection();
            PreparedStatement ps;
            try {
                if (id == 0){
                    String cekSQL = "SELECT*FROM pakaian WHERE merk = ?";
                    ps = c.prepareStatement(cekSQL);
                    ps.setString(1, merk);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()){
                        JOptionPane.showMessageDialog(null,
                                "Data sama sudah ada");
                    }else{
                        String insertSQL = "INSERT INTO pakaian VALUES (NULL, ?, ?, ?)";
                        ps = c.prepareStatement(insertSQL);
                        ps.setString(1, merk);
                        ps.setString(2, warna);
                        ps.setString(3, jenis);
                        ps.executeUpdate();
                        dispose();
                    }
                }else{
                    String cekSQL = "SELECT*FROM pakaian WHERE merk = ? AND id != ?";
                    ps = c.prepareStatement(cekSQL);
                    ps.setString(1, merk);
                    ps.setInt(2, id);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()){
                        JOptionPane.showMessageDialog(null,
                                "Data sama sudah ada");
                    }else{
                        String updateSQL = "UPDATE pakaian SET merk = ?, warna = ?, jenis = ? WHERE id = ?";
                        ps = c.prepareStatement(updateSQL);
                        ps.setString(1, merk);
                        ps.setString(2, warna);
                        ps.setString(3, jenis);
                        ps.setInt(4, id);
                        ps.executeUpdate();
                        dispose();
                    }

                }

            }catch (SQLException ex){
                throw new RuntimeException(ex);
            }
        });

        init();
    }

    public void init(){
        setContentPane(mainPanel);
        setTitle("Input Pakaian");
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }


}
