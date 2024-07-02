package ingweb.main.aziendatrasporti.mo.mo;

import ingweb.main.aziendatrasporti.mo.ModelObject;
import java.sql.Date;
import java.util.ArrayList;

public class Worker extends ModelObject {

    private String name;
    private String surname;
    private String fiscalCode;
    private Date birthDate;
    private String telNumber;
    private ArrayList<License> licenses;

    public Worker(int code, String name, String surname, String fiscalCode, Date birthDate, String telNumber, boolean deleted) {

        this.setCode(code);
        this.name=name;
        this.surname=surname;
        this.fiscalCode=fiscalCode;
        this.birthDate=birthDate;
        this.telNumber=telNumber;
        this.setDeleted(deleted);
    }

    public Worker(String name, String surname, String fiscalCode, Date birthDate, String telNumber) {

        this.name=name;
        this.surname=surname;
        this.fiscalCode=fiscalCode;
        this.birthDate=birthDate;
        this.telNumber=telNumber;
    }

    public Object[] asList() { return new Object[]{this.getCode(), this.name, this.surname, this.fiscalCode, this.birthDate, this.telNumber, this.isDeleted()}; }
    public Object[] data() { return new Object[]{this.name, this.surname, this.fiscalCode, this.birthDate, this.telNumber}; }
    public String toString() { return this.name+" "+this.surname+" ("+this.fiscalCode+"): "+this.birthDate+" - "+this.telNumber; }
    public String display() { return this.name+" "+this.surname+" ("+this.fiscalCode+")"; }

    public boolean equals(Object o) {

        if (!(o instanceof Worker)) return false;
        if (((Worker)o).getCode()!=this.getCode()) return false;
        if (!((Worker)o).name.equals(this.name)) return false;
        if (!((Worker)o).surname.equals(this.surname)) return false;
        if (!((Worker)o).fiscalCode.equals(this.fiscalCode)) return false;
        if (((Worker)o).birthDate.equals(this.birthDate)) return false;
        if (((Worker)o).telNumber.equals(this.telNumber)) return false;
        if (((Worker)o).licenses.equals(this.licenses)) return false;
        return ((Worker)o).isDeleted()==this.isDeleted();
    }

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
}
