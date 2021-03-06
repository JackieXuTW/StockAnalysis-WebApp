<%-- 
    Document   : displayFilterDB
    Created on : 2014/1/30, 下午 06:06:21
    Author     : Leon
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.sql.*"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>display filter db</title>
        <link rel="stylesheet" type="text/css" href="../CSS/OTTZ_general.css" />
        
    </head>
    <body>  
        <!--置入網頁開頭 用於logo等物件-->
        <%@include file="/header.jsp"%> 
        <!--置入網頁選單--> 
        <%@include file="/mainMenu.jsp" %> 
        <hr style="size:4px">
        
        <h1>Filter Training</h1>
      
        <sql:setDataSource driver="com.microsoft.sqlserver.jdbc.SQLServerDriver" user="user" password="shawn" 
                           url="jdbc:sqlserver://localhost;databaseName=ottzDB"
                           scope="session"
                           var="myDS"/>
  
     
        <sql:query var="rs" dataSource="jdbc/ottzDB" scope="page">
            SELECT * FROM [ottzDB].[dbo].[FilterTrain] ORDER BY createTime DESC
        </sql:query>
            
        <table id="mytable" class="showTable">
            <tr>
                <th>資料建立時間</th>
                <th>紀錄編號</th>
                <th>最佳期數</th>
                <th>最佳濾嘴比例</th>
                <th>建模開始日期</th>
                <th>建模結束日期</th>
                <th>建模股票代號</th>
                <th>刪除</th>
                <th>測試參數</th>
            </tr>
            <c:forEach var="row" items="${rs.rows}">
                <tr>
                    <td>${row['createTime']}</td>
                    <td>${row['dataKey']}</td>
                    <td>${row['bestN']}</td>
                    <td>${row['bestK']}</td>
                    <td>${row['StartDate']}</td>
                    <td>${row['EndDate']}</td>
                    <td>${row['StockSymbol']}</td>
                    <td>
                        <a href="../OperateDBFilter?action=deleteFilterTrain&id=${row['dataKey']}" onclick='return confirm("確定刪除此筆記錄?")'>刪除</a>
                    </td>
                    <td>
                        <a href='../testing/filterTesting_yahoofinance.jsp?sendFilter=true&bestN=${row['bestN']}&bestK=${row['bestK']}&testStockSymbol=${row['StockSymbol']}'>測試參數</a>
                    </td>
                </tr>
            </c:forEach>
        </table>
        
        <!--置入網頁註腳 權責聲明等物件-->    
        <%@include file="/footer.jsp" %>
    </body>
</html>
