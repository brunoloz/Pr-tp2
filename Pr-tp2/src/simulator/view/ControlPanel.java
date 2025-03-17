package simulator.view;

import java.awt.Component;

import javax.swing.JPanel;

import simulator.control.Controller;

public class ControlPanel extends JPanel {
	
	private Controller _ctr;
	
	public ControlPanel(Controller ctr) {
		_ctr = ctr;
		initGUI();
	}
	
	private void initGUI() {
		
	}

}
