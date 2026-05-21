public abstract class Carnivore extends Animal {
    protected double stamina = 1.0;
    protected double maxStamina = 1.0;
    protected double sprintMultiplier = 1.7;

    public Carnivore(double x, double y, double speed) {
        super(x, y, speed);
    }

    @Override
    protected boolean canEat(Entity e) {
        return e instanceof Herbivore;
    }

    @Override
    protected boolean canEnter(Terrain t) {
        return super.canEnter(t) && t != Terrain.BUSH;
    }

    @Override
    protected void move(double dt, World world) {
        double sprintFactor = 1.0;
        if (sprinting && stamina > 0) {
            sprintFactor = sprintMultiplier;
            stamina = Math.max(0, stamina - dt * 0.3);
        } else {
            stamina = Math.min(maxStamina, stamina + dt * 0.2);
        }
        double terrainFactor = world.getTerrainAt(x, y).speedFactor;
        double seasonFactor = world.getSeason() == Season.DROUGHT ? 0.6 : 1.0;
        double factor = terrainFactor * seasonFactor * sprintFactor;
        double newX = x + Math.cos(direction) * speed * factor * dt;
        double newY = y + Math.sin(direction) * speed * factor * dt;
        if (canEnter(world.getTerrainAt(newX, newY))) {
            x = newX;
            y = newY;
        }
    }

    public double getStamina() {
        return stamina;
    }
}
