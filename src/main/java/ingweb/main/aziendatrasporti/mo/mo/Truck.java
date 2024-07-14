package ingweb.main.aziendatrasporti.mo.mo;

import ingweb.main.aziendatrasporti.mo.ModelObject;
import java.util.ArrayList;

public class Truck extends ModelObject {

    String numberPlate;
    String brand;
    String model;
    boolean available;
    ArrayList<License> neededLicenses=new ArrayList<>();

    public Truck(String numberPlate, String brand, String model, boolean available) {

        this.numberPlate=numberPlate;
        this.brand=brand;
        this.model=model;
        this.available=available;
    }

    public Truck(int code, String numberPlate, String brand, String model, boolean available, boolean deleted) {

        this.setCode(code);
        this.numberPlate=numberPlate;
        this.brand=brand;
        this.model=model;
        this.available=available;
        this.setDeleted(deleted);
    }

    public Object[] asList() { return new Object[]{this.getCode(), this.numberPlate, this.brand, this.model, this.available, this.isDeleted()}; }
    public Object[] data() { return new Object[]{this.numberPlate, this.brand, this.model, this.available}; }
    public String toString() { return this.numberPlate.toUpperCase()+": "+this.brand+" "+this.model+" ("+(this.available ? "Y" : "N")+")"; }
    public String display() { return this.numberPlate+": "+this.brand+" "+this.model; }

    public boolean equals(Object o) {

        if (!(o instanceof Truck)) return false;
        if (((Truck)o).getCode()!=this.getCode()) return false;
        if (!((Truck)o).numberPlate.equals(this.numberPlate)) return false;
        if (!((Truck)o).brand.equals(this.brand)) return false;
        if (!((Truck)o).model.equals(this.model)) return false;
        if (((Truck)o).available!=this.available) return false;
        return ((Truck)o).isDeleted()==this.isDeleted();
    }

    public String getNumberPlate() { return numberPlate; }
    public void setNumberPlate(String numberPlate) { this.numberPlate=numberPlate; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand=brand; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model=model; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available=available; }

    public ArrayList<License> getNeededLicenses() { return neededLicenses; }
    public void setNeededLicenses(ArrayList<License> neededLicenses) { this.neededLicenses=neededLicenses; }
}
