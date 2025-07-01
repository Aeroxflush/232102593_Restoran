/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Restoran;

import com.mysql.cj.jdbc.CallableStatement;
import java.awt.HeadlessException;
import java.time.*;
import java.sql.*;
import javax.swing.JOptionPane;

/**
 *
 * @author Gero
 */
public class TransaksiService {
    
    public static void simpan(
                          LocalDate tgl,
                          LocalTime jam,
                          String nama,
                          int total,
                          int bayar,
                          int kembali,
                          String catatan) throws SQLException {

        String sql = "{call simpanTransaksi(?,?,?,?,?,?,?)}";
    
        try (var con = Koneksi.getConnection(); CallableStatement cs = (CallableStatement) con.prepareCall(sql)) {

            cs.setDate(1,  java.sql.Date.valueOf(tgl));
            cs.setTime(2,  java.sql.Time.valueOf(jam));
            cs.setString(3, nama);
            cs.setInt(4,  total);
            cs.setInt(5,  bayar);
            cs.setInt(6,  kembali);
            cs.setString(7, catatan);

            cs.executeUpdate();
        } catch (SQLException e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    public static void update(
                          int id,
                          LocalDate tgl,
                          LocalTime jam,
                          String nama,
                          int total,
                          int bayar,
                          int kembali,
                          String catatan) throws SQLException {
        
        String sql = "{call updateTransaksi(?,?,?,?,?,?,?,?)}";
    
        try (var con = Koneksi.getConnection(); CallableStatement cs = (CallableStatement) con.prepareCall(sql)) {

            cs.setInt(1, id);
            cs.setDate(2, java.sql.Date.valueOf(tgl));
            cs.setTime(3, java.sql.Time.valueOf(jam));
            cs.setString(4, nama);
            cs.setInt(5, total);
            cs.setInt(6, bayar);
            cs.setInt(7, kembali);
            cs.setString(8, catatan);

            cs.executeUpdate();
        } catch (SQLException e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    public static void delete(int id) throws SQLException{
        String sql = "DELETE FROM " + Koneksi.TABEL_TRANSAKSI + " WHERE id_transaksi = ?";

        try (Connection con = Koneksi.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, id);
        ps.executeUpdate();
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
}
