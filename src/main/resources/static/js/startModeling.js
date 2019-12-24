function openModeling(name) {
    localStorage.clear();
    localStorage.setItem("stationName",name);
    document.location.replace("/modeling?station=" + name);
}