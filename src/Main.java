import javafx.application.Application;
import javafx.animation.AnimationTimer;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class Main extends Application {
    private World world;
    private Canvas canvas;

    @Override
    public void start(Stage stage) {
        world = new World();
        world.add(new Rabbit(400, 300));
        world.add(new Rabbit(200, 200));

        canvas = new Canvas(800, 600);
        Pane root = new Pane(canvas);
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
                render();
            }
        };
        timer.start();
    }

    private void render() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.LIGHTGREEN);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for (Entity e : world.getEntities()) {
            if (e instanceof Rabbit) {
                gc.setFill(Color.WHITE);
                gc.fillOval(e.getX() - 5, e.getY() - 5, 10, 10);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
