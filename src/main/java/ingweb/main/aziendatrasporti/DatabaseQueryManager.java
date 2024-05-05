package ingweb.main.aziendatrasporti;
import java.sql.*;
import java.util.*;

public class DatabaseQueryManager {

    private static String[] params; //parameter list

    public static void setParams(String[] p) { params=p; }
    public static String[] getParams() { return params; }

    public static ArrayList<String[]> asArrayList(ResultSet res) throws SQLException {

        var result=new ArrayList<String[]>(); //empty list
        while (res.next()) { //check every element in result list

            var newArr=new ArrayList<String>(); //create new empty array
            for (var c: params) newArr.add(res.getString(c)); //push in array every column element
            result.add(newArr.toArray(new String[params.length])); //add new element to result list
        }

        return result; //return updated list
    }

    public static String getURL(String username, String password, String dbName) {

        var serverTimezone= Calendar.getInstance().getTimeZone().getID(); //server timezone
        var params="allowPublicKeyRetrieval=true&useSSL=false&serverTimezone="+serverTimezone; //other parameters
        var uri="jdbc:mysql://localhost:3306/"+dbName; //URI containing remote location of DB
        return uri+"?user="+username+"&password="+password+"&"+params; //return complete URL for database call
    }

    public static PreparedStatement getStatement(String sql, String url) throws SQLException {

        //prepare SQL query
        var connection=DriverManager.getConnection(url); //connect to DB with specified username and password
        connection.setAutoCommit(false); //disable auto commit for SQL queries
        return connection.prepareStatement(sql); //create new valid SQL query
    }
}
