/*
*
* @author Nikos Kontonasios
*/

function createModal(url, name) {
	var xhr = new XMLHttpRequest();
	xhr.addEventListener("load", function() {
		var obj = JSON.parse(xhr.responseText);
		if (xhr.status === 200) {
			var modalTitle = document.getElementById('modal-title');
			modalTitle.innerHTML = name;
			appendToModal(obj);
			var modal = document.getElementById('modal');
			openModal(modal);
			console.log("Returned status of " + xhr.status);
		} else {
			console.log("Returned status of " + xhr.status);
		}
	});

	xhr.open("POST", "http://localhost:8080/Entity_Recognition_Greek/ModalServlet");
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send(url);
}

function appendToModal(obj) {
	var table = document.createElement('table');
	table.id = "tbl";
	var thead = document.createElement('thead');
	var tbody = document.createElement('tbody');
	table.appendChild(thead);
	table.appendChild(tbody);
	var modal_body = document.getElementById('modal-body');
	modal_body.appendChild(table);
	var row = document.createElement('tr');
	var row_header_1 = document.createElement('th');
	row_header_1.innerHTML = "Predicate";
	var row_header_2 = document.createElement('th');
	row_header_2.innerHTML = "Object";
	row.appendChild(row_header_1);
	row.appendChild(row_header_2);
	thead.appendChild(row);

	document.getElementById('modal-body').appendChild(table);
	obj.forEach(data => {
		var predicate = data.predicate;
		var object = data.object;
		predicate = predicate.split("/").pop();
		if(predicate.lastIndexOf("#") != -1)
			predicate = predicate.split("#").pop();
		if(predicate === "page"){
			var temp = "<a href='"+object+"'>"+object+"</a>";
			object = temp;
		}
		else if(predicate === "wikiPageExternalLink"){
			var temp = "<a href='"+object+"'>"+object+"</a>";
			object = temp;
		}
		else{
			object = object.split("/").pop();
			if(object.lastIndexOf("#") != -1)
				object = object.split("#").pop();
		}
		
		row = document.createElement('tr');
		var row_data_1 = document.createElement('td');
		row_data_1.innerHTML = predicate;
		var row_data_2 = document.createElement('td');
		row_data_2.innerHTML = object;
		row.appendChild(row_data_1);
		row.appendChild(row_data_2);
		tbody.appendChild(row);
	});
}

var modalCloseButton = document.getElementById('modal-close');
var overlay = document.getElementById('overlay');

modalCloseButton.addEventListener('click', () => {
	var modal = document.getElementById('modal');
	closeModal(modal);
});

function openModal(modal) {
	if (modal == null) return;
	modal.classList.add('active');
	overlay.classList.add('active');
}

function closeModal(modal) {
	if (modal == null) return;
	var modal_body = document.getElementById('modal-body');
	var table = document.getElementById('tbl');
	modal_body.removeChild(table);
	modal.classList.remove('active');
	overlay.classList.remove('active');
}