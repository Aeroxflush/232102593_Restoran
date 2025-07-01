# 💻 Aplikasi Kasir Restoran (Java Swing + MySQL)

Aplikasi kasir sederhana berbasis Java `JFrame` dan database MySQL. Dirancang untuk ~~TUGAS KAMPUS~~ kebutuhan transaksi restoran skala kecil—mulai dari input pembelian, cetak struk, hingga manajemen data transaksi.

## 🛠️ Fitur

- Input transaksi (dengan total, bayar, kembalian otomatis)
- Tambah, Update, dan Hapus data transaksi
- Simpan data ke MySQL melalui Stored Procedure / SP
- Cetak struk pembayaran
- Tabel transaksi interaktif (klik row → data muncul otomatis)
- Tampilan waktu real-time (kayak-nya ¯\_(ツ)_/¯)
- Checkbox untuk update jam ketika mengedit data transaksi (kalo mau sih, ntahlah)

## 🧰 Teknologi

- Java SE (Swing GUI)
- MySQL Database
- JDBC
- Stored Procedure (MySQL)
- XAMPP *_duhh 😒_
- IDE: NetBeans (disarankan)
- [JCalendar](https://github.com/toedter/jcalendar)
- [Tema FlatLaf buat Java Swing](https://github.com/JFormDesigner/FlatLaf)

## 🗃️ Struktur Tabel `datatransaksi`

| Kolom           | Tipe Data      |
|----------------|----------------|
| id_transaksi   | INT (PK, AUTO_INCREMENT) |
| tanggal        | DATE           |
| jam            | TIME           |
| nama_pelanggan | VARCHAR(100)   |
| total_transaksi | INT           |
| bayar          | INT            |
| kembalian      | INT            |
| catatan        | TEXT/VARCHAR   |

## ⚙️ Stored Procedure

### Tambah:
```sql
CALL tambahTransaksi(p_tanggal, p_jam, p_nama, p_total, p_bayar, p_kembali, p_catatan);
````

### Update:

```sql
CALL updateTransaksi(p_id, p_tanggal, p_jam, p_nama, p_total, p_bayar, p_kembali, p_catatan);
```

### Hapus:

```sql
CALL hapusTransaksi(p_id);
```

## 🖨️ Cetak Struk

* Struk ditampilkan di `JTextArea`
* Bisa langsung dicetak dengan printer
* Format rapi dan wrap otomatis jika teks catatan terlalu panjang

## 🔌 Koneksi Database

Pastikan konfigurasi file `Koneksi.java` sesuai:

```java
private static final String URL = "jdbc:mysql://localhost/db_restoran?serverTimezone=Asia/Jakarta";
private static final String USER = "root";
private static final String PASS = "";
```

## 📸 Tampilan (opsional)
*SOON*

## 📝 Catatan

* `id_transaksi` tidak bisa diubah secara manual
* Checkbox jam tidak mempengaruhi fungsi core, hanya kosmetik
* Auto-resize kolom tabel bisa dimatikan
* Teks catatan (Menu yg pembeli pesan) tidak include dengan harga masing-masing menu di Cetak Struk

---
Made with 💙 by **Gero**

