/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import static app.PivotPoints.getPivots;
import static app.PivotPoints.refine;
import bean.PivotPointBean;
import bean.Stock;
import bean.StockPrice;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.ApplicationConstants;

/**
 *
 * @author PRASAD
 */
public class PivotStrategy {

    public static void main(String[] args) {
        StockAnalysis ana = new StockAnalysis();
        Stock s = null;
        try {
            s = ana.getDatafromFile("HDIL");
        } catch (IOException ex) {
            Logger.getLogger(PivotStrategy.class.getName()).log(Level.SEVERE, null, ex);
        }
     
        java.sql.Date d = null;
        s= ChangeTimeFrame.changeTimeFrame(s.getStockList(),s, 5);
        LinkedList ls=s.getStockList();
        ana.addIndicators(s);
        LinkedList pvtList;
        LinkedList refinedList = null;
        try {
            pvtList = getPivots(ls, 0);
            refinedList = refine(pvtList, 0);
        } catch (ParseException ex) {
            Logger.getLogger(PivotStrategy.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        
        int currentcandleNo=ls.size()-1;
        StockPrice sp=(StockPrice) ls.get(currentcandleNo);
        HashMap indicators = s.getIndicators();
        
        
        PivotPointBean pbs1[] = (PivotPointBean[]) refinedList.get(refinedList.size()-1);
        PivotPointBean temphighBean = pbs1[1];
        PivotPointBean templowBean = pbs1[0];
        System.out.println("current pivot High ::"+temphighBean.pvtTime+"   "+temphighBean.pvtvalue+"  "+temphighBean.pivotAtCandle);
        System.out.println("current pivot Low ::"+templowBean.pvtTime+"   "+templowBean.pvtvalue+"  "+temphighBean.pivotAtCandle);
        System.out.println("current::   "+sp);
        System.out.println(((double[]) indicators.get(ApplicationConstants.RSI))[currentcandleNo]+"  ADX  "+((double[]) indicators.get(ApplicationConstants.ADX))[currentcandleNo]+" ATR "+((double[]) indicators.get(ApplicationConstants.ATR))[currentcandleNo]);
        System.out.println(((double[]) indicators.get(ApplicationConstants.BBLower))[currentcandleNo]+"  BBUpper  "+((double[]) indicators.get(ApplicationConstants.BBUpper))[currentcandleNo]+" BBSMA  "+((double[]) indicators.get(ApplicationConstants.BBSma))[currentcandleNo]);
        
        double BBUpper[]=((double[]) indicators.get(ApplicationConstants.BBUpper));
        double BBLower[]=((double[]) indicators.get(ApplicationConstants.BBLower));
        double BBMid[]=((double[]) indicators.get(ApplicationConstants.BBSma));
        
        for(int i=0;i<BBUpper.length;i++){
            System.out.println("    "+ls.get(i));
            System.out.println("  Lower  "+BBLower[i]+" Upper   "+BBUpper[i]+"  Mid  "+BBMid[i]);
        }
        

    }
}
