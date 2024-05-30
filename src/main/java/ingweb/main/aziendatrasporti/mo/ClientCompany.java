package ingweb.main.aziendatrasporti.mo;

public class ClientCompany implements ModelObject {

    String name;
    String socialReason;
    String location;
    String manager;
    boolean deleted;

    public ClientCompany(String name, String socialReason, String location, String manager, boolean deleted) {

        this.name=name;
        this.socialReason=socialReason;
        this.location=location;
        this.manager=manager;
        this.deleted=deleted;
    }

    public String toString() { return name+" ("+socialReason+") - "+location+": "+manager; }

    public Object[] asList() {

        return new Object[] {name,socialReason,location,manager,deleted};
    }

    public String getName() { return name; }
    public void setName(String name) { this.name=name; }

    public String getSocialReason() { return socialReason; }
    public void setSocialReason(String socialReason) { this.socialReason=socialReason; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location=location; }

    public String getManager() { return manager; }
    public void setManager(String manager) { this.manager=manager; }

    public boolean isDeleted() { return deleted; }
    public void setDeleted(boolean deleted) { this.deleted=deleted; }
}
