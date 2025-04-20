package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import simulator.control.Controller;
import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.SetWeatherEvent;
import simulator.model.TrafficSimObserver;
import simulator.model.Weather;

public class ChangeRoadWeatherDialog extends JDialog implements TrafficSimObserver{
	
	private Controller _ctrl;
	private List<Road> _roads = new ArrayList<>();
	private int _time = 0;
    private JComboBox<Road> _roadComboBox;
    private JComboBox<Weather> _weatherComboBox;
    private SpinnerNumberModel n_spinner;

	public ChangeRoadWeatherDialog(Frame f, Controller ctrl) {
		super(f, true);
		this._ctrl = ctrl;
		this.setTitle("Change Road's Weather");
		initGUI();
		this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		_ctrl.addObserver(this);
	}
	
	private void initGUI() {
		
		
		this.setLayout(new BorderLayout());
		this.setSize(800, 200);
		
	    // Panel superior con texto descriptivo
	    JPanel topPanel = new JPanel();
	    JLabel label = new JLabel("<html><b>Schedule an event to change the weather of a road after"
	    		+ "a given number of simulation ticks from now.</b></html>");
	    topPanel.add(label);
	    
	    // Panel central con JComboBox
	    JPanel centerPanel = new JPanel();
	    n_spinner = new SpinnerNumberModel(10, 1, 10000, 10);
	    JSpinner ticks = new JSpinner(n_spinner);
	    centerPanel.add(new JLabel("Road: "));
	    _roadComboBox = new JComboBox<Road>();
	    centerPanel.add(_roadComboBox);
	    
	    centerPanel.add(new JLabel("Weather: "));
	    _weatherComboBox = new JComboBox<>(Weather.values());
	    centerPanel.add(_weatherComboBox);
	    
	    centerPanel.add(new JLabel("Ticks: "));
	    centerPanel.add(ticks);
	    
	    // Panel inferior con botones
	    JPanel bottomPanel = new JPanel();
	    JButton acceptButton = new JButton("Accept");
	    JButton cancelButton = new JButton("Cancel");
	    
	    acceptButton.addActionListener(e->{
	    	if(!_roads.isEmpty()) {
		        Road selectedRoad = (Road) _roadComboBox.getSelectedItem();
		        Weather selectedWeather = (Weather) _weatherComboBox.getSelectedItem();
	    		Pair<String, Weather> info = new Pair<>(selectedRoad.getId(), selectedWeather);
	    		List<Pair<String, Weather>> events = new ArrayList<>();
	    		events.add(info);
	    		SetWeatherEvent we = new SetWeatherEvent(_time + n_spinner.getNumber().intValue(), events);
	    		_ctrl.addEvent(we);
	    	}
	    	this.setVisible(false);
	    });
	    
	    cancelButton.addActionListener(e ->{
	    	setVisible(false);
	    });
	    
	    bottomPanel.add(acceptButton);
	    bottomPanel.add(cancelButton);
	    
	    this.add(topPanel, BorderLayout.NORTH);
	    this.add(centerPanel, BorderLayout.CENTER);
	    this.add(bottomPanel, BorderLayout.SOUTH);
	    
	    setVisible(false);
	}
	
	public void open() {
		setVisible(true);
	}

	@Override
	public void onAdvance(RoadMap map, Collection<Event> events, int time) {
		// TODO Auto-generated method stub
	    _roads.clear();
	    _roadComboBox.removeAllItems();

	    for (Road r : map.getRoads()) {
	        _roads.add(r);
	        _roadComboBox.addItem(r);
	    }
	    this._time =time;
	}

	@Override
	public void onEventAdded(RoadMap map, Collection<Event> events, Event e, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReset(RoadMap map, Collection<Event> events, int time) {
		// TODO Auto-generated method stub
		this._roads.clear();
	}

	@Override
	public void onRegister(RoadMap map, Collection<Event> events, int time) {
		// TODO Auto-generated method stub
	    _roads.clear();
	    _roadComboBox.removeAllItems();

	    for (Road r : map.getRoads()) {
	        _roads.add(r);
	        _roadComboBox.addItem(r);
	    }

	    this._time = time;
	}

}
