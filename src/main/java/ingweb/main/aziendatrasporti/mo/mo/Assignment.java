package ingweb.main.aziendatrasporti.mo.mo;

import ingweb.main.aziendatrasporti.mo.ModelObject;

public class Assignment extends ModelObject {

    private Service service;
    private Worker firstDriver;
    private Worker secondDriver;
    private Truck truck;

    public Assignment(int code, Service service, Worker firstDriver, Worker secondDriver, Truck truck, boolean deleted) {

        this.setCode(code);
        this.service=service;
        this.firstDriver=firstDriver;
        this.secondDriver=secondDriver;
        this.truck=truck;
        this.setDeleted(deleted);
    }

    public Assignment(Service service, Worker firstDriver, Worker secondDriver, Truck truck) {

        this.service=service;
        this.firstDriver=firstDriver;
        this.secondDriver=secondDriver;
        this.truck=truck;
    }

    public Object[] data() { return new Object[]{this.service, this.firstDriver, this.secondDriver, this.truck}; }
    public Object[] asList() { return new Object[]{this.getCode(), this.service, this.firstDriver, this.secondDriver, this.truck, this.isDeleted()}; }
    public String display() { return toString(); }
    public String toString() { return "["+this.service+"]: {"+this.firstDriver+", "+this.secondDriver+"} - "+this.truck; }

    public boolean equals(Object o) {

        if (!(o instanceof Assignment)) return false;
        if (((Assignment)o).getCode()!=this.getCode()) return false;
        if (!((Assignment)o).service.equals(this.service)) return false;
        if (!((Assignment)o).firstDriver.equals(this.firstDriver)) return false;
        if (!((Assignment)o).secondDriver.equals(this.secondDriver)) return false;
        if (!((Assignment)o).truck.equals(this.truck)) return false;
        return ((Assignment)o).isDeleted()==this.isDeleted();
    }

    public Service getService() { return service; }
    public void setService(Service service) { this.service = service; }

    public Worker getFirstDriver() { return firstDriver; }
    public void setFirstDriver(Worker firstDriver) { this.firstDriver = firstDriver; }

    public Worker getSecondDriver() { return secondDriver; }
    public void setSecondDriver(Worker secondDriver) { this.secondDriver = secondDriver; }

    public Truck getTruck() { return truck; }
    public void setTruck(Truck truck) { this.truck = truck; }
}
