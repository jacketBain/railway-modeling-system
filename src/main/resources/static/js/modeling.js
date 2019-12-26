let canvas = new fabric.Canvas('canvas', { hoverCursor: 'pointer',
    selection: false,  width: 1000, height: window.innerHeight});
let leftPadding = 120, blockWidth = 100, blockHeight = 50,
    blockIntervalX = 200, wayHeight = 100;

let currentTime;
let currentTimeInput = $("#currentTime");
let speedRangeValue = $("#speedRangeValue");
let timeSpeed = $('#speedRange');
let timerId;

let data;
let schedule;
let blocks;
let trains;
let events;
let wayBlocks;

initModeling();

function initModeling() {
    resetTime();
    getTimeSpeed();
}

function startModeling() {
    timerId = setInterval(function() {
        currentTime = getTimeInputDate();
        currentTime = new Date(currentTime.getTime() + getTimeSpeed());
        printTime();
        modeling();
    }, 1000);
}

function stopModeling() {
    clearInterval(timerId);
    resetTime();
}

function pauseModeling() {
    clearInterval(timerId);
}

function getTimeSpeed() {
    let sec = 1000;
    let speed;
    switch (timeSpeed.val()) {
        case "1":
            speed = sec;
            speedRangeValue.text("1 секунда");
            break;
        case "2":
            speed = sec * 15;
            speedRangeValue.text("15 секунд");
            break;
        case "3":
            speed = sec * 30;
            speedRangeValue.text("30 секунд");
            break;
        case "4":
            speed = sec * 60;
            speedRangeValue.text("1 минута");
            break;
        case "5":
            speed = sec * 120;
            speedRangeValue.text("2 минуты");
            break;
        case "6":
            speed = sec * 300;
            speedRangeValue.text("5 минут");
            break;
        case "7":
            speed = sec * 600;
            speedRangeValue.text("10 минут");
            break;
        case "8":
            speed = sec * 900;
            speedRangeValue.text("15 минут");
            break;
        case "9":
            speed = sec * 1800;
            speedRangeValue.text("30 минут");
            break;
        case "10":
            speed = sec * 3600;
            speedRangeValue.text("1 час");
            break;
        default:
            speed = 0;
    }
    return speed;
}

function resetTime() {
    currentTime = new Date(Date.UTC(1970, 1, 1, 0, 0, 0));
    printTime();
}

function getTimeInputDate() {
    return new Date('01.01.1970 ' + currentTimeInput.val() + ' GMT+00:00');
}

function getTimeString() {
    let hour = currentTime.getUTCHours();
    let min  = currentTime.getUTCMinutes();
    let sec  = currentTime.getUTCSeconds();
    hour = (hour < 10 ? "0" : "") + hour;
    min = (min < 10 ? "0" : "") + min;
    sec = (sec < 10 ? "0" : "") + sec;
    return hour + ':' + min + ':' + sec;
}

function printTime() {
    $('#currentTime').val(getTimeString());
}

function drawTopology() {
    if(data === null) {
        alert(data['message']);
    }
    else {
        canvas.clear();

        let blocks = data['blocks'];
        let links = data['links'];
        let ways = data['ways'];

        wayBlocks = [];

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
                lockRotation : true,
                selectable: false
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
            let line = new fabric.Line([
                    leftPadding + (wayBlocks[block['way']].length - 1) * blockIntervalX,
                    wayHeight * block['way'] + blockHeight / 2,
                    leftPadding + (wayBlocks[block['way']].length - 1) * blockIntervalX + blockWidth,
                    wayHeight * block['way'] + blockHeight / 2],
                {   stroke : color,
                    strokeWidth: 5,
                    lockMovementX: true,
                    lockMovementY : true,
                    lockScalingX: true,
                    lockScalingY : true,
                    lockRotation : true,
                    selectable: false
                });

            let text = new fabric.Text('БУ ' + block['name'] + (block['platformNumber'] === null ? '' : ' Пл: ' + block['platformNumber']) +'\nдлина:' + block['length'], {
                fontFamily: 'Helvetica',
                fontSize: 15,
                left: leftPadding - 20 + (wayBlocks[block['way']].length - 1) * blockIntervalX + blockHeight / 2,
                top: wayHeight * block['way'] - 15,
                lockMovementX: true,
                lockMovementY : true,
                lockScalingX: true,
                lockScalingY : true,
                lockRotation : true,
                selectable: false
            });
            canvas.add(line);
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
                    lockRotation : true,
                    selectable: false
                });
            line.toObject = function(){
                return {
                    blockFrom: link['blockFrom'],
                    blockTo: link['blockTo']
                };
            };
            canvas.add(line);
        }
    }
}

