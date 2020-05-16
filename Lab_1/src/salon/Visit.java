package salon;


import java.io.*;
import java.util.ArrayList;
import java.util.TreeSet;

public class Visit implements Serializable{
    private int account;
    private ArrayList<Service> services;

    public Visit() { }

    public Visit(int account) {
        this.account = account;
    }

    public Visit(int account, ArrayList<Service> services) {
        this.account = account;
        this.services = services;
    }

    public int getAccount() {
        return this.account;
    }

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
        StringBuilder temp = new StringBuilder("Account: " + this.account + "\nServices: ");
        for (Service service : this.services) {
            temp.append(service.toString()).append(' ');
        }
        return temp.toString();
    }
}
