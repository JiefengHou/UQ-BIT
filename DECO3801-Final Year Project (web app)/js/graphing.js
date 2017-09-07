// The data sets
var dps = {};
// The current ids to be graphed
var node_ids = {};
var line_ids = {};
var meter_ids = {};
// Status of queries currently queued.
var waiting_response_voltage = false;
var waiting_response_current = false;
var waiting_response_meters = false;
// A tracking method for the initial requests
var initialRequests = 0;
var requestsReturned = 0;
// The current ids, in a string format
var node_ids_string = "";
var line_ids_string = "";
var meter_ids_string = "";
// The refresh timer
var refreshIntervalId;
// The status of the graph
var graphIsActive = false;
var graphClosed = false;
// The chart to draw on
var chart;
// The interval to select points at
var interval;
// The current start date of the graph
var graphStartDate;
// The details to limit record selection
var maxRecords = 2200;
var recordInterval = 5; // seconds

newNodeIDString();
newLineIDString();

/**
 * Converts a given date to a UTC date.
 *
 * @method dateToUTC
 * @param {Date}
 *            date the date to convert to a UTC date
 * @return {Date} A date in the UTC format
 */
function dateToUTC(date) {
  return new Date(Date.UTC(date.getUTCFullYear(), date.getUTCMonth(), date
    .getUTCDate(), date.getUTCHours(), date.getUTCMinutes(), date
    .getUTCSeconds()));
}

/**
 * Converts a given date to a UTC String format.
 *
 * @method convertDateToUTCString
 * @param {Date}
 *            date The date to convert to UTC string
 * @return {String} The string repesentation of the date in UTC format.
 */
function convertDateToUTCString(date) {
  var utcDate = dateToUTC(date);
  return (utcDate.getUTCFullYear() + "-" + (utcDate.getUTCMonth() + 1) 
  	+ "-" + utcDate.getUTCDate() + " " + utcDate.getUTCHours() + ":" 
  	+ utcDate.getUTCMinutes() + ":" + utcDate.getUTCSeconds());
}

/**
 * Based on the current fields set of the page calls the functions to generate a
 * csv to donwload. Once reading, a getFile button is provided that will call a
 * script to download a button.
 *
 * @method downloadCSV
 */
function downloadCsv() {
  // Get the filtered dates
  var fromDate = $("#downloadFromDate").data("DateTimePicker").getDate();
  var toDate = $("#downloadToDate").data("DateTimePicker").getDate();
  if (fromDate != null)
    fromDate = convertDateToString(fromDate._d);
  if (toDate != null)
    toDate = convertDateToString(toDate._d);

  // Get the current ids active
  var id = activeDetails[downloadIds];

  // Get the current type of those ids.
  var req = "";
  if (activeDetails[downloadType] == "line") {
    req = "getcsv.php?lines=" + id + "&fromDate=" + fromDate + "&toDate=" + toDate;
  } else if (activeDetails[downloadType] == "node") {
    req = "getcsv.php?nodes=" + id + "&fromDate=" + fromDate + "&toDate=" + toDate;
  } else if (activeDetails[downloadType] == "meter") {
    req = "getcsv.php?meters=" + id + "&fromDate=" + fromDate + "&toDate=" + toDate;
  }

  // Change the download button to loading
  $('#prepareDownBtn').button('loading');
  // Send a request to generate the file.
  $.ajax({
    url: req,
    success: function(file) {
      // Once generated hide the download button
      document.getElementById('prepareDownBtn').style.display = "none";
      // Show the get file button and add the link.
      document.getElementById("downloadLink").style.display = "block";
      document.getElementById("downloadLink").setAttribute("href",
        "downloadfile.php?file=" + file);

      // Reset the download button
      $('#prepareDownBtn').button('reset');
    }
  });

}

/**
 * Clears a current download. That is remove the get File button and add the
 * download button
 *
 * @method clearDownload
 */
function clearDownload() {
  document.getElementById("downloadLink").style.display = "none";
  document.getElementById('prepareDownBtn').style.display = "block";
}

