package simulator.model;

public abstract class NewRoadEvent extends Event {
    protected String id;
    protected String srcJunc;
    protected String destJunc;
    protected int length;
    protected int co2Limit;
    protected int maxSpeed;
    protected Weather weather;

    public NewRoadEvent(int time, String id, String srcJunc, String destJunc, int length, int co2Limit, int maxSpeed, Weather weather) {
        super(time);
        this.id = id;
        this.srcJunc = srcJunc;
        this.destJunc = destJunc;
        this.length = length;
        this.co2Limit = co2Limit;
        this.maxSpeed = maxSpeed;
        this.weather = weather;
    }

    @Override
    public void execute(RoadMap roadMap) {
        Junction src = roadMap.getJunction(srcJunc);
        Junction dest = roadMap.getJunction(destJunc);
        
        if (src == null || dest == null) {
            throw new IllegalArgumentException("Los cruces de la carretera no existen en el mapa");
        }
        
        Road road = createRoad(id, src, dest, maxSpeed, co2Limit, length, weather);
        roadMap.addRoad(road);
    }

    protected abstract Road createRoad(String id, Junction src, Junction dest, int maxSpeed, int co2Limit, int length, Weather weather);

}