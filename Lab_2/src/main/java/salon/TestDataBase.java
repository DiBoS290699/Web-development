package salon;

import java.sql.SQLException;
import java.util.ArrayList;

//Класс тестировщик основных функций базы данных
public class TestDataBase {

    private ArrayList<Service> testServices;    //Тестовые услуги
    private ArrayList<Visit> testVisits;        //Тестовые посещения
    private ArrayList<Client> testClients;      //Тестовые клиенты
    private DataBase dataBase = new DataBase();     //Создание экземпляра базы данных

    public static void main(String[] args) throws SQLException{
        TestDataBase tdb = new TestDataBase();
        tdb.startTest();
    }

    //Запуск тестировщика
    public void startTest() throws SQLException {
        testServices = new ArrayList<Service>();
        for(int i = 1; i <= 10; ++i) {  //10 услуг от type1 до type10
            String type = "type" + Integer.toString(i);
            double price = 10*i;
            int count = 12 - i;
            double cost = price * count;
            Service service = new Service(type, price, count, cost);
            testServices.add(service);
        }

        testVisits = new ArrayList<Visit>();
        for(int i = 1; i <= 6; ++i) {   //6 визитов. 1 визит: type1-type5, 2 визит: type2-type6 .....
            ArrayList<Service> servs = new ArrayList<Service>();
            for(int p = 0; p < 5; ++p) {
                servs.add(testServices.get(i + p - 1));
            }
            testVisits.add(new Visit(i, servs));
        }

        testClients = new ArrayList<Client>();
        for(int i = 1; i <= 5; ++i) {   //5 клиентов. 1 клиент: 1 и 2 визиты, 2 клиент: 2 и 3 визиты .....
            String name = "name" + Integer.toString(i);
            String login = "login" + Integer.toString(i);
            String password = "password" + Integer.toString(i);
            ArrayList<Visit> viss = new ArrayList<Visit>();
            viss.add(testVisits.get(i - 1));
            viss.add(testVisits.get(i));
            testClients.add(new Client(i, name, login, password, viss));
        }

        dropTables();       //Удаление предыдущих таблиц
        createTables();     //Создание новых таблиц
        addClients();       //Добавление тестовых клиентов
        deleteService("type2");     //Удаление услуги с типом type2
        Client newClient = testClients.get(0);      //Создание копии первого клиента
        newClient.setId(7);                 //Изменение Id копии первого клиента
        updateClient(1);            //Обновление имени первого клиента в таблице
        insertInFile("test.txt", newClient);    //Сериализация клиента в файл
        insertFromFile("test.txt");             //Десериализация клиента из файла и запись его в таблицу
        isUser("login2", "password2");  //Проверка существования клиента с такими login и password
        findVisitsByClient(testClients.get(4));     //Вывод на консоль визиты пятого клиента, взятые из таблиц
        findServicesByVisit(testVisits.get(4));     //Вывод на консоль услуги пятого визита, взятые из таблиц
        showAllTables();    //Отображение всех таблиц в консоли
    }

    public void createTables() throws SQLException {
        dataBase.createTable();
    }

    public void addClients() throws SQLException{
        int numbClients = testClients.size();
        for(int i = 0; i < numbClients; ++i) {
            dataBase.insertClient(testClients.get(i));
        }
    }

    public void deleteService(String type) throws SQLException{
        dataBase.deleteService(type);
    }

    public void updateClient(int id) throws SQLException{
        dataBase.updateClientNameById(id, "updateClientName");
    }

    public void insertFromFile(String path) throws SQLException{
        dataBase.insertClientFromFile(path);
    }

    public void insertInFile(String path, Client client){
        dataBase.insertClientInFile(path, client);
    }

    public void isUser(String login, String password) throws SQLException{
        boolean isUser = dataBase.existClient(login, password);
        System.out.println("Does user exist?  " + isUser);
    }

    public void findVisitsByClient(Client client) throws SQLException{
        ArrayList<Visit> viss =  dataBase.findVisitsByClient(client);
        for(int i = 0; i < viss.size(); i++){
            System.out.print("Visit №" + Integer.toString(i) + ": " + viss.get(i) + '\n');
        }
    }

    public void findServicesByVisit(Visit visit) throws SQLException{
        ArrayList<Service> servs =  dataBase.findServicesByVisit(visit);
        for(int i = 0; i < servs.size(); i++){
            System.out.print("Service №" + Integer.toString(i) + ": " + servs.get(i) + '\n');
        }
    }

    public void showTableVisit() throws SQLException {
        dataBase.showTableVisit();
    }

    public void showTableService() throws SQLException {
        dataBase.showTableService();
    }

    public void showTableClient() throws SQLException {
        dataBase.showTableClient();
    }

    public void showTableVisitList() throws SQLException {
        dataBase.showTableVisitList();
    }

    public void showAllTables() throws SQLException {
        dataBase.showTableClient();
        dataBase.showTableVisitList();
        dataBase.showTableVisit();
        dataBase.showTableService();
    }

    public void dropTables() throws SQLException {
        dataBase.dropTables();
    }
}
