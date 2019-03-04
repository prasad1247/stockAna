/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import bean.PivotPointBean;
import bean.Stock;
import bean.StockPrice;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
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
public class PivotPoints extends TimerTask {

    static SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy,HH:mm:ss");

    public static void main(String[] args) {
        System.setProperty("http.proxyHost", "10.0.3.111");
        System.setProperty("http.proxyPort", "8080");
        Timer t = new Timer();
        PivotPoints pp = new PivotPoints();
        t.schedule(pp, 1000, 120000);
    }

    public static LinkedList refine(LinkedList pvtList, double RefineVal) throws ParseException {
        double tempPrice;
        PivotPointBean temphighBean = new PivotPointBean(), templowBean = new PivotPointBean();
        temphighBean.pvtTime = formatter.parse("15-02-2015,14:40:00");
        LinkedList refinedPivots = new LinkedList();
        LinkedList refinedPivots1 = new LinkedList();
        LinkedList refinedPivots2 = new LinkedList();
        for (Object o : pvtList) {
            PivotPointBean pbs[] = new PivotPointBean[2];
            PivotPointBean pb = (PivotPointBean) o;
            if (pb.pvttype == "low") {
                if (Math.abs(temphighBean.pvtvalue - pb.pvtvalue) > RefineVal) {
                    templowBean = pb;
                } else {
                    if (Math.abs(templowBean.pvtvalue - pb.pvtvalue) > RefineVal) {
                        templowBean = pb;
                    } else {
                        tempPrice = Math.min(templowBean.pvtvalue, pb.pvtvalue);
                        if (tempPrice != templowBean.pvtvalue) {
                            templowBean = pb;
                        }
                    }
                }
                refinedPivots.add(templowBean);
//                        System.out.println("Low pivot "+templowBean.pvtvalue+"  "+templowBean.pvtTime);
            }
            pbs[0] = templowBean;
            if (pb.pvttype == "high") {
                if (Math.abs(pb.pvtvalue - templowBean.pvtvalue) > RefineVal) {
                    temphighBean = pb;
                } else {
                    if (Math.abs(temphighBean.pvtvalue - pb.pvtvalue) > RefineVal) {
                        temphighBean = pb;
                    } else {
                        tempPrice = Math.max(temphighBean.pvtvalue, pb.pvtvalue);
                        if (tempPrice != temphighBean.pvtvalue) {
                            temphighBean = pb;
                        }
                    }
                }
//                        System.out.println("High pivot "+temphighBean.pvtvalue+"  "+temphighBean.pvtTime);
                refinedPivots1.add(temphighBean);

            }
            pbs[1] = temphighBean;
            refinedPivots2.add(pbs);
        }
        for (Object o : refinedPivots2) {
            PivotPointBean pbs1[] = (PivotPointBean[]) o;
//            System.out.println("-------------------------------------------------------------------------------");
//            System.out.println(" Low " + pbs1[0].pvttype + "  " + pbs1[0].pvtvalue + "   " + pbs1[0].pvtTime);
//            System.out.println(" High " + pbs1[1].pvttype + "  " + pbs1[1].pvtvalue + "   " + pbs1[1].pvtTime);
        }
        return refinedPivots2;
    }

