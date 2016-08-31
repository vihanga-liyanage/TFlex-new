package teaeli;

import classes.Blend;
import classes.Ingredient;
import classes.StockHistory;
import classes.AutoSuggest;
import classes.Order;
import classes.User;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import classes.PDF;
import javax.swing.ImageIcon;

public class ManagerPannel extends javax.swing.JFrame {

    PDF pdf = new PDF();
    User user = new User();
    Ingredient ingredient = new Ingredient();
    Blend blend = new Blend();
    StockHistory blendHistoryStock = new StockHistory();
    StockHistory ingredientHistoryStock = new StockHistory();
    public static IngredientDetails ingredientDetails = new IngredientDetails();
    Order order = new Order();
    int blendGo = 0, ingredientGo = 0;
    private Object ingTable;
    java.util.Date date = new java.util.Date();
    SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy.MM.dd");
    String today = sdf3.format(date);

    /**
     * Creates new form AdminPannel
     */
    public ManagerPannel() {
        //Setting icon
        ImageIcon img = new ImageIcon("src\\img\\icon-1.png");
        this.setIconImage(img.getImage());
        
        try {
            setUIFont(new javax.swing.plaf.FontUIResource("Segoe UI", Font.PLAIN, 14));
        } catch (Exception e) {
        }

        //Changing look and feel
        //for metal - javax.swing.plaf.metal.MetalLookAndFeel
        //for windows - com.sun.java.swing.plaf.windows.WindowsLookAndFeel
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(ManagerPannel.class.getName()).log(Level.SEVERE, null, ex);
        }

        initComponents();

