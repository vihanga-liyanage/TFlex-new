package classes;

import static classes.DBConnection.logger;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

public class Ingredient {

    // attributes
    private int ingID, ingCategoryID, supID;
    private float orderReqQty, orderExcessQty, oldStockQty, updatedStockQTy;
    private String ingName, ingCategoryName, stockUpdateReason;
    private float unitPrice, visibleStock, alocatedStock, invisibleStock;

    DBConnection dbConn = DBConnection.getInstance();

    //constructor
    public Ingredient() {
        this.ingID = 0;
        this.ingCategoryID = 0;
        this.visibleStock = 0;
        this.alocatedStock = 0;
        this.invisibleStock = 0;
        this.ingName = "";
        this.ingCategoryName = "";
        this.stockUpdateReason = "";
        this.supID = 0;
        this.oldStockQty = 0;
        this.updatedStockQTy = 0;
        this.unitPrice = 0.0f;
        this.orderReqQty = 0;
        this.orderExcessQty = 0;
    }

    //getters and setters
    public int getIngID() {
        return ingID;
    }

    public void setIngID(int ingID) {
        this.ingID = ingID;
    }

    public int getIngCategoryID() {
        return ingCategoryID;
    }

    public void setIngCategoryID(int ingCategoryID) {
        this.ingCategoryID = ingCategoryID;
    }

    public float getVisibleStock() {
        return visibleStock;
    }

    public void setVisibleStock(float visibleStock) {
        this.visibleStock = visibleStock;
    }

    public float getAlocatedStock() {
        return alocatedStock;
    }

    public void setAlocatedStock(float alocatedStock) {
        this.alocatedStock = alocatedStock;
    }

    public float getInvisibleStock() {
        return invisibleStock;
    }

    public void setInvisibleStock(float invisibleStock) {
        this.invisibleStock = invisibleStock;
    }

    public int getSupID() {
        return supID;
    }

    public void setSupID(int supID) {
        this.supID = supID;
    }

    public String getIngName() {
        return ingName;
    }

    public void setIngName(String ingName) {
        this.ingName = ingName;
    }

    public float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public float getOrderReqQty() {
        return orderReqQty;
    }

    public void setOrderReqQty(float orderReqQty) {
        this.orderReqQty = orderReqQty;
    }

    public float getOrderExcessQty() {
        return orderExcessQty;
    }

    public void setOrderExcessQty(float orderExcessQty) {
        this.orderExcessQty = orderExcessQty;
    }

    public String getIngCategoryName() {
        return ingCategoryName;
    }

    public void setIngCategoryName(String ingCategoryName) {
        this.ingCategoryName = ingCategoryName;
    }

    public String getStockUpdateReason() {
        return stockUpdateReason;
    }

    public void setStockUpdateReason(String stockUpdateReason) {
        this.stockUpdateReason = stockUpdateReason;
    }

    public float getOldStockQty() {
        return oldStockQty;
    }

    public void setOldStockQty(float oldStockQty) {
        this.oldStockQty = oldStockQty;
    }

    public float getUpdatedStockQTy() {
        return updatedStockQTy;
    }

    public void setUpdatedStockQTy(float updatedStockQTy) {
        this.updatedStockQTy = updatedStockQTy;
    }

    /* Get blend data when ing name is given -thisara */
    public ResultArray getIngDataByIngName(String ingName) {
        ResultArray res = null;
        this.setIngName(ingName.replace("'", "''"));
        String query = "SELECT * FROM ingredient WHERE ingName='" + this.getIngName() + "'";
        res = dbConn.getResultArray(query);
        return res;
    }

    /* start of populateIngredientTable method */
    public void populateIngredientTable(DefaultTableModel tableModel) {
        ResultArray resultSet;
        String query = "SELECT ing.categoryName , i.ingName,i.visibleStock,i.invisibleStock "
                + "FROM ingredient i JOIN ingredientcategory ing ON i.ingCategoryID = ing.ingCategoryID "
                + "ORDER BY ing.categoryName,i.ingName ";
        resultSet = dbConn.getResultArray(query);
        tableModel.setRowCount(0);

        while (resultSet.next()) {
            Vector newRow = new Vector();
            for (int i = 0; i <= 4; i++) {
                newRow.addElement(resultSet.getString(i));
            }
            tableModel.addRow(newRow);
        }
    }

