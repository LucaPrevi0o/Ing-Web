package ingweb.main.aziendatrasporti.dao;

import ingweb.main.aziendatrasporti.mo.mo.Assignment;
import ingweb.main.aziendatrasporti.mo.mo.Worker;

import java.util.ArrayList;

public interface AssignmentDAO {

    public ArrayList<Assignment> findAll();
    public Assignment findByCode(int code);
    public ArrayList<Assignment> findAllByWorker(Worker worker);
    public int findLastCode();
    public void addAssignment(Assignment assignment);
    public void removeAssignment(Assignment assignment);
}
