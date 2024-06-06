package ingweb.main.aziendatrasporti.dao;

import ingweb.main.aziendatrasporti.mo.Worker;
import java.util.*;

//interface allows the DAO to be independent of the database-specific method implementation of every functionality
public interface WorkerDAO {

    public ArrayList<Worker> findAll();
    public Worker findByCode(int code);
    public Worker findByFiscalCode(String fiscalCode);
    public void addWorker(Worker worker);
    public void removeWorker(Worker worker);
    public void updateWorker(Worker worker);
}
