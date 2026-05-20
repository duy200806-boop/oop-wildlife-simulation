public class Rabbit extends Herbivore {
    public Rabbit(double x, double y) {
        super(x, y, 50);
        strategy = new ScaredStrategy(200);
    }
}
