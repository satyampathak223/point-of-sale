
function getOrderUrl() {
    var baseUrl = $("meta[name=baseUrl]").attr("content")
    return baseUrl + "/api/orders";
}
function getInvoiceUrl() {
    var baseUrl = $("meta[name=baseUrl]").attr("content")
    return baseUrl + "/api/get-invoice";
}

function openCreateOrderDialog(event) {
    console.log("openCreateOrderDialog called")
    $('#order-form-modal').modal('toggle');
    orderItems = [];
    refreshOrderForm();
}

var orderItems = [];
var orderId = '';

function clearFormItems() {
    $("#order-item-form input[name=barcode]").val('');
    $("#order-item-form input[name=quantity]").val('');
}
function addItem() {
    console.log("inside add item")
    var data = {};
    data['barcode'] = $("#order-item-form input[name=barcode]").val();
    data['quantity'] = $("#order-item-form input[name=quantity]").val();
    orderItems.push(data);
    console.log(orderItems);
    refreshOrderForm();
    clearFormItems();
}

function createOrder() {
    var order = { "orderItemFormList": orderItems }
    var json = JSON.stringify(order);
    var url = getOrderUrl();
    console.log(json);

    //Make ajax call
    console.log(json);
    $.ajax({
        url: url,
        type: 'POST',
        data: json,
        headers: {
            'Content-Type': 'application/json'
        },
        success: function (response) {
            $('#order-form-modal').modal('toggle');
            makeOrderDropdown();
            makeToast(true, "");
            orderItems = []
        },
        error: function (error) {
            $('#order-form-modal').modal('toggle');
            var message = error.responseJSON.message;
            errorData = message;
            var pos = message.indexOf(",");
            message = message.slice(0, pos);
            message += "...."
            makeToast(false, message);
        }
    });
}

//HELPER METHOD
function toJson($form) {
    var serialized = $form.serializeArray();
    console.log(serialized);
    var s = '';
    for (s in serialized) {
        data[serialized[s]['name']] = serialized[s]['value']
    }
    var json = JSON.stringify(data);

    console.log(json);
    return json;
}

function refreshOrderForm() {
    console.log('Printing product data');
    var $tbody = $('#brand-table').find('tbody');
    $tbody.empty();
    for (var i in orderItems) {
        var e = orderItems[i];
        console.log(e);
        var buttonHtml = '<button class="btn" onclick="removeProduct(' + i + ')"><i class="fa fa-trash"></i></button>'
        console.log(e.id);
        var row = '<tr>'
            + '<td>' + e.barcode + '</td>'
            + '<td>' + e.quantity + '</td>'
            + '<td>' + buttonHtml + '</td>'
            + '</tr>';
        $tbody.append(row);
    }
}

dropDownOrder = $("#select-order");
function makeOrderDropdown() {
    emptyDropdown(dropDownOrder);

    url = getOrderUrl();

    $.ajax({
        url: url,
        type: 'GET',
        headers: {
            'Content-Type': 'application/json'
        },
        success: function (data) {
            var selectValues = {}
            console.log(data);
            for (var i in data) {
                val = data[i];
                val = "Order " + val.id + "   [" + val.createdAt + " ]";
                selectValues[val] = val;
            }
            $.each(selectValues, function (key, value) {
                dropDownOrder.append($("<option></option>")
                    .attr("value", key)
                    .text(value));
            });

            dropDownOrder.change(function () {
                orderId = dropDownOrder.val().split(' ')[1];
                getOrderDetails();
            });

        },
        error: function (error) {

        }
    });
}


function emptyDropdown(dropDown) {
    dropDown.empty();
    dropDown.append($("<option></option>")
        .attr("value", null)
        .text("select order"));
    dropDown.val("select order");
}

errorData = "";

function downloadErrors() {
    writeFileData(errorData);
}
function removeProduct(i) {

    orderItems.splice(i, 1)
    refreshOrderForm();
}

function getOrderDetails() {
    var url = getOrderUrl() + "/" + orderId;

    //Make ajax call
    $.ajax({
        url: url,
        type: 'GET',
        headers: {
            'Content-Type': 'application/json'
        },
        success: function (data) {
            console.log(data);
            displayOrderDetails(data);

        },
        error: function (error) {
            console.log(error)
            displayOrderDetails();
        }
    });
}
function displayOrderDetails(data) {
    console.log('Printing order details');
    var $tbody = $('#order-table').find('tbody');
    $tbody.empty();
    var total = 0;
    for (var i in data) {
        var e = data[i];
        //	    var buttonHtml ='<button class="btn" onclick="displayEditOrder(' + e.id + ')"><i class="fa fa-edit"></i> edit</button>'
        console.log(e.id);
        var sno = parseInt(i) + 1;
        var row = '<tr>'
            + '<td>' + sno + '</td>'
            + '<td>' + e.productName + '</td>'
            + '<td>' + e.barcode + '</td>'
            + '<td>' + e.quantity + '</td>'
            + '<td>' + e.mrp + '</td>'
            + '<td>' + (e.mrp * e.quantity) + '</td>'
            //        + '<td>' + buttonHtml + '</td>'
            + '</tr>';
        $tbody.append(row);
        total += (e.mrp * e.quantity);
    }
    var row = '<tr>'
        + '<td></td>'
        + '<td></td>'
        + '<td></td>'
        + '<td></td>'
        + '<td><b>Total</b></td>'
        + '<td><b>' + total + '</b></td>'
        //        + '<td>' + buttonHtml + '</td>'
        + '</tr>';
    $tbody.append(row);

}

function generateInvoice() {
    emptyDropdown(dropDownOrder);
    console.log(orderId);
    url = getInvoiceUrl() + "/" + orderId;

    $.ajax({
        url: url,
        type: 'GET',
        headers: {
            'Content-Type': 'application/json'
        },
        success: function (data) {
            makeToast(true,"Invoice generation successful")
        },
        error: function (error) {

        }
    });
    makeOrderDropdown();
}

//INITIALIZATION CODE
function init() {
    $('#open-add-dialog').click(openCreateOrderDialog);
    $('#form-create-order').click(createOrder);
    $('#add-item').click(addItem);
    $('#generate-invoice').click(generateInvoice);
    $("#download-errors").click(downloadErrors);

}

//function paginate(){
//      $('#order-table').DataTable();
//      $('.dataTables_length').addClass('bs-select');
//}
$(document).ready(init);
$(document).ready(makeOrderDropdown);
