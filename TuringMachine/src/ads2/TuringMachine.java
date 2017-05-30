package ads2;

import java.awt.*;
import java.io.*;

public class TuringMachine implements TuringMachineInterface
{
  // Useful information...
  // '\u0394' is the unicode character code of the delta symbol
  // e.g. char blank='\u0394';

  private TuringTapeInterface tape;
  private TransitionStateVector transitions = new TransitionStateVector();
  private CharacterVector symbolsVec = new CharacterVector();
  private CharacterVector tapeVec = new CharacterVector();
  private StringVector haltVec = new StringVector();
  private String startState, tapeConfiguration;
  private String currentState;
  private char blankSymbol;
  
  // other class variables go here
  
  public boolean Load(String filename)
  {
    // This is a two pass loader.
    try
    {
      // Pass 1: parse the file and look for all the state names as listed in the transition section of the file
      // This is used to extract all state names so they can be used in the second pass
      ExtractStateNames(filename);

      // Pass 2: At this point, you should know about all state names in the machine's configuration so let's
      // parse the file again and process all information
      BufferedReader in=new BufferedReader(new InputStreamReader(new FileInputStream(filename), "Unicode"));
      String line=in.readLine();  // reads in a single line at a time and processes it
      while (line!=null)
      {
        int index=line.indexOf('=');  // get the character index of the equals sign
        if (index>0)
        {
          String lhs=line.substring(0, index).toLowerCase();  // get the left-hand side of keyword of the line
          index=line.indexOf('{', index);
          int last=line.lastIndexOf('}'); // gets the terminating characters for the right-hand side (i.e. key=value)
          if (index>0 && last>0)
          {
            String rhs=line.substring(index+1, last);
            if (lhs.equals("symbols"))  // this line relates to all the symbols the machine can use
              ParseSymbols(rhs);
            else if (lhs.equals("blank")) // this is the default blank symbol
              ParseBlank(rhs.charAt(0));
            else if (lhs.equals("start")) // this is the start state of the machine
              ParseStartState(rhs.trim());
            else if (lhs.equals("halts")) // this is the list of halt states
              ParseHaltStates(rhs);
            else if (lhs.equals("tape"))    // this is the initial tape configuration
              ParseTapeConfiguration(rhs.trim());
            else  // these are state transition tables
            {
              if (line.length()>0 && line.charAt(0)=='[')
              {
                index=line.indexOf(']');
                if (index>0)
                {
                  String name=line.substring(1, index).trim();
                  UpdateStateInfo(name, rhs);
                }
              }
            }
          }
        }
        line=in.readLine(); // read the next line and parse that one
      }      
      
      in.close();
      return true;
    }
    catch (IOException e) 
    { 
      System.out.println("TuringMachine.Load(String filename): "+e);
      return false;
    }
  }
  
  // This method performs the first pass of the file to extract all state names
  private void ExtractStateNames(String filename) throws IOException
  {
    BufferedReader in=new BufferedReader(new InputStreamReader(new FileInputStream(filename), "Unicode"));
    String line=in.readLine();
    int transitionVecNo = 0;
    while (line!=null)
    {
      if (line.length()>0 && line.charAt(0)=='[')
      {
        int index=line.indexOf(']');
        //start of expression
        int SOE=line.indexOf('{');
        char initialState = '\u0394';
        char finalState = '\u0394';
        char moveDirection = 'N';
        String nextTransition = "";
        //in expression
        boolean IE = false;
        //part of expression
        int POE = 0;
        String statename=line.substring(1, index).trim();
        transitions.AddItem(statename);
        for (int i = SOE + 1; i < line.length(); i++)
        {
            char currentChar = line.charAt(i);
            if (currentChar == '{')
            {
                IE = true;
                POE = 0;
            }
            else if (currentChar != ',' && currentChar != ' ' && currentChar != '}' && IE == true)
            {
                if (POE == 0)
                {
                    initialState = line.charAt(i);
                }
                else if (POE == 1)
                {
                    finalState = line.charAt(i);
                }
                else if (POE == 2)
                {
                    moveDirection = line.charAt(i);
                }
                else if (POE > 2)
                {                    
                    nextTransition += line.substring(i, i+1);
                }
                
                POE ++;
            }
            else if (currentChar == '}' && IE == true)
            {
                POE = 0;
                IE = false;
                transitions.GetItemByIndex(transitionVecNo).AddItem(statename, initialState, finalState, moveDirection, nextTransition);
                nextTransition = "";
            }
        }
        transitionVecNo++;
      }
      
      line=in.readLine();
    }
    in.close();
  }
  
