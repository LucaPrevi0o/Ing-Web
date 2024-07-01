package ingweb.main.aziendatrasporti.dao.mysql;

import ingweb.main.aziendatrasporti.mo.ClientCompany;
import ingweb.main.aziendatrasporti.mo.Truck;
import ingweb.main.aziendatrasporti.mo.Worker;
import java.sql.*;
import java.sql.Date;
import java.util.*;

//this class contains a list of custom methods for easier query construction and data access from the result set
//of each execution, passing through the parameter list of the data and allowing direct access to its object
public class MySqlQueryManager {

    //return the query result as a list of String[] instead of a standard ResultSet, using a String[]
    //list of column names to extract the data from the set
    public static ArrayList<String[]> asList(ResultSet res, String[] params) {

        try { //fill list with every element from the result set

            var result=new ArrayList<String[]>(); //empty list
            while (res.next()) { //check every element in result list

                var newArr=new ArrayList<String>(); //create new empty array
                for (var c: params) newArr.add(res.getString(c)); //push in array every column element
                result.add(newArr.toArray(new String[params.length])); //add new element to result list
            }
            return result; //return updated list
        } catch (Exception e) {

            e.printStackTrace(); //print out error message
            return new ArrayList<>(); //return empty list
        }
    }

    //return the list of column names for the specific data access object, related to the implemented database
    public static String[] getColumnNames(Connection connection, String table) {

        var result=new ArrayList<String>(); //result set to store the column names
        var query="show columns from "+table; //query on the default schema to extract the database column names
        var res=getResult(connection, query); //execute query
        var resList=asList(res, new String[]{"Field"}); //get result as an ArrayList
        for (var item: resList) result.add(item[0]); //every item should have only one item because only the first column is used
        return result.toArray(new String[0]); //return the result as an array of String
    }

    //this method allows the dynamic construction of the database URL location by passing the usr/pwd parameters
    //and a database name (useful because the project is based around different schemas for data storage)
    public static String getURL(String username, String password, String dbName) {

        var serverTimezone=Calendar.getInstance().getTimeZone().getID(); //server timezone
        var params="allowPublicKeyRetrieval=true&useSSL=false&serverTimezone="+serverTimezone; //other parameters
        var uri="jdbc:mysql://localhost:3306/"+dbName; //URI containing remote location of DB
        return uri+"?user="+username+"&password="+password+"&"+params; //return complete URL
    }

    //definition of a new MySQL database connection based on the generated URL
    public static Connection getConnection(String url) {

        try { //construct and return new database connection

            Class.forName("com.mysql.cj.jdbc.Driver"); //append Driver handler dependency
            var c=DriverManager.getConnection(url); //set up a new MySQL connection
            c.setAutoCommit(false); //set manual commit for transaction management
            return c; //return new connection
        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }

    //execute new query and get the result set
    public static ResultSet getResult(Connection c, String sql) {

        try { //create statement and return its execution result

            var statement=c.createStatement(); //new empty statement
            return statement.executeQuery(sql); //execute statement by query
        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }

    //execute new update query in the database receiving data from the parameter list of the updated object
    public static void execute(Connection connection, String sql, Object[] params) {

        try { //create statement and return its execution result

            var statement=connection.prepareStatement(sql); //new empty statement
            for (var index=0; index<params.length; index++) //loop over every parameter in the list

                //depending on the object type in the parameter list, set the SQL-specific parameter in the result set;
                //this allows for dynamic generation of the result set depending only on the type of each attribute
                //(the only thing to note is the ordering of the SQL parameters, that starts with an index of 1
                //instead of the Java zero-index standard)
                if (params[index] instanceof String) statement.setString(index+1, (String)params[index]);
                else if (params[index] instanceof Integer) statement.setInt(index+1, (Integer)params[index]);
                else if (params[index] instanceof Double) statement.setDouble(index+1, (Double)params[index]);
                else if (params[index] instanceof Boolean) statement.setBoolean(index+1, (Boolean)params[index]);
                else if (params[index] instanceof Date) statement.setDate(index+1, (Date)params[index]);
                else if (params[index] instanceof Time) statement.setTime(index+1, (Time)params[index]);
                else if (params[index] instanceof ClientCompany) statement.setString(index+1, ((ClientCompany)params[index]).getSocialReason());
                else if (params[index] instanceof Truck) statement.setString(index+1, ((Truck)params[index]).getNumberPlate());
                else if (params[index] instanceof Worker) statement.setString(index+1, ((Worker)params[index]).getFiscalCode());
                else if (params[index]==null) statement.setNull(index+1, Types.VARCHAR);
            statement.executeUpdate(); //execute statement in the query with the parameters set to the corresponding values
        } catch (Exception e) { e.printStackTrace(); }
    }
}
