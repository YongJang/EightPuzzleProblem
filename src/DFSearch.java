import java.util.ArrayList;
import java.util.Stack;

public class DFSearch
{
    // 깊이 우선 탐색은 특정한 경우에 탐색 시간이 매우 오래 걸린다.
    // 따라서 깊이에 제한을 두어 이를 방지한다.
    private static int LIMITED_DEPTH = 20;

    public static void search(int[] board, int[] goal)
    {
        SearchNode root = new SearchNode(new PuzzleState(board, goal));
        Stack<SearchNode> stack = new Stack<SearchNode>();

        stack.add(root);

        performSearch(stack, true);
    }


    // 한 번 방문했던 노드를 다시 추가하지 않도록 하는 함수
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

    public static void performSearch(Stack<SearchNode> s, boolean d)
    {
        int searchCount = 1;

        // 스택이 비어있으면 No Solution
        while (!s.isEmpty())
        {
            SearchNode tempNode = (SearchNode) s.pop();

            if (!tempNode.getCurState().isGoal())
            {
                // 현재 Node 에서 가능한 Successor 생성
                if (tempNode.getCost() >= LIMITED_DEPTH){
                    continue;
                }
                ArrayList<State> tempSuccessors = tempNode.getCurState().genSuccessors();

                for (int i = 0; i < tempSuccessors.size(); i++)
                {
                    SearchNode newNode = new SearchNode(tempNode,
                            tempSuccessors.get(i), tempNode.getCost() + tempSuccessors.get(i).findCost(), 0);

                    if (!checkRepeats(newNode))
                    {
                        s.add(newNode);
                    }
                }
                searchCount++;
            }
            // GOAL 을 찾은 경우
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

        // 주어진 깊이 내에서 해답을 찾지 못한 경우
        System.out.println("No Solution. When Limit Depth is " + LIMITED_DEPTH);
    }
}