/**
 * Checks if the JSON array contains the node_id or line_id parsed
 *
 * @method jsonContainsNodeLine
 * @param {int}
 *            id The (database) id of the element
 * @param {string}
 *            reading_type The (database) reading type of the element
 * @param {string}
 *            type Either 'node_id' or 'line_id'
 * @param {object}
 *            jsonObj The json array to search through
 */
function jsonContainsNodeLine(id, type, jsonObj) {
  for (var i = 0; i < jsonObj.length; ++i) {
    if (jsonObj[i][type] == id) {
      return true;
    }
  }
  return false;
}

/**
 * Creates a new chart for the given type.
 *
 * @method newChart
 * @param {String}
 *            type The type of chart (tile) "processed" or "raw"
 */
function newChart(type) {
  // Set the chart title given the type
  var titleString = "Live Processed Data";
  if (type == "raw")
    titleString = "Live Raw Data";

  // Create the chart and set axis, etc.
  chart = new CanvasJS.Chart("chartContainer", {
    title: {
      text: titleString
    },
    legend: {
      cursor: "pointer",
      itemclick: function(e) {
        if (typeof(e.dataSeries.visible) === "undefined" || e.dataSeries.visible) {
          e.dataSeries.visible = false;
        } else {
          e.dataSeries.visible = true;
        }

        chart.render();
      }
    },
    axisX: {
      title: "Timestamp"
    },
    axisY: {
      title: "Voltage (V)",
      includeZero: false
    },
    axisY2: {
      title: "Current (A)",
      includeZero: false
    },
    zoomEnabled: true,
    panEnabled: true,
    data: []
  });
}

/**
 * Clears the current graph and reset the ids, data points, interval and any
 * waiting periods currently being served.
 *
 * @method clearGraph
 * @param {String}
 *            type Once cleared make a new chart of this type
 */
function clearGraph(type) {
  dps = {};
  node_ids = {};
  line_ids = {};
  meter_ids = {};
  waiting_response_voltage = false;
  waiting_response_current = false;
  waiting_response_meter = false;
  node_ids_string = "";
  line_ids_string = "";
  meter_ids_string = "";
  interval = -1;

  clearInterval(refreshIntervalId);
  newChart(type);
  newNodeIDString();
  newLineIDString();
  newMeterIDString();
  graphIsActive = false;
}

/**
 * Adds a set of daata points to the current chart.
 *
 * @methodn addDataStreamToChart
 * @param {String}
 *            dpsname The name of the datapoints to plot
 * @param {String}
 *            type The type of reading (voltage or current)
 */
function addDataStreamToChart(dpsname, type) {
  if (type == "voltage") {
    var data = {
      type: "line",
      name: dpsname,
      showInLegend: true,
      legendText: dpsname,
      xValueType: "dateTime",
      dataPoints: dps[dpsname].dps
    };
    chart.options.data.push(data);
  } else if (type == "current") {
    var data = {
      type: "line",
      name: dpsname,
      showInLegend: true,
      axisYType: "secondary",
      legendText: dpsname,
      xValueType: "dateTime",
      dataPoints: dps[dpsname].dps
    };
    chart.options.data.push(data);
  }
}

/**
 * Adds a given id and its associated phase to a data point series, for a given
 * type.
 *
 * @method addToDPS
 * @param {String}
 *            id The id of the item
 * @param {String}
 *            phase The phase being plotted
 * @param {String}
 *            type The type of reading (voltage or current)
 */
function addToDPS(id, phase, type) {
  var tmpdate = new Date(0);
  var tmputcDate = dateToUTC(tmpdate);
  dps[id + phase] = {};
  dps[id + phase].phase = phase;
  dps[id + phase].dps = [];
  dps[id + phase].lastdate = tmputcDate;
  addDataStreamToChart(id + phase, type);
}

/**
 * Adds a new node to the current chart, creating a new data series for the id
 * and phase provided.
 *
 * @method addNode
 * @param {String}
 *            nodeId The id of the node
 * @param {String}
 *            phase The name of the phase
 */
