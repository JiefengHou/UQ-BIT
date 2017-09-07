A visualiser and tester for COMP3702 Assignment 1, by Dimitri Klimenko (tutor).


(1) Commands and arguments
The runnable files and their arguments are:
    tester.Tester [-e maxError] [-v] problem-file [solution-file]
    visualiser.Visualiser [problem-file] [solution-file]


(2) Running the Visualiser
To run it, simply run visualiser.jar with Java 7 (double-clicking should work
if Java is installed properly). If this doesn't work or you want to run it
with a different version, I recommend using Eclipse - simply add the contents
of a1-tools.zip to a new project.
Alternatively, see the manual compilation instructions in section (4).

You can also run it from the command line with optional
command-line arguments:
    java -jar visualiser.jar [problem-file] [solution-file]

Note that these commands may require you to use full path to java.exe,
as per section (5).


(3) Running the Tester
To run the tester in Eclipse, simply add the contents of a1-tools.zip
to a new project, and then create a new Run Configuration for tester.Tester;
then add the command line arguments for Tester as per the above specifications.

The [-e maxError] argument allows you to specify the maximum allowable
floating point error; the default value is 1e-5.

The [-v] argument gives the line number for each invalid configuration whenever
one of the tests fails.

Specifying a problem file without a solution file will simply verify that the
initial and goal states are valid; if a solution file is also given, all
of the requirements of the assignment will be tested to ensure the solution
is valid.

For example, the example I/O from the assignment specifications can 
be tested using the following command-line arguments:
     example_input.txt example_output.txt
    
Alternatively, see the manual compilation instructions in section (4).


(4) Manual Compilation
If you want to compile and run the code manually, you will need to do the
following:
1) Download and install Apache Ant.
2) Extract a1-tools.zip to the desired folder.
3) From within that folder, run the command
    ant

The following commands should now work for running the visualiser and tester:
    java -cp bin visualiser.Visualiser [problem-file] [solution-file]
    java -cp bin tester.Tester [-e maxError] [-v] problem-file [solution-file]

The commands above may require full paths to Java; see section (5).


(5) The command line and the system path
Note that for the command-line commands to work Java would have to be on your
system path; if not, you'll have to specify a full path instead of just 
"java", e.g.
"C:\Program Files (x86)\Java\jdk1.7.0_25\bin\java.exe"
or
/usr/java/jdk1.7.0_25/bin/java
