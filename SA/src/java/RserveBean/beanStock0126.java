/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RserveBean;
/**
 *
 * @author YaTing
 */
import java.lang.reflect.Array;
import java.util.*;
import org.rosuda.REngine.*;
import org.rosuda.REngine.Rserve.*;

public class beanStock0126 { 
    private String[] stockSymbol;
    private String trainStartDate;
    private String trainEndDate;
    private String testStartDate;
    private String testEndDate;
    private String shortSelling;
    private String freq;
    private String style;
    private double money;
    
    
    
    private String[] Nor_stockName;
    private double Nor_expReturn;
    private double Nor_risk;    
    private double[] Nor_portfolio;
    private double[] Nor_sumReturn;
    private double Nor_change;
    
    private String[] CCM_stockName;
    private double CCM_expReturn;
    private double CCM_risk;    
    private double[] CCM_portfolio;
    private double[] CCM_sumReturn;
    private double CCM_change;
    
    private String[] SIM_stockName;
    private double SIM_expReturn;
    private double SIM_risk;    
    private double[] SIM_portfolio;
    private double[] SIM_sumReturn;
    private double SIM_change;
    
    private String stockPicName = ""; 
    
    private String[] ErrorMsg1 = null;
    private String[] ErrorMsg2 = null;
    private String[] ErrorMsg3 = null;
    private String[] ErrorMsg4 = null;
    
