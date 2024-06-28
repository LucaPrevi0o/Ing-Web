package ingweb.main.aziendatrasporti.mo;

public class Assignment implements ModelObject {

    private Service service;
    private Worker firstDriver;
    private Worker secondDriver;
    private Truck truck;
    private boolean deleted;

    public Object[] asList() { return new Object[]{service, firstDriver, secondDriver, truck, deleted}; }

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
