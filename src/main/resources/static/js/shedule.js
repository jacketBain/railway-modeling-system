function createTrain() {
    $.post(
        "/shedule/add_train?station=" + localStorage.getItem("stationName"),
        {
            'trainNumber' : $('#inputTrainNumber').val(),
            'platform' : $('#inputPlatform').val(),
            'way' : $('#inputWay').val(),
            'type' : $('#inputType').val(),
            'length' : $('#inputLength').val(),
            'arriveTime' : $('#inputArriveTime').val(),
            'departureTime' : $('#inputDepartureTime').val(),
            'cityFrom' : $('#inputCityFrom').val(),
            'cityTo' : $('#inputCityTo').val(),
        },
        function (data) {
            processData(data);
            if(data['status'] !== 'ERROR')
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