###################################################################
#
#   CSSE1001 - Assignment 2
#
#   Student Number: 43034002
#
#   Student Name: Jiefeng Hou(Nick)
#
###################################################################

####################################################################
#
# Do not change the following code
#
####################################################################

from Tkinter import *
import tkMessageBox
import tkFileDialog

# Formatting for use in the __str__ methods
PART_FORMAT = "{0:10}{1:30}{2:>10}"
COMPOUND_FORMAT = "{0:10}{1:30}{2:>10}  {3}"

# Note: some of the supplied definitions below rely on the classes
# you need to write.

def load_items_from_file(products, filename):
    """Add the items in the supplied file to the products object.

    load_items_from_file(Products, str) -> None

    Precondition: Assumes the supplied is of the correct format
    """
    fid = open(filename, 'U')
    for line in fid:
        item_info = line.split(',')
        if len(item_info) > 2:     # ignores blank lines
            item_id = item_info[0].strip()
            item_name = item_info[1].strip()
            if ':' in item_info[2]:   # compound 
                items = item_info[2:]
                products.add_item(item_id, 
                                  Compound(item_id, item_name, products, 
                                           get_components(items)))
            else:   # part
                item_cost = int(item_info[2].strip())
                products.add_item(item_id, Part(item_id, item_name, item_cost))
    fid.close()

def get_components(items):
    """Return a list of pairs of IDs and numbers in items.

    get_components(list(str)) -> list((str, int))
    """
    components = []
    for item in items:
        item = item.strip()
        itemid, _, itemnumstr = item.partition(':')
        itemid = itemid.strip()
        itemnumstr = itemnumstr.strip()
        components.append((itemid, int(itemnumstr)))
    return components
 


def save_items_to_file(products, filename):
    """Save the items in products to the given file.

    save_items_to_file(Products, str) -> None
    """
    f = open(filename, 'w')
    keys = products.get_keys()
    for key in keys:
        f.write("{0}\n".format(repr(products.get_item(key))))
    f.close()

def items_string(items_list):
    """Convert a list of Id, number pairs into a string representation.

    items_string(list((str, int))) -> str
    """
    result = []
    for itemid, num in items_list:
        result.append("{0}:{1}".format(itemid, num))
    return ','.join(result)
                                  
     

####################################################################
#
# Insert your code below
#
####################################################################




class Item(object):
    def __init__(self,itemID,name):
        self._itemID=itemID
        self._name=name
        self._depend=None

    def get_name(self):
        return self._name

    def get_ID(self):
        return self._itemID

    def set_name(self,name):
        self._name=name

    def get_depend(self):
        return []

class Part(Item):
    def __init__(self,itemID,name,cost):
        Item.__init__(self,itemID,name)

        self._cost=cost

    def get_cost(self):
        return self._cost

    def set_cost(self,cost):
        self._cost=cost
        
        

    def __repr__(self):
        return '{0},{1},{2}'.format(self._itemID,self._name,self._cost)

    def __str__(self):
        return PART_FORMAT.format(self._itemID,self._name,self._cost)
        

class Compound(Item):
    def __init__(self,itemID,name,products,itemlist):
        Item.__init__(self,itemID,name)
        self._products=products
        self._itemlist=itemlist
        if self._products is not None:
            cost=0
            ecost=0
            itemlist=self._itemlist
            if itemlist is not None:
                for c in itemlist:
                    for key in self._products.get_keys():
                        if c[0]==key:
                            a=self._products.get_item(key).get_cost()
                            ecost=int(c[1])*int(a)
                            cost=cost+ecost
        self._cost=cost  

        

    def get_cost(self):
        return self._cost


    def get_items_list(self):
        return self._itemlist
        

    def get_items_str(self):
        if self._itemlist==None:
            return 'None'
        
        
        if ':' not in self._itemlist:
            self._itemlist=items_string(self._itemlist)
            return self._itemlist

        else:
            return self._itemlist
        
        
        
    def set_items(self,itemlist):
        if ':' in itemlist:
            itemlist=itemlist.split(',')
            self._itemlist=get_components(itemlist)
            if self._products is not None:
                cost=0
                ecost=0
                itemlist=self._itemlist
                if itemlist is not None:
                    for c in itemlist:
                        for key in self._products.get_keys():
                            if c[0]==key:
                                a=self._products.get_item(key).get_cost()
                                ecost=int(c[1])*int(a)
                                cost=cost+ecost
            self._cost=cost
        else:
            self._itemlist=itemlist

 
        
        

    def get_depend(self):
        self._depend=[]
        if self._itemlist is not None:
            for c in self._itemlist:
                self._depend.append(c[0])            
        return self._depend
        
        

    def __repr__(self):
        return '{0},{1},{2}'.format(self._itemID,self._name,self.get_items_str())

    def __str__(self):
        return COMPOUND_FORMAT.format(self._itemID,self._name,self._cost,self.get_items_str())
        

