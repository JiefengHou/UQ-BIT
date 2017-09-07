<!--  The navbar page is navagation bar, it can import into
  different pages to reduce duplicate code.
  -->
<?php
  // Global imports
  require_once('import.php');
  require_once ('dbconnect.php');
?>
<html>
  <head>
    <!-- css and javascript files -->
    <link href="css/bootstrap-select.min.css" rel="stylesheet" />
    <link href="css/bootstrap-switch.min.css" rel="stylesheet" />
    <script src="js/bootstrap-switch.min.js"></script> 
    <script src="js/bootstrap-select.min.js"></script> 
    <script type="text/javascript" src="js/navbar.js"></script> 
    <script>
      //  Initialize datetimepicker
      $(document).ready( function() {
        $('#select').selectpicker();
        $('#net-filter').selectpicker();
        populateNetworkList();
      });
    </script> 
  </head>
  <body>
    <!-- navigation bar -->
    <div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
      <div class="container-fluid">
        <div class="navbar-header"> 
          <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse"> 
            <span class="sr-only">Toggle navigation</span> <span class="icon-bar"></span> 
            <span class="icon-bar"></span> 
            <span class="icon-bar"></span>
          </button> 
          <a class="navbar-brand" href="index.php">UQ Visualiser</a> 
        </div>
        <div class="navbar-collapse collapse nav-search">
          <form class="navbar-form navbar-left" role="search">
            <div class="form-group input-search">
              <input id="input" type="text" class="form-control" placeholder="Search..." onkeydown="preventEnter();" 
                onkeyup="pageSearch();" autocomplete="off" /> 
              <div id="result"></div>
            </div>
            <select id="select" class="selectpicker" data-width="140px" onchange="pageSearch();">
              <option value="Sub Net">Substation</option>
              <option value="Endpoint">Endpoint</option>
              <option value="High Voltage Line">High Voltage Line</option>
              <option value="Low Voltage Line">Low Voltage Line</option>
            </select>
            <button id="rst-search-button" type="button" class="btn btn-primary overview-button" 
              onclick="resetMap();">Reset Map
            </button> 
            <select id="net-filter" class="selectpicker" data-width="140px" data-size="14" 
              onchange="applyNetFilter()" multiple>
            </select> 
            <button id="filter-button" type="button" disabled="true" class="btn btn-primary overview-button" 
              onclick="applyNetFilter()">Filter
            </button> 
            <button id="clear-filter-button" type="button" disabled="true" class="btn btn-primary overview-button" 
              onclick="updateMap(false, true, true);">Clear Filter
            </button> 
          </form>
          <ul id="navi1" class="nav navbar-nav navbar-right">
            <li id="overview-nav" class=""><a href="index.php">Overview</a></li>
            <li id="user-guide-nav" class=""><a href="user_guide.php">User Guide</a></li>
            <li id="about-us-nav" class=""><a href="about_us.php">About Us</a></li>
            <li id="FAQ-nav" class=""><a href="FAQ.php">FAQ</a></li>
          </ul>
          <ul id="navi2" class="nav navbar-nav navbar-right">
            <li class="dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown">Pages<span class="caret"></span></a> 
              <ul class="dropdown-menu" role="menu">
                <li id="overview-nav" class=""><a href="index.php">Overview</a></li>
                <li id="user-guide-nav" class=""><a href="user_guide.php">User Guide</a></li>
                <li id="about-us-nav" class=""><a href="about_us.php">About Us</a></li>
                <li id="FAQ-nav" class=""><a href="FAQ.php">FAQ</a></li>
              </ul>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </body>
</html>