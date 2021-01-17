package gargoyle.netsquares.events;

import gargoyle.netsquares.logic.i.IGame;

import java.text.MessageFormat;

public class GameNote<V> {
    private final IGame game;
    private final GameNoteType type;
    private final V value;

    public GameNote(final IGame game, final GameNoteType type) {
        this(game, type, null);
    }

    public GameNote(final IGame game, final GameNoteType type, V value) {
        this.game = game;
        this.type = type;
        this.value = value;
    }

    public IGame getGame() {
        return game;
    }

    public GameNoteType getType() {
        return type;
    }

    public V getValue() {
        return value;
    }

    @Override
    public String toString() {
        return MessageFormat.format("GameNote'{'type={0}'}'", type);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof GameNote)) return false;
        final GameNote gameNote = (GameNote) o;
        return game != null ? game.equals(gameNote.game) : gameNote.game == null && type == gameNote.type;

    }

    @Override
    public int hashCode() {
        int result = game != null ? game.hashCode() : 0;
        result = 31 * result + type.hashCode();
        return result;
    }
}
