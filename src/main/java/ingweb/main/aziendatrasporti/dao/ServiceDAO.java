package ingweb.main.aziendatrasporti.dao;

import ingweb.main.aziendatrasporti.mo.ClientCompany;
import ingweb.main.aziendatrasporti.mo.Service;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

public interface ServiceDAO {

    public ArrayList<Service> findAllData();
    public Service findDataByCode(int code);
    public Service findDataByDateStartTimeAndDuration(Date date, Time startTime, Time duration);
    public void addService(Service service);
    public void removeService(Service service);
    public void updateService(Service service);
}
