package simulator.factories;

import org.json.JSONObject;

import simulator.model.DequeuingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchingStrategy;
import simulator.model.NewJunctionEvent;

public class NewJunctionEventBuilder extends Builder<Event> {
    private Factory<LightSwitchingStrategy> _lssFactory;
    private Factory<DequeuingStrategy> _dqsFactory;

	public NewJunctionEventBuilder(Factory<LightSwitchingStrategy> lssFactory, Factory<DequeuingStrategy> dqsFactory) {
		super("new_junction", "");
		this._lssFactory = lssFactory;
		this._dqsFactory = dqsFactory;
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Event create_instance(JSONObject data) {
		// TODO Auto-generated method stub
        
		int time = data.getJSONObject("data").getInt("time");
		
		String id = data.getJSONObject("data").getString("id");
		
        int x = data.getJSONObject("data").getJSONArray("coor").getInt(0);
        int y = data.getJSONObject("data").getJSONArray("coor").getInt(1);
        
        JSONObject joLs = data.getJSONObject("data").getJSONObject("ls_strategy");
        LightSwitchingStrategy lsStrategy = _lssFactory.create_instance(joLs); 
        
        JSONObject joDq = data.getJSONObject("data").getJSONObject("dq_strategy");
        DequeuingStrategy dqStrategy = _dqsFactory.create_instance(joDq);
        
        return new NewJunctionEvent(time, id, lsStrategy, dqStrategy, x, y);
	}

}
