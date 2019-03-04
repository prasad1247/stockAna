function changeWidth()
{
	var iFrameDiv = document.getElementById("scroll");
	var iFrameTableDiv = document.getElementById("sub_list");
	var rightDiv = document.getElementById("right_column").style.display;
	var leftDiv = document.getElementById("left_column").style.display;	

	if(rightDiv == 'none' && leftDiv == 'none'){
		if(screen.width == 1024)
			iFrameDiv.style.width = 950+"px";			
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
		{
			iFrameDiv.style.width = 825+"px";
		}
	}	
	else{	
		if(screen.width == 1024)
			iFrameDiv.style.width = 350+"px";
		else if (screen.width == 1280)
			iFrameDiv.style.width = 480+"px";		
	}	
}

function setSubListDivWidth()
{
	var iFrameDiv = document.getElementById("scroll");
	var pagination_holder_data = document.getElementById("pagination_holder_data");
	if (screen.width == 1280)
	{
		iFrameDiv.style.width = 480+"px";
		pagination_holder_data.style.width=300+"px";
		pagination_holder_data.style.marginLeft=110+"px";
	}
}