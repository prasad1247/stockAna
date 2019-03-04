/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import static app.PivotBackTester.formatter1;
import static app.PivotForwardTester.generatePivot;
import static app.PivotForwardTester.refinePivots;
import static app.PivotPoints.formatter;
import bean.PivotPointBean;
import bean.Stock;
import bean.StockPrice;
import bean.TradeBean;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.ApplicationConstants;

/**
 *
 * @author PRASAD
 */
public class AnotherForwardTester {

    private static Date stopdate = null;
    static LinkedList<PivotPointBean> pivotlist = new LinkedList<PivotPointBean>();
    static LinkedList refinedPivots2 = new LinkedList();
    static boolean debug = false;
    static double triggerPivot;
    static boolean successContinue = false;
    static int threeInsideBar=0;

    static PivotPointBean temphighBean = new PivotPointBean(), templowBean = new PivotPointBean();

    static int gapTaken = 1;
    static int insiderBarTaken = 1;
    static int sellTaken = 1;
    static int refineTaken = 0;
    static int timeframe = 5;
    static int immidiateFailTaken = 1;
    static int threeOppositeFailTaken = 1;
    static int candleSizeTaken = 1;

    public static void main(String[] args) {
        StockAnalysis ana = new StockAnalysis();
        int sellFlag = 0, buyFlag = 0;
        Stock s = null;
        try {
            stopdate = formatter.parse("31-07-2015,14:45:00");
            temphighBean.pvtTime = formatter.parse("28-03-2010,14:05:00");
            templowBean.pvtTime = formatter.parse("28-03-2010,14:05:00");
        } catch (ParseException ex) {
            Logger.getLogger(PivotForwardTester.class.getName()).log(Level.SEVERE, null, ex);
        }
        String symbol = "nse_1min";
        try {
            s = ana.getDatafromDB(symbol);
        } catch (IOException ex) {
            Logger.getLogger(PivotStrategy.class.getName()).log(Level.SEVERE, null, ex);
        }
        LinkedList mainList = s.getStockList();

        s = ChangeTimeFrame.changeTimeFrame(mainList, s, timeframe);
        LinkedList ls = s.getStockList();
        ana.addIndicators(s);
        StringBuilder data = new StringBuilder();
        int no_of_shares = 0;
        double rupee = 2000;
        double profit = 0;
        double highest = 0;
        Date debugDate = null;
        int tradeActivated = 0, RR = 1;
        boolean buy = false, sell = false;
        double buyPrice = 0, sellPrice = 0, target = 0, stoploss = 0, points;
        double totalPoints, totalBuyPoints, totalSellPoints, buySuccessPoints = 0, buyFailedPoints = 0, sellSuccessPoints = 0, sellFailedPoints = 0, sellCanceledPoints = 0, buyCanceledPoints = 0;
        int totalTrades, totalBuyTrades, totalSellTrades, buySuccessTrades = 0, buyFailedTrades = 0, sellSuccessTrades = 0, sellFailedTrades = 0, sellCanceledTrades = 0, buyCanceledTrades = 0;
        PivotPointBean highBean = null, lowBean = null;
        String signalData = "";
        String targetData = "";
        boolean gapOpenDay = false;
        StockPrice callPrice = null;
        HashMap indicators = s.getIndicators();
        double BBUpper[] = ((double[]) indicators.get(ApplicationConstants.BBUpper));
        double BBLower[] = ((double[]) indicators.get(ApplicationConstants.BBLower));
        double BBMid[] = ((double[]) indicators.get(ApplicationConstants.BBSma));
        double ATR[] = ((double[]) indicators.get(ApplicationConstants.ATR));
        double ADX[] = ((double[]) indicators.get(ApplicationConstants.ADX));
        double RSI[] = ((double[]) indicators.get(ApplicationConstants.RSI));
        LinkedList tradeList = new LinkedList();
        TradeBean tb = null;
//        try {
//                
//                PivotPoints.refine( getPivots(ls, 0), 10);
//            } catch (ParseException ex) {
//                Logger.getLogger(PivotStrategy.class.getName()).log(Level.SEVERE, null, ex);
//            }
        for (int i = 3; i < ls.size(); i++) {

            StockPrice sp1 = (StockPrice) ls.get(i);
            PivotPointBean[] cpivots = refinePivots(generatePivot(sp1, -0.5, ((StockPrice) ls.get(i - 1))), refineTaken);
            if (!gapOpenDay || gapTaken == 0) {
                highBean = cpivots[1];
                lowBean = cpivots[0];
            } else {
                long minTime = Math.min(cpivots[0].pvtTime.getTime(), cpivots[1].pvtTime.getTime());
                Date mind = new Date(minTime);
                if (formatter1.format(highBean.pvtTime).equals(formatter1.format(mind))) {
                    highBean = cpivots[0];
                    lowBean = cpivots[1];
                    gapOpenDay = false;
                } else {
                    cpivots[1] = highBean;
                    cpivots[0] = lowBean;
                }
            }

            if (i > 1 && PivotBackTester.isGapOpen(sp1, ls.get(i - 1), ATR[i])) {
                gapOpenDay = true;
                highBean = new PivotPointBean();
                lowBean = new PivotPointBean();
                highBean.pivotAtCandle = sp1;
                highBean.pvttype = "high";
                highBean.pvtvalue = sp1.getHigh();
                highBean.pvtTime = sp1.getDateTime();
                lowBean.pvttype = "low";
                lowBean.pivotAtCandle = sp1;
                lowBean.pvtvalue = sp1.getLow();
                lowBean.pvtTime = sp1.getDateTime();
                continue;
            }

            if (Test.priceWithintime(sp1.getDateTime()) || 1 == 1) {
                if (isBuyTriggered(sp1, cpivots, buy, s, i) && buyFlag == 0) {
                    if (sell) {
                        if (debug) {
                            System.out.println("short cancelled for buy ");
                            System.out.println("----------------------------------------------------------------------------------------------------------------------------");
                        }
                        sell = false;
                        points = sellPrice - sp1.getClose();
                        sellCanceledPoints += points;
                        sellCanceledTrades++;
                        profit += no_of_shares * points;
                        highest = Math.min(sp1.getLow(), highest);
                        data.append("," + sp1.getDateTime() + "," + sp1.getClose() + "," + points + "," + no_of_shares + ",Canceled," + highest);
                    }
                    highest = sp1.getClose();;
                    buy = true;
                    sellFlag = 1;
                    no_of_shares = (int) (rupee / sp1.getClose());
                    buyPrice = sp1.getClose();
                    callPrice = sp1;
                    //target = Math.min(sp1.getClose() + ((sp1.getClose() - lowBean.pvtvalue) * RR),sp1.getClose()+20);
                    target = sp1.getClose() + ((sp1.getClose() - lowBean.pvtvalue) * RR);
                    stoploss = Math.max(sp1.getClose() - ((sp1.getClose() - lowBean.pvtvalue)), sp1.getClose() - 70);
                    threeInsideBar = 0;
//                        showTrayIcon("BUY","at price:: "+buyPrice+" :: Target :: "+target+" :: "+stoploss+"\n Current Time :: " + ls.get(ls.size() - 1));
                    signalData = "BUY :: at time :: " + sp1.getDateTime() + " price:: " + buyPrice + " :: Target :: " + target + " :: " + stoploss + "\n Current Time :: " + ls.get(ls.size() - 1);
                    //  stoploss = sp1.getLow();
                    tb = new TradeBean();
                    tb.setCallPrice(buyPrice);
                    tb.setCallDate(sp1.getDateTime());
                    tb.setHighbean(highBean);
                    tb.setLowbean(lowBean);
                    tb.setTarget(target);
                    tb.setStoploss(stoploss);
                    tb.setAtr(ATR[i]);
                    tb.setBbMid(new double[]{BBMid[i - 2], BBMid[i - 2], BBMid[i - 1], BBMid[i]});
                    tb.setRsi(new double[]{RSI[i - 2], RSI[i - 2], RSI[i - 1], RSI[i]});
                    tb.setAdx(new double[]{ADX[i - 2], ADX[i - 2], ADX[i - 1], ADX[i]});

                    data.append("\n" + symbol + ",BUY," + sp1.getDateTime() + "," + buyPrice + "," + target + "," + stoploss + "," + lowBean.pvtvalue + "," + highBean.pvtvalue);
                    if (i > 2) {
                        data.append("," + BBMid[i - 2] + " :: " + BBMid[i - 2] + " :: " + BBMid[i - 1] + " :: " + BBMid[i]);
                        data.append("," + ATR[i] + "," + ADX[i - 2] + " :: " + ADX[i - 2] + " :: " + ADX[i - 1] + " :: " + ADX[i]);
                        data.append("," + RSI[i - 2] + " :: " + RSI[i - 2] + " :: " + RSI[i - 1] + " :: " + RSI[i]);
                    }
                    if (debug) {
                        System.out.println("----------------------------------------------------------------------------------------------------------------------------");
                        System.out.println("Buy Started at " + sp1 + "  Target  " + target + " stoploss " + stoploss);
                        System.out.println(" Low " + lowBean.pvttype + "  " + lowBean.pvtvalue + "   " + lowBean.pvtTime);
                        System.out.println(" High " + highBean.pvttype + "  " + highBean.pvtvalue + "   " + highBean.pvtTime);
                        if (i > 3) {
                            System.out.println(" BB ::  " + "  " + BBMid[i - 2] + "  " + BBMid[i - 2] + "  " + BBMid[i - 1] + "  " + BBMid[i]);
                            System.out.println(" ATR " + ATR[i]);
                        }

                    }

                } else if (isSellTriggered(sp1, cpivots, i, s, sell) && sellFlag == 0 && sellTaken == 1) {
                    if (buy) {
                        if (debug) {
                            System.out.println("buy cancelled for short ");
                            System.out.println("----------------------------------------------------------------------------------------------------------------------------");
                        }
                        buy = false;
                        points = sp1.getClose() - buyPrice;
                        buyCanceledPoints += points;
                        buyCanceledTrades++;
                        profit += no_of_shares * points;
                        highest = Math.max(sp1.getHigh(), highest);
                        data.append("," + sp1.getDateTime() + "," + sp1.getClose() + "," + points + "," + no_of_shares + ",Canceled," + highest);
                    }
                    threeInsideBar = 0;
                    highest = sp1.getClose();;
                    sell = true;
                    buyFlag = 1;
                    callPrice = sp1;
                    no_of_shares = (int) (rupee / sp1.getClose());
                    sellPrice = sp1.getClose();
                    //target =  Math.min(sp1.getClose() - ((highBean.pvtvalue - sp1.getClose()) * RR),sp1.getClose()-20);
                    target = sp1.getClose() - ((highBean.pvtvalue - sp1.getClose()) * RR);
                    stoploss = Math.min(sp1.getClose() + 70, sp1.getClose() + ((highBean.pvtvalue - sp1.getClose())));

                    tb = new TradeBean();
                    tb.setCallPrice(sellPrice);
                    tb.setCallDate(sp1.getDateTime());
                    tb.setHighbean(highBean);
                    tb.setLowbean(lowBean);
                    tb.setTarget(target);
                    tb.setStoploss(stoploss);
                    tb.setAtr(ATR[i]);
                    tb.setBbMid(new double[]{BBMid[i - 2], BBMid[i - 2], BBMid[i - 1], BBMid[i]});
                    tb.setRsi(new double[]{RSI[i - 2], RSI[i - 2], RSI[i - 1], RSI[i]});
                    tb.setAdx(new double[]{ADX[i - 2], ADX[i - 2], ADX[i - 1], ADX[i]});

                    //    stoploss = sp1.getHigh();
//                        showTrayIcon("SELL","at price:: "+sellPrice+" :: Target :: "+target+" :: "+stoploss+"\n Current Time :: " + ls.get(ls.size() - 1));
                    signalData = "SELL :: at price:: " + sellPrice + " :: Target :: " + target + " :: " + stoploss + "\n Current Time :: " + ls.get(ls.size() - 1);
                    data.append("\n" + symbol + ",SELL," + sp1.getDateTime() + "," + sellPrice + "," + target + "," + stoploss + "," + lowBean.pvtvalue + "," + highBean.pvtvalue);
                    if (i > 2) {
                        data.append("," + BBMid[i - 2] + " :: " + BBMid[i - 2] + " :: " + BBMid[i - 1] + " :: " + BBMid[i]);
                        data.append("," + ATR[i] + "," + ADX[i - 2] + " :: " + ADX[i - 2] + " :: " + ADX[i - 1] + " :: " + ADX[i]);
                        data.append("," + RSI[i - 2] + " :: " + RSI[i - 2] + " :: " + RSI[i - 1] + " :: " + RSI[i]);
                    }
                    if (debug) {
                        System.out.println("----------------------------------------------------------------------------------------------------------------------------");
                        System.out.println("Short Started  " + sp1 + "  Target  " + target + " stoploss " + stoploss);
                        System.out.println(" Low " + lowBean.pvttype + "  " + lowBean.pvtvalue + "   " + lowBean.pvtTime);
                        System.out.println(" High " + highBean.pvttype + "  " + highBean.pvtvalue + "   " + highBean.pvtTime);
                        if (i > 3) {
                            System.out.println(" BB ::  " + "  " + BBMid[i - 2] + "  " + BBMid[i - 2] + "  " + BBMid[i - 1] + "  " + BBMid[i]);
                            System.out.println(" ATR " + ATR[i]);
                        }
                    }
                }
            }
            if (buy && buyFlag == 0) {
                highest = Math.max(sp1.getHigh(), highest);
                if (isbuyTargetReached(sp1, target, buyPrice, stoploss, i, s)) {
                    buy = false;
                    sellFlag = 0;
                    points = sp1.getClose() - buyPrice;
                    successContinue = false;
                    buySuccessPoints += points;
                    buySuccessTrades++;
                    profit += no_of_shares * points;
                    data.append("," + sp1.getDateTime() + "," + sp1.getClose() + "," + points + "," + no_of_shares + ",Reached," + highest);
//                         showTrayIcon("BUY Reached","at price:: "+sp1.getClose()+" :: Time :: "+sp1.getDateTime()+" :: "+"\n Current Time :: " + ls.get(ls.size() - 1));
                    targetData = "BUY Reached :: at price:: " + sp1.getClose() + " :: Time :: " + sp1.getDateTime() + " :: " + "\n Current Time :: " + ls.get(ls.size() - 1);
                    if (debug) {
                        System.out.println("buy target reached at " + sp1
                                + "\nTrades  " + buySuccessTrades + " points  " + points + ""
                                + "   " + buySuccessPoints);
                        System.out.println("----------------------------------------------------------------------------------------------------------------------------");
                    }
                } else if (isBuyFailed(sp1, stoploss, callPrice, ls, i)) {
                    buyFailedPoints += sp1.getClose() - buyPrice;
                    buyFailedTrades++;
                    points = sp1.getClose() - buyPrice;
                    profit += no_of_shares * points;
                    buy = false;
                    sellFlag = 0;
                    data.append("," + sp1.getDateTime() + "," + sp1.getClose() + "," + points + "," + no_of_shares + ",Failed," + highest);
//                        showTrayIcon("BUY Failed","at price:: "+sp1.getClose()+" :: Time :: "+sp1.getDateTime()+" :: "+"\n Current Time :: " + ls.get(ls.size() - 1));
                    targetData = "BUY Failed :: at price:: " + sp1.getClose() + " :: Time :: " + sp1.getDateTime() + " :: " + "\n Current Time :: " + ls.get(ls.size() - 1);
                    if (debug) {
                        System.out.println("buy failed at  " + sp1
                                + "\ntrades  " + buyFailedTrades + " points  " + points + "   " + buyFailedPoints);
                        System.out.println("----------------------------------------------------------------------------------------------------------------------------");
                    }
                }

            }
            if (sell && sellFlag == 0 && sellTaken == 1) {
                highest = Math.min(sp1.getLow(), highest);
                if (isSellTargetReached(sp1, target, buyPrice, stoploss, i, s)) {
                    successContinue = false;
                    sell = false;
                    buyFlag = 0;
                    points = sellPrice - sp1.getClose();
                    sellSuccessPoints += points;
                    profit += no_of_shares * points;
                    sellSuccessTrades++;
                    data.append("," + sp1.getDateTime() + "," + sp1.getClose() + "," + points + "," + no_of_shares + ",Reached," + highest);
//                        showTrayIcon("SELL Reached","at price:: "+sp1.getClose()+" :: Time :: "+sp1.getDateTime()+" :: "+"\n Current Time :: " + ls.get(ls.size() - 1));
                    targetData = "SELL Reached :: at price:: " + sp1.getClose() + " :: Time :: " + sp1.getDateTime() + " :: " + "\n Current Time :: " + ls.get(ls.size() - 1);
                    if (debug) {
                        System.out.println("short target reached at " + sp1
                                + "\nTrades  " + sellSuccessTrades + " points  " + points + "   " + sellSuccessPoints);
                        System.out.println("----------------------------------------------------------------------------------------------------------------------------");
                    }
                } else if (isSellFailed(sp1, stoploss, callPrice, ls, i)) {
                    points = sellPrice - sp1.getClose();
                    sellFailedPoints += points;
                    sellFailedTrades++;
                    profit += no_of_shares * points;
                    data.append("," + sp1.getDateTime() + "," + sp1.getClose() + "," + points + "," + no_of_shares + ",Failed," + highest);
//                        showTrayIcon("SELL Failed","at price:: "+sp1.getClose()+" :: Time :: "+sp1.getDateTime()+" :: "+"\n Current Time :: " + ls.get(ls.size() - 1));
                    targetData = "SELL Failed :: at price:: " + sp1.getClose() + " :: Time :: " + sp1.getDateTime() + " :: " + "\n Current Time :: " + ls.get(ls.size() - 1);
                    if (debug) {
                        System.out.println("short failed at " + sp1
                                + "\ntrades " + sellFailedTrades + "  points  " + points + "   " + sellFailedPoints);
                        System.out.println("----------------------------------------------------------------------------------------------------------------------------");
                    }
                    sell = false;
                    buyFlag = 0;
                }

            }

        }
        totalBuyPoints = buySuccessPoints + buyFailedPoints + buyCanceledPoints;
        totalSellPoints = sellSuccessPoints + sellFailedPoints + sellCanceledPoints;
        totalBuyTrades = buySuccessTrades + buyFailedTrades + buyCanceledTrades;
        totalSellTrades = sellSuccessTrades + sellFailedTrades + sellCanceledTrades;
        totalPoints = totalBuyPoints + totalSellPoints;
        totalTrades = totalBuyTrades + totalSellTrades;

//            System.out.println("Total Profit :: " + profit + "  shares bought " + no_of_shares);
        System.out.println("Total Cost :: " + (-totalTrades * 2) + "  totalTrades :: " + totalTrades + " totalPoints " + totalPoints);
        System.out.println("totalBuyTrades " + totalBuyTrades + " totalBuyPoints  " + totalBuyPoints + "  buy " + buySuccessTrades + " points " + buySuccessPoints + " failed " + buyFailedTrades + "  " + buyFailedPoints);
        System.out.println("totalSellTrades " + totalSellTrades + " totalSellPoints " + totalSellPoints + " sell " + sellSuccessTrades + " points " + sellSuccessPoints + " failed " + sellFailedTrades + "  " + sellFailedPoints);
        System.out.println("sell " + sellCanceledTrades + " points " + sellCanceledPoints + " buy  " + buyCanceledTrades + "  " + buyCanceledPoints);
        System.out.println("----------------------------------------------------------------------------------------------------------------------------");
        System.out.println(data);
//                            System.out.pri
    }

