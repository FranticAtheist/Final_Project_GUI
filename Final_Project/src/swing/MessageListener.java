package swing;

import java.awt.TextArea;
import java.nio.ByteBuffer;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortMessageListener;

public class MessageListener implements SerialPortMessageListener {

	private TextArea textPanel;
	private ByteBuffer bb;

	public TextArea getTextPanel() {
		return textPanel;
	}

	public void setTextPanel(TextArea textPanel) {
		this.textPanel = textPanel;
	}

	@Override
	public int getListeningEvents() {
		return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
	}

	@Override
	public byte[] getMessageDelimiter() {
		return new byte[] { (byte) 0x17, (byte) 0x17 };
	}

	@Override
	public boolean delimiterIndicatesEndOfMessage() {
		return true;
	}

	@Override
	public void serialEvent(SerialPortEvent event) {
		bb = ByteBuffer.allocate(16);
		byte[] delimitedMessage = event.getReceivedData();
		if (delimitedMessage.length == 8) {
			for (int i = 0; i < delimitedMessage.length; i = i + 2) {
				bb.put((byte) 0x00);
				bb.put((byte) 0x00);
				bb.put(delimitedMessage[i + 1]);
				bb.put(delimitedMessage[i]);
			}
			System.out.println((short) bb.getInt(0));
			System.out.println((short) bb.getInt(4));
			System.out.println((short) bb.getInt(8));
			System.out.println((short) bb.getInt(12));

//			textPanel.append("got data");
			for (int i = 0; i < 3; i++) {
				textPanel.append(String.valueOf(((short) bb.getInt(i)/4096.0)));
				textPanel.append("\n");
			}
		}
		// for(int i=0; i < 8 ;i=i+2) {
		// System.out.println((short) delimitedMessage[i+1]& 0xff<< 8 |
		// delimitedMessage[i]& 0xff );
		// }
		System.out.println("\n");
		// char temp[] = delimitedMessage.toCharArray();
		// int[] value = new int[8];
		// for(int i =0;i<delimitedMessage.length();i++) {
		// value[i] = (int) temp[i];
		// System.out.println((int)temp[i]);
		// }
	}

}
