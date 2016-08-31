package teaeli;

import classes.Blend;
import classes.DBConnection;
import classes.Ingredient;
import classes.Validation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
//import sun.org.mozilla.javascript.internal.Token;

public class AddNewBlend extends javax.swing.JFrame {

    private Ingredient ingredient1;
    private Blend blend;
    public AdminPannel adminpanel;

    DBConnection dbConn = DBConnection.getInstance();

    public AddNewBlend() {

        //Setting icon
        ImageIcon img = new ImageIcon("src\\img\\icon-1.png");
        this.setIconImage(img.getImage());
        
        initComponents();
        
        Dimension screenSize, frameSize;
        int x, y;
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frameSize = getSize();
        x = (screenSize.width - frameSize.width)/2;
        y = (screenSize.height - frameSize.height)/2;
        setLocation(x, y);
        setResizable(false);

        //Changing table headers to bold
        addNewBlendFlavourTbl.getTableHeader().setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        addNewBlendIngTbl.getTableHeader().setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));

        setResizable(false);

        Ingredient ingredient = new Ingredient();
        ingredient.initIngCombo(ingCombo);

        Ingredient base = new Ingredient();
        base.initIngCombo(baseCombo);

        Ingredient flavour = new Ingredient();
        flavour.initFlavourCombo(flavourCombo);

        ingredient1 = new Ingredient();
        blend = new Blend();

        ingCombo.setSelectedIndex(-1);
        flavourCombo.setSelectedIndex(-1);
        blendCategoryCombo.setSelectedIndex(-1);
        //baseCombo.setSelectedIndex(-1);

        //setting focus to ing per txt when item selected
        ingCombo.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    ingPerTxt.requestFocus();
                }
            }
        });
        
        //Validation ingredient percentage, when key released
        ingPerTxt.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String ingPercentage = ingPerTxt.getText();
                if (ingPercentage.length() > 0) {
                    if (!(new Validation().isFloatPure(ingPercentage))) {
                        JOptionPane.showMessageDialog(ingPerTxt, "Ingredient percentage must be a valid number.", "Invalid ingredient percentage", 2);
                        ingPerTxt.setText(ingPercentage.substring(0, ingPercentage.length() - 1));
                    } else if (Float.parseFloat(ingPercentage) < 0) {
                        JOptionPane.showMessageDialog(ingPerTxt, "Ingredient percentage cannot be less than 0.", "Invalid ingredient percentage", 2);
                        ingPerTxt.setText(ingPercentage.substring(0, ingPercentage.length() - 1));
                    }
                }
            }

            public void keyTyped(KeyEvent e) {}

            public void keyPressed(KeyEvent e) {}
        });
        
        //Validation flavour percentage, when key released
        flavourPerTxt.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String flvPercentage = flavourPerTxt.getText();
                if (flvPercentage.length() > 0) {
                    if (!(new Validation().isFloatPure(flvPercentage))) {
                        JOptionPane.showMessageDialog(flavourPerTxt, "Flavour percentage must be a valid number.", "Invalid flavour percentage", 2);
                        flavourPerTxt.setText(flvPercentage.substring(0, flvPercentage.length() - 1));
                    } else if (Float.parseFloat(flvPercentage) < 0) {
                        JOptionPane.showMessageDialog(flavourPerTxt, "Flavour percentage cannot be less than 0.", "Invalid flavour percentage", 2);
                        flavourPerTxt.setText(flvPercentage.substring(0, flvPercentage.length() - 1));
                    }
                }
            }

            public void keyTyped(KeyEvent e) {}

            public void keyPressed(KeyEvent e) {}
        });

        //setting focus to flavour per txt when item selected
        flavourCombo.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    flavourPerTxt.requestFocus();
                }
            }
        });
        
        blendCodeTxt.requestFocus();
        
        //setting focus to base combo when blend category is selected
        blendCategoryCombo.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    baseCombo.requestFocus();
                }
            }
        });
        
        //setting focus to ing combo when base ing is selected
        baseCombo.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    ingCombo.requestFocus();
                }
            }
        });
        
        //enabling delete button on row select
        final ListSelectionModel ingModel = addNewBlendIngTbl.getSelectionModel();
        ingModel.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent lse) {
                if (!ingModel.isSelectionEmpty()) {
                    dltIngBtn.setEnabled(true);
                }
            }
        });
        final ListSelectionModel flvModel = addNewBlendFlavourTbl.getSelectionModel();
        flvModel.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent lse) {
                if (!flvModel.isSelectionEmpty()) {
                    dltFlavourBtn.setEnabled(true);
                }
            }
        });
        
    }

    //End of the Constructor

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    //method to refresh related tables and close this window
    private void close() {
        adminpanel.initSettingsBlendCombo();
        adminpanel.populateProductTable();
        adminpanel.initStockBlendCombo();
        adminpanel.populateBlendStockTable();
        this.setVisible(false);
        this.dispose();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        cancelBtn = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        blendNameTxt = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        flavourPerTxt = new javax.swing.JTextField();
        ingPerTxt = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        ingPerAddBtn = new javax.swing.JButton();
        flavourPerAddBtn = new javax.swing.JButton();
        addNewBlendBtn = new javax.swing.JButton();
        blendCodeTxt = new javax.swing.JTextField();
        ingCombo = new javax.swing.JComboBox();
        flavourCombo = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        addNewBlendFlavourTbl = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        addNewBlendIngTbl = new javax.swing.JTable();
        baseCombo = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        blendCategoryCombo = new javax.swing.JComboBox();
        dltIngBtn = new javax.swing.JButton();
        dltFlavourBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Add New Blend");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, " Add New Blend ", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI Semibold", 0, 14))); // NOI18N

        cancelBtn.setText("Cancel");
        cancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtnActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Add Flavours");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Add Ingredients");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Blend Name");

        blendNameTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                blendNameTxtActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Blend Code");

        flavourPerTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                flavourPerTxtActionPerformed(evt);
            }
        });

        ingPerTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ingPerTxtActionPerformed(evt);
            }
        });

        jLabel7.setText("%");

        jLabel6.setText("%");

        ingPerAddBtn.setText("Add");
        ingPerAddBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ingPerAddBtnActionPerformed(evt);
            }
        });

        flavourPerAddBtn.setText("Add");
        flavourPerAddBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                flavourPerAddBtnActionPerformed(evt);
            }
        });

        addNewBlendBtn.setText("Add New");
        addNewBlendBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addNewBlendBtnActionPerformed(evt);
            }
        });

        blendCodeTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                blendCodeTxtActionPerformed(evt);
            }
        });

        ingCombo.setEditable(true);
        ingCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        ingCombo.setSelectedIndex(-1);
        ingCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ingComboActionPerformed(evt);
            }
        });

        flavourCombo.setEditable(true);
        flavourCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        flavourCombo.setSelectedIndex(-1);
        flavourCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                flavourComboActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Base Composition ");

        addNewBlendFlavourTbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Flavour", "Precentage (%)"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        addNewBlendFlavourTbl.setRowHeight(24);
        jScrollPane3.setViewportView(addNewBlendFlavourTbl);

        addNewBlendIngTbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Ingredient", "Precentage (%)"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        addNewBlendIngTbl.setRowHeight(24);
        jScrollPane4.setViewportView(addNewBlendIngTbl);

        baseCombo.setEditable(true);
        baseCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                baseComboActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setText("Category");

        blendCategoryCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Artisian Special V1", "Teaeli Organic", "Teaeli Iced Teas", "TFM" }));
        blendCategoryCombo.setToolTipText("");
        blendCategoryCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                blendCategoryComboActionPerformed(evt);
            }
        });

        dltIngBtn.setText("Delete Ingredient");
        dltIngBtn.setEnabled(false);
        dltIngBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dltIngBtnActionPerformed(evt);
            }
        });

        dltFlavourBtn.setText("Delete Flavour");
        dltFlavourBtn.setEnabled(false);
        dltFlavourBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dltFlavourBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(cancelBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(addNewBlendBtn))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(dltIngBtn)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(ingCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(ingPerTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel6)
                                        .addGap(46, 46, 46)
                                        .addComponent(ingPerAddBtn))
                                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(dltFlavourBtn))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(41, 41, 41)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(flavourCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jLabel4))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(flavourPerTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel7)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(flavourPerAddBtn))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel5)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(baseCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(blendNameTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel8)
                                        .addGap(18, 18, 18)
                                        .addComponent(blendCategoryCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel3)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(blendCodeTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(blendCodeTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(blendNameTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(blendCategoryCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(baseCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ingCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ingPerTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(ingPerAddBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(flavourCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(flavourPerTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(flavourPerAddBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 367, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dltIngBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dltFlavourBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addNewBlendBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void blendNameTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_blendNameTxtActionPerformed
        blendCategoryCombo.requestFocus();
    }//GEN-LAST:event_blendNameTxtActionPerformed

    private void flavourPerTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_flavourPerTxtActionPerformed
        flavourPerAddBtn.requestFocus();
    }//GEN-LAST:event_flavourPerTxtActionPerformed

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_cancelBtnActionPerformed

    private void blendCodeTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_blendCodeTxtActionPerformed
        blendNameTxt.requestFocus();
    }//GEN-LAST:event_blendCodeTxtActionPerformed

    private void ingPerAddBtnActionPerformed(java.awt.event.ActionEvent evt) {                                             
        if (ingCombo.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(ingCombo, "Please select an ingredient to add.", "Empty Ingredient Selection", 2);
            ingCombo.requestFocus();
            return;
        }
        if (ingPerTxt.getText().equals("")) {
            JOptionPane.showMessageDialog(ingPerTxt, "Please enter an ingredient percentage to add.", "Empty Ingredient Percentage", 2);
            ingPerTxt.requestFocus();
            return;
        }
        String ingName = (String) ingCombo.getSelectedItem();
        float ingPer = Float.parseFloat(ingPerTxt.getText());
        boolean isNew = true;
        //Search if the ingredient is already added
        int rowCount = addNewBlendIngTbl.getRowCount();
        float totalPercentage = ingPer;
        for (int i = 0; i < rowCount; i++) {
            if (ingName.equals(addNewBlendIngTbl.getValueAt(i, 0))) {
                isNew = false;
            }
            totalPercentage += Float.parseFloat(addNewBlendIngTbl.getValueAt(i, 1).toString());
        }
        if (totalPercentage > 99) {
            JOptionPane.showMessageDialog(ingPerTxt, "Total of ingredient percentages cannot be greater than 100.", "Invalid Ingredient Percentages", 2);
            ingPerTxt.setText("");
            ingPerTxt.requestFocus();
            return;
        }
        if (isNew) {
            Vector newRow = new Vector();
            newRow.addElement(ingName);
            newRow.addElement(ingPer);

            DefaultTableModel model = (DefaultTableModel) addNewBlendIngTbl.getModel();
            model.addRow(newRow);
        } else {
            int dialogResult = JOptionPane.showConfirmDialog(addNewBlendIngTbl, "Ingredient already exists. Do you wish to update the precentage?", "Confirm Update", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (dialogResult == JOptionPane.YES_OPTION){
                for (int i = 0; i < addNewBlendIngTbl.getRowCount(); i++) {
                    if (ingName.equals(addNewBlendIngTbl.getValueAt(i, 0))) {
                        float oldPercentage = Float.parseFloat(addNewBlendIngTbl.getValueAt(i, 1).toString());
                        addNewBlendIngTbl.setValueAt(ingPer + oldPercentage, i, 1);
                    }
                }
            }
        }
        
        ingPerTxt.setText("");
        ingCombo.setSelectedIndex(-1);
        ingCombo.requestFocus();
    }

    private void flavourComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_flavourComboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_flavourComboActionPerformed

    private void flavourPerAddBtnActionPerformed(java.awt.event.ActionEvent evt) {                                                 
        if (flavourCombo.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(flavourCombo, "Please select a flavour to add.", "Empty Flavour Selection", 2);
            flavourCombo.requestFocus();
            return;
        }
        if (flavourPerTxt.getText().equals("")) {
            JOptionPane.showMessageDialog(flavourPerTxt, "Please enter a flavour percentage to add.", "Empty Flavour Percentage", 2);
            flavourPerTxt.requestFocus();
            return;
        }
        String flavourName = (String) flavourCombo.getSelectedItem();
        float flavourPer = Float.parseFloat(flavourPerTxt.getText());
        boolean isNew = true;
        //Search if the ingredient is already added
        int rowCount = addNewBlendFlavourTbl.getRowCount();
        float totalPercentage = flavourPer;
        for (int i = 0; i < rowCount; i++) {
            if (flavourName.equals(addNewBlendFlavourTbl.getValueAt(i, 0))) {
                isNew = false;
            }
            totalPercentage += Float.parseFloat(addNewBlendFlavourTbl.getValueAt(i, 1).toString());
        }
        if (totalPercentage > 99) {
            JOptionPane.showMessageDialog(flavourPerTxt, "Total of flavour percentages cannot be greater than 100.", "Invalid Flavour Percentages", 2);
            flavourPerTxt.setText("");
            flavourPerTxt.requestFocus();
            return;
        }
        if (isNew) {
            Vector newRow = new Vector();
            newRow.addElement(flavourName);
            newRow.addElement(flavourPer);

            DefaultTableModel model = (DefaultTableModel) addNewBlendFlavourTbl.getModel();
            model.addRow(newRow);
        } else {
            int dialogResult = JOptionPane.showConfirmDialog(addNewBlendFlavourTbl, "Flavour already exists. Do you wish to update the precentage?", "Confirm Update", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (dialogResult == JOptionPane.YES_OPTION){
                for (int i = 0; i < addNewBlendFlavourTbl.getRowCount(); i++) {
                    if (flavourName.equals(addNewBlendFlavourTbl.getValueAt(i, 0))) {
                        addNewBlendFlavourTbl.setValueAt(flavourPer, i, 1);
                    }
                }
            }
        }
        
        flavourPerTxt.setText("");
        flavourCombo.setSelectedIndex(-1);
        flavourCombo.requestFocus();

    }


    private void blendCategoryComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_blendCategoryComboActionPerformed
       
    }//GEN-LAST:event_blendCategoryComboActionPerformed

    String blendID, blendName, blendCategory, base;

    private void addNewBlendBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNewBlendBtnActionPerformed
        
        //validation
        if (blendCodeTxt.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please add a blend code.", "Empty Fields", 2);
            blendCodeTxt.requestFocus();
            return;
        } else if (blend.checkExistingBlendID(blendCodeTxt.getText()) == 1) {
            JOptionPane.showMessageDialog(this, "Blend code already exists.", "Error", 0);
            blendCodeTxt.requestFocus();
            return;
        }
        
        if (blendNameTxt.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please add a blend name.", "Empty Fields", 2);
            blendNameTxt.requestFocus();
            return;
        } else if (blend.checkExistingBlendName(blendNameTxt.getText()) == 1) {
            JOptionPane.showMessageDialog(this, "Blend name already exists.", "Error", 0);
            blendNameTxt.requestFocus();
            return;
        }
        
        if (blendCategoryCombo.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Please select a blend category.", "Empty Fields", 2);
            blendCategoryCombo.requestFocus();
            return;
        }
        
        if (baseCombo.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Please select a base composition.", "Empty Fields", 2);
            baseCombo.requestFocus();
            return;
        }
        
        
        blendID = blendCodeTxt.getText();
        blendName = blendNameTxt.getText();
        
        blendCategory = blendCategoryCombo.getSelectedItem().toString();
        base = baseCombo.getSelectedItem().toString();

        int ingCount = addNewBlendIngTbl.getRowCount();
        int flavCount = addNewBlendFlavourTbl.getRowCount();

        if (ingCount == 0) {
            JOptionPane.showMessageDialog(this, "A blend must have at least one ingredient", "No Ingredients Added", 2);
            ingCombo.requestFocus();
            return;
        }
        
        //Adding blend to blend table
        blend.addNewBlend(blendID, blendName, base, blendCategory);

        //Adding ingredients to recipie table
        for (int i = 0; i < ingCount; i++) {
            int ingID = blend.getIngIDRecByIngName(addNewBlendIngTbl.getValueAt(i, 0).toString());
            String ingPercentage = addNewBlendIngTbl.getValueAt(i, 1).toString();
            String[] data = {blendID, String.valueOf(ingID), ingPercentage, "0"};
            blend.addRecipie(data);
        }
        
        //Adding flavours to recipie table
        for (int i = 0; i < flavCount; i++) {
            int flavourID = blend.getIngIDRecByIngName(addNewBlendFlavourTbl.getValueAt(i, 0).toString());
            String flvPercentage = addNewBlendFlavourTbl.getValueAt(i, 1).toString();
            String[] data = {blendID, String.valueOf(flavourID), flvPercentage, "1"};
            blend.addRecipie(data);
        }
        
        JOptionPane.showMessageDialog(this, "Blend added succesfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        close();
    }//GEN-LAST:event_addNewBlendBtnActionPerformed

    private void ingPerTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ingPerTxtActionPerformed
        ingPerAddBtn.requestFocus();
    }//GEN-LAST:event_ingPerTxtActionPerformed

    private void baseComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_baseComboActionPerformed
        
    }//GEN-LAST:event_baseComboActionPerformed

    private void ingComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ingComboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ingComboActionPerformed

    private void dltIngBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dltIngBtnActionPerformed
        int dialogResult = JOptionPane.showConfirmDialog(addNewBlendIngTbl, "Please confirm record deletion", "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (dialogResult == JOptionPane.YES_OPTION){
            DefaultTableModel model = (DefaultTableModel)addNewBlendIngTbl.getModel();
            model.removeRow(addNewBlendIngTbl.getSelectedRow());
            dltIngBtn.setEnabled(false);
        }
    }//GEN-LAST:event_dltIngBtnActionPerformed

    private void dltFlavourBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dltFlavourBtnActionPerformed
        int dialogResult = JOptionPane.showConfirmDialog(addNewBlendFlavourTbl, "Please confirm record deletion", "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (dialogResult == JOptionPane.YES_OPTION){
            DefaultTableModel model = (DefaultTableModel)addNewBlendFlavourTbl.getModel();
            model.removeRow(addNewBlendFlavourTbl.getSelectedRow());
            dltFlavourBtn.setEnabled(false);
        }
    }//GEN-LAST:event_dltFlavourBtnActionPerformed

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
            java.util.logging.Logger.getLogger(AddNewBlend.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddNewBlend.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddNewBlend.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddNewBlend.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AddNewBlend().setVisible(true);
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addNewBlendBtn;
    private javax.swing.JTable addNewBlendFlavourTbl;
    private javax.swing.JTable addNewBlendIngTbl;
    private javax.swing.JComboBox baseCombo;
    private javax.swing.JComboBox blendCategoryCombo;
    private javax.swing.JTextField blendCodeTxt;
    private javax.swing.JTextField blendNameTxt;
    private javax.swing.JButton cancelBtn;
    private javax.swing.JButton dltFlavourBtn;
    private javax.swing.JButton dltIngBtn;
    private javax.swing.JComboBox flavourCombo;
    private javax.swing.JButton flavourPerAddBtn;
    private javax.swing.JTextField flavourPerTxt;
    private javax.swing.JComboBox ingCombo;
    private javax.swing.JButton ingPerAddBtn;
    private javax.swing.JTextField ingPerTxt;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    // End of variables declaration//GEN-END:variables
}
