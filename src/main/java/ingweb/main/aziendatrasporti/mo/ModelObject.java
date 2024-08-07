package ingweb.main.aziendatrasporti.mo;

//ModelObject abstract class: every model object has to inherit from this in order to properly work;
//this abstract class allows from quick access to every MO-specific list of attributes in order to quickly get the
//list of attributes that need to be extracted from each DAO via database queries, and manage every database
//property that is not directly connected to the object itself (its code key and its logical deletion value); its
//behaviour is similar to an interface, because it defines a list of properties that a MO has to have in order to
//work properly, but it needs to stay as an abstract class in order to be type-erased in generic definitions for DAOs
public abstract class ModelObject {

    private int code=0; //code primary key
    private boolean deleted=false; //logical flag for deletion

    public abstract Object[] asList(); //list of every attribute of the entity
    public abstract Object[] data(); //list of specific data visible from list view
    public abstract String display(); //method for easy display of essential data in list view

    //getter-setter methods for code
    public void setCode(int code) { this.code=code; }
    public int getCode() { return this.code; }

    //getter-setter methods for deleted
    public void setDeleted(boolean deleted) { this.deleted=deleted; }
    public boolean isDeleted() { return this.deleted; }
}
