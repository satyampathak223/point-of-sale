
function getProductUrl() {
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/products";
}

function getBrandListUrl() {
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/distinct-brand";
}

function getCategoriesListUrl(brandName) {
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/categories-by-brand/" + brandName;
}

function displayAddProduct() {
	$('#add-product-modal').modal('toggle');
    resetProductForm();
}

//BUTTON ACTIONS
function addProduct(event) {
	//Set the values to update
	var $form = $("#add-product-form");
	var json = toJson($form);
	var url = getProductUrl();
	json = "[" + json + "]";
	$.ajax({
		url: url,
		type: 'POST',
		data: json,
		headers: {
			'Content-Type': 'application/json'
		},
		success: function (response) {
			console.log("Product created");
			makeToast(true, "");
			getProductList();     //...
		},
		error: function (error) {
		    var message=error.responseJSON.message;
		    makeToast(false, message);

			alert("An error has occurred while adding product");
			console.log(error);
		}
	});
	resetAddDialog();
	$('#add-product-modal').modal('toggle');
	return false;
}

function resetAddDialog(){
    $("#add-product-form select[name=brandName]").val('');
    $("#add-product-form select[name=category]").val('');
    $("#add-product-form input[name=id]").val('');
    $("#add-product-form input[name=mrp]").val('');
    $("#add-product-form input[name=barCode]").val('');
    $("#add-product-form input[name=name]").val('');
}

function updateProduct(event) {
	$('#edit-product-modal').modal('toggle');
	//Get the ID
	var id = $("#product-edit-form input[name=id]").val();
	var url = getProductUrl() + "/" + id;
	console.log("id=", id);
	//Set the values to update
	var $form = $("#product-edit-form");
	var json = toJson($form);

	$.ajax({
		url: url,
		type: 'PUT',
		data: json,
		headers: {
			'Content-Type': 'application/json'
		},
		success: function (response) {
			console.log("Product update");
			makeToast(true, "");
			getProductList();     //...
		},
		error: function (error) {
			console.log("An error has occurred");
			var message=error.responseJSON.message;
		    makeToast(false, message);
		}
	});

	return false;
}


function getProductList() {
	var url = getProductUrl();
	$.ajax({
		url: url,
		type: 'GET',
		success: function (data) {
			console.log("Product data fetched");
			console.log(data);
			displayProductList(data);     //...
		},
		error: function () {
			alert("An error has occurred");
		}
	});
}

//UI DISPLAY METHODS

function displayProductList(data) {
	console.log('Printing product data');
	var $tbody = $('#product-table').find('tbody');
	$tbody.empty();
	for (var i in data) {
		var e = data[i];
		//		var buttonHtml = '<button onclick="deleteProduct(' + e.id + ')">delete</button>'
		var buttonHtml = ' <button onclick="displayEditProduct(' + e.id + ')" class="btn btn-primary" >Edit</button>'
		var row = '<tr>'
			//		+ '<td>' + e.id + '</td>'
			+ '<td>' + e.barCode + '</td>'
			+ '<td>' + e.brandName + '</td>'
			+ '<td>' + e.category + '</td>'
			+ '<td>' + e.name + '</td>'
			+ '<td>' + e.mrp + '</td>'
			+ '<td>' + buttonHtml + '</td>'
			+ '</tr>';
		$tbody.append(row);
	}
paginate();
}

function displayEditProduct(id) {
	var url = getProductUrl() + "/" + id;
	$.ajax({
		url: url,
		type: 'GET',
		success: function (data) {
			console.log("Product data fetched");
			console.log(data);
			displayProduct(data);     //...
		},
		error: function () {
			alert("An error has occurred");
		}
	});
}

function displayProduct(data) {
    makeDropdowns($("#edit-brand-select"), data.brandName, $("#edit-category-select"), data.category)
	$("#product-edit-form input[name=barCode]").val(data.barCode);
	$("#product-edit-form input[name=brandName]").val(data.brandName);
	$("#product-edit-form input[name=category]").val(data.category);
	$("#product-edit-form input[name=name]").val(data.name);
	$("#product-edit-form input[name=mrp]").val(data.mrp);
	$("#product-edit-form input[name=id]").val(data.id);
	$('#edit-product-modal').modal('toggle');
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
	console.log(json);
	return json;
}

// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;


function processData() {
	var file = $('#productFile')[0].files[0];
	readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results) {
	fileData = results.data;
	uploadRows();
}

function uploadRows() {
	var json = JSON.stringify(fileData);
	var url = getProductUrl();
	console.log(json);

	//Make ajax call
	$.ajax({
		url: url,
		type: 'POST',
		data: json,
		headers: {
			'Content-Type': 'application/json'
		},
		success: function (response) {
			$('#upload-product-modal').modal('toggle');
			makeToast(true, "");
			getProductList();

		},
		error: function (error) {
			$('#upload-product-modal').modal('toggle');
			console.log("This is an error",error);
			var message = error.responseJSON.message;
			errorData = message;
			var pos = message.indexOf(",");
			message = message.slice(0, pos);
			message += "....";
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
	var $file = $('#productFile');
	$file.val('');
	$('#productFileName').html("Choose File");
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
	var $file = $('#productFile');
	var fileName = $file.val();
	$('#productFileName').html(fileName);
}

function displayUploadData() {
	resetUploadDialog();
	$('#upload-product-modal').modal('toggle');
}


//INITIALIZATION CODE
function init() {
	$('#add-product').click(addProduct);
	$('#update-product').click(updateProduct);
	$('#refresh-data').click(getProductList);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
	$('#productFile').on('change', updateFileName)
	$('#add-product-dialog').click(displayAddProduct)
    $("#download-errors").click(downloadErrors);

}

function emptyDropdown(dropDown) {
	dropDown.empty();
	dropDown.append($("<option></option>")
		.attr("value", null)
		.text("select"));
	dropDown.val("select");
}

function makeDropdowns(dropDownBrand, initialBrand, dropDownCategory, initialCategory) {
	emptyDropdown(dropDownBrand);
	emptyDropdown(dropDownCategory);
	url = getBrandListUrl();

	$.ajax({
		url: url,
		type: 'GET',
		headers: {
			'Content-Type': 'application/json'
		},
		success: function (data) {
			var selectValues = {}
			console.log(data);
			for (var value in data) {
				selectValues[data[value]] = data[value];
			}
			$.each(selectValues, function (key, value) {
				dropDownBrand.append($("<option></option>")
					.attr("value", key)
					.text(value));
			});
			if (initialBrand != null) {
				dropDownBrand.val(initialBrand);
			}

			dropDownBrand.change(function () {
				makeCategoryDropDown(dropDownBrand.val(), dropDownCategory, null);
			});

			makeCategoryDropDown(initialBrand, dropDownCategory, initialCategory);
		},
		error: function (error) {
		}
	});
}

function makeCategoryDropDown(brandName, dropDownCategory, initialCategory) {
	emptyDropdown(dropDownCategory);

	url = getCategoriesListUrl(brandName);

	$.ajax({
		url: url,
		type: 'GET',
		headers: {
			'Content-Type': 'application/json'
		},
		success: function (data) {
			var selectValues = {}
			console.log(data);
			for (var value in data) {
				selectValues[data[value]] = data[value];
			}
			$.each(selectValues, function (key, value) {
				dropDownCategory.append($("<option></option>")
					.attr("value", key)
					.text(value));
			});
			if (initialCategory != null) {
				dropDownCategory.val(initialCategory);
			}
		},
		error: function (error) {
		}
	});
}

function resetProductForm() {
	makeDropdowns($("#form-brand-select"), null, $("#form-category-select"), null)
	$("#product-form input[name=productName]").val("");
	$("#product-form input[name=brandName]").val("");
	$("#product-form input[name=category]").val("");
	$("#product-form input[name=mrp]").val("");
	$("#product-form input[name=category]").val("");
	$("#product-form input[name=barcode]").val("");
}

function paginate(){
      $('#product-table').DataTable();
      $('.dataTables_length').addClass('bs-select');
}

$(document).ready(init);
$(document).ready(getProductList);

