let addArrow = document.getElementById('addArrow');
let drawArea = document.getElementById('canvas');

let isOpenedAddWay;
let isOpenedAddArrow;

function drawTopology(data){
    if(data == null){
        //TODO ошибка
    }
    else{
        let waysCount = data['ways'].length;
        let blocks = data['blocks'];
        let links = data['links'];

        let canvas = new fabric.Canvas('canvas', {width: window.innerWidth / 3 * 2, height: 140 * waysCount});

        let rectBlock = new fabric.Rect();
        let line = new fabric.Line();
        let text = null;

        let wayBlocks = [];
        $.each(blocks, function (index, block) {
            if (wayBlocks[block['way']] === undefined) {
                wayBlocks[block['way']] = 0;
            } else {
                wayBlocks[block['way']]++;
            }
            rectBlock = new fabric.Rect({
                left:  20 + wayBlocks[block['way']] * 200,
                top: 70 * block['way'],
                fill: '#E6E6FA',
                width: 100,
                height: 50,
                strokeWidth: 2,
                stroke: "black"
            });
            text = new fabric.Text('БУ ' + block['name'] + '\nдлина:' + block['length'], {
                fontFamily: 'Helvetica',
                fontSize: 15,
                left: wayBlocks[block['way']] * 200 + 25,
                top: 70 * block['way'] + 6
            });
                canvas.add(rectBlock);
                canvas.add(text);
        });

        $.each(links, function (index, link) {
            $.each(blocks, function (index, block) {
                if (block['name'] === link['blockFrom']){
                    let i = 0;
                    for (; i < block.length; i++){
                        if (block['name'] === link['blockTo']){
                            break;
                        }
                    }
                    line = new fabric.Line([20 + index*200 + 100, 70 * block['way'] + 25, 20 + i * 200, 70 * block['way'] + 25], {stroke : 'black'});
                    canvas.add(line);
                }

            });
        })
    }
}

function addWay() {
    $.post(
        "/constructor/addWay",
        {'number': document.getElementById('inputWayNumber').value, 'station': localStorage.getItem("stationName")},
        function (data) {
            if (data !== null) {
                drawTopology(data);
            }
        }
    );
}

function addBlock() {
    $.post(
        "/constructor/addBlock",
        {'name': document.getElementById('inputBlockName').value, 'way': document.getElementById('inputWay').value, 'length': document.getElementById('inputBlockLength').value,
        'platformNumber': document.getElementById('inputPlatformNumber').value, 'station': localStorage.getItem("stationName")},
        function (data) {
            if (data !== null) {
                drawTopology(data);
            }
        }
    );
}

function addLink() {
    $.post(
        "/constructor/addLink",
        {'blockFrom': document.getElementById('inputBlockFromName').value, 'blockTo': document.getElementById('inputBlockToName').value, 'station': localStorage.getItem("stationName")},
        function (data) {
            if (data !== null) {
                drawTopology(data);
            }
        }
    );
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
        "/constructor/station",
        {'name':localStorage.getItem("stationName")},
        function (data) {
            drawTopology(data);
        }
    )
});