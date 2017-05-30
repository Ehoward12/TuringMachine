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
public class TransitionsVector {
    
    private int noOfItems;
    private TransitionInformation[] data;
    private int growBy;
    private String[] tempArray;
    private String stateName;

    // Default Constructor
    public TransitionsVector(String stateNameIn) {
        stateName = stateNameIn;
        noOfItems = 0;
        growBy = 10;
        data = new TransitionInformation[5];
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


    public TransitionInformation GetItemByIndex(int index) {
        TransitionInformation item;
        item = data[index];
        if (index >= 0 && index < noOfItems) {
            return item;
        } else {
            return null;
        }
    }


    public void AddItem(String name, char initialState, char finalState, char moveDirection, String nextTransition) {
        if (name != "")
        {
            //Grows array if need be
            if (noOfItems == data.length) 
                growArray();
            TransitionInformation temp = new TransitionInformation(initialState, finalState, moveDirection, nextTransition);
            data[noOfItems] = temp;
            noOfItems ++;
        }
    }

    private void growArray() {
        TransitionInformation tmp[] = new TransitionInformation[data.length + growBy];
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
    
    public String getStateName()
    {
        return stateName;
    }
}
