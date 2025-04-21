package simulator.factories;

import simulator.model.Event;
import simulator.model.NewInterCityRoadEvent;
import simulator.model.Weather;

public class NewInterCityRoadEventBuilder extends NewRoadEventBuilder {

	public NewInterCityRoadEventBuilder() {
		super("new_inter_city_road", "hola");
	}

	@Override
	protected Event create_event(int time, String id, String srcJunc, String destJunc, int length, int co2Limit,
			int maxSpeed, Weather weather) {
		return new NewInterCityRoadEvent(time, id, srcJunc, destJunc, length, co2Limit, maxSpeed, weather);
	}


	

}
