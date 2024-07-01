package ingweb.main.aziendatrasporti.dao.mysql.dao;

import ingweb.main.aziendatrasporti.dao.ClientDAO;
import ingweb.main.aziendatrasporti.dao.mysql.MySqlQueryManager;
import ingweb.main.aziendatrasporti.mo.mo.ClientCompany;
import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;

public class MySqlClientDAO implements ClientDAO {

    private final Connection connection;
    private final String[] allColumns={"codice", "nome", "ragione_sociale", "sede", "nome_responsabile", "cf_responsabile", "datanascita_responsabile", "numerotelefono_responsabile", "deleted"};

    private String parseParams() {

        var s="";
        for (var i=1; i<allColumns.length-1; i++) s+=(allColumns[i]+", ");
        return s+allColumns[allColumns.length-1];
    }

    private String addParams() {

        var s="";
        for (var i=1; i<allColumns.length-1; i++) s+="?, ";
        return s+"?";
    }

    public MySqlClientDAO(Connection connection) { this.connection=connection; }

    public ClientCompany getClient(String[] item) {

        return new ClientCompany(Integer.parseInt(item[0]), item[1], item[2], item[3], item[4], item[5], Date.valueOf(item[6]), item[7], item[8].equals("1"));
    }

    public ArrayList<ClientCompany> findAll() {

        var clients=new ArrayList<ClientCompany>();
        var query="select * from azienda_cliente";
        var res=MySqlQueryManager.getResult(connection, query); //execute query on the database
        var resList=MySqlQueryManager.asList(res, allColumns); //parse results
        for (var item: resList) { //add every element of the result set as new clientCompany

            //parse obtained result as correct data type
            var clientCompany=getClient(item);
            if (!clientCompany.isDeleted()) clients.add(clientCompany); //add clientCompany to the result list if not set as deleted
        }

        return clients; //return list of valid services
    }

    public ClientCompany findByCode(int code) {

        var query="select * from azienda_cliente where codice='"+code+"'";
        var res=MySqlQueryManager.getResult(connection, query); //execute query on the database
        var resList=MySqlQueryManager.asList(res, allColumns); //parse results
        if (resList.size()!=1) return null;
        var item=resList.get(0);
        var clientCompany=getClient(item);
        return (clientCompany.isDeleted() ? null : clientCompany);
    }

    public ClientCompany findBySocialReason(String socialReason) {

        var query="select * from azienda_cliente where ragione_sociale='"+socialReason+"'";
        var res=MySqlQueryManager.getResult(connection, query); //execute query on the database
        var resList=MySqlQueryManager.asList(res, allColumns); //parse results
        if (resList.size()!=1) return null;
        var item=resList.get(0);
        var clientCompany=getClient(item);
        return (clientCompany.isDeleted() ? null : clientCompany);
    }

    public void addClient(ClientCompany client) {

        var query="insert into azienda_cliente ("+parseParams()+") values ("+addParams()+")";
        MySqlQueryManager.execute(connection, query, client.asList());
    }

    //remove account from database (setting the logic deletion true)
    public void removeClient(ClientCompany clientCompany) {

        var query="update azienda_cliente set deleted=1 where (codice = ?)"; //empty query
        MySqlQueryManager.execute(connection, query, new Object[]{clientCompany.getCode()}); //execute update with parameters
    }

    public void updateClient(ClientCompany clientCompany) {

        var params=clientCompany.asList();
        var query="update azienda_cliente set ";
        for (var i=0; i<params.length-1; i++) query+=(allColumns[i]+"=?, ");
        query+=(allColumns[allColumns.length-1]+"=? where (codice = '"+clientCompany.getCode()+"')");
        MySqlQueryManager.execute(connection, query, params);
    }
}
