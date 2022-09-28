/*
*
* @author Nikos Kontonasios
*/

function sendEntities(entities) {
	var xhr = new XMLHttpRequest();
	xhr.addEventListener("load", function() {
		var obj = JSON.parse(xhr.responseText);
		if (xhr.status === 200) {
			obj.forEach(entity => {
				appendEntityToHTML(entity);
			});
			document.getElementById("entities_title").innerHTML = obj.length + " Entities Found";
			console.log("Returned status of " + xhr.status);
		} else {
			console.log("Returned status of " + xhr.status);
		}
	});
	xhr.open("POST", "http://localhost:8080/Entity_Recognition_Greek/AddPropertiesToJsonServlet");
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send(JSON.stringify(entities));
}

function findEntities() {
	// removes the entities of the previous search from the web app
	removeEntities();
	
	var service_url = 'https://babelfy.io/v1/disambiguate';
	var text = $("#inputText").val();
	var lang = 'EL';
	var key = 'aea59324-ebe3-4d5b-b82c-217dc97f0dda';
	var annotation_type = 'NAMED_ENTITIES';
	var matching_type = 'EXACT_MATCHING';

	var params = {
		'text': text,
		'lang': lang,
		'annType': annotation_type,
		'match': matching_type,
		'key': key
	};

	var entities = [];

	$.getJSON(service_url + "?", params, function(response) {
		document.getElementById("toAppend").innerHTML = "";

		$.each(response, function(key, val) {
			var entity = {};
			//Entity is considered valid only if it has a DBpedia URL
			if (val.DBpediaURL != "") {
				// retrieving char and token fragments
				entity.cfStart = val.charFragment.start;
				entity.cfEnd = val.charFragment.end;
				entity.tfStart = val.tokenFragment.start;
				entity.tfEnd = val.tokenFragment.end;
				entity.fragment = text.substring(entity.cfStart, entity.cfEnd + 1);
				
				// retrieving DBpedia URL
				entity.url = val.DBpediaURL;
				entities.push(entity);
			}
		});
		entities = removeUnnecessaryEntities(entities);
		sendEntities(entities);
	});
	return entities;
}

function removeUnnecessaryEntities(entities) {
	for (var i = 0; i < entities.length; i++) {
		if (entities[i].tfStart === entities[i].tfEnd) {
			continue;
		}
		if (i < entities.length - 1) {
			if (entities[i + 1].tfStart >= entities[i].tfStart && entities[i + 1].tfEnd <= entities[i].tfEnd) {
				entities.splice(i + 1, 1);
				i--;
			}
		}
		if (i > 0) {
			if (entities[i - 1].tfStart >= entities[i].tfStart && entities[i - 1].tfEnd <= entities[i].tfEnd) {
				entities.splice(i - 1, 1);
				i--;
			}
		}
	}
	removeDuplicateEntities(entities);
	return entities;
}

function removeDuplicateEntities(entities){
	var toMap = {};
	for (var i = 0; i < entities.length; i++){
		if(toMap[entities[i].url]){
			entities.splice(i, 1);
			i--;
		}
		else
			toMap[entities[i].url] = true;
	}
}