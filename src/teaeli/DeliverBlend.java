package teaeli;

import classes.Blend;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class DeliverBlend extends javax.swing.JFrame {

    private int allocatedStockQty, deliverStockQty, unallocatedStockQty, freeQty, sampleStockQty;
    private Object pannel;

    public void setPannel(Object pannel) {
        this.pannel = pannel;
    }

    public DeliverBlend() {
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

        this.allocatedStockQty = 0;
        this.deliverStockQty = 0;
        this.unallocatedStockQty = 0;
        this.freeQty = 0;
        this.sampleStockQty = 0;

        //for validation of deliver qty field
        deliverQtyTxt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String deliverQty = deliverQtyTxt.getText();
                if (deliverQty.length() > 0) {
                    if (!testForInteger(deliverQty)) {
                        JOptionPane.showMessageDialog(deliverQtyTxt, "Deliver quantity must be a valid number",
                                "Invalid Deliver Quantity", 2);
                        deliverQtyTxt.setText(null);
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

        //for validation of unallocate qty field
        unallocateQtyTxt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String unallocateQty = unallocateQtyTxt.getText();
                if (unallocateQty.length() > 0) {
                    if (!testForInteger(unallocateQty)) {
                        JOptionPane.showMessageDialog(unallocateQtyTxt, "Unallocate quantity must be a valid number", "Invalid Unallocate Quantity", 2);
                        unallocateQtyTxt.setText(null);
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

        //for validation of unallocate qty field
        sampleQtyTxt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String sampleQty = sampleQtyTxt.getText();
                if (sampleQty.length() > 0) {
                    if (!testForInteger(sampleQty)) {
                        JOptionPane.showMessageDialog(sampleQtyTxt, "Sample quantity must be a valid number", "Invalid Sample Quantity", 2);
                        sampleQtyTxt.setText(null);
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

        //Prompt confirmation on window close
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                int confirmed = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to close the window?\nAll data you entered will be lost.", "Confirm window close",
                        JOptionPane.YES_NO_OPTION, 2);
                if (confirmed == JOptionPane.YES_OPTION) {
                    dispose();
                }
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

        jScrollPane1 = new javax.swing.JScrollPane();
        deliverNoteTxt = new javax.swing.JTextArea();
        deliverNote = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        blendName = new javax.swing.JLabel();
        blendNameLbl = new javax.swing.JLabel();
        blendCatg = new javax.swing.JLabel();
        blendCatgLbl = new javax.swing.JLabel();
        allocatedQtyLbl = new javax.swing.JLabel();
        freeQtyLbl = new javax.swing.JLabel();
        freeStockQty = new javax.swing.JLabel();
        allocatedQty = new javax.swing.JLabel();
        deliverQty = new javax.swing.JLabel();
        deliverQtyTxt = new javax.swing.JTextField();
        deliverQtyTypeCombo = new javax.swing.JComboBox();
        allocateFreeStockCombo = new javax.swing.JComboBox();
        unallocatingQty = new javax.swing.JLabel();
        unallocateQtyTxt = new javax.swing.JTextField();
        unAllocatingQtyTypeCombo = new javax.swing.JComboBox();
        SampleQty = new javax.swing.JLabel();
        sampleQtyTxt = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        sampleDeliverCombo = new javax.swing.JComboBox();
        cancelBtn = new javax.swing.JButton();
        deliverBtn = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        deliverReasonTxt = new javax.swing.JTextArea();
        deliverReason = new javax.swing.JLabel();

        deliverNoteTxt.setColumns(20);
        deliverNoteTxt.setRows(5);
        jScrollPane1.setViewportView(deliverNoteTxt);

        deliverNote.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        deliverNote.setText("Deliver Note");

        jTextField1.setText("jTextField1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Deliver Blend");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, " Deliver Blend ", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI Semibold", 0, 16))); // NOI18N

        blendName.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        blendName.setText("Blend Name");

        blendNameLbl.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        blendNameLbl.setText("Artisian Blend V2");

        blendCatg.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        blendCatg.setText("Blend Category");

        blendCatgLbl.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        blendCatgLbl.setText("Almond Truffle - V1 ");

        allocatedQtyLbl.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        allocatedQtyLbl.setText("100 g");

        freeQtyLbl.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        freeQtyLbl.setText("50 g");

        freeStockQty.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        freeStockQty.setText("Buffer Stock");

        allocatedQty.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        allocatedQty.setText("Allocated Qty In Stock");

        deliverQty.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        deliverQty.setText("Delivering Qty");

        deliverQtyTxt.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        deliverQtyTypeCombo.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        deliverQtyTypeCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "g", "kg" }));

        allocateFreeStockCombo.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        allocateFreeStockCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "No", "Yes", "Other" }));
        allocateFreeStockCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                allocateFreeStockComboActionPerformed(evt);
            }
        });

        unallocatingQty.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        unallocatingQty.setText("Allocate buffer to free stock ?");

        unallocateQtyTxt.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        unallocateQtyTxt.setToolTipText("");
        unallocateQtyTxt.setEnabled(false);

        unAllocatingQtyTypeCombo.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        unAllocatingQtyTypeCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "g", "kg" }));
        unAllocatingQtyTypeCombo.setEnabled(false);

        SampleQty.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        SampleQty.setText("Sample Deliver");
        SampleQty.setToolTipText("");

        sampleQtyTxt.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        sampleQtyTxt.setEnabled(false);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setText("(g)");
        jLabel1.setEnabled(false);

        sampleDeliverCombo.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        sampleDeliverCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "No", "Yes" }));
        sampleDeliverCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sampleDeliverComboActionPerformed(evt);
            }
        });

        cancelBtn.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cancelBtn.setText("Cancel");
        cancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtnActionPerformed(evt);
            }
        });

        deliverBtn.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        deliverBtn.setText("Deliver");
        deliverBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deliverBtnActionPerformed(evt);
            }
        });

        deliverReasonTxt.setColumns(20);
        deliverReasonTxt.setRows(5);
        jScrollPane2.setViewportView(deliverReasonTxt);

        deliverReason.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        deliverReason.setText("Deliver Note");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(deliverReason)
                    .addComponent(blendCatg)
                    .addComponent(blendName)
                    .addComponent(allocatedQty)
                    .addComponent(freeStockQty)
                    .addComponent(deliverQty)
                    .addComponent(unallocatingQty)
                    .addComponent(SampleQty))
                .addGap(38, 38, 38)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(unallocateQtyTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(unAllocatingQtyTypeCombo, 0, 1, Short.MAX_VALUE))
                        .addComponent(blendCatgLbl)
                        .addComponent(blendNameLbl)
                        .addComponent(allocatedQtyLbl)
                        .addComponent(freeQtyLbl)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(deliverQtyTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(deliverQtyTypeCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(allocateFreeStockCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(sampleDeliverCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(sampleQtyTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jLabel1)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(56, 56, 56)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cancelBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(deliverBtn)
                .addGap(68, 68, 68))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(blendName, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(blendNameLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(blendCatg, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(blendCatgLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(allocatedQty, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(allocatedQtyLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(freeStockQty, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(freeQtyLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(deliverQty, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(deliverQtyTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(deliverQtyTypeCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(allocateFreeStockCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(unallocatingQty, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(unallocateQtyTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(unAllocatingQtyTypeCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(SampleQty, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sampleDeliverCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sampleQtyTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(deliverReason, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(deliverBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
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

    private void allocateFreeStockComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_allocateFreeStockComboActionPerformed

        //enabled unallocate qty text field on selection of other
        if (allocateFreeStockCombo.getSelectedIndex() == 2) {
            unallocateQtyTxt.setEnabled(true);
            unAllocatingQtyTypeCombo.setEnabled(true);
        } else {
            unallocateQtyTxt.setEnabled(false);
            unAllocatingQtyTypeCombo.setEnabled(false);
        }
    }//GEN-LAST:event_allocateFreeStockComboActionPerformed

    private void sampleDeliverComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sampleDeliverComboActionPerformed

        //enabled sample qty text field on selection of yes
        if (sampleDeliverCombo.getSelectedIndex() == 1) {
            sampleQtyTxt.setEnabled(true);
            jLabel1.setEnabled(true);
        } else {
            sampleQtyTxt.setEnabled(false);
            jLabel1.setEnabled(false);
        }
    }//GEN-LAST:event_sampleDeliverComboActionPerformed

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed

        int confirmed = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to close the window?\nAll data you entered will be lost.", "Confirm window close",
                JOptionPane.YES_NO_OPTION, 2);
        if (confirmed == JOptionPane.YES_OPTION) {
            dispose();
        }
    }//GEN-LAST:event_cancelBtnActionPerformed

    private void deliverBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deliverBtnActionPerformed
        
        deliverStockQty = 0;
        unallocatedStockQty = 0;
        sampleStockQty = 0;
        
        if(testForInteger(deliverQtyTxt.getText())) 
            deliverStockQty = Integer.parseInt(deliverQtyTxt.getText());
        
        if( (allocateFreeStockCombo.getSelectedIndex() == 2) && testForInteger(unallocateQtyTxt.getText()))
            unallocatedStockQty = Integer.parseInt(unallocateQtyTxt.getText());
        
        if( (sampleDeliverCombo.getSelectedIndex() == 1) && testForInteger(sampleQtyTxt.getText()))
            sampleStockQty = Integer.parseInt(sampleQtyTxt.getText());

        if (deliverStockQty > 0) {

            allocatedStockQty = Integer.parseInt(allocatedQtyLbl.getText().replace(" g", ""));
            freeQty = Integer.parseInt(freeQtyLbl.getText().replace(" g", ""));

            if (!deliverQtyCheck()) {
                JOptionPane.showMessageDialog(this, "Deliver quantity should be less than or equal to "
                        + allocatedStockQty + " g. ", "Stock Quantiy Exceeds", 2);
                deliverQtyTxt.setText(null);
            } else {

                if (!unallocateQtyCheck()) {
                    JOptionPane.showMessageDialog(this, "Unallocating quantity should be less than or equal to "
                            + (allocatedStockQty - deliverStockQty) + " g. ", "Stock Quantiy Exceeds", 2);
                    unallocateQtyTxt.setText(null);
                    
                } else {
                    if (!smapleQtyCheck()) {
                        JOptionPane.showMessageDialog(this, "Sample quantity exceeds free stock quantity", "Stock Quantiy Exceeds", 2);
                        unallocateQtyTxt.setText(null);
                    } else {
                        Blend blendDeliver = new Blend();

                        blendDeliver.setBlendName(blendNameLbl.getText().replace("'", "\\'"));

                        //set old stock qty and updated qty for blend deliver history
                        blendDeliver.setOldStockQty(allocatedStockQty);
                        blendDeliver.setUpdatedStockQTy(deliverStockQty);
                        blendDeliver.setStockUpdateReason(deliverReasonTxt.getText());

                        blendDeliver.setDeliverQty(deliverStockQty);

                        //set deliver remove qty if allocate to free stock combo is Yes or other
                        if (allocateFreeStockCombo.getSelectedIndex() == 1) {

                            unallocatedStockQty = allocatedStockQty - deliverStockQty;
                            blendDeliver.setDelRemoveQty(unallocatedStockQty);

                        } else if (allocateFreeStockCombo.getSelectedIndex() == 2) {
                            blendDeliver.setDelRemoveQty(unallocatedStockQty);
                        }

                        //set sample qty if sample delive combo is yes
                        if (sampleDeliverCombo.getSelectedIndex() == 1) {
                            blendDeliver.setSampleQty(sampleStockQty);
                        }

                        //set new ordered stock
                        int leftAlocatedStock = allocatedStockQty - (deliverStockQty + unallocatedStockQty);
                        blendDeliver.setAlocatedStock(leftAlocatedStock);

                        //set new visible stock
                        blendDeliver.setVisibleStock((freeQty + unallocatedStockQty) - sampleStockQty);

                        if (blendDeliver.updateDeliverDetails()) {
                            JOptionPane.showMessageDialog(this, "Updated Successfuly !", "Update Success", 1);
                            close();
                        } else {
                            JOptionPane.showMessageDialog(this, "There were some issues with the database. Please contact developers.\n\nError code : DeliverBlend 484", "Error", 0);
                            System.exit(0);
                        }
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please add deliver amount to deliver", "Empty Fields", 2);
        }
    }//GEN-LAST:event_deliverBtnActionPerformed

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
            java.util.logging.Logger.getLogger(DeliverBlend.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new DeliverBlend().setVisible(true);
            }
        });
    }

    //method to test for integer values
    private boolean testForInteger(String text) {
        try {
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException e) {
            System.out.println("Num Exception : " + e);
            return false;
        }
    }

    private boolean deliverQtyCheck() {

        if (deliverQtyTypeCombo.getSelectedIndex() == 1) {
            deliverStockQty *= 1000;
        }

        return (deliverStockQty <= allocatedStockQty);
    }

    private boolean unallocateQtyCheck() {

        int leftQty = allocatedStockQty - deliverStockQty;

        if (unAllocatingQtyTypeCombo.getSelectedIndex() == 1) {
            unallocatedStockQty *= 1000;
        }

        return (unallocatedStockQty <= leftQty);
    }

    private boolean smapleQtyCheck() {

        int totalFreeStock = freeQty + unallocatedStockQty;

        return (sampleStockQty <= totalFreeStock);

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel SampleQty;
    public javax.swing.JComboBox allocateFreeStockCombo;
    private javax.swing.JLabel allocatedQty;
    public javax.swing.JLabel allocatedQtyLbl;
    private javax.swing.JLabel blendCatg;
    public javax.swing.JLabel blendCatgLbl;
    private javax.swing.JLabel blendName;
    public javax.swing.JLabel blendNameLbl;
    public javax.swing.JButton cancelBtn;
    public javax.swing.JButton deliverBtn;
    private javax.swing.JLabel deliverNote;
    public javax.swing.JTextArea deliverNoteTxt;
    private javax.swing.JLabel deliverQty;
    public javax.swing.JTextField deliverQtyTxt;
    public javax.swing.JComboBox deliverQtyTypeCombo;
    private javax.swing.JLabel deliverReason;
    public javax.swing.JTextArea deliverReasonTxt;
    public javax.swing.JLabel freeQtyLbl;
    private javax.swing.JLabel freeStockQty;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField1;
    public javax.swing.JComboBox sampleDeliverCombo;
    public javax.swing.JTextField sampleQtyTxt;
    public javax.swing.JComboBox unAllocatingQtyTypeCombo;
    public javax.swing.JTextField unallocateQtyTxt;
    private javax.swing.JLabel unallocatingQty;
    // End of variables declaration//GEN-END:variables
}
