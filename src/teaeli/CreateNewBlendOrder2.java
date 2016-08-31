
package teaeli;

import classes.Blend;
import classes.Ingredient;
import classes.Order;
import classes.PDF;
import classes.ResultArray;
import classes.Validation;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;


public class CreateNewBlendOrder2 extends javax.swing.JFrame implements PropertyChangeListener{

    private Blend blend;
    private Ingredient ingredient;
    private PDF pdf;
    private Order order;
    public Object pannel;
    public CreateNewBlendOrder1 createNewBlendOrder1;
    public List<List<String>> blendList;
    private Task task;

    /**
     * Creates new form AddNewOrder
     *
     * @param cNBO1
     */
    public CreateNewBlendOrder2(CreateNewBlendOrder1 cNBO1) {
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

        //Making table headers semibold
        blendListTbl.getTableHeader().setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        masterPlanTbl.getTableHeader().setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));

        //setting phase 1 object
        createNewBlendOrder1 = cNBO1;

        //Loading required class objects
        blend = new Blend();
        ingredient = new Ingredient();
        order = new Order();

        pdf = new PDF();
        

        //Setting date
        DateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy");
        Date today = new Date();
        dateLabel.setText(formatter.format(today));

        //Setting order id
        orderIDLabel.setText(createNewBlendOrder1.getOrderID());

        //Init blendListTbl
        DefaultTableModel model = createNewBlendOrder1.getBlendListTbl();
        DefaultTableModel blendTBModel = (DefaultTableModel) blendListTbl.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            Vector row = new Vector();
            row.add(model.getValueAt(i, 0));
            row.add(model.getValueAt(i, 6));
            blendTBModel.addRow(row);
        }

        //Adding listner to prompt confirmation on window close
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmClose();
            }
        });

        //Populating masterPlanTbl
        populateMasterPlanTbl();

        //update excess qty on row select
        final ListSelectionModel mod = masterPlanTbl.getSelectionModel();
        mod.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent lse) {
                if (!mod.isSelectionEmpty()) {
                    int count = masterPlanTbl.getRowCount();
                    for (int i = 0; i < count; i++) {
                        setExcessQty(i);
                    }
                }
            }
        });
        
        //Removing the category colum from master table
        masterPlanTbl.removeColumn(masterPlanTbl.getColumn(masterPlanTbl.getColumnName(8)));
        
        //enabling sorting
        blendListTbl.setAutoCreateRowSorter(true);
        masterPlanTbl.setAutoCreateRowSorter(true);
        
        progressBar.setVisible(false);
    }

    private void confirmClose(){
        cancelBtn.doClick();
    }
    
    private void close(){
        this.dispose();
    }
    
    private void populateMasterPlanTbl() {
        for (int i = 0; i < blendListTbl.getRowCount(); i++) {
            String blendName = blendListTbl.getValueAt(i, 0).toString();
            blendName = blendName.replace("'", "\\'");
            int blendQty = parseInt(blendListTbl.getValueAt(i, 1).toString());
            if (blendQty > 0) {
                ResultArray res = blend.getRecipie(blendName);
                String baseID = "";
                float totalIngPercentage = 0;
                while (res.next()) {
                    baseID = res.getString(0);
                    String ingID = res.getString(1);
                    float ingPercentage = Float.parseFloat(res.getString(2));
                    if (res.getString(3).equals("0")) {
                        totalIngPercentage += ingPercentage;
                    }
                    ResultArray ingData = ingredient.getIngDataByID(ingID);
                    ingData.next();
                    addIngToMasterTbl(blendQty, ingPercentage, (List<String>) ingData.getRow());
                }
                //Adding base composition with calculated percentage
                ResultArray baseData = ingredient.getIngDataByID(baseID);
                baseData.next();
                addIngToMasterTbl(blendQty, 100 - totalIngPercentage, (List<String>) baseData.getRow());
            }
        }
    }

    //method to reset excess qty
    private void setExcessQty(int row) {
        String ingName = masterPlanTbl.getValueAt(row, 0).toString();
        ingName = ingName.replace("'", "\\'");
        float requiredQty = parseFloat(masterPlanTbl.getValueAt(row, 4).toString());
        if (new Validation().isFloat(masterPlanTbl.getValueAt(row, 6).toString())) {
            float finalQty = parseFloat(masterPlanTbl.getValueAt(row, 6).toString());
            if (finalQty < requiredQty) {
                JOptionPane.showMessageDialog(masterPlanTbl, "<html>You cannot decrease the <b>" + ingName + "</b> final quantity less than required quantity!</html>", "Invalid Final Quantity", 2);
                masterPlanTbl.setValueAt(formatNum(requiredQty), row, 6);
            } else {
                masterPlanTbl.setValueAt(formatNum(finalQty - requiredQty), row, 5);
            }
        } else {
            JOptionPane.showMessageDialog(masterPlanTbl, "<html>Please enter a valid final quantity for <b>" + ingName + "</b>.</html>", "Invalid Final Quantity", 2);
            masterPlanTbl.setValueAt(formatNum(requiredQty), row, 6);
        }
    }

    //Adding an ingredient into master plan
    private void addIngToMasterTbl(int blendQty, float percentage, List<String> row) {

        boolean isNew = true;
        float ingQty = (float) blendQty * percentage / 100.0f;
        for (int i = 0; i < masterPlanTbl.getRowCount(); i++) {
            if (masterPlanTbl.getValueAt(i, 0).equals(row.get(1))) {
                ingQty += parseFloat(masterPlanTbl.getValueAt(i, 1).toString());

                masterPlanTbl.setValueAt(formatNum(ingQty), i, 1);
                float visible = parseFloat(masterPlanTbl.getValueAt(i, 2).toString());
                float invisible = parseFloat(masterPlanTbl.getValueAt(i, 3).toString());
                float balance = 0;

                balance = ingQty - visible;
                if (balance > 0) {
                    balance = balance - invisible;
                }
                if (balance < 0) {
                    balance = 0;
                }
                masterPlanTbl.setValueAt(formatNum(balance), i, 4);
                float excess = parseFloat(masterPlanTbl.getValueAt(i, 5).toString());
                masterPlanTbl.setValueAt(formatNum(excess + balance), i, 6);
                isNew = false;
                break;
            }
        }
        if (isNew) {
            Vector newRow = new Vector();
            newRow.addElement(row.get(1));
            newRow.addElement(formatNum(ingQty));
            float visible = parseFloat(row.get(3));
            float invisible = parseFloat(row.get(5));
            newRow.addElement(formatNum(visible));
            newRow.addElement(formatNum(invisible));

            //calculating qty required
            float balance = 0;
            balance = ingQty - visible;
            if (balance > 0) {
                balance = balance - invisible;
            }
            if (balance < 0) {
                balance = 0;
            }

            newRow.addElement(formatNum(balance));
            newRow.addElement("0.0");
            newRow.addElement(formatNum(balance));
            newRow.addElement(row.get(6));

            //setting category into the hidden field
            newRow.addElement(row.get(2));
            

            DefaultTableModel model = (DefaultTableModel) masterPlanTbl.getModel();
            model.addRow(newRow);
        }
    }

    //Rounding method for doubles
    public static float round(float num, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(num);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.floatValue();
    }

    //formatting numbers to add commas
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

    private String formatNum(int num) {
        return formatNum(String.valueOf(num));
    }

    private String formatNum(float num) {
        num = round(num, 3);
        return formatNum(Float.toString(num));
    }

    //overiding Integer.parseInt() to accept nums with commas
    private int parseInt(String num) {
        try {
            return Integer.parseInt(num);
        } catch (NumberFormatException e) {
            if (num.matches("[[0-9]{1,2}+,]*")) {
                num = num.replace(",", "");
                return Integer.parseInt(num);
            }
        }
        return 0;
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

    private CreateNewBlendOrder2() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void readBlendListTbl() {
        DefaultTableModel model = createNewBlendOrder1.getBlendListTbl();
        for (int i = 0; i < model.getRowCount(); i++) {
            String blendName = model.getValueAt(i, 0).toString();
            blendName = blendName.replace("'", "\\'");
            int reqQty = (parseInt(model.getValueAt(i, 1).toString()));
            int visibleStock = parseInt(model.getValueAt(i, 2).toString());
            int invisibleStock = parseInt(model.getValueAt(i, 3).toString());
            String balanceQty = String.valueOf(parseInt(model.getValueAt(i, 4).toString()));
            String excessQty = String.valueOf(parseInt(model.getValueAt(i, 5).toString()));

            String blendID = blend.getBlendIDByBlendName(blendName);
            
            //placing order blend
            String[] data = {orderIDLabel.getText(), blendID, String.valueOf(reqQty), String.valueOf(visibleStock), String.valueOf(invisibleStock), balanceQty, excessQty};
            if (!order.placeOrderBlends(data)) {
                JOptionPane.showMessageDialog(rootPane, "There were some issues with the database. Please contact developers.\n\nError code : CreatNewBlendOrder2 348", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }

            //calculating stocks
            if (reqQty > visibleStock) {
                reqQty -= visibleStock;
                visibleStock = 0;
                if (reqQty > invisibleStock) {
                    invisibleStock = 0;
                } else {
                    invisibleStock -= reqQty;
                }
            } else {
                visibleStock -= reqQty;
            }
            invisibleStock += parseInt(excessQty);
            
            //updating blend stock
            data = new String[]{String.valueOf(visibleStock), String.valueOf(invisibleStock), blendID};
            if (!blend.updateBlendStock(data)) {
                JOptionPane.showMessageDialog(rootPane, "There were some issues with the database. Please contact developers.\n\nError code : CreatNewBlendOrder2 369", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        }
    }

    private void readMasterPlanTbl() {
        DefaultTableModel model = (DefaultTableModel) masterPlanTbl.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            String ingName = model.getValueAt(i, 0).toString();
            ingName = ingName.replace("'", "\\'");
            float reqQty = (parseFloat(model.getValueAt(i, 1).toString()));
            float visibleStock = parseFloat(model.getValueAt(i, 2).toString());
            float invisibleStock = parseFloat(model.getValueAt(i, 3).toString());
            String balanceQty = String.valueOf(parseFloat(model.getValueAt(i, 4).toString()));
            String excessQty = String.valueOf(parseFloat(model.getValueAt(i, 5).toString()));

            String ingID = ingredient.getIngIDByIngName(ingName);

            //placing order ingredients
            String[] data = {orderIDLabel.getText(), ingID, String.valueOf(reqQty), String.valueOf(visibleStock), String.valueOf(invisibleStock), balanceQty, excessQty};
            if (!order.placeOrderIngredients(data)) {
                JOptionPane.showMessageDialog(rootPane, "There were some issues with the database. Please contact developers.\n\nError code : CreatNewBlendOrder2 391", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }

            //calculating stocks
            if (reqQty > visibleStock) {
                reqQty -= visibleStock;
                visibleStock = 0;
                if (reqQty > invisibleStock) {
                    invisibleStock = 0;
                } else {
                    invisibleStock -= reqQty;
                }
            } else {
                visibleStock -= reqQty;
            }
            invisibleStock += parseFloat(excessQty);
            
            //updating ingredient stock
            data = new String[]{String.valueOf(visibleStock), String.valueOf(invisibleStock), ingID};
            if (!ingredient.updateIngredientStock(data)) {
                JOptionPane.showMessageDialog(rootPane, "There were some issues with the database. Please contact developers.\n\nError code : CreatNewBlendOrder2 412", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
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

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        blendListTbl = new javax.swing.JTable();
        tblMasterPlanScrollPane = new javax.swing.JScrollPane();
        masterPlanTbl = new javax.swing.JTable();
        confirmBtn = new javax.swing.JButton();
        cancelBtn = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        dateLabel = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        orderIDLabel = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();

        jLabel1.setText("jLabel1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Create New Blend Order");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, " Create New Blend Order - Phase 2 ", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI Semibold", 0, 16))); // NOI18N

        blendListTbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Blend Name", "Final Qty (g)"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
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
        }

        masterPlanTbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Ingredient", "Qty Required (g)", "Visible Stock (g)", "Invisible Stock (g)", "Balance Qty Required (g)", "Excess Qty (g)", "Final Qty (g)", "Supplier Name", "category"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, true, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        masterPlanTbl.setRowHeight(24);
        masterPlanTbl.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                masterPlanTblPropertyChange(evt);
            }
        });
        tblMasterPlanScrollPane.setViewportView(masterPlanTbl);
        if (masterPlanTbl.getColumnModel().getColumnCount() > 0) {
            masterPlanTbl.getColumnModel().getColumn(0).setResizable(false);
            masterPlanTbl.getColumnModel().getColumn(0).setPreferredWidth(200);
            masterPlanTbl.getColumnModel().getColumn(1).setResizable(false);
            masterPlanTbl.getColumnModel().getColumn(2).setResizable(false);
            masterPlanTbl.getColumnModel().getColumn(3).setResizable(false);
            masterPlanTbl.getColumnModel().getColumn(3).setPreferredWidth(80);
            masterPlanTbl.getColumnModel().getColumn(4).setResizable(false);
            masterPlanTbl.getColumnModel().getColumn(4).setPreferredWidth(120);
            masterPlanTbl.getColumnModel().getColumn(5).setResizable(false);
            masterPlanTbl.getColumnModel().getColumn(5).setPreferredWidth(55);
            masterPlanTbl.getColumnModel().getColumn(6).setResizable(false);
            masterPlanTbl.getColumnModel().getColumn(7).setResizable(false);
            masterPlanTbl.getColumnModel().getColumn(7).setPreferredWidth(230);
            masterPlanTbl.getColumnModel().getColumn(8).setResizable(false);
        }

        confirmBtn.setText("Confirm");
        confirmBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmBtnActionPerformed(evt);
            }
        });

        cancelBtn.setText("< Back");
        cancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtnActionPerformed(evt);
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

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setText("Please edit final qty column to add excess amounts.");

        progressBar.setEnabled(false);
        progressBar.setPreferredSize(new java.awt.Dimension(146, 10));
        progressBar.setStringPainted(true);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tblMasterPlanScrollPane)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jScrollPane1)
                                .addGap(30, 30, 30))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(confirmBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tblMasterPlanScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 389, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(confirmBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel6))
                    .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

    private void blendListTblPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_blendListTblPropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_blendListTblPropertyChange

    private void masterPlanTblPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_masterPlanTblPropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_masterPlanTblPropertyChange
    
    private void confirmBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmBtnActionPerformed
        
        int count = blendListTbl.getRowCount();
        for (int i = 0; i < count; i++) {
            setExcessQty(i);
        }
        int dialogResult = JOptionPane.showConfirmDialog(this, "Are you sure you want to place this order?\nYou cannot undo after the confirmation.", "Confirm order placing", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (dialogResult == JOptionPane.YES_OPTION) {
            
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            progressBar.setVisible(true);
            task = new Task();
            task.addPropertyChangeListener(this);
            task.execute();
        }
    }//GEN-LAST:event_confirmBtnActionPerformed

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
        int dialogResult = JOptionPane.showConfirmDialog(this, "Are you sure you want to go back?", "Confirm window close", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (dialogResult == JOptionPane.YES_OPTION) {
            createNewBlendOrder1.setVisible(true);
            this.dispose();
        }
    }//GEN-LAST:event_cancelBtnActionPerformed

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
            java.util.logging.Logger.getLogger(CreateNewBlendOrder2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CreateNewBlendOrder2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CreateNewBlendOrder2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CreateNewBlendOrder2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CreateNewBlendOrder2().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable blendListTbl;
    public javax.swing.JButton cancelBtn;
    public javax.swing.JButton confirmBtn;
    private javax.swing.JLabel dateLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JTable masterPlanTbl;
    private javax.swing.JLabel orderIDLabel;
    private javax.swing.JProgressBar progressBar;
    public javax.swing.JScrollPane tblMasterPlanScrollPane;
    // End of variables declaration//GEN-END:variables

    private void openOrderConfirmation(){
        String orderId = orderIDLabel.getText();
            OrderConfirmation oc = new OrderConfirmation(this, orderId);
            oc.setVisible(true);
            oc.pannel = this.pannel;
            createNewBlendOrder1.dispose();
            this.setVisible(false);
    }
    //inner class to carry out order placement process
    class Task extends SwingWorker<Void, Void> {

        @Override
        public Void doInBackground() {
            
            //placing the order in order table
            if (!order.placeOrder(orderIDLabel.getText())) {
                JOptionPane.showMessageDialog(rootPane, "There were some issues with the database. Please contact developers.\n\nError code : CreatNewBlendOrder2 684", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
            setProgress(10);
            
            //placing orderBlends and updating blend table
            readBlendListTbl();
            setProgress(45);

            //placing orderIngredients and updating ingredient table
            readMasterPlanTbl();
            setProgress(80);

            DefaultTableModel model = (DefaultTableModel) masterPlanTbl.getModel();

            //Generating master plan PDF
            JTable temp = new JTable(model);
            temp.setAutoCreateRowSorter(true);
            temp.getRowSorter().toggleSortOrder(8);
            DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
            Date today = new Date();
            String[] data = {orderIDLabel.getText(), formatter.format(today)};
            pdf.generateMasterPlanPDF(temp, data);
            setProgress(90);
            
            //Removing entries of 0 balance
            for (int i=0; i<model.getRowCount(); i++) {
                if (parseFloat(model.getValueAt(i, 6).toString()) <= 0) {
                    model.removeRow(i);
                    i -= 1;
                }
            }
            setProgress(100);
            
            //Move into order confirmation
            openOrderConfirmation();
            
            return null;
        }

        @Override
        public void done() {
            Toolkit.getDefaultToolkit().beep();
            setCursor(null); //turn off the wait cursor
        }
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress" == evt.getPropertyName()) {
            int progress = (Integer) evt.getNewValue();
            progressBar.setValue(progress);
        } 
    }
}
