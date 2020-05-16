package salon;


import java.io.*;
import java.util.ArrayList;
import java.util.TreeSet;

public class Visit implements Serializable{
    private int id;
    private ArrayList<Service> services;

    public Visit() { }

    public Visit(int account) {
        this.id = account;
    }

    public Visit(int account, ArrayList<Service> services) {
        this.id = account;
        this.services = services;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) { this.id = id; }

    public void setServices(ArrayList<Service> servs) { this.services = servs; }

    public Service getService(int index) { return this.services.get(index); }

    public int getSizeServices() {
        return this.services.size();
    }

    public void addService(int index, Service service) {
        this.services.add(index, service);
    }

    public void removeService(int index) {
        this.services.remove(index);
    }

    public void writeFile(String path) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
            oos.writeObject(this.services);
        } catch (Throwable er) {
            er.printStackTrace();
        }
    }

    public void readFile(String path) {
        try (ObjectInputStream oin = new ObjectInputStream(new FileInputStream(path))) {
            this.services = (ArrayList<Service>) oin.readObject();
        } catch(Throwable er) {
            er.printStackTrace();
        }
    }

    public void sortServices() {
        TreeSet<Service> set = new TreeSet<>(services);
        services = new ArrayList<>(set);
    }

    public String toString() {
        StringBuilder temp = new StringBuilder("Account: " + this.id + "\nServices: ");
        for (Service service : this.services) {
            temp.append(service.toString()).append(' ');
        }
        return temp.toString();
    }
}
