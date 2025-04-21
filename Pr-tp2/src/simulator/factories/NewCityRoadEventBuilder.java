package simulator.factories;

import simulator.model.Event;
import simulator.model.NewCityRoadEvent;
import simulator.model.Weather;

public class NewCityRoadEventBuilder extends NewRoadEventBuilder{

	public NewCityRoadEventBuilder() {
		super("new_city_road", "hola");
		
	}

	@Override
	protected Event create_event(int time, String id, String srcJunc, String destJunc, int length, int co2Limit,
			int maxSpeed, Weather weather) {
		return new NewCityRoadEvent(time, id, srcJunc, destJunc, length, co2Limit, maxSpeed, weather);
	}




}
