package simulator.factories;

import org.json.JSONObject;

import simulator.model.LightSwitchingStrategy;
import simulator.model.RoundRobinStrategy;

public class RoundRobinStrategyBuilder extends Builder<LightSwitchingStrategy> {



	public RoundRobinStrategyBuilder() {
		super("round_robin_lss", "hola");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected LightSwitchingStrategy create_instance(JSONObject data) {
		// TODO Auto-generated method stub
		//int timeslot = data.getJSONObject("data").getInt("timeslot");
		int timeslot = data.has("timeslot") ? data.getInt("timeslot") : 1;
		return new RoundRobinStrategy(timeslot);
	}

}
