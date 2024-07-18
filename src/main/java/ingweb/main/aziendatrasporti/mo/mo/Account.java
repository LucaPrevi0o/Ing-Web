package ingweb.main.aziendatrasporti.mo.mo;

import ingweb.main.aziendatrasporti.mo.ModelObject;

public class Account extends ModelObject {

    private String username;
    private String password;
    private String fullName;
    private String bankCoordinates;
    private int level;

    //static values for account levels
    public final static int WORKER_LEVEL=0;
    public final static int ADMIN_LEVEL=1;
    public final static int MANAGER_LEVEL=2;

    public String toString() { return this.fullName+": "+this.username+"@"+this.password+" ("+this.level+", "+this.isDeleted()+") - "+this.bankCoordinates; }
    public Object[] asList() { return new Object[] {this.getCode(), this.username, this.password, this.fullName, this.bankCoordinates, this.level, this.isDeleted()}; }
    public Object[] data() { return new Object[] {this.username, this.password, this.fullName, this.bankCoordinates, this.level}; }
    public String display() { return this.fullName+" ("+this.username+"): "+this.level; }

    public Account(int code, String username, String password, String fullName, String bankCoordinates, int level, boolean deleted) {

        this.setCode(code);
        this.username=username;
        this.password=password;
        this.fullName=fullName;
        this.bankCoordinates=bankCoordinates;
        this.level=level;
        this.setDeleted(deleted);
    }

    public Account(String username, String password, String fullName, String bankCoordinates, int level) {

        this.username=username;
        this.password=password;
        this.fullName=fullName;
        this.bankCoordinates=bankCoordinates;
        this.level=level;
    }

    public boolean equals(Object o) {

        if (!(o instanceof Account)) return false;
        if (((Account)o).getCode()!=this.getCode()) return false;
        if (!((Account)o).username.equals(this.username)) return false;
        if (!((Account)o).password.equals(this.password)) return false;
        if (!((Account)o).fullName.equals(this.fullName)) return false;
        if (!((Account)o).bankCoordinates.equals(this.bankCoordinates)) return false;
        if (((Account)o).level!=this.level) return false;
        return ((Account)o).isDeleted()==this.isDeleted();
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName=fullName; }

    public String getBankCoordinates() { return bankCoordinates; }
    public void setBankCoordinates(String bankCoordinates) { this.bankCoordinates=bankCoordinates; }

    public int getLevel() { return level; }
    public void setLevel(int level) { this.level=level; }
}
