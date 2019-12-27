let canvas = new fabric.Canvas('canvas', { hoverCursor: 'pointer',
    selection: false,  width: 1000, height: window.innerHeight});
let isOpenedAddWay = 0, isOpenedAddArrow = 0, isOpenedBlock = 0;


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
                left: leftPadding - 20 + (wayBlocks[block['way']].length - 1) * blockIntervalX + blockHeight / 2,
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
                    let i = 0;
                    for(;i < data['blocks'].length; i++) {
                        if (data['blocks'][i]['name'] === e.target.toObject().name) {
                            break;
                        }
                    }

                    let ways = data['ways'];
                    let wayOptionsHtml = "";

                    for (let i = 0; i < ways.length - 1; i++){
                        for (let j = 0; j < ways.length - i - 1; j++){
                            if(ways[j]['number'] > ways[j + 1]['number']){
                                let tmp = ways[j];
                                ways[j] = ways[j + 1];
                                ways[j + 1] = tmp;
                            }
                        }
                    }

                    for (let i = 0; i < ways.length; i++){
                        wayOptionsHtml += "<option>" + ways[i]['number'] + "</option>";
                    }
                    $('#editBlockWay').html(wayOptionsHtml);

                    let block = data['blocks'][i];
                    $('#blockName').val(e.target.toObject().name);
                    $('#editBlockName').val(block['name']);
                    $('#editBlockLength').val(block['length']);
                    $('#editBlockWay').val(block['way']);
                    if (block['platformNumber'] !== null) {
                        $('#editHasPlatform').prop('checked', true);
                        $('#editPlatformNumber').val(block['platformNumber']);
                        $('#editPlatformGroup').show();
                    } else {
                        $('#editHasPlatform').prop('checked', false);
                        $('#editPlatformGroup').hide();
                    }
                    $('#editBlockModal').modal('toggle');
                } else if (e.target.type === "text") {
                    $('#editWayModal').modal('toggle');
                    $('#wayNumber').val(e.target.toObject().number);
                    $('#editWayNumber').val(e.target.toObject().number);
                } else if (e.target.type === "line") {
                    $('#blockFrom').val(e.target.toObject().blockFrom);
                    $('#blockTo').val(e.target.toObject().blockTo);
                    $('#removeLinkModal').modal('toggle');
                }

            }
        });
    }
}

function processData(newData) {
    if (newData !== "") {
        if (newData.status !== "ERROR") {
            data = newData;
            drawTopology();
            let ways = [];
            for (let i = 0; i < data['ways'].length; i++){
                let way = data['ways'][i]['number'];
                if(!ways.includes(way)){
                    ways.push(way);
                }
            }
            ways.sort();

            let waysHtml;
            for (let i = 0; i < ways.length; i++) {
                waysHtml += "<option>" + ways[i] + "</option>";
            }
            $('#inputWay').html(waysHtml);
        } else {
            alert(newData.message);
        }
    }
}

function showEditPlatformGroup() {
    if($('#editHasPlatform').prop('checked') === true){
        $('#editPlatformGroup').show();
    }
    else{
        $('#editPlatformGroup').hide();
        $('#editPlatformNumber').val("");
    }
}

function showAddPlatformGroup() {
    if($('#hasPlatform').prop('checked') === true) {
        $('#addPlatformGroup').show();
    }
    else {
        $('#addPlatformGroup').hide();
        $('#hasPlatform').val("");
    }
}

function addWay() {
    $.post(
        "/constructor/addWay",
        {'number': $('#inputWayNumber').val(), 'station': localStorage.getItem("stationName")},
        function (newData) {
            processData(newData);
        }
    );
}

function addBlock() {
    if($('#hasPlatform').prop('checked') === true && $('#inputPlatformNumber').val() === ""){
        alert("Номер платформы не может быть пустым");
    }
    else{
        $.post(
            "/constructor/addBlock",
            {'name': $('#inputBlockName').val(), 'way': $('#inputWay').val(), 'length': $('#inputBlockLength').val(),
                'platformNumber': document.getElementById('hasPlatform').checked ? $('#inputPlatformNumber').val() : null, 'station': localStorage.getItem("stationName")},
            function (newData) {
                processData(newData);
            }
        );
    }
}

function addLink() {
    $.post(
        "/constructor/addLink",
        {'blockFrom': $('#inputBlockFromName').val(), 'blockTo': $('#inputBlockToName').val(), 'station': localStorage.getItem("stationName")},
        function (newData) {
            processData(newData);
        }
    );
}

function editWay() {
    $.post(
        "/constructor/editWay?old_number=" + $('#wayNumber').val(),
        {'number': $('#editWayNumber').val(), 'station': localStorage.getItem("stationName")},
        function (newData) {
            if(newData.status !== 'ERROR'){
                $('#editWayModal').modal('toggle');
            }
            processData(newData);
        }
    );
}

function editBlock() {
    let length = $('#editBlockLength').val().toString();
    if(length.split('.').length > 1) {
        if(parseInt(length.split('.')[1]) !== 0){
            alert("Длина может быть только целым числом");
        }
        else{
            length = $('#editBlockLength').val().toString().split('.')[0];
        }
    }
    $.post(
        "/constructor/editBlock?old_name=" + $('#blockName').val(),
        {
            'name': $('#editBlockName').val(),
            'way': $('#editBlockWay').val(),
            'length': length,
            'platformNumber': $('#editPlatformNumber').val(),
            'station': localStorage.getItem("stationName")
        },
        function (newData) {
            if (newData.status !== 'ERROR') {
                $('#editBlockModal').modal('toggle');
            }
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
            if(newData.status !== 'ERROR'){
                $('#editWayModal').modal('toggle');
            }
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
            if(newData.status !== 'ERROR'){
                $('#editBlockModal').modal('toggle');
            }
            processData(newData);
        }
    )
}

function removeLink() {
    $('#removeLinkModal').modal('toggle');
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
        $('#addWayGroup').hide();
        isOpenedAddWay = 1;
    }
    else{
        $('#addWayGroup').show();
        isOpenedAddWay = 0;
    }
}

function showAddArrow(){
    if(isOpenedAddArrow === 0){
        $('#addArrowGroup').hide();
        isOpenedAddArrow = 1;
    }
    else{
        $('#addArrowGroup').show();
        isOpenedAddArrow = 0;
    }
}

function showAddBlock(){
    if(isOpenedBlock === 0){
        $('#addBlockGroup').hide();
        isOpenedBlock = 1;
    }
    else{
        $('#addBlockGroup').show();
        isOpenedBlock = 0;
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