import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class BFSearch
{
    // root Node 를 큐에 추가하는 것으로 탐색 시작
    public static void search(int[] board, int[] goal)
    {
        SearchNode root = new SearchNode(new PuzzleState(board, goal));
        Queue<SearchNode> queue = new LinkedList<SearchNode>();

        queue.add(root);

        performSearch(queue, true);
    }

    // 방문했던 노드를 다시 방문하지 않도록 하는 함수
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

    public static void performSearch(Queue<SearchNode> q, boolean d)
    {
        int searchCount = 1;

        while (!q.isEmpty())
        {
            SearchNode tempNode = (SearchNode) q.poll();

            if (!tempNode.getCurState().isGoal())

            {
                ArrayList<State> tempSuccessors = tempNode.getCurState()
                        .genSuccessors();

                for (int i = 0; i < tempSuccessors.size(); i++)
                {
                    SearchNode newNode = new SearchNode(tempNode,
                            tempSuccessors.get(i), tempNode.getCost()
                            + tempSuccessors.get(i).findCost(), 0);

                    if (!checkRepeats(newNode))
                    {
                        q.add(newNode);
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
                    System.out.println();
                    System.out.println();
                }
                System.out.println("COST : " + tempNode.getCost());
                if (d)
                {
                    System.out.println("Search Count: "
                            + searchCount);
                }

                System.exit(0);
            }
        }

        System.out.println("No Solution.");
    }
}
