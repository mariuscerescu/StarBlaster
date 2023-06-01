import java.util.ArrayList;
import java.util.Random;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

public class App extends Application{
    public static void main(String[] args) throws Exception {
        launch(args);
    }

    int checkCollisions(ImageView ship, ArrayList<ImageView> aliens, ArrayList<ImageView> exclude){
        for(int i = 0; i < aliens.size(); i++){
            if(!exclude.contains(aliens.get(i)) && ship.getBoundsInParent().intersects(aliens.get(i).getBoundsInParent())){
                return i;
            }
        }
        return -1;
    }

    boolean stopAliensFromPlayCheck = true;
    Random random = new Random();
    int score = 0;
    int hearts = 3;
    int lastAlienKiiled = -1;
    int freezeOrbScore = 100;
    int alienToChacheToOrb;
    boolean orbShouldAppear = false;

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScene.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        Scene scene = new Scene(root);
        
        stage.setTitle("Cerescu Marius | Laboratorul 7 | Tehnici avansate de programare");
        stage.setResizable(false);

        TranslateTransition laserTransition = new TranslateTransition();
        laserTransition.setNode(controller.laserImageView);
        laserTransition.setDuration(Duration.millis(75));
        laserTransition.setInterpolator(Interpolator.LINEAR);

        TranslateTransition shipTransition = new TranslateTransition();
        shipTransition.setNode(controller.shipImageView);
        shipTransition.setDuration(Duration.millis(75));
        shipTransition.setInterpolator(Interpolator.LINEAR);

        FadeTransition laserFadeOnTransition = new FadeTransition();
        laserFadeOnTransition.setNode(controller.laserImageView);
        laserFadeOnTransition.setInterpolator(Interpolator.LINEAR);
        laserFadeOnTransition.setDuration(Duration.millis(300));
        laserFadeOnTransition.setFromValue(0);
        laserFadeOnTransition.setToValue(1);

        FadeTransition laserFadeOffTransition = new FadeTransition();
        laserFadeOffTransition.setNode(controller.laserImageView);
        laserFadeOffTransition.setInterpolator(Interpolator.LINEAR);
        laserFadeOffTransition.setDuration(Duration.millis(300));
        laserFadeOffTransition.setFromValue(1);
        laserFadeOffTransition.setToValue(0);

        ArrayList<TranslateTransition> aliens = new ArrayList<>();
        ArrayList<ImageView> exclude = new ArrayList<>();
        ArrayList<FadeTransition> aliensFadeOff = new ArrayList<>();
        ArrayList<FadeTransition> aliensFadeOn = new ArrayList<>();

        for(int i = 0; i < 11; i++){
            int delay = random.nextInt(15);
            aliens.add(new TranslateTransition());
            aliens.get(i).setNode(controller.aliensQueue.get(i));
            aliens.get(i).setDuration(Duration.seconds(10));
            aliens.get(i).setByY(400);
            aliens.get(i).setCycleCount(TranslateTransition.INDEFINITE);
            aliens.get(i).setInterpolator(Interpolator.LINEAR);
            aliens.get(i).setDelay(Duration.seconds(delay));
        }

        for(int i = 0; i < 11; i++){
            aliensFadeOff.add(new FadeTransition());
            aliensFadeOff.get(i).setNode(controller.aliensQueue.get(i));
            aliensFadeOff.get(i).setInterpolator(Interpolator.LINEAR);
            aliensFadeOff.get(i).setDuration(Duration.millis(200));
            aliensFadeOff.get(i).setFromValue(1);
            aliensFadeOff.get(i).setToValue(0);

            aliensFadeOn.add(new FadeTransition());
            aliensFadeOn.get(i).setNode(controller.aliensQueue.get(i));
            aliensFadeOn.get(i).setInterpolator(Interpolator.LINEAR);
            aliensFadeOn.get(i).setDuration(Duration.millis(200));
            aliensFadeOn.get(i).setFromValue(0);
            aliensFadeOn.get(i).setToValue(1);
        }

