package ingweb.main.aziendatrasporti.dao;

import ingweb.main.aziendatrasporti.mo.mo.ServiceBill;
import java.util.ArrayList;

public interface BillDAO {

    public int findLastCode();
    public void addBill(ServiceBill serviceBill);
    public ArrayList<ServiceBill> findAll();
}
