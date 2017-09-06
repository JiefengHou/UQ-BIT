"""
For course INFS3200
Author: Yang LIU (Ben)
Email: arkilis@gmail.com
Date: 19 June 2012
"""

#welcome info
print("University of Queensland, School of ITEE, INFS3200")
print(80*"=")
print("Help Tips:")
print("Usage 1: edit_distance(string1,string2) e.g. edit_distance('dog','cat')")
print("Usage 2: ed_names(sample name, int k) e.g.ed_names('Deng', 1)")
print("Usage 3: ed_group(int k) e.g ed_group(1)")
print(8*" "+"'Minxia', 1)")
print(9*" "+"k=1 means trying to match first name.")
print(9*" "+"k=2 means trying to match surname.")
print(80*"=")
print("\r\n");

"""
Compute the edit distance between two given
strings (s1 and s2)
"""
def edit_distance(s1, s2):
    d = {}
    lenstr1 = len(s1)
    lenstr2 = len(s2)
    for i in xrange(-1,lenstr1+1):
        d[(i,-1)] = i+1
    for j in xrange(-1,lenstr2+1):
        d[(-1,j)] = j+1
 
    for i in xrange(lenstr1):
        for j in xrange(lenstr2):
            if s1[i] == s2[j]:
                cost = 0
            else:
                cost = 1
            d[(i,j)] = min(
                           d[(i-1,j)] + 1, # deletion
                           d[(i,j-1)] + 1, # insertion
                           d[(i-1,j-1)] + cost, # substitution
                          )
            if i and j and s1[i]==s2[j-1] and s1[i-1] == s2[j]:
                d[(i,j)] = min (d[(i,j)], d[i-2,j-2] + cost) # transposition
 
    return d[lenstr1-1,lenstr2-1]

"""
get a list of strings that with ED< 2
k=1 means trying to match first name.
k=2 means trying to match surname.
"""
def ed_names(sample_name, k):

    fp= open("Athlete.txt")
    res= []

    list_lines= fp.readlines()

    for line in list_lines:
        name= []
        name= line.split(",")
        szTmp= name[k]
        szTmp= szTmp[1:len(szTmp)-1]
        if(edit_distance(sample_name, szTmp)< 3):
             print(sample_name + " and "+ szTmp+ " : %d" % edit_distance(sample_name, szTmp))
             res.append(szTmp)
    
    fp.close()
    print("%d matched!" % len(res))
    return res

"""
get a list of strings that with ED <2 using a group of strings
"""
def ed_group(k):
    print("")
    fp= open("Athlete.txt")
    output = open('results.txt', 'w')
    list_lines= fp.readlines()
    res={}

    c= 0
    for line in list_lines:
        c+=1
        if c<5:
            name= []
            name= line.split(",")
            szTmp= name[k]
            szTmp= szTmp[1:len(szTmp)-1]
            res[szTmp]= ed_names(szTmp, k)

    
    for key in res.keys():
        output.write(key+"\r\n")
        output.write(", ".join(res[key]))
        output.write("\r\n")

    fp.close()
    output.close()
    return res

