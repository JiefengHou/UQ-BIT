<?php
  /*
   * The following functions are used for specific queries for initial map data.
   */
  include ('dbconnect.php'); // Connect to the database
  
  /*
   * Returns an array of arrays.
   * Each array contains the details of a single line, that has a starting node
   * of type 'startType' and ending node of type 'endType'.
   * idx 0: Line ID
   * idx 1: Name
   * idx 2: Starting Node ID
   * idx 3: Ending Node ID
   * idx (4,5): Start (Latitude, Longitude)
   * idx (5,6) ... (n, n + 1): Subsequent points (Latitude, Longitude)
   * idx (n + 2, n + 3): End (Latitude, Longitude)
   */
  function getLines($startType, $endType) {
      $lines = array();
      $result = mysql_query ("SELECT line_id, name, start_id, end_id, start_lat,"
              . "start_long, end_lat, end_long, start_net, end_net FROM "
              . "view_lines WHERE start_type=\"" . $startType . "\" AND "
              . "end_type=\"" . $endType . "\";");
  
      if ($result) {
          while ($row = mysql_fetch_array ($result)) {
              $lineData = array();
              array_push($lineData, $row['line_id'], $row['name'],
                      $row['start_id'], $row['end_id'], $row['start_net'],
                      $row['end_net'], $row['start_lat'], $row['start_long']);
               
              $linePoints = mysql_query (
                      "SELECT latitude, longitude FROM view_line_points WHERE "
                      . "line=" . $row['line_id'] . ";");
              if ($linePoints) {
                  while ($point = mysql_fetch_array ($linePoints)) {
                      array_push ($lineData, $point['latitude'],
                              $point['longitude']);
                  }
              }
               
              array_push ($lineData, $row['end_lat'], $row['end_long']);
              array_push ($lines, $lineData);
          }
      }
      return $lines;
  }
  
  /*
   * Returns an array of arrays.
   * Each array contains the details of a single node which is of type 'type'.
   * idx 0: LNode ID
   * idx 1: Name
   * idx 2: Type
   * idx 3: Latitude
   * idx 4: Longitude
   */
  function getNodes($type) {
      $highSearch = 'High Voltage Line';
      $lowSearch = 'Low Voltage Line';
      $endQuery = 'Building';
  
      $allNodes = array ();
      $result = mysql_query ("SELECT node_id, name, lat, lng, type, net, image "
              . "FROM view_nodes WHERE type=\"" . $type . "\";");
  
      if ($result) {
          while ($row = mysql_fetch_array ($result)) {
              $node = array();
              array_push ($node, $row['node_id'], $row['name'], $row['type'],
                      $row['net'], $row['image']);
               
              $lines = mysql_query ("SELECT line_id, end_type FROM view_lines "
                      . "WHERE (start_id = {$row['node_id']} OR end_id = "
                      . "{$row['node_id']});");
               
              if ($lines) {
                  while ($line = mysql_fetch_array ($lines)) {
                      if ($line['end_type'] === $endQuery) {
                          array_push ($node, $line['line_id'], $lowSearch);
                      } else {
                          array_push ($node, $line['line_id'], $highSearch);
                      }
                  }
              }
               
              array_push ($node, $row['lat'], $row['lng']);
               
              array_push ($allNodes, $node);
          }
      }
      return $allNodes;
  }
?>