/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RserveBean;

import org.rosuda.REngine.*;
import org.rosuda.REngine.Rserve.*;
import java.util.Arrays;
/**
 *
 * @author YaTing
 */
public class Correlation {
    
        private int nRow;
        private int ncol;
        private String[] colNames;
        private String[] rowNames;
        private String[][] recordTrade;        
        
public void excuteCor(){
try{
        
        RConnection c = new RConnection();
        REXP x;
        c.eval("library(RODBC)\n" +
                "channel1 <- odbcConnect(\"StockDB\",uid=\"user\",pwd=\"shawn\")\n" +
                "sqldata <- sqlQuery(channel1,\"select * from [dbo].[Correlation]\")\n" +
                "odbcClose(channel1)\n" +
                "sqldata[ ,1] <- as.Date(sqldata[,1])\n" +
                "rownames(sqldata) <- sqldata[ ,1]\n" +
                "DataLag1 <- data.frame(rbind(sqldata[1, ],sqldata[1:179, ]))\n" +
                "DataLag1[1, ] <- NA\n" +
                "sqldata[1, ] <- NA\n" +
                "Rt <- 1+((sqldata[ ,-1] - DataLag1[ ,-1]) / DataLag1[ ,-1])\n" +
                "induscor <- cor(Rt[, -1], use=\"complete.obs\", method=\"pearson\")");
                
            this.nRow = c.eval("nrow(induscor)").asInteger();
            this.ncol = c.eval("ncol(induscor)").asInteger();
            this.colNames = c.eval("colnames(induscor)").asStrings();
            this.rowNames = c.eval("row.names(induscor)").asStrings();
            this.recordTrade = new String[nRow][ncol];

            for(int i =1; i <= nRow; i++)
            {
                    x = c.eval("as.character(induscor[" + i + ",])"); //將R中Dataframe的資料一行一行取出，並存入java的多維陣列
                    this.recordTrade[i-1] = x.asStrings();
                    //System.out.println(Arrays.toString(recordTrade[i-1]));
                    //System.out.println(i);               
            }
            c.eval("rm(list = ls())");
 c.close();
  
    }catch(Exception e){
        System.out.println("錯誤說明"+e.getMessage());
    }
}
        public String getCorTable(){
        java.text.DecimalFormat df = new java.text.DecimalFormat(); //將小數點縮至二位的方法
        df.setMaximumFractionDigits(2);
        
        String resultHead = "<table id='newspaper-b' width='100%'><thead><th>Industry</th>";
        String resultBody = "<tbody>";
        String result="";
                
        for (String s:colNames) //先印出表頭(欄位)名稱
        {resultHead = resultHead.concat("<th>"+ s + "</th>");}
        resultHead = resultHead.concat("<th>Industry</th></thead>");

        
        for(int i=0; i <= recordTrade[0].length-1; i++) //再印出相關係數，因為recordTrade是一個二維的陣列，所以用巢狀迴圈印出
             {
              {resultBody = resultBody.concat("<tr><td>"+ this.rowNames[i] + "</td>");} //第一欄先印欄位名稱
                 for (String r:recordTrade[i])
                    {resultBody = resultBody.concat("<td>"+ df.format(Double.parseDouble(r)) + "</td>");} //Double.parseDouble將String轉為double
              {resultBody = resultBody.concat("<td>"+ this.rowNames[i] + "</td></tr>");}  //因表格龐大故最後一欄也加上欄位名稱，讓表格較易讀
             }
            
        resultBody = resultBody.concat("</tr></tbody></table>");
        result = resultHead.concat(resultBody);
        return result;        
        }
}