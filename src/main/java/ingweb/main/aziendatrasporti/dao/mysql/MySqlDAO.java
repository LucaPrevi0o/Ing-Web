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

    public static void setColumns(String[] x) { columns=x; }
    public static String[] getColumns() { return columns; }

    public static void setConnection(Connection x) { connection=x; }
    public static Connection getConnection() { return connection; }

    public static void setTableName(String x) { tableName=x; }
    public static String getTableName() { return tableName; }

    public abstract T get(String[] item); //abstract function that allows object construction based on query result

    public ArrayList<T> select() { //generic select query for every field of data

        var result=new ArrayList<T>();
        var query="select * from "+tableName;
        var res=MySqlQueryManager.getResult(connection, query);
        var resList=MySqlQueryManager.asList(res, columns);
        for (var item: resList) {

            System.out.println(Arrays.toString(item));
            var o=this.get(item);
            if (!o.isDeleted()) result.add(o);
        }

        for (var item: result) System.out.println(item.toString());
        return result;
    }

    //generic select query based on field-specific field search
    public T select(int fieldIndex, Object fieldValue) {

        var query="select * from "+tableName+" where "+columns[fieldIndex]+"='"+fieldValue+"'";
        var res=MySqlQueryManager.getResult(connection, query);
        var resList=MySqlQueryManager.asList(res, columns);
        if (resList.size()!=1) return null;
        var item=resList.get(0);
        var object=get(item);
        return (object.isDeleted() ? null : object);
    }

    //function for automatic code key generation (field used as primary key)
    public static int lastCode() {

        var query="select max("+columns[0]+") as "+columns[0]+" from "+tableName;
        var res=MySqlQueryManager.getResult(connection, query);
        var resList=MySqlQueryManager.asList(res, new String[]{columns[0]});
        return Integer.parseInt(resList.get(0)[0]);
    }

    //generic insert query
    public static void insert(Object[] data) {

        if (data.length!=columns.length) System.out.println("Data and length are different for insertion");
        var query="insert into "+tableName+" (";
        for (var i=0; i<columns.length-1; i++) query+=columns[i]+", ";
        query+=columns[columns.length-1]+") values (";
        for (var i=0; i<columns.length-1; i++) query+="?, ";
        query+="?)";
        MySqlQueryManager.execute(connection, query, data);
    }

    //generic remove query (only logical deletion is implemented, records do not get erased from the database)
    public static void remove(int code) {

        var query="update "+tableName+" set "+columns[columns.length-1]+"=? where "+columns[0]+"="+code;
        MySqlQueryManager.execute(connection, query, new Object[]{1});
    }

    //generic update query based on code primary key
    public static void update(Object[] data) {

        if (data.length!=columns.length) System.out.println("Data and length are different for update");
        var query="update "+tableName+" set ";
        for (var i=0; i<columns.length-1; i++) query+=columns[i]+"=?, ";
        query+=columns[columns.length-1]+"=? where "+columns[0]+"="+data[0];
        for (var d: data) System.out.println(d);
        MySqlQueryManager.execute(connection, query, data);
    }
}
