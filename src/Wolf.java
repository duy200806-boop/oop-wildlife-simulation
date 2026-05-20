public class Wolf extends Carnivore {
    public Wolf(double x, double y) {
        super(x, y, 70);
        strategy = new HunterStrategy(200);
    }
}
