function removeEntities() {
	document.getElementById("toAppend").innerHTML = "";
	document.getElementById("entities_title").innerHTML = "Entities";
}

function imgError(source){
	source.src = "images/pic01.jpg";
	source.onerror = "";
	return;
}

function appendEntityToHTML(entity) {
	var image = "images/pic01.jpg";
	if(entity.images[0])
		image = entity.images[0].img;
	var toAppend = "<div class=\"col-4 col-12-medium\">" +
		"<section class=\"highlight\">" +
		"<a href=" + image + " class=\"image featured\"><img src=" + image + " onerror=\"imgError(this)\" height=\"200\" alt=\"\" /></a>" +
		"<h3><a href=" + entity.url + ">" + entity.fragment + "</a></h3>" +
		"<p>"+entity.abstract+"</p>" +
		"<ul class=\"actions\">" +
		"<li><a class=\"button style1\" onClick=\"createModal(\'" + entity.url + "\',\'" + entity.fragment + "\')\">Learn More</a></li>" +
		"</ul>" +
		"</section>" +
		"</div>";
	var entityContainer = document.getElementById("toAppend");
	entityContainer.innerHTML += toAppend;
}