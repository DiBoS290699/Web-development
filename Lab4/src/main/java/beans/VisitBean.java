package beans;

import beautySalon.Service;


import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import java.io.IOException;
import java.sql.SQLException;

@ManagedBean
@SessionScoped
public class VisitBean {
    private VisitEJB visitEJB = new VisitEJB();
    private int ID;
    private String type;
    private double price;
    private int count;
    private double cost;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void insertService(int ID) {
        this.ID = ID;
        System.out.println(ID);
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("insert.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void typeValidator(FacesContext facesContext, UIComponent uiComponent, Object o){
        String toValidate;
        try {
            toValidate = o.toString();
            if (toValidate.equals("")) {
                FacesMessage facesMessage = new FacesMessage("Тип услуги должнен быть непустым.");
                throw new ValidatorException(facesMessage);
            }
        } catch (ValidatorException e) {
            e.printStackTrace();
        }
    }

    public void priceValidator(FacesContext facesContext, UIComponent uiComponent, Object o) {
        double toValidate;
        toValidate = Double.parseDouble(o.toString());
        if (toValidate <= 0) {
            FacesMessage facesMessage = new FacesMessage("Цена должна быть положительной и не равна нулю");
            throw new ValidatorException(facesMessage);
        }
    }

    public void countValidator(FacesContext facesContext, UIComponent uiComponent, Object o) {
        int toValidate;
        toValidate = Integer.parseInt(o.toString());
        if (toValidate <= 0) {
            FacesMessage facesMessage = new FacesMessage("Количество должно быть положительным и не равно нулю.");
            throw new ValidatorException(facesMessage);
        }
    }

    public void costValidator(FacesContext facesContext, UIComponent uiComponent, Object o) {
        double toValidate;
        toValidate = Double.parseDouble(o.toString());
        if (toValidate <= 0) {
            FacesMessage facesMessage = new FacesMessage("Общая стоимость должна быть положительна и не равна нулю.");
            throw new ValidatorException(facesMessage);
        }
    }

    public String addService(CustomerBean customerBean){
        Service service = new Service(type, price, count, cost);
        try {
            visitEJB.addService(service, ID);
            customerBean.updateClient();
        } catch (SQLException e) {
            e.printStackTrace();
            return "error";
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return "success";
    }



}