function pleaseTakeMeFromHere() {
    var arrayOfEndpoints = ["/home", "/characters", "/initiativeTracker", "/characterAdd", "/levelsTable"];

    var randomEndpoint = arrayOfEndpoints[Math.floor(Math.random() * arrayOfEndpoints.length)];

    var random = "http://localhost:8080"+randomEndpoint

    window.location.replace(random);
}