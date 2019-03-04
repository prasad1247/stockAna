/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import bean.Stock;
import bean.StockPrice;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author PRASAD
 */
public class ChangeTimeFrame {

    static SimpleDateFormat formatter1 = new SimpleDateFormat("HH:mm:ss");
    static SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
    static SimpleDateFormat formatter2 = new SimpleDateFormat("dd-MM-yyyy,HH:mm:ss");

    public static void main(String[] args) {
        try {
            int timeMultiplier = 15;  //for 3 mins 2,5 mins=4,for 1hr= 59,15 mins=14 etc 
            StockAnalysis ana = new StockAnalysis();
            Stock s = ana.getDatafromFile("ABIRLANUVO");
            int mod = 11 % 5;
            System.out.println("" + (mod + 11));
            
            s = changeTimeFrame(s.getStockList(),s, timeMultiplier);
//
//            for (int i = 0; i < newList.size(); i++) {
//                System.out.println("" + newList.get(i));
//            }

        } catch (IOException ex) {
            Logger.getLogger(ChangeTimeFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//    public static LinkedList changeTimeFrame(LinkedList ls, int timeMultiplier) {
//        double currH = 0, currL = 999999999, currO = 0, currC;
//        Date currD = null;
//        int currV = 0;
//        int cuurT = 0;
//        LinkedList newList = new LinkedList();
//        for (int i = 0; i < ls.size(); i++) {
//            StockPrice sp1 = (StockPrice) ls.get(i);
//            currH = Math.max(sp1.getHigh(), currH);
//            currL = Math.min(sp1.getLow(), currL);
//            currV += sp1.getVolume();
//            if (cuurT == 0) {
//                currO = sp1.getOpen();
//
//            }
//            if (formatter1.format(sp1.getDateTime()).contains("09:15:00")) {
//                if (timeMultiplier < 9) {  // 3 min,5 min
//                    cuurT = -1;
//                } else if (timeMultiplier == 9) { //10 min
//                    cuurT = -6;
//                } else if (timeMultiplier < 30) { // 15min,30 min
//                    cuurT = -1;
//                } else if (timeMultiplier > 30 && timeMultiplier < 60) { // 15min,30 min
//                    cuurT = -1;
//                }
//            }
//
//            if (cuurT == timeMultiplier || ls.size() == (i + 1)) {
//                currC = sp1.getClose();
//                currD = sp1.getDateTime();
//                StockPrice sp = new StockPrice();
//                sp.setClose(currC);
//                sp.setOpen(currO);
//                sp.setHigh(currH);
//                sp.setLow(currL);
//                sp.setVolume(currV);
//                sp.setDateTime(currD);
//                newList.add(sp);
//                currH = 0;
//                currL = 999999999;
//                currV = 0;
//                currO = 0;
//                currC = 0;
//                cuurT = -1;
//            }
//            
//                    
//            cuurT++;
//        }
//        return newList;
//    }
    public static Stock changeTimeFrame(LinkedList ls, Stock s, int minutesT) {
        double currH = 0, currL = 999999999, currO = 0, currC;
        Date currD = null, startD = null, endDay = null;

        int currV = 0;
        int cuurT = 0;
        String startTime = "09:15:00";
        String endTime = "15:30:00";
        Date tDate = null;
        Calendar calendar = Calendar.getInstance();
        LinkedList newList = new LinkedList();
        for (int i = 0; i < ls.size(); i++) {
            try {
                StockPrice sp1 = (StockPrice) ls.get(i);
                String sDate = formatter.format(sp1.getDateTime());
                currH = Math.max(sp1.getHigh(), currH);
                currL = Math.min(sp1.getLow(), currL);
                currV += sp1.getVolume();

                if (endDay != null && sp1.getDateTime().getTime() < endDay.getTime()) {
                    if (currO == 0) {
                        currO = sp1.getOpen();
                    }
                    if (tDate.getTime() <= sp1.getDateTime().getTime()) {
                        currC = sp1.getClose();
                        currD = sp1.getDateTime();
                        StockPrice sp = new StockPrice();
                        sp.setClose(currC);
                        sp.setOpen(currO);
                        sp.setHigh(currH);
                        sp.setLow(currL);
                        sp.setVolume(currV);
                        sp.setDateTime(currD);
                        newList.add(sp);
                        currH = 0;
                        currL = 999999999;
                        currV = 0;
                        currO = 0;
                        currC = 0;
                        cuurT = -1;
                        calendar.setTime(tDate);
                        calendar.add(Calendar.MINUTE, minutesT);
                        tDate = calendar.getTime();
                    }

                } else if (endDay != null && sp1.getDateTime().getTime() == endDay.getTime()) {
                    currC = sp1.getClose();
                    currD = tDate;
                    StockPrice sp = new StockPrice();
                    sp.setClose(currC);
                    sp.setOpen(currO);
                    sp.setHigh(currH);
                    sp.setLow(currL);
                    sp.setVolume(currV);
                    sp.setDateTime(currD);
                    newList.add(sp);

                    currH = 0;
                    currL = 999999999;
                    currV = 0;
                    currO = 0;
                    currC = 0;
                    cuurT = -1;
                } else {
                    startD = formatter2.parse(sDate + "," + startTime);
                    endDay = formatter2.parse(sDate + "," + endTime);
                    currO = sp1.getOpen();
                    calendar.setTime(startD);
                    calendar.add(Calendar.MINUTE, minutesT);
                    tDate = calendar.getTime();
                }
            } catch (ParseException ex) {
                Logger.getLogger(ChangeTimeFrame.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        int serice = newList.size();
        Date[] date = new Date[serice];
        double[] high = new double[serice];
        double[] low = new double[serice];
        double[] open = new double[serice];
        double[] close = new double[serice];
        double[] volume = new double[serice];
        int i=0;
        for (Object o : newList) {
            StockPrice o1 = (StockPrice) o;
            date[i] = o1.getDateTime();
            open[i] = o1.getOpen();
            high[i] = o1.getHigh();
            low[i] = o1.getLow();
            close[i] = o1.getClose();
            volume[i] = o1.getVolume();
            i++;
        }
        s.setClose(close);
        s.setClose(close);
        s.setHigh(high);
        s.setLow(low);
        s.setOpen(open);
        s.setVolume(volume);
        s.setStockList(newList);
        return s;
    }

}
