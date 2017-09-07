// Google Map that will be drawn on
var map;
// Boundaries for states of the map
var overallBounds;
var searchBounds;
var connBounds;
var networkBounds;
var allowedBounds;

// The current state of the display
var displayState = -1;
var displayLines = -1;

// InfoWindow variables
var infoWindow;
var infoWindowState = false;
var closedWindow = false;
var dispBoxClosed = false;

// Stores the substation lines
var highVoltageLines = [];
var highVoltageStr = [];
var highVoltageLineMarkers = [];
var highVoltageStrMarkers = [];
var highVoltageLineColor = "blue";
var highVoltageLineWeight = 3;
var highVoltageLineOpacity = 1.0;

// Stores the endpoints lines
var lowVoltageLines = [];
var lowVoltageStr = [];
var lowVoltageLineMarkers = [];
var lowVoltageStrMarkers = [];
var lowVoltageLineColor = "green";
var lowVoltageLineWeight = 3;
var lowVoltageLineOpacity = 1.0;

// Stores the nodes
var allNodeMarkers = [];
var substationNodeMarkers = [];
var endpointNodeMarkers = [];
var nodeIndex = [];
var lineIndex = [];

// Stores the network details
var networkItems = [];

// Stores the connections
var subConnect = [];
var endConnect = [];
var allConnect = [];
var highConnect = [];
var lowConnect = [];

// A index to keep track of the overall marker index
var markerIdx = 1;

// Marker types
var substationType = 1;
var endpointType = 2;
var allNodesType = 3;
var lowVoltageLinesType = 4;
var highVoltageLinesType = 5;

// Search Types
var endSearch = "Endpoint";
var subNetSearch = "Sub Net";
var highVolSearch = "High Voltage Line";
var lowVolSearch = "Low Voltage Line";
var networkSearch = 'Network Names';
// Node Types
var dbEnd = "Building";
var dbSub = "Substation";
var dbNet = "Network Centre";
// Search Responses
var searchEmpty = "Empty Search Invalid!";
var searchNone = "No Results Found.";

// Icon for map Markers
var substationIcon = 'images/icon-substation.png';
var endpointIcon = 'images/icon-endpoint.png';
var highVoltageLineIcon = 'images/icon-powerline.png';
var lowVoltageLineIcon = 'images/icon-powerline.png';

// Icon for searches
var substationSearchIcon = 'images/icon-substation2.png';
var endpointSearchIcon = 'images/icon-endpoint2.png';
var highVolSearchIcon = 'images/icon-powerline2.png';
var lowVolSearchIcon = 'images/icon-powerline2.png';

// Details of active marker
var markerActive = false;
var markerSelected = [];
// Details of the current network filters
var filterActive = false;
var filterNetworks = [];
// Details of current search results
var searchActive = false;
var searchResults = [];
// Details of current toggle status
var toggleStore = [];

// Default map settings
var defaultZoom = 18;
var maxZoom = 21;
var minZoom = 15;
var zoomedIn = 19;

// Array Indexes
// Line Settings
var iLId = 0;
var iLName = 1;
var iLStartId = 2;
var iLEndId = 3;
var iLStartNet = 4;
var iLEndNet = 5;
var iLCoords = 6;
// Node Settings
var iNId = 0;
var iNName = 1;
var iNType = 2;
var iNNet = 3;
var iNImg = 4;
var iNLines = 5;

// Document elements
// Search
var searchSel = "select";
var searchInp = "input";
var searchBtn = "sbutton";
var searchRes = "result";
// Toggles
var toggStr = 'straight-lines';
var toggSub = 'substation';
var toggEnd = 'endpoint';
var toggHigh = 'highVoltageLine';
var toggLow = 'lowVoltageLine';
// Filters
var filterSel = "net-filter";
var filterBtn = "filter-button";
var filterRes = "clear-filter-button";

// Active Item Details and Indexes
var activeDetails;
var downloadType = "1";
var downloadOrigType = "2";
var downloadIds = "3";
var downloadOrigIds = "4";
var downloadLat = "5";
var downloadLng = "6";
var downloadFrom = "7";
var downloadTo = "8";

// The distance in metres allowed between 2 markers to not group them
var markerThreshold = 1;
var minBounds = 100;

// Timers needed for searching
var noResultsTimer;
var timerLength = 5000; // milliseconds

/**
 * Initialize the main map and generate all the markers. The markers are then
 * bound to the map, and the map sized.
 *
 * @method initalizeMap
 * @param highVolLines
 *            An array containing details of High Voltage Lines
 * @param lowVolLines
 *            An array containing details of Low Voltage Lines
 * @param subNodes
 *            An array containing details of Substations
 * @param netNodes
 *            An array containing details of Network Centres
 * @param endNodes
 *            An array containing details of Endpoints
 */
function initializeMap(highVolLines, lowVolLines, subNodes, netNodes, endNodes) {
  var map_canvas = document.getElementById('map_canvas');
  overallBounds = new google.maps.LatLngBounds();
  allowedBounds = new google.maps.LatLngBounds();

  // Set map style
  var stylesArray = [{
    "featureType": "road",
    "elementType": "labels",
    "stylers": [{
      "visibility": "off"
    }]
  }, {
    "featureType": "poi",
    "elementType": "labels",
    "stylers": [{
      "visibility": "off"
    }]
  }, {
    "featureType": "transit",
    "elementType": "labels.text",
    "stylers": [{
      "visibility": "off"
    }]
  }]

  // Set map options (location, zoom)
  var map_options = {
    zoom: defaultZoom,
    minZoom: minZoom,
    maxZoom: maxZoom,
    mapTypeId: google.maps.MapTypeId.ROADMAP,
    styles: stylesArray
  }
  map = new google.maps.Map(map_canvas, map_options);

  pushAllNodes(subNodes, netNodes, endNodes);
  pushAllLines(highVolLines, lowVolLines);

  // set the map bounds
  allowedBounds.extend(new google.maps.LatLng(-27.488674, 153.011638));
  allowedBounds = allowedBounds.union(overallBounds);

  // Add a listener to limit the focus of the map
  var currentCenter = map.getCenter(); // Current centre focus
  // Add the listener to check for a centre change
  google.maps.event.addListener(map, 'center_changed', function() {
    if (allowedBounds.contains(map.getCenter())) {
      // still within valid bounds, so save the last valid position
      currentCenter = map.getCenter();
      return;
    }
    // New centre not valid => keep the current position
    map.setCenter(currentCenter);
  });

  // Add a listener to map, show all markers when user clicks the map
  google.maps.event.addListener(map, 'click', function() {
    dispBoxClosed = true;
    graphIsActive = false;
    updateMap(true, true, false);
  });

  // Fit map to all current points
  resetMap();
}

/**
 * Resets the map to its original state.
 *
 * @method resetMap
 */
function resetMap() {
  setupToggles();
  setToggles([true, false, false, false]);
  saveToggles();
  closedWindow = true;
  updateMap(true, true, true);
  map.fitBounds(overallBounds);
}

/**
 * Saves the current state of the toggles and then updates the map.
 *
 * @method toggleChange
 */
function toggleChange() {
  saveToggles();
  updateMap(false, false, false);
}

/**
 * Updates the map to match all the current defined filters and settings.
 *
 * @method updateMap
 * @param {bool}
 *            resSearch Will reset any current search on the map
 * @param {bool}
 *            resMarker Will reset any current active marker
 * @param {bool}
 *            resFilter Will reset any current filter on the map
 */
function updateMap(resSearch, resMarker, resFilter) {
  if (searchActive && resSearch) {
    closeInfoWindow();

    searchActive = false;
    searchResults = [];

    $("#" + searchBtn).prop('disabled', true);
    document.getElementById(searchInp).value = "";
    document.getElementById(searchRes).style.display = "none";

    restoreToggles();

    displayState = -1;
  }

  if (markerActive && resMarker) {
    hideInfor();
    closeInfoWindow();

    markerActive = false;
    markerSelected = [];

    restoreToggles();

    displayState = -1;
  }

  if (filterActive && resFilter) {
    $("#" + filterSel).selectpicker('val', '');
    $("#" + filterBtn).prop('disabled', false);
    $("#" + filterRes).prop('disabled', true);

    filterActive = false;
    filterNetworks = [];

    restoreToggles();

    if (searchActive)
      pageSearch();

    displayState = -1;
  }

  displayMarkers();
}

