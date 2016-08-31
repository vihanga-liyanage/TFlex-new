package classes;

import static classes.DBConnection.logger;
import java.util.logging.Level;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;
import teaeli.EditProfile;
import static teaeli.TeaELI.loginFrame;


public class User {

    private String userName, Password, firstName, lastName, designation;
    private int userID;

    DBConnection dbConn = DBConnection.getInstance();

    public User() {
        this.userName = "";
        this.Password = "";
        this.firstName = "";
        this.lastName = "";
        this.designation = "";
        getIDByUsername();
        
        //System.out.println("User onject created, userId:" + userID + " username:" + loginFrame.user);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    //Method to add the result set of the user table in the DB to the user table in the Admin Pannel
    public void viewUser(DefaultTableModel tModel) {
        ResultArray resultSet;
        String query = "SELECT userID, username, firstname, lastname FROM user;";
        resultSet = dbConn.getResultArray(query);
        tModel.setRowCount(0);

        while (resultSet.next()) {
            tModel.addRow(new Object[]{resultSet.getString(0), resultSet.getString(1), resultSet.getString(2), resultSet.getString(3)});
        }
    }

    //method to remove user from the user table in the admin pannel(completely remove user from the system)
    public int removeUser(int id) {
        String query = "DELETE FROM user WHERE userID = '" + id + "'";
        int rslt = dbConn.updateResult(query);
        return rslt;
    }

    //Method to check whether the username is already in the database when adding a new user
    public int checkUserName(String userName) {
        ResultArray resultSet;
        String query = "SELECT username FROM user";
        resultSet = dbConn.getResultArray(query);

        while (resultSet.next()) {
            if (userName.equals(resultSet.getString(0))) {
                return 0;//the provided username already exists in the db
            }
        }
        return 1;//the provided username does not exist in the db
    }

    public int addNewUser(User user) {
        String query = "INSERT INTO user (username,firstname,lastname,password,designation)values(  '" + user.getUserName() + "','" + user.getFirstName() + "','" + user.getLastName() + "', '" + user.getPassword() + "', '" + user.getDesignation() + "')";
        int rslt = dbConn.updateResult(query);
        return rslt;
    }

    /* satrt of getIDByUsername method */
    public void getIDByUsername() {
        ResultArray resultArray;
        try {
            String query = "SELECT userID FROM user WHERE username = '" + loginFrame.user + "'";
            resultArray = dbConn.getResultArray(query);
            if (resultArray.next()) {
                this.setUserID(Integer.parseInt(resultArray.getString(0)));
            }
        } catch (NumberFormatException e) {
            logger.log(Level.WARNING, e.getMessage());
        } 
    }
    
    public int updateUserName(String firstname, String lastname, String username) {
        String query = "UPDATE user SET firstname =' " + firstname + "', lastname = '" + lastname + "' WHERE username = '" + username + "'";
        int rst = dbConn.updateResult(query);
        return rst;
    }
    
    public  int updatePassword(String firstname, String lastname, String username, String newpassword, String currentpassword){
        String query = "UPDATE user SET firstname =' "+firstname+"', lastname = '"+lastname+"',password = '"+newpassword+"' WHERE username = '"+username+"' and password = '"+currentpassword+"'";
        int rst = dbConn.updateResult(query);
        return rst;
    }
    
    public void getUserDetails(){
        
        EditProfile editProfile = new EditProfile();
        editProfile.setVisible(true);
        editProfile.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        String userName = loginFrame.user;

        String query = "SELECT username,firstname,lastname FROM user where username = ('" + userName + "')";
        ResultArray rs = dbConn.getResultArray(query);

        while (rs.next()) {
            setUserName(rs.getString(0));
            setFirstName(rs.getString(1).trim());
            setLastName(rs.getString(2));
        }

        editProfile.setVisible(true);
        editProfile.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        editProfile.lblUserName.setText(getUserName());
        editProfile.txtFirstName.setText(getFirstName());
        editProfile.txtLastName.setText(getLastName());
    }
    
    public String getUserFirstName(String username){
        String query = "SELECT firstname FROM user WHERE username='" + username + "'";
        ResultArray rs = dbConn.getResultArray(query);
        String name = "";
        while (rs.next()){
            name = rs.getString(0);
        }
        return name;
    }
}
