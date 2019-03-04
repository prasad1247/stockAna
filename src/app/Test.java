/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import bean.PivotPointBean;
import bean.Stock;
import bean.StockPrice;
import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;
import download.DownloadFromGoogle;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import start.StockHelper;

/**
 *
 * @author hp
 */
public class Test {

    public void changeTimeFrame(Stock stock, long time) {
        double oldC[] = stock.getClose();
        double oldO[] = stock.getOpen();
        double oldL[] = stock.getLow();
        double oldH[] = stock.getHigh();

        int outputInt[];
        MInteger outBegIdx = new MInteger();
        MInteger outNbElement = new MInteger();
        RetCode retCode;
        Core lib = new Core();
        int lookback;

        LinkedList lC = new LinkedList();
        LinkedList lO = new LinkedList();
        LinkedList lL = new LinkedList();
        LinkedList lH = new LinkedList();

        int count = 0;
        Double c = 0d;
        Double o = 0d;
        Double l = 0d;
        Double h = 0d;
        Date dateTime[] = stock.getDateTime();
        Date startDate = dateTime[0];
        Date nextDate = new Date((startDate.getTime() + time));
        for (int i = 0; i < dateTime.length; i++) {
            if (dateTime[i].getTime() <= nextDate.getTime()) {
                System.out.println("d  " + dateTime[i] + "  " + nextDate);
                c += oldC[i];
                o += oldO[i];
                l += oldL[i];
                h += oldH[i];
                count++;
            } else {
                //   System.out.println("count  "+count);
                //   System.out.println("c  "+c);
                c = (c / count);
                lC.add((c));
                lO.add((o));
                lL.add((l));
                lH.add((h));
                count = 0;
                c = 0d;
                o = 0d;
                l = 0d;
                h = 0d;
                nextDate = new Date((dateTime[i].getTime() + time));
                System.out.println("d1111  " + dateTime[i] + " 22222 " + nextDate);
                c += oldC[i];
                o += oldO[i];
                l += oldL[i];
                h += oldH[i];
                count++;
            }
        }

        double[] newC = new double[lC.size()];
        for (int i = 0; i < lC.size(); i++) {
            newC[i] = (Double) lC.get(i); // Watch out for NullPointerExceptions!
        }
        double output[] = new double[newC.length];
        double output1[] = new double[newC.length];
        lookback = lib.emaLookback(3);
        retCode = lib.ema(0, newC.length - 1, newC, lookback + 1, outBegIdx, outNbElement, output1);
        output = rightJustify(output1, outBegIdx.value);
        System.out.println("=== " + output[newC.length - 1]);
    }

    public double[] rightJustify(double[] digits, int len) {
        System.arraycopy(digits, 0, digits, len, digits.length - len);
        Arrays.fill(digits, 0, len, 0);
        return digits;
    }