    public String excuteStockportfolio() 
    {
        try{
        RConnection c = new RConnection();
        REXP x;
        
        String fieldName = convertStringArrayToField(this.stockSymbol);
        String trainDataStatement = "Train<-getReturns(c(" + fieldName + "),freq='"+ this.freq +"',get='overlapOnly',start='" + this.trainStartDate + "', end='" + this.trainEndDate + "')";
        String trainDataStatementNoIndex = "Train1<-getReturns(c(" + fieldName.substring(8, fieldName.length()) + "),freq='"+ this.freq +"',get='overlapOnly',start='" + this.trainStartDate + "', end='" + this.trainEndDate + "')";
        String testDataStatement = "Test<-getReturns(c(" + fieldName.substring(8, fieldName.length()) + "),freq='"+ this.freq +"',get='overlapOnly',start='" + this.testStartDate + "', end='" + this.testEndDate + "')";
       
        c.eval("library(stockPortfolio)\n" +
                "\n" +
                "errormsg1<<-0\n" +
                "errormsg2<<-0\n" +
                "errormsg3<<-0\n" +
                "errormsg4<<-0\n" +
                "\n" +
                "\n" +
                "tryCatch({\n" +
                trainDataStatement + "\n" +
                trainDataStatementNoIndex +"\n" +
                testDataStatement + "\n" +
                "}, error = function(e) {\n" +
                "  errormsg1<<-conditionMessage(e)\n" +
                "})\n" +
                "\n" +
                "tryCatch({\n" +
                "Nor<-stockModel(Train1,Rf=0.0000552,model='none',get='overlapOnly',freq='"+ this.freq +"')\n" +
                "SIM<-stockModel(Train,Rf=0.0000552,model='SIM',get='overlapOnly',freq='"+ this.freq +"',index=1,shortSelling='"+ this.shortSelling +"')\n" +
                "CCM<-stockModel(Train1,Rf=0.0000552,model='CCM',get='overlapOnly',freq='"+ this.freq +"',shortSelling='"+ this.shortSelling +"')\n" +
                "}, error = function(e) {\n" +
                "  errormsg2<<-conditionMessage(e)\n" +
                "})\n" +
                "\n" +
                "tryCatch({\n" +
                "opNor<-optimalPort(Nor,Rf=0.0000552)\n" +
                "opCCM<-optimalPort(CCM,Rf=0.0000552)\n" +
                "opSIM<-optimalPort(SIM,Rf=0.0000552)\n" +
                "}, error = function(e) {\n" +
                "  errormsg3<<-conditionMessage(e)\n" +
                "})\n" +
                "\n" +
                "tryCatch({\n" +
                "tpNor <- testPort(Test, opNor)\n" +
                "tpCCM <- testPort(Test, opCCM)\n" +
                "tpSIM <- testPort(Test, opSIM)\n" +
                "}, error = function(e) {\n" +
                "  errormsg4<<-conditionMessage(e)\n" +
                "})");
        // Error處理
        x=c.eval("errormsg1");
        this.ErrorMsg1 = x.asStrings();
        x=c.eval("errormsg2");
        this.ErrorMsg2 = x.asStrings();
        x=c.eval("errormsg3");
        this.ErrorMsg3 = x.asStrings();
        x=c.eval("errormsg4");
        this.ErrorMsg4 = x.asStrings();
        
        if (ErrorHandle(ErrorMsg1,ErrorMsg2,ErrorMsg3,ErrorMsg4).equals("noError"))
        {
        //先取出變數(股票)名稱
        x = c.eval("names(opNor$X)");
        this.Nor_stockName = x.asStrings();
        //在R上將需要的資料組成list後 取出該list內容值
        c.eval("NorList = list(exp_return= opNor$R, risk=opNor$risk, portfolio=opNor$X, sum_return= tpNor$sumRet, change=tpNor$change)");
        x = c.eval("NorList");
        //轉成RList
        RList Nor_list = x.asList();        
        //取得expect return
        this.Nor_expReturn = Nor_list.at("exp_return").asDouble();
        //取得risk
        this.Nor_risk = Nor_list.at("risk").asDouble();
        //取得portfolio
        this.Nor_portfolio = Nor_list.at("portfolio").asDoubles();
        //取得summary return
        this.Nor_sumReturn = Nor_list.at("sum_return").asDoubles();
        //取得change
        this.Nor_change = Nor_list.at("change").asDouble();
        
        
        //model: CCM
        //先取出變數(股票)名稱
        x = c.eval("names(opCCM$X)");
        this.CCM_stockName = x.asStrings();
        //在R上將需要的資料組成list後 取出該list內容值
        c.eval("CcmList = list(exp_return= opCCM$R, risk=opCCM$risk, portfolio=opCCM$X, sum_return= tpCCM$sumRet, change=tpCCM$change)");
        x = c.eval("CcmList");
        //轉成RList
        RList Ccm_list = x.asList();        
        //取得expect return
        this.CCM_expReturn = Ccm_list.at("exp_return").asDouble();
        //取得risk
        this.CCM_risk = Ccm_list.at("risk").asDouble();
        //取得portfolio
        this.CCM_portfolio = Ccm_list.at("portfolio").asDoubles();
        //取得summary return
        this.CCM_sumReturn = Ccm_list.at("sum_return").asDoubles();
        //取得change
        this.CCM_change = Ccm_list.at("change").asDouble();
        
        
        //model: SIM
        //先取出變數(股票)名稱
        x = c.eval("names(opSIM$X)");
        this.SIM_stockName = x.asStrings();
        //在R上將需要的資料組成list後 取出該list內容值
        c.eval("SimList = list(exp_return= opSIM$R, risk=opSIM$risk, portfolio=opSIM$X, sum_return= tpSIM$sumRet, change=tpSIM$change)");
        x = c.eval("SimList");
        //轉成RList
        RList Sim_list = x.asList();        
        //取得expect return
        this.SIM_expReturn = Sim_list.at("exp_return").asDouble();
        //取得risk
        this.SIM_risk = Sim_list.at("risk").asDouble();
        //取得portfolio
        this.SIM_portfolio = Sim_list.at("portfolio").asDoubles();
        //取得summary return
        this.SIM_sumReturn = Sim_list.at("sum_return").asDoubles();
        //取得change
        this.SIM_change = Sim_list.at("change").asDouble();
        

        
        //繪圖
        //將系統時間轉字串如20130820101753 作為圖檔名稱
        String picName = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        //注意此處儲存路徑須依據執行主機更改
        c.eval("png(file = \"F:/webimg/SP/" + picName + ".png\",width = 1200, height = 600, units = \"px\")");
        this.stockPicName = picName;
        c.eval("plot(tpNor,col='#006BDC',lty=1, lwd=2)\n" +
                "lines(tpCCM, col='red', lty=1, lwd=2)\n" +
                "lines(tpSIM, col='green3',lty=1, lwd=2)\n" +
                "legend('topleft',c('Nor','CCM','SIM'), col=c('#006BDC','red','green3'), lty=1, lwd=2)");
        c.eval("dev.off()");
        c.eval("rm(list = ls())");
        
        
       //setStock到此結束 關閉R connection
       c.close();
       return "NoError";
        }
     else
        {
        c.close();
        return "Error!";
        }
    }catch(Exception e){
        return "錯誤說明"+e.getMessage();
    }
    }
    
