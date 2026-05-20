import java.util.ArrayList;
import java.util.List;

public class World {
    private final double width;
    private final double height;
    private List<Entity> entities = new ArrayList<>();

    public World(double width, double height) {
        this.width = width;
        this.height = height;
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
