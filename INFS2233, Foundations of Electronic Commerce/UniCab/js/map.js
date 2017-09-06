function passengerInitialize() {
  var mapOptions = {
      center: new google.maps.LatLng(-27.497039, 153.013118),
      zoom: 16,
      mapTypeId: google.maps.MapTypeId.ROADMAP
    };

  var map = new google.maps.Map(document.getElementById("map_canvas"), mapOptions);

  var marker=new google.maps.Marker({
    position:map.getCenter(),
  });

  var marker1=new google.maps.Marker({
    position:new google.maps.LatLng(-27.494517, 153.009541),
  });

  var marker2=new google.maps.Marker({
    position:new google.maps.LatLng(-27.498124, 153.006977),
  });

  var marker3=new google.maps.Marker({
    position:new google.maps.LatLng(-27.498258, 153.014487),
  });

  marker.setMap(map);
  marker1.setMap(map);
  marker2.setMap(map);
  marker3.setMap(map);

  var infowindow = new google.maps.InfoWindow({
    content:'<h5>You are here!</h5>'
  });

  var infowindow1 = new google.maps.InfoWindow({
    content:'<div class="table-map"><table class="table table-hover"><tr><td><strong>Passenger:</strong></td>'
            +'<td><a href="profile.html" target="_blank">Simon</a></td></tr><tr><td><strong>Rating:</strong></td><td>4.9 (out of 5)</td></tr><tr>'
            +'<td><strong>Depart From:</strong></td><td>The University of Queensland</td></tr>'
            +'<tr><td><strong>Arrive At:</strong></td><td>Toowng</td></tr><tr>'
            +'<td><strong>Date:</strong></td><td>05/06/2015</td></tr><tr><td><strong>Time:</strong></td><td>12:30</td></tr>'
            +'<td><strong>Phone:</strong></td><td>0401212120</td></tr>'
            +'<tr><td><strong>Additional Information:</strong></td><td>'
            +'Hi, I am Simon, nice to meet you.</td></tr></table>'
            +'<button type="button" class="btn btn-danger center-block">Pick him/her</button><br></div>'
  });

  var infowindow2 = new google.maps.InfoWindow({
    content:'<div class="table-map"><table class="table table-hover"><tr><td><strong>Passenger:</strong></td>'
            +'<td><a href="profile.html" target="_blank">Brandon</a></td></tr><tr><td><strong>Rating:</strong></td><td>4.8 (out of 5)</td></tr><tr>'
            +'<td><strong>Depart From:</strong></td><td>The University of Queensland</td></tr>'
            +'<tr><td><strong>Arrive At:</strong></td><td>Toowng</td></tr><tr>'
            +'<td><strong>Date:</strong></td><td>05/06/2015</td></tr><tr><td><strong>Time:</strong></td><td>16:30</td></tr>'
            +'<tr><td><strong>Phone:</strong></td><td>0402121965</td></tr>'
            +'<tr><td><strong>Additional Information:</strong></td><td>'
            +'Hi, I am Brandon, nice to meet you.</td></tr></table>'
            +'<button type="button" class="btn btn-danger center-block">Pick him/her</button><br></div>'
  });

  var infowindow3 = new google.maps.InfoWindow({
    content:'<div class="table-map"><table class="table table-hover"><tr><td><strong>Passenger:</strong></td>'
            +'<td><a href="profile.html" target="_blank">Alice</a></td></tr><tr><td><strong>Rating:</strong></td><td>4 (out of 5)</td></tr><tr>'
            +'<td><strong>Depart From:</strong></td><td>The University of Queensland</td></tr>'
            +'<tr><td><strong>Arrive At:</strong></td><td>Toowong</td></tr><tr>'
            +'<td><strong>Date:</strong></td><td>05/06/2015</td></tr><tr><td><strong>Time:</strong></td><td>8:30</td></tr>'
            +'<tr><td><strong>Phone:</strong></td><td>040879654</td></tr>'
            +'<tr><td><strong>Additional Information:</strong></td><td>'
            +'Hi, I am Alice, nice to meet you.</td></tr></table>'
            +'<button type="button" class="btn btn-danger center-block">Pick him/her</button><br></div>'
  });  

  infowindow.open(map,marker);

  google.maps.event.addListener(marker, 'click', function() {
    infowindow.open(map,marker);
    infowindow1.close();
    infowindow2.close();
    infowindow3.close();
  });

  google.maps.event.addListener(marker1, 'click', function() {
    infowindow1.open(map,marker1);
    infowindow.close();
    infowindow2.close();
    infowindow3.close();
  });

  google.maps.event.addListener(marker2, 'click', function() {
    infowindow2.open(map,marker2);
    infowindow1.close();
    infowindow.close();
    infowindow3.close();
  });

  google.maps.event.addListener(marker3, 'click', function() {
    infowindow3.open(map,marker3);
    infowindow1.close();
    infowindow2.close();
    infowindow.close();
  });

  $('#resultPassenger').removeClass("hide");
  google.maps.event.trigger(map, "resize");
  map.setCenter(new google.maps.LatLng(-27.497039, 153.013118));
}