function addNode(nodeId, phase) {
  var tmp = node_ids[nodeId] || {};
  tmp.id = nodeId;
  var phases = tmp.phases || [];
  if (phases.indexOf(phase) == -1) {
    phases.push(phase);
    addToDPS(nodeId, phase, "voltage");
  }
  tmp.phases = phases;
  node_ids[nodeId] = tmp;
  newNodeIDString();
}

/**
 * Adds a new meter to the current chart, creating a new data series for the id
 * and phase provided.
 *
 * @method addMeter
 * @param {String}
 *            meterId The id of the meter
 * @param {String}
 *            phase The name of the phase
 */
function addMeter(meterId, phase) {
  var tmp = meter_ids[meterId] || {};
  tmp.id = meterId;
  var phases = tmp.phases || [];
  if (phases.indexOf(phase) == -1) {
    phases.push(phase);
    if (phase.charAt(0) == "V")
      addToDPS(meterId, phase, "voltage");
    else if (phase.charAt(0) == "I")
      addToDPS(meterId, phase, "current");
  }
  tmp.phases = phases;
  meter_ids[meterId] = tmp;
  newMeterIDString();
}

/**
 * Adds a new line to the current chart, creating a new data series for the id
 * and phase provided.
 *
 * @method addLine
 * @param {String}
 *            lineId The id of the line
 * @param {String}
 *            phase The name of the phase
 */
function addLine(lineId, phase) {
  var tmp = line_ids[lineId] || {};
  tmp.id = lineId;
  var phases = tmp.phases || [];
  if (phases.indexOf(phase) == -1) {
    phases.push(phase);
    addToDPS(lineId, phase, "current");
  }
  tmp.phases = phases;
  line_ids[lineId] = tmp;
  newLineIDString();
}

/**
 * Sets a new node id string (for querying) based on the node ids set.
 *
 * @method newNodeIDString
 */
function newNodeIDString() {
  node_ids_string = "";
  for (var nodeId in node_ids) {
    node_ids_string += nodeId + ",";
  }
  node_ids_string = node_ids_string.substring(0, node_ids_string.length - 1);
}

/**
 * Sets a new meter id string (for querying) based on the meter ids set.
 *
 * @method newMeterIDString
 */
function newMeterIDString() {
  meter_ids_string = "";
  for (var meterId in meter_ids) {
    meter_ids_string += meterId + ",";
  }
  meter_ids_string = meter_ids_string.substring(0,
    meter_ids_string.length - 1);
}

/**
 * Sets a new line id string (for querying) based on the line ids set.
 *
 * @method newLineIDString
 */
function newLineIDString() {
  line_ids_string = "";
  for (var lineId in line_ids) {
    line_ids_string += lineId + ",";
  }
  line_ids_string = line_ids_string.substring(0, line_ids_string.length - 1);
}

/**
 * Disables (or enables) the graph toggle switches.
 *
 * @method disableGraphToggles
 * @param {bool}
 *            state If true, toggles are disabled, otherwise they are enabled
 */
function disableGraphToggles(state) {
  $('#nodeComparison').bootstrapSwitch('disabled', state);
  $('#processedOrRawData').bootstrapSwitch('disabled', state);
  $('#intervalOpt').bootstrapSwitch('disabled', state);
}

/**
 * Extracts the phases from a JSON object, and adds the readin values and times
 * for the phases to the appropriate data series, based on the information
 * provided in the object.
 *
 * @method extractPhaseJSON
 * @param jsonObj
 *            A json object containg a series of ids, reading_types, times and
 *            reading values to be extracted and added to the appropriate data
 *            series
 */
