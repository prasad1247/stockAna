/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import static app.HeiknAshi.updateSeries;
import static app.PivotBackTester.formatter1;
import static app.PivotBackTester.successContinue;
import static app.PivotBackTester.threeInsideBar;
import static app.PivotPoints.formatter;
import bean.PivotPointBean;
import bean.Stock;
import bean.StockPrice;
import bean.TradeBean;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.ApplicationConstants;
import util.ApplicationUtils;

/**
 *
 * @author PRASAD
 */
public class PivotForwardTester {

	private static Date stopdate = new Date();
	static LinkedList<PivotPointBean> pivotlist = new LinkedList<PivotPointBean>();
	static LinkedList refinedPivots2 = new LinkedList();
	static boolean debug = true;
	static double triggerPivot;
	static PivotPointBean triggerPL,triggerPH;
	static StockPrice triggerVal;
	static boolean initsellTrig;
	static int gapTaken = 0;
	static int insiderBarTaken = 1;
	static int sellTaken = 1;
	static double refineTaken = 0.1;
	static int timeframe = 15;
	static int immidiateFailTaken = 0;
	static int threeOppositeFailTaken = 1;
	static int candleSizeTaken = 1;

	public static void main(String[] args) {
		StockAnalysis ana = new StockAnalysis();
		int sellFlag = 0, buyFlag = 0;
		Stock s = null;
		try {
			stopdate = formatter.parse("28-01-2019,11:30:00");
			temphighBean.pvtTime = formatter.parse("28-03-2010,14:05:00");
			templowBean.pvtTime = formatter.parse("28-03-2010,14:05:00");
		} catch (ParseException ex) {
			Logger.getLogger(PivotForwardTester.class.getName()).log(Level.SEVERE, null, ex);
		}
		String symbol = "ZEEL";
		int suc = 0, fai = 0;
		// LinkedList<String> symbolList =
		// ApplicationUtils.readSymbols(ApplicationConstants.SYMBOL_LIST_PATH);
		for (int cn = 0; cn < 1; cn++) {
			try {
				// symbol = symbolList.get(cn);
				s = ana.getDatafromDB("");
			} catch (IOException ex) {
				Logger.getLogger(PivotStrategy.class.getName()).log(Level.SEVERE, null, ex);
			}
			LinkedList mainList = s.getStockList();
			pivotlist = new LinkedList<PivotPointBean>();
			// s = ChangeTimeFrame.changeTimeFrame(mainList, s, timeframe);
			LinkedList ls = s.getStockList();
			// ls =updateSeries(s.getStockList(), 0, true);
			ana.addIndicators(s);
			StringBuilder data = new StringBuilder();
			int no_of_shares = 0;
			double rupee = 2000;
			double profit = 0;
			double highest = 0;
			Date debugDate = null;
			int tradeActivated = 0, RR = 1;
			boolean buy = false, sell = false;
			double buyPrice = 0, sellPrice = 0, target = 0, stoploss = 0, points;
			double totalPoints, totalBuyPoints, totalSellPoints, buySuccessPoints = 0, buyFailedPoints = 0,
					sellSuccessPoints = 0, sellFailedPoints = 0, sellCanceledPoints = 0, buyCanceledPoints = 0;
			int totalTrades, totalBuyTrades, totalSellTrades, buySuccessTrades = 0, buyFailedTrades = 0,
					sellSuccessTrades = 0, sellFailedTrades = 0, sellCanceledTrades = 0, buyCanceledTrades = 0;
			PivotPointBean highBean = null, lowBean = null;
			String signalData = "";
			String targetData = "";
			boolean gapOpenDay = false;
			StockPrice callPrice = null;
			int buyIndex = 0;
			HashMap indicators = s.getIndicators();
			double BBUpper[] = ((double[]) indicators.get(ApplicationConstants.BBUpper));
			double BBLower[] = ((double[]) indicators.get(ApplicationConstants.BBLower));
			double BBMid[] = ((double[]) indicators.get(ApplicationConstants.BBSma));
			double ATR[] = ((double[]) indicators.get(ApplicationConstants.ATR));
			double ADX[] = ((double[]) indicators.get(ApplicationConstants.ADX));
			double RSI[] = ((double[]) indicators.get(ApplicationConstants.RSI));
			LinkedList tradeList = new LinkedList();
			StockPrice spMinus1 = null;
			TradeBean tb = null;
			Date tradeStartDate = null, tradeEndDate = null;
			double bSuccesInday = 0, sSuccessInDay = 0;
			StockPrice buyBean = null, sellBean = null;
			// try {
			//
			// PivotPoints.refine( getPivots(ls, 0), 10);
			// } catch (ParseException ex) {
			// Logger.getLogger(PivotStrategy.class.getName()).log(Level.SEVERE,
			// null, ex);
			// }
			for (int i = 3; i < ls.size(); i++) {

				StockPrice sp1 = (StockPrice) ls.get(i);
				spMinus1 = (StockPrice) ls.get(i - 1);
				if (stopdate.getTime() < sp1.getDateTime().getTime()) {
					String ss = "stop";
				}
				PivotPointBean[] cpivots = refinePivots(generatePivot(sp1, refineTaken, ((StockPrice) ls.get(i - 1))),
						refineTaken);
				if (!gapOpenDay || gapTaken == 0) {
					highBean = cpivots[1];
					lowBean = cpivots[0];
				} else {
					long minTime = Math.min(cpivots[0].pvtTime.getTime(), cpivots[1].pvtTime.getTime());
					Date mind = new Date(minTime);
					if (formatter1.format(highBean.pvtTime).equals(formatter1.format(mind))) {
						highBean = cpivots[0];
						lowBean = cpivots[1];
						gapOpenDay = false;
					} else {
						cpivots[1] = highBean;
						cpivots[0] = lowBean;
					}
				}

				if (i > 1 && PivotBackTester.isGapOpen(sp1, ls.get(i - 1), ATR[i])) {
					gapOpenDay = true;
					highBean = new PivotPointBean();
					lowBean = new PivotPointBean();
					highBean.pivotAtCandle = sp1;
					highBean.pvttype = "high";
					highBean.pvtvalue = sp1.getHigh();
					highBean.pvtTime = sp1.getDateTime();
					lowBean.pvttype = "low";
					lowBean.pivotAtCandle = sp1;
					lowBean.pvtvalue = sp1.getLow();
					lowBean.pvtTime = sp1.getDateTime();
					continue;
				}

				if (Test.priceWithintime(sp1.getDateTime()) || 1 == 1) {
					if (isBuyTriggered(sp1, cpivots, buy, s, i, spMinus1) && buyFlag == 0) {
						initsellTrig=false;
						if (sell) {
							if (debug) {
								System.out.println("short cancelled for buy ");
								System.out.println(
										"----------------------------------------------------------------------------------------------------------------------------");
							}
							sell = false;
							points = sellPrice - sp1.getClose();
							sellCanceledPoints += points;
							sellCanceledTrades++;
							profit += no_of_shares * points;
							highest = Math.min(sp1.getLow(), highest);
							data.append("," + sp1.getDateTime() + "," + sp1.getClose() + "," + points + ","
									+ no_of_shares + ",Canceled," + highest);
						}
						highest = sp1.getClose();
						;
						buy = true;
						sellFlag = 1;
						no_of_shares = (int) (rupee / sp1.getClose());
						buyPrice = sp1.getClose();
						callPrice = sp1;
						tradeStartDate = sp1.getDateTime();
						// target = Math.min(sp1.getClose() + ((sp1.getClose() -
						// lowBean.pvtvalue) * RR),sp1.getClose()+20);
						target = sp1.getClose() + ((sp1.getClose() - lowBean.pvtvalue) * RR) / 2;
						// stoploss = Math.max(sp1.getClose() - ((sp1.getClose()
						// - lowBean.pvtvalue)), sp1.getClose() - 20);
						stoploss = lowBean.pvtvalue;
						threeInsideBar = 0;
						// showTrayIcon("BUY","at price:: "+buyPrice+" :: Target
						// :: "+target+" :: "+stoploss+"\n Current Time :: " +
						// ls.get(ls.size() - 1));
						signalData = "BUY :: at time :: " + sp1.getDateTime() + " price:: " + buyPrice
								+ " :: Target :: " + target + " :: " + stoploss + "\n Current Time :: "
								+ ls.get(ls.size() - 1);
						// stoploss = sp1.getLow();
						tb = new TradeBean();
						tb.setCallPrice(buyPrice);
						tb.setCallDate(sp1.getDateTime());
						tb.setHighbean(highBean);
						tb.setLowbean(lowBean);
						tb.setTarget(target);
						tb.setStoploss(stoploss);
						tb.setAtr(ATR[i]);
						tb.setBbMid(new double[] { BBMid[i - 2], BBMid[i - 2], BBMid[i - 1], BBMid[i] });
						tb.setRsi(new double[] { RSI[i - 2], RSI[i - 2], RSI[i - 1], RSI[i] });
						tb.setAdx(new double[] { ADX[i - 2], ADX[i - 2], ADX[i - 1], ADX[i] });

						data.append("\n" + symbol + ",BUY," + sp1.getDateTime() + "," + buyPrice + "," + target + ","
								+ stoploss + "," + lowBean.pvtvalue + "," + highBean.pvtvalue);
						if (i > 2) {
							data.append("," + BBMid[i - 2] + " :: " + BBMid[i - 2] + " :: " + BBMid[i - 1] + " :: "
									+ BBMid[i]);
							data.append("," + ATR[i] + "," + ADX[i - 2] + " :: " + ADX[i - 2] + " :: " + ADX[i - 1]
									+ " :: " + ADX[i]);
							data.append("," + RSI[i - 2] + " :: " + RSI[i - 2] + " :: " + RSI[i - 1] + " :: " + RSI[i]);
						}
						if (debug) {
							System.out.println(
									"----------------------------------------------------------------------------------------------------------------------------");
							System.out
									.println("Buy Started at " + sp1 + "  Target  " + target + " stoploss " + stoploss);
							System.out.println(
									" Low " + lowBean.pvttype + "  " + lowBean.pvtvalue + "   " + lowBean.pvtTime);
							System.out.println(
									" High " + highBean.pvttype + "  " + highBean.pvtvalue + "   " + highBean.pvtTime);
							if (i > 3) {
								System.out.println(" BB ::  " + "  " + BBMid[i - 2] + "  " + BBMid[i - 2] + "  "
										+ BBMid[i - 1] + "  " + BBMid[i]);
								System.out.println(" ATR " + ATR[i]);
							}

						}

					} else if (isSellTriggered(sp1, cpivots, i, s, sell, spMinus1) && sellFlag == 0 && sellTaken == 1) {
						if (buy) {
							if (debug) {
								System.out.println("buy cancelled for short ");
								System.out.println(
										"----------------------------------------------------------------------------------------------------------------------------");
							}
							buy = false;
							points = sp1.getClose() - buyPrice;
							buyCanceledPoints += points;
							buyCanceledTrades++;
							profit += no_of_shares * points;
							highest = Math.max(sp1.getHigh(), highest);
							data.append("," + sp1.getDateTime() + "," + sp1.getClose() + "," + points + ","
									+ no_of_shares + ",Canceled," + highest);
						}
						threeInsideBar = 0;
						highest = sp1.getClose();
						;
						sell = true;
						buyFlag = 1;
						callPrice = sp1;
						no_of_shares = (int) (rupee / sp1.getClose());
						sellPrice = sp1.getClose();
						tradeStartDate = sp1.getDateTime();
						buyIndex = i;
						// target = Math.min(sp1.getClose() -
						// ((highBean.pvtvalue - sp1.getClose()) *
						// RR),sp1.getClose()-20);
						target = sp1.getClose() - ((highBean.pvtvalue - sp1.getClose()) * RR) / 2;
						// stoploss = Math.min(sp1.getClose() + 20,
						// sp1.getClose() + ((highBean.pvtvalue -
						// sp1.getClose())));
						// stoploss = sp1.getClose()- (target-sp1.getClose())/2;
						stoploss = highBean.pvtvalue;
						tb = new TradeBean();
						tb.setCallPrice(sellPrice);
						tb.setCallDate(sp1.getDateTime());
						tb.setHighbean(highBean);
						tb.setLowbean(lowBean);
						tb.setTarget(target);
						tb.setStoploss(stoploss);
						tb.setAtr(ATR[i]);
						tb.setBbMid(new double[] { BBMid[i - 2], BBMid[i - 2], BBMid[i - 1], BBMid[i] });
						tb.setRsi(new double[] { RSI[i - 2], RSI[i - 2], RSI[i - 1], RSI[i] });
						tb.setAdx(new double[] { ADX[i - 2], ADX[i - 2], ADX[i - 1], ADX[i] });

						// stoploss = sp1.getHigh();
						// showTrayIcon("SELL","at price:: "+sellPrice+" ::
						// Target :: "+target+" :: "+stoploss+"\n Current Time
						// :: " + ls.get(ls.size() - 1));
						signalData = "SELL :: at price:: " + sellPrice + " :: Target :: " + target + " :: " + stoploss
								+ "\n Current Time :: " + ls.get(ls.size() - 1);
						data.append("\n" + symbol + ",SELL," + sp1.getDateTime() + "," + sellPrice + "," + target + ","
								+ stoploss + "," + lowBean.pvtvalue + "," + highBean.pvtvalue);
						if (i > 2) {
							data.append("," + BBMid[i - 2] + " :: " + BBMid[i - 2] + " :: " + BBMid[i - 1] + " :: "
									+ BBMid[i]);
							data.append("," + ATR[i] + "," + ADX[i - 2] + " :: " + ADX[i - 2] + " :: " + ADX[i - 1]
									+ " :: " + ADX[i]);
							data.append("," + RSI[i - 2] + " :: " + RSI[i - 2] + " :: " + RSI[i - 1] + " :: " + RSI[i]);
						}
						if (debug) {
							System.out.println(
									"----------------------------------------------------------------------------------------------------------------------------");
							System.out
									.println("Short Started  " + sp1 + "  Target  " + target + " stoploss " + stoploss);
							System.out.println(
									" Low " + lowBean.pvttype + "  " + lowBean.pvtvalue + "   " + lowBean.pvtTime);
							System.out.println(
									" High " + highBean.pvttype + "  " + highBean.pvtvalue + "   " + highBean.pvtTime);
							if (i > 3) {
								System.out.println(" BB ::  " + "  " + BBMid[i - 2] + "  " + BBMid[i - 2] + "  "
										+ BBMid[i - 1] + "  " + BBMid[i]);
								System.out.println(" ATR " + ATR[i]);
							}
						}
					}
				}
				if (buy && buyFlag == 0) {
					highest = Math.max(sp1.getHigh(), highest);
					if (isbuyTargetReached(sp1, target, buyPrice, stoploss, i, s)) {
						buy = false;
						sellFlag = 0;
						points = sp1.getClose() - buyPrice;
						successContinue = false;
						buySuccessPoints += points;
						buySuccessTrades++;
						profit += no_of_shares * points;
						data.append("," + sp1.getDateTime() + "," + sp1.getClose() + "," + points + "," + no_of_shares
								+ ",Reached," + highest);
						// showTrayIcon("BUY Reached","at price::
						// "+sp1.getClose()+" :: Time :: "+sp1.getDateTime()+"
						// :: "+"\n Current Time :: " + ls.get(ls.size() - 1));
						targetData = "BUY Reached :: at price:: " + sp1.getClose() + " :: Time :: " + sp1.getDateTime()
								+ " :: " + "\n Current Time :: " + ls.get(ls.size() - 1);
						tradeEndDate = sp1.getDateTime();
						int diffInDays = (int) ((tradeStartDate.getTime() - tradeEndDate.getTime())
								/ (1000 * 60 * 60 * 24));
						bSuccesInday += diffInDays;
						if (debug) {
							System.out.println("buy target reached at " + sp1 + "\nTrades  " + buySuccessTrades
									+ " points  " + points + "" + "   " + buySuccessPoints + "   " + bSuccesInday);
							System.out.println(
									"----------------------------------------------------------------------------------------------------------------------------");
						}
					} else if (isBuyFailed(sp1, stoploss, callPrice, ls, i)) {
						buyFailedPoints += sp1.getClose() - buyPrice;
						buyFailedTrades++;
						points = sp1.getClose() - buyPrice;
						profit += no_of_shares * points;
						buy = false;
						sellFlag = 0;
						data.append("," + sp1.getDateTime() + "," + sp1.getClose() + "," + points + "," + no_of_shares
								+ ",Failed," + highest);
						// showTrayIcon("BUY Failed","at price::
						// "+sp1.getClose()+" :: Time :: "+sp1.getDateTime()+"
						// :: "+"\n Current Time :: " + ls.get(ls.size() - 1));
						targetData = "BUY Failed :: at price:: " + sp1.getClose() + " :: Time :: " + sp1.getDateTime()
								+ " :: " + "\n Current Time :: " + ls.get(ls.size() - 1);
						if (debug) {
							System.out.println("buy failed at  " + sp1 + "\ntrades  " + buyFailedTrades + " points  "
									+ points + "   " + buyFailedPoints);
							System.out.println(
									"----------------------------------------------------------------------------------------------------------------------------");
						}
					}

				}
				if (sell && sellFlag == 0 && sellTaken == 1) {
					highest = Math.min(sp1.getLow(), highest);
					if (isSellTargetReached(sp1, target, sellPrice, stoploss, i, s, buyIndex)) {
						successContinue = false;
						sell = false;
						buyFlag = 0;
						points = sellPrice - sp1.getClose();
						sellSuccessPoints += points;
						profit += no_of_shares * points;
						sellSuccessTrades++;
						data.append("," + sp1.getDateTime() + "," + sp1.getClose() + "," + points + "," + no_of_shares
								+ ",Reached," + highest);
						// showTrayIcon("SELL Reached","at price::
						// "+sp1.getClose()+" :: Time :: "+sp1.getDateTime()+"
						// :: "+"\n Current Time :: " + ls.get(ls.size() - 1));
						targetData = "SELL Reached :: at price:: " + sp1.getClose() + " :: Time :: " + sp1.getDateTime()
								+ " :: " + "\n Current Time :: " + ls.get(ls.size() - 1);
						tradeEndDate = sp1.getDateTime();
						int diffInDays = (int) ((tradeStartDate.getTime() - tradeEndDate.getTime())
								/ (1000 * 60 * 60 * 24));
						sSuccessInDay += diffInDays;
						if (debug) {
							System.out.println("short target reached at " + sp1 + "\nTrades  " + sellSuccessTrades
									+ " points  " + points + "   " + sellSuccessPoints + "   " + sSuccessInDay);
							System.out.println(
									"----------------------------------------------------------------------------------------------------------------------------");
						}
					} else if (isSellFailed(sp1, stoploss, callPrice, ls, i)) {
						points = sellPrice - sp1.getClose();
						sellFailedPoints += points;
						sellFailedTrades++;
						profit += no_of_shares * points;
						data.append("," + sp1.getDateTime() + "," + sp1.getClose() + "," + points + "," + no_of_shares
								+ ",Failed," + highest);
						// showTrayIcon("SELL Failed","at price::
						// "+sp1.getClose()+" :: Time :: "+sp1.getDateTime()+"
						// :: "+"\n Current Time :: " + ls.get(ls.size() - 1));
						targetData = "SELL Failed :: at price:: " + sp1.getClose() + " :: Time :: " + sp1.getDateTime()
								+ " :: " + "\n Current Time :: " + ls.get(ls.size() - 1);
						if (debug) {
							System.out.println("short failed at " + sp1 + "\ntrades " + sellFailedTrades + "  points  "
									+ points + "   " + sellFailedPoints);
							System.out.println(
									"----------------------------------------------------------------------------------------------------------------------------");
						}
						sell = false;
						buyFlag = 0;
					}

				}

			}
			totalBuyPoints = buySuccessPoints + buyFailedPoints + buyCanceledPoints;
			totalSellPoints = sellSuccessPoints + sellFailedPoints + sellCanceledPoints;
			totalBuyTrades = buySuccessTrades + buyFailedTrades + buyCanceledTrades;
			totalSellTrades = sellSuccessTrades + sellFailedTrades + sellCanceledTrades;
			totalPoints = totalBuyPoints + totalSellPoints;
			totalTrades = totalBuyTrades + totalSellTrades;

			// System.out.println("Total Profit :: " + profit + " shares bought
			// " + no_of_shares);
			PivotPointBean pb1 = pivotlist.getLast();
			PivotPointBean pb2 = pivotlist.get(pivotlist.size() - 2);
			if (callPrice.getDateTime().after(stopdate)) {
				System.out.println(" last buy call " + symbol + "  " + callPrice);
				System.out.println(" last " + symbol + "  " + ls.getLast());
				System.out.println(
						" last " + symbol + "  " + pb1.pvtvalue + "  " + pb1.pvttype + "  " + pb1.pivotAtCandle);
				System.out.println(
						" last " + symbol + "  " + pb1.pvtvalue + "  " + pb2.pvttype + "  " + pb2.pivotAtCandle);
			}
			System.out.println(symbol + "  " + "Total Cost :: " + (-totalTrades * 2) + "  totalTrades :: " + totalTrades
					+ " totalPoints " + totalPoints);
			System.out.println("totalBuyTrades " + totalBuyTrades + " totalBuyPoints  " + totalBuyPoints + "  buy "
					+ buySuccessTrades + " points " + buySuccessPoints + " failed " + buyFailedTrades + "  "
					+ buyFailedPoints);
			System.out.println("totalSellTrades " + totalSellTrades + " totalSellPoints " + totalSellPoints + " sell "
					+ sellSuccessTrades + " points " + sellSuccessPoints + " failed " + sellFailedTrades + "  "
					+ sellFailedPoints);
			System.out.println("sell " + sellCanceledTrades + " points " + sellCanceledPoints + " buy  "
					+ buyCanceledTrades + "  " + buyCanceledPoints);

			if (totalPoints > 0) {
				System.out.println(" Successful stock count " + (suc++));
			} else {
				System.out.println(" fail stock count " + (fai++));
			}
			System.out.println(
					"----------------------------------------------------------------------------------------------------------------------------");
			// System.out.println(data);
			break;
		}
		// System.out.pri
	}

