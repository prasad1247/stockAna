/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import bean.Stock;
import bean.StockPrice;
import bean.TradeBean;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.ApplicationConstants;
import util.ApplicationUtils;

/**
 *
 * @author Priyanka
 */
public class HeiknAshi {

   static Date d = null, debugDate = null;
    public static void main(String[] args) {
        StockAnalysis ana = new StockAnalysis();
        Stock s = null;
        try {
            File[] Listf = new File(ApplicationConstants.SYMBOL_PATH).listFiles();
            HashMap<String, LinkedList<TradeBean>> tradeMap = new HashMap();
            SimpleDateFormat fo = new SimpleDateFormat("dd-MMM-yy");
            
            d = fo.parse("10-Jan-16");
            debugDate = fo.parse("05-Jan-18");
            for (int i = 0; i < Listf.length; i++) {
                String stockName = Listf[i].getName().replace(".csv", "");
                s = ana.getDatafromFile(stockName);
//                s = ana.getDatafromFile("COX&KINGS".replace(".csv", ""));
                s = ana.addIndicators(s);
                LinkedList weekList = daysToWeek(s.getStockList());
 //               tradeMap.put("COX&KINGS", getSingnals(updateSeries(weekList, 0, true), Listf[i].getName()));
//                tradeMap.put("COX&KINGS", getSingnals(updateSeries(s.getStockList(), 0, true), Listf[i].getName(), debugDate));
                tradeMap.put(stockName, getSingnals(updateSeries(s.getStockList(), 0, true), Listf[i].getName(), debugDate));
//                break;
            }
            int noOfTrades = 0, percentToSell = 25, tradePerDay = 0, profitTrades = 0, lossTrades = 0;
            LinkedList<String> symbolList = ApplicationUtils.readSymbols(ApplicationConstants.SYMBOL_LIST_PATH);
            double amtToInvest = 240000, amtInvested = 0, totalProfit = 0, mayProfit = 0, amtPerStock = 3000, totalInvestMent = 0, totalPlus = 0, totalMinus = 0;
            HashMap<String, Integer> investMap = new HashMap();
            HashMap<Integer, Integer> percentMap = new HashMap();
            LinkedList tradeCount = new LinkedList();

            Calendar c = Calendar.getInstance();
            String debugStock = "COX&KINGS";
            boolean debug = false;
            c.setTime(d);
            for (int i = 0; i < Listf.length; i++) {
                String stockName = Listf[i].getName().replace(".csv", "");
//                LinkedList<TradeBean> tradeList = tradeMap.get(debugStock);
                LinkedList<TradeBean> tradeList = tradeMap.get(stockName);
                tradeScan(stockName, tradeList, c);
//                break;
                
            }
//            for (int i = 0; i < 765; i++) {
//                if (c.getTime().before(debugDate)) {
//
//                    debug = true;
//
//                }
//                amtToInvest += investMap.get(fo.format(c.getTime())) == null ? 0 : investMap.get(fo.format(c.getTime()));
//                amtInvested -= investMap.get(fo.format(c.getTime())) == null ? 0 : investMap.get(fo.format(c.getTime()));
//                for (String nitfy200 : symbolList) {
//                    //for (Iterator<String>name =tradeMap.keySet().iterator();name.hasNext();) {
//                    //   String nitfy200= name.next();
//
//                    if (nitfy200.equals(debugStock) && debug) {
//                        String ab = "aa";
//                    }
//                    LinkedList<TradeBean> tradeList = tradeMap.get(nitfy200);
//                    if (tradeList == null) {
//                        continue;
//                    }
//                    if (tradePerDay > 4) {
//                        break;
//                    }
//                    tradeScan(nitfy200, tradeList, c);
//                    tradePerDay++;
//                }
//                if (tradePerDay > 0) {
//                    tradeCount.add(tradePerDay);
//                }
//                tradePerDay = 0;
//                c.add(Calendar.DATE, 1);
//            }

        } catch (Exception ex) {
            Logger.getLogger(PivotStrategy.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void tradeScan(String stockName, LinkedList<TradeBean> tradeList, Calendar c) {
        int noOfTrades = 0, percentToSell = 25, profitTrades = 0, lossTrades = 0;
        double amtToInvest = 240000000, amtInvested = 0, totalProfit = 0, mayProfit = 0, amtPerStock = 3000, totalInvestMent = 0, totalPlus = 0, totalMinus = 0;
        HashMap<String, Integer> investMap = new HashMap();
        HashMap<Integer, Integer> percentMap = new HashMap();
        for (TradeBean tb : tradeList) {
            if (amtToInvest > 0 ) {
                amtInvested += amtPerStock;
                int amtToput = (int) (investMap.get(tb.getSellStock().getDate()) == null ? amtPerStock : investMap.get(tb.getSellStock().getDate()) + amtPerStock);
                investMap.put(tb.getSellStock().getDate(), amtToput);
                int shares = (int) (amtPerStock / tb.getBuyStock().getClose());
                amtToInvest -= amtPerStock;
                double mprofit = 0;
                noOfTrades++;

                totalInvestMent = Math.max(totalInvestMent, amtInvested);
                if (tb.getHighStock().getDateTime().after(tb.getBuyStock().getDateTime())) {
                    int percent = (int) (((tb.getHighStock().getHigh() - tb.getBuyStock().getClose()) * 100) / tb.getBuyStock().getClose());
                    int percentTopt = percentMap.get(percent) == null ? 1 : percentMap.get(percent) + 1;
                    // percentMap.put(percent, percentTopt);
                }
                if (tb.getHighStock().getClose() > (tb.getBuyStock().getClose() + (tb.getBuyStock().getClose() * percentToSell / 100))) {
                    mprofit = (tb.getBuyStock().getClose() * percentToSell / 100) * shares;
                    mayProfit += mprofit;
                } else {
                    mprofit = (tb.getSellStock().getClose() - tb.getBuyStock().getClose()) * shares;
                    mayProfit += mprofit;
                }
                double profit = (tb.getSellStock().getClose() - tb.getBuyStock().getClose()) * shares;
                if (profit > 0) {
                    totalPlus += profit;
                    profitTrades++;
                } else {
                    totalMinus += profit;
                    lossTrades++;
                }
                totalProfit += profit;
                if(tb.getBuyStock().getDateTime().after(debugDate)){
                System.out.print(stockName + " buy  " + tb.getBuyStock().getDate() + " sell  " + tb.getSellStock().getDate() + " profit " + profit + " Investment " + amtInvested + "   " + amtToInvest + "  ");
                System.out.print(tb.getBuyStock().getClose() + "   " + tb.getSellStock().getClose() + " Highest Reached " + tb.getHighStock().getHigh() + "  " + tb.getHighStock().getDate() + "  \n");
                System.out.println("tradess  " + noOfTrades + "    " + totalProfit + "--------" + totalInvestMent + "-------" + (100 * totalProfit / totalInvestMent) + "---" + mayProfit + "-----------------------------------------------------------------------------  " + mprofit);
                System.out.println("total pls   " + profitTrades + "  mins " + lossTrades);
                System.out.println("total pls   " + totalPlus + "  mins " + totalMinus);
                }
            }
        }
        System.out.println(stockName+"#"+profitTrades+"#"+lossTrades+"#"+totalPlus+"#"+totalMinus+"#"+noOfTrades+"#"+totalProfit+"#"+totalInvestMent+"#"+(100 * totalProfit / totalInvestMent));
    }

    public static LinkedList updateSeries(LinkedList source, int skip, boolean newBar) {

        if (source == null) {
            throw new IllegalArgumentException("Null source (CandleSeries).");
        }
        LinkedList heAshi = new LinkedList();
        for (int i = 2; i < source.size(); i++) {
            if (source.size() > skip) {
                // get the current data item... 
                StockPrice candleItem = (StockPrice) source.get(i);

                if (source.size() > 1) {
                    /*
     * Get the prev candle the new candle may be just forming or 
     * completed if back testing. Hiekin-Ashi bats must be formed 
     * from completed bars. 
                     */

                    double xOpenPrev = 0;
                    double xClosePrev = 0;

                    StockPrice prevItem = null;
                    if (heAshi.size() > 1) {
                        prevItem = (StockPrice) heAshi.getLast();
                    } else {
                        prevItem = candleItem;
                    }

                    xClosePrev = prevItem.getClose();
                    xOpenPrev = prevItem.getOpen();

                    double xClose = (candleItem.getOpen() + candleItem.getHigh()
                            + candleItem.getLow() + candleItem.getClose()) / 4;

                    double xOpen = (xOpenPrev + xClosePrev) / 2;

                    double xHigh = Math.max(candleItem.getHigh(),
                            Math.max(xClose, xOpen));

                    double xLow = Math.min(candleItem.getLow(),
                            Math.min(xOpen, xClose));

                    StockPrice currDataItem = new StockPrice();
                    currDataItem.setOpen(roundTo(xOpen));
                    currDataItem.setHigh(roundTo(xHigh));
                    currDataItem.setLow(roundTo(xLow));
                    currDataItem.setClose(roundTo(xClose));
                    currDataItem.setDate(candleItem.getDate());
                    currDataItem.setDateTime(candleItem.getDateTime());
                    heAshi.add(currDataItem);
                    //System.out.println(currDataItem);
                }
            }
        }
        return heAshi;
    }

    public static LinkedList daysToWeek(LinkedList<StockPrice> dayList) {
        Calendar cal = Calendar.getInstance();
        int currWeek = -1;
        LinkedList<StockPrice> weekList = new LinkedList();
        StockPrice weekSp = null;
        int cnt = 0;
        for (StockPrice sp : dayList) {
            cal.setTime(sp.getDateTime());
            int week = cal.get(Calendar.WEEK_OF_YEAR);
            if (currWeek == -1 || currWeek != week) {
                weekSp = new StockPrice();
                weekSp.setOpen(sp.getOpen());
                weekSp.setLow(sp.getLow());
                weekSp.setHigh(sp.getHigh());
                weekSp.setDate(sp.getDate());
                weekSp.setDateTime(sp.getDateTime());
            }
            if (currWeek != week) {
                weekList.add(weekSp);
            }
            if (currWeek == week) {
                weekSp.setClose(sp.getClose());
                weekSp.setLow(Math.min(weekSp.getLow(), sp.getLow()));
                weekSp.setHigh(Math.max(weekSp.getHigh(), sp.getHigh()));
                cnt++;

            } else {
                currWeek = week;
            }
        }

        return weekList;

    }

    public static double roundTo(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    public static LinkedList<TradeBean> getSingnals(LinkedList<StockPrice> weekList, String stockName, Date debugDate) {
        double buy = 0;
        StockPrice buySp = null, sellSp = null, highSp = null;
        TradeBean tb = null;
        LinkedList tradeList = new LinkedList();
        for (int i = 0; i < weekList.size() - 1; i++) {
            StockPrice sp = weekList.get(i);
            StockPrice nextSp = weekList.get(i + 1);
            if (debugDate.before(sp.getDateTime())) {
                String debug = "true";
            }
            buy = plainSellStratgey(sp, nextSp, buy);
            buy = modifiedStratgey(sp, nextSp, buy);
            buy = plainBuyStratgey(sp, nextSp, buy);

            if (buy == 3) {  // got red candle close the trade
                sellSp = sp;
                tb.setSellStock(sellSp);
                tradeList.add(tb);
                buy = 0;
            }

            if (buy == 1) {
                buySp = sp;
                tb = new TradeBean();
                tb.setBuyStock(buySp);
                highSp = null;
                buy = 2;
            }

            if (buy == 2 && (highSp == null || highSp.getHigh() < sp.getHigh())) { //find the highest candle
                highSp = nextSp;
                tb.setHighStock(highSp);
            }
            if (i + 1 == weekList.size() - 1 && tb != null) {
                tb.setSellStock(tb.getBuyStock());
                tradeList.add(tb);
            }
        }
        return tradeList;
    }

    static double plainSellStratgey(StockPrice sp, StockPrice nextSp, double buy) {
        if (buy == 2) {
            if (sp.getClose() < sp.getOpen()) { //first Candle is red after buy is on
                return 3;
            }
        }
        return buy;
    }

    static double plainBuyStratgey(StockPrice sp, StockPrice nextSp, double buy) {
        if (buy == 0) {
            if (sp.getClose() < sp.getOpen()) { //first Candle is red
                if (nextSp.getClose() > nextSp.getOpen()) //second Canle is green then buy.
                {
                    return 0.9;
                }
            }
        }
        return buy;
    }

    private static double modifiedStratgey(StockPrice sp, StockPrice nextSp, double buy) {
        if (buy == 0.9) { //first Candle is red
            if (sp.getOpen() == sp.getLow()) //second Canle is green then buy.
            {
                return 1;
            }
            if (sp.getClose() < sp.getOpen()) {
                return 0;
            }
        }
        return buy;
    }
}
