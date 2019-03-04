/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.ApplicationConstants;

/**
 *
 * @author PRASAD
 */
public class FileUpdater {

    private static SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy,HH:mm:ss");

    public static void main(String[] args) {
        String symbol = "NIFTY";
        LinkedList stockPriceList = new LinkedList();
        String fileName = ApplicationConstants.SYMBOL_PATH + "/" + symbol + ".csv";
        String path = ApplicationConstants.SYMBOL_PATH + "/" + "NIFTY2.csv";
        int cnt = 0;
        int cnt2 = 0;
        BufferedWriter output = null;

        try {
            File f1 = new File(path);
            FileWriter fw = new FileWriter(f1, true);
            output = new BufferedWriter(fw);
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String tempInput = "";
            while ((tempInput = br.readLine()) != null
                    && !tempInput.equals("")) {
                if (cnt > 11) {
                    if (cnt2 < 4) {

                        output.append(tempInput);

                        output.append("\n");
                        cnt2++;
                    } else {
                        output.append(tempInput);
                        output.append("\n");
                        cnt2 = 0;
                        System.out.println("sleeping");
                        output.close();
                        Thread.sleep(2000);
                        fw = new FileWriter(f1, true);
                        output = new BufferedWriter(fw);
                    }
                }
                cnt++;

            }
            String[] lastRec = tempInput.split(",");
            Date lDate = formatter.parse(lastRec[0] + "," + lastRec[1]);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileUpdater.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileUpdater.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(FileUpdater.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(FileUpdater.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
