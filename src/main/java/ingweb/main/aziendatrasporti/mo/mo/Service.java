package ingweb.main.aziendatrasporti.mo.mo;

import ingweb.main.aziendatrasporti.mo.ModelObject;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

public class Service extends ModelObject {

    String name;
    ClientCompany clientCompany;
    Date date;
    Time startTime, duration;
    ArrayList<License> validLicenses;

    public Service(int code, String name, ClientCompany clientCompany, Date date, Time startTime, Time duration, boolean deleted) {

        this.setCode(code);
        this.name=name;
        this.clientCompany=clientCompany;
        this.date=date;
        this.startTime=startTime;
        this.duration=duration;
        this.setDeleted(deleted);
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
        if (((Service)o).getCode()!=this.getCode()) return false;
        if (!((Service)o).name.equals(this.name)) return false;
        if (!((Service)o).clientCompany.equals(this.clientCompany)) return false;
        if (!((Service)o).date.equals(this.date)) return false;
        if (!((Service)o).startTime.equals(this.startTime)) return false;
        if (!((Service)o).duration.equals(this.duration)) return false;
        return ((Service)o).isDeleted()==this.isDeleted();
    }

    public Object[] asList() { return new Object[]{this.getCode(), this.name, this.clientCompany.getSocialReason(), this.date, this.startTime, this.duration, this.isDeleted()}; }
    public Object[] data() { return new Object[]{this.name, this.clientCompany, this.date, this.startTime, this.duration}; }
    public String toString() { return this.name+" ("+this.getCode()+") - ["+this.clientCompany+"] - "+this.date+" - "+this.startTime+" ("+this.duration+")"; }

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
}
