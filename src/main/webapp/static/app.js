
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
    return json;
}

function makeToast(isSuccessful, message) {
    var toastHeading = document.getElementById('toast-heading');
    var toastMessage = document.getElementById('toast-message');
//    var element=document.getElementById('toast');
    console.log("Inside maketoast function");
    if (isSuccessful) {
        $("#toast").addClass("bg-success");
        if ($('#toast').hasClass('bg-danger')){
            $("#toast").toggleClass("bg-danger");
        }
        console.log("Toast is about to show for success")
        toastHeading.innerHTML = "Success";
        toastHeading.style.color = 'green';

        $("#download-errors").hide();

    } else {
        if ($('#toast').hasClass('bg-success')){
            $("#toast").toggleClass("bg-success")
        }
        $("#toast").addClass("bg-danger")
        console.log("Toast is about to show for error")
        toastHeading.innerHTML = "Error";
        toastHeading.style.color = 'red';
        $("#download-errors").show();

    }
    toastMessage.innerHTML = message;

    var options = {
        animation: true,
        delay: 20000
    };

    var toastHTMLElement = document.getElementById("toast");

    var toastElement = new bootstrap.Toast(toastHTMLElement, options)

    toastElement.show();
}


function handleAjaxError(response) {
    var response = JSON.parse(response.responseText);
    alert(response.message);
}

function readFileData(file, callback) {
    var config = {
        header: true,
        delimiter: "\t",
        skipEmptyLines: "greedy",
        complete: function (results) {
            callback(results);
        }
    }
    Papa.parse(file, config);
}


function writeFileData(arr) {
    var config = {
        quoteChar: '',
        escapeChar: '',
        delimiter: "\t"
    };

//    var data = Papa.unparse(arr, config);
    var blob = new Blob([arr], { type: 'text/plain;charset=utf-8;' });
    var fileUrl = null;

    if (navigator.msSaveBlob) {
        fileUrl = navigator.msSaveBlob(blob, 'download.txt');
    } else {
        fileUrl = window.URL.createObjectURL(blob);
    }
    var tempLink = document.createElement('a');
    tempLink.href = fileUrl;
    tempLink.setAttribute('download', 'download.txt');
    tempLink.click();
}
