package ingweb.main.aziendatrasporti.mo.mo;

import ingweb.main.aziendatrasporti.mo.ModelObject;

import java.sql.Date;

public class ClientCompany extends ModelObject {

    private String name;
    private String socialReason;
    private String location;
    private String managerName;
    private String managerFiscalCode;
    private Date managerBirthDate;
    private String managerTelNumber;

    public ClientCompany(String name, String socialReason, String location, String managerName, String managerFiscalCode, Date managerBirthDate, String managerTelNumber) {

        this.name=name;
        this.socialReason=socialReason;
        this.location=location;
        this.managerName=managerName;
        this.managerFiscalCode=managerFiscalCode;
        this.managerBirthDate=managerBirthDate;
        this.managerTelNumber=managerTelNumber;
    }

    public ClientCompany(int code, String name, String socialReason, String location, String managerName, String managerFiscalCode, Date managerBirthDate, String managerTelNumber, boolean deleted) {

        this.setCode(code);
        this.name=name;
        this.socialReason=socialReason;
        this.location=location;
        this.managerName=managerName;
        this.managerFiscalCode=managerFiscalCode;
        this.managerBirthDate=managerBirthDate;
        this.managerTelNumber=managerTelNumber;
        this.setDeleted(deleted);
    }

    public String toString() { return this.name+" ("+this.socialReason+") - "+this.location+": "+this.managerName+" ("+this.managerFiscalCode+"), "+this.managerBirthDate+" - "+this.managerTelNumber; }
    public String display() { return this.name+" ("+this.socialReason+")"; }
    public Object[] asList() { return new Object[] {this.getCode(), this.name, this.socialReason, this.location, this.managerName, this.managerFiscalCode, this.managerBirthDate, this.managerTelNumber, this.isDeleted()}; }
    public Object[] data() { return new Object[] {this.name, this.socialReason, this.location, this.managerName, this.managerFiscalCode, this.managerBirthDate, this.managerTelNumber}; }

    public String getName() { return name; }
    public void setName(String name) { this.name=name; }

    public String getSocialReason() { return socialReason; }
    public void setSocialReason(String socialReason) { this.socialReason=socialReason; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location=location; }

    public String getManagerFiscalCode() { return managerFiscalCode; }
    public void setManagerFiscalCode(String managerFiscalCode) { this.managerFiscalCode=managerFiscalCode; }

    public String getManagerName() { return managerName; }
    public void setManagerName(String managerName) { this.managerName=managerName; }

    public Date getManagerBirthDate() { return managerBirthDate; }
    public void setManagerBirthDate(Date managerBirthDate) { this.managerBirthDate=managerBirthDate; }

    public String getManagerTelNumber() { return managerTelNumber; }
    public void setManagerTelNumber(String managerTelNumber) { this.managerTelNumber=managerTelNumber; }
}
