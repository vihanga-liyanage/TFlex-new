
package teaeli;

import classes.Blend;
import classes.Order;
import classes.ResultArray;
import classes.Validation;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;


public class CreateNewBlendOrder1 extends javax.swing.JFrame {
    
    private Blend blend;
    
    public Object pannel;
    
    /**
     * Creates new form AddNewOrder
     */
    public CreateNewBlendOrder1() {
        //Setting icon
        ImageIcon img = new ImageIcon("src\\img\\icon-1.png");
        this.setIconImage(img.getImage());
        
        //Add windows look and feel
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(AdminPannel.class.getName()).log(Level.SEVERE, null, ex);
        }
        initComponents();
        this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        
        //Loading required class objects
        blend = new Blend();
        
        //Setting date
        DateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy");
        Date today = new Date();
        dateLabel.setText(formatter.format(today));
        
        //Setting order id
        String orderID = new Order().getLastOrderID();

        if (orderID == "") {
            orderID = "OD00000000";
        }

        int num = Integer.parseInt(orderID.substring(2));
        num++;
        String newID = "OD";
        while (newID.length() < 10) {
            if ((newID.length() + String.valueOf(num).length()) >= 10)
                break;
            newID += "0";
        }
        newID += String.valueOf(num);
        orderIDLabel.setText(newID);
        
        //Initialize blendCombo
        blend.initBlendCombo(blendsCombo);
        blendsCombo.setSelectedIndex(-1);
        
