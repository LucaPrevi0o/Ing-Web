package ingweb.main.aziendatrasporti.dao.mysql;

import ingweb.main.aziendatrasporti.mo.ClientCompany;
import ingweb.main.aziendatrasporti.mo.Truck;
import ingweb.main.aziendatrasporti.mo.Worker;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class MySqlQueryManager {

    //return the query result as a list of String[] instead of a standard ResultSet
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

            e.printStackTrace();
            return new ArrayList<>();
        }
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
            c.setAutoCommit(false); //set manual commit
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

    //execute new update query in the DB (does not return)
    public static void execute(Connection connection, String sql, Object[] params) {

        try { //create statement and return its execution result

            var statement=connection.prepareStatement(sql); //new empty statement
            for (var index=0; index<params.length; index++)
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
            statement.executeUpdate(); //execute statement by query
        } catch (Exception e) { e.printStackTrace(); }
    }
}