/**
 * Pushes all the lines in the subLines list provided into the data structures
 * for high voltage lines so they can be displayed on the map when necessary.
 *
 * @method pushAllLines
 * @param highLines
 *            An array containing the High Voltage Lines.
 * @param lowLines
 *            An array containing the Low Voltage Lines
 */
function pushAllLines(highLines, lowLines) {
  // Push the lines, and get the starting marker index
  pushLines(highLines, highVoltageLines, highVoltageLineMarkers,
    highVoltageStr, highVoltageStrMarkers, highVoltageLineColor,
    highVoltageLineWeight, highVoltageLineOpacity, highVoltageLineIcon,
    highVolSearch, highConnect);

  pushLines(lowLines, lowVoltageLines, lowVoltageLineMarkers, lowVoltageStr,
    lowVoltageStrMarkers, lowVoltageLineColor, lowVoltageLineWeight,
    lowVoltageLineOpacity, lowVoltageLineIcon, lowVolSearch, lowConnect);
}

/**
 * Pushes google maps line and marker structures into the datasets provided,
 * dependent on the provided settings.
 *
 * @method pushLines
 * @param {Array}
 *            lines Details of lines to create the google maps objects.
 * @param {Array}
 *            pushLine An array to push the google PolyLines too.
 * @param {Array}
 *            pushMarker An array to push the google markers associated with the
 *            lines in pushLine.
 * @param {Array}
 *            pushStrLine An array to push straight versions of the google
 *            PolyLines too.
 * @param {Array}
 *            pushStrMarker An array to push the google markers associated with
 *            the straight lines in pushStrLine too.
 * @param {String}
 *            color The color to make the lines
 * @param {int}
 *            weight The weight to make the lines
 * @param {double}
 *            opacity The opacity of the line
 * @param {String}
 *            icon The path of the icon to use for the marker.
 * @param {int}
 *            type The type of map structure being added.
 * @param {Array}
 *            connections An array to push the connections (start and end node)
 *            to.
 */
function pushLines(lines, pushLine, pushMarker, pushStrLine, pushStrMarker,
  color, weight, opacity, icon, type, connections) {
  var startIndex = markerIdx;
  var typeId = 0;

  if (type == highVolSearch)
    typeId = highVoltageLinesType;
  else if (type == lowVolSearch)
    typeId = lowVoltageLinesType;

  // Loop through the list and push items into data structures
  for (var i = 0; i < lines.length; i++) {
    if (type == highVolSearch)
      connections.push([lines[i][iLId], lines[i][iLStartId], substationType,
        lines[i][iLEndId], substationType, lines[i][iLId], highVolSearch
      ]);
    else
      connections.push([lines[i][iLId], lines[i][iLStartId], substationType,
        lines[i][iLEndId], endpointType, lines[i][iLId], lowVolSearch
      ]);

    // Get the points, make a new line and push it
    var points = getLinePoints(lines[i].slice(iLCoords));
    pushLine.push(getNewLine(points, color, weight, opacity));

    // Straight line.
    var strPoints = [points[0], points[points.length - 1]];
    pushStrLine.push(getNewLine(strPoints, color, weight, opacity));

    // Push the details of a line
    lineIndex.push([lines[i][iLId], i, lines[i][iLName]]);

    // Get the middle of the line, get a marker and push it
    var midPoint = getPolyMidPoint(points);
    var content = getLineMarkerContent(lines[i][iLName], lines[i][iLId],
      midPoint.lat(), midPoint.lng(), type);
    var marker = getNewMarker(midPoint, icon, content, markerIdx);
    pushMarker.push(marker);
    addListenerToMarker(marker, i, lines[i][iLId], type, typeId,
      lines[i][iLName], null);
    markerIdx++;

    // Get the midpoint of the straight line, get a marker and push it
    midPoint = getPolyMidPoint(strPoints);
    content = getLineMarkerContent(lines[i][iLName], lines[i][iLId], midPoint
      .lat(), midPoint.lng(), type);
    marker = getNewMarker(midPoint, icon, content, markerIdx);
    pushStrMarker.push(marker);
    addListenerToMarker(marker, i, lines[i][iLId], type, typeId,
      lines[i][iLName], null);
    markerIdx++;

    pushOntoNetwork(lines[i][iLStartNet], lines[i][iLId], typeId);
    pushOntoNetwork(lines[i][iLEndNet], lines[i][iLId], typeId);
  }
  return startIndex;
}

/**
 * Generates a string appropriate for a Line marker information window.
 *
 * @method getLineMarkerContent
 * @param {String}
 *            name The name of the line
 * @param {int}
 *            id The id of the line (in the database)
 * @param {float}
 *            lat The latitude of the marker
 * @param {float}
 *            lng The longitude of the marker
 * @param {String}
 *            type The (database) type of the line.
 * @return {String} HTML representation of infoWindow content.
 */
function getLineMarkerContent(name, id, lat, lng, type) {
  return '<table class="table table-condensed"><tr><td class="markers-table"><h5>' 
       + '<a href="#" onclick="showInfor(' + id + ',\'' + type + '\',' + lat + ',' 
       + lng + ',\'' + name + '\',null);">' + name + " (Power Line) " 
       + '</a></h5>' + '</td></tr></table>';
}

/**
 * Pushes the sets of nodes provided into their respective data structures.
 * Three sets of nodes are stored: Endpoints only, Substations & Network Centres
 * (grouped at the same location), and all three combined (grouped by location).
 *
 * @method pushAllNodes
 * @param {Array}
 *            subNodes The list of substations
 * @param {Array}
 *            netNodes The list of network centres
 * @param {Array}
 *            endNodes The list of endpoints.
 */
function pushAllNodes(subNodes, netNodes, endNodes) {
  // Get lists replacing the Coords with Google Mapelements.
  var subList = convertNodeCoords(subNodes);
  var netList = convertNodeCoords(netNodes);
  var endList = convertNodeCoords(endNodes);

  // Push endpoint nodes
  pushNodes(endList, endpointNodeMarkers, endpointType);

  // Make a single list of centres and substations
  var allList = subList.slice(0);
  joinLists(allList, netList);
  // Push the substation nodes
  pushNodes(allList, substationNodeMarkers, substationType);

  // Add the endpoints to the list of all nodes then push
  joinLists(allList, endList);
  pushNodes(allList, allNodeMarkers, allNodesType);
}

/**
 * Takes a list of nodes and changes the two distinct lat/lng elements and makes
 * a single element which contains a google maps LatLng representation.
 *
 * @method ConverNodeCoords(nodeList)
 * @param {Array}
 *            nodeList The list of nodes.
 * @param {Array}
 *            A new copy of the list with google LatLng coordinates.
 */
function convertNodeCoords(nodeList) {
  var newList = [];
  for (var i = 0; i < nodeList.length; i++) {
    var node = [];
    node.push(nodeList[i][iNId], nodeList[i][iNName], nodeList[i][iNType],
      nodeList[i][iNNet], nodeList[i][iNImg]);

    var length = nodeList[i].length;

    for (var j = iNLines; j < length - 2; j++)
      node.push(nodeList[i][j]);

    node.push(new google.maps.LatLng(nodeList[i][length - 2],
      nodeList[i][length - 1]));

    newList.push(node);
  }
  return newList;
}

/**
 * Pushes the google maps markers into the data structure for the list of nodes
 * provided.
 *
 * @method pushNodes
 * @param {Array}
 *            nodeList The list of nodes.
 * @param {Array}
 *            pushNode Where to push the google markers.
 * @param {int}
 *            nodeType The type of being created.
 */
function pushNodes(nodeList, pushNode, nodeType) {
  var markerIcon;
  var list = nodeList.slice(0); // Take a local copy

  // Go through each node and add it.
  for (var i = 0; i < list.length; i++) {
    var conns = [];
    var markerNodes = []; // Nodes at the same position

    // Choose the marker type
    if (list[i][iNType] == dbEnd)
      markerIcon = endpointIcon;
    else
      markerIcon = substationIcon;

    markerNodes.push(list[i]); // Node is at this position

    var coord = list[i].length - 1;

    // Check for nodes to add to this marker, then remove them.
    for (var j = (i + 1); j < list.length; j++) {
      var nextCoord = list[j].length - 1;
      if (groupNodes(list[i][coord], list[j][nextCoord])) {
        markerNodes.push(list[j]); // Add it to the marker
        // Node ID reference to index in data arrays if not adding all
        nodeIndex.push([list[j][iNId], i, nodeType]);
        joinLists(conns, list[j].slice(iNLines, nextCoord));
        list.splice(j, 1); // Remove node as it will be added.
        j--;
      }
    }

    overallBounds.extend(list[i][coord]); // For dynamic binding.

    // Node ID reference to index in data arrays, if not adding All
    nodeIndex.push([list[i][iNId], i, nodeType]);

    // Get a new marker, add it to the data list. Add a listener.
    var marker = getNewMarker(list[i][coord], markerIcon,
      getNodeContentString(markerNodes), markerIdx);
    pushNode.push(marker);
    addListenerToMarker(marker, i, list[i][iNId], list[i][iNType], nodeType,
      list[i][iNName], list[i][iNImg]);

    pushOntoNetwork(list[i][iNNet], list[i][iNId], nodeType);

    joinLists(conns, list[i].slice(iNLines, coord));
    joinLists(conns, [list[i][iNId], nodeType]);
    pushNodeConns(nodeType, list[i][iNId], conns);

    markerIdx++; // Increment global marker index.
  }
}

