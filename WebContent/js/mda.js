var whitespace = " \t\n\r";


// function to get date
function getDate()
{
    currentdate = new Date()
    day = currentdate.getDay();
    date = currentdate.getDate();
    month = currentdate.getMonth();
    year = currentdate.getFullYear();
					
    dayArray=["Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"];
    day=dayArray[day];
	
    if(date==1||date==21||date==31)
        date = date+"<sup>st</sup>";
    else if(date==2||date==22)
        date = date+"<sup>nd</sup>";
    else if(date==3||date==23)
        date = date+"<sup>rd</sup>";
    else
        date = date+"<sup>th</sup>";
					
    monthArray=["January","February","March","April","May","June","July","August","September","October","November","December"];
    month=monthArray[month];

    return date + " " + month + ", " + year;
}	

// Function to open popUp div
function showPopUp(el)
{
    var cvr = document.getElementById("cover");
    var dlg = document.getElementById(el);
    cvr.style.display = "block";

    dlg.style.visibility = "visible";
    dlg.style.marginTop = "150";
    dlg.style.marginLeft = "250";
    dlg.style.display = "block";

    if (document.body.style.overflow = "hidden") {
        cvr.style.width = "100%";
        cvr.style.top = document.body.scrollTop+"px";
        cvr.style.height = document.body.offsetHeight+"px";
    }
}

// Function to close popUp div
function closePopUp(el) 
{
    //	var cvr = document.getElementById("cover");
    var dlg = document.getElementById(el);
    //	cvr.style.display = "none";
    dlg.style.display = "none";
    if(document.all)
        document.body.style.overflowY = "hidden";
    else
        document.body.style.overflowY = "scroll";
}

/*Marking all checkboxes*/
function allcheck(checked)
{
    var i = 0;
    var obj = document.getElementsByName("search_checkbox");
    if(checked==true)
    {
        for (i = 0 ; i<obj.length ; i++ )
        {
            obj[i].checked = true;
        }
    }
    else
    {
        for (var i = 0 ; i<obj.length ; i++ )
        {
            obj[i].checked = false;
        }
    }
}

function allcheck2(checked)
{
    var i = 0;
    var obj = document.getElementsByName("search_checkbox2");
    if(checked==true)
    {
        for (i = 0 ; i<obj.length ; i++ )
        {
            obj[i].checked = true;
        }
    }
    else
    {
        for (var i = 0 ; i<obj.length ; i++ )
        {
            obj[i].checked = false;
        }
    }
}
/* An Ajax function
@ param method type ( post or get), URL, parameter, Asynchronous handling status, call_back function object */

function callAjax(method_type,url,params,varAsync,callBack)
{
    var xmlHttp;
    // Creating an XMLHttpRequest object
    if  (window.XMLHttpRequest) 
        xmlHttp = new XMLHttpRequest(); 
    else if (window.ActiveXObject) 
        xmlHttp = new ActiveXObject("Microsoft.XMLHTTP"); 
	
	
    // checking state change
    /*
	xmlHttp.onreadystatechange = function ()
	{
		if (xmlHttp.readyState == 4)
		{
			if (xmlHttp.status == 200)
			{
				callBack(xmlHttp.responseText);
			}
				
		}
	}; 
*/
    // opening and sending request
    xmlHttp.open(method_type,url,varAsync);
    xmlHttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xmlHttp.send(params);

    if(!varAsync)
    {
        callBack(xmlHttp.responseText);
    }
    else
    {
        xmlHttp.onreadystatechange = function ()
        {
            if (xmlHttp.readyState == 4)
            {
                if (xmlHttp.status == 200)
                {
                    callBack(xmlHttp.responseText);
                } 
            }
        }
    }
}

function OnDeviceTypeChange(){
    var deviceType=document.getElementById('deviceType').value;
    // alert(deviceType);
    var out = {
        response : function validation(info){           
            
            var jsonObj = eval("(" + info + ")");
            
            //alert(jsonObj.innerStr);
            document.getElementById("locationDetails").innerHTML=jsonObj.innerStr;
        }
    }
    
    var url = "ajax";
    var ui = "OnDeviceTypeChange";
    var params = "deviceType=" + escape(deviceType)+"&uiaction="+ ui;
    callAjax("POST",url,params,false,out.response);
}

function generateAllConfigXMLs(){    
    var generateAction=document.getElementById("generateAction").value;
    var params = "";
    var i;
    var errString="";   
    var loadprofiledays;
    var loadprofiletype;
    var energydata;  
    var deviceId = "";
    var out = {
        response : function validationConfigFile(res){                    
            var jsonObj = eval("(" + res + ")");  
            if(jsonObj.errors!=null){
                document.getElementById("errorMsg").innerHTML="<tr><td colspan=\"5\">"+jsonObj.errors+". \n\
            <a href=\"javascript:MM_openBrWindow('mda?uiActionName=viewXMLLogger','XMLGenerationExceptions','fullscreen=no,toolbar=no,status=no,menubar=no,scrollbars=yes,resizable=yes,directories=yes,ocation=yes,width=750,height=450,left=100,top=100')\"> More..</a></td></tr>//";
            }else{
                alert(jsonObj.msgString);
            //var size= jsonObj.Size;
            //        /* alert(size);
            //                 for (i = 0 ; i<size ; i++ )
            //                    {
            //                         alert(i);
            //                    } working*/
            }
            allcheck(false);
        }
    }
    if(generateAction == 1){
        deviceId =getSelectedDevices();
        params ="&deviceId="+escape(deviceId);
        if(deviceId=="")
            errString="No Device Selected !!!";
    }
    if(deviceId!="" || generateAction == 2){
             
        loadprofiledays= document.getElementById("loadprofiledays").value;
        loadprofiletype= document.getElementById("loadprofiletype").value;
        energydata=document.getElementById("energydata").value;
            
        if(!document.getElementById("operation6").checked &&
            (document.getElementById("loadprofiledays").value!="0" ||document.getElementById("loadprofiletype").value!="NO" )){
            errString="Please Tick Load Profile CheckBox..";
            document.getElementById("operation6").focus();
        }else if(document.getElementById("operation6").checked && document.getElementById("loadprofiledays").value=="0" && document.getElementById("loadprofiletype").value!="NO"){
            errString="Please Specify Days For Load Profile..";
            document.getElementById("loadprofiledays").focus();
        }else if(document.getElementById("operation6").checked && document.getElementById("loadprofiledays").value!="0" && document.getElementById("loadprofiletype").value=="NO" ){
            errString="Please Select Profile Type For Load Profile..";
            document.getElementById("loadprofiletype").focus();
        }        
        if(document.getElementById("operation3").checked){
            params=params+"&operation3="+escape(document.getElementById("operation3").value);
        }
        if(document.getElementById("operation4").checked){
            params=params+"&operation4="+escape(document.getElementById("operation4").value);
        }
        if(document.getElementById("operation5").checked){
            params=params+"&operation5="+escape(document.getElementById("operation5").value);
        }
        if(document.getElementById("operation6").checked){
            params=params+"&operation6="+escape(document.getElementById("operation6").value);
        }
        if(document.getElementById("operation7").checked){
            params=params+"&operation7="+escape(document.getElementById("operation7").value);
        }
        if(document.getElementById("operation8").checked){
            params=params+"&operation8="+escape(document.getElementById("operation8").value);
        }  
        if(document.getElementById("operation9").checked){
            params=params+"&operation9="+escape(document.getElementById("operation9").value);
        } 

        if(document.getElementById("devicetype")!=null){
            params=params+"&devicetype="+escape(document.getElementById("devicetype").value);
        }
        if(document.getElementById("zone")!=null){
            params=params+"&zone="+escape(document.getElementById("zone").value);
        }
        if(document.getElementById("circle")!=null){
            params=params+"&circle="+escape(document.getElementById("circle").value);
        }
        if(document.getElementById("division")!=null){
            params=params+"&division="+escape(document.getElementById("division").value);
        }
        if(document.getElementById("subDivision")!=null){
            params=params+"&subDivision="+escape(document.getElementById("subDivision").value);
        }
        if(document.getElementById("installpointtype")!=null){
            params=params+"&installpointtype="+escape(document.getElementById("installpointtype").value);
        }
        if(document.getElementById("project")!=null){
            params=params+"&project="+escape(document.getElementById("project").value);
        }
        if(document.getElementById("town")!=null){
            params=params+"&town="+escape(document.getElementById("town").value);
        }
        if(document.getElementById("searchCriteria")!=null){
            params=params+"&searchCriteria="+escape(document.getElementById("searchCriteria").value);           
        }
        if(document.getElementById("data")!=null){
            params=params+"&searchCriteriadata="+escape(document.getElementById("data").value);           
        }
  
        if(document.getElementById("newlyAdded").checked){
            params=params+"&newlyAdded="+escape(document.getElementById("newlyAdded").value);           
        }
  
        if(document.getElementById("statusresult")!=null){
            params=params+"&statusresult="+escape(document.getElementById("statusresult").value);
        }
        if(errString!=""){
            alert(errString);  
        }else{
            var url = "ajax";
            var ui = "generateAllConfigXMLs";
            params=params+"&loadprofiledays="+escape(loadprofiledays)+"&loadprofiletype="+loadprofiletype+"&energydata="+energydata+"&uiaction="+ ui;        
            //alert(params);
            callAjax("POST",url,params,false,out.response);      
        }
    }
    else alert("No Device Selected !!!");
}

function getSchedules(){
    var profilerType= document.searchform.profilerType.value;

    var out = {
        response : function validationProfilerType(res){                    
            var jsonObj = eval("(" + res + ")"); 
           // alert(jsonObj.msgString);
            document.getElementById("schedulesList").innerHTML="<select name='schedules'  class='textfield' onChange='getScheduleOpParam();'>"+ jsonObj.msgString +"</select>";
            //alert("<select name='schedules'  class='textfield'>"+ jsonObj.msgString +"</select>");
        }
    }
    var url = "ajax";
    var ui = "getSchedules";
    var params=params+"&profilerType="+escape(profilerType)+"&uiaction="+ ui;    
    //alert(params);
    callAjax("POST",url,params,false,out.response); 
}

function getScheduleOpParam(){
    var scheduleId= document.searchform.schedules.value;
    var out = {
        response : function validationScheduleOpParam(res){                    
            var jsonObj = eval("(" + res + ")");           
            document.getElementById("operation3").checked=jsonObj.operation3=="true"?true:false;
            document.getElementById("operation4").checked=jsonObj.operation4=="true"?true:false;
            document.getElementById("operation5").checked=jsonObj.operation5=="true"?true:false;
            document.getElementById("operation6").checked=jsonObj.operation6=="true"?true:false;
            document.getElementById("operation7").checked=jsonObj.operation7=="true"?true:false;
            document.getElementById("operation8").checked=jsonObj.operation8=="true"?true:false;
            document.getElementById("operation9").checked=jsonObj.operation9=="true"?true:false;            
            document.getElementById("loadprofiledays").value=jsonObj.loadprofiledays;         
            document.getElementById("loadprofiletype").value=jsonObj.loadprofiletype;
            document.getElementById("energydata").value=jsonObj.energydata;
        }
    }
    if(scheduleId=="-1"){
        document.getElementById("operation3").disabled=true;
        document.getElementById("operation4").disabled=true;
        document.getElementById("operation5").disabled=true;
        document.getElementById("operation6").disabled=true;
        document.getElementById("operation7").disabled=true;
        document.getElementById("operation8").disabled=true;
        document.getElementById("operation9").disabled=true;
        document.getElementById("loadprofiledays").disabled=true;
        document.getElementById("loadprofiletype").disabled=true;
        document.getElementById("energydata").disabled=true;
    }else{
        document.getElementById("operation3").disabled=false;
        document.getElementById("operation4").disabled=false;
        document.getElementById("operation5").disabled=false;
        document.getElementById("operation6").disabled=false;
        document.getElementById("operation7").disabled=false;
        document.getElementById("operation8").disabled=false;
        document.getElementById("operation9").disabled=false;
        document.getElementById("loadprofiledays").disabled=false;
        document.getElementById("loadprofiletype").disabled=false;
        document.getElementById("energydata").disabled=false;
    }
    var url = "ajax";
    var ui = "getScheduleOpParam";
    var params=params+"&scheduleId="+escape(scheduleId)+"&uiaction="+ ui;    
    //alert(params);
    callAjax("POST",url,params,false,out.response); 
}

