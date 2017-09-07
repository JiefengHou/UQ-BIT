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
    """takes a strings representing the item ID and name of the item"""
    
    def __init__(self,itemID,name):
        """Constructor: Item(str,str)
        """
        self._itemID=itemID
        self._name=name
        self._depend=None

    def get_name(self):
        """returns the name of the item
        get_name()->str
        """
        return self._name

    def get_ID(self):
        """returns the ID of the item
        get_ID()->str
        """
        return self._itemID

    def set_name(self,name):
        """updates the name of the item
        set_name(name)->None
        """
        self._name=name

    def get_depend(self):
        """returns the empty list
        get_depend()->list
        """
        return []

class Part(Item):
    """takes a strings representing the item ID and name of the item as well as the cost of the part"""
    
    def __init__(self,itemID,name,cost):
        """Constructor: Part(str,str,int)
        """        
        Item.__init__(self,itemID,name)
        self._cost=cost

    def get_cost(self):
        """returns the cost of the item
        get_cost()->int
        """        
        return self._cost

    def set_cost(self,cost):
        """updates the cost of the item
        set_cost(int)->None
        """  
        self._cost=cost

    def __repr__(self):
        """return a string representation of the item(used to write the item data to a file)
        """  
        return '{0},{1},{2}'.format(self._itemID,self._name,self._cost)
    

    def __str__(self):
        """return a more detailed representation of the item(used to write the item data to the listbox)
        """  
        return PART_FORMAT.format(self._itemID,self._name,self._cost)
        

class Compound(Item):
    """takes a strings representing the item ID and name of the item, the products and a list of 
       pairs of IDs and numbers representing the components of the compound item
    """
    def __init__(self,itemID,name,products,itemlist):
        """Constructor: Compound(str,str,str,list((str,int)))
        """            
        Item.__init__(self,itemID,name)
        self._products=products
        self._itemlist=itemlist
        
    def get_cost(self):
        """returns the cost of the item
        get_cost()->int
        """
        self.get_items_list()
        cost=0
        if self._itemlist is not None:
            for ID in self._itemlist:
                for key in self._products.get_keys():
                    if ID[0]==key:
                        cost +=int(ID[1])*int(self._products.get_item(ID[0]).get_cost())
        self._cost=cost  
        return self._cost

    def get_items_list(self):
        """returns the items list
        get_items_list()->list((str,int))
        """
        if self._itemlist==None:
            return 'None'
        if ':' in self._itemlist:
            self._itemlist=get_components(self._itemlist.split(','))
            return self._itemlist
        else:
            return self._itemlist
        

    def get_items_str(self):
        """returns a string representation items list
        get_items_str()->str
        """   
        if self._itemlist==None:
            return 'None'
        if ':' not in self._itemlist:
            self._itemlist=items_string(self._itemlist)
            return self._itemlist
        else:
            return self._itemlist


    def set_items(self,itemlist):
        """updates the items list
        set_items(list((str,int))->None
        """  
        if ':' in itemlist:
            itemlist=itemlist.split(',')
            self._itemlist=get_components(itemlist)
        else:
            self._itemlist=itemlist

    def get_depend(self):
        """overrides the method in the super class and returns the list of all the item IDs in the items list
        get_depend()->list(str)
        """ 
        self._depend=[]
        self.set_items(self._itemlist)
        for c in self._itemlist:
            self._depend.append(c[0])            
        return self._depend

    def __repr__(self):
        """return a string representation of the item(used to write the item data to a file)
        """  
        return '{0},{1},{2}'.format(self._itemID,self._name,self.get_items_str())

    def __str__(self):
        """return a more detailed representation of the item (used to write the item data to the listbox)
        """  
        return COMPOUND_FORMAT.format(self._itemID,self._name,self.get_cost(),self.get_items_str())
        

class Products():
    """This is the model for the program and is used to keep track of all the items
       using a dictionary whose keys are item IDs and whose values are item objects.
    """
    def __init__(self,dic=None):
        """Constructor: Products()
        """          
        if dic is None:
            dic={}
        self._dic=dic

    def load_items(self, filename):
        """loads the items from a file
        load_items(str)->None
        """          
        self.delete_all()
        load_items_from_file(self, filename)
        

    def save_items(self, filename):
        """saves the items to a file
        save_items(str)->None
        """ 
        save_items_to_file(self, filename)
        

    def get_item(self,itemID):
        """returns the item for a given item ID
        get_item(str)->list(str)
        """        
        for c in self._dic.keys():
            if c==itemID:
                return self._dic[c]
        
    def add_item(self,itemID,value):
        """adds a new item to the dictionary
        add_item(str,Parts)->None
        """  
        self._itemID=itemID
        self._value=value
        self._dic[itemID]=value

    def get_keys(self):
        """returns all the keys in the dictionary in sorted order
        get_keys()->list(str)
        """  
        a=self._dic.keys()
        a.sort()
        return a

    def remove_item(self,itemID):
        """removes a given item from the dictionary
        remove_item(str)->None
        """         
        for c in self._dic.keys():
            if c==itemID:
                del self._dic[c]

    def delete_all(self):
        """resets the dictionary to be empty
        delete_all()->None
        """
        self._dic.clear()

    def check_depend(self,itemID):
        """checks if any item in the dictionary depends on the item with the given ID
        check_depend(str)->bool
        """
        for key in self.get_keys():
            a=self.get_item(key).get_depend()
            if itemID in a:
                return True
        return False