function driverInitialize() {
  var mapOptions = {
      center: new google.maps.LatLng(-27.485521, 152.991754),
      zoom: 16,
      mapTypeId: google.maps.MapTypeId.ROADMAP
    };

  var map = new google.maps.Map(document.getElementById("map_canvas1"), mapOptions);

  var marker=new google.maps.Marker({
    position:map.getCenter(),
  });

  var marker1=new google.maps.Marker({
    position:new google.maps.LatLng(-27.486435, 152.990241),
  });

  var marker2=new google.maps.Marker({
    position:new google.maps.LatLng(-27.484922, 152.987623),
  });

  var marker3=new google.maps.Marker({
    position:new google.maps.LatLng(-27.485983, 152.992178),
  });

  marker.setMap(map);
  marker1.setMap(map);
  marker2.setMap(map);
  marker3.setMap(map);

  var infowindow = new google.maps.InfoWindow({
    content:'<h5>You are here!</h5>'
  });

  var infowindow1 = new google.maps.InfoWindow({
    content:'<div class="table-map"><table class="table table-hover"><tr><td><strong>Driver:</strong></td>'
            +'<td><a href="profile.html" target="_blank">Mark</a></td></tr><tr><td><strong>Rating:</strong></td><td>4.5 (out of 5)</td></tr><tr>'
            +'<td><strong>Depart From:</strong></td><td>Toowong</td></tr>'
            +'<tr><td><strong>Arrive At:</strong></td><td>The University of Queensland</td></tr><tr>'
            +'<td><strong>Date:</strong></td><td>05/06/2015</td></tr><tr><td><strong>Time:</strong></td><td>12:30</td></tr><tr>'
            +'<td><strong>Fee:</strong></td><td>$5</td></tr><tr><td><strong>Phone:</strong></td><td>0401212120</td></tr>'
            +'<tr><td><strong>Additional Information:</strong></td><td>'
            +'Hi, I am Mark, nice to meet you.</td></tr></table>'
            +'<button type="button" class="btn btn-danger center-block">Ask him/her</button><br></div>'
  });

  var infowindow2 = new google.maps.InfoWindow({
    content:'<div class="table-map"><table class="table table-hover"><tr><td><strong>Driver:</strong></td>'
            +'<td><a href="profile.html" target="_blank">Dylan</a></td></tr><tr><td><strong>Rating:</strong></td><td>4.8 (out of 5)</td></tr><tr>'
            +'<td><strong>Depart From:</strong></td><td>Toowong</td></tr>'
            +'<tr><td><strong>Arrive At:</strong></td><td>The University of Queensland</td></tr><tr>'
            +'<td><strong>Date:</strong></td><td>05/06/2015</td></tr><tr><td><strong>Time:</strong></td><td>10:30</td></tr><tr>'
            +'<td><strong>Fee:</strong></td><td>$3</td></tr><tr><td><strong>Phone:</strong></td><td>0405647890</td></tr>'
            +'<tr><td><strong>Additional Information:</strong></td><td>'
            +'Hi, I am Dylan, nice to meet you.</td></tr></table>'
            +'<button type="button" class="btn btn-danger center-block">Ask him/her</button><br></div>'
  });

  var infowindow3 = new google.maps.InfoWindow({
    content:'<div class="table-map"><table class="table table-hover"><tr><td><strong>Driver:</strong></td>'
            +'<td><a href="profile.html" target="_blank">Erica</a></td></tr><tr><td><strong>Rating:</strong></td><td>4.9 (out of 5)</td></tr><tr>'
            +'<td><strong>Depart From:</strong></td><td>Toowong</td></tr>'
            +'<tr><td><strong>Arrive At:</strong></td><td>The University of Queensland</td></tr><tr>'
            +'<td><strong>Date:</strong></td><td>05/06/2015</td></tr><tr><td><strong>Time:</strong></td><td>15:30</td></tr><tr>'
            +'<td><strong>Fee:</strong></td><td>$4</td></tr><tr><td><strong>Phone:</strong></td><td>0409876543</td></tr>'
            +'<tr><td><strong>Additional Information:</strong></td><td>'
            +'Hi, I am Erica, nice to meet you.</td></tr></table>'
            +'<button type="button" class="btn btn-danger center-block">Ask him/her</button><br></div>'
  });  

  infowindow.open(map,marker);

  google.maps.event.addListener(marker, 'click', function() {
    infowindow.open(map,marker);
    infowindow1.close();
    infowindow2.close();
    infowindow3.close();
  });

  google.maps.event.addListener(marker1, 'click', function() {
    infowindow1.open(map,marker1);
    infowindow.close();
    infowindow2.close();
    infowindow3.close();
  });

  google.maps.event.addListener(marker2, 'click', function() {
    infowindow2.open(map,marker2);
    infowindow1.close();
    infowindow.close();
    infowindow3.close();
  });

  google.maps.event.addListener(marker3, 'click', function() {
    infowindow3.open(map,marker3);
    infowindow1.close();
    infowindow2.close();
    infowindow.close();
  });

  $('#resultDriver').removeClass("hide");
  google.maps.event.trigger(map, "resize");
  map.setCenter(new google.maps.LatLng(-27.485521, 152.991754));
}