

package sip;

import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class SIP {

   
    public static String IP = "";
    public static void main(String[] args) {
        showDialog();
        if(IP.equals("")){
            showDialog();
        }else{
            F_User fUser= new F_User();
            fUser.setVisible(true);
            fUser.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
        
    }
    
    public static void showDialog(){
        IP =JOptionPane.showInputDialog(null, "Masukkan Alamat IP");
    }
    
}
