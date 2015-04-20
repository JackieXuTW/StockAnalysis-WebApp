<%-- 
    Document   : dailySelect
    Created on : 2013/9/12, 上午 05:28:32
    Author     : Leon
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="java.sql.*,java.util.*,java.text.*"%>
<%!
	java.sql.Connection conn = null;
%>
<%
//建立DB連線 與撈取需要的資料
    request.setCharacterEncoding("UTF-8");
    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();

    //conn = DriverManager.getConnection("jdbc:sqlserver://1.34.193.83;databaseName=Stock","user","shawn");
    conn = DriverManager.getConnection("jdbc:sqlserver://Localhost;databaseName=Stock","user","shawn");
    //取出stock名單 
    Statement statStockInfo = conn.createStatement();
    String sqlStockInfo = "SELECT Symbol, Name, Industry, onMarketDate, Style FROM Sym_Indus WHERE  (Symbol NOT IN ('$TWT')) ORDER BY Industry, Symbol";
    ResultSet rsStockInfo = statStockInfo.executeQuery(sqlStockInfo);
    //取出每日收盤價資料中的所有日期列表　追加參數scroll sesitive才可自由移動指標(預設是forward only)
    //Statement statDailyCloseDate = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
    //String sqlDailyCloseDate = "SELECT DISTINCT DP.Date FROM StockClosePrice DP ORDER BY DP.Date";
    //ResultSet rsDailyCloseDate = statDailyCloseDate.executeQuery(sqlDailyCloseDate);
    
    
%>
<!DOCTYPE html>
<html>
<head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Stock Porfolio Select Page</title>    
        <script type="text/javascript" src="jquery.min.js"></script> 
        <script type="text/javascript" src="formwizard.js"></script> 
        <script language="javascript" type="text/javascript">
            //form to form物件
             var myform=new formtowizard({
                    formid: 'stockportfolioForm',
                    persistsection: true,
                    revealfx: ['slide', 500],             
            })
        </script>
        
        <script type="text/javascript" src="jquery.min.js"></script>
        <script language="javascript" type="text/javascript">
        //選擇參數的onChange事件 
        function changeSelect(selectID){
            $("#" + selectID + "_check").html(
                                $("#" + selectID).find(":selected").text()
                              );
        }
        </script>

        <script type="text/javascript"> 
        //判斷結束日期是否大於開始日期
         function checkdate() {    
            var s1 = document.getElementById("trainStartDate").value; 
            var s2 = document.getElementById("trainEndDate").value;
            var s3 = document.getElementById("testStartDate").value;
            var s4 = document.getElementById("testEndDate").value;
            var TRsDate = new Date(s1); 
            var TReDate = new Date(s2);
            var TEsDate = new Date(s3); 
            var TEeDate = new Date(s4); 
            
            if((TRsDate > TReDate) || (TEsDate > TEeDate)){
                if (TRsDate > TReDate){
                    alert(TRsDate > TReDate);
                    alert("資料無法送出，因為建模期間的開始日大於結束日");
                    return false;
                }
            alert("資料無法送出，因為測試期間的開始日大於結束日");
            return false;
            }
            return true;
         }
    </script> 

        <style type="text/css">
            @import url("Rserve1001CSS.css");
            @import url("formwizard.css");
        </style>
</head>
    <body style="width: 65%;margin: auto;">
