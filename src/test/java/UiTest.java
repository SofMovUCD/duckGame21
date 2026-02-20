import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.exception.ComponentLookupException;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.movshovich.Board;
import org.movshovich.BoardDraw;
import org.movshovich.buttArrMaker;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.Assert.assertEquals;

public class UiTest extends AssertJSwingJUnitTestCase {
    private FrameFixture window;

    @BeforeEach
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
                JLabel num = new JLabel(Integer.toString(i+1));
                num.setName(Integer.toString(i+1));
                JLabel chars = new JLabel(Character.toString(i+65));
                chars.setName(Character.toString(i+65));
                num.setBounds(10, 50 + i*68, 15,15);
                chars.setBounds(60 + i*68, 10, 15,15 );
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

    @Test
    public void buttonPressToUpdateLabel() {
        //GIVEN button press
        window.button("0 0").click();
        //CHECK label updated to correct value
        assertEquals("Black to move", BoardDraw.nextMove.getText());
    }

    @Test
    public void buttonPressToDeleteButton(){
        //GIVEN button press
        window.button("0 0").click();
        //CHECK button does not exist anymore
        assertThatThrownBy(() -> {
            window.button("0 0").isEnabled();
        }).isInstanceOf(ComponentLookupException.class)
                .hasMessageStartingWith("Unable to find component");
    }

    @Test
    public void numAndLett(){
        //test all the letters and number on the sides and top exist and are correct
        for(int i = 0; i < 11; i++){
            assertEquals(window.label(Integer.toString(i+1)).text(), Integer.toString(i+1));
            assertEquals(window.label(Character.toString(i+65)).text(), Character.toString(i+65));
        }
    }
    @Test
    public void switchTurn(){
        window.button("0 0").click();
        assertEquals(-1, buttArrMaker.isBlackTurn);
        window.button("0 1").click();
        assertEquals(1, buttArrMaker.isBlackTurn);
    }

    @Test
    public void checkAllRhombusButtonsExist(){
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                window.button(Integer.toString(i)+ " "+ Integer.toString(2*j+1)).isEnabled();
            }
        }
    }

    @Test
    public void checkAllOctagonalButtonsExist(){
        for(int i = 0; i < 11; i++){
            for(int j = 0; j < 11; j++){
                window.button(Integer.toString(i)+ " "+ Integer.toString(2*j)).isEnabled();
            }
        }
    }
}