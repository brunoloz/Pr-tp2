package simulator.view;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

//import dialog.compras.DialogoCompras;
import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class ChangeCO2Dialog extends JDialog implements TrafficSimObserver{
	 
//	private JPanel _panelIzquierdo;
//	private JComboBox<String> _comboBox;
	
	private JComboBox<String> comboBox;
	private JTextArea area;
	private JPanel panelIzquierdo;
	private int indexComboBox;
	public int estado;
	
	public ChangeCO2Dialog(Frame parent) {
		super(parent, true);
		this.setTitle("Change CO2 Class");
		this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		initGUI();
		
	}
	
	
	private void initGUI() {
//		JPanel mainPanel = new JPanel();
//	     mainPanel.setLayout(new FlowLayout());
//	    
//	     
//	     _panelIzquierdo = new JPanel();
//	     
//	     BoxLayout layout  = new BoxLayout(_panelIzquierdo,BoxLayout.Y_AXIS);
//		 _panelIzquierdo.setLayout(layout);
//		 this._comboBox = new JComboBox<String>();
//		 
//		 
//		 _panelIzquierdo.add(_comboBox);
//		 mainPanel.add(_panelIzquierdo);
//		 
//		 
//		 this.add(mainPanel);
//		 this.setVisible(false);
//		 this.pack();
		JPanel mainPanel = new JPanel();
	     mainPanel.setLayout(new FlowLayout());
	    
	     
	     panelIzquierdo = new JPanel();
	     
	     BoxLayout layout  = new BoxLayout(panelIzquierdo,BoxLayout.Y_AXIS);
		 panelIzquierdo.setLayout(layout);
		 this.comboBox = new JComboBox<String>();
			
		 panelIzquierdo.add(this.comboBox);
		
	     mainPanel.add(panelIzquierdo);
	     
	     JPanel panelDerecho = new JPanel();
         
	     
	     area = new JTextArea(15,15);
	     area.setEditable(false);
	     
       JPanel panelBotones = new JPanel(new FlowLayout());
		 
		 JButton ok = new JButton("OK");
		 ok.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e ) {
				if (comboBox.getSelectedItem() != null) {
					estado = 1;
					indexComboBox = comboBox.getSelectedIndex();
					ChangeCO2Dialog.this.setVisible(false);
				}
			}
			  
		  });
		 panelBotones.add(ok);
		 
		 JButton cancel = new JButton("Cancel");
		 cancel.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
				   estado = 0;
				   ChangeCO2Dialog.this.setVisible(false);
					
				}
				  
			  });
		 
		 panelBotones.add(cancel);
	     
	     JScrollPane scroll = new JScrollPane(area);
	     panelDerecho.add(scroll);
	     panelDerecho.add(panelBotones);
	     
	     
		 
		 
	     mainPanel.add(panelDerecho);
		
	     
	   
	     this.add(mainPanel);
	     this.setVisible(false); 
	     this.pack();
	}

	@Override
	public void onAdvance(RoadMap map, Collection<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEventAdded(RoadMap map, Collection<Event> events, Event e, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReset(RoadMap map, Collection<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRegister(RoadMap map, Collection<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

}
