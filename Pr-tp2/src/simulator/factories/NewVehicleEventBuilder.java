package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.NewVehicleEvent;

public class NewVehicleEventBuilder extends Builder<Event> {

	public NewVehicleEventBuilder() {
		super("new_vehicle", "");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Event create_instance(JSONObject data) {
		// TODO Auto-generated method stub
		int time = data.getJSONObject("data").getInt("time");
		String id = data.getJSONObject("data").getString("id");
		int maxSpeed = data.getJSONObject("data").getInt("maxspeed");
		int contClass = data.getJSONObject("data").getInt("class");
        JSONArray itineraryArray = data.getJSONArray("itinerary");
        List<String> itinerary = new ArrayList<>();
        for (int i = 0; i < itineraryArray.length(); i++) {
            itinerary.add(itineraryArray.getString(i));
        }
		
        return new NewVehicleEvent(time, id, maxSpeed, contClass, itinerary);
	}

}
