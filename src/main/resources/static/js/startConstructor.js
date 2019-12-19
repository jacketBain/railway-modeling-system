var stationName = document.getElementById("inputStationName");
var stationCity = document.getElementById("inputStationCity");
var error = document.getElementById("stationNameError");

function checkStation(){
    $.get(
      "/startConstructor/checkName",
        {'name' : stationName.value},
        function (data) {
          if(data.status === "SUCCESS"){
              error.innerHTML = "";
              enableCreate = true;
          }
          else {
              error.innerHTML = "<div id=\"warning\" class=\"alert alert-danger alert-dismissible mt-2 mb-2\" role=\"alert\">\n" +
                  "\t<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">\n" +
                  "\t\t<span aria-hidden=\"true\">&times;</span>\n" +
                  "\t</button>" +
                  "Станция с таким именем уже есть в базе данных" +
                  "</div>";
              enableCreate = false;
          }
        }
    );
}

function createStation() {
    $.post(
        "/startConstructor",
        {'name': stationName.value,
            'city': stationCity.value},
        function (data) {
            if (data.status === "SUCCESS") {
                localStorage.clear();
                localStorage.setItem("stationName", data.message);
                document.location.replace("/constructor");
            }
        }
    );
}

function openStation(name) {
    localStorage.clear();
    localStorage.setItem("stationName", name);
    document.location.replace("/constructor");
}

stationName.addEventListener("input", function () {
    if(stationName.validity.valid){
        if(stationName.value === "")
            error.innerHTML = "";
        else{
            checkStation();
        }
    }
    else {
        if(stationName.value === "")
            error.innerHTML = "";
        else {
            error.innerHTML = `<div id="warning" class="alert alert-danger alert-dismissible mt-2 mb-2" role="alert">
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                            Некорректное имя станции
                        </div>`
        }
    }
});



function removeStation(name) {
    $.post(
        "/constructor/removeStation",
        { 'name' : name },
        function (data) {
            if (data.status === "SUCCESS") {
                document.location.replace("/startConstructor");
            }
        }
    );
}