package ingweb.main.aziendatrasporti.dao.mysql.dao;

import ingweb.main.aziendatrasporti.dao.ServiceDAO;
import ingweb.main.aziendatrasporti.dao.mysql.DataAccessObject;
import ingweb.main.aziendatrasporti.dao.mysql.MySqlQueryManager;
import ingweb.main.aziendatrasporti.mo.ClientCompany;
import ingweb.main.aziendatrasporti.mo.Service;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

public class MySqlServiceDAO extends DataAccessObject implements ServiceDAO {

    public MySqlServiceDAO(Connection connection, String tableName) {

        DataAccessObject.setConnection(connection);
        DataAccessObject.setTableName(tableName);
        DataAccessObject.setColumns(MySqlQueryManager.getColumnNames(connection, tableName));
    }

    public Service getService(String[] item) {

        return new Service(Integer.parseInt(item[0]), item[1],
            new ClientCompany(null, item[2], null, null, null, null, null, false),
            Date.valueOf(item[3]), Time.valueOf(item[4]), Time.valueOf(item[5]), item[6].equals("1"));
    }

    public ArrayList<Service> findAll() {

        var result=new ArrayList<Service>();
        var resList=select();
        for (var item: resList) {

            var service=getService(item);
            if (!service.isDeleted()) result.add(service);
        }

        return result;
    }

    public Service findByCode(int code) {

        var resList=select(code);
        if (resList.size()!=1) return null;
        var item=resList.get(0);
        var service=getService(item);
        return (service.isDeleted() ? null : service);
    }

    public int findLastCode() { return lastCode(); }
    public void addService(Service service) { insert(service.asList()); }
    public void removeService(Service service) { remove(service.getCode()); }
    public void updateService(Service service) { update(service.data()); }
}
