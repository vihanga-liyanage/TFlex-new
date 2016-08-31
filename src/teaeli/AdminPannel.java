package teaeli;

import classes.Blend;
import classes.Ingredient;
import classes.StockHistory;
import classes.AutoSuggest;
import classes.Order;
import classes.Supplier;
import classes.User;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
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
import classes.ResultArray;
import java.util.Calendar;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import static teaeli.TeaELI.loginFrame;

public class AdminPannel extends javax.swing.JFrame {

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
    public AdminPannel() {
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
            Logger.getLogger(AdminPannel.class.getName()).log(Level.SEVERE, null, ex);
        }

        initComponents();

        //Changing table headers to bold
        orderListTable.getTableHeader().setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        inventryIngredientTable.getTableHeader().setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        inventryBlendTable.getTableHeader().setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        settingsIngredientTable.getTableHeader().setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        productTable.getTableHeader().setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        blendDetailsTbl.getTableHeader().setFont(new Font("Segoe UI Bold", Font.PLAIN, 14));
        ingStockHistoryTbl.getTableHeader().setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        blendStockHistoryTbl.getTableHeader().setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        userTable.getTableHeader().setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));

        startClock();

        //Keep the window fullscreen
        this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);

        //set all users details to the users table in the users tab
        populateUserTable();

        /*Start of ingredient class method calls*/
        //populate serch ingredient combobox in settings->ingredient
        AutoSuggest searchIngredientComboBoxAutoSuggest = new AutoSuggest();
        searchIngredientComboBoxAutoSuggest.setAutoSuggest(searchIngredientComboBox, ingredient.loadNameForSearchStockIngComboBox());

        searchIngredientComboBox.setSelectedIndex(-1);

        //start of view all ingredients
        populateSettingsIngredientTable();
        //end of view all ingredients

        /* populate inventryIngredientTable in inventory management*/
        populateIngStockTable();

        /*populate inventryBlendTable in the inventory management*/
        populateBlendStockTable();

        /*Populate ingredientstock history*/
        populateIngHistoryTable();

        /*Populate blendstock history*/
        populateBlendHistoryTable();

        /* populate product table in the blend tab*/
        populateProductTable();

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

        /*Auto suggest method loads for the combo box in blends tab*/
        initSettingsBlendCombo();

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

        //method for combox value setting when table row select in settings Ingredient
        final ListSelectionModel selectionalModForSettingsIngTable = settingsIngredientTable.getSelectionModel();
        selectionalModForSettingsIngTable.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent lsevt) {
                if (!selectionalModForSettingsIngTable.isSelectionEmpty()) {
                    int row = selectionalModForSettingsIngTable.getMinSelectionIndex();
                    searchIngredientComboBox.setSelectedItem(settingsIngredientTable.getValueAt(row, 0));
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

        //method for combox value setting when table row select in settings blend tab
        final ListSelectionModel productModel = productTable.getSelectionModel();
        productModel.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent lse) {
                if (!productModel.isSelectionEmpty()) {
                    int row = productModel.getMinSelectionIndex();
                    searchBlendComboBox.setSelectedItem(productTable.getValueAt(row, 1));
                    loadBlendDetails(row);
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

        //method for enter key pressed in ingredient in settings tab
        searchIngredientComboBox.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    serchIngredientCombo();
                }
            }
        });

        searchBlendComboBox.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchProductCombo();
                }
            }
        });

        //enabling sorting for tables
        orderListTable.setAutoCreateRowSorter(true);
        inventryIngredientTable.setAutoCreateRowSorter(true);
        inventryBlendTable.setAutoCreateRowSorter(true);
        settingsIngredientTable.setAutoCreateRowSorter(true);
        productTable.setAutoCreateRowSorter(true);
        blendDetailsTbl.setAutoCreateRowSorter(true);
        ingStockHistoryTbl.setAutoCreateRowSorter(true);
        blendStockHistoryTbl.setAutoCreateRowSorter(true);
        userTable.setAutoCreateRowSorter(true);

    }

    public void setGreetings(String greeting) {
        greetingsLbl.setText(greeting);
    }

    public void populateIngStockTable() {
        ingredient.populateIngredientTable((DefaultTableModel) inventryIngredientTable.getModel());
    }

    public void populateBlendStockTable() {
        blend.populateBlendTable((DefaultTableModel) inventryBlendTable.getModel());
    }

    public void populateIngHistoryTable() {
        ingredientHistoryStock.populateStockIngredientHistoryTable((DefaultTableModel) ingStockHistoryTbl.getModel());
    }

    public void populateBlendHistoryTable() {
        blendHistoryStock.populateStockBlendHistoryTable((DefaultTableModel) blendStockHistoryTbl.getModel());
    }

    public void populateProductTable() {
        blend.populateProductTable((DefaultTableModel) productTable.getModel());
    }

    public void populateUserTable() {
        user.viewUser((DefaultTableModel) userTable.getModel());
    }

    public void populateOrderListTable() {
        order.populateOrderListTable((DefaultTableModel) orderListTable.getModel());
    }

    public void populateSettingsIngredientTable() {
        DefaultTableModel model = (DefaultTableModel) settingsIngredientTable.getModel();
        ingredient.viewAllIngredients(model);
    }

    public void initSettingsBlendCombo() {
        blend.initBlendCombo(searchBlendComboBox);
        searchBlendComboBox.setSelectedIndex(-1);
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
        settingsPanel = new javax.swing.JPanel();
        settingsTabbedPane = new javax.swing.JTabbedPane();
        settingsIngPanel = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        searchIngredientBtn = new javax.swing.JButton();
        addItemBtn = new javax.swing.JButton();
        jScrollPane10 = new javax.swing.JScrollPane();
        settingsIngredientTable = new javax.swing.JTable();
        searchIngredientComboBox = new javax.swing.JComboBox();
        settingsBlendPanel = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        productTable = new javax.swing.JTable();
        searchProductBtn = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        addProductBtn = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        blendDetailsTbl = new javax.swing.JTable();
        inventoryBlendLbl1 = new javax.swing.JLabel();
        searchBlendComboBox = new javax.swing.JComboBox();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        blendNameLbl = new javax.swing.JLabel();
        blendCatgLbl = new javax.swing.JLabel();
        blendBaseLbl = new javax.swing.JLabel();
        settingsIngHistoryPanel = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        ingStockHistoryTbl = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        ingStartDate = new org.jdesktop.swingx.JXDatePicker();
        jLabel8 = new javax.swing.JLabel();
        ingEndDate = new org.jdesktop.swingx.JXDatePicker();
        btnIngredientGo = new javax.swing.JButton();
        btnIngredientHistoryReport = new javax.swing.JButton();
        btnCancelIngHistory = new javax.swing.JButton();
        settingsBlendHistoryPanel = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        blendStockHistoryTbl = new javax.swing.JTable();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        blendStartDate = new org.jdesktop.swingx.JXDatePicker();
        jLabel12 = new javax.swing.JLabel();
        blendEndDate = new org.jdesktop.swingx.JXDatePicker();
        btnBlendGo = new javax.swing.JButton();
        btnBlendHistoryReportGeneration = new javax.swing.JButton();
        BtnCancelBlendHistory = new javax.swing.JButton();
        settingsUserPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        userTable = new javax.swing.JTable();
        addUserBtn = new javax.swing.JButton();
        deleteUserBtn = new javax.swing.JButton();
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
                "Blend Category", "Blend Name", "Buffer Stock (g)", "Invisible Stock (g)"
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

        settingsPanel.setBackground(new java.awt.Color(255, 255, 255));

        settingsTabbedPane.setBackground(new java.awt.Color(255, 255, 255));

        settingsIngPanel.setBackground(new java.awt.Color(255, 255, 255));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("Start typing ingredient name to update");

        searchIngredientBtn.setText("Edit");
        searchIngredientBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchIngredientBtnActionPerformed(evt);
            }
        });

        addItemBtn.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        addItemBtn.setText("Add New Ingredient");
        addItemBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addItemBtnActionPerformed(evt);
            }
        });

        settingsIngredientTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "IngredientName", "Supplier", "Unit Price"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        settingsIngredientTable.setRowHeight(24);
        jScrollPane10.setViewportView(settingsIngredientTable);
        if (settingsIngredientTable.getColumnModel().getColumnCount() > 0) {
            settingsIngredientTable.getColumnModel().getColumn(0).setResizable(false);
            settingsIngredientTable.getColumnModel().getColumn(1).setResizable(false);
            settingsIngredientTable.getColumnModel().getColumn(2).setResizable(false);
        }

        searchIngredientComboBox.setEditable(true);
        searchIngredientComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchIngredientComboBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout settingsIngPanelLayout = new javax.swing.GroupLayout(settingsIngPanel);
        settingsIngPanel.setLayout(settingsIngPanelLayout);
        settingsIngPanelLayout.setHorizontalGroup(
            settingsIngPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, settingsIngPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(settingsIngPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane10)
                    .addGroup(settingsIngPanelLayout.createSequentialGroup()
                        .addGroup(settingsIngPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(settingsIngPanelLayout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 1019, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(settingsIngPanelLayout.createSequentialGroup()
                                .addComponent(searchIngredientComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(searchIngredientBtn)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 723, Short.MAX_VALUE)))
                        .addComponent(addItemBtn)))
                .addContainerGap())
        );
        settingsIngPanelLayout.setVerticalGroup(
            settingsIngPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(settingsIngPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(settingsIngPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(addItemBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(settingsIngPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchIngredientComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchIngredientBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 389, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        settingsTabbedPane.addTab("    Ingredients Edit    ", settingsIngPanel);

        settingsBlendPanel.setBackground(new java.awt.Color(255, 255, 255));

        productTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Blend Code", "Blend Name", "Base Composition"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        productTable.setRowHeight(24);
        productTable.setRowSelectionAllowed(false);
        jScrollPane4.setViewportView(productTable);
        if (productTable.getColumnModel().getColumnCount() > 0) {
            productTable.getColumnModel().getColumn(0).setResizable(false);
            productTable.getColumnModel().getColumn(1).setResizable(false);
            productTable.getColumnModel().getColumn(1).setPreferredWidth(200);
            productTable.getColumnModel().getColumn(2).setResizable(false);
            productTable.getColumnModel().getColumn(2).setPreferredWidth(240);
        }

        searchProductBtn.setText("Edit");
        searchProductBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchProductBtnActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Start typing blend name to view details");

        addProductBtn.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        addProductBtn.setText("Add New Blend");
        addProductBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addProductBtnActionPerformed(evt);
            }
        });

        blendDetailsTbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Ingredient ", "Percentage (%)"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        blendDetailsTbl.setRowHeight(24);
        jScrollPane3.setViewportView(blendDetailsTbl);
        if (blendDetailsTbl.getColumnModel().getColumnCount() > 0) {
            blendDetailsTbl.getColumnModel().getColumn(0).setResizable(false);
            blendDetailsTbl.getColumnModel().getColumn(0).setPreferredWidth(200);
            blendDetailsTbl.getColumnModel().getColumn(1).setResizable(false);
        }

        inventoryBlendLbl1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 16)); // NOI18N
        inventoryBlendLbl1.setText("Blend Details");

        searchBlendComboBox.setEditable(true);
        searchBlendComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchBlendComboBoxActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel13.setText("Blend Name             :");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel14.setText("Blend Category        :");

        jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel16.setText("Base Composition    :");

        blendNameLbl.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        blendCatgLbl.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        blendBaseLbl.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        javax.swing.GroupLayout settingsBlendPanelLayout = new javax.swing.GroupLayout(settingsBlendPanel);
        settingsBlendPanel.setLayout(settingsBlendPanelLayout);
        settingsBlendPanelLayout.setHorizontalGroup(
            settingsBlendPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(settingsBlendPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(settingsBlendPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(settingsBlendPanelLayout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 689, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(settingsBlendPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(inventoryBlendLbl1)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 515, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(settingsBlendPanelLayout.createSequentialGroup()
                                .addGroup(settingsBlendPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                                    .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(settingsBlendPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(blendNameLbl, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                                    .addComponent(blendCatgLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(blendBaseLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGap(0, 8, Short.MAX_VALUE))
                    .addGroup(settingsBlendPanelLayout.createSequentialGroup()
                        .addGroup(settingsBlendPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(settingsBlendPanelLayout.createSequentialGroup()
                                .addComponent(searchBlendComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(searchProductBtn))
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(addProductBtn)))
                .addContainerGap())
        );
        settingsBlendPanelLayout.setVerticalGroup(
            settingsBlendPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(settingsBlendPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(settingsBlendPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(settingsBlendPanelLayout.createSequentialGroup()
                        .addGap(78, 78, 78)
                        .addComponent(blendNameLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(settingsBlendPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(blendCatgLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14))
                        .addGap(18, 18, 18)
                        .addGroup(settingsBlendPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(blendBaseLbl, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(settingsBlendPanelLayout.createSequentialGroup()
                        .addGroup(settingsBlendPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(addProductBtn))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(settingsBlendPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(searchProductBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(searchBlendComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(inventoryBlendLbl1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addGroup(settingsBlendPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(102, 102, 102))
        );

        settingsTabbedPane.addTab("    Blends Edit    ", settingsBlendPanel);

        settingsIngHistoryPanel.setBackground(new java.awt.Color(255, 255, 255));

        ingStockHistoryTbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Ingredient Name", "Old Qty", "Updated Qty", "Reason", "Updated By"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        ingStockHistoryTbl.setRowHeight(23);
        jScrollPane8.setViewportView(ingStockHistoryTbl);
        if (ingStockHistoryTbl.getColumnModel().getColumnCount() > 0) {
            ingStockHistoryTbl.getColumnModel().getColumn(0).setResizable(false);
            ingStockHistoryTbl.getColumnModel().getColumn(0).setPreferredWidth(135);
            ingStockHistoryTbl.getColumnModel().getColumn(1).setResizable(false);
            ingStockHistoryTbl.getColumnModel().getColumn(1).setPreferredWidth(200);
            ingStockHistoryTbl.getColumnModel().getColumn(2).setResizable(false);
            ingStockHistoryTbl.getColumnModel().getColumn(3).setResizable(false);
            ingStockHistoryTbl.getColumnModel().getColumn(4).setResizable(false);
            ingStockHistoryTbl.getColumnModel().getColumn(4).setPreferredWidth(400);
            ingStockHistoryTbl.getColumnModel().getColumn(5).setResizable(false);
            ingStockHistoryTbl.getColumnModel().getColumn(5).setPreferredWidth(200);
        }

        jLabel2.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setText("Search History By Date Range");

        jLabel4.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Starts from");

        jLabel8.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("To");

        ingEndDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ingEndDateActionPerformed(evt);
            }
        });

        btnIngredientGo.setText("Go");
        btnIngredientGo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIngredientGoActionPerformed(evt);
            }
        });

        btnIngredientHistoryReport.setText("Generate Report");
        btnIngredientHistoryReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIngredientHistoryReportActionPerformed(evt);
            }
        });

        btnCancelIngHistory.setText("Cancel");
        btnCancelIngHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelIngHistoryActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout settingsIngHistoryPanelLayout = new javax.swing.GroupLayout(settingsIngHistoryPanel);
        settingsIngHistoryPanel.setLayout(settingsIngHistoryPanelLayout);
        settingsIngHistoryPanelLayout.setHorizontalGroup(
            settingsIngHistoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(settingsIngHistoryPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(settingsIngHistoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 1230, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, settingsIngHistoryPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnIngredientHistoryReport, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(settingsIngHistoryPanelLayout.createSequentialGroup()
                        .addGroup(settingsIngHistoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addGroup(settingsIngHistoryPanelLayout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(18, 18, 18)
                                .addComponent(ingStartDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel8)
                                .addGap(18, 18, 18)
                                .addComponent(ingEndDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnIngredientGo)
                                .addGap(18, 18, 18)
                                .addComponent(btnCancelIngHistory)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        settingsIngHistoryPanelLayout.setVerticalGroup(
            settingsIngHistoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(settingsIngHistoryPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(settingsIngHistoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ingStartDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ingEndDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnIngredientGo)
                    .addComponent(btnCancelIngHistory))
                .addGap(34, 34, 34)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 339, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnIngredientHistoryReport, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        settingsTabbedPane.addTab("    Ingredient Stock History    ", settingsIngHistoryPanel);

        settingsBlendHistoryPanel.setBackground(new java.awt.Color(255, 255, 255));

        blendStockHistoryTbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Blend Name", "Old Qty", "Updated Qty", "Reason", "Updated By"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        blendStockHistoryTbl.setRowHeight(24);
        jScrollPane9.setViewportView(blendStockHistoryTbl);
        if (blendStockHistoryTbl.getColumnModel().getColumnCount() > 0) {
            blendStockHistoryTbl.getColumnModel().getColumn(0).setResizable(false);
            blendStockHistoryTbl.getColumnModel().getColumn(0).setPreferredWidth(135);
            blendStockHistoryTbl.getColumnModel().getColumn(1).setResizable(false);
            blendStockHistoryTbl.getColumnModel().getColumn(1).setPreferredWidth(200);
            blendStockHistoryTbl.getColumnModel().getColumn(2).setResizable(false);
            blendStockHistoryTbl.getColumnModel().getColumn(3).setResizable(false);
            blendStockHistoryTbl.getColumnModel().getColumn(4).setResizable(false);
            blendStockHistoryTbl.getColumnModel().getColumn(4).setPreferredWidth(400);
            blendStockHistoryTbl.getColumnModel().getColumn(5).setResizable(false);
            blendStockHistoryTbl.getColumnModel().getColumn(5).setPreferredWidth(200);
        }

        jLabel10.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel10.setText("Search History By Date Range");

        jLabel11.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Starts from");

        jLabel12.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("To");

        btnBlendGo.setText("Go");
        btnBlendGo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBlendGoActionPerformed(evt);
            }
        });

        btnBlendHistoryReportGeneration.setText("Generate Report");
        btnBlendHistoryReportGeneration.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBlendHistoryReportGenerationActionPerformed(evt);
            }
        });

        BtnCancelBlendHistory.setText("Cancel");
        BtnCancelBlendHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCancelBlendHistoryActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout settingsBlendHistoryPanelLayout = new javax.swing.GroupLayout(settingsBlendHistoryPanel);
        settingsBlendHistoryPanel.setLayout(settingsBlendHistoryPanelLayout);
        settingsBlendHistoryPanelLayout.setHorizontalGroup(
            settingsBlendHistoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(settingsBlendHistoryPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(settingsBlendHistoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 1230, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, settingsBlendHistoryPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnBlendHistoryReportGeneration, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(settingsBlendHistoryPanelLayout.createSequentialGroup()
                        .addGroup(settingsBlendHistoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(settingsBlendHistoryPanelLayout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addGap(18, 18, 18)
                                .addComponent(blendStartDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel12)
                                .addGap(18, 18, 18)
                                .addComponent(blendEndDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnBlendGo)
                                .addGap(18, 18, 18)
                                .addComponent(BtnCancelBlendHistory)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        settingsBlendHistoryPanelLayout.setVerticalGroup(
            settingsBlendHistoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(settingsBlendHistoryPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(settingsBlendHistoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(blendStartDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(blendEndDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBlendGo)
                    .addComponent(BtnCancelBlendHistory))
                .addGap(34, 34, 34)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnBlendHistoryReportGeneration, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        settingsTabbedPane.addTab("    Blend Stock History    ", settingsBlendHistoryPanel);

        settingsUserPanel.setBackground(new java.awt.Color(255, 255, 255));

        userTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "User ID", "Username", "Firstname", "Lastname"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        userTable.setRowHeight(24);
        jScrollPane1.setViewportView(userTable);

        addUserBtn.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        addUserBtn.setText("Add New User");
        addUserBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addUserBtnActionPerformed(evt);
            }
        });

        deleteUserBtn.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        deleteUserBtn.setText("Delete User");
        deleteUserBtn.setMinimumSize(new java.awt.Dimension(200, 25));
        deleteUserBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteUserBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout settingsUserPanelLayout = new javax.swing.GroupLayout(settingsUserPanel);
        settingsUserPanel.setLayout(settingsUserPanelLayout);
        settingsUserPanelLayout.setHorizontalGroup(
            settingsUserPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(settingsUserPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(settingsUserPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1230, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, settingsUserPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(settingsUserPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(deleteUserBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(addUserBtn, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addContainerGap())
        );
        settingsUserPanelLayout.setVerticalGroup(
            settingsUserPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(settingsUserPanelLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(addUserBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(deleteUserBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        settingsTabbedPane.addTab("    Users    ", settingsUserPanel);

        javax.swing.GroupLayout settingsPanelLayout = new javax.swing.GroupLayout(settingsPanel);
        settingsPanel.setLayout(settingsPanelLayout);
        settingsPanelLayout.setHorizontalGroup(
            settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(settingsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(settingsTabbedPane)
                .addContainerGap())
        );
        settingsPanelLayout.setVerticalGroup(
            settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(settingsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(settingsTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 506, Short.MAX_VALUE)
                .addContainerGap())
        );

        mainTabbedPane.addTab("    Settings    ", settingsPanel);

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

    private void addItemBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addItemBtnActionPerformed
        AddIngredient addItem = new AddIngredient();
        addItem.setAdminPannel(this);
        addItem.setVisible(true);
        addItem.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }//GEN-LAST:event_addItemBtnActionPerformed

    private void addProductBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addProductBtnActionPerformed
        AddNewBlend addNewBlend = new AddNewBlend();
        addNewBlend.setVisible(true);
        addNewBlend.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addNewBlend.adminpanel = this;
    }//GEN-LAST:event_addProductBtnActionPerformed

    private void searchIngredientBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchIngredientBtnActionPerformed
        serchIngredientCombo();
    }//GEN-LAST:event_searchIngredientBtnActionPerformed

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

    private void addUserBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addUserBtnActionPerformed
        AddNewUser newUser = new AddNewUser();
        newUser.setAdminPannel(this);
        newUser.setVisible(true);
        newUser.setDefaultCloseOperation(HIDE_ON_CLOSE);
    }//GEN-LAST:event_addUserBtnActionPerformed


    private void searchProductBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchProductBtnActionPerformed
        searchProductCombo();
    }//GEN-LAST:event_searchProductBtnActionPerformed


    private void logoutBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutBtnActionPerformed
        this.setVisible(false);
        loginFrame = new LoginFrame();
        loginFrame.setVisible(true);
        loginFrame.setSize(740, 400);
        this.dispose();
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

    private void deleteUserBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteUserBtnActionPerformed
        DefaultTableModel model = (DefaultTableModel) userTable.getModel();
        if (userTable.getSelectedRow() == -1) {
            if (userTable.getSelectedRow() == 0) {
                JOptionPane.showMessageDialog(this, "Table is empty", "No Records to show", 0);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a row to delete", "Empty Selection", 2);
            }
        } else {
            int dialogButton = JOptionPane.YES_NO_OPTION;
            String uID = model.getValueAt(userTable.getSelectedRow(), 0).toString();
            int a = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete user having Employee ID of " + uID + "? ", "Warning", dialogButton);
            if (a == JOptionPane.YES_OPTION) {

                int id = Integer.parseInt(uID);

                int rst = user.removeUser(id);
                if (rst == 1) {
                    user.viewUser((DefaultTableModel) userTable.getModel());
                    JOptionPane.showMessageDialog(this, "User successfully deleted", "Deleted Successfuly", 1);
                } else {
                    JOptionPane.showMessageDialog(this, "There were some issues with the database. Please contact developers.\n\nError code : AdminPannel 1584", "Error", 0);
                    System.exit(0);
                }

            } else {
                return;
            }

        }
    }//GEN-LAST:event_deleteUserBtnActionPerformed

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
    /* end of searchStockBlendCombo method */

    /* start of searchIngredientCombo method */
    private void serchIngredientCombo() {
        String[] resultArray = new String[5];
        String searchItem = (String) searchIngredientComboBox.getSelectedItem();
        if (searchItem == null) {
            JOptionPane.showMessageDialog(this, "Please select an Ingredient!!!", "Empty Ingredient Selection", 2);
        } else {
            resultArray = ingredient.viewAllDetailsOfAIngredient((String) searchIngredientComboBox.getSelectedItem());
            IngredientDetails itemDetails = new IngredientDetails();
            Supplier supplier = new Supplier();

            //set values to fields in IngredientDetails window
            //load supplier list to combobox
            AutoSuggest supplierComboboxAutoSuggest = new AutoSuggest();
            try {
                supplierComboboxAutoSuggest.setAutoSuggest(itemDetails.supplierCombobox, supplier.loadSuppliersForCombobox());
            } catch (SQLException ex) {
                Logger.getLogger(AdminPannel.class.getName()).log(Level.SEVERE, null, ex);
            }

            itemDetails.itemNameTxt.setText(resultArray[0]);
            itemDetails.setName(resultArray[1]); //set ingid as name
            itemDetails.itemTypeCombo.setSelectedItem(resultArray[2]);
            itemDetails.supplierCombobox.setSelectedItem(resultArray[3]);
            itemDetails.unitPriceTxt.setText(resultArray[4]);

            itemDetails.pannel = this;
            itemDetails.setVisible(true);
            itemDetails.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            searchIngredientComboBox.setSelectedIndex(-1);
        }
    }

    /* sttart of searchProductCombo method */
    private void searchProductCombo() {
        BlendDetails blendDetails = new BlendDetails();
        blendDetails.adminpanel = this;
        String blendID = "";
        String blendName = "";
        int baseID = 0;
        String base = "";
        int ret = 0;

        if (searchBlendComboBox.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Please Select a Blend", "Empty Blend Selection", 2);
        } else {
            try {
                blendName = searchBlendComboBox.getSelectedItem().toString();
                blendID = blend.getBlendIDByBlendName(blendName);
                blendDetails.blendNameTxt.setText(blendName);
                blendDetails.blendCodeTxt.setText(blendID);
                blendDetails.blendCodeTxt.setEditable(false);
                blendDetails.blendNameTxt.setEditable(false);

                Blend blend = new Blend();
                ResultArray res = blend.getDataByBlendID(blendID);
                res.next();
                //setting base component
                baseID = Integer.parseInt(res.getString(0));
                base = blend.getIngByBaseName(baseID);
                //setting blend category
                String category = res.getString(1);
                blendDetails.blendCategoryCombo.setSelectedItem(category);
                
                blendDetails.baseCombo.setSelectedItem(base);

                ingredient.populateBlendIngTable((DefaultTableModel) blendDetails.ingTable.getModel(), blendID);
                ingredient.populateBlendFlavourTable((DefaultTableModel) blendDetails.flavourTable.getModel(), blendID);

                blendDetails.ingCombo.setSelectedIndex(-1);
                //blendDetails.ingCombo.requestFocus();
                blendDetails.flavoursCombo.setSelectedIndex(-1);
                //blendDetails.flavoursCombo.requestFocus();
                blendDetails.setVisible(true);
                blendDetails.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                orderSearchCombo.setSelectedIndex(-1);
            } catch (NullPointerException e) {
                JOptionPane.showMessageDialog(this, "Please select a blend!!!", "Empty Blend Selection", 2);
            }

        }
    }
    /* end of searchProductCombo method */

    private void orderSearchComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_orderSearchComboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_orderSearchComboActionPerformed

    private void searchStockBlendComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchStockBlendComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchStockBlendComboBoxActionPerformed

    private void searchIngredientComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchIngredientComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchIngredientComboBoxActionPerformed

    private void searchStockIngComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchStockIngComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchStockIngComboBoxActionPerformed

    private void searchBlendComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchBlendComboBoxActionPerformed
        // TODO add your handling code here:
        //searchBlendComboBox.setEnabled(true);
    }//GEN-LAST:event_searchBlendComboBoxActionPerformed

    private void btnIngredientGoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIngredientGoActionPerformed
        SimpleDateFormat javadate = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date();

        if (ingStartDate.getDate() == null || ingEndDate.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Date feilds cannot be empty.", "Date Selection Error", 2);
        } else if (ingStartDate.getDate().after(ingEndDate.getDate())) {
            JOptionPane.showMessageDialog(this, "Please enter a valid date range.", "Date Selection Error", 2);
        } else if (ingStartDate.getDate().after(currentDate) || ingEndDate.getDate().after(currentDate)) {
            JOptionPane.showMessageDialog(this, "Date range can't go beyond current date.", "Date Selection Error", 2);
        } else {
            Date start = ingStartDate.getDate();
            Date end = ingEndDate.getDate();

            // increase date by one, because between query doesent get inclusive data in sQlite
            Calendar c = Calendar.getInstance();
            c.setTime(end);
            c.add(Calendar.DATE, 1);
            end = c.getTime();

            String startdate = javadate.format(start);
            String enddate = javadate.format(end);

            if (start.equals(end)) {
                StockHistory ingredientHistoryStock = new StockHistory();
                ingredientHistoryStock.populateStockIngredientHistoryTableForDate((DefaultTableModel) ingStockHistoryTbl.getModel(), startdate, enddate);
                ingredientGo = 1;
            } else {

                StockHistory ingredientHistoryStock = new StockHistory();
                ingredientHistoryStock.populateStockIngredientHistoryTableByDate((DefaultTableModel) ingStockHistoryTbl.getModel(), startdate, enddate);
                ingredientGo = 1;
            }
        }
    }//GEN-LAST:event_btnIngredientGoActionPerformed

    private void btnBlendGoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBlendGoActionPerformed
        SimpleDateFormat javadate = new SimpleDateFormat("yyyy-MM-dd");

        Date currentDate = new Date();

        if (blendStartDate.getDate() == null || blendEndDate.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Date feilds cannot be empty.", "Date Selection Error", 2);
        } else if (blendStartDate.getDate().after(blendEndDate.getDate())) {
            JOptionPane.showMessageDialog(this, "Please enter a valid date range.", "Date Selection Error", 2);
        } else if (blendStartDate.getDate().after(currentDate) || blendEndDate.getDate().after(currentDate)) {
            JOptionPane.showMessageDialog(this, "Date range can't go beyond current date.", "Date Selection Error", 2);
        } else {

            Date start = blendStartDate.getDate();
            Date end = blendEndDate.getDate();

            // increase date by one, because between query doesent get inclusive data in sQlite
            Calendar c = Calendar.getInstance();
            c.setTime(end);
            c.add(Calendar.DATE, 1);
            end = c.getTime();

            String startdate = javadate.format(start);
            String enddate = javadate.format(end);

            if (start.equals(end)) {
                StockHistory stockhistory = new StockHistory();
                stockhistory.populateStockBlendHistoryTableForDate((DefaultTableModel) blendStockHistoryTbl.getModel(), startdate, enddate);
                blendGo = 1;
            } else {
                StockHistory stockhistory = new StockHistory();
                stockhistory.populateStockBlendHistoryTableByDate((DefaultTableModel) blendStockHistoryTbl.getModel(), startdate, enddate);
                blendGo = 1;
            }
        }
    }//GEN-LAST:event_btnBlendGoActionPerformed

    private void ingEndDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ingEndDateActionPerformed

    }//GEN-LAST:event_ingEndDateActionPerformed

    private void btnIngredientHistoryReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIngredientHistoryReportActionPerformed
        if (ingredientGo == 0) {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to genarate the entire history PDF?",
                    "Confirm",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE
            );
            if (confirm == JOptionPane.YES_OPTION) {

                int year = Calendar.getInstance().get(Calendar.YEAR);

                String sDate = String.valueOf(year) + ".01.01";
                String name = sDate + "-" + today;
                DefaultTableModel model = (DefaultTableModel) ingStockHistoryTbl.getModel();
                JTable temp = new JTable(model);
                pdf.IngStockHistoryPdfGeneration(temp, today, name);
            }
        } else {

            String sDate = sdf3.format(ingStartDate.getDate());
            String eDate = sdf3.format(ingEndDate.getDate());
            String name = sDate + "-" + eDate;
            DefaultTableModel model = (DefaultTableModel) ingStockHistoryTbl.getModel();
            JTable temp = new JTable(model);
            pdf.IngStockHistoryPdfGeneration(temp, today, name);
        }
    }//GEN-LAST:event_btnIngredientHistoryReportActionPerformed

    private void BtnCancelBlendHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCancelBlendHistoryActionPerformed
        blendStartDate.setDate(null);
        blendEndDate.setDate(null);
        StockHistory stockhistory = new StockHistory();
        stockhistory.populateStockBlendHistoryTable((DefaultTableModel) blendStockHistoryTbl.getModel());
        blendGo = 0;
    }//GEN-LAST:event_BtnCancelBlendHistoryActionPerformed

    private void btnCancelIngHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelIngHistoryActionPerformed
        ingStartDate.setDate(null);
        ingEndDate.setDate(null);
        StockHistory ingredientHistoryStock = new StockHistory();
        ingredientHistoryStock.populateStockIngredientHistoryTable((DefaultTableModel) ingStockHistoryTbl.getModel());
        ingredientGo = 0;
    }//GEN-LAST:event_btnCancelIngHistoryActionPerformed

    private void btnBlendHistoryReportGenerationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBlendHistoryReportGenerationActionPerformed
        if (blendGo == 0) {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to genarate the entire history PDF?",
                    "Confirm",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE
            );
            if (confirm == JOptionPane.YES_OPTION) {

                int year = Calendar.getInstance().get(Calendar.YEAR);

                String sDate = String.valueOf(year) + ".01.01";
                String name = sDate + "-" + today;
                DefaultTableModel model = (DefaultTableModel) blendStockHistoryTbl.getModel();
                JTable temp = new JTable(model);
                pdf.BlendStockHistoryPdfGeneration(temp, today, name);
            }
        } else {

            String sDate = sdf3.format(blendStartDate.getDate());
            String eDate = sdf3.format(blendEndDate.getDate());
            String name = sDate + "-" + eDate;
            DefaultTableModel model = (DefaultTableModel) blendStockHistoryTbl.getModel();
            JTable temp = new JTable(model);
            pdf.BlendStockHistoryPdfGeneration(temp, today, name);
        }
    }//GEN-LAST:event_btnBlendHistoryReportGenerationActionPerformed

    /* start of loadBlendDetails method */
    private void loadBlendDetails(int row) {

        Blend blendDetails = new Blend();

        blendDetails.setBlendID((String) productTable.getValueAt(row, 0));
        blendDetails.setBlendName((String) productTable.getValueAt(row, 1));
        blendDetails.setBaseName((String) productTable.getValueAt(row, 2));

        blendDetails.setBlendName(blendDetails.getBlendName().replace("'", "\\'"));
        blendDetails.setBaseName(blendDetails.getBaseName().replace("'", "\\'"));

        blendDetails.setBlendName(blendDetails.getBlendName().trim());
        blendDetails.setBaseName(blendDetails.getBaseName().trim());

        blendNameLbl.setText(blendDetails.getBlendName());
        blendBaseLbl.setText(blendDetails.getBaseName());

        blendDetails.getBlendCatgFromBlendID();
        blendCatgLbl.setText(blendDetails.getBlendCategory());

        boolean load = blendDetails.loadBlendIngredientDetails((DefaultTableModel) blendDetailsTbl.getModel());

        if (load) {
            load = blendDetails.loadBlendFlavourDetails((DefaultTableModel) blendDetailsTbl.getModel());
        } else {
            JOptionPane.showMessageDialog(this, "No recepie found for this Blend.", "No recepie", 0);
        }
    }
    /* end of loadBlendDetails method */

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
            java.util.logging.Logger.getLogger(AdminPannel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AdminPannel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AdminPannel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AdminPannel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AdminPannel().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnCancelBlendHistory;
    private javax.swing.JButton addItemBtn;
    private javax.swing.JButton addNewBlendsBtn;
    private javax.swing.JButton addProductBtn;
    private javax.swing.JButton addUserBtn;
    private javax.swing.JLabel blendBaseLbl;
    private javax.swing.JLabel blendCatgLbl;
    private javax.swing.JButton blendDeliveryBtn;
    private javax.swing.JTable blendDetailsTbl;
    private org.jdesktop.swingx.JXDatePicker blendEndDate;
    private javax.swing.JLabel blendNameLbl;
    private org.jdesktop.swingx.JXDatePicker blendStartDate;
    public javax.swing.JTable blendStockHistoryTbl;
    private javax.swing.JButton btnBlendGo;
    private javax.swing.JButton btnBlendHistoryReportGeneration;
    private javax.swing.JButton btnCancelIngHistory;
    private javax.swing.JButton btnIngredientGo;
    private javax.swing.JButton btnIngredientHistoryReport;
    private javax.swing.JButton deleteUserBtn;
    private javax.swing.JLabel greetingsLbl;
    private org.jdesktop.swingx.JXDatePicker ingEndDate;
    private org.jdesktop.swingx.JXDatePicker ingStartDate;
    public javax.swing.JTable ingStockHistoryTbl;
    private javax.swing.JLabel inventoryBlendLbl;
    private javax.swing.JLabel inventoryBlendLbl1;
    private javax.swing.JLabel inventoryIngredientsLbl;
    private javax.swing.JPanel inventoryManagementBlendPanel;
    private javax.swing.JPanel inventoryManagementIngredientPanel;
    private javax.swing.JSplitPane inventoryManagementSplitPane;
    private javax.swing.JPanel inventoryPanel;
    private javax.swing.JTable inventryBlendTable;
    private javax.swing.JTable inventryIngredientTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JLabel logoLabel;
    private javax.swing.JButton logoutBtn;
    public static javax.swing.JTabbedPane mainTabbedPane;
    private javax.swing.JPanel orderHandlingPanel;
    private javax.swing.JTable orderListTable;
    private javax.swing.JComboBox orderSearchCombo;
    private javax.swing.JTable productTable;
    private javax.swing.JButton profileBtn;
    private javax.swing.JComboBox searchBlendComboBox;
    private javax.swing.JButton searchIngredientBtn;
    public javax.swing.JComboBox searchIngredientComboBox;
    private javax.swing.JButton searchOrderBtn;
    private javax.swing.JButton searchProductBtn;
    private javax.swing.JComboBox searchStockBlendComboBox;
    private javax.swing.JButton searchStockBlendsBtn;
    private javax.swing.JButton searchStockIngBtn;
    public javax.swing.JComboBox searchStockIngComboBox;
    private javax.swing.JPanel settingsBlendHistoryPanel;
    private javax.swing.JPanel settingsBlendPanel;
    private javax.swing.JPanel settingsIngHistoryPanel;
    private javax.swing.JPanel settingsIngPanel;
    public static javax.swing.JTable settingsIngredientTable;
    private javax.swing.JPanel settingsPanel;
    private javax.swing.JTabbedPane settingsTabbedPane;
    private javax.swing.JPanel settingsUserPanel;
    private javax.swing.JLabel timeLabel;
    public javax.swing.JTable userTable;
    // End of variables declaration//GEN-END:variables
    private javax.swing.JTable flavourTable;

}
