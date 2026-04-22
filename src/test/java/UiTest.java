import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JButtonFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.assertj.swing.fixture.Containers.showInFrame;
import static org.junit.Assert.assertEquals;

import org.movshovich.QuaxRebuild.src.*;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;


public class UiTest extends AssertJSwingJUnitTestCase {
    private FrameFixture windowF;
    private FrameFixture windowP;


    @BeforeEach
    public void onSetUp() {
        GuiActionRunner.execute(Game::new); // starts game
        System.out.println("ok1");
        //Game.plrByID(Game.getCurrentPlayer()).makeMove(); //EDT violation
        windowF = new FrameFixture(robot(), DrawBoard.boardFrame);
        System.out.println("ok2");
        //windowP = showInFrame(DrawBoard.overlayPanel);
        System.out.println("ok3");
        windowF.show(); // shows the frame to test


    }


    @Test
    public void buttonPressToUpdateArray() {
        JButton btn = windowF.button("0 0").target();
        GuiActionRunner.execute(() ->{btn.doClick();});

        assertEquals("The board array at 0,0 should be updated to 1", 1, Board.getTile(0,0).getValue());
    }

    @Test
    public void buttonPressToUpdateLabel() {
        //GIVEN button press
        JButton btn = windowF.button("0 0").target();
        GuiActionRunner.execute(() ->{btn.doClick();});
        GuiActionRunner.execute(Game::nextTurn);
        //CHECK label updated to correct value
        windowF.label("nextMoveLabelName").requireText("WHITE to play");
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
    JButton btn = windowF.button("0 0").target();
    GuiActionRunner.execute(()->{btn.doClick();});
    assertEquals("The array should be 1 (Black) after first move",1, Board.getTile(0,0).getValue());
}

    @Test
    public void numAndLett(){
        //test all the letters and number on the sides and top exist and are correct
        for(int i = 0; i < 11; i++){
            assertEquals(windowF.label(Integer.toString(i+1)).text(), Integer.toString(i+1));
            assertEquals(windowF.label(Character.toString(i+65)).text(), Character.toString(i+65));
        }
    }
    @Test
    public void switchTurn(){
        JButton btn = windowF.button("0 0").target();
        GuiActionRunner.execute(()->{btn.doClick();}); //click button
        GuiActionRunner.execute(Game::nextTurn); //switch turn (same way as in main)
        assertEquals(-1, Game.getCurrentPlayer());
        JButton btn2 = windowF.button("0 1").target();
        GuiActionRunner.execute(()->{btn2.doClick();}); //click button
        GuiActionRunner.execute(Game::nextTurn); //switch turn (same way as in main)
        assertEquals(1, Game.getCurrentPlayer());
    }

    @Test
    public void checkAllRhombusButtonsExist(){
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                windowF.button((2*i+1)+ " "+ j).isEnabled();
            }
        }
    }

    @Test
    public void checkAllOctagonalButtonsExist(){
        for(int i = 0; i < 11; i++){
            for(int j = 0; j < 11; j++){
                windowF.button(2*i+ " "+ j).isEnabled();
            }
        }
    }

    @Test
    public void PieRuleButtonSwapsPlayers() {
        GuiActionRunner.execute(() -> {
                    Game.plrByID(Game.getCurrentPlayer()).makeMove(); //bot should make move // First move
                });
//        robot().waitForIdle();

        GuiActionRunner.execute(Game::initPiRuleButton);
        GuiActionRunner.execute( () -> {
            DrawBoard.buttonPane.add(Game.piRuleBut);
            DrawBoard.buttonPane.setComponentZOrder(Game.piRuleBut, 0);
        });


        JButtonFixture piBtn = windowF.button("Activate Pi Rule").click();
//        piBtn.doClick();
//        GuiActionRunner.execute(() -> );
        assertEquals("Player 0 ID should now be -1",-1, Game.getPlrList().getFirst().getPlayerId());
    }

    //check

    @Test
    public void BoardStartsEmpty() {
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 21; j++) {
                assertEquals(0, Board.getTile(i,j).getValue());
            }
        }
    }

    @Test
    public void testLabelColorChange() {
        assertEquals("BLACK to play", DrawBoard.nextMove.getText());
        windowF.button("0 0").click();
        assertEquals("WHITE to play", DrawBoard.nextMove.getText());
    }

