package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.SetContClassEvent;
import simulator.model.SetWeatherEvent;
import simulator.model.Weather;

public class SetContClassEventBuilder extends Builder<Event> {

	public SetContClassEventBuilder() {
		super("set_cont_class", "");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Event create_instance(JSONObject data) {
		// TODO Auto-generated method stub
		
        int time = data.getJSONObject("data").getInt("time");
        
        JSONArray infoArray = data.getJSONObject("data").getJSONArray("info");
        
        List<Pair< String, Integer>> contInfoList = new ArrayList<>();
        
        for (int i = 0; i < infoArray.length(); i++) {
            JSONObject contData = infoArray.getJSONObject(i);
            
            String vehicle = contData.getString("vehicle");
            int contClass = contData.getInt("class");
            
            
            contInfoList.add(new Pair<String, Integer>(vehicle, contClass));
        }

        return new SetContClassEvent(time, contInfoList);
	}

}
