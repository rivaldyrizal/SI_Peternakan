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
import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javafx.stage.FileChooser;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

public class F_Admin extends javax.swing.JFrame {

    /**
     * Creates new form F_Admin
     */
    Statement stat;
    ResultSet rs;
    PreparedStatement pst;
    Connection con;
    String sql;
    String imgPath = null;
    String idJenis;
    File selectedFile=null;
    List<Item> itemList = new ArrayList<Item>();

    public F_Admin() {
        super("Admin - Sistem Informasi Peternakan Sapi Pak Nana");
        initComponents();
        koneksi DB = new koneksi();
        DB.config();
        con = DB.con;
        stat = DB.stm;
        
//        Toolkit tk=Toolkit.getDefaultToolkit();
//        int xsize=(int) tk.getScreenSize().getWidth();
//        int ysize=(int) tk.getScreenSize().getHeight();
//        this.setSize(xsize, ysize);

        getJenisSapiComboBox();
//        getJenisSapiDataComboBox();
        showTableInputSapi();// untuk tombol simpan
        showTablePembelianSapi();//untuk tombol cari berdasarkan jenis sapi
        showAllTablePembelianSapi();// untuk tombol menampilkan semua data
        showTableJenisSapi();//untuk tombol simpan pada pengaturan
        showTableDataSapi();// unutk tombol cari pada data sapi

        //kurang ketika data sudah dibeli namun tabel masih ada pada menu pembelian
        //menu pilihan pada data ternaka pada tombol cari
    }

