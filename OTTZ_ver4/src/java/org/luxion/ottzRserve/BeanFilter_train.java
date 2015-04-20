/*
 * 4/8
 * 修改為使用RjavaOTTZ package模式
 */
package org.luxion.ottzRserve;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import org.rosuda.REngine.*;
import org.rosuda.REngine.Rserve.*;
/**
 *
 * @author Leon
 */
public class BeanFilter_train extends BeanOTTZ_train{
    private String bestN;
    private String bestK;
    
    public BeanFilter_train() throws Exception{
        
    }
    
    @Override
    public void excute() throws REXPMismatchException, Exception{
        try{
            REXP x;      
//            String havaData = c.eval("exists('TrainData')").asString();
            //String[] temp3 = c.eval(".path.package()").asStrings();
            c.eval("fil <- filterTraining(TrainData)");
            String temp1 = c.eval("exists('fil')").asString();
            String temp2 = c.eval("class(fil$Bestn)").asString();
//            c.eval("bestn_temp<- filterTrain$Bestn");
//            String temp2 =c.eval("exists('bestn_temp')").asString();
//            String temp4 =c.eval("class(filterTrain$Bestk)").asString();
//            String temp5 = c.eval("bestn_temp").asString();
            
                    
            //-----------紀錄最佳N K參數            
            x = c.eval("fil$Bestn");
            this.bestN = x.asString();
            x = c.eval("fil$Bestk");
            this.bestK = x.asString();
        }catch(RserveException e){
            throw new Exception("發生Rserve Exception: " + e);
        }
    }
    public String getBestN(){
        return this.bestN;
    }
    public String getBestK(){
        return this.bestK;
    }
    
}