        //Changing table headers to bold
        orderListTable.getTableHeader().setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        inventryIngredientTable.getTableHeader().setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        inventryBlendTable.getTableHeader().setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));

        startClock();

        //Keep the window fullscreen
        this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);

        /* populate inventryIngredientTable in inventory management*/
        populateIngStockTable();

        /*populate inventryBlendTable in the inventory management*/
        populateBlendStockTable();

        /*populate main order table in the order details tab*/
        populateOrderListTable();

        /*load the orderComboBox in the order details tab*/
        AutoSuggest searchOrderComboBoxAutoSuggest = new AutoSuggest();
        searchOrderComboBoxAutoSuggest.setAutoSuggest(orderSearchCombo, order.loadOrderComboBox());

        orderSearchCombo.setSelectedIndex(-1);

        /* combox auto suggests in inventory management */
        AutoSuggest searchStockIngComboBoxAutoSuggest = new AutoSuggest();
        searchStockIngComboBoxAutoSuggest.setAutoSuggest(searchStockIngComboBox, ingredient.loadNameForSearchStockIngComboBox());

        searchStockIngComboBox.setSelectedIndex(-1);

        initStockBlendCombo();

        //method for combox value setting when table row select in inventryIngredient
        final ListSelectionModel selectionalModForStockIngTable = inventryIngredientTable.getSelectionModel();
        selectionalModForStockIngTable.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent lsevt) {
                if (!selectionalModForStockIngTable.isSelectionEmpty()) {
                    int row = selectionalModForStockIngTable.getMinSelectionIndex();
                    searchStockIngComboBox.setSelectedItem(inventryIngredientTable.getValueAt(row, 1));
                }
            }

        });

        //methods for combox value settings when table row select in inventryBlend
        final ListSelectionModel selectionalModForStockBlendTable = inventryBlendTable.getSelectionModel();
        selectionalModForStockBlendTable.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent lsevt) {
                if (!selectionalModForStockBlendTable.isSelectionEmpty()) {
                    int row = selectionalModForStockBlendTable.getMinSelectionIndex();
                    searchStockBlendComboBox.setSelectedItem(inventryBlendTable.getValueAt(row, 1));
                }
            }

        });

        //method for combox value setting when table row select in order main table
        final ListSelectionModel selectionalModForOrderMainTable = orderListTable.getSelectionModel();
        selectionalModForOrderMainTable.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent lsevt) {
                if (!selectionalModForOrderMainTable.isSelectionEmpty()) {
                    int row = selectionalModForOrderMainTable.getMinSelectionIndex();
                    orderSearchCombo.setSelectedItem(orderListTable.getValueAt(row, 0));
                }
            }

        });

        //method for enter key pressed in ingredient
        searchStockIngComboBox.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchStockIngredientCombo();
                }
            }
        });

        //method for enter key pressed in blend
        searchStockBlendComboBox.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchStockBlendCombo();
                }
            }
        });

        //enabling sorting for tables
        orderListTable.setAutoCreateRowSorter(true);
        inventryIngredientTable.setAutoCreateRowSorter(true);
        inventryBlendTable.setAutoCreateRowSorter(true);

    }

    public void setGreetings(String greeting){
        greetingsLbl.setText(greeting);
    }
    
    public void populateIngStockTable() {
        ingredient.populateIngredientTable((DefaultTableModel) inventryIngredientTable.getModel());
    }

    public void populateBlendStockTable() {
        blend.populateBlendTable((DefaultTableModel) inventryBlendTable.getModel());
    }

    public void populateOrderListTable() {
        order.populateOrderListTable((DefaultTableModel) orderListTable.getModel());
    }
    
    public void initStockBlendCombo() {
        AutoSuggest searchStockBlendComboBoxAutoSuggest = new AutoSuggest();
        searchStockBlendComboBoxAutoSuggest.setAutoSuggest(searchStockBlendComboBox, blend.loadNameForSearchStockBlendsComboBox());
        searchStockBlendComboBox.setSelectedIndex(-1);
    }
    
    //Setting default font
    public static void setUIFont(javax.swing.plaf.FontUIResource f) {
        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, f);
            }
        }
    }

    private void startClock() {
        Timer timer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tickTock();
            }
        });
        timer.setRepeats(true);
        timer.setCoalesce(true);
        timer.setInitialDelay(0);
        timer.start();
    }

    public void tickTock() {
        timeLabel.setText(DateFormat.getDateTimeInstance().format(new Date()));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainTabbedPane = new javax.swing.JTabbedPane();
        orderHandlingPanel = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        orderListTable = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        addNewBlendsBtn = new javax.swing.JButton();
        searchOrderBtn = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        orderSearchCombo = new javax.swing.JComboBox();
        inventoryPanel = new javax.swing.JPanel();
        inventoryManagementSplitPane = new javax.swing.JSplitPane();
        inventoryManagementIngredientPanel = new javax.swing.JPanel();
        inventoryIngredientsLbl = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        searchStockIngBtn = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        inventryIngredientTable = new javax.swing.JTable();
        searchStockIngComboBox = new javax.swing.JComboBox();
        inventoryManagementBlendPanel = new javax.swing.JPanel();
        inventoryBlendLbl = new javax.swing.JLabel();
        searchStockBlendsBtn = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        inventryBlendTable = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        searchStockBlendComboBox = new javax.swing.JComboBox();
        blendDeliveryBtn = new javax.swing.JButton();
        logoLabel = new javax.swing.JLabel();
        timeLabel = new javax.swing.JLabel();
        greetingsLbl = new javax.swing.JLabel();
        logoutBtn = new javax.swing.JButton();
        profileBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("TFlex By Reid Solutions");
        setBackground(new java.awt.Color(51, 51, 255));
        setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N

        mainTabbedPane.setBackground(new java.awt.Color(153, 153, 153));
        mainTabbedPane.setFont(new java.awt.Font("Segoe UI Symbol", 0, 15)); // NOI18N

        orderHandlingPanel.setBackground(new java.awt.Color(255, 255, 255));

        orderListTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Order ID", "Status", "Placed Date", "Placed By"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        orderListTable.setRowHeight(24);
        jScrollPane7.setViewportView(orderListTable);
        if (orderListTable.getColumnModel().getColumnCount() > 0) {
            orderListTable.getColumnModel().getColumn(0).setResizable(false);
            orderListTable.getColumnModel().getColumn(0).setPreferredWidth(200);
            orderListTable.getColumnModel().getColumn(1).setResizable(false);
            orderListTable.getColumnModel().getColumn(1).setPreferredWidth(120);
            orderListTable.getColumnModel().getColumn(2).setResizable(false);
            orderListTable.getColumnModel().getColumn(2).setPreferredWidth(120);
            orderListTable.getColumnModel().getColumn(3).setResizable(false);
            orderListTable.getColumnModel().getColumn(3).setPreferredWidth(250);
        }

        jLabel1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 16)); // NOI18N
        jLabel1.setText("Raw Material Orders");
        jLabel1.setMaximumSize(new java.awt.Dimension(31, 14));
        jLabel1.setMinimumSize(new java.awt.Dimension(31, 14));
        jLabel1.setPreferredSize(new java.awt.Dimension(31, 14));

        addNewBlendsBtn.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        addNewBlendsBtn.setText("Create New Blend Order");
        addNewBlendsBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addNewBlendsBtnActionPerformed(evt);
            }
        });

        searchOrderBtn.setText("Go");
        searchOrderBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchOrderBtnActionPerformed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel15.setText("Select order ID to view details");

        orderSearchCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                orderSearchComboActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout orderHandlingPanelLayout = new javax.swing.GroupLayout(orderHandlingPanel);
        orderHandlingPanel.setLayout(orderHandlingPanelLayout);
        orderHandlingPanelLayout.setHorizontalGroup(
            orderHandlingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(orderHandlingPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(orderHandlingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(orderHandlingPanelLayout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(orderHandlingPanelLayout.createSequentialGroup()
                        .addGroup(orderHandlingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 1255, Short.MAX_VALUE)
                            .addGroup(orderHandlingPanelLayout.createSequentialGroup()
                                .addGroup(orderHandlingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(orderHandlingPanelLayout.createSequentialGroup()
                                        .addComponent(orderSearchCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(searchOrderBtn))
                                    .addComponent(jLabel15))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 823, Short.MAX_VALUE)
                                .addComponent(addNewBlendsBtn)))
                        .addContainerGap())))
        );
        orderHandlingPanelLayout.setVerticalGroup(
            orderHandlingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(orderHandlingPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(orderHandlingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addNewBlendsBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(orderHandlingPanelLayout.createSequentialGroup()
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(orderHandlingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(searchOrderBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(orderSearchCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 406, Short.MAX_VALUE)
                .addContainerGap())
        );

        mainTabbedPane.addTab("    Order Handling    ", orderHandlingPanel);

        inventoryPanel.setBackground(new java.awt.Color(255, 255, 255));

        inventoryManagementSplitPane.setBorder(null);
        inventoryManagementSplitPane.setDividerLocation(650);
        inventoryManagementSplitPane.setDividerSize(0);
        inventoryManagementSplitPane.setResizeWeight(0.5);

        inventoryManagementIngredientPanel.setBackground(new java.awt.Color(255, 255, 255));
        inventoryManagementIngredientPanel.setPreferredSize(new java.awt.Dimension(715, 498));

        inventoryIngredientsLbl.setFont(new java.awt.Font("Segoe UI Semibold", 0, 16)); // NOI18N
        inventoryIngredientsLbl.setText("Ingredients");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setText("Start typing ingredient name to update stock");

        searchStockIngBtn.setText("Go");
        searchStockIngBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchStockIngBtnActionPerformed(evt);
            }
        });

        inventryIngredientTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cateogry", "Ingredient Name", "Visible Stock (g)", "Invisible Stock (g)"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        inventryIngredientTable.setRowHeight(24);
        jScrollPane6.setViewportView(inventryIngredientTable);
        if (inventryIngredientTable.getColumnModel().getColumnCount() > 0) {
            inventryIngredientTable.getColumnModel().getColumn(1).setResizable(false);
            inventryIngredientTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        }

        searchStockIngComboBox.setEditable(true);
        searchStockIngComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchStockIngComboBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout inventoryManagementIngredientPanelLayout = new javax.swing.GroupLayout(inventoryManagementIngredientPanel);
        inventoryManagementIngredientPanel.setLayout(inventoryManagementIngredientPanelLayout);
        inventoryManagementIngredientPanelLayout.setHorizontalGroup(
            inventoryManagementIngredientPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inventoryManagementIngredientPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(inventoryManagementIngredientPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 630, Short.MAX_VALUE)
                    .addGroup(inventoryManagementIngredientPanelLayout.createSequentialGroup()
                        .addGroup(inventoryManagementIngredientPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(inventoryIngredientsLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)
                            .addGroup(inventoryManagementIngredientPanelLayout.createSequentialGroup()
                                .addComponent(searchStockIngComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(searchStockIngBtn)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        inventoryManagementIngredientPanelLayout.setVerticalGroup(
            inventoryManagementIngredientPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inventoryManagementIngredientPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inventoryIngredientsLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addGroup(inventoryManagementIngredientPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchStockIngBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchStockIngComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 413, Short.MAX_VALUE))
        );

        inventoryManagementSplitPane.setLeftComponent(inventoryManagementIngredientPanel);

        inventoryManagementBlendPanel.setBackground(new java.awt.Color(255, 255, 255));
        inventoryManagementBlendPanel.setPreferredSize(new java.awt.Dimension(615, 498));

        inventoryBlendLbl.setFont(new java.awt.Font("Segoe UI Semibold", 0, 16)); // NOI18N
        inventoryBlendLbl.setText("Blends");

        searchStockBlendsBtn.setText("Go");
        searchStockBlendsBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchStockBlendsBtnActionPerformed(evt);
            }
        });

        inventryBlendTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Blend Category", "Blend Name", "Visible Stock (g)", "Invisible Stock (g)"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        inventryBlendTable.setRowHeight(24);
        jScrollPane2.setViewportView(inventryBlendTable);
        if (inventryBlendTable.getColumnModel().getColumnCount() > 0) {
            inventryBlendTable.getColumnModel().getColumn(1).setResizable(false);
            inventryBlendTable.getColumnModel().getColumn(1).setPreferredWidth(200);
            inventryBlendTable.getColumnModel().getColumn(2).setResizable(false);
        }

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("Start typing blend name to update stock");

        searchStockBlendComboBox.setEditable(true);
        searchStockBlendComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchStockBlendComboBoxActionPerformed(evt);
            }
        });

        blendDeliveryBtn.setText("Deliver Blend");
        blendDeliveryBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                blendDeliveryBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout inventoryManagementBlendPanelLayout = new javax.swing.GroupLayout(inventoryManagementBlendPanel);
        inventoryManagementBlendPanel.setLayout(inventoryManagementBlendPanelLayout);
        inventoryManagementBlendPanelLayout.setHorizontalGroup(
            inventoryManagementBlendPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inventoryManagementBlendPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(inventoryManagementBlendPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 605, Short.MAX_VALUE)
                    .addGroup(inventoryManagementBlendPanelLayout.createSequentialGroup()
                        .addGroup(inventoryManagementBlendPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(inventoryManagementBlendPanelLayout.createSequentialGroup()
                                .addComponent(searchStockBlendComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(searchStockBlendsBtn)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(blendDeliveryBtn))
                            .addComponent(inventoryBlendLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        inventoryManagementBlendPanelLayout.setVerticalGroup(
            inventoryManagementBlendPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inventoryManagementBlendPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inventoryBlendLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addGroup(inventoryManagementBlendPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchStockBlendsBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchStockBlendComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(blendDeliveryBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 413, Short.MAX_VALUE))
        );

        inventoryManagementSplitPane.setRightComponent(inventoryManagementBlendPanel);

        javax.swing.GroupLayout inventoryPanelLayout = new javax.swing.GroupLayout(inventoryPanel);
        inventoryPanel.setLayout(inventoryPanelLayout);
        inventoryPanelLayout.setHorizontalGroup(
            inventoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, inventoryPanelLayout.createSequentialGroup()
                .addComponent(inventoryManagementSplitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 1265, Short.MAX_VALUE)
                .addContainerGap())
        );
        inventoryPanelLayout.setVerticalGroup(
            inventoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, inventoryPanelLayout.createSequentialGroup()
                .addComponent(inventoryManagementSplitPane)
                .addContainerGap())
        );

        mainTabbedPane.addTab("    Inventory Management    ", inventoryPanel);

        logoLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/logo-new (Custom).png"))); // NOI18N

        timeLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        timeLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        greetingsLbl.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        logoutBtn.setText("Log Out");
        logoutBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutBtnActionPerformed(evt);
            }
        });

        profileBtn.setText("Profile");
        profileBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                profileBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mainTabbedPane)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(logoLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(greetingsLbl)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(profileBtn)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(logoutBtn))
                            .addComponent(timeLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(logoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(timeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(greetingsLbl)
                            .addComponent(logoutBtn)
                            .addComponent(profileBtn))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mainTabbedPane))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addNewBlendsBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNewBlendsBtnActionPerformed
        String[] counts = order.getOrderCounts();
        if ((Integer.parseInt(counts[0]) > 0) && (Integer.parseInt(counts[1]) > 0)) {
            JOptionPane.showMessageDialog(this, "You have 1 pending order and 1 not completed order. You cannot place new orders.", "Error", JOptionPane.WARNING_MESSAGE);
        } else if (Integer.parseInt(counts[0]) > 1) {
            JOptionPane.showMessageDialog(this, "You have 2 pending orders. You cannot place new orders.", "Error", JOptionPane.WARNING_MESSAGE);
        } else if (Integer.parseInt(counts[1]) > 1) {
            JOptionPane.showMessageDialog(this, "You have 2 not completed orders. You cannot place new orders.", "Error", JOptionPane.WARNING_MESSAGE);
        } else {
            CreateNewBlendOrder1 createNewBlendOrder = new CreateNewBlendOrder1();
            createNewBlendOrder.setVisible(true);
            createNewBlendOrder.pannel = this;
        }
    }//GEN-LAST:event_addNewBlendsBtnActionPerformed

    private void profileBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_profileBtnActionPerformed
        user.getUserDetails();
    }//GEN-LAST:event_profileBtnActionPerformed



    private void logoutBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutBtnActionPerformed
        this.setVisible(false);
        LoginFrame lf = new LoginFrame();
        lf.setVisible(true);
        lf.setSize(740, 400);
    }//GEN-LAST:event_logoutBtnActionPerformed

    private void searchOrderBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchOrderBtnActionPerformed
        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setPannel(this);
        String id = "";
        try {
            id = orderSearchCombo.getSelectedItem().toString();
            Order tmp = order.viewOrder((DefaultTableModel) orderDetails.blendTable.getModel(), (DefaultTableModel) orderDetails.orderDetailsTable.getModel(), id);
            orderDetails.orderIDLabel.setText(tmp.getOrderID());
            orderDetails.dateLabel.setText(tmp.getDate());

            for (int i = 0; i < orderListTable.getRowCount(); i++) {
                if (id.equals(orderListTable.getValueAt(i, 0).toString())) {
                    if (null != orderListTable.getValueAt(i, 1).toString()) {
                        switch (orderListTable.getValueAt(i, 1).toString()) {
                            case "Pending":
                                orderDetails.orderCompletedBtn.setVisible(false);
                                break;
                            case "Received":
                                orderDetails.orderReceivedBtn.setVisible(false);
                                orderDetails.updateOrderBtn.setVisible(false);
                                orderDetails.orderDetailsTable.setEnabled(false);
                                break;
                            case "Completed":
                                orderDetails.orderCompletedBtn.setVisible(false);
                                orderDetails.orderReceivedBtn.setVisible(false);
                                orderDetails.updateOrderBtn.setVisible(false);
                                orderDetails.orderDetailsTable.setEnabled(false);
                                break;
                        }
                    }
                }
            }

            orderDetails.setVisible(true);
            orderDetails.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            orderSearchCombo.setSelectedIndex(-1);
        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(this, "Please select a order ID!!!", "Empty Order Selection", 2);
        }
    }//GEN-LAST:event_searchOrderBtnActionPerformed

    private void searchStockIngBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchStockIngBtnActionPerformed
        searchStockIngredientCombo();
    }//GEN-LAST:event_searchStockIngBtnActionPerformed

    private void searchStockBlendsBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchStockBlendsBtnActionPerformed
        searchStockBlendCombo();
    }//GEN-LAST:event_searchStockBlendsBtnActionPerformed

    private void blendDeliveryBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_blendDeliveryBtnActionPerformed

        int selectedIndex = searchStockBlendComboBox.getSelectedIndex();

        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "Please select an ingredient name!!!", "Empty Ingredient Selection", 2);
        } else {

            String selectedIngName = (String) searchStockBlendComboBox.getSelectedItem();

            Blend blendDelivery = new Blend();

            if (blendDelivery.checkAndLoadBlendDeliverDetails(selectedIngName.replace("'", "\\'"))) {

                searchStockBlendComboBox.setSelectedIndex(-1);

                DeliverBlend deliverBlend = new DeliverBlend();

                deliverBlend.setPannel(this);
                deliverBlend.setVisible(true);
                deliverBlend.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
                deliverBlend.blendNameLbl.setText(blendDelivery.getBlendName());
                deliverBlend.blendCatgLbl.setText(blendDelivery.getBlendCategory());
                deliverBlend.allocatedQtyLbl.setText(String.valueOf(blendDelivery.getAlocatedStock()) + " g");
                deliverBlend.freeQtyLbl.setText(String.valueOf(blendDelivery.getVisibleStock()) + " g");

            } else {
                JOptionPane.showMessageDialog(this, "Please select a valid blend name!!!", "Invalid Blend Name", 2);
                searchStockBlendComboBox.setSelectedIndex(-1);
            }
        }
    }//GEN-LAST:event_blendDeliveryBtnActionPerformed

    /* start of searchStockIngredientCombo method */
    private void searchStockIngredientCombo() {

        int selectedIndex = searchStockIngComboBox.getSelectedIndex();

        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "Please select an ingredient!!!", "Empty Ingredient Selection", 2);
        } else {

            String selectedIngName = (String) searchStockIngComboBox.getSelectedItem();

            Ingredient ingredeintForStock = new Ingredient();

            if (ingredeintForStock.checkAndLoadIngredientStockDetails(selectedIngName.replace("'", "\\'"))) {
                searchStockIngComboBox.setSelectedIndex(-1);

                UpdateIngStock updateStock = new UpdateIngStock();

                updateStock.setPannel(this);
                updateStock.setVisible(true);
                updateStock.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                updateStock.updateStockItemNameLbl.setText(ingredeintForStock.getIngName());
                updateStock.updateStockCategoryLbl.setText(ingredeintForStock.getIngCategoryName());
                updateStock.stockQtyLbl.setText(String.valueOf(ingredeintForStock.getVisibleStock()) + " g");
            } else {
                JOptionPane.showMessageDialog(this, "Plese Select a valid ingredient!!!", "Invalid Ingredient Name", 2);
                searchStockIngComboBox.setSelectedIndex(-1);
            }
        }
    }
    /* end of searchStockIngredientCombo method */

    /* start of searchStockBlendCombo method */
    private void searchStockBlendCombo() {

        int selectedIndex = searchStockBlendComboBox.getSelectedIndex();

        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "Please select a blend!!!", "Empty Blend Selection", 2);
        } else {

            Blend blendForStock = new Blend();

            String selectedBlendName = (String) searchStockBlendComboBox.getSelectedItem();

            if (blendForStock.checkAndLoadBlendStockDetails(selectedBlendName.replace("'", "\\'"))) {
                searchStockBlendComboBox.setSelectedIndex(-1);

                UpdateBlendStock updateBlendStock = new UpdateBlendStock();

                updateBlendStock.setPannel(this);
                updateBlendStock.setVisible(true);
                updateBlendStock.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                updateBlendStock.updateStockItemNameLbl.setText(blendForStock.getBlendName());
                updateBlendStock.updateStockItemCategoryLbl.setText(blendForStock.getBlendCategory());
                updateBlendStock.stockQtyLbl.setText(String.valueOf(blendForStock.getVisibleStock()) + " g");
            } else {
                JOptionPane.showMessageDialog(this, "Please select a valid blend!!!", "Invalid Blend Name", 2);
                searchStockBlendComboBox.setSelectedIndex(-1);
            }
        }
    }

    private void orderSearchComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_orderSearchComboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_orderSearchComboActionPerformed

    private void searchStockBlendComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchStockBlendComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchStockBlendComboBoxActionPerformed

    private void searchStockIngComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchStockIngComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchStockIngComboBoxActionPerformed

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
                if ("Metal".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ManagerPannel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManagerPannel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManagerPannel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManagerPannel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ManagerPannel().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addNewBlendsBtn;
    private javax.swing.JButton blendDeliveryBtn;
    private javax.swing.JLabel greetingsLbl;
    private javax.swing.JLabel inventoryBlendLbl;
    private javax.swing.JLabel inventoryIngredientsLbl;
    private javax.swing.JPanel inventoryManagementBlendPanel;
    private javax.swing.JPanel inventoryManagementIngredientPanel;
    private javax.swing.JSplitPane inventoryManagementSplitPane;
    private javax.swing.JPanel inventoryPanel;
    private javax.swing.JTable inventryBlendTable;
    private javax.swing.JTable inventryIngredientTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JLabel logoLabel;
    private javax.swing.JButton logoutBtn;
    public static javax.swing.JTabbedPane mainTabbedPane;
    private javax.swing.JPanel orderHandlingPanel;
    private javax.swing.JTable orderListTable;
    private javax.swing.JComboBox orderSearchCombo;
    private javax.swing.JButton profileBtn;
    private javax.swing.JButton searchOrderBtn;
    private javax.swing.JComboBox searchStockBlendComboBox;
    private javax.swing.JButton searchStockBlendsBtn;
    private javax.swing.JButton searchStockIngBtn;
    public javax.swing.JComboBox searchStockIngComboBox;
    private javax.swing.JLabel timeLabel;
    // End of variables declaration//GEN-END:variables
    private javax.swing.JTable flavourTable;

}
