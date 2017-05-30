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
public class TransitionStateVector {
    
    private int noOfItems;
    private TransitionsVector[] data;
    private int growBy;
    private String[] tempArray;

    // Default Constructor
    public TransitionStateVector() {

        noOfItems = 0;
        growBy = 10;
        data = new TransitionsVector[5];
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


    public TransitionsVector GetItemByIndex(int index) {
        TransitionsVector item;
        item = data[index];
        if (index >= 0 && index < noOfItems) {
            return item;
        } else {
            return null;
        }
    }


    public void AddItem(String name) {
        if (name != "")
        {
            //Grows array if need be
            if (noOfItems == data.length) 
                growArray();
            TransitionsVector temp = new TransitionsVector(name);
            data[noOfItems] = temp;
            noOfItems ++;
        }
    }

    private void growArray() {
        TransitionsVector tmp[] = new TransitionsVector[data.length + growBy];
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
    
    public TransitionsVector GetState(String state)
    {
        for (int i = 0; i < GetNoOfItems(); i++)
        {
            if (data[i].getStateName().equals(state))
            {
                return data[i];
            }
        }
        return null;
    }
    
}
