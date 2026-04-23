package itmo.lab;

import itmo.lab.data.Worker;
import itmo.lab.data.Organization;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Коллекция работников предприятия.
 *
 * Хранит работников в TreeMap, где ключ - id работника.
 * Обеспечивает уникальность id и названий организаций.
 */
public class WorkersCollection {

    private TreeMap<Integer, Worker> workers = new TreeMap<>();
    private final LocalDateTime creationTime;
    private String fileName;

    public WorkersCollection() {
        this.creationTime = LocalDateTime.now();
    }

    public WorkersCollection(String fileName) {
        this.creationTime = LocalDateTime.now();
        this.fileName = fileName;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public String getFileName() {
        return fileName;
    }

    public int size() {
        return workers.size();
    }

    public void setWorkers(TreeMap<Integer, Worker> workers) {
        this.workers = workers;
    }

    public TreeMap<Integer, Worker> getWorkers() {
        return workers;
    }

    public boolean insert(int id, Worker element) {
        if (workers.containsKey(id)) {
            return false;
        }

        Organization newOrg = element.getOrganization();
        if (newOrg != null && newOrg.getFullName() != null && !newOrg.getFullName().isBlank()) {
            String newName = newOrg.getFullName().trim().toLowerCase();
            for (Worker existingWorker : workers.values()) {
                Organization existingOrg = existingWorker.getOrganization();
                if (existingOrg != null && existingOrg.getFullName() != null) {
                    String existingName = existingOrg.getFullName().trim().toLowerCase();
                    if (existingName.equals(newName)) {
                        throw new IllegalArgumentException(
                                "Организация с именем '" + newOrg.getFullName() + "' уже существует в коллекции"
                        );
                    }
                }
            }
        }

        workers.put(id, element);
        return true;
    }

    public boolean insert(Worker element) {
        int id = element.getId();
        if (id <= 0) {
            id = generateId();
            element.setId(id);
        }
        return insert(id, element);
    }

    public boolean remove(int id) {
        return workers.remove(id) != null;
    }

    public int removeLower(int id) {
        List<Integer> toRemove = new ArrayList<>();
        for (int key : workers.keySet()) {
            if (key < id) {
                toRemove.add(key);
            }
        }
        for (int key : toRemove) {
            workers.remove(key);
        }
        return toRemove.size();
    }

    public int removeGreaterKey(int id) {
        List<Integer> toRemove = new ArrayList<>();
        for (int key : workers.keySet()) {
            if (key > id) {
                toRemove.add(key);
            }
        }
        for (int key : toRemove) {
            workers.remove(key);
        }
        return toRemove.size();
    }

    public boolean update(int id, Worker worker) {
        if (!workers.containsKey(id)) {
            return false;
        }
        
        Organization newOrg = worker.getOrganization();
        if (newOrg != null && newOrg.getFullName() != null && !newOrg.getFullName().isBlank()) {
            String newName = newOrg.getFullName().trim().toLowerCase();
            Worker oldWorker = workers.get(id);
            String oldOrgName = (oldWorker != null && oldWorker.getOrganization() != null 
                    && oldWorker.getOrganization().getFullName() != null)
                    ? oldWorker.getOrganization().getFullName().trim().toLowerCase()
                    : null;
            
            for (Worker existingWorker : workers.values()) {
                if (existingWorker.getId() == id) continue;
                Organization existingOrg = existingWorker.getOrganization();
                if (existingOrg != null && existingOrg.getFullName() != null) {
                    String existingName = existingOrg.getFullName().trim().toLowerCase();
                    if (existingName.equals(newName) && !newName.equals(oldOrgName == null ? "" : oldOrgName)) {
                        throw new IllegalArgumentException(
                                "Организация с именем '" + newOrg.getFullName() + "' уже существует в коллекции"
                        );
                    }
                }
            }
        }
        
        worker.setId(id);
        workers.put(id, worker);
        return true;
    }

    public Worker get(int id) {
        return workers.get(id);
    }

    public boolean containsKey(int id) {
        return workers.containsKey(id);
    }

    public List<Worker> getAll() {
        return new ArrayList<>(workers.values());
    }

    public void clear() {
        workers.clear();
    }

    public boolean isEmpty() {
        return workers.isEmpty();
    }

    public int generateId() {
        if (workers.isEmpty()) return 1;
        return workers.lastKey() + 1;
    }

    public List<LocalDateTime> getUniqueStartDates() {
        List<LocalDateTime> dates = new ArrayList<>();
        for (Worker w : workers.values()) {
            if (!dates.contains(w.getStartDate())) {
                dates.add(w.getStartDate());
            }
        }
        return dates;
    }
}