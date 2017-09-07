<!-- The about us page that show some information about UQ power grid team and client -->
<html lang="en">
  <head>
    <meta charset="utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="author" content="UQ Visualiser" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>UQ Visualiser</title>
    <link rel="icon" type="image/png" href="favicon.png" />
    <!-- css and javascript files --> 
    <link href="css/style.css" rel="stylesheet" />
    <link href="css/bootstrap.min.css" rel="stylesheet" />
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script> 
    <script src="js/bootstrap.min.js"></script> 
    <script>
      $(document).ready( function() {
        $('#about-us-nav').addClass( 'active' );
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
          <img src="images/logo.png" alt="UQ Visualiser" class="img-rounded logo" /> 
          <h1><span>About Us</span></h1>
          <h4><em>UQ Visualiser</em></h4>
        </div>
        <hr class="line" />
        <div id="content">
          <div class="content-guide">
            <div class="content-title">
              <h2>Team Bio's</h2>
            </div>
            <div class="content-content">
              <div class="one-person">
                <img src="images/merrick.jpg" alt="Merrick Heley" class="img-thumbnail personal-img" /> 
                <div class="personal-detail">
                  <h3>Merrick Heley</h3>
                  <h4><span><em>Project Manager</em></span></h4>
                  <p> Merrick is a 5th year Mechatronics Engineering and Computer Science undergraduate who took on the 
                    role of Project Manager, he was involved with multiple aspects of this project including front end 
                    development, user testing, documentation, co-oridnating team and client meetings, bug checking and 
                    many other miscellaneous tasks.
                  </p>
                </div>
              </div>
              <hr class="line" />
              <div class="one-person">
                <img src="images/craig.jpg" alt="Craig Knott" class="img-thumbnail personal-img" /> 
                <div class="personal-detail">
                  <h3>Craig Knott</h3>
                  <h4><span><em>Backend Programmer</em></span></h4>
                  <p> Craig is a 5th year Mechatronics Engineering and Information Technology undergraduate who was the 
                    lead back end programmer on this project, implementing the DataDaemon, which extracts raw data from the 
                    smart meters and processes it using an algorithm provided by Dr Olav Krause before inserting it into the 
                    database. Craig was also involved in the development of the Graphing and Information panels for the Front 
                    End.
                  </p>
                </div>
              </div>
              <hr class="line" />
              <div class="one-person">
                <img src="images/jason.jpg" alt="Jason Barton" class="img-thumbnail personal-img" /> 
                <div class="personal-detail">
                  <h3>Jason Barton</h3>
                  <h4><span><em>Frontend Programmer</em></span></h4>
                  <p> Jason is a 3rd year Information Technology undergraduate who took on the role of front end support 
                    developer for this project but was involved in various things throughout this project such as documentation, 
                    user testing, bug checking and other miscellaneous activities.
                  </p>
                </div>
              </div>
              <hr class="line" />
              <div class="one-person">
                <img src="images/jayke.jpg" alt="Jayke Anderson" class="img-thumbnail personal-img" /> 
                <div class="personal-detail">
                  <h3>Jayke Anderson</h3>
                  <h4><span><em>Database/Frontend Programmer</em></span></h4>
                  <p> Jayke is a 4th year Accounting and Information Technology undegraduate, in this project he took on 
                    the role of lead database designer, he created a scalable database that could be modified and queried 
                    <u>extremely</u> efficiently, as well as working on multiple aspects of the front end such as a 
                    download past data function and other miscellaneous things.
                  </p>
                </div>
              </div>
              <hr class="line" />
              <div class="one-person">
                <img src="images/nick.jpg" alt="Jiefeng Hou (Nick)" class="img-thumbnail personal-img" /> 
                <div class="personal-detail">
                  <h3>Jiefeng Hou (Nick)</h3>
                  <h4><span><em>Frontend Programmer</em></span></h4>
                  <p> Nick is a 3rd year Information Technology undergraduate and took the role of lead front end developer 
                    for this project, he implemented a significant amount of the front end, including most of the features 
                    that are currently visible on the website at present.
                  </p>
                </div>
              </div>
            </div>
          </div>
          <hr class="line" />
          <div class="content-guide">
            <div class="content-title">
              <h2>Client Bio's</h2>
            </div>
            <div class="content-content">
              <div class="one-person">
                <img src="images/olav.jpg" alt="Dr Olav Krause" class="img-thumbnail personal-img" /> 
                <div class="personal-detail">
                  <h3>Olav Krause</h3>
                  <h4><span><em>Professor</em></span></h4>
                  <p> Dr Olav Krause has a PHD in Electrical Engineering and also a Masters of Science, both attained 
                    at the University of Dortmund. He envisioned this project after he found there was no visual 
                    representation of the UQ Power Grid currently implemented, and thought it beneficial if there was 
                    such an implementation.
                  </p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </body>
</html>