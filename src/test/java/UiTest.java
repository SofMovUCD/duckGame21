import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.exception.ComponentLookupException;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.movshovich.Board;
import org.movshovich.BoardDraw;
import org.movshovich.Player;
import org.movshovich.buttArrMaker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.movshovich.BoardDraw.buttonPane;
import static org.movshovich.BoardDraw.overlayPanel;

public class UiTest extends AssertJSwingJUnitTestCase {
    private FrameFixture window;

    @BeforeEach
    protected void onSetUp() {
            JFrame frame = GuiActionRunner.execute(() -> {
                //Init board name and closing function
                JFrame boardFrame = new JFrame("Quax Player vs Player");
                boardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                //overlay panel to place above
                overlayPanel = new JPanel();
                overlayPanel.setLayout(new OverlayLayout(overlayPanel));
                overlayPanel.setPreferredSize(new Dimension(810, 1000));

                //Create Panes for placing buttons
                buttonPane = new JLayeredPane();
                JLayeredPane placedPane = new JLayeredPane();
                placedPane.setSize(810, 1000);
                //create label
                BoardDraw.nextMove = new JLabel("BLACK to play");
                BoardDraw.nextMove.setBounds(400, 830, 150, 50);
                //do the numbers and letters at edge of board
                for(int i = 0; i < 11; i++){
                    JLabel num = new JLabel(Integer.toString(i+1));
                    JLabel chars = new JLabel(Character.toString(i+65));
                    num.setBounds(10, 50 + i*68, 15,15);
                    chars.setBounds(60 + i*68, 10, 15,15 );
                    chars.setForeground(Color.WHITE);
                    num.setName(Integer.toString(i+1)); //for testing
                    chars.setName(Character.toString((char)(i+65))); //for testing
                    placedPane.add(num);
                    placedPane.add(chars);
                }
                //l.size
                BoardDraw.nextMove.setOpaque(true);
                placedPane.add(BoardDraw.nextMove);

                //create buttons
                buttArrMaker.initButtArr(buttonPane, placedPane);
                //Add Buttons, Tiles and Board to working Panel
                // Top Layer (Invisible buttons)
                overlayPanel.add(placedPane); // Middle Layer (Tiles being placed)
                overlayPanel.add(new BoardDraw()); // Bottom Layer (The Brown Board)
                boardFrame.add(overlayPanel);

                //Set board properties
                boardFrame.setSize(810,1000);
                boardFrame.setLocationRelativeTo(null);
                boardFrame.setVisible(true);
                return boardFrame;
        });
        // IMPORTANT: note the call to 'robot()'
        // we must use the Robot from AssertJSwingJUnitTestCase
        window = new FrameFixture(robot(), frame);
        Board.plrList.add(new Player(1));
        Board.plrList.add(new Player(-1));
        window.show(); // shows the frame to test
    }


//    @Test
//    public void buttonPressToUpdateArray() {
//        //GIVEN button press
//       /* window.button("0 2").click();
//        //CHECK boardTile updated to correct value
//        assertEquals( -1, Board.board[0][0][0]);*/
//
//        GuiActionRunner.execute(() -> {
//            JButton btn = window.button("0 0").target();
//            for (ActionListener al : btn.getActionListeners()) {
//                al.actionPerformed(new ActionEvent(btn, ActionEvent.ACTION_PERFORMED, null));
//            }
//        });
//        assertEquals(-1, Board.board[0][0][0]);
//    }

    @Test
    public void buttonPressToUpdateLabel() {
        //GIVEN button press
        window.button("0 0").click();
        //CHECK label updated to correct value
        assertEquals("BLACK to play", BoardDraw.nextMove.getText());
    }
/*
    @Test
    public void buttonPressToDeleteButton(){
        //GIVEN button press
        window.button("0 0").click();
        //CHECK button does not exist anymore
        assertThatThrownBy(() -> {
            window.button("0 0").isEnabled();
        }).isInstanceOf(ComponentLookupException.class)
                .hasMessageStartingWith("Unable to find component");
    }*/
@Test
public void buttonPressToDeleteButton(){
    window.button("0 0").click();
    assertEquals(1, Board.board[0][0][0]);
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
        assertEquals(-1, Board.currentPlayer);
        window.button("0 1").click();
        assertEquals(1, Board.currentPlayer);
    }

    @Test
    public void checkAllRhombusButtonsExist(){
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                window.button(i+ " "+ (2*j+1)).isEnabled();
            }
        }
    }

    @Test
    public void checkAllOctagonalButtonsExist(){
        for(int i = 0; i < 11; i++){
            for(int j = 0; j < 11; j++){
                window.button(i+ " "+ 2*j).isEnabled();
            }
        }
    }

    //check
}