import javafx.application.Application;
import javafx.animation.AnimationTimer;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

public class Main extends Application {
    private World world;

    @Override
    public void start(Stage stage) {
        world = new World();
        world.add(new Rabbit(400, 300));
        world.add(new Rabbit(200, 200));

        StackPane root = new StackPane();
        Scene scene = new Scene(root, 800, 600);

        stage.setTitle("Eco Sim");
        stage.setScene(scene);
        stage.show();

        AnimationTimer timer = new AnimationTimer() {
            long last = 0;

            @Override
            public void handle(long now) {
                if (last == 0) {
                    last = now;
                    return;
                }
                double dt = (now - last) / 1e9;
                last = now;
                world.tick(dt);
            }
        };
        timer.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
