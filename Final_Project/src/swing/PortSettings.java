package swing;

import com.fazecast.jSerialComm.SerialPort;

public class PortSettings {
	private SerialPort port;
	private int baudrate;
	private int stopLength;
	private int parityType;
	private boolean isConnected;
	public PortSettings(SerialPort port, int baudrate, int stopLength, int parityType) {
	this.port = port;
	this.baudrate = baudrate;
	this.stopLength = stopLength;
	this.parityType = parityType;
	isConnected = false;
	}
	public SerialPort getPort() {
		return port;
	}
	public int getBaudrate() {
		return baudrate;
	}
	public void setBaudrate(int baudrate) {
		this.baudrate = baudrate;
	}
	public int getStopLength() {
		return stopLength;
	}
	public void setStopLength(int stopLength) {
		this.stopLength = stopLength;
	}
	public int getParityType() {
		return parityType;
	}
	public void setParityType(int parityType) {
		this.parityType = parityType;
	}
	public void setPort(SerialPort connectedPort) {
		this.port = connectedPort;
	}
	public boolean isConnected() {
		return isConnected;
	}
	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}
	
}
