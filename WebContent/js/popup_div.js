// JavaScript Document
function showPopUp(el) {
var cvr = document.getElementById("cover")
var dlg = document.getElementById(el)
cvr.style.display = "block"
dlg.style.display = "block"
if (document.body.style.overflow = "hidden") {
	cvr.style.width = "100%"
	cvr.style.height = "100%"
	}
}
function closePopUp(el) {
var cvr = document.getElementById("cover")
var dlg = document.getElementById(el)
cvr.style.display = "none"
dlg.style.display = "none"
document.body.style.overflowY = "none"
}