import java.util.ArrayList;
import java.util.Arrays;

public class PuzzleState implements State
{

    private final int PUZZLE_SIZE = 9;
    private int hamming = 0;
    private int manhattan = 0;
    private int[] hammingState = new int[8];
    private int[] manhattanState = new int[8];

    private int[] GOAL;
    private int[] curBoard;
    private  static ArrayList<State> memoState = new ArrayList<State>();


    public PuzzleState(int[] board, int[] goal)
    {
        curBoard = board;
        GOAL = goal;
        setHamming();
        setManhattan();
    }

    @Override
    public double findCost()
    {
        return 1;
    }

    // SET hamming value
    private void setHamming()
    {
        for (int i = 0; i < curBoard.length; i++)
        {
            int curNumber = curBoard[i];
            if (curNumber == 0)
            {
                continue;
            }

            if (curNumber != GOAL[i])
            {
                hamming++;
                hammingState[curNumber - 1] = 1;
            }
            else
            {
                hammingState[curNumber - 1] = 0;
            }
        }
    }

    // SET manhattan value
    private void setManhattan()
    {
        for(int i = 0; i < curBoard.length; i++)
        {
            int curNumber = curBoard[i];

            if(curNumber == 0)
            {
                continue;
            }

            int curNumberX = (i + 1) % 3;
            int curNumberY = ((i + 1) / 3) + 1;

            int goalNumberX = 0;
            int goalNumberY = 0;

            for(int j = 0; j < GOAL.length; j++)
            {
                if(curNumber == GOAL[j]){
                    goalNumberX = (j + 1) % 3;
                    goalNumberY = ((j + 1) / 3) + 1;
                    break;
                }
            }

            manhattanState[curNumber - 1] = Math.abs(goalNumberX - curNumberX) + Math.abs(goalNumberY - curNumberY);
            manhattan += Math.abs(goalNumberX - curNumberX) + Math.abs(goalNumberY - curNumberY);

        }
    }

    // hole 위치
    private int getHole()
    {
        int holeIndex = -1;

        for (int i = 0; i < PUZZLE_SIZE; i++)
        {
            if (curBoard[i] == 0)
                holeIndex = i;
        }
        return holeIndex;
    }


    public int getHamming()
    {
        return hamming;
    }

    public int getManhattan()
    {
        return manhattan;
    }

    public int[] getCurBoard()
    {
        return curBoard;
    }

    private int[] copyBoard(int[] state)
    {
        int[] ret = new int[PUZZLE_SIZE];
        for (int i = 0; i < PUZZLE_SIZE; i++)
        {
            ret[i] = state[i];
        }
        return ret;
    }

    // 주어진 Node 에서 hole 의 위치에 따라 가능한 자식 Node 를 추가하는 함수
    // Successor 를 생성하는 함수
    @Override
    public ArrayList<State> genSuccessors()
    {
        ArrayList<State> successors = new ArrayList<State>();
        int hole = getHole();

        // hole 이 좌측 모서리에 있지 않을 때
        if (hole != 0 && hole != 3 && hole != 6)
        {
            // hole 의 좌측 이동에 따른 successor 생성
            swapAndStore(hole - 1, hole, successors);
        }
        // hole 이 하단 모서리에 있지 않을 때
        if (hole != 6 && hole != 7 && hole != 8)
        {
            swapAndStore(hole + 3, hole, successors);
        }
        // hole 이 상단 모서리에 있지 않을 때
        if (hole != 0 && hole != 1 && hole != 2)
        {
            swapAndStore(hole - 3, hole, successors);
        }
        // hole 이 우측 모서리에 있지 않을 때
        if (hole != 2 && hole != 5 && hole != 8)
        {
            swapAndStore(hole + 1, hole, successors);
        }

        return successors;
    }

    // hole 이 이동 가능한 자식 Node 의 유무에 따라 리스트에 자식 Node 를 추가
    private void swapAndStore(int d1, int d2, ArrayList<State> s)
    {
        int[] cpy = copyBoard(curBoard);
        int temp = cpy[d1];
        cpy[d1] = curBoard[d2];
        cpy[d2] = temp;
        PuzzleState newState = new PuzzleState(cpy, GOAL);
        boolean isAlreadyIn = false;
        for(int i = 0; i < memoState.size(); i++){
            if(memoState.get(i).equals(newState)){
                isAlreadyIn = true;
            }
        }
        // 새로 추가하려는 Node 가 이전에 방문 했던 Node 이면 추가하지 않는다.
        if(!isAlreadyIn) {
            s.add((new PuzzleState(cpy, GOAL)));
            memoState.add(new PuzzleState(cpy, GOAL));
        }
    }

    @Override
    public boolean isGoal()
    {
        if (Arrays.equals(curBoard, GOAL))
        {
            return true;
        }
        return false;
    }

    @Override
    public void printState()
    {
        System.out.println(curBoard[0] + " " + curBoard[1] + " " + curBoard[2]);
        System.out.println(curBoard[3] + " " + curBoard[4] + " " + curBoard[5]);
        System.out.println(curBoard[6] + " " + curBoard[7] + " " + curBoard[8]);
    }

    @Override
    public void printHamming()
    {
        int temp[] = new int[8];
        System.out.println();
        System.out.println("1  2  3  4  5  6  7  8");
        System.out.println("----------------------");
        for(int i = 0; i < hammingState.length; i++)
        {
            System.out.print(hammingState[i] + "  ");
        }
        System.out.println();
        System.out.println("Hamming : " + getHamming());
    }

    @Override
    public void printManhattan()
    {
        System.out.println();
        System.out.println("1  2  3  4  5  6  7  8");
        System.out.println("----------------------");

        for(int i = 0; i < manhattanState.length; i++)
        {
            System.out.print(manhattanState[i] + "  ");
        }

        System.out.println();
        System.out.println("Manhattan : " + getManhattan());
    }

    @Override
    public boolean equals(State s)
    {
        if (Arrays.equals(curBoard, ((PuzzleState) s).getCurBoard()))
        {
            return true;
        }
        else
            return false;
    }
}