/**
 * Pushes the lines that a node with a given id has as connections into the
 * appropriate data structures.
 *
 * @method pushNodeConns
 * @param {int}
 *            type The type of node being pushed.
 * @param {int}
 *            id The id of the node the lines relate too.
 * @param {Array}
 *            lines The array of lines, that are connected to the node with the
 *            given id.
 */
function pushNodeConns(type, id, lines) {
  var conns = [];

  conns.push(id);
  for (var i = 0; i < lines.length; i++) {
    conns.push(lines[i]);
  }

  if (type == substationType)
    subConnect.push(conns);
  else if (type == endpointType)
    endConnect.push(conns);
  else if (type == allNodesType)
    allConnect.push(conns);
}

/**
 * Pushes an element onto a network.
 *
 * @method pushOntoNetwork
 * @param {int}
 *            network The (database) id of the network to add the item to.
 * @param {int}
 *            id The (database) id of the element to add.
 * @param {int}
 *            type The type of element being added.
 */
function pushOntoNetwork(network, id, type) {
  for (var i = 0; i < networkItems.length; i++) {
    if (network == networkItems[i][0]) {
      networkItems[i][1].push([id, type]);
      return;
    }
  }
  // Haven't pushed yet. New network
  networkItems.push([network, []]);
  networkItems[networkItems.length - 1][1].push([id, type]);
}

/**
 * getNetworkIds()
 *
 * Returns the current filtered network Ids in a String representation. E.g.
 * id1, id2, id3, ...
 */
function getNetworkIds() {
  var nets = "";
  for (var i = 0; i < filterNetworks.length; i++) {
    nets += filterNetworks[i];
    if (!(i == (filterNetworks.length - 1)))
      nets += ", ";
  }
  return nets;
}

/**
 * Retrieves the currently selected networks, and applies the necessary filter.
 *
 * @method applyNetFilter
 */
function applyNetFilter() {
  var options = $("#" + filterSel).val();

  if (options == null && !filterActive)
    return;
  else if (options == null && filterActive)
    updateMap(false, true, true);
  else {
    $("#" + filterBtn).prop('disabled', false);
    $("#" + filterRes).prop('disabled', false);

    filterNetworks = options;

    if (searchActive) {
      filterActive = true;
      pageSearch();
    } else {
      displayState = -1;
      if (filterActive) {
        updateMap(false, false, false);
      } else {
        saveToggles();
        setToggles([true, true, true, true]);
        filterActive = true;
        updateMap(false, false, false);
      }
    }
  }
}

/**
 * Generates a string appropriate for a Node marker information window.
 *
 * @method getNodeContentString
 * @param {Array}
 *            list A list of nodes to add to the window.
 */
function getNodeContentString(list) {
  // Start of the content
  var content = '<table class="table table-condensed">';

  // Add the section for each node
  for (var i = 0; i < list.length; i++) {
    var coord = list[i].length - 1;
    content += '<tr><td class="markers-table"><h5><a href=' + '"#" onclick="showInfor(' 
            + list[i][iNId] + ', \'' + list[i][iNType] + '\',' 
            + list[i][coord].lat() + ',' + list[i][coord].lng() 
            + ',\'' + list[i][iNName] + '\', \'' + list[i][iNImg] 
            + '\');">' + list[i][iNName] + " (" + list[i][iNType] 
            + ") " + '</a></h5></td></tr>';
  }
  // Add the end and return
  return content + '</table>';
}

/**
 * Adds all the elements in list2 to the end of list1.
 *
 * @method joinLists
 * @param {Array}
 *            list1 List to have items added to the end of.
 * @param {Array}
 *            list2 Items to add to the end of list1.
 */
function joinLists(list1, list2) {
  for (var i = 0; i < list2.length; i++) {
    list1.push(list2[i]);
  }
}

/**
 * Determines if two nodes should be grouped together due to their location.
 *
 * @method groupNodes
 * @param {LatLng}
 *            node1 The google LatLng of the first node.
 * @param {LatLng}
 *            node2 The google LatLng of the second node.
 * @return {bool} true: nodes should be on the same marker, false: otherwise.
 */
function groupNodes(node1, node2) {
  if (google.maps.geometry.spherical.computeDistanceBetween(node1, node2) < markerThreshold)
    return true;
  else
    return false;
}

/**
 * getLinePoints(line)
 *
 * Returns an array of google LatLng points generated from a sequential set of
 * points provided at line (e.g. [lat1, lng1, lat2, lng2] would be become:
 * [LatLng1, LatLng2]
 */
function getLinePoints(line) {
  var points = [];
  // For each set of sequential points generate a new LatLng and push it
  for (var i = 0; i < (line.length / 2); i++) {
    points.push(new google.maps.LatLng(parseFloat(line[2 * i]),
      parseFloat(line[(2 * i) + 1])));
  }
  return points;
}

/**
 * getNewMarker(markerPoint, markerIcon, markerContent, markerIdx)
 *
 * Returns a new google maps marker located at markerPoint, with the icon
 * markerIcon, an information window based on the HTML at markerContent, and
 * givern an index markerIdx.
 */
function getNewMarker(markerPoint, markerIcon, markerContent, markerIdx) {
  var marker = new google.maps.Marker({
    map: map,
    position: markerPoint,
    icon: markerIcon,
    infor: markerContent,
    index: markerIdx
  });
  return marker;
}

/**
 * getNewLine(linePoints, lineColor, lineWeight, lineOpacity)
 *
 * Returns a new google maps line, that follows the points given at linePoints
 * has a color of lineColor, weight of lineWeight and opacity of lineOpacity.
 */
function getNewLine(linePoints, lineColor, lineWeight, lineOpacity) {
  var line = new google.maps.Polyline({
    map: map,
    path: linePoints,
    strokeColor: lineColor,
    strokeWeight: lineWeight,
    strokeOpacity: lineOpacity,
  });
  return line;
}

/**
 * Adds a listener to a given marker.
 *
 * @method addListenerToMarker
 * @param {google.maps.Marker}
 *            marker The marker to add the listener to.
 * @param {int}
 *            connectIdx The index of the details of the node at this marker in
 *            the relevant connections store.
 * @param {int}
 *            id The (database) id of the item
 * @param {String}
 *            typeStr The (database) string representation of the item
 * @param {int}
 *            typeId The id of the element type.
 */
function addListenerToMarker(marker, connectIdx, id, typeStr, typeId, name, img) {
  google.maps.event.addListener(marker, 'click', function() {
    closedWindow = false;

    saveToggles();
    setToggles([true, true, true, true]);

    markerSelected = [connectIdx, typeId];
    markerActive = true;
    displayState = -1;
    updateMap(false, false, false);
    dispBoxClosed = false;

    map.setCenter(marker.getPosition());

    showInfor(id, typeStr, marker.position.lat(), marker.position.lng(), name,
      img);
  });
}

/**
 * Sets the toggles to the settings provided.
 *
 * @method setToggles
 * @param {Array}
 *            toggles The toggle settings.
 */
function setToggles(toggles) {
  $('#' + toggSub).bootstrapSwitch('state', toggles[0]);
  $('#' + toggEnd).bootstrapSwitch('state', toggles[1]);
  $('#' + toggHigh).bootstrapSwitch('state', toggles[2]);
  $('#' + toggLow).bootstrapSwitch('state', toggles[3]);
}

/**
 * Saves the current settings and state of the toggles.
 *
 * @method saveToggles()
 */
function saveToggles() {
  var idx = getToggleStateIdx(searchActive, filterActive, markerActive);

  toggleStore[idx] = [$('#' + toggSub).bootstrapSwitch('state'),
    $('#' + toggEnd).bootstrapSwitch('state'),
    $('#' + toggHigh).bootstrapSwitch('state'),
    $('#' + toggLow).bootstrapSwitch('state')
  ];
}

