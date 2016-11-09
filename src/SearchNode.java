// 각 Node 들에 대한 class
public class SearchNode
{

    private State curState;
    private SearchNode parent;
    private double cost; // g(n) root 노드로 부터의 비용
    private double hCost; // h(n) 현재 노드에서 평가 함수로 부터 구해지는 비용
    private double fCost; // f(n) = g(n) + h(n)

    public SearchNode(State s)
    {
        curState = s;
        parent = null;
        cost = 0;
        hCost = 0;
        fCost = 0;
    }

    public SearchNode(SearchNode prev, State s, double c, double h)
    {
        parent = prev;
        curState = s;
        cost = c;
        hCost = h;
        fCost = cost + hCost;
    }

    public State getCurState()
    {
        return curState;
    }

    public SearchNode getParent()
    {
        return parent;
    }

    public double getCost()
    {
        return cost;
    }

    public double getHCost()
    {
        return hCost;
    }

    public double getFCost()
    {
        return fCost;
    }
}
