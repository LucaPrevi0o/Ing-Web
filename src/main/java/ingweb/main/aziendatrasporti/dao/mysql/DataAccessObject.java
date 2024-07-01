package ingweb.main.aziendatrasporti.dao.mysql;

import java.sql.Connection;
import java.util.ArrayList;

public abstract class DataAccessObject {

    private static String[] columns;
    private static Connection connection;
    private static String tableName;

    public static void setColumns(String[] x) { columns=x; }
    public static String[] getColumns() { return columns; }

    public static void setConnection(Connection x) { connection=x; }
    public static Connection getConnection() { return connection; }

    public static void setTableName(String x) { tableName=x; }
    public static String getTableName() { return tableName; }

    public static ArrayList<String[]> select() {

        var query="select * from "+tableName;
        var res=MySqlQueryManager.getResult(connection, query);
        return MySqlQueryManager.asList(res, columns);
    }

    public static ArrayList<String[]> select(int code) {

        var query="select * from "+tableName+" where "+columns[0]+"="+code;
        var res=MySqlQueryManager.getResult(connection, query);
        return MySqlQueryManager.asList(res, columns);
    }

    public static int lastCode() {

        var query="select max("+columns[0]+") as "+columns[0]+" from "+tableName;
        var res=MySqlQueryManager.getResult(connection, query);
        var resList=MySqlQueryManager.asList(res, new String[]{columns[0]});
        return Integer.parseInt(resList.get(0)[0]);}

    public static void insert(Object[] data) {

        if (data.length!=columns.length) System.out.println("Data and length are different");
        var query="insert into "+tableName+" (";
        for (var i=0; i<columns.length-1; i++) query+=columns[i]+", ";
        query+=columns[columns.length-1]+") values (";
        for (var i=0; i<columns.length-1; i++) query+="?, ";
        query+="?)";
        MySqlQueryManager.execute(connection, query, data);
    }

    public static void remove(int code) {

        var query="update "+tableName+" set "+columns[columns.length-1]+"=? where "+columns[0]+"="+code;
        MySqlQueryManager.execute(connection, query, new Object[]{1});
    }

    public static void update(Object[] data) {

        if (data.length!=columns.length) System.out.println("Data and length are different");
        var query="update "+tableName+" set ";
        for (var i=1; i<columns.length-2; i++) query+=columns[i]+"='"+data[i]+"', ";
        query+=columns[columns.length-2]+"='"+data[data.length-2]+"' where "+columns[0]+"="+data[0];
        MySqlQueryManager.execute(connection, query, data);
    }
}
