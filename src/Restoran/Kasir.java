/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Restoran;

import com.formdev.flatlaf.FlatLightLaf;
import java.awt.HeadlessException;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.Date;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Locale;
import javax.swing.JFrame;
import javax.swing.JSpinner;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Mahasiswa
 */
public class Kasir extends javax.swing.JFrame {
    
    private int i;
    private JTextField[] hargaFields, daftarFields, totalFields;
    private JSpinner[] jumlahFields;
    int hargaSeblak1, hargaSeblak2, hargaSeblak3, hargaPaket1, hargaPaket2;
    int totalTransaksi, kembalian;
    String namaMenu;
    
    /**
     * Creates new form Kasir
     */
    public Kasir() {
        initComponents();
        formatUntukTxtBayar();
        aturGambar();
        konfirmasiSebelumExit();
        Waktu waktu = new Waktu((txtjam));
        
        hargaFields = new JTextField[] {hargaDaftar1, hargaDaftar2, hargaDaftar3, hargaDaftar4};
        daftarFields = new JTextField[] {daftar1, daftar2, daftar3, daftar4};
        jumlahFields = new JSpinner[] {jmlhDaftar1, jmlhDaftar2, jmlhDaftar3, jmlhDaftar4};
        totalFields = new JTextField[] {totalDaftar1, totalDaftar2, totalDaftar3, totalDaftar4};
        
        /*  ChangeListener buat JSpinner*/
        for (i = 0; i < jumlahFields.length; i++) {
            final int row = i;
            jumlahFields[row].addChangeListener(e -> updateSubtotalDanTotal(row));
        }
        
        hargaSeblak1 = 28000;
        hargaSeblak2 = 32000;
        hargaSeblak3 = 35000;
        hargaPaket1 = 25000;
        hargaPaket2 = 28000;
    }
    
    private void kosongkan(){
        for (JTextField Teks : hargaFields) Teks.setText(null);
        for (JTextField Teks : daftarFields) Teks.setText(null);
        for (JTextField Teks : totalFields) Teks.setText(null);
        for (JSpinner Teks : jumlahFields) Teks.setValue(0);
        
        txtTtlTransaksi.setText(null);
        totalTransaksi = 0;
        for (JSpinner sp : jumlahFields) sp.setValue(0);
        txtBayar.setText(null);
        txtKembalian.setText(null);
    }
    
    private void tambahMenu(String namaMenu){
        //  ngecek JTextField daftar, kalo ada
        for (i = 0; i < daftarFields.length; i++){
            if (namaMenu.equals(daftarFields[i].getText())){
                int jumlah = (int) jumlahFields[i].getValue();
                jumlahFields[i].setValue(jumlah + 1);
                return;
            }
        }
        
        //  Kalo JTextField daftar, kosong
        for (i = 0; i < daftarFields.length; i++){
            if (daftarFields[i].getText().isEmpty()){
                    daftarFields[i].setText(namaMenu);
                    
                    int harga = switch (namaMenu) {
                        case "Chat Terakhir" -> hargaSeblak1;
                        case "Move On Gagal" -> hargaSeblak2;
                        case "Seen doang" -> hargaSeblak3;
                        case "Paket 1" -> hargaPaket1;
                        case "Paket 2" -> hargaPaket2;
                        default -> 0;
                    };
                    hargaFields[i].setText(String.valueOf(harga));
                    jumlahFields[i].setValue(1);
                    return;
            }
        }
        // Kalo full
        JOptionPane.showMessageDialog(this, "Daftar Penuh silahkan reset dengan klik tombol batal", "Info", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void hitungTotal(){
        totalTransaksi = 0;
        for (JTextField totalField : totalFields) {
            if (!totalField.getText().isEmpty()) {
                totalTransaksi += Integer.parseInt(totalField.getText());
            }
        }
        txtTtlTransaksi.setText(formatAngka(totalTransaksi));
    }
    
    private int bayarDanKembalian(){
        if (txtBayar.getText().isEmpty() || txtBayar.getText().isBlank()){
            JOptionPane.showMessageDialog(this, "Silahkan masukkan nominal!", "Info", JOptionPane.INFORMATION_MESSAGE);
            return 0;
        }
        
        double total = Double.parseDouble(txtBayar.getText().replace(".", ""));
        kembalian = (int) (total - totalTransaksi);
        txtKembalian.setText(String.valueOf(formatAngka(kembalian))); 
        return kembalian;
    }
    
    private void aturGambar(){
        String[] paths = {
            "/Restoran/img/seblak_chat_terakhir.png",
            "/Restoran/img/seblak_move_on_gagal.png",
            "/Restoran/img/seblak_seen_doang.png",
            "/Restoran/img/seblak_paket_1.png",
            "/Restoran/img/seblak_paket_2.png",
        };
        
        JButton[] tombol2 = {
            seblak1,
            seblak2,
            seblak3,
        };
        
        int targetWidth = 207;
        int targetHeight = 120;
        
        for (int im = 0; im < tombol2.length; im++) {
            java.net.URL imgURL = getClass().getResource(paths[im]);
            if (imgURL != null) {
                ImageIcon icon = new ImageIcon(imgURL);
                Image img = icon.getImage();
                Image newImg = img.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
                tombol2[im].setIcon(new ImageIcon(newImg));
            } else {
                System.err.println("Gagal load: " + paths[im]);
            }
        }
    }
    
    private String formatAngka(double angka) {
        NumberFormat nf = NumberFormat.getInstance(new Locale("id", "ID"));
        return nf.format(angka);
    }
    
    private void formatUntukTxtBayar() {
        txtBayar.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                try {
                    double angka = Double.parseDouble(txtBayar.getText().replaceAll("[^\\d]", ""));
                    txtBayar.setText(formatAngka(angka));
                } catch (NumberFormatException ex) {
                    txtBayar.setText("");
                }
            }
        });
    }
    
    private void updateSubtotalDanTotal(int row) {
        if (hargaFields[row].getText().isEmpty()) return;
        
        int harga   = Integer.parseInt(hargaFields[row].getText());
        int jumlah  = (int) jumlahFields[row].getValue();
        int subTot  = harga * jumlah;
        totalFields[row].setText(String.valueOf(subTot));
        hitungTotal();       // totalTransaksi ← loop semua totalFields
    }
    
    private void konfirmasiSebelumExit(){
        // listener buat X
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                int ok = JOptionPane.showConfirmDialog(
                        Kasir.this,
                        "Yakin mau keluar?\nData yang belum disimpan akan hilang!",
                        "Konfirmasi",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                if (ok == JOptionPane.YES_OPTION) {
                     System.exit(0);
                }
                else setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            }
        });
    }
    
