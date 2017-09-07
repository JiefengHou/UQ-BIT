#
# Tests for assignment 1
# This tests the assignments functions (except interact).
# Only a few tests are done and so are by no means comprehensive.
# The error output of the functions are not checked.
#
# It is not necessary to understand this code in order to do assignment 1.
#


import sys

# The following if-else is for cases in IDLE where the Python interpreter does
# not restart on F5 - i.e. Linux and possibly Mac
# It has no effect when the interpreter does restart on F5.
#
if globals().has_key('init_modules'):
    for m in [x for x in sys.modules.keys() if x not in init_modules]:
        del(sys.modules[m])
else:
    init_modules = sys.modules.keys()

print "Running simple tests on assign1.py\nThis program must be run in the same folder that contains assign1.py, marks.py, marks1.py and assign1_marks.py"

print 
print "Importing assign1.py..."


try:
    import assign1
except Exception as e:
    print str(e)
    sys.exit()

print "Imported."

print
print "Testing get_marks_from_file('marks1.csv')"
print

try:
    data = [['s111111', '10', '', '', '', ''], ['s222222', '', '', '', '', ''], ['s333333', '10', '', '', '', ''], ['s444444', '', '', '', '', ''], ['s555555', '7', '', '', '', ''], ['s666666', '9', '', '', '', ''], ['s777777', '', '', '', '', '']]
    records = assign1.get_marks_from_file('marks1.csv')
    if records == data:
        print 'OK'
    else:
        print "Wrong: you got {0} - the correct result is {1}".format(records, data)
except Exception as e:
    print >> sys.stderr, str(e)

print
print "Testing get_marks_from_file('assign1_marks.csv')"
print

try:
    data = [['s555555', '7'], ['s333333', '10'], ['s666666', '9'], ['s111111', '10'], ['s999999', '9']]
    records = assign1.get_marks_from_file('assign1_marks.csv')
    if records == data:
        print 'OK'
    else:
        print "Wrong: you got {0} - the correct result is {1}".format(records, data)
except Exception as e:
    print >> sys.stderr, str(e)


marks = [['s111111', '10', '', '', '', ''], ['s222222', '', '', '', '', ''], ['s333333', '10', '', '', '', ''], ['s444444', '', '', '', '', ''], ['s555555', '7', '', '', '', ''], ['s666666', '9', '', '', '', ''], ['s777777', '', '', '', '', '']]
new_marks = [['s111111', '10', '10', '', '', ''], ['s222222', '', '', '', '', ''], ['s333333', '10', '', '', '', ''], ['s444444', '', '', '', '', ''], ['s555555', '7', '', '', '', ''], ['s666666', '9', '', '', '', ''], ['s777777', '', '', '', '', '']]
print
print "Testing update_mark(marks, 's111111', '10', 2, True) where marks is {0}".format(marks)
print

try:
    assign1.update_mark(marks, 's111111', '10', 2, True)
    if marks == new_marks:
        print 'OK'
    else:
        print "Wrong: marks should now be {0}, you got {1}".format(new_marks, marks)
except Exception as e:
    print >> sys.stderr, str(e)



marks = [['s111111', '', '', '', '', ''], ['s222222', '', '', '', '', ''], ['s333333', '', '', '', '', ''], ['s444444', '', '', '', '', ''], ['s555555', '', '', '', '', ''], ['s666666', '', '', '', '', ''], ['s777777', '', '', '', '', '']]
a1_marks = [['s555555', '7'], ['s333333', '10'], ['s666666', '9'], ['s111111', '10'], ['s999999', '9']]
new_marks = [['s111111', '10', '', '', '', ''], ['s222222', '', '', '', '', ''], ['s333333', '10', '', '', '', ''], ['s444444', '', '', '', '', ''], ['s555555', '7', '', '', '', ''], ['s666666', '9', '', '', '', ''], ['s777777', '', '', '', '', '']]
print
print "Testing merge_marks(marks, a1_marks, 1) where \nmarks = {0}\nand\na1_marks = {1}".format(marks, a1_marks)
print

try:
    assign1.merge_marks(marks, a1_marks, 1)
    if marks == new_marks:
        print 'OK'
    else:
        print "Wrong: marks should now be {0}, you got {1}".format(new_marks, marks)
except Exception as e:
    print >> sys.stderr, str(e)


print
print "Testing save_marks_to_file(marks, 'tmp.csv') where\nmarks = {0}".format(marks)
print

try:
    correct_text = 's111111,10,,,,\ns222222,,,,,\ns333333,10,,,,\ns444444,,,,,\ns555555,7,,,,\ns666666,9,,,,\ns777777,,,,,\n'
    assign1.save_marks_to_file(marks, 'tmp.csv')
    fp = open('tmp.csv', 'rU')
    text = fp.read()
    fp.close()
    if text == correct_text:
        print 'OK'
    else:
        print "Wrong: the contents of 'tmp.csv' should be \n{0}\nyou got \n{1}".format(correct_text, text)
except Exception as e:
    print >> sys.stderr, str(e)
    
