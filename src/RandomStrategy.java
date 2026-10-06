import java.util.Random;

/**
 * Random strategy randomly chooses
 * rock, paper, or scissors.
 */
public class RandomStrategy implements Strategy
{
    private Random random = new Random();

    /**
     * Randomly chooses a move.
     *
     * @param playerMove the player's move
     * @return R, P, or S
     */
    @Override
    public String getMove(String playerMove)
    {
        int choice = random.nextInt(3);

        if (choice == 0)
        {
            return "R";
        }
        else if (choice == 1)
        {
            return "P";
        }
        else
        {
            return "S";
        }
    }
}