    public static LinkedList getPivots(LinkedList ls, double refineVal) throws ParseException {

        Date stopdate = formatter.parse("17-02-2016,12:20:00");
        double temppvtlow = 999999999, pvtlow = 0, pvthigh = 0, temppvthigh = 0;
        LinkedList pivotlist = new LinkedList();
        int low = 1, high = 0;
        for (int i = 1; i < ls.size(); i++) {
            StockPrice sp1 = (StockPrice) ls.get(i);

            if (!inSideBar((StockPrice) ls.get(i - 1), sp1, 0) || 0 == 0) {
                if (stopdate.getTime() < sp1.getDateTime().getTime()) {
                    String ss = "stop";
                }
                if (sp1.getLow() - temppvtlow <= refineVal && low == 1) {
                    temppvtlow = sp1.getLow();
                } else if (high == 0) {
                    pvtlow = temppvtlow;

                    PivotPointBean pb = new PivotPointBean();
                    pb.pivotAtCandle = (StockPrice) ls.get(i - 1);
                    pb.pvtvalue = temppvtlow;
                    pb.pvttype = "low";
                    pb.pvtTime = pb.pivotAtCandle.getDateTime();
                    pivotlist.add(pb);
//                    System.out.println("pvt  :: " + pb.pvttype + "  " + pb.pvtvalue+"  "+pb.pvtTime);
                    low = 0;
                    high = 1;
                    temppvthigh = 0;

                }
                if (sp1.getHigh() - temppvthigh >= refineVal && high == 1) {
                    temppvthigh = sp1.getHigh();
                } else if (low == 0) {
                    pvthigh = temppvthigh;

                    PivotPointBean pb = new PivotPointBean();
                    pb.pivotAtCandle = (StockPrice) ls.get(i - 1);;
                    pb.pvtvalue = temppvthigh;
                    pb.pvttype = "high";
                    pb.pvtTime = pb.pivotAtCandle.getDateTime();
                    pivotlist.add(pb);
//                    System.out.println("pvt  :: " + pb.pvttype + "  " + pb.pvtvalue+"  "+pb.pvtTime);
                    low = 1;
                    high = 0;
                    temppvtlow = 999999999;

                }
            }
        }

        return pivotlist;
    }

