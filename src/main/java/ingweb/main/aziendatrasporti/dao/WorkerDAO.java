package ingweb.main.aziendatrasporti.dao;

import ingweb.main.aziendatrasporti.mo.mo.Service;
import ingweb.main.aziendatrasporti.mo.mo.Worker;
import java.util.*;

public interface WorkerDAO {

    public ArrayList<Worker> findAll();
    public ArrayList<Worker> findAvailableByService(Service service);
    public Worker findByCode(int code);
    public int findLastCode();
    public void addWorker(Worker worker);
    public void removeWorker(Worker worker);
    public void updateWorker(Worker worker);
}
