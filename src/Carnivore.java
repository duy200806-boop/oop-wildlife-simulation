public abstract class Carnivore extends Animal {
    public Carnivore(double x, double y, double speed) {
        super(x, y, speed);
    }

    @Override
    protected boolean canEat(Entity e) {
        return e instanceof Herbivore;
    }

    @Override
    protected boolean canEnter(Terrain t) {
        return super.canEnter(t) && t != Terrain.BUSH;
    }
}
