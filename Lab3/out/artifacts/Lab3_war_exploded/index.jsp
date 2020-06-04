<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%!
  private String userIsFound(ServletContext application) {
    String topLabel = (String)application.getAttribute("top_label");
    if (topLabel.equals("USER NOT FOUND"))
      return "background: #ffacac;";
    else
      return "background: #c2c2c2;";
  }
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>index</title>
  <link rel="stylesheet" href="styles\index.css">
</head>
<body>
<div class="container">
  <section class="logo">
    <div class="logo_title">
      <span>${top_label}</span>
    </div>
  </section>
  <section class="form_container">
    <form action="index" method="post">
      <div class="inputs_wrapper">
        <div class="input_field clearfix">
          <div class="a">
            <input type="text" name="login" placeholder="Enter username" required style="<%=userIsFound(application)%>">
            <span></span>
          </div>
        </div>
        <div class="input_field clearfix">
          <div class="a">
            <!--                        <input type="password" name="password" placeholder="Enter password" required style="<%=userIsFound(application)%>">-->
            <input type="password" name="password" placeholder="Enter password" required style="<%=userIsFound(application)%>">
          </div>
        </div>
      </div>
      <button>LOGIN</button>
    </form>
  </section>
</div>
</body>
</html>