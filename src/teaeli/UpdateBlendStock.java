package teaeli;

import classes.Blend;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class UpdateBlendStock extends javax.swing.JFrame {

    private Object pannel;

    public void setPannel(Object pannel) {
        this.pannel = pannel;
    }

    public UpdateBlendStock() {
        initComponents();
        //Setting icon
        ImageIcon img = new ImageIcon("src\\img\\icon-1.png");
        this.setIconImage(img.getImage());
        
        Dimension screenSize, frameSize;
        int x, y;
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frameSize = getSize();
        x = (screenSize.width - frameSize.width)/2;
        y = (screenSize.height - frameSize.height)/2;
        setLocation(x, y);
        setResizable(false);

        //for validation of change qty field
        newQtyTxt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String newQty = newQtyTxt.getText();
                if (newQty.length() > 0) {
                    if (!testForInteger(newQty)) {
                        JOptionPane.showMessageDialog(newQtyTxt, "Change Qty value must be a valid number", "Invalid Quantity", 2);
                        newQtyTxt.setText(null);
                    }
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }
        });
    }

    //method to refresh related tables and close this window
    private void close() {
        this.setVisible(false);
        if ("teaeli.AdminPannel".equals(pannel.getClass().getName())) {
            AdminPannel adminPannel = (AdminPannel) pannel;
            adminPannel.populateBlendStockTable();
            adminPannel.populateBlendHistoryTable();
        } else if ("teaeli.ManagerPannel".equals(pannel.getClass().getName())) {
            ManagerPannel managerPannel = (ManagerPannel) pannel;
            managerPannel.populateBlendStockTable();
        }
        this.dispose();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToggleButton1 = new javax.swing.JToggleButton();
        jPanel2 = new javax.swing.JPanel();
        updateStockReasonLbl = new javax.swing.JLabel();
        updateStockQtyInStockLbl = new javax.swing.JLabel();
        newQtyTxt = new javax.swing.JTextField();
        saveBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        reasonToChangeTxt = new javax.swing.JTextArea();
        updateStockItemNameLbl = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        cancelBtn = new javax.swing.JButton();
        updateStockItemName = new javax.swing.JLabel();
        stockQtyLbl = new javax.swing.JLabel();
        updateStockChangeName = new javax.swing.JLabel();
        updateStockItemCategoryName = new javax.swing.JLabel();
        updateStockItemCategoryLbl = new javax.swing.JLabel();
        blendStockIncreaseBtn = new javax.swing.JRadioButton();
        blendStockDecreasedBtn = new javax.swing.JRadioButton();

        jToggleButton1.setText("jToggleButton1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Update Blend Stock");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, " Update Blend Stock ", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI Semibold", 0, 16))); // NOI18N

        updateStockReasonLbl.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        updateStockReasonLbl.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        updateStockReasonLbl.setText("Reason to Change");

        updateStockQtyInStockLbl.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        updateStockQtyInStockLbl.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        updateStockQtyInStockLbl.setText("Qty In Stock");

        saveBtn.setText("Save");
        saveBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveBtnActionPerformed(evt);
            }
        });

        reasonToChangeTxt.setColumns(20);
        reasonToChangeTxt.setRows(5);
        jScrollPane1.setViewportView(reasonToChangeTxt);

        updateStockItemNameLbl.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        updateStockItemNameLbl.setText("Almond Truffle - V1 ");

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("(g)");

        cancelBtn.setText("Cancel");
        cancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtnActionPerformed(evt);
            }
        });

        updateStockItemName.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        updateStockItemName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        updateStockItemName.setText("Blend Name");

        stockQtyLbl.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        stockQtyLbl.setText("500 g");

        updateStockChangeName.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        updateStockChangeName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        updateStockChangeName.setText("Changing Qty");

        updateStockItemCategoryName.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        updateStockItemCategoryName.setText("Blend Category");

        updateStockItemCategoryLbl.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        updateStockItemCategoryLbl.setText("Artisian Blend V2");

        blendStockIncreaseBtn.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        blendStockIncreaseBtn.setText("increased");
        blendStockIncreaseBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                blendStockIncreaseBtnActionPerformed(evt);
            }
        });

        blendStockDecreasedBtn.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        blendStockDecreasedBtn.setText("decreased");
        blendStockDecreasedBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                blendStockDecreasedBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(updateStockReasonLbl))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(updateStockItemCategoryName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(updateStockItemName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(updateStockQtyInStockLbl, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(updateStockChangeName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(stockQtyLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(updateStockItemNameLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                            .addComponent(cancelBtn)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(saveBtn))
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(blendStockIncreaseBtn)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(blendStockDecreasedBtn))
                                            .addComponent(newQtyTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 20, Short.MAX_VALUE)))
                        .addGap(16, 16, 16))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(updateStockItemCategoryLbl)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(updateStockItemName, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(updateStockItemNameLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(updateStockItemCategoryName)
                    .addComponent(updateStockItemCategoryLbl))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(updateStockQtyInStockLbl)
                    .addComponent(stockQtyLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(updateStockChangeName, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4))
                    .addComponent(newQtyTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(blendStockDecreasedBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(blendStockIncreaseBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(updateStockReasonLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
        this.close();
    }//GEN-LAST:event_cancelBtnActionPerformed

	private void blendStockIncreaseBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_blendStockIncreaseBtnActionPerformed
            if (this.blendStockDecreasedBtn.isSelected()) {
                this.blendStockDecreasedBtn.setSelected(false);
            }
    }//GEN-LAST:event_blendStockIncreaseBtnActionPerformed

    private void blendStockDecreasedBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_blendStockDecreasedBtnActionPerformed
        if (this.blendStockIncreaseBtn.isSelected()) {
            this.blendStockIncreaseBtn.setSelected(false);
        }
    }//GEN-LAST:event_blendStockDecreasedBtnActionPerformed

    private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtnActionPerformed
        String changeQty = this.newQtyTxt.getText();
        int stockChangeQty, oldStockQty;

        if (!changeQty.isEmpty()) {

            stockChangeQty = Integer.parseInt(changeQty);
            String reason = this.reasonToChangeTxt.getText();

            if (!reason.isEmpty() && (this.blendStockIncreaseBtn.isSelected() || this.blendStockDecreasedBtn.isSelected())) {

                Blend blend = new Blend();
                blend.setBlendName(this.updateStockItemNameLbl.getText().replace("'", "\\'"));
                blend.setStockUpdateReason(this.reasonToChangeTxt.getText());

                String oldStock = this.stockQtyLbl.getText().replace(" g", "");

                oldStockQty = Integer.parseInt(oldStock);
                blend.setOldStockQty(oldStockQty);
                blend.setUpdatedStockQTy(stockChangeQty);

                if (this.blendStockIncreaseBtn.isSelected()) {
                    blend.setVisibleStock(oldStockQty + stockChangeQty);

                    if (blend.updateStockQty()) {
                        JOptionPane.showMessageDialog(this, "Stock updated successfuly!!!", "Updated Successfully", 1);
                        close();
                    } else {
                        JOptionPane.showMessageDialog(this, "There were some issues with the database. Please contact developers.\n\nError code : UpdateBlendStock 302", "Error", 0);
                        System.exit(0);
                    }
                } else {
                    blend.setVisibleStock(oldStockQty - stockChangeQty);

                    //multiply updatedQty to show minus value
                    blend.setUpdatedStockQTy(blend.getUpdatedStockQTy()*-1);
                    
                    if (blend.getVisibleStock() < 0) {
                        JOptionPane.showMessageDialog(this, "Stock Qty can not be negative!!!", "Invalid Stock Quantity", 2);
                        this.newQtyTxt.setText(null);
                    } else {
                        if (blend.updateStockQty()) {
                            JOptionPane.showMessageDialog(this, "Stock updated successfuly!!!", "Updated Successfully", 1);
                            close();
                        } else {
                            JOptionPane.showMessageDialog(this, "There were some issues with the database. Please contact developers.\n\nError code : UpdateBlendStock 316", "Error", 0);
                            System.exit(0);
                        }
                    }
                }

            } else {
                JOptionPane.showMessageDialog(this, "Please fill all fields before save", "Empty Fields", 2);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please fill all fields before save", "Empty Fields", 2);
        }
    }//GEN-LAST:event_saveBtnActionPerformed

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
            java.util.logging.Logger.getLogger(UpdateBlendStock.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UpdateBlendStock.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UpdateBlendStock.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UpdateBlendStock.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new UpdateBlendStock().setVisible(true);
            }
        });
    }

    private boolean testForInteger(String text) {
        try {
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException e) {
            System.out.println("Num Exception : " + e);
            return false;
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JRadioButton blendStockDecreasedBtn;
    public javax.swing.JRadioButton blendStockIncreaseBtn;
    private javax.swing.JButton cancelBtn;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToggleButton jToggleButton1;
    public javax.swing.JTextField newQtyTxt;
    public javax.swing.JTextArea reasonToChangeTxt;
    private javax.swing.JButton saveBtn;
    public javax.swing.JLabel stockQtyLbl;
    private javax.swing.JLabel updateStockChangeName;
    public javax.swing.JLabel updateStockItemCategoryLbl;
    private javax.swing.JLabel updateStockItemCategoryName;
    private javax.swing.JLabel updateStockItemName;
    public javax.swing.JLabel updateStockItemNameLbl;
    private javax.swing.JLabel updateStockQtyInStockLbl;
    private javax.swing.JLabel updateStockReasonLbl;
    // End of variables declaration//GEN-END:variables
}