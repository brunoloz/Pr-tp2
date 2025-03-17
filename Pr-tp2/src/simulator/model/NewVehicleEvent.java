package simulator.model;

import java.util.ArrayList;
import java.util.List;

public class NewVehicleEvent extends Event {

	private String _id;
    private int _maxSpeed;
    private int _contClass;
    private List<String> _itinerary;

    public NewVehicleEvent(int time, String id, int maxSpeed, int contClass, List<String> itinerary) {
        super(time);
        this._id = id;
        this._maxSpeed = maxSpeed;
        this._contClass = contClass;
        this._itinerary = itinerary;
    }

    @Override
    public void execute(RoadMap roadMap) {
        List<Junction> route = new ArrayList<>();
        for (String juncId : _itinerary) {
            Junction junc = roadMap.getJunction(juncId);
            if (junc == null) {
                throw new IllegalArgumentException("El cruce " + juncId + " no existe en el mapa");
            }
            route.add(junc);
        }
        
        Vehicle vehicle = new Vehicle(_id, _maxSpeed, _contClass, route);
        roadMap.addVehicle(vehicle);
        vehicle.moveToNextRoad();
    }
    
    @Override
    public String toString() {
    	return "New Vehicle '"+ _id +"'";
    }
}