    public ImageIcon ResizeImage(String imagePath, byte[] pic) {
        ImageIcon myImage = null;

        if (imagePath != null) {
            myImage = new ImageIcon(imagePath);
        } else {
            myImage = new ImageIcon(pic);
        }
        Image img = myImage.getImage();
        Image img2 = img.getScaledInstance(lbl_img.getWidth(), lbl_img.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon image = new ImageIcon(img2);
        return image;
    }

    public boolean cekInput() {
        if (umurTextField.getText() == null || beratTextField.getText() == null || hargaTextField.getText() == null) {
            return false;
        } else {
            try {
                Float.parseFloat(hargaTextField.getText());
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }
    
    public void refreshTable(){
        showAllTablePembelianSapi();
        showTableDataSapi();
        showTableInputSapi();
        showTableJenisSapi();
        showTablePembelianSapi();
        JOptionPane.showMessageDialog(null, "Refresh selesai");
    }

    public void showTableInputSapi() {
        DefaultTableModel dm = new DefaultTableModel(0, 0);
        String s[] = new String[]{"Id sapi", "Jenis sapi", "Usia (tahun)", "Berat (Kg)", "Harga (Rp)", "Tanggal update"};
        dm.setColumnIdentifiers(s);
        inputTable.setModel(dm);
        try {
            pst = con.prepareStatement("select * from t_sapimasuk");
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

    public void showTablePembelianSapi() {

        DefaultTableModel dm = new DefaultTableModel(0, 0);
        String s[] = new String[]{"Id sapi", "Jenis sapi", "Usia (tahun)", "Berat (Kg)", "Harga (Rp)", "Tanggal update"};
        dm.setColumnIdentifiers(s);
        pembelianTable.setModel(dm);

        try {
            String jenissapi = jenisSapiPembelianComboBox.getSelectedItem().toString();
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
        String s[] = new String[]{"Id sapi", "Jenis sapi", "Usia (tahun)", "Berat (Kg)", "Harga (Rp)", "Tanggal update"};
        dm.setColumnIdentifiers(s);
        pembelianTable.setModel(dm);
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

    public void showTableJenisSapi() {
        DefaultTableModel dm = new DefaultTableModel(0, 0);
        String s[] = new String[]{"Id sapi", "Jenis sapi"};
        dm.setColumnIdentifiers(s);
        jenisSapiTable.setModel(dm);
        try {
            pst = con.prepareStatement("select * from t_jenissapi");
            rs = pst.executeQuery();
            while (rs.next()) {
                String id_jenissapi = rs.getString(1);
                String jenissapi = rs.getString(2);
                //System.out.println(jenis);

                Vector<String> vector = new Vector<String>();
                vector.add(id_jenissapi);
                vector.add(jenissapi);
                dm.addRow(vector);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Eror " + e);
        }
    }

    public void showTableDataSapi() {

        String pilihan = jenisSapiDataComboBox.getSelectedItem().toString();

        if (pilihan.equals("Sapi masuk")) {
            DefaultTableModel dm = new DefaultTableModel(0, 0);
            String s[] = new String[]{"Nama pembeli", "id sapi", "Jenis sapi", "Usia (tahun)", "Berat (Kg)", "Harga (Rp)", "Tanggal beli"};
            dm.setColumnIdentifiers(s);
            dataTable.setModel(dm);
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
        } else {
            DefaultTableModel dm = new DefaultTableModel(0, 0);
            String s[] = new String[]{"nama pembeli", "Id sapi", "Jenis sapi", "Usia (tahun)", "Berat (Kg)", "Harga (Rp)", "Tanggal beli"};
            dm.setColumnIdentifiers(s);
            dataTable.setModel(dm);
            try {
                pst = con.prepareStatement("SELECT namapembeli, t_sapimasuk.id_sapi,jenissapi, umur, berat, harga, tanggalbeli from t_sapimasuk INNER JOIN t_sapikeluar ON t_sapimasuk.id_sapi=t_sapikeluar.id_sapi ");
                rs = pst.executeQuery();
                while (rs.next()) {
                    String nama = rs.getString(1);
                    String id = rs.getString(2);
                    String jenis = rs.getString(3);
                    String usia = rs.getString(4);
                    String jumlah = rs.getString(5);
                    String harga = rs.getString(6);
                    String tanggal = rs.getString(7);
                    //System.out.println(jenis);

                    Vector<String> vector = new Vector<String>();
                    vector.add(nama);
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

    }

//    public void setComboBox() {
//        for (Item en : itemList) {
//            jenisSapiInputComboBox.addItem(en.toString());
//
//        }
//    }
    public void getJenisSapiComboBox() {
        itemList.clear();
        jenisSapiInputComboBox.removeAllItems();
        // jenisSapiDataComboBox.removeAllItems();
        jenisSapiPembelianComboBox.removeAllItems();
        try {
            pst = con.prepareStatement("select * from t_jenissapi");
            rs = pst.executeQuery();
            while (rs.next()) {
                String id_jenissapi = rs.getString(1);
                String jenissapi = rs.getString(2);
//                comboBoxItems.add(new Item(id_jenissapi, jenissapi).toString());
                itemList.add(new Item(id_jenissapi, jenissapi));
//                jenisSapiInputComboBox.addItem(new Item(id_jenissapi, jenissapi).toString()); 
//                jenisSapiInputComboBox.addItem(itemList.toString());
            }

            for (Item en : itemList) {
                jenisSapiInputComboBox.addItem(en.toString());
                // jenisSapiDataComboBox.addItem(en.toString());
                jenisSapiPembelianComboBox.addItem(en.toString());
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
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jenisSapiInputComboBox = new javax.swing.JComboBox<>();
        beratTextField = new javax.swing.JTextField();
        addButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        inputTable = new javax.swing.JTable();
        umurTextField = new javax.swing.JTextField();
        hargaTextField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        updateButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        tambahGambarButton = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        lbl_img = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jPanel17 = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        pembelianTable = new javax.swing.JTable();
        beliPembelianButton = new javax.swing.JButton();
        jenisSapiPembelianComboBox = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        cariPembelianButton = new javax.swing.JButton();
        tampilkanCariButton = new javax.swing.JButton();
        namaPembeliTextField = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        dataTable = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        jenisSapiDataComboBox = new javax.swing.JComboBox<>();
        cariDataButton = new javax.swing.JButton();
        deleteDataButton = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel10 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jenisSapiTextField = new javax.swing.JTextField();
        simpanButton = new javax.swing.JButton();
        jPanel16 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jenisSapiTable = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jPanel22 = new javax.swing.JPanel();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jPanel23 = new javax.swing.JPanel();
        usernameTextField = new javax.swing.JTextField();
        simpanUsernameButton = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        jPanel24 = new javax.swing.JPanel();
        newPass = new javax.swing.JTextField();
        konfirmasiPass = new javax.swing.JTextField();
        simpanPasswordButton = new javax.swing.JButton();
        passLama = new javax.swing.JPasswordField();
        sandiEdit = new javax.swing.JCheckBox();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        keluarButton = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setMinimumSize(new java.awt.Dimension(1103, 673));

        jTabbedPane1.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        jTabbedPane1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 15)); // NOI18N
        jTabbedPane1.setMinimumSize(new java.awt.Dimension(1103, 673));
        jTabbedPane1.setPreferredSize(new java.awt.Dimension(1103, 673));

        jPanel1.setBackground(new java.awt.Color(101, 255, 193));
        jPanel1.setPreferredSize(new java.awt.Dimension(1103, 673));
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel1MouseClicked(evt);
            }
        });

        jPanel4.setBackground(new java.awt.Color(0, 0, 51));
        jPanel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel4MouseClicked(evt);
            }
        });

        jPanel7.setBackground(new java.awt.Color(255, 102, 0));
        jPanel7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel7MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 8, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 51, Short.MAX_VALUE)
        );

        jLabel7.setBackground(new java.awt.Color(153, 255, 204));
        jLabel7.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(153, 255, 204));
        jLabel7.setText("Input Data");
        jLabel7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel7MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel1.setText("Jenis Sapi");

        jLabel2.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel2.setText("Usia");

        jLabel3.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel3.setText("Berat");

        jenisSapiInputComboBox.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        jenisSapiInputComboBox.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jenisSapiInputComboBoxMouseClicked(evt);
            }
        });
        jenisSapiInputComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jenisSapiInputComboBoxActionPerformed(evt);
            }
        });

        beratTextField.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        addButton.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        addButton.setForeground(new java.awt.Color(0, 204, 0));
        addButton.setText("Add");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        inputTable.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        inputTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Jenis sapi", "Usia sapi", "Berat", "Harga", "Tanggal", "ID"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        inputTable.getTableHeader().setReorderingAllowed(false);
        inputTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                inputTableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(inputTable);

        umurTextField.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        hargaTextField.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        jLabel4.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel4.setText("Harga");

        updateButton.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        updateButton.setForeground(new java.awt.Color(255, 204, 0));
        updateButton.setText("Update");
        updateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateButtonActionPerformed(evt);
            }
        });

        deleteButton.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        deleteButton.setForeground(new java.awt.Color(255, 0, 0));
        deleteButton.setText("Delete");
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        tambahGambarButton.setText("Browse...");
        tambahGambarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tambahGambarButtonActionPerformed(evt);
            }
        });

        jLabel20.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel20.setText("Gambar");

        lbl_img.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jButton2.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        jButton2.setText("Refresh");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 928, Short.MAX_VALUE)
                        .addGap(8, 8, 8))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jenisSapiInputComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(2, 2, 2)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel3)
                                                    .addComponent(jLabel2)))
                                            .addComponent(jLabel4)
                                            .addComponent(jLabel20))
                                        .addGap(26, 26, 26))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButton2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(beratTextField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(hargaTextField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(tambahGambarButton, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                            .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(updateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(deleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(umurTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lbl_img, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jenisSapiInputComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(umurTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(beratTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(13, 13, 13)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(hargaTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addGap(11, 11, 11)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel20)
                            .addComponent(tambahGambarButton))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(addButton)
                            .addComponent(updateButton)
                            .addComponent(deleteButton)
                            .addComponent(jButton2)))
                    .addComponent(lbl_img, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Pengelolaan data", jPanel1);

        jPanel17.setBackground(new java.awt.Color(101, 255, 193));
        jPanel17.setPreferredSize(new java.awt.Dimension(1103, 673));

        jPanel18.setBackground(new java.awt.Color(0, 0, 51));

        jPanel19.setBackground(new java.awt.Color(255, 102, 0));

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 8, Short.MAX_VALUE)
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 49, Short.MAX_VALUE)
        );

        jLabel12.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(153, 255, 204));
        jLabel12.setText("Menu Pembelian Sapi");

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel12)))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        pembelianTable.setModel(new javax.swing.table.DefaultTableModel(
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
                "Jenis sapi", "Usia", "Berat", "Harga", "Tanggal ", "Id"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        pembelianTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pembelianTableMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(pembelianTable);

        beliPembelianButton.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        beliPembelianButton.setText("Beli");
        beliPembelianButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                beliPembelianButtonMouseClicked(evt);
            }
        });
        beliPembelianButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                beliPembelianButtonActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel13.setText("Lihat sapi ");

        cariPembelianButton.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        cariPembelianButton.setText("Cari");
        cariPembelianButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cariPembelianButtonMouseClicked(evt);
            }
        });
        cariPembelianButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cariPembelianButtonActionPerformed(evt);
            }
        });

        tampilkanCariButton.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        tampilkanCariButton.setText("Tampilkan semua");
        tampilkanCariButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tampilkanCariButtonMouseClicked(evt);
            }
        });
        tampilkanCariButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tampilkanCariButtonActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel14.setText("Nama pembeli");

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(jLabel13))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(namaPembeliTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tampilkanCariButton)
                            .addGroup(jPanel17Layout.createSequentialGroup()
                                .addComponent(jenisSapiPembelianComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cariPembelianButton))))
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 928, Short.MAX_VALUE)))
                .addGap(18, 18, 18))
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(408, 408, 408)
                .addComponent(beliPembelianButton, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addGap(45, 45, 45)
                        .addComponent(jLabel14))
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jenisSapiPembelianComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cariPembelianButton))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tampilkanCariButton)
                        .addGap(12, 12, 12)
                        .addComponent(namaPembeliTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 436, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(beliPembelianButton, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Pembelian", jPanel17);

        jPanel2.setBackground(new java.awt.Color(101, 255, 193));
        jPanel2.setPreferredSize(new java.awt.Dimension(1103, 673));

        jPanel5.setBackground(new java.awt.Color(0, 0, 51));

        jPanel8.setBackground(new java.awt.Color(255, 102, 0));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 8, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 51, Short.MAX_VALUE)
        );

        jLabel8.setBackground(new java.awt.Color(153, 255, 204));
        jLabel8.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(153, 255, 204));
        jLabel8.setText("Data Sapi");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(21, 21, 21))))
        );

        dataTable.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        dataTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Jenis sapi", "Usia sapi", "Berat", "Harga", "Id", "Tanggal", "Title 7"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(dataTable);

        jLabel6.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel6.setText("Data sapi");

        jenisSapiDataComboBox.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        jenisSapiDataComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sapi keluar", "Sapi masuk", " " }));
        jenisSapiDataComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jenisSapiDataComboBoxActionPerformed(evt);
            }
        });

        cariDataButton.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        cariDataButton.setText("Cari");
        cariDataButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cariDataButtonMouseClicked(evt);
            }
        });
        cariDataButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cariDataButtonActionPerformed(evt);
            }
        });

        deleteDataButton.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        deleteDataButton.setText("Batal");
        deleteDataButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteDataButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 928, Short.MAX_VALUE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jenisSapiDataComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(19, 19, 19)
                                .addComponent(cariDataButton))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(412, 412, 412)
                        .addComponent(deleteDataButton, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                    .addComponent(jenisSapiDataComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cariDataButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 487, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(deleteDataButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        jTabbedPane1.addTab("Data sapi", jPanel2);

        jPanel3.setBackground(new java.awt.Color(101, 255, 193));
        jPanel3.setPreferredSize(new java.awt.Dimension(1103, 673));

        jPanel6.setBackground(new java.awt.Color(0, 0, 51));

        jPanel9.setBackground(new java.awt.Color(255, 102, 0));

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 8, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 51, Short.MAX_VALUE)
        );

        jLabel9.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(153, 255, 204));
        jLabel9.setText("Pengaturan");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(776, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        jTabbedPane2.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N

        jPanel15.setBorder(javax.swing.BorderFactory.createTitledBorder("Input Jenis Sapi"));

        jLabel11.setText("Jenis Sapi");

        simpanButton.setText("Simpan");
        simpanButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                simpanButtonMouseClicked(evt);
            }
        });
        simpanButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simpanButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jenisSapiTextField)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGap(0, 111, Short.MAX_VALUE)
                        .addComponent(simpanButton, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jenisSapiTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(simpanButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(8, 8, 8))
        );

        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder("Data Jenis Sapi"));

        jenisSapiTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "ID jenis sapi", "Jenis sapi"
            }
        ));
        jScrollPane3.setViewportView(jenisSapiTable);

        jButton1.setText("Hapus");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 870, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(395, 395, 395))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1))
        );

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane2.addTab("Jenis sapi", jPanel10);

        jPanel11.setBackground(new java.awt.Color(0, 204, 153));
        jPanel11.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jPanel21.setBackground(new java.awt.Color(51, 255, 204));

        jLabel15.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel15.setText("Pengaturan akun");

        jPanel22.setBackground(new java.awt.Color(255, 153, 0));

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 9, Short.MAX_VALUE)
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 38, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane3.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N

        jPanel23.setBackground(new java.awt.Color(0, 204, 153));
        jPanel23.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel23.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N

        simpanUsernameButton.setText("Simpan");
        simpanUsernameButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simpanUsernameButtonActionPerformed(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Username baru");

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap(304, Short.MAX_VALUE)
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(simpanUsernameButton)
                    .addComponent(usernameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(364, Short.MAX_VALUE))
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(usernameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(simpanUsernameButton)
                .addContainerGap(368, Short.MAX_VALUE))
        );

        jTabbedPane3.addTab("Username", jPanel23);

        jPanel24.setBackground(new java.awt.Color(0, 204, 153));
        jPanel24.setForeground(new java.awt.Color(255, 255, 255));

        konfirmasiPass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                konfirmasiPassActionPerformed(evt);
            }
        });

        simpanPasswordButton.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        simpanPasswordButton.setText("Simpan");
        simpanPasswordButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simpanPasswordButtonActionPerformed(evt);
            }
        });

        sandiEdit.setBackground(new java.awt.Color(0, 204, 153));
        sandiEdit.setForeground(new java.awt.Color(255, 255, 255));
        sandiEdit.setText("Tampilkan sandi");
        sandiEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sandiEditActionPerformed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Password baru");

        jLabel17.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Konfirmasi password");

        jLabel18.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Password lama");

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addGap(184, 184, 184)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel24Layout.createSequentialGroup()
                        .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel16)
                            .addComponent(jLabel17)
                            .addComponent(jLabel18))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(passLama)
                            .addComponent(newPass)
                            .addComponent(konfirmasiPass, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(sandiEdit))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel24Layout.createSequentialGroup()
                        .addComponent(simpanPasswordButton)
                        .addGap(150, 150, 150)))
                .addContainerGap(321, Short.MAX_VALUE))
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(newPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(konfirmasiPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(passLama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sandiEdit)
                    .addComponent(jLabel18))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(simpanPasswordButton)
                .addContainerGap(318, Short.MAX_VALUE))
        );

        jTabbedPane3.addTab("Sandi", jPanel24);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jTabbedPane3)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane3))
        );

        jTabbedPane2.addTab("Akun", jPanel11);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTabbedPane2)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Pengaturan", jPanel3);

        jPanel12.setBackground(new java.awt.Color(101, 255, 193));
        jPanel12.setPreferredSize(new java.awt.Dimension(1103, 673));

        jPanel13.setBackground(new java.awt.Color(0, 0, 51));

        jPanel14.setBackground(new java.awt.Color(255, 102, 0));

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 9, Short.MAX_VALUE)
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jLabel10.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(153, 255, 204));
        jLabel10.setText("Log Out");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        jPanel20.setBackground(new java.awt.Color(101, 255, 193));

        jLabel5.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        jLabel5.setText("Apakah anda yakin ingin keluar?");

        keluarButton.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        keluarButton.setText("Ya");
        keluarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                keluarButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(keluarButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(37, 37, 37))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(keluarButton, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(230, 230, 230)
                .addComponent(jLabel21)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap(306, Short.MAX_VALUE)
                .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(295, 295, 295))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(57, 57, 57)
                .addComponent(jLabel21)
                .addGap(71, 71, 71)
                .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Log out", jPanel12);

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

    private void jenisSapiInputComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jenisSapiInputComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jenisSapiInputComboBoxActionPerformed

    private void keluarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_keluarButtonActionPerformed
        F_User fUser = new F_User();
        setVisible(false);
        fUser.setVisible(true);// TODO add your handling code here: 
    }//GEN-LAST:event_keluarButtonActionPerformed


    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        //Button simpan pada input
