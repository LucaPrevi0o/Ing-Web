package ingweb.main.aziendatrasporti.dao.mysql.dao;

import ingweb.main.aziendatrasporti.dao.ClientDAO;
import ingweb.main.aziendatrasporti.dao.mysql.MySqlQueryManager;
import ingweb.main.aziendatrasporti.mo.ClientCompany;
import ingweb.main.aziendatrasporti.mo.Worker;

import java.sql.Connection;
import java.util.ArrayList;

public class MySqlClientDAO implements ClientDAO {

    private final Connection connection;
    private final String[] allColumns={"nome", "ragione_sociale", "sede", "responsabile", "deleted"};

    private String parseParams() {

        var s="";
        for (var i=0; i<allColumns.length-1; i++) s+=(allColumns[i]+", ");
        return s+allColumns[allColumns.length-1];
    }

    private String addParams() {

        var s="";
        for (var i=0; i<allColumns.length-1; i++) s+="?, ";
        return s+"?";
    }

    public MySqlClientDAO(Connection connection) { this.connection=connection; }

    public ArrayList<ClientCompany> findAll() {

        var clients=new ArrayList<ClientCompany>();
        var query="select * from azienda_cliente";
        var res=MySqlQueryManager.getResult(connection, query); //execute query on the database
        var resList=MySqlQueryManager.asList(res, allColumns); //parse results
        for (var item: resList) { //add every element of the result set as new clientCompany

            //parse obtained result as correct data type
            var clientCompany=new ClientCompany(item[0], item[1], item[2], item[3], item[4].equals("1"));
            if (!clientCompany.isDeleted()) clients.add(clientCompany); //add clientCompany to the result list if not set as deleted
        }

        return clients; //return list of valid services
    }

    public ClientCompany findBySocialReason(String socialReason) {

        var query="select * from azienda_cliente where ragione_sociale='"+socialReason+"'";
        var res=MySqlQueryManager.getResult(connection, query); //execute query on the database
        var resList=MySqlQueryManager.asList(res, allColumns); //parse results
        if (!resList.isEmpty()) {

            if (resList.size()>1) return null; //return null error value if list has more than 1 element
            var item=resList.get(0); //get first (and only) instance of the list and return its value
            return new ClientCompany(item[0], item[1], item[2], item[3], item[4].equals("1"));
        }
        return null; //return null if none is found
    }

    public void addClient(ClientCompany client) {

        var query="insert into azienda_cliente ("+parseParams()+") values ("+addParams()+")";
        MySqlQueryManager.execute(connection, query, client.asList());
    }

    //remove account from database (setting the logic deletion true)
    public void removeClient(ClientCompany clientCompany) {

        var query="update azienda_cliente set deleted=1 where (ragione_sociale = ?)"; //empty query
        MySqlQueryManager.execute(connection, query, new Object[]{clientCompany.getSocialReason()}); //execute update with parameters
    }

    public void updateClient(ClientCompany worker) {

        var params=worker.asList();
        var query="update azienda_cliente set ";
        for (var i=0; i<params.length-1; i++) query+=(allColumns[i]+"=?, ");
        query+=(allColumns[allColumns.length-1]+"=? where (ragione_sociale = '"+worker.getSocialReason()+"')");
        MySqlQueryManager.execute(connection, query, params);
    }
}
