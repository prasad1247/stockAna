/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import bean.Stock;
import download.DownloadFromGoogle;
import java.util.Date;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import util.ApplicationConstants;
import util.ApplicationUtils;

/**
 *
 * @author Administrator
 */
public class UpdateThread extends TimerTask {

    @Override
    public void run() {
        
//       updateSingleStock("RCOM");
        updateStocks();
        System.out.println("update done :: "+new Date());
    }

    public static void updateStocks() {
        LinkedList symbolList = ApplicationUtils.readSymbols(ApplicationConstants.SYMBOL_LIST_PATH);
        ExecutorService executor = Executors.newFixedThreadPool(1);
        String symbol = "";
        LinkedList allStockList = new LinkedList();
        DownloadFromGoogle dfg = null;
        try {
            for (Object sym : symbolList) {
                try {
                    symbol = (String) sym;
                    String fileName = ApplicationConstants.SYMBOL_PATH + "/" + symbol + ".csv";
                    dfg = new DownloadFromGoogle(symbol, ApplicationConstants.INTERVAL_SEC, ApplicationConstants.PERIOD1, fileName);
                    try {
                        dfg.run();
                    } catch (Exception e) {
                        throw e;
                    }
                    Thread.sleep(1000);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            executor.shutdown();
            while (!executor.isTerminated()) {
            }
        }
    }

    public void updateSingleStock(String symbol) {
        DownloadFromGoogle dfg = null;
        try {
            try {
                String fileName = ApplicationConstants.SYMBOL_PATH + "/" + symbol + ".csv";
                dfg = new DownloadFromGoogle(symbol, ApplicationConstants.INTERVAL_SEC, ApplicationConstants.PERIOD1, fileName);
                dfg.run();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } finally {
        }
    }

    public void startMonitor(UpdateThread monitor) {
        Timer timer = null;
        TimerTask timerTask = null;

        // final Process x = p;
        // final Runtime y=r;
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        timerTask = monitor;
        timer = new Timer();
        timer.schedule(timerTask, 0, 300000);
    }

    public static void main(String[] args) {
          System.setProperty("http.proxyHost", "10.0.3.111");
            System.setProperty("http.proxyPort", "8080");
        UpdateThread t = new UpdateThread();
        t.startMonitor(t);
    }

}
