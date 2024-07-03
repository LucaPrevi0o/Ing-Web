package ingweb.main.aziendatrasporti.dao;

import ingweb.main.aziendatrasporti.mo.mo.Service;
import ingweb.main.aziendatrasporti.mo.mo.Truck;
import java.util.ArrayList;

public interface TruckDAO {

    public ArrayList<Truck> findAll();
    public ArrayList<Truck> findAvailableByService(Service service);
    public ArrayList<Truck> findAllAvailable();
    public Truck findByCode(int code);
    public Truck findByNumberPlate(String numberPlate);
    public int findLastCode();
    public void addTruck(Truck truck);
    public void updateTruck(Truck truck);
    public void removeTruck(Truck truck);
}