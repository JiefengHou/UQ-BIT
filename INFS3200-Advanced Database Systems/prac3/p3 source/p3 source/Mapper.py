#!/usr/bin/env python
import time,datetime
import sys

curDate= datetime.datetime(*(time.localtime(time.time()))[0:6])

for line in sys.stdin:
    red=""
    print line
    line= line.strip()
    AthleteID, FirstName, LastName, DOB, Gender, Country= line.split('\t')
    if DOB!="":
        date1= time.strptime(DOB, "%Y/%m/%d %H:%M:%S")
        date2= datetime.datetime(*date1[0:6])
        red= curDate- date2
	print ",".join([AthleteID, FirstName, LastName, str((red.days)/365)]) 
        
    else:
        red="NULL"
	print ",".join([AthleteID, FirstName, LastName, str(red)])
