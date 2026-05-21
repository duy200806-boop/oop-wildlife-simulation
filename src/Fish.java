public class Fish extends WaterAnimal {
    public Fish(double x, double y) {
        super(x, y, 30);
        strategy = new PassiveStrategy();
    }
}
