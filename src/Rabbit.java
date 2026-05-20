public class Rabbit extends Animal {
    private double directionTimer = 0;

    public Rabbit(double x, double y) {
        super(x, y, 50);
        direction = Math.random() * Math.PI * 2;
    }

    @Override
    public void update(double dt) {
        directionTimer += dt;
        if (directionTimer > 2.0) {
            directionTimer = 0;
            direction = Math.random() * Math.PI * 2;
        }
        move(dt);
    }
}