        //Validation on qty, when key released
        blendsQtyTxt.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String qty = blendsQtyTxt.getText();
                if (qty.length() > 0) {
                    if (!(new Validation().isInt(qty))) {
                        JOptionPane.showMessageDialog(blendsQtyTxt, "Blend quantity must be a valid number.", "Invalid Blend Quantity", 2);
                        blendsQtyTxt.setText(qty.substring(0, qty.length() - 1));
                    } else if (Integer.parseInt(qty) < 0) {
                        JOptionPane.showMessageDialog(blendsQtyTxt, "Blend quantity cannot be less than 0.", "Invalid Blend Quantity", 2);
                        blendsQtyTxt.setText(qty.substring(0, qty.length() - 1));
                    }
                }
            }

            public void keyTyped(KeyEvent e) {
            }

            public void keyPressed(KeyEvent e) {
            }
        });
        
        //Changing blendlist table header font
        blendListTbl.getTableHeader().setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));

        //set focus to blendCombo
        blendsCombo.requestFocus();
        
        //setting focus to qty txt when item selected
        blendsCombo.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    blendsQtyTxt.requestFocus();
                }
            }
        });
        
        deleteBtn.setEnabled(false);
        
        //enabling delete button and update excess qty on row select
        final ListSelectionModel mod = blendListTbl.getSelectionModel();
        mod.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent lse) {
                if (!mod.isSelectionEmpty()) {
                    deleteBtn.setEnabled(true);
                    int count = blendListTbl.getRowCount();
                    for (int i=0; i<count; i++) {
                        setExcessQty(i);
                    }
                }
            }
        });
        
        //Prompt confirmation on window close
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int confirmed = JOptionPane.showConfirmDialog(null, 
                    "Are you sure you want to close the window?\nAll data you entered will be lost.", "Confirm window close",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (confirmed == JOptionPane.YES_OPTION) {
                    close();
                }
            }
        });
        
        //enabling sorting
        blendListTbl.setAutoCreateRowSorter(true);
        
    }
    //End of constructor =======================================================
    
    private void close(){
        this.dispose();
    }
    
    //method to reset excess qty
    private void setExcessQty(int row){
        String blendName = blendListTbl.getValueAt(row, 0).toString();
        blendName = blendName.replace("'", "\\'");
        int requiredQty = parseInt(blendListTbl.getValueAt(row, 4).toString());
        if (new Validation().isInt(blendListTbl.getValueAt(row, 6).toString())) {
            int finalQty = parseInt(blendListTbl.getValueAt(row, 6).toString());
            if (finalQty < requiredQty) {
                JOptionPane.showMessageDialog(blendListTbl, "<html>You cannot decrease the <b>" + blendName + "</b> final quantity less than required quantity!</html>", "Error", 2);
                blendListTbl.setValueAt(formatNum(requiredQty), row, 6);
            } else {
                blendListTbl.setValueAt(formatNum(finalQty - requiredQty), row, 5);
            }
        } else {
            JOptionPane.showMessageDialog(blendListTbl, "<html>Please enter a valid final quantity for <b>" + blendName + "</b>.</html>", "Invalid Final Blend Quantity", 2);
            blendListTbl.setValueAt(formatNum(requiredQty), row, 6);
        }
    }
    
    //formatting numbers to add commas
    private String formatNum(String num){
        String decimal=num, point = null;
        if(num.contains(".")){
            String[] temp = num.split("\\.");
            decimal = temp[0];
            point = temp[1];
        }
        int i = decimal.length();
        while (i > 3) {
            String part1 = decimal.substring(0, i-3);
            String part2 = decimal.substring(i-3);
            decimal = part1 + "," + part2;
            i-=3;
        }
        if (point != null){
            decimal += "." + point;
        }
        return decimal;
    }
    private String formatNum(int num){
        return formatNum(String.valueOf(num));
    }
    private String formatNum(float num){
        return formatNum(Float.toString(num));
    }
    
    //overiding Integer.parseInt() to accept nums with commas
    private int parseInt(String num){
        try{
            return Integer.parseInt(num);
        } catch (NumberFormatException e){
            if (num.matches("[[0-9]{1,2}+,]*")) {
                num = num.replace(",", "");
                return Integer.parseInt(num);
            }
        }
        return 0;
    }
    
    //overiding Float.parseFloat() to accept nums with commas
    private float parseFloat(String num){
        try{
            return Float.parseFloat(num);
        } catch (NumberFormatException e){
            if (num.matches("[[0-9]{1,2}+,]*.[0-9]*")) {
                num = num.replace(",", "");
                return Float.parseFloat(num);
            }
        }
        return 0;
    }
   
    //getters to pass objects to phase 2
    public String getOrderID(){
        return orderIDLabel.getText();
    }
    public DefaultTableModel getBlendListTbl(){
        return (DefaultTableModel) blendListTbl.getModel();
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        blendsCombo = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        blendWeightCombo = new javax.swing.JComboBox();
        blendAddBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        blendListTbl = new javax.swing.JTable();
        createOrderBtn = new javax.swing.JButton();
        blendsQtyTxt = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        dateLabel = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        orderIDLabel = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        deleteBtn = new javax.swing.JButton();

        jLabel1.setText("jLabel1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Create New Blend Order");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, " Create New Blend Order - Phase 1 ", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI Semibold", 0, 16))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setText("Quantity");

        blendsCombo.setEditable(true);
        blendsCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        blendsCombo.setSelectedIndex(-1);
        blendsCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                blendsComboActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel3.setText("Blends");

        blendWeightCombo.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        blendWeightCombo.setMaximumRowCount(2);
        blendWeightCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "g", "kg", " " }));
        blendWeightCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                blendWeightComboActionPerformed(evt);
            }
        });

        blendAddBtn.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        blendAddBtn.setText("Add");
        blendAddBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                blendAddBtnActionPerformed(evt);
            }
        });

        blendListTbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Blend Name", "Qty Required (g)", "Visible Stock (g)", "Invisible Stock (g)", "Balance Qty Required(g)", "Excess Qty (g)", "Final Qty (g)"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        blendListTbl.setRowHeight(24);
        blendListTbl.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                blendListTblPropertyChange(evt);
            }
        });
        jScrollPane1.setViewportView(blendListTbl);
        if (blendListTbl.getColumnModel().getColumnCount() > 0) {
            blendListTbl.getColumnModel().getColumn(0).setResizable(false);
            blendListTbl.getColumnModel().getColumn(0).setPreferredWidth(250);
            blendListTbl.getColumnModel().getColumn(1).setResizable(false);
            blendListTbl.getColumnModel().getColumn(1).setPreferredWidth(180);
            blendListTbl.getColumnModel().getColumn(2).setResizable(false);
            blendListTbl.getColumnModel().getColumn(2).setPreferredWidth(180);
            blendListTbl.getColumnModel().getColumn(3).setResizable(false);
            blendListTbl.getColumnModel().getColumn(3).setPreferredWidth(180);
            blendListTbl.getColumnModel().getColumn(4).setResizable(false);
            blendListTbl.getColumnModel().getColumn(4).setPreferredWidth(180);
            blendListTbl.getColumnModel().getColumn(5).setResizable(false);
            blendListTbl.getColumnModel().getColumn(5).setPreferredWidth(180);
            blendListTbl.getColumnModel().getColumn(6).setResizable(false);
            blendListTbl.getColumnModel().getColumn(6).setPreferredWidth(180);
        }

        createOrderBtn.setText("Create Order");
        createOrderBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createOrderBtnActionPerformed(evt);
            }
        });

        blendsQtyTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                blendsQtyTxtActionPerformed(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        dateLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        dateLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        dateLabel.setText("Feb 18, 2016");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Date :");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addComponent(dateLabel)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dateLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5))
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        orderIDLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        orderIDLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        orderIDLabel.setText("0001");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Order No :");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(orderIDLabel)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(orderIDLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jLabel15.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel15.setText("Start typing blend name to begin.");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setText("Please edit final qty column to add excess amounts.");

        deleteBtn.setText("Delete");
        deleteBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(blendsCombo, 0, 250, Short.MAX_VALUE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(28, 28, 28)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(blendsQtyTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(blendWeightCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(blendAddBtn))))
                            .addComponent(jLabel15))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 306, Short.MAX_VALUE)
                        .addComponent(deleteBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(createOrderBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(blendsCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(blendAddBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(blendsQtyTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(blendWeightCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(27, 27, 27)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(createOrderBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(deleteBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void blendWeightComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_blendWeightComboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_blendWeightComboActionPerformed

    private void blendListTblPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_blendListTblPropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_blendListTblPropertyChange

    private void createOrderBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createOrderBtnActionPerformed
        try {
            blendListTbl.getCellEditor().stopCellEditing();
        } catch (Exception e) {}
        
        int count = blendListTbl.getRowCount();
        int totalFinalQty = 0;
        for (int i=0; i<count; i++) {
            setExcessQty(i);
            totalFinalQty += parseInt(blendListTbl.getValueAt(i, 6).toString());
        }
        if (blendListTbl.getRowCount() > 0) {
            if (totalFinalQty > 0) {
                CreateNewBlendOrder2 creatNewBlendOrder2 = new CreateNewBlendOrder2(this);
                creatNewBlendOrder2.setVisible(true);
                creatNewBlendOrder2.pannel = this.pannel;
                this.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(this, "Final order quantity is zero. Please add more blends.", "Error", 2);
                blendsCombo.requestFocus();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please add at least one blend to create an order.", "Empty Blend Selection", 2);
            blendsCombo.requestFocus();
        }
                
    }//GEN-LAST:event_createOrderBtnActionPerformed

    private void blendAddBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_blendAddBtnActionPerformed
        if (blendsCombo.getSelectedIndex() == -1){
            JOptionPane.showMessageDialog(blendsCombo, "Please select a blend to add.", "Empty Blend Selection",2);
            blendsCombo.requestFocus();
            blendsCombo.setSelectedIndex(-1);
        } else if (blendsQtyTxt.getText().equals("")) {
            JOptionPane.showMessageDialog(blendsQtyTxt, "Please enter blend quantity to add.", "Empty Blend Quantity", 2);
            blendsQtyTxt.requestFocus();
        } else if (parseInt(blendsQtyTxt.getText()) <= 0) {
            JOptionPane.showMessageDialog(blendsQtyTxt, "Blend quantity is zero.", "Invalid Blend Quantity", 2);
            blendsQtyTxt.requestFocus();
        } else {
            String blendName = (String) blendsCombo.getSelectedItem();
            blendName = blendName.replace("'", "\\'");
            int blendQty = parseInt(blendsQtyTxt.getText());
            if (blendWeightCombo.getSelectedItem().equals("kg")) {
                blendQty *= 1000;
            }
            boolean isNew = true;
            //Search if the blend is already added
            int rowCount = blendListTbl.getRowCount();
            for (int i = 0; i < rowCount; i++) {
                if (blendName.equals(blendListTbl.getValueAt(i, 0))) {
                    //calculating qty required
                    blendQty += parseInt(blendListTbl.getValueAt(i, 1).toString());
                    blendListTbl.setValueAt(formatNum(blendQty), i, 1);
                    int visible = parseInt(blendListTbl.getValueAt(i, 2).toString());
                    int invisible = parseInt(blendListTbl.getValueAt(i, 3).toString());
                    int balance = 0;
                    balance = blendQty - visible;
                    if (balance > 0) {
                        balance = balance - invisible;
                    }
                    if (balance < 0) {
                        balance = 0;
                    }
                    blendListTbl.setValueAt(formatNum(balance), i, 4);
                    int excess = parseInt(blendListTbl.getValueAt(i, 5).toString());
                    blendListTbl.setValueAt(formatNum(excess + balance), i, 6);
                    isNew = false;
                    break;
                }
            }
            if (isNew) {
                ResultArray res = blend.getBlendDataByBlendName(blendName);
                Vector newRow = new Vector();
                res.next();
                newRow.addElement(res.getString(1));
                newRow.addElement(formatNum(blendQty));
                newRow.addElement(formatNum(res.getString(3)));
                newRow.addElement(formatNum(res.getString(5)));

                //calculating qty required
                int visible = parseInt(res.getString(3));
                int invisible = parseInt(res.getString(5));
                int balance = 0;
                balance = blendQty - visible;
                if (balance > 0) {
                    balance = balance - invisible;
                }
                if (balance < 0) {
                    balance = 0;
                }
                newRow.addElement(formatNum(balance));
                newRow.addElement(0);
                newRow.addElement(formatNum(balance));
                DefaultTableModel model = (DefaultTableModel) blendListTbl.getModel();
                model.addRow(newRow);
            }
            
            blendsQtyTxt.setText("");
            blendsCombo.setSelectedIndex(-1);
            blendsCombo.requestFocus();
        }
    }//GEN-LAST:event_blendAddBtnActionPerformed

    private void blendsQtyTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_blendsQtyTxtActionPerformed
        blendAddBtn.requestFocus();
    }//GEN-LAST:event_blendsQtyTxtActionPerformed

    private void blendsComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_blendsComboActionPerformed
        try {
            blendListTbl.getCellEditor().stopCellEditing();
        } catch (Exception e) {}
    }//GEN-LAST:event_blendsComboActionPerformed

    private void deleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBtnActionPerformed
        int dialogResult = JOptionPane.showConfirmDialog(blendListTbl, "Please confirm record deletion", "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (dialogResult == JOptionPane.YES_OPTION){
            DefaultTableModel model = (DefaultTableModel)blendListTbl.getModel();
            model.removeRow(blendListTbl.getSelectedRow());
            deleteBtn.setEnabled(false);
        }
    }//GEN-LAST:event_deleteBtnActionPerformed

    
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
            java.util.logging.Logger.getLogger(CreateNewBlendOrder1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CreateNewBlendOrder1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CreateNewBlendOrder1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CreateNewBlendOrder1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CreateNewBlendOrder1().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton blendAddBtn;
    private javax.swing.JTable blendListTbl;
    private javax.swing.JComboBox blendWeightCombo;
    private javax.swing.JComboBox blendsCombo;
    private javax.swing.JTextField blendsQtyTxt;
    private javax.swing.JButton createOrderBtn;
    private javax.swing.JLabel dateLabel;
    private javax.swing.JButton deleteBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel orderIDLabel;
    // End of variables declaration//GEN-END:variables
}