function populateDetails(consumerNumber)
{
    var out = {

        response : function validation(info)
        {
            if(info == "INVALIDUSER")
            {
                alert("Consumer Number "+consumerNumber+" not found ")
            }
            else{
                var jsonObj = eval("(" + info + ")");
                getSubLocations1("subDivision", 0, false, jsonObj.locationId);
                getSubLocations1("substation", jsonObj.locationId, false, jsonObj.substationId );
                getSubLocations1("feeder", jsonObj.substationId, false, jsonObj.feederId);
                getSubLocations1("dtc", jsonObj.feederId, false, jsonObj.transformerId);
                getSubLocations1("consumer", jsonObj.transformerId, false, jsonObj.consumerId);
            }
        }
    }
    var url = "ajax";
    var ui = "populateConsumerMapping";
    var params = "consumerNumber=" + escape(consumerNumber)+"&uiaction="+ ui;
    callAjax("POST",url,params,false,out.response);
}

function getProfiles(profileType)
{
    var out = {

        response : function validation(info)
        {
            var jsonObj = eval("(" + info + ")");
            document.getElementById("profileDiv").innerHTML  = "<select name='profiles'  id='profiles' class='textfield' style='width:170px'>"+jsonObj.profileString+"</select>";
        }
    }
    var url = "ajax";
    var ui = "populateProfiles";
    var params = "profileType=" + escape(profileType)+"&uiaction="+ ui;
    callAjax("POST",url,params,false,out.response);
}

function getProfilesconn(profileType)
{
    var out = {

        response : function validation(info)
        {
            var jsonObj = eval("(" + info + ")");
            document.getElementById("profileDiv").innerHTML  = "<select name='profiles'  id='profiles' class='textfield' style='width:170px'>"+jsonObj.profileString+"</select>";
        }
    }
    var url = "ajax";
    var ui = "populateProfilesconn";
    var params = "profileType=" + escape(profileType)+"&uiaction="+ ui;
    callAjax("POST",url,params,false,out.response);
}
// function to check duplicate serial number
function checkSerialNumber(serialNumber)
{
    var out = {

        response : function validation(text)
        {

            if (text == "INVALIDSERIALNUMBER")
            {
                alert("Serial Number is Present in Database. \n Please Add Another One.");
                document.getElementById("deviceSerialNo").value = "";
            //document.getElementById("password").value = "";
            }
        }
    }
    var url = "ajax";
    var ui = "validateSerialNumber";
    var params = "serialNumber=" + escape(serialNumber)+"&uiaction="+ ui;
    callAjax("POST",url,params,false,out.response);
}

// function to populating the location dropdown--
function getLocation(value)
{
    var str;
    var optionStr;
    if (value != -1)
    {
        optionStr = locationArray[value];
        str = '<SELECT class=textfield id="officeLocation" name="officeLocation">'+optionStr+'</SELECT>';
    }
    else
    {
        str = '<SELECT class=textfield id="officeLocation" name="officeLocation"><OPTION selected value = "-1"> All </OPTION></SELECT>';
    }
    document.getElementById("locationlist").innerHTML = str;
}

// function that calls Ajax servlet to validate the user information -----
function validateUser()
{
    var out = { 
					
        response : function validation(text)
        {
			
            if (text == "INVALIDUSER")
            {
                alert("Invalid Username or Password !!!");
                document.getElementById("username").value = ""; 
                document.getElementById("password").value = ""; 
            }
            else if (text == "INACTIVEUSER")
            {
                alert("Invalid Username or Password !!!");
                document.getElementById("username").value = ""; 
                document.getElementById("password").value = ""; 
            }
            else if (text == "ACTIVEUSER")
            {
                document.loginform.submit();
            }
        }
    }
			
    // appending url---
    var url = "ajax";
    var ui = "validateuser";
    var params = "login_id=" + escape(document.loginform.username.value)+"&password=" + escape(document.loginform.password.value)+"&uiaction="+ ui;
    callAjax("POST",url,params,false,out.response); 
}

// function to validate the input date by the user--
function validate_date()
{
    var str1 = document.searchform.scheduledfrom.value;
    var str2 = document.searchform.scheduledto.value;
	
    if (str1 != "" && str2 != "")
    {
		
        var str3 = parseInt((document.searchform.scheduledfromhr.value),10);
        var str4 = parseInt((document.searchform.scheduledtohr.value),10);
        var str5 = parseInt((document.searchform.scheduledfrommin.value),10);
        var str6 = parseInt((document.searchform.scheduledtomin.value),10);
        var mArr1 = str1.split("-");
        var mArr2 = str2.split("-");
			
        if (document.searchform.scheduledfromhr.value == "")
        {
            str3 = 0;
        }
        if (document.searchform.scheduledtohr.value == "")
        {
            str4=0;
        }
        if (document.searchform.scheduledfrommin.value == "")
        {
            str5 = 0;
        }

        if (document.searchform.scheduledtomin.value == "")
        {
            str6 = 0;
        }
		
        var date1 = new Date();
        var date2 = new Date(); 
        date1.setDate(parseInt(checkForZero(mArr1[0])));
        date1.setMonth(getMonth(mArr1[1]));
        date1.setFullYear(parseInt(checkForZero(mArr1[2])));
        date2.setDate(parseInt(checkForZero(mArr2[0])));
        date2.setMonth(getMonth(mArr2[1]));
        date2.setFullYear(parseInt(checkForZero(mArr2[2])));
        date1.setHours(str3,str5,00);
        date2.setHours(str4,str6,00);

        if(date2 < date1) 
        {
            alert("Start Date cannot be greater than End Date !!!");
            return false;
        }
        else if ((str3 || str3 == 0) && (str4 || str4 == 0) && (str5 || str5 == 0) && (str6 || str6 == 0))
        {
            if ((str3 > 23 || str3 < 0) || (str4 > 23 || str4 < 0))
            {
                alert("Invalid Time !!! Hours value must be greater than 0 and less than 24 !!!");
                return false;
            }
            else if ((str5 > 59 || str5 < 0) || (str6 > 59 || str6 < 0))
            {
                alert("Invalid Time !!! Minutes value must be greater than 0 and less than 60 !!!");
                return false;
            }
            else
            {
                return true;
            }
        }
        else
        {
            alert("Enter Valid Time !!!");
            return false;
        }
    }
    else if (str1 != "")
    {
		
        var str3 = parseInt((document.searchform.scheduledfromhr.value),10);
        var str5 = parseInt((document.searchform.scheduledfrommin.value),10);
							
        if (document.searchform.scheduledfromhr.value == "")
        {
            str3 = 0;
        }
		
        if (document.searchform.scheduledfrommin.value == "")
        {
            str5 = 0;
        }
				
        if ((str3 || str3 == 0)  && (str5 || str5 == 0))
        {
            if ((str3 > 23 || str3 < 0) || (str4 > 23 || str4 < 0))
            {
                alert("Invalid Time !!! Hours value must be greater than 0 and less than 24 !!!");
                return false;
            }
            else if ((str5 > 59 || str5 < 0) || (str6 > 59 || str6 < 0))
            {
                alert("Invalid Time !!! Minutes value must be greater than 0 and less than 60 !!!");
                return false;
            }
            else
            {
                return true;
            }
        }
        else
        { 
            alert("Enter Valid Time !!!");
            return false;
        }
    }
    else if (str2 != "")
    {
		
        var str4 = parseInt((document.searchform.scheduledtohr.value),10);
        var str6 = parseInt((document.searchform.scheduledtomin.value),10);
        var mArr2 = str2.split("-");
		
        if (document.searchform.scheduledtohr.value == "")
        {
            str4=0;
        }
        if (document.searchform.scheduledtomin.value == "")
        {
            str6 = 0;
        }
	
        if ((str4 || str4 == 0) && (str6 || str6 == 0))
        {
            if ((str3 > 23 || str3 < 0) || (str4 > 23 || str4 < 0))
            {
                alert("Invalid Time !!! Hours value must be greater than 0 and less than 24 !!!");
                return false;
            }
            else if ((str5 > 59 || str5 < 0) || (str6 > 59 || str6 < 0))
            {
                alert("Invalid Time !!! Minutes value must be greater than 0 and less than 60 !!!");
                return false;
            }
            else
            {
                return true;
            }
        }
        else
        {
            alert("Enter Valid Time !!!");
            return false;
        }
    }
    else {
        return true;
    }
}

// function to get month
function getMonth(mon)
{
    var monArr = new Array ("Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec");
    for (i=0;i<=11 ;i++ )
    {
        if (mon == monArr[i])
        {
            return i;
        }
    }
    return -1;
}

//function to check Zero in date
function checkForZero(value)
{
    if (value.charAt(0) == '0')
    {
        value = value.charAt(1);
    }
    return value;
}

function disableDateFields(value)
{
    if (value != -1)
    {
        ///This is for clear the fileds data.
        document.getElementById("scheduledfrom").value = "";
        document.getElementById("scheduledfromhr").value = "";
        document.getElementById("scheduledfrommin").value = "";
        document.getElementById("scheduledto").value = "";
        document.getElementById("scheduledtohr").value = "";
        document.getElementById("scheduledtomin").value = "";

        ///This is for disabled the fileds.
        document.getElementById("scheduledfrom").disabled = true;
        document.getElementById("scheduledfromhr").disabled = true;
        document.getElementById("scheduledfrommin").disabled = true;
        document.getElementById("scheduledto").disabled = true;
        document.getElementById("scheduledtohr").disabled = true;
        document.getElementById("scheduledtomin").disabled = true;
    ///alert(document.getElementById("scheduleDateTimeDiv").style);
    ///document.getElementById("scheduleDateTimeDiv").style.display = 'block';
    }
    else 
    {
        document.getElementById("scheduledfrom").disabled = false;
        document.getElementById("scheduledfromhr").disabled = false;
        document.getElementById("scheduledfrommin").disabled = false;
        document.getElementById("scheduledto").disabled = false;
        document.getElementById("scheduledtohr").disabled = false;
        document.getElementById("scheduledtomin").disabled = false;
    ///alert(document.getElementById("scheduleDateTimeDiv").style);
    ///document.getElementById("scheduleDateTimeDiv").style.display = 'none';
    }
}

function callCalender(ele,formate)
{
    ///alert("Hello...");
    if (document.getElementById(ele).disabled == false)
    {
        registeredClick = true;
        if(formate)
            yyyyFormat = true;

        showCalendarControl(document.getElementById(ele));
    }
    else
        return false;
}

function clearInput(ele)
{
    document.getElementById(ele).value = "";
}

// -- Resets the form to the selected values--
function resetForm()
{
    document.getElementById("devicetype").value = selectedValuesArray["devicetype"];
    document.getElementById("zone").value = selectedValuesArray["zone"];
    document.getElementById("scheduleperiod").value = selectedValuesArray["scheduleperiod"];
    document.getElementById("scheduledfrom").value = selectedValuesArray["scheduledfrom"];
    document.getElementById("scheduledfromhr").value = selectedValuesArray["scheduledfromhr"];
    document.getElementById("scheduledfrommin").value = selectedValuesArray["scheduledfrommin"];
    document.getElementById("scheduledto").value = selectedValuesArray["scheduledto"];
    document.getElementById("scheduledtohr").value = selectedValuesArray["scheduledtohr"];
    document.getElementById("scheduledtomin").value = selectedValuesArray["scheduledtomin"];
    document.getElementById("installpointtype").value = selectedValuesArray["installpointtype"];
    document.getElementById("numberid").value = selectedValuesArray["numberid"];
    getSubLocations('circle',document.getElementById("zone").value,true);
    disableDateFields(document.getElementById("scheduleperiod").value);
}

// -- Function That calls ajax servlet to invalidate user or logout--
function logout(uiaction)
{
    var code = { 
					
        response : function logoutUser(text)	{
			
            if (text == "LOGOUT-SUCESS")
            {
        //document.getElementById("uiActionName").value = uiaction;
        //document.loginform.submit();
        }
        }
    }
    // appending url---
    var url = "ajax";
    var ui = "logout";
    var params = "uiaction="+ ui;
    callAjax("POST",url,params,false,code.response); 
}

/*function changeSubAction(uiaction)
{
	document.searchform.subAction.value = uiaction;
	document.searchform.submit();
}*/