//        String idJenis = "";
        if (cekInput()) {
            try {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date();
                int i = jenisSapiInputComboBox.getSelectedIndex();
                String id_jenissapi = itemList.get(i).getId();
                String jenissapi = jenisSapiInputComboBox.getSelectedItem().toString();
             
                InputStream img = new FileInputStream(new File(imgPath));
//                sql = "insert into t_sapimasuk(id_jenissapi, jenissapi, umur, berat, harga, tanggalmasuk, gambar)values('" + id_jenissapi + "','" + jenissapi + "','" + umurTextField.getText() + "','" + beratTextField.getText() + "','" + hargaTextField.getText() + "','" + dateFormat.format(date) + "',?)";
                sql = "insert into t_sapimasuk(id_jenissapi, jenissapi, umur, berat, harga, tanggalmasuk, gambar) values(?,?,?,?,?,?,?)";
                PreparedStatement pst = con.prepareStatement(sql);
                pst.setString(1, id_jenissapi);
                pst.setString(2, jenissapi);
                pst.setString(3, umurTextField.getText());
                pst.setString(4, beratTextField.getText());
                pst.setString(5, hargaTextField.getText());
                pst.setString(6, dateFormat.format(date));
                pst.setBlob(7, img);
                pst.executeUpdate();
//            jenis=jenisSapiComboBox.getSelectedItem().toString();
//            pst.setString(1, jenis);
//            pst.setString(2, umur.getText());
//            pst.setString(3, jumlah.getText());
//            pst.setString(4, harga.getText());
                
                showTableInputSapi();
                showAllTablePembelianSapi();
                showTableDataSapi();
                umurTextField.setText("");
                beratTextField.setText("");
                hargaTextField.setText("");
                lbl_img.setIcon(null);
                JOptionPane.showMessageDialog(null, "Data telah ditambahkan");

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Eror atau terdapat data belum terisi "+e);// TODO add your handling code here:
            }
        }else{
            JOptionPane.showMessageDialog(null, "Masukan salah atau terdapat data belum terisi");
        }
    }//GEN-LAST:event_addButtonActionPerformed

    private void jenisSapiInputComboBoxMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jenisSapiInputComboBoxMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jenisSapiInputComboBoxMouseClicked

    private void simpanButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_simpanButtonMouseClicked
        //Button simpan pada tambah isi combo box
        if (!jenisSapiTextField.getText().equals("")) {
            try {
                sql = "insert into t_jenissapi(jenissapi)values('" + jenisSapiTextField.getText() + "')";
                PreparedStatement pst = con.prepareStatement(sql);
                pst.executeUpdate(sql);
                showTableJenisSapi();
                JOptionPane.showMessageDialog(null, "Succes Update");
                jenisSapiTextField.setText("");
                getJenisSapiComboBox();
//                getJenisSapiDataComboBox();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Eror atau terdapat data belum terisi" );
            }
        } else {
            JOptionPane.showMessageDialog(null, "Inputan tidak boleh kosong");
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_simpanButtonMouseClicked

    private void jenisSapiDataComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jenisSapiDataComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jenisSapiDataComboBoxActionPerformed

    private void cariPembelianButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cariPembelianButtonMouseClicked
        showTablePembelianSapi();        // TODO add your handling code here:
    }//GEN-LAST:event_cariPembelianButtonMouseClicked

    private void tampilkanCariButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tampilkanCariButtonMouseClicked
        showAllTablePembelianSapi();        // TODO add your handling code here:
    }//GEN-LAST:event_tampilkanCariButtonMouseClicked

    private void pembelianTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pembelianTableMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_pembelianTableMouseClicked

    private void beliPembelianButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_beliPembelianButtonMouseClicked
        int column = 0;
        int row = pembelianTable.getSelectedRow();
        String valueId_sapi = pembelianTable.getModel().getValueAt(row, column).toString();
        String namaPembeli = namaPembeliTextField.getText();

        if (!namaPembeli.equals("")) {
            try {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date();
                sql = "insert into t_sapikeluar(id_sapi, namapembeli, tanggalbeli)values('" + valueId_sapi + "','" + namaPembeliTextField.getText() + "','" + dateFormat.format(date) + "')";
                PreparedStatement pst = con.prepareStatement(sql);
                pst.executeUpdate(sql);
                showAllTablePembelianSapi();
                showTableDataSapi();
                namaPembeliTextField.setText("");
                JOptionPane.showMessageDialog(null, "Data pembelian telah diupdate");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Eror atau terdapat data belum terisi");// TODO add your handling code here:
            }
        } else {
            JOptionPane.showMessageDialog(null, "Nama pembeli tidak boleh kosong");
        }

