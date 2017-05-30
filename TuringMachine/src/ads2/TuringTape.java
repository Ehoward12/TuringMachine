package ads2;

public class TuringTape implements TuringTapeInterface
{
  DoublyLinkedList list = new DoublyLinkedList();
  int currentPlacement;
  int defaultPlacement;
  char blankSymbol;
    
  public TuringTape(char blank, String initconfig)
  {
      for (int i = 0; i < 30; i++)
      {
          list.AddItem(blank);
      }
      currentPlacement = 15;
      defaultPlacement = 15;
      blankSymbol = blank;
  }
  
  public char GetCurrentSymbol()
  {
    return list.GetItemByIndex(currentPlacement); // stub return - needs implementing
  }
  
  public char GetBlankSymbol()
  {
    return blankSymbol;
  }
  
  public char GetSymbol(int offset)
  {
    return list.GetItemByIndex(currentPlacement + offset);
  }
  
  public void WriteCurrentSymbol(char symbol)
  {
      list.DeleteItem(currentPlacement);
      list.InsertItem(currentPlacement, symbol);
  }

  public void MoveHeadLeft()
  {
      currentPlacement --;
      if (currentPlacement < 10)
      {
        for (int i = 0; i < 10; i++)
        {
            list.InsertItem(0, blankSymbol);
        }
      }
  }
  
  public void MoveHeadRight()
  {
      currentPlacement ++;
      if (currentPlacement > list.GetNoOfItems() - 10)
      {
        for (int i = 0; i < 10; i++)
        {
            list.AddItem(blankSymbol);
        }
        defaultPlacement += 10;
      }
  }
  
  public int GetLengthOfStoredTape()
  {
      return list.GetNoOfItems();
  }
  
  public void ChangeHead(int head)
  {
      currentPlacement = head;
  }
  
  public void ResetPlacement()
  {
      currentPlacement = defaultPlacement;
  }

}