    private static boolean isBuyTriggered(StockPrice sp1, PivotPointBean[] cPivots, boolean active, Stock s, int i) {
        PivotPointBean highBean = cPivots[1], lowBean = cPivots[0];
        boolean isTriggered = false;
        if (sp1.getClose() - highBean.pvtvalue >2){
                isTriggered = true;
                triggerPivot = highBean.pvtvalue;
            
        }
        HashMap indicators = s.getIndicators();
        double BBMid[] = ((double[]) indicators.get(ApplicationConstants.BBSma));

        if (BBMid[i] > 200) {
            isTriggered = false;
        }
        if (active) {
            isTriggered = false;
        }

        return isTriggered;
    }

    private static boolean isbuyTargetReached(StockPrice sp1, double target, double buyPrice, double stoploss, int i, Stock s) {
        boolean targetReached = false;
        LinkedList ls = s.getStockList();
        if (successContinue) {
            if (i > 1) {
                StockPrice spOld = (StockPrice) ls.get(i - 1);
                if (!PivotPoints.inSideBar(spOld, sp1,0)) {
                    threeInsideBar = 0;
                    if (sp1.getClose() >= spOld.getClose()) {
                    } else {
                        targetReached = true;
                    }
                } else {
                    threeInsideBar++;
                }
            } else {
                targetReached = true;
            }
            if (threeInsideBar == 3) {
                threeInsideBar = 0;
                targetReached = true;
            }
        }
        if (sp1.getHigh() - target > 0 && !successContinue) {
            stoploss = buyPrice;
//            System.out.println("buy success continueing..");
            successContinue = true;
        }

        if (formatter.format(sp1.getDateTime()).contains("15:30:00")) {
            targetReached = true;
        }
        return targetReached;
    }
      private static boolean isSellTriggered(StockPrice sp1, PivotPointBean[] cpivots, int i, Stock s, boolean active) {
        PivotPointBean highBean = cpivots[1], lowBean = cpivots[0];
        boolean isTriggered = false;
        if (stopdate.getTime() < sp1.getDateTime().getTime()) {
            String ss = "stop";
        }

        if (lowBean.pvtvalue - sp1.getClose() > 2 ){
                isTriggered = true;
                triggerPivot = highBean.pvtvalue;
            
        }
        HashMap indicators = s.getIndicators();
        double BBMid[] = ((double[]) indicators.get(ApplicationConstants.BBSma));

        if (active) {
            isTriggered = false;
        }
        return isTriggered;
    }

