import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class AStarSearch
{

    public static void search(int[] board, boolean d, char heuristic, int[] goal)
    {
        SearchNode root = new SearchNode(new PuzzleState(board, goal));
        Queue<SearchNode> q = new LinkedList<SearchNode>();
        q.add(root);

        int searchCount = 1; // 노드 방문 횟수

        while (!q.isEmpty()) // 큐가 비어 있다면 No Solution.
        {
            SearchNode tempNode = (SearchNode) q.poll();

            // 방문 노드가 GOAL 이면
            if (!tempNode.getCurState().isGoal())
            {
                // Successsors 생성
                ArrayList<State> tempSuccessors = tempNode.getCurState().genSuccessors();
                ArrayList<SearchNode> nodeSuccessors = new ArrayList<SearchNode>();

                // tempSuccessors 에서 Hamming Function 과 Manhattan Function 에따라 nodeSuccessors 에 추가
                for (int i = 0; i < tempSuccessors.size(); i++)
                {
                    SearchNode checkedNode;
					/*
					 *  A* 알고리즘 비용 함수
					 *  f(n) = g(n) + h(n)
					 *  f(n) = 비용, g(n) = 시작점에서 부터의 비용, h(n) = 방문 노드에서의 휴리스틱 비용
					 */

                    // Hamming Function
                    if (heuristic == 'h')
                    {
                        checkedNode = new SearchNode(tempNode,
                                tempSuccessors.get(i), tempNode.getCost() + tempSuccessors.get(i).findCost(),
                                ((PuzzleState) tempSuccessors.get(i)).getHamming());
                    }
                    // Manhattan Function
                    else
                    {
                        checkedNode = new SearchNode(tempNode,
                                tempSuccessors.get(i), tempNode.getCost() + tempSuccessors.get(i).findCost(),
                                ((PuzzleState) tempSuccessors.get(i)).getManhattan());
                    }

                    // 같은 노드를 다시 방문하지 않기 위해 체크
                    if (!checkRepeats(checkedNode))
                    {
                        nodeSuccessors.add(checkedNode);
                    }
                }

                // nodeSuccessors 에 추가된 노드가 없다면 큐에서 새로운 노드를 꺼냄
                if (nodeSuccessors.size() == 0)
                    continue;

                // A* 알고리즘은 f(n) 에 따라 다음에 방문할 노드를 선택한다.
                // f(n) 이 가장 낮은 노드를 탐색한다.
                SearchNode minNode = nodeSuccessors.get(0);
                for (int i = 0; i < nodeSuccessors.size(); i++)
                {
                    if (minNode.getFCost() > nodeSuccessors.get(i)
                            .getFCost())
                    {
                        minNode = nodeSuccessors.get(i);
                    }
                }

                int minValue = (int) minNode.getFCost();

                // 가장 낮은 값과 같은 f(n) 을 가진 노드들을 큐에 추가한다.
                for (int i = 0; i < nodeSuccessors.size(); i++)
                {
                    if (nodeSuccessors.get(i).getFCost() == minValue)
                    {
                        q.add(nodeSuccessors.get(i));
                    }
                }

                searchCount++;
            }
            // GOAL 을 찾았을 때
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
                System.out.println("The cost was: " + tempNode.getCost());
                if (d)
                {
                    System.out.println("Search Count : "
                            + searchCount);
                }

                System.exit(0);
            }
        }

        // 큐에 남아있는 노드가 없으면 해답을 찾을 수 없다.
        System.out.println("No Solution.");
    }

    // 방문했던 노드를 다시 방문하지 않기 위한 함수
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

}
