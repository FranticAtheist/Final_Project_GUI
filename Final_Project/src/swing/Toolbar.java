package swing;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.fazecast.jSerialComm.SerialPort;

public class Toolbar extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ToolbarListener toolbarListener;
	private JButton connectButton;


	private JButton disconnectButton;
	private JButton changeSettingsButton;
	private JComboBox<String> baudCombo;
	private JComboBox<String> portCombo;
	private SerialPort ports[] = SerialPort.getCommPorts();
	private JComboBox<String> parityCombo;
	private JComboBox<String> stopLengthCombo;

	public Toolbar() {
		setBorder(BorderFactory.createEtchedBorder());
		connectButton = new JButton("connect");
		disconnectButton = new JButton("disconnect");
		baudCombo = new JComboBox<String>();
		portCombo = new JComboBox<String>();
		parityCombo = new JComboBox<String>();
		stopLengthCombo = new JComboBox<String>();
		changeSettingsButton = new JButton("change");
		
		
		connectButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				
				SerialPort port = ports[portCombo.getSelectedIndex()];
				int baudRate = baudCombo.getSelectedIndex();
				int parityType = parityCombo.getSelectedIndex();
				int stopLength = stopLengthCombo.getSelectedIndex();
				
				ToolbarEvent ev = new ToolbarEvent(this, port, parityType, stopLength, baudRate, connectButton);
				
				if(toolbarListener != null) {
					toolbarListener.toolbarEventOccurred(ev);
				}
			}
		});
		
		disconnectButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				
				SerialPort port = ports[portCombo.getSelectedIndex()];
				int baudRate = baudCombo.getSelectedIndex();
				int parityType = parityCombo.getSelectedIndex();
				int stopLength = stopLengthCombo.getSelectedIndex();
				
				ToolbarEvent ev = new ToolbarEvent(this, port, parityType, stopLength, baudRate, disconnectButton);
				
				if(toolbarListener != null) {
					toolbarListener.toolbarEventOccurred(ev);
				}
			}
		});
		
		changeSettingsButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				
				SerialPort port = ports[portCombo.getSelectedIndex()];
				int baudRate = baudCombo.getSelectedIndex();
				int parityType = parityCombo.getSelectedIndex();
				int stopLength = stopLengthCombo.getSelectedIndex();
				
				ToolbarEvent ev = new ToolbarEvent(this, port, parityType, stopLength, baudRate, changeSettingsButton);
				
				if(toolbarListener != null) {
					toolbarListener.toolbarEventOccurred(ev);
				}
			}
		});
		


		disconnectButton.setVisible(false);
		changeSettingsButton.setVisible(false);
		connectButton.setBackground(new Color(0, 200, 0));
		disconnectButton.setBackground(new Color(200, 0, 0));
		
		// set up baud combo box
		DefaultComboBoxModel<String> baudModel = new DefaultComboBoxModel<String>();
		for (String rate : Utility.StringBoudRates) {
			baudModel.addElement(rate);
		}
		baudCombo.setModel(baudModel);
		baudCombo.setSelectedIndex(1);

		// set up parity combo box
		DefaultComboBoxModel<String> parityModel = new DefaultComboBoxModel<String>();
		for (String parity : Utility.parityTypeString) {
			parityModel.addElement(parity);
		}
		parityCombo.setModel(parityModel);
		parityCombo.setSelectedIndex(0);

		// set up stop length combo box
		DefaultComboBoxModel<String> stopModel = new DefaultComboBoxModel<String>();
		for (String stopLength : Utility.stopLengthsString) {
			stopModel.addElement(stopLength);
		}
		stopLengthCombo.setModel(stopModel);
		stopLengthCombo.setSelectedIndex(0);
		
		//set up port combo box
		DefaultComboBoxModel<String> portModel = new DefaultComboBoxModel<String>();
		for (SerialPort port : ports) {
			portModel.addElement(port.getSystemPortName());
		}
		portCombo.setModel(portModel);
		//portCombo.setSelectedIndex(0);

		setLayout(new FlowLayout(FlowLayout.LEFT));

		add(connectButton);
		add(changeSettingsButton);
		add(disconnectButton);
		add(new JLabel("port: "));
		add(portCombo);
		add(new JLabel("baud rate: "));
		add(baudCombo);
		add(new JLabel("stop: "));
		add(stopLengthCombo);
		add(new JLabel("parity: "));
		add(parityCombo);
		

	}



	public void setToolbarListener(ToolbarListener toolbarListener) {
		this.toolbarListener = toolbarListener;
	}
	public JButton getConnectButton() {
		return connectButton;
	}

	public void setConnectButton(JButton connectButton) {
		this.connectButton = connectButton;
	}

	public JButton getDisconnectButton() {
		return disconnectButton;
	}

	public void setDisconnectButton(JButton disconnectButton) {
		this.disconnectButton = disconnectButton;
	}

	public JButton getChangeSettingsButton() {
		return changeSettingsButton;
	}

	public void setChangeSettingsButton(JButton changeSettingsButton) {
		this.changeSettingsButton = changeSettingsButton;
	}



	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
