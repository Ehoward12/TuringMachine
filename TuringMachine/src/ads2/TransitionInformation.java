
package ads2;

public class TransitionInformation {
    private char initialState;
    private char finalState;
    private char moveDirection;
    private String nextTransition;
    
    
    public TransitionInformation(char firstStateIn, char finalStateIn, char moveIn, String nextTransitionIn)
    {
        initialState = firstStateIn;
        finalState = finalStateIn;
        moveDirection = moveIn;
        nextTransition = nextTransitionIn;
    }
    
    public char GetInitialState()
    {
        return initialState;
    }
    
    public char GetFinalState()
    {
        return finalState;
    }
    
    public char GetMove()
    {
        return moveDirection;
    }
    
    public String GetNextTransition()
    {
        return nextTransition;
    }
}