    public static void main(String[] args) {
        try {
            System.setProperty("http.proxyHost", "10.0.3.111");
            System.setProperty("http.proxyPort", "8080");
            StockAnalysis ana = new StockAnalysis();
            Stock s = ana.getDatafromFile("TATASTEEL");
            ana.addIndicators(s);
            java.sql.Date d = null;
            LinkedList stockList = s.getStockList();
//            for(Object o:s.getStockList()){
//                StockPrice sp=(StockPrice)o;
//                System.out.println(""+sp);
//            }
//            ana.getDatafromFile("TATACOFFEE"); 
//            for (Object o : stockList) {
//
//                StockPrice sdb = (StockPrice) o;
//                        d = new java.sql.Date(sdb.getDateTime().getTime());
//            System.out.println("d "+d+"  "+sdb.getDateTime());
//            }
            //StockHelper.insertData_min(s);
            LinkedList ls = s.getStockList();
            //    StockPrice sp = (StockPrice) ls.get(0);
            double pvtlow = 999999999, pvthigh = 0, temp = 0;
            double pvtlow1 = 999999999, pvthigh1 = 0, temp1 = 0;
            int low = 1, high = 0, sh = 0, by = 0, act = 0;
            double target = 0, stoploss = 0, total = 0, c = 0;
            double target1 = 0, stoploss1 = 0, total1 = 0, c1 = 0;
            double ft1 = 0, st1 = 0, ft = 0, st = 0;
            int failed = 0, suu = 0;
            int failed1 = 0, suu1 = 0;
            double bprice = 0, sprice = 0;
            int fl = 0, fll = 0, dnt = 2;
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy,HH:mm:ss");
            Date stopdate = formatter.parse("03-02-2016,13:10:00");
            LinkedList pivotlist = new LinkedList();
            for (int i = 1; i < ls.size(); i++) {
                StockPrice sp1 = (StockPrice) ls.get(i);
                if (stopdate.getTime() < sp1.getDateTime().getTime()) {
                    String ss = "stop";
                }
//                if (sp1.getClose() < pvtlow1 && pvtlow1 < 999999999 && act == 0 && dnt == 2) {
//                    target = sp1.getClose() - ((pvthigh1 - sp1.getClose()) * 2);
//                    stoploss = sp1.getClose() + (pvthigh1 - sp1.getClose());
//                    c = (pvthigh1 - sp1.getClose()) * 2;
//                    if (by == 1) {
//                        total1 -= bprice - sp1.getClose();
//                        failed1++;
//                        ft1 -= bprice - sp1.getClose();
//                        by = 0;
//                        System.out.println("buy cancelled for short :: " + ft1 + " points  " + (bprice - sp1.getClose()) + "   " + failed1);
//                        act = 0;
//                    }
//                    System.out.println("short started  " + sp1.getDateTime() + " Price " + sp1.getClose() + " target " + target + " sto " + stoploss + " points " + c);
//                    sprice = sp1.getClose();
//                    act = 1;
//                    sh = 1;
//                    by = 0;
//                }
////                if (sp1.getClose() > pvthigh1 && pvthigh1 > 0 && act == 0) {
////                    target1 = sp1.getClose() + ((sp1.getClose() - pvtlow1));
////                    stoploss1 = sp1.getClose() - (sp1.getClose() - pvtlow1);
////                    c1 = (sp1.getClose() - pvtlow1);
////                    if (c1 > 1500) {
////                        System.out.println("buy canceled..." + c1);
////                        act = 0;
////                        by = 0;
////                        sh = 1;
////                    } else {
////                        if (priceWithintime(sp1.getDateTime())) {
////                            if (sh == 1) {
////
////                                total -= sp1.getClose() - sprice;
////                                ft -= sp1.getClose() - sprice;
////                                failed++;
////                                act = 0;
////                                sh = 0;
////                                System.out.println("short cancelled for buy " + ft + "  points  " + (sp1.getClose() - sprice) + "  " + failed);
////                            }
////                            System.out.println("buy started  " + sp1.getDateTime() + " Price " + sp1.getClose() + " target " + target1 + " sto " + stoploss1 + " points " + c1);
////                            bprice = sp1.getClose();
////                            act = 1;
////                            by = 1;
////                            sh = 0;
////                        }
////                    }
////                }
//                if (act == 1) {
//                    if (sh == 1) {
//                        if (sp1.getLow() <= target) {
//
//                            total += c;
//                            st += c;
//                            suu++;
//                            act = 0;
//                            sh = 0;
//                            dnt = 1;
//                            System.out.println("short target reached " + st + " points  " + c + "   " + sp1 + "  " + suu);
//                        } else if (sp1.getClose() >= stoploss) {
//
//                            total -= (sp1.getClose() - sprice);
//                            ft -= (sp1.getClose() - sprice);
//                            failed++;
//                            act = 0;
//                            sh = 0;
//                            dnt = 1;
//                            System.out.println("short failed " + ft + "  points  " + (sp1.getClose() - sprice) + "   " + sp1 + "   " + failed);
//                        }
//                    }
//                    if (by == 1) {
//                        if (sp1.getHigh() >= target1) {
//                            total1 += c1;
//                            suu1++;
//                            st1 += c1;
//                            act = 0;
//                            by = 0;
//                            System.out.println("buy success " + st1 + " points  " + c1 + "   " + sp1 + "   " + suu1);
//
//                        } else if (sp1.getClose() <= stoploss1) {
//
//                            total1 -= bprice - sp1.getClose();
//                            failed1++;
//                            ft1 -= bprice - sp1.getClose();
//                            by = 0;
//                            System.out.println("buy failed " + ft1 + " points  " + (bprice - sp1.getClose()) + "   " + sp1 + "   " + failed1);
//                            act = 0;
//                        }
//                    }
//
//                }
                if (pvtlow > sp1.getLow() && low == 1) {
                    pvtlow = sp1.getLow();
                } else if (high == 0) {
                    if (Math.abs(pvthigh1 - pvtlow) > 2) 
                    {
                        pvtlow1 = pvtlow;
                        System.out.println(pvtlow1 + "   pvtlow at " + ls.get(i - 1));
                        PivotPointBean pb = new PivotPointBean();
                        pb.pivotAtCandle = sp1;
                        pb.pvtvalue = pvtlow;
                        pb.pvttype = "low";
                        pivotlist.add(pb);
                        if (dnt == 1) {
                            dnt = 2;
                        }

                    } 
                    else {
                        if (Math.abs(pvtlow1 - pvtlow) > 2) {
                            pvtlow1 = pvtlow;
                        } else {
                            pvtlow1 = Math.min(pvtlow1, pvtlow);
                        }
                        System.out.println(pvtlow + "  old " + pvtlow1 + "   pvtlow at " + ls.get(i - 1));
                        fl = 1;
                        if (dnt == 1) {
                            dnt = 2;
                        }
                    }
                    low = 0;
                    high = 1;
                    pvthigh = 0;

                }
                if (pvthigh < sp1.getHigh() && high == 1) {
                    pvthigh = sp1.getHigh();
                } else if (low == 0) {
                    if (Math.abs(pvthigh - pvtlow1) > 2) 
                    {
                        
                        pvthigh1 = pvthigh;
                        System.out.println(pvthigh1 + "   pvthigh at " + ls.get(i - 1));
                        PivotPointBean pb = new PivotPointBean();
                        pb.pivotAtCandle = sp1;
                        pb.pvtvalue = pvthigh;
                        pb.pvttype = "high";
                        pivotlist.add(pb);
                    } 
                    else {
                        if (Math.abs(pvthigh1 - pvthigh) > 2) {
                            pvthigh1 = pvthigh;
                        } else {
                            pvthigh1 = Math.max(pvthigh1, pvthigh);
                        }
                        System.out.println(pvthigh + "   old " + pvthigh1 + "   pvthigh at " + ls.get(i - 1));
                        fl = 1;
                    }
                    low = 1;
                    high = 0;
                    pvtlow = 999999999;
                }

////                }if(sp1.getClose()>pvthigh){
////                    System.out.println("buy started "+sp1);
////                }
//
            }
            System.out.println("short Total  " + total + " failed  " + failed + "  success  " + suu);
            System.out.println(" buy Total  " + total1 + " failed  " + failed1 + "  success  " + suu1);
            System.out.println(" short success   " + st + " failed  " + ft + " buy success  " + st1 + "  failed " + ft1);
            //ana.displayAllStockList();
            // new Test().changeTimeFrame(s,1800000);
            pvthigh1 = 0;
            pvtlow1 = 0;
//            for (Object o : pivotlist) {
//                PivotPointBean pb = (PivotPointBean) o;
//                if (pb.pvttype == "low") {
//                    if (Math.abs(pvthigh1 - pb.pvtvalue) > 2) {
//                        System.out.println("pivot low " + pb.pvtvalue + "  " + pb.pivotAtCandle);
//                        pvtlow1 = pb.pvtvalue;
//                    } else {
//                        pvtlow1 = Math.min(pb.pvtvalue, pvtlow1);
//                        System.out.println("pivot low " + pvtlow1 + "  " + pb.pivotAtCandle);
//                    }
//                }
//                if (pb.pvttype == "high") {
//                    if (Math.abs(pb.pvtvalue - pvtlow1) > 2) {
//                        System.out.println("pivot high " + pb.pvtvalue + "  " + pb.pivotAtCandle);
//                        pvthigh1 = pb.pvtvalue;
//                    } else {
//                        pvthigh1 = Math.max(pb.pvtvalue, pvthigh1);
//                        System.out.println("pivot high " + pvthigh1 + "  " + pb.pivotAtCandle);
//                    }
//                }
//            }
//
//            for (int i = 1; i < ls.size(); i++) {
//                    StockPrice sp1 = (StockPrice) ls.get(i);
//                    
//            }

        } catch (Exception ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }

    }

    public static boolean priceWithintime(Date d) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String d1 = "09:20:00";
        String d2 = "15:30:00";
        String dToTest = sdf.format(d);
        boolean isSplit = false;
        boolean isWithin = false;
        try {
            Date dt1 = null;
            Date dt2 = null;
            Date dt3 = null;
            dt1 = sdf.parse(d1);
            dt2 = sdf.parse(d2);
            dt3 = sdf.parse(dToTest);
            isSplit = (dt2.compareTo(dt1) < 0);
//            System.out.println("[split]: " + isSplit);
            if (isSplit) {
                isWithin = (dt3.after(dt1) || dt3.before(dt2));
            } else {
                isWithin = (dt3.after(dt1) && dt3.before(dt2));
            }

        } catch (ParseException ex) {
            Logger.getLogger(DownloadFromGoogle.class.getName()).log(Level.SEVERE, null, ex);
        }
        return isWithin;
    }

}

