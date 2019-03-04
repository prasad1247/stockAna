package start;

import bean.Stock;
import bean.StockDataBean;
import bean.StockPrice;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import StockBean.DataMap;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author hp
 */
public class StockHelper {

    static String ip = "127.0.0.1";

    public static Connection getConnetion() {
        Connection con = null;
        try {

            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://" + ip + ":3306/stock_data?useServerPrepStmts=false&rewriteBatchedStatements=true", "root", "root");
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return con;
    }

    private static void closeConnection(Connection con, ResultSet rs, Statement st) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(StockHelper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException ex) {
                Logger.getLogger(StockHelper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (st != null) {
            try {
                st.close();
            } catch (SQLException ex) {
                Logger.getLogger(StockHelper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public static LinkedList getStockName() {
        StringBuilder sql = new StringBuilder();
        Connection con = null;
        //     System.out.println("fName " + client + "   " + prov + "  ");
        PreparedStatement stmt = null;
        ResultSet rs = null;
        LinkedList stockNameList = new LinkedList();
        try {
            con = getConnetion();
            sql.append(" select distinct stockid from stockdata");
            stmt = con.prepareStatement(sql.toString());
            rs = stmt.executeQuery();
            while (rs.next()) {
                stockNameList.add(rs.getString("stockid"));
            }
        } catch (Exception e) {
        } finally {
            closeConnection(con, rs, stmt);
        }
        return stockNameList;
    }

    public static void insertData(LinkedList stockList) {
        StringBuilder sql = null;
        Connection con = null;
        long st = System.currentTimeMillis();
        //     System.out.println("fName " + client + "   " + prov + "  ");
        PreparedStatement stmt = null;
        ResultSet rs = null;
        java.sql.Date cDate = null;
        try {
            con = getConnetion();

            sql = new StringBuilder();
            sql.append("select MAX(date) from stockdata where stockid=?");
            stmt = con.prepareStatement(sql.toString());
            stmt.setString(1, ((StockDataBean) stockList.get(0)).getName());
            rs = stmt.executeQuery();
            if (rs.next()) {
                cDate = rs.getDate(1);
            }

            sql = new StringBuilder();
            sql.append("insert into stockdata(stockid,date,open,high,low,close,volume) values(?,?,?,?,?,?,?)");
            stmt = con.prepareStatement(sql.toString());
            java.sql.Date d = null;
            for (Object o : stockList) {

                StockDataBean sdb = (StockDataBean) o;
                if (cDate.getTime() < sdb.getDateTime().getTime()) {
                    stmt.setString(1, sdb.getName());
                    d = new java.sql.Date(sdb.getDateTime().getTime());
                    stmt.setDate(2, d);
                    stmt.setDouble(3, sdb.getOpen());
                    stmt.setDouble(4, sdb.getHigh());
                    stmt.setDouble(5, sdb.getLow());
                    stmt.setDouble(6, sdb.getClose());
                    stmt.setDouble(7, sdb.getVolume());
                    //stmt.executeUpdate();
                    stmt.addBatch();
                }
            }
            long s2 = System.currentTimeMillis();
            int[] count = stmt.executeBatch();
            long s3 = System.currentTimeMillis();
            System.out.println(" time 1:: " + (s2 - st) + " time 2:: " + (s3 - s2) + " eime3:: " + (s3 - st));
//             for(int i=0;i<count.length;i++){
//                System.out.println("Query "+i+" has effected "+count[i]+" times");
//            }
            //con.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(con, rs, stmt);
        }

    }
    
     public static void insertData_min(Stock s) {
        StringBuilder sql = null;
        Connection con = null;
        long st = System.currentTimeMillis();
        //     System.out.println("fName " + client + "   " + prov + "  ");
        PreparedStatement stmt = null;
        ResultSet rs = null;
        java.sql.Date cDate = null;
        try {
            con = getConnetion();
            LinkedList stockList=s.getStockList();
            sql = new StringBuilder();
            sql.append("select MAX(date) from stockdata_min where stockid=?");
            stmt = con.prepareStatement(sql.toString());
            stmt.setString(1, s.getName());
            rs = stmt.executeQuery();
            if (rs.next()) {
                cDate = rs.getDate(1);
            }
            

            sql = new StringBuilder();
            sql.append("insert into stockdata_min(stockid,date,open,high,low,close,volume) values(?,?,?,?,?,?,?)");
            stmt = con.prepareStatement(sql.toString());
            java.sql.Timestamp d = null;
            for (Object o : stockList) {

                StockPrice sdb = (StockPrice) o;
                if (cDate==null || cDate.getTime() < sdb.getDateTime().getTime()) {
                    stmt.setString(1, s.getName());
                    d = new java.sql.Timestamp(sdb.getDateTime().getTime());
//                    System.out.println("d "+d+"  "+sdb.getDateTime());
                    stmt.setTimestamp(2, d);
                    stmt.setDouble(3, sdb.getOpen());
                    stmt.setDouble(4, sdb.getHigh());
                    stmt.setDouble(5, sdb.getLow());
                    stmt.setDouble(6, sdb.getClose());
                    stmt.setDouble(7, sdb.getVolume());
                    //stmt.executeUpdate();
                    stmt.addBatch();
                }
            }
            long s2 = System.currentTimeMillis();
            int[] count = stmt.executeBatch();
            long s3 = System.currentTimeMillis();
            System.out.println(" time 1:: " + (s2 - st) + " time 2:: " + (s3 - s2) + " eime3:: " + (s3 - st));
//             for(int i=0;i<count.length;i++){
//                System.out.println("Query "+i+" has effected "+count[i]+" times");
//            }
            //con.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(con, rs, stmt);
        }

    }

    public static Stock getStockData(String sname) {
        StringBuilder sql = new StringBuilder();
        Connection con = null;
        //     System.out.println("fName " + client + "   " + prov + "  ");
        PreparedStatement stmt = null;
        ResultSet rs = null;
        LinkedList stocDataList = new LinkedList();
        Stock mainBean = new Stock();
        int serice=getDataCount(sname);
        Date[] date = new Date[serice];
        double[] high = new double[serice];
        double[] low = new double[serice];
        double[] open = new double[serice];
        double[] close = new double[serice];
        double[] volume = new double[serice];
        try {
            con = getConnetion();
            sql.append(" select open,high,low,close,date,volume from stockdata where stockid=? order by date asc");
            stmt = con.prepareStatement(sql.toString());
            stmt.setString(1, sname);
            rs = stmt.executeQuery();


            String[] dataArray = null;
            int i = 0;
            int interval = 1;

            try {

                while (rs.next()) {
                    StockPrice sp = new StockPrice();
                    sp.setDateTime(new Date(rs.getDate("date").getTime()));
                    sp.setOpen(rs.getDouble("open"));
                    sp.setHigh(rs.getDouble("high"));
                    sp.setLow(rs.getDouble("low"));
                    sp.setClose(rs.getDouble("close"));
                    sp.setVolume((int)rs.getDouble("volume"));
                    stocDataList.add(sp);

                }

                for (Object o : stocDataList) {
                    StockPrice o1 = (StockPrice) o;
                    date[i] = o1.getDateTime();
                    open[i] = o1.getOpen();
                    high[i] = o1.getHigh();
                    low[i] = o1.getLow();
                    close[i] = o1.getClose();
                    volume[i] = o1.getVolume();
                    i++;
                }
                mainBean.setDateTime(date);
                mainBean.setClose(close);
                mainBean.setHigh(high);
                mainBean.setLow(low);
                mainBean.setOpen(open);
                mainBean.setVolume(volume);
                mainBean.setStockList(stocDataList);
                mainBean.setName(sname);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (Exception e) {
        } finally {
            closeConnection(con, rs, stmt);
        }
        return mainBean;
    }

    private static int getDataCount(String sname) {
         StringBuilder sql = new StringBuilder();
        Connection con = null;
        //     System.out.println("fName " + client + "   " + prov + "  ");
        PreparedStatement stmt = null;
        ResultSet rs = null;
       int count=0;
        try {
            con = getConnetion();
            sql.append(" select count(1) from stockdata where stockid=?");
            stmt = con.prepareStatement(sql.toString());
            stmt.setString(1, sname);
            rs = stmt.executeQuery();
            while (rs.next()) {
                count=rs.getInt(1);
            }
        } catch (Exception e) {
        } finally {
            closeConnection(con, rs, stmt);
        }
        return count;
    }

    public static String getFundamentals(String sname) {
        StringBuilder sql = new StringBuilder();
        Connection con = null;
        //     System.out.println("fName " + client + "   " + prov + "  ");
        PreparedStatement stmt = null;
        ResultSet rs = null;
       int count=0;
        try {
            con = getConnetion();
            sql.append(" select pe,eps,industry_pe,pricecash,sector from fundamentals where stockid=?");
            stmt = con.prepareStatement(sql.toString());
            stmt.setString(1, sname);
            rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.println("");
                System.out.print("PE "+rs.getString("pe"));
                System.out.print("\tEPS "+rs.getString("eps"));
                System.out.print("\tIndustry pe "+rs.getString("industry_pe"));
                System.out.print("\tP/C "+rs.getString("pricecash"));
                System.out.print("\tSector "+rs.getString("sector"));
                System.out.println("");
            }
        } catch (Exception e) {
        } finally {
            closeConnection(con, rs, stmt);
        }
        return count+"";
    }
    
	public static bean.DataMap executeQuery1(String sql) {
		Statement stmt = null;
		Connection con = null;
		ResultSet rs = null;
		bean.DataMap data = new bean.DataMap();
		try {
			con = getConnetion();
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			ResultSetMetaData metadata = rs.getMetaData();
			int columnCount = metadata.getColumnCount();

			while (rs.next()) {
				for (int i = 1; i <= columnCount; i++) {
					data.put(metadata.getColumnLabel(i), rs.getObject(i));
				}
				data.add();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// closeConnection(con, null, stmt);
		}
		return data;
	}
    
    
}