class Products():
    def __init__(self,dic=None):
        
        if dic is None:
            dic={}
        self._dic=dic

    def load_items(self, filename):
        self.delete_all()
        load_items_from_file(self, filename)
        

    def save_items(self, filename):
        save_items_to_file(self, filename)
        

    def get_item(self,itemID):
        for c in self._dic.keys():
            if c==itemID:
                return self._dic[c]
        
    def add_item(self,itemID,value):
        if itemID not  in self._dic.keys():
            self._itemID=itemID
            self._value=value
            self._dic[itemID]=value
        else:
            return 'ID already exists'
        

    def get_keys(self):
        a=self._dic.keys()
        a.sort()
        return a

    def remove_item(self,itemID):
        for c in self._dic.keys():
            if c==itemID:
                del self._dic[c]

    def delete_all(self):
        self._dic.clear()

    def check_depend(self,itemID):
        for key in self.get_keys():
            a=self.get_item(key).get_depend()
            if itemID in a:
                return True
        return False



class View(Listbox):
    def __init__(self,master,font=('Courier',10)):
       Listbox.__init__(self,master,font=('Courier',10))



class Controller(object):
    def __init__(self,master):

        
        self._master = master
        self._master.title("Bikes R Us: Products")

        self._frame1=Frame(self._master)
        self._frame1.pack(side=TOP,expand=True,fill=BOTH)

        self._frame2=Frame(self._master)
        self._frame2.pack(side=BOTTOM,padx=10, pady=5)

        self._frame3=Frame(self._master)
        self._frame3.pack(side=BOTTOM,padx=10, pady=5)

        self._products=Products()
        self._var=StringVar()
        self._command=''


        """Menu"""
        menubar = Menu(self._master)
        master.config(menu=menubar)
        
        filemenu = Menu(menubar)
        menubar.add_cascade(label="File", menu=filemenu)
        filemenu.add_command(label="Open Products File", command=self.open_file)
        filemenu.add_command(label="Save Products File", command=self.save_file)
        filemenu.add_command(label="Exit", command=self.close)
        

        """Listbox"""

        
        self._Listbox=View(self._frame1)
        self._Listbox.pack(side=TOP, expand=True, fill=BOTH, pady=20)


        """Entry Widget"""


        label=Label(self._frame2, textvariable=self._var)
        label.pack(side=LEFT,padx=10, pady=5)

        self._entry=Entry(self._frame2,width=80)
        self._entry.pack(side=LEFT,padx=10, pady=5)

        button=Button(self._frame2, text="OK", command=self.ok)
        button.pack(side=LEFT,padx=10, pady=5)
        
        


        """Buttons"""
    
        
        button1=Button(self._frame3, text="Add Part", command=self.add_part)
        button1.pack(side=LEFT,padx=10, pady=5)

        button2=Button(self._frame3, text="Add Compound", command=self.add_compound)
        button2.pack(side=LEFT,padx=10, pady=5)

        button3=Button(self._frame3, text="Update Name",command=self.update_name)
        button3.pack(side=LEFT,padx=10, pady=5)

        button4=Button(self._frame3, text="Update Cost", command=self.update_cost)
        button4.pack(side=LEFT,padx=10, pady=5)

        button5=Button(self._frame3, text="Update Items", command=self.update_items)
        button5.pack(side=LEFT,padx=10, pady=5)

        button6=Button(self._frame3, text="Remove Item", command=self.remove_item)
        button6.pack(side=LEFT,padx=10, pady=5)


    def open_file(self):
        self._filename = tkFileDialog.askopenfilename()
        if self._filename:
            self._products.load_items(self._filename)
            self._Listbox.delete(0,END)
            for key in self._products.get_keys():
                self._Listbox.insert(END,str(self._products.get_item(key)))
        

            
    def save_file(self):
        self._filename = tkFileDialog.asksaveasfile()
        if self._filename:
            self._products.save_items(self._filename)


    def close(self):
        self._master.destroy()

        
        
    #OK Button
    def ok(self):
        #Add Part Button
        if self._command=='add_part':
            if self._entry.get() =='':
                tkMessageBox.showerror("Add Part", "ID cannot be empty")
            else:
                a=0
                for key in self._products.get_keys():
                    if self._entry.get()==key:
                        tkMessageBox.showerror("Add Part", "ID already exists")
                        a=1
                if a==0:
                    self._products.add_item(self._entry.get(),Part(self._entry.get(),'No Name',0))
                    self._Listbox.insert(END,str(self._products.get_item(self._entry.get())))
                
            
        #Add Compond Button                 
        if self._command=='add_compound':
            if self._entry.get() =='':
                tkMessageBox.showerror("Add Compound", "ID cannot be empty")
            else:
                a=0
                for key in self._products.get_keys():
                    if self._entry.get()==key:
                        tkMessageBox.showerror("Add Compound", "ID already exists")
                        a=1
                if a==0:
                    self._products.add_item(self._entry.get(),Compound(self._entry.get(),'No Name',self._products,None))
                    self._Listbox.insert(END,str(self._products.get_item(self._entry.get())))
                
            
        #Update Name Button   
        if self._command=='update_name':
            self._current=self._Listbox.get(ANCHOR)
            for key in self._products.get_keys():
                if self._current==str(self._products.get_item(key)):
                    self._item=self._products.get_item(key)
                    
            self._item.set_name(self._entry.get())
            self._Listbox.delete(ANCHOR)
            self._Listbox.insert(ANCHOR,str(self._item))

            
        #Update Cost Button
        if self._command=='update_cost':
            if self._entry.get().isdigit()==False:
                tkMessageBox.showerror("Update cost", "Cost must be a number")
            else:
                self._current=self._Listbox.get(ANCHOR)
                for key in self._products.get_keys():
                    if self._current==str(self._products.get_item(key)):
                        if ':' in repr(self._products.get_item(key)).split(',')[2] or repr(self._products.get_item(key)).split(',')[2]=='None':
                            tkMessageBox.showerror("Part Error", "This item is not a part")
                        else:
                            self._item=self._products.get_item(key)
                
                self._item.set_cost(self._entry.get())
                for key in self._products.get_keys():
                    if ':' in repr(self._products.get_item(key)).split(',')[2]:
                        self._products.get_item(key).set_items(self._products.get_item(key).get_items_list())
                self._Listbox.delete(0,END)
                for key in self._products.get_keys():
                    self._Listbox.insert(END,str(self._products.get_item(key)))
                

        #Update Item Button
        if self._command=='update_item':
            try:
                itemlist=self._entry.get()
                get_components(itemlist.split(','))
                
            except:
                tkMessageBox.showerror("Compound Item", "Invalid item list")
                
            else:
                self._current=self._Listbox.get(ANCHOR)
                for key in self._products.get_keys():
                    if self._current==str(self._products.get_item(key)):
                        if ':' in repr(self._products.get_item(key)).split(',')[2] or repr(self._products.get_item(key)).split(',')[2]=='None':
                            self._item=self._products.get_item(key)

                        else:
                            tkMessageBox.showerror("Commpound Error", "This item is not a compound")

                a=[]
                aa=0
                for key in self._products.get_keys():
                    if ':' not in repr(self._products.get_item(key)).split(',')[2]:
                        if repr(self._products.get_item(key)).split(',')[2]!='None':
                            a.append(key)
                        
                    
                for c in get_components(itemlist.split(',')):
                    if c[0] not in a:
                        tkMessageBox.showerror("Compound Item", "Invalid item list")
                        aa=1
                        
                if aa==0:
                    self._item.set_items(self._entry.get())       
                    self._Listbox.delete(ANCHOR)
                    self._Listbox.insert(ANCHOR,str(self._item))


        self._command=''    
        self._var.set('')
        self._entry.delete(0,END)
            


    def add_part(self):
        self._var.set('Add Part ID:')
        self._command='add_part'
     



    def add_compound(self):
        self._var.set('Add Compound ID:')
        self._command='add_compound'
      
        


    def update_name(self):
        self._entry.delete(0,END)
        self._current=self._Listbox.get(ANCHOR)
        if self._current=='':
            tkMessageBox.showerror("Selection Error", "No item selected")
        else:
            self._var.set('Update Name:')
            for key in self._products.get_keys():
                if self._current==str(self._products.get_item(key)):
                    self._item=self._products.get_item(key)
                    self._entry.insert(0,self._item.get_name())
                
        self._command='update_name'
      

    def update_cost(self):
        self._entry.delete(0,END)
        self._current=self._Listbox.get(ANCHOR)
        if self._current=='':
            tkMessageBox.showerror("Selection Error", "No item selected")
        else:
            self._var.set('Update Cost:')            
            for key in self._products.get_keys():
                if self._current==str(self._products.get_item(key)):
                    if ':' in repr(self._products.get_item(key)).split(',')[2] or repr(self._products.get_item(key)).split(',')[2]=='None':
                        tkMessageBox.showerror("Part Error", "This item is not a part")
                        self._var.set('')
                        self._entry.delete(0,END)
                    else:
                        self._item=self._products.get_item(key)
                        self._entry.insert(0,self._item.get_cost())

        self._command='update_cost'
        


    def update_items(self):
        self._entry.delete(0,END)
        self._current=self._Listbox.get(ANCHOR)
        if self._current=='':
            tkMessageBox.showerror("Selection Error", "No item selected")
        else:
            self._var.set('Update Compound Items:')
            for key in self._products.get_keys():
                if self._current==str(self._products.get_item(key)):
                    if ':' in repr(self._products.get_item(key)).split(',')[2] or repr(self._products.get_item(key)).split(',')[2]=='None':
                        self._item=self._products.get_item(key)
                        self._entry.insert(0,self._item.get_items_str())
                    
                    else:
                        tkMessageBox.showerror("Commpound Error", "This item is not a compound")
                        self._var.set('')
                        self._entry.delete(0,END)
                
        self._command='update_item'
        

    def remove_item(self):
        self._current=self._Listbox.get(ANCHOR)
        if self._current=='':
            tkMessageBox.showerror("Selection Error", "No item selected")
        else:
            a=0
            for key in self._products.get_keys():
                if self._current==str(self._products.get_item(key)):
                    for c in self._products.get_keys():
                        if ':' in repr(self._products.get_item(c)).split(',')[2]:
                            itemlist=get_components(repr(self._products.get_item(c)).split(',')[2:])
                            self._products.get_item(c).set_items(itemlist)
                    if self._products.check_depend(key)==1:
                        tkMessageBox.showerror("Remove Error", "At least one compound item refers to this item")
                    else:
                        self._products.remove_item(key)
                        self._Listbox.delete(ANCHOR)

                        


####################################################################
#
# WARNING: Leave the following code at the end of your code
#
# DO NOT CHANGE ANYTHING BELOW
#
####################################################################
                
class StoreApp():
    def __init__(self, master=None):
        master.title("Bikes R Us: Products")
        self.controller = Controller(master)

def main():
    root = Tk()
    app = StoreApp(root)
    root.mainloop()
    
if  __name__ == '__main__':
    main()

    
                    
                    
                
                
                
            

        
