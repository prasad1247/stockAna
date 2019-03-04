<%@page import="util.ApplicationConstants"%>

<%
long startLong = new java.util.Date().getTime();
%>
<script>
var startDate = new Date();
startDate.setTime(<%=startLong%>);
var startMs = startDate.getTime() * 1;
</script>

<meta http_equiv="Content_Type" content="text/html; charset=iso_8859_1">

<link href="<%=ApplicationConstants.CSS_PATH%>main.css" rel="stylesheet" type="text/css">
<link href="<%=ApplicationConstants.CSS_PATH%>mda.css" rel="stylesheet" type="text/css">
<link href="<%=ApplicationConstants.CSS_PATH%>CalendarControl.css"  rel="stylesheet" type="text/css"/>

<script language="JavaScript" src="<%=ApplicationConstants.JS_PATH%>CalendarControl.js"></script>
<script language="JavaScript" src="<%=ApplicationConstants.JS_PATH%>mda.js"></script>
<script language="JavaScript" src="<%=ApplicationConstants.JS_PATH%>fsmenudiv.js"></script>
<script language="JavaScript" src="<%=ApplicationConstants.JS_PATH%>boxdiv.js"></script>
<script language="JavaScript" src="<%=ApplicationConstants.JS_PATH%>errormessages.js"></script>
<script language="JavaScript" src="<%=ApplicationConstants.JS_PATH%>dropdown.js"></script>


