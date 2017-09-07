<?php
  /*
   * Connects to the database host with the FrontEnd username and password.  It
  * then selects the power_grid database for queries to be executed on.
  */
  
  // Connection Data
  $username = "FrontEnd";
  $userpass = "fLUyR5FFYEb9z6q3";
  $dbhost = "localhost";
  $dbdatabase = "uq_power_grid";
  
  // Connect to the database host
  $db_connect = mysql_connect ( $dbhost, $username, $userpass ) 
    or die ( "Unable to connect to MySQL" );
  
  // Select the database
  $selected = mysql_select_db ( $dbdatabase, $db_connect ) 
    or die ( "Could not select database test" );
?>