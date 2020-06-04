package dataSource;

import beautySalon.Client;
import beautySalon.Service;
import beautySalon.Visit;


import java.sql.*;
import java.util.ArrayList;

public class DataBase {
    private final String url = "jdbc:postgresql://localhost:5432/postgres";
    private final String login = "postgres";
    private final String password = "qwerty";

    // Подключение к базе данных
    public Connection getConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(url, login, password);
        }
        catch (Throwable er) {
            er.printStackTrace();
        }
        return null;
    }

    //Создание таблиц
    public void createTable() throws SQLException {
        Connection connect = getConnection();
        connect.setAutoCommit(false);   //Отключение функции "авто-коммит"
        PreparedStatement state = connect.prepareStatement("create table client(id integer primary key, " +
                "name text, login text, password text)");
        state.execute();
        state = connect.prepareStatement("create table visitList(client_id integer, visit_id integer)");
        state.execute();
        state = connect.prepareStatement("create table visit(id integer, type_service text)");
        state.execute();
        state = connect.prepareStatement("create table service" +
                "(type text primary key, price numeric, count integer, cost numeric)");
        state.execute();
        connect.commit();
        connect.close();
    }

    // Проверка существования услуги (Service) в таблице service с таким же type как в передаваемом классе
    public boolean existService(Service serv) throws SQLException {
        Connection connect = getConnection();
        PreparedStatement state = connect.prepareStatement("select exists(select type from service where type = ?)");
        state.setString(1, serv.getType());
        ResultSet rs = state.executeQuery();
        rs.next();
        connect.close();
        return rs.getBoolean(1);
    }

    // Проверка существования визита (Visit) в таблице visit с таким же id как в передаваемом классе
    public boolean existVisit(Visit vis) throws SQLException {
        Connection connect = getConnection();
        PreparedStatement state = connect.prepareStatement("select exists(select id from visit where id = ?)");
        state.setInt(1, vis.getId());
        ResultSet rs = state.executeQuery();
        rs.next();
        connect.close();
        return rs.getBoolean(1);
    }

    // Добавление клиента в таблицу client и данных о нём в остальные таблицы
    public void insertClient(Client client) throws SQLException{
        Connection connect = getConnection();
        connect.setAutoCommit(false);   //Отключение функции "авто-коммит"
        PreparedStatement stateClient = connect.prepareStatement("insert into client" +
                "(id, name, login, password) values(?, ?, ?, ?)");
        stateClient.setInt(1, client.getId());
        stateClient.setString(2, client.getName());
        stateClient.setString(3, client.getLogin());
        stateClient.setString(4, client.getPassword());
        stateClient.execute();

        PreparedStatement stateVisitList = connect.prepareStatement("insert into visitList( client_id, visit_id) values(?, ?)");
        PreparedStatement stateVisit = connect.prepareStatement("insert into visit(id, type_service) values(?, ?)");
        PreparedStatement stateService = connect.prepareStatement("insert into service(type, price, count, cost) values(?,?,?,?)");

        int sizeVisits = client.getSizeVisits();
        for(int i = 0; i < sizeVisits; i++){    // Добавление посещений салона красоты клиентом
            Visit vis = client.getVisit(i);
            stateVisitList.setInt(2, vis.getId());
            stateVisitList.setInt(1, client.getId());
            stateVisitList.execute();

            if(!existVisit(vis)){   // Проверка на существование этого визита в таблице Visit
                int sizeServices = vis.getSizeServices();
                for(int j = 0; j < sizeServices; j++){  // Добавление услуг, купленных клиентом в данном посещении салона
                    stateVisit.setInt(1, vis.getId());
                    Service service = vis.getService(j);
                    stateVisit.setString(2, service.getType());
                    stateVisit.execute();
                    if(!existService(service)) {    // Проверка на существование этой услуги в таблице Service
                        stateService.setString(1, service.getType());
                        stateService.setDouble(2, service.getPrice());
                        stateService.setInt(3, service.getCount());
                        stateService.setDouble(4, service.getCost());
                        stateService.execute();
                    }
                }
            }
            connect.commit();
        }
        connect.close();
    }

    //Добавление клиента из сериализованного файла
    public void insertClientFromFile(String path) throws SQLException {
        insertClient(Client.readFile(path));
    }

    //Добавление клиента в сериализованный файл
    public void insertClientInFile(String path, Client client) {
        Client.writeFile(path, client);
    }

    //Проверка на существование клиента с таким же Id
    public boolean existClient(String login, String password) throws SQLException{
        Connection connect = getConnection();
        PreparedStatement state = connect.prepareStatement("select exists(select id from client where login = ? and password = ?)");
        state.setString(1, login);
        state.setString(2, password);
        ResultSet rs = state.executeQuery();
        rs.next();
        connect.close();
        return rs.getBoolean(1);
    }

    //Нахождение посещений клиента в таблице по его Id
    public ArrayList<Visit> findVisitsByClient(Client client) throws SQLException{
        Connection connect = getConnection();
        PreparedStatement state = connect.prepareStatement("select visit_id from visitList where client_id = ?");
        state.setInt(1, client.getId());
        ResultSet rs = state.executeQuery();
        ArrayList result = new ArrayList();
        while(rs.next()) {
            Visit vis = new Visit();
            vis.setId(rs.getInt("visit_id"));
            ArrayList<Service> servs = findServicesByVisit(vis);
            vis.setServices(servs);
            result.add(vis);
        }
        connect.close();
        return result;
    }

    //Нахождение купленных услуг в таблице на данном посещении
    public ArrayList<Service> findServicesByVisit(Visit visit) throws SQLException {
        Connection connect = getConnection();
        PreparedStatement state = connect.prepareStatement("select * from service where type in " +
                "(select type_service from visit where id = ?)");
        state.setInt(1, visit.getId());
        ResultSet rs = state.executeQuery();
        ArrayList<Service> result = new ArrayList<Service>();
        while(rs.next()){
            Service service = new Service(rs.getString("type"), rs.getDouble("price"),
                    rs.getInt("count"), rs.getDouble("cost"));
            result.add(service);
        }
        connect.close();
        return result;
    }

    //Удаление услуги по его типу
    public void deleteService(String type) throws SQLException{
        Connection connect = getConnection();
        PreparedStatement state = connect.prepareStatement("delete from service where type = ?");
        state.setString(1, type);
        state.execute();
        connect.close();
    }

    //Обновление имени клиента в таблице по его Id
    public void updateClientNameById(int id, String newName) throws SQLException{
        Connection connect = getConnection();
        PreparedStatement state = connect.prepareStatement("update client set name = ? where id = ?");
        state.setString(1, newName);
        state.setInt(2, id);
        state.execute();
        connect.close();
    }

    //Удаление клиента и его списка посещений из таблиц по его Id
    public void deleteClientById(int id) throws SQLException{
        Connection connect = getConnection();
        PreparedStatement stateVisitList = connect.prepareStatement("delete from visitlist where client_id == ?");
        PreparedStatement stateClient = connect.prepareStatement("delete from client where id == ?");
        stateVisitList.setInt(1, id);
        stateClient.setInt(1, id);
        stateVisitList.execute();
        stateClient.execute();
        connect.close();
    }

    // Отображение в консоли таблицы VisitList
    public void showTableVisitList() throws SQLException {
        Connection connect = getConnection();
        PreparedStatement state = connect.prepareStatement("select * from visitlist");
        ResultSet rs = state.executeQuery();
        System.out.println("The VisitList table:");
        System.out.println("Client_Id          Visit_Id");
        while(rs.next()){
            System.out.println(rs.getInt("client_id") + "          " + rs.getInt("visit_id"));
        }
    }

    // Отображение в консоли таблицы Visit
    public void showTableVisit() throws SQLException {
        Connection connect = getConnection();
        PreparedStatement state = connect.prepareStatement("select * from visit");
        ResultSet rs = state.executeQuery();
        System.out.println("The Visit table:");
        System.out.println("Id          Type_service");
        while(rs.next()){
            System.out.println(rs.getInt("id") + "          " + rs.getString("type_service"));
        }
    }

    public Client getClient(String login, String password) throws SQLException, ClassNotFoundException{
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("select id, name from client where login=? and password=?");
        statement.setString(1, login);
        statement.setString(2, password);
        ResultSet rs = statement.executeQuery();
        rs.next();
        ArrayList <Visit> visits = new ArrayList<>();
        Client tempUser = new Client(rs.getInt("id"), rs.getString("name"), login, password, visits);
        visits = findVisitsByClient(tempUser);
        tempUser.setVisits(visits);
        connection.close();
        return tempUser;
    }

    // Отображение в консоли таблицы Service
    public void showTableService() throws SQLException {
        Connection connect = getConnection();
        PreparedStatement state = connect.prepareStatement("select * from service");
        ResultSet rs = state.executeQuery();
        System.out.println("The Service table:");
        System.out.println("Type        Price       Count       Cost");
        while(rs.next()){
            System.out.println(rs.getString("type") + "          "
                    + rs.getDouble("price") + "          "
                    + rs.getInt("count") + "          "
                    + rs.getDouble("cost"));
        }
    }

    // Отображение в консоли таблицы Client
    public void showTableClient() throws SQLException {
        Connection connect = getConnection();
        PreparedStatement state = connect.prepareStatement("select * from client");
        ResultSet rs = state.executeQuery();
        System.out.println("The Client table:");
        System.out.println("Id          Name        Login       Password");
        while(rs.next()){
            System.out.println(rs.getInt("id") + "          " + rs.getString("name")
            + "          " + rs.getString("login") + "          " + rs.getString("password"));
        }
    }

    //Удаление таблиц
    public void dropTables() throws SQLException{
        Connection connect = getConnection();
        PreparedStatement state = connect.prepareStatement("drop table client, service, visit, visitList");
        state.execute();
        connect.close();
    }
}
