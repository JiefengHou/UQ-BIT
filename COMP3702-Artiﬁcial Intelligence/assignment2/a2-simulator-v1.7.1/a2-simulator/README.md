comp3702-a2-2014
================

##Class to place solution in

The student should place his/her code in the solver package. 
In particular the student should overwrite the Consultant class 
solveTour method.

##Main Simulator Classes

**RaceSimTools**
A static class contains most of the functions for running the logic 
behind a race simulation. The student can probably make use of the 
functions in here in determining his/her strategy.

**RaceSim**
For simulating a race. Calls functions from RaceSimTools.

**Setup**
Use this for loading the input files

**Tour**
Simulates an entire tour. Creates a RaceSim for each race.

##Other Classes

The following are supporting, immutable classes 

**Action** 
An enumeration {FS, FM, FF, NE, SE, ST} 

**Cycle**
Defines cycle types 

**Direction**
An enumeration for compass directions, {N, NE, E, SE, ...}

**Distractor**
Representation of a distractor. 

**GridCell**
A simple cell, containing row and column 

**Opponent**
Representation of an opponent. Contains its string id, policy, and current position

**Player**
Representation of a player. Contains its string id, cycle, and current position

**Track**
Representation of a track. Contains a map, which records empty cells and obstacle cells,
as well as player starting positions. Also contains Opponents and Distractors, set
to their starting positions
