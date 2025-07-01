/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Restoran;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import javax.swing.JLabel;
import javax.swing.Timer;

/**
 *
 * @author Gero
 */
public class Waktu {
    private static final ZoneId JKT = ZoneId.of("Asia/Jakarta");
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("HH:mm:ss");
    private final Timer timer;
    
    public Waktu(JLabel label){
        timer = new Timer(1000, e -> {
        String jamStr = LocalTime.now(JKT).format(DTF);
        label.setText(jamStr);
        });
        timer.start();
    }
    
    public void stop() {
        timer.stop();
    }
    
    public static LocalDate tanggal(){
        return LocalDate.now();
    }
    
    public static LocalTime jam(){
        return LocalTime.now();
    }
}
