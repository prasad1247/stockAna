/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import bean.Stock;
import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import start.StockHelper;
import util.ApplicationConstants;
import util.ApplicationUtils;

/**
 *
 * @author Administrator
 */
public class TradeSignals {

    public static int sig_ema_315(Stock stock) {
        boolean up = false;
        int signalperiod = 0;
        HashMap indiMap = stock.getIndicators();
        double ema3[] = (double[]) indiMap.get(ApplicationConstants.EMA_3);
        double ema15[] = (double[]) indiMap.get(ApplicationConstants.EMA_15);
        try {
            if (ema3 != null && ema15 != null) {
                if (ema15[ema15.length - 1] < ema3[ema3.length - 1]) {
                    up = true;
                }
                int i = ema15.length - 1;
                for (; i > 0; i--) {
//                System.out.println("ema15-3 : " +ema15[i]+" "+ema3[i]);
                    if (up) {
                        if (ema15[i] < ema3[i]) {
                            signalperiod++;
                        } else {
                            break;
                        }
                    } else {
                        if (ema15[i] > ema3[i]) {
                            signalperiod++;
                        } else {
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return signalperiod;
    }

    public void sig_adx() {
    }

    public static void newStat(Stock stock, int type) {
        boolean up = false;
        int signalperiod = 0, upPeriod = 0, dwnperiod = 0;
        HashMap indiMap = stock.getIndicators();
        double ema3[] = (double[]) indiMap.get(ApplicationConstants.EMA_3);
        double ema15[] = (double[]) indiMap.get(ApplicationConstants.EMA_15);
        double cci[] = (double[]) indiMap.get("CCI");
        double adx[] = (double[]) indiMap.get(ApplicationConstants.ADX);
        double atr[] = (double[]) indiMap.get(ApplicationConstants.ATR);
        double rsi[] = (double[]) indiMap.get(ApplicationConstants.RSI);
        String trend = "";
        double b = 0, s, d, t = 0, c = 0;
        int pt = 0, lt = 0;
        ;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
        String sda = "03-02-2015";
        Date today = null;
        try {

            today = sdf.parse(sda);
        } catch (ParseException ex) {
        
            Logger.getLogger(TradeSignals.class.getName()).log(Level.SEVERE, null, ex);
        }
        //System.out.println(" "+today);
        Date bdate = null;
        try {
            if (ema3 != null && ema15 != null) {
                if (ema15[0] < ema3[0]) {
                    trend = "up";
                }
                int i = 0;
                double atrr = 0;
                for (i = 00; i < ema15.length; i++) {
//                System.out.println("ema15-3 : " +ema15[i]+" "+ema3[i]);
                    if (ema15[i] < ema3[i]) {
                        if (trend.equals("up")) {
                            upPeriod++;
                            if (up == false) {
                                if (cci[i] > cci[i - 1] && cci[i - 1] > cci[i - 2]) {
                                    if (cci[i] > 60) {
//                                        System.out.println("buy" + " stock " + stock.getDateTime()[i] + "  " + stock.getClose()[i]);
                                        b = stock.getOpen()[i + 1];
                                        bdate = stock.getDateTime()[i];
                                        c = cci[i];
                                        atrr = atr[i];
                                        up = true;
                                        if (type == 1) {
                                            if (bdate.getTime() == today.getTime()) {
                                                //if (engulfingPattern(stock) == 100) {
                                                System.out.println(stock.getName() + "," + stock.getDateTime()[i] + ", " + stock.getClose()[i] + "  " + stock.getVolume()[i] + "  " + stock.getClose()[stock.getClose().length - 1]);
                                                System.out.println(" CCI :: " + c + "  " + cci[i - 1] + "  " + cci[i - 2] + ", ADX :: " + adx[i] + " C Diff " + (cci[i - 1] - cci[i]));
                                                System.out.println(" EMA3 :: " + ema3[i] + " EMA15 " + ema15[i] + " UPTrend " + upPeriod + " ATR " + atr[i] + ", RSI :: " + rsi[i]);
                                                StockHelper.getFundamentals(stock.getName());
                                                System.out.println("------------------------------------------------");
                                            }
                                            //}
                                        }
                                    }
                                }
                            }//else if(up==true){ 
                            else if ((cci[i - 1] - cci[i] > 60  //|| atrr/2+ b<stock.getHigh()[i]
                                    ) && up == true) {

                                if (atr[i - 1] + stock.getClose()[i - 1] < stock.getHigh()[i]) {
                                    s = atrr + stock.getClose()[i - 1];
                                } else {
                                    if (stock.getClose()[i - 1] - atrr / 2 > stock.getClose()[i]) {
                                        s = stock.getClose()[i - 1] - atrr / 2;
                                    } else {
                                        s = stock.getClose()[i];
                                    }
                                }
                                d = s - b;
                                t += d * (10000 / b);
                                if (d < 0) {
                                    lt++;
                                } else {
                                    pt++;
                                }
                                 if (bdate.getTime() >= today.getTime()) {
//                                    System.out.println("buy " + b + " Date " + bdate + "  sell " + s + " stock " + stock.getDateTime()[i] + "  " + d + " bc " + c + " sc " + (cci[i - 1] - cci[i]) + "  " + cci[i]);
//                                    System.out.println(" val  " + atrr + "  " + atr[i - 1] + "  " + (atrr + stock.getClose()[i - 1]) + "    " + stock.getHigh()[i]);
//                                    System.out.println(" this Trade  " + (d * (10000 / b)));
                                }
                                up = false;
                            }
                        } else {
                            trend = "up";

//                            upPeriod = 1;
//                                  b = stock.getOpen()[i+1];
//                                bdate = stock.getDateTime()[i+1];
//                                c = cci[i];
//                                atrr=atr[i];
//                                up = true;
//                            //System.out.println(" trend Change from dwn to up :: length " + dwnperiod+" stock "+stock.getDateTime()[i]+"  "+stock.getClose()[i]);
//                              if (type == 1) {
//                                    if (bdate.getTime() == today.getTime()) {
//                                        //if (engulfingPattern(stock) == 100) {
//                                                System.out.println(stock.getName() + "," + stock.getDateTime()[i] + ", " + stock.getClose()[i] + "  " + stock.getVolume()[i]+"  "+stock.getClose()[stock.getClose().length-1]);
//                                                System.out.println(" CCI :: " + c + "  " + cci[i - 1] + "  " + cci[i - 2] + ", ADX :: " + adx[i] + " C Diff " + (cci[i - 1] - cci[i]));
//                                                System.out.println(" EMA3 :: " + ema3[i] + " EMA15 " + ema15[i] + " UPTrend " + upPeriod + " ATR " + atr[i] + ", RSI :: " + rsi[i]);
//                                                StockHelper.getFundamentals(stock.getName());
//                                                System.out.println("------------------------------------------------");
//                                            }
//                                        //}
//                                    }
                            dwnperiod = 0;
                        }

                    } else {
                        if (trend.equals("down")) {
                            dwnperiod++;
//                            if (dwnperiod>4 && stock.getClose()[i] > stock.getClose()[i-1]) {
//                                if(cci[i]>cci[i-1] && cci[i-1]>cci[i-2]){
//                                     b = stock.getClose()[i];
//                                bdate = stock.getDateTime()[i];
//                                c = cci[i];
//                                atrr=atr[i];
//                                    if (bdate.getTime() == today.getTime()) {
//                                                System.out.println(stock.getName() + "," + stock.getDateTime()[i] + ", " + stock.getClose()[i] + "  " + stock.getVolume()[i]+"  "+stock.getClose()[stock.getClose().length-1]);
//                                                System.out.println(" CCI :: " + c + "  " + cci[i - 1] + "  " + cci[i - 2] + ", ADX :: " + adx[i] + " C Diff " + (cci[i - 1] - cci[i])+cci[cci.length-1]);
//                                                System.out.println(" EMA3 :: " + ema3[i] + " EMA15 " + ema15[i] + " UPTrend " + upPeriod + " ATR " + atr[i] + ", RSI :: " + rsi[i]);
//                                                StockHelper.getFundamentals(stock.getName());
//                                                System.out.println("------------------------------------------------");
//                                    }
//                                }
//                            }
                        } else {
                            trend = "down";
                            dwnperiod = 1;
//                            //System.out.println(" trend Change from up to down :: length " + upPeriod+" stock "+stock.getDateTime()[i]+"  "+stock.getClose()[i]);
                            upPeriod = 0;
                        }

                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (type == 2) {
            //System.out.println(stock.getName() + " Total  " + t + "  pt " + pt + "  lt  " + lt + " % " + (t / 100));
            System.out.println(stock.getName() + "," + t + "," + pt + "," + lt + "," + (t / 100));
        }

    }

    public static int engulfingPattern(Stock stock) {
        int output[] = new int[stock.getClose().length];
        int outputInt[];
        MInteger outBegIdx = new MInteger();
        MInteger outNbElement = new MInteger();
        RetCode retCode;
        Core lib = new Core();

        //lib.cdlEveningStar(0, stock.getClose().length - 1, stock.getOpen(), stock.getHigh(), stock.getLow(), stock.getClose(),0.3, outBegIdx, outNbElement, output);
        lib.cdlEngulfing(0, stock.getClose().length - 1, stock.getOpen(), stock.getHigh(), stock.getLow(), stock.getClose(), outBegIdx, outNbElement, output);
        output = rightJustify(output, outBegIdx.value);
        String sda = "03-11-2014";
        Date today = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
        try {
            today = sdf.parse(sda);
        } catch (ParseException ex) {
            Logger.getLogger(TradeSignals.class.getName()).log(Level.SEVERE, null, ex);
        }
        int val = 0;
        for (int i = 0; i < stock.getDateTime().length; i++) {
            if (stock.getDateTime()[i].getTime() == today.getTime()) {
                if (output[i] != 0) {
                    System.out.println(stock.getName() + "  " + stock.getDateTime()[i] + "   " + output[i]);
                    val = output[i];
                }
            }
        }
        return val;
    }

    public static int[] rightJustify(int[] digits, int len) {
        try {
            System.arraycopy(digits, 0, digits, len, digits.length - len);
            Arrays.fill(digits, 0, len, 0);
        } catch (Exception e) {
            System.out.println(" faulty " + digits);
        }
        return digits;
    }

    public static void main(String[] args) {
        StockAnalysis sa = new StockAnalysis();
        LinkedList symbolList = ApplicationUtils.readSymbols(ApplicationConstants.SYMBOL_LIST_PATH);
        try {
            for (Object sym : symbolList) {

                String symbol =  (String) sym;
                //   Stock st = sa.getDatafromFile(symbol);
                Stock st = StockHelper.getStockData(symbol);
                try {
                    st.setName(symbol);
                    st = sa.addIndicators(st);
                    st.setEmaSignal(TradeSignals.sig_ema_315(st));
                } catch (Exception e) {
                }
                newStat(st, 1);

                ///engulfingPattern(st);
                //break;

            }
        } catch (Exception ex) {
            Logger.getLogger(TradeSignals.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void anotherSig(Stock stock, int type) {
        boolean up = false;
        int signalperiod = 0, upPeriod = 0, dwnperiod = 0;
        HashMap indiMap = stock.getIndicators();
        double ema3[] = (double[]) indiMap.get(ApplicationConstants.EMA_3);
        double ema15[] = (double[]) indiMap.get(ApplicationConstants.EMA_15);
        double cci[] = (double[]) indiMap.get("CCI");
        double adx[] = (double[]) indiMap.get(ApplicationConstants.ADX);
        double atr[] = (double[]) indiMap.get(ApplicationConstants.ATR);
        double rsi[] = (double[]) indiMap.get(ApplicationConstants.RSI);
        String trend = "";
        double b = 0, s, d, t = 0, c = 0;
        int pt = 0, lt = 0;
        double dwpercentage = 0.0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
        String sda = "02-01-2015";
        Date today = null;
        try {

            today = sdf.parse(sda);
        } catch (ParseException ex) {
            Logger.getLogger(TradeSignals.class.getName()).log(Level.SEVERE, null, ex);
        }
        //System.out.println(" "+today);
        Date bdate = null;
        try {
            if (ema3 != null && ema15 != null) {
                if (ema15[0] < ema3[0]) {
                    trend = "up";
                }
                int i = 0;
                double atrr = 0;
                for (i = 00; i < ema15.length; i++) {
//                System.out.println("ema15-3 : " +ema15[i]+" "+ema3[i]);
                    if (ema15[i] < ema3[i]) {
                        if (trend.equals("up")) {
                            upPeriod++;
                            if (up == false) {
//                                 if (cci[i] > cci[i - 1] && cci[i - 1] > cci[i - 2] )
                                {
                                    //System.out.println("buy"+" stock "+stock.getDateTime()[i]+"  "+stock.getClose()[i]);
//                                b = stock.getOpen()[i+1];
//                                bdate = stock.getDateTime()[i];
//                                c = cci[i];
//                                atrr=atr[i];
//                                up = true;
//                                if (type == 1) {
//                                    if (bdate.getTime() == today.getTime()) {
//                                        //if (engulfingPattern(stock) == 100) {
//                                                System.out.println(stock.getName() + "," + stock.getDateTime()[i] + ", " + stock.getClose()[i] + "  " + stock.getVolume()[i]+"  "+stock.getClose()[stock.getClose().length-1]);
//                                                System.out.println(" CCI :: " + c + "  " + cci[i - 1] + "  " + cci[i - 2] + ", ADX :: " + adx[i] + " C Diff " + (cci[i - 1] - cci[i]));
//                                                System.out.println(" EMA3 :: " + ema3[i] + " EMA15 " + ema15[i] + " UPTrend " + upPeriod + " ATR " + atr[i] + ", RSI :: " + rsi[i]);
//                                                StockHelper.getFundamentals(stock.getName());
//                                                System.out.println("------------------------------------------------");
//                                            }
//                                        //}
//                                    }
                                }
                            }//else if(up==true){ 
                            else if ((cci[i - 1] - cci[i] > 60 || cci[i] < 80 //|| atrr/2+ b<stock.getHigh()[i]
                                    ) && up == true) {

                                if (atr[i - 1] + stock.getClose()[i - 1] < stock.getHigh()[i]) {
                                    s = atrr + stock.getClose()[i - 1];
                                } else {
                                    if (stock.getClose()[i - 1] - atrr / 2 > stock.getClose()[i]) {
                                        s = stock.getClose()[i - 1] - atrr / 2;
                                    } else {
                                        s = stock.getClose()[i];
                                    }
                                }
                                d = s - b;
                                t += d * (10000 / b);
                                if (d < 0) {
                                    lt++;
                                } else {
                                    pt++;
                                }
                                if (type == 2) {
                                    System.out.println("buy " + b + " Date " + bdate + "  sell " + s + " stock " + stock.getDateTime()[i] + "  " + d + " bc " + c + " sc " + (cci[i - 1] - cci[i]) + "  " + cci[i]);
                                    System.out.println(" val  " + atrr + "  " + atr[i - 1] + "  " + (atrr + stock.getClose()[i - 1]) + "    " + stock.getHigh()[i]);
                                    System.out.println(" this Trade  " + (d * (10000 / b)));
                                }
                                up = false;
                            }
                        } else {

                            trend = "up";

                            upPeriod = 1;
                            b = stock.getOpen()[i + 1];
                            bdate = stock.getDateTime()[i + 1];
                            c = cci[i];
                            atrr = atr[i];
                            up = true;
                            //System.out.println(" trend Change from dwn to up :: length " + dwnperiod+" stock "+stock.getDateTime()[i]+"  "+stock.getClose()[i]);
                            if (type == 1) {
                                if (bdate.getTime() == today.getTime()) {
                                    //if (engulfingPattern(stock) == 100) {
                                    System.out.println(stock.getName() + "," + stock.getDateTime()[i] + ", " + stock.getClose()[i] + "  " + stock.getVolume()[i] + "  " + stock.getClose()[stock.getClose().length - 1]);
                                    System.out.println(" CCI :: " + c + "  " + cci[i - 1] + "  " + cci[i - 2] + ", ADX :: " + adx[i] + " C Diff " + (cci[i - 1] - cci[i]));
                                    System.out.println(" EMA3 :: " + ema3[i] + " EMA15 " + ema15[i] + " UPTrend " + upPeriod + " ATR " + atr[i] + ", RSI :: " + rsi[i]);
                                    StockHelper.getFundamentals(stock.getName());
                                    System.out.println("------------------------------------------------");
                                }
                                //}
                            }
                            dwnperiod = 0;
                        }

                    } else {
                        if (trend.equals("down")) {
                            dwnperiod++;
//                            if (dwnperiod>4 && stock.getClose()[i] > stock.getClose()[i-1]) {
//                                if(cci[i]>cci[i-1] && cci[i-1]>cci[i-2]){
//                                     b = stock.getClose()[i];
//                                bdate = stock.getDateTime()[i];
//                                c = cci[i];
//                                atrr=atr[i];
//                                    if (bdate.getTime() == today.getTime()) {
//                                                System.out.println(stock.getName() + "," + stock.getDateTime()[i] + ", " + stock.getClose()[i] + "  " + stock.getVolume()[i]+"  "+stock.getClose()[stock.getClose().length-1]);
//                                                System.out.println(" CCI :: " + c + "  " + cci[i - 1] + "  " + cci[i - 2] + ", ADX :: " + adx[i] + " C Diff " + (cci[i - 1] - cci[i])+cci[cci.length-1]);
//                                                System.out.println(" EMA3 :: " + ema3[i] + " EMA15 " + ema15[i] + " UPTrend " + upPeriod + " ATR " + atr[i] + ", RSI :: " + rsi[i]);
//                                                StockHelper.getFundamentals(stock.getName());
//                                                System.out.println("------------------------------------------------");
//                                    }
//                                }
//                            }
                        } else {
                            trend = "down";
                            dwnperiod = 1;

//                            //System.out.println(" trend Change from up to down :: length " + upPeriod+" stock "+stock.getDateTime()[i]+"  "+stock.getClose()[i]);
                            upPeriod = 0;
                        }

                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (type == 2) {
            //System.out.println(stock.getName() + " Total  " + t + "  pt " + pt + "  lt  " + lt + " % " + (t / 100));
            System.out.println(stock.getName() + "," + t + "," + pt + "," + lt + "," + (t / 100));
        }

    }
}
