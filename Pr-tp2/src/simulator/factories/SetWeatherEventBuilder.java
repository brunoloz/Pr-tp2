package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.SetWeatherEvent;
import simulator.model.Weather;

public class SetWeatherEventBuilder extends Builder<Event> {

	public SetWeatherEventBuilder() {
		super("set_weather", "");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Event create_instance(JSONObject data) {
		// TODO Auto-generated method stub
        int time = data.getJSONObject("data").getInt("time");
        
        JSONArray infoArray = data.getJSONObject("data").getJSONArray("info");
        
        List<Pair< String, Weather>> weatherInfoList = new ArrayList<>();
        
        for (int i = 0; i < infoArray.length(); i++) {
            JSONObject weatherData = infoArray.getJSONObject(i);
            
            String road = weatherData.getString("road");
            String weatherStr = weatherData.getString("weather");
            
            Weather weather = Weather.valueOf(weatherStr);
            
            weatherInfoList.add(new Pair<String, Weather>(road, weather));
        }

        return new SetWeatherEvent(time, weatherInfoList);
	}

}
