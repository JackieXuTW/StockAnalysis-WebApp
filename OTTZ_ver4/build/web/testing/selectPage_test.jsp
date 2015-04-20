<%-- 
    Document   : selectPage_testing
    Created on : 2014/4/18, 上午 12:32:08
    Author     : Leon
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Technical Analysis Method</title>
        
        <script type="text/javascript" src="../jquery.min.js"></script>

        <style type="text/css">
            @import url("../CSS/OTTZ_general.css");
            @import url("../CSS/formwizard.css");
        </style>
    </head>
    <body>
         <!--置入網頁開頭 用於logo等物件-->
        <%@include file="/header.jsp"%> 
        <!--置入網頁選單--> 
        <%@include file="/mainMenu.jsp" %>    
        <div class="TopTitle">
            <h1 style="text-align: center">Tesing</h1>
        </div>
        <table align="center">
            <tr><td>
            此功能主要是將你想要測式的方法、設定的短期數、長期數套用至設定的測試期間，最後以模擬投資的方式給你參考此參數組合的效益。
              </td></tr>
        </table>
        
            
        <div class="selectionMainTest">
            <form action="../TestingSelectGuide" method="post" name="selectTestingForm" id="selectTestingForm">
            <!--第一頁-->
            <fieldset class="sectionwrap" style="width: 800px">
                <legend>選擇測試方法</legend>
                <table border="0" class="selectionTable">  
                    <tr>
                        <td>Testing Method</td>
                    </tr>
                    <tr>
                        <td>
                            <select name="method">
                                <option value="Filter">Filter</option>
                                <option value="MA">MA</option>                                
                                <option value="RSI">RSI</option>
                            </select>                            
                        </td>
                    </tr>
                </table>
            </fieldset>
            
            <!--第二頁-->
            <fieldset class="sectionwrap" style="width: 800px">
                <legend>選擇資料來源</legend>
                <table border="0" class="selectionTable">  
                    <tr>
                        <td>Data Source</td>  
                    </tr>
                    <tr>
                        <td>
                            <select name="data">
                                <option value="yahooFinance">Daily</option>
                                <option value="SqlServer">Real-Time Data</option>  
                            </select> 
                        </td>
                    </tr>
                    
                </table>
            </fieldset>

            <!--最終頁 確認與送出-->
            <fieldset class="sectionwrap" style="width: 800px">
                <legend>最終確認</legend>
                <table class="selectionTable">                   
                    <tr>
                        <td colspan="100">
                            <input type="submit" value="送出">
                            <input type="reset" name="Reset" id="button" value="清除">
                        </td>
                    </tr>                
                </table>
            </fieldset>
            </form>
        </div> 
        <!--置入網頁註腳 權責聲明等物件-->    
        <%@include file="/footer.jsp" %>
    </body>
</html>
