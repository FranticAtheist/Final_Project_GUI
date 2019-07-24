package swing;

import java.util.EventObject;

import javax.swing.JButton;

import com.fazecast.jSerialComm.SerialPort;

public class ToolbarEvent extends EventObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SerialPort currentPort;
	private int parity;
	private int stop;
	private int baudrate;
	private JButton clicked;

	public ToolbarEvent(Object source) {
		super(source);
		
	}
	public ToolbarEvent(Object source, SerialPort currentPort, int parity, int stop, int baudrate, JButton clicked) {
		super(source);
		this.currentPort = currentPort;
		this.parity = parity;
		this.stop = stop;
		this.baudrate = baudrate;
		this.clicked = clicked;
	}
	public JButton getClicked() {
		return clicked;
	}
	public ToolbarEvent(Object source, SerialPort currentPort) {
		super(source);
		this.currentPort = currentPort;
	}
	public SerialPort getCurrentPort() {
		return currentPort;
	}

	public int getParity() {
		return parity;
	}
	public int getStop() {
		return stop;
	}
	public int getBaudrate() {
		return baudrate;
	}

}
