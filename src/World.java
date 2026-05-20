import java.util.ArrayList;
import java.util.List;

public class World {
    private List<Entity> entities = new ArrayList<>();

    public void add(Entity e) {
        entities.add(e);
    }

    public void tick(double dt) {
        for (Entity e : entities) {
            if (e.isAlive()) {
                e.update(dt);
            }
        }
    }

    public List<Entity> getEntities() {
        return entities;
    }
}
