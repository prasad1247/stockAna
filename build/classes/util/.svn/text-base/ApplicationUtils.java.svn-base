/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class ApplicationUtils {

    public static LinkedList readSymbols(String fileName) {

        FileReader fr;
        LinkedList symbolList = new LinkedList();
        File f = new File(fileName);
        try {
            fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            String strLine;
            //Read File Line By Line
            while ((strLine = br.readLine()) != null) {
                symbolList.add(strLine.trim());
            }
        } catch (IOException ex) {
            Logger.getLogger(ApplicationUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return symbolList;
    }
    public static double Round(double Rval, int Rpl) {
        double p = Math.pow(10, Rpl);
        Rval *= p;
        float tmp = Math.round(Rval);
        return (double) tmp / p;
    }
}
