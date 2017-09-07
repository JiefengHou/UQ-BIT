<?php
  /*
   * This api can be used to determine if a particular line or node has any data
   * in the results tables.  It is also used to query for the meters at the
   * particular nodes or lines, and determine if raw data exists.  The max and
   * min dates are provided when such data exists.
   * POST variables:
   * id: The id of the item
   * tpye: The tpe (node or line)
   * meter: If checking for raw meter data for the item
   * returnMeter: If the ids of the meters are being requested.
   */
  include ('dbconnect.php'); // Connect to the database
  
  // Ensure minimum fields have been provided.
  if (!isset ($_POST['id']) || !isset ($_POST['type'])) die ("");
  
  $id = check_value($_POST['id']);
  $type = check_value($_POST['type']);
  $getMeter = "";
  $returnMeterIds = "";
  
  // Check if optional fields have been given
  if (isset ($_POST['meter'])) $getMeter = check_value($_POST['meter']);
  if (isset ($_POST['returnMeter'])) $returnMeterIds 
    = check_value($_POST['returnMeter']);
  
  // Die if missing compulsory fields
  if ($id === "" || $type === "") die ("");
  
  $times = array(); // Stores the max and min time of data
  
  // Determine the type of node request (if node) and take action
  if ($type === "node" && $getMeter === "true" && $returnMeterIds === "true") {
      // Get the meters for the node and return the list.
      $meterClause = "node_id = {$id}";
      $meterList = getMeterIds($meterClause);
      $meterList = trim ($meterList, "(");
      $meterList = trim ($meterList, ")");
      echo $meterList;
      die ("");
  } else if ($type === "node" && $getMeter === "true") {
      // Get the max and min date for the raw data and return
      $meterClause = "node_id = {$id}";
      $meterList = getMeterIds($meterClause);
      $query = "SELECT reading_time AS 'rt' FROM meter_read WHERE meter_id 
        IN {$meterList} ORDER BY reading_time ASC LIMIT 1;;";
      array_push ($times, getTime ($query));
      $query = "SELECT reading_time AS 'rt' FROM meter_read WHERE meter_id 
        IN {$meterList} ORDER BY reading_time DESC LIMIT 1;;";
      array_push ($times, getTime ($query));
      echo json_encode ($times);
      die ("");
  } else if ($type === "node") {
      // Get the max and min date for the processed data and return
      $query = "SELECT reading_time AS 'rt' FROM node_results WHERE node_id={$id} 
        ORDER BY reading_time ASC LIMIT 1;";
      array_push ($times, getTime ($query));
      $query = "SELECT reading_time AS 'rt' FROM node_results WHERE node_id={$id} 
        ORDER BY reading_time DESC LIMIT 1;";
      array_push ($times, getTime ($query));
      echo json_encode ($times);
      die ("");
  }
  
  // Determine the type of lines request (if line) and return results
  if ($type === "line" && $getMeter === "true" && $returnMeterIds === "true") {
      // Return list of attached meters and return
      $meterClause = "line_id = {$id}";
      $meterList = getMeterIds($meterClause);
      $meterList = trim ($meterList, "(");
      $meterList = trim ($meterList, ")");
      echo $meterList;
      die ("");
  } else if ($type === "line" && $getMeter === "true") {
      // Get max and min date of raw data and return
      $meterClause = "line_id = {$id}";
      $meterList = getMeterIds($meterClause);
      $query = "SELECT reading_time AS 'rt' FROM meter_read WHERE meter_id 
        IN {$meterList} ORDER BY reading_time ASC LIMIT 1;";
      array_push ($times, getTime ($query));
      $query = "SELECT reading_time AS 'rt' FROM meter_read WHERE meter_id 
        IN {$meterList} ORDER BY reading_time DESC LIMIT 1;";
      array_push ($times, getTime ($query));
      echo json_encode ($times);
      die ("");
  } else if ($type === "line") {
      // Get max and min date of procssed data and return
      $query = "SELECT reading_time AS 'rt' FROM line_results WHERE line_id={$id} 
        ORDER BY reading_time ASC LIMIT 1";
      array_push ($times, getTime ($query));
      $query = "SELECT reading_time AS 'rt' FROM line_results WHERE line_id={$id} 
        ORDER BY reading_time DESC LIMIT 1";
      array_push ($times, getTime ($query));
      echo json_encode ($times);
      die ("");
  }
  
  // Should not reach here, just a fail-safe.
  if (empty($times)) die("");
  else echo json_encode ($times);
  
  /*
   * Returns the meter ids matching a given where clause.
   */
  function getMeterIds($whereClause) {
      $query = "SELECT id FROM meter WHERE " . $whereClause . ";";
      $list = "(";
      $result = mysql_query ($query);
      if ($result) {
          while ($row = mysql_fetch_array ($result)) {
              $list .= $row['id'] . ",";
          }
          $list = rtrim ($list, ",");
      }
      return $list . ")";
  }
  
  /*
   * Takes a query string, gets the 'rt' attribute and returns it.
   */
  function getTime($query) {
      $result = mysql_query ($query);
      if ($result) {
          $data= mysql_fetch_assoc ($result);
          if (!($data['rt'] === null)) return $data['rt'];
          else die("");
      } else die ("");
  }
  
  
  /*
   * This is function is used to escape special characters in a string.
   *
   * $value: the value input by user
   */
  function check_value($value) {
      if (get_magic_quotes_gpc ()) {
          $value = stripslashes ($value);
      }
      if (!is_numeric ($value)) {
          $value = mysql_real_escape_string ($value);
      }
      return $value;
  }
?>