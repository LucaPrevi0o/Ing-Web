package ingweb.main.aziendatrasporti.dao.mysql.dao;

import ingweb.main.aziendatrasporti.dao.WorkerDAO;
import ingweb.main.aziendatrasporti.dao.mysql.MySqlDAO;
import ingweb.main.aziendatrasporti.dao.mysql.MySqlQueryManager;
import ingweb.main.aziendatrasporti.mo.mo.Service;
import ingweb.main.aziendatrasporti.mo.mo.Worker;
import java.sql.*;
import java.sql.Date;
import java.util.*;

public class MySqlWorkerDAO extends MySqlDAO<Worker> implements WorkerDAO {

    public MySqlWorkerDAO(Connection connection, String tableName) {

        MySqlDAO.setConnection(connection);
        MySqlDAO.setTableName(tableName);
        MySqlDAO.setColumns(MySqlQueryManager.getColumnNames(connection, tableName));
    }

    public Worker get(String[] item) { return new Worker(Integer.parseInt(item[0]), item[1], item[2], item[3], Date.valueOf(item[4]), item[5], item[6].equals("1")); }

    public ArrayList<Worker> findAll() { return selectAll(); }
    public Worker findByCode(int code) { return select(new int[]{0}, new Object[]{code}); }
    public Worker findByFiscalCode(String fiscalCode) { return select(new int[]{3}, new Object[]{fiscalCode}); }
    public void addWorker(Worker worker) { insert(worker.asList()); }
    public int findLastCode() { return lastCode(); }
    public void removeWorker(Worker worker) { remove(worker.getCode()); }
    public void updateWorker(Worker worker) { update(worker.asList()); }

    public ArrayList<Worker> findAvailableByService(Service service) {

        var result=new ArrayList<Worker>();
        var query=
                "SELECT m.* " +
                "FROM dipendente m " +
                "JOIN patenti_autista pm ON m.codice_fiscale = pm.dipendente " +
                "WHERE NOT EXISTS (" +
                "    SELECT 1 " +
                "    FROM assegnamento a " +
                "    JOIN servizio s ON s.codice = a.servizio" +
                "    WHERE s.data = (" +
                "       select s2.data from servizio s2" +
                "       where s2.codice = '"+service.getCode()+"'" +
                "    )" +
                "    AND ("+
                "       s.ora_inizio > (" +
                "          select s2.ora_inizio from servizio s2" +
                "          where s2.codice = '"+service.getCode()+"'" +
                "       ) AND s.ora_inizio < ADDTIME("+
                "          (" +
                "              select s2.ora_inizio from servizio s2" +
                "              where s2.codice = '"+service.getCode()+"'" +
                "          ), (" +
                "              select s2.durata from servizio s2" +
                "              where s2.codice = '"+service.getCode()+"'" +
                "          )"+
                "       ) OR s.ora_inizio < (" +
                "          select s2.ora_inizio from servizio s2" +
                "          where s2.codice = '"+service.getCode()+"'" +
                "       ) AND ("+
                "          select s2.ora_inizio from servizio s2" +
                "          where s2.codice = '"+service.getCode()+"'" +
                "       ) < ADDTIME(s.ora_inizio, s.durata)"+
                "    )"+
                "    AND (a.primo_autista = m.codice_fiscale" +
                "    OR a.secondo_autista = m.codice_fiscale)" +
                ")" +
                "GROUP BY m.codice " +
                "HAVING NOT EXISTS ( " +
                "    SELECT ps.patente  " +
                "    FROM patenti_servizio ps  " +
                "    WHERE ps.servizio = '"+service.getCode()+"'" +
                "    AND ps.patente NOT IN ( " +
                "        SELECT pm2.patente  " +
                "        FROM patenti_autista pm2  " +
                "        WHERE pm2.dipendente = m.codice_fiscale " +
                "    ) " +
                ")";

        var res=MySqlQueryManager.getResult(getConnection(), query);
        var resList=MySqlQueryManager.asList(res, getColumns());
        for (var item: resList) {

            var truck=get(item);
            if (!truck.isDeleted()) result.add(truck);
        }

        return result;
    }
}
