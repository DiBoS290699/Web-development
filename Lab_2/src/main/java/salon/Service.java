package salon;

import java.io.*;

public class Service implements Serializable, Comparable<Service> {
    private String type;
    private double price;
    private int count;
    private double cost;

    public Service(String type, double pr, int cou, double cos)
        throws ExceptionInInitializerError {
        if(pr < 0 || cou < 0 || cos < 0) {
            throw new ExceptionInInitializerError("Error initializing the object of the salon.Service");
        }

        this.type = type;
        this.price = pr;
        this.count = cou;
        this.cost = cos;
    }

    public String getType() { return this.type; }

    public void setType(String ser) { this.type = ser; }

    public double getPrice() { return this.price; }

    public void setPrice(double pr) { this.price = pr; }

    public int getCount() { return this.count; }

    public void setCount(int cou) { this.count = cou; }

    public double getCost() { return this.cost; }

    public void setCost(double cos) { this.cost = cos; }

    public static void writeFile(String path, Service service) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
            oos.writeObject(service);
        } catch (Throwable er) {
            er.printStackTrace();
        }
    }

    public static Service readFile(String path) {
        try (ObjectInputStream oin = new ObjectInputStream(new FileInputStream(path))) {
            Service service = (Service) oin.readObject();
            return service;
        } catch(Throwable er) {
            er.printStackTrace();
            return null;
        }
    }

    public int compareTo(Service serv) {
        return this.type.compareTo(serv.getType());
    }

    public String toString() {
        return "(Type: " + type + "; Price: " + price + "; Count: " + count + "; Cost: " + cost + ')';
    }

}
