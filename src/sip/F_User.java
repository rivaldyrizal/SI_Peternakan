/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sip;

/**
 *
 * @author AKHSAN
 */

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class F_User extends javax.swing.JFrame {
    

    /**
     * Creates new form F_User
     */
//    Connection con=null;
//    PreparedStatement pst=null;
//    ResultSet rs=null;
    Statement stat;
    ResultSet rs;
    PreparedStatement pst;
    Connection con;
    String sql;
    
    byte[] gbr;
    
    public F_User() {
        super("SIPANA - Sistem Informasi Peternakan Sapi Pak Nana");
        initComponents();
//        Toolkit tk=Toolkit.getDefaultToolkit();
//        int xsize=(int) tk.getScreenSize().getWidth();
//        int ysize=(int) tk.getScreenSize().getHeight();
//        this.setSize(xsize, ysize);
        
        koneksi DB = new koneksi();
        DB.config();
        con = DB.con;
        stat = DB.stm;
        getJenisSapiPencarianComboBox();
        showTablePembelianSapi();
        showAllTablePembelianSapi();
        //showUrutkatPencarianTable();
    }
    
    public void getJenisSapiPencarianComboBox(){
        try {
            pst = con.prepareStatement("select * from t_jenissapi");
            rs = pst.executeQuery();
            while (rs.next()) {
                String id_jenissapi = rs.getString(1);
                String jenissapi = rs.getString(2);
                jenisSapiPencarianComboBox.addItem(new Item(id_jenissapi, jenissapi).toString());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Eror "+e);
        }
    }
    
    public void showTablePembelianSapi() {

        DefaultTableModel dm = new DefaultTableModel(0, 0);
        String s[] = new String[]{"Nomor sapi","Jenis sapi", "Usia (tahun)", "Berat (Kg)", "Harga (Rp)", "Tanggal update"};
        dm.setColumnIdentifiers(s);
        userTable.setModel(dm);

        try {
            String jenissapi = jenisSapiPencarianComboBox.getSelectedItem().toString();
            pst = con.prepareStatement("SELECT * FROM t_sapimasuk m WHERE jenissapi='" + jenissapi + "' and NOT EXISTS (SELECT * FROM t_sapikeluar k WHERE m.id_sapi=k.id_sapi)");
            rs = pst.executeQuery();
            while (rs.next()) {
               String id = rs.getString(1);
                String jenis = rs.getString(3);
                String usia = rs.getString(4);
                String jumlah = rs.getString(5);
                String harga = rs.getString(6);
                String tanggal = rs.getString(7);
                //System.out.println(jenis);

                Vector<String> vector = new Vector<String>();
                vector.add(id);
                vector.add(jenis);
                vector.add(usia);
                vector.add(jumlah);
                vector.add(harga);
                vector.add(tanggal);
                dm.addRow(vector);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Eror " + e);
        }

    }

    public void showAllTablePembelianSapi() {
        DefaultTableModel dm = new DefaultTableModel(0, 0);
        String s[] = new String[]{"Nomor sapi","Jenis sapi", "Usia (tahun)", "Berat (Kg)", "Harga (Rp)", "Tanggal update"};
        dm.setColumnIdentifiers(s);
        userTable.setModel(dm);
        try {
            pst = con.prepareStatement("SELECT * FROM t_sapimasuk m WHERE NOT EXISTS (SELECT * FROM t_sapikeluar k WHERE m.id_sapi=k.id_sapi)");
            rs = pst.executeQuery();
            while (rs.next()) {
                String id = rs.getString(1);
                String jenis = rs.getString(3);
                String usia = rs.getString(4);
                String jumlah = rs.getString(5);
                String harga = rs.getString(6);
                String tanggal = rs.getString(7);
                //System.out.println(jenis);

                Vector<String> vector = new Vector<String>();               
                vector.add(id);
                vector.add(jenis);
                vector.add(usia);
                vector.add(jumlah);
                vector.add(harga);
                vector.add(tanggal);
                dm.addRow(vector);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Eror " + e);
        }
    }
    
    public void showUrutkatPencarianTable(){
        DefaultTableModel dm = new DefaultTableModel(0, 0);
        String s[] = new String[]{"Nomor sapi","Jenis sapi", "Usia (tahun)", "Berat (Kg)", "Harga (Rp)", "Tanggal update"};
        dm.setColumnIdentifiers(s);
        userTable.setModel(dm);

        try {
            String urut = urutPencarianComboBox.getSelectedItem().toString();
            pst = con.prepareStatement("SELECT * FROM t_sapimasuk m WHERE NOT EXISTS (SELECT * FROM t_sapikeluar k WHERE m.id_sapi=k.id_sapi) ORDER BY "+urut+" DESC");
            rs = pst.executeQuery();
            while (rs.next()) {
               String id = rs.getString(1);
                String jenis = rs.getString(3);
                String usia = rs.getString(4);
                String jumlah = rs.getString(5);
                String harga = rs.getString(6);
                String tanggal = rs.getString(7);
                //System.out.println(jenis);

                Vector<String> vector = new Vector<String>();
                vector.add(id);
                vector.add(jenis);
                vector.add(usia);
                vector.add(jumlah);
                vector.add(harga);
                vector.add(tanggal);
                dm.addRow(vector);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Eror " + e);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jenisSapiPencarianComboBox = new javax.swing.JComboBox<>();
        tampilkanPencarianButton = new javax.swing.JButton();
        cariPencarianButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        userTable = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        urutPencarianComboBox = new javax.swing.JComboBox<>();
        cariUrutkanButton = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        password = new javax.swing.JPasswordField();
        jPanel11 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        user = new javax.swing.JTextField();
        sandiCheckBox = new javax.swing.JCheckBox();
        jLabel9 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 51, 102));
        setMinimumSize(new java.awt.Dimension(750, 420));
        setUndecorated(true);
        setResizable(false);

        jTabbedPane1.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        jTabbedPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jTabbedPane1.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        jTabbedPane1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 15)); // NOI18N
        jTabbedPane1.setMinimumSize(new java.awt.Dimension(1369, 731));
        jTabbedPane1.setPreferredSize(new java.awt.Dimension(1369, 731));

        jPanel2.setBackground(new java.awt.Color(0, 153, 204));
        jPanel2.setMinimumSize(new java.awt.Dimension(1369, 731));
        jPanel2.setPreferredSize(new java.awt.Dimension(1369, 731));

        jPanel12.setBackground(new java.awt.Color(0, 153, 204));
        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Jenis Sapi", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Rounded MT Bold", 0, 12), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel12.setForeground(new java.awt.Color(255, 255, 255));

        jenisSapiPencarianComboBox.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N

        tampilkanPencarianButton.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        tampilkanPencarianButton.setText("Tampilkan semua");
        tampilkanPencarianButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tampilkanPencarianButtonMouseClicked(evt);
            }
        });
        tampilkanPencarianButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tampilkanPencarianButtonActionPerformed(evt);
            }
        });

        cariPencarianButton.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        cariPencarianButton.setText("Cari");
        cariPencarianButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cariPencarianButtonMouseClicked(evt);
            }
        });
        cariPencarianButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cariPencarianButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jenisSapiPencarianComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cariPencarianButton))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(tampilkanPencarianButton)
                        .addGap(0, 101, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jenisSapiPencarianComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cariPencarianButton, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tampilkanPencarianButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.setBackground(new java.awt.Color(0, 102, 204));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setBackground(new java.awt.Color(255, 102, 0));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 8, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 11, -1, 44));

        jLabel1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(204, 255, 204));
        jLabel1.setText("Sistem Informasi Peternakan Sapi Pak Nana");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 11, 529, 44));

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/w4.jpg"))); // NOI18N
        jLabel8.setText("jLabel8");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -210, -1, -1));

        userTable.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        userTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Jenis sapi", "Usia sapi", "Berat", "Harga", "null", "Title 6"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        userTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                userTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(userTable);

        jLabel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel6MouseClicked(evt);
            }
        });

        jPanel13.setBackground(new java.awt.Color(0, 153, 204));
        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Urutkan", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Rounded MT Bold", 0, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        urutPencarianComboBox.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        urutPencarianComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih", "Harga", "Umur", "Berat" }));

        cariUrutkanButton.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        cariUrutkanButton.setText("Cari");
        cariUrutkanButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cariUrutkanButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(urutPencarianComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cariUrutkanButton, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cariUrutkanButton, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                    .addComponent(urutPencarianComboBox))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, 268, Short.MAX_VALUE)
                            .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(183, 183, 183)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1255, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(104, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 456, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Pencarian", jPanel2);

        jPanel3.setBackground(new java.awt.Color(0, 51, 153));
        jPanel3.setPreferredSize(new java.awt.Dimension(1369, 731));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel7.setBackground(new java.awt.Color(0, 102, 204));

        jPanel8.setBackground(new java.awt.Color(255, 102, 0));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 8, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 48, Short.MAX_VALUE)
        );

        jLabel2.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(153, 255, 204));
        jLabel2.setText("Menu Petugas");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(1057, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel3.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        jPanel6.setBackground(new java.awt.Color(47, 111, 206));

        jButton2.setBackground(new java.awt.Color(255, 153, 51));
        jButton2.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 11)); // NOI18N
        jButton2.setText("Masuk");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        password.setBackground(new java.awt.Color(47, 111, 206));
        password.setForeground(new java.awt.Color(255, 255, 255));
        password.setBorder(null);
        password.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passwordActionPerformed(evt);
            }
        });
        password.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                passwordKeyPressed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        jLabel4.setText("Admin Login");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        user.setBackground(new java.awt.Color(47, 111, 206));
        user.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        user.setForeground(new java.awt.Color(255, 255, 255));
        user.setBorder(null);
        user.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userActionPerformed(evt);
            }
        });

        sandiCheckBox.setBackground(new java.awt.Color(47, 111, 206));
        sandiCheckBox.setForeground(new java.awt.Color(255, 255, 255));
        sandiCheckBox.setText("Tampilkan sandi");
        sandiCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sandiCheckBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(sandiCheckBox)
                        .addGap(63, 63, 63)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(user, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(54, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23)
                .addComponent(user, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sandiCheckBox))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jPanel3.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 250, 350, -1));

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/w2.jpg"))); // NOI18N
        jPanel3.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -70, 1340, 920));

        jTabbedPane1.addTab("Masuk", jPanel3);

        jPanel5.setBackground(new java.awt.Color(0, 153, 204));
        jPanel5.setMinimumSize(new java.awt.Dimension(1369, 731));
        jPanel5.setPreferredSize(new java.awt.Dimension(1369, 731));

        jPanel9.setBackground(new java.awt.Color(0, 102, 204));
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel10.setBackground(new java.awt.Color(255, 102, 0));

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 8, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel9.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, -1, 48));

        jLabel3.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(102, 255, 204));
        jLabel3.setText("Bantuan");
        jPanel9.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(22, 15, 112, 44));

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/w4.jpg"))); // NOI18N
        jLabel7.setText("jLabel7");
        jPanel9.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -450, -1, -1));

        jLabel5.setBackground(new java.awt.Color(255, 255, 255));
        jLabel5.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("*Menu bantuan masih kosong (dalam tahap pengembagan)");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(285, 285, 285)
                .addComponent(jLabel5)
                .addContainerGap(396, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(296, 296, 296)
                .addComponent(jLabel5)
                .addGap(0, 334, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Bantuan", jPanel5);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void passwordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passwordActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_passwordActionPerformed

    
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
//       String sql = "select * from login where username = ? and password = ?";
//        try
//        {
//            pst = con.prepareStatement(sql);
//            pst.setString(1, user.getText());
//            pst.setString(2, password.getText());
//            rs = pst.executeQuery();
//            if (rs.next())
//            {
//                JOptionPane.showMessageDialog(null, "Username and Password correct");
//            }
//            else
//            {
//                JOptionPane.showMessageDialog(null, "invalid username and password");
//            }
//        }
//        catch (Exception e)
//        {
//            JOptionPane.showMessageDialog(null, e);
//        }// TODO add your handling code here:

        try {
            sql = "SELECT * FROM admin WHERE username='" + user.getText() + "' AND password='" + String.valueOf(password.getPassword()) + "'";
            rs = stat.executeQuery(sql);
            if (rs.next()) {
                if (user.getText().equals(rs.getString("username")) && String.valueOf(password.getPassword()).equals(rs.getString("password"))) {
                   // JOptionPane.showMessageDialog(null, "berhasil login");
                    F_Admin fAdmin = new F_Admin();
                    setVisible(false);
                    fAdmin.setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(null, "username atau password salah");
                user.setText("");
                password.setText("");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Eror");
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void userActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_userActionPerformed

    private void sandiCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sandiCheckBoxActionPerformed
        if(sandiCheckBox.isSelected()){
            password.setEchoChar((char)0);
        }else{
            password.setEchoChar('\u26ab');
        }// TODO add your handling code here:
    }//GEN-LAST:event_sandiCheckBoxActionPerformed

    private void passwordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_passwordKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            try {
                sql = "SELECT * FROM admin WHERE username='" + user.getText() + "' AND password='" + String.valueOf(password.getPassword()) + "'";
                rs = stat.executeQuery(sql);
                if (rs.next()) {
                    if (user.getText().equals(rs.getString("username")) && String.valueOf(password.getPassword()).equals(rs.getString("password"))) {
                        //JOptionPane.showMessageDialog(null, "berhasil login");
                        F_Admin fAdmin = new F_Admin();
                        setVisible(false);
                        fAdmin.setVisible(true);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "username atau password salah");
                    user.setText("");
                    password.setText("");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Eror");
            }
        }// TODO add your handling code here:
    }//GEN-LAST:event_passwordKeyPressed

    private void tampilkanPencarianButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tampilkanPencarianButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tampilkanPencarianButtonActionPerformed

    private void tampilkanPencarianButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tampilkanPencarianButtonMouseClicked
        showAllTablePembelianSapi();        // TODO add your handling code here:
    }//GEN-LAST:event_tampilkanPencarianButtonMouseClicked

    private void userTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_userTableMouseClicked
        int row = userTable.getSelectedRow();
        String Table_click = (userTable.getModel().getValueAt(row, 0).toString());
        try {
            String sql = "select * from t_sapimasuk where id_sapi='" + Table_click + "'";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                gbr = rs.getBytes("gambar");
                
                ImageIcon icon = new ImageIcon(new ImageIcon(gbr).getImage().getScaledInstance(jLabel6.getWidth(), jLabel6.getHeight(), Image.SCALE_SMOOTH));

                jLabel6.setIcon(icon);
                // System.out.println(gbr);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "eror " + e);
        }
    }//GEN-LAST:event_userTableMouseClicked

    private void cariPencarianButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cariPencarianButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cariPencarianButtonActionPerformed

    private void cariPencarianButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cariPencarianButtonMouseClicked
        showTablePembelianSapi();
    }//GEN-LAST:event_cariPencarianButtonMouseClicked

    private void jLabel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseClicked
        NewJDialog fImage = new NewJDialog(F_User.this, true);
        fImage.gambar(gbr);
        fImage.setVisible(true);
    }//GEN-LAST:event_jLabel6MouseClicked

    private void cariUrutkanButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cariUrutkanButtonActionPerformed
        showUrutkatPencarianTable();// TODO add your handling code here:
    }//GEN-LAST:event_cariUrutkanButtonActionPerformed

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
            java.util.logging.Logger.getLogger(F_User.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(F_User.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(F_User.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(F_User.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                F_User FU = new F_User();
                FU.setVisible(true);
                FU.setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
        });
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cariPencarianButton;
    private javax.swing.JButton cariUrutkanButton;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JComboBox<String> jenisSapiPencarianComboBox;
    private javax.swing.JPasswordField password;
    private javax.swing.JCheckBox sandiCheckBox;
    private javax.swing.JButton tampilkanPencarianButton;
    private javax.swing.JComboBox<String> urutPencarianComboBox;
    private javax.swing.JTextField user;
    private javax.swing.JTable userTable;
    // End of variables declaration//GEN-END:variables

}