function schedule(uiaction)
{
    var code = { 
					
        response : function logoutUser(text)	{
            alert(text);
            divMenu2.hide(document.getElementById("schedule_div"));
            document.searchform.submit();
        }
    }
    // appending url---
    var deviceStr = getSelectedDevices();
    var url = "ajax";
    var ui = uiaction;
    var params = "uiaction="+ ui + "&deviceStr="+ deviceStr;

    // checking if devices are selected---
	

    if (uiaction == "autoscheduleall")
    {
        if (confirm("Do you want to schedule All the Devices ?") )
        {	
            // Calling Ajax Servlet
            callAjax("POST",url,params,true,code.response);
        }
    }
    else
    {	
        if (confirm("Do you want to schedule selected Device(s) ?") )
        {	
            // Calling Ajax Servlet
            callAjax("POST",url,params,true,code.response);
        }
        else
            divMenu2.hide(document.getElementById("schedule_div"));
    }
} 


/*Get the comma seperated list of devices selected*/
function getSelectedDevices()
{
    var i = 0;
    var deviceStr = "";
    var obj = document.getElementsByName("search_checkbox");
    for (i = 0 ; i<obj.length ; i++ )
    {
        if (obj[i].checked == true)
        {	
            if (deviceStr == "")
            {
                deviceStr = deviceStr + obj[i].value
            }
            else
            {
                deviceStr = deviceStr + "," + obj[i].value;
            }
        }	
    }
    return deviceStr;
}

function getSelectedDevices2()
{
    var i = 0;
    var deviceStr = "";
    var obj = document.getElementsByName("search_checkbox2");
    for (i = 0 ; i<obj.length ; i++ )
    {
        if (obj[i].checked == true)
        {	
            if (deviceStr == "")
            {
                deviceStr = deviceStr + obj[i].value
            }
            else
            {
                deviceStr = deviceStr + "," + obj[i].value;
            }
        }	
    }
    return deviceStr;
}
/*function to check whether devices are selected or not*/
function checkSelectedDevices(uiAction)
{
    var i;
    var obj = document.getElementsByName("search_checkbox");
    var chk = false;
    for (i = 0 ; i<obj.length ; i++ )
    {
        if (obj[i].checked == true)
        {	
            chk = true;	
            break;
        }
    }

    if (chk == true && uiAction == "schedule")
    {
        clearInput("scheduleddt");
        clearInput("scheduledhr");
        clearInput("scheduledmin");
        document.getElementById("updateSchSelect").value = "1";
        suggestTerm(document.getElementById("updateSchSelect").value,"schdule_time");
        divMenu2.show("schedule_div",document.getElementById("schedule"),-225,-260);
    }
    else if (chk == true && (uiAction == "autoschedule" || uiAction == "removeschedule"))
    {
        schedule(uiAction);
    }
    else
        alert("No Device Selected !!!");
    return chk;
}

/*Check whether given input field is empty*/
function checkDateField(selectId,date,hrs,min)
{
    if (document.getElementById(selectId).value == "3")
    {
        if (document.getElementById(date).value =="" )
        {
            alert("Enter Date !!!");
            return false;
        }
        else if (document.getElementById(hrs).value =="") 
        {
            alert("Enter Hours !!!");
            return false;
        }
        else if (document.getElementById(min).value =="" )
        {
            alert("Enter Minutes !!!");
            return false;
        }
        else
        {
            schedule('schedule');

            return true;
        }
    }
    else
    {
        schedule('schedule');
        return true;
    }
}


/* Function to call AjaxServlet for getting the Search Results*/
function getSearchPaging()
{
    var code = {
		
        response : function serach(text)	
        {
            changeSubAction('paging');
        }
    }

    // appending url---
	
    var pgsize = parseInt(document.getElementById("pageSize").value);
    var lastrow = parseInt(document.getElementById("lastRow").value);
    var cachepageSize = parseInt(document.getElementById('cachePageSize').value);

    var min = 0;
    var max = pgsize * cachepageSize;

    if (document.getElementById("nextOrPrevious").value == "next" )
    {
        min = pgsize*(Math.ceil(lastrow/pgsize));
        max = pgsize*(Math.ceil(lastrow/pgsize) +cachepageSize);
    }
    else if (document.getElementById("nextOrPrevious").value == "previous" )
    {
        min = pgsize*(Math.ceil(lastrow/pgsize) - (cachepageSize+1));
        max = pgsize*(Math.ceil(lastrow/pgsize) - 1);
    }
    else if (document.getElementById("nextOrPrevious").value == "go" )
    {
        min = pgsize*(Math.ceil(lastrow/pgsize));
        max = pgsize*(Math.ceil(lastrow/pgsize) +cachepageSize);
    }
	
    var url = "ajax";
    var ui = "paging";
    var lastValue = document.getElementById("lastValue").value;
    var dataType = document.getElementById("lastValueDataType").value;
    var type = document.getElementById("uiType").value;
    var params = "min=" + min + "&max=" + max + "&uiaction="+ ui + "&lastrow="+lastrow + "&uiType=" + type + "&lastValue=" +lastValue + "&dataType="+dataType;


    // Calling Ajax Servlet
    callAjax("POST",url,params,false,code.response);
}


/*Function that checks whether location value is entered or not */
function validateLocation()
{
    if (document.getElementById("officeLocationType").value != "-1" && (document.getElementById("officeLocation").value == "-1" || document.getElementById("officeLocation").value == ""))
    {
        alert("Please select a Location.");
        return false;
    }
    else
        return true;
}

//-- Function to check the keyCode for go button in paging
/*function checkKeyCode(event)
{
	if (event.keyCode == 13)
	{
		pagingNavigation('go');
	}
}
*/
function checkKeyCode(event,type)
{
    if (event.keyCode == 13)
    {
        if (type == "subStation")
            getSubStationList(true,'go');
        else if (type == "inFeeder")
            getInFeederList(true,'go');
        else if (type == "outFeeder")
            getOutFeederList(true,'go');
        else if (type == "transformer")
            getTransformerList(true,'go');
        else if (type == "consumer")
            getConsumerList(true,'go');
        else
            pagingNavigation('go');
    }
}

function suggestTerm(val, divName)
{
    if(val =='2')
    {
        if (document.getElementById(divName).style.display == 'none')
        {
            document.getElementById(divName).style.display = 'block';
        }
    }
    else
    {
        document.getElementById(divName).style.display = 'none';
    }
}

// Calls AjaxServlet and retrieves the list of substations.
// Also passes sorting and paging related parameters
function getNetworkSearchResults(isPaging,nextOrPrevious,lastRow,columnNameNew,sortOrderNew)
{
    document.getElementById("network_search_results").style.display = "block";
    var code = { 
					
        response : function setSubstationList(text)	{

            document.getElementById("horLine").style.display = '';
            //document.getElementById("scroll").innerHTML = text;
            document.getElementById("substation_list_div").innerHTML = text;
            //	setSubListDivWidth();
            checkSelected("substation_checkbox",substationMap);
																	
            if(!isPaging) {
                getInFeederList(false);
                getOutFeederList(false);
                getTransformerList(false);
                getConsumerList(false);
            }
        }
    }

    // Sorting logic
	
    var columnNameOld;
    var sortOrderOld;
    var subStationFilterId;
    //var subStationFilterIdStr = "";
	
    if (document.getElementById("columnNameSub"))
    {
        columnNameOld = document.getElementById("columnNameSub").value;
    }
    if (document.getElementById("sortOrderSub"))
    {
        sortOrderOld  = document.getElementById("sortOrderSub").value;
    }

    var url = "ajax";
    //	var params = "uiaction="+document.getElementById("uiaction").value+"&officeLocationType="+ document.getElementById("officeLocationType").value + "&officeLocation="+ document.getElementById("officeLocation").value+"&subAction=getSubstationList&voltageLevel="+document.getElementById("voltageLevel").value;
    var voltageLevel = "";

    if(document.getElementById("voltageLevel"))
        voltageLevel = document.getElementById("voltageLevel").value;
	
    var params = "uiaction="+document.getElementById("uiaction").value+"&subAction=getSubstationList&voltageLevel="+voltageLevel+"&zone="+document.getElementById("zone").value+"&circle="+document.getElementById("circle").value+"&division="+document.getElementById("division").value+"&subDivision="+document.getElementById("subDivision").value;
	
    params += "&isPaging="+isPaging;

    if (nextOrPrevious)
    {
        if(nextOrPrevious == "go")
        {
            if(isNaN(document.searchform.pageNoSubStation[0].value))
                document.searchform.pageNoSubStation[0].value = '1';
            if(isNaN(document.searchform.pageNoSubStation[1].value))
                document.searchform.pageNoSubStation[0].value = '1';
            if(document.searchform.pageNoSubStation[0].value == '' && document.searchform.pageNoSubStation[1].value == '')
                document.searchform.pageNoSubStation[0].value = '1';
            if(document.searchform.pageNoSubStation[0].value == '' && document.searchform.pageNoSubStation[1].value != '')
                document.searchform.pageNoSubStation[0].value = document.searchform.pageNoSubStation[1].value;

            if(document.searchform.pageNoSubStation[0].value == '0' || document.searchform.pageNoSubStation[0].value == '-1')
                document.searchform.pageNoSubStation[0].value = '1';

            params += "&pageNo="+document.searchform.pageNoSubStation[0].value;
        }	
		
        params += "&nextOrPrevious="+nextOrPrevious;
    }
			
    if(lastRow)
        params += "&lastRow="+lastRow;

    if (columnNameNew)
        params += "&columnNameNew="+columnNameNew;

    if (sortOrderNew)
        params += "&sortOrderNew="+sortOrderNew;

    if (columnNameOld)
    {
        params += "&columnNameOld="+columnNameOld;
    }
    if (sortOrderOld)
    {
        params += "&sortOrderOld="+sortOrderOld;
    }
    /*	if (subStationFilterIdStr)
	{
		params += "&subStationFilterIdStr=" + subStationFilterIdStr;
	}
*/			
    //alert(params);
	
    callAjax("POST",url,params,false,code.response);
	
}


// Calls AjaxServlet and retrieves the list of inFeeders.
// Also passes sorting and paging related parameters

function getSubStationList(isPaging,nextOrPrevious,lastRow,columnNameNew,sortOrderNew)
{
	
    //alert(" Inside getSubStationList")
    document.getElementById("network_search_results").style.display = "block";
    var code = { 
					
        response : function setSubstationList(text)	{

            document.getElementById("horLine").style.display = '';
            //	document.getElementById("scroll").innerHTML = text;
            document.getElementById("substation_list_div").innerHTML = text;
            checkSelected("substation_checkbox",substationMap);
        //	changeWidth();
										
        }
    }


    // Sorting logic
	
    var columnNameOld;
    var sortOrderOld;
    var subStationFilterId;
    var subStationFilterIdStr = "";
	
    //alert("Inside sorting loging in js");

    if (document.getElementById("columnNameSub"))
    {
        columnNameOld = document.getElementById("columnNameSub").value;
    }
    if (document.getElementById("sortOrderSub"))
    {
        sortOrderOld  = document.getElementById("sortOrderSub").value;
    }

    var url = "ajax";
    var params = "uiaction="+document.getElementById("uiaction").value+"&subAction=getSubstationList&subStationFilterIdStr="+document.getElementById("subStationIdStr").value;
		
    params += "&isPaging="+isPaging;

    if (nextOrPrevious)
    {
        if(nextOrPrevious == "go")
        {
            if(isNaN(document.searchform.pageNoSubStation[0].value))
                document.searchform.pageNoSubStation[0].value = '1';
            if(isNaN(document.searchform.pageNoSubStation[1].value))
                document.searchform.pageNoSubStation[0].value = '1';
            if(document.searchform.pageNoSubStation[0].value == '' && document.searchform.pageNoSubStation[1].value == '')
                document.searchform.pageNoSubStation[0].value = '1';
            if(document.searchform.pageNoSubStation[0].value == '' && document.searchform.pageNoSubStation[1].value != '')
                document.searchform.pageNoSubStation[0].value = document.searchform.pageNoSubStation[1].value;

            if(document.searchform.pageNoSubStation[0].value == '0' || document.searchform.pageNoSubStation[0].value == '-1')
                document.searchform.pageNoSubStation[0].value = '1';

            params += "&pageNo="+document.searchform.pageNoSubStation[0].value;
        }	
		
        params += "&nextOrPrevious="+nextOrPrevious;
    }
			
    if(lastRow)
        params += "&lastRow="+lastRow;

    if (columnNameNew)
        params += "&columnNameNew="+columnNameNew;

    if (sortOrderNew)
        params += "&sortOrderNew="+sortOrderNew;

    if (columnNameOld)
    {
        params += "&columnNameOld="+columnNameOld;
    }
    if (sortOrderOld)
    {
        params += "&sortOrderOld="+sortOrderOld;
    }
    if (subStationFilterIdStr)
    {
        params += "&subStationFilterIdStr=" + subStationFilterIdStr;
    }
			
    //alert(params);
	
    callAjax("POST",url,params,false,code.response);
}





