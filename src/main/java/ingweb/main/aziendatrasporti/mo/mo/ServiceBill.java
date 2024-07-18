package ingweb.main.aziendatrasporti.mo.mo;

import ingweb.main.aziendatrasporti.mo.ModelObject;

public class ServiceBill extends ModelObject {

    private Service service;
    private String paymentMethod;
    private float amount;

    public Object[] asList() { return new Object[]{this.service, this.paymentMethod, this.amount}; }
    public Object[] data() { return new Object[]{this.getCode(), this.service, this.paymentMethod, this.amount, this.isDeleted()}; }
    public String display() { return this.service+" (€"+this.amount+" - "+this.paymentMethod+")"; }
    public String toString() { return this.amount+", "+this.paymentMethod+" - "+this.service; }

    public float getAmount() { return amount; }
    public void setAmount(float amount) { this.amount=amount; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod=paymentMethod; }

    public Service getService() { return service; }
    public void setService(Service service) { this.service=service; }
}
