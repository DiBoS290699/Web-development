package servlets;

import beautySalon.Client;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;


@WebServlet("/result.xml")
public class ResultXMLServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext servletContext = getServletContext();
        HttpSession session = request.getSession();
        Client client = (Client) session.getAttribute("user");
        if(client == null) {
            RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher("/error.jsp");
        }

        JAXBContext context = null;
        try {
            context = JAXBContext.newInstance(Client.class);
            // Создание объекта который выполнит сериализацию
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            // Запишем System.out
            marshaller.marshal(client, System.out);
            response.setHeader("Contend-disposition", "attachment; filename=result.xml");
            response.setContentType("application/xml");
            // Конвертируем xml в string
            StringWriter writer = new StringWriter();
            marshaller.marshal(client, writer);
            PrintWriter respWriter = response.getWriter();
            respWriter.println(writer.toString());
            writer.close();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}
