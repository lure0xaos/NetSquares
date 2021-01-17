package gargoyle.netsquares.gui;

import gargoyle.netsquares.beans.Messages;
import gargoyle.netsquares.beans.Resources;
import gargoyle.netsquares.gui.i.IPlayerInfo;
import gargoyle.netsquares.logic.i.IGame;
import gargoyle.netsquares.player.a.Player;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.io.IOException;

public class JPlayerInfo extends JPanel implements IPlayerInfo {
    private static final String MSG_NAME = "name";
    private static final String MSG_SCORE = "score";
    private static final String NAME_FONT_LOCATION = "gargoyle/netsquares/ZX-Spectrum.ttf";
    private static final String SCORE_FONT_LOCATION = "gargoyle/netsquares/digital-7 (mono).ttf";
    private static final long serialVersionUID = 1L;
    private static final Font nameFont;
    private static final Font scoreFont;
    private static final Messages messages;

    static {
        try {
            nameFont = Resources.forCurrentThread().loadFont(NAME_FONT_LOCATION);
        } catch (final IOException e) {
            throw new IllegalArgumentException(e);
        } catch (final ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
        try {
            scoreFont = Resources.forCurrentThread().loadFont(SCORE_FONT_LOCATION);
        } catch (final IOException e) {
            throw new IllegalArgumentException(e);
        } catch (final ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
        try {
            messages = Messages.messages(JPlayerInfo.class);
        } catch (final ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private final JLabel name;
    private final JLabel score;
    private transient Player player;

    public JPlayerInfo(final Player player) {
        setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        this.player = player;
        setLayout(new GridLayout(2, 1));
        final Font baseFont = getFont();
        final int baseFontSize = baseFont.getSize();
        final JPanel pnlName = new JPanel(new GridLayout(2, 1));
        pnlName.setOpaque(false);
        pnlName.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        pnlName.add(createLabel(baseFont, messages.message(MSG_NAME) + ":", baseFontSize * 2, SwingConstants.BOTTOM));
        pnlName.add(name = createLabel(nameFont, "", baseFontSize * 3, SwingConstants.TOP));
        add(pnlName);
        final JPanel pnlScore = new JPanel(new GridLayout(2, 1));
        pnlScore.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        pnlScore.setOpaque(false);
        pnlScore.add(createLabel(baseFont, messages.message(MSG_SCORE) + ":", baseFontSize * 2, SwingConstants.BOTTOM));
        pnlScore.add(score = createLabel(scoreFont, "", baseFontSize * 5, SwingConstants.TOP));
        add(pnlScore);
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(final Player player) {
        this.player = player;
    }

    private JLabel createLabel(final Font nameFont, final String text, final float size, final int verticalAlign) {
        final JLabel label = new JLabel(text);
        label.setFont(nameFont.deriveFont(size));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        if (verticalAlign == SwingConstants.TOP) label.setAlignmentY(TOP_ALIGNMENT);
        if (verticalAlign == SwingConstants.BOTTOM) label.setAlignmentY(BOTTOM_ALIGNMENT);
        if (verticalAlign == SwingConstants.CENTER) label.setAlignmentY(CENTER_ALIGNMENT);
        label.setVerticalTextPosition(verticalAlign);
        label.setVerticalAlignment(verticalAlign);
        return label;
    }

    @Override
    public void updateInfo(final IGame game) {
        if (player != null) {
            name.setText(player.getName());
            score.setText(String.valueOf(player.getScore()));
            setOpaque(false);
            name.setForeground(game.getColor((player.equals(game.getCurrentPlayer()))));
            score.setForeground(game.getColor(player.getScore()));
        }
    }
}
