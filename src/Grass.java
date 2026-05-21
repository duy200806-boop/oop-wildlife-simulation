public class Grass extends Plant {
    public Grass(double x, double y) {
        super(x, y);
        reproduceInterval = 12.0;
        reproduceChance = 0.30;
    }

    @Override
    protected Plant createChild(double x, double y) {
        return new Grass(x, y);
    }
}