  private void ParseSymbols(String values)
  {
    for (int i = 0; i < values.length(); i++)
    {
        if (values.charAt(i) != ',' && values.charAt(i) != ' ')
        {
            symbolsVec.AddItem(values.charAt(i));
        }
    }
  }
  
  private void ParseBlank(char blank)
  {
    blankSymbol = blank;
  }
  
  private void ParseStartState(String state)
  {
    startState = state;
    currentState = startState;
  }

  private void ParseHaltStates(String values)
  {
    String tmp = "";
    for (int i = 0; i < values.length(); i++)
    {
        if (values.charAt(i) != ',' && values.charAt(i) != ' ')
        {
            for (int j = i; j < values.length(); j++)
            {
                if (j == values.length() - 1)
                {
                    i = j;
                }
                if (values.charAt(j) != ',' && values.charAt(j) != ' ')
                {
                    tmp += values.charAt(j);
                }
                else
                {
                    i = j;
                    j = values.length();
                }
            }
            haltVec.AddItem(tmp);
            tmp = "";
        }      
    }
    // This method should make a note of the halt state, checking that each is valid
  }
  
  private void ParseTapeConfiguration(String tapeconfig)
  {
    tapeConfiguration = tapeconfig;
    tape = new TuringTape(blankSymbol, "h8");
    tapeVec = new CharacterVector();
    for (int i = 0; i < tapeconfig.length(); i++)
    {
        if (tapeconfig.charAt(i) != ',' && tapeconfig.charAt(i) != ' ')
        {
            tapeVec.AddItem(tapeconfig.charAt(i));
        }
    }
    for (int i = 0; i < tapeVec.GetNoOfItems(); i++)
    {
        tape.WriteCurrentSymbol(tapeVec.GetItemByIndex(i));
        tape.MoveHeadRight();
    }
    for (int i = 0; i < tapeVec.GetNoOfItems(); i++)
    {
        tape.MoveHeadLeft();
    }
  }
  
  private void UpdateStateInfo(String name, String info)
  {
    // This method should record the state information against the state name
    // Input and output tape symbols need to be validate
    // Head movement needs to be validate ('L', 'R', or 'N')
    // Transition to state name needs to be validated
    boolean validated1 = false;
    boolean validated2 = false;
    boolean validated3 = false;
    for (int i = 0; i < transitions.GetNoOfItems(); i++)
    {
        for (int l = 0; l < transitions.GetItemByIndex(i).GetNoOfItems(); l++)
        {
            for (int j = 0; j < symbolsVec.GetNoOfItems(); j++)
            {
                if (transitions.GetItemByIndex(i).GetItemByIndex(l).GetFinalState() == symbolsVec.GetItemByIndex(j))
                {
                    validated1 = true;
                }
                if (transitions.GetItemByIndex(i).GetItemByIndex(l).GetInitialState() == symbolsVec.GetItemByIndex(j))
                {
                    validated2 = true;
                }
                if (validated1 == true && validated2 == true)
                {
                    validated3 = true;
                }
            }
            if (!validated3)
            {
                System.out.println("Problem while validating: symbol found not included in list of valid symbols");
                System.exit(0);
            }
            else
            {
                validated1 = false;
                validated2 = false;
                validated3 = false;
            }
            if (!IsValidMove(transitions.GetItemByIndex(i).GetItemByIndex(l).GetMove()))
            {
                System.out.println("Problem while validating: move found which is not valid");
                System.exit(0);
            }
            if (!IsValidStateName(transitions.GetItemByIndex(i).GetItemByIndex(l).GetNextTransition()))
            {
                System.out.println("Problem while validating: state transition found is not valid");
                System.exit(0);
            }
        }
    }
  }

