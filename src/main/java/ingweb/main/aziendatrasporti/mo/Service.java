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
    ArrayList<License> validLicenses;
    boolean deleted;

    public Service(int code, String name, ClientCompany clientCompany, Date date, Time startTime, Time duration, boolean deleted) {

        this.code=code;
        this.name=name;
        this.clientCompany=clientCompany;
        this.date=date;
        this.startTime=startTime;
        this.duration=duration;
        this.deleted=deleted;
    }

    public Service(String name, ClientCompany clientCompany, Date date, Time startTime, Time duration) {

        this.name=name;
        this.clientCompany=clientCompany;
        this.date=date;
        this.startTime=startTime;
        this.duration=duration;
    }

    public boolean equals(Object o) {

        if (!(o instanceof Service)) return false;
        if (((Service)o).code!=this.code) return false;
        if (!((Service)o).name.equals(this.name)) return false;
        if (!((Service)o).clientCompany.equals(this.clientCompany)) return false;
        if (!((Service)o).date.equals(this.date)) return false;
        if (!((Service)o).startTime.equals(this.startTime)) return false;
        if (!((Service)o).duration.equals(this.duration)) return false;
        return ((Service)o).deleted==this.deleted;
    }

    public Object[] asList() { return new Object[]{this.code, this.name, this.clientCompany, this.date, this.startTime, this.duration, this.deleted}; }
    public Object[] data() { return new Object[]{this.name, this.clientCompany, this.date, this.startTime, this.duration}; }
    public String toString() { return this.name+" ("+this.code+") - ["+this.clientCompany+"] - "+this.date+" - "+this.startTime+" ("+this.duration+")"; }

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

    public ArrayList<License> getValidLicenses() { return validLicenses; }
    public void setValidLicenses(ArrayList<License> validLicenses) { this.validLicenses=validLicenses; }

    public boolean isDeleted() { return deleted; }
    public void setDeleted(boolean deleted) { this.deleted=deleted; }
}
