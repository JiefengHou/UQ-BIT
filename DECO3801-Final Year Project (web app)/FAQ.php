<!-- The FAQ page including many frequently asked questions -->
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="author" content="UQ Visualiser">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>UQ Visualiser</title>
    <link rel="icon" type="image/png" href="favicon.png" />
    <!-- css and javascript files -->
    <link href="css/style.css" rel="stylesheet">
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <script>
      $(document).ready( function() {
        $('#FAQ-nav').addClass( 'active' );
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
          <h1><span>Frequently Asked Questions</span></h1>
          <h4><em>UQ Visualiser</em></h4>
        </div>
        <hr class="line" />
        <div id="content">
          <div class="content-guide">
            <div class="content-title">
              <h2>Quick Find</h2>
            </div>
            <div class="content-content">
              <ol id="quick-find" class="guide-list">
                <li><a href="#faq1">Who does the UQ Visualiser benefit?</a></li>
                <li><a href="#faq2">Why was it commissioned?</a></li>
                <li><a href="#faq3">What does the client's algorithm actually do? Why is it better than just raw data?</a></li>
                <li><a href="#faq4">What is a substation?</a></li>
                <li><a href="#faq5">What is an end point?</a></li>
                <li><a href="#faq6">What is a high voltage line?</a></li>
                <li><a href="#faq7">What is a low voltage line?</a></li>
                <li><a href="#faq8">What can I do with this the data from the visualiser?</a></li>
                <li><a href="#faq9">Do you have an API that I can use to access this information directly and perform my own calculations?</a></li>
                <li><a href="#faq10">How many markers does the visualiser support?</a></li>
                <li><a href="#faq11">What data does the visualiser store?</a></li>
                <li><a href="#faq12">How much data does the visualiser store? How far back does it go?</a></li>
              </ol>
            </div>
          </div>
          <hr class="line" />
          <div class="content-guide">
            <div class="content-title">
              <h2>FAQs</h2>
            </div>
            <div class="content-content">
              <ol id="faq" class="guide-list">
                <li id="faq1">
                  <h4>Who does the UQ Visualiser benefit?</h4>
                  <p>Our system was designed for Olav Krause, an electrical engineering lecturer at UQ. 
                    It is designed to be able to provide useful information about the UQ power grid for experienced, 
                    technical users, as well as being able to be used by inexperienced users (e.g. students) for 
                    general interest purposes.</p>
                </li>
                <li id="faq2">
                  <h4>Why was it commissioned?</h4>
                  <p>The UQ Visualiser was commissioned as a proof of concept for the clients Adaptive State Estimator (ASE) 
                  algorithm, and to allow them to interact with the algorithms output in an intuitive way.</p>
                </li>
                <li id="faq3">
                  <h4>What does the client's algorithm actually do? Why is it better than just raw data?</h4>
                  <p>This algorithm uses information about the network topology and the raw readings from nodes to form 
                  an estimate about the networks state. This allows for a more accurate view of the network.</p>
                </li>
                <li id="faq4">
                  <h4>What is a substation?</h4>
                  <p>A substation houses the transformers which handle distributing the voltages received from a high voltage 
                  power line to voltages acceptable for use within buildings and everyday appliances. 
                  It is a central point of a particular network and provides connections to additional networks.</p>
                </li>
                <li id="faq5">
                  <h4>What is an end point?</h4>
                  <p>An endpoint is a terminating location on low voltage line. These are commonly buildings, but may also be 
                  greenhouses, solar panels, etc. The voltages at the end points are suitable for use in everyday devices.</p>
                </li>
                <li id="faq6">
                  <h4>What is a high voltage line?</h4>
                  <p>A high voltage line connects two substations on a power grid. The voltages along these lines are high 
                  and not suitable for use within buildings or devices.</p>
                </li>
                <li id="faq7">
                  <h4>What is a low voltage line?</h4>
                  <p>A low voltage line is generally a connection between a substation and endpoint. It could also be a 
                  connection between two endpoints. The voltages on these lines are acceptable for use within buildings 
                  and everyday appliances.</p>
                </li>
                <li id="faq8">
                  <h4>What can I do with this the data from the visualiser?</h4>
                  <p>The data from the visualiser can be used to identify trends or faults in the UQ power grid. The graphed 
                  history is the most important tool in this context, and displays the history of the node to the user so 
                  they can easily discern when any noticeable changes in the power grid happened.</p>
                </li>
                <li id="faq9">
                  <h4>Do you have an API that I can use to access this information directly and perform my own calculations?</h4>
                  <p>Yes, the following requests can be made to access the power grid data. All requests return JSON objects 
                  (application/json type content).</p>
                  <br>
                  <table class="table table-bordered table-hover">
                    <tr>
                      <td><b>Page</b></td>
                      <td colspan="2">/getjson.php?</td>
                    </tr>
                    <tr>
                      <td><b>Request</b></td>
                      <td colspan="2">GET</td>
                    </tr>
                    <tr>
                      <td><b>Return Type</b></td>
                      <td colspan="2">JSON object (application/json type content)</td>
                    </tr>
                    <tr>
                      <td><b>Returns</b></td>
                      <td colspan="2">An array containing, arrays which contain the id, reading type, reading value 
                      and reading time that match the parameters.</td>
                    </tr>
                    <tr>
                      <td><b>Property</b></td>
                      <td><b>Expected Type</b></td>
                      <td><b>Description</b></td>
                    </tr>
                    <tr>
                      <td>Nodes</td>
                      <td>List&lt;Integer&gt; or "all"</td>
                      <td>A comma separated list of node ids or the string "all" if all nodes are wanted.</td>
                    </tr>
                    <tr>
                      <td>Lines</td>
                      <td>List&lt;Integer&gt; or "all"</td>
                      <td>A comma separated list of line ids or the string "all" if all lines are wanted.</td>
                    </tr>
                    <tr>
                      <td>Meters</td>
                      <td>List&lt;Integer&gt; or "all"</td>
                      <td>A comma separated list of meter ids or the string "all" if all meters are wanted.</td>
                    </tr>
                    <tr>
                      <td>FromDate</td>
                      <td>Date String<br>(Y-m-d H:i:s)</td>
                      <td>The date time to get data from.</td>
                    </tr>
                    <tr>
                      <td>ToDate</td>
                      <td>Date String<br>(Y-m-d H:i:s)</td>
                      <td>The date time to get data to.</td>
                    </tr>
                  </table>
                  <br>
                  <table class="table table-bordered table-hover">
                    <tr>
                      <td><b>Page</b></td>
                      <td colspan="2">/getcsv.php?</td>
                    </tr>
                    <tr>
                      <td><b>Request</b></td>
                      <td colspan="2">GET</td>
                    </tr>
                    <tr>
                      <td><b>Return Type</b></td>
                      <td colspan="2">String</td>
                    </tr>
                    <tr>
                      <td><b>Returns</b></td>
                      <td colspan="2">The file name of the prepared download which contains data matching the request 
                      (can be passed to downloadFile.php)</td>
                    </tr>
                    <tr>
                      <td><b>Property</b></td>
                      <td><b>Expected Type</b></td>
                      <td><b>Description</b></td>
                    </tr>
                    <tr>
                      <td>Nodes</td>
                      <td>List&lt;Integer&gt; or "all"</td>
                      <td>A comma separated list of node ids or the string "all" if all nodes are wanted.</td>
                    </tr>
                    <tr>
                      <td>Lines</td>
                      <td>List&lt;Integer&gt; or "all"</td>
                      <td>A comma separated list of line ids or the string "all" if all lines are wanted.</td>
                    </tr>
                    <tr>
                      <td>Meters</td>
                      <td>List&lt;Integer&gt; or "all"</td>
                      <td>A comma separated list of meter ids or the string "all" if all meters are wanted.</td>
                    </tr>
                    <tr>
                      <td>FromDate</td>
                      <td>Date String<br>(Y-m-d H:i:s)</td>
                      <td>The date time to get data from.</td>
                    </tr>
                    <tr>
                      <td>ToDate</td>
                      <td>Date String<br>(Y-m-d H:i:s)</td>
                      <td>The date time to get data to.</td>
                    </tr>
                  </table>
                  <br>
                  <table class="table table-bordered table-hover">
                    <tr>
                      <td><b>Page</b></td>
                      <td colspan="2">/datacheck.php?</td>
                    </tr>
                    <tr>
                      <td><b>Request</b></td>
                      <td colspan="2">GET</td>
                    </tr>
                    <tr>
                      <td><b>Return Type</b></td>
                      <td colspan="2">JSON object (application/json type content).</td>
                    </tr>
                    <tr>
                      <td><b>Returns</b></td>
                      <td colspan="2">
                        <ol>
                          <li>Returns an array containing the first and last timestamp for the matching items, or</li>
                          <li>Returns an array of meter ids attached to the given item.</li>
                        </ol>
                      </td>
                    </tr>
                    <tr>
                      <td><b>Property</b></td>
                      <td><b>Expected Type</b></td>
                      <td><b>Description</b></td>
                    </tr>
                    <tr>
                      <td>Id</td>
                      <td>Integer</td>
                      <td>The id of the line or node to check for data.</td>
                    </tr>
                    <tr>
                      <td>Type</td>
                      <td>String</td>
                      <td>"node" if the given id is for a node, "line" if it is for a line.</td>
                    </tr>
                    <tr>
                      <td>Meter</td>
                      <td>Boolean</td>
                      <td>If true, the dates returned will be instead those associated with the meters attached to the given 
                      id/type.
                        <br>If false, the dates returned will be associated with the given type/id.
                      </td>
                    </tr>
                    <tr>
                      <td>ReturnMeter</td>
                      <td>Boolean</td>
                      <td>If true, returns the meter ids attached to the given id/type (meter must also be true).
                        <br>If false, the dates returned will be associated with the given type/id.
                      </td>
                    </tr>
                  </table>
                  <br>
                  <table class="table table-bordered table-hover">
                    <tr>
                      <td><b>Page</b></td>
                      <td colspan="2">/downloadFile.php?</td>
                    </tr>
                    <tr>
                      <td><b>Request</b></td>
                      <td colspan="2">GET</td>
                    </tr>
                    <tr>
                      <td><b>Returns</b></td>
                      <td colspan="2">Downloaded zip with the name provided.</td>
                    </tr>
                    <tr>
                      <td><b>Property</b></td>
                      <td><b>Expected Type</b></td>
                      <td><b>Description</b></td>
                    </tr>
                    <tr>
                      <td>File</td>
                      <td>String</td>
                      <td>Takes the name of a file from a getcsv.php request and downloads the file (note no other files 
                      can be downloaded).</td>
                    </tr>
                  </table>
                </li>
                <li id="faq10">
                  <h4>How many markers does the visualiser support?</h4>
                  <p>The visualiser currently displays the data for two networks, 1 and 11. However, the visualiser 
                  was designed to be able to be expanded by the client to include all of UQ's networks (around 40) 
                  with thousands of markers.</p>
                </li>
                <li id="faq11">
                  <h4>What data does the visualiser store?</h4>
                  <p>The visualiser stores the raw data read from the power meters, along with the processed output 
                  from the ASE algorithm. This data includes all three phases of electrical properties such as 
                  voltage, current, power and reactive power.</p>
                </li>
                <li id="faq12">
                  <h4>How much data does the visualiser store? How far back does it go?</h4>
                  <p>Our system was designed for Olav Krause, an electrical engineering lecturer at UQ. The 
                  visualiser stores data on the electrical properties of the two example networks (1 and 11), and 
                  stores approximately 200MB of data per day. Because of the sheer amount of data being recorded 
                  and the limited system the server runs on, the visualiser only stores one weeks worth of data.</p>
                </li>
              </ol>
            </div>
          </div>
        </div>
      </div>
    </div>
  </body>
</html>