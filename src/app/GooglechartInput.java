/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import bean.Stock;
import bean.StockPrice;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author PRASAD
 */
public class GooglechartInput {
static SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    public static void main(String[] args) {
        StockAnalysis ana = new StockAnalysis();
        int sellFlag = 0, buyFlag = 0;
        Stock s = null;
        String symbol = "nifty";
        try {
            s = ana.getDatafromFile(symbol);
        } catch (IOException ex) {
            Logger.getLogger(PivotStrategy.class.getName()).log(Level.SEVERE, null, ex);
        }
        LinkedList mainList = s.getStockList();
        for(Object o:mainList){
             StockPrice sp1 = (StockPrice) o;
             System.out.println("[new Date(\""+formatter.format(sp1.getDateTime())+"\"),"+sp1.getLow()+","+sp1.getOpen()+","+sp1.getClose()+","+sp1.getHigh()+"],");
//             System.out.println(""+formatter.format(sp1.getDateTime())+","+sp1.getOpen()+","+sp1.getHigh()+","+sp1.getLow()+","+sp1.getClose()+",");
        }
    }

}
