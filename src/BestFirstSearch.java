import java.util.ArrayList;
import java.util.Stack;

public class BestFirstSearch
{
    // Best First Search 는 깊이 우선 탐색을 이용하며 특정한 경우에 탐색 시간이 매우 오래 걸린다.
    // 따라서 깊이에 제한을 두어 이를 방지한다.
    private static int LIMITED_DEPTH = 100;

    public static void search(int[] board, char heuristic, int[] goal)
    {
        SearchNode root = new SearchNode(new PuzzleState(board, goal));
        Stack<SearchNode> stack = new Stack<SearchNode>();

        stack.add(root);

        performSearch(stack, heuristic, true);
    }

    // 한 번 방문했던 노드를 다시 방문하지 않도록 하는 함수
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

    // Best First 탐색은 h(n) 의 값에 따라 다음 방문할 노드를 선택한다.
    // Successor 를 생성한 후 스택에 쌓인 노드들 중에 최적의 h(n) 값을 가진 노드를 방문한다.
    // h(n) 의 값에 따라 노드들을 정렬하는 함수
    private static Stack<SearchNode> bestFirstSort(Stack<SearchNode> s)
    {
        ArrayList<SearchNode> arrayList = new ArrayList<SearchNode>();
        while (!s.isEmpty())
        {
            SearchNode newNode = (SearchNode) s.pop();
            arrayList.add(newNode);
        }

        for(int i = 0; i < arrayList.size(); i++)
        {
            SearchNode tempNode;
            for(int j = 0; j < arrayList.size(); j++)
            {
                if(arrayList.get(i).getHCost() > arrayList.get(j).getHCost())
                {
                    tempNode = arrayList.get(i);
                    arrayList.set(i, arrayList.get(j));
                    arrayList.set(j, tempNode);
                }
            }
        }
        for(int i = 0; i < arrayList.size(); i++)
        {
            s.push(arrayList.get(i));
        }
        return s;
    }

    public static void performSearch(Stack<SearchNode> s, char heuristic, boolean d)
    {
        int searchCount = 1;

        // 스택이 비어있다면 No Solution
        while (!s.isEmpty())
        {
            SearchNode tempNode = (SearchNode) s.pop();

            if (!tempNode.getCurState().isGoal())
            {
                // 현재 Node 에서 가능한 Successor 생성
                if (tempNode.getCost() >= LIMITED_DEPTH){
                    continue;
                }
                ArrayList<State> tempSuccessors = tempNode.getCurState()
                        .genSuccessors();

                for (int i = 0; i < tempSuccessors.size(); i++)
                {
                    // Best First Search 의 평가 함수로 사용될 휴리스틱에 따라 새로운 노드들을 스택에 쌓는 과정
                    SearchNode newNode;
                    // Hamming Function
                    if (heuristic == 'h')
                    {
                        newNode = new SearchNode(tempNode, tempSuccessors.get(i), tempNode.getCost() + tempSuccessors.get(i).findCost(), ((PuzzleState)tempSuccessors.get(i)).getHamming());
                    }
                    // Manhattan Function
                    else
                    {
                        newNode = new SearchNode(tempNode, tempSuccessors.get(i), tempNode.getCost() + tempSuccessors.get(i).findCost(), ((PuzzleState)tempSuccessors.get(i)).getManhattan());
                    }

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
            s = bestFirstSort(s);
        }

        // 주어진 조건 하에서 해답을 찾지 못한 경우
        System.out.println("No Solution. When Limit Depth is " + LIMITED_DEPTH);
    }
}
