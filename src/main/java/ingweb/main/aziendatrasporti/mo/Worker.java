package ingweb.main.aziendatrasporti.mo;

import java.sql.Date;
import java.util.ArrayList;


public class Worker implements ModelObject {

    private int code;
    private String name;
    private String surname;
    private String fiscalCode;
    private Date birthDate;
    private String telNumber;
    private ArrayList<License> licenses;
    private boolean deleted;

    public Worker(int code, String name, String surname, String fiscalCode, Date birthDate, String telNumber, boolean deleted) {

        this.code=code;
        this.name=name;
        this.surname=surname;
        this.fiscalCode=fiscalCode;
        this.birthDate=birthDate;
        this.telNumber=telNumber;
        this.deleted=deleted;
    }

    public Worker(String name, String surname, String fiscalCode, Date birthDate, String telNumber, boolean deleted) {

        this.name=name;
        this.surname=surname;
        this.fiscalCode=fiscalCode;
        this.birthDate=birthDate;
        this.telNumber=telNumber;
        this.deleted=deleted;
    }

    public Object[] asList() { return new Object[]{name, surname, fiscalCode, birthDate, telNumber, deleted}; }

    public boolean equals(Object o) {

        if (!(o instanceof Worker)) return false;
        if (((Worker)o).code!=this.code) return false;
        if (!((Worker)o).name.equals(this.name)) return false;
        if (!((Worker)o).surname.equals(this.surname)) return false;
        if (!((Worker)o).fiscalCode.equals(this.fiscalCode)) return false;
        if (((Worker)o).birthDate.equals(this.birthDate)) return false;
        if (((Worker)o).telNumber.equals(this.telNumber)) return false;
        if (((Worker)o).licenses.equals(this.licenses)) return false;
        return ((Worker)o).deleted==this.deleted;
    }

    public String toString() {

        var res=name+" "+surname+" ("+fiscalCode+") - "+birthDate+" - "+telNumber+": ";
        for (var license: licenses) res+="\""+license+"\" ";
        return res;
    }

    public String display() { return this.name+" "+this.surname+" ("+this.fiscalCode+")"; }

    public int getCode() { return this.code; }
    public void setCode(int code) { this.code=code; }

    public String getName() { return name; }
    public void setName(String name) { this.name=name; }

    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname=surname; }

    public String getFiscalCode() { return fiscalCode; }
    public void setFiscalCode(String fiscalCode) { this.fiscalCode=fiscalCode; }

    public Date getBirthDate() { return birthDate; }
    public void setBirthDate(Date birthDate) { this.birthDate=birthDate; }

    public String getTelNumber() { return telNumber; }
    public void setTelNumber(String telNumber) { this.telNumber=telNumber; }

    public ArrayList<License> getLicenses() { return licenses; }
    public void setLicenses(ArrayList<License> licenses) { this.licenses = licenses; }

    public boolean isDeleted() { return deleted; }
    public void setDeleted(boolean deleted) { this.deleted=deleted; }
}
