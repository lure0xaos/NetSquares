package gargoyle.netsquares.events;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class GameNotifier {
    private final List<IGameNotifier> notifiers = new ArrayList<IGameNotifier>();

    public GameNotifier() {
        super();
    }

    public synchronized void addGameNotifier(final IGameNotifier notifier) {
        notifiers.add(notifier);
    }

    public synchronized void notify(final GameNote gameNote) {
        final Stack<GameNote> repeat = new Stack<GameNote>();
        repeat.push(gameNote);
        do {
            final GameNote note = repeat.pop();
            for (final IGameNotifier notifier : notifiers) {
                final GameNote notify = notifier.notify(note);
                if (notify != null) {
                    repeat.push(notify);
                }
            }
        } while (!repeat.isEmpty());
    }

    public synchronized void removeGameNotifier(final IGameNotifier notifier) {
        notifiers.remove(notifier);
    }

    public synchronized void removeGameNotifiers() {
        notifiers.clear();
    }
}
