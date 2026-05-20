public class HunterStrategy implements SurvivalStrategy {
    private final double sightRange;

    public HunterStrategy(double sightRange) {
        this.sightRange = sightRange;
    }

    @Override
    public void act(Animal self, World world, double dt) {
        Entity prey = findNearestPrey(self, world);
        if (prey == null) {
            return;
        }
        double dx = prey.getX() - self.getX();
        double dy = prey.getY() - self.getY();
        self.direction = Math.atan2(dy, dx);
    }

    private Entity findNearestPrey(Animal self, World world) {
        Entity nearest = null;
        double bestDist = sightRange;
        for (Entity e : world.getEntities()) {
            if (e == self || !e.isAlive()) {
                continue;
            }
            if (!(e instanceof Herbivore)) {
                continue;
            }
            double dx = e.getX() - self.getX();
            double dy = e.getY() - self.getY();
            double d = Math.sqrt(dx * dx + dy * dy);
            if (d < bestDist) {
                bestDist = d;
                nearest = e;
            }
        }
        return nearest;
    }
}