    //Populate Blend detail's ingredients table according to blend
    public void populateBlendIngTable(DefaultTableModel tableModel, String blendID) {
        ResultArray resultSet;
        String query = "SELECT I.ingName, R.ingPercent "
                + "FROM ingredient I, recipie R "
                + "WHERE I.ingID = R.ingID AND R.blendID = '" + blendID + "' AND R.type = 0";
        resultSet = dbConn.getResultArray(query);
        tableModel.setRowCount(0);
        while (resultSet.next()) {
            String nme = resultSet.getString(0);
            double per = Double.parseDouble(resultSet.getString(1));
            tableModel.addRow(new Object[]{nme,per});
        }
    }

    //Populate Blend detail's flavours table according to blend
    public void populateBlendFlavourTable(DefaultTableModel tableModel, String blendID) {
        ResultArray resultSet;
        String query = "SELECT I.ingName, R.ingPercent "
                + "FROM ingredient I, recipie R "
                + "WHERE I.ingID = R.ingID AND R.blendID = '" + blendID + "' AND R.type = 1";
        resultSet = dbConn.getResultArray(query);
        tableModel.setRowCount(0);
        while (resultSet.next()) {
            String nme = resultSet.getString(0);
            double per = Double.parseDouble(resultSet.getString(1));
            tableModel.addRow(new Object[]{nme,per});
        }
    }

    /* start of initializing ing combo in AddNewBlend */
    public void initIngCombo(JComboBox ingCombo) {
        ResultArray res = null;
        AutoSuggest autoSuggest = new AutoSuggest();
        String query = "SELECT ingName FROM ingredient "
                + "WHERE ingCategoryID=1 OR ingCategoryID=3 OR ingCategoryID=4 OR ingCategoryID=5 OR ingCategoryID=6 OR ingCategoryID=7 "
                + "ORDER BY ingName";
        res = dbConn.getResultArray(query);
        autoSuggest.setAutoSuggest(ingCombo, res);
        ingCombo.setSelectedIndex(-1);
    }

    /* start of initializing flavour combo in AddNewBlend */
    public void initFlavourCombo(JComboBox ingCombo) {
        ResultArray res = null;
        AutoSuggest autoSuggest = new AutoSuggest();
        String query = "SELECT ingName FROM ingredient WHERE ingCategoryID=2 ORDER BY ingName";
        res = dbConn.getResultArray(query);
        autoSuggest.setAutoSuggest(ingCombo, res);
    }

    /* start of loadNameForSearchStockIngComboBox method*/
    public ResultArray loadNameForSearchStockIngComboBox() {
        String query = "SELECT ingName FROM ingredient";
        return dbConn.getResultArray(query);
    }

    /* start of initializing flavours combo in AddNewBlend */
    public void initBaseCombo(JComboBox ingCombo) {
        ResultArray res = null;
        AutoSuggest autoSuggest = new AutoSuggest();
        String query = "SELECT ingName FROM ingredient WHERE ingCategoryID=2 OR ingCategoryID=3 OR ingCategoryID=4 OR ingCategoryID=5 OR ingCategoryID=6 ORDER BY ingName";
        res = dbConn.getResultArray(query);
        autoSuggest.setAutoSuggest(ingCombo, res);
    }

    /* start of checkAndLoadIngredientStockDetails method */
    public boolean checkAndLoadIngredientStockDetails(String selectedIngName) {
        boolean validIngName = false;
        ResultArray resultArray;
        this.setIngName(selectedIngName.replace("'", "''"));

        try {
            //query to load ingredient details
            String query = "SELECT i.ingName, i.visibleStock, ing.categoryName FROM ingredient i JOIN ingredientcategory ing ON i.ingCategoryID = ing.ingCategoryID WHERE ingName = '" + selectedIngName + "'";
            resultArray = dbConn.getResultArray(query);
            if (resultArray.next()) {

                //set ingeredient attribute values
                this.setIngName(resultArray.getString(0));
                this.setVisibleStock(Float.parseFloat(resultArray.getString(1)));
                this.setIngCategoryName(resultArray.getString(2));

                validIngName = true;
            }
        } catch (NumberFormatException e) {
            logger.log(Level.WARNING, e.getMessage());
            JOptionPane.showMessageDialog(null, "There were some issues with the database. Please contact developers.\n\nError code : Ingredient 277", "Error", 0);
            System.exit(0);
        }
        return validIngName;
    }

