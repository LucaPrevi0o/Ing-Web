package ingweb.main.aziendatrasporti.dao.mysql.dao;
import ingweb.main.aziendatrasporti.dao.AssignmentDAO;
import ingweb.main.aziendatrasporti.dao.mysql.MySqlQueryManager;
import ingweb.main.aziendatrasporti.mo.Assignment;
import ingweb.main.aziendatrasporti.mo.Service;
import ingweb.main.aziendatrasporti.mo.Truck;
import ingweb.main.aziendatrasporti.mo.Worker;

import java.sql.Connection;
import java.util.ArrayList;

public class MySqlAssignmentDAO implements AssignmentDAO {

    private final Connection connection;
    private final String[] allColumns={"servizio", "primo_autista", "secondo_autista", "mezzo", "deleted"};

    public Assignment getAssignment(String[] item) {

        return new Assignment(
            new Service(Integer.parseInt(item[0]), null, null, null, null, null, false),
            new Worker(null, null, item[1], null, null, false),
            new Worker(null, null, item[2], null, null, false),
            new Truck(item[3], null, null, false, false),false
        );
    }

    public MySqlAssignmentDAO(Connection connection) { this.connection=connection; }

    public ArrayList<Assignment> findAll() {

        var result=new ArrayList<Assignment>();
        var query="select * from assegnamento";
        var res= MySqlQueryManager.getResult(connection, query);
        var resList=MySqlQueryManager.asList(res, allColumns);
        for (var item: resList) {

            var assignment=getAssignment(item);
            if (!assignment.isDeleted()) result.add(assignment);
        }

        return result;
    }
}
