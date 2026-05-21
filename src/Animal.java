public abstract class Animal extends Entity {
    protected double speed;
    protected double direction;
    protected SurvivalStrategy strategy;

    protected double hunger = 0;
    protected double hungerRate = 0.01;
    protected double maxHunger = 1.0;

    protected double thirst = 0;
    protected double thirstRate = 0.013;
    protected double maxThirst = 1.0;

    private final SurvivalStrategy aggressiveStrategy = new AggressiveStrategy();
    private final SurvivalStrategy thirstyStrategy = new ThirstyStrategy();

    public Animal(double x, double y, double speed) {
        super(x, y);
        this.speed = speed;
        this.direction = 0;
    }

    @Override
    public void update(double dt, World world) {
        hunger += hungerRate * dt;
        thirst += thirstRate * dt;
        if (hunger >= maxHunger || thirst >= maxThirst) {
            alive = false;
            world.getEventBus().publish(EventType.DEATH);
            return;
        }
        SurvivalStrategy brain = pickBrain();
        if (brain != null) {
            brain.act(this, world, dt);
        }
        applySeparation(world);
        move(dt, world);
        tryEat(world);
        tryDrink(world);
    }

    private void applySeparation(World world) {
        if (this instanceof ApexEntity) {
            return;
        }
        double pushX = 0, pushY = 0;
        double range = 35;
        for (Entity e : world.getEntities()) {
            if (e == this || !e.isAlive() || !(e instanceof Animal)) {
                continue;
            }
            boolean isBig = e instanceof ApexEntity;
            boolean isPred = (e instanceof Carnivore) && (this instanceof Herbivore);
            if (!isBig && !isPred) {
                continue;
            }
            double dx = x - e.getX();
            double dy = y - e.getY();
            double d2 = dx * dx + dy * dy;
            if (d2 > range * range || d2 < 0.01) {
                continue;
            }
            double d = Math.sqrt(d2);
            double strength = (range - d) / range;
            pushX += (dx / d) * strength;
            pushY += (dy / d) * strength;
        }
        if (pushX != 0 || pushY != 0) {
            double dx = Math.cos(direction) + pushX;
            double dy = Math.sin(direction) + pushY;
            direction = Math.atan2(dy, dx);
        }
    }

    private SurvivalStrategy pickBrain() {
        if (thirst > 0.7) return thirstyStrategy;
        if (hunger > 0.7) return aggressiveStrategy;
        return strategy;
    }

    protected void tryDrink(World world) {
        Terrain[] neighbors = {
                world.getTerrainAt(x, y - 12),
                world.getTerrainAt(x, y + 12),
                world.getTerrainAt(x + 12, y),
                world.getTerrainAt(x - 12, y)
        };
        for (Terrain t : neighbors) {
            if (t == Terrain.WATER) {
                thirst = Math.max(0, thirst - 0.6);
                return;
            }
        }
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
        double terrainFactor = world.getTerrainAt(x, y).speedFactor;
        double seasonFactor = world.getSeason() == Season.DROUGHT ? 0.6 : 1.0;
        double factor = terrainFactor * seasonFactor;
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