	public static boolean isSellFailed(StockPrice sp1, double stoploss, StockPrice callPrice, LinkedList ls, int i) {
		boolean flag = false;
		initsellTrig=false;
		if (sp1.getDateTime().getTime() - callPrice.getDateTime().getTime() < 300000) {
			flag = immidiateFail(sp1, callPrice) && (immidiateFailTaken == 1);

			// if (flag) {
			// System.out.println("immidiate faile " + sp1);
			// }
		}
		if (sp1.getDateTime().getTime() - callPrice.getDateTime().getTime() == 900000) {
		}
		if (threeoppositfail((StockPrice) ls.get(i - 2), (StockPrice) ls.get(i - 1), (StockPrice) ls.get(i), "sell")
				&& ((StockPrice) ls.get(i - 2)).getClose() > callPrice.getClose() && threeOppositeFailTaken == 1) {
			if (debug) {
				System.out.println("sell failed due to three opposite candles  " + sp1);
			}
			flag = true;
		}

		if (sp1.getClose() >= stoploss) {
			flag = true;
		}

		if (sp1.getClose() >= callPrice.getHigh()) {
			if (debug) {
				System.out.println("sell failed due to price reached callprice's high  " + sp1);
			}
			flag = true;
		}
		return flag;
	}

	public static boolean isBuyFailed(StockPrice sp1, double stoploss, StockPrice callPrice, LinkedList ls, int i) {
		boolean flag = false;
		if (sp1.getClose() <= stoploss) {
			flag = true;
		}
		if (sp1.getDateTime().getTime() - callPrice.getDateTime().getTime() == 300000) {
			flag = immidiateFail(sp1, callPrice) && (immidiateFailTaken == 1);
			// if (flag) {
			// System.out.println("immidiate faile " + sp1 + " " + callPrice);
			// }
		}
		if (sp1.getDateTime().getTime() - callPrice.getDateTime().getTime() == 900000) {

		}
		if (threeoppositfail((StockPrice) ls.get(i - 2), (StockPrice) ls.get(i - 1), (StockPrice) ls.get(i), "buy")
				&& ((StockPrice) ls.get(i - 2)).getClose() < callPrice.getClose() && threeOppositeFailTaken == 1) {
			flag = true;
			if (debug) {
				System.out.println("buy failed due to three opposite candles  " + sp1);
			}
			// System.out.println(callPrice+" buy iii "+sp1);
		}
		if (sp1.getClose() <= callPrice.getLow()) {
			if (debug) {
				System.out.println("buy failed due to price reached to callprice's low  " + sp1);
			}
			flag = true;
		}
		return flag;
	}