    /* start of getIngIDFromIngName method */
    public void getIngIDFromIngName() {
        this.setIngName(this.getIngName().replace("'", "''"));
        ResultArray resultArray;
        try {
            String query = "SELECT ingID FROM ingredient WHERE ingName = '" + this.getIngName() + "'";
            resultArray = dbConn.getResultArray(query);
            if (resultArray.next()) {
                this.setIngID(Integer.parseInt(resultArray.getString(0)));
            }
        } catch (NumberFormatException e) {
            logger.log(Level.WARNING, e.getMessage());
            JOptionPane.showMessageDialog(null, "There were some issues with the database. Please contact developers.\n\nError code : Ingredient 294", "Error", 0);
            System.exit(0);
        }
    }

    /* start of updateStockQty method */
    public boolean updateStockQty() {
        boolean updated = false;

        //get user id 
        User updatedUser = new User();
        updatedUser.getIDByUsername();

        //get ingID of the ingredient
        this.getIngIDFromIngName();

        //query to update ingredient stock
        String query = "UPDATE ingredient SET visibleStock = '" + this.getVisibleStock() + "' WHERE ingID = '" + this.getIngID() + "'";

        int i = dbConn.updateResult(query);

        if (i == 1) {
            //query to inesrt into stock history
            query = "INSERT INTO ingredientstockhistory"
                    + "(`ingID`,`oldQty`, `updatedQty`, `reason`, `updatedBy`)"
                    + " VALUES ('" + this.getIngID() + "', '" + this.getOldStockQty() + "','" + this.getUpdatedStockQTy() + "','" + this.getStockUpdateReason() + "','" + updatedUser.getUserID() + "')";

            i = dbConn.updateResult(query);

            if (i == 1) {
                updated = true;
            }
        }
        return updated;
    }

    //start of view all ingredients method
    public void viewAllIngredients(DefaultTableModel table) {
        ResultArray res = null;
        String query = "SELECT ingName,unitPrice,supName FROM ingredient,supplier where ingredient.supID = supplier.supID";
        res = dbConn.getResultArray(query);
        table.setRowCount(0);
        while (res.next()) {
            //DefaultTableModel model = (DefaultTableModel) adminPannel.settingsIngredientTable.getModel();
            table.addRow(new Object[]{res.getString(0), res.getString(2), res.getString(1)});
        }
    }

    public String getUnitPriceByIngName(String ingName) {
        String unitPrice = "";
        ResultArray res = null;
        this.setIngName(ingName.replace("'", "''"));
        String query = "SELECT unitPrice FROM ingredient where ingredient.ingName = '" + this.getIngName() + "' ";
        res = dbConn.getResultArray(query);
        if (res.next()) {
            unitPrice = res.getString(0);
        }
        return unitPrice;
    }

    //start of view all details of a ingredient
    public String[] viewAllDetailsOfAIngredient(String ingredientName) {
        ResultArray res = null;
        String[] resultArray = new String[5];
        //set name of the ingredient
        resultArray[0] = ingredientName;

        String ingName = ingredientName.replace("'", "''");
        
        String query = "SELECT ingID,categoryName,supName,unitPrice "
                + "FROM ingredient I,supplier S,ingredientcategory IC "
                + "where I.ingName = '" + ingName + "' and I.supID = S.supID and I.ingCategoryID = IC.ingCategoryID;";
        
        res = dbConn.getResultArray(query);
        
        while (res.next()) {
            for (int i = 1; i <= 4; i++) {
                resultArray[i] = res.getString(i-1);
            }
        }
        return resultArray;
    }

