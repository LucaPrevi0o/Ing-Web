package ingweb.main.aziendatrasporti.dao.mysql;

import ingweb.main.aziendatrasporti.mo.ModelObject;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;

//abstract MySQL DAO implementation: methods implemented as specific for every object type allow for dynamic query
//construction based entirely on the MySQL schema, as the interface for the database communication is also dynamically
//generated based on the result set from the base schema (containing the data that represents the model of the object
//inside the database itself); as every DAO extends this class, to allow for simpler query construction, it's necessary
//to re-define the interface every time the transaction works with a different table, because the DAO can store only
//one table definition at a time
public abstract class MySqlDAO<T extends ModelObject> {

    private static String[] columns; //list of column names for the table
    private static Connection connection; //reference to the established MySQL database connection
    private static String tableName; //table name (this is not dynamic, and has to be specified in code)

    public final static Object IS_NULL=new Object(); //static reference for null column
    public final static Object IS_NOT_NULL=new Object(); //static reference for not null column

    public static void setColumns(String[] x) { columns=x; }
    public static String[] getColumns() { return columns; }

    public static void setConnection(Connection x) { connection=x; }
    public static Connection getConnection() { return connection; }

    public static void setTableName(String x) { tableName=x; }
    public static String getTableName() { return tableName; }

    public abstract T get(String[] item); //abstract function that allows object construction based on query result

    //generation of a composite query with multiple filter clauses
    private String generateCompositeQuery(int[] indexes, Object[] values) {

        if (indexes.length!=values.length) return ""; //break early if the arrays do not have same length
        var query="select * from "+tableName+" where "; //table selection
        for (var i=0; i<indexes.length; i++) {

            if (indexes[i]>=columns.length) return ""; //break early if an index is bigger than the length of all columns
            if (i>0) query+="and "; //chain every filter clause
            query+=columns[indexes[i]];
            if (values[i]== IS_NULL) query+=" is null "; //filter by null column
            else if (values[i]== IS_NOT_NULL) query+=" is not null "; //filter by not null column
            else query+=" = '"+values[i]+"' "; //filter by value
        }

        query+=" and "+columns[columns.length-1]+" = '0'"; //only select elements flagged as not deleted
        return query;
    }

    public ArrayList<T> selectAll() { //generic select query for every field of data

        var result=new ArrayList<T>();
        var query="select * from "+tableName+" where "+columns[columns.length-1]+" = 0";
        var res=MySqlQueryManager.getResult(connection, query);
        var resList=MySqlQueryManager.asList(res, columns);
        for (var item: resList) result.add(get(item));
        return result;
    }

    //generic select query based on field-specific field search for arbitrary number of parameters
    public ArrayList<T> selectAll(int[] fieldIndexes, Object[] fieldValues) {

        var result=new ArrayList<T>();
        var query=generateCompositeQuery(fieldIndexes, fieldValues);
        if (query.isEmpty()) return new ArrayList<>();
        var res=MySqlQueryManager.getResult(connection, query);
        var resList=MySqlQueryManager.asList(res, columns);
        for (var item: resList) result.add(get(item));
        return result;
    }

    //generic select query based on field-specific field search for arbitrary number of parameters
    public T select(int[] fieldIndexes, Object[] fieldValues) {

        var query=generateCompositeQuery(fieldIndexes, fieldValues);
        System.out.println(query);
        if (query.isEmpty()) return null;
        var res=MySqlQueryManager.getResult(connection, query);
        var resList=MySqlQueryManager.asList(res, columns);
        if (resList.size()!=1) return null;
        return get(resList.get(0));
    }

    //function for automatic code key generation (field used as primary key)
    public static int lastCode() {

        var query="select max("+columns[0]+") as "+columns[0]+" from "+tableName;
        var res=MySqlQueryManager.getResult(connection, query);
        var resList=MySqlQueryManager.asList(res, new String[]{columns[0]});
        return (resList.get(0)[0]==null ? 0 : Integer.parseInt(resList.get(0)[0]));
    }

    //generic insert query for every field of the table
    public static void insert(Object[] data) {

        var query="insert into "+tableName+" (";
        for (var i=0; i<columns.length-1; i++) query+=columns[i]+", ";
        query+=columns[columns.length-1]+") values (";
        for (var i=0; i<columns.length-1; i++) query+="?, ";
        query+="?)";
        MySqlQueryManager.execute(connection, query, data);
    }

    //generic remove query (only logical deletion is used, records do not get deleted from the database)
    public static void remove(int code) {

        var query="update "+tableName+" set "+columns[columns.length-1]+" = ? where "+columns[0]+" = '"+code+"'";
        MySqlQueryManager.execute(connection, query, new Object[]{1});
    }

    //generic delete query to cancel items from the database
    public static void delete(int index, Object code) {

        var query="delete from "+tableName+" where "+columns[index]+" = ?";
        MySqlQueryManager.execute(connection, query, new Object[]{code});
    }

    //generic update query based on code primary key
    public static void update(Object[] data) {

        var query="update "+tableName+" set ";
        for (var i=0; i<columns.length-1; i++) query+=columns[i]+" = ?, "; //first column (primary key) is not updated, but necessary in the query
        query+=columns[columns.length-1]+" = ? where "+columns[0]+" = '"+data[0]+"'"; //specify record to update based on first column
        MySqlQueryManager.execute(connection, query, data); //array always contains first column as update, even if it's not updated
    }
}
