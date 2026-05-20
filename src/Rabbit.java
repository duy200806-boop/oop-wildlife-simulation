public class Rabbit extends Animal {
    public Rabbit(double x, double y) {
        super(x, y, 50);
    }

    @Override
    public void update(double dt) {
        move(dt);
    }
}
