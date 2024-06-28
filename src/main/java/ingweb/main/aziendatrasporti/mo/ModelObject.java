package ingweb.main.aziendatrasporti.mo;

//ModelObject interface: every model object has to inherit from this in order to properly work;
//this interface allows from quick access to every MO-specific list of attributes in order to quickly get the
//list of attributes that need to be extracted from each DAO via database queries
public interface ModelObject {

    public Object[] asList(); //list of every attribute to be set for the object
}