// Calls AjaxServlet and retrieves the list of inFeeders.
// Also passes sorting and paging related parameters

function getInFeederList(isPaging,nextOrPrevious,lastRow,columnNameNew,sortOrderNew)
{
    var code = { 
					
        response : function setInFeederList(text)	{
            document.getElementById("in_feeder_list_div").innerHTML = text;

            checkSelected("in_feeder_checkbox",inFeederMap);

        //if(!isPaging)
        //getOutFeederList("false");
					
        }
    }

    // Sorting logic
	
    var columnNameOld;
    var sortOrderOld;


    //var subStationStringFromHiddenField = document.getElementById("subStationIdStr").value;
    //alert("Filtered substations***"+subStationStringFromHiddenField);

    if (document.getElementById("columnNameIn"))
    {
        columnNameOld = document.getElementById("columnNameIn").value;
    }
    if (document.getElementById("sortOrderIn"))
    {
        sortOrderOld  = document.getElementById("sortOrderIn").value;
    }
	
    // Till here

    // appending url---
    var url = "ajax";
    var params = "uiaction="+document.getElementById("uiaction").value+"&subAction=getInFeederList&subStationIdStr="+document.getElementById("subStationIdStr").value;

    params += "&isPaging="+isPaging;

    if (nextOrPrevious)
    {
        if(nextOrPrevious == "go")
        {
            if(isNaN(document.searchform.pageNoInFeeder[0].value))
                document.searchform.pageNoInFeeder[0].value = '1';
            if(isNaN(document.searchform.pageNoInFeeder[1].value))
                document.searchform.pageNoInFeeder[0].value = '1';
            if(document.searchform.pageNoInFeeder[0].value == '' && document.searchform.pageNoInFeeder[1].value == '')
                document.searchform.pageNoInFeeder[0].value = '1';
            if(document.searchform.pageNoInFeeder[0].value == '' && document.searchform.pageNoInFeeder[1].value != '')
                document.searchform.pageNoInFeeder[0].value = document.searchform.pageNoInFeeder[1].value;

            if(document.searchform.pageNoInFeeder[0].value == '0' || document.searchform.pageNoInFeeder[0].value == '-1')
                document.searchform.pageNoInFeeder[0].value = '1';

            params += "&pageNo="+document.searchform.pageNoInFeeder[0].value;
        }	
		
        params += "&nextOrPrevious="+nextOrPrevious;
    }
		
    if(lastRow)
        params += "&lastRow="+lastRow;

    if (columnNameNew)
        params += "&columnNameNew="+columnNameNew;

    if (sortOrderNew)
        params += "&sortOrderNew="+sortOrderNew;

    if (columnNameOld)
    {
        params += "&columnNameOld="+columnNameOld;
    }
    if (sortOrderOld)
    {
        params += "&sortOrderOld="+sortOrderOld;
    }

    //alert(params);

    callAjax("POST",url,params,false,code.response);
}

// Calls AjaxServlet and retrieves the list of outfeeders.
// Also passes sorting and paging related parameters

function getOutFeederList(isPaging,nextOrPrevious,lastRow,columnNameNew,sortOrderNew)
{
    //alert("getOutFeederList()");
    var code = { 
					
        response : function setOutFeederList(text)	{
            document.getElementById("out_feeder_list_div").innerHTML = text;

            checkSelected("out_feeder_checkbox",outFeederMap);

        //alert("getOutFeederList()");

        //if(!isPaging)
        //getTransformerList("false");
        }
    }

    // Sorting logic
	
    var columnNameOld;
    var sortOrderOld;
    var outFeederFilterId;
    var outFeederFilterIdStr = "";

    //var subStationStringFromHiddenField = document.getElementById("subStationIdStr").value;
	
    if (document.getElementById("columnNameOut"))
    {
        columnNameOld = document.getElementById("columnNameOut").value;
    }

    if (document.getElementById("sortOrderOut"))
    {
        sortOrderOld  = document.getElementById("sortOrderOut").value;
    }

    // Till here
    // appending url---
    var url = "ajax";
    var params = "uiaction="+document.getElementById("uiaction").value+"&subAction=getOutFeederList&subStationIdStr="+document.getElementById("subStationIdStr").value;

    params += "&isPaging="+isPaging;

    if (nextOrPrevious)
    {
        if(nextOrPrevious == "go")
        {
            if(isNaN(document.searchform.pageNoOutFeeder[0].value))
                document.searchform.pageNoOutFeeder[0].value = '1';
            if(isNaN(document.searchform.pageNoOutFeeder[1].value))
                document.searchform.pageNoOutFeeder[0].value = '1';
            if(document.searchform.pageNoOutFeeder[0].value == '' && document.searchform.pageNoOutFeeder[1].value == '')
                document.searchform.pageNoOutFeeder[0].value = '1';
            if(document.searchform.pageNoOutFeeder[0].value == '' && document.searchform.pageNoOutFeeder[1].value != '')
                document.searchform.pageNoOutFeeder[0].value = document.searchform.pageNoOutFeeder[1].value;

            if(document.searchform.pageNoOutFeeder[0].value == '0' || document.searchform.pageNoOutFeeder[0].value == '-1')
                document.searchform.pageNoOutFeeder[0].value = '1';

            params += "&pageNo="+document.searchform.pageNoOutFeeder[0].value;
        }	
		
        params += "&nextOrPrevious="+nextOrPrevious;
    }
	
		
    if(lastRow)
        params += "&lastRow="+lastRow;

    if (columnNameNew)
        params += "&columnNameNew="+columnNameNew;

    if (sortOrderNew)
        params += "&sortOrderNew="+sortOrderNew;

    if (columnNameOld)
    {
        params += "&columnNameOld="+columnNameOld;
    }
    if (sortOrderOld)
    {
        params += "&sortOrderOld="+sortOrderOld;
    }

    if (outFeederFilterIdStr)
    {
        params += "&outFeederFilterIdStr=" + outFeederFilterIdStr;
    }

    //alert(params);

    callAjax("POST",url,params,false,code.response);
}

// Calls AjaxServlet and retrieves the list of transformers.
// Also passes sorting and paging related parameters
function getTransformerList(isPaging,nextOrPrevious,lastRow,columnNameNew,sortOrderNew)
{

    var code = { 
					
        response : function setTransformerList(text)	{
            document.getElementById("transformer_list_div").innerHTML = text;

            checkSelected("transformer_checkbox",transformerMap);

        //if(!isPaging)
        //	getConsumerList("false");
        }
    }

    // Sorting logic
	
    var columnNameOld;
    var sortOrderOld;

    var outFeederFilterId;
    var outFeederFilterIdStr = "";
	
    if (document.getElementById("columnNameTran"))
    {
        columnNameOld = document.getElementById("columnNameTran").value;
    //alert(columnNameOld);
    }
    if (document.getElementById("sortOrderTran"))
    {
        sortOrderOld  = document.getElementById("sortOrderTran").value;
    //alert(sortOrderOld);
    }

    // appending url---
    var url = "ajax";
    var params = "uiaction="+document.getElementById("uiaction").value+"&subAction=getTransformerList&outFeederIdStr="+document.getElementById("outFeederIdStr").value+"&inFeederIdStr="+document.getElementById("inFeederIdStr").value;
	
    params += "&isPaging="+isPaging;

    if (nextOrPrevious)
    {
		 		
        if(nextOrPrevious == "go")
        {
            if(isNaN(document.searchform.pageNoTransformer[0].value))
                document.searchform.pageNoTransformer[0].value = '1';
            if(isNaN(document.searchform.pageNoTransformer[1].value))
                document.searchform.pageNoTransformer[0].value = '1';
            if(document.searchform.pageNoTransformer[0].value == '' && document.searchform.pageNoTransformer[1].value == '')
                document.searchform.pageNoTransformer[0].value = '1';
            if(document.searchform.pageNoTransformer[0].value == '' && document.searchform.pageNoTransformer[1].value != '')
                document.searchform.pageNoTransformer[0].value = document.searchform.pageNoTransformer[1].value;

            if(document.searchform.pageNoTransformer[0].value == '0' || document.searchform.pageNoTransformer[0].value == '-1')
                document.searchform.pageNoTransformer[0].value = '1';

            params += "&pageNo="+document.searchform.pageNoTransformer[0].value;
        }	
		
        params += "&nextOrPrevious="+nextOrPrevious;
    }
			
    if(lastRow)
        params += "&lastRow="+lastRow;

    if (columnNameNew)
        params += "&columnNameNew="+columnNameNew;

    if (sortOrderNew)
        params += "&sortOrderNew="+sortOrderNew;

    if (columnNameOld)
    {
        params += "&columnNameOld="+columnNameOld;
    }
    if (sortOrderOld)
    {
        params += "&sortOrderOld="+sortOrderOld;
    }

    if (outFeederFilterIdStr)
    {
        params += "&outFeederFilterIdStr=" + outFeederFilterIdStr;
    }

    //alert(params);

    callAjax("POST",url,params,false,code.response);
}

// Calls AjaxServlet and retrieves the list of consumers.
// Also passes sorting and paging related parameters
function getConsumerList(isPaging,nextOrPrevious,lastRow,columnNameNew,sortOrderNew)
{
    //alert("Inside getConsumerList");

    var code = { 
        response : function setConsumerList(text)	{
            document.getElementById("consumer_list_div").innerHTML = text;
					

            checkSelected("consumer_checkbox",consumerMap);
        }
    }

    // Sorting logic
	
    var columnNameOld;
    var sortOrderOld;

    var transformerFilterId;
    var transformerFilterIdStr = "";
		
    if (document.getElementById("columnNameCon"))
    {
        columnNameOld = document.getElementById("columnNameCon").value;
    }

    if (document.getElementById("sortOrderCon"))
    {
        sortOrderOld  = document.getElementById("sortOrderCon").value;
    }
	
    // appending url---
    var url = "ajax";
    var params = "uiaction="+document.getElementById("uiaction").value+"&subAction=getConsumerList&transformerIdStr="+document.getElementById("transformerIdStr").value;


    params += "&isPaging="+isPaging;

    if (nextOrPrevious)
    {
        if(nextOrPrevious == "go")
        {
            if(isNaN(document.searchform.pageNoConsumer[0].value))
                document.searchform.pageNoConsumer[0].value = '1';
            if(isNaN(document.searchform.pageNoConsumer[1].value))
                document.searchform.pageNoConsumer[0].value = '1';
            if(document.searchform.pageNoConsumer[0].value == '' && document.searchform.pageNoConsumer[1].value == '')
                document.searchform.pageNoConsumer[0].value = '1';
            if(document.searchform.pageNoConsumer[0].value == '' && document.searchform.pageNoConsumer[1].value != '')
                document.searchform.pageNoConsumer[0].value = document.searchform.pageNoConsumer[1].value;

            if(document.searchform.pageNoConsumer[0].value == '0' || document.searchform.pageNoConsumer[0].value == '-1')
                document.searchform.pageNoConsumer[0].value = '1';

            params += "&pageNo="+document.searchform.pageNoConsumer[0].value;
        }	
		
        params += "&nextOrPrevious="+nextOrPrevious;
    }
			
    if(lastRow)
        params += "&lastRow="+lastRow;

    if (columnNameNew)
        params += "&columnNameNew="+columnNameNew;

    if (sortOrderNew)
        params += "&sortOrderNew="+sortOrderNew;

    if (columnNameOld)
    {
        params += "&columnNameOld="+columnNameOld;
    }
    if (sortOrderOld)
    {
        params += "&sortOrderOld="+sortOrderOld;
    }

    if (transformerFilterIdStr)
    {
        params += "&transformerFilterIdStr=" + transformerFilterIdStr;
    }

    //alert(params);
	
    callAjax("POST",url,params,false,code.response);
}