function extractPhaseJSON(jsonObj) {
  for (var i = 0; i < jsonObj.length; ++i) {
    var series = "";
    if (jsonObj[i].node_id) {
      series = jsonObj[i].node_id + jsonObj[i].reading_type_id;
    } else if (jsonObj[i].line_id) {
      series = jsonObj[i].line_id + jsonObj[i].reading_type_id;
    } else if (jsonObj[i].meter_id) {
      series = jsonObj[i].meter_id + jsonObj[i].reading_type_id;
    }
    if (dps[series]) {
      var t = jsonObj[i].reading_time.split(/[- :]/);
      var datetime = new Date(Date.UTC(t[0], t[1] - 1, t[2], t[3], t[4],
        t[5]));
      if (dps[series].lastdate < dateToUTC(datetime)) {
        dps[series].dps.push({
          x: datetime,
          y: parseFloat(jsonObj[i].reading_value)
        });
        dps[series].lastdate = dateToUTC(datetime);
      }
    }
  }
}

/**
 * Handles the JSON object given in response to a request issued.
 *
 * @method handleJSONResponse
 * @param json
 *            The JSON object to handle
 */
function handleJSONResponse(json) {
  // Should be in the format [{x: val, y: val}, {x: val, y: val}, ...]
  extractPhaseJSON(json);

  // Render the new chart
  chart.render();
  // Update the live values
  printLiveVals();
}

/**
 * Issues a request for the meter values, that match the dates and ids provided.
 *
 * @method getMeterVals
 * @param {String}
 *            fromDate The string representation of a UTC date to request
 *            readings from
 * @param {String}
 *            toDate The string representation of a UTC date to request readings
 *            to
 * @param {String}
 *            meter_string A string of meter_ids to use. If not provided the ids
 *            at meter_ids_string will be used.
 * @param {function}
 *            callback The function to call once results have been received
 */
function getMeterVals(fromDate, toDate, meter_string, callback) {
  meter_string = typeof meter_string !== 'undefined' ? meter_string : meter_ids_string;

  if (meter_string == "")
    return;

  // Build the query to issue
  if (meter_string.length > 0) {
    var query = "getjson.php?meters=" + meter_string + "&interval=" + interval;
    if (fromDate != "")
      query += "&fromDate=" + fromDate;
    if (toDate != "")
      query += "&toDate=" + toDate;

    // Set the waiting a response to true
    waiting_response_meters = true;
    $.getJSON(query, function(json) {
      // Ensure callback is a function
      if ($.isFunction(callback)) {
        // Increment the returned requests
        if (requestsReturned < initialRequests)
          requestsReturned++;
        // Notify that a response has been received
        waiting_response_meters = false;
        // Handle the response if a graph is active
        if (!graphClosed)
          callback(json);
      }
    });
  }
}

/**
 * Issues a request for the nodes values, that match the dates and ids provided.
 *
 * @method getVoltageVals
 * @param {String}
 *            fromDate The string representation of a UTC date to request
 *            readings from
 * @param {String}
 *            toDate The string representation of a UTC date to request readings
 *            to
 * @param {String}
 *            node_string A string of meter_ids to use. If not provided the ids
 *            at node_ids_string will be used.
 * @param {function}
 *            callback The function to call once results have been received
 */
function getVoltageVals(fromDate, toDate, node_string, callback) {
  node_string = typeof node_string !== 'undefined' ? node_string : node_ids_string;

  if (node_string == "")
    return;

  // Build the query to issue
  if (node_string.length > 0) {
    var query = "getjson.php?nodes=" + node_string + "&interval=" + interval;
    if (fromDate != "")
      query += "&fromDate=" + fromDate;
    if (toDate != "")
      query += "&toDate=" + toDate;

    // Set the waiting a response to true
    waiting_response_voltage = true;
    $.getJSON(query, function(json) {
      // Ensure callback is a function
      if ($.isFunction(callback)) {
        // Increment the returned requests
        if (requestsReturned < initialRequests)
          requestsReturned++;
        // Notify that a response has been received
        waiting_response_voltage = false;
        // Handle the response if a graph is active
        if (!graphClosed)
          callback(json);
      }
    });
  }
}

