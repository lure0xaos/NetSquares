package gargoyle.netsquares.i;

import gargoyle.netsquares.gui.i.NSScreen;
import gargoyle.netsquares.model.Directions;
import gargoyle.netsquares.player.a.Player;

import javax.swing.*;

public interface INetSquares {
    void alert(String message);

    boolean confirm(String message);

    Player createComputerPlayer(String name, Directions allowedDirections);

    Player createHumanPlayer(String name, Directions allowedDirections);

    void showScreen(NSScreen form);
}
