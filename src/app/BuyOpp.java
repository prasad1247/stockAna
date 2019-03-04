/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import bean.Stock;
import bean.StockPrice;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Priyanka
 */
public class BuyOpp {

    public static void main(String[] args) {
        StockAnalysis ana = new StockAnalysis();
        Stock s = null;

        try {
            s = ana.getDatafromFile("COX&KINGS".replace(".csv", ""));
            int j = 0, dnTrend = 0, upTrend = 0;
            int checkReverseCount = 0, reverseCount = 3;

            StockPrice startStock = null;
            
            for (int i = 0; i < s.getStockList().size(); i++) {

                StockPrice sp = (StockPrice) s.getStockList().get(i);
                StockPrice sp1 = (StockPrice) s.getStockList().get(i + 1);

                if (sp.getClose() > sp1.getClose()) {
                    startStock = sp;
                    dnTrend = 1;
                } else if (sp.getClose() < sp1.getClose()) {
                    upTrend = 1;
                }

                if (dnTrend == 1) {
                    if (sp.getClose() < sp1.getClose() && checkReverseCount != reverseCount) {
                        checkReverseCount++;
                    }
                    if (checkReverseCount==reverseCount) {
                        
                    }

                }
            }
        } catch (IOException ex) {
            Logger.getLogger(BuyOpp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
