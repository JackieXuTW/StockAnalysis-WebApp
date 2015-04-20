<%-- 

--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>MA Main Training</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/CSS/OTTZ_general.css" />
    </head>
    <body>        
        <!--置入網頁開頭 用於logo等物件-->
        <%@include file="/header.jsp"%> 
        <!--置入網頁選單--> 
        <%@include file="/mainMenu.jsp" %> 
        
        <h1 align="center">Training Result - Moving Average</h1>   
        <table>
            <tr><td>
            移動平均法是先設定一個短期數及長期數，如果是以天為單位的話，短線進出業界經常使用的是5天及10天，即將收盤價每5天及10天平均一次，
            當短天期突破長天期時則為買進訊號，反之，當短天期跌破長天期時為賣出訊號。此功能主要是計算出最佳的短期數及長期數給你參考。
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
                <th style="color: #EF4339;">最佳的短期數</th>                
                <th style="color: #EF4339;">最佳的長期數</th>
            </tr>
            <tr>
                <td>${Bests}</td>
                <td>${Bestl}</td>                   
            </tr>                       
        </table>
            <table>
            <tr><td> 
                *如果最佳短期數與最佳長期數為0，則表示系統試過範圍內所有的參數組合，皆無法找到買賣點                
                </td></tr>
            </table>
            
        <form action='OperatorDBMA_servlet' method="post">
            <input type='hidden' name='action' value='addMaTrain'/>
            <input type='hidden' name='bestS' value='${Bests}'>
            <input type='hidden' name='bestL' value='${Bestl}'>
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
