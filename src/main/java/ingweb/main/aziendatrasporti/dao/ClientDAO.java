package ingweb.main.aziendatrasporti.dao;

import ingweb.main.aziendatrasporti.mo.mo.ClientCompany;
import java.util.ArrayList;

public interface ClientDAO {

    public ArrayList<ClientCompany> findAll();
    public ClientCompany findByCode(int code);
    public ClientCompany findBySocialReason(String socialReason);
    public int findLastCode();
    public void addClient(ClientCompany service);
    public void removeClient(ClientCompany service);
    public void updateClient(ClientCompany service);
}
