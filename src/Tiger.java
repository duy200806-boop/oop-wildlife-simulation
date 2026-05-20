public class Tiger extends Carnivore {
    public Tiger(double x, double y) {
        super(x, y, 80);
        strategy = new HunterStrategy(250);
    }
}
