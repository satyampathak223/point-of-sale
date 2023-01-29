
function getInventoryUrl() {
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/inventories";
}

function updateInventory(event) {
	$('#edit-inventory-modal').modal('toggle');
	//Get the ID
	var id = $("#inventory-edit-form input[name=productId]").val();
	var url = getInventoryUrl() + "/" +id;

	//Set the values to update
	var $form = $("#inventory-edit-form");
	var json = toJson($form);

	$.ajax({
		url: url,
		type: 'PUT',
		data: json,
		headers: {
			'Content-Type': 'application/json'
		},
		success: function (response) {
			console.log("Inventory update");
			getInventoryList();     //...
		},
		error: function () {
			alert("An error has occurred");
		}
	});

	return false;
}


function getInventoryList() {
	var url = getInventoryUrl();
	$.ajax({
		url: url,
		type: 'GET',
		success: function (data) {
			console.log("Inventory data fetched");
			console.log(data);
			displayInventoryList(data);     //...
		},
		error: function (error) {
			alert(error.responseJSON.message);
		}
	});
}

//UI DISPLAY METHODS

function displayInventoryList(data) {
	console.log('Printing inventory data');
	var $tbody = $('#inventory-table').find('tbody');
	$tbody.empty();
	for (var i in data) {
		var e = data[i];
		var buttonHtml = ' <button onclick="displayEditInventory(' + e.productId + ')" class="btn btn-primary" >Edit</button>'
		var row = '<tr>'
			//		+ '<td>' + e.id + '</td>'
			+ '<td>' + e.barcode + '</td>'
			+ '<td>' + e.quantity + '</td>'
			+ '<td>' + buttonHtml + '</td>'
			+ '</tr>';
		$tbody.append(row);
	}
}

function displayEditInventory(id) {
	var url = getInventoryUrl() + "/" + id;
	$.ajax({
		url: url,
		type: 'GET',
		success: function (data) {
			console.log("Inventory data fetched");
			console.log(data);
			displayInventory(data);     //...
		},
		error: function () {
			alert("An error has occurred");
		}
	});
}

function displayInventory(data) {
	$("#inventory-edit-form input[name=barcode]").val(data.barcode);
	$("#inventory-edit-form input[name=quantity]").val(data.quantity);
	$("#inventory-edit-form input[name=productId]").val(data.productId);
	$('#edit-inventory-modal').modal('toggle');
}


//HELPER METHOD
function toJson($form) {
	var serialized = $form.serializeArray();
	console.log(serialized);
	var s = '';
	var data = {};
	for (s in serialized) {
		data[serialized[s]['name']] = serialized[s]['value']
	}
	var json = JSON.stringify(data);
	console.log("This is json function",json);
	return json;
}

// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;


function processData() {
	var file = $('#inventoryFile')[0].files[0];
	readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results) {
	fileData = results.data;
	uploadRows();
}

function uploadRows(){
	var json = JSON.stringify(fileData);
	var url = getInventoryUrl();
    console.log(json);
	//Make ajax call
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	        $('#upload-inventory-modal').modal('toggle');
	   		makeToast(true, "");
            getInventoryList();

	   },
	   error: function(error){
	        $('#upload-inventory-modal').modal('toggle');
	        console.log(error);
	        var message =  error.responseJSON.message;
	        errorData = message;
	        var pos = message.indexOf(",");
            message = message.slice(0, pos);
            message += "...."
            console.log(message);
	   		makeToast(false, message);
	   }
	});
}

function downloadErrors() {
	writeFileData(errorData);
}

function resetUploadDialog() {
	//Reset file name
	var $file = $('#inventoryFile');
	$file.val('');
	$('#inventoryFileName').html("Choose File");
	//Reset various counts
	processCount = 0;
	fileData = [];
	errorData = [];
	//Update counts
	updateUploadDialog();
}

function updateUploadDialog() {
	$('#rowCount').html("" + fileData.length);
	$('#processCount').html("" + processCount);
	$('#errorCount').html("" + errorData.length);
}

function updateFileName() {
	var $file = $('#inventoryFile');
	var fileName = $file.val();
	$('#inventoryFileName').html(fileName);
}

function displayUploadData() {
	resetUploadDialog();
	$('#upload-inventory-modal').modal('toggle');
}


//INITIALIZATION CODE
function init() {
//	$('#add-inventory').click(addInventory);
	$('#update-inventory').click(updateInventory);
	$('#refresh-data').click(getInventoryList);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
	$('#inventoryFile').on('change', updateFileName)
    $("#download-errors").click(onClick);
}

$(document).ready(init);
$(document).ready(getInventoryList);

