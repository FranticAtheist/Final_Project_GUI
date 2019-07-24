package swing;

import java.awt.TextArea;
import java.io.IOException;
import java.util.Arrays;

import javax.rmi.CORBA.Util;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortMessageListener;

public class MessageListener implements SerialPortMessageListener {
	private SerialPort connectedPort;
	private int state;
	private String text;
	private int failedAttempts;
	private boolean doneSending;
	private int index;
	private byte[] toSendBytes;
	private boolean changingPortSettings;
	private PortSettings portSettings;
	private boolean waitingForResponse;
	private int mode;
	private boolean recivingFile ;
	private TextArea textPanel;
	public MessageListener(SerialPort connectedPort, String toSend) {
		this.connectedPort = connectedPort;
		this.text = toSend;
		this.state = 0;
		this.failedAttempts = 0;
		this.doneSending = false;
		connectedPort.addDataListener(this);
		this.index = 0;
		this.toSendBytes = null;
		this.changingPortSettings = false;
		this.portSettings = null;
		this.mode = -1;
		this.waitingForResponse = false;
		this.recivingFile = false;
		this.textPanel = null; 

	}
	public MessageListener(SerialPort connectedPort) {
		this.connectedPort = connectedPort;
		text = null;
		this.state = 0;
		connectedPort.addDataListener(this);

	}
	public int getListeningEvents() {
		return SerialPort.LISTENING_EVENT_DATA_WRITTEN | SerialPort.LISTENING_EVENT_DATA_RECEIVED;
	}

	@Override
	public void serialEvent(SerialPortEvent event) {
		if (event.getEventType() == SerialPort.LISTENING_EVENT_DATA_WRITTEN) {
			setIndex(getIndex() + 1);

			try {
				if (getToSendBytes() == null) {
					System.out.println("trying to send null");
					return;
				}
				if (getIndex() == getToSendBytes().length) {
					setDoneSending(true);
					System.out.println("All bytes were successfully transmitted!");
					return;
				}
				connectedPort.getOutputStream().write(getToSendBytes()[getIndex()]);
				connectedPort.getOutputStream().flush();
				System.out.println("was sent: " + getToSendBytes()[getIndex()]);

			} catch (IOException e) {
				System.out.println(e);
				e.printStackTrace();
			}

		}
		if (event.getEventType() == SerialPort.LISTENING_EVENT_DATA_RECEIVED) {
			byte[] reveivedData = event.getReceivedData();
			System.out.println("Received data of size: " + reveivedData.length);
			for (int i = 0; i < reveivedData.length; ++i) {
				System.out.println(reveivedData[i] +":"+Integer.toHexString(reveivedData[i]));
			}
			StringBuilder sb = new StringBuilder();
				if(isRecivingFile()) {
					if(reveivedData[0] != Utility.ACK) {
						sb.append(reveivedData[0]);
					}else {
						textPanel.setText("");
						textPanel.append(sb.toString());
					}
				}
			for (int i = 0; i < reveivedData.length; ++i) {
				if (reveivedData[i] == Utility.ACK) {
					if (isChangingPortSettings() & state == Utility.sendMessageState) {
						if (portSettings != null) {
							changePortSettings();
						}
					}
					this.setState(((this.getState() + 1) % 3));
					System.out.println("Ack, current state" + state);
					if (state == 0 & isDoneSending()) {
						System.out.println("transfer successfull");
						setWaitingForResponse(false);
						return;
					}
				} else if (reveivedData[i] == Utility.NEG_ACK) {
					this.setFailedAttempts(this.getFailedAttempts() + 1);
					if (getFailedAttempts() == Utility.MAX_AMOUNT_OF_ATTEPTS) {
						System.out.println("to many failed attempts");
						setWaitingForResponse(false);
						return;
					}
					System.out.println("Neg Ack, current state" + state);
					System.out.println("number of failed attempts:" + getFailedAttempts());
				}if(reveivedData[0]  == Utility.END_OF_TEXT) {
					
					setRecivingFile(true);
					
				}
				if (getFailedAttempts() <= Utility.MAX_AMOUNT_OF_ATTEPTS) {
					if (isDoneSending()) {
						switch (state) {
							case 0 :
								sendMode(getMode());
								break;
							case 1 :
								sendSize(text);
								break;
							case 2 :
								sendMessage(text);
								break;
						}
					}
				} else {
					return;
				}

			}
			return;

		}

	}
	private void changePortSettings() {
		System.out.println("changing port settings !!!");
		getConnectedPort().setBaudRate(Utility.intBaudRates[portSettings.getBaudrate()]);
		getConnectedPort().setParity(Utility.parityType[portSettings.getParityType()]);
		getConnectedPort().setNumStopBits(Utility.stopLengths[portSettings.getStopLength()]);
		setChangingPortSettings(false);
	}
	private void sendSize(String message) {
		byte[] toSend = new byte[Utility.MAX_SIZE_STRING];
		byte[] mirrored = new byte[Utility.MAX_SIZE_STRING];
		for (int i = 0; i < Utility.MAX_SIZE_STRING; i++) {
			toSend[i] = '0';
		}
		char[] temp = String.valueOf(message.length()).toCharArray();
		byte[] messageSize = new String(temp).getBytes();
		for (int i = 0; i < messageSize.length; i++) {
			toSend[messageSize.length - 1 - i] = messageSize[i];
		}
		for (int i = 0; i < Utility.MAX_SIZE_STRING; i++) {
			mirrored[Utility.MAX_SIZE_STRING - 1 - i] = toSend[i];
		}
		setIndex(0);
		setToSendBytes(mirrored);
		setDoneSending(false);
		try {
			connectedPort.getOutputStream().write(getToSendBytes()[getIndex()]);
			connectedPort.getOutputStream().flush();
			System.out.println("was sent: " + getToSendBytes()[getIndex()]);
		} catch (IOException e1) {
			System.out.println(e1);
			e1.printStackTrace();
		}
	}
	private void sendMessage(String message) {
		byte[] test = message.getBytes();
		setIndex(0);
		setToSendBytes(test);
		setDoneSending(false);
		try {
			connectedPort.getOutputStream().write(getToSendBytes()[getIndex()]);
			connectedPort.getOutputStream().flush();
			System.out.println("was sent: " + getToSendBytes()[getIndex()]);
		} catch (IOException e1) {
			System.out.println(e1);
			e1.printStackTrace();
		}
	}
	private void sendMode(Integer typeMode) {
		byte[] toSend = {(byte) (typeMode.byteValue() + '0')};
		setDoneSending(false);
		setToSendBytes(toSend);
		setIndex(0);
		setWaitingForResponse(true);
		try {
			connectedPort.getOutputStream().write(toSend[0]);
			connectedPort.getOutputStream().flush();
			System.out.println("was sent: " + getToSendBytes()[getIndex()]);
			// connectedPort.getPort().getOutputStream().write(typeMode.byteValue()
			// + '0');
		} catch (IOException e1) {
			System.out.println(e1);
			e1.printStackTrace();
		}
	}

