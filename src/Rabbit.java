public class Rabbit extends Herbivore {
    public Rabbit(double x, double y) {
        super(x, y, 50);
        strategy = new ScaredStrategy(200);
    }

    @Override
    protected Herbivore createChild(double x, double y) {
        return new Rabbit(x, y);
    }
}
