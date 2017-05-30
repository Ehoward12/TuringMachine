package ads2;

import java.awt.Graphics2D;

/**
 * This interface defines the core Turing Machine behaviour
 * <p>
 * DO NOT EDIT THIS FILE
 */

public interface TuringMachineInterface
{
  /**
   * Constant used to indicate that the tape head should move left
   */
  public static char MOVE_HEAD_LEFT='L';

  /**
   * Constant used to indicate that the tape head should move right
   */
  public static char MOVE_HEAD_RIGHT='R';

  /**
   * Constant used to indicate that the tape head should not move
   */
  public static char MOVE_HEAD_NONE='N';
  
  /**
   * Loads a Turing Machine configuration in from a file on the disk
   * <p>
   * @param  filename a filename for a given tape confirmation to load
   * @return          true if the configuration loaded successfully, else false
   */
  public boolean Load(String filename);
  
  /**
   * Resets the Turing Machine back to its original configuration as if it were just loaded
   */
  public void Restart();

  /**
   * Executes the next instruction based on the machine and tape configuration
   * <p>
   * As the machine executes its next instruction, the tape and machine confirmation
   * should be updated as appropriate.
   * @return    One of the MOVE_HEAD_? constants to indicate how the machine head moved 
   *            while executing the instruction.  E.g. if the head moved left then the
   *            MOVE_HEAD_LEFT constant should be returned
   */
  public char ExecuteNextStep();
  
  /**
   * Returns the name of the current state
   * @return    The name of the current state of the Turing Machine
   */
  public String GetCurrentStateName();

  /**
   * Returns the tape being used by the Turing Machine
   * @return    The tape being used by the Turing Machine in the current configuration
   */
  public TuringTapeInterface GetTape();
  
  /**
   * Determines if the current state of the Turing Machine is a halt state
   * @return    true if the current state is a member of the halting states, else false
   */
  public boolean IsCurrentStateHalt();
  
  /**
   * Renders the current state to the graphics context / canvas.
   * <p>
   * This method is part of the extra marks and is used to display the graphical table
   * in the example demonstrated in the video
   * @param graphics  the graphics context to draw the current state to
   * @param width     the width of the context / canvas
   * @param height    the height of the drawing context / canvas
   */
  public void DrawCurrentState(Graphics2D graphics, int width, int height);
}