private LocalDate getTanggal() {
    return Waktu.tanggal();
}

private LocalTime getJam() {
    return Waktu.jam();
}

private String getNama() {
    String nama = txtNamaPembeli.getText().trim();
    return nama.isEmpty() ? "Guest" : nama;
}

private int getBayar() {
    return Integer.parseInt(txtBayar.getText().replace(".", ""));
}
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnBatal = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        daftar1 = new javax.swing.JTextField();
        daftar2 = new javax.swing.JTextField();
        daftar3 = new javax.swing.JTextField();
        daftar4 = new javax.swing.JTextField();
        hargaDaftar1 = new javax.swing.JTextField();
        hargaDaftar2 = new javax.swing.JTextField();
        hargaDaftar3 = new javax.swing.JTextField();
        hargaDaftar4 = new javax.swing.JTextField();
        totalDaftar1 = new javax.swing.JTextField();
        totalDaftar2 = new javax.swing.JTextField();
        totalDaftar3 = new javax.swing.JTextField();
        totalDaftar4 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtTtlTransaksi = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtBayar = new javax.swing.JTextField();
        btnHitungBiaya = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        paket1 = new javax.swing.JButton();
        paket2 = new javax.swing.JButton();
        seblak1 = new javax.swing.JButton();
        seblak2 = new javax.swing.JButton();
        seblak3 = new javax.swing.JButton();
        txtKembalian = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jmlhDaftar1 = new javax.swing.JSpinner();
        jmlhDaftar2 = new javax.swing.JSpinner();
        jmlhDaftar3 = new javax.swing.JSpinner();
        jmlhDaftar4 = new javax.swing.JSpinner();
        txtNamaPembeli = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        btnProses = new javax.swing.JButton();
        txtjam = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Welcome to Seblak Galau!");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setIconImages(null);
        setResizable(false);

        btnBatal.setText("Batal");
        btnBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatalActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Urbanist", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 0, 0));
        jLabel1.setText("Seblak Galau");

        jLabel2.setFont(new java.awt.Font("Urbanist", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 102));
        jLabel2.setText("Daftar menu yang di order :");

        jLabel3.setText("Nama");

        jLabel4.setText("Harga");

        jLabel5.setText("Jumlah");

        daftar1.setEditable(false);

        daftar2.setEditable(false);

        daftar3.setEditable(false);

        daftar4.setEditable(false);

        hargaDaftar1.setEditable(false);
        hargaDaftar1.setName(""); // NOI18N

        hargaDaftar2.setEditable(false);
        hargaDaftar2.setName(""); // NOI18N

        hargaDaftar3.setEditable(false);
        hargaDaftar3.setName(""); // NOI18N

        hargaDaftar4.setEditable(false);
        hargaDaftar4.setName(""); // NOI18N

        totalDaftar1.setEditable(false);

        totalDaftar2.setEditable(false);

        totalDaftar3.setEditable(false);

        totalDaftar4.setEditable(false);

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Total");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Total Transaksi");

        txtTtlTransaksi.setEditable(false);

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Bayar");

        btnHitungBiaya.setText("Hitung");
        btnHitungBiaya.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHitungBiayaActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Bookman Old Style", 3, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 0, 0));
        jLabel9.setText("Nangis karena pedes... atau kenangan?");

        paket1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Restoran/img/seblak_paket_1.png"))); // NOI18N
        paket1.setToolTipText("Paket 1” [25 000]");
        paket1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                paket1ActionPerformed(evt);
            }
        });

        paket2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Restoran/img/seblak_paket_2.png"))); // NOI18N
        paket2.setToolTipText("Paket 2 [28 000]");
        paket2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                paket2ActionPerformed(evt);
            }
        });

        seblak1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Restoran/img/seblak_chat_terakhir.png"))); // NOI18N
        seblak1.setToolTipText("Seblak \"Chat Terakhir\" [28 000]");
        seblak1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seblak1ActionPerformed(evt);
            }
        });

        seblak2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Restoran/img/seblak_move_on_gagal.png"))); // NOI18N
        seblak2.setToolTipText("Seblak \"Move On Gagal\"  [32 000]");
        seblak2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seblak2ActionPerformed(evt);
            }
        });

        seblak3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Restoran/img/seblak_seen_doang.png"))); // NOI18N
        seblak3.setToolTipText("Seblak \"Seen Doang\"  [35 000]");
        seblak3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seblak3ActionPerformed(evt);
            }
        });

        txtKembalian.setEditable(false);
        txtKembalian.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtKembalian.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel10.setText("Kembalian");

        txtNamaPembeli.setText("Guest");
        txtNamaPembeli.setToolTipText("");

        jLabel11.setText("Nama Pembeli");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel12.setText("Simpan ke database!");

        btnProses.setText("Proses!");
        btnProses.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProsesActionPerformed(evt);
            }
        });

        txtjam.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtjam.setText("Loading...");

        jLabel13.setText("Jam:");

        jMenu1.setText("Database");
        jMenu1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu1MouseClicked(evt);
            }
        });
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Cetak");

        jMenu3.setText("Struk");
        jMenu3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu3MouseClicked(evt);
            }
        });
        jMenu2.add(jMenu3);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(seblak2, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(seblak3, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(33, 33, 33)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(daftar1, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(daftar2, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(daftar3, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(daftar4, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(12, 12, 12)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(hargaDaftar1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(hargaDaftar2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(hargaDaftar3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(hargaDaftar4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(12, 12, 12)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(2, 2, 2)
                                        .addComponent(jLabel5))
                                    .addComponent(jmlhDaftar1, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jmlhDaftar2, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jmlhDaftar3, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jmlhDaftar4, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(totalDaftar1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(totalDaftar2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(totalDaftar3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(totalDaftar4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(btnBatal)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(seblak1, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(paket1, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(43, 43, 43)
                                .addComponent(paket2, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 419, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(txtTtlTransaksi, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
                    .addComponent(txtBayar)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(txtNamaPembeli, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12)
                            .addComponent(btnHitungBiaya, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtKembalian, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addComponent(jLabel11))
                            .addComponent(btnProses))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(28, 28, 28))
            .addGroup(layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel13))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtjam)))
                .addGap(31, 31, 31))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel13))
                        .addGap(1, 1, 1)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(txtjam))
                        .addGap(21, 21, 21)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(seblak1, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(paket1, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(paket2, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(jLabel2)))
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(6, 6, 6)
                                .addComponent(hargaDaftar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(hargaDaftar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(hargaDaftar3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(hargaDaftar4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(7, 7, 7)
                                .addComponent(jmlhDaftar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(jmlhDaftar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(jmlhDaftar3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(jmlhDaftar4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(jLabel6)
                                .addGap(6, 6, 6)
                                .addComponent(totalDaftar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(totalDaftar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(totalDaftar3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(totalDaftar4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addComponent(jLabel3)
                                    .addGap(6, 6, 6)
                                    .addComponent(daftar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(6, 6, 6)
                                    .addComponent(daftar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(6, 6, 6)
                                    .addComponent(daftar3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(6, 6, 6)
                                    .addComponent(daftar4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(12, 12, 12)
                                    .addComponent(btnBatal, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(seblak2, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(12, 12, 12)
                                    .addComponent(seblak3, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNamaPembeli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(jLabel7)
                        .addGap(6, 6, 6)
                        .addComponent(txtTtlTransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(txtBayar, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(btnHitungBiaya, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(23, 23, 23)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(txtKembalian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnProses)
                        .addGap(29, 29, 29)))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalActionPerformed
        kosongkan();
    }//GEN-LAST:event_btnBatalActionPerformed

    private void seblak3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_seblak3ActionPerformed
        namaMenu = "Seen doang";
        tambahMenu(namaMenu);
    }//GEN-LAST:event_seblak3ActionPerformed

    private void seblak2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_seblak2ActionPerformed
        namaMenu = "Move On Gagal";
        tambahMenu(namaMenu);
    }//GEN-LAST:event_seblak2ActionPerformed

    private void seblak1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_seblak1ActionPerformed
        namaMenu = "Chat Terakhir";
        tambahMenu(namaMenu);
    }//GEN-LAST:event_seblak1ActionPerformed

    private void paket2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_paket2ActionPerformed
        namaMenu = "Paket 2";
        tambahMenu(namaMenu);
    }//GEN-LAST:event_paket2ActionPerformed

    private void paket1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_paket1ActionPerformed
        namaMenu = "Paket 1";
        tambahMenu(namaMenu);
    }//GEN-LAST:event_paket1ActionPerformed

    private void btnHitungBiayaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHitungBiayaActionPerformed
        bayarDanKembalian();
    }//GEN-LAST:event_btnHitungBiayaActionPerformed

    private void jMenu1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu1MouseClicked
        new DataTransaksi().setVisible(true);
    }//GEN-LAST:event_jMenu1MouseClicked
    
    private String buildCatatan() {
    StringBuilder sb = new StringBuilder();

    for (i = 0; i < daftarFields.length; i++) {
        String menu   = daftarFields[i].getText().trim();
        if (menu.isEmpty()) continue;

        int qty = (int) jumlahFields[i].getValue();
        if (sb.length() > 0) sb.append(";  ");      // pemisah
        sb.append(menu).append(" x").append(qty);
    }
    return sb.toString();
}
    
    
    private void btnProsesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProsesActionPerformed
          String catatan = buildCatatan();  
        // ── 1. Validasi sederhana ─────────────────────────────────────
        if (totalTransaksi == 0) {
            JOptionPane.showMessageDialog(this,
                    "Belum ada item yang dibeli!",
                    "Info", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int bayar = getBayar();                 // parse dari txtBayar
        if (bayar < totalTransaksi) {
            JOptionPane.showMessageDialog(this,
                    "Uang bayar kurang!",
                    "Info", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // ── 2. Simpan ke database via TransaksiService ────────────────
        try {
            TransaksiService.simpan(
                getTanggal(),                           // tanggal sekarang
                getJam(),                               // jam sekarang
                getNama(),                              // txtNamaPelanggan (atau "Guest")
                totalTransaksi,                         // grand‑total
                bayar,                                  // uang bayar
                bayarDanKembalian(),                 // kembalian
                catatan
            );

            JOptionPane.showMessageDialog(this,
                    "Transaksi berhasil disimpan!",
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
            
            kosongkan();   // reset semua field & spinner

        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Gagal simpan: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnProsesActionPerformed

    private void jMenu3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu3MouseClicked
        new CetakStruk().setVisible(true);
    }//GEN-LAST:event_jMenu3MouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Kasir.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Kasir.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Kasir.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Kasir.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            System.err.println("FlatLaf gagal load: " + e);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Kasir().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBatal;
    private javax.swing.JButton btnHitungBiaya;
    private javax.swing.JButton btnProses;
    private javax.swing.JTextField daftar1;
    private javax.swing.JTextField daftar2;
    private javax.swing.JTextField daftar3;
    private javax.swing.JTextField daftar4;
    private javax.swing.JTextField hargaDaftar1;
    private javax.swing.JTextField hargaDaftar2;
    private javax.swing.JTextField hargaDaftar3;
    private javax.swing.JTextField hargaDaftar4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JSpinner jmlhDaftar1;
    private javax.swing.JSpinner jmlhDaftar2;
    private javax.swing.JSpinner jmlhDaftar3;
    private javax.swing.JSpinner jmlhDaftar4;
    private javax.swing.JButton paket1;
    private javax.swing.JButton paket2;
    private javax.swing.JButton seblak1;
    private javax.swing.JButton seblak2;
    private javax.swing.JButton seblak3;
    private javax.swing.JTextField totalDaftar1;
    private javax.swing.JTextField totalDaftar2;
    private javax.swing.JTextField totalDaftar3;
    private javax.swing.JTextField totalDaftar4;
    private javax.swing.JTextField txtBayar;
    private javax.swing.JTextField txtKembalian;
    private javax.swing.JTextField txtNamaPembeli;
    private javax.swing.JTextField txtTtlTransaksi;
    private javax.swing.JLabel txtjam;
    // End of variables declaration//GEN-END:variables
}