class View(Listbox):
    """This class provides the view of the item information list and should inherit form the listbox class
    """    
    def __init__(self,master,font=('Courier',10)):
        """Constructor: View(Listbox)
        """ 
        Listbox.__init__(self,master,font=('Courier',10))

    def update(self, products):
        """delete all the items in the view, and then (re)display them
        update(list(str))->None
        """        
        self.delete(0,END)
        for ID in products.get_keys():
            self.insert(END,str(products.get_item(ID)))
            

class Controller(object):
    """This class is responsible for creating all the GUI components and interacting with the user
    """  
    def __init__(self,master):
        """Constructor:Controller(object)
        """
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
        self._ID=''
     
        

        """three menu items in file menu 
        """
        menubar = Menu(self._master)
        master.config(menu=menubar)
        
        filemenu = Menu(menubar)
        menubar.add_cascade(label="File", menu=filemenu)
        filemenu.add_command(label="Open Products File", command=self.open_file)
        filemenu.add_command(label="Save Products File", command=self.save_file)
        filemenu.add_command(label="Exit", command=self.close)
        

        """Listbox - display the required item information in alphabetic order on item IDs
        """
        self._Listbox=View(self._frame1)
        self._Listbox.pack(side=TOP, expand=True, fill=BOTH, pady=20)

        """Entry Widget - all other interactions require data to be entered/modified.
        """
        
        """Label"""
        label=Label(self._frame2, textvariable=self._var)
        label.pack(side=LEFT,padx=10, pady=5)
        
        """Entry widget"""
        self._entry=Entry(self._frame2,width=80)
        self._entry.pack(side=LEFT,padx=10, pady=5)
        
        """OK button"""
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
        """open a le containing items information using the askopenfilename widget
        """
        self._filename = tkFileDialog.askopenfilename()
        if self._filename:
            self._products.load_items(self._filename)
            self._Listbox.delete(0,END)
            for key in self._products.get_keys():
                self._Listbox.insert(END,str(self._products.get_item(key)))

    def save_file(self):
        """save all the current items information using the asksaveasfilename widget
        """
        self._filename = tkFileDialog.asksaveasfilename()
        if self._filename:
            self._products.save_items(self._filename)


    def close(self):
        """exit the program
        """
        self._master.destroy()


    """OK Button"""
    def ok(self):
        """Add Part Button
        """
        if self._command=='add_part':
            if self._entry.get() =='':
                tkMessageBox.showerror("Add Part Error", "ID cannot be empty")
            else:
                if self._entry.get() in self._products.get_keys():
                    tkMessageBox.showerror("Add Part Error", "ID already exists")
                else:
                    self._products.add_item(self._entry.get(),Part(self._entry.get(),'No Name',0))

                
            
        """Add Compound Button
        """
        if self._command=='add_compound':
            if self._entry.get() =='':
                tkMessageBox.showerror("Add Compound Error", "ID cannot be empty")
            else:
                if self._entry.get() in self._products.get_keys():
                    tkMessageBox.showerror("Add Compound Error", "ID already exists")
                else:
                    self._products.add_item(self._entry.get(),Compound(self._entry.get(),'No Name',self._products,None))

                
            
        """Update Name Button
        """
        if self._command=='update_name':
            if self.select_item().isdigit()==False:
                tkMessageBox.showerror("Selection Error", "No item selected")
            else:
                self._ID=self._products.get_keys()[int(self._selection)]
                self._products.get_item(self._ID).set_name(self._entry.get())


            
        """Update Cost Button
        """
        if self._command=='update_cost':
            if self.select_item().isdigit()==False:
                tkMessageBox.showerror("Selection Error", "No item selected")
            else:
                if self._entry.get().isdigit()==False:
                    tkMessageBox.showerror("Update cost", "Cost must be a number")
                else:
                    """Test you select a Compound or not"""
                    if isinstance(self._products.get_item(self._ID), Compound):
                        tkMessageBox.showerror("Part Error", "This item is not a part")
                    else:
                        self._ID=self._products.get_keys()[int(self._selection)]
                        self._products.get_item(self._ID).set_cost(self._entry.get())



        """Update Item Button
        """
        """Test itemlist which you enter is valid or not """
        if self._command=='update_item':
            try:
                itemlist=self._entry.get()
                get_components(itemlist.split(','))
            except:
                tkMessageBox.showerror("Compound Item", "Invalid item list")
                
            else:
                """Test you select a Part or not"""
                if isinstance(self._products.get_item(self._ID), Part):
                    tkMessageBox.showerror("Commpound Error", "This item is not a compound")
                else:
                    """Test the itemID in itemlist which you enter exist in PartID"""
                    partID=[]
                    a=0
                    for key in self._products.get_keys():
                        if isinstance(self._products.get_item(key), Part):
                            partID.append(key)
                    for item in get_components(itemlist.split(',')):
                        if item[0] not in partID:
                            tkMessageBox.showerror("Compound Item", "Invalid item list")
                            a=1 
                    if a==0:
                        self._ID=self._products.get_keys()[int(self._selection)]
                        self._products.get_item(self._ID).set_items(self._entry.get())

        self._Listbox.update(self._products)                 
        self._command=''
        self._var.set('')
        self._entry.delete(0,END)


    def add_part(self):
        """The "Add Part" button is used when the user wishes to add a new part
        """        
        self._var.set('Add Part ID:')
        self._command='add_part'
        

    def add_compound(self):
        """The "Add Part" button is used when the user wishes to add a new compound
        """ 
        self._var.set('Add Compound ID:')
        self._command='add_compound'
      

    def update_name(self):
        """The "Update Name" button is used to edit the name of the selected item
        """ 
        self._entry.delete(0,END)
        """Test you select a item from the listbox or not"""
        if self.select_item().isdigit()==False: 
            tkMessageBox.showerror("Selection Error", "No item selected")
        else:
            self._var.set('Update Name:')
            self._ID=self._products.get_keys()[int(self._selection)]
            self._entry.insert(0,self._products.get_item(self._ID).get_name())
        self._command='update_name'
      

    def update_cost(self):
        """The "Update Cost" button is used to edit the cost of the selected part(not used for compound items)
        """ 
        self._entry.delete(0,END)
        """Test you select a item from the listbox or not"""
        if self.select_item().isdigit()==False: 
            tkMessageBox.showerror("Selection Error", "No item selected")
        else:
            self._ID=self._products.get_keys()[int(self._selection)]
            if isinstance(self._products.get_item(self._ID), Compound):
                tkMessageBox.showerror("Part Error", "This item is not a part")
            else:
                self._var.set('Update Cost:')
                self._entry.insert(0,self._products.get_item(self._ID).get_cost())
        self._command='update_cost'
        


    def update_items(self):
        """The "Update Items" button is used to edit the items that make up the the selected compound item
        """ 
        self._entry.delete(0,END)
        """Test you select a item from the listbox or not"""
        if self.select_item().isdigit()==False:   
            tkMessageBox.showerror("Selection Error", "No item selected")   
        else:
            self._ID=self._products.get_keys()[int(self._selection)]
            if isinstance(self._products.get_item(self._ID), Compound):
                self._var.set('Update Compound Items:')
                self._entry.insert(0,self._products.get_item(self._ID).get_items_str())    
            else:
                tkMessageBox.showerror("Commpound Error", "This item is not a compound")
                self._var.set('')
                self._entry.delete(0,END)
        self._command='update_item'
        

    def remove_item(self):
        """The "Remove Item" button removes the selected item
        """ 
        self._var.set('')
        self._entry.delete(0,END)
        """Test you select a item from the listbox or not"""
        if self.select_item().isdigit()==False:   
            tkMessageBox.showerror("Selection Error", "No item selected")
        else:
            self._ID=self._products.get_keys()[int(self._selection)]
            if self._products.check_depend(self._ID)==1:
                tkMessageBox.showerror("Remove Error", "At least one compound item refers to this item")
            else:
                self._products.remove_item(self._ID)
                self._Listbox.update(self._products)
                self._selection=''
                       
                        

    def select_item(self):
        """select one item which you want to edit
        """         
        try:
            self._Listbox.curselection()[0]
        except:
            self._selection=''
            return self._selection
        else:
            self._selection=self._Listbox.curselection()[0]
            return self._selection
        
       
        

                        


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

    
                    
                    
                
                
                
            

        
