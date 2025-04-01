package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.DequeuingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchingStrategy;
import simulator.model.NewJunctionEvent;

public class NewJunctionEventBuilder extends Builder<Event> {
    private Factory<LightSwitchingStrategy> _lssFactory;
    private Factory<DequeuingStrategy> _dqsFactory;

	public NewJunctionEventBuilder(Factory<LightSwitchingStrategy> lssFactory, Factory<DequeuingStrategy> dqsFactory) {
		super("new_junction", "hola");
		this._lssFactory = lssFactory;
		this._dqsFactory = dqsFactory;
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Event create_instance(JSONObject data) {
		// TODO Auto-generated method stub
        
	    int time = data.getInt("time");
        String id = data.getString("id");

        // Obtener coordenadas
        JSONArray coorArray = data.getJSONArray("coor");
        int xCoor = coorArray.getInt(0);
        int yCoor = coorArray.getInt(1);

        // Obtener estrategias usando las fábricas
        LightSwitchingStrategy lsStrategy = _lssFactory.create_instance(data.getJSONObject("ls_strategy"));
        DequeuingStrategy dqStrategy = _dqsFactory.create_instance(data.getJSONObject("dq_strategy"));

        return new NewJunctionEvent(time, id, lsStrategy, dqStrategy, xCoor, yCoor);
    }

}
