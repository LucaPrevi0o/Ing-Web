package ingweb.main.aziendatrasporti.mo;

import java.sql.Date;

public class ClientCompany implements ModelObject {

    int code;
    String name;
    String socialReason;
    String location;
    String managerName;
    String managerFiscalCode;
    Date managerBirthDate;
    String managerTelNumber;
    boolean deleted;

    public ClientCompany(String name, String socialReason, String location, String managerName, String managerFiscalCode, Date managerBirthDate, String managerTelNumber, boolean deleted) {

        this.name=name;
        this.socialReason=socialReason;
        this.location=location;
        this.managerName=managerName;
        this.managerFiscalCode=managerFiscalCode;
        this.managerBirthDate=managerBirthDate;
        this.managerTelNumber=managerTelNumber;
        this.deleted=deleted;
    }

    public ClientCompany(int code, String name, String socialReason, String location, String managerName, String managerFiscalCode, Date managerBirthDate, String managerTelNumber, boolean deleted) {

        this.code=code;
        this.name=name;
        this.socialReason=socialReason;
        this.location=location;
        this.managerName=managerName;
        this.managerFiscalCode=managerFiscalCode;
        this.managerBirthDate=managerBirthDate;
        this.managerTelNumber=managerTelNumber;
        this.deleted=deleted;
    }

    public String toString() { return name+" ("+socialReason+") - "+location+": "+managerName+" ("+managerFiscalCode+"), "+managerBirthDate+" - "+managerTelNumber; }

    public String display() { return this.name+" ("+this.socialReason+")"; }

    public Object[] asList() { return new Object[] {name, socialReason, location, managerName, managerFiscalCode, managerBirthDate, managerTelNumber, deleted}; }

    public int getCode() { return this.code; }
    public void setCode(int code) { this.code=code; }

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

    public boolean isDeleted() { return deleted; }
    public void setDeleted(boolean deleted) { this.deleted=deleted; }
}