function MM_openBrWindow(theURL,winName,features) { //v2.0
    window.open(theURL,winName,features);
}

// Functions for the network module. 
function show(id)
{
    if (document.getElementById(id).style.display == 'none')
    {
        document.getElementById(id).style.display = '';
    }
}

function hide(id)
{
    document.getElementById(id).style.display = 'none';
}


// Function that collapses and expands the Substation div
function changeWidth()
{
    var iFrameDiv = document.getElementById("scroll");
    var iFrameTableDiv = document.getElementById("sub_list");
    var rightDiv = document.getElementById("right_column").style.display;
    var leftDiv = document.getElementById("left_column").style.display;	

    if (document.all)                // ---- for IE --->>
    {
        if(rightDiv == 'none' && leftDiv == 'none'){
            if(screen.width == 1024)
            {
                iFrameDiv.style.width = 945+"px";			
                iFrameTableDiv.style.width=935+"px";
            }
            else if (screen.width == 1280)
            {
                iFrameDiv.style.width = 1200+"px";
                iFrameTableDiv.style.width=1195+"px";
            }
        }
        else if((rightDiv == 'none' && leftDiv == '') || (rightDiv == '' && leftDiv == 'none')){	
            if(screen.width == 1024)
                iFrameDiv.style.width = 650+"px";
            else if (screen.width == 1280)
                iFrameDiv.style.width = 825+"px";
        }	
        else{	
            if(screen.width == 1024)
                iFrameDiv.style.width = 350+"px";
            else if (screen.width == 1280)
                iFrameDiv.style.width = 480+"px";		
        }	
    }
    else // for other browsers -----
    {
        if(rightDiv == 'none' && leftDiv == 'none'){
            if(screen.width == 1024)
            {
                iFrameDiv.style.width = 935+"px";		
                iFrameTableDiv.style.width=930+"px";
            }
            else if (screen.width == 1280)
            {
                iFrameDiv.style.width = 1200+"px";
                iFrameTableDiv.style.width=1195+"px";
            }
        }
        else if((rightDiv == 'none' && leftDiv == '') || (rightDiv == '' && leftDiv == 'none')){	
            if(screen.width == 1024)
                iFrameDiv.style.width = 635+"px";
            else if (screen.width == 1280)
                iFrameDiv.style.width = 825+"px";
        }	
        else{	
            if(screen.width == 1024)
                iFrameDiv.style.width = 320+"px";
            else if (screen.width == 1280)
                iFrameDiv.style.width = 480+"px";
        }	
    }
}


function setSubListDivWidth()
{
    var iFrameDiv = document.getElementById("scroll");
    if (document.all)
    {
        if (screen.width == 1280)
            iFrameDiv.style.width = 480+"px";
        else if(screen.width == 1024)
            iFrameDiv.style.width = 350+"px";
    }
    else
    {
        if (screen.width == 1280)
            iFrameDiv.style.width = 480+"px";
        else if(screen.width == 1024)
            iFrameDiv.style.width = 320+"px";
    }
}

//Function to open the rules popup window

function openNewWindow(url,title,param)
{
    //alert("openNewWindow");
    window.open(url,title,param);
}

// Function to process Apply Filter/Apply Rule functionality for Network module

function selectFilterRule(id)
{
    var val = document.getElementById(id).value;
    var subStationFilterId = "";
    var subStationFilterIdStr = "";
    var inFeederFilterId = "";
    var inFeederFilterIdStr = "";
    var outFeederFilterId = "";
    var outFeederFilterIdStr = "";
    var transformerFilterId = "";
    var transformerFilterIdStr = "";
    var consumerFilterId = "";
    var consumerFilterIdStr = "";
    var deviceSelected = "";
		
    //if(val == 1)
    //{
		
    if (id == "subFilterRule")
    {
        //getting values of substation ids selected for rules
        deviceSelected = "substation";
        subStationFilterIdStr = getSelectedCheckBoxStr(substationMap);
		
        if(val == 2)
        {
            if(subStationFilterIdStr == "")
            {
                alert(selSubStaFil);
                return false;
            }
            // Setting the subStationIdStr hidden field to the substation filtered values
            // as first time, during filtering the subStationIdStr hidden field is not set by the AjaxServlet.
            document.searchform.subStationIdStr.value = subStationFilterIdStr;
            //getNetworkSearchResults(false);
            getInFeederList(false);
            getOutFeederList(false);
            getTransformerList(false);
            getConsumerList(false);
        }
    /*
			if (subStationFilterIdStr.length <= 0)
			{
				alert("Apply rules is not applicable to substations");
				return false;
			}

			if (subStationFilterIdStr.length >= 0)
			{
				alert("Apply rules is not applicable to substations");
				return false;
			}
			
			// Till here
			//alert("Calling selectFilterRule for rules");
			var renderUrl = document.getElementById("renderUrl").value;
			renderUrl += "&selectedCheckBoxStr="+subStationFilterIdStr;
			// Add the subAction to renderUrl
			renderUrl += "&deviceSelected="+deviceSelected;

			//alert(renderUrl);

			openNewWindow(renderUrl,'rules','status=1,scrollbars=yes,width=800,height=430');
		*/
    }

    if(id == "inFilterRule")
    {
        deviceSelected = "feeder";
        inFeederFilterIdStr = getSelectedCheckBoxStr(inFeederMap);
        if(val == 1)
        {
            if(inFeederFilterIdStr == "")
            {
                alert(eSelInFeeders);
                return false;
            }
            var renderUrl = document.getElementById("renderUrl").value;
            renderUrl += "&selectedCheckBoxStr="+inFeederFilterIdStr;
            // Add the subAction to renderUrl
            renderUrl += "&deviceSelected="+deviceSelected;
            openNewWindow(renderUrl,'rules','scrollbars=yes,width=900,height=430');
        }
        else if(val == 2)
        {
            if(inFeederFilterIdStr == "")
            {
                alert(selInFedFil);
                return false;
            }
            document.searchform.inFeederIdStr.value = inFeederFilterIdStr;
            document.searchform.outFeederIdStr.value = getSelectedCheckBoxStr(outFeederMap);
            getTransformerList(false);
            getConsumerList(false);
        }
        else if(val == 3)
        {
            var inFeedBoxLength = "";
            if(inFeederFilterIdStr != "")
                inFeedBoxLength = inFeederFilterIdStr.split(',');
				
            if(inFeedBoxLength.length < 1)
                alert(eSelInFeedResult);
            else if(inFeedBoxLength.length > 1)
                alert(eSelOneInFeed);
            else if(inFeedBoxLength.length == 1)
            {
                document.getElementById("uiActionName").value = "postSearch";
                document.getElementById("installpointtype").value = "1";
                document.getElementById("numberid").value = inFeederFilterIdStr;
                document.searchform.submit();
            }
        }
    }
    else if (id == "outFilterRule")
    {

        deviceSelected = "feeder";
        outFeederFilterIdStr = getSelectedCheckBoxStr(outFeederMap);
        if(val == 1)
        {
            if(outFeederFilterIdStr == "")
            {
                alert(eSelOutFeeders);
                return false;
            }
            // Till here
            var renderUrl = document.getElementById("renderUrl").value;
            renderUrl += "&selectedCheckBoxStr="+outFeederFilterIdStr;
            // Add the subAction to renderUrl
            renderUrl += "&deviceSelected="+deviceSelected;

            openNewWindow(renderUrl,'rules','scrollbars=yes,width=900,height=430');
        }
        else if(val == 2)
        {
            if(outFeederFilterIdStr == "")
            {
                alert(selOutFedFil);
                return false;
            }
            // Setting the outFeederIdStr hidden field to the outfeeder filtered values
            // as first time, during filtering the outFeederIdStr hidden field is not set by the AjaxServlet.
            document.searchform.outFeederIdStr.value = outFeederFilterIdStr;
            document.searchform.inFeederIdStr.value = getSelectedCheckBoxStr(inFeederMap)
            //getOutFeederList(false);
            getTransformerList(false);
            getConsumerList(false);
        }
        else if(val == 3)
        {
            var outFeedBoxLength = "";
            if(outFeederFilterIdStr != "")
                outFeedBoxLength = outFeederFilterIdStr.split(',');

            if(outFeedBoxLength.length < 1)
                alert(eSelOutFeedResult)
            else if(outFeedBoxLength.length > 1)
                alert(eSelOneOutFeed)
            else if(outFeedBoxLength.length == 1)
            {
                document.getElementById("uiActionName").value = "postSearch";
                document.getElementById("installpointtype").value = "1";
                document.getElementById("numberid").value = outFeederFilterIdStr;
                document.searchform.submit();
            }
        }
    }
    else if (id == "tranformerFilterRule")
    {
        deviceSelected = "transformer";
        transformerFilterIdStr = getSelectedCheckBoxStr(transformerMap);
        if(val == 1)
        {
            if(transformerFilterIdStr == "")
            {
                alert(eSelTrans);
                return false;
            }
            // Till here
            var renderUrl = document.getElementById("renderUrl").value;
            renderUrl += "&selectedCheckBoxStr="+transformerFilterIdStr;
            // Add the subAction to renderUrl
            renderUrl += "&deviceSelected="+deviceSelected;
            openNewWindow(renderUrl,'rules','scrollbars=yes,width=900,height=430');
        }
        else if(val == 2)
        {
            if(transformerFilterIdStr == "")
            {
                alert(selTransFil);
                return false;
            }
            // Setting the transformerIdStr hidden field to the transformer filtered values
            // as first time, during filtering the transformerIdStr hidden field is not set by the AjaxServlet.
            document.searchform.transformerIdStr.value = transformerFilterIdStr;
            getConsumerList(false);
        }
        else if(val == 3)
        {
            var transBoxLength = "";
            if(transformerFilterIdStr != "")
                transBoxLength = transformerFilterIdStr.split(',');

            if(transBoxLength.length < 1)
                alert(eSelTransResult);
            else if(transBoxLength.length > 1)
                alert(eSelOneTrans);
            else if(transBoxLength.length == 1)
            {
                document.getElementById("uiActionName").value = "postSearch";
                document.getElementById("installpointtype").value = "2";
                document.getElementById("numberid").value = transformerFilterIdStr;
                document.searchform.submit();
            }
        }
    }
    else if(id == "consumerFilterRule")
    {
        deviceSelected = "consumer";
        consumerFilterIdStr = getSelectedCheckBoxStr(consumerMap);
        if(val == 1)
        {
            if(consumerFilterIdStr == "")
            {
                alert(eSelCon);
                return false;
            }
            // Till here
            var renderUrl = document.getElementById("renderUrl").value;
            renderUrl += "&selectedCheckBoxStr="+consumerFilterIdStr;
            // Add the subAction to renderUrl
            renderUrl += "&deviceSelected="+deviceSelected;
            openNewWindow(renderUrl,'rules','scrollbars=yes,width=900,height=430');
        }
        else if(val == 3)
        {
            var consumerBoxLength = "";
            if(consumerFilterIdStr != "")
                consumerBoxLength = consumerFilterIdStr.split(',');

            if(consumerBoxLength.length < 1)
                alert(eSelConResult);
            else if(consumerBoxLength.length > 1)
                alert(eSelOneCon);
            else if(consumerBoxLength.length == 1)
            {
                document.getElementById("uiActionName").value = "postSearch";
                document.getElementById("installpointtype").value = "3";
                document.getElementById("numberid").value = consumerFilterIdStr;
                document.searchform.submit();
            }

        }
    }
//	}
/*
	else if(val == 2)
	{
		if(id == "subFilterRule")
		{
			deviceSelected = "substation";
			subStationFilterIdStr = getSelectedCheckBoxStr(substationMap);
			if(subStationFilterIdStr == "")
			{
				alert(selSubStaFil);
				return false;
			}
			
			// Setting the subStationIdStr hidden field to the substation filtered values
			// as first time, during filtering the subStationIdStr hidden field is not set by the AjaxServlet.
			document.searchform.subStationIdStr.value = subStationFilterIdStr;
			//getNetworkSearchResults(false);
			getInFeederList(false);
			getOutFeederList(false);
			getTransformerList(false);
			getConsumerList(false);
		} 
		else if (id == "inFilterRule")
		{
			deviceSelected = "infeeder";
			inFeederFilterIdStr = getSelectedCheckBoxStr(inFeederMap);
			if(inFeederFilterIdStr == "")
			{
				alert(selInFedFil);
				return false;
			}

			document.searchform.inFeederIdStr.value = inFeederFilterIdStr;
			document.searchform.outFeederIdStr.value = getSelectedCheckBoxStr(outFeederMap);
					
			//alert("Before executing in inFilterRule");
			//getInFeederList(false);
			getTransformerList(false);
			getConsumerList(false);
		} 
		else if (id == "outFilterRule")
		{
			deviceSelected = "outfeeder";
			outFeederFilterIdStr = 	getSelectedCheckBoxStr(outFeederMap);
			if(outFeederFilterIdStr == "")
			{
				alert(selOutFedFil);
				return false;
			}

			// Setting the outFeederIdStr hidden field to the outfeeder filtered values
			// as first time, during filtering the outFeederIdStr hidden field is not set by the AjaxServlet.
			document.searchform.outFeederIdStr.value = outFeederFilterIdStr;
			document.searchform.inFeederIdStr.value = getSelectedCheckBoxStr(inFeederMap)
			//getOutFeederList(false);
			getTransformerList(false);
			getConsumerList(false);
		}
		else if (id == "tranformerFilterRule")
		{
			deviceSelected = "transformer";
			transformerFilterIdStr = getSelectedCheckBoxStr(transformerMap);
			if(transformerFilterIdStr == "")
			{
				alert(selTransFil);
				return false;
			}

			// Setting the transformerIdStr hidden field to the transformer filtered values
			// as first time, during filtering the transformerIdStr hidden field is not set by the AjaxServlet.
			document.searchform.transformerIdStr.value = transformerFilterIdStr;
			getConsumerList(false);
		}
		else if (id == "consumerFilterRule")
		{
			alert("Filter is not applicabale to  Consumers");
			return false;
		} 
	} */
}

