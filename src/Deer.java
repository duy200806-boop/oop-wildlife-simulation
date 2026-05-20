public class Deer extends Herbivore {
    public Deer(double x, double y) {
        super(x, y, 40);
        strategy = new ScaredStrategy(180);
    }
}