	public static void generateSignal(StockPrice sp1) {
		PivotPointBean[] cPivots = generatePivot(sp1, -0.5, sp1);

	}

	static double temppvtlow = 999999999, pvtlow = 0, pvthigh = 0, temppvthigh = 0, temppvtLow;

	public static PivotPointBean[] generatePivot(StockPrice sp1, double refinePer, StockPrice spOld) {
		double refineVal = 0;
		if (stopdate.getTime() < sp1.getDateTime().getTime()) {
			String ss = "stop";
		}
		int low = 1, high = 0;
		if (pivotlist.size() == 0) {
			temppvtlow = sp1.getLow();
			temppvthigh = sp1.getHigh();
			PivotPointBean pb = new PivotPointBean();
			pb.pivotAtCandle = sp1;
			pb.pvtvalue = temppvtlow;
			pb.pvttype = "low";
			pb.pvtTime = pb.pivotAtCandle.getDateTime();
			pivotlist.add(pb);
			pb = new PivotPointBean();
			pb.pivotAtCandle = sp1;
			pb.pvtvalue = temppvthigh;
			pb.pvttype = "high";
			pb.pvtTime = pb.pivotAtCandle.getDateTime();
			pivotlist.add(pb);
		}
		if (pivotlist.getLast().pvttype.equals("high")) {
			low = 1;
			high = 0;
			refineVal = templowBean.pvtvalue * refinePer / 100;
		} else {
			low = 0;
			high = 1;
			refineVal = -temphighBean.pvtvalue * refinePer / 100;
		}

		if (!PivotPoints.inSideBar((StockPrice) spOld, sp1, refineVal) || insiderBarTaken == 0) {

			if (pivotlist.getLast().pvttype.equals("high")) {
				low = 1;
				high = 0;
				refineVal = templowBean.pvtvalue * refinePer / 100;
			} else {
				low = 0;
				high = 1;
				refineVal = -temphighBean.pvtvalue * refinePer / 100;
			}

			if ((sp1.getLow() - temppvtlow) <= refineVal && low == 1) {
				temppvtlow = sp1.getLow();
				tpvtLow = sp1;
			} else if (high == 0) {
				pvtlow = temppvtlow;
				// System.out.println(pvtlow + " pvtlow at " + ls.get(i - 1));
				PivotPointBean pb = new PivotPointBean();
				pb.pivotAtCandle = tpvtLow;
				pb.pvtvalue = temppvtlow;
				pb.pvttype = "low";
				pb.pvtTime = pb.pivotAtCandle.getDateTime();
				pivotlist.add(pb);
				// System.out.println("pvt :: " + pb.pvttype + " " + pb.pvtvalue
				// + " " + pb.pvtTime);
				low = 0;
				high = 1;
				temppvthigh = 0;

			}
			if ((sp1.getHigh() - temppvthigh) >= refineVal && high == 1) {
				temppvthigh = sp1.getHigh();
				tpvtHigh = sp1;
			} else if (low == 0) {
				pvthigh = temppvthigh;
				// System.out.println(pvthigh + " pvthigh at " + ls.get(i - 1));
				PivotPointBean pb = new PivotPointBean();
				pb.pivotAtCandle = tpvtHigh;
				pb.pvtvalue = temppvthigh;
				pb.pvttype = "high";
				pb.pvtTime = pb.pivotAtCandle.getDateTime();
				pivotlist.add(pb);
				// System.out.println("pvt :: " + pb.pvttype + " " + pb.pvtvalue
				// + " " + pb.pvtTime);
				low = 1;
				high = 0;
				temppvtlow = 999999999;
			}

		}
		PivotPointBean[] pb = new PivotPointBean[2];
		if (((PivotPointBean) pivotlist.getLast()).pvttype.equals("high")) {
			pb[0] = (PivotPointBean) pivotlist.get(pivotlist.size() - 2);
			pb[1] = (PivotPointBean) pivotlist.get(pivotlist.size() - 1);
		} else {
			pb[0] = (PivotPointBean) pivotlist.get(pivotlist.size() - 1);
			pb[1] = (PivotPointBean) pivotlist.get(pivotlist.size() - 2);

		}
		PivotPointBean ss = (PivotPointBean) pivotlist.getLast();

		return pb;
	}

