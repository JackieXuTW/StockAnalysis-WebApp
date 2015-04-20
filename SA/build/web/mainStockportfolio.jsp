<%-- 
    Document   : index
    Created on : 2013/7/8, 下午 10:49:45
    Author     : User
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
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>StockPortfolio</title>
<style type="text/css">
            @import url("Rserve1001CSS.css");
            @import url("formwizard.css");
        </style>
</style>
</head>


<body>
    <br>
    <div id="selectedTitle"> Stock Portfolio Method    </div>
    
    <!-- 以下為顯示選取名稱與日期 -->
    <% 
        String symbol[]=request.getParameterValues("stockSymbol");         
        String trainStartDate =request.getParameter("trainStartDate"); 
        String trainEndDate = request.getParameter("trainEndDate");
        String testStartDate = request.getParameter("testStartDate");
        String testEndDate = request.getParameter("testEndDate");
        String shortSelling = request.getParameter("shortSelling");
        String freq = request.getParameter("freq");
        String money = request.getParameter("money");
    %>
            
    <table border="0" align="center" class="table_showselect">
        <tr>
            <th colspan="100">選擇的股票</th>
        </tr>
        <tr>
            <% 
                if (symbol!=null) 
                {   			
                    int size=Array.getLength(symbol); 
                            for (int i=0;i<size;i++) 
                              { 
                                     out.println("<td>"+symbol[i]+"</td>"); 
                              } 
                }
                else
                {out.println("<td>目前沒有選取任何股票</td>");}
            %>
        </tr>
 </table>
<table align="center" class="table_showselect">
        <tr>
            <th colspan="100">選取的日期</th>
        </tr>
        <tr>
            <%
                if(trainStartDate!=null && trainEndDate !=null)
                {
                    out.println("<td>建模資料開始日期: " + trainStartDate + "</td>");
                    out.println("<td>建模資料結束日期: " + trainEndDate + "</td>");
                }
            %>
        </tr>
        <tr>
            <%
                if(testStartDate!=null && testEndDate !=null)
                {
                    out.println("<td>測試資料開始日期: " + testStartDate + "</td>");
                    out.println("<td>測試資料結束日期: " + testEndDate + "</td>");
                }
            %>
        </tr>
    </table>
        <table align="center" class="table_showselect">
        <tr>
            <%
                out.println("<td>是否融資融券?: " + shortSelling + "</td>");
                out.println("<td>資料計算單位: " + freq + "</td>");
                out.println("<td>投資金額: " + money + "萬</td>");
                %>
        </tr>
        </table>

    <!-- 以下為Rserve使用local資料庫 -->

    <jsp:useBean id="stockPortfolio" scope="page" class="RserveBean.beanStock0126"/>
        
        <br>
        <jsp:setProperty name="stockPortfolio" property="symbol" param="stockSymbol"/>
        <jsp:setProperty name="stockPortfolio" property="trainStartDate" param="trainStartDate"/>
        <jsp:setProperty name="stockPortfolio" property="trainEndDate" param="trainEndDate"/>
        <jsp:setProperty name="stockPortfolio" property="testStartDate" param="testStartDate"/>
        <jsp:setProperty name="stockPortfolio" property="testEndDate" param="testEndDate"/>
        <jsp:setProperty name="stockPortfolio" property="shortSelling" param="shortSelling"/>
        <jsp:setProperty name="stockPortfolio" property="freq" param="freq"/>
        <jsp:setProperty name="stockPortfolio" property="money" param="money"/>
        
        
        <% if (symbol!=null) 
        {
            if (stockPortfolio.excuteStockportfolio().equals("NoError"))
            {
        %> 
      
        <jsp:getProperty name="stockPortfolio" property="norTable" />
        <br><hr><br>
        <jsp:getProperty name="stockPortfolio" property="ccmTable" />
        <br><hr><br>
        <jsp:getProperty name="stockPortfolio" property="simTable" />
        <!-- plot圖檔顯示
            stockPictureTag會直接傳回html的img標籤 如
            <img src="./plotimages/stockPortfolio/20130820101808.jpg">
            stockPictureName會傳回img的路徑名稱 如
            ./plotimages/stockPortfolio/20130820101808.jpg
        -->
        <!-- 823因為plot不明的輸出延遲問題, 棄用直接叫出圖片的stockPictureTag 改為按鈕事件來取用stockPictureName
            <jsp:getProperty name="stockPortfolio" property="stockPictureTag" /><br><br>
        -->
        
        
        <img name="img1" src="" style="display:block; margin:auto;"><br><br>
        <input type="button" value="以測試資料為主顯示走勢圖" name="plot1" onclick="changeImg()" class="imgbutton">
        
        <script type="text/javascript">
            function changeImg(){
                document.images['img1'].src="<jsp:getProperty name='stockPortfolio' property='stockPictureName' />";
                 }
        </script>
        <% 
                }
            else
            { 
             // out.print(stockPortfolio.PrintErrorMsg()); %>
             <div id='selectbody'><jsp:getProperty name="stockPortfolio" property="printErrorMsg" /></div>
            <%}
        }
        else
               {out.println("<div id='selectbody'>請至上一頁選取欲分析的股票</div>");}
        %>
</body>
</html>



