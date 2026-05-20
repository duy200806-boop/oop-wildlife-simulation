import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class SpriteRenderer implements Renderer {

    @Override
    public void render(GraphicsContext gc, World world, Camera camera) {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

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
            if (!e.isAlive()) {
                continue;
            }
            drawEntity(gc, e);
        }

        gc.restore();
    }

    private void drawEntity(GraphicsContext gc, Entity e) {
        double x = e.getX();
        double y = e.getY();

        if (e instanceof Grass) {
            gc.setFill(Color.LIMEGREEN);
            gc.fillOval(x - 4, y - 4, 8, 8);
            gc.setStroke(Color.DARKGREEN);
            gc.strokeOval(x - 4, y - 4, 8, 8);
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
