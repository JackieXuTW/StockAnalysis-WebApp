<%-- 
    Document   : displayFilter_train
    Created on : 2014/4/4, 下午 12:53:37
    Author     : Leon

    the view used from FilterTrain_Servlet.java
    attr:
        Bestn, Bestk
    param:
        trainStockSymbol, trainStartDate, trainEndDate
        
--%>

<%@page import="org.luxion.ottzRserve.BeanFilter_train"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Filter Main Training</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/CSS/OTTZ_general.css" />
    </head>
    <body>        
        <!--置入網頁開頭 用於logo等物件-->
        <%@include file="/header.jsp"%> 
        <!--置入網頁選單--> 
        <%@include file="/mainMenu.jsp" %> 
        
        <h1 align="center">Training Data - Filter Rule</h1>   
        <table>
            <tr><td>
            濾嘴法則是先設定一個比率及期數，例如比率為0.2，期數為5，假設是以每日的收盤價做計算，則每5日計算出一個平均值，
            如果漲勢超過20%則買進，跌勢超過20%則賣出。我們的Training功能主要是計算出最佳的濾嘴比例及期數給你參考。
                </td></tr>
        </table>
        <table class="showTable">
            <tr>
                <th>股票代號</th><th>建模開始日期</th><th>建模結束日期</th>                
            </tr>
            <tr>
                <td>${param.trainStockSymbol}</td>
                <td>${trainStartDatetime}</td>
                <td>${trainEndDatetime}</td> 
            </tr>
            <tr>
                <th style="color: #EF4339;">最佳期數*</th>                
                <th style="color: #EF4339;">最佳濾嘴比例*</th>
            </tr>
            <tr>
                <td>${Bestn}</td>
                <td>${Bestk}</td>                   
            </tr>                       
        </table>
            <table>
            <tr><td> 
                *如果最佳期數與最佳濾嘴比例為0，則表示系統試過範圍內所有的參數組合，皆無法找到買賣點                
                </td></tr>
            </table>
            
        <form action='OperateDBFilter' method="post">
            <input type='hidden' name='action' value='addFilterTrain'/>
            <input type='hidden' name='bestN' value='${Bestn}'>
            <input type='hidden' name='bestK' value='${Bestk}'>
            <input type="hidden" name="trainStockSymbol" value="${param.trainStockSymbol}">
            <input type="hidden" name="trainStartDate" value="${trainStartDatetime}">
            <input type="hidden" name="trainEndDate" value="${trainEndDatetime}">
            <input type='submit' value='寫入此筆資料'>
            <input type="button" value='回上頁' onclick="history.go(-1); " />
        </form>
        <!--置入網頁註腳 權責聲明等物件-->    
        <%@include file="/footer.jsp" %>
    </body>
</html>
