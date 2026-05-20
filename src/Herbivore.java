public abstract class Herbivore extends Animal {
    public Herbivore(double x, double y, double speed) {
        super(x, y, speed);
    }

    @Override
    protected boolean canEat(Entity e) {
        return e instanceof Plant;
    }
}
