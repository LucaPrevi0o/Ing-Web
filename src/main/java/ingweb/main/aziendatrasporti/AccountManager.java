package ingweb.main.aziendatrasporti;
import java.util.*;

public class AccountManager {
    
    public static ArrayList<String[]> getAccountList(boolean isAdmin) {

        try { //get valid accounts or admin accounts

            Class.forName("com.mysql.cj.jdbc.Driver"); //append dependency to DB driver

            var user="LoginManager"; //set user for DB access
            var pass="L4mm5hkX"; //set password for DB access
            var db="AziendaTrasportiDB"; //select DB to access
            var url=DatabaseQueryManager.getURL(user, pass, db); //get URL for database call

            var query="SELECT * FROM Accounts"+(isAdmin ? " WHERE admin=1" : "")+";"; //SQL query
            var statement=DatabaseQueryManager.getStatement(query, url); //get SQL statement to execute

            var res=statement.executeQuery(); //get results for query
            DatabaseQueryManager.setParams(new String[]{"usr", "pwd"});
            return DatabaseQueryManager.asArrayList(res); //return list of every account
        } catch (Exception ex) { //catch every possible exception

            ex.printStackTrace(); //temporary
            return null; //null return for error handling
        }
    }
}
