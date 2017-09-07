<?php
  /*
   * Returns the reading results as a JSON based on the parametrs provided.
   * fromDate: Get results from this date/time
   * toDate: Get results to this date/time
   * nodes: The node ids to match
   * lines: The line ids to match
   * meters: The meter ids to match
   * interval: Select a record every X seconds.
   */
  header('Content-Type: application/jsonrequest'); // Set the header
  include ('query_functions.php'); // Load the query functions
  include ('dbconnect.php'); // Load the database
  ob_start(); // Ensures clean buffer
  
  /*
   * This function takes a query, executes it, and returns the results as a JSON
   */
  function returnQueryAsJSON($query) {
      $result = mysql_query($query); // Execute the query
      $json = "["; // Start the JSON string
      // Go through the results
      if (mysql_num_rows($result) > 0) {
          $headers = array();
          // Add the headers
          for ($col = 0; $col < mysql_num_fields($result); $col++) {
              $headers[] = mysql_field_name($result, $col);
          }
  
          // Add the results
          while ($row = mysql_fetch_row($result)) {
              $json .= "{";
              for ($col = 0; $col < mysql_num_fields($result); $col++) {
                  $json .= "\"$headers[$col]\":\"$row[$col]\",";
              }
              $json = substr($json, 0, -1) ."},";
          }
      }
  
      unset($result); // Release the results from memory
      if (strlen($json) >= 2) {
          $json = substr($json, 0, -1);
      }
      $json .= "]";
      return $json; // Return the file JSON
  }
  
  // Check if a From Date has been provided
  if (isset($_REQUEST['fromDate'])) {
      if (($fromDate = strtotime($_REQUEST['fromDate'])) === false)
          die ("The From Date is in a bad format! '".$fromDate."'"); // Some funny input
      else $fromDate = date("Y-m-d H:i:s", $fromDate);
  } else $fromDate = "";
  
  // Check if a To Date has been provided
  if (isset($_REQUEST['toDate'])) {
      if (($toDate = strtotime($_REQUEST['toDate'])) === false)
          die ("The To Date is in a bad format!"); // Some funny input
      else $toDate = date("Y-m-d H:i:s", $toDate);
  } else $toDate = "";
  
  // Get the array of nodes
  if (isset($_REQUEST['nodes'])) {
      if ($_REQUEST['nodes'] === "all") $nodes = "all"; // Want all nodes
      // Make input into an array
      else if (!is_array($nodes = explode(",", $_REQUEST['nodes'])))
          die ("Node IDs should be separated by a single ,");
  } else $nodes = "";
  
  // Get the array of lines
  if (isset($_REQUEST['lines'])) {
      if ($_REQUEST['lines'] === "all") $lines = "all"; // Want all lines
      // Make input into an array
      else if (!is_array($lines = explode(",", $_REQUEST['lines'])))
          die ("Line IDs should be separated by a single ,");
  } else $lines = ""; // No lines
  
  // Get the array of meters
  if (isset($_REQUEST['meters'])) {
      if ($_REQUEST['meters'] === "all") $meters = "all"; // Want all lines
      // Make input into an array
      else if (!is_array($meters = explode(",", $_REQUEST['meters'])))
          die ("Line IDs should be separated by a single ,");
  } else $meters = "";
  
  // Get the interval to sample results at
  if (isset($_REQUEST['interval'])) {
      if (is_numeric($_REQUEST['interval'])) $interval = intval($_REQUEST['interval']);
      else $interval = -1;
  } else $interval = -1;
  
  // Return the node results
  if (!($nodes === "")) {
      echo returnQueryAsJSON("SELECT node_id, reading_type_id, reading_time, reading_value"
        .getNodeClause($nodes, $fromDate, $toDate, $interval, true));
  }
  
  // Return the line results
  if (!($lines === "")) {
      echo returnQueryAsJSON("SELECT line_id, reading_type_id, reading_time, reading_value"
        .getLineClause($lines, $fromDate, $toDate, $interval, false, true));
  }
  
  // Return the meter results
  if (!($meters === "")) {
      echo returnQueryAsJSON("SELECT meter_id, reading_type_id, reading_time, reading_value"
        .getMeterClause($meters, $fromDate, $toDate, $interval, true, true));
  }
?>