
package ads2;

public class DLLNode
{
  /* This method returns the String item of data held in this node */
    
  private DLLNode next;
  private DLLNode prev;
  private char data;

  //Constructor for the dummy node
  public DLLNode(DLLNode prev)
  {
      this.data = 'Δ';
      this.next = null;
      this.prev = prev;
  }
  
  //Constructor for any new root node
  public DLLNode(char value)
  {
    this.data = value;
    this.next = null;
    this.prev = null;
  }
  
  //Constructor for a node when next is known
  public DLLNode(char value, DLLNode next)
  {
      this.data = value;
      this.next = next;
      this.prev = null;
  }
  
  //Constructor for a node when next and previous is known
  public DLLNode(char value, DLLNode next, DLLNode prev)
  {
      this.data = value;
      this.next = next;
      this.prev = prev;
  }
  
  //Returns data
  public char GetDataItem()
  {
    return data;
  }
  
  /* This method returns the node immediately prior to this node in the list or
   * null if there is no such node */
  public DLLNode GetPreviousNode()
  {
    return prev;
  }
  
  /* This method returns the node immediately after this node in the list or null
   * if there is no such node. */
  public DLLNode GetNextNode()
  {
    return next;
  }
  
  //Returns the number of items, 1 if next.data is equal to 0
  public int GetNoOfItems()
  {
    return(next==null?1:1+next.GetNoOfItems());  
  }
  
  //REcursive routine to find an item by its index
  public char GetItemByIndex(int index)
  {
    if (index==0)
    {
        return data;
    }
    //Calls itself if the next node is not the dummy node
    else if (next != null)
    {
        return next.GetItemByIndex(index - 1);
    }
    else
    {
        return 'Δ';
    }
  }
  
  //Makes the next of the end node equal to a new node, essentially adds an item to the end
  public DLLNode AddItem(char value)
  {
    next = new DLLNode(value);
    return next;
  }
  
  //Sets the previous of a node when it needs to
  public void SetPrevious(DLLNode node)
  {
    prev = node;  
  }
  
  //REcursive routine to add an item at requested index
  public void InsertItem(int index, char value)
  {
      if (index == 1)
      {
          DLLNode Temp = new DLLNode(value, next, this);
          next.prev = Temp;
          next = Temp;
      }
      else
      {
          next.InsertItem(index-1, value);
      }
  }
  
  //Recursive routine to delete an item
  public void DeleteItem(int index)
  {
      if (index == 1)
      {
          //Resets the previous of the node a couple from the current node to the current node
          (next.next).prev = this;
          //Sets the next equal to the node a couple from the current node
          next = next.next;          
      }
      else
      {
          next.DeleteItem(index - 1);
      }
  }
  
  //Recursive routine to find the node being requested
  public DLLNode GetNodeByIndex(int index)
  {
      if (index == 0)
      {
        return this;        
      }
      else
      {
        return next.GetNodeByIndex(index - 1);
      }
  }
  
  //Routine to set the dummy node, essentially creates an empty node with a previous value as the end node
  public DLLNode SetDummyNode()
  {
      next = new DLLNode(this);
      return next;
  }
  
  //Used to return back information about a node
  //public String GetNodeInfo(int index)
  //{
   //   if (index == 0)
    //  {
    //    String val = "Previous: " + (GetPreviousNode().data) + " Next: " + (GetNextNode().data).toString() + " Data: " + (GetDataItem()).toString();
   //     return val;        
   //   }
   //   else
    //  {
    //    return next.GetNodeInfo(index - 1);
    //  }    
  //}
         
}