	@Override
	public boolean delimiterIndicatesEndOfMessage() {
		return true;
	}

	@Override
	public byte[] getMessageDelimiter() {
		return new byte[]{};
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getFailedAttempts() {
		return failedAttempts;
	}
	public void setFailedAttempts(int failedAttempts) {
		this.failedAttempts = failedAttempts;
	}
	public boolean isDoneSending() {
		return doneSending;
	}
	public void setDoneSending(boolean doneSending) {
		this.doneSending = doneSending;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public byte[] getToSendBytes() {
		return toSendBytes;
	}
	public void setToSendBytes(byte[] toSendBytes) {
		this.toSendBytes = toSendBytes;
	}
	public SerialPort getConnectedPort() {
		return connectedPort;
	}
	public void setConnectedPort(SerialPort connectedPort) {
		this.connectedPort = connectedPort;
	}
	public boolean isChangingPortSettings() {
		return changingPortSettings;
	}
	public void setChangingPortSettings(boolean changingPortSettings) {
		this.changingPortSettings = changingPortSettings;
	}

	public void setPortSettings(PortSettings portSettings) {
		this.portSettings = portSettings;
	}
	public PortSettings getPortSettings() {
		return portSettings;
	}
	public int getMode() {
		return mode;
	}
	public void setMode(int mode) {
		this.mode = mode;
	}
	public boolean isWaitingForResponse() {
		return waitingForResponse;
	}
	public void setWaitingForResponse(boolean waitingForResponse) {
		this.waitingForResponse = waitingForResponse;
	}
	public boolean isRecivingFile() {
		return recivingFile;
	}
	public void setRecivingFile(boolean recivingFile) {
		this.recivingFile = recivingFile;
	}
	public TextArea getTextPanel() {
		return textPanel;
	}
	public void setTextPanel(TextArea textPanel) {
		this.textPanel = textPanel;
	}

}
