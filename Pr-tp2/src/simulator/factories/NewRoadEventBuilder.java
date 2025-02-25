package simulator.factories;

import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.Weather;

public abstract class  NewRoadEventBuilder extends Builder<Event>{

	public NewRoadEventBuilder(String typeTag, String desc) {
		super(typeTag, desc);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Event create_instance(JSONObject data) {
		// TODO Auto-generated method stub
		
		int time = data.getInt("time");
		String id = data.getString("id");
		String srcJunc = data.getString("src");
		String destJunc = data.getString("dest");
		int length = data.getInt("length");
		int co2Limit = data.getInt("co2limit");
		int maxSpeed = data.getInt("maxspeed");		
		String sw = data.getString("weather");
		Weather weather = Weather.valueOf(sw);
		return create_event(time, id, srcJunc, destJunc, 
				length, co2Limit, maxSpeed, weather);
	}
	
	protected abstract Event create_event(int time, String id, String srcJunc, String destJunc, 
			int length, int co2Limit, int maxSpeed, Weather weather);
}
