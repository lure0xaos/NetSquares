package gargoyle.netsquares.model;

import java.util.Objects;

public abstract class Player {
    private final String name;
    private int score;

    protected Player(String name) {
        this.name = name;
        reset();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(name, player.name);
    }

    void addScore(int score) {
        this.score += score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    protected abstract Move move(Board board, Directions directions);

    @Override
    public String toString() {
        return String.format("%s \"%s\" (%d)", getClass().getSimpleName(), name, score);
    }

    void reset() {
        score = 0;
    }
}
