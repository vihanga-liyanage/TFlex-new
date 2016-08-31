package classes;

import static classes.DBConnection.logger;
import java.sql.SQLException;
import java.util.logging.Level;

public class Supplier {

    private int supplierID;
    private String SupplierName;

    DBConnection dbConn = DBConnection.getInstance();

    public Supplier() {
        this.supplierID = 0;
        this.SupplierName = null;
    }

    public int getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(int supplierID) {
        this.supplierID = supplierID;
    }

    public String getSupplierName() {
        return SupplierName;
    }

    public void setSupplierName(String SupplierName) {
        this.SupplierName = SupplierName;
    }

    //start of load suppliers for combobox
    public ResultArray loadSuppliersForCombobox() throws SQLException {
        ResultArray resultArray = new ResultArray();

        try {
            String query = "SELECT supName FROM supplier";

            resultArray = dbConn.getResultArray(query);
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage());
        }
        return resultArray;

    }


    public int addNewSupplier(String Name) throws SQLException {
        int insertOk = 0;
        this.setSupplierName(Name.replace("'", "''"));
        String query = "INSERT INTO supplier(supName) values('" + this.getSupplierName() + "')";
        insertOk = dbConn.updateResult(query);
        return insertOk;
    }


       
    //start of get suplier id by name

    public int getSupplierIDByName(String supplierName) throws SQLException {

        int supplierID = 0;

        ResultArray resultArray;

        this.setSupplierName(supplierName.replace("'", "''"));

        //set name of the ingredient
        String query = "SELECT supID from supplier WHERE supplier.supName= '" + this.getSupplierName() + "' ";
        try {
            resultArray = dbConn.getResultArray(query);

            if (resultArray.next()) {
                supplierID = Integer.parseInt(resultArray.getString(0));
            }

        } catch (NumberFormatException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
        return supplierID;
    }
    //end of get suplier id by name

}
