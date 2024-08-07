package ingweb.main.aziendatrasporti.dao;

import ingweb.main.aziendatrasporti.mo.mo.Assignment;
import ingweb.main.aziendatrasporti.mo.mo.ClientCompany;
import ingweb.main.aziendatrasporti.mo.mo.Service;
import ingweb.main.aziendatrasporti.mo.mo.Worker;

import java.util.ArrayList;

public interface AssignmentDAO {

    public ArrayList<Assignment> findAll();
    public ArrayList<Assignment> findAllByWorker(Worker worker);
    public ArrayList<Assignment> findAllByClientCompany(ClientCompany clientCompany);
    public ArrayList<Assignment> findAllCompleted();
    public Assignment findByCode(int code);
    public Assignment findByService(Service service);
    public int findLastCode();
    public void addAssignment(Assignment assignment);
    public void deleteAssignment(Assignment assignment);
    public void removeAssignment(Assignment assignment);
    public void updateAssignment(Assignment assignment);
}