/**
 * Issues a request for the line values, that match the dates and ids provided.
 *
 * @method getCurrentVals
 * @param {String}
 *            fromDate The string representation of a UTC date to request
 *            readings from
 * @param {String}
 *            toDate The string representation of a UTC date to request readings
 *            to
 * @param {String}
 *            line_string A string of meter_ids to use. If not provided the ids
 *            at line_ids_string will be used.
 * @param {function}
 *            callback The function to call once results have been received
 */
function getCurrentVals(fromDate, toDate, line_string, callback) {
  line_string = typeof line_string !== 'undefined' ? line_string : line_ids_string;

  if (line_string == "")
    return;

  // Build the query to issue
  if (line_string.length > 0) {
    var query = "getjson.php?lines=" + line_string + "&interval=" + interval;
    if (fromDate != "")
      query += "&fromDate=" + fromDate;
    if (toDate != "")
      query += "&toDate=" + toDate;

    waiting_response_current = true;
    // Set the waiting a response to true
    $.getJSON(query, function(json) {
      // Ensure callback is a function
      if ($.isFunction(callback)) {
        // Increment the returned requests
        if (requestsReturned < initialRequests)
          requestsReturned++;
        // Notify that a response has been received
        waiting_response_current = false;
        // Handle the response if a graph is active
        if (!graphClosed)
          callback(json);
      }
    });
  }
};

/**
 * Get the name of the table that the results from a phase should be displayed
 * in.
 *
 * @method getTableName
 * @param {String}
 *            phase The phase to be displayed in a table
 * @return {String} The name (HTML id) of the table.
 */
function getTableName(phase) {
  if (phase.charAt(0) == "V")
    return "latestVoltagesTable";
  else if (phase.charAt(0) == "I")
    return "latestCurrentsTable";
  else
    return "";
}

/**
 * Gets the cell index (column number on a row) for a given phase.
 *
 * @method getPhaseCellIdx
 * @param {String}
 *            phase The phase to get the index for
 * @return {int} The index (column number) on a row.
 */
function getPhaseCellIdx(phase) {
  if (phase == "V_a")
    return 1;
  else if (phase == "V_b")
    return 2;
  else if (phase == "V_c")
    return 3;
  else if (phase == "I_a")
    return 1;
  else if (phase == "I_b")
    return 2;
  else if (phase == "I_c")
    return 3;
  else
    return -1;
}

/**
 * Adds the latest values for the ids of the items provided to the approriate
 * tables.
 *
 * @method addValueItems
 * @param {String}
 *            ident A unique id to prefix the cells and rows with for the value
 * @param {String}
 *            items A list of items to add the latest values for
 */
function addValueItems(ident, items) {
  // Go through each of the items
  for (var i in items) {
    item = items[i];
    // Go through each of the phases in the item
    for (var j in item.phases) {
      phase = item.phases[j];
      // Get the latest reading value if it exists (otherwise 0)
      var readingValue = typeof dps[item.id + phase].dps[dps[item.id + phase].dps.length - 1] 
        !== 'undefined' ? dps[item.id + phase].dps[dps[item.id + phase].dps.length - 1].y : 0;
      // Get the cell for this item/phase
      var cell = document.getElementById(ident + "-" + item.id + "-" + phase);
      // If the cell doesn't exist already, add it, and set the value
      if (cell == null) {
        // The row to store the item in
        var row = document.getElementById(ident + "-" + phase.charAt(0) + "-" + item.id);
        // If the row does not exist, add it, add the cell
        if (row == null) {
          var table = document.getElementById(getTableName(phase));
          row = table.insertRow(table.rows.length);
          row.id = ident + "-" + phase.charAt(0) + "-" + item.id;
          // Id Field
          cell = row.insertCell(0);
          cell.id = ident + "-" + item.id + "-id";
          cell.innerHTML = item.id;
          // Phase field
          cell = row.insertCell(getPhaseCellIdx(phase));
          cell.id = ident + "-" + item.id + "-" + phase;
          cell.innerHTML = readingValue;
        } else { // Row exists
          // Phase field
          cell = row.insertCell(getPhaseCellIdx(phase));
          cell.id = ident + "-" + item.id + "-" + phase;
          cell.innerHTML = readingValue;
        }
      } else { // Cell exists just update
        cell.innerHTML = readingValue;
      }
    }
  }
}