    @Override
    public void run() {
        try {

            StockAnalysis ana = new StockAnalysis();
            LinkedList symbolList = ApplicationUtils.readSymbols(ApplicationConstants.SYMBOL_LIST_PATH);
            SimpleDateFormat formatter1 = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat formatter2 = new SimpleDateFormat("dd-MM-yyyy");
            String symbol = "";
            boolean istoday = false;

            for (Object sym : symbolList) {

//                symbol = (String) sym;
                symbol = "HDIL";
                System.out.println("---------------------------------" + symbol + "----------------------------------------------");
                Stock s = ana.getDatafromFile(symbol);

                java.sql.Date d = null;
                s = ChangeTimeFrame.changeTimeFrame(s.getStockList(), s, 5);
                LinkedList ls = s.getStockList();
                ana.addIndicators(s);
                LinkedList pvtList = getPivots(ls, 0);
                LinkedList refinedList = refine(pvtList, 0);
                double target = 0, stoploss = 0, total = 0, c = 0;
                double target1 = 0, stoploss1 = 0, total1 = 0, c1 = 0;
                double sprice = 0, bprice = 0;
                int low = 1, high = 0, sh = 0, by = 0, act = 0;
                double ft1 = 0, st1 = 0, ft = 0, st = 0, sft = 0, bft = 0;
                int failed = 0, suu = 0, bfailed = 0, sfailed = 0;
                int failed1 = 0, suu1 = 0;
                int newFlag = 0;
                int targetMulti = 1;
                int checkCandleC = 0;
                HashMap indis = s.getIndicators();

                String lastCall = "", lastPivotH = "", lastPivotL = "", lastTarget = "";

                PivotPointBean temphighBean = null, templowBean = null;
                PivotPointBean highBean = new PivotPointBean(), lowBean = new PivotPointBean();
                Date stopdate = formatter.parse("24-02-2016,12:50:00");

                for (int i = 1; i < ls.size(); i++) {
                    StockPrice sp1 = (StockPrice) ls.get(i);
                    if (stopdate.getTime() < sp1.getDateTime().getTime()) {
//                        String ss = "stop";
                    }

                    for (int j = 0; j < refinedList.size(); j++) {
                        PivotPointBean pbs1[] = (PivotPointBean[]) refinedList.get(j);
                        temphighBean = pbs1[1];
                        templowBean = pbs1[0];
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(sp1.getDateTime());

                        int unroundedMinutes = calendar.get(Calendar.MINUTE);
                        int mod = unroundedMinutes % 5;
                        calendar.set(Calendar.MINUTE, unroundedMinutes + mod);
                        if (stopdate.getTime() < temphighBean.pvtTime.getTime()) {
//                            System.out.println("stop");
                        }

                        if (Math.max(temphighBean.pvtTime.getTime(), templowBean.pvtTime.getTime()) > calendar.getTime().getTime() && j > 1) {
                            try {
                                highBean = ((PivotPointBean[]) refinedList.get(j - 1))[1];
                                lowBean = ((PivotPointBean[]) refinedList.get(j - 1))[0];
                                break;
                            } catch (Exception e) {
                                System.out.println("aa");
                            }

                        }
                    }
                    if (istoday || formatter2.format(highBean.pvtTime).equals(formatter2.format(lowBean.pvtTime)) && formatter2.format(highBean.pvtTime).equals(formatter2.format(sp1.getDateTime()))) {

                        if (sp1.getClose() < lowBean.pvtvalue && sh == 0) {
                            target = sp1.getClose() - ((highBean.pvtvalue - sp1.getClose()) * targetMulti);
                            stoploss = sp1.getClose() + (highBean.pvtvalue - sp1.getClose());
                            c = (highBean.pvtvalue - sp1.getClose()) * targetMulti;
                            if (Test.priceWithintime(sp1.getDateTime())) {
                                System.out.println("-------------------------------------------------------------------------------");
                                if (by == 1) {
                                    total1 -= bprice - sp1.getClose();
                                    bfailed++;
                                    bft -= bprice - sp1.getClose();
                                    by = 0;
                                    System.out.println("buy cancelled for short :: " + ft1 + " points  " + (bprice - sp1.getClose()) + "   " + bfailed);
                                    act = 0;
                                }
                                lastCall = "short started  " + sp1.getDateTime() + " Price " + sp1.getClose() + " target " + target + " sto " + stoploss + " points " + c;
                                lastPivotH = " High " + highBean.pvttype + "  " + highBean.pvtvalue + "   " + highBean.pvtTime;
                                lastPivotL = " Low " + lowBean.pvttype + "  " + lowBean.pvtvalue + "   " + lowBean.pvtTime;

                                checkCandleC = 0;
                                System.out.println("short started  " + sp1.getDateTime() + " Price " + sp1.getClose() + " target " + target + " sto " + stoploss + " points " + c);
                                System.out.println("RSI  " + ((double[]) indis.get(ApplicationConstants.RSI))[i]);
                                System.out.println("ADX  " + ((double[]) indis.get(ApplicationConstants.ADX))[i]);
                                System.out.print("  BBUpper  " + ((double[]) indis.get(ApplicationConstants.BBUpper))[i] + " Lower  " + ((double[]) indis.get(ApplicationConstants.BBLower))[i]);
                                System.out.print("  ATR  " + ((double[]) indis.get(ApplicationConstants.ATR))[i] + "\n");
//                            System.out.println(" -High " + highBean.pvttype + "  " + highBean.pvtvalue + "   " + highBean.pvtTime);
                                sprice = sp1.getClose();
                                act = 1;
                                sh = 1;
                                by = 0;
                            }
                        }

                        if (sp1.getClose() > highBean.pvtvalue && by == 0) {
                            target1 = sp1.getClose() + ((sp1.getClose() - lowBean.pvtvalue) * targetMulti);
                            stoploss1 = sp1.getClose() - ((sp1.getClose() - lowBean.pvtvalue));
                            c1 = (sp1.getClose() - lowBean.pvtvalue) * targetMulti;

                            if (Test.priceWithintime(sp1.getDateTime())) {
                                System.out.println("-------------------------------------------------------------------------------");
                                if (sh == 1) {
                                    total -= sp1.getClose() - sprice;
                                    sft -= sp1.getClose() - sprice;
                                    sfailed++;
                                    act = 0;
                                    sh = 0;
                                    System.out.println("short cancelled for buy " + ft + "  points  " + (sp1.getClose() - sprice) + "  " + sfailed);
                                }
                                lastCall = "buy started  " + sp1.getDateTime() + " Price " + sp1.getClose() + " target " + target1 + " sto " + stoploss1 + " points " + c1;
                                lastPivotH = " High " + highBean.pvttype + "  " + highBean.pvtvalue + "   " + highBean.pvtTime;
                                lastPivotL = " Low " + lowBean.pvttype + "  " + lowBean.pvtvalue + "   " + lowBean.pvtTime;
                                System.out.println("buy started  " + sp1.getDateTime() + " Price " + sp1.getClose() + " target " + target1 + " sto " + stoploss1 + " points " + c1);
                                System.out.println("RSI  " + ((double[]) indis.get(ApplicationConstants.RSI))[i]);
                                System.out.print("  BBUpper  " + ((double[]) indis.get(ApplicationConstants.BBUpper))[i] + " Lower  " + ((double[]) indis.get(ApplicationConstants.BBLower))[i]);
                                System.out.print("  ATR  " + ((double[]) indis.get(ApplicationConstants.ATR))[i] + "\n");
                                System.out.println("ADX  " + ((double[]) indis.get(ApplicationConstants.ADX))[i]);
                                System.out.println(" Low " + lowBean.pvttype + "  " + lowBean.pvtvalue + "   " + lowBean.pvtTime);
                                System.out.println(" High " + highBean.pvttype + "  " + highBean.pvtvalue + "   " + highBean.pvtTime);
                                bprice = sp1.getClose();
                                act = 1;
                                by = 1;
                                sh = 0;
                            }
                        }

                        if (act == 1) {
                            if (sh == 1) {
                                if (sp1.getLow() <= target && newFlag == 0) {
                                    newFlag = 1;
                                    stoploss = bprice;
                                    lastTarget = "short target reached continueing  " + st + " points  " + c + "   " + sp1 + "  " + suu;
//                                System.out.println("short target reached continueing  " + st + " points  " + c + "   " + sp1 + "  " + suu);
//                                System.out.println("RSI  " + ((double[]) indis.get(ApplicationConstants.RSI))[i]);
//                                System.out.print("  BBUpper  " + ((double[]) indis.get(ApplicationConstants.BBUpper))[i] + " Lower  " + ((double[]) indis.get(ApplicationConstants.BBLower))[i]);
//                                System.out.print("  ATR  " + ((double[]) indis.get(ApplicationConstants.ATR))[i]);
                                } else if (sp1.getClose() >= stoploss) {
                                    total += (sprice - sp1.getClose());
                                    ft += (sprice - sp1.getClose());
                                    failed++;
                                    act = 0;
                                    sh = 0;
                                    newFlag = 0;
                                    System.out.println("short failed " + ft + "  points  " + (sp1.getClose() - sprice) + "   " + sp1 + "   " + failed);
                                    System.out.println("RSI  " + ((double[]) indis.get(ApplicationConstants.RSI))[i]);
                                    System.out.print("  BBUpper  " + ((double[]) indis.get(ApplicationConstants.BBUpper))[i] + " Lower  " + ((double[]) indis.get(ApplicationConstants.BBLower))[i]);
                                    System.out.print("  ATR  " + ((double[]) indis.get(ApplicationConstants.ATR))[i] + "\n");
                                } else if (formatter1.format(sp1.getDateTime()).contains("15:30:00")) {
                                    total += (sprice - sp1.getClose());
                                    ft += (sprice - sp1.getClose());
                                    failed++;
                                    act = 0;
                                    sh = 0;
                                    newFlag = 0;
                                    System.out.println("short closed " + ft + "  points  " + (sp1.getClose() - sprice) + "   " + sp1 + "   " + failed);
                                    System.out.println("RSI  " + ((double[]) indis.get(ApplicationConstants.RSI))[i]);
                                    System.out.print("  BBUpper  " + ((double[]) indis.get(ApplicationConstants.BBUpper))[i] + " Lower  " + ((double[]) indis.get(ApplicationConstants.BBLower))[i]);
                                    System.out.print("  ATR  " + ((double[]) indis.get(ApplicationConstants.ATR))[i] + "\n");
                                }
                                if (newFlag == 1) {
                                    StockPrice spOld = (StockPrice) ls.get(i - 1);
                                    if (sp1.getClose() <= spOld.getClose()) {
//                                    System.out.println("short continueing  " + st + " points  " + c + "   " + sp1 + "  " + suu);
//                                    System.out.println("RSI  " + ((double[]) indis.get(ApplicationConstants.RSI))[i]);
//                                    System.out.print("  BBUpper  " + ((double[]) indis.get(ApplicationConstants.BBUpper))[i] + " Lower  " + ((double[]) indis.get(ApplicationConstants.BBLower))[i]);
//                                    System.out.print("  ATR  " + ((double[]) indis.get(ApplicationConstants.ATR))[i]);
                                    } else {
                                        newFlag = 0;
                                        total += c;
                                        st += c;
                                        suu++;
                                        act = 0;
                                        sh = 0;
                                        System.out.println("short target reached complete  " + st + " points  " + c + "   " + sp1 + "  " + suu);
                                        System.out.println("RSI  " + ((double[]) indis.get(ApplicationConstants.RSI))[i]);
                                        System.out.print("  BBUpper  " + ((double[]) indis.get(ApplicationConstants.BBUpper))[i] + " Lower  " + ((double[]) indis.get(ApplicationConstants.BBLower))[i]);
                                        System.out.print("  ATR  " + ((double[]) indis.get(ApplicationConstants.ATR))[i] + "\n");
                                    }
                                }
                            }
                            if (by == 1) {

                                if (sp1.getHigh() >= target1 && newFlag == 0) {
                                    newFlag = 1;
                                    stoploss = bprice;
                                    System.out.println("buy success continueing" + st1 + " points  " + c1 + "   " + sp1 + "   " + suu1);
//                                System.out.println("RSI  " + ((double[]) indis.get(ApplicationConstants.RSI))[i]);
//                                System.out.print("  BBUpper  " + ((double[]) indis.get(ApplicationConstants.BBUpper))[i] + " Lower  " + ((double[]) indis.get(ApplicationConstants.BBLower))[i]);
//                                System.out.print("  ATR  " + ((double[]) indis.get(ApplicationConstants.ATR))[i]);

                                } else if (sp1.getClose() <= stoploss1) {
                                    total1 += bprice - sp1.getClose();
                                    failed1++;
                                    ft1 += bprice - sp1.getClose();
                                    by = 0;
                                    newFlag = 0;
                                    System.out.println("buy failed " + ft1 + " points  " + (bprice - sp1.getClose()) + "   " + sp1 + "   " + failed1);
                                    System.out.println("RSI  " + ((double[]) indis.get(ApplicationConstants.RSI))[i]);
                                    System.out.print("  BBUpper  " + ((double[]) indis.get(ApplicationConstants.BBUpper))[i] + " Lower  " + ((double[]) indis.get(ApplicationConstants.BBLower))[i]);
                                    System.out.print("  ATR  " + ((double[]) indis.get(ApplicationConstants.ATR))[i] + "\n");
                                    act = 0;
                                } else if (formatter1.format(sp1.getDateTime()).contains("15:30:00")) {
                                    total1 += bprice - sp1.getClose();
                                    failed1++;
                                    ft1 += bprice - sp1.getClose();
                                    by = 0;
                                    newFlag = 0;
                                    System.out.println("buy closed " + ft1 + " points  " + (bprice - sp1.getClose()) + "   " + sp1 + "   " + failed1);
                                    System.out.println("RSI  " + ((double[]) indis.get(ApplicationConstants.RSI))[i]);
                                    System.out.print("  BBUpper  " + ((double[]) indis.get(ApplicationConstants.BBUpper))[i] + " Lower  " + ((double[]) indis.get(ApplicationConstants.BBLower))[i]);
                                    System.out.print("  ATR  " + ((double[]) indis.get(ApplicationConstants.ATR))[i] + "\n");
                                    act = 0;
                                }
                                if (newFlag == 1) {
                                    StockPrice spOld = (StockPrice) ls.get(i - 1);
                                    if (sp1.getClose() >= spOld.getClose()) {
//                                    System.out.println("buy continueing  " + st + " points  " + c + "   " + sp1 + "  " + suu);
//                                    System.out.println("RSI  " + ((double[]) indis.get(ApplicationConstants.RSI))[i]);
//                                    System.out.print("  BBUpper  " + ((double[]) indis.get(ApplicationConstants.BBUpper))[i] + " Lower  " + ((double[]) indis.get(ApplicationConstants.BBLower))[i]);
//                                    System.out.print("  ATR  " + ((double[]) indis.get(ApplicationConstants.ATR))[i]);
                                    } else {
                                        newFlag = 0;
                                        total1 += c1;
                                        suu1++;
                                        st1 += c1;
                                        act = 0;
                                        by = 0;
                                        System.out.println("buy target reached complete  " + st + " points  " + c + "   " + sp1 + "  " + suu);
                                        System.out.println("RSI  " + ((double[]) indis.get(ApplicationConstants.RSI))[i]);
                                        System.out.print("  BBUpper  " + ((double[]) indis.get(ApplicationConstants.BBUpper))[i] + " Lower  " + ((double[]) indis.get(ApplicationConstants.BBLower))[i]);
                                        System.out.print("  ATR  " + ((double[]) indis.get(ApplicationConstants.ATR))[i] + "\n");
                                    }
                                }
                            }
                        }
                    }
                    /////////////////////////Method 1 //////////////////////////////////////////////////   
                    ///////////////////////Method 2 //////////////////////////////////////////////////   
                }
                System.out.println("Current Time :: " + ls.get(ls.size() - 1));
                System.out.println("short Total  " + total + " failed  " + failed + "  success  " + suu);
                System.out.println(" buy Total  " + total1 + " failed  " + failed1 + "  success  " + suu1);
                System.out.println(" short success   " + st + " failed  " + ft + " buy success  " + st1 + "  failed " + ft1);
                System.out.println(" short cancelled   " + sfailed + "  " + bfailed + " Short points  " + sft + "  buy points " + bft);
                System.out.println("-------------------------------------------------------------------------------");
                System.out.println(lastCall + "    " + sh + " " + by + "  " + act);
                System.out.println(lastPivotH);
                System.out.println(lastPivotL);
                System.out.println("-------------------------------------------------------------------------------\n");
                break;
            }
        } catch (IOException ex) {
            Logger.getLogger(PivotPoints.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(PivotPoints.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    static StockPrice tempoldsp;

    public static boolean inSideBar(StockPrice oldsp, StockPrice sp, double refineVal) {
        boolean flag = false;
        if (tempoldsp == null) {
            tempoldsp = oldsp;
        }
        if (tempoldsp.getOpen() > oldsp.getClose() && tempoldsp.getClose() < oldsp.getClose()) {

        } else if (tempoldsp.getOpen() < oldsp.getClose() && tempoldsp.getClose() > oldsp.getClose()) {

        } else {
            tempoldsp = oldsp;
        }

        if (tempoldsp.getHigh() - sp.getClose() > refineVal && -refineVal <= tempoldsp.getLow() - sp.getClose()
                && tempoldsp.getHigh() - sp.getOpen() >= refineVal && -refineVal <= tempoldsp.getLow() - sp.getOpen()) {
//            System.out.println("sp iis insidebar -ve " + sp);

            flag = true;
        } else if (-refineVal <= tempoldsp.getHigh() - sp.getClose() && tempoldsp.getLow() - sp.getClose() >= refineVal
                && -refineVal <= tempoldsp.getHigh() - sp.getOpen() && tempoldsp.getLow() - sp.getOpen() >= refineVal) {
//            System.out.println("sp iis insidebar +ve " + sp);
            flag = true;
        } else {
            tempoldsp = null;
        }

        return flag;
    }

    public static boolean inSideBarTarget(StockPrice oldsp, StockPrice sp) {
        boolean flag = false;
        if (tempoldsp == null) {
            tempoldsp = oldsp;
        }
        if (tempoldsp.getOpen() > oldsp.getClose() && tempoldsp.getClose() < oldsp.getClose()) {

        } else if (tempoldsp.getOpen() < oldsp.getClose() && tempoldsp.getClose() > oldsp.getClose()) {

        } else {
            tempoldsp = oldsp;
        }

        if (tempoldsp.getOpen() >= sp.getClose() && tempoldsp.getClose() <= sp.getClose()
                && tempoldsp.getOpen() >= sp.getOpen() && tempoldsp.getClose() <= sp.getOpen()) {
//            System.out.println("sp iis insidebar -ve " + sp);

            flag = true;
        } else if (tempoldsp.getOpen() <= sp.getClose() && tempoldsp.getClose() >= sp.getClose()
                && tempoldsp.getOpen() <= sp.getOpen() && tempoldsp.getClose() >= sp.getOpen()) {
//            System.out.println("sp iis insidebar +ve " + sp);
            flag = true;
        } else {
            tempoldsp = null;
        }

        return flag;
    }

//oldsp.getOpen>oldsp.getClose() && 
}
