package simulator.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Junction;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;
import simulator.model.VehicleStatus;

public class JunctionsTableModel extends AbstractTableModel implements TrafficSimObserver{

	
	private List<Junction> _junctions = new ArrayList<Junction>();
	String _headers[] = {"Id", "Green", "Queues"};
	
	public JunctionsTableModel(Controller ctr) {
		ctr.addObserver(this);
	}
	
	@Override
	public int getRowCount() {
		
		return this._junctions.size();
	}

	@Override
	public int getColumnCount() {
		
		return this._headers.length;
	}
	
	public String getColumnName(int columnIndex) {
		return _headers[columnIndex];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		
		Junction j = this._junctions.get(rowIndex);
		List<Road> roads = j.getInRoads();
		String val = "";
		switch(columnIndex) {
		case 0:
			val = j.getId();
			break;
		case 1:
			int greenIdx = j.getGreenLightIndex();
			if (greenIdx == -1 || greenIdx >= roads.size()) {
				val = "NONE";
			} else {
				val = roads.get(greenIdx).getId();
			}
			break;
		case 2:
			for(Road r1 : roads) {
				List<Vehicle> lv = new ArrayList<Vehicle>();
				for(Vehicle v : r1.getVehicles()) {
					if(v.getStatus() == VehicleStatus.WAITING) lv.add(v);
				}
				val += r1.getId() + ":" + lv;
			}
			break;
		}
		return val;
	}

	@Override
	public void onAdvance(RoadMap map, Collection<Event> events, int time) {
		
		_junctions = new ArrayList<>(map.getJunctions());
		fireTableDataChanged();
	}

	@Override
	public void onEventAdded(RoadMap map, Collection<Event> events, Event e, int time) {
		
		_junctions = new ArrayList<>(map.getJunctions());
		fireTableDataChanged();
	}

	@Override
	public void onReset(RoadMap map, Collection<Event> events, int time) {
		
		
	}

	@Override
	public void onRegister(RoadMap map, Collection<Event> events, int time) {
		
		
	}

}
