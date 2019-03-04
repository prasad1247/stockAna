

var openImg = new Image();
openImg.src = "images/icon_movedn.gif";
var closedImg = new Image();
closedImg.src = "images/icon_moveup.gif";

function trim(stringToTrim) 
{
	return stringToTrim.replace(/^\s+|\s+$/g,"");
}


function createTextBox(objName)
{
	var obj = document.getElementById(objName);
	var data = document.getElementById(obj.path).innerText;
	obj.innerHTML = "<input id='txtid' spanId ='" + obj.id + "' xPath='"+ obj.path + "' type='text' width='6px'  onblur =' changeValue(this);' class='textfield_input_big'  value='"+data+"' />";
}

function changeValue(obj)
{
	var spanObj = document.getElementById(obj.spanId);
	var node = document.getElementById(obj.xPath);
//	alert("obj.value" + obj.value );
//	alert("node.innerText " + node.innerText);
	
	xPathMap.put(obj.xPath,node.innerText + "~" + obj.value);
//	alert("hashMap value::" + xPathMap.showMe());
}

function submitForm()
{
	
	var xPathStr = "";
	var keyset = new Array();
	var valset = new Array();
	keyset = xPathMap.keySet();
	valset = xPathMap.valSet();
	for (i=0;i<keyset.length ; i++ )
	{
		xPathStr = xPathStr + keyset[i] + "=" + valset[i] + ";";
	}
	document.getElementById("xPathArr").value = xPathStr;
	
//	alert("XPATH STRING :: " + document.getElementById("xPathArr").value);
//	window.opener.document.getElementById("subAction").value = "sort";

	var xmlSubmit = document.getElementsByName("xmlSubmit");
	for (var i =0;i<xmlSubmit.length ;i++ )
	{
		xmlSubmit[i].disabled = true;	
	}
	document.xmlForm.submit();

//	window.close();
//	window.opener.searchform.submit();
}

function showNode(path)
{
//	alert(path);
//	alert(path.search(/sImg/));
	if (path.search(/sImg/) == 0)
	{
//		alert("its correct");
		path = path.slice(4);
		openImg.src = "images/open.gif";
		closedImg.src = "images/close.gif";
	}
	else
	{
		openImg.src = "images/icon_movedn.gif";
		closedImg.src = "images/icon_moveup.gif";
	}

	var objPath = path + "obj";	
	var objNode = document.getElementById(objPath); 
	var imgPath = path + "img";	
	var imgNode = document.getElementById(imgPath); 
//	alert(objPath);
//	alert(imgPath);

	if (objNode.style.display == "block"  || objNode.style.display == "")
	{
		objNode.style.display = "none";
		imgNode.src = openImg.src;
	}
	else
	{
		objNode.style.display = "block";
		imgNode.src = closedImg.src;
	}

} 

function collapse(divname,imgname,mainDivName)
{
	var mainDiv = document.getElementById(mainDivName);
	var divObj = mainDiv.getElementsByTagName("div");
	var imgObj = document.getElementsByName(imgname);
	
	for (var i=0; i<divObj.length ;i++ )
	{
		if (divObj[i].name == divname)
		{
			divObj[i].style.display = "none";
		}
	}
	for (i=0;i<imgObj.length ;i++ )
	{
		imgObj[i].src = openImg.src;
	} 
}

function expand(divname,imgname,mainDivName)
{
	var mainDiv = document.getElementById(mainDivName);
	var divObj = mainDiv.getElementsByTagName("div");
	var imgObj = document.getElementsByName(imgname);
	var tabObj = mainDiv.getElementsByTagName("table");
	
	for (var i=0; i<divObj.length ;i++ )
	{
		if (divObj[i].name == divname)
		{
			divObj[i].style.display = "block";
		}
	}

	for (i=0;i<imgObj.length ;i++ )
	{
		imgObj[i].src = closedImg.src;
	} 

	for (i=0;i<tabObj.length ;i++ )
	{
		if (tabObj[i].name == divname)
		{
			tabObj[i].style.display = "block";
		}
	} 
}