// TODO add your handling code here:
    }//GEN-LAST:event_beliPembelianButtonMouseClicked

    private void cariDataButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cariDataButtonMouseClicked
        showTableDataSapi(); // TODO add your handling code here:
    }//GEN-LAST:event_cariDataButtonMouseClicked

    private void beliPembelianButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_beliPembelianButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_beliPembelianButtonActionPerformed

    private void cariPembelianButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cariPembelianButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cariPembelianButtonActionPerformed

    private void inputTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_inputTableMouseClicked
        int row = inputTable.getSelectedRow();
        String Table_click = (inputTable.getModel().getValueAt(row, 0).toString());
        try {
            String sql = "select * from t_sapimasuk where id_sapi='" + Table_click + "'";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                String add1 = rs.getString("umur");
                umurTextField.setText(add1);

                String add2 = rs.getString("berat");
                beratTextField.setText(add2);

                String add3 = rs.getString("harga");
                hargaTextField.setText(add3);

                byte[] gbr = rs.getBytes("gambar");
                ImageIcon icon;
                icon = new ImageIcon(new ImageIcon(gbr).getImage().getScaledInstance(lbl_img.getWidth(), lbl_img.getHeight(), Image.SCALE_SMOOTH));

                lbl_img.setIcon(icon);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "eror " + e);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_inputTableMouseClicked

    private void updateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateButtonActionPerformed

        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();

            int row = inputTable.getSelectedRow();
            String Table_click = (inputTable.getModel().getValueAt(row, 0).toString());
            InputStream img = new FileInputStream(new File(imgPath));

