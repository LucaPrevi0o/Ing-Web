package ingweb.main.aziendatrasporti.dao;

import ingweb.main.aziendatrasporti.mo.Service;
import ingweb.main.aziendatrasporti.mo.Truck;
import java.util.ArrayList;

public interface TruckDAO {

    public ArrayList<Truck> findAll();
    public Truck findByNumberPlate(String numberPlate);
    public void addTruck(Truck truck);
    public void updateTruck(Truck truck);
    public void removeTruck(Truck truck);
}
