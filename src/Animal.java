public abstract class Animal extends Entity {
    protected double speed;
    protected double direction;
    protected SurvivalStrategy strategy;

    public Animal(double x, double y, double speed) {
        super(x, y);
        this.speed = speed;
        this.direction = 0;
    }

    @Override
    public void update(double dt, World world) {
        if (strategy != null) {
            strategy.act(this, world, dt);
        }
        move(dt);
    }

    protected void move(double dt) {
        x += Math.cos(direction) * speed * dt;
        y += Math.sin(direction) * speed * dt;
    }

    public void setStrategy(SurvivalStrategy strategy) {
        this.strategy = strategy;
    }

    public double getSpeed() {
        return speed;
    }
}
