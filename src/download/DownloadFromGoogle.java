/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package download;

import bean.Stock;
import bean.StockPrice;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class DownloadFromGoogle implements Runnable {

    private SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy,HH:mm:ss");
    static long Meantime = 0l;
    long time = 0l;
    String dateFormat = "";
    private String sym;
    private String path;
    private int inter;
    private String per;

    public DownloadFromGoogle() {
    }

    public DownloadFromGoogle(String symbol, int interval, String period, String savePath) {
        this.sym = symbol;
        this.inter = interval;
        this.per = period;
        this.path = savePath;

    }

    public BufferedReader getData(String symbol, int interval, String period, boolean firstRun) {
        String urlString = "";
        if (firstRun) {
            urlString = "http://www.google.com/finance/getprices?q=" + symbol + "&x=NSE&i=" + interval + "&p=" + period + "&f=d,o,h,l,c,v";
        } else {
            urlString = "http://www.google.com/finance/info?client=ig&q=" + symbol;
        }
        BufferedReader in = null;
        try {
            System.out.println("url  "+urlString);
            URL url = new URL(urlString);
            long start = System.currentTimeMillis();
            in = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));
            System.out.println(" time " + (System.currentTimeMillis() - start));
        } catch (IOException ex) {
            Logger.getLogger(DownloadFromGoogle.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        return in;
    }

    public Stock parseFromStreamData(BufferedReader in, int interval) {
        String split[] = new String[6];
        String inputLine = "";
        int i = 0;
        LinkedList stockPriceList = new LinkedList();
        Stock stock = new Stock();
        try {
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.contains("EXCHANGE") || inputLine.contains("MARKET") || inputLine.contains("INTERVAL") || inputLine.contains("COLUMNS") || inputLine.contains("DATA") || inputLine.contains("TIMEZONE")) {
                    continue;
                } else {
                    if (inputLine.contains("a1")) {
                        split = inputLine.split(",");
                        Meantime = Long.parseLong(split[0].substring(1));
                        time = Meantime * 1000;
                    } else {
                        split = inputLine.split(",");
                        int abc = Integer.parseInt(split[0]);
                        time = (Meantime + (abc * interval)) * 1000;
                    }
                    Date d = new Date(time);
                    if (priceWithintime(d) || interval==86400) {
                        dateFormat = formatter.format(d);
                        StockPrice sp = new StockPrice();
                        sp.setDateTime(d);
                        sp.setOpen(Double.parseDouble(split[4]));
                        sp.setHigh(Double.parseDouble(split[2]));
                        sp.setLow(Double.parseDouble(split[3]));
                        sp.setClose(Double.parseDouble(split[1]));
                        sp.setVolume(Integer.parseInt(split[5]));
                        stockPriceList.add(sp);
                        i++;
                    }
                }
            }
            in.close();
            stock.setStockList(stockPriceList);
        } catch (IOException ex) {
            Logger.getLogger(DownloadFromGoogle.class.getName()).log(Level.SEVERE, null, ex);
        }
        return stock;

    }

    private boolean priceWithintime(Date d) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String d1 = "09:14:00";
        String d2 = "15:31:00";
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

    public void saveInFile(LinkedList stockList, String path) {
        File f = new File(path);
        BufferedWriter output = null;
        StockPrice stockPrice;
        String dateFormat = "";
        Date lDate = null;
        String inputLine = "";
        BufferedReader br = null;
        FileReader fr = null;
        int cnt = 0;
      
        try {
            fr = new FileReader(f);
            br = new BufferedReader(fr);
        } catch (FileNotFoundException ex) {
         //   Logger.getLogger(DownloadFromGoogle.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (br != null) {
            try {

                String tempInput = "";
                while ((tempInput = br.readLine()) != null
                        && !tempInput.equals("")) {
                    if (cnt == 1705) {
                        System.out.println("break");
                    }
                    inputLine = tempInput;
                    cnt++;
                }
                String[] lastRec = inputLine.split(",");
                lDate = formatter.parse(lastRec[0] + "," + lastRec[1]);
            } catch (Exception e) {
                try {
                    System.out.println("cnt   " + cnt + "   " + br.readLine());
                } catch (IOException ex) {
                    Logger.getLogger(DownloadFromGoogle.class.getName()).log(Level.SEVERE, null, ex);
                }
                e.printStackTrace();
            } finally {
                try {
                    br.close();
                    fr.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        }

        try {
            File f1 = new File(path);
            FileWriter fw = new FileWriter(f1, true);
            output = new BufferedWriter(fw);
//            try {
//                PrintWriter writer = new PrintWriter(f);
//                writer.print("");
//                writer.close();
//            } catch (Exception e) {
//            }

            for (Object s : stockList) {
                stockPrice = (StockPrice) s;
                
                if (lDate == null || lDate.getTime() < stockPrice.getDateTime().getTime()) {
                    dateFormat = formatter.format(stockPrice.getDateTime());
                    output.append(dateFormat + ",");
                    output.append("" + stockPrice.getOpen() + ",");
                    output.append("" + stockPrice.getHigh() + ",");
                    output.append("" + stockPrice.getLow() + ",");
                    output.append("" + stockPrice.getClose() + ",");
                    output.append("" + stockPrice.getVolume() + ",");
                    output.append("\n");
                }

            }
            output.close();
            fw.close();

        } catch (IOException ex) {
            Logger.getLogger(DownloadFromGoogle.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Stock downloadStockDetails(String symbol, int interval, String period) {
        return parseFromStreamData(getData(symbol, interval, period, true), interval);
    }

    public Stock downloadAndSaveDetails(String symbol, int interval, String period, String savePath) {
        Stock sp = parseFromStreamData(getData(symbol, interval, period, true), interval);
        saveInFile(sp.getStockList(), savePath);
        return sp;
    }

    @Override
    public void run() {
        try {
            downloadAndSaveDetails(this.sym, this.inter, this.per, this.path);
            System.out.println("downloaded :: " + this.sym);
        } catch (Exception e) {
        }
    }
}
