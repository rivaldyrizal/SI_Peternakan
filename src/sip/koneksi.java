package sip;

import java.sql.*;
import javax.swing.JOptionPane;

public class koneksi {

    Connection con;
    Statement stm;

    public void config() {
        String ip = SIP.IP;
        System.out.println(ip);
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://" + ip + ":3306/sip", "root", "");
            stm = con.createStatement();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Koneksi gagal atau kesalahan pada alamat IP \n" + e.getMessage());
            SIP.showDialog();
            System.exit(0);
        }
    }
}