        Timeline checkCollisionsTimeline = new Timeline(new KeyFrame(Duration.millis(100), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if(score == freezeOrbScore){
                    alienToChacheToOrb = random.nextInt(11);
                    freezeOrbScore += 50;
                    orbShouldAppear = true;
                }
                
                //Verificăm dacă un extraterestru a fost ucis
                int alienKilled = checkCollisions(controller.laserImageView, controller.aliensQueue, exclude);
        
                if(alienKilled != -1 && controller.laserImageView.getOpacity() != 0){

                    if(!controller.aliensQueue.get(alienKilled).getImage().equals(controller.slowOrb)){
                        // exclude.add(controller.aliensQueue.get(alienKilled));
                        // aliensFadeOff.get(alienKilled).play();
                        if(alienKilled != lastAlienKiiled){
                            controller.scoreLabel.setText(String.valueOf(++score));
                            lastAlienKiiled = alienKilled;
                        }

                        //Crestem viteza extraterestrului ucis
                        aliens.get(alienKilled).stop();
                        aliens.get(alienKilled).setDuration(Duration.seconds(aliens.get(alienKilled).getDuration().toSeconds() * 0.95));
                        controller.aliensQueue.get(alienKilled).setTranslateY(0);
                        int delay = random.nextInt(10);
                        aliens.get(alienKilled).setDelay(Duration.seconds(delay));
                        if(alienKilled == alienToChacheToOrb && orbShouldAppear){
                            controller.aliensQueue.get(alienKilled).setImage(controller.slowOrb);
                            orbShouldAppear = false;
                        }
                        aliens.get(alienKilled).play();
                    }
                }
        
                //Verificăm dacă nava spațială a fost ucisă
                int alienThatKilledShip = checkCollisions(controller.shipImageView, controller.aliensQueue, exclude);

                if(alienThatKilledShip != -1){

                    if (controller.aliensQueue.get(alienThatKilledShip).getImage().equals(controller.slowOrb)) {
                        for(TranslateTransition alien : aliens){
                            alien.setDuration(Duration.seconds(alien.getDuration().toSeconds() * 1.05));
                            aliensFadeOff.get(alienThatKilledShip).play();
                        }
                      }else{
                        exclude.add(controller.aliensQueue.get(alienThatKilledShip));
                        aliensFadeOff.get(alienThatKilledShip).play();
    
                        if(hearts == 3){
                            controller.heart3.setVisible(false);
                            hearts--;
                        }else if(hearts == 2){
                            controller.heart2.setVisible(false);
                            hearts--;
                        }else if (hearts == 1){
                            controller.heart1.setVisible(false);
                            hearts--;
                        }else if(hearts == 0){
                            controller.gameStarted = false;
                            controller.shipImageView.setImage(controller.explotion);
                            for(TranslateTransition alien : aliens){
                                alien.pause();
                            }
                            controller.gameOver.setVisible(true);
                            controller.restartBtn.setVisible(true);
                        }
                      }

                };

                //Facem ca extratereștrii uciși să reapară din nou
                for(int i = 0; i < controller.aliensQueue.size(); i++){
                    if(controller.aliensQueue.get(i).getTranslateY() > 391){

                        if(orbShouldAppear == false && i == alienToChacheToOrb){
                            controller.aliensQueue.get(i).setImage(controller.alienImage);
                        }

                        if(i == alienToChacheToOrb && orbShouldAppear){
                            controller.aliensQueue.get(i).setImage(controller.slowOrb);
                            orbShouldAppear = false;
                        }

                        if(exclude.contains(controller.aliensQueue.get(i))){
                            exclude.remove(controller.aliensQueue.get(i));
                            aliensFadeOn.get(i).play();
                        }

                        controller.redLine.setLayoutX(controller.redLine.getLayoutX() - 20);

                        if(controller.redLine.getLayoutX() < -587){
                            controller.gameStarted = false;
                            controller.shipImageView.setImage(controller.explotion);
                            for(TranslateTransition alien : aliens){
                                alien.pause();
                            }
                            controller.gameOver.setVisible(true);
                            controller.restartBtn.setVisible(true);
                        }
                    }
                }

            }           
        }));
        

        checkCollisionsTimeline.setCycleCount(Timeline.INDEFINITE);
        checkCollisionsTimeline.play();

        Timeline checkRestartButton = new Timeline(new KeyFrame(Duration.millis(100), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                //Pornim mișcarea extratereștrilor
                if(controller.gameStarted && stopAliensFromPlayCheck){
                    stopAliensFromPlayCheck = false;
                    for(TranslateTransition alien : aliens){
                        alien.play();
                    }
                }
                
                //Verificăm dacă butonul restart a fost apăsat
                if (controller.restartCheck) {
                    score = 0;
                    controller.scoreLabel.setText(String.valueOf(score));
                    controller.gameOver.setVisible(false);
                    controller.restartBtn.setVisible(false);
                    exclude.clear();
                    controller.restartCheck = false;
                    controller.gameStarted = true;
                    controller.shipImageView.setTranslateX(0);
                    controller.laserImageView.setTranslateX(0);
                    stopAliensFromPlayCheck = true;

                    controller.shipImageView.setImage(controller.spaceShip);
                    controller.redLine.setLayoutX(1);

                    controller.heart1.setVisible(true);
                    controller.heart2.setVisible(true);
                    controller.heart3.setVisible(true);

                    hearts = 3;

                    for(int i = 0; i < aliens.size(); i++){
                        aliens.get(i).stop();
                        controller.aliensQueue.get(i).setTranslateY(0);
                        aliens.get(i).setDuration(Duration.seconds(10));
                    }

                }
            }           
        }));

        checkRestartButton.setCycleCount(Timeline.INDEFINITE);
        checkRestartButton.play();

            scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    if(controller.gameStarted == true){  
                    switch (event.getCode()) {
                        case A:
                        if(controller.shipImageView.getTranslateX() != -250.0){
                            shipTransition.setByX(-50);
                            laserTransition.setByX(-50);
                            shipTransition.play();
                            laserTransition.play();
                        }
                            break;
                        case D:
                        if(controller.shipImageView.getTranslateX() != 250.0){
                        shipTransition.setByX(50);
                        laserTransition.setByX(50);
                        shipTransition.play();
                        laserTransition.play();
                        }
                            break;
                        case S:
                        laserFadeOnTransition.play();
                        laserFadeOffTransition.play();
                        default:
                            break;
                    }
                }
            }
                
            });


        stage.setScene(scene);
        stage.show();
    }
}
