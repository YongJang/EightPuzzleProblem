import java.util.ArrayList;
import java.util.Stack;

public class HillClimbingSearch
{
    // Hill Climbing Search 는 깊이 우선 탐색을 이용하며 특정한 경우에 탐색 시간이 매우 오래 걸린다.
    // 따라서 깊이에 제한을 두어 이를 방지한다.
    private static int LIMITED_DEPTH = 20;

    public static void search(int[] board, char heuristic, int[] goal)
    {
        PuzzleState firstState = new PuzzleState(board,goal);
        SearchNode root;
        if (heuristic == 'h')
        {
            root = new SearchNode(null, firstState, 0, firstState.getHamming());
        }
        else
        {
            root = new SearchNode(null, firstState, 0, firstState.getManhattan());
        }

        Stack<SearchNode> stack = new Stack<SearchNode>();

        stack.add(root);

        performSearch(stack, heuristic, true);
    }

    // 방문 했던 노드를 다시 추가하지 않도록 함
    private static boolean checkRepeats(SearchNode n)
    {
        boolean retValue = false;
        SearchNode checkNode = n;

        while (n.getParent() != null && !retValue)
        {
            if (n.getParent().getCurState().equals(checkNode.getCurState()))
            {
                retValue = true;
            }
            n = n.getParent();
        }

        return retValue;
    }

    public static void performSearch(Stack<SearchNode> s, char heuristic, boolean d)
    {
        int searchCount = 1;

        while (!s.isEmpty())
        {
            SearchNode tempNode = (SearchNode) s.pop();

            if (!tempNode.getCurState().isGoal())
            {
                if (tempNode.getCost() >= LIMITED_DEPTH){
                    continue;
                }
                ArrayList<State> tempSuccessors = tempNode.getCurState()
                        .genSuccessors();

                for (int i = 0; i < tempSuccessors.size(); i++)
                {
                    SearchNode newNode;
                    if (heuristic == 'h')
                    {
                        newNode = new SearchNode(tempNode, tempSuccessors.get(i), tempNode.getCost() + tempSuccessors.get(i).findCost(), ((PuzzleState) tempSuccessors.get(i)).getHamming());
                    }
                    else
                    {
                        newNode = new SearchNode(tempNode, tempSuccessors.get(i), tempNode.getCost() + tempSuccessors.get(i).findCost(), ((PuzzleState) tempSuccessors.get(i)).getManhattan());
                    }

                    // 부모의 h(n), 휴리스틱 비용보다 자식 노드의 h(n) 이 작을 때 스택에 현재 Node 를 추가
                    if (newNode.getParent().getHCost() >= newNode.getHCost())
                    {
                        if (!checkRepeats(newNode))
                        {
                            s.add(newNode);
                        }
                    }

                }
                searchCount++;
            }
            else
            {
                Stack<SearchNode> solutionPath = new Stack<SearchNode>();
                solutionPath.push(tempNode);
                tempNode = tempNode.getParent();

                while (tempNode.getParent() != null)
                {
                    solutionPath.push(tempNode);
                    tempNode = tempNode.getParent();
                }
                solutionPath.push(tempNode);

                int loopSize = solutionPath.size();

                for (int i = 0; i < loopSize; i++)
                {
                    tempNode = solutionPath.pop();
                    tempNode.getCurState().printState();
                    if (heuristic == 'h')
                    {
                        tempNode.getCurState().printHamming();
                    }
                    else
                    {
                        tempNode.getCurState().printManhattan();
                    }
                    System.out.println();
                    System.out.println();
                }
                System.out.println("COST : " + tempNode.getCost());
                if (d)
                {
                    System.out.println("Search Count : "
                            + searchCount);
                }

                System.exit(0);
            }
        }

        System.out.println("No Solution. When Limit Depth is " + LIMITED_DEPTH);
    }
}
