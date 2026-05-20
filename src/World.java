import java.util.ArrayList;
import java.util.List;

public class World {
    private final double width;
    private final double height;
    private final int tileSize = 32;
    private final int gridW;
    private final int gridH;
    private final Terrain[][] grid;
    private List<Entity> entities = new ArrayList<>();

    public World(double width, double height) {
        this.width = width;
        this.height = height;
        this.gridW = (int) (width / tileSize);
        this.gridH = (int) (height / tileSize);
        this.grid = new Terrain[gridW][gridH];
        for (int i = 0; i < gridW; i++) {
            for (int j = 0; j < gridH; j++) {
                grid[i][j] = Terrain.GRASS;
            }
        }
        for (int i = 5; i < 12; i++) {
            for (int j = 8; j < 14; j++) {
                grid[i][j] = Terrain.MUD;
            }
        }
        for (int i = 25; i < 32; i++) {
            for (int j = 3; j < 8; j++) {
                grid[i][j] = Terrain.BUSH;
            }
        }
        for (int i = 20; i < 25; i++) {
            for (int j = 15; j < 20; j++) {
                grid[i][j] = Terrain.BUSH;
            }
        }
    }

    public Terrain getTerrainAt(double x, double y) {
        int i = (int) (x / tileSize);
        int j = (int) (y / tileSize);
        if (i < 0 || i >= gridW || j < 0 || j >= gridH) {
            return Terrain.GRASS;
        }
        return grid[i][j];
    }

    public int getTileSize() {
        return tileSize;
    }

    public int getGridW() {
        return gridW;
    }

    public int getGridH() {
        return gridH;
    }

    public Terrain getTile(int i, int j) {
        return grid[i][j];
    }

    public void add(Entity e) {
        entities.add(e);
    }

    public void tick(double dt) {
        for (Entity e : entities) {
            if (e.isAlive()) {
                e.update(dt, this);
                if (e.x < 0) e.x = 0;
                if (e.x > width) e.x = width;
                if (e.y < 0) e.y = 0;
                if (e.y > height) e.y = height;
            }
        }
    }

    public List<Entity> getEntities() {
        return entities;
    }
}