/**
 * Restores the toggles to their saved settings for this state.
 *
 * @method restoreToggles
 */
function restoreToggles() {
  var idx = getToggleStateIdx(searchActive, filterActive, markerActive);
  setToggles(toggleStore[idx]);
}

/**
 * Sets up the toggle store and initialised saved values.
 *
 * @method setupToggles()
 */
function setupToggles() {
  var buckets = getToggleStateIdx(true, true, true);
  for (var i = 0; i <= buckets; i++)
    toggleStore.push([false, false, false, false]);
}

/**
 * Gets the index of the toggle store for a particular state.
 *
 * @method getToggleStateIdx
 * @param {bool}
 *            search True if a search is active.
 * @param {bool}
 *            filter True if a filter is active.
 * @param {bool}
 *            marker True if a marker is active.
 * @return {int} The index in the toggle store for the state.
 */
function getToggleStateIdx(search, filter, marker) {
  if (!search && !filter && !marker)
    return 0;
  else if (search && !filter && !marker)
    return 1;
  else if (search && filter && !marker)
    return 2;
  else if (search && !filter && marker)
    return 3;
  else if (!search && filter && !marker)
    return 4;
  else if (!search && filter && marker)
    return 5;
  else if (!search && !filter && marker)
    return 6;
  else if (search && filter && marker)
    return 7;
  else
    return 8; // Fail safe.
}

/**
 * Adds a listener to close an information window.
 *
 * @method closeWindowListener
 * @param {google.maps.InfoWindow}
 *            window The window to add the listener to.
 */
function closeWindowListener(window) {
  // Add a listener to close the window, and put map back to previous state
  google.maps.event.addListener(window, 'closeclick', function() {
    closedWindow = true;
    updateMap(false, true, false, false, false);
    map.fitBounds(overallBounds);
  });
}

/**
 * Clears all the shapes from the map.
 *
 * @method clearAllShapes
 */
function clearAllShapes() {
  clearShapes(highVoltageLineMarkers);
  clearShapes(highVoltageLines);
  clearShapes(highVoltageStr);
  clearShapes(highVoltageStrMarkers);
  clearShapes(lowVoltageLines);
  clearShapes(lowVoltageLineMarkers);
  clearShapes(lowVoltageStr);
  clearShapes(lowVoltageStrMarkers);
  clearShapes(allNodeMarkers);
  clearShapes(substationNodeMarkers);
  clearShapes(endpointNodeMarkers);
}

/**
 * Clears the given overlays from the map.
 *
 * @method clearShapes
 * @param {google.maps.
 *            Overlays} overlays A list of overlays to clear from the map.
 */
function clearShapes(overlays) {
  for (var i = 0; i < overlays.length; i++) {
    overlays[i].setMap(null);
  }
}

/**
 * Displays a shape by its index in the relevant data structures.
 *
 * @method displayShapeByIndex
 * @param {int}
 *            type The id of the shape/element type
 * @param {int}
 *            idx The index of the shape.
 * @param {bool}
 *            straight Set to true if the displaying staight lines
 * @param {google.maps.LatLngBounds}
 *            bounds The bounds to extend to fit the shape being displayed to.
 */
function displayShapeByIndex(type, idx, straight, bounds) {
  if (type == substationType) {
    substationNodeMarkers[idx].setMap(map);
    bounds.extend(substationNodeMarkers[idx].position);
  } else if (type == endpointType) {
    endpointNodeMarkers[idx].setMap(map);
    bounds.extend(endpointNodeMarkers[idx].position);
  } else if (type == allNodesType) {
    allNodeMarkers[idx].setMap(map);
    bounds.extend(allNodeMarkers[idx].position);
  } else if (type == highVoltageLinesType && straight) {
    highVoltageStr[idx].setMap(map);
    highVoltageStrMarkers[idx].setMap(map);
    extendLineBounds(bounds, highVoltageStr[idx]);
  } else if (type == highVoltageLinesType && !straight) {
    highVoltageLines[idx].setMap(map);
    highVoltageLineMarkers[idx].setMap(map);
    extendLineBounds(bounds, highVoltageLines[idx]);
  } else if (type == lowVoltageLinesType && straight) {
    lowVoltageStr[idx].setMap(map);
    lowVoltageStrMarkers[idx].setMap(map);
    extendLineBounds(bounds, lowVoltageStr[idx]);
  } else if (type == lowVoltageLinesType && !straight) {
    lowVoltageLines[idx].setMap(map);
    lowVoltageLineMarkers[idx].setMap(map);
    extendLineBounds(bounds, lowVoltageLines[idx]);
  }
}

/**
 * Activates (display) all shapes specified on the map.
 *
 * @method activateShapes
 * @param {bool}
 *            sub If the substation(/net) nodes should be displayed
 * @param {bool}
 *            end If the endpoint nodes should be displayed
 * @param {bool}
 *            all If the all nodes should be displayed
 * @param {bool}
 *            highVol If the high voltage lines should be displayed
 * @param {bool}
 *            lowVol If the low voltage lines should be displayed
 * @param {bool}
 *            straight If the straight lines should be displayed
 * @param {bool}
 *            changeBound If the bounds should be changed once all shapes are
 *            activated.
 */
function activateShapes(sub, end, all, highVol, lowVol, straight, changeBound) {
  var newBounds = new google.maps.LatLngBounds();

  if (sub)
    for (var i = 0; i < substationNodeMarkers.length; i++)
      displayShapeByIndex(substationType, i, straight, newBounds);
  if (end)
    for (var i = 0; i < endpointNodeMarkers.length; i++)
      displayShapeByIndex(endpointType, i, straight, newBounds);
  if (all)
    for (var i = 0; i < allNodeMarkers.length; i++)
      displayShapeByIndex(allNodesType, i, straight, newBounds);
  if (highVol)
    for (var i = 0; i < highVoltageLines.length; i++)
      displayShapeByIndex(highVoltageLinesType, i, straight, newBounds);
  if (lowVol)
    for (var i = 0; i < lowVoltageLines.length; i++)
      displayShapeByIndex(lowVoltageLinesType, i, straight, newBounds);

  if (changeBound)
    map.fitBounds(newBounds);
}

/**
 * Prevents the default action on an enter press.
 *
 * @method preventEnter
 */
function preventEnter() {
  if (event.keyCode == 13) {
    event.preventDefault();
  }
}

/**
 * Searches the database for matching items based on the current page status.
 *
 * @method pageSearch
 */
function pageSearch() {
  var type = $("#" + searchSel).val();
  var input = $("#" + searchInp).val().trim();

  if (input == "") {
    if (noResultsTimer != null) {
      clearTimeout(noResultsTimer);
      document.getElementById(searchRes).style.display = "none";
    }
    updateMap(true, false, false);
    return;
  }

  if (filterActive) {
    var nets = getNetworkIds();
    searchByNet(type, input, nets);
  } else
    searchByType(type, input);

}

/**
 * Searches the given string by its type in the database and updates the map to
 * show the results, if no results are found the user is notified.
 *
 * @method searchByType
 * @param {String}
 *            type The search type to be conducted.
 * @param {String}
 *            input The phrase to search.
 */
function searchByType(type, input) {
  $.ajax({
    type: "POST",
    url: "map_search.php",
    data: {
      'input': input,
      'search-type': type
    },
    success: function(data) {
      if (data == "")
        noSearchData(type, input);
      else {
        searchResults = [];
        var temp = JSON.parse(data);
        for (var i = 0; i < temp.length; i++) {
          searchResults.push(temp[i], getTypeID(type));
        }
        saveToggles();
        setToggles([false, false, false, false]);
        searchActive = true;
        displayState = -1;
        updateMap(false, false, false);
        if (google.maps.geometry.spherical.computeDistanceBetween(searchBounds
            .getNorthEast(), searchBounds.getSouthWest()) > minBounds)
          map.fitBounds(searchBounds);
        else {
          map.setCenter(searchBounds.getCenter());
          map.setZoom(zoomedIn);
        }
      }
    }
  });
}

/**
 * Searches the given phrase by its type and network in the database and updates
 * the map to show the results. User notified if no results found.
 *
 * @method searchByNet
 * @param {String}
 *            type The search type to be conducted.
 * @param {String}
 *            input The phrase to search.
 * @param {String}
 *            nets A string of the networks the item can be located in.
 */
