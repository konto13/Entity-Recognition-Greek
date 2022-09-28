/*
*
* @author Nikos Kontonasios
*/

function findEntitiesWithTranslation() {
	// removes the entities of the previous search from the web app
	removeEntities();
	
	var text = $("#inputText").val();
	var data = "source_language=el&target_language=en&text="+text;

	var xhr = new XMLHttpRequest();
	xhr.withCredentials = true;

	xhr.addEventListener("readystatechange", function() {
		if (this.readyState === this.DONE) {
			var obj = JSON.parse(this.responseText);
			sendTranslatedText(obj.data.translatedText);
		}
	});

	xhr.open("POST", "https://text-translator2.p.rapidapi.com/translate");
	xhr.setRequestHeader("content-type", "application/x-www-form-urlencoded");
	xhr.setRequestHeader("X-RapidAPI-Key", "7e593d7020msh108ceb449958c83p17a525jsn0b0c00d25af6");
	xhr.setRequestHeader("X-RapidAPI-Host", "text-translator2.p.rapidapi.com");

	xhr.send(data);
}

function sendTranslatedText(text){
	var xhr = new XMLHttpRequest();
	xhr.addEventListener("load", function() {
		var obj = JSON.parse(xhr.responseText);
		console.log(obj);
		if (xhr.status === 200) {
			obj.forEach(entity => {
				appendEntityToHTML(entity);
			});
			document.getElementById("entities_title").innerHTML = obj.length + " Entities Found";
			console.log("Returned status of " + xhr.status);
		} else {
			console.log("bad");
			console.log("Returned status of " + xhr.status);
		}
	});

	xhr.open("POST", "http://localhost:8080/Entity_Recognition_Greek/LODsyndesisRestClientServlet");
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send(text);
}