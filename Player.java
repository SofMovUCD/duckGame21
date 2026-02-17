import java.awt.*;

public class Player {
    private int playerId;
    private Color tileColour;

    public Player(int playerId){
        this.playerId = playerId;
        this.tileColour = playerId== 1? Color.black: Color.white;
    }
    public int getPlayerId(){return playerId;}

    public void makeMove(){
        //should add button pane

        //should remove button pane
    }

}
