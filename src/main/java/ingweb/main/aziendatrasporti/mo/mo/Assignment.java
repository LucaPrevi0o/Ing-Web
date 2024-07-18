package ingweb.main.aziendatrasporti.mo.mo;

import ingweb.main.aziendatrasporti.mo.ModelObject;

public class Assignment extends ModelObject {

    private Service service;
    private Worker firstDriver;
    private Worker secondDriver;
    private Truck truck;
    private String comment;
    boolean completed;

    public Assignment(int code, Service service, Worker firstDriver, Worker secondDriver, Truck truck, String comment, boolean completed, boolean deleted) {

        this.setCode(code);
        this.service=service;
        this.firstDriver=firstDriver;
        this.secondDriver=secondDriver;
        this.truck=truck;
        this.comment=comment;
        this.completed=completed;
        this.setDeleted(deleted);
    }

    public Assignment(Service service, Worker firstDriver, Worker secondDriver, Truck truck, String comment, boolean completed) {

        this.service=service;
        this.firstDriver=firstDriver;
        this.secondDriver=secondDriver;
        this.truck=truck;
        this.comment=comment;
        this.completed=completed;
    }

    public Object[] data() { return new Object[]{this.service, this.firstDriver, this.secondDriver, this.truck, this.comment, this.completed}; }
    public Object[] asList() { return new Object[]{this.getCode(), this.service, this.firstDriver, this.secondDriver, this.truck, this.comment, this.completed, this.isDeleted()}; }
    public String display() { return "["+this.service.display()+"]: (["+this.firstDriver.display()+"], ["+this.secondDriver.display()+"]) - ["+this.truck.display()+"] - "+this.comment+" ("+this.completed+")"; }
    public String toString() { return "["+this.service+"]: (["+this.firstDriver+"], ["+this.secondDriver+"]) - ["+this.truck+"] - "+this.comment+" ("+this.completed+")"; }

    public boolean equals(Object o) {

        if (!(o instanceof Assignment)) return false;
        if (((Assignment)o).getCode()!=this.getCode()) return false;
        if (!((Assignment)o).service.equals(this.service)) return false;
        if (!((Assignment)o).firstDriver.equals(this.firstDriver)) return false;
        if (!((Assignment)o).secondDriver.equals(this.secondDriver)) return false;
        if (!((Assignment)o).truck.equals(this.truck)) return false;
        if (!((Assignment)o).comment.equals(this.comment)) return false;
        return (((Assignment)o).isDeleted()==this.isDeleted() && ((Assignment)o).completed==this.completed);
    }

    public Service getService() { return service; }
    public void setService(Service service) { this.service = service; }

    public Worker getFirstDriver() { return firstDriver; }
    public void setFirstDriver(Worker firstDriver) { this.firstDriver = firstDriver; }

    public Worker getSecondDriver() { return secondDriver; }
    public void setSecondDriver(Worker secondDriver) { this.secondDriver = secondDriver; }

    public Truck getTruck() { return truck; }
    public void setTruck(Truck truck) { this.truck = truck; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment=comment; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed=completed; }
}
