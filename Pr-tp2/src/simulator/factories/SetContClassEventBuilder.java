package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.SetContClassEvent;

public class SetContClassEventBuilder extends Builder<Event> {

	public SetContClassEventBuilder() {
		super("set_cont_class", "hola");
	}

	@Override
	protected Event create_instance(JSONObject data) {
		
        int time = data.getInt("time");
        
        JSONArray infoArray = data.getJSONArray("info");
        
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