	static PivotPointBean temphighBean = new PivotPointBean(), templowBean = new PivotPointBean();
	static StockPrice tpvtLow = new StockPrice(), tpvtHigh = new StockPrice();

	public static PivotPointBean[] refinePivots(PivotPointBean[] pb, double refinePer) {
		double tempPrice;
		double RefineVal = 0;
		if (pb[0].pvtTime != null && stopdate.getTime() < pb[0].pvtTime.getTime()) {
			String ss = "stop";
		}

		PivotPointBean pbs[] = new PivotPointBean[2];
		if (pb[0].pvttype == "low") {
			RefineVal = temphighBean.pvtvalue * refinePer / 100;
			if (Math.abs(temphighBean.pvtvalue - pb[0].pvtvalue) > RefineVal) {
				templowBean = pb[0];
			} else {
				if (Math.abs(templowBean.pvtvalue - pb[0].pvtvalue) > RefineVal) {
					templowBean = pb[0];
				} else {
					tempPrice = Math.min(templowBean.pvtvalue, pb[0].pvtvalue);
					if (tempPrice != templowBean.pvtvalue) {
						templowBean = pb[0];
					}
				}

			}
			// System.out.println("Low pivot " + templowBean.pvtvalue + " " +
			// templowBean.pvtTime);

		}
		pbs[0] = templowBean;
		if (pb[1].pvttype == "high") {
			RefineVal = templowBean.pvtvalue * refinePer / 100;
			if (Math.abs(pb[1].pvtvalue - templowBean.pvtvalue) > RefineVal) {
				temphighBean = pb[1];
			} else {
				if (Math.abs(temphighBean.pvtvalue - pb[1].pvtvalue) > RefineVal) {
					temphighBean = pb[1];
				} else {
					tempPrice = Math.max(temphighBean.pvtvalue, pb[1].pvtvalue);
					if (tempPrice != temphighBean.pvtvalue) {
						temphighBean = pb[1];
					}
				}
			}
			// System.out.println("High pivot " + temphighBean.pvtvalue + " " +
			// temphighBean.pvtTime);
		}
		pbs[1] = temphighBean;
		refinedPivots2.add(pbs);
		return pbs;
	}