    public void setSymbol(String[] symbol){
        this.stockSymbol = symbol;
    }
    
    public void setTrainStartDate(String trainStartDate){
        this.trainStartDate = trainStartDate;
    }
    
    public void setTrainEndDate(String trainEndDate){
        this.trainEndDate= trainEndDate;
    }
    
     public void setTestStartDate(String testStartDate){
        this.testStartDate = testStartDate;
    }
    
    public void setTestEndDate(String testEndDate){
        this.testEndDate= testEndDate;
    }
    
    public void setShortSelling(String shortSelling){
        this.shortSelling= shortSelling;
    }

    public void setFreq(String freq){
        this.freq= freq;
    }
    
    public void setMoney(String money){
        this.money= Double.parseDouble(money);
    }
        
    
    
    public String getNorTable()
    {
        String NorTable = "";
        NorTable = NorTable.concat("<table align='center' class='table_result'>");
        NorTable = NorTable.concat("<caption>Model: M-V</caption>");
        NorTable = NorTable.concat("<tr><td  width='220px' align='center'>預期的報酬率</td><td colspan='100'>" + formatDecimal2(this.Nor_expReturn) + "</td></tr>");
        NorTable = NorTable.concat("<tr><td  width='220px' align='center'>風險估計</td><td colspan='100'>" + formatDecimal2(this.Nor_risk) + "</td></tr>");
        NorTable = NorTable.concat(convertTableHead("股票名稱",this.stockSymbol));
        NorTable = NorTable.concat(convertTablePortfolio("建議投資比例",this.Nor_portfolio));
        NorTable = NorTable.concat(convertTableMoney("應投資金額",this.Nor_portfolio));
        NorTable = NorTable.concat(convertTableSummaryReturn("個別的報酬率",this.Nor_sumReturn));
        //change值取增加的百分比 故-1
        NorTable = NorTable.concat("<tr><td  width='220px' align='center'>投資組合於測試期間的獲利率</td><td colspan='100'>" + formatDecimal2(this.Nor_change - 1) + "</td></tr>");
        NorTable = NorTable.concat("</table>");
        
        return NorTable;
    }
    
