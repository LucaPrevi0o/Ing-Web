package ingweb.main.aziendatrasporti.mo;

import java.util.ArrayList;

public class Truck implements ModelObject {

    int code;
    String numberPlate;
    String brand;
    String model;
    boolean available;
    ArrayList<License> neededLicenses=new ArrayList<>();
    boolean deleted;

    public Truck(String numberPlate, String brand, String model, boolean available, boolean deleted) {

        this.numberPlate=numberPlate;
        this.brand=brand;
        this.model=model;
        this.available=available;
        this.deleted=deleted;
    }

    public Truck(int code, String numberPlate, String brand, String model, boolean available, boolean deleted) {

        this.code=code;
        this.numberPlate=numberPlate;
        this.brand=brand;
        this.model=model;
        this.available=available;
        this.deleted=deleted;
    }

    public Object[] asList() { return new Object[]{numberPlate, brand, model, available, deleted}; }

    public String toString() {

        var res=numberPlate.toUpperCase()+": "+brand+" "+model+" ("+(available ? "Y" : "N")+") - ";
        for (var license: neededLicenses) res+="\""+license+"\" ";
        return res;
    }

    public boolean equals(Object o) {

        if (!(o instanceof Truck)) return false;
        if (!((Truck)o).numberPlate.equals(this.numberPlate)) return false;
        if (!((Truck)o).brand.equals(this.brand)) return false;
        if (!((Truck)o).model.equals(this.model)) return false;
        if (((Truck)o).available!=this.available) return false;
        return ((Truck)o).deleted==this.deleted;
    }

    public int getCode() { return this.code; }
    public void setCode(int code) { this.code=code; }

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

    public boolean isDeleted() { return deleted; }
    public void setDeleted(boolean deleted) { this.deleted=deleted; }
}
