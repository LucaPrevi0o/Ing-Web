package ingweb.main.aziendatrasporti.mo;

//ModelObject interface: every model object has to inherit from this in order to properly work;
//this interface allows from quick access to every MO-specific list of attributes in order to quickly get the
//list of attributes that need to be extracted from each DAO via database queries, and manage every database
//property that is not directly connected to the object itself (its code key and its logical deletion value)
public abstract class ModelObject {

    private int code=0; //code primary key
    private boolean deleted=false; //logical flag for deletion

    public abstract Object[] asList(); //list of every attribute
    public abstract Object[] data(); //list of specific data for insertion

    public void setCode(int code) { this.code=code; }
    public int getCode() { return this.code; }

    public void setDeleted(boolean deleted) { this.deleted=deleted; }
    public boolean isDeleted() { return this.deleted; }
}
