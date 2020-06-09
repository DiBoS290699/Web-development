package beans;


import beautySalon.Service;
import dataSource.DataBase;

import javax.faces.application.FacesMessage;
import javax.faces.validator.ValidatorException;
import java.sql.SQLException;

public class VisitEJB {
    public void addService(Service service, int ID) throws SQLException {
        DataBase dataBase = new DataBase();
        if(dataBase.existService(service)){
            FacesMessage facesMessage = new FacesMessage("Такая услуга уже существует.");
            throw new ValidatorException(facesMessage);
        }
        dataBase.insertService(service, ID);
    }

}