	static double tTrigerPivot = 0;
	static double tsTrigerPivot = 0;

	private static boolean isBuyTriggered(StockPrice sp1, PivotPointBean[] cPivots, boolean active, Stock s, int i,
			StockPrice spMinus1) {
		PivotPointBean highBean = cPivots[1], lowBean = cPivots[0];
		HashMap indicators = s.getIndicators();
		double atr[] = ((double[]) indicators.get(ApplicationConstants.ATR));
		boolean isTriggered = false;
		if (sp1.getClose() - highBean.pvtvalue > 0 && triggerPivot != highBean.pvtvalue) {
			if (checkCandleSize(sp1, atr[i]) || candleSizeTaken == 0) {
				isTriggered = true;
				tTrigerPivot = triggerPivot;
				triggerPivot = highBean.pvtvalue;
			}
		}

		/*------------- Watch for two consequative red candles*/
		// if (spMinus1.getClose() < spMinus1.getOpen() && spMinus1.getOpen() >
		// sp1.getOpen() && isTriggered) { //red candle
		// isTriggered = false;
		// triggerPivot = tTrigerPivot;
		// if (debug) {
		// System.out.println("buy failed due to no two consequative green
		// candles.." + sp1 + " " + spMinus1 + " " + highBean.pvtvalue);
		// }
		// }
		if (sp1.getClose() < sp1.getOpen() && isTriggered) { // red candle
			isTriggered = false;
			triggerPivot = tTrigerPivot;
			if (debug) {
				System.out.println("buy failed due to no tirgger green candle.." + sp1 + "  " + highBean.pvtvalue);
			}
		}

		/*-----------------Watch for Difference from pivotHigh and buy is less than ATR*/

		// if (sp1.getClose() - highBean.pvtvalue > atr[i] * 1 && isTriggered) {
		// isTriggered = false;
		// triggerPivot = tTrigerPivot;
		// System.out.println("buy failed due to diff less than ATR *2.5.." +
		// sp1 + " " + highBean.pvtvalue);
		// }
		double BBMid[] = ((double[]) indicators.get(ApplicationConstants.BBSma));
		// if (i > 2 && BBMid[i - 2] < BBMid[i - 1] && BBMid[i - 1] < BBMid[i]
		// && Math.signum(BBMid[i - 2]) == 1) {
		// isTriggered = true;
		// } else {
		// isTriggered = false;
		// }
		// if (BBMid[i] > 200) {
		// isTriggered = false;
		// }
		if (active) {
			isTriggered = false;
		}

		// if (sp1.getDateTime().getTime() - highBean.pvtTime.getTime() > (7 *
		// 60 * 60 * 1000)) {
		// isTriggered = false;
		// }
		return isTriggered;
	}

