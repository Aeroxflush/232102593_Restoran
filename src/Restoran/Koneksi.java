/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Restoran;

import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Gero
 */
public class Koneksi {
    private static final String URL = "jdbc:mysql://localhost/db_restoran?serverTimezone=Asia/Jakarta";
    private static final String USER = "root";
    private static final String PASS = "";
    public static final String TABEL_TRANSAKSI  = "datatransaksi";
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
    
    public void tabel(JTable tabel){
        DefaultTableModel model = new DefaultTableModel();
        
        try {
            Connection con = DriverManager.getConnection(URL, USER, PASS);
            Statement stat = con.createStatement();
            ResultSet res = stat.executeQuery("SELECT * FROM " + TABEL_TRANSAKSI);

            java.sql.ResultSetMetaData meta = res.getMetaData();
            int columnCount = meta.getColumnCount();

            // kolom ngikutin jumlah kolom di DB(?)
            for (int i = 1; i <= columnCount; i++) {
                model.addColumn(meta.getColumnName(i));
            }

            // ngatur format jam & tanggal
            DateTimeFormatter fmtTanggalJam = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            DateTimeFormatter fmtJam        = DateTimeFormatter.ofPattern("HH:mm:ss");

            while (res.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    Object val = res.getObject(i);

                    // untuk cek kolom tanggal & jam
                    String colName = meta.getColumnLabel(i);

                    if ("tanggal".equalsIgnoreCase(colName)) {
                        // kolom tipe DATE
                        val = res.getTimestamp(i)
                                 .toLocalDateTime()
                                 .format(fmtTanggalJam);
                    } else if ("jam".equalsIgnoreCase(colName)) {
                        // kolom tipe TIME
                        val = res.getTime(i)
                                 .toLocalTime()
                                 .format(fmtJam);
                    }
                    rowData[i - 1] = val; // masukin ke array baris
                }
                model.addRow(rowData);
            }
            tabel.setModel(model);
            
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal load data: " + e);
        }
    }
    
    public static void pesanRefreshTabel(String teks) {
    // bikin pane & dialog
    JOptionPane pane = new JOptionPane(teks + "\nRefresh tabel...", JOptionPane.INFORMATION_MESSAGE);
    JDialog dlg = pane.createDialog("Cihuy");

    // timer tutup setelah 1 detik (1 000 ms)
    new javax.swing.Timer(1000, e -> dlg.dispose()).start();

    dlg.setVisible(true);  // tampil
}

}
