import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class BasicRenderer implements Renderer {

    @Override
    public void render(GraphicsContext gc, World world) {
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
    }

    private void drawEntity(GraphicsContext gc, Entity e) {
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
        } else if (e instanceof Elephant) {
            gc.setFill(Color.DARKGRAY);
            gc.fillOval(e.getX() - 12, e.getY() - 12, 24, 24);
        }
    }

    private Color terrainColor(Terrain t) {
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
            case ROCK:
                return Color.GRAY;
            default:
                return Color.GRAY;
        }
    }
}
