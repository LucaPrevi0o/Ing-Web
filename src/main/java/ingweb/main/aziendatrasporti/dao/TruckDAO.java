package ingweb.main.aziendatrasporti.dao;

import ingweb.main.aziendatrasporti.mo.License;
import ingweb.main.aziendatrasporti.mo.Service;
import ingweb.main.aziendatrasporti.mo.Truck;
import java.util.ArrayList;

public interface TruckDAO {

    public ArrayList<Truck> findAll();
    public ArrayList<Truck> findAllAssigned();
    public Truck findByCode(int code);
    public Truck findByNumberPlate(String numberPlate);
    public ArrayList<Truck> findAllByLicenses(ArrayList<License> licenses);
    public ArrayList<Truck> findAllAvailableByService(Service service);
    public void addTruck(Truck truck);
    public void updateTruck(Truck truck);
    public void removeTruck(Truck truck);
}
