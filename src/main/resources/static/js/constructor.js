let blocksList = {};
let stationName = document.getElementById("stationNameVal").getAttribute("name").value;

$(document).ready(function () {
    $.get(
        "/constructor",
        {'name' : stationName },
        function (data) {
            for (let i = 0; i < data.length(); i++) {
                alert(data[i]);
                blocksList.add(data[i]);
            }
        }
    )
});