function searchByNet(type, input, nets) {
  $.ajax({
    type: "POST",
    url: "map_search.php",
    data: {
      'input': input,
      'search-type': type,
      'network': nets
    },
    success: function(data) {
      if (data == "")
        noSearchData(type, input);
      else {
        searchResults = [];
        var temp = JSON.parse(data);
        for (var i = 0; i < temp.length; i++) {
          searchResults.push(temp[i], getTypeID(type));
        }
        saveToggles();
        setToggles([false, false, false, false]);
        searchActive = true;
        displayState = -1;
        updateMap(false, false, false);
        if (google.maps.geometry.spherical.computeDistanceBetween(searchBounds
            .getNorthEast(), searchBounds.getSouthWest()) > minBounds)
          map.fitBounds(searchBounds);
        else {
          map.setCenter(searchBounds.getCenter());
          map.setZoom(zoomedIn);
        }
      }
    }
  });
}

/**
 * Gets the type/element id given the search string.
 *
 * @method getTypeID
 * @param {String}
 *            typeStr The search string
 * @return {int} The id of the type/element.
 */
function getTypeID(typeStr) {
  if (typeStr == subNetSearch)
    return substationType;
  else if (typeStr == endSearch)
    return endpointType;
  else if (typeStr == highVolSearch)
    return highVoltageLinesType;
  else if (typeStr == lowVolSearch)
    return lowVoltageLinesType;
}

/**
 * Notifies the user if there is no search results.
 *
 * @method noSearchData
 * @param {String}
 *            type The type of search made.
 * @param {String}
 *            input The input of the search.
 */
function noSearchData(type, input) {
  displayResultBox(type, input, searchNone);
  $("#" + searchBtn).prop('disabled', true);
  if (noResultsTimer != null)
    clearTimeout(noResultsTimer);
  noResultsTimer = setTimeout(function() {
    document.getElementById(searchRes).style.display = "none";
    updateMap(true, false, false);
  }, timerLength);
}

/**
 * Extends the bounds by a line, to ensure all points on the line will fit into
 * the given bounds.
 *
 * @method extendLineBounds
 * @param {google.maps.LatLngBounds}
 *            bounds The bounds to extend
 * @param {google.maps.PolyLine}
 *            line The line to extend the line by.
 */
function extendLineBounds(bounds, line) {
  var points = line.getPath().getArray();
  for (var i = 0; i < points.length; i++) {
    bounds.extend(points[i]);
  }
}

/**
 * Returns the index of a line in its structure given the database id.
 *
 * @method getLineIndex
 * @param {int}
 *            id The database id of the line.
 * @return {int} The index of the line in its data structure if found. If the
 *         line is not found, -1 is returned.
 */
function getLineIndex(id) {
  for (var i = 0; i < lineIndex.length; i++) {
    if (id == lineIndex[i][0]) {
      return lineIndex[i][1];
    }
  }
  return -1; // Not Found
}

/**
 * Returns the index of a node in its structure given the database id.
 *
 * @method getNodeIndex
 * @param {int}
 *            id The database id of the node.
 * @param {int}
 *            type The type of node being searched.
 * @return {int} The index of the node in its data structure if found. If the
 *         node is not found, -1 is returned.
 */
function getNodeIndex(id, type) {
  for (var i = 0; i < nodeIndex.length; i++) {
    if (id == nodeIndex[i][0] && type == nodeIndex[i][2]) {
      return nodeIndex[i][1];
    }
  }
  return -1; // Not found
}

/**
 * Gets the icons and title given a particular search type.
 *
 * @method getSearchIcons
 * @param {String}
 *            searchType The type of search
 * @return An object containing two elements: (title) the title of the icon
 *         (image) the page of the image for the icon.
 */
function getSearchIcons(searchType) {
  if (searchType == subNetSearch) {
    return {
      title: dbSub,
      image: substationSearchIcon
    };
  } else if (searchType == endSearch) {
    return {
      title: endSearch,
      image: endpointSearchIcon
    };
  } else if (searchType == highVolSearch) {
    return {
      title: highVolSearch,
      image: highVolSearchIcon
    };
  } else if (searchType == lowVolSearch) {
    return {
      title: lowVolSearch,
      image: lowVolSearchIcon
    };
  } else {
    return {
      title: "",
      image: ""
    };
  }
}

/**
 * Displays the search result box on the screen.
 *
 * @method displayResultBox
 * @param {String}
 *            searchType The type of search
 * @param {String}
 *            searchInput The input of the search
 * @param {String}
 *            message The message to show.
 */
function displayResultBox(searchType, searchInput, message) {
  var result = document.getElementById('result');
  var icons = getSearchIcons(searchType);
  var image = icons.image;
  var title = icons.title;

  result.style.display = "block";
  result.innerHTML = "<div class='search-result'><h4 class=" 
                   + "'search-result-title'>No Result Found.</h4>" 
                   + "<img src='" + image + "' class='img-thumbnail img-result'>" 
                   + "<h4 class='search-result-content'>" + title + ":<br>" 
                   + message + "</h4></div>";
}

/**
 * Displays the required markers on the map, dependent on the current state of
 * the map.
 *
 * @method displaysMarkers
 */
function displayMarkers() {
  // Get the checkbox status
  var selSub = $('#substation').bootstrapSwitch('state');
  var selEnd = $('#endpoint').bootstrapSwitch('state');
  var selHighLines = $('#highVoltageLine').bootstrapSwitch('state');
  var selLowLines = $('#lowVoltageLine').bootstrapSwitch('state');
  var selStrLines = $('#straight-lines').bootstrapSwitch('state');

  var desiredState = (7 * selSub) + (5 * selEnd) + (3 * selHighLines) + (selLowLines);
  var desiredLines = selStrLines;

  if (desiredState == displayState && displayLines == desiredLines)
    return;
  else if (desiredState == 7 && displayState == 7)
    return;
  else if (desiredState == 5 && displayState == 5)
    return;
  else if (desiredState == 12 && displayState == 12)
    return;
  else {
    closeInfoWindow();
    clearAllShapes();
  }

  var selAll = 0;
  if (selEnd && selSub) {
    selSub = 0;
    selEnd = 0;
    selAll = 1;
  }

  if (searchActive) {
    displaySearchResults(selStrLines);
  }
  if (markerActive) {
    displayConnections(markerSelected[0], markerSelected[1], selSub, selEnd,
      selAll, selHighLines, selLowLines, selStrLines);
    joinSetBounds(map.getBounds(), connBounds);
  } else if (filterActive && !searchActive) {
    setNetworks(selSub, selEnd, selAll, selHighLines, selLowLines, selStrLines);
    joinSetBounds(map.getBounds(), networkBounds);
  } else if (!searchActive) {
    activateShapes(selSub, selEnd, selAll, selHighLines, selLowLines,
      selStrLines, !closedWindow);
  }

  displayState = desiredState;
  displayLines = desiredLines;
}

/**
 * Displays a shape on the map given its database id and type.
 *
 * @method displayShapeById
 * @param {int}
 *            type The type of the element.
 * @param {int}
 *            id The (database) id of the element.
 * @param {bool}
 *            straight If straight lines should be shown.
 * @param {google.maps.LatLngBounds}
 *            bounds The boundaries to extend to ensure the shape fits.
 */
function displayShapeById(type, id, straight, bounds) {
  var idx;
  if (type == substationType || type == endpointType || type == allNodesType)
    idx = getNodeIndex(id, type);
  else
    idx = getLineIndex(id);

  displayShapeByIndex(type, idx, straight, bounds);
}

/**
 * Displays the current search results on the map.
 *
 * @method displaySearchResults
 * @param {bool}
 *            straight If straight lines should be displayed.
 */
function displaySearchResults(straight) {
  searchBounds = new google.maps.LatLngBounds();
  for (var i = 0; i < searchResults.length; i += 2)
    displayShapeById(searchResults[i + 1], searchResults[i], straight,
      searchBounds);
}

/**
 * Displays the connections of a particular type item.
 *
 * @method displayConnections
 * @param {int}
 *            idx The index of the item in its data connection structure.
 * @param {int}
 *            type The type of the item being displayed.
 * @param {bool}
 *            setSub If the substation connections are being displayed.
 * @param {bool}
 *            setEnd If the endpoint connections are being displayed.
 * @param {bool}
 *            setAll If the all node connections are being displayed.
 * @param {bool}
 *            setHigh If the connected high voltage lines are being displayed.
 * @param {bool}
 *            setLow If the connected low voltage lines are being displayed.
 * @param {bool}
 *            straight If straight lines are being displayed.
 */
