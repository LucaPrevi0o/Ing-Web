package ingweb.main.aziendatrasporti.mo;

public class Assignment implements ModelObject {

    private Service service;
    private Worker firstDriver;
    private Worker secondDriver;
    private Truck truck;
    private boolean deleted;

    public Assignment(Service service, Worker firstDriver, Worker secondDriver, Truck truck, boolean deleted) {

        this.service = service;
        this.firstDriver = firstDriver;
        this.secondDriver = secondDriver;
        this.truck = truck;
        this.deleted = deleted;
    }
    public Object[] data() { return asList(); }

    public Object[] asList() { return new Object[]{service, firstDriver, secondDriver, truck, deleted}; }
    public boolean equals(Object o) {

        if (!(o instanceof Assignment)) return false;
        if (!((Assignment)o).service.equals(this.service)) return false;
        if (!((Assignment)o).firstDriver.equals(this.firstDriver)) return false;
        if (!((Assignment)o).secondDriver.equals(this.secondDriver)) return false;
        if (!((Assignment)o).truck.equals(this.truck)) return false;
        return ((Assignment)o).deleted==this.deleted;
    }

    public Service getService() { return service; }
    public void setService(Service service) { this.service = service; }

    public Worker getFirstDriver() { return firstDriver; }
    public void setFirstDriver(Worker firstDriver) { this.firstDriver = firstDriver; }

    public Worker getSecondDriver() { return secondDriver; }
    public void setSecondDriver(Worker secondDriver) { this.secondDriver = secondDriver; }

    public Truck getTruck() { return truck; }
    public void setTruck(Truck truck) { this.truck = truck; }

    public boolean isDeleted() { return deleted; }
    public void setDeleted(boolean deleted) { this.deleted = deleted; }
}
