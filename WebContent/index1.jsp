<%-- 
    Document   : index
    Created on : Nov 20, 2012, 3:10:55 PM
    Author     : Administrator
--%>

<%@page import="javax.servlet.RequestDispatcher"%>
<%@page import="util.ApplicationConstants"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
       
          <%
        RequestDispatcher rd=getServletContext().getRequestDispatcher("/app");
        request.setAttribute(ApplicationConstants.UIACTION_NAME, ApplicationConstants.UIACTION_DISPLAY_STOCKS);
        rd.forward(request, response);
        %>
       
    </body>
</html>