/**
 * Prints the latest live values into the table, and ensure the appropriate
 * tables are displayed.
 *
 * @method printLiveVals
 */
function printLiveVals() {
  addValueItems("nodeVals", node_ids);
  addValueItems("lineVals", line_ids);
  addValueItems("meterVals", meter_ids);

  if (jQuery.isEmptyObject(node_ids) && jQuery.isEmptyObject(meter_ids)) {
    document.getElementById("latestVoltagesVals").style.display = "none";
    document.getElementById("latestVoltagesTable").style.display = "none";
  } else {
    document.getElementById("latestVoltagesTable").style.display = "table";
    document.getElementById("latestVoltagesVals").style.display = "block";
  }

  if (jQuery.isEmptyObject(line_ids) && jQuery.isEmptyObject(meter_ids)) {
    document.getElementById("latestCurrentVals").style.display = "none";
    document.getElementById("latestCurrentsTable").style.display = "none";
  } else {
    document.getElementById("latestCurrentsTable").style.display = "table";
    document.getElementById("latestCurrentVals").style.display = "block";
  }
}

/**
 * Loads the points to start the graph for each item, by issuing a query for
 * each individual item. The graph start date is then set to the date provided.
 * The dates used for the queries are determined by the ranges currently
 * selected by the user.
 *
 * @method loadAllPoints
 * @param {Date}
 *            date The date to set the graph starts at
 */
function loadAllPoints(date) {
  graphStartDate = date;
  var dates = getDateRange();

  // Send queries for nodes
  for (var node in node_ids) {
    var ids = "";
    for (var phze in node_ids[node].phases) {
      if (dps[node + node_ids[node].phases[phze]].dps.length == 0) {
        ids = node;
        break;
      }
    }
    if (ids != "") {
      getVoltageVals(dates.from, dates.to, ids, handleJSONResponse);
    }
  }

  // Send queries for lines
  for (var line in line_ids) {
    var ids = "";
    for (var phze in line_ids[line].phases) {
      if (dps[line + line_ids[line].phases[phze]].dps.length == 0) {
        ids = line;
        break;
      }
    }
    if (ids != "") {
      getCurrentVals(dates.from, dates.to, ids, handleJSONResponse);
    }
  }

  // Send queries for meters
  for (var meter in meter_ids) {
    var ids = "";
    for (var phze in meter_ids[meter].phases) {
      if (dps[meter + meter_ids[meter].phases[phze]].dps.length == 0) {
        ids = meter;
        break;
      }
    }
    if (ids != "") {
      getMeterVals(dates.from, dates.to, ids, handleJSONResponse);
    }
  }
  chart.render();
}

/**
 * Runs the graph. That is displays the initial results, and then continually
 * updates the graph based on the interval set. If the interval is not specified
 * and all results are being selected, the update rate will be every 5 seconds.
 * Otherwise, it will update at the rate of the interval.
 *
 * @method graphrunner
 */
