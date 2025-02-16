package simulator.model;

public class NewInterCityRoadEvent extends NewRoadEvent {

	public NewInterCityRoadEvent(int time, String id, String srcJunc, String destJunc, int length, int co2Limit, int maxSpeed, Weather weather) {
        super(time, id, srcJunc, destJunc, length, co2Limit, maxSpeed, weather);
    }

    @Override
    protected Road createRoad(String id, Junction src, Junction dest, int maxSpeed, int co2Limit, int length, Weather weather) {
        return new InterCityRoad(id, src, dest, maxSpeed, co2Limit, length, weather);
    }

}
