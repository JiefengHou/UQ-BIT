<?php
/*
 * Contains functions for building query's to retrieve data from the
 * UQ Visualiser database.
 */

/*
 * Returns the string that can be added to the WHERE clause of a query
 * to filter results by a certain reading_time.
 * Both fromDate and toDate are optional.
 */
function getTimeClause($fromDate, $toDate) {
    $clause = ""; // The clause to return

    // Add the fromDate if applicable
    if (!($fromDate === "")) $clause .= " reading_time >= '".$fromDate."'";

    // Add the toDate if applicable
    if (!($toDate === "") && !($fromDate === "")) $clause .= " AND reading_time <= '".$toDate."'";
    else if (!($toDate === "")) $clause .= " reading_time <= '" .$toDate . "'";

    return $clause;
}

/*
 * Returns the string that can be added to the WHERE clause of a query to
 * select results at the given interval.
 * Giving an interval of -1 will return an empty string (i.e. return all
 * results)
 */
function getIntervalClause($interval) {
    if ($interval === -1) return "";
    if ($interval === 1800) return " thirtyMinMod=TRUE";
    else return " MOD(UNIX_TIMESTAMP(reading_time),{$interval}) IN (0, 1, 2, 3, 4)";
}

/*
 * Returns the string that can be added to the WHERE clause of a query
 * to filter results by IDs in the list provided.
 */
function getIdClause($idList) {
    $clause = " IN (";

    $firstElem = true; // To identify the first ID
    foreach ($idList as $id) {
        if(!$firstElem) $clause .= ","; // First ID has no preceding comma
        $clause .= $id;
        $firstElem = false;
    }

    return $clause.")";
}

/*
 * Returns the string that can be added to a WHERE clause to select readings
 * only matching certain types.
 * Setting the type will true will ensure results match that type.
 */
function getTypeClause($voltages, $current, $power, $voltagePhase) {
    if ($voltages == false && $current == false && $power == false && $voltagePhase == false) return "";
    $clause = " (";
    if ($voltages) $clause .= "reading_type_id LIKE 'V\__'";

    if ($voltages && $current) $clause .= " OR reading_type_id LIKE 'I\__'";
    else if ($current) $clause .= "reading_type_id LIKE 'I\__'";

    if (($voltages || $current) && $power) $clause .= " OR reading_type_id LIKE 'W\__'";
    else if ($power) $clause .= "reading_type_id LIKE 'W\__'";

    if (($voltages || $current || $power) && $voltagePhase) $clause .= " OR reading_type_id LIKE 'V\___'";
    else if ($voltagePhase) $clause .= "reading_type_id LIKE 'V\___'";

    $clause .= ")";
    return $clause;
}

/*
 * Returns the clause to select data from node_results which is built from the
 * options provided.  No field is compulsory.  If no fields are provided,
 * FROM node_results will be returned.
 */
function getNodeClause($nodeList, $fromDate, $toDate, $interval = -1, $voltages = false, 
    $current = false, $power = false, $voltagePhase = false) {

    $time = getTimeClause($fromDate, $toDate);
    $interval = getIntervalClause($interval);
    $type = getTypeClause($voltages, $current, $power, $voltagePhase);

    $clause = " FROM node_results WHERE ";
    if ($nodeList === "all") { // No need to filter by ids
        if (!($time === "")) $clause .= $time;

        if ($time === "" && !($interval === "")) $clause .= $interval;
        else if (!($interval === "")) $clause .= " AND".$interval;

        if ($time === "" && $interval === "" && !($type === "")) $clause .= $type;
        else if (!($type === "")) $clause .= " AND".$type;

        if ($time === "" && $interval === "" && $type === "") $clause .= "1";
    } else {
        $clause .= " node_id".getIdClause($nodeList);

        if (!($time === "")) $clause .= " AND".$time;

        if (!($interval === "")) $clause .= " AND".$interval;

        if (!($type === "")) $clause .= " AND".$type;
    }
    return $clause;
}

/*
 * Returns the clause to select data from line_results which is built from the
 * options provided.  No field is compulsory.  If no fields are provided,
 * FROM line_results will be returned.
 */
function getLineClause($lineList, $fromDate, $toDate, $interval = -1, $voltages = false, 
    $current = false, $power = false, $voltagePhase = false) {

    $time = getTimeClause($fromDate, $toDate);
    $interval = getIntervalClause($interval);
    $type = getTypeClause($voltages, $current, $power, $voltagePhase);

    $clause = " FROM line_results WHERE ";
    if ($lineList === "all") { // No need to filter by ids
        if (!($time === "")) $clause .= $time;

        if ($time === "" && !($interval === "")) $clause .= $interval;
        else if (!($interval === "")) $clause .= " AND".$interval;

        if ($time === "" && $interval === "" && !($type === "")) $clause .= $type;
        else if (!($type === "")) $clause .= " AND".$type;

        if ($time === "" && $interval === "" && $type === "") $clause .= "1";
    } else {
        $clause .= " line_id".getIdClause($lineList);

        if (!($time === "")) $clause .= " AND".$time;

        if (!($interval === "")) $clause .= " AND".$interval;

        if (!($type === "")) $clause .= " AND".$type;
    }
    return $clause;
}

/*
 * Returns the clause to select data from meter_read which is built from the
 * options provided.  No field is compulsory.  If no fields are provided,
 * FROM meter_read will be returned.
 */
function getMeterClause($meterList, $fromDate, $toDate, $interval = -1, $voltages = false, 
    $current = false, $power = false, $voltagePhase = false) {
    
    $time = getTimeClause($fromDate, $toDate);
    $interval = getIntervalClause($interval);
    $type = getTypeClause($voltages, $current, $power, $voltagePhase);

    $clause = " FROM meter_read WHERE ";
    if ($meterList === "all") { // No need to filter by ids
        if (!($time === "")) $clause .= $time;

        if ($time === "" && !($interval === "")) $clause .= $interval;
        else if (!($interval === "")) $clause .= " AND".$interval;

        if ($time === "" && $interval === "" && !($type === "")) $clause .= $type;
        else if (!($type === "")) $clause .= " AND".$type;

        if ($time === "" && $interval === "" && $type === "") $clause .= "1";
    } else {
        $clause .= " meter_id".getIdClause($meterList);

        if (!($time === "")) $clause .= " AND".$time;

        if (!($interval === "")) $clause .= " AND".$interval;

        if (!($type === "")) $clause .= " AND".$type;
    }
    return $clause;
}
?>