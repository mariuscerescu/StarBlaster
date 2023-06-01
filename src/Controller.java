import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Controller{

    @FXML
    public ImageView shipImageView;

    @FXML
    public ImageView laserImageView;

    @FXML
    public Label titleLabel, scoreLabel;

    @FXML
    public ImageView heart1 = new ImageView(), heart2 = new ImageView(), heart3 = new ImageView();

    @FXML
    Label gameOver;

    @FXML
    private Button startBtn;

    @FXML
    Button restartBtn;

    @FXML
    private Label rulesLabel1, rulesLabel2, rulesLabel3, rulesLabel4, rulesLabel5, rulesLabel6;

    @FXML
    public ImageView redLine = new ImageView();

    public boolean gameStarted = false;

    public boolean restartCheck = false;

    @FXML
    public ImageView alien1, alien2, alien3, alien4, alien5, alien6, alien7, alien8, alien9, alien10, alien11;

    public ArrayList<ImageView> aliensQueue = new ArrayList<>();

    public Image explotion = new Image("Images/explotion.png");

    public Image spaceShip = new Image("Images/ship.png");

    public Image slowOrb = new Image("Images/slowOrb.png");

    public Image alienImage = new Image("Images/alien.png");

    @FXML
    void startBtn(ActionEvent event) {
        gameStarted = true;
        titleLabel.setVisible(false);
        startBtn.setVisible(false);
        rulesLabel1.setVisible(false);
        rulesLabel2.setVisible(false);
        rulesLabel3.setVisible(false);
        rulesLabel4.setVisible(false);
        rulesLabel5.setVisible(false);
        rulesLabel6.setVisible(false);
    }

    @FXML
    void restartBtn(ActionEvent event) {
        restartCheck = true;
    }

    public void initialize(){
        gameOver.setVisible(false);
        restartBtn.setVisible(false);
        laserImageView.setMouseTransparent(true);
        aliensQueue.add(alien1);
        aliensQueue.add(alien2);
        aliensQueue.add(alien3);
        aliensQueue.add(alien4);
        aliensQueue.add(alien5);
        aliensQueue.add(alien6);
        aliensQueue.add(alien7);
        aliensQueue.add(alien8);
        aliensQueue.add(alien9);
        aliensQueue.add(alien10);
        aliensQueue.add(alien11);
    }

}
