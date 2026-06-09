package simulation.model;

public abstract class Plant extends Entity {
    protected double reproduceTimer = 0;
    protected double reproduceInterval = 20.0;
    protected double reproduceChance = 0.15;

    public Plant(double x, double y) {
        super(x, y);
    }

    @Override
    public void update(double dt, World world) {
        double intervalMult = world.getSeason() == Season.DROUGHT ? 3.0 : 1.0;
        double chanceMult = world.getSeason() == Season.DROUGHT ? 0.5 : 1.0;
        reproduceTimer += dt;
        if (reproduceTimer >= reproduceInterval * intervalMult) {
            reproduceTimer = 0;
            if (Math.random() < reproduceChance * chanceMult) {
                tryReproduce(world);
            }
        }
    }

    private void tryReproduce(World world) {
        double angle = Math.random() * Math.PI * 2;
        double dist = 20 + Math.random() * 30;
        double nx = x + Math.cos(angle) * dist;
        double ny = y + Math.sin(angle) * dist;
        Terrain t = world.getTerrainAt(nx, ny);
        if (t == Terrain.WATER || t == Terrain.ROCK) {
            return;
        }
        Plant child = createChild(nx, ny);
        if (child != null) {
            world.add(child);
        }
    }

    protected abstract Plant createChild(double x, double y);
}
