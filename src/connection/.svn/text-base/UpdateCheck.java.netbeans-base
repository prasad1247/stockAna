/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package connection;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author Administrator
 */
public class UpdateCheck {

    public static void readFile() {
        int data = -1;
        String generateURL = "";
        //SET THE DESTINATION ZIP FILE
        //--------------------------------------------------------------
        //
        File tempFile = new File(System.getProperty("user.dir") + "/Temp/version.txt");
        BufferedOutputStream fileWriter = null;
        try {
            fileWriter = new BufferedOutputStream(new FileOutputStream(tempFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //---------------------------------------------------------------
        //READ THE FILE
        generateURL = "https://docs.google.com/document/d/1CJfTk3LVj_uY2-3MyCbWu3ie59E5DP4S_r5sa-b6jPI/export?format=txt&id=1CJfTk3LVj_uY2-3MyCbWu3ie59E5DP4S_r5sa-b6jPI&authkey=CPCd4a4F&tfe=yh_119";
        URL file = null;
        HttpURLConnection fileHttp = null;
        try {
            file = new URL(generateURL);
            fileHttp = (HttpURLConnection) file.openConnection();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
      //  fileHttp.setRequestProperty("user-agent", "User Agent: Mozilla/5.0 (compatible; Konqueror/4.1; Linux) KHTML/4.1.3 (like Gecko) SUSE");
        fileHttp.setDoInput(true);
        BufferedInputStream fileReader = null;
        try {
            fileReader = new BufferedInputStream(fileHttp.getInputStream());
            while ((data = fileReader.read()) != -1) {
                fileWriter.write(data);
            }
            fileWriter.close();
        } catch (IOException e) {
            //System.out.println("File "+type+"_"+date+".zip not found on website");
            e.printStackTrace();
        }
    }
}
