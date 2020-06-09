package beans;

import beautySalon.Client;


import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.sql.SQLException;
import java.util.ArrayList;


@ManagedBean
@SessionScoped
public class CustomerBean {
    private final String MESSAGE_USER_NOT_FOUND = "notfound";
    private final String MESSAGE_USER_IS_FOUND = "found";
    private final String MESSAGE_SQL_ERROR = "sqlerror";

    private final String LBL_LOGIN = "ВХОД";
    private final String LBL_NOTFOUND = "ПОЛЬЗОВАТЕЛЬ НЕ НАЙДЕН";

    @EJB
    private final CustomerEJB customerEJB;
    private Client client;
    private String login;
    private String password;
    private String message;
    private String topLabel = LBL_LOGIN;


    public CustomerBean(){
        this.customerEJB = new CustomerEJB();
    }

    public String getTopLabel() {
        return topLabel;
    }

    public void setTopLabel(String topLabel) {
        this.topLabel = topLabel;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Client getClient(){
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String validateClientLogin() {
        try {
            client = customerEJB.validateClientLogin(login, password);

            if (client == null) {
                topLabel = LBL_NOTFOUND;
                return message = MESSAGE_USER_NOT_FOUND;
            }
            else {
                topLabel = LBL_LOGIN;
                return message = MESSAGE_USER_IS_FOUND;
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
            return message = MESSAGE_SQL_ERROR;
        }
    }

    public void downloadXML() {
        JAXBContext context = null;
        FacesContext ctx = FacesContext.getCurrentInstance();
        HttpServletResponse resp = (HttpServletResponse) ctx.getExternalContext().getResponse();
        System.out.println("a");
        try {
            context = JAXBContext.newInstance(Client.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            // Write to System.out
            m.marshal(client, System.out);
            resp.setHeader("Content-disposition","attachment; filename=result.xml");
            resp.setContentType("application/xml");
            // Convert xml to string
            StringWriter writer = new StringWriter();
            m.marshal(client, writer);
            PrintWriter respWriter = resp.getWriter();
            respWriter.println(writer.toString());
            writer.close();
            ctx.responseComplete();
        } catch (JAXBException | IOException e) {
            e.printStackTrace();

        }
    }

    public String logOut() {
        client = null;
        return "logout";
    }

    public void deleteService(String type_service) {
        try {
            customerEJB.deleteService(type_service);
            updateClient();
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    public void updateClient() throws SQLException, ClassNotFoundException {
        client = customerEJB.validateClientLogin(login, password);
    }

}