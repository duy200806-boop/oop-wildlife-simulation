public abstract class Animal extends Entity {
    protected double speed;
    protected double direction;
    protected SurvivalStrategy strategy;

    protected double hunger = 0;
    protected double hungerRate = 0.01;
    protected double maxHunger = 1.0;
    private SurvivalStrategy normalStrategy;
    private boolean aggressive = false;

    public Animal(double x, double y, double speed) {
        super(x, y);
        this.speed = speed;
        this.direction = 0;
    }

    @Override
    public void update(double dt, World world) {
        hunger += hungerRate * dt;
        if (hunger >= maxHunger) {
            alive = false;
            world.getEventBus().publish(EventType.DEATH);
            return;
        }
        if (hunger > 0.7 && !aggressive) {
            normalStrategy = strategy;
            strategy = new AggressiveStrategy();
            aggressive = true;
        } else if (hunger < 0.3 && aggressive) {
            strategy = normalStrategy;
            aggressive = false;
        }
        if (strategy != null) {
            strategy.act(this, world, dt);
        }
        move(dt, world);
        tryEat(world);
    }

    protected void tryEat(World world) {
        for (Entity e : world.getEntities()) {
            if (e == this || !e.isAlive()) {
                continue;
            }
            if (!canEat(e)) {
                continue;
            }
            double dx = e.getX() - x;
            double dy = e.getY() - y;
            if (Math.sqrt(dx * dx + dy * dy) < 10) {
                eat(e);
                world.getEventBus().publish(this instanceof Carnivore ? EventType.ATTACK : EventType.EAT);
                return;
            }
        }
    }

    protected boolean canEat(Entity e) {
        return false;
    }

    protected void eat(Entity food) {
        food.alive = false;
        hunger = Math.max(0, hunger - 0.5);
    }

    protected void move(double dt, World world) {
        double factor = world.getTerrainAt(x, y).speedFactor;
        double newX = x + Math.cos(direction) * speed * factor * dt;
        double newY = y + Math.sin(direction) * speed * factor * dt;
        if (canEnter(world.getTerrainAt(newX, newY))) {
            x = newX;
            y = newY;
        }
    }

    protected boolean canEnter(Terrain t) {
        return t != Terrain.ROCK;
    }

    public void setStrategy(SurvivalStrategy strategy) {
        this.strategy = strategy;
    }

    public double getSpeed() {
        return speed;
    }

    public double getHunger() {
        return hunger;
    }
}
