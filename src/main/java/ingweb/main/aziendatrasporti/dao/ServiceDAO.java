package ingweb.main.aziendatrasporti.dao;

import ingweb.main.aziendatrasporti.mo.mo.Service;
import java.util.ArrayList;

public interface ServiceDAO {

    public ArrayList<Service> findAllNotAssigned();
    public ArrayList<Service> findAllRequested();
    public Service findByCode(int code);
    public int findLastCode();
    public void addService(Service service);
    public void removeService(Service service);
    public void updateService(Service service);
}
