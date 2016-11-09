import java.util.ArrayList;

public interface State
{
    boolean isGoal();
    ArrayList<State> genSuccessors();
    double findCost(); // 1 return
    public void printState();
    public void printHamming();
    public void printManhattan();
    public boolean equals(State s);
}
