package simulator.factories;

import org.json.JSONObject;

import simulator.model.Event;

public class SetWeatherEventBuilder extends Builder<Event> {

	public SetWeatherEventBuilder(String typeTag, String desc) {
		super(typeTag, desc);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Event create_instance(JSONObject data) {
		// TODO Auto-generated method stub
		return null;
	}

}
