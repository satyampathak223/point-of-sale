
function getBrandUrl() {
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brands";
}

//BUTTON ACTIONS

function displayAddBrand() {
	// 	resetUploadDialog();
	$('#add-brand-modal').modal('toggle');
}

function addBrand(event) {
	//Set the values to update
	//	$('#add-brand-modal').modal('toggle');
	var $form = $("#add-brand-form");
	var json = "["+toJson($form)+"]";
	var url = getBrandUrl();
	console.log("This is the json",json);

	$.ajax({
		url: url,
		type: 'POST',
		data: json,
		headers: {
			'Content-Type': 'application/json'
		},
		success: function (response) {
			console.log("Brand created");
			getBrandList();     //...
		},
		error: function () {
			alert("An error has occurred");
		}
	});
	resetAddDialog();
    $('#add-brand-modal').modal('toggle');
	return false;
}

function resetAddDialog(){
    $("#add-brand-form input[name=category]").val('');
    $("#add-brand-form input[name=id]").val('');
    $("#add-brand-form input[name=name]").val('');
}

function updateBrand(event) {
	$('#edit-brand-modal').modal('toggle');
	//Get the ID
	var id = $("#brand-edit-form input[name=id]").val();
	var url = getBrandUrl() + "/" + id;

	//Set the values to update
	var $form = $("#brand-edit-form");
	var json = toJson($form);

	$.ajax({
		url: url,
		type: 'PUT',
		data: json,
		headers: {
			'Content-Type': 'application/json'
		},
		success: function (response) {
			console.log("Brand update");
			getBrandList();     //...
		},
		error: function () {
			alert("An error has occurred");
		}
	});

	return false;
}


function getBrandList() {
	var url = getBrandUrl();
	$.ajax({
		url: url,
		type: 'GET',
		success: function (data) {
			console.log("Brand data fetched");
			console.log(data);
			displayBrandList(data);     //...
		},
		error: function (error) {
			alert(error.responseJSON.message);
		}
	});
}

//UI DISPLAY METHODS

function displayBrandList(data) {
	console.log('Printing brand data');
	var $tbody = $('#brand-table').find('tbody');
	$tbody.empty();
	for (var i in data) {
		var e = data[i];
		var buttonHtml = ' <button onclick="displayEditBrand(' + e.id + ')" class="btn btn-primary" >Edit</button>'
		var row = '<tr>'
			//		+ '<td>' + e.id + '</td>'
			+ '<td>' + e.name + '</td>'
			+ '<td>' + e.category + '</td>'
			+ '<td>' + buttonHtml + '</td>'
			+ '</tr>';
		$tbody.append(row);
	}
}

function displayEditBrand(id) {
	var url = getBrandUrl() + "/" + id;
	$.ajax({
		url: url,
		type: 'GET',
		success: function (data) {
			console.log("Brand data fetched");
			console.log(data);
			displayBrand(data);     //...
		},
		error: function () {
			alert("An error has occurred");
		}
	});
}

function displayBrand(data) {
	$("#brand-edit-form input[name=name]").val(data.name);
	$("#brand-edit-form input[name=category]").val(data.category);
	$("#brand-edit-form input[name=id]").val(data.id);
	$('#edit-brand-modal').modal('toggle');
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
	var file = $('#brandFile')[0].files[0];
	readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results) {
	fileData = results.data;
	uploadRows();
}

function uploadRows(){
	var json = JSON.stringify(fileData);
	var url = getBrandUrl();
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
	        $('#upload-brand-modal').modal('toggle');
	   		makeToast(true, "", null);
            getBrandList();

	   },
	   error: function(error){
	        $('#upload-brand-modal').modal('toggle');
	        console.log(error);
	        var message =  error.responseJSON.message;
	        errorData = message;
	        var pos = message.indexOf(",");
            message = message.slice(0, pos);
            message += "...."
            console.log(message);
	   		makeToast(false, message, downloadErrors);
	   }
	});
}

function downloadErrors() {
	writeFileData(errorData);
}

function resetUploadDialog() {
	//Reset file name
	var $file = $('#brandFile');
	$file.val('');
	$('#brandFileName').html("Choose File");
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
	var $file = $('#brandFile');
	var fileName = $file.val();
	$('#brandFileName').html(fileName);
}

function displayUploadData() {
	resetUploadDialog();
	$('#upload-brand-modal').modal('toggle');
}


//INITIALIZATION CODE
function init() {
	$('#add-brand').click(addBrand);
	$('#update-brand').click(updateBrand);
	$('#refresh-data').click(getBrandList);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
	$('#brandFile').on('change', updateFileName)
	$('#add-brand-dialog').click(displayAddBrand)
}

$(document).ready(init);
$(document).ready(getBrandList);

