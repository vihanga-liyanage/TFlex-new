package teaeli;

import classes.Ingredient;
import classes.PDF;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class OrderConfirmation extends javax.swing.JFrame {

    public Object pannel;
    Ingredient ing = new Ingredient();
    List<List> mainList;
    List<List> mainList2;
    Set<String> supplierList;
    List totalList;
    List taxList;
    List discountList;
    PDF pdf;
    String orderID = "";

    private CreateNewBlendOrder2 createNewBlendOrder2;

    /**
     * Creates new form OrderConfirmation
     *
     * @param cnb
     * @param orderID
     */
    public OrderConfirmation(CreateNewBlendOrder2 cnb, String orderID) { // pass CreateNewBlendOrder2 object to get the master list in the interface
        //Setting icon
        ImageIcon img = new ImageIcon("src\\img\\icon-1.png");
        this.setIconImage(img.getImage());
        
        initComponents();
        setSize(1058, 580);
        Dimension screenSize, frameSize;
        int x, y;
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frameSize = getSize();
        x = (screenSize.width - frameSize.width)/2;
        y = (screenSize.height - frameSize.height)/2;
        setLocation(x, y);
        setResizable(false);

        //Changing table headers to bold
        tblMasterPlanEditingView.getTableHeader().setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        supplierWiseOrderDetailsTbl.getTableHeader().setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));

        this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);

        createNewBlendOrder2 = cnb;

        setResizable(false);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        //set orderID
        this.orderID = orderID;
        //set supplier names in the table
        supplierList = getSupplierNames(cnb);
        DefaultTableModel model = (DefaultTableModel) tblMasterPlanEditingView.getModel();
        for (String supplierName : supplierList) {
            model.addRow(new Object[]{supplierName, 0.00, 0.00});
        }
        //group details supplierwise
        final List<List> mainList = groupDetailsSupplierwise(supplierList, cnb);

        //method for view supplierwise details when supplier name is seleced in the table
        final ListSelectionModel selectionalModForSupplierwiseTable = tblMasterPlanEditingView.getSelectionModel();
        selectionalModForSupplierwiseTable.addListSelectionListener(new ListSelectionListener() {

            DefaultTableModel tpmodel = (DefaultTableModel) supplierWiseOrderDetailsTbl.getModel();

            @Override
            public void valueChanged(ListSelectionEvent lsevt) {
                tpmodel.setRowCount(0);
                if (!selectionalModForSupplierwiseTable.isSelectionEmpty()) {
                    int row = selectionalModForSupplierwiseTable.getMinSelectionIndex();
                    int rowid = tblMasterPlanEditingView.getSelectedRow();
                    viewPOOrder(rowid, mainList, tpmodel);
                }
            }

        });

        //enabling sorting
        tblMasterPlanEditingView.setAutoCreateRowSorter(true);
        supplierWiseOrderDetailsTbl.setAutoCreateRowSorter(true);

    }

    private OrderConfirmation() {
        initComponents();
        this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        setResizable(false);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    //get distinct supplier list from master table
    public Set<String> getSupplierNames(CreateNewBlendOrder2 cnb) {
        String[] suppliers = null;

        DefaultTableModel model = (DefaultTableModel) cnb.masterPlanTbl.getModel();
        Set<String> set = new HashSet<String>();
        String supName;
        for (int i = 0; i < model.getRowCount(); i++) {
            supName = model.getValueAt(i, 7).toString();
            set.add(supName);
        }
        TreeSet sortedSet = new TreeSet<String>(set);
        return sortedSet;
    }

    public List<List> groupDetailsSupplierwise(Set<String> supplierList, CreateNewBlendOrder2 cnb) {
        //create arrays for the suppliers 
        mainList = new ArrayList<List>();
        DefaultTableModel masterTableModel = (DefaultTableModel) cnb.masterPlanTbl.getModel();
        for (String supName : supplierList) {
            List<String> supwiseDetails = new ArrayList<String>();
            for (int i = 0; i < masterTableModel.getRowCount(); i++) {
                String supNameFromMaster = masterTableModel.getValueAt(i, 7).toString();
                if (supName.equals(supNameFromMaster)) {
                    supwiseDetails.add(masterTableModel.getValueAt(i, 0).toString());
                    supwiseDetails.add(masterTableModel.getValueAt(i, 6).toString());

                }
            }
            mainList.add(supwiseDetails);
        }
        return mainList;
    }

    public void viewPOOrder(int rowid, List<List> mainList, DefaultTableModel tpmodel) {
        List lst = mainList.get(rowid);

        for (int i = 0; i < lst.size(); i++) {
            if (i % 2 == 0) {
                float unitPrice = parseFloat(ing.getUnitPriceByIngName(lst.get(i).toString()));
                float qty = parseFloat(lst.get(i + 1).toString());
                float total = (qty / 1000) * unitPrice;
                String unitPriceString = formatNum(Float.toString(unitPrice));
                String totalString = formatNum(Float.toString(total));
                tpmodel.addRow(new Object[]{lst.get(i), lst.get(i + 1), unitPriceString, totalString});

            }
        }

    }

    public List<List> setUnitPriceAndTotal() {
        mainList2 = new ArrayList<List>();
        totalList = new ArrayList();

        for (List lst : this.mainList) {
            float fullTotal = 0;
            List<String> unitPriceAndTotalList = new ArrayList<String>();
            for (int i = 0; i < lst.size(); i++) {
                if (i % 2 == 0) {
                    float unitPrice = parseFloat(ing.getUnitPriceByIngName(lst.get(i).toString()));
                    float qty = parseFloat(lst.get(i + 1).toString());
                    float total = (qty / 1000) * unitPrice;
                    unitPriceAndTotalList.add(formatNum(Float.toString(unitPrice)));
                    unitPriceAndTotalList.add(formatNum(Float.toString(total)));
                    fullTotal += total;
                }
            }
            totalList.add(fullTotal);
            mainList2.add(unitPriceAndTotalList);
        }

        return mainList2;
    }

    public List getDiscounts() {
        discountList = new ArrayList();
        String errorDiscount = "0";
        for (int i = 0; i < tblMasterPlanEditingView.getRowCount(); i++) {
            float discount = Float.parseFloat(tblMasterPlanEditingView.getValueAt(i, 1).toString());
            if (discount < 0 || discount >= 100) {
                errorDiscount = "1";
            }
            if (errorDiscount == "0") {
                discountList.add(tblMasterPlanEditingView.getValueAt(i, 1));
            }
        }
        discountList.add(errorDiscount);
        return discountList;
    }

    public List getTaxes() {
        taxList = new ArrayList();
        for (int i = 0; i < tblMasterPlanEditingView.getRowCount(); i++) {
            taxList.add(tblMasterPlanEditingView.getValueAt(i, 2));
        }
        return taxList;
    }

    //overiding Float.parseFloat() to accept nums with commas
    private float parseFloat(String num) {
        try {
            return Float.parseFloat(num);
        } catch (NumberFormatException e) {
            if (num.matches("[[0-9]{1,2}+,]*.[0-9]*")) {
                num = num.replace(",", "");
                return Float.parseFloat(num);
            }
        }
        return 0;
    }

    private String formatNum(String num) {
        String decimal = num, point = null;
        if (num.contains(".")) {
            String[] temp = num.split("\\.");
            decimal = temp[0];
            point = temp[1];
        }
        int i = decimal.length();
        while (i > 3) {
            String part1 = decimal.substring(0, i - 3);
            String part2 = decimal.substring(i - 3);
            decimal = part1 + "," + part2;
            i -= 3;
        }
        if (point != null) {
            decimal += "." + point;
        }
        return decimal;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        tblMasterPlanScrollPane = new javax.swing.JScrollPane();
        tblMasterPlanEditingView = new javax.swing.JTable();
        tblMasterPlanScrollPane1 = new javax.swing.JScrollPane();
        supplierWiseOrderDetailsTbl = new javax.swing.JTable();
        generatePdfBtn = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Order Confirmation");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, " Order Confirmation ", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI Semibold", 0, 16))); // NOI18N

        tblMasterPlanEditingView.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Supplier Name", "Discount", "Tax"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblMasterPlanEditingView.setRowHeight(24);
        tblMasterPlanEditingView.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tblMasterPlanEditingViewPropertyChange(evt);
            }
        });
        tblMasterPlanScrollPane.setViewportView(tblMasterPlanEditingView);
        if (tblMasterPlanEditingView.getColumnModel().getColumnCount() > 0) {
            tblMasterPlanEditingView.getColumnModel().getColumn(0).setMinWidth(25);
            tblMasterPlanEditingView.getColumnModel().getColumn(1).setMinWidth(70);
            tblMasterPlanEditingView.getColumnModel().getColumn(1).setPreferredWidth(10);
            tblMasterPlanEditingView.getColumnModel().getColumn(1).setMaxWidth(70);
            tblMasterPlanEditingView.getColumnModel().getColumn(2).setMinWidth(70);
            tblMasterPlanEditingView.getColumnModel().getColumn(2).setPreferredWidth(10);
            tblMasterPlanEditingView.getColumnModel().getColumn(2).setMaxWidth(70);
        }

        supplierWiseOrderDetailsTbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Ingridient", "Quantity", "Price", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        supplierWiseOrderDetailsTbl.setRowHeight(24);
        supplierWiseOrderDetailsTbl.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                supplierWiseOrderDetailsTblPropertyChange(evt);
            }
        });
        tblMasterPlanScrollPane1.setViewportView(supplierWiseOrderDetailsTbl);
        if (supplierWiseOrderDetailsTbl.getColumnModel().getColumnCount() > 0) {
            supplierWiseOrderDetailsTbl.getColumnModel().getColumn(1).setMinWidth(80);
            supplierWiseOrderDetailsTbl.getColumnModel().getColumn(1).setPreferredWidth(2);
            supplierWiseOrderDetailsTbl.getColumnModel().getColumn(1).setMaxWidth(80);
            supplierWiseOrderDetailsTbl.getColumnModel().getColumn(2).setMinWidth(80);
            supplierWiseOrderDetailsTbl.getColumnModel().getColumn(2).setPreferredWidth(20);
            supplierWiseOrderDetailsTbl.getColumnModel().getColumn(2).setMaxWidth(80);
            supplierWiseOrderDetailsTbl.getColumnModel().getColumn(3).setMinWidth(80);
            supplierWiseOrderDetailsTbl.getColumnModel().getColumn(3).setPreferredWidth(20);
            supplierWiseOrderDetailsTbl.getColumnModel().getColumn(3).setMaxWidth(80);
        }

        generatePdfBtn.setText("Generate PDF");
        generatePdfBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generatePdfBtnActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI Semibold", 0, 16)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setText("Supplier-wise order details");

        jLabel3.setFont(new java.awt.Font("Segoe UI Semibold", 0, 16)); // NOI18N
        jLabel3.setText("Suppliers");
        jLabel3.setMaximumSize(new java.awt.Dimension(31, 14));
        jLabel3.setMinimumSize(new java.awt.Dimension(31, 14));
        jLabel3.setPreferredSize(new java.awt.Dimension(31, 14));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jLabel1.setText("Click on each supplier to view order details");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(generatePdfBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tblMasterPlanScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 377, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel1)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tblMasterPlanScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 593, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tblMasterPlanScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 379, Short.MAX_VALUE)
                    .addComponent(tblMasterPlanScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(generatePdfBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
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

    private void tblMasterPlanEditingViewPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tblMasterPlanEditingViewPropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_tblMasterPlanEditingViewPropertyChange

    private void supplierWiseOrderDetailsTblPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_supplierWiseOrderDetailsTblPropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_supplierWiseOrderDetailsTblPropertyChange

    private void generatePdfBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generatePdfBtnActionPerformed
        //generating pdfs
        setUnitPriceAndTotal();
        if (tblMasterPlanEditingView.getCellEditor() != null) {
            tblMasterPlanEditingView.getCellEditor().stopCellEditing();
        }
        getDiscounts();
        int lastIndex = discountList.size() - 1;
        String errorDiscount = discountList.get(lastIndex).toString();
        if (errorDiscount == "1") {

            JOptionPane.showMessageDialog(this, "Discount cannot be greater than 100!", "Invalid Discount", 2);
        } else {
            getTaxes();
            pdf = new PDF();
            String path = pdf.generateSupplierwisePO(supplierList, mainList, mainList2, orderID, discountList, taxList, totalList);

            if (path != null) {
                int response = JOptionPane.showConfirmDialog(
                        null,
                        "Purchase orders saved successfully. Do you want to open the containing folder?",
                        "Purchase orders",
                        JOptionPane.YES_NO_OPTION
                );
                if (response == JOptionPane.YES_OPTION) {
                    try {
                        //Opening the new directory
                        Desktop.getDesktop().open(new File(path));
                    } catch (IOException ex) {
                        System.out.println("IOException : " + ex.getMessage());
                    }
                }

                //finishing order
                this.createNewBlendOrder2.dispose();
                
                //Re-generating the admin panel since the data is changed
                if ("teaeli.AdminPannel".equals(pannel.getClass().getName())) {
                    AdminPannel adminPannel = new AdminPannel();
                    adminPannel.setVisible(true);
                    AdminPannel old = (AdminPannel) pannel;
                    old.dispose();
                } else if ("teaeli.ManagerPannel".equals(pannel.getClass().getName())) {
                    ManagerPannel managerPannel = new ManagerPannel();
                    managerPannel.setVisible(true);
                    ManagerPannel old = (ManagerPannel) pannel;
                    old.dispose();
                }
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "There were some issues with the database. Please contact developers.\n\nError code : OrderConfirmation 460", "Error", 0);
                System.exit(0);
            }
        }

    }//GEN-LAST:event_generatePdfBtnActionPerformed

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
            java.util.logging.Logger.getLogger(OrderConfirmation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(OrderConfirmation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(OrderConfirmation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(OrderConfirmation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new OrderConfirmation().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton generatePdfBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    public javax.swing.JTable supplierWiseOrderDetailsTbl;
    public javax.swing.JTable tblMasterPlanEditingView;
    public javax.swing.JScrollPane tblMasterPlanScrollPane;
    public javax.swing.JScrollPane tblMasterPlanScrollPane1;
    // End of variables declaration//GEN-END:variables
}
