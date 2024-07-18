package ingweb.main.aziendatrasporti.dao;

import ingweb.main.aziendatrasporti.mo.mo.ServiceBill;

public interface BillDAO {

    public int findLastCode();
    public void addBill(ServiceBill serviceBill);
}
