package ingweb.main.aziendatrasporti.dao;

import ingweb.main.aziendatrasporti.mo.License;
import ingweb.main.aziendatrasporti.mo.Service;
import ingweb.main.aziendatrasporti.mo.Truck;
import ingweb.main.aziendatrasporti.mo.Worker;
import java.util.ArrayList;

public interface LicenseDAO {

    public ArrayList<License> findAll();
    public ArrayList<License> findAllByService(Service service);
    public ArrayList<License> findAllByTruck(String numberPlate);
    public ArrayList<License> findAllByWorker(Worker worker);
    public void addLicensesByWorker(Worker worker, ArrayList<License> license);
    public void updateLicensesByWorker(Worker worker, ArrayList<License> license);
    public void addLicensesByTruck(Truck truck, ArrayList<License> license);
    public void updateLicensesByTruck(Truck truck, ArrayList<License> license);
    public void addLicensesByService(Service service, ArrayList<License> license);
    public void updateLicensesByService(Service service, ArrayList<License> license);
}
