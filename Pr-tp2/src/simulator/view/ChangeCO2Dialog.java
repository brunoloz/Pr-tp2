package simulator.view;

import java.awt.BorderLayout;
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

//import dialog.compras.DialogoCompras;
import simulator.control.Controller;
import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.SetContClassEvent;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;

public class ChangeCO2Dialog extends JDialog implements TrafficSimObserver {

	private Controller _ctrl;
	private List<Vehicle> _vehicle = new ArrayList<>();
	private int _time = 0;
	private JComboBox<Vehicle> _vehicleComboBox;
	private JComboBox<Integer> _CO2ComboBox;
	private SpinnerNumberModel n_spinner;

	public ChangeCO2Dialog(Frame parent, Controller ctr) {
		super(parent, true);
		this._ctrl = ctr;
		this.setTitle("Change CO2 Class");
		this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		initGUI();
		_ctrl.addObserver(this);

	}

	private void initGUI() {

		this.setLayout(new BorderLayout());
		this.setSize(800, 200);

		// Panel superior con texto descriptivo
		JPanel topPanel = new JPanel();
		JLabel label = new JLabel("<html><b>Schedule an event to change the weather of a road after\"\n"
				+ "a given number of simulation ticks from now.</b></html>");
		topPanel.add(label);

		// Panel central con JComboBox
		JPanel centerPanel = new JPanel();
		n_spinner = new SpinnerNumberModel(10, 1, 10000, 10);
		JSpinner ticks = new JSpinner(n_spinner);
		centerPanel.add(new JLabel("Vehicle: "));
		_vehicleComboBox = new JComboBox<Vehicle>();
		centerPanel.add(_vehicleComboBox);

		centerPanel.add(new JLabel("CO2 Class: "));
		_CO2ComboBox = new JComboBox<>();
		for(int i = 0; i < 11; i++) {
			_CO2ComboBox.addItem(i);
		}

		centerPanel.add(_CO2ComboBox);

		centerPanel.add(new JLabel("Ticks: "));
		centerPanel.add(ticks);

		// Panel inferior con botones
		JPanel bottomPanel = new JPanel();
		JButton acceptButton = new JButton("Accept");
		JButton cancelButton = new JButton("Cancel");

		acceptButton.addActionListener(e -> {
			if (!_vehicle.isEmpty()) {
				Vehicle selectedVehicle = (Vehicle) _vehicleComboBox.getSelectedItem();
				Integer selectedCO2Class = (Integer) _CO2ComboBox.getSelectedItem();
				Pair<String, Integer> info = new Pair<>(selectedVehicle.getId(), selectedCO2Class);
				List<Pair<String, Integer>> events = new ArrayList<>();
				events.add(info);
				SetContClassEvent co2 = new SetContClassEvent(_time + n_spinner.getNumber().intValue(), events);
				_ctrl.addEvent(co2);
			}
			this.setVisible(false);
		});

		cancelButton.addActionListener(e -> {
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
		
		_vehicle.clear();
	    _vehicleComboBox.removeAllItems();

	    for (Vehicle v : map.getVehicles()) {
	        _vehicle.add(v);
	        _vehicleComboBox.addItem(v);
	    }
	    this._time =time;
	}

	

	@Override
	public void onEventAdded(RoadMap map, Collection<Event> events, Event e, int time) {
		

	}

	@Override
	public void onReset(RoadMap map, Collection<Event> events, int time) {
		
		this._vehicle.clear();

	}

	@Override
	public void onRegister(RoadMap map, Collection<Event> events, int time) {
		
		_vehicle.clear();
	    _vehicleComboBox.removeAllItems();

	    for (Vehicle v : map.getVehicles()) {
	        _vehicle.add(v);
	        _vehicleComboBox.addItem(v);
	    }
	    this._time =time;

	}

}