<!-- 顯示股票選取選單 -->
    <div id="selectionTitle">Stock Portfolio Method</div>
    <table><tr><td><div class="goindex"><a href='/SA/index.html'>回首頁</a></div></td></tr></table>
    <div id="selectionMain">       
    <form action="mainStockportfolio.jsp" method="post" name="stockportfolioForm" id="stockportfolioForm" onsubmit="return checkdate()">
        <!--第一頁 選擇建模日期-->
        <fieldset class="sectionwrap" style="width: 800px">
            <legend>輸入建模資料日期</legend>
            <table border="0" class="table1">  
                <tr>
                    <td>建模開始日期 YYYY-MM-DD</td>
                    <td>
                    <%--
                                 <select name="trainStartDate" id="trainStartDate" onchange="changeSelect('trainStartDate')">
                                    <%
                                             rsDailyCloseDate.beforeFirst();
                                             while (rsDailyCloseDate.next())
                                            {     
                                                out.println("<Option Value=" + rsDailyCloseDate.getString(1) + ">" + rsDailyCloseDate.getString(1) + "</option>");                   
                                            }                                                       
                                    %>
                                </select>
                                 --%>
                                 <input type ="date" name="trainStartDate" id="trainStartDate" value = "2013-01-01" onchange="changeSelect('trainStartDate')">
                    </td>
                    <td>建模結束日期 YYYY-MM-DD</td>
                    <td>
                       <%--
                                    <select name="trainEndDate" id="trainEndDate" onchange="changeSelect('trainEndDate')">
                                        <%
                                                 rsDailyCloseDate.beforeFirst();
                                                 while (rsDailyCloseDate.next())
                                                {                   
                                                       out.println("<Option Value=" + rsDailyCloseDate.getString(1) + ">" + rsDailyCloseDate.getString(1) + "</option>");                   
                                                }                          
                                        %>
                                    </select>
                                    --%>
                                    <input type ="date" name="trainEndDate" id="trainEndDate" value = "2013-01-01" onchange="changeSelect('trainEndDate')">
                    </td>
                </tr>
            </table>
        </fieldset>
        
        <!--第二頁 選擇測試日期-->
        <fieldset class="sectionwrap" style="width: 800px">
            <legend>輸入測試資料日期</legend>
            <table border="0" class="table1">
                <tr>
                    <td>測試開始日期</td>
                    <td>
                       <%--    
                                <select name="testStartDate" id="testStartDate" onchange="changeSelect('testStartDate')">
                                    <%
                                             rsDailyCloseDate.beforeFirst();
                                             while (rsDailyCloseDate.next())
                                            {      
                                                out.println("<Option Value=" + rsDailyCloseDate.getString(1) + ">" + rsDailyCloseDate.getString(1) + "</option>");                   
                                            }                                                       
                                    %>
                                </select>
                                --%>
                                <input type ="date" name="testStartDate" id="testStartDate" value = "2013-01-01" onchange="changeSelect('testStartDate')">
                    </td>
                    <td>測試結束日期</td>
                    <td>
                       <%--
                                <select name="testEndDate" id="testEndDate" onchange="changeSelect('testEndDate')">
                                    <%
                                             rsDailyCloseDate.beforeFirst();
                                             while (rsDailyCloseDate.next())
                                            {                   
                                                   out.println("<Option Value=" + rsDailyCloseDate.getString(1) + ">" + rsDailyCloseDate.getString(1) + "</option>");                   
                                            }                          
                                    %>
                                </select>
                                --%>
                                <input type ="date" name="testEndDate" id="testEndDate" value = "2013-01-01" onchange="changeSelect('testEndDate')">
                    </td>
                </tr>
            </table>
        </fieldset>
        
        <!--第三頁 選擇股票-->
        <fieldset class="sectionwrap" style="width: 800px">
            <legend>選取分析股票</legend>
            <div id='content'>不知道該選什麼股票? 使用<a href='http://pchome.syspower.com.tw/choice/sto2' target='blank'>PChome快速選股</a>、<a href='http://tw.screener.finance.yahoo.net/screener/basic.html' target='blank'>Yahoo!奇摩選股</a>
                、<a href='http://www.cmoney.tw/screeners/' target='blank'>CMoney選股網</a>做參考<br>
            或以產業間的<a href='cor.jsp' target='blank'>相關係數</a>做參考<br>
            </div>         
                <input type="button" id="clearBtn" value="清除已選的股票">
            <script>
                $('#clearBtn').click(function(){
                    $('input[name=stockSymbol]').attr('checked', false);
                });
            </script>
            <div style=" height:550px;overflow:auto">
            <table border="0" class="table1">
            <tr>
                <th class="stockHead">股票代號</th>
                <th class="stockHead">股票名稱</th>
                <th class="stockHead">產業</th>
                <th class="stockHead">上市時間</th>
                <th class="stockHead">型態</th>
            </tr>
            <%
            //顯示stock清單
                    while (rsStockInfo.next())
                    {
                            out.println("<tr><td>");
                            out.println("<input type=checkbox name=stockSymbol value="+rsStockInfo.getString(1)+">"+rsStockInfo.getString(1)+"</td>");
                            out.println("<td>"+rsStockInfo.getString(2)+"</td>");
                            out.println("<td>"+rsStockInfo.getString(3)+"</td>");
                            out.println("<td>"+rsStockInfo.getString(4)+"</td>");
                            out.println("<td>"+rsStockInfo.getString(5)+"</td></tr>");
                    }

            %>
            </table>   
            </div>
        </fieldset>
        
        <!--最終頁 確認與送出-->
        <fieldset class="sectionwrap" style="width: 800px">
            <legend>其它設定</legend>
            <table class="table1">
                <tr>
                    <td>是否融資融券? </td>
                    <td>
                        <input type=radio name="shortSelling" value="y"> 是
                        <input type=radio name="shortSelling" value="n" checked> 否
                    <td>            
                </tr>
                <tr>
                    <td>以天、週或以月為單位? </td>
                    <td> 
                        <input type=radio name="freq" value="day" checked> 天
                        <input type=radio name="freq" value="week"> 週
                        <input type=radio name="freq" value="month"> 月                                     
                    <td> 
                </tr>
               <tr>
                    <td>投資金額(萬元)</td>
                    <td> 
                        <input type=text name="money" value="100">                              
                    <td> 
                </tr>  
                    <td colspan="100">
                        <input type="submit" value="送出">
                        <input type="reset" name="Reset" id="button" value="清除">
                    </td>
                </tr>                
            </table>
        </fieldset> 
    </form>
</div>
            
            
        <%
                //關閉JDBC connection
                rsStockInfo.close(); 
                statStockInfo.close();
                //rsDailyCloseDate.close();
                //statDailyCloseDate.close();
                conn.close();
        %>
</div>
</body>
</html>