package ingweb.main.aziendatrasporti.dao;

import ingweb.main.aziendatrasporti.mo.ClientCompany;
import ingweb.main.aziendatrasporti.mo.Service;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

public interface ServiceDAO {

    public ArrayList<Service> findAll();
    public Service findByCode(int code);
    public Service findByDateStartTimeAndDuration(Date date, Time startTime, Time duration);
    public ArrayList<Service> findByClientCompany(ClientCompany clientCompany);
    public ArrayList<Service> findByDate(Date date);
    public void addService(Service service);
    public void removeService(Service service);
    public void updateService(Service service);
}
