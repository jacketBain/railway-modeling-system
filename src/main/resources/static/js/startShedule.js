function openCreateStation(){
    window.location.href="/start_Constructor";
}

function openStation(name) {
    localStorage.clear();
    localStorage.setItem("stationName",name);
    document.location.replace("/shedule");
}