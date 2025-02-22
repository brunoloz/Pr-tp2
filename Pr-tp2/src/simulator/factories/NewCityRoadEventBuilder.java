package simulator.factories;

import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.NewCityRoadEvent;
import simulator.model.Weather;

public class NewCityRoadEventBuilder extends Builder<Event> {

	public NewCityRoadEventBuilder(String typeTag, String desc) {
		super(typeTag, desc);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Event create_instance(JSONObject data) {
		// TODO Auto-generated method stub
		int time = data.getJSONObject("data").getInt("time");
		String id = data.getJSONObject("data").getString("id");
		String srcJunc = data.getJSONObject("data").getString("src");
		String destJunc = data.getJSONObject("data").getString("dest");
		int length = data.getJSONObject("data").getInt("length");
		int co2Limit = data.getJSONObject("data").getInt("co2limit");
		int maxSpeed = data.getJSONObject("data").getInt("maxspeed");		
		String sw = data.getJSONObject("data").getString("weather");
		Weather weather = Weather.valueOf(sw);
		return new NewCityRoadEvent(time, id, srcJunc, destJunc, length, co2Limit, maxSpeed, weather);
	}


}
