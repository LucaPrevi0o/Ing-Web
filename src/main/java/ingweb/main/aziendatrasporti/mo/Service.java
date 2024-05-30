package ingweb.main.aziendatrasporti.mo;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

public class Service implements ModelObject {

    int code;
    String name;
    ClientCompany clientCompany;
    Date date;
    Time startTime, duration;
    Worker firstDriver, secondDriver;
    Truck truck;
    ArrayList<License> validLicenses;
    boolean deleted;

    public Service(int code, String name, ClientCompany clientCompany, Date date, Time startTime, Time duration, Worker firstDriver, Worker secondDriver, Truck truck, boolean deleted) {

        this.code=code;
        this.name=name;
        this.clientCompany=clientCompany;
        this.date=date;
        this.startTime=startTime;
        this.duration=duration;
        this.firstDriver=firstDriver;
        this.secondDriver=secondDriver;
        this.truck=truck;
        this.deleted=deleted;
    }

    public Service(int code, String name, ClientCompany clientCompany, Date date, Time startTime, Time duration, boolean deleted) {

        this.code=code;
        this.name=name;
        this.clientCompany=clientCompany;
        this.date=date;
        this.startTime=startTime;
        this.duration=duration;
        this.deleted=deleted;
    }

    public Object[] asList() {

        var arr=new Object[10];
        arr[0]=code;
        arr[1]=name;
        arr[2]=clientCompany;
        arr[3]=date;
        arr[4]=startTime;
        arr[5]=duration;
        arr[6]=firstDriver;
        arr[7]=secondDriver;
        arr[8]=truck;
        arr[9]=deleted;
        return arr;
    }

    public int getCode() { return code; }
    public void setCode(int code) { this.code=code; }

    public String getName() { return name; }
    public void setName(String name) { this.name=name; }

    public ClientCompany getClientCompany() { return clientCompany; }
    public void setClientCompany(ClientCompany clientCompany) { this.clientCompany=clientCompany; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date=date; }

    public Time getStartTime() { return startTime; }
    public void setStartTime(Time startTime) { this.startTime=startTime; }

    public Time getDuration() { return duration; }
    public void setDuration(Time duration) { this.duration=duration; }

    public Worker getFirstDriver() { return firstDriver; }
    public void setFirstDriver(Worker firstDriver) { this.firstDriver=firstDriver; }

    public Worker getSecondDriver() { return secondDriver; }
    public void setSecondDriver(Worker secondDriver) { this.secondDriver=secondDriver; }

    public Truck getTruck() { return truck; }
    public void setTruck(Truck truck) { this.truck=truck; }

    public ArrayList<License> getValidLicenses() { return validLicenses; }
    public void setValidLicenses(ArrayList<License> validLicenses) { this.validLicenses=validLicenses; }

    public boolean isDeleted() { return deleted; }
    public void setDeleted(boolean deleted) { this.deleted=deleted; }
}
