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
        world = new World(1280, 720);
        for (int i = 0; i < 30; i++) {
            double gx, gy;
            int tries = 0;
            do {
                gx = Math.random() * 1280;
                gy = Math.random() * 720;
                tries++;
            } while (world.getTerrainAt(gx, gy) == Terrain.WATER && tries < 10);
            world.add(new Grass(gx, gy));
        }
        world.add(new Rabbit(640, 360));
        world.add(new Rabbit(300, 200));
        world.add(new Rabbit(900, 200));
        world.add(new Rabbit(500, 300));
        world.add(new Deer(800, 200));
        world.add(new Deer(400, 500));
        world.add(new Wolf(100, 100));
        world.add(new Tiger(1100, 100));

        canvas = new Canvas(1280, 720);
        Pane root = new Pane(canvas);
        Scene scene = new Scene(root, 1280, 720);

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
        int ts = world.getTileSize();
        for (int i = 0; i < world.getGridW(); i++) {
            for (int j = 0; j < world.getGridH(); j++) {
                gc.setFill(colorOf(world.getTile(i, j)));
                gc.fillRect(i * ts, j * ts, ts, ts);
            }
        }

        for (Entity e : world.getEntities()) {
            if (!e.isAlive()) {
                continue;
            }
            if (e instanceof Grass) {
                gc.setFill(Color.DARKGREEN);
                gc.fillRect(e.getX() - 3, e.getY() - 3, 6, 6);
            } else if (e instanceof Rabbit) {
                gc.setFill(Color.WHITE);
                gc.fillOval(e.getX() - 5, e.getY() - 5, 10, 10);
            } else if (e instanceof Deer) {
                gc.setFill(Color.SADDLEBROWN);
                gc.fillOval(e.getX() - 8, e.getY() - 8, 16, 16);
            } else if (e instanceof Wolf) {
                gc.setFill(Color.DARKRED);
                gc.fillOval(e.getX() - 7, e.getY() - 7, 14, 14);
            } else if (e instanceof Tiger) {
                gc.setFill(Color.ORANGE);
                gc.fillOval(e.getX() - 9, e.getY() - 9, 18, 18);
            }
        }
    }

    private Color colorOf(Terrain t) {
        switch (t) {
            case GRASS:
                return Color.LIGHTGREEN;
            case MUD:
                return Color.SADDLEBROWN;
            case FOREST:
                return Color.DARKGREEN;
            case BUSH:
                return Color.DARKOLIVEGREEN;
            case WATER:
                return Color.LIGHTBLUE;
            default:
                return Color.GRAY;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
