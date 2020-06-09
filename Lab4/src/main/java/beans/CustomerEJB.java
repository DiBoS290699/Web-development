package beans;

import beautySalon.Client;
import dataSource.DataBase;

import javax.ejb.Stateless;
import java.sql.SQLException;

@Stateless
public class CustomerEJB {

    public Client validateClientLogin(String login, String password) throws SQLException, ClassNotFoundException {
        DataBase dataSource = new DataBase();
        if(dataSource.existClient(login, password)){
            return dataSource.getClient(login, password);
        } else {
            return null;
        }
    }

    public void deleteService(String type_srevice) throws SQLException, ClassNotFoundException {
        DataBase dataSource = new DataBase();
        dataSource.deleteService(type_srevice);
    }

}