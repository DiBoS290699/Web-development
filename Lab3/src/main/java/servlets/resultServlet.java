package servlets;


import beautySalon.Client;
import dataSource.DataBase;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/result")
public class resultServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String path = "/result.jsp";
        ServletContext servletContext = getServletContext();
        HttpSession session = request.getSession();

        Client client = (Client) session.getAttribute("user");
        if(client == null){
            RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher("/error.jsp");
            requestDispatcher.forward(request, response);
        }
        try {
            DataBase dataSource = new DataBase();
            client = dataSource.getClient(client.getLogin(), client.getPassword());
            System.out.println(client);
            session.setAttribute("user", client);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(path);
            try {
                requestDispatcher.forward(request, response);

            }catch (ServletException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}
