import javafx.application.Application;
import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
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
    private Camera camera;
    private StatisticsCollector stats;

    @Override
    public void start(Stage stage) {
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        double winW = bounds.getWidth();
        double winH = bounds.getHeight();
        double worldW = winW;
        double worldH = winH - 50;

        world = new World(worldW, worldH);
        camera = new Camera(worldW, worldH);

        for (int i = 0; i < 30; i++) {
            double gx, gy;
            int tries = 0;
            do {
                gx = Math.random() * worldW;
                gy = Math.random() * worldH;
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
        stats = new StatisticsCollector(world.getEventBus());

        canvas = new Canvas(worldW, worldH);

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

        Button exportBtn = new Button("Export CSV");
        exportBtn.setOnAction(e -> {
            String filename = "stats-" + System.currentTimeMillis() + ".csv";
            try {
                stats.exportCsv(filename);
                System.out.println("Stats exported to " + filename);
            } catch (Exception ex) {
                System.out.println("Export failed: " + ex.getMessage());
            }
        });

        HBox controls = new HBox(8, toggleBtn, zoomInBtn, zoomOutBtn, zoomResetBtn, exportBtn);
        controls.setPadding(new Insets(5));

        BorderPane root = new BorderPane();
        root.setTop(controls);
        root.setCenter(canvas);
        Scene scene = new Scene(root, winW, winH);

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
        stage.setMaximized(true);
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
                stats.tick(dt, world);
                renderer.render(canvas.getGraphicsContext2D(), world, camera);
            }
        };
        timer.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