function ToggleSwap(divId, imageId){	
    var divObject = document.getElementById(divId);		
    var imageObject = document.getElementById(imageId);	
    if(divObject.style.display == "block" || divObject.style.display == ""){
        divObject.style.display = "none";
    }else{		
        divObject.style.display = "";	
    }		
    if(imageObject.src.indexOf("icon_moveup.gif")>0)
    {		
        imageObject.src = imageObject.src.replace("icon_moveup.gif", "icon_movedn.gif");
    }else
    {	
        imageObject.src = imageObject.src.replace("icon_movedn.gif", "icon_moveup.gif");
    }	
}


// Function to get comma seperated device ids of selected devices, for filtering and applying rules in Network Module

function getSelectedCheckBoxStr(map)
{
    //alert("Inside getSelectedCheckBoxStr");
    var filterId;
    var filterIdStr = "";

    var filterIdArr = new Array();
    filterIdArr = map.keySet();

    for (i =0; i<filterIdArr.length ; i++ )
    {
        filterId = filterIdArr[i];
        if (filterIdStr == "")
        {
            filterIdStr = filterId;
        }
        else
        {
            filterIdStr +=  ", " + filterId;
        }
    }
		
    //alert(subStationFilterIdStr);
    return filterIdStr;
}

// Function to get comma seperated device ids of selected rules in Rule Module

function getSelectedRuleStr(checkBoxName)
{
    var filterId;
    var filterIdStr = "";
    if (document.getElementsByName(checkBoxName))
    {
        deviceSelected = "infeeder";
        filterId = document.getElementsByName(checkBoxName);
        for (var i = 0;i< filterId.length; i++)
        {
            if (filterId[i].checked)
            {
                //alert("Checked()");
				
                if (filterIdStr == "")
                {
                    filterIdStr = filterId[i].value;
                }
                else
                {
                    filterIdStr +=  ", " + filterId[i].value;
                }
            }
        }
    //alert(subStationFilterIdStr);
    }
    return filterIdStr;
}



// Function to check/uncheck all checkboxes 

function checkUnchekAll(checked,name,checkBoxName)
{
    var i = 0;
    var obj = document.getElementsByName(name);
    if(checked==true)
    {
        for (i = 0 ; i<obj.length ; i++ )
        {
            obj[i].checked = true;
            addSelectedIdToMap(checkBoxName,obj[i])
        }
    }
    else
    {
        for (var i = 0 ; i<obj.length ; i++ )
        {
            obj[i].checked = false;
            addSelectedIdToMap(checkBoxName,obj[i])
        }
    }
}

//function to move the data from one list box to other list box
function move(fbox, tbox) 
{
    var arrFbox = new Array();
    var arrTbox = new Array();
    var arrLookup = new Array();
    var i;
    for (i = 0; i < tbox.options.length; i++) 
    {
        arrLookup[tbox.options[i].text] = tbox.options[i].value;
        arrTbox[i] = tbox.options[i].text;
    }
	
    var fLength = 0;
    var tLength = arrTbox.length;
    for(i = 0; i < fbox.options.length; i++) 
    {
        arrLookup[fbox.options[i].text] = fbox.options[i].value;
         
        if (fbox.options[i].selected && fbox.options[i].value != "") 
        {
            arrTbox[tLength] = fbox.options[i].text;
            tLength++;
        }
        else
        {
            arrFbox[fLength] = fbox.options[i].text;
            fLength++;
        }
    }
	
    arrFbox.sort();
    arrTbox.sort();
    fbox.length = 0;
    tbox.length = 0;
    var c;
    for(c = 0; c < arrFbox.length; c++) 
    {
        var no = new Option();
        no.value = arrLookup[arrFbox[c]];
        no.text = arrFbox[c];
        fbox[c] = no;
    }
	
    for(c = 0; c < arrTbox.length; c++) 
    {
        var no = new Option();
        no.value = arrLookup[arrTbox[c]];
        no.text = arrTbox[c];
        tbox[c] = no;
    }
}

function move1(fbox,tbox, ebox)
{
    var arrFbox = new Array();
    var arrTbox = new Array();
    var arrLookup = new Array();
    var i;
    for (i = 0; i < tbox.options.length; i++)
    {
        arrLookup[tbox.options[i].text] = tbox.options[i].value;
        arrTbox[i] = tbox.options[i].text;
    }
       
    var fLength = 0;
    var tLength = arrTbox.length;
       
    for(i = 0; i < fbox.options.length; i++)
    {
        arrLookup[fbox.options[i].text] = fbox.options[i].value;

        if (fbox.options[i].selected && fbox.options[i].value != "")
        {
            arrTbox[tLength] = fbox.options[i].text;
            tLength++;
        }
        else
        {
            arrFbox[fLength] = fbox.options[i].text;
            fLength++;
        }
    }
	
    arrFbox.sort();
    arrTbox.sort();
    fbox.length = 0;
    tbox.length = 0;
       
    var c;
    for(c = 0; c < arrFbox.length; c++)
    {
        var no = new Option();
        no.value = arrLookup[arrFbox[c]];
        no.text = arrFbox[c];
        fbox[c] = no;
    }

    for(c = 0; c < arrTbox.length; c++)
    {
        var no = new Option();
        no.value = arrLookup[arrTbox[c]];
        no.text = arrTbox[c];
        tbox[c] = no;
    }
    for(c = 0; c < arrTbox.length; c++)
    {
        var no = new Option();
        no.value = arrLookup[arrTbox[c]];
        no.text = arrTbox[c];
        ebox[c] = no;
    }
   
}


function convertListBoxToString(listBoxObj)
{
    var str = "";
    for(var i=0; i<listBoxObj.length; i++)
    {
        if(str != "")
        {
            str += ",";
        }
        str += listBoxObj.options[i].value;
    }
    return str;
}

function isEmpty(s)
{   
    return ((s == null) || (s.length == 0))
}

function isDigit (c)
{
    return ((c >= "0") && (c <= "9"))
}

function isInteger (s)
{   
    var i;
    if (isEmpty(s))
        if (isInteger.arguments.length == 1) 
            return false;
        else 
            return (isInteger.arguments[1] == true);

    for (i = 0; i < s.length; i++){
        var c = s.charAt(i);
        if (!isDigit(c)) 
            return false;
    }    
    return true;
}

// remove whitespace from both sides of the string
function trim(stringToTrim) 
{
    return stringToTrim.replace(/^\s+|\s+$/g,"");
}
	
// Function to maintain persistance of checkboxes selected in NetworkModule

function addSelectedIdToMap(checkBoxName,object)
{
    var substationId;
    var inFeederId;
    var outFeederId;
    var transformerId;
    var consumerId;
    var checked;

    if (checkBoxName == "substation")
    {
        checked = object.checked;
        substationId = object.value;

        if (checked == true)
        {
            //alert("Sub is checked");
            substationMap.put(substationId,true);
        }
        else
        {
            //alert("Sub is unchecked");
            substationMap.remove(substationId);
        }

        var result = substationMap.showMe();
			
    }

    if (checkBoxName == "in_feeder")
    {
        checked = object.checked;
        inFeederId = object.value;

        if (checked == true)
        {
            //alert("Feeder is checked");
            inFeederMap.put(inFeederId,true);
        }
        else
        {
            //alert("Feeder is unchecked");
            inFeederMap.remove(inFeederId);
        }

        var result = inFeederMap.showMe();
    //alert("the inFeederids selected***"+ result);
    }

    if (checkBoxName == "out_feeder")
    {
        checked = object.checked;
        outFeederId = object.value;

        if (checked == true)
        {
            //alert("Feeder is checked");
            outFeederMap.put(outFeederId,true);
        }
        else
        {
            //alert("Feeder is unchecked");
            outFeederMap.remove(outFeederId);
        }

        var result = outFeederMap.showMe();
    //alert("the outFeederIds selected***"+ result);
    }

    if (checkBoxName == "transformer")
    {
        checked = object.checked;
        transformerId = object.value;

        if (checked == true)
        {
            //alert("Transformer is checked");
            transformerMap.put(transformerId,true);
        }
        else
        {
            //alert("Transformer is unchecked");
            transformerMap.remove(transformerId);
        }

        var result = transformerMap.showMe();
    //alert("the transformers selected***"+ result);
    }

    if (checkBoxName == "consumer")
    {
        checked = object.checked;
        consumerId = object.value;

        if (checked == true)
        {
            //alert("consumer is checked");
            consumerMap.put(consumerId,true);
        }
        else
        {
            //alert("consumer is unchecked");
            consumerMap.remove(consumerId);
        }

        var result = consumerMap.showMe();
    //alert("the consumers selected***"+ result);
    }
	
			
}

// function to populating the location dropdown--
function getLocationDropDown(value)
{
    var str;
    var optionStr;
    if (value != -1)
    {
        optionStr = locationArray[value];
        str = '<SELECT class=textfield id="officeLocation" name="officeLocation">'+optionStr+'</SELECT>';
    }
    else
    {
        str = '<SELECT class=textfield id="officeLocation" name="officeLocation"><OPTION selected value = "-1">- Select -</OPTION></SELECT>';
    }
    document.getElementById("locationlist").innerHTML = str;
}

// Removes initial (leading) whitespace characters from s.
// Global variable whitespace (see above)
// defines which characters are considered whitespace.

function stripInitialWhitespace(s)
{
    var i = 0;
    while ((i < s.length) && charInString (s.charAt(i), whitespace))
        i++;
		
    return s.substring (i, s.length);
}

function charInString (c, s){   
    for (i = 0; i < s.length; i++){   
        if (s.charAt(i) == c) 
            return true;
    }
    return false
}


// Logic to keep check boxes selected based on the value in inFeeder Map
function checkSelected(checkBoxName,map)
{
		
    if (checkBoxName == "substation_checkbox")
    {
        var subStationIdInPage = document.getElementsByName(checkBoxName);
		
        for (var i = 0;i< subStationIdInPage.length; i++)
        {
            var result = substationMap.findIt(subStationIdInPage[i].value);
            if (result != -1)
            {
                subStationIdInPage[i].checked = true;
            }

        }
    }

    if (checkBoxName == "in_feeder_checkbox")
    {
        var inFeederIdInPage = document.getElementsByName(checkBoxName);
			
        for (var i = 0;i< inFeederIdInPage.length; i++)
        {
            var result = inFeederMap.findIt(inFeederIdInPage[i].value);
            if (result != -1)
            {
                inFeederIdInPage[i].checked = true;
            }

        }
    }

    if (checkBoxName == "out_feeder_checkbox")
    {
        var outFeederIdInPage = document.getElementsByName(checkBoxName);
			
        for (var i = 0;i< outFeederIdInPage.length; i++)
        {
            var result = outFeederMap.findIt(outFeederIdInPage[i].value);
            if (result != -1)
            {
                outFeederIdInPage[i].checked = true;
            }

        }
    }

    if (checkBoxName == "transformer_checkbox")
    {
        var transformerIdInPage = document.getElementsByName(checkBoxName);
			
        for (var i = 0;i< transformerIdInPage.length; i++)
        {
            var result = transformerMap.findIt(transformerIdInPage[i].value);
            if (result != -1)
            {
                transformerIdInPage[i].checked = true;
            }

        }
    }

    if (checkBoxName == "consumer_checkbox")
    {
        var consumerIdInPage = document.getElementsByName(checkBoxName);
			
        for (var i = 0;i< consumerIdInPage.length; i++)
        {
            var result = consumerMap.findIt(consumerIdInPage[i].value);
            if (result != -1)
            {
                consumerIdInPage[i].checked = true;
            }

        }
    }
		
// Till here

}  // Ends checkSelected

