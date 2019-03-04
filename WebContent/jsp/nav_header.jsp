
<%@page import="javax.servlet.http.*"%>
<%@page import="util.ApplicationConstants"%>



<%
             //   SecUserBean secUserObj = (SecUserBean) session.getAttribute(ApplicationConstants.USER_SESSION_NAME);
                String user = "Guest";
             /*   if (secUserObj != null) {
                        //user = secUserObj.getFirstName() + " " + secUserObj.getLastName();
                        user = secUserObj.getLoginId();
                }*/
%>
<div class="header">
        <table id="header_table" width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                        <td align="right" width="20%">
                                <div class="logo"><img height='55'  src="<%=ApplicationConstants.IMAGE_PATH%>msedclogo.gif"/></div>
                        </td>
                        <td width='60%' align='center' valign="top">
                                <b class='compHeading'>Stock Analysis.</b><br>
                                <b id="center" class='systemTitle'>Different Analysis System</b><br>
                                <div class="seperator"></div>
                                <b id="center" class="userLogin">User: <%=user%> </b>
                        </td>
                        <td align="right" width="20%">
                                <div class="welcome_date">
                                        <span class="text_whitebold">
                                                Date : <script type="text/javascript">document.write(getDate());</script>
                                        </span>
                                   
                                </div>


                        </td>

                </tr>
                <tr>
                        <td>
                        </td>
                </tr>
        </table>
</div>