function graphrunner() {
  // Graph update interval
  var timerInterval = 5000;
  if (interval != -1)
    timerInterval = interval * 1000;

  // Function to update the chart.
  var updateChart = function() {
    // Only update if inital requests have returned
    if (initialRequests != requestsReturned)
      return;

    // Get date to request values from
    var utcDate = dateToUTC(new Date());
    for (var series in dps) {
      if (dps[series].lastdate < utcDate)
        utcDate = dps[series].lastdate;
    }

    var fromDate = utcDate.getUTCFullYear() + "-" + (utcDate.getUTCMonth() + 1) 
      + "-" + utcDate.getUTCDate() + " " + utcDate.getUTCHours() + ":" 
      + utcDate.getUTCMinutes() + ":" + utcDate.getUTCSeconds();

    // Get the latest values, if not waiting for a response
    if (!waiting_response_voltage) {
      getVoltageVals(fromDate, "", node_ids_string, handleJSONResponse);
    }
    if (!waiting_response_current) {
      getCurrentVals(fromDate, "", line_ids_string, handleJSONResponse);
    }
    if (!waiting_response_meter) {
      getMeterVals(fromDate, "", meter_ids_string, handleJSONResponse);
    }
    chart.render(); // Render the graph
  };

  // Take appropriate inital action (depending on node comparison/new)
  if (graphIsActive) {
    clearInterval(refreshIntervalId);
    loadAllPoints(graphStartDate);
    disableGraphToggles(false);
  } else {
    loadAllPoints(new Date(new Date() - 3600000));
    disableGraphToggles(false);
    if (getDateRange().to != "")
      return;
  }

  refreshIntervalId = setInterval(function() {
    updateChart();
  }, timerInterval);

  graphIsActive = true;
}

/**
 * Gets and sets the minimum allowed interval based on the date range provided.
 * This ensure an appropriate amount of records are selected (around 1800).
 *
 * @method getMinInterval
 * @param {Date}
 *            fromDate The earliest a record should occur
 * @param {Date}
 *            toDate The latest a record should occur
 */
function getMinInterval(fromDate, toDate) {
  var fromSec = 0;
  var toSec = 0;
  var expRec = 3;
  // Get the time in seconds (not milli)
  if (fromDate != null)
    fromSec = new Date(fromDate).getTime() / 1000;
  if (toDate != null)
    toSec = new Date(toDate).getTime() / 1000;
  else
    toSec = new Date().getTime() / 1000;

  // Get the expected phases per id
  if (activeDetails[downloadType] == "meter")
    expRec = 6;

  // Guess the approximate records
  var records = (toSec - fromSec) / recordInterval * expRec;

  // Set the interval
  if (records < maxRecords)
    interval = -1;
  else if (interval != 1800)
    interval = Math.round((toSec - fromSec) / maxRecords * expRec);
}

/**
 * Returns the dates that the graph should be displayed from and to, and wether
 * or not the graph should be updated.
 *
 * @method getDateRange
 * @return {Object} Contains three variables. from: When the graph should be
 *         displayed from to: When the graph should be displayed to update: If
 *         the graph should be updated (that is no end date)
 */
function getDateRange() {
  var fromDate = $("#graphFromDate").data("DateTimePicker").getDate();
  var toDate = $("#graphToDate").data("DateTimePicker").getDate();
  var live = true;

  // Set interval if setting a date filter without 30 min interval
  if (fromDate != null && interval != 1800)
    getMinInterval(fromDate, toDate);

  // Convert date to string
  if (fromDate != null)
    fromDate = convertDateToString(fromDate._d);
  else
    fromDate = "";

  // Convert date to string
  if (toDate != null)
    toDate = convertDateToString(toDate._d);
  else
    toDate = "";

  // Determin if live
  if (toDate != "")
    live = false;

  // Case when selecting past hour
  if (fromDate == "" && interval == -1 && live)
    fromDate = convertDateToUTCString(new Date(new Date() - 3600000));

  return {
    from: fromDate,
    to: toDate,
    update: live
  };
}

/**
 * Adds all the ids/phases back to a data series.
 *
 * @method reAddDPS
 */
function reAddDPS() {
  var meters = meter_ids_string.split(",");
  var nodes = node_ids_string.split(",");
  var lines = line_ids_string.split(",");

  if (meter_ids_string == "")
    meters = {};
  if (node_ids_string == "")
    nodes = {};
  if (line_ids_string == "")
    lines = {};

  for (var i = 0; i < meters.length; i++) {
    addMeter(meters[i], "V_a");
    addMeter(meters[i], "V_b");
    addMeter(meters[i], "V_c");
    addMeter(meters[i], "I_a");
    addMeter(meters[i], "I_b");
    addMeter(meters[i], "I_c");
    initialRequests++;
  }
  for (var i = 0; i < nodes.length; i++) {
    addNode(nodes[i], "V_a");
    addNode(nodes[i], "V_b");
    addNode(nodes[i], "V_c");
    initialRequests++;
  }
  for (var i = 0; i < lines.length; i++) {
    addLine(lines[i], "I_a");
    addLine(lines[i], "I_b");
    addLine(lines[i], "I_c");
    initialRequests++;
  }
}