    public String getCcmTable()
    {
        String CcmTable = "";
        CcmTable = CcmTable.concat("<table align='center' class='table_result'>");
        CcmTable = CcmTable.concat("<caption>Model: CCM</caption>");
        CcmTable = CcmTable.concat("<tr><td  width='200px' align='center'>預期的報酬率</td><td colspan='100'>" + formatDecimal2(this.CCM_expReturn) + "</td></tr>");
        CcmTable = CcmTable.concat("<tr><td  width='200px' align='center'>風險估計</td><td colspan='100'>" + formatDecimal2(this.CCM_risk) + "</td></tr>");
        CcmTable = CcmTable.concat(convertTableHead("股票名稱",this.stockSymbol));
        CcmTable = CcmTable.concat(convertTablePortfolio("建議投資比例",this.CCM_portfolio));
        CcmTable = CcmTable.concat(convertTableMoney("應投資金額",this.CCM_portfolio));
        CcmTable = CcmTable.concat(convertTableSummaryReturn("個別的報酬率",this.CCM_sumReturn));
        //change值取增加的百分比 故-1
        CcmTable = CcmTable.concat("<tr><td  width='150px' align='center'>投資組合獲利率</td><td colspan='100'>" + formatDecimal2(this.CCM_change - 1) + "</td></tr>");
        CcmTable = CcmTable.concat("</table>");
        
        return CcmTable;
    }
   
    
    public String getSimTable()
    {
        String SimTable = "";
        SimTable = SimTable.concat("<table align='center' class='table_result'>");
        SimTable = SimTable.concat("<caption>Model: SIM</caption>");
        SimTable = SimTable.concat("<tr><td  width='200px' align='center'>預期的報酬率</td><td colspan='100'>" + formatDecimal2(this.SIM_expReturn) + "</td></tr>");
        SimTable = SimTable.concat("<tr><td  width='200px' align='center'>風險估計</td><td colspan='100'>" + formatDecimal2(this.SIM_risk) + "</td></tr>");
        SimTable = SimTable.concat(convertTableHead("Stock Name",this.stockSymbol));
        SimTable = SimTable.concat(convertTablePortfolio("建議投資比例",this.SIM_portfolio));
        SimTable = SimTable.concat(convertTableMoney("應投資金額",this.SIM_portfolio));
        SimTable = SimTable.concat(convertTableSummaryReturn("個別的報酬率",this.SIM_sumReturn));
        //change值取增加的百分比 故-1
        SimTable = SimTable.concat("<tr><td  width='150px' align='center'>投資組合獲利率</td><td colspan='100'>" + formatDecimal2(this.SIM_change - 1) + "</td></tr>");
        SimTable = SimTable.concat("</table>");
        
        return SimTable;
    }
    
 
    public String getStockPictureTag()
    {
        //<img src="/webimg/SP/20130820101808.jpg"> revised by YaTing
        String result = "<img src=\"/webimg/SP/" + this.stockPicName + ".png\">";
        return result;
    }
    
    public String getStockPictureName()
    {
        return "/webimg/SP/" + this.stockPicName + ".png";
    }

    
    //以下為method
    //-------------------------------------------------------------------------------------------------
    public String convertTableHead(String title, String[] keyName)
    {
        //此method將String[]的keyName轉成table表頭
        String result="<tr>";
        result = result.concat("<th  width='150px' valign='center'>" + title +"</th>");
        for(String s:keyName)
            {result = result.concat("<th>"+sqlSymbolSearchName(s)+"</th>");}        
        result = result.concat("<tr>");        
        return result;
    }
    
     public String convertTablePortfolio(String title, double[] values)
    {        
        //此method將double[]的portfolio值轉成table型式      
        String result="<tr align='center'>";
        result = result.concat("<td width='150px' align='center'>" + title + "</td>");
        for(double j:values)
            {result = result.concat("<td>"+formatDecimal2(j) + "</td>");}        
        result = result.concat("<tr>");        
        return result;
    }
     
          public String convertTableMoney(String title, double[] values)
    {        
        //此method將double[]的portfolio值轉成table型式
        java.text.DecimalFormat df = new java.text.DecimalFormat();
        df.setMaximumFractionDigits(2);
        String result="<tr align='center'>";
        result = result.concat("<td width='150px' align='center'>" + title + "</td>");
        for(double j:values)
            {result = result.concat("<td>"+df.format(this.money * j) + "萬</td>");}
        result = result.concat("<tr>");        
        return result;
    }
     
      public String convertTableSummaryReturn(String title, double[] values)
    {        
        //此method將double[]的summary return值轉成table型式 在此僅取增加的百分比 故將j-1     
        String result="<tr align='center'>";
        result = result.concat("<td width='150px' align='center'>" + title + "</td>");
        for(double j:values)
            {result = result.concat("<td>"+formatDecimal2(j - 1) + "</td>");}        
        result = result.concat("<tr>");        
        return result;
    }
    
