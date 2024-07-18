package ingweb.main.aziendatrasporti.dao.mysql.dao;

import ingweb.main.aziendatrasporti.dao.ClientDAO;
import ingweb.main.aziendatrasporti.dao.mysql.MySqlDAO;
import ingweb.main.aziendatrasporti.dao.mysql.MySqlQueryManager;
import ingweb.main.aziendatrasporti.mo.mo.ClientCompany;

import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;

public class MySqlClientDAO extends MySqlDAO<ClientCompany> implements ClientDAO {

    public MySqlClientDAO(Connection connection, String tableName) {

        MySqlDAO.setConnection(connection);
        MySqlDAO.setTableName(tableName);
        MySqlDAO.setColumns(MySqlQueryManager.getColumnNames(connection, tableName));
    }

    public ClientCompany get(String[] item) {

        return new ClientCompany(Integer.parseInt(item[0]), item[1], item[2], item[3], item[4], item[5],
        (item[6]==null ? null : Date.valueOf(item[6])), item[7], item[8].equals("1"));
    }

    public ArrayList<ClientCompany> findAll() { return selectAll(); }
    public ClientCompany findByCode(int code) { return select(new int[]{0}, new Object[]{code}); }
    public ClientCompany findBySocialReason(String socialReason) { return select(new int[]{2}, new Object[]{socialReason}); }
    public int findLastCode() { return lastCode(); }
    public void addClient(ClientCompany service) { insert(service.asList()); }
    public void removeClient(ClientCompany service) { remove(service.getCode()); }
    public void updateClient(ClientCompany service) { update(service.asList()); }
}
