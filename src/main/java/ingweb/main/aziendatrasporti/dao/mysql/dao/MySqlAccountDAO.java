package ingweb.main.aziendatrasporti.dao.mysql.dao;

import ingweb.main.aziendatrasporti.dao.AccountDAO;
import ingweb.main.aziendatrasporti.dao.mysql.MySqlQueryManager;
import ingweb.main.aziendatrasporti.mo.Account;
import java.sql.*;
import java.util.*;

public class MySqlAccountDAO implements AccountDAO {

    private final Connection connection; //reference to MySQL database connection
    private final String[] allColumns={"usr", "pwd", "fullname", "admin", "deleted"};

    private String parseParams() {

        var s="";
        for (var i=0; i<allColumns.length-1; i++) s+=allColumns[i]+", ";
        return s+allColumns[allColumns.length-1];
    }

    //constructor (gets the reference to the open DB connection)
    public MySqlAccountDAO(Connection connection) { this.connection=connection; }

    //return a list of every account found on the database, specifying the account admin level
    public ArrayList<Account> findAll(boolean admin) {

        var accounts=new ArrayList<Account>(); //empty list
        var query="select * from accounts"; //SQL query to extract data
        if (admin) query+=" where admin=1"; //set alternative query for data filtering
        var res=MySqlQueryManager.getResult(connection, query); //execute query on the database
        var resList=MySqlQueryManager.asList(res, allColumns); //parse results
        for (var item: resList) { //add every element of the result set as new account

            var account=new Account(item[0], item[1], item[2], item[3].equals("1"), item[4].equals("1")); //new account
            if (!account.isDeleted()) accounts.add(account); //add account to the result list if not set as deleted
        }

        return accounts; //return list of valid accounts
    }

    //return, if exists, the account related to a specific (unique) username
    public Account findByUsernameAndPassword(String username, String password) {

        var query="select * from accounts where usr='"+username+"' and pwd='"+password+"'"; //SQL query to extract data
        var res=MySqlQueryManager.getResult(connection, query); //execute query on the database
        var resList=MySqlQueryManager.asList(res, allColumns); //parse results
        if (resList==null || resList.size()!=1) return null; //return null error value if list has more than 1 element
        var a=resList.get(0); //get first (and only) element in list and return its value
        return new Account(a[0], a[1], a[2], a[3].equals("1"), a[4].equals("1"));
    }

    //return, if exists, the account related to a specific (unique) username
    public Account findByUsername(String username) {

        var query="select * from accounts where usr='"+username+"'"; //SQL query to extract data
        var res=MySqlQueryManager.getResult(connection, query); //execute query on the database
        var resList=MySqlQueryManager.asList(res, allColumns); //parse results
        if (resList==null || resList.size()!=1) return null; //return null error value if list has more than 1 element
        var a=resList.get(0); //get first (and only) element in list and return its value
        return new Account(a[0], a[1], a[2], a[3].equals("1"), a[4].equals("1"));
    }

    public Account findLoggedAccount() { return null; }

    //insert a new account in the database passing login data
    public void addAccount(String... params) {

        if (params.length!=3) return; //only 3 parameters are needed to insert a new account
        var query="insert into accounts ("+parseParams()+") values (?, ?, ?, 0)"; //empty query
        MySqlQueryManager.execute(connection, query, params); //execute insertion with parameters
    }

    //remove account from database (setting the logic deletion true)
    public void removeAccount(Account account) {

        var query="update accounts set deleted=1 where (usr = ?)"; //empty query
        var params=new String[]{account.getUsername()}; //parameter string array
        MySqlQueryManager.execute(connection, query, params); //execute update with parameters
    }

    public void createAccount(Account account) {}
    public void deleteAccount(Account account) {}
}