/**
 * Initialises the graph, adds the data series, displays the initial data, and
 * then starts the graphrunner. Will add back existing data if doing a
 * comparison.
 *
 * @method initShowGraphCallback
 * @param {bool}
 *            reAdd If the existing values should be added (doing a compar)
 */
function initShowGraphCallback(reAdd) {
  if (activeDetails[downloadIds] == null || activeDetails[downloadIds] == "")
    return;
  var ids = activeDetails[downloadIds].split(",");
  initialRequests = 0;
  initialReturned = 0;
  graphClosed = false;

  if (activeDetails[downloadType] == "meter" && !reAdd) {
    for (var i = 0; i < ids.length; i++) {
      addMeter(ids[i], "V_a");
      addMeter(ids[i], "V_b");
      addMeter(ids[i], "V_c");
      addMeter(ids[i], "I_a");
      addMeter(ids[i], "I_b");
      addMeter(ids[i], "I_c");
      initialRequests++;
    }
  } else if (activeDetails[downloadType] == "node" && !reAdd) {
    for (var i = 0; i < ids.length; i++) {
      addNode(ids[i], "V_a");
      addNode(ids[i], "V_b");
      addNode(ids[i], "V_c");
      initialRequests++;
    }
  } else if (activeDetails[downloadType] == "line" && !reAdd) {
    for (var i = 0; i < ids.length; i++) {
      addLine(ids[i], "I_a");
      addLine(ids[i], "I_b");
      addLine(ids[i], "I_c");
      initialRequests++;
    }
  }

  if (reAdd)
    reAddDPS();
  graphrunner();
}

/**
 * Initalises the graph. Sets the variables, and clears existing data if
 * necessary and makes a new chart.
 *
 * @method initializeShowGraph
 */
function initializeShowGraph() {
  disableGraphToggles(true);
  clearInterval(refreshIntervalId);
  if (document.getElementById("processedOrRawData").checked) {
    if (!document.getElementById("nodeComparison").checked) {
      clearGraph("processed");
    }
  } else {
    if (!document.getElementById("nodeComparison").checked) {
      clearGraph("raw");
    }
  }

  // Node comaprison on 30 minute interval, and currently 30 min interval
  if (!document.getElementById("intervalOpt").checked && interval == 1800 
  	&& document.getElementById("nodeComparison").checked) {
    // Clear old data - needs to be re added
    dps = {};
    node_ids = {};
    meter_ids = {};
    line_ids = {};
    clearInterval(refreshIntervalId);

    if (document.getElementById("processedOrRawData").checked)
      newChart("processed");
    else
      newChart("raw");

    interval = -1;
    graphIsActive = false;
    graphStartDate = new Date(new Date() - 3600000);
    initShowGraphCallback(true);
  } else if (document.getElementById("intervalOpt").checked && interval != 1800 
  	&& document.getElementById("nodeComparison").checked) {
    // Node compare, 30 min interval, not currently 30min
    // Clear old data - needs to be re added
    dps = {};
    node_ids = {};
    meter_ids = {};
    line_ids = {};
    clearInterval(refreshIntervalId);

    if (document.getElementById("processedOrRawData").checked)
      newChart("processed");
    else
      newChart("raw");

    interval = 1800;
    graphIsActive = false;
    graphStartDate = activeDetails[downloadFrom];
    initShowGraphCallback(true);
  } else if (document.getElementById("intervalOpt").checked) {
    // 30 min interval, no comparison
    interval = 1800;
    getDateRange();
    initShowGraphCallback(false);
  } else {
    // no 30 min interval, no comparison
    interval = -1;
    getDateRange();
    initShowGraphCallback(false);
  }
}