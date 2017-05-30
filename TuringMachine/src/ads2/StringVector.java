/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ads2;

/**
 *
 * @author Elliot Howard
 */
public class StringVector {
    
    private int noOfItems;
    private String[] data;
    private int growBy;

    // Default Constructor
    public StringVector() {
        noOfItems = 0;
        growBy = 10;
        data = new String[5];
    }


    public void SetGrowBy(int growby) {
        this.growBy = growBy;
    }

    public int GetCapacity() {
        return data.length - noOfItems;
    }

    public int GetNoOfItems() {
        return noOfItems;
    }


    public String GetItemByIndex(int index) {
        String item;
        item = data[index];
        if (index >= 0 && index < noOfItems) {
            return item;
        } else {
            return null;
        }
    }


    public int FindItem(String search) {
        // You must use recursive binary search to search your sorted vector data collection 
        if (search != "")
            return BinarySearch(search.toUpperCase(), 0, noOfItems - 1, true);
        else
            return -1;
    }

    //Function for the binary search, used for placing and searching
    private int BinarySearch(String search, int low, int high, boolean flipFlop) {
        
        int middle;
        boolean found = false;
        while(!found)
        {
            middle = ((high + low) / 2);
            if (high < low)
            {
                //This is used so that different things can be returned depending on the function that called them
                if (flipFlop == true)
                    return -1;
                else
                    return low;
            }
            else if ((data[middle].toUpperCase()).equals(search))
            {
                return middle;
            }
            else if ((data[middle]).compareToIgnoreCase(search) > 0)
            {
                high = middle - 1;
            }
            else
            {
                low = middle + 1;
            }
        }
        return -1;
}


    public void AddItem(String value) {
        if (value != "")
        {
            //Grows array if need be
            if (noOfItems == data.length) 
                growArray();
            int place = BinarySearch(value.toUpperCase(), 0, noOfItems - 1, false);  
            //Shifts items along if need be
            for (int i = noOfItems - 1; i >= place; i--)
                data[i + 1] = data[i];
            data[place] = value;
            noOfItems ++;
        }
    }

    private void growArray() {
        String tmp[] = new String[data.length + growBy];
        System.arraycopy(data, 0, tmp, 0, data.length);
        data = tmp;
    }


    public void DeleteItem(int index) {
        if (index >= noOfItems) {
            //System.out.println("This is out of range");
        } else {
            for (int i = index; i != noOfItems - 1; i++) {
                data[i] = data[i + 1];
            }
            noOfItems--;
            data[noOfItems] = null;
        }
    }


    public String toString() {
        String newString = "";
        for (int i = 0; i < noOfItems; i++) {
            newString += (GetItemByIndex(i) + " ");
        }

        return newString;
    }
}