//            sql = "UPDATE t_sapimasuk SET `umur`='" + umurTextField.getText() + "',`berat`='" + beratTextField.getText() + "',`harga`='" + hargaTextField.getText() + "',`tanggalmasuk`='" + dateFormat.format(date) + "',`gambar`='" + img + "' WHERE `id_sapi` ='" + Table_click + "'";
            sql = "UPDATE t_sapimasuk SET `umur`= ?,`berat`= ?,`harga`= ?,`tanggalmasuk`= ?,`gambar`= ? WHERE `id_sapi` = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, umurTextField.getText());
            pst.setString(2, beratTextField.getText());
            pst.setString(3, hargaTextField.getText());
            pst.setString(4, dateFormat.format(date));
            pst.setBlob(5, img);
            pst.setString(6, Table_click);
            pst.executeUpdate();
            
            showTableInputSapi();
            showAllTablePembelianSapi();
            showTableDataSapi();
            umurTextField.setText("");
            beratTextField.setText("");
            hargaTextField.setText("");
            JOptionPane.showMessageDialog(null, "Data telah diupdate");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Terdapat data belum terisi");// TODO add your handling code here:
        }// TODO add your handling code here:
    }//GEN-LAST:event_updateButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed

        int p = JOptionPane.showConfirmDialog(null, "Do you really want to delete?", "Delete", JOptionPane.YES_NO_OPTION);
        if (p == 0) {
            try {
                int row = inputTable.getSelectedRow();
                String Table_click = (inputTable.getModel().getValueAt(row, 0).toString());

                sql = "DELETE FROM `t_sapimasuk` WHERE id_sapi = '" + Table_click + "'";
                PreparedStatement pst = con.prepareStatement(sql);
                pst.executeUpdate(sql);
                showTableInputSapi();
                showAllTablePembelianSapi();
                showTableDataSapi();
                umurTextField.setText("");
                beratTextField.setText("");
                hargaTextField.setText("");
                JOptionPane.showMessageDialog(null, "Data telah terhapus");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Pilih data untuk dihapus");// TODO add your handling code here:
            }
        }// TODO add your handling code here:
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void konfirmasiPassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_konfirmasiPassActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_konfirmasiPassActionPerformed

    private void simpanUsernameButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simpanUsernameButtonActionPerformed
        try {
            sql = "UPDATE admin SET `username`='" + usernameTextField.getText() + "'";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.executeUpdate(sql);
            JOptionPane.showMessageDialog(null, "Data telah diupdate");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Eror " + e);
        }
    }//GEN-LAST:event_simpanUsernameButtonActionPerformed

    private void sandiEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sandiEditActionPerformed
        if (sandiEdit.isSelected()) {
            passLama.setEchoChar((char) 0);
        } else {
            passLama.setEchoChar('\u26ab');
        }// TODO add your handling code here:
    }//GEN-LAST:event_sandiEditActionPerformed

    private void simpanPasswordButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simpanPasswordButtonActionPerformed
        try {
            sql = "UPDATE admin SET `password`='" + newPass.getText() + "'";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.executeUpdate(sql);
            JOptionPane.showMessageDialog(null, "Data telah diupdate");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Eror " + e);
        }// TODO add your handling code here:
    }//GEN-LAST:event_simpanPasswordButtonActionPerformed

    private void cariDataButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cariDataButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cariDataButtonActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int p = JOptionPane.showConfirmDialog(null, "Do you really want to delete?", "Delete", JOptionPane.YES_NO_OPTION);
        if (p == 0) {
            try {
                int row = jenisSapiTable.getSelectedRow();
                String Table_click = (jenisSapiTable.getModel().getValueAt(row, 0).toString());

                sql = "DELETE FROM `t_jenissapi` WHERE id_jenissapi = '" + Table_click + "'";
                PreparedStatement pst = con.prepareStatement(sql);
                pst.executeUpdate(sql);
                showTableJenisSapi();
                JOptionPane.showMessageDialog(null, "Data telah diupdate");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Eror atau data belum terpilih");// TODO add your handling code here:
            }
        }// TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void tambahGambarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tambahGambarButtonActionPerformed
        JFileChooser file = new JFileChooser();
        file.setCurrentDirectory(new File("E:\\#BISMILLAH\\#Semester 3\\1. PBO\\#TA\\sapi"));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("*.image", "jpg", "png");
        int result = file.showSaveDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = file.getSelectedFile();
            String path = selectedFile.getAbsolutePath();
            lbl_img.setIcon(ResizeImage(path, null));
            imgPath = path;
        } else if (result == JFileChooser.CANCEL_OPTION) {
            //JOptionPane.showMessageDialog(null, "Eror terdapat kesalahan");
        }// TODO add your handling code here:
    }//GEN-LAST:event_tambahGambarButtonActionPerformed

    private void simpanButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simpanButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_simpanButtonActionPerformed

    private void tampilkanCariButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tampilkanCariButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tampilkanCariButtonActionPerformed

    private void jPanel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseClicked
        umurTextField.setText("");
        beratTextField.setText("");
        hargaTextField.setText("");        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel1MouseClicked

    private void jPanel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel4MouseClicked
        umurTextField.setText("");
        beratTextField.setText("");
        hargaTextField.setText("");// TODO add your handling code here:
    }//GEN-LAST:event_jPanel4MouseClicked

    private void jLabel7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MouseClicked
        umurTextField.setText("");
        beratTextField.setText("");
        hargaTextField.setText("");        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel7MouseClicked

    private void jPanel7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel7MouseClicked
        umurTextField.setText("");
        beratTextField.setText("");
        hargaTextField.setText(""); // TODO add your handling code here:
    }//GEN-LAST:event_jPanel7MouseClicked

    private void deleteDataButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteDataButtonActionPerformed

         String pilihan = jenisSapiDataComboBox.getSelectedItem().toString();

        if (pilihan.equals("Sapi keluar")) {
            int p = JOptionPane.showConfirmDialog(null, "Apakah anda yakin untuk membatalkan?", "Delete", JOptionPane.YES_NO_OPTION);
            if (p == 0) {
                try {
                    int row = dataTable.getSelectedRow();
                    String Table_click = (dataTable.getModel().getValueAt(row, 1).toString());

                    sql = "DELETE FROM `t_sapikeluar` WHERE id_sapi = '" + Table_click + "'";
                    PreparedStatement pst = con.prepareStatement(sql);
                    pst.executeUpdate(sql);
                    showTableInputSapi();
                    showAllTablePembelianSapi();
                    showTableDataSapi();
                    umurTextField.setText("");
                    beratTextField.setText("");
                    hargaTextField.setText("");
                    JOptionPane.showMessageDialog(null, "Data telah terhapus");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Pilih data untuk dihapus");// TODO add your handling code here:
                }
            }
        }else {
            JOptionPane.showMessageDialog(null, "Data sapi masuk tidak dapat dihapus pada menu ini");
        }// TODO add your handling code here:
    }//GEN-LAST:event_deleteDataButtonActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        refreshTable();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

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
            java.util.logging.Logger.getLogger(F_Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(F_Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(F_Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(F_Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new F_Admin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JButton beliPembelianButton;
    private javax.swing.JTextField beratTextField;
    private javax.swing.JButton cariDataButton;
    private javax.swing.JButton cariPembelianButton;
    private javax.swing.JTable dataTable;
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton deleteDataButton;
    private javax.swing.JTextField hargaTextField;
    private javax.swing.JTable inputTable;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
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
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JComboBox<String> jenisSapiDataComboBox;
    private javax.swing.JComboBox<String> jenisSapiInputComboBox;
    private javax.swing.JComboBox<String> jenisSapiPembelianComboBox;
    private javax.swing.JTable jenisSapiTable;
    private javax.swing.JTextField jenisSapiTextField;
    private javax.swing.JButton keluarButton;
    private javax.swing.JTextField konfirmasiPass;
    private javax.swing.JLabel lbl_img;
    private javax.swing.JTextField namaPembeliTextField;
    private javax.swing.JTextField newPass;
    private javax.swing.JPasswordField passLama;
    private javax.swing.JTable pembelianTable;
    private javax.swing.JCheckBox sandiEdit;
    private javax.swing.JButton simpanButton;
    private javax.swing.JButton simpanPasswordButton;
    private javax.swing.JButton simpanUsernameButton;
    private javax.swing.JButton tambahGambarButton;
    private javax.swing.JButton tampilkanCariButton;
    private javax.swing.JTextField umurTextField;
    private javax.swing.JButton updateButton;
    private javax.swing.JTextField usernameTextField;
    // End of variables declaration//GEN-END:variables
}
