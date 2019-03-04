function getNextList(type,object,isLoad)
{
    var out = {
			response : function (res)
			{
                            var inner;
                           // alert(res);
                           if(type=="substation")
                               {
                                    inner = "<select id = \"substation\" name=\"substation\" class=\"textfield_small\" style=\"width:170px\"onchange = 'getNextList(\"feeder\",this);'>" + res +"</select>";
                                    document.getElementById("substationDiv").innerHTML = inner;
                               }
				else if(type=="feeder"){

                                    inner = "<select id = \"feeder\" name=\"feeder\" class=\"textfield_small\" style=\"width:170px\"onchange = 'getNextList(\"dtc\",this);'>" + res +"</select>";
                                    document.getElementById("feederDiv").innerHTML = inner;
                                }
                                else if(type=="dtc")
                                {
                                    inner = "<select id = \"transformer\" name=\"transformer\"class=\"textfield_small\" style=\"width:170px\" onchange ='getNextList(\"consumer\",this);'>" + res +"</select>";
                                    document.getElementById("dtcDiv").innerHTML = inner;
                                }
                                else
                                {
                                    inner = "<select id = \"consumer\" name=\"consumer\"class=\"textfield_small\" style=\"width:170px\" >" + res +"</select>";
                                    document.getElementById("consumerDiv").innerHTML = inner;
                                }




			}
	};


	// appending url---
	var url = "ajax";
	var ui = "getAjaxFeederList";
	var params = "parent_code=" + escape(object.value)+"&type="+type+"&uiaction="+ ui;

	callAjax("POST",url,params,false,out.response);
}

function getSubstation()
{
    var obj = document.getElementById("subDivision");
	var out = {
			response : function (res)
			{

				var inner = "<select id = \"substation\" name=\"substation\"class=\"textfield_small\" style=\"width:170px\"onchange = 'getNextList(\"feeder\",this);'>" + res +"</select>";
				document.getElementById("substationDiv").innerHTML = inner;

			}
	};


	// appending url---
	var url = "ajax";
	var ui = "getAjaxSubstationList";
	var params = "subdivisionId=" + escape(obj.value)+"&uiaction="+ ui;

	callAjax("POST",url,params,false,out.response);

}
 