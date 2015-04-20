<%-- 
    Document   : cor
    Created on : 2014/3/20, 下午 09:56:08
    Author     : USER
--%>

<%@page import="org.rosuda.REngine.*"%>
<%@page import="org.rosuda.REngine.Rserve.*"%>
<%@page import="java.lang.reflect.Array"%>
<%@page import="java.util.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8" %>

<%request.setCharacterEncoding("utf-8");%>

<!DOCTYPE html>
<html>
    <head>
        <style type="text/css">
            @import url("style.css");
            #CorTitle{
            position: relative;
            left: 41%;
            top: 30px;
            font-family: Georgia, "Times New Roman", Times, serif;
            font-size: 36px;
            color: #006699;
            }
        </style>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Correlation</title>
    </head>
    <body>
        <div id="CorTitle">The Correlation of Industry of Taiwan Stock Exchange Market</div>
        <jsp:useBean id="correlation" scope="page" class="RserveBean.Correlation"/>
        <% correlation.excuteCor(); %>
        <jsp:getProperty name="correlation" property="corTable" />
    </body>
</html>