    public  String convertStringArrayToField(String[] stringArray)
    {
        /*
        此method將取得的字串陣列組合為sql可讀的field名稱, 並於字串尾額外加上$TWT
        判斷股票為上市或上櫃，如為上市股票，則必需加上.TW，如為上櫃股票，則必需加上.TWO revised by YaTing
        例如: 
        字串陣列 {2317, 2330, 3008, 3189, 3406}
        轉成字串 '^TWII','2317.TW', '2330', '3008', '3189', '3406' revised by YaTing
        */
        try
        {
        java.sql.Connection conn = null;
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
        conn = java.sql.DriverManager.getConnection("jdbc:sqlserver://Localhost;databaseName=Stock","user","shawn");
        String fieldName = "";
        String indexName = "'^TWII',";
        
        for (int i=0;i<Array.getLength(stringArray);i++) 
        { 
            String sql = "select Symbol, StyleN from [dbo].[StockNameInd] where Symbol= " + "'"+stringArray[i] + "'";
            java.sql.Statement stmt = conn.createStatement();
            java.sql.ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()) //由資料庫中撈取資料，判斷該股票為上市或上櫃股
            {
            style = rs.getString(2);
            rs.close();
            stmt.close();
            }
            if(style.equals("1"))
            {
               fieldName = fieldName.concat( "'"+ stringArray[i] + ".TW',"); //上市股結尾是.TW
            }
            else
            {
               fieldName = fieldName.concat( "'"+ stringArray[i] + ".TWO',"); //上櫃股結尾是.TWO
            }
        }
        conn.close();    
        fieldName = indexName.concat(fieldName);
        return fieldName.substring(0, fieldName.length()-1);
        }
        catch(Exception e)
        {
            return "Convert String Exception: " + e;
        }
    }
    
    public String formatDecimal2(double number)
    {
        //將double數值轉為百分比 並截自小數點第2位四捨五入 revised by YaTing
        java.text.DecimalFormat df = new java.text.DecimalFormat();
        df.setMaximumFractionDigits(2);
        return df.format(number * 100.00) + "%";
    }
    
    public String sqlSymbolSearchName(String Symbol) 
    {
        //將股票的代號也撈出 結果為 台積電(2330) revised by YaTing
        String ResultName = "NoMatch";
        String SymbolName = "NoMatch";
        try
        {    
        java.sql.Connection conn = null;
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
        conn = java.sql.DriverManager.getConnection("jdbc:sqlserver://Localhost;databaseName=Stock","user","shawn");
        String sql = "SELECT Symbol,Name FROM Sym_Indus WHERE Symbol = " + "'"+Symbol + "'";
        java.sql.Statement stmt = conn.createStatement();
        java.sql.ResultSet rs = stmt.executeQuery(sql);
        
        if(rs.next())
           ResultName = rs.getString("Name");
           SymbolName = ResultName.concat("("+ Symbol +")");
        rs.close();
        stmt.close();
        conn.close();
        return SymbolName;
        }
        catch(Exception e)
        {
            return "Symbol seach:" + Symbol + ", Exception: " + e;
        }
    }
    
    public String ErrorHandle(String[] error1, String[] error2, String[] error3, String[] error4)
    {
        //判斷R的error
        if ( error1[0].equals("0.0") && error2[0].equals("0.0") && error3[0].equals("0.0") && error4[0].equals("0.0"))
            return "noError";
        else       
            if (error1[0].length() > 5)
                return "Error1: " + error1[0] + " 可能由於日期選擇錯誤或網路連線品質不佳因此無法下載資料";
          else
                if (error2[0].length() > 5)
                    return "Error2: " + error2[0] + "此投資組合的報酬率低於定存，不建議投資";
                else 
                    if (error3[0].length() > 5)
                        return "Error3: "+ error3[0] + "此投資組合的報酬率低於定存，不建議投資";
                    else 
                        if (error4[0].length() > 5)
                            return "Error4:" + error4[0];        
        return null;
    }
    
    public String getPrintErrorMsg() {
    //印出R的error  
    return ErrorHandle(ErrorMsg1,ErrorMsg2,ErrorMsg3,ErrorMsg4);
    
    }
    
    
}
