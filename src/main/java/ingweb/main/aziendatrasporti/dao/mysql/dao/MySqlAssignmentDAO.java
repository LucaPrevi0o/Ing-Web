package ingweb.main.aziendatrasporti.dao.mysql.dao;

import ingweb.main.aziendatrasporti.dao.AssignmentDAO;
import ingweb.main.aziendatrasporti.dao.mysql.MySqlDAO;
import ingweb.main.aziendatrasporti.dao.mysql.MySqlQueryManager;
import ingweb.main.aziendatrasporti.mo.mo.*;
import java.sql.Connection;
import java.util.ArrayList;

public class MySqlAssignmentDAO extends MySqlDAO<Assignment> implements AssignmentDAO {

    public MySqlAssignmentDAO(Connection connection, String tableName) {

        MySqlDAO.setConnection(connection);
        MySqlDAO.setTableName(tableName);
        MySqlDAO.setColumns(MySqlQueryManager.getColumnNames(connection, tableName));
    }

    public Assignment get(String[] item) {

        return new Assignment(Integer.parseInt(item[0]),
            new Service(Integer.parseInt(item[1]), null, null, null, null, null, false),
            new Worker(null, null, item[2], null, null),
            new Worker(null, null, item[3], null, null),
            new Truck(item[4], null, null, false), item[5], item[6].equals("1"), item[7].equals("1")
        );
    }

    public ArrayList<Assignment> findAllCompleted() { return selectAll(new int[]{6}, new Object[]{"1"}); }
    public Assignment findByCode(int code) { return select(new int[]{0}, new Object[]{code}); }
    public Assignment findByService(Service service) { return select(new int[]{1, 6}, new Object[]{service.getCode(), "1"}); }
    public int findLastCode() { return lastCode(); }
    public void addAssignment(Assignment assignment) { insert(assignment.asList()); }
    public void deleteAssignment(Assignment assignment) { delete(0, assignment.getCode()); }
    public void removeAssignment(Assignment assignment) { remove(assignment.getCode()); }
    public void updateAssignment(Assignment assignment) { update(assignment.asList()); }

    public ArrayList<Assignment> findAll() {

        var result=new ArrayList<Assignment>();
        var query="select * from "+getTableName()+" where "+getColumns()[1]+" in (select codice from servizio where cliente in (select ragione_sociale from azienda_cliente where eliminato = '0') and eliminato = '0') and "+getColumns()[2]+" in (select codice_fiscale from dipendente where eliminato = '0') and "+getColumns()[4]+" in (select targa from mezzo where eliminato = '0') and "+getColumns()[6]+" = 0 and "+getColumns()[getColumns().length-1]+" = 0";
        var res=MySqlQueryManager.getResult(getConnection(), query);
        var resList=MySqlQueryManager.asList(res, getColumns());
        for (var item: resList) result.add(get(item));
        return result;
    }

    public ArrayList<Assignment> findAllByWorker(Worker worker) {

        var result=new ArrayList<Assignment>();
        var query="select * from "+getTableName()+" where ("+getColumns()[2]+" in (select codice_fiscale from dipendente where codice_fiscale = '"+worker.getFiscalCode()+"') or "+getColumns()[3]+" = (select codice_fiscale from dipendente where codice_fiscale = '"+worker.getFiscalCode()+"')) and "+getColumns()[6]+" = 0 and "+getColumns()[getColumns().length-1]+" = 0";
        var res=MySqlQueryManager.getResult(getConnection(), query);
        var resList=MySqlQueryManager.asList(res, getColumns());
        for (var item: resList) result.add(get(item));
        return result;
    }

    public ArrayList<Assignment> findAllByClientCompany(ClientCompany clientCompany) {

        var result=new ArrayList<Assignment>();
        var query="select "+getTableName()+".* from "+getTableName()+" join "+getColumns()[1]+" on "+getTableName()+"."+getColumns()[1]+" = "+getColumns()[1]+".codice where "+getColumns()[1]+".cliente = '"+clientCompany.getSocialReason()+"' and "+getColumns()[6]+" = 0";
        var res=MySqlQueryManager.getResult(getConnection(), query);
        var resList=MySqlQueryManager.asList(res, getColumns());
        for (var item: resList) result.add(get(item));
        return result;
    }
}