<?php
include ('dbconnect.php'); // Connect to the database
/*
 * The following utilised as a search script for substations, endpoints
 * and lines.
 */
// Types of searches handled/query names
$endSearch = 'Endpoint';
$subNetSearch = "Sub Net";
$highSearch = 'High Voltage Line';
$lowSearch = 'Low Voltage Line';
$networkSearch = 'Network Names';

$endQuery = 'Building';
$subQuery = 'Substation';
$netQuery = 'Network Centre';

$availableSearches = [$endSearch, $subNetSearch, $highSearch, $lowSearch,
$networkSearch];

// Ensure values are received.
if (!isset ($_POST['input']) || !isset ($_POST['search-type'])) {
    die ("");
}

// Sanitise the inputs and ensure not empty.
$input = check_value($_POST['input']);
$search = check_value($_POST ['search-type']);
$network = "";
if ($input === "" || $search === "") {
    die ("");
}

// Check if applying network filter also.
if (isset ($_POST['network'])) {
    $network = check_value($_POST['network']);
}

// Ensure the search can be handled
if (!(in_array($search, $availableSearches))) {
    die ("");
}

// Start the searches
// Substation Including Network Centres
if ($search === $subNetSearch) {
    // SQL Query
    $nodeQuery = "SELECT node_id FROM view_nodes WHERE "
    . "name LIKE '%{$input}%' AND (type='{$subQuery}' OR "
    . "type='{$netQuery}')";
    if (!($network === "")) $nodeQuery .= " AND net IN ({$network})";
    $nodeQuery .= ";";
    $result = mysql_query ($nodeQuery);
    // Return the results to the requester
    returnResults($result, "node_id");
    // Search completed
    die ("");
}

// Endpoint Search
if ($search === $endSearch) {
    // SQL query
    $nodeQuery = "SELECT node_id FROM view_nodes WHERE "
    . "name LIKE '%{$input}%' AND type='{$endQuery}' ";
    if (!($network === "")) $nodeQuery .= " AND net IN ({$network})";
    $nodeQuery .= ";";
    $result = mysql_query ($nodeQuery);
    // Return the results to the requester
    returnResults($result, "node_id");
    // Search completed
    die ("");
}

// High Voltage Line Search
if ($search === $highSearch) {
    // SQL Query
    $lineQuery = "SELECT line_id FROM view_lines "
    . "WHERE name LIKE '%{$input}%' AND start_type='{$subQuery}' "
    . "AND end_type='{$subQuery}'";
    if (!($network === "")) $lineQuery .= " AND (start_net IN ({$network}) "
    . "OR end_net IN ({$network}))";
    $lineQuery .= ";";
    $result = mysql_query ($lineQuery);
    // Return the results to the requester
    returnResults($result, "line_id");
    // Search completed
    die ("");
}

// Low Voltage Line Search
if ($search === $lowSearch) {
    // SQL Query
    $lineQuery = "SELECT line_id FROM view_lines "
    . "WHERE name LIKE '%{$input}%' AND start_type='{$netQuery}' "
    . "AND end_type='{$endQuery}'";
    if (!($network === "")) $lineQuery .= " AND (start_net IN ({$network}) "
    . "OR end_net IN ({$network}))";
    $lineQuery .= ";";
    $result = mysql_query ($lineQuery);
    // Return the results to the requester
    returnResults($result, "line_id");
    // Search completed
    die ("");
}

// Names of networks
if ($search == $networkSearch) {
    // SQL Query
    $query = "SELECT id, description FROM network;";
    $result = mysql_query($query);
    $list = array();
    if ($result) {
        while ($row = mysql_fetch_array ($result)) {
            $network = array();
            array_push ($network, $row["id"], $row["description"]);
            array_push ($list, $network);
        }
    }
    if (empty ($list)) {
        die ("");
    } else {
        echo json_encode($list);
    }
}

/*
 * This is function is used to escape special characters in a string.
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

/*
 * Encodes the results into a JSON form and returns them to the requester,
 * from the $queryResult provided. id specified the name of the id field.
 */
function returnResults($queryResult, $id) {
    $results = [];
    if ($queryResult) {
        while ($row = mysql_fetch_array ($queryResult)) {
            array_push ($results, $row[$id]);
        }
    }
    if (empty ($results)) {
        die ("");
    } else {
        echo json_encode($results);
    }
}
?>