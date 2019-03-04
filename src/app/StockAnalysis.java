/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MAType;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

import StockBean.DataMap;
import bean.Stock;
import bean.StockPrice;
import bean.TransferObj;
import download.DownloadFromGoogle;
import start.StockHelper;
import util.ApplicationConstants;
import util.ApplicationUtils;

/**
 *
 * @author Administrator
 */
public class StockAnalysis implements Callable<Stock> {

    String symbol;

    public StockAnalysis() {
    }

    public StockAnalysis(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public Stock call() throws Exception {
        Stock stock = getDatafromFile(symbol);
        try {

            stock.setName(symbol);
            stock = addIndicators(stock);
            stock.setEmaSignal(TradeSignals.sig_ema_315(stock));
        } catch (Exception e) {
            throw e;
        }
        return stock;
    }

    public TransferObj displayAllStockList() {
        TransferObj tfo = new TransferObj();
        LinkedList symbolList = ApplicationUtils.readSymbols(ApplicationConstants.SYMBOL_LIST_PATH);
        ExecutorService executor = Executors.newFixedThreadPool(1);
        String symbol = "";
        LinkedList<Stock> allStockList = new LinkedList<Stock>();
        System.setProperty("http.proxyHost", "10.0.3.111");
        System.setProperty("http.proxyPort", "8080");
        for (Object sym : symbolList) {
            try {
                symbol = (String) sym;
                System.out.println("For Symbol:" + symbol);
                StockAnalysis sa = new StockAnalysis(symbol);
                //  Future<Stock> newStock = executor.submit(sa);
                Stock stock = getDatafromFile(symbol);

                try {

                    stock.setName(symbol);
                    stock = addIndicators(stock);
                    stock.setEmaSignal(TradeSignals.sig_ema_315(stock));
                } catch (Exception e) {
                    throw e;
                }
                allStockList.add(stock);

            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
            }
        }
        executor.shutdown();
        tfo.setAllstockList(allStockList);
        return tfo;
    }

    public TransferObj downloadAllStocks() {
        TransferObj tfo = new TransferObj();
        LinkedList symbolList = ApplicationUtils.readSymbols(ApplicationConstants.SYMBOL_LIST_PATH);
        String symbol = "";
        DownloadFromGoogle dfg = new DownloadFromGoogle();
        LinkedList allStockList = new LinkedList();
        for (Object sym : symbolList) {
            try {
                symbol = (String) sym;
                System.out.println("For Symbol:" + symbol);
                Stock sp = dfg.downloadStockDetails(symbol, ApplicationConstants.INTERVAL_SEC, ApplicationConstants.PERIOD);
                sp.setName(symbol);
                allStockList.add(sp);
                break;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        tfo.setAllstockList(allStockList);
        return tfo;
    }

    public Stock getDatafromFile(String symbol) throws IOException {
        LinkedList stockPriceList = new LinkedList();
        String fileName = ApplicationConstants.SYMBOL_PATH + "/" + symbol + ".csv";
        Stock mainBean = null;
        int serice = count(fileName);
        Date[] date = new Date[serice];
        double[] high = new double[serice];
        double[] low = new double[serice];
        double[] open = new double[serice];
        double[] close = new double[serice];
        double[] volume = new double[serice];
        File dataFile = new File(fileName);
        Date d2 = new Date();
        BufferedReader br = null;
        //     System.out.println("aa   "+dataFile.lastModified() +"    "+(d2.getTime()+200000));
        if (dataFile.exists()) {
//            dataFile.setReadOnly();
            String strLine = "";
            mainBean = new Stock();
            String[] dataArray = null;
            int i = 0;
            int interval = 1;
            DateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            //   DateFormat formatter1 =new SimpleDateFormat("dd-MM-yyyy HH:mm:ss") ;
            DateFormat fmtter1 = new SimpleDateFormat("dd-MMM-yy");
            String dateString = "";
            try {
                br = new BufferedReader(new FileReader(dataFile));
                while ((strLine = br.readLine()) != null) {
                    dataArray = strLine.split(",");
                    Date d = formatter1.parse(dataArray[0] + " " + dataArray[1]);
                    dateString = fmtter1.format(d);
                    StockPrice sp = new StockPrice();
                    sp.setDateTime(d);
                    sp.setDate(dateString);
                    sp.setOpen(Double.parseDouble(dataArray[2]));
                    sp.setHigh(Double.parseDouble(dataArray[3]));
                    sp.setLow(Double.parseDouble(dataArray[4]));
                    sp.setClose(Double.parseDouble(dataArray[5]));
                    sp.setVolume(Integer.parseInt(dataArray[6]));
                    stockPriceList.add(sp);

                }

                Collections.sort(stockPriceList, new Comparator<StockPrice>() {
                    @Override
                    public int compare(StockPrice o1, StockPrice o2) {
                        return o1.getDateTime().compareTo(o2.getDateTime());
                    }
                });

                for (Object o : stockPriceList) {
                    try{
                    StockPrice o1 = (StockPrice) o;
                    date[i] = o1.getDateTime();
                    open[i] = o1.getOpen();
                    high[i] = o1.getHigh();
                    low[i] = o1.getLow();
                    close[i] = o1.getClose();
                    volume[i] = o1.getVolume();
                    i++;
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
                mainBean.setDateTime(date);
                mainBean.setClose(close);
                mainBean.setHigh(high);
                mainBean.setLow(low);
                mainBean.setOpen(open);
                mainBean.setVolume(volume);
                mainBean.setStockList(stockPriceList);
                mainBean.setName(symbol);

            } catch (ParseException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                br.close();
            }
        } else {
            DownloadFromGoogle dfg = new DownloadFromGoogle();
            dfg.downloadAndSaveDetails(symbol, ApplicationConstants.INTERVAL_SEC, ApplicationConstants.PERIOD, fileName);
            mainBean = getDatafromFile(symbol);
            System.out.println("downloading");
        }
        return mainBean;
    }

    public Stock getDatafromDB(String symbol) throws IOException {
        LinkedList stockPriceList = new LinkedList();
        Stock mainBean = null;
            String strLine = "";
            mainBean = new Stock();
            String[] dataArray = null;
            int i = 0;
            int interval = 1;
            DateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            //   DateFormat formatter1 =new SimpleDateFormat("dd-MM-yyyy HH:mm:ss") ;
            DateFormat fmtter1 = new SimpleDateFormat("dd-MMM-yy");
            String dateString = "";
            try {
        		bean.DataMap map = StockHelper.executeQuery1(
        				"select * from stockdata s,stock_info i where s.stock_id=i.id and i.stock_name='AXIS_Bank' and open > 0 order by s.stock_time asc");
        	       int serice = (map.getData().size());
        	        Date[] date = new Date[serice];
        	        double[] high = new double[serice];
        	        double[] low = new double[serice];
        	        double[] open = new double[serice];
        	        double[] close = new double[serice];
        	        double[] volume = new double[serice];
        	        Date d2 = new Date();
        	 
        		while (map.next()) {
                    dataArray = strLine.split(",");
                    Date d = new Date(map.getInt("stock_time")*1000L);
                    dateString = fmtter1.format(d);
                    StockPrice sp = new StockPrice();
                    sp.setDateTime(d);
                    sp.setDate(dateString);
                    sp.setOpen(map.getFloat("open"));
                    sp.setHigh( map.getFloat("high"));
                    sp.setLow( map.getFloat("low"));
                    sp.setClose( map.getFloat("close"));
                    sp.setVolume(map.getFloat("volume").intValue());
                    stockPriceList.add(sp);

                }

                Collections.sort(stockPriceList, new Comparator<StockPrice>() {
                    @Override
                    public int compare(StockPrice o1, StockPrice o2) {
                        return o1.getDateTime().compareTo(o2.getDateTime());
                    }
                });

                for (Object o : stockPriceList) {
                    try{
                    StockPrice o1 = (StockPrice) o;
                    date[i] = o1.getDateTime();
                    open[i] = o1.getOpen();
                    high[i] = o1.getHigh();
                    low[i] = o1.getLow();
                    close[i] = o1.getClose();
                    volume[i] = o1.getVolume();
                    i++;
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
                mainBean.setDateTime(date);
                mainBean.setClose(close);
                mainBean.setHigh(high);
                mainBean.setLow(low);
                mainBean.setOpen(open);
                mainBean.setVolume(volume);
                mainBean.setStockList(stockPriceList);
                mainBean.setName(symbol);

            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
            }
        return mainBean;
    }

    public int count(String filename) throws IOException {
        File f = new File(filename);
        boolean empty = true;
        int count = 0;
        if (f.exists()) {
            InputStream is = new BufferedInputStream(new FileInputStream(f));
            try {
                byte[] c = new byte[1024];
                int readChars = 0;
                while ((readChars = is.read(c)) != -1) {
                    empty = false;
                    for (int i = 0; i < readChars; ++i) {
                        if (c[i] == '\n') {
                            ++count;
                        }
                    }
                }
            } finally {
                is.close();
            }
        }
        return (count == 0 && !empty) ? 1 : count;
    }

    public Stock addIndicators(Stock stock) {
        HashMap indicators = new HashMap();
        double input[] = stock.getClose();
        double output[] = new double[input.length];
        double output1[] = new double[input.length];
        double output2[] = new double[input.length];
        double output3[] = new double[input.length];
        double output4[] = new double[input.length];
        double output5[] = new double[input.length];
        double output6[] = new double[input.length];
        int outputInt[];
        MInteger outBegIdx = new MInteger();
        MInteger outNbElement = new MInteger();
        RetCode retCode;
        Core lib = new Core();
        int lookback;

        lookback = lib.emaLookback(15);
        retCode = lib.ema(0, input.length - 1, input, lookback + 1, outBegIdx, outNbElement, output);
        output = rightJustify(output, outBegIdx.value);
        indicators.put(ApplicationConstants.EMA_15, output);

        lookback = lib.emaLookback(3);
        retCode = lib.ema(0, input.length - 1, input, lookback + 1, outBegIdx, outNbElement, output1);
        output = rightJustify(output1, outBegIdx.value);
        indicators.put(ApplicationConstants.EMA_3, output1);

        retCode = lib.adx(0, input.length - 1, stock.getHigh(), stock.getLow(), stock.getClose(), 14, outBegIdx, outNbElement, output2);
        output = rightJustify(output2, outBegIdx.value);
        indicators.put(ApplicationConstants.ADX, output2);

        retCode = lib.rsi(0, input.length - 1, stock.getClose(), 14, outBegIdx, outNbElement, output3);
        output = rightJustify(output3, outBegIdx.value);
        indicators.put(ApplicationConstants.RSI, output3);

        lib.atr(0, input.length - 1, stock.getHigh(), stock.getLow(), stock.getClose(), 15, outBegIdx, outNbElement, output4);
        output = rightJustify(output4, outBegIdx.value);
        indicators.put(ApplicationConstants.ATR, output4);

        output1 = new double[input.length];
        output2 = new double[input.length];
        output3 = new double[input.length];

        lib.bbands(0, input.length - 1, input, 20, 2, 2, MAType.Sma, outBegIdx, outNbElement, output1, output2, output3);
        output4 = rightJustify(output1, outBegIdx.value);
        output5 = rightJustify(output2, outBegIdx.value);
        output6 = rightJustify(output3, outBegIdx.value);

         indicators.put(ApplicationConstants.BBUpper, output4);
         indicators.put(ApplicationConstants.BBSma, output5);
         indicators.put(ApplicationConstants.BBLower, output6);
         
        
//        lookback = lib.rsiLookback(8);
//        retCode = lib.rsi(0, input.length - 1, input, lookback + 1, outBegIdx, outNbElement, output3);
//        output = rightJustify(output3, lookback);
//        indicators.put(ApplicationConstants.RSI, output3);
        lib.cci(0, input.length - 1, stock.getHigh(), stock.getLow(), stock.getClose(), 20, outBegIdx, outNbElement, output5);
        output = rightJustify(output5, outBegIdx.value);
        indicators.put("CCI", output5);
        stock.setIndicators(indicators);
        return stock;
    }

    public static double[] rightJustify(double[] digits, int len) {
        try {
            System.arraycopy(digits, 0, digits, len, digits.length - len);
            Arrays.fill(digits, 0, len, 0);
        } catch (Exception e) {
            System.out.println(" faulty " + digits);
        }
        return digits;
    }

    public void changeTimeFrame(Stock stock, long time) {
        double oldC[] = stock.getClose();
        double oldO[] = stock.getOpen();
        double oldL[] = stock.getLow();
        double oldH[] = stock.getHigh();
        int count = 0;
        Double c = 0d;
        Double o = 0d;
        Double l = 0d;
        Double h = 0d;
        Date dateTime[] = stock.getDateTime();
        Date startDate = dateTime[0];
        Date nextDate = new Date((startDate.getTime() + time));
        for (int i = 0; i < dateTime.length; i++) {
            if (dateTime[i].getTime() < nextDate.getTime()) {
                c += oldC[i];
                o += oldO[i];
                l += oldL[i];
                h += oldH[i];
                count++;
            } else {
                nextDate = new Date((dateTime[i].getTime() + time));

            }
        }
    }
}
