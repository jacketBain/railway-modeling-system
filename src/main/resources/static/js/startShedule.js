function openStation(name) {
    localStorage.clear();
    localStorage.setItem("stationName",name);
    document.location.replace("/shedule?station=" + name);
}