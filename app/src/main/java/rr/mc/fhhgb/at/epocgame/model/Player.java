package rr.mc.fhhgb.at.epocgame.model;

/**
 * class for the highscore
 * @author Windischhofer, Rohner
 */
public class Player {
    private String name;
    private int score;

    /**
     * constructor
     * @param name the players name
     * @param score the players score
     */
    public Player(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
