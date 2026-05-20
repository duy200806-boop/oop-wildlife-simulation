import javafx.application.Application;
import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class Main extends Application {
    private World world;
    private Canvas canvas;
    private Renderer renderer = new BasicRenderer();
    private Camera camera = new Camera(1280, 720);

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
        world.add(new Elephant(700, 400));

        new AudioSystem(world.getEventBus());

        canvas = new Canvas(1280, 720);

        Button toggleBtn = new Button("Renderer: Basic");
        toggleBtn.setOnAction(e -> {
            if (renderer instanceof BasicRenderer) {
                renderer = new SpriteRenderer();
                toggleBtn.setText("Renderer: Sprite");
            } else {
                renderer = new BasicRenderer();
                toggleBtn.setText("Renderer: Basic");
            }
        });

        Button zoomInBtn = new Button("Zoom +");
        zoomInBtn.setOnAction(e -> camera.zoomIn());
        Button zoomOutBtn = new Button("Zoom -");
        zoomOutBtn.setOnAction(e -> camera.zoomOut());
        Button zoomResetBtn = new Button("Reset");
        zoomResetBtn.setOnAction(e -> camera.setZoom(1.0));

        HBox controls = new HBox(8, toggleBtn, zoomInBtn, zoomOutBtn, zoomResetBtn);
        controls.setPadding(new Insets(5));

        BorderPane root = new BorderPane();
        root.setTop(controls);
        root.setCenter(canvas);
        Scene scene = new Scene(root, 1280, 770);

        canvas.setOnMouseClicked(e -> {
            double[] w = camera.screenToWorld(e.getX(), e.getY());
            double cx = w[0];
            double cy = w[1];
            if (e.getButton() == MouseButton.SECONDARY) {
                world.setTileAt(cx, cy, Terrain.ROCK);
            } else if (world.getTerrainAt(cx, cy) != Terrain.WATER) {
                world.add(new Grass(cx, cy));
            }
        });

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
                renderer.render(canvas.getGraphicsContext2D(), world, camera);
            }
        };
        timer.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
