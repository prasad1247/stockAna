/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import static app.PivotPoints.formatter;
import static app.PivotPoints.getPivots;
import static app.PivotPoints.refine;
import bean.PivotPointBean;
import bean.Stock;
import bean.StockPrice;
import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.ApplicationConstants;
import util.ApplicationUtils;

/**
 *
 * @author PRASAD
 */
public class PivotBackTester extends TimerTask {

    static boolean debug = false;
    static SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");
    static boolean successContinue = false;

    public static void main(String[] args) {
        Timer t = new Timer();
        PivotBackTester pp = new PivotBackTester();
        t.schedule(pp, 1000, 20000);
//        showTrayIcon();
    }

    @Override
    public void run() {
        getPivotsignal();
    }

    public static void getPivotsignal() {
        double totalp = 0;
        int trader = 0;
        StockAnalysis ana = new StockAnalysis();
        Stock s = null;
        String symbol = "";

        LinkedList symbolList = ApplicationUtils.readSymbols(ApplicationConstants.SYMBOL_LIST_PATH);
        for (Object sym : symbolList) {
            symbol = (String) sym;
            symbol = "NIFTY";
            try {
                s = ana.getDatafromFile(symbol);
            } catch (IOException ex) {
                Logger.getLogger(PivotStrategy.class.getName()).log(Level.SEVERE, null, ex);
                
            }
            System.out.println("-------------------------------------------------------------" + symbol + "--------------------------------------------------------------");
            java.sql.Date d = null;
            LinkedList mainList = s.getStockList();
            s = ChangeTimeFrame.changeTimeFrame(mainList, s, 5);
            LinkedList ls = s.getStockList();
            LinkedList pvtList;
            LinkedList refinedList = null;
            try {
                pvtList = getPivots(ls, 0);
                refinedList = refine(pvtList, 30);
            } catch (ParseException ex) {
                Logger.getLogger(PivotStrategy.class.getName()).log(Level.SEVERE, null, ex);
            }
//            s = ChangeTimeFrame.changeTimeFrame(mainList, s, 5);
            ana.addIndicators(s);
            ls = s.getStockList();

            int currentcandleNo = ls.size() - 1;
            StockPrice sp = (StockPrice) ls.get(currentcandleNo);
            HashMap indicators = s.getIndicators();

            PivotPointBean pbs1[] = (PivotPointBean[]) refinedList.get(refinedList.size() - 1);
            PivotPointBean temphighBean = pbs1[1];
            PivotPointBean templowBean = pbs1[0];
//            System.out.println("current pivot High ::" + temphighBean.pvtTime + "   " + temphighBean.pvtvalue + "  " + temphighBean.pivotAtCandle);
//            System.out.println("current pivot Low ::" + templowBean.pvtTime + "   " + templowBean.pvtvalue + "  " + temphighBean.pivotAtCandle);
//            System.out.println("current::   " + sp);
//            System.out.println("RSI " + ((double[]) indicators.get(ApplicationConstants.RSI))[currentcandleNo] + "  ADX  " + ((double[]) indicators.get(ApplicationConstants.ADX))[currentcandleNo] + " ATR " + ((double[]) indicators.get(ApplicationConstants.ATR))[currentcandleNo]);
//            System.out.println("BBLower " + ((double[]) indicators.get(ApplicationConstants.BBLower))[currentcandleNo] + "  BBUpper  " + ((double[]) indicators.get(ApplicationConstants.BBUpper))[currentcandleNo] + " BBSMA  " + ((double[]) indicators.get(ApplicationConstants.BBSma))[currentcandleNo]);
            String signalData = "";
            String targetData = "";
            double BBUpper[] = ((double[]) indicators.get(ApplicationConstants.BBUpper));
            double BBLower[] = ((double[]) indicators.get(ApplicationConstants.BBLower));
            double BBMid[] = ((double[]) indicators.get(ApplicationConstants.BBSma));
            double ATR[] = ((double[]) indicators.get(ApplicationConstants.ATR));
            double ADX[] = ((double[]) indicators.get(ApplicationConstants.ADX));
            double RSI[] = ((double[]) indicators.get(ApplicationConstants.RSI));
            int sellFlag = 0, buyFlag = 0;

//        for (int i = 0; i < BBUpper.length; i++) {
//            System.out.println("    " + ls.get(i));
//            System.out.println("  Lower  " + BBLower[i] + " Upper   " + BBUpper[i] + "  Mid  " + BBMid[i]);
//        }
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

            boolean gapOpenDay = false;
            try {
                debugDate = formatter.parse("11-03-2016,09:30:00");
            } catch (ParseException ex) {
                Logger.getLogger(PivotBackTester.class.getName()).log(Level.SEVERE, null, ex);
            }
            PivotPointBean highBean = null, lowBean = null;
            PivotPointBean[] cPivots = null;
            for (int i = 0; i < ls.size(); i++) {

                StockPrice sp1 = (StockPrice) ls.get(i);

                if (!gapOpenDay) {
                    cPivots = getCurrentPivots(refinedList, sp1.getDateTime(), debugDate);
                    highBean = cPivots[0];
                    lowBean = cPivots[1];
                } else {
                    cPivots = getCurrentPivots(refinedList, sp1.getDateTime(), debugDate);
                    long minTime = Math.min(cPivots[0].pvtTime.getTime(), cPivots[1].pvtTime.getTime());
                    Date mind = new Date(minTime);
                    if (formatter1.format(highBean.pvtTime).equals(formatter1.format(mind))) {
                        highBean = cPivots[0];
                        lowBean = cPivots[1];
                        gapOpenDay = false;
                    } else {
                        cPivots[0] = highBean;
                        cPivots[1] = lowBean;
                    }
                }
                if (debugDate.getTime() < sp1.getDateTime().getTime()) {
                    String ss = "stop";
                }

                if (i > 1 && isGapOpen(sp1, ls.get(i - 1), ATR[i])) {
                    gapOpenDay = true;
                    highBean = new PivotPointBean();
                    lowBean = new PivotPointBean();
                    highBean.pvttype = "high";
                    highBean.pvtvalue = sp1.getHigh();
                    highBean.pvtTime = sp1.getDateTime();
                    lowBean.pvttype = "low";
                    lowBean.pvtvalue = sp1.getLow();
                    lowBean.pvtTime = sp1.getDateTime();
                    continue;
                }
                StockPrice lowday = daysLowHigh(sp1, "low");
                if (Test.priceWithintime(sp1.getDateTime()) || 1 == 1) {

                    if (isBuyTriggered(sp1, cPivots, i, s, buy) && sp.getDateTime().getTime() > cPivots[0].pvtTime.getTime() && buyFlag == 0) {
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
                        target = sp1.getClose() + ((sp1.getClose() - lowBean.pvtvalue) * RR);
                        stoploss = sp1.getClose() - ((sp1.getClose() - lowBean.pvtvalue) );
                        threeInsideBar=0;
//                        showTrayIcon("BUY","at price:: "+buyPrice+" :: Target :: "+target+" :: "+stoploss+"\n Current Time :: " + ls.get(ls.size() - 1));
                        signalData = "BUY :: at time :: "+sp1.getDateTime()+" price:: " + buyPrice + " :: Target :: " + target + " :: " + stoploss + "\n Current Time :: " + ls.get(ls.size() - 1);
                        //  stoploss = sp1.getLow();
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

                    } else if (isSellTriggered(sp1, cPivots, i, s, sell) && sellFlag == 0 && 1==1) {
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
                      threeInsideBar=0;
                        highest = sp1.getClose();;
                        sell = true;
                        buyFlag = 1;
                        no_of_shares = (int) (rupee / sp1.getClose());
                        sellPrice = sp1.getClose();
                        target = sp1.getClose() - ((highBean.pvtvalue - sp1.getClose()) * RR);
                        stoploss = sp1.getClose()+ ((highBean.pvtvalue - sp1.getClose()));
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
                    } else if (sp1.getClose() <= stoploss) {
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
                if (sell && sellFlag == 0 && 1==1) {
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
                    } else if (sp1.getClose() >= stoploss) {
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
            totalp += profit;
            trader += totalTrades;
//            System.out.println("Total Profit :: " + profit + "  shares bought " + no_of_shares);
            System.out.println("Total Cost :: " + (-totalTrades * 2) + "  totalTrades :: " + totalTrades + " totalPoints " + totalPoints);
            System.out.println("totalBuyTrades " + totalBuyTrades + " totalBuyPoints  " + totalBuyPoints + "  buy " + buySuccessTrades + " points " + buySuccessPoints + " failed " + buyFailedTrades + "  " + buyFailedPoints);
            System.out.println("totalSellTrades " + totalSellTrades + " totalSellPoints " + totalSellPoints + " sell " + sellSuccessTrades + " points " + sellSuccessPoints + " failed " + sellFailedTrades + "  " + sellFailedPoints);
            System.out.println("sell " + sellCanceledTrades + " points " + sellCanceledPoints + " buy  " + buyCanceledTrades + "  " + buyCanceledPoints);
            System.out.println("----------------------------------------------------------------------------------------------------------------------------");
//                            System.out.println("Buy :: "+signalData);
//                            System.out.println("Target :: "+targetData);
//            System.out.println("----------------------------------------------------------------------------------------------------------------------------");
            System.out.println(data);
//            if (buy) {
//                showTrayIcon("Buy", signalData);
//                System.out.println("Buy :: "+signalData);
//                System.out.println("Time :: "+new Date());
//            } else if (sell) {
//                showTrayIcon("SELL", signalData);
//                System.out.println("SELL :: "+signalData);
//                System.out.println("Time :: "+new Date());
//            } else{
//                showTrayIcon("Target", targetData);
//                System.out.println("Target :: "+targetData);
//                System.out.println("Time :: "+new Date());
//            }
            break;
        }
//        System.out.println("total profit " + totalp + "  trades  " + trader);
    }

    public static PivotPointBean[] getCurrentPivots(LinkedList refinedList, Date currentDate, Date stopdate) {
        PivotPointBean pivots[] = new PivotPointBean[2];
        PivotPointBean temphighBean = null, templowBean = null;
        PivotPointBean highBean = new PivotPointBean(), lowBean = new PivotPointBean();
        for (int j = 0; j < refinedList.size(); j++) {
            PivotPointBean pbs1[] = (PivotPointBean[]) refinedList.get(j);
            temphighBean = pbs1[1];
            templowBean = pbs1[0];

            if (stopdate != null && stopdate.getTime() < temphighBean.pvtTime.getTime()) {
                String s = "Stp";
            }

            if ((Math.max(temphighBean.pvtTime.getTime(), templowBean.pvtTime.getTime()) > currentDate.getTime() && j > 1)) {
                try {
                    highBean = ((PivotPointBean[]) refinedList.get(j - 1))[1];
                    lowBean = ((PivotPointBean[]) refinedList.get(j - 1))[0];
                    break;
                } catch (Exception e) {
                    System.out.println("aa");
                }

            } else if (j == refinedList.size() - 1) {
                try {
                    highBean = ((PivotPointBean[]) refinedList.get(j))[1];
                    lowBean = ((PivotPointBean[]) refinedList.get(j))[0];
                    break;
                } catch (Exception e) {
                    System.out.println("aa");
                }
            }
        }
        pivots[0] = highBean;
        pivots[1] = lowBean;
        return pivots;
    }

    private static boolean isBuyTriggered(StockPrice sp1, PivotPointBean[] cPivots, int i, Stock s, boolean active) {
        PivotPointBean highBean = cPivots[0], lowBean = cPivots[1];
        boolean isTriggered = false;
        if (sp1.getClose() > highBean.pvtvalue) {
            isTriggered = true;
        }
        HashMap indicators = s.getIndicators();
        double BBMid[] = ((double[]) indicators.get(ApplicationConstants.BBSma));
//        if (i > 2 && BBMid[i - 2] < BBMid[i - 1] && BBMid[i - 1] < BBMid[i] && Math.signum(BBMid[i - 2]) == 1) {
//            isTriggered = true;
//        } else {
//            isTriggered = false;
//        }
        if (BBMid[i] > 200) {
            isTriggered = false;
        }
        if (active) {
            isTriggered = false;
        }
//        if (sp1.getDateTime().getTime() - highBean.pvtTime.getTime() > (7 * 60 * 60 * 1000)) {
//            isTriggered = false;
//        }

        return isTriggered;

    }

    private static boolean isSellTriggered(StockPrice sp1, PivotPointBean[] cPivots, int i, Stock s, boolean active) {
        PivotPointBean highBean = cPivots[0], lowBean = cPivots[1];
        boolean isTriggered = false;
        if (sp1.getClose() < lowBean.pvtvalue && !active) {
            isTriggered = true;
        }
        HashMap indicators = s.getIndicators();
        double BBMid[] = ((double[]) indicators.get(ApplicationConstants.BBSma));
//        if (i > 2 && BBMid[i - 2] > BBMid[i - 1] && BBMid[i - 1] > BBMid[i] && Math.signum(BBMid[i - 2]) == -1) {
//            isTriggered = true;
//        } else {
//            isTriggered = false;
//        }

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
                if (!inSideBar(spOld, sp1)) {
                    if (sp1.getClose() >= spOld.getClose()) {
                    } else {
                        targetReached = true;
                    }
                }else{
                    threeInsideBar++;
                }
            } else {
                targetReached = true;
            }
            if(threeInsideBar==3){
                threeInsideBar=0;
                targetReached=true;
            }
        }
        if (sp1.getHigh() >= target && !successContinue) {
            stoploss = buyPrice;
//            System.out.println("buy success continueing..");
            successContinue = true;
        }

        if (formatter.format(sp1.getDateTime()).contains("15:30:00")) {
            targetReached = true;
        }
        return targetReached;
    }
static int threeInsideBar=0;
    private static boolean isSellTargetReached(StockPrice sp1, double target, double buyPrice, double stoploss, int i, Stock s) {
        boolean targetReached = false;
        LinkedList ls = s.getStockList();
        if (successContinue) {
            if (i > 1) {
                StockPrice spOld = (StockPrice) ls.get(i - 1);
                if (!PivotPoints.inSideBar(spOld, sp1,0)) {
                    if (sp1.getClose() <= spOld.getClose()) {
                    } else {
                        targetReached = true;
                    }
                }else{
                    threeInsideBar++;
                }
            } else {
                targetReached = true;
            }
            if(threeInsideBar==3){
                threeInsideBar=0;
                targetReached=true;
            }
        }
        if (sp1.getLow() <= target && !successContinue) {
            stoploss = buyPrice;
//            System.out.println("Short success continueing..");
            successContinue = true;
        }

        if (formatter.format(sp1.getDateTime()).contains("15:30:00")) {
            targetReached = true;
        }
        return targetReached;
    }

    public static boolean isGapOpen(StockPrice sp1, Object spOld1, double atr) {
        StockPrice spOld = (StockPrice) spOld1;
        boolean gap = false;
        if (formatter.format(spOld.getDateTime()).contains("15:30:00")) {
            if (Math.abs(spOld.getClose() - sp1.getOpen()) >= atr) {
                gap = true;
            }
        }
        return gap;
    }

    static StockPrice daysLowCandle;
    static StockPrice daysHighCandle;

    public static StockPrice daysLowHigh(StockPrice sp1, String lowHigh) {
        if (daysLowCandle == null || !formatter1.format(daysLowCandle.getDateTime()).equals(formatter1.format(sp1.getDateTime()))) {
            daysLowCandle = sp1;
        } else {

            if (lowHigh.equals("low")) {
                if (daysLowCandle.getClose() > sp1.getClose()) {
                    daysLowCandle = sp1;
                }
            }
        }

        return daysLowCandle;
    }
    static StockPrice tempoldsp;

    public static boolean inSideBar(StockPrice oldsp, StockPrice sp) {
        boolean flag = false;
        if (tempoldsp == null) {
            tempoldsp = oldsp;
        }
        if (tempoldsp.getOpen() > oldsp.getClose() && tempoldsp.getClose() < oldsp.getClose()) {

        } else if (tempoldsp.getOpen() < oldsp.getClose() && tempoldsp.getClose() > oldsp.getClose()) {

        } else {
            tempoldsp = oldsp;
        }

        if (tempoldsp.getOpen() > sp.getClose() && tempoldsp.getClose() < sp.getClose()) {
//            System.out.println("sp iis insidebar -ve " + sp);
            flag = true;
        } else if (tempoldsp.getOpen() < sp.getClose() && tempoldsp.getClose() > sp.getClose()) {
//            System.out.println("sp iis insidebar +ve " + sp);
            flag = true;
        } else {
            tempoldsp = null;
        }

        return flag;
    }
    static Image image = Toolkit.getDefaultToolkit().getImage("images/arrow-prev.png");

    static TrayIcon trayIcon = new TrayIcon(image, "Tester2");

    public static void showTrayIcon(String signal, String data) {

        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();
            tray.remove(trayIcon);
            trayIcon.setImageAutoSize(true);
            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                System.err.println("TrayIcon could not be added.");
            }
            trayIcon.displayMessage(signal, data, TrayIcon.MessageType.INFO);
        }
    }

}
