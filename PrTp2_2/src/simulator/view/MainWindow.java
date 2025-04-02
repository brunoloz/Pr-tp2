package simulator.view;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import java.awt.*;

import simulator.control.Controller;

public class MainWindow extends JFrame {

	private Controller _ctrl;
	private Border _defaultBorder = BorderFactory.createLineBorder(Color.black, 1);

	public MainWindow(Controller ctrl) {
		super("Traffic Simulator");
		ImageIcon h = new ImageIcon("resources/icons/car_front.png");
		this.setIconImage(h.getImage());
		_ctrl = ctrl;
		initGUI();
	}

	private void initGUI() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		this.setContentPane(mainPanel);

		mainPanel.add(new ControlPanel(_ctrl), BorderLayout.PAGE_START);
		mainPanel.add(new StatusBar(_ctrl),BorderLayout.PAGE_END);
		
		JPanel viewsPanel = new JPanel(new GridLayout(1, 2));
		mainPanel.add(viewsPanel, BorderLayout.CENTER);

		JPanel tablesPanel = new JPanel();
		tablesPanel.setLayout(new BoxLayout(tablesPanel, BoxLayout.Y_AXIS));
		viewsPanel.add(tablesPanel);

		JPanel mapsPanel = new JPanel();
		mapsPanel.setLayout(new BoxLayout(mapsPanel, BoxLayout.Y_AXIS));
		viewsPanel.add(mapsPanel);

		// tables
		JPanel eventsView = createViewPanel(new JTable(new EventsTableModel(_ctrl)), "Events");
		eventsView.setPreferredSize(new Dimension(500, 200));
		tablesPanel.add(eventsView);
		
		eventsView.setBorder(BorderFactory.createTitledBorder(_defaultBorder, "Events", TitledBorder.LEFT, TitledBorder.TOP));
		
		
		JPanel vehiclesView = createViewPanel(new JTable(new VehiclesTableModel(_ctrl)), "Vehicles");
		vehiclesView.setPreferredSize(new Dimension(500, 200));
		tablesPanel.add(vehiclesView);
		
		vehiclesView.setBorder(BorderFactory.createTitledBorder(_defaultBorder, "Vehicles", TitledBorder.LEFT, TitledBorder.TOP));
		
		
		JPanel roadsView = createViewPanel(new JTable(new RoadsTableModel(_ctrl)), "Roads");
		roadsView.setPreferredSize(new Dimension(500, 200));
		tablesPanel.add(roadsView);
		
		roadsView.setBorder(BorderFactory.createTitledBorder(_defaultBorder, "Roads", TitledBorder.LEFT, TitledBorder.TOP));
		
		// TODO add other tables
		JPanel junctionsView = createViewPanel(new JTable(new JunctionsTableModel(_ctrl)), "Junctions");
		junctionsView.setPreferredSize(new Dimension(500, 200));
		tablesPanel.add(junctionsView);
		
		
		junctionsView.setBorder(BorderFactory.createTitledBorder(_defaultBorder, "Junctions", TitledBorder.LEFT, TitledBorder.TOP));
		

		// maps
		JPanel mapView = createViewPanel(new MapComponent(_ctrl), "Map");
		mapView.setPreferredSize(new Dimension(500, 400));
		mapsPanel.add(mapView);
		
		
		mapView.setBorder(BorderFactory.createTitledBorder(_defaultBorder, "Map", TitledBorder.LEFT, TitledBorder.TOP));
		
		JPanel mapByRoadView = createViewPanel(new MapByRoadComponent(_ctrl), "MapByRoad");
		mapByRoadView.setPreferredSize(new Dimension(500, 400));
		mapsPanel.add(mapByRoadView);
		
		mapByRoadView.setBorder(BorderFactory.createTitledBorder(_defaultBorder, "Map By Road", TitledBorder.LEFT, TitledBorder.TOP));
		
		// TODO add a map for MapByRoadComponent
		// ...
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
	}

	private JPanel createViewPanel(JComponent c, String title) {
		JPanel p = new JPanel( new BorderLayout() );
            // TODO add a framed border to p with title
		p.add(new JScrollPane(c));
		return p;
	}
}
