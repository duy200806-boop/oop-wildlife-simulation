public abstract class Herbivore extends Animal {
    protected double breedTimer = 0;
    protected double breedInterval = 20.0;

    public Herbivore(double x, double y, double speed) {
        super(x, y, speed);
    }

    @Override
    protected boolean canEat(Entity e) {
        return e instanceof Plant;
    }

    @Override
    public void update(double dt, World world) {
        super.update(dt, world);
        if (!alive) {
            return;
        }
        if (world.getSeason() == Season.BREEDING && hunger < 0.3) {
            breedTimer += dt;
            if (breedTimer >= breedInterval) {
                breedTimer = 0;
                double ox = (Math.random() - 0.5) * 20;
                double oy = (Math.random() - 0.5) * 20;
                Herbivore child = createChild(x + ox, y + oy);
                if (child != null) {
                    world.add(child);
                }
            }
        }
    }

    protected abstract Herbivore createChild(double x, double y);
}
