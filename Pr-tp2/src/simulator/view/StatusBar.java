package simulator.view;

import java.awt.Component;

import javax.swing.JFrame;

import simulator.control.Controller;

public class StatusBar extends JFrame {
	
	private Controller _ctr;
	
	public StatusBar(Controller ctr) {
		_ctr = ctr;
	}

}
