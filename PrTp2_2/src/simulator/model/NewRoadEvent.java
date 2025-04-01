package simulator.model;

public abstract class NewRoadEvent extends Event {
    protected String _id;
    protected String _srcJunc;
    protected String _destJunc;
    protected int _length;
    protected int _co2Limit;
    protected int _maxSpeed;
    protected Weather _weather;

    public NewRoadEvent(int time, String id, String srcJunc, String destJunc, int length, int co2Limit, int maxSpeed, Weather weather) {
        super(time);
        this._id = id;
        this._srcJunc = srcJunc;
        this._destJunc = destJunc;
        this._length = length;
        this._co2Limit = co2Limit;
        this._maxSpeed = maxSpeed;
        this._weather = weather;
    }

    @Override
    public void execute(RoadMap roadMap) {
        Junction src = roadMap.getJunction(_srcJunc);
        Junction dest = roadMap.getJunction(_destJunc);
        
        if (src == null || dest == null) {
            throw new IllegalArgumentException("Los cruces de la carretera no existen en el mapa");
        }
        
        Road road = createRoad(_id, src, dest, _maxSpeed, _co2Limit, _length, _weather);
        roadMap.addRoad(road);
    }

    protected abstract Road createRoad(String id, Junction src, Junction dest, int maxSpeed, int co2Limit, int length, Weather weather);
    
    @Override
    public String toString() {
    	return "New Road '"+ _id +"'";
    }

}