<!-- the user guide page that explain how to use UQ 
  Power Grid Visualiser, including overview 
  guide and functional overview.
  -->
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="author" content="UQ Visualiser">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>UQ Visualiser</title>
    <link rel="icon" type="image/png" href="favicon.png" />
    <!-- css style -->
    <link href="css/style.css" rel="stylesheet">
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <!-- javascript file -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <script>
      $(document).ready( function() {
        $('#user-guide-nav').addClass( 'active' );
      });
    </script>
  </head>
  <body>
    <!-- navigation -->
    <?php include('navbar_aboutme.php');?>
    <!-- content -->
    <div id="wrapper">
      <div id="container">
        <div id="title">
          <img src="images/logo.png" alt="UQ Visualiser" class="img-rounded logo">
          <h1><span>User Guide</span></h1>
          <h4><em>UQ Visualiser</em></h4>
        </div>
        <hr class="line" />
        <div id="content">
          <!-- overview guide -->
          <div class="content-guide">
            <div class="content-title">
              <h2>Overview Guide</h2>
            </div>
            <div class="content-content">
              <img src="images/initial-map-overview.png" alt="Overview of the UQ Visualiser" class="img-thumbnail"></img>
              <ol class="guide-list">
                <li>Clicking this will redirect the user back to the main overview page.</li>
                <li>This bar allows the user to search for a particular substation or node by 
                typing in the id they are looking for.</li>
                <li>This drop-down menu allows the user select what they are looking for (Substation, Endpoint, 
                  High Voltage Line, Low Voltage Line).
                </li>
                <li>This button resets the map back to the original state.</li>
                <li>This drop-down menu allows the user to filter networks on or off.</li>
                <li>This button simply applies the currently selected filters.</li>
                <li>This button simply clears the currently selected filters.</li>
                <li>Clicking this will redirect the user back to the main oveview page.</li>
                <li>Clicking this will redirect the user to the user guide page.</li>
                <li>Clicking this will redirect the user to the team bio's page.</li>
                <li>Clicking this will redirect the user to the FAQ page.</li>
                <li>This is the map visualisation of the UQ Visualiser displayed via Google Maps.</li>
                <li>This slider toggles substations on or off.</li>
                <li>This slider toggles high voltage lines on or off.</li>
                <li>This slider toggles endpoints on or off.</li>
                <li>This slider toggles low voltage lines on or off.</li>
                <li>This slider toggles straight lines on or off.</li>
              </ol>
            </div>
          </div>
          <hr class="line" />
          <!-- functional guide -->
          <div class="content-guide">
            <div class="content-title">
              <h2>Functional Guide</h2>
            </div>
            <div class="content-content">
              <img src="images/second-map-overview.png" alt="An overview when a node is clicked on" class="img-thumbnail"></img>
              <ol class="guide-list">
                <li>This shows the name and type of the currently clicked item.</li>
                <li>This shows the live data of the currently clicked item (updates asynchronously).</li>
                <li>This toggles the information panel to be shown or hidden.</li>
                <li>This removes the information panel from the screen.</li>
                <li>This allows the user to select a start time for which they wish to download the history for.</li>
                <li>This allows the user to select an end time for which they wish to download the history for.</li>
                <li>Clicking this button will download the history for the selected times as shown above.</li>
                <li>This shows the latitude and longitude of the currently selected item.</li>
                <li>This tab displays customization options for the currently displayed graph.</li>
                <li>This brings the user to a tab where they customize the graph to show data between specified dates.</li>
                <li>This slider toggles node comparisons on or off.</li>
                <li>This slider toggles between raw data and processed data.</li>
                <li>This slider toggles between 30 minute updates (on) and 5 second updates (off).</li>
                <li>This is where the graph displays the live data that from the currently selected item (if any).</li>
              </ol>
            </div>
          </div>
        </div>
      </div>
    </div>
  </body>
</html>