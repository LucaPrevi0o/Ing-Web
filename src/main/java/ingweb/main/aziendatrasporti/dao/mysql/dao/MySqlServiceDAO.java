package ingweb.main.aziendatrasporti.dao.mysql.dao;

import ingweb.main.aziendatrasporti.dao.ServiceDAO;
import ingweb.main.aziendatrasporti.dao.mysql.MySqlDAO;
import ingweb.main.aziendatrasporti.dao.mysql.MySqlQueryManager;
import ingweb.main.aziendatrasporti.mo.mo.ClientCompany;
import ingweb.main.aziendatrasporti.mo.mo.Service;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

public class MySqlServiceDAO extends MySqlDAO<Service> implements ServiceDAO {

    public MySqlServiceDAO(Connection connection, String tableName) {

        MySqlDAO.setConnection(connection);
        MySqlDAO.setTableName(tableName);
        MySqlDAO.setColumns(MySqlQueryManager.getColumnNames(connection, tableName));
    }

    public Service get(String[] item) {

        return new Service(Integer.parseInt(item[0]), item[1],
            new ClientCompany(null, item[2], null, null, null, null, null),
            (item[3]==null ? null : Date.valueOf(item[3])), (item[4]==null ? null : Time.valueOf(item[4])), (item[5]==null ? null : Time.valueOf(item[5])), item[6].equals("1"));
    }

    public ArrayList<Service> findAllNotAssigned() {

        var query="select * from "+getTableName()+" where "+getColumns()[0]+" not in (select servizio from assegnamento join "+getTableName()+" on "+getTableName()+"."+getColumns()[0]+" = assegnamento.servizio) and "+getColumns()[4]+" and "+getColumns()[2]+" in (select ragione_sociale from azienda_cliente where "+getColumns()[getColumns().length-1]+" = '0')";
        return findList(query);
    }

    public ArrayList<Service> findAllRequested() {

        var query="select * from "+getTableName()+" where "+getColumns()[3]+" is null and "+getColumns()[4]+" is null and "+getColumns()[2]+" in (select ragione_sociale from azienda_cliente where eliminato = '0')";
        return findList(query);
    }

    public Service findByCode(int code) { return select(new int[]{0}, new Object[]{code}); }
    public int findLastCode() { return lastCode(); }
    public void addService(Service service) { insert(service.asList()); }
    public void removeService(Service service) { remove(service.getCode()); }
    public void deleteService(Service service) { delete(0, service.getCode()); }
    public void updateService(Service service) { update(service.asList()); }
}
