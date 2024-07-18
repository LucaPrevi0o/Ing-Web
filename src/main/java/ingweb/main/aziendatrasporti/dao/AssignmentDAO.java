package ingweb.main.aziendatrasporti.dao;

import com.mysql.cj.xdevapi.Client;
import ingweb.main.aziendatrasporti.mo.mo.Assignment;
import ingweb.main.aziendatrasporti.mo.mo.ClientCompany;
import ingweb.main.aziendatrasporti.mo.mo.Worker;

import java.util.ArrayList;

public interface AssignmentDAO {

    public ArrayList<Assignment> findAll();
    public ArrayList<Assignment> findAllByWorker(Worker worker);
    public ArrayList<Assignment> findAllByClientCompany(ClientCompany clientCompany);
    public ArrayList<Assignment> findAllCompleted();
    public Assignment findByCode(int code);
    public int findLastCode();
    public void addAssignment(Assignment assignment);
    public void removeAssignment(Assignment assignment);
    public void updateAssignment(Assignment assignment);
    public void completeAssignment(Assignment assignment);
}
