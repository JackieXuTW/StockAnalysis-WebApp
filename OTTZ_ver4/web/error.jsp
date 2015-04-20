<%-- 
    Document   : error
    Created on : 2014/4/16, 上午 01:08:47
    Author     : Leon
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" isErrorPage="true"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Error Page</title>
    </head>
    <body>
        <h1>Error Message</h1>
        <%
            out.println(exception);
        %>
    </body>
</html>