	private static boolean isbuyTargetReached(StockPrice sp1, double target, double buyPrice, double stoploss, int i,
			Stock s) {
		boolean targetReached = false;
		LinkedList ls = s.getStockList();
		if (successContinue) {
			if (i > 1) {
				StockPrice spOld = (StockPrice) ls.get(i - 1);
				if (!PivotPoints.inSideBarTarget(spOld, sp1)) {
					threeInsideBar = 0;
					if (sp1.getClose() >= spOld.getClose()) {
					} else {
						targetReached = true;
					}
				} else {
					threeInsideBar++;
				}
			} else {
				targetReached = true;
			}
			if (threeInsideBar == 3) {
				threeInsideBar = 0;
				targetReached = true;
			}
		}
		if (sp1.getHigh() - target > -2 && !successContinue) {
			stoploss = buyPrice;
			// System.out.println("buy success continueing..");
			successContinue = true;
			// targetReached = true;
		}

		if (formatter.format(sp1.getDateTime()).contains("15:30:00")) {
			targetReached = true;
		}
		return targetReached;
	}

	private static boolean isSellTriggered(StockPrice sp1, PivotPointBean[] cpivots, int i, Stock s, boolean active,
			StockPrice spMinus1) {
		PivotPointBean highBean = cpivots[1], lowBean = cpivots[0];
		boolean isTriggered = false;
		HashMap indicators = s.getIndicators();

		double atr[] = ((double[]) indicators.get(ApplicationConstants.ATR));
		if (stopdate.getTime() < sp1.getDateTime().getTime()) {
			String ss = "stop";
			
		}

		if (!initsellTrig && lowBean.pvtvalue - sp1.getClose() > 0 && triggerPivot != lowBean.pvtvalue) {
			if (checkCandleSize(sp1, atr[i]) || candleSizeTaken == 0) {
				initsellTrig = true;
				tsTrigerPivot = triggerPivot;
				triggerPL=lowBean;
				triggerPH=highBean;
				triggerVal=sp1;
				System.out.println("trigger piveot   "+triggerPL.pvtvalue+"  "+triggerPL.pivotAtCandle.getDateTime());
				System.out.println("trigger pivot high   "+triggerPH.pvtvalue+"  "+triggerPH.pivotAtCandle.getDateTime());
				
				triggerPivot = lowBean.pvtvalue;
			}
		}
		
		if(initsellTrig && highBean.pivotAtCandle.getDateTime().after(triggerVal.getDateTime())){
	
			isTriggered = true;
		}
		// if (spMinus1.getClose() > spMinus1.getOpen() && spMinus1.getClose() >
		// sp1.getClose() && isTriggered) { //green candle
		// isTriggered = false;
		// triggerPivot = tsTrigerPivot;
		// if (debug) {
		// System.out.println("sell failed due to no two consequative red
		// candles.." + sp1 + " " + lowBean.pvtvalue);
		// }
		// }
//		if (sp1.getClose() > sp1.getOpen() && isTriggered) { // green candle
//			isTriggered = false;
//			triggerPivot = tsTrigerPivot;
//			if (debug) {
//				System.out.println("sell failed due to no red trigger candles.." + sp1 + "  " + lowBean.pvtvalue);
//			}
//		}

		/*-----------------Watch for Difference from pivotHigh and buy is less than ATR*/
//		if (lowBean.pvtvalue - sp1.getClose() > atr[i] * 1 && isTriggered) {
//			isTriggered = false;
//			triggerPivot = tsTrigerPivot;
//			if (debug) {
//				System.out.println("sell failed due to no atr is acheived.." + sp1 + "  " + lowBean.pvtvalue);
//			}
//		}
	

		double BBMid[] = ((double[]) indicators.get(ApplicationConstants.BBSma));
		// if (i > 2 && BBMid[i - 2] > BBMid[i - 1] && BBMid[i - 1] > BBMid[i]
		// && Math.signum(BBMid[i - 2]) == -1) {
		// isTriggered = true;
		// } else {
		// isTriggered = false;
		// }

		if (active) {
			isTriggered = false;
		}
		return isTriggered;
	}

