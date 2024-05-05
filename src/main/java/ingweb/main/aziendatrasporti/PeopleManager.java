package ingweb.main.aziendatrasporti;

import java.sql.DriverManager;
import java.util.ArrayList;

public class PeopleManager {

    private static String[] params={"Nome", "Cognome", "CodiceFiscale", "DataNascita", "NumeroTelefono"};

    public static ArrayList<String[]> getPeopleList() {

        try { //get valid accounts or admin accounts

            Class.forName("com.mysql.cj.jdbc.Driver"); //append dependency to DB driver

            var user="LoginManager"; //set user for DB access
            var pass="L4mm5hkX"; //set password for DB access
            var db="AZIENDA-TRASPORTI"; //select DB to access
            var url=DatabaseQueryManager.getURL(user, pass, db); //get URL for database call

            var query="SELECT * FROM Dipendente;"; //SQL query
            var statement=DatabaseQueryManager.getStatement(query, url); //get SQL statement to execute

            var res=statement.executeQuery(); //get results for query
            DatabaseQueryManager.setParams(params);
            return DatabaseQueryManager.asArrayList(res); //return list of every account
        } catch (Exception ex) { //catch every possible exception

            ex.printStackTrace(); //temporary
            return null; //null return for error handling
        }
    }

    public static void addPerson(String[] personData) {

        try { //get valid accounts or admin accounts

            Class.forName("com.mysql.cj.jdbc.Driver"); //append dependency to DB driver

            var user="LoginManager"; //set user for DB access
            var pass="L4mm5hkX"; //set password for DB access
            var db="AZIENDA-TRASPORTI"; //select DB to access
            var url=DatabaseQueryManager.getURL(user, pass, db); //get URL for database call

            var query="INSERT INTO DIPENDENTE VALUES ("; //SQL query
            for (var data=0; data<personData.length-1; data++) query+=("'"+personData[data]+"', ");
            query+=personData[personData.length-1]+");";
            System.out.println(query);
            var c=DriverManager.getConnection(url);
            var statement=c.createStatement(); //get SQL statement to execute
            statement.executeUpdate(query); //get results for query
        } catch (Exception ex) { //catch every possible exception

            ex.printStackTrace(); //temporary
        }
    }
}
