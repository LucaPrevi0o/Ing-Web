package ingweb.main.aziendatrasporti.dao;

import ingweb.main.aziendatrasporti.mo.mo.Account;
import java.util.ArrayList;

//interface allows the DAO to be independent of the database-specific method implementation of every functionality
public interface AccountDAO {

    public ArrayList<Account> findAll();
    public ArrayList<Account> findAllByLevel(int level);
    public void addAccount(Account account);
    public void removeAccount(Account account);
    public void updateAccount(Account account);
    public Account findByUsernameAndPassword(String username, String password);
    public Account findLoggedAccount();
    public Account findByUsername(String username);
    public Account findByCode(int code);

    public void createAccount(Account account);
    public void deleteAccount(Account account);
}