    private static boolean isSellTargetReached(StockPrice sp1, double target, double buyPrice, double stoploss, int i, Stock s) {
        boolean targetReached = false;
        LinkedList ls = s.getStockList();
        if (successContinue) {
            if (i > 1) {
                StockPrice spOld = (StockPrice) ls.get(i - 1);
                if (!PivotPoints.inSideBar(spOld, sp1,0)) {
                    threeInsideBar = 0;
                    if (sp1.getClose() <= spOld.getClose()) {
                    } else {
                        targetReached = true;
                    }
                } else {
                    threeInsideBar++;
                }
            } else {
                targetReached = true;
            }
            if (threeInsideBar == 3) {
                threeInsideBar = 0;
                targetReached = true;
            }
        }
        if (sp1.getLow() - target < 0 && !successContinue) {
            stoploss = buyPrice;
//            System.out.println("Short success continueing..");
            successContinue = true;
        }

        if (formatter.format(sp1.getDateTime()).contains("15:30:00")) {
            targetReached = true;
        }
        return targetReached;
    }
  public static boolean isSellFailed(StockPrice sp1, double stoploss, StockPrice callPrice, LinkedList ls, int i) {
        boolean flag = false;

        if (sp1.getClose() >= stoploss) {
            flag = true;
        }
        return flag;
    }

    public static boolean isBuyFailed(StockPrice sp1, double stoploss, StockPrice callPrice, LinkedList ls, int i) {
        boolean flag = false;
        if (sp1.getClose() <= stoploss) {
            flag = true;
        }
        return flag;
    }


}
