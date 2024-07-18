package ingweb.main.aziendatrasporti.mo.mo;

import ingweb.main.aziendatrasporti.mo.ModelObject;

public class ServiceBill extends ModelObject {

    private Service service;
    private String clientBankCoords;
    private String destinationBankCoords;
    private float amount;

    public Object[] asList() { return new Object[]{this.getCode(), this.service, this.clientBankCoords, this.destinationBankCoords, this.amount, this.isDeleted()}; }
    public Object[] data() { return new Object[]{this.service, this.amount, this.clientBankCoords, this.destinationBankCoords}; }
    public String display() { return this.service+" (€"+this.amount+")"; }
    public String toString() { return this.amount+" ("+this.clientBankCoords+" -> "+this.destinationBankCoords+") - "+this.service; }

    public ServiceBill(Service service, String clientBankCoords, String destinationBankCoords, float amount) {

        this.service=service;
        this.clientBankCoords=clientBankCoords;
        this.destinationBankCoords=destinationBankCoords;
        this.amount=amount;
    }

    public ServiceBill(int code, Service service, String clientBankCoords, String destinationBankCoords, float amount, boolean deleted) {

        this.setCode(code);
        this.service=service;
        this.clientBankCoords=clientBankCoords;
        this.destinationBankCoords=destinationBankCoords;
        this.amount=amount;
        this.setDeleted(deleted);
    }

    public float getAmount() { return amount; }
    public void setAmount(float amount) { this.amount=amount; }

    public String getClientBankCoords() { return clientBankCoords; }
    public void setClientBankCoords(String clientBankCoords) { this.clientBankCoords=clientBankCoords; }

    public String getDestinationBankCoords() { return destinationBankCoords; }
    public void setDestinationBankCoords(String destinationBankCoords) { this.destinationBankCoords=destinationBankCoords; }

    public Service getService() { return service; }
    public void setService(Service service) { this.service=service; }
}
