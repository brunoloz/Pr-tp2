package simulator.factories;

import org.json.JSONObject;

import simulator.model.LightSwitchingStrategy;
import simulator.model.MostCrowdedStrategy;

public class MostCrowdedStrategyBuilder extends Builder<LightSwitchingStrategy> {

	public MostCrowdedStrategyBuilder() {
		super("most_crowded_lss", "");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected LightSwitchingStrategy create_instance(JSONObject data) {
		// TODO Auto-generated method stub
		int timeslot = data.getJSONObject("data").getInt("timeslot");
		
		return new MostCrowdedStrategy(timeslot);
	}

}