//    @Test
//    public void PieRuleDisappearsAfterClick() {
//        window.button("0 0").click();
//        GuiActionRunner.execute(() -> {
//            Game.initPiRuleButton();
//            //DrawBoard.overlayPanel.add(Game.piRuleBut);
//            //Game.piRuleBut.doClick();
//        });
//
//        assertThatThrownBy(() -> window.button("Activate Pi Rule"))
//                .isInstanceOf(ComponentLookupException.class);
//    }

    @Test
    public void BlackWinCondition() {
        for(int i = 0; i <= 11; i++) {
           //may need to create tile
            Board.getTile(i,0).setValue(1);
        }
        GuiActionRunner.execute(() -> {
            Board.checkWin(Game.plrByID(1));
        });
        // Check win for Black
        assertEquals("BLACK WINS!!", DrawBoard.nextMove.getText()); //doesnt exist yet
    }

    @Test
    public void WhiteWinCondition() {
        for(int j = 0; j <= 20; j += 2) {
            Board.getTile(0,j).setValue(-1);
        }
        GuiActionRunner.execute(() -> {
            Board.checkWin(Game.plrByID(-1));
        });

        assertEquals("WHITE WINS!!", DrawBoard.nextMove.getText());
    }

//    @Test
//    public void botChoosesVerticalPath() {
//        Board.plrList.clear();
//        Bot blackBot = new Bot(1);
//        Player whitePlayer = new Player(-1); // Add the opponent!
//        Board.plrList.add(blackBot);
//        Board.plrList.add(whitePlayer);
//        Board.currentPlayer = 1;
//
//        GuiActionRunner.execute(() -> blackBot.makeMove());
//        assertEquals(1, Board.board[0][10][0]);
//    }
//
//    @Test
//    public void botManeuversAroundObstacle() {
//        Board.plrList.set(0, new Bot(1));
//        Board.board[0][10][0] = 1;
//        Board.board[1][10][0] = -1;
//
//        GuiActionRunner.execute(() -> ((Bot)Board.plrList.get(0)).makeMove());
//        assertEquals("Bot should use Rhombus to maneuver", 1, Board.board[0][11][0]);
//    }

    @Test
    public void botTakesWinningMove() {
//        Board.plrList.set(0, new Bot(1));
//        Bot blackBot = (Bot) Board.plrList.get(0);
        for(int i = 0; i < 10; i++) {
            Board.getTile(i,10).setValue(1);
        }
        //Game.currentPlayer = 1;
        Game.plrByID(Game.getCurrentPlayer()).makeMove(); //bot should make move

        GuiActionRunner.execute(() -> Board.checkWin(Game.plrByID(1)));
        assertEquals("BLACK WINS!!", DrawBoard.nextMove.getText());
    }

//    @Test
//    public void botHandlesFullBoard() {
//        Board.plrList.set(0, new Bot(1));
//        Bot blackBot = (Bot) Board.plrList.get(0);
//        for (int i = 0; i < 11; i++) {
//            for (int j = 0; j < 21; j++) {
//                Board.board[i][j][0] = 5;
//            }
//        }
//        GuiActionRunner.execute(() -> blackBot.makeMove());
//    }
//
//    @Test
//    public void botUsesFallbackWhenColumnFull() {
//        Board.plrList.set(0, new Bot(1));
//        for(int i = 0; i < 11; i++) { Board.board[i][10][0] = -1; }
//
//        GuiActionRunner.execute(() -> ((Bot)Board.plrList.get(0)).makeMove());
//        assertEquals("Bot should fallback to column 0", 1, Board.board[0][0][0]);
//    }

}