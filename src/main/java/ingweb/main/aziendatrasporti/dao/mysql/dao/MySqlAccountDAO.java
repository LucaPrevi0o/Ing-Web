package ingweb.main.aziendatrasporti.dao.mysql.dao;

import ingweb.main.aziendatrasporti.dao.AccountDAO;
import ingweb.main.aziendatrasporti.dao.mysql.MySqlDAO;
import ingweb.main.aziendatrasporti.dao.mysql.MySqlQueryManager;
import ingweb.main.aziendatrasporti.mo.mo.Account;
import java.sql.*;
import java.util.*;

public class MySqlAccountDAO extends MySqlDAO<Account> implements AccountDAO {

    public MySqlAccountDAO(Connection connection, String tableName) {

        MySqlDAO.setConnection(connection);
        MySqlDAO.setTableName(tableName);
        MySqlDAO.setColumns(MySqlQueryManager.getColumnNames(connection, tableName));
    }

    public Account get(String[] item) { return new Account(item[0], item[1], item[2], Integer.parseInt(item[3]), item[4].equals("1")); }

    public ArrayList<Account> findAll() { return selectAll(); }
    public ArrayList<Account> findAllByLevel(int level) { return selectAll(3, level); }
    public Account findByUsernameAndPassword(String username, String password) { return select(new int[]{0, 1}, new Object[]{username, password}); }
    public Account findByUsername(String username) { return select(0, username); }
    public void addAccount(Account account) { insert(account.asList()); }

    public Account findLoggedAccount() { return null; }

    //remove account from database (setting the logic deletion true)
    public void removeAccount(Account account) {

        var query="update accounts set deleted=1 where (usr = ?)"; //empty query
        MySqlQueryManager.execute(getConnection(), query, new String[]{account.getUsername()}); //execute update with parameters
    }

    public void updateAccount(Account account) {

        var query="update accounts set usr=?, pwd=?, fullname=?, admin=?, deleted=? where usr='"+account.getUsername()+"'";
        MySqlQueryManager.execute(getConnection(), query, account.asList());
    }

    public void createAccount(Account account) {}
    public void deleteAccount(Account account) {}
}