function displayConnections(idx, type, setSub, setEnd, setAll, setHigh, setLow,
  straight) {
  var conns = [];
  var id = -1;

  if (type == allNodesType) {
    conns = allConnect[idx].slice(1);
    id = allConnect[idx][0];
  } else if (type == substationType) {
    conns = subConnect[idx].slice(1);
    id = subConnect[idx][0];
    if (setAll)
      type = allNodesType;
  } else if (type == endpointType) {
    conns = endConnect[idx].slice(1);
    id = endConnect[idx][0];
    if (setAll)
      type = allNodesType;
  } else if (type == highVoltageLinesType) {
    conns = highConnect[idx].slice(1);
    id = highConnect[idx][0];
  } else if (type == lowVoltageLinesType) {
    conns = lowConnect[idx].slice(1);
    id = lowConnect[idx][0];
  }

  setConnections(conns, setSub, setEnd, setAll, setHigh, setLow, straight);

  setActiveInfo(id, type, straight);
}

/**
 * Opens an information window for the active node given.
 *
 * @method setActiveInfo
 * @param {int}
 *            id The (database) id of the active item.
 * @param {int}
 *            type The id of the type of item.
 * @param {bool}
 *            straight If straight lines are being displayed.
 */
function setActiveInfo(id, type, straight) {
  displayShapeById(type, id, straight, connBounds);
  var idx;

  if (type == substationType || type == endpointType || type == allNodesType)
    idx = getNodeIndex(id, type);
  else
    idx = getLineIndex(id);

  if (type == substationType)
    openNewInfo(substationNodeMarkers[idx]);
  else if (type == endpointType)
    openNewInfo(endpointNodeMarkers[idx]);
  else if (type == allNodesType)
    openNewInfo(allNodeMarkers[idx]);
  else if (type == highVoltageLinesType && straight)
    openNewInfo(highVoltageStrMarkers[idx]);
  else if (type == highVoltageLinesType && !straight)
    openNewInfo(highVoltageLineMarkers[idx]);
  else if (type == lowVoltageLinesType && straight)
    openNewInfo(lowVoltageStrMarkers[idx]);
  else if (type == lowVoltageLinesType && !straight)
    openNewInfo(lowVoltageLineMarkers[idx]);
}

/**
 * Given a set of connections it will display these on the map.
 *
 * @method setConnections
 * @param {Array}
 *            conns The connections to display
 * @param {bool}
 *            setSub If substations are being displayed.
 * @param {bool}
 *            setEnd If endpoints are being displayed.
 * @param {bool}
 *            setAll If all nodes are being displayed.
 * @param {bool}
 *            setHigh If high voltage lines are being displayed.
 * @param {bool}
 *            setLow If low voltage lines are being displayed.
 * @param {bool}
 *            straight If straight lines are being displayed.
 */
function setConnections(conns, setSub, setEnd, setAll, setHigh, setLow,
  straight) {
  connBounds = new google.maps.LatLngBounds();
  for (var i = 0; i < conns.length; i += 2) {
    var type = conns[i + 1];
    var id = conns[i];
    if (type == substationType && setSub)
      displayShapeById(type, id, straight, connBounds);
    else if (type == substationType && setAll)
      displayShapeById(allNodesType, id, straight, connBounds);
    else if (type == endpointType && setEnd)
      displayShapeById(type, id, straight, connBounds);
    else if (type == endpointType && setAll)
      displayShapeById(allNodesType, id, straight, connBounds);
    else if (type == allNodesType && setAll)
      displayShapeById(type, id, straight, connBounds);
    else if (type == highVolSearch && setHigh)
      displayShapeById(highVoltageLinesType, id, straight, connBounds);
    else if (type == lowVolSearch && setLow)
      displayShapeById(lowVoltageLinesType, id, straight, connBounds);
  }
}

/**
 * Opens a new information window attached to the given marker.
 *
 * @method openNewInfo
 * @param {google.maps.Marker}
 *            newMark The marker which should have its information window
 *            opened.
 */
function openNewInfo(newMark) {
  closeInfoWindow();
  infoWindow = new google.maps.InfoWindow({
    content: newMark.infor,
    disableAutoPan: false,
  });
  infoWindow.open(map, newMark);
  closeWindowListener(infoWindow);
  infoWindowState = true;
}

/**
 * Closes the current (if open) information window.
 *
 * @method closeInfoWindow
 */
function closeInfoWindow() {
  if (infoWindowState) {
    infoWindow.close();
    infoWindowState = false;
    dispBoxClosed = true;
    closedWindow = true;
  }
}

/**
 * Displays all items on the current active networks if they are to be
 * displayed.
 *
 * @method setNetworks
 * @param {bool}
 *            setSub If substations are being displayed.
 * @param {bool}
 *            setEnd If endpoints are being displayed.
 * @param {bool}
 *            setAll If all nodes are being displayed.
 * @param {bool}
 *            setHigh If high voltage lines are being displayed.
 * @param {bool}
 *            setLow If low voltage lines are being displayed.
 * @param {bool}
 *            straight If straight lines are being displayed.
 */
function setNetworks(setSub, setEnd, setAll, setHigh, setLow, straight) {
  networkBounds = new google.maps.LatLngBounds();
  for (var h = 0; h < filterNetworks.length; h++) {
    for (var i = 0; i < networkItems.length; i++) {
      if (filterNetworks[h] == networkItems[i][0]) {
        for (var j = 0; j < networkItems[i][1].length; j++) {
          var type = networkItems[i][1][j][1];
          var id = networkItems[i][1][j][0];
          if (type == substationType && setSub)
            displayShapeById(type, id, straight, networkBounds);
          if (type == endpointType && setEnd)
            displayShapeById(type, id, straight, networkBounds);
          if (type == allNodesType && setAll)
            displayShapeById(type, id, straight, networkBounds);
          if (type == highVoltageLinesType && setHigh)
            displayShapeById(type, id, straight, networkBounds);
          if (type == lowVoltageLinesType && setLow)
            displayShapeById(type, id, straight, networkBounds);
        }
        break;
      }
    }
  }
}

/**
 * Sets new boundaries of the map which are a union of the boundaries provided,
 * provided the new boundaries doesn't violate zoom settings.
 *
 * @method joinSetBounds
 * @param {google.maps.LatLngBounds}
 *            bounds1 The first set of bounds
 * @param {google.maps.LatLngBounds}
 *            bounds2 The second set of bounds
 */
function joinSetBounds(bounds1, bounds2) {
  if (bounds1 == null)
    return;

  if (bounds1.contains(bounds2.getNorthEast()) && bounds1.contains(bounds2.getSouthWest()))
    return;

  var joinBounds = bounds1.union(bounds2);

  if (google.maps.geometry.spherical.computeDistanceBetween(joinBounds
      .getNorthEast(), joinBounds.getSouthWest()) > minBounds)
    map.fitBounds(joinBounds);
}

/**
 * Converts a given co-ordinate to a degree, hour, minute format.
 *
 * @method toDMS
 * @param (float) dec The decimal format of the co-ordinate
 * @return String The co-ordinate as deg hour min
 */
function toDMS(dec) {
  var deg = Math.floor(Math.abs(dec));
  var hour = Math.floor((Math.abs(dec) - deg) * 60);
  var min = Math.floor(((Math.abs(dec) - deg) * 60 - hour) * 60);

  return (deg + '\xB0' + hour + '\'' + min + '\"');
}

/**
 * Convers a date to a string representation
 *
 * @method convertDateToString
 * @param {Date} date The date to convert to a string
 * @return {String} A string representation of the date provided.
 */
function convertDateToString(date) {
  var utcDate = new Date(Date.UTC(date.getUTCFullYear(), date.getUTCMonth(),
    date.getUTCDate(), date.getUTCHours(), date.getUTCMinutes(), date
    .getUTCSeconds()));
  return (utcDate.getUTCFullYear() + "-" 
    + (utcDate.getUTCMonth() + 1) + "-" + utcDate.getUTCDate() + " " 
    + utcDate.getUTCHours() + ":" + utcDate.getUTCMinutes() + ":" 
    + utcDate.getUTCSeconds());
}

/**
 * Gets the base path of the url.
 *
 * @method getBasePath
 * @return {String} The base path of the url
 */
function getBasePath() {
  var a = document.createElement('a');
  a.href = document.baseURI;
  return a.pathname.slice(0, a.pathname.lastIndexOf("/") + 1);
}