function modeling() {
    let objects = canvas.getObjects('circle');
    for (let i in objects) {
        canvas.remove(objects[i]);
    }

    for (let i = 0; i < trains.length; i++) {
        modelingTrain(trains[i]);
    }
}

function modelingTrain(train) {
    let inBlock;
    let leftEvent;
    let rightEvent;
    //currentTime = new Date(Date.UTC(1970, 1, 1, 0, 20, 0));
    for (let i = 0; i < events.length; i++) {
        let event = events[i];
        if (train['number'] === event['train'] && event['time'] <= currentTime.getTime()) {
            if (leftEvent === undefined || leftEvent['time'] < event['time']) {
                leftEvent = event;
            }
        }
        if (train['number'] === event['train'] && event['time'] >= currentTime.getTime()) {
            if (rightEvent === undefined || rightEvent['time'] > event['time']) {
                rightEvent = event;
            }
        }
    }
    if (leftEvent === undefined || rightEvent === undefined) {
        return;
    } else if (leftEvent['type'] === "Освобождение") {
        inBlock = false;
    } else {
        inBlock = true;
    }
    drawTrain(train,leftEvent,rightEvent,inBlock);
}

function drawTrain(train,leftEvent,rightEvent,inBlock) {
    if (inBlock) {
        let currentBlock;
        for (let i = 0; i < blocks.length; i++) {
            let block = blocks[i];
            if (block['name'] === leftEvent['block']) {
                currentBlock = block;
                break;
            }
        }
        let trainChordX;
        let nextTime;
        if (leftEvent['type'] === "Стоянка") {
            nextTime = train['arrive'];
        } else {
            nextTime = rightEvent['time'];
        }
        trainChordX = blockWidth / (nextTime - 60 * 1000 - leftEvent['time']) * (currentTime.getTime() - leftEvent['time']);
        if (trainChordX > blockWidth) {
            trainChordX = blockWidth;
        }
        if (train['direction'] === "ODD") {
            trainChordX = blockWidth - trainChordX;
        }

        let leftOffset;
        for (let j = 0; j < wayBlocks[currentBlock['way']].length; j++) {
            if (wayBlocks[currentBlock['way']][j]['name'] === currentBlock['name']) {
                leftOffset = j;
                break;
            }
        }
        let circle = new fabric.Circle({
            left: leftPadding + blockIntervalX * leftOffset + trainChordX,
            top: wayHeight * currentBlock['way'] + blockHeight / 2 + 3,
            radius: 5,
            strokeWidth: 2,
            stroke: 'red',
            fill: 'White',
            selectable: false,
            originX: 'center',
            originY: 'center'
        });
        canvas.add(circle);
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
            $.get(
                "/modeling/schedule",
                {'station': localStorage.getItem("stationName")},
                function (newSchedule) {
                    processSchedule(newSchedule);
                }
            )
        } else {
            alert(newData.message);
        }
    }
}

function processSchedule(newSchedule) {
    if (newSchedule !== "") {
        if (newSchedule.status !== "ERROR") {
            schedule = newSchedule;

            trains = schedule['trains'];
            events = schedule['events'];
            blocks = data['blocks'];

            for (let i =0; i < events.length; i++) {
                events[i]['time'] = new Date('01.01.1970 '+ events[i]['time'] + ' GMT+00:00').getTime();
            }

            for (let i = 0; i < trains.length; i++) {
                trains[i]['arrive'] = new Date('01.01.1970 '+ trains[i]['arrive'] + ' GMT+00:00').getTime();
                trains[i]['departure'] = new Date('01.01.1970 '+ trains[i]['departure'] + ' GMT+00:00').getTime();
            }
        } else {
            alert(newSchedule.message);
        }
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