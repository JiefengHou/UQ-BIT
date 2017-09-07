<?php
  /*
   * Given a set of parameters it generates a csv (or csv's) containing data
   * fulfilling these requirements and adds them to a zip file.  The filename
   * (not path) of this file is returned. The downloadFile script can the be
   * used to download the file.
   */
  include ('dbconnect.php'); // Connect to the site database
  include ('query_functions.php'); // Load in functions to design queries
  
  set_time_limit(0);
  // Check if a From Date has been provided
  if (isset($_REQUEST['fromDate'])) {
      if (($fromDate = strtotime($_REQUEST['fromDate'])) === false)
        die ("The From Date is in a bad format!"); // Some funny input
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
  } else $nodes = ""; // No nodes
  
  // Get the array of lines
  if (isset($_REQUEST['lines'])) {
      if ($_REQUEST['lines'] === "all") $lines = "all"; // Want all lines
      // Make input into an array
      else if (!is_array($lines = explode(",", $_REQUEST['lines'])))
        die ("Line IDs should be separated by a single ,");
  } else $lines = ""; // No lines
  
  // Get the array of meters
  if (isset($_REQUEST['meters'])) {
      if ($_REQUEST['meters'] === "all") $meters = "all";
      else if (!is_array($meters = explode(",", $_REQUEST['meters'])))
        die ("Meter IDs should be separated by a single ,");
  } else $meters = ""; // No meters
  
  // Directory's and filenames.
  $directory = "/var/tmp/web/";
  $nodeFileName = $directory."nodes.csv";
  $lineFileName = $directory."lines.csv";
  $meterFileName = $directory."meters.csv";
  $zipDownload = "readings" . date("d_m_Y_G_i_s") . ".zip";
  $zipFileName = $directory.$zipDownload;
  
  // Make the directory if it doesn't exist.
  if (!file_exists($directory)) mkdir ($directory, 0777, true);
  
  // Save the node results into csv
  if (!($nodes === ""))
      saveCsv("SELECT node_id AS 'Node_Id', reading_time 
        AS 'Time_UTC', reading_type_id AS 'Type', reading_value 
        AS 'Value'".getNodeClause($nodes, $fromDate, $toDate), $nodeFileName);
  
  // Save the line results into csv
  if (!($lines === ""))
      saveCsv("SELECT line_id AS 'Line_Id', reading_time 
        AS 'Time_UTC', reading_type_id AS 'Type', reading_value 
        AS 'Value'".getLineClause($lines, $fromDate, $toDate), $lineFileName);
  
  // Save the meter results into csv
  if (!($meters === ""))
      saveCsv("SELECT meter_id AS 'Meter_Id', reading_time 
        AS 'Time_UTC', reading_type_id AS 'Type', reading_value 
        AS 'Value'".getMeterClause($meters, $fromDate, $toDate), $meterFileName);
  
  // Add the csv files into an array
  $files = array($nodeFileName, $lineFileName, $meterFileName);
  
  // Create a new zip and add the csvs
  $zip = new ZipArchive;
  
  // Add all the files to the zip
  if ($zip->open($zipFileName, ZipArchive::CREATE) !== true) {
      die ("Cannot create zip");
  } else {
      foreach ($files as $file) {
          if (file_exists($file)) {
              $filename = substr ($file, strripos ($file, "/") + 1);
              if ($zip->addFile($file, $filename) === false) die ("cannot add {$filename} to zip");
          }
      }
  }
  
  $zip->close();
  
  foreach($files as $file) unlink($file); // Delete the csvs
  
  echo $zipDownload; // Return the zip filename
  
  /*
   * This function takes a SQL query string, executes it, and saves the results
  * into the file provided.  It will save the results in a comma delimited
  * format with the first row containing the column headings.
  */
  function saveCsv($query, $fileName) {
      $result = mysql_unbuffered_query($query); // Get the result
  
      $temp = fopen($fileName, "w"); // A pointer to the file
      // Check if there are results and then process
      if ($result) {
          $headers = []; // Array to store column headings
          // Get the headers
          for ($i = 0; $i < mysql_num_fields($result); $i++) {
            array_push ($headers, mysql_field_name($result, $i));
          }
  
          fputcsv ($temp, $headers); // Save the headers to the file
  
          // Process each row of the result
          while ($row = mysql_fetch_row($result)) {
            fputcsv ($temp, $row); // Save the row to the file.
          }
      }
      fclose($temp); // Close the file
  }
?>