/**
 * Method that handles the Raw or Processed Data switch being toggled. That is,
 * it updates the active details, and displays the necessary graph and values.
 *
 * @method switchRawProc
 */
function switchRawProc() {
  // Set the new original values
  activeDetails[downloadType] = activeDetails[downloadOrigType];
  activeDetails[downloadIds] = activeDetails[downloadOrigIds];

  // Reset the values if not doing a comparison
  if (!document.getElementById("nodeComparison").checked) {
    resetLatestValues("latestVoltagesTable");
    resetLatestValues("latestCurrentsTable");
  }

  // Set the downloadType if it has not already been changed
  if (activeDetails[downloadType] == dbSub 
    || activeDetails[downloadType] == dbEnd 
    || activeDetails[downloadType] == dbNet)
    activeDetails[downloadType] = "node";
  else if (activeDetails[downloadType] == highVolSearch 
    || activeDetails[downloadType] == lowVolSearch)
    activeDetails[downloadType] = "line";

  // Determine type of data to request/select
  if ($('#processedOrRawData').bootstrapSwitch('state')) {
    checkDownloadAvail();
  } else {
    checkRawAvail();
  }

  // Clear any current downloads
  clearDownload();
}

/**
 * Requests the meter ids attached to the active download type and ids. Once
 * received, it sets the active type to a meter, with necessary ids, and
 * display the necessary information (e.g. download fields)
 *
 * @method setMeterDownload
 */
function setMeterDownload() {
  $.ajax({
    type: "POST",
    url: "datacheck.php",
    data: {
      'id': activeDetails[downloadIds],
      'type': activeDetails[downloadType],
      'meter': 'true',
      'returnMeter': 'true'
    },
    success: function(data) {
      activeDetails[downloadType] = 'meter';
      activeDetails[downloadIds] = data;
      showDownFields(false);
    }
  });
}

/**
 * Checks if there is raw data available for the active type and id.  If
 * the data is available, it will update the active type to meters, and the
 * attached ids.  It will also display the necessary fields and appropriate
 * graph.
 *
 * @method checkRawAvail
 */
function checkRawAvail() {
  if (!dispBoxClosed)
    $.ajax({
      type: "POST",
      url: "datacheck.php",
      data: {
        'id': activeDetails[downloadIds],
        'type': activeDetails[downloadType],
        'meter': 'true'
      },
      success: function(data) {
        if (data == "") { // No data hide the download fields
          hideDownFields();
        } else {
          var temp = JSON.parse(data);
          activeDetails[downloadFrom] = new Date(temp[0] + ' UTC');
          activeDetails[downloadTo] = new Date(temp[1] + ' UTC');
          setMeterDownload();
        }
      }
    });
}

/**
 * Checks if there is data available for the active type and id.  If there
 * is no processed data, it will then check for raw data. It will aim to
 * firstly display the processed download information and graph, but will
 * default back to raw data if necessary.
 *
 * @method checkDownloadAvail
 */
function checkDownloadAvail() {
  // Set the downloadType
  if (activeDetails[downloadType] == dbSub 
    || activeDetails[downloadType] == dbEnd 
    || activeDetails[downloadType] == dbNet)
    activeDetails[downloadType] = "node";
  else if (activeDetails[downloadType] == highVolSearch 
    || activeDetails[downloadType] == lowVolSearch)
    activeDetails[downloadType] = "line";
  // Ensure the display hasn't been closed.
  if (!dispBoxClosed)
    $.ajax({
      type: "POST",
      url: "datacheck.php",
      data: {
        'id': activeDetails[downloadIds],
        'type': activeDetails[downloadType]
      },
      success: function(data) {
        // Hide the toggles
        document.getElementById("checkboxes").style.display = "none";
        // Hide any current download fields
        hideDownFields();
        // Show the information window if the display hasn't been closed
        if (!dispBoxClosed)
          showInfoWindow(true);
        if (data == "") { // Do a raw data check if no processed data
          checkRawAvail();
        } else {
          // Take necessary actions with the data
          var temp = JSON.parse(data);
          activeDetails[downloadFrom] = new Date(temp[0] + ' UTC');
          activeDetails[downloadTo] = new Date(temp[1] + ' UTC');
          showDownFields(true);
        }
      }
    });
}

/**
 * Given the id of a datetime picker element, it will set the max, min and
 * selected dates.  If one of these should not be set, simply provide null.
 *
 * @method setDatePicker
 * @param {String} id The id of the HTML element
 * @param {String Date} from The date or string representation of the minimum
 * date that can be selected
 * @param {String Date} to The date or string representation of the maximum
 * date that can be selected
 * @param {String Date} selected The date or string representation to set th
 * picker to.
 */
function setDatePicker(id, from, to, selected) {
  $('#' + id).data("DateTimePicker").setMinDate(from);
  $('#' + id).data("DateTimePicker").setMaxDate(to);
  $('#' + id).data("DateTimePicker").setDate(selected);
}

/**
 * Shows the download fields, and updates the date time pickers for the graph
 * and download fields with the appropriate dates for the current active item.
 *
 * @method showDownFields
 * @param {bool} procState The state to set the processed or raw data switch
 * to (true for on, false for off).
 */
function showDownFields(procState) {
  var fromDate = activeDetails[downloadFrom];
  var toDate = activeDetails[downloadTo];

  // Set all the dates
  setDatePicker('downloadFromDate', fromDate, toDate, fromDate);
  setDatePicker('downloadToDate', fromDate, toDate, toDate);
  setDatePicker('graphFromDate', fromDate, toDate, null);
  setDatePicker('graphToDate', fromDate, toDate, null);

  // Set the toggle
  $('#processedOrRawData').bootstrapSwitch('state', procState);

  // Display the live values and download history boxes
  document.getElementById("nodeValues").style.display = "block";
  document.getElementById("downHist").style.display = "block";

  // Show the graph window (as there is data)
  if (!dispBoxClosed)
    showGraphWindow(true);
  initializeShowGraph(); // Start the graph
}

/**
 * Hides the download fields
 *
 * @method hideDownFields
 */
function hideDownFields() {
  document.getElementById("downHist").style.display = "none";
  document.getElementById("nodeValues").style.display = "none";
  showGraphWindow(false);
}

/**
 * Shows the graph window for an element
 *
 * @method showGraphWindow(show, lat, lng)
 * @param {int}
 *            1: should be display information window 0: should be hide
 *            information window
 * @param {float}
 *            lat The latitude of the element.
 * @param {float}
 *            lng The longitude of the element.
 */
function showGraphWindow(show) {
  var newCenter = map.getCenter();
  if (show) {
    document.getElementById("graph").style.display = "block";
    // if width of window > 1000
    if ($(window).width() >= 1000) {
      var mapHeight = Math.floor(($(window).height() * 0.84 - 340) / 
        ($(window).height() * 0.84) * 100);
      if ($(window).height() > 800) {
        // if height of window > 800
        document.getElementById('map').style.width = '69%';
      } else if ($(window).height() > 700) {
        // if height of window > 700
        document.getElementById('map').style.width = '61%';
        document.getElementById('chartContainer').style.width = '62%';
        document.getElementById('contentContainer').style.width = '36%';
      } else {
        // if height of window <= 700
        document.getElementById('map').style.width = '59%';
        document.getElementById('chartContainer').style.width = '60%';
        document.getElementById('chartContainer').style.height = '200px';
        document.getElementById('contentContainer').style.width = '38%';
        document.getElementById('contentContainer').style.height = '200px';
      }
      document.getElementById('map').style.height = mapHeight + '%';
      document.getElementById('collapseOnePanel').style.maxHeight 
        = mapHeight / 100 * $(window).height() - 43 + 'px';
    }
    // if change size of map
    google.maps.event.trigger(map, "resize");
    // if size of map height > 1000 after changed
    if ($(window).height() > 1000) {
      map.setCenter(new google.maps.LatLng(activeDetails[downloadLat],
        activeDetails[downloadLng]));
    } else if ($(window).height() <= 1000 && $(window).height() > 768) {
      // if size of map height > 768 and <= 1000 after changed
      // offset map postion
      offsetCenter(new google.maps.LatLng(activeDetails[downloadLat],
        activeDetails[downloadLng]), 0, -70);
    } else {
      // if size of map height >= 768 after changed
      // offset map postion
      offsetCenter(new google.maps.LatLng(activeDetails[downloadLat],
        activeDetails[downloadLng]), -30, -110);
    }
  } else {
    document.getElementById("graph").style.display = "none";
    if ($(window).height() < 700) {
      document.getElementById('map').style.height = '80%';
    } else {
      document.getElementById('map').style.height = '84%';
    }
    // if size of map changed, reset map center
    google.maps.event.trigger(map, "resize");
    map.setCenter(newCenter);
  }
}

