function createTrain() {
    $.post(
        "/shedule/add_train?station=" + localStorage.getItem("stationName"),
        {
            'trainNumber' : document.getElementById('inputTrainNumber').value,
            'platform' : document.getElementById('inputPlatform').value,
            'way' : document.getElementById('inputWay').value,
            'type' : document.getElementById('inputType').value,
            'length' : document.getElementById('inputLength').value,
            'arriveTime' : document.getElementById('inputArriveTime').value,
            'departureTime' : document.getElementById('inputDepartureTime').value,
            'cityFrom' : document.getElementById('inputCityFrom').value,
            'cityTo' : document.getElementById('inputCityTo').value,
        },
        function (data) {
            processData(data);
            $('#createTrainModal').modal('toggle');
        }
    );
}

function showEditTrainDialog(trainDOM) {
    let train = $(trainDOM);
    $('#trainName').val(train.find('.number').text());
    $('#editTrainNumber').val(train.find('.number').text());
    $('#editPlatform').val(train.find('.platform').text());
    $('#editWay').val(train.find('.way').text());
    $('#editType').val(train.find('.trainType').text());
    $('#editLength').val(train.find('.length').text());
    $('#editArriveTime').val(train.find('.arrive').text());
    $('#editDepartureTime').val(train.find('.departure').text());
    let route = train.find('.route').text().split('-');
    $('#editCityFrom').val(route[0].replace(' ', ''));
    $('#editCityTo').val(route[1].replace(' ', ''));
    $('#editTrainModal').modal('toggle');
}

function editTrain() {
    $.post(
        "/shedule/editTrain?station=" + localStorage.getItem("stationName") + "&old_number=" + $('#trainName').val(),
        {'trainNumber': $('#editTrainNumber').val(),
            'platform': $('#editPlatform').val(),
            'way': $('#editWay').val(),
            'type': $('#editType').val(),
            'length': $('#editLength').val(),
            'arriveTime': $('#editArriveTime').val(),
            'departureTime': $('#editDepartureTime').val(),
            'cityFrom': $('#editCityFrom').val(),
            'cityTo': $('#editCityTo').val()},
        function (newData) {
            processData(newData);
        }
    );
}

function removeTrain() {
    $.get(
        "/shedule/removeTrain",
        {
            'trainNumber': $('#editTrainNumber').val(),
            'station' : localStorage.getItem("stationName")
        },
        function (newData) {
            processData(newData);
        }
    )
}

function processData(newData) {
    data = newData;
    if (data['status'] === 'ERROR') {
        alert(data['message']);
    } else {
        document.location.reload();
    }
}