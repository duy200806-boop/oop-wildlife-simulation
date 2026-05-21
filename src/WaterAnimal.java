public abstract class WaterAnimal extends Animal {
    public WaterAnimal(double x, double y, double speed) {
        super(x, y, speed);
    }

    @Override
    protected boolean canEnter(Terrain t) {
        return t == Terrain.WATER;
    }
}
