package beautySalon;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.*;
import java.util.ArrayList;


@XmlRootElement
public class Client implements Serializable {

    private int id;
    private String name;
    private String login;
    private String password;
    private ArrayList<Visit> visits;

    public Client(){}

    public Client(int id, String name, String login, String password, ArrayList<Visit> visits) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.password = password;
        this.visits = visits;
    }

    public int getId() {
        return id;
    }   // Гетер поля Id

    public void setId(int id) {
        this.id = id;
    }   // Сетер поля Id

    public String getName() {
        return name;
    }   // Гетер поля name

    public void setName(String name) {
        this.name = name;
    }   // Сетер поля name

    public String getLogin() {
        return login;
    }   // Гетер поля login

    public void setLogin(String login) {
        this.login = login;
    }   // Сетер поля login

    @XmlTransient
    public String getPassword() {
        return password;
    }   // Гетер поля password

    public void setPassword(String password) {
        this.password = password;
    }   // Сетер поля password

    public void setVisits(ArrayList<Visit> visits) {
        this.visits = visits;
    }

    public ArrayList<Visit> getVisits() {
        return visits;
    }

    public int getSizeVisits() {
        return this.visits.size();
    }   // Гетер количества посещений

    public void addVisit(int index, Visit visit) {
        this.visits.add(index, visit);
    }   // Добавление визита по индексу

    public void removeVisit(int index) {
        this.visits.remove(index);
    }   // Удаление визита по индексу

    public Visit getVisit(int index) {
        return this.visits.get(index);
    }   // Гетер визита по индексу

    //Чтение из файла сериализованного клиента
    public static Client readFile(String path) {
        try (ObjectInputStream oin = new ObjectInputStream(new FileInputStream(path))) {
            Client client = (Client) oin.readObject();
            return client;
        } catch(Throwable er) {
            er.printStackTrace();
            return null;
        }
    }

    //Запись в файл сериализованного клиента
    public static void writeFile(String path, Client client) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
            oos.writeObject(client);
        } catch (Throwable er) {
            er.printStackTrace();
        }
    }

}
