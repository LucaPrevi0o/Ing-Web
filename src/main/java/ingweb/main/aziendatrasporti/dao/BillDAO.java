package ingweb.main.aziendatrasporti.dao;

import ingweb.main.aziendatrasporti.mo.mo.ServiceBill;
import java.util.ArrayList;

public interface BillDAO {

    public int findLastCode();
    public void addBill(ServiceBill serviceBill);
    public void removeBill(ServiceBill serviceBill);
    public ServiceBill findByCode(int code);
    public ArrayList<ServiceBill> findAll();
}
