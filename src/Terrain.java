public enum Terrain {
    GRASS(1.0),
    MUD(0.4),
    FOREST(0.8),
    WATER(0.0);

    public final double speedFactor;

    Terrain(double speedFactor) {
        this.speedFactor = speedFactor;
    }
}
