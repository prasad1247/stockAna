/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import bean.PivotPointBean;
import java.util.Date;

/**
 *
 * @author PRASAD
 */
public class TradeBean {

    private Date callDate;
    private double callPrice;
    private double target;
    private double stoploss;
    private PivotPointBean lowbean;
    private PivotPointBean highbean;
    private double atr;
    private double[] adx;
    private double[] bbMid;
    private double[] rsi;
    private Date completeDate;
    private double completePrice;
    private double points;
    private double highest;
    private StockPrice buyStock;
    private StockPrice sellStock;
    private StockPrice highStock;
    
    

    /**
     * @return the callDate
     */
    public Date getCallDate() {
        return callDate;
    }

    /**
     * @param callDate the callDate to set
     */
    public void setCallDate(Date callDate) {
        this.callDate = callDate;
    }

    /**
     * @return the callPrice
     */
    public double getCallPrice() {
        return callPrice;
    }

    /**
     * @param callPrice the callPrice to set
     */
    public void setCallPrice(double callPrice) {
        this.callPrice = callPrice;
    }

    /**
     * @return the target
     */
    public double getTarget() {
        return target;
    }

    /**
     * @param target the target to set
     */
    public void setTarget(double target) {
        this.target = target;
    }

    /**
     * @return the stoploss
     */
    public double getStoploss() {
        return stoploss;
    }

    /**
     * @param stoploss the stoploss to set
     */
    public void setStoploss(double stoploss) {
        this.stoploss = stoploss;
    }

    /**
     * @return the lowbean
     */
    public PivotPointBean getLowbean() {
        return lowbean;
    }

    /**
     * @param lowbean the lowbean to set
     */
    public void setLowbean(PivotPointBean lowbean) {
        this.lowbean = lowbean;
    }

    /**
     * @return the highbean
     */
    public PivotPointBean getHighbean() {
        return highbean;
    }

    /**
     * @param highbean the highbean to set
     */
    public void setHighbean(PivotPointBean highbean) {
        this.highbean = highbean;
    }

    /**
     * @return the atr
     */
    public double[] getAdx() {
        return adx;
    }

    /**
     * @param atr the atr to set
     */
    public void setAdx(double[] adx) {
        this.adx = adx;
    }

    /**
     * @return the bbMid
     */
    public double[] getBbMid() {
        return bbMid;
    }

    /**
     * @param bbMid the bbMid to set
     */
    public void setBbMid(double[] bbMid) {
        this.bbMid = bbMid;
    }

    /**
     * @return the rsi
     */
    public double[] getRsi() {
        return rsi;
    }

    /**
     * @param rsi the rsi to set
     */
    public void setRsi(double[] rsi) {
        this.rsi = rsi;
    }

    /**
     * @return the completeDate
     */
    public Date getCompleteDate() {
        return completeDate;
    }

    /**
     * @param completeDate the completeDate to set
     */
    public void setCompleteDate(Date completeDate) {
        this.completeDate = completeDate;
    }

    /**
     * @return the completePrice
     */
    public double getCompletePrice() {
        return completePrice;
    }

    /**
     * @param completePrice the completePrice to set
     */
    public void setCompletePrice(double completePrice) {
        this.completePrice = completePrice;
    }

    /**
     * @return the poinis
     */
    public double getPoinis() {
        return points;
    }

    /**
     * @param poinis the poinis to set
     */
    public void setPoinis(double poinis) {
        this.points = poinis;
    }

    /**
     * @return the highest
     */
    public double getHighest() {
        return highest;
    }

    /**
     * @param highest the highest to set
     */
    public void setHighest(double highest) {
        this.highest = highest;
    }

    /**
     * @param atr the atr to set
     */
    public void setAtr(double atr) {
        this.atr = atr;
    }

    /**
     * @return the atr
     */
    public double getAtr() {
        return atr;
    }

    /**
     * @return the buyStock
     */
    public StockPrice getBuyStock() {
        return buyStock;
    }

    /**
     * @param buyStock the buyStock to set
     */
    public void setBuyStock(StockPrice buyStock) {
        this.buyStock = buyStock;
    }

    /**
     * @return the sellStock
     */
    public StockPrice getSellStock() {
        return sellStock;
    }

    /**
     * @param sellStock the sellStock to set
     */
    public void setSellStock(StockPrice sellStock) {
        this.sellStock = sellStock;
    }

    /**
     * @return the highStock
     */
    public StockPrice getHighStock() {
        return highStock;
    }

    /**
     * @param highStock the highStock to set
     */
    public void setHighStock(StockPrice highStock) {
        this.highStock = highStock;
    }

}
