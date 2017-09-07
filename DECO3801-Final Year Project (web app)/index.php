<!-- The index page that displays the graphical visualisation
  of the UQ Power Grid, users can also search in the search 
  bar for various different option (substations, buildings
  etc.) This page also contains the main navigation to the
  history page and the logout function-->
<!DOCTYPE html>
<?php
  require_once ('dbconnect.php');
  include ('map_data.php');
  $subLines = getLines("Substation", "Substation");
  $endLines = getLines("Network Centre", "Building");
  $subNodes = getNodes("Substation"); 
  $netNodes = getNodes("Network Centre");
  $endNodes = getNodes("Building");
?>
<html lang="en">
  <head>
    <meta charset="utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <meta name="author" content="UQ Visualiser" />
    <title>UQ Visualiser</title>
    <link rel="icon" type="image/png" href="favicon.png" />
    <?php
      // Global imports
      require_once('import.php');
    ?> 
    <!-- css and javascript files --> 
    <link href="css/bootstrap-datetimepicker.min.css" rel="stylesheet" />
    <script src="https://maps.googleapis.com/maps/api/js?libraries=geometry"></script> 
    <script type="text/javascript" src="js/canvasjs.min.js"></script> 
    <script type="text/javascript" src="js/graphing.js"></script> 
    <script type="text/javascript" src="js/map.js"></script> 
    <script>
      var subLines = <?php echo json_encode($subLines); ?>;
      var endLines = <?php echo json_encode($endLines); ?>;
      var subNodes = <?php echo json_encode($subNodes); ?>;
      var netNodes = <?php echo json_encode($netNodes); ?>;
      var endNodes = <?php echo json_encode($endNodes); ?>;     
      // load function when the page is loading 
      $(document).ready( function() {      
        // add class 'active' to nav bar list which id is overview-nav 
        $('#overview-nav').addClass( 'active' );
        // Initialize switches 
        $('#substation').bootstrapSwitch();
        $('#endpoint').bootstrapSwitch();
        $('#highVoltageLine').bootstrapSwitch();
        $('#lowVoltageLine').bootstrapSwitch();
        $('#straight-lines').bootstrapSwitch();
        $('#nodeComparison').bootstrapSwitch();
        $('#processedOrRawData').bootstrapSwitch();
        $('#intervalOpt').bootstrapSwitch();
        //  Initialize datetimepicker
        $('#downloadFromDate').datetimepicker({
          defaultDate: null
        });
        $('#downloadToDate').datetimepicker({
          defaultDate: null
        });
        $('#graphFromDate').datetimepicker({
          defaultDate: null
        });
        $('#graphToDate').datetimepicker({
          defaultDate: null     
        });
        //  show collapse 
        $('#collapseOne').collapse('show');
        $('#collapseTwo').collapse('show');
        // Call initialize and checkBox function when page is loading 
        initializeMap(subLines, endLines, subNodes, netNodes, endNodes);
       });
    </script> 
    <!-- HTML5 Shim and Respond.js IE8 sup port of HTML5 elements and media queries --> 
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// --> 
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]--> 
  </head>
  <body>
    <!-- navigation --> 
    <?php include('navbar.php');?> 
    <!-- content --> 
    <div class="container1">
      <div id="map">
        <!-- map --> 
        <div id="map_canvas" class="overview-map"></div>
        <!-- checkbox (switches) --> 
        <div id="checkboxes" class="overview-checkbox">
          <div class="overview-box" title="Substations">
            <img src="images/icon-substation2.png" class="img-thumbnail img-checkbox" alt="Substations" /> 
            <div class="bootstrap-switch bootstrap-switch-small overview-switch" onclick="toggleChange();"> 
              <input id="substation" type="checkbox" checked="" /> 
            </div>
          </div>
          <div class="overview-box" title="High Voltage Lines">
            <img src="images/icon-powerline3.png" class="img-thumbnail img-checkbox" alt="High Voltage Lines" /> 
            <div class="bootstrap-switch bootstrap-switch-small overview-switch" onclick="toggleChange();"> 
              <input id="highVoltageLine" type="checkbox" /> 
            </div>
          </div>
          <div class="overview-box" title="Endpoints">
            <img src="images/icon-endpoint2.png" class="img-thumbnail img-checkbox" alt="Endpoints" /> 
            <div class="bootstrap-switch bootstrap-switch-small overview-switch" onclick="toggleChange();"> 
              <input id="endpoint" type="checkbox" /> 
            </div>
          </div>
          <div class="overview-box" title="Low Voltage Lines">
            <img src="images/icon-powerline4.png" class="img-thumbnail img-checkbox" alt="Low Voltage Lines" /> 
            <div class="bootstrap-switch bootstrap-switch-small overview-switch" onclick="toggleChange();"> 
              <input id="lowVoltageLine" type="checkbox" /> 
            </div>
          </div>
          <div class="overview-box" title="Straight Lines">
            <img src="images/icon-powerline2.png" class="img-thumbnail img-checkbox" alt="Low Voltage Lines" /> 
            <div class="bootstrap-switch bootstrap-switch-small overview-switch" onclick="updateMap(false, false, false);"> 
              <input id="straight-lines" type="checkbox" /> 
            </div>
          </div>
        </div>
      </div>
      <!-- information window -->
      <div id="information">
        <div class="panel-group" id="accordion">
          <div class="panel panel-default">
            <div class="panel-heading">
              <h4 class="panel-title">
                <span class="glyphicon glyphicon-list-alt"></span> More Information 
                <div class="box-icon"> 
                  <a data-toggle="collapse" data-parent="#accordion" href="#collapseOne"> <span id="collapseIcon-one" class="glyphicon"></span> </a> 
                  <a href="#" onclick="hideInfor();"><span class="glyphicon glyphicon-remove"></span></a> 
                </div>
              </h4>
            </div>
            <div id="collapseOne" class="panel-collapse collapse">
              <div id="collapseOnePanel" class="panel-body">
                <h1 id="collapseOneNodeId"></h1>
                <hr />
                <div id="nodeValues" class="info-value-container">
                  <h2 class="collapseTitle collapseTitleFirst">Latest Values</h2>
                  <div id="latestVoltagesVals">
                    <h3 class="information-table-text">Voltages</h3>
                    <table id="latestVoltagesTable" class="table table-bordered">
                      <thead>
                        <tr>
                          <th class="text-center">Id</th>
                          <th class="text-center">V_a</th>
                          <th class="text-center">V_b</th>
                          <th class="text-center">V_c</th>
                        </tr>
                      </thead>
                      <tbody></tbody>
                    </table>
                  </div>
                  <div id="latestCurrentVals">
                    <h3 class="information-table-text">Currents</h3>
                    <table id="latestCurrentsTable" class="table table-bordered">
                      <thead>
                        <tr>
                          <th class="text-center">Id</th>
                          <th class="text-center">I_a</th>
                          <th class="text-center">I_b</th>
                          <th class="text-center">I_c</th>
                        </tr>
                      </thead>
                      <tbody></tbody>
                    </table>
                  </div>
                </div>
                <div id="downHist" class="info-value-container">
                  <h2 class="collapseTitle">Download History</h2>
                  <div class="form-group">
                    <label for="downloadToDate" class="dateText">Start Time</label> 
                    <div class="input-group date" data-format="yyyy-MM-dd hh:mm:ss" id="downloadFromDate" onchange="clearDownload()"> 
                      <input type="text" class="form-control" /> 
                      <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span> </span> 
                    </div>
                    <label for="downloadToDate" class="dateText">End Time</label> 
                    <div class="input-group date" id="downloadToDate" onchange="clearDownload()"> 
                      <input type="text" data-format="yyyy-MM-dd hh:mm:ss" class="form-control" /> 
                      <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span> </span> 
                    </div>
                    <button id="prepareDownBtn" type="button" class="btn btn-primary overview-button information-button" onclick="downloadCsv()" data-loading-text="Preparing...">Download</button> 
                    <a id="downloadLink" href=""><button id="downloadGetBtn" type="button" class="btn btn-primary overview-button information-button" onclick="clearDownload()">Get File</button></a> 
                  </div>
                </div>
                <div id="gpsCoords" class="info-value-container">
                  <h2 class="collapseTitle">GPS Coordinates</h2>
                  <p id="collapseOneGPSCoordss"></p>
                </div>
                <div id="imgDetails" class="info-value-container">
                  <h2 class="collapseTitle">Image</h2>
                  <img id="collapseOneImage" class="img-thumbnail" width="100%" height="100%" /> 
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <!-- graph window -->
      <div id="graph">
        <div class="panel-group" id="accordion1">
          <div class="panel panel-default">
            <div class="panel-heading">
              <h4 class="panel-title">
                <span class="glyphicon glyphicon-list-alt"></span> Graph 
                <div class="box-icon"> 
                  <a data-toggle="collapse" data-parent="#accordion1" href="#collapseTwo"> <span id="collapseIcon-two" class="glyphicon"></span> </a> 
                  <a href="#" onclick="hideInfor();"><span class="glyphicon glyphicon-remove"></span></a> 
                </div>
              </h4>
            </div>
            <div id="collapseTwo" class="panel-collapse collapse">
              <div id="collapseTwoPanel" class="panel-body">
                <div id="chartContainer" style="height: 230px; width:70%;"></div>
                <div id="contentContainer" style="height: 230px; width:28%;">
                  <div class="btn-group" data-toggle="buttons" style="width:100%; margin-top:5px;"> 
                    <label class="btn btn-primary active grapg-button" onclick="showGraphOption(1)"> <input type="radio" name="options" id="option1" autocomplete="off" checked="" />Options </label> 
                    <label class="btn btn-primary grapg-button" onclick="showGraphOption(0)"> <input type="radio" name="options" id="option2" autocomplete="off" />Date </label> 
                  </div>
                  <div id="graph-option">
                    <div class="graph-box">
                      <h4 class="switch-text">Node Comparison</h4>
                      <div class="bootstrap-switch bootstrap-switch-small graph-switch" onclick="initializeShowGraph()"> 
                        <input id="nodeComparison" type="checkbox" /> 
                      </div>
                    </div>
                    <div class="graph-box">
                      <h4 class="switch-text">Processed Data</h4>
                      <div class="bootstrap-switch bootstrap-switch-small graph-switch" onclick="switchRawProc()"> 
                        <input id="processedOrRawData" type="checkbox" checked="" /> 
                      </div>
                    </div>
                    <div class="graph-box">
                      <h4 class="switch-text">30min Interval</h4>
                      <div class="bootstrap-switch bootstrap-switch-small graph-switch" onclick="initializeShowGraph()"> 
                        <input id="intervalOpt" type="checkbox" checked="" /> 
                      </div>
                    </div>
                  </div>
                  <div id="graph-date">
                    <label for="graphFromDate" class="dateText">From</label> 
                    <div class="input-group date" data-format="yyyy-MM-dd hh:mm:ss" id="graphFromDate"> 
                      <input type="text" class="form-control" /> 
                      <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span> </span> 
                    </div>
                    <label for="graphToDate" class="dateText">To</label> 
                    <div class="input-group date" data-format="yyyy-MM-dd hh:mm:ss" id="graphToDate"> 
                      <input type="text" class="form-control" /> 
                      <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span> </span> 
                    </div>
                    <button id="filterGraphDate" type="button" class="btn btn-primary" onclick="initializeShowGraph()">Filter</button> 
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- Include all compiled plugins (below), or include individual files as needed --> 
    <script type="text/javascript" src="js/bootstrap-switch.min.js"></script> 
    <script type="text/javascript" src="js/moment.min.js"></script> 
    <script type="text/javascript" src="js/bootstrap-datetimepicker.min.js"></script> 
    <script>
      //  hide collapse when user click reset map button
      $('#rst-search-button').click(function(){
        if(collapseState) {
          $('.navbar-collapse').collapse('hide');
        }
      });
      //  show collapse which id is collapseIcon-one
      $('#collapseOne').on('shown.bs.collapse', function() { 
      $("#collapseIcon-one").removeClass("glyphicon-chevron-down").addClass("glyphicon-chevron-up");
      });
      //  hide collapse which id is collapseIcon-one
      $('#collapseOne').on('hidden.bs.collapse', function() { 
      $("#collapseIcon-one").removeClass("glyphicon-chevron-up").addClass("glyphicon-chevron-down");
      });
      //  show collapse which id is collapseIcon-two
      $('#collapseTwo').on('shown.bs.collapse', function() { 
        $("#collapseIcon-two").removeClass("glyphicon-chevron-down").addClass("glyphicon-chevron-up");
      });
      //  hide collapse which id is collapseIcon-two
      $('#collapseTwo').on('hidden.bs.collapse', function() { 
      $("#collapseIcon-two").removeClass("glyphicon-chevron-up").addClass("glyphicon-chevron-down");
      });
    </script>  
  </body>
</html>