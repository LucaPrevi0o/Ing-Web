package ingweb.main.aziendatrasporti.mo;

public class Account implements ModelObject {

    private String username;
    private String password;
    private String fullName;
    private boolean admin;
    private boolean deleted;

    public String toString() { return fullName+": "+username+"@"+password+" ("+admin+", "+deleted+")"; }
    public Object[] asList() { return new Object[] {username, password, fullName, admin, deleted}; }
    public Object[] data() { return asList(); }

    public Account(String username, String password, String fullName, boolean admin, boolean deleted) {

        this.username=username;
        this.password=password;
        this.fullName=fullName;
        this.admin=admin;
        this.deleted=deleted;
    }

    public boolean equals(Object o) {

        if (!(o instanceof Account)) return false;
        if (!((Account)o).username.equals(this.username)) return false;
        if (!((Account)o).password.equals(this.password)) return false;
        if (!((Account)o).fullName.equals(this.fullName)) return false;
        if (((Account)o).admin!=this.admin) return false;
        return ((Account)o).deleted==this.deleted;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName=fullName; }

    public boolean isAdmin() { return admin; }
    public void setAdmin(boolean admin) { this.admin=admin; }

    public boolean isDeleted() { return deleted; }
    public void setDeleted(boolean deleted) { this.deleted=deleted; }
}
