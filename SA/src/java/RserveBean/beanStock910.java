/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RserveBean;

import java.lang.reflect.Array;
import java.util.*;
import org.rosuda.REngine.*;
import org.rosuda.REngine.Rserve.*;

/**
 *
 * @author Leon
 */
public class beanStock910 {
    private String[] stockSymbol;
    private String trainStartDate;
    private String trainEndDate;
    private String testStartDate;
    private String testEndDate;
    
    
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

    
    public void excuteStockportfolio() 
    {
        try{
        RConnection c = new RConnection();
        REXP x; 
        //x = c.eval("R.version.string");
        //this.result = x; 
        
        c.eval("\"library(RODBC)\\n\" +\"library(DBI)\\n\" +\"library(sqldf)\\n\" +\"library(stockPortfolio)\"");

        //建立RODBC connection, 816開始改用localDB
        c.eval("channel1 <- odbcConnect('StockDB',uid='user',pwd='shawn')");
        
        //以下依照使用者選取symbol 建立data選擇陳述式
        
        String fieldName = convertStringArrayToField(this.stockSymbol);
        
        
        //String trainDataStatement = "trainset <- sqlQuery(channel1, 'SELECT * FROM StockClosePrice abc PIVOT (SUM(ClosePrice) FOR Symbol IN ([1101], [1201], [1301], [$TWT])) bcd WHERE Date BETWEEN \\'2013-4-01\\' AND \\'2013-7-31\\' ORDER BY Date')";
        //String testDataStatement = "testset <- sqlQuery(channel1, 'SELECT * FROM StockClosePrice abc PIVOT (SUM(ClosePrice) FOR Symbol IN ([1101], [1102], [1103], [$TWT])) bcd WHERE Date BETWEEN \\'2013-8-01\\' AND \\'2013-9-01\\' ORDER BY Date')";
        
        String trainDataStatement = "trainset <- sqlQuery(channel1, 'SELECT * FROM StockClosePrice abc PIVOT (SUM(ClosePrice) FOR Symbol IN ("
                + fieldName
                + "))bcd WHERE Date BETWEEN "
                + " \\' " + this.trainStartDate + " \\' AND \\' " + this.trainEndDate + " \\' ORDER BY Date')";
        
        String testDataStatement = "testset <- sqlQuery(channel1, 'SELECT * FROM StockClosePrice abc PIVOT (SUM(ClosePrice) FOR Symbol IN ("
                + fieldName
                + "))bcd WHERE Date BETWEEN "
                + " \\' " + this.testStartDate + " \\' AND \\' " + this.testEndDate + " \\' ORDER BY Date')";
    
        
        c.eval(trainDataStatement);
        c.eval(testDataStatement);
        
        c.eval("Industry <- sqlQuery(channel1, 'SELECT  StockNameInd.Symbol, Industry.En_Industry FROM Industry INNER JOIN  StockNameInd ON Industry.[Key] = StockNameInd.Industry_key')");
        c.eval("odbcClose(channel1)");     
        
        /*
        //分割建模與測試資料集
        c.eval("frow<-nrow(data)");
        c.eval("mrow<-nrow(data)-10");
        c.eval("lrow<-nrow(data)-11");
        c.eval("trainset<- data[1:mrow, ]");
        c.eval("testset<- data[mrow:frow, ]");
        */
        
        //將資料的日期欄位作為row.name, 並移除日期欄位
        c.eval("row.names(trainset)<-trainset$Date");
        c.eval("row.names(testset)<-testset$Date");
        c.eval("trainset$Date<-NULL");
        c.eval("testset$Date<-NULL");
        //將資料集轉為矩陣
        c.eval("Train<-data.matrix(trainset)");
        c.eval("Test<-data.matrix(testset)");
        //找出$TWT的欄位編號 並移除$TWT
        c.eval("indexTr<-which( colnames(Train)==\"$TWT\" )");
        c.eval("indexTe<-which( colnames(Test)==\"$TWT\" )");
        c.eval("Train1<-Train[,-indexTr]");
        
        //分別以三種模式建模
        c.eval("Nor<-stockModel(Train1,Rf=0.0000552,model='none',industry=Industry$En_Industry,get='overlapOnly',recentLast=F,rawStockPrices=TRUE,freq='day')");
        c.eval("SIM<-stockModel(Train,Rf=0.0000552,model='SIM',industry=Industry$En_Industry,shortSelling='n',recentLast=F,rawStockPrices=TRUE,freq='day',index=indexTr)");
        c.eval("CCM<-stockModel(Train1,Rf=0.0000552,model='CCM',industry=Industry$En_Industry,shortSelling='n',recentLast=F,rawStockPrices=TRUE,freq='day')");
        
        c.eval("opNor<-optimalPort(Nor,Rf=-1)");
        c.eval("opCCM<-optimalPort(CCM,Rf=-1)");
        c.eval("opSIM<-optimalPort(SIM,Rf=-1)");
        
        //測試收益百分比
        c.eval("tpNor <- testPort(Test, opNor)");
        c.eval("tpCCM <- testPort(Test, opCCM)");
        c.eval("tpSIM <- testPort(Test, opSIM)");
        
        //以下將三種建模結果存到預先建立的屬性中
        //824開始改為輸出optimalPort與testPort兩方的重要參數
        
        //model: none
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
        c.eval("png(file = \"F:/webimg/" + picName + ".png\")");
        this.stockPicName = picName;
        c.eval("plot(tpNor,col='#006BDC')");
        c.eval("lines(tpNor, col='#006BDC', lty=1, lwd=2)");
        c.eval("lines(tpCCM, col='#FF8F19', lty=2, lwd=2)");
        c.eval("lines(tpSIM, col='#FF1989', lty=4, lwd=2)");
        c.eval("legend('topleft',c('Nor','CCM','SIM'), col=c('#006BDC','#FF8F19','#FF1989'), lty=c(1,2,4))");
        c.eval("dev.off()");     
        
        
       //setStock到此結束 關閉R connection
       c.close();
      
    }catch(Exception e){
        System.out.println("錯誤說明"+e.getMessage());
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
    
    
    public String getNorTable()
    {
        String NorTable = "";
        NorTable = NorTable.concat("<table width='100%' border='1'>");
        NorTable = NorTable.concat("<caption>Model: none</caption>");
        NorTable = NorTable.concat("<tr><td  width='150px'>Expect Return </td><td colspan='100'>" + formatDecimal4(this.Nor_expReturn) + "</td></tr>");
        NorTable = NorTable.concat("<tr><td  width='150px'>Risk Estimate </td><td colspan='100'>" + formatDecimal4(this.Nor_risk) + "</td></tr>");
        NorTable = NorTable.concat(convertTableHead("Stock Name",this.Nor_stockName));
        NorTable = NorTable.concat(convertTablePortfolio("Optimal Portfolio.",this.Nor_portfolio));
        NorTable = NorTable.concat(convertTableSummaryReturn("Summary Return",this.Nor_sumReturn));
        //change值取增加的百分比 故-1
        NorTable = NorTable.concat("<tr><td  width='150px'>Change in portfolio value</td><td colspan='100'>" + formatDecimal4(this.Nor_change - 1) + "</td></tr>");
        NorTable = NorTable.concat("</table>");
        
        return NorTable;
    }
    
    public String getCcmTable()
    {
        String CcmTable = "";
        CcmTable = CcmTable.concat("<table width='100%' border='1'>");
        CcmTable = CcmTable.concat("<caption>Model: CCM</caption>");
        CcmTable = CcmTable.concat("<tr><td  width='150px'>Expect Return </td><td colspan='100'>" + formatDecimal4(this.CCM_expReturn) + "</td></tr>");
        CcmTable = CcmTable.concat("<tr><td  width='150px'>Risk Estimate </td><td colspan='100'>" + formatDecimal4(this.CCM_risk) + "</td></tr>");
        CcmTable = CcmTable.concat(convertTableHead("Stock Name",this.CCM_stockName));
        CcmTable = CcmTable.concat(convertTablePortfolio("Optimal Portfolio.",this.CCM_portfolio));
        CcmTable = CcmTable.concat(convertTableSummaryReturn("Summary Return",this.CCM_sumReturn));
        //change值取增加的百分比 故-1
        CcmTable = CcmTable.concat("<tr><td  width='150px'>Change in portfolio value</td><td colspan='100'>" + formatDecimal4(this.CCM_change - 1) + "</td></tr>");
        CcmTable = CcmTable.concat("</table>");
        
        return CcmTable;
    }
   
    
    public String getSimTable()
    {
        String SimTable = "";
        SimTable = SimTable.concat("<table width='100%' border='1'>");
        SimTable = SimTable.concat("<caption>Model: SIM</caption>");
        SimTable = SimTable.concat("<tr><td  width='150px'>Expect Return </td><td colspan='100'>" + formatDecimal4(this.SIM_expReturn) + "</td></tr>");
        SimTable = SimTable.concat("<tr><td  width='150px'>Risk Estimate </td><td colspan='100'>" + formatDecimal4(this.SIM_risk) + "</td></tr>");
        SimTable = SimTable.concat(convertTableHead("Stock Name",this.SIM_stockName));
        SimTable = SimTable.concat(convertTablePortfolio("Optimal Portfolio.",this.SIM_portfolio));
        SimTable = SimTable.concat(convertTableSummaryReturn("Summary Return",this.SIM_sumReturn));
        //change值取增加的百分比 故-1
        SimTable = SimTable.concat("<tr><td  width='150px'>Change in portfolio value</td><td colspan='100'>" + formatDecimal4(this.SIM_change - 1) + "</td></tr>");
        SimTable = SimTable.concat("</table>");
        
        return SimTable;
    }
    
     

 
    public String getStockPictureTag()
    {
        //<img src="./plotimages/stockPortfolio/20130820101808.jpg">
        String result = "<img src=\"./plotimages/stockPortfolio/" + this.stockPicName + ".jpg\">";
        return result;
    }
    
    public String getStockPictureName()
    {
        return "/webimg/" + this.stockPicName + ".png";
    }

    
    //以下為method
    //-------------------------------------------------------------------------------------------------
    public String convertTableHead(String title, String[] keyName)
    {
        //此method將String[]的keyName轉成table表頭
        String result="<tr>";
        result = result.concat("<th  width='150px'>" + title +"</th>");
        for(String s:keyName)
            {result = result.concat("<th>"+sqlSymbolSearchName(s)+"</th>");}        
        result = result.concat("<tr>");        
        return result;
    }
    
     public String convertTablePortfolio(String title, double[] values)
    {        
        //此method將double[]的portfolio值轉成table型式      
        String result="<tr>";
        result = result.concat("<td width='150px'>" + title + "</td>");
        for(double j:values)
            {result = result.concat("<td>"+formatDecimal4(j) + "</td>");}        
        result = result.concat("<tr>");        
        return result;
    }
     
      public String convertTableSummaryReturn(String title, double[] values)
    {        
        //此method將double[]的summary return值轉成table型式 在此僅取增加的百分比 故將j-1     
        String result="<tr>";
        result = result.concat("<td width='150px'>" + title + "</td>");
        for(double j:values)
            {result = result.concat("<td>"+formatDecimal4(j - 1) + "</td>");}        
        result = result.concat("<tr>");        
        return result;
    }
    
    public  String convertStringArrayToField(String[] stringArray)
    {
        /*
        此method將取得的字串陣列組合為sql可讀的field名稱, 並於字串尾額外加上$TWT
        例如: 
        字串陣列 {2317, 2330, 3008, 3189, 3406}
        轉成字串 [2317], [2330], [3008], [3189], [3406], [$TWT]        
        */
        String fieldName = "";        
        for (int i=0;i<Array.getLength(stringArray);i++) 
        { 
               fieldName = fieldName.concat("[" + stringArray[i] + "], ");
        } 
        fieldName = fieldName.concat("[$TWT]");
        return fieldName;
    }
    
    public String formatDecimal4(double number)
    {
        //將double數值轉為百分比 並截自小數點第4位四捨五入
        java.text.DecimalFormat df = new java.text.DecimalFormat();
        df.setMaximumFractionDigits(4);
        df.setMinimumFractionDigits(4);
        return df.format(number * 100.00) + "%";
    }
    
    public String sqlSymbolSearchName(String Symbol) 
    {
        String ResultName = "NoMatch";
        try
        {    
        java.sql.Connection conn = null;
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
        conn = java.sql.DriverManager.getConnection("jdbc:sqlserver://Localhost;databaseName=Stock","user","shawn");
        String sql = "SELECT Symbol,Name FROM Sym_Indus WHERE Symbol = " + "'"+Symbol + "'";
        java.sql.Statement stmt = conn.createStatement();
        java.sql.ResultSet rs = stmt.executeQuery(sql);
        
        if(rs.next())
           ResultName= rs.getString("Name");
        rs.close();
        stmt.close();
        conn.close();
        return ResultName;
        }
        catch(Exception e)
        {
            return "Symbol seach:" + Symbol + ", Exception: " + e;
        }
    }
}
