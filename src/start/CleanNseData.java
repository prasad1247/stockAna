/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package start;

import bean.StockDataBean;
import bean.StockPrice;
import download.DownloadFromGoogle;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hp
 */
public class CleanNseData {

    static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    static SimpleDateFormat nsedateFormat = new SimpleDateFormat("dd-MMM-yy");
    private static SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy,HH:mm:ss");

    public static void main(String[] args) {
        
        cleanNseDataOther("");
//        String data;
//        HashMap AllStockMap = new HashMap();
//        File dataFolder = new File("E:\\prasad\\NSE EOD Data Downloader v3.0\\equity");
//        File dataFiles[] = dataFolder.listFiles();
//        try {
//            for (int i = 0; i < dataFiles.length; i++) {
//                BufferedReader br = new BufferedReader(new FileReader(dataFiles[i]));
//                String line = "";
//                while ((line = br.readLine()) != null) {
//                    String[] tarray = line.split(",");
//                    StockDataBean sdb = new StockDataBean();
//                    sdb.setName(tarray[0]);
//                    sdb.setDate(tarray[1]);
    //                    Date d = dateFormat.parse(tarray[1]);
//                    sdb.setDateTime(d);
//                    sdb.setOpen(Double.parseDouble(tarray[2]));
//                    sdb.setHigh(Double.parseDouble(tarray[3]));
//                    sdb.setLow(Double.parseDouble(tarray[4]));
//                    sdb.setClose(Double.parseDouble(tarray[5]));
//                    sdb.setVolume(Integer.parseInt(tarray[6]));
//                    LinkedList dataList = (LinkedList) AllStockMap.get(sdb.getName());
//                    if (dataList != null) {
//                        dataList.add(sdb);
//                    } else {
//                        dataList = new LinkedList();
//                        dataList.add(sdb);
//                    }
//                    AllStockMap.put(sdb.getName(), dataList);
//                }
//            }
//        } catch (Exception e) {
//        }
//        System.out.println("");
//
//        Iterator it = AllStockMap.keySet().iterator();
//        String path = "D:\\Trading\\RealDATA\\";
//        File f = new File("D:\\Trading\\RealDATA\\s2.txt");
//        PrintWriter writer = null;
//        LinkedList stockNameList = StockHelper.getStockName();
//        boolean flag = false;
//        try {
//            writer = new PrintWriter(f);
//            while (it.hasNext()) {
//                String sname = (String) it.next();
//                LinkedList dataList = (LinkedList) AllStockMap.get(sname);
//                System.out.println("" + sname);
////              
////                for(Object o:stockNameList){
////                    String n1=(String)o;
////                    if(n1.equals(sname)){
////                        flag=true;
////                        break;
////                    }
////                }
////                if(flag){
////                    flag=false;
////                    continue;
////                }
////                saveInFile(dataList, path+sname + ".csv");
//                writer.println(sname);
////                StockHelper.insertData(dataList);
//
//            }
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(CleanNseData.class.getName()).log(Level.SEVERE, null, ex);
//        } finally {
//            writer.close();
//        }

    }

    public static void cleanNseData(String filename) {
        BufferedReader br = null;
        try {
            File ff = new File("D:\\trading\\RealData\\hdil_n.txt");
            br = new BufferedReader(new FileReader(ff));
            String line = "";
            LinkedList dataList = new LinkedList();
            while ((line = br.readLine()) != null) {
                String[] tarray = line.split(",");
                StockPrice sdb = new StockPrice();
                
                sdb.setDate(tarray[2]);
//                System.out.println(tarray[2]);
                Date d = nsedateFormat.parse(tarray[2]);
                sdb.setDateTime(d);
                sdb.setOpen(Double.parseDouble(tarray[4]));
                sdb.setHigh(Double.parseDouble(tarray[5]));
                sdb.setLow(Double.parseDouble(tarray[6]));
                sdb.setClose(Double.parseDouble(tarray[8]));
                sdb.setVolume(Integer.parseInt(tarray[10]));
                dataList.add(sdb);
            }
            new DownloadFromGoogle().saveInFile(dataList,"D:\\trading\\RealData\\HDIL_NSE.csv");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CleanNseData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CleanNseData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(CleanNseData.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(CleanNseData.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private static SimpleDateFormat formatterOther = new SimpleDateFormat("yyyyMMdd,HH:mm");
    
    public static void cleanNseDataOther(String filename) {
        BufferedReader br = null;
         int cnt=0;
        try {
            File ff = new File("D:\\trading\\RealData\\nifty_1min2.txt");
            br = new BufferedReader(new FileReader(ff));
            String line = "";
            LinkedList dataList = new LinkedList();
           
            while ((line = br.readLine()) != null) {
                String[] tarray = line.split(",");
                StockPrice sdb = new StockPrice();
                cnt++;
                sdb.setDate(tarray[1]);
//                System.out.println(tarray[2]);
                Date d = formatterOther.parse(tarray[1]+","+tarray[2]);
                sdb.setDateTime(d);
                sdb.setOpen(Double.parseDouble(tarray[3]));
                sdb.setHigh(Double.parseDouble(tarray[4]));
                sdb.setLow(Double.parseDouble(tarray[5]));
                sdb.setClose(Double.parseDouble(tarray[6]));
                sdb.setVolume(Integer.parseInt(tarray[7]));
                dataList.add(sdb);
            }
            new DownloadFromGoogle().saveInFile(dataList,"D:\\trading\\RealData\\NSE_1Min2.csv");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CleanNseData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CleanNseData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(CleanNseData.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            System.out.println("cnt  "+cnt);
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(CleanNseData.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
 
    
    public static void saveInFile(LinkedList stockList, String path) {
        File f = new File(path);
        BufferedWriter output = null;
        StockDataBean stockPrice;
        String dateFormat = "";

        try {
            FileWriter fr = new FileWriter(f, true);
            output = new BufferedWriter(fr);
            try {
                PrintWriter writer = new PrintWriter(f);
                writer.print("");
                writer.close();
            } catch (Exception e) {
            }

            for (Object s : stockList) {
                stockPrice = (StockDataBean) s;
                dateFormat = formatter.format(stockPrice.getDateTime());

                output.append(dateFormat + ",");
                output.append("" + stockPrice.getOpen() + ",");
                output.append("" + stockPrice.getHigh() + ",");
                output.append("" + stockPrice.getLow() + ",");
                output.append("" + stockPrice.getClose() + ",");
                output.append("" + stockPrice.getVolume() + ",");
                output.append("\n");
            }
            output.close();
            fr.close();

        } catch (IOException ex) {
        }
    }
}
