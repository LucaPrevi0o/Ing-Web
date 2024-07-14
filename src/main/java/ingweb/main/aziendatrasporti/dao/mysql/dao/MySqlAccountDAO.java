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

    public Account get(String[] item) { return new Account(Integer.parseInt(item[0]), item[1], item[2], item[3], Integer.parseInt(item[4]), item[5].equals("1")); }

    public ArrayList<Account> findAll() { return selectAll(); }
    public ArrayList<Account> findAllByLevel(int level) { return selectAll(new int[]{4}, new Object[]{level}); }
    public Account findByUsernameAndPassword(String username, String password) { return select(new int[]{1, 2}, new Object[]{username, password}); }
    public Account findByUsername(String username) { return select(new int[]{1}, new Object[]{username}); }
    public Account findByCode(int code) { return select(new int[]{0}, new Object[]{code}); }
    public void addAccount(Account account) { insert(account.asList()); }
    public void updateAccount(Account account) { update(account.asList()); }
    public void removeAccount(Account account) { remove(account.getCode()); }

    public void createAccount(Account account) {}
    public void deleteAccount(Account account) {}
    public Account findLoggedAccount() { return null; }
}