    //start of update ingredient method
    public int updateIngredient(int ingredientID, String ingredientName, int ingCategory, int supID, float unitPrice) throws SQLException {
        int insertOK = 0;
        
        //set name of the ingredient
        String replacedIngName = ingredientName.replace("'", "''");
        
        String query = "Update ingredient SET ingName = '" + replacedIngName + "', ingCategoryID = '" + ingCategory + "',supID= '" + supID + "',unitPrice = '" + unitPrice + "' WHERE ingID = '" + ingredientID + "'";
        insertOK = dbConn.updateResult(query);
        return insertOK;
    }

    public ResultArray getSupplierDetails() {
        ResultArray res = null;
        String query = "SELECT * FROM supplier";
        res = dbConn.getResultArray(query);
        return res;
    }

    public int addNewIngredient(String Name, String type, String supplier, float price) {
        String rslt1= "", rslt2 ="";
        
        String query1 = "SELECT ingCategoryID FROM ingredientcategory WHERE categoryName = '" + type + "' ";
        
        ResultArray rs1 = dbConn.getResultArray(query1);
        
        rs1.next();
        rslt1 = rs1.getString(0);
        
        String supplierName = supplier.replace("'", "''");
        
        String query2 = "SELECT supID FROM supplier WHERE supName = '" + supplierName + "' ";
        
        ResultArray rs2 = dbConn.getResultArray(query2);
        rs2.next();
        rslt2 = rs2.getString(0);

        String replacedIngName = Name.replace("'", "''");
        
        String query3 = "INSERT INTO ingredient(ingName,ingCategoryID,visibleStock,alocatedStock,invisibleStock,supID,unitPrice) values('" + replacedIngName + "','" + rslt1 + "',0,0,0,'" + rslt2 + "','" + price + "') ";
        
        int rslt3 = dbConn.updateResult(query3);
        return rslt3;
    }

    //getting ingredient data by ingID
    public ResultArray getIngDataByID(String ingID) {
        String query = "SELECT i.ingID, i.ingName, ic.categoryName, i.visibleStock, i.alocatedStock, i.invisibleStock, s.supName  \n"
                + "FROM ingredient i INNER JOIN supplier s ON i.supID=s.supID INNER JOIN ingredientcategory ic ON i.ingCategoryID=ic.ingCategoryID\n"
                + "WHERE ingID='" + ingID + "'";

        return dbConn.getResultArray(query);
    }

    public String getIngIDByIngName(String base) {
        
        String baseName = base.replace("'", "''");
        
        String query = "SELECT ingID FROM ingredient WHERE ingName = '" + baseName + "' ";
        
        ResultArray res = dbConn.getResultArray(query);
        res.next();
        return res.getString(0);
    }

    //updating blend stocks after a new order
    public boolean updateIngredientStock(String[] data) {
        String query = "UPDATE ingredient SET visibleStock='" + data[0] + "', invisibleStock='" + data[1] + "' WHERE ingID='" + data[2] + "'";
        return (dbConn.updateResult(query) == 1);
    }
    
    /* start of updateIngredientStock method -- for orderRecieved when no pending */
    public boolean updateIngredientStockWithoutPending(){
        String replacedIngName = this.getIngName().replace("'", "''");
        
        String query = "UPDATE ingredient SET "
                + "visibleStock = visibleStock + '" + this.getOrderExcessQty() + "' "
                + " , invisibleStock = invisibleStock - '" + this.getOrderExcessQty() + "' "
                + " WHERE ingName = '" + replacedIngName + "' ";
        
        return (dbConn.updateResult(query) == 1);
    }
    
    /* start of updateIngredientStock method -- for orderRecieved */
    public boolean updateIngredientStockWithPending(){
        String replacedIngName = this.getIngName().replace("'", "''");
        
        String query = "UPDATE ingredient SET "
                + "visibleStock = '" + this.getVisibleStock() + "', invisibleStock = '" + this.getInvisibleStock() + "' "
                + " WHERE ingName = '" + replacedIngName + "' ";
        
        return (dbConn.updateResult(query) == 1);
    }
}
