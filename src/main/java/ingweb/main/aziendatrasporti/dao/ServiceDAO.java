package ingweb.main.aziendatrasporti.dao;

import ingweb.main.aziendatrasporti.mo.Service;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

public interface ServiceDAO {

    public ArrayList<Service> findAllNotAssigned();
    public ArrayList<Service> findAllAssigned();
    public ArrayList<Service> findAllAssignedByFiscalCode(String fiscalCode);
    public Service findByCode(int code);
    public Service findAssignedByCode(int code);
    public Service findByDateStartTimeAndDuration(Date date, Time startTime, Time duration);
    public void addService(Service service);
    public void removeService(Service service);
    public void updateService(Service service);
    public void assignService(Service service);
    public void updateAssignment(Service service);
}