function isAlphanumericUP (s)
{
    var i;
    for (i = 0; i < s.length; i++)
    {
        var c = s.charAt(i);
        if (! ( isLetter(c) || isDigit(c) || c=='_' ) )
            return false;
    }
    return true;
}

function isLetter (c)
{
    return ( ((c >= "a") && (c <= "z")) || ((c >= "A") && (c <= "Z")) )
}

function compareFromToDates(fromDate,toDate)
{	
    var submitValue = false;
    if(fromDate != null && toDate != null)
    {     
        if(fromDate <= toDate)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    else
    {
        return true;
    }
    return submitValue;		
}

function getDateObject(dateString,dateSeperator)
{
    //This function return a date object after accepting 
    //a date string ans dateseparator as arguments
    var curValue=dateString;
    var sepChar=dateSeperator;
    var curPos=0;
    var cDate,cMonth,cYear;
    var dtObject = null;

    //extract day portion
    curPos=dateString.indexOf(sepChar);
    cDate=dateString.substring(0,curPos);

    //extract month portion	
    endPos=dateString.indexOf(sepChar,curPos+1);	 
    cMonth=dateString.substring(curPos+1,endPos);
		
    //extract year portion	
    curPos=endPos;
    endPos=curPos+5;	
    cYear=curValue.substring(curPos+1,endPos);

    var datePattern = cMonth+" "+cDate+","+cYear;
    //Create Date Object
    dtObject = new Date(datePattern);	
    return dtObject;
}


// function to get the location Dropdowns from ajax
function getSubLocations(type,parentSelValue,isLoad)
{

    var selectedValue = -1;
    var flag=0;

    var out = {

        response : function getLocationOptionString(text)
        {
            var jsonObj = eval("(" + text + ")");
            var optStr =jsonObj.responseString;
            //  alert(text);
            popuateOptionString(type,optStr);

            if(isLoad)
            {
                if(type == 'circle')
                    getSubLocations('division',selectedValue,true);
                else if(type == 'division')
                    getSubLocations('subDivision',selectedValue,true);
                else if(type == 'subDivision')
                    getSubLocations('substation',selectedValue,true);
                else if(type =='substation')
                    getSubLocations('feeder',selectedValue,true);
                else if(type =='feeder')
                    getSubLocations('dtc',selectedValue,true);
                else if(type =='dtc')
                    getSubLocations('consumer',selectedValue,true);
                else if(type =='project')
                    getSubLocations('town',selectedValue,true);

            }

        }
    }

    if(isLoad)
    {
        if(type == "circle")
            selectedValue =  document.getElementById("selectedCircle").value;
        else if(type == 'division')
            selectedValue =  document.getElementById("selectedDivision").value;
        else if(type == 'subDivision')
            selectedValue =  document.getElementById("selectedSubDivision").value;
        else if(type == 'substation')
            selectedValue =  document.getElementById("selectedSubstation").value;
        else if(type == 'feeder')
            selectedValue =  document.getElementById("selectedFeeder").value;
        else if(type == 'dtc')
            selectedValue =  document.getElementById("selectedDtc").value;
        else if(type == 'consumer')
            selectedValue =  document.getElementById("selectedConsumer").value;
        if(type == 'town')
            selectedValue =  document.getElementById("selectedTown").value;
        if(type == 'project')
            selectedValue =  document.getElementById("project").value;

    }

    // appending url---
    var url = "ajax";
    var ui = "getLocation";
    if (parentSelValue =="-1")
        flag=1;
    else
        flag=0;

    var params = "parentLocId=" + parentSelValue+"&flag="+flag+"&type="+type+"&selectedValue=" +selectedValue+"&uiaction="+ ui;

    if(parentSelValue != -1)
    {
        callAjax("POST",url,params,false,out.response);
    }
    else
    {
        popuateOptionString(type,"");
    }
}
function popuateOptionString(type,optStr)
{
    if(type == "circle")
    {
        str = '<SELECT class=textfield id="circle" name="circle" onchange = "getSubLocations(\'division\',this.value)"><option selected value = -1>- Select -</option>'+optStr+'</SELECT>';
        document.getElementById("circleDiv").innerHTML = str;
					

    }
    else if(type == 'division')
    {
        str = '<SELECT class=textfield id="division" name="division" onchange = "getSubLocations(\'subDivision\',this.value)"><option selected value = -1>- Select -</option>'+optStr+'</SELECT>';
        document.getElementById("divisionDiv").innerHTML = str;
				

    }
    else if(type == 'subDivision')
    {
        str = '<SELECT class=textfield id="subDivision" name="subDivision" onchange="getSubLocations(\'substation\',this.value)"><option selected value = -1>- Select -</option>'+optStr+'</SELECT>';
        document.getElementById("subDivisionDiv").innerHTML = str;
                            
    }
    else if(type =='substation')
    {
        str = '<SELECT class=textfield id="substation" name="substation"  onchange="getSubLocations(\'feeder\',this.value)"><option selected value = -1>- Select -</option>'+optStr+'</SELECT>';
        document.getElementById("substationDiv").innerHTML = str;
                            
    }
    else if(type == 'feeder')
    {
        str = '<SELECT class=textfield id="feeder" name="feeder" onchange="getSubLocations(\'dtc\',this.value)"><option selected value = -1>- Select -</option>'+optStr+'</SELECT>';
        document.getElementById("feederDiv").innerHTML = str;
                              
    }
    else if(type=='dtc')
    {
        str = '<SELECT class=textfield id="dtc" name="dtc" onchange="getSubLocations(\'consumer\',this.value)"><option selected value = -1>- Select -</option>'+optStr+'</SELECT>';
        document.getElementById("dtcDiv").innerHTML = str;
                              
    }
    else if(type=='consumer')
    {
        str = '<SELECT class=textfield id="consumer" name="consumer">'+optStr+'</SELECT>';
        document.getElementById("consumerDiv").innerHTML = str;
    }
    else if(type=='town')
    {
        str = '<SELECT class=textfield id="town" name="town" ><option selected value = -1>- Select -</option>'+optStr+'</SELECT>';
        //alert(str);
        document.getElementById("townDiv").innerHTML = str;
    // alert(optStr);
    }

}

function getSubLocations1(type,parentSelValue,isLoad,selectedVal)
{
    //if(type=='dtc')
    //  getSubLocations1('consumer',parentSelValue);

    var selectedValue = "";
    if(selectedVal == "")
        selectedValue = -1;
    else
        selectedValue = selectedVal;
    var flag=0;

    var out = {

        response : function getLocationOptionString(text)
        {
            var jsonObj = eval("(" + text + ")");
            var optStr =jsonObj.responseString;
            var optStr1 =jsonObj.responseString1;
            //  alert("optStr :"+optStr);
            //  alert("optStr1:"+optStr1);
            ///  alert("text: "+text);
            popuateOptionString1(type,optStr,optStr1);

            if(isLoad)
            {
                if(type == 'circle')
                    getSubLocations1('division',selectedValue,true);
                else if(type == 'division')
                    getSubLocations1('subDivision',selectedValue,true);
                else if(type == 'subDivision')
                    getSubLocations1('substation',selectedValue,true);
                else if(type =='substation')
                    getSubLocations1('feeder',selectedValue,true);
                else if(type =='feeder'){
                    getSubLocations1('dtc',selectedValue,true);
                    getSubLocations1('consumer',selectedValue,true);
                }
                else if(type =='dtc')
                    getSubLocations1('consumer',selectedValue,true);
                else if(type =='town')
                    getSubLocations1('town',selectedValue,true);

            }

        }
    }

    if(isLoad)
    {
      
        if(type == 'town')
            selectedValue =  document.getElementById("selectedTown").value;

    }

    // appending url---
    var url = "ajax";
    var ui = "getLocation";
    if (parentSelValue =="-1")
        flag=1;
    else
        flag=0;

    var params = "parentLocId=" + parentSelValue+"&flag="+flag+"&type="+type+"&selectedValue=" +selectedValue+"&uiaction="+ ui;

    if(parentSelValue != -1)
    {
        callAjax("POST",url,params,false,out.response);
    }
    else
    {
        popuateOptionString1(type,"","");
    }
}
function popuateOptionString1(type,optStr,optStr1)
{
    if(type == "circle")
    {
        str = '<SELECT class=textfield id="circle" name="circle" onchange = "getSubLocations1(\'division\',this.value)" style="width:300px"><option selected value = -1>- Select -</option>'+optStr+'</SELECT>';
        document.getElementById("circleDiv").innerHTML = str;


    }
    else if(type == 'division')
    {
        str = '<SELECT class=textfield id="division" name="division" onchange = "getSubLocations1(\'subDivision\',this.value)" style="width:300px"><option selected value = -1>- Select -</option>'+optStr+'</SELECT>';
        document.getElementById("divisionDiv").innerHTML = str;


    }
    else if(type == 'subDivision')
    {
        str = '<SELECT class=textfield id="subDivision" name="subDivision" onchange="getSubLocations1(\'substation\',this.value)" style="width:300px"><option selected value = -1>- Select -</option>'+optStr+'</SELECT>';
        document.getElementById("subDivisionDiv").innerHTML = str;

    }
    else if(type =='substation')
    {
        str = '<SELECT class=textfield id="substation" name="substation"  onchange="getSubLocations1(\'feeder\',this.value)" style="width:300px"><option selected value = -1>- Select -</option>'+optStr+'</SELECT>';
        document.getElementById("substationDiv").innerHTML = str;

    }
    else if(type == 'feeder')
    {
        str = '<SELECT class=textfield id="feeder" name="feeder" onchange="getSubLocations1(\'dtc\',this.value)" style="width:300px"><option selected value = -1>- Select -</option>'+optStr+'</SELECT>';
        document.getElementById("feederDiv").innerHTML = str;

    }
    else if(type=='dtc')
    {
        str = '<SELECT class=textfield id="dtc" name="dtc" onchange="getSubLocations1(\'consumer\',this.value)" style="width:300px"><option selected value = -1>- Select -</option>'+optStr+'</SELECT>';
        document.getElementById("dtcDiv").innerHTML = str;
        str = '<SELECT class=textfield id="consumer" name="consumer" style="width:300px"><option selected value = -1>- Select -</option>'+optStr1+'</SELECT>';
        document.getElementById("consumerDiv").innerHTML = str;

    }
    else if(type=='consumer')
    {
        str = '<SELECT class=textfield id="consumer" name="consumer" style="width:300px">'+optStr+'</SELECT>';
        document.getElementById("consumerDiv").innerHTML = str;
    // alter(optStr);
    }
    else if(type=='town')
    {
        str = '<SELECT class=textfield id="town" name="town" style="width:150px"><option selected value = -1>- Select -</option>'+optStr+'</SELECT>';
        //alert(str);
        document.getElementById("townDiv").innerHTML = str;
    // alert(optStr);
    }


}


function getSubLocations2(type,parentSelValue,isLoad)
{

    var selectedValue = -1;
    var flag=0;

    var out = {

        response : function getLocationOptionString(text)
        {
            var jsonObj = eval("(" + text + ")");
            var optStr =jsonObj.responseString;
            //  alert(text);
            popuateOptionString2(type,optStr);

            if(isLoad)
            {
                if(type == 'circle')
                    getSubLocations2('division',selectedValue,true);
                else if(type == 'division')
                    getSubLocations2('subDivision',selectedValue,true);
                else if(type == 'subDivision')
                    getSubLocations2('substation',selectedValue,true);
                else if(type =='substation')
                    getSubLocations2('feeder',selectedValue,true);
                else if(type =='feeder')
                    getSubLocations2('dtc',selectedValue,true);
                else if(type =='dtc')
                    getSubLocations2('consumer',selectedValue,true);

            }

        }
    }

    if(isLoad)
    {
        if(type == "circle")
            selectedValue =  document.getElementById("selectedCircle").value;
        else if(type == 'division')
            selectedValue =  document.getElementById("selectedDivision").value;
        else if(type == 'subDivision')
            selectedValue =  document.getElementById("selectedSubDivision").value;
        else if(type == 'substation')
            selectedValue =  document.getElementById("selectedSubstation").value;
        else if(type == 'feeder')
            selectedValue =  document.getElementById("selectedFeeder").value;
        else if(type == 'dtc')
            selectedValue =  document.getElementById("selectedDtc").value;
        else if(type == 'consumer')
            selectedValue =  document.getElementById("selectedConsumer").value;

    }

    // appending url---
    var url = "ajax";
    var ui = "getLocation";
    if (parentSelValue =="-1")
        flag=1;
    else
        flag=0;

    var params = "parentLocId=" + parentSelValue+"&flag="+flag+"&type="+type+"&selectedValue=" +selectedValue+"&uiaction="+ ui;

    if(parentSelValue != -1)
    {
        callAjax("POST",url,params,false,out.response);
    }
    else
    {
        popuateOptionString2(type,"");
    }
}
function popuateOptionString2(type,optStr)
{
    if(type == "circle")
    {
        str = '<SELECT class=textfield id="circle" name="circle" onchange = "getSubLocations2(\'division\',this.value)"><option selected value = -1>- Select -</option>'+optStr+'</SELECT>';
        document.getElementById("circleDiv").innerHTML = str;
					

    }
    else if(type == 'division')
    {
        str = '<SELECT class=textfield id="division" name="division" onchange = "getSubLocations2(\'subDivision\',this.value)"><option selected value = -1>- Select -</option>'+optStr+'</SELECT>';
        document.getElementById("divisionDiv").innerHTML = str;
				

    }
    else if(type == 'subDivision')
    {
        str = '<SELECT class=textfield id="subDivision" name="subDivision" onchange="getSubLocations2(\'substation\',this.value)"><option selected value = -1>- Select -</option>'+optStr+'</SELECT>';
        document.getElementById("subDivisionDiv").innerHTML = str;
                            
    }
    else if(type =='substation')
    {
        str = '<SELECT class=textfield id="substation" name="substation"  onchange="getSubLocations2(\'feeder\',this.value)"><option selected value = -1>- Select -</option>'+optStr+'</SELECT>';
        document.getElementById("substationDiv").innerHTML = str;
                            
    }
    else if(type == 'feeder')
    {
        str = '<SELECT class=textfield id="feeder" name="feeder" onchange="getSubLocations2(\'dtc\',this.value)"><option selected value = -1>- Select -</option>'+optStr+'</SELECT>';
        document.getElementById("feederDiv").innerHTML = str;
                              
    }
    else if(type=='dtc')
    {
        str = '<SELECT class=textfield id="dtc" name="dtc" onchange="getSubLocations2(\'consumer\',this.value)"><option selected value = -1>- Select -</option>'+optStr+'</SELECT>';
        document.getElementById("dtcDiv").innerHTML = str;
                              
    }
//  else if(type=='consumer')
//      {
//           str = '<SELECT class=textfield id="consumer" name="consumer">'+optStr+'</SELECT>';
//             document.getElementById("consumerDiv").innerHTML = str;
//      }
                

}



function getInprogressDevices()
{
    var code = { 
						
        response : function deviceInfo(text)	{
            document.getElementById("devicecontent").innerHTML = text;
        }
    }
    var url = "ajax";
    var params = "uiaction=getInprogressDevices";
    callAjax("POST",url,params,false,code.response);
} 
function getDeviceRules()
{
    var code = { 
						
        response : function ruleInfo(text)	{
            document.getElementById("rulecontent").innerHTML = text;
        }
    }
    var url = "ajax";
    var params = "uiaction=getDeviceRules";
    callAjax("POST",url,params,false,code.response);
} 
function getSystemAlerts()
{
    var code = { 
						
        response : function alertInfo(text)	{
            document.getElementById("systemalertcontent").innerHTML = text;
        }
    }
    var url = "ajax";
    var params = "uiaction=getSystemAlerts";
    callAjax("POST",url,params,false,code.response);
} 

var winOpened = false ; 
var a;
function openWinPop(popupurl)
{ 
    var url = popupurl+"&popup=true'";
    a = window.open(url,'2001','scrollbars=yes,width=800,height=390,resizable=No');
    winOpened = true;
}
function getArea(object,checkAreaStr,selectedLocation,isLoad)
{
    if(!isLoad)
    {
        var available="<select multiple size='5' name='availableArea' style='background-color:#F6FAFF;width:175px;'></select>";
        document.getElementById("availableAreaDiv").innerHTML = available;
    }
   
    var out = {

        response : function (res)
        {
            if(isLoad)
            {
                //alert(res);
                var inner="<select class=textfield id='selectedLocation' name='selectedLocation'>"+res+"</select>"
                document.getElementById("availableAreaDiv").innerHTML = inner;
            }
            else
            {
                var inner = "<select multiple size='5' name='availableArea' style='background-color:#F6FAFF;width:250px;'>"+res+"</select>";
                document.getElementById("availableAreaDiv").innerHTML = inner;
            }
        }
    };



    // appending url---
    var url = "ajax";
    var ui = "getAjaxAreaList";
    if (object =="-1")
        selectedLocation="-1";
 
    
    var params = "area_code=" + escape(object)+"&checkAreaStr="+checkAreaStr+"&selectedLocation="+selectedLocation+"&uiaction="+ ui;

    callAjax("POST",url,params,false,out.response);
}

function getReadDate(object)
{


    var out = {

        response : function (res)
        {
                        
        {
                var inner="<select class=textfield id='readDate' name='readDate'>"+res+"</select>"
                document.getElementById("FromDate").innerHTML = inner;
                document.getElementById("FromDate1").innerHTML = inner;
            }


        }
    };



    // appending url---
    var url = "ajax";
    var ui = "getAjaxDateList";

    var params = "auth_code=" + escape(object)+"&uiaction="+ ui;

    callAjax("POST",url,params,false,out.response);
}



function autofitIframe(id,MoreFlag){
    ///function autofitIframe(id){
    var margine = 10;
    ///alert(MoreFlag);

    if(!MoreFlag)
    {
        if(parent.document.getElementById(id))
        {

            //		 if (!window.opera && !document.mimeType && document.all && document.getElementById){
            if (!window.opera && document.all && document.getElementById){
                parent.document.getElementById(id).style.height=(this.document.body.offsetHeight - margine) +"px";
            }
            else if(document.getElementById) {
                parent.document.getElementById(id).style.height=(this.document.body.scrollHeight  - margine) +"px"
            }
        }
    }
        
}
function loadSelectedXmlFile(fileId)
{
    var code = {

        response : function deleteRecords(res)
        {
            if(res == "")
            {
                document.getElementById("td"+fileId).innerHTML = "Unable to Load File Java Script Error";
            }
            else{
                var jsonObj = eval("(" + res + ")");
                var fileLoadId = jsonObj.fileId;
                //alert(fileLoadId);
                var result = jsonObj.result;
                var url = "'mda?uiActionName=getLoadFileErrors&fileId="+fileLoadId+"','1018','status=yes,scrollbars=yes,width=950,height=700,top=100,left=100'";
                document.getElementById("td"+fileId).innerHTML = "<a href =javascript:openWinPopup("+url+") >"+result+" </a>";
            }
                            
        }
    }
    var url = "ajax";
    var ui = "ajaxLoadXmlFiles";
    params =  "uiaction="+ui+ "&fileNo="+fileId;

    // Calling Ajax Servlet
    callAjax("POST",url,params,false,code.response);

}


function loadFile()
{
        
    var obj = document.loadForm.chkbox;
    var operationValue = "";
    if(!isNaN(obj.length))
    {

        //alert(obj.length);
        for(x=0;x<obj.length;x++){
            if(obj[x].checked){
                // if (operationValue == ""){
                //   operationValue += obj[x].value;
                document.getElementById("td"+obj[x].value).innerHTML = "<img src='./images/grey_loader.gif' alt=''/>";
                loadSelectedXmlFile(obj[x].value);
                operationValue += obj[x].value;


            //}
            /*else{
                                operationValue += ", " + obj[x].value;
                                loadSelectedXmlFile(obj[x].value);
                           }*/
            }
        }
    }
    else{
        if(obj.checked){
            document.getElementById("td"+obj.value).innerHTML = "<img src='/image/grey_loader.gif' alt=''/>";
            loadSelectedXmlFile(obj.value);
            operationValue = obj.value;
                         
        }
        document.getElementById("td"+obj.value).innerHTML = "<img src='/image/grey_loader.gif' alt=''/>";
        loadSelectedXmlFile(obj.value);
                        
    }
    if(operationValue == "")
    {
        alert("plz Select File");
    }
                 
                     
}

function isNumber(evt)
{
    var returnVal = false;
    var charCode = (evt.which) ? evt.which : event.keyCode;
    ///alert(charCode);
    if (charCode > 31 && (charCode < 48 || charCode > 57 ))
    {
        /*if(charCode==46)     ///For . keyCode onKeyPress is 46   or onkeyDown/KeyUp code is 190
                                returnVal = true;
                        else*/
        returnVal = false;
    }
    else
        returnVal = true;
    return returnVal;
}

function getRemoteCommProtocolType(obj)
{
    var protocolID = obj.value;
    alert("here");
    var code = {

        response : function getProtocolType(protocolID)	{

            //document.getElementById("horLine").style.display = '';
            //document.getElementById("").innerHTML = val;
            //alert("protocolID::"+protocolID);
            document.getElementById("protocolType").value = protocolID;
            if(protocolID == "TCPIP")
            {
                alert(" Enter IP Address And Telephone Number. ");
                //document.getElementById("ipAddress").value="test";
                document.getElementById("ipAddress").disabled=false;
                document.getElementById("telephoneNumber").disabled=true;
            //document.getElementById("divTelephone").style.display= none;
            //document.getElementById("divIPAddress").style.display="";
            }
            else
            {
                alert(" Enter IP Address And Telephone Number. ");
            //document.getElementById("telephoneNumber").value="test";
            //document.getElementById("telephoneNumber").disabled=false;
            //document.getElementById("ipAddress").disabled=true;
            //document.getElementById("divIPAddress").style.display="none";
            //document.getElementById("divIPAddress").style.display="none";
            }

        }
    }
    var url = "ajax";
    var uiActionName = "getRemoteCommProtocolID";
    var params = "protocolID=" + escape(protocolID)+"&uiaction="+ uiActionName;
    callAjax("POST",url,params,false,code.response);

}

function getCrossoverInstallpointOption(){    
    var crossover=document.getElementById("crossoverYes").checked;
    if(crossover){
        crossover="Y";
    }
    var code = {

        response : function getInstallpointOptionStr(res)	{
            if(res == "")
            {
                document.getElementById("installPointOptionStr").innerHTML = "<select name=\"installPoint\" id = \"installPoint\" class=\"textfield_small\" onChange=\"onInstallPointChange(this);\" style=\"width:300px\">"
            +  "<option selected value = -1>- Select -</option></select>";
            }
            else{
                var jsonObj = eval("(" + res + ")");
                var installPointOptionStr = jsonObj.installPointOptionStr;               
                document.getElementById("installPointOptionStr").innerHTML = "<select name=\"installPoint\" id = \"installPoint\" class=\"textfield_small\" onChange=\"onInstallPointChange(this);\" style=\"width:300px\">"
                + installPointOptionStr + "</select>";
                hide('feeder_information');
                hide('transformer_information');
                hide('consumer_information');
                document.getElementById("profileDiv").innerHTML  = "<select name='profiles'  id='profiles' class='textfield' style='width:300px'><option>- Select -</option></select>";
            }
        }
    }
    var url = "ajax";
    var uiActionName = "getCrossoverInstallpointOption";
    var params = "crossover=" + escape(crossover)+"&uiaction="+ uiActionName;
    callAjax("POST",url,params,false,code.response);
}