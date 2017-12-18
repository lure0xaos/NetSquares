package gargoyle.netsquares.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public abstract class Player {
    private final String name;
    private int score;

    protected Player(String name) {
        this.name = name;
        reset();
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

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(name, player.name);
    }

    @Override
    public String toString() {
        return String.format("%s \"%s\" (%d)", getClass().getSimpleName(), name, score);
    }

    protected abstract @Nullable Move move(@NotNull Board board, @NotNull Directions directions);

    void reset() {
        score = 0;
    }
}