/**
 * Resets the latest values table with the given id.  The rest will hide the
 * table and then remove all of the rows (except the header).
 *
 * @method resetLatestValues
 * @param {String} tableName The HTML id of the table containing the values
 */
function resetLatestValues(tableName) {
  // Hide the table
  document.getElementById(tableName).style.display = "none";

  // Ensure the table exists and has rows.
  if (document.getElementById(tableName) == null 
    || document.getElementById(tableName).rows == null) return;

  // Get the number of rows
  var startLength = document.getElementById(tableName).rows.length;

  // Continuously delete the last row (doesn't delete header)
  for (var i = 1; i < startLength; i++) {
    document.getElementById(tableName).deleteRow(-1);
  }
}

/**
 * Shows the information window for an element
 *
 * @method showInfoWindow(show)
 * @param {int}
 *            1: should be display information window 0: should be hide
 *            information window
 */
function showInfoWindow(show) {
  var newCenter = map.getCenter();
  if (show) {
    document.getElementById("information").style.display = "block";
    var height = $('#map').height();
    // if width of window > 1000
    if ($(window).width() >= 1000) {
      if ($(window).height() > 800) {
        document.getElementById('map').style.width = '69%';
      }
      if ($(window).height() > 700) {
        document.getElementById('map').style.width = '61%';
      } else {
        document.getElementById('map').style.width = '59%';
      }
      // set max height of information window cannot more than height of map
      document.getElementById("collapseOnePanel").style.maxHeight = height - 43 + "px";
    }

  } else {
    document.getElementById('map').style.width = '100%';
    document.getElementById("information").style.display = "none";
  }
  // if size of map changed, reset map center
  google.maps.event.trigger(map, "resize");
  map.setCenter(newCenter);
}

/**
 * Shows the information panel for an element
 *
 * @method showInfor
 * @param {int}
 *            id The (database) id of the element.
 * @param {String}
 *            type The (database) type of the element.
 * @param {float}
 *            lat The latitude of the element.
 * @param {float}
 *            lng The longitude of the element.
 * @param {int}
 *        The path to the image to display
 */
function showInfor(id, type, lat, lng, name, img) {
  // Get the direction for the co-ordinate
  name = typeof name !== 'undefined' ? name : "";
  var latDir = "N";
  var lngDir = "E";
  if (lat < 0) {
    latDir = "S";
  }
  if (lng < 0) {
    lngDir = "W";
  }

  // Clear the live values if not doing a comparison
  if (!document.getElementById("nodeComparison").checked) {
    resetLatestValues("latestVoltagesTable");
    resetLatestValues("latestCurrentsTable");
  }

  // Clear any current downloads
  clearDownload();

  // Show the node name and co-ordinates
  document.getElementById("collapseOneNodeId").innerHTML = "" + name;
  document.getElementById("collapseOneGPSCoordss").innerHTML = "Latitude: " 
    + toDMS(lat) + latDir + "<br>Longitude: " + toDMS(lng) + lngDir;

  // Display the image if available
  if (img != null && img != "null") {
    document.getElementById("collapseOneImage").setAttribute("src",
      getBasePath() + img);
    document.getElementById("imgDetails").style.display = "block";
  } else {
    document.getElementById("collapseOneImage").removeAttribute("src");
    document.getElementById("imgDetails").style.display = "none";
  }

  // Set the active details
  activeDetails = new Object();
  activeDetails[downloadType] = type;
  activeDetails[downloadOrigType] = type;
  activeDetails[downloadIds] = id.toString();
  activeDetails[downloadOrigIds] = id.toString();
  activeDetails[downloadLat] = lat;
  activeDetails[downloadLng] = lng;

  // Check for data, update fields, show graph
  checkDownloadAvail();
}

/**
 * Hides the information panel.
 *
 * @method hideInfor
 */
function hideInfor() {
  // Clear the graph
  dispBoxClosed = true;
  graphIsActive = false;
  clearInterval(refreshIntervalId);
  graphClosed = true;
  document.getElementById("checkboxes").style.display = "block";
  showInfoWindow(false);
  showGraphWindow(false);
}

/**
 * Returns the set of coordinates that is midway along a path of given points.
 *
 * @method getPolyMidPoint
 * @param {Array
 *            <gogole.maps.LatLng>} points An array of the points in order from
 *            start --> end.
 * @return {google.maps.LatLng} The midway point.
 */
function getPolyMidPoint(points) {
  // Only two points. Just return midway distance
  if (points.length == 2) {
    return getCoordsByDist(points[0], points[1], getDistance(points[0],
      points[1]) / 2);
  }

  // Halfway 'distance'
  var midDistance = 0;
  // Measure total distance between all points
  for (var i = 0; i < points.length - 1; i++) {
    midDistance += getDistance(points[i], points[i + 1]);
  }
  // Divide by 2 to get halfway
  midDistance = midDistance / 2;

  /*
   * Go through and find the two points which midway point lies between Return
   * coords of distance along that line
   */
  var currDistance = 0;
  for (var i = 0; i < points.length - 1; i++) {
    var distance = getDistance(points[i], points[i + 1]);

    if ((currDistance + distance) > midDistance) {
      return getCoordsByDist(points[i], points[i + 1], midDistance - currDistance);
    }
    currDistance += distance;
  }

  // Shouldn't every really get here
  return points[0];
}

/**
 * Gets the coordinate that is the given distance between away from point in the
 * direction of another.
 *
 * @method getCoordsByDist
 * @param {google.maps.LatLng}
 *            start Where to start calculating the distance from
 * @param {google.maps.LatLng}
 *            end The point to head towards when calculating the distance.
 * @param {float}
 *            distance The distance of the desired point from start towards end.
 * @return {google.maps.LatLng} The coordinate the given distance between the
 *         given two points.
 */
function getCoordsByDist(start, end, distance) {
  var dLat = end.lat() - start.lat();
  var dLng = end.lng() - start.lng();
  var dLatAbs = Math.abs(dLat);
  var dLngAbs = Math.abs(dLng);

  var length = Math.sqrt(Math.pow(dLatAbs, 2) + Math.pow(dLngAbs, 2));
  var latStep = dLat / (length / distance);
  var lngStep = dLng / (length / distance);

  return new google.maps.LatLng(start.lat() + latStep, start.lng() + lngStep);
}

/**
 * Gets the distance between two points.
 *
 * @method getDistance
 * @param {google.maps.LatLng}
 *            start The first point.
 * @param {google.maps.LatLng}
 *            end The second point.
 */
function getDistance(start, end) {
  var dLat = start.lat() - end.lat();
  var dLng = start.lng() - end.lng();
  return Math.sqrt(Math.pow(dLat, 2) + Math.pow(dLng, 2));
}

/**
 * set center with offset value
 *
 * @method offsetCenter
 * @param {google.maps.LatLng}
 *            original center position
 * @param {int}
 *            offsetx is the distance that point to move to the
 *            right(positive)/left(negative) in pixels
 * @param {int}
 *            offsety is the distance that point to move
 *            upwards(positive)/downwards(negative) in pixels
 */
function offsetCenter(latlng, offsetx, offsety) {
  var scale = Math.pow(2, map.getZoom());
  var nw = new google.maps.LatLng(map.getBounds().getNorthEast().lat(), map
    .getBounds().getSouthWest().lng());

  var worldCoordinateCenter = map.getProjection().fromLatLngToPoint(latlng);
  var pixelOffset = new google.maps.Point((offsetx / scale) || 0, (offsety / scale) || 0);
  var worldCoordinateNewCenter = new google.maps.Point(worldCoordinateCenter.x 
    - pixelOffset.x, worldCoordinateCenter.y + pixelOffset.y);

  var newCenter = map.getProjection().fromPointToLatLng(
    worldCoordinateNewCenter);
  map.setCenter(newCenter);
}

/**
 * display target div
 *
 * @method showGraphOption(option)
 * @param {int}
 *            1: display div which id is graph-option, 0: display div which id
 *            is graph-date
 */
function showGraphOption(option) {
  if (option) {
    document.getElementById("graph-option").style.display = "block";
    document.getElementById("graph-date").style.display = "none";
  } else {
    document.getElementById("graph-option").style.display = "none";
    document.getElementById("graph-date").style.display = "block";
  }
}