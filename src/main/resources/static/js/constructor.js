let addWay = document.getElementById('addWay');
let addArrow = document.getElementById('addArrow');

let blocksList = {};

let isOpenedAddWay;
let isOpenedAddArrow;

function drawTopology(data){
    if(data == null){

    }
    else{

    }
}

function showAddWay(){
    if(isOpenedAddWay === 0){
        addWay.style.display = 'block';
        isOpenedAddWay = 1;
    }
    else{
        addWay.style.display = 'none';
        isOpenedAddWay = 0;
    }
}

function showAddArrow(){
    if(isOpenedAddArrow === 0){
        addArrow.style.display = 'block';
        isOpenedAddArrow = 1;
    }
    else{
        addArrow.style.display = 'none';
        isOpenedAddArrow = 0;
    }
}

$(document).ready(function () {
    $.get(
        "/constructor/stations/",
        {'name': 'Samara'},
        function (data) {
                drawTopology(data);
        }
    )
});