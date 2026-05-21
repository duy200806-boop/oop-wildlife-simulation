public class Duck extends WaterAnimal {
    public Duck(double x, double y) {
        super(x, y, 25);
        strategy = new PassiveStrategy();
    }
}
