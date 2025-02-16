package simulator.model;

import java.util.ArrayList;
import java.util.List;

public class NewVehicleEvent extends Event {

	private String id;
    private int maxSpeed;
    private int contClass;
    private List<String> itinerary;

    public NewVehicleEvent(int time, String id, int maxSpeed, int contClass, List<String> itinerary) {
        super(time);
        this.id = id;
        this.maxSpeed = maxSpeed;
        this.contClass = contClass;
        this.itinerary = itinerary;
    }

    @Override
    public void execute(RoadMap roadMap) {
        List<Junction> route = new ArrayList<>();
        for (String juncId : itinerary) {
            Junction junc = roadMap.getJunction(juncId);
            if (junc == null) {
                throw new IllegalArgumentException("El cruce " + juncId + " no existe en el mapa");
            }
            route.add(junc);
        }
        
        Vehicle vehicle = new Vehicle(id, maxSpeed, contClass, route);
        roadMap.addVehicle(vehicle);
        vehicle.moveToNextRoad();
    }
}
