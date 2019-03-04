/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.util.Date;
import java.util.LinkedList;

/**
 *
 * @author Administrator
 */
public class StockPrice {

    private double open;
    private double low;
    private double high;
    private double close;
    private double ltp;
    private Date dateTime;
    
    private int volume;
    String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @return the open
     */
    public double getOpen() {
        return open;
    }

    /**
     * @param open the open to set
     */
    public void setOpen(double open) {
        this.open = open;
    }

    /**
     * @return the low
     */
    public double getLow() {
        return low;
    }

    /**
     * @param low the low to set
     */
    public void setLow(double low) {
        this.low = low;
    }

    /**
     * @return the high
     */
    public double getHigh() {
        return high;
    }

    /**
     * @param high the high to set
     */
    public void setHigh(double high) {
        this.high = high;
    }

    /**
     * @return the close
     */
    public double getClose() {
        return close;
    }

    /**
     * @param close the close to set
     */
    public void setClose(double close) {
        this.close = close;
    }

    /**
     * @return the ltp
     */
    public double getLtp() {
        return ltp;
    }

    /**
     * @param ltp the ltp to set
     */
    public void setLtp(double ltp) {
        this.ltp = ltp;
    }

    /**
     * @return the dateTime
     */
    public Date getDateTime() {
        return dateTime;
    }

    /**
     * @param dateTime the dateTime to set
     */
    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }



    /**
     * @return the volume
     */
    public int getVolume() {
        return volume;
    }

    @Override
    public String toString() {
        return "StockPrice{" + "open=" + open + ", low=" + low + ", high=" + high + ", close=" + close + ", ltp=" + ltp + ", dateTime=" + dateTime + ", volume=" + volume + ", date=" + date + '}';
    }

    /**
     * @param volume the volume to set
     */
    public void setVolume(int volume) {
        this.volume = volume;
    }

    public void setDateTime(String string) {
        throw new UnsupportedOperationException("Not yet implemented");
    }


}
