import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SpriteRenderer implements Renderer {
    private static final String SPRITE_PATH = "/resources/sprites/";

    private final Image[] grass;
    private final Image[] fruitTree;
    private final Image[] rabbit;
    private final Image[] deer;
    private final Image[] wolf;
    private final Image[] tiger;
    private final Image[] elephant;
    private final Image[] fish;
    private final Image[] duck;

    private double frameTime = 0;
    private int currentFrame = 0;

    public SpriteRenderer() {
        grass = loadFrames("grass");
        fruitTree = loadFrames("fruit_tree");
        rabbit = loadFrames("rabbit");
        deer = loadFrames("deer");
        wolf = loadFrames("wolf");
        tiger = loadFrames("tiger");
        elephant = loadFrames("elephant");
        fish = loadFrames("fish");
        duck = loadFrames("duck");
    }

    private Image[] loadFrames(String prefix) {
        List<Image> list = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            Image img = loadOne(SPRITE_PATH + prefix + "_" + i + ".png");
            if (img == null) break;
            list.add(img);
        }
        if (list.isEmpty()) {
            Image img = loadOne(SPRITE_PATH + prefix + ".png");
            if (img != null) list.add(img);
        }
        return list.toArray(new Image[0]);
    }

    private Image loadOne(String path) {
        try {
            InputStream is = getClass().getResourceAsStream(path);
            if (is == null) return null;
            return new Image(is);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void render(GraphicsContext gc, World world, Camera camera) {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

        frameTime += 1.0 / 60;
        if (frameTime > 0.15) {
            frameTime = 0;
            currentFrame++;
        }

        gc.save();
        camera.apply(gc);

        int ts = world.getTileSize();
        for (int i = 0; i < world.getGridW(); i++) {
            for (int j = 0; j < world.getGridH(); j++) {
                gc.setFill(terrainColor(world.getTile(i, j)));
                gc.fillRect(i * ts, j * ts, ts, ts);
            }
        }

        for (Entity e : world.getEntities()) {
            if (!e.isAlive()) continue;
            Image img = pickFrame(framesFor(e));
            if (img != null) {
                double size = sizeFor(e);
                gc.drawImage(img, e.getX() - size / 2, e.getY() - size / 2, size, size);
            } else {
                drawProcedural(gc, e);
            }
        }

        gc.restore();
    }

    private Image[] framesFor(Entity e) {
        if (e instanceof Grass) return grass;
        if (e instanceof FruitTree) return fruitTree;
        if (e instanceof Rabbit) return rabbit;
        if (e instanceof Deer) return deer;
        if (e instanceof Wolf) return wolf;
        if (e instanceof Tiger) return tiger;
        if (e instanceof Elephant) return elephant;
        if (e instanceof Fish) return fish;
        if (e instanceof Duck) return duck;
        return null;
    }

    private Image pickFrame(Image[] frames) {
        if (frames == null || frames.length == 0) return null;
        return frames[Math.floorMod(currentFrame, frames.length)];
    }

    private double sizeFor(Entity e) {
        if (e instanceof Grass) return 12;
        if (e instanceof FruitTree) return 28;
        if (e instanceof Rabbit) return 18;
        if (e instanceof Deer) return 26;
        if (e instanceof Wolf) return 24;
        if (e instanceof Tiger) return 30;
        if (e instanceof Elephant) return 40;
        if (e instanceof Fish) return 14;
        if (e instanceof Duck) return 16;
        return 12;
    }

    private void drawProcedural(GraphicsContext gc, Entity e) {
        double x = e.getX();
        double y = e.getY();

        if (e instanceof Grass) {
            gc.setFill(Color.LIMEGREEN);
            gc.fillOval(x - 4, y - 4, 8, 8);
            gc.setStroke(Color.DARKGREEN);
            gc.strokeOval(x - 4, y - 4, 8, 8);
        } else if (e instanceof FruitTree) {
            gc.setFill(Color.SADDLEBROWN);
            gc.fillRect(x - 2, y, 4, 12);
            gc.setFill(Color.FORESTGREEN);
            gc.fillOval(x - 10, y - 12, 20, 20);
            gc.setFill(Color.RED);
            gc.fillOval(x - 5, y - 8, 3, 3);
            gc.fillOval(x + 2, y - 6, 3, 3);
            gc.fillOval(x - 3, y - 3, 3, 3);
        } else if (e instanceof Fish) {
            gc.setFill(Color.SILVER);
            gc.fillOval(x - 5, y - 2, 10, 5);
            gc.setFill(Color.DARKGRAY);
            gc.fillPolygon(new double[] { x + 5, x + 8, x + 8 }, new double[] { y, y - 3, y + 3 }, 3);
            gc.setFill(Color.BLACK);
            gc.fillOval(x - 3, y - 1, 1.5, 1.5);
        } else if (e instanceof Duck) {
            gc.setFill(Color.WHITE);
            gc.fillOval(x - 6, y - 4, 12, 8);
            gc.setFill(Color.YELLOW);
            gc.fillOval(x + 3, y - 6, 5, 5);
            gc.setFill(Color.ORANGE);
            gc.fillRect(x + 6, y - 5, 3, 2);
        } else if (e instanceof Rabbit) {
            drawAnimal(gc, x, y, 12, Color.WHITESMOKE, Color.GRAY);
            gc.setFill(Color.BLACK);
            gc.fillOval(x - 2, y - 3, 2, 2);
            gc.fillOval(x + 1, y - 3, 2, 2);
            gc.setFill(Color.WHITESMOKE);
            gc.fillOval(x - 4, y - 9, 3, 6);
            gc.fillOval(x + 1, y - 9, 3, 6);
        } else if (e instanceof Deer) {
            drawAnimal(gc, x, y, 18, Color.PERU, Color.SADDLEBROWN);
            gc.setFill(Color.BLACK);
            gc.fillOval(x - 3, y - 4, 2, 2);
            gc.fillOval(x + 1, y - 4, 2, 2);
        } else if (e instanceof Wolf) {
            drawAnimal(gc, x, y, 16, Color.INDIANRED, Color.DARKRED);
            gc.setFill(Color.YELLOW);
            gc.fillOval(x - 3, y - 3, 2, 2);
            gc.fillOval(x + 1, y - 3, 2, 2);
        } else if (e instanceof Tiger) {
            drawAnimal(gc, x, y, 20, Color.ORANGE, Color.DARKORANGE);
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(1.5);
            gc.strokeLine(x - 8, y - 4, x + 8, y - 4);
            gc.strokeLine(x - 8, y + 2, x + 8, y + 2);
        } else if (e instanceof Elephant) {
            drawAnimal(gc, x, y, 28, Color.LIGHTGRAY, Color.DIMGRAY);
            gc.setFill(Color.DIMGRAY);
            gc.fillRect(x - 2, y + 6, 4, 10);
        }
    }

    private void drawAnimal(GraphicsContext gc, double x, double y, double size, Color fill, Color outline) {
        gc.setFill(fill);
        gc.fillOval(x - size / 2, y - size / 2, size, size);
        gc.setStroke(outline);
        gc.setLineWidth(2);
        gc.strokeOval(x - size / 2, y - size / 2, size, size);
    }

    private Color terrainColor(Terrain t) {
        switch (t) {
            case GRASS:
                return Color.web("#7cbc6c");
            case MUD:
                return Color.web("#6b4423");
            case FOREST:
                return Color.web("#2d5016");
            case BUSH:
                return Color.web("#3e6b1e");
            case WATER:
                return Color.web("#3498db");
            case ROCK:
                return Color.web("#7f7f7f");
            default:
                return Color.GRAY;
        }
    }
}
