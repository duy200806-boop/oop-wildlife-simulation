import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class World {
    private final double width;
    private final double height;
    private final int tileSize = 32;
    private final int gridW;
    private final int gridH;
    private final Terrain[][] grid;
    private List<Entity> entities = new ArrayList<>();
    private final EventBus eventBus = new EventBus();

    public EventBus getEventBus() {
        return eventBus;
    }

    public World(double width, double height) {
        this.width = width;
        this.height = height;
        this.gridW = (int) (width / tileSize);
        this.gridH = (int) (height / tileSize);
        this.grid = new Terrain[gridW][gridH];
        generateTerrain();
    }

    private void generateTerrain() {
        Random rng = new Random();
        for (int i = 0; i < gridW; i++) {
            for (int j = 0; j < gridH; j++) {
                grid[i][j] = Terrain.GRASS;
            }
        }

        boolean[][] water = new boolean[gridW][gridH];
        for (int i = 0; i < gridW; i++) {
            for (int j = 0; j < gridH; j++) {
                if (i >= gridW * 0.6 && j >= gridH * 0.55) {
                    water[i][j] = rng.nextDouble() < 0.55;
                }
            }
        }
        water = smoothCa(water, 5);
        overlay(water, Terrain.WATER, false);

        boolean[][] forest = new boolean[gridW][gridH];
        for (int i = 0; i < gridW; i++) {
            for (int j = 0; j < gridH; j++) {
                if (i < gridW * 0.45) {
                    forest[i][j] = rng.nextDouble() < 0.5;
                }
            }
        }
        forest = smoothCa(forest, 5);
        overlay(forest, Terrain.FOREST, true);

        boolean[][] bush = new boolean[gridW][gridH];
        for (int i = 0; i < gridW; i++) {
            for (int j = 0; j < gridH; j++) {
                bush[i][j] = rng.nextDouble() < 0.18;
            }
        }
        bush = smoothCa(bush, 3);
        overlay(bush, Terrain.BUSH, true);

        boolean[][] mud = new boolean[gridW][gridH];
        for (int i = 0; i < gridW; i++) {
            for (int j = 0; j < gridH; j++) {
                mud[i][j] = rng.nextDouble() < 0.08;
            }
        }
        mud = smoothCa(mud, 2);
        overlay(mud, Terrain.MUD, true);
    }

    private boolean[][] smoothCa(boolean[][] g, int iters) {
        for (int iter = 0; iter < iters; iter++) {
            boolean[][] next = new boolean[gridW][gridH];
            for (int i = 0; i < gridW; i++) {
                for (int j = 0; j < gridH; j++) {
                    int n = neighborCount(g, i, j);
                    if (g[i][j]) {
                        next[i][j] = n >= 4;
                    } else {
                        next[i][j] = n >= 5;
                    }
                }
            }
            g = next;
        }
        return g;
    }

    private int neighborCount(boolean[][] g, int x, int y) {
        int n = 0;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) {
                    continue;
                }
                int nx = x + dx;
                int ny = y + dy;
                if (nx >= 0 && nx < gridW && ny >= 0 && ny < gridH && g[nx][ny]) {
                    n++;
                }
            }
        }
        return n;
    }

    private void overlay(boolean[][] g, Terrain t, boolean onlyOnGrass) {
        for (int i = 0; i < gridW; i++) {
            for (int j = 0; j < gridH; j++) {
                if (!g[i][j]) {
                    continue;
                }
                if (onlyOnGrass && grid[i][j] != Terrain.GRASS) {
                    continue;
                }
                grid[i][j] = t;
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

    public void setTileAt(double x, double y, Terrain t) {
        int i = (int) (x / tileSize);
        int j = (int) (y / tileSize);
        if (i >= 0 && i < gridW && j >= 0 && j < gridH) {
            grid[i][j] = t;
        }
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
