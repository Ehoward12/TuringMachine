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
public class CharacterVector {
    
    private int noOfItems;
    private char[] data;
    private int growBy;

    // Default Constructor
    public CharacterVector() {
        noOfItems = 0;
        growBy = 10;
        data = new char[5];
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


    public char GetItemByIndex(int index) {
        char item;
        item = data[index];
        if (index >= 0 && index < noOfItems) {
            return item;
        } else {
            return '`';
        }
    }


    public void AddItem(char character) {
        if (character != '`')
        {
            //Grows array if need be
            if (noOfItems == data.length) 
                growArray();
            data[noOfItems] = character;
            noOfItems ++;
        }
    }

    private void growArray() {
        char tmp[] = new char[data.length + growBy];
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
            data[noOfItems] = '`';
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