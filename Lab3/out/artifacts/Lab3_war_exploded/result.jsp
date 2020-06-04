<%@ page import="beautySalon.Client" %>
<%@ page import="beautySalon.Visit" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="beautySalon.Service" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Client client = (Client) request.getSession().getAttribute("user");
    if (client == null) {
        response.setHeader("Error", "5;url=index.jsp");
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>result</title>
    <link rel="stylesheet" href="styles/result.css">
</head>

<body>
<header>
    <div class="container">
        <div class="title">
            <h1>Welcome to the salon, <%=client.getName()%></h1>
        </div>
    </div>
</header>

<div class="container conOne">
    <a href="index.jsp">Log out</a>
    <a href="result.xml">Download XML</a>
</div>
<div class="container conTwo">
    <table class="data_table">
        <caption>Your services</caption>
        <tr>

            <th>Id</th>
            <th>Type</th>
            <th>Price</th>
            <th>Count</th>
            <th>Cost</th>
        </tr>
        <%
            ArrayList<Visit> visits = client.getVisits();
            for(Visit v : visits){
                for(int i = 0; i < v.getSizeServices(); i++){
                    Service service = v.getService(i);
        %>

        <tr>
            <td><%=v.getId()%></td>
            <td><%=service.getType()%></td>
            <td><%=service.getPrice()%></td>
            <td><%=service.getCount()%></td>
            <td><%=service.getCost()%></td>
        </tr>

        <%
                }
            }
        %>
    </table>
</div>

</body>
</html>