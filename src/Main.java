import java.io.*;
import java.util.Arrays;

public class Main
{
    public static void main(String[] args) throws IOException
    {
        boolean debug = true;

        BufferedReader in = new BufferedReader(new FileReader(Main.class.getResource("").getPath() + "/in.txt"));
        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
        int numbers[] = new int[9];
        for(int i = 0; i < 3; i++)
        {
            String puzzle = in.readLine();
            String strNumbers[] = puzzle.split(" ");
            for(int j = 0; j < 3; j++)
            {
                numbers[(3 * i) + j] = Integer.parseInt(strNumbers[j]);
            }
        }
        int goals[] = new int[9];
        in = new BufferedReader(new FileReader(Main.class.getResource("").getPath() + "/g.txt"));
        for(int i = 0; i < 3; i++)
        {
            String puzzle = in.readLine();
            String strNumbers[] = puzzle.split(" ");
            for(int j = 0; j < 3; j++)
            {
                goals[(3 * i) + j] = Integer.parseInt(strNumbers[j]);
            }
        }

        if (Arrays.equals(numbers, goals))
        {
            System.out.println("Already Same.");
            System.exit(-1);
        }

        System.out.println("Choose Number");
        System.out.println("1. BFS");
        System.out.println("2. DFS");
        System.out.println("3. HillClimbing Search using Hamming Function");
        System.out.println("4. HillClimbing Search using Manhattan Function");
        System.out.println("5. Best First Search using Hamming Function");
        System.out.println("6. Best First Search using Manhattan Function");
        System.out.println("7. A* Search using Hamming Function");
        System.out.println("8. A* Search using Manhattan Function");
        System.out.print("--> ");

        String searchType = keyboard.readLine();

        if (!searchType.equals("exit"))
        {
            int[] startingStateBoard = numbers;

            if (searchType.equals("1"))
            {
                DFSearch.search(startingStateBoard, goals);
            }
            else if (searchType.equals("2"))
            {
                BFSearch.search(startingStateBoard, goals);
            }
            else if (searchType.equals("3"))
            {
                HillClimbingSearch.search(startingStateBoard, 'h', goals);
            }
            else if (searchType.equals("4"))
            {
                HillClimbingSearch.search(startingStateBoard, 'm', goals);
            }
            else if (searchType.equals("5"))
            {
                BestFirstSearch.search(startingStateBoard, 'h', goals);
            }
            else if (searchType.equals("6"))
            {
                BestFirstSearch.search(startingStateBoard, 'm', goals);
            }
            else if (searchType.equals("7"))
            {
                AStarSearch.search(startingStateBoard, debug, 'h', goals);
            }
            else if (searchType.equals("8"))
            {
                AStarSearch.search(startingStateBoard, debug, 'm', goals);
            }
            else
            {
                printWrong();
            }
        }
        else
        {

            printWrong();

        }
    }
    private static void printWrong()
    {
        System.out.println("Wrong input");
        System.exit(-1);
    }

}
