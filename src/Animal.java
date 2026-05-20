public abstract class Animal extends Entity {
    protected double speed;
    protected double direction;

    public Animal(double x, double y, double speed) {
        super(x, y);
        this.speed = speed;
        this.direction = 0;
    }

    protected void move(double dt) {
        x += Math.cos(direction) * speed * dt;
        y += Math.sin(direction) * speed * dt;
    }

    public double getSpeed() {
        return speed;
    }
}
