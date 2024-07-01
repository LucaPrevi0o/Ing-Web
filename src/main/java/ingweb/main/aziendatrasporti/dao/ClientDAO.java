package ingweb.main.aziendatrasporti.dao;

import ingweb.main.aziendatrasporti.mo.mo.ClientCompany;
import java.util.ArrayList;

public interface ClientDAO {

    public ArrayList<ClientCompany> findAll();
    public ClientCompany findBySocialReason(String socialReason);
    public ClientCompany findByCode(int code);
    public void addClient(ClientCompany client);
    public void removeClient(ClientCompany clientCompany);
    public void updateClient(ClientCompany client);
}
