import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.movshovich.Board;
import org.movshovich.BoardDraw;
import org.movshovich.buttArrMaker;

import javax.swing.*;
import java.awt.*;

import static org.junit.Assert.assertEquals;

public class UiTest  extends AssertJSwingJUnitTestCase {
    private FrameFixture window;

    @Override
    protected void onSetUp() {
            JFrame frame = GuiActionRunner.execute(() -> {
            JFrame boardFrame = new JFrame("Quax Game");
            boardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Create overlay panel
            JPanel overlayPanel = new JPanel();
            overlayPanel.setLayout(new OverlayLayout(overlayPanel));
            overlayPanel.setPreferredSize(new Dimension(810, 1000));

            // Create panes
            JLayeredPane buttonPane = new JLayeredPane();
            JLayeredPane placedPane = new JLayeredPane();
            placedPane.setSize(810, 1000);

            // Create label
            BoardDraw.nextMove = new JLabel("Black to move");
            BoardDraw.nextMove.setBounds(400, 830, 150, 50);

            // Add numbers and letters
            for (int i = 0; i < 11; i++) {
                JLabel num = new JLabel(Integer.toString(i + 1));
                JLabel chars = new JLabel(Character.toString(i + 65));
                num.setBounds(10, 50 + i * 68, 15, 15);
                chars.setBounds(60 + i * 68, 10, 15, 15);
                chars.setForeground(Color.WHITE);
                placedPane.add(num);
                placedPane.add(chars);
            }

            BoardDraw.nextMove.setOpaque(true);
            placedPane.add(BoardDraw.nextMove);

            // Create buttons
            buttArrMaker.initButtArr(buttonPane, placedPane);

            // Add all layers
            overlayPanel.add(buttonPane);
            overlayPanel.add(placedPane);
            overlayPanel.add(new BoardDraw());
            boardFrame.add(overlayPanel);

            boardFrame.setSize(810, 1000);
            boardFrame.setLocationRelativeTo(null);

            return boardFrame;
        });
        // IMPORTANT: note the call to 'robot()'
        // we must use the Robot from AssertJSwingJUnitTestCase
        window = new FrameFixture(robot(), frame);
        window.show(); // shows the frame to test
    }


    @Test
    public void buttonPressToUpdateArray() {
        //GIVEN button press
        window.button("0 0").click();
        //CHECK boardTile updated to correct value
        assertEquals( -1, Board.board[0][0][0]);
    }
}