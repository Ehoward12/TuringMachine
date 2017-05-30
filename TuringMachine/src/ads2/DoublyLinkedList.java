
package ads2;

public class DoublyLinkedList
{
  //Creates the root node which will always be at the start of the list
  private DLLNode root;
  //Creates the end node which will always be at the end of the list
  private DLLNode end;
  //Creates a temporary node which will be used when adding new items
  private DLLNode tempNode;
  //Creates a dummy node which will always be at the end of the list and which will
  //point back to the previous node
  private DLLNode dummyEndNode;
  
  //Default constructor
  public DoublyLinkedList()
  {
      root = null;
      end = null;
      dummyEndNode = null;
  }

  /* Return the number of items contained within this data structure.
   * Use recursion to count the number of nodes in the linked list */
  public int GetNoOfItems()
  {
    return (root==null?0:root.GetNoOfItems());
  }

  /* Returns the String value held at index (base zero) or null if the index
   * is out of bounds */
  public char GetItemByIndex(int index)
  {
    return (index>=0 && root != null?root.GetItemByIndex(index):null);
  }
  
  /* Returns the doubly-linked list node at index (base zero) or null if the index
   * is out of bounds */
  public DLLNode GetNodeByIndex(int index)
  {
    if (index > GetNoOfItems() - 1)
    {
        return null;
    }
    else
    {
        return root.GetNodeByIndex(index);
    }
  }
  
  /* Adds value to the end of the data structure */
  public void AddItem(char value)
  {
    //Checks to see if root is null, if it is makes a new start node
    if (root==null)
    {
        root=new DLLNode(value);
        //Sets the dummy node after the root node
        dummyEndNode = root.SetDummyNode();
    }
    //Checks to see if root has been made but end hasn't
    else if (end==null)
    {
        //Sets up the end node, giving it a previous value and adding the dummy node after
        end = root.AddItem(value);
        end.SetPrevious(root);
        dummyEndNode = end.SetDummyNode();
    }
    //Calls this if root and end have already been set
    else
    {
        //Makes the temp equal to end to save it temporarily
        tempNode = end;
        //Sets the new end node as the item being added
        end = end.AddItem(value);  
        //Sets the previous for this node
        end.SetPrevious(tempNode);
        //Puts the dummy node after this node
        dummyEndNode = end.SetDummyNode();
        //Resets the tempNode
        tempNode = null;
    }
    
  }
  
  /* Inerts value into the data structure at index (base zero) or at the end
   * if there are less items in the data structure than index */
  public void InsertItem(int index, char value)
  {
      //Checks to see if the index is out of range, in which case it just adds a node to the end
      if (index > GetNoOfItems() - 1)
      {
          AddItem(value);
      }
      //Checks to see if index is 0 and if it is then it makes the new node root
      else if (index == 0)
      {
          tempNode = new DLLNode(value, root);
          root.SetPrevious(tempNode);
          root = tempNode;
          
      }
      //Any standard insertion it just calls the function which will insert it
      else
      {
          root.InsertItem(index, value);
      }
  }

  /* Removes the item at index from the data structure - if index is out of
   * bounds then the data structure remains unchanged */
  public void DeleteItem(int index)
  {
      //Checks to see if it is out of range and gives an error if it is
      if (index > GetNoOfItems() - 1 || index < 0)
      {
          System.out.println("Index is out of range");
      }
      //Checks to see if it is deleteing the last item in the list, resets end
      else if (index == GetNoOfItems() - 1 && index != 0)
      {
          end = GetNodeByIndex(index-1);
          root.DeleteItem(index);
          dummyEndNode = end.SetDummyNode();
      }
      //A standard deletion will simply call the delete function
      else if (index > 0)
      {
          root.DeleteItem(index);
      }
      //checks to see if root is being deleted and there's more items in the list
      else if (GetNoOfItems() > 1)
      {
          root = root.GetNextNode();
          root.SetPrevious(null);
      }
      //Calls if root is the only item in the list and its being deleted
      else
      {
          root = null;
          end = null;
      }
  }
  
  /* if you want extra internal information about the state of your linked list when
   * tested, update the following toString method to dump any information you are 
   * interested in - this method is not marked */
//  public String toString(int index)
 // {
 //   if (index == 0)
 //       return "Previous: " + null + " Next: " + (root.GetNextNode().GetDataItem()).toString() + " Data: " + (root.GetDataItem()).toString();
 //   else if (index == GetNoOfItems() - 1)
  //      return "Previous: " + (end.GetPreviousNode().GetDataItem()).toString() + " Next: " + null + " Data: " + (end.GetDataItem()).toString();
  //  else
   //     return root.GetNodeInfo(index);
 // }
}