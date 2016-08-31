package classes;

import static classes.DBConnection.logger;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Blend {

    //attributes
    private String blendID, blendName, baseName, blendCategory, stockUpdateReason;
    private int visibleStock, alocatedStock, invisibleStock;
    private int orderReqQty, orderExcessQty, oldStockQty, updatedStockQTy;
    private int deliverQty, delRemoveQty, sampleQty;

    Ingredient ingredient;
    DBConnection dbConn = DBConnection.getInstance();

    //constructor
    public Blend() {
        this.blendID = "";
        this.blendName = "";
        this.baseName = "";
        this.blendCategory = "";
        this.stockUpdateReason = "";
        this.visibleStock = 0;
        this.alocatedStock = 0;
        this.invisibleStock = 0;
        this.orderExcessQty = 0;
        this.orderReqQty = 0;
        this.oldStockQty = 0;
        this.updatedStockQTy = 0;
        this.deliverQty = 0;
        this.delRemoveQty = 0;
        this.sampleQty = 0;

        ingredient = new Ingredient();
    }

    /* Start of setters and getters */
    public String getBlendID() {
        return blendID;
    }

    public void setBlendID(String blendID) {
        this.blendID = blendID;
    }

    public String getBlendName() {
        return blendName;
    }

    public void setBlendName(String blendName) {
        this.blendName = blendName;
    }

    public String getBaseName() {
        return baseName;
    }

    public void setBaseName(String baseName) {
        this.baseName = baseName;
    }

    public int getVisibleStock() {
        return visibleStock;
    }

    public void setVisibleStock(int visibleStock) {
        this.visibleStock = visibleStock;
    }

    public int getAlocatedStock() {
        return alocatedStock;
    }

    public void setAlocatedStock(int alocatedStock) {
        this.alocatedStock = alocatedStock;
    }

    public int getInvisibleStock() {
        return invisibleStock;
    }

    public void setInvisibleStock(int invisibleStock) {
        this.invisibleStock = invisibleStock;
    }

    public int getOrderReqQty() {
        return orderReqQty;
    }

    public void setOrderReqQty(int orderReqQty) {
        this.orderReqQty = orderReqQty;
    }

    public int getOrderExcessQty() {
        return orderExcessQty;
    }

    public void setOrderExcessQty(int orderExcessQty) {
        this.orderExcessQty = orderExcessQty;
    }

    public String getBlendCategory() {
        return blendCategory;
    }

    public void setBlendCategory(String blendCategory) {
        this.blendCategory = blendCategory;
    }

    public String getStockUpdateReason() {
        return stockUpdateReason;
    }

    public void setStockUpdateReason(String stockUpdateReason) {
        this.stockUpdateReason = stockUpdateReason;
    }

    public int getOldStockQty() {
        return oldStockQty;
    }

    public void setOldStockQty(int oldStockQty) {
        this.oldStockQty = oldStockQty;
    }

    public int getUpdatedStockQTy() {
        return updatedStockQTy;
    }

    public void setUpdatedStockQTy(int updatedStockQTy) {
        this.updatedStockQTy = updatedStockQTy;
    }

    public void setDeliverQty(int deliverQty) {
        this.deliverQty = deliverQty;
    }

    public void setDelRemoveQty(int delRemoveQty) {
        this.delRemoveQty = delRemoveQty;
    }

    public int getSampleQty() {
        return sampleQty;
    }

    public void setSampleQty(int sampleQty) {
        this.sampleQty = sampleQty;
    }


    /* start of populateBlendTable method */
    public void populateBlendTable(DefaultTableModel tableModel) {
        ResultArray resultArray;
        String query = "SELECT blendCategory,blendName,visibleStock,invisibleStock FROM blend ORDER BY blendCategory, blendName";
        resultArray = dbConn.getResultArray(query);
        tableModel.setRowCount(0);
        while (resultArray.next()) {
            Vector newRow = new Vector();
            for (int i = 0; i <= 4; i++) {
                newRow.addElement(resultArray.getString(i));
            }
            tableModel.addRow(newRow);
        }
    }

    /* start of initializing blend combo in CreateNewBlendOrder */
    public void initBlendCombo(JComboBox blendsCombo) {
        AutoSuggest autoSuggest = new AutoSuggest();
        String query = "SELECT blendName FROM blend ORDER BY blendName";
        ResultArray res = dbConn.getResultArray(query);
        autoSuggest.setAutoSuggest(blendsCombo, res);
    }

    /* start of loadNameForSearchStockBlendsComboBox method */
    public ResultArray loadNameForSearchStockBlendsComboBox() {
        ResultArray resultArray = null;
        String query = "SELECT blendName FROM blend ";
        resultArray = dbConn.getResultArray(query);
        return resultArray;
    }

    /* start of checkAndLoadBlendStockDetails method */
    public boolean checkAndLoadBlendStockDetails(String selectedBlendName) {
        boolean validBlendName = false;
        
        String repBlendName = selectedBlendName.replace("'", "''");
        
        ResultArray resultArray;
        try {
            String query = "SELECT blendName, visibleStock, blendCategory FROM blend WHERE blendName = '" + repBlendName + "'";
            
            resultArray = dbConn.getResultArray(query);
            if (resultArray.next()) {
                //set blend details
                this.setBlendName(resultArray.getString(0));
                this.setVisibleStock(Integer.parseInt(resultArray.getString(1)));
                this.setBlendCategory(resultArray.getString(2));

                validBlendName = true;
            }
        } catch (NumberFormatException e) {
            logger.log(Level.WARNING, e.getMessage());
            JOptionPane.showMessageDialog(null, "There were some issues with the database. Please contact developers.\n\nError code : Blend 208", "Error", 0);
            System.exit(0);
        }
        return validBlendName;
    }

    /* start of getBlendIDFromBlendName method */
    public void getBlendIDFromBlendName() {
        ResultArray resultArray;
        
        this.setBlendName(this.getBaseName().replace("'","''"));
        
        String query = "SELECT blendID FROM blend WHERE blendName = '" + this.getBlendName() + "'";
        resultArray = dbConn.getResultArray(query);
        if (resultArray.next()) {
            this.setBlendID(resultArray.getString(0));
        }
    }

    /* start of updateStockQty method */
    public boolean updateStockQty() {
        boolean updated = false;

        try {
            //get current user id
            User updatedUser = new User();
            updatedUser.getIDByUsername();

            //get blendID
            this.getBlendIDFromBlendName();

            //query to update blend stock
            String query = "UPDATE blend SET visibleStock = '" + this.getVisibleStock() + "' WHERE blendID = '" + this.getBlendID() + "'";

            int i = dbConn.updateResult(query);

            if (i == 1) {
                //query to insert into stock history table
                query = "INSERT INTO blendstockhistory "
                        + "(`blendID`, `oldQty`, `updatedQty`, `reason`, `updatedBy`) "
                        + "VALUES ('" + this.getBlendID() + "', '" + this.getOldStockQty() + "', '" + this.getUpdatedStockQTy() + "', '" + this.getStockUpdateReason() + "', '" + updatedUser.getUserID() + "')";

                i = dbConn.updateResult(query);

                if (i == 1) {
                    updated = true;
                }
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage());
            JOptionPane.showMessageDialog(null, "There were some issues with the database. Please contact developers.\n\nError code : Blend 260", "Error", 0);
            System.exit(0);
        }
        return updated;
    }

    /* Get blend data when blend name is given */
    public ResultArray getBlendDataByBlendName(String blendName) {
        String repBlendName = blendName.replace("'", "''");
        String query = "SELECT * FROM blend WHERE blendName='" + repBlendName + "'";
        return dbConn.getResultArray(query);
    }

    public String getBlendNameByBlendID(String blendID) {
        String query = "SELECT blendName FROM blend WHERE blendID = '" + blendID + "' ";
        ResultArray res = dbConn.getResultArray(query);
        res.next();
        return res.getString(0);
    }

    public ResultArray getDataByBlendID(String blendID) {
        String query = "SELECT baseID, blendCategory FROM blend WHERE blendID = '" + blendID + "' ";
        ResultArray res = dbConn.getResultArray(query);
        return res;
    }

    public String getIngByBaseName(int base) {
        String query = "SELECT ingName FROM ingredient WHERE ingID = '" + base + "' ";
        ResultArray res = dbConn.getResultArray(query);
        res.next();
        return res.getString(0);
    }

    public String getBlendIDByBlendName(String blendName) {
        String repBlendName = blendName.replace("'", "''");
        String query = "SELECT blendID FROM blend WHERE blendName = '" + repBlendName + "' ";
        ResultArray res = dbConn.getResultArray(query);
        res.next();
        return res.getString(0);
    }

    public int checkExistingBlendID(String blendID) {
        String query = "SELECT blendID FROM blend WHERE blendID = '" + blendID + "' ";
        ResultArray res = dbConn.getResultArray(query);

        if (res.next() == true) {
            return 1;
        } else {
            return 0;
        }
    }

    public int checkExistingBlendName(String blendName) {
        String replacedBlendName = blendName.replace("'", "''");
        String query = "SELECT blendID FROM blend WHERE blendName = '" + replacedBlendName + "' ";
        ResultArray res = dbConn.getResultArray(query);

        if (res.next() == true) {
            return 1;
        } else {
            return 0;
        }
    }

    public int getIngIDRecByIngName(String ingName) {
        String replacedIngName = ingName.replace("'", "''");
        String query = "SELECT ingID FROM ingredient WHERE ingName = '" + replacedIngName + "' ";
        int ID = 0;
        ResultArray res = new ResultArray();
        res = dbConn.getResultArray(query);
        if (res.next()) {
            ID = Integer.parseInt(res.getString(0));
        }
        return ID;
    }

    /* start of the method to load values to the productTable in the blends tab*/
    public void populateProductTable(DefaultTableModel tModel) {
        ResultArray resultArray;
        String query = "SELECT b.blendID, b.blendName, i.ingName FROM ingredient i JOIN blend b ON i.ingID = b.baseID ORDER BY b.blendName";
        resultArray = dbConn.getResultArray(query);
        tModel.setRowCount(0);
        while (resultArray.next()) {
            tModel.addRow(new Object[]{resultArray.getString(0), resultArray.getString(1), resultArray.getString(2)});
        }
    }

    //Add new blend method
    public int addNewBlend(String blendID, String blendName, String base, String blendCategory) {
        String baseCom = ingredient.getIngIDByIngName(base);
        String replacedBlendName = blendName.replace("'", "''");
        String query = "INSERT INTO blend values('" + blendID + "','" + replacedBlendName + "','" + baseCom + "',0,0,0,'" + blendCategory + "') ";
        int ret = dbConn.updateResult(query);
        return ret;
    }

    //Add new recipie
    public int addRecipie(String[] data) {
        String query = "INSERT INTO recipie (blendID, ingID, ingPercent, type) "
                + "VALUES ('" + data[0] + "','" + data[1] + "','" + data[2] + "','" + data[3] + "')";
        return dbConn.updateResult(query);
    }

    //Update Blend when Change the base/category or both
    public int updateBlend(String blendID, String blendName, String base, String blendCategory) {
        String repBlendName = blendName.replace("'", "''");
        String baseCom = ingredient.getIngIDByIngName(base);
        String query = "UPDATE blend SET baseID = '" + baseCom + "', blendCategory = '" + blendCategory + "' WHERE blendID = '" + blendID + "' ";
        int ret = dbConn.updateResult(query);
        return ret;
    }

    /* start of checkAndLoadBlendDeliverDetails method */
    public boolean checkAndLoadBlendDeliverDetails(String selectedBlendName) {
        boolean validBlendName = false;
        ResultArray resultArray;
        String repBlendName = selectedBlendName.replace("'", "''");
        
        try {
            String query = "SELECT blendName, visibleStock, alocatedStock, blendCategory FROM blend WHERE blendName = '" + repBlendName + "'";
            resultArray = dbConn.getResultArray(query);
            if (resultArray.next()) {
                //set blend details
                this.setBlendName(resultArray.getString(0));
                this.setVisibleStock(Integer.parseInt(resultArray.getString(1)));
                this.setAlocatedStock(Integer.parseInt(resultArray.getString(2)));
                this.setBlendCategory(resultArray.getString(3));

                validBlendName = true;
            }
        } catch (NumberFormatException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
        return validBlendName;
    }

    /* start of updateDeliverDetails method */
    public boolean updateDeliverDetails() {
        boolean updated = false;
        //get current user id
        User updatedUser = new User();
        updatedUser.getIDByUsername();

        //get blendID
        this.getBlendIDFromBlendName();

        try {

            //query to update blend stock
            String query = "UPDATE blend SET visibleStock = '" + this.getVisibleStock() + "' , alocatedStock = '"
                    + this.getAlocatedStock() + "' WHERE blendID = '" + this.getBlendID() + "'";
            int i = dbConn.updateResult(query);

            if (i == 1) {
                String reason = "Blend Deliver (From allocated stock) : " + this.getStockUpdateReason();

                //query to insert into stock history table about delivered amount
                query = "INSERT INTO blendstockhistory (`blendID`, `oldQty`, `updatedQty`, `reason`, `updatedBy`) "
                        + "VALUES ('" + this.getBlendID() + "','"
                        + this.getOldStockQty() + "','" + this.getUpdatedStockQTy() + "','" + reason
                        + "','" + updatedUser.getUserID() + "')";

                i = dbConn.updateResult(query);

                if (this.getSampleQty() > 0 && i == 1) {

                    int oldStock = ((this.getVisibleStock() + this.sampleQty) - this.delRemoveQty);
                    int updatedStock = this.sampleQty;
                    reason = "Deliver Sample Amount (From free stock) : " + this.getStockUpdateReason();

                    //query to insert into stock history table about sample deliver amount
                    query = "INSERT INTO blendstockhistory "
                            + "(`blendID`, `oldQty`, `updatedQty`, `reason`, `updatedBy`) "
                            + "VALUES ('" + this.getBlendID() + "','"
                            + oldStock + "','" + updatedStock + "','" + reason + "','" + updatedUser.getUserID() + "')";

                    i = dbConn.updateResult(query);

                    if (this.delRemoveQty > 0 && i == 1) {

                        oldStock = this.getVisibleStock() - this.delRemoveQty;
                        updatedStock = this.delRemoveQty;

                        reason = "Undelivered Amount (To free stock) : " + this.getStockUpdateReason();

                        //query to insert into stock history table about ubdelivered amount
                        query = "INSERT INTO blendstockhistory "
                                + "(`blendID`, `oldQty`, `updatedQty`, `reason`, `updatedBy`) "
                                + "VALUES ('" + this.getBlendID() + "','"
                                + oldStock + "','" + updatedStock + "','" + reason + "','" + updatedUser.getUserID() + "')";

                        i = dbConn.updateResult(query);

                        if (i == 1) {
                            updated = true;
                        }
                    } else if (i == 1) {
                        updated = true;
                    }
                } else if (i == 1) {
                    updated = true;
                }
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage());
        }
        return updated;
    }

    //getting recipie data for blend
    public ResultArray getRecipie(String blendName) {
        String repBlendName = blendName.replace("'", "''");
        String query = "SELECT b.baseID, r.ingID, r.ingPercent, r.type \n"
                + "FROM blend b \n"
                + "INNER JOIN recipie r on b.blendID=r.blendID \n"
                + "WHERE b.blendName='" + repBlendName + "'";
        return dbConn.getResultArray(query);
    }

    //updating blend stocks after a new order
    public boolean updateBlendStock(String[] data) {
        String query = "UPDATE blend SET visibleStock='" + data[0] + "', invisibleStock='" + data[1] + "' WHERE blendID='" + data[2] + "'";
        return (dbConn.updateResult(query) == 1);
    }

    /* start of loadBlendIngredientDetails method */
    public boolean loadBlendIngredientDetails(DefaultTableModel defaultTableModel) {
        ResultArray resultArray;
        boolean load = false;
        
        String query = "SELECT i.ingName, r.ingPercent FROM ingredient i JOIN "
                + "recipie r ON i.ingID = r.ingID WHERE r.blendID = '" + this.getBlendID() + "' AND type = '0'";

        resultArray = dbConn.getResultArray(query);

        defaultTableModel.setRowCount(0);

        while (resultArray.next()) {
            Vector newRow = new Vector();
            for (int i = 0; i <= 4; i++) {
                newRow.addElement(resultArray.getString(i));
            }
            defaultTableModel.addRow(newRow);
            load = true;
        }
        return load;
    }

    /* start of loadBlendFlavourDetails */
    public boolean loadBlendFlavourDetails(DefaultTableModel defaultTableModel) {
        ResultArray resultArray;
        boolean load = false;
        String query = "SELECT i.ingName, r.ingPercent FROM ingredient i JOIN "
                + "recipie r ON i.ingID = r.ingID WHERE r.blendID = '" + this.getBlendID() + "' AND type = '1'";

        resultArray = dbConn.getResultArray(query);
        //defaultTableModel.setRowCount(0);
        Vector mainRow = new Vector();
        defaultTableModel.addRow(mainRow);

        mainRow = new Vector();
        mainRow.addElement("<html> <b>   Flavour </b> <html>");
        mainRow.addElement("<html> <b>   Percentage (%) </b> </html>");
        defaultTableModel.addRow(mainRow);

        while (resultArray.next()) {
            Vector newRow = new Vector();
            for (int i = 0; i <= 4; i++) {
                newRow.addElement(resultArray.getString(i));
            }
            defaultTableModel.addRow(newRow);
            load = true;
        }
        return load;
    }

    /* start of getBlendCatgFromBlendName method */
    public void getBlendCatgFromBlendName() {
        ResultArray resultArray;
        this.setBlendName(this.getBlendName().replace("'", "''"));
        String query = "SELECT blendCategory FROM blend WHERE blendName = '" + this.getBlendName() + "'";
        resultArray = dbConn.getResultArray(query);
        if (resultArray.next()) {
            this.setBlendCategory(resultArray.getString(0));
        }
    }

    /* start of getBlendCatgFromBlendID method */
    public void getBlendCatgFromBlendID() {
        ResultArray resultArray;
        String query = "SELECT blendCategory FROM blend WHERE blendID = '" + this.getBlendID() + "'";
        resultArray = dbConn.getResultArray(query);
        if (resultArray.next()) {
            this.setBlendCategory(resultArray.getString(0));
        }
    }

    /* start of updateBlendStock method -- for orderRecieved when no pending */
    public boolean updateBlendStockWithoutPending() {
        String repBlendName = this.getBlendName().replace("'", "''");
        String query = "UPDATE blend SET alocatedStock = alocatedStock + '" + this.getOrderReqQty() + "'"
                + " , visibleStock = visibleStock + '" + this.getOrderExcessQty() + "' "
                + " , invisibleStock = invisibleStock - '" + this.getOrderExcessQty() + "' "
                + " WHERE blendName = '" + repBlendName + "' ";

        return (dbConn.updateResult(query) == 1);
    }

    /* start of updateBlendStock method -- for orderRecieved */
    public boolean updateBlendStockWithPending() {
        String repBlendName = this.getBlendName().replace("'", "''");
        String query = "UPDATE blend SET alocatedStock = '" + this.getAlocatedStock() + "'"
                + " , visibleStock = '" + this.getVisibleStock() + "', invisibleStock = '" + this.getInvisibleStock() + "' "
                + " WHERE blendName = '" + repBlendName + "' ";

        return (dbConn.updateResult(query) == 1);
    }
    
    //Method for updating the recipies
    public int updateRecipie(String blendID, int ingID, double per, int type){
        String query = "INSERT INTO recipie (blendID, ingID, ingPercent, type) values('"+blendID+"', '"+ingID+"', '"+per+"', '"+type+"')";
        int rslt = dbConn.updateResult(query);
        return rslt;       
    }
    
    //Method to delete the recepie before updating
    public int deleteRecepie(String blendID){
        String query = "DELETE FROM recipie WHERE blendID = '" + blendID + "' ";
        int rslt = dbConn.updateResult(query);
        return rslt;
    }
}
