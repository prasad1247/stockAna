/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 *
 * @author PRASAD
 */
public class DownloadBSEData {

   static String bseUrl="http://www.bseindia.com/markets/equity/EQReports/MarketWatch.aspx?expandable=2";
    
     public static String getMBrands() {
        try {
            Document doc = Jsoup.connect(bseUrl).get();
            Elements ls = doc.select("div.listingblock > ul > li > a[href]");
            for (int j = 0; j < ls.size(); j++) {
                System.out.println("" + ls.get(j).text() + "   " + ls.get(j).absUrl("href"));
            }
        } catch (IOException ex) {
            Logger.getLogger(DownloadBSEData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    
}
