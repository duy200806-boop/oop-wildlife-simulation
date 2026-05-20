public class Wolf extends Animal {
    public Wolf(double x, double y) {
        super(x, y, 70);
        strategy = new HunterStrategy(200);
    }

    @Override
    protected boolean canEat(Entity e) {
        return e instanceof Rabbit;
    }
}
