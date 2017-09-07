
###################################################################
#
#   CSSE1001 - Assignment 1
#
#   Student Number: 43034002    
#
#   Student Name: Jiefeng Hou (Nick)
#
###################################################################


def interact():
    get_marks_from_file('marks.csv')

    


"""Get marks module"""              
def get_marks_from_file(filename):
    """Return the marks from the file"""
    import csv
    f=open(filename,'rU')
    line=[]
    for c in f:
         
         line.append(c.strip('\n').split(','))
    print line
        


"""Update marks module"""       
def update_mark(all_marks, stud_num, mark, column, check_result_exists):
    """Update the marks into a file """

    column=int(column)
    a=0
    for c in all_marks:
        if c[0]==stud_num:
            a=a+1
            if check_result_exists!=bool(c[column]):
                c[column]=mark
            else:
                print '{0} has a value in column {1} - update ignored'.format(c[0],column)

    if stud_num !=c[0] and a==0:
        print '{0} cannot be merged - no match found.'.format(stud_num)
        a=a+1   

"""Merge marks module"""       
def merge_marks(all_marks, new_marks, column):
    """Merge makrs from a file and a new file"""
    a=0
    column=int(column)
    for c in all_marks:
        for i in new_marks:
            if c[0]==i[0]:
                
                if c[column]=='':
                    c[column]=i[column]
                    
                else:
                    print '{0} has a value in column {1} - update ignored'.format(c[0],column)
                    
            
    if c[0]!=i[0] and a==0:
        print '{0} cannot be merged - no match found.'.format(i[0])
        a=a+1

                
"""Save marks module"""      
def save_marks_to_file(records,filename):
    """Save marks to a new file"""
    import csv
    if filename=='':
        print 'Merge ignored.'
    else:
        fileName=csv.writer(open(filename,'wb'))
        for c in records:
            fileName.writerow(c)
        print 'Done.'

            

##################################################
# !!!!!! Do not change (or add to) the code below !!!!!
# 
# This code will run the interact function if
# you use Run -> Run Module  (F5)
# Because of this we have supplied a "stub" definition
# for interact above so that you won't get an undefined
# error when you are writing and testing your other functions.
# When you are ready please change the definition of interact above.
###################################################

if __name__ == '__main__':
    interact()
