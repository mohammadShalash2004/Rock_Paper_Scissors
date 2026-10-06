/**
 * Cheat strategy always chooses the move
 * that beats the player's move.
 */
public class Cheat implements Strategy
{
    /**
     * Returns the winning move against the player.
     *
     * @param playerMove the player's move
     * @return computer move
     */
    @Override
    public String getMove(String playerMove)
    {
        switch (playerMove)
        {
            case "R":
                return "P";

            case "P":
                return "S";

            case "S":
                return "R";

            default:
                return "R";
        }
    }
}