	private static boolean isSellTargetReached(StockPrice sp1, double target, double buyPrice, double stoploss, int i,
			Stock s, int buyIndex) {
		boolean targetReached = false;
		initsellTrig=false;
		LinkedList ls = s.getStockList();
		if (successContinue) {
			if (i > 1) {
				StockPrice spOld = (StockPrice) ls.get(i - 1);
				if (!PivotPoints.inSideBarTarget(spOld, sp1)) {
					threeInsideBar = 0;
					if (sp1.getClose() <= spOld.getClose()) {
					} else if (i - buyIndex > 3) {
						targetReached = true;
					}
				} else {
					threeInsideBar++;
				}
			} else {
				targetReached = true;
			}
			if (threeInsideBar == 3) {
				threeInsideBar = 0;
				targetReached = true;
			}
		}
		if (sp1.getLow() - target < 2 && !successContinue) {
			stoploss = buyPrice;
			// System.out.println("Short success continueing..");
			successContinue = true;
			// targetReached = true;
		}

		if (formatter.format(sp1.getDateTime()).contains("15:30:00")) {
			targetReached = true;
		}
		return targetReached;
	}

	public static boolean immidiateFail(StockPrice sp, StockPrice callPrice) {
		boolean flag = false;
		if (callPrice.getOpen() <= sp.getClose() && callPrice.getClose() < sp.getClose()
				&& callPrice.getOpen() > sp.getOpen()) {
			flag = true;
		} else if (callPrice.getOpen() >= sp.getClose() && callPrice.getClose() > sp.getClose()
				&& callPrice.getOpen() < sp.getOpen()) {
			flag = true;
		}
		return flag;
	}

	public static boolean threeoppositfail(StockPrice sp3, StockPrice sp2, StockPrice sp1, String action) {
		boolean flag = false;

		if (action.equals("buy")) {
			if (sp3.getClose() > sp2.getClose() && sp2.getClose() > sp1.getClose()) {
				flag = true;
			}
		} else {
			if (sp3.getClose() < sp2.getClose() && sp2.getClose() < sp1.getClose()) {
				flag = true;
			}
		}

		return flag;
	}

	public static boolean checkCandleSize(StockPrice sp1, double atr) {

		boolean flag = false;
		if (Math.abs(sp1.getOpen() - sp1.getClose()) > (atr - atr * 0.2)) {
			// System.out.println(sp1+" "+callPrice);
			flag = true;
		}
		return flag;
	}

	public static boolean checkCandleWithPivot(PivotPointBean highBean, StockPrice sp1) {
		StockPrice callPrice = highBean.pivotAtCandle;
		boolean flag = false;
		if (Math.abs(sp1.getOpen() - sp1.getClose()) - Math.abs(callPrice.getOpen() - callPrice.getClose()) > 5) {
			// System.out.println(sp1+" "+callPrice);
			flag = true;
		}
		return flag;
	}
}
