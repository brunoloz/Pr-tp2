package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.NewVehicleEvent;

public class NewVehicleEventBuilder extends Builder<Event> {

	public NewVehicleEventBuilder() {
		super("new_vehicle", "hola");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Event create_instance(JSONObject data) {
		// TODO Auto-generated method stub
		int time = data.getInt("time");
		String id = data.getString("id");
		int maxSpeed = data.getInt("maxspeed");
		int contClass = data.getInt("class");
		List<String> itinerary = new ArrayList<>();
        JSONArray itineraryArray = data.getJSONArray("itinerary");
        for (int i = 0; i < itineraryArray.length(); i++) {
            itinerary.add(itineraryArray.getString(i));
        }
		
        return new NewVehicleEvent(time, id, maxSpeed, contClass, itinerary);
	}

}