  // Helper method to validate that the move character is valid - you could use this in UpdateStateInfo
  private boolean IsValidMove(char move)
  {
    move&=0xFFDF; // convert to upper case
    return (move==MOVE_HEAD_LEFT || move==MOVE_HEAD_RIGHT || move==MOVE_HEAD_NONE);
  }
  
  public boolean IsValidStateName(String state)
  {
      for (int i = 0; i < transitions.GetNoOfItems(); i++)
      {
          if (state.equals(transitions.GetItemByIndex(i).getStateName()))
          {
              return true;
          }
      }
      return false;
  }
  
  public String GetCurrentStateName()
  {
    return currentState;    // this is a stub method and needs updating with your code
  }

  
  public void Restart()
  {
    ParseTapeConfiguration(tapeConfiguration);   
    currentState = startState;
  }
  
  public char ExecuteNextStep()
  {
    TransitionsVector tmp = transitions.GetState(currentState);
    char currentSymbol;
    char futureSymbol;
    for (int i = 0; i < tmp.GetNoOfItems(); i++)
    {
        currentSymbol = tape.GetCurrentSymbol();
        if (tmp.GetItemByIndex(i).GetInitialState() == currentSymbol)
        {
            futureSymbol = tmp.GetItemByIndex(i).GetFinalState();
            tape.WriteCurrentSymbol(futureSymbol);
            currentState = tmp.GetItemByIndex(i).GetNextTransition();
            if (tmp.GetItemByIndex(i).GetMove() == 'R')
            {
                tape.MoveHeadRight();
                return MOVE_HEAD_RIGHT;    // this is a stub method and needs updating with your code
            }
            else if (tmp.GetItemByIndex(i).GetMove() == 'L')
            {
                tape.MoveHeadLeft();
                return MOVE_HEAD_LEFT;
            }
            else
                return MOVE_HEAD_NONE;
        }
    }
    return 0;
    
  }

  // I'll give you this method
  public TuringTapeInterface GetTape()
  {
    return tape;
  }
  
  public boolean IsCurrentStateHalt() 
  { 
    for (int i = 0; i < haltVec.GetNoOfItems(); i++)
    {
        if (currentState.equals(haltVec.GetItemByIndex(i)))
        {
            return true; // this is a stub method and needs updating with your code
        }
    }
    return false;
  }
  
  public void MakeNewTape()
  {
      
  }
  
  public void DrawCurrentState(Graphics2D graphics, int width, int height)
  {
    // Some basic drawing code
    // Gets size information about the current font 
    FontMetrics metrics=graphics.getFontMetrics();

    // Fill the area white
    graphics.setColor(Color.WHITE);
    graphics.fillRect(0, 0, width, height);
    
    graphics.setColor(Color.red);
    // Draw outline of the canvas
    graphics.drawRect(0, 0, width, height);
    
    // Draw some text to the screen
    graphics.drawString("STATE: " + transitions.GetState(currentState).getStateName(), 0, 30);
    graphics.drawLine(0, 35, width, 35);
    graphics.drawString("Read", 0, 70);
    graphics.drawString("Write", 120, 70);
    graphics.drawString("Head", 240, 70);
    graphics.drawString("State", 360, 70);
    graphics.drawLine(0, 75, width, 75);
    graphics.drawLine(100, 35, 100, height);
    graphics.drawLine(220, 35, 220, height);
    graphics.drawLine(340, 35, 340, height);
    for (int i = 0; i < transitions.GetState(currentState).GetNoOfItems(); i++)
    {
        graphics.drawString(transitions.GetState(currentState).GetItemByIndex(i).GetInitialState() + "", 0, 75+(40*(i+1)));
        graphics.drawString(transitions.GetState(currentState).GetItemByIndex(i).GetFinalState() + "", 120, 75+(40*(i+1)));
        graphics.drawString(transitions.GetState(currentState).GetItemByIndex(i).GetMove()+ "", 240, 75+(40*(i+1)));
        graphics.drawString(transitions.GetState(currentState).GetItemByIndex(i).GetNextTransition() + "", 360, 75+(40*(i+1)));
        graphics.drawLine(0, 75+(40*(i+1)) + 5, width, 75+(40*(i+1)) + 5);
    }
    
  }

}
