let addArrow = document.getElementById('addArrow');
let drawArea = document.getElementById('canvas');
let canvas = new fabric.Canvas('canvas', { hoverCursor: 'pointer',
    selection: false,  width: 1000, height: window.innerHeight});
let isOpenedAddWay;
let isOpenedAddArrow;

let data;

function drawTopology() {
    if(data === null) {
        alert(data['message']);
    }
    else {
        canvas.clear();

        let leftPadding = 120, blockWidth = 100, blockHeight = 50,
            blockIntervalX = 200, wayHeight = 100;
        let blocks = data['blocks'];
        let links = data['links'];
        let ways = data['ways'];

        let wayBlocks = [];

        for (let i = 0; i < ways.length; i++) {
            wayBlocks[ways[i]['number']] = [];
            let wayText = new fabric.Text('Путь ' + ways[i]['number'], {
                fontFamily: 'Helvetica',
                fontSize: 15,
                left: 40,
                top: wayHeight * ways[i]['number'] + 12,
                lockMovementX: true,
                lockMovementY : true,
                lockScalingX: true,
                lockScalingY : true,
                lockRotation : true
            });
            wayText.toObject = function(){
                return {
                    number: ways[i]['number']
                };
            };
            canvas.add(wayText);
        }

        for (let i = 0; i < blocks.length; i++) {
            let block = blocks[i];
            wayBlocks[block['way']].push(block);
            let color = '#E6E6FA';
            if (block['platformNumber'] !== null) {
                color = '#F08080';
            }
            let rectBlock = new fabric.Rect({
                left:  leftPadding + (wayBlocks[block['way']].length - 1) * blockIntervalX,
                top: wayHeight * block['way'],
                fill: color,
                width: blockWidth,
                height: blockHeight,
                strokeWidth: 2,
                stroke: "black",
                lockMovementX: true,
                lockMovementY : true,
                lockScalingX: true,
                lockScalingY : true,
                lockRotation : true
            });

            rectBlock.toObject = function(){
              return {
                name: block['name']
              };
            };

            let text = new fabric.Text('БУ ' + block['name'] + (block['platformNumber'] === null ? '' : ' Пл: ' + block['platformNumber']) +'\nдлина:' + block['length'], {
                fontFamily: 'Helvetica',
                fontSize: 15,
                left: leftPadding - 10 + (wayBlocks[block['way']].length - 1) * blockIntervalX + blockHeight / 2,
                top: wayHeight * block['way'] + 6,
                lockMovementX: true,
                lockMovementY : true,
                lockScalingX: true,
                lockScalingY : true,
                lockRotation : true,
                selectable: false
            });
                canvas.add(rectBlock);
                canvas.add(text);
        }

        for (let i = 0; i < links.length; i++) {
            let link = links[i];
            let blockFromIndex, blockToIndex,
            blockFromWay, blockToWay;
            for (let j = 0; j < ways.length; j++) {
                for (let k = 0; k < wayBlocks[ways[j]['number']].length; k++) {
                    if (wayBlocks[ways[j]['number']][k]['name'] === link['blockFrom']) {
                        blockFromIndex = k;
                        blockFromWay = ways[j]['number'];
                    } else if (wayBlocks[ways[j]['number']][k]['name'] === link['blockTo']) {
                        blockToIndex = k;
                        blockToWay = ways[j]['number'];
                    }
                }
            }

            let line = new fabric.Line([
                leftPadding + blockFromIndex * blockIntervalX + blockWidth,
                wayHeight * blockFromWay + blockHeight / 2,
                leftPadding + blockToIndex * blockIntervalX,
                wayHeight * blockToWay + blockHeight / 2],
                {   stroke : 'black',
                    strokeWidth: 5,
                    lockMovementX: true,
                    lockMovementY : true,
                    lockScalingX: true,
                    lockScalingY : true,
                    lockRotation : true
                });
            line.toObject = function(){
                return {
                    blockFrom: link['blockFrom'],
                    blockTo: link['blockTo']
                };
            };
            canvas.add(line);
        }

        canvas.on({
            'object:selected': function (e) {
                if(e.target.type === "rect") {
                    document.getElementById('editBlock').style.display = 'block';
                    let i = 0;
                    for(;i < data['blocks'].length; i++) {
                        if (data['blocks'][i]['name'] === e.target.toObject().name) {
                            break;
                        }
                    }
                    let block = data['blocks'][i];
                    document.getElementById('blockName').value = e.target.toObject().name;
                    document.getElementById('editBlockName').value = block['name'];
                    document.getElementById('editBlockLength').value = block['length'];
                    document.getElementById('editBlockWay').value = block['way'];
                    if (block['platformNumber'] !== "") {
                        document.getElementById('editHasPlatform').checked = true;
                        document.getElementById('editPlatformNumber').value = block['platformNumber'];
                    }
                } else if (e.target.type === "text") {
                    document.getElementById('editWay').style.display = 'block';
                    document.getElementById('wayNumber').value = e.target.toObject().number;
                    document.getElementById('editWayNumber').value = e.target.toObject().number;
                } else if (e.target.type === "line") {
                    document.getElementById('removeLink').style.display = 'block';
                    document.getElementById('blockFrom').value = e.target.toObject().blockFrom;
                    document.getElementById('blockTo').value = e.target.toObject().blockTo;
                }

            },
            'selection:cleared' : function (e) {
                document.getElementById('editBlock').style.display = 'none';
                document.getElementById('editWay').style.display = 'none';
                document.getElementById('removeLink').style.display = 'none';
            }
        });
    }
}

function processData(newData) {
    if (newData !== "") {
        if (newData.status !== "ERROR") {
            data = newData;
            drawTopology();

            let ways;

            for (let i = 0; i < data['ways'].length; i++) {
                ways += "<option>" + data['ways'][i]['number'] + "</option>";
            }

            document.getElementById('inputWay').innerHTML = ways;
        } else {
            alert(newData.message);
        }
    }
}

function addWay() {
    $.post(
        "/constructor/addWay",
        {'number': document.getElementById('inputWayNumber').value, 'station': localStorage.getItem("stationName")},
        function (newData) {
            processData(newData);
        }
    );
}

function addBlock() {
    $.post(
        "/constructor/addBlock",
        {'name': document.getElementById('inputBlockName').value, 'way': document.getElementById('inputWay').value, 'length': document.getElementById('inputBlockLength').value,
        'platformNumber': document.getElementById('hasPlatform').checked ? document.getElementById('inputPlatformNumber').value : null, 'station': localStorage.getItem("stationName")},
        function (newData) {
            processData(newData);
        }
    );
}

function addLink() {
    $.post(
        "/constructor/addLink",
        {'blockFrom': document.getElementById('inputBlockFromName').value, 'blockTo': document.getElementById('inputBlockToName').value, 'station': localStorage.getItem("stationName")},
        function (newData) {
            processData(newData);
        }
    );
}

function editWay() {
    $.post(
        "/constructor/editWay?old_number=" + document.getElementById('wayNumber').value,
        {'number': document.getElementById('editWayNumber').value, 'station': localStorage.getItem("stationName")},
        function (newData) {
            processData(newData);
        }
    );
}

function editBlock() {
    $.post(
        "/constructor/editBlock?old_name=" + document.getElementById('blockName').value,
        {'name': document.getElementById('editBlockName').value, 'way': document.getElementById('editBlockWay').value, 'length': document.getElementById('editBlockLength').value,
            'platformNumber': document.getElementById('editPlatformNumber').value, 'station': localStorage.getItem("stationName")},
        function (newData) {
            processData(newData);
        }
    );
}

function removeWay() {
    $.get(
        "/constructor/removeWay",
        {'number': document.getElementById('editWayNumber').value,
            'station' : localStorage.getItem("stationName")},
        function (newData) {
            processData(newData);
        }
    )
}

function removeBlock() {
    $.get(
        "/constructor/removeBlock",
        {'name': document.getElementById('editBlockName').value,
            'station' : localStorage.getItem("stationName")},
        function (newData) {
            processData(newData);
        }
    )
}

function removeLink() {
    $.get(
        "/constructor/removeLink",
        {'blockFrom': document.getElementById('blockFrom').value,
            'blockTo' : document.getElementById('blockTo').value,
            'station' : localStorage.getItem("stationName")},
        function (newData) {
            processData(newData);
        }
    )
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
        {'name': localStorage.getItem("stationName")},
        function (newData) {
            processData(newData);
        }
    )
});