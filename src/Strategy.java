/**
 * Strategy interface for choosing a computer move.
 */
public interface Strategy
{
    /**
     * Gets the computer's move.
     *
     * @param playerMove the player's move
     * @return R, P, or S
     */
    String getMove(String playerMove);
}