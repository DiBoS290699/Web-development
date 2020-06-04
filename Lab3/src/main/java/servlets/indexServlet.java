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


//Производим мапинг сервлета
@WebServlet("/index")
public class indexServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext context = getServletContext();
        String topLabel = (String) context.getAttribute("top_label");

        HttpSession session = request.getSession();
        if (session!= null) {
            Client user = (Client) session.getAttribute("user");
            if(user != null) {
                session.removeAttribute("user");
            }
        }

        if (topLabel==null) {
            context.setAttribute("top_label", "SIGN IN");
        }

        String path = "/index.jsp";
        RequestDispatcher requestDispatcher = context.getRequestDispatcher(path);
        requestDispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request == null) {
            System.out.println("Null request");
            return;
        }

        String login = request.getParameter("login");
        String password = request.getParameter("password");

        System.out.println(login);
        System.out.println(password);

        boolean userExists = false;
        try {
            DataBase dataSource = new DataBase();
            userExists = dataSource.existClient(login, password);
            System.out.println(userExists);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ServletContext context = getServletContext();
        if(userExists) {
            context.setAttribute("top_label", "SIGN IN");
            Client client = new Client();
            client.setLogin(login);
            client.setPassword(password);

            HttpSession session = request.getSession();
            session.setAttribute("user", client);
            String path = request.getContextPath() + "/result";
            try {
                response.sendRedirect(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            context.setAttribute("top_label", "USER NOT FOUND");
            String path = "/index.jsp";
            RequestDispatcher requestDispatcher = context.getRequestDispatcher(path);
            try {
                requestDispatcher.forward(request, response);
            } catch (ServletException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}
