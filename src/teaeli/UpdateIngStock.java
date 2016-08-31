package teaeli;

import classes.Ingredient;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class UpdateIngStock extends javax.swing.JFrame {

    public UpdateIngStock() {
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
                    if (!testForFloat(newQty)) {
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

    private Object pannel;

    public void setPannel(Object pannel) {
        this.pannel = pannel;
    }

    //method to refresh related tables and close this window
    private void close() {
        this.setVisible(false);
        if ("teaeli.AdminPannel".equals(pannel.getClass().getName())) {
            AdminPannel adminPannel = (AdminPannel) pannel;
            adminPannel.populateIngStockTable();
            adminPannel.populateIngHistoryTable();
        } else if ("teaeli.ManagerPannel".equals(pannel.getClass().getName())) {
            ManagerPannel managerPannel = (ManagerPannel) pannel;
            managerPannel.populateIngStockTable();
        }
        this.dispose();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        updateStockReasonName = new javax.swing.JLabel();
        updateStockQtyInStockName = new javax.swing.JLabel();
        newQtyTxt = new javax.swing.JTextField();
        saveBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        reasonTxt = new javax.swing.JTextArea();
        updateStockItemNameLbl = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        cancelBtn = new javax.swing.JButton();
        updateStockItemName = new javax.swing.JLabel();
        stockQtyLbl = new javax.swing.JLabel();
        updateStockChangeName = new javax.swing.JLabel();
        updateStockCategoryName = new javax.swing.JLabel();
        updateStockCategoryLbl = new javax.swing.JLabel();
        stockIncreasedBtn = new javax.swing.JRadioButton();
        stockDecreaseBtn = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Update Ingredient Stock");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, " Update Ingredient Stock ", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI Semibold", 0, 16))); // NOI18N

        updateStockReasonName.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        updateStockReasonName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        updateStockReasonName.setText("Reason to Change");

        updateStockQtyInStockName.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        updateStockQtyInStockName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        updateStockQtyInStockName.setText("Qty In Stock");

        saveBtn.setText("Save");
        saveBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveBtnActionPerformed(evt);
            }
        });

        reasonTxt.setColumns(20);
        reasonTxt.setRows(5);
        jScrollPane1.setViewportView(reasonTxt);

        updateStockItemNameLbl.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        updateStockItemNameLbl.setText("Aloe Vera C/cut ");

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
        updateStockItemName.setText("Ingredient Name");

        stockQtyLbl.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        stockQtyLbl.setText("250 g");

        updateStockChangeName.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        updateStockChangeName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        updateStockChangeName.setText("Changing Qty");

        updateStockCategoryName.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        updateStockCategoryName.setText("Ingredient Cateogry");

        updateStockCategoryLbl.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        updateStockCategoryLbl.setText("Tea");

        stockIncreasedBtn.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        stockIncreasedBtn.setText("increased");
        stockIncreasedBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stockIncreasedBtnActionPerformed(evt);
            }
        });

        stockDecreaseBtn.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        stockDecreaseBtn.setText("decreased");
        stockDecreaseBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stockDecreaseBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(updateStockItemName)
                    .addComponent(updateStockCategoryName)
                    .addComponent(updateStockQtyInStockName, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(updateStockReasonName)
                    .addComponent(updateStockChangeName, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(stockQtyLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(stockIncreasedBtn)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(stockDecreaseBtn))
                                    .addComponent(newQtyTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                        .addComponent(cancelBtn)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(saveBtn))
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(updateStockItemNameLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(updateStockCategoryLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                    .addComponent(updateStockCategoryName, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(updateStockCategoryLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(stockQtyLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(updateStockQtyInStockName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(newQtyTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(updateStockChangeName, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(stockIncreasedBtn)
                    .addComponent(stockDecreaseBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(updateStockReasonName, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveBtn)
                    .addComponent(cancelBtn))
                .addContainerGap(15, Short.MAX_VALUE))
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
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_cancelBtnActionPerformed

    private void stockIncreasedBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stockIncreasedBtnActionPerformed
        if (this.stockDecreaseBtn.isSelected()) {
            this.stockDecreaseBtn.setSelected(false);
        }
    }//GEN-LAST:event_stockIncreasedBtnActionPerformed

    private void stockDecreaseBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stockDecreaseBtnActionPerformed
        if (this.stockIncreasedBtn.isSelected()) {
            this.stockIncreasedBtn.setSelected(false);
        }
    }//GEN-LAST:event_stockDecreaseBtnActionPerformed

    private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtnActionPerformed

        String changeQty = this.newQtyTxt.getText();
        float stockChangeQty, oldStockQty;

        if (!changeQty.isEmpty()) {

            //set  new updating qty and reason
            stockChangeQty = Float.parseFloat(changeQty);
            String reason = this.reasonTxt.getText();

            if (!reason.isEmpty() && (this.stockIncreasedBtn.isSelected() || this.stockDecreaseBtn.isSelected())) {

                //create ing object and call for update
                Ingredient ingredient = new Ingredient();
                ingredient.setIngName(this.updateStockItemNameLbl.getText().replace("'", "\\'"));
                ingredient.setStockUpdateReason(this.reasonTxt.getText());

                String oldStock = this.stockQtyLbl.getText().replace(" g", "");

                oldStockQty = Float.parseFloat(oldStock);
                ingredient.setOldStockQty(oldStockQty);
                ingredient.setUpdatedStockQTy(stockChangeQty);

                if (this.stockIncreasedBtn.isSelected()) {
                    ingredient.setVisibleStock(oldStockQty + stockChangeQty);

                    if (ingredient.updateStockQty()) {
                        JOptionPane.showMessageDialog(this, "Stock updated successfuly!!!", "Updated Successfully", 1);
                        close();
                    } else {
                        JOptionPane.showMessageDialog(this, "There were some issues with the database. Please contact developers.\n\nError code : UpdateIngStock 298", "Error", 0);
                        System.exit(0);
                    }
                } else {
                    ingredient.setVisibleStock(oldStockQty - stockChangeQty);
                    
                    //multiply updatedQty to show minus value
                    ingredient.setUpdatedStockQTy(ingredient.getUpdatedStockQTy()*-1);
                    
                    if (ingredient.getVisibleStock() < 0) {
                        JOptionPane.showMessageDialog(this, "Stock Qty can not be negative !", "Invalid Stock Quantity", 2);
                        this.newQtyTxt.setText(null);
                    } else {
                        if (ingredient.updateStockQty()) {
                            JOptionPane.showMessageDialog(this, "Stock updated successfuly!!!", "Updated Successfully", 1);
                            close();
                        } else {
                            JOptionPane.showMessageDialog(this, "There were some issues with the database. Please contact developers.\n\nError code : UpdateIngStock 312", "Error", 0);
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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UpdateIngStock.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new UpdateIngStock().setVisible(true);
            }
        });
    }

    /* method to test for float */
    private boolean testForFloat(String text) {
        try {
            Float.parseFloat(text);
            return true;
        } catch (NumberFormatException e) {
            System.out.println("Num Exception : " + e);
            return false;
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelBtn;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JTextField newQtyTxt;
    public javax.swing.JTextArea reasonTxt;
    private javax.swing.JButton saveBtn;
    public javax.swing.JRadioButton stockDecreaseBtn;
    public javax.swing.JRadioButton stockIncreasedBtn;
    public javax.swing.JLabel stockQtyLbl;
    public javax.swing.JLabel updateStockCategoryLbl;
    private javax.swing.JLabel updateStockCategoryName;
    private javax.swing.JLabel updateStockChangeName;
    private javax.swing.JLabel updateStockItemName;
    public javax.swing.JLabel updateStockItemNameLbl;
    private javax.swing.JLabel updateStockQtyInStockName;
    private javax.swing.JLabel updateStockReasonName;
    // End of variables declaration//GEN-END:variables
}
