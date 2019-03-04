<%@page import="java.util.concurrent.Future"%>
<%@page import="util.ApplicationUtils"%>
<%@page import="bean.Stock"%>
<%@page import="bean.TransferObj"%>
<%@page import="bean.StockPrice"%>
<%@page import="java.util.*"%>
<%@page import="java.util.HashMap"%>
<%



%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
	<script type="text/javascript">window.jQuery || document.write('<script type="text/javascript" src="js/jquery-1.7.2.min.js"><\/script>');</script>
	<script type="text/javascript" src="js/jquery.main.js"></script>
       <script type="text/javascript" src="js/jquery-ui/jquery.ui.core.min.js"></script>
    <script src="js/jquery-ui/jquery.ui.widget.min.js" type="text/javascript"></script>
    <script src="js/jquery-ui/jquery.ui.accordion.min.js" type="text/javascript"></script>
    <script src="js/jquery-ui/jquery.effects.core.min.js" type="text/javascript"></script>
    <script src="js/jquery-ui/jquery.effects.slide.min.js" type="text/javascript"></script>
    <script src="js/jquery-ui/jquery.ui.mouse.min.js" type="text/javascript"></script>
    <script src="js/jquery-ui/jquery.ui.sortable.min.js" type="text/javascript"></script>
    <script src="js/ta/jquery.dataTables.js" type="text/javascript"></script>
    <script src="js/ta/jquery.dataTables.columnFilter.js"  type="text/javascript" ></script>


        <script>
   $(document).ready(function(){
     $('#example').dataTable()
		  .columnFilter({ sPlaceHolder: "head:before",
			aoColumns: [ 
                                      { type: "text" },
                                      null,
                                      null,
                                      null,
                                      
                                      null,
                                       null,
                                   // { type: "select", values: [ 'Gecko', 'Trident', 'KHTML', 'Misc', 'Presto', 'Webkit', 'Tasman']  },
				     null,
				     null,
                                      { type: "number" },
                                     { type: "number" },
                                     { type: "number" },
                                               
                                     { type: "text" }
				]

		});
});
</script>
        
        <meta http-equiv="content-Type" content="text/html; charset=iso-8859-1" />
        <title>Stocks</title>
        <jsp:include page="nav_jscss.jsp" />
        <%
            TransferObj tfo = (TransferObj) request.getAttribute("TransferObj");
            int len = tfo.getAllstockList().size();
            System.out.println("  le "+len);
        %>
    </head>
    <body>
        <form name="homeform" >
            <div id="cover"><img src="<%=ApplicationConstants.IMAGE_PATH%>spacer.gif"></div>
            <!-- Start header content -->
            <%@ include file="nav_header.jsp" %>
            <!-- Ends header content -->
            <!-- Start Horizontal Menu  -->
            <!--<td valign="top" width="150px"> -->

            <!--</td>-->
            <!-- Ends Horizontal Menu  -->
            
    
            <div id="body_container_holder">
                

                <!-- hidden fields  -->               
                <table id="example" class="display datatable" cellspacing="0" width="100%">  
                    <thead>
                    <tr>
                        <th>Name</th>
                        <th>Date</th>
                        <th>open</th>
                        <th>high</th>
                        <th>low</th>
                        <th>close</th>
                        <th><%=ApplicationConstants.EMA_3%></th>
                        <th><%=ApplicationConstants.EMA_15%></th>
                        <th><%=ApplicationConstants.ADX%></th>
                        <th><%=ApplicationConstants.RSI%></th>
                        <th><%=ApplicationConstants.ATR%></th>
                        <th>EMA3-15 Signal</th>
                    </tr> 
                         <tr>
                        <th>Name</th>
                        <th>Date</th>
                        <th>open</th>
                        <th>high</th>
                        <th>low</th>
                        <th>close</th>
                        <th><%=ApplicationConstants.EMA_3%></th>
                        <th><%=ApplicationConstants.EMA_15%></th>
                        <th><%=ApplicationConstants.ADX%></th>
                        <th><%=ApplicationConstants.RSI%></th>
                        <th><%=ApplicationConstants.ATR%></th>
                        <th>EMA3-15 Signal</th>
                    </tr> 
                        </thead>
                        <tbody>
                    <%
                        HashMap indicators = null;
                        int i=0;
                        if (tfo != null && len > 0) {
                            Iterator itr = tfo.getAllstockList().iterator();
                            Stock mainBean = null;
                            StockPrice sp = null;
                            Future f=null;
                            while (itr.hasNext()) {
                                try{
                                //f= (Future<Stock>) itr.next();
                                //mainBean=(Stock)f.get();
                                mainBean=(Stock)itr.next();
                                indicators = mainBean.getIndicators();
                                double[] val = (double[]) indicators.get(ApplicationConstants.EMA_3);
                                double[] val1 = (double[]) indicators.get(ApplicationConstants.EMA_15);
                                double[] val2 = (double[]) indicators.get(ApplicationConstants.ADX);
                                double[] val3 = (double[]) indicators.get(ApplicationConstants.RSI);
                                double[] val4 = (double[]) indicators.get(ApplicationConstants.ATR);
                                int emaSig = mainBean.getEmaSignal();
                                sp = (StockPrice) mainBean.getStockList().getLast();
                                 System.out.println("  "+(i++));
                    %>
                    <tr>
                        <td>
                            <%=mainBean.getName()%>
                        </td>
                        <td>
                            <%=sp.getDateTime()%>
                        </td>
                        <td>
                            <%=sp.getOpen()%>
                        </td>
                        <td>
                            <%=sp.getHigh()%>
                        </td>
                        <td>
                            <%=sp.getLow()%>
                        </td>
                        <td>
                            <%=sp.getClose()%>
                        </td>
                        <td>
                            <%=ApplicationUtils.Round(val[val.length - 1], 2)%>
                        </td>
                        <td>
                            <%=ApplicationUtils.Round(val1[val1.length - 1], 2)%>
                        </td>
                        <td>
                            <%=ApplicationUtils.Round(val2[val2.length - 1], 2)%>
                        </td>
                        <td>
                            <%=ApplicationUtils.Round(val3[val3.length - 1], 2)%>
                        </td>
                        <td>
                            <%=ApplicationUtils.Round(val4[val4.length - 1], 2)%>
                        </td>
                         <td>
                            <%
                                if (val1[val1.length - 1] < val[val.length - 1]) {
                                    out.print("UP Signal Came " + emaSig + " period before");
                                } else {
                                    out.print("Down Signal Came " + emaSig + " period before");
                                }

                            %>
                        </td>

                    </tr>   
                    <% }catch(Exception e ){
                                e.printStackTrace();
                            
                            }
                        }
                            
                        } 
                    %>
                        </tbody>
                </table>
            </div>
        </form>
    </body>
</html>
