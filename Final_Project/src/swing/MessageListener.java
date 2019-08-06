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
		return new byte[] { (byte) 0xFF, (byte) 0xFF };
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
			int[] acc = new int[4];
			for (int i=0;i<4;i++) {
				acc[i] = (short)bb.getInt(4*i);
				System.out.println(acc[i]);
			}
			
//			System.out.println((short) bb.getInt(0));
//			System.out.println((short) bb.getInt(4));
//			System.out.println((short) bb.getInt(8));
//			System.out.println((short) bb.getInt(12));

//			textPanel.append("got data");
			System.out.println();
			for (int i = 0; i < 3; i++) {
//				short value = (short) bb.getInt(i*4);
				textPanel.append(String.valueOf(acc[i]/4096.0));
				textPanel.append("\n");
			}
		}
		if(delimitedMessage.length == 4) {
			for (int i = 0; i < delimitedMessage.length; i = i + 2) {
				bb.put((byte) 0x00);
				bb.put((byte) 0x00);
				bb.put(delimitedMessage[i+1]);
				bb.put(delimitedMessage[i]);
			}
			int pulse_time = (int)bb.getInt(0);
			if(pulse_time == 0xf5f5) {
				System.out.println("Senser to close!");
			}
			else {
			float distance = (float) (pulse_time*64*50*(331.3+0.606*28.8)/24000000);
			System.out.println(distance + " cm");
			textPanel.append(String.valueOf(distance));
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
//	public class javaFevalFunc{
//	    public void main(String[] args) throws Exception{
//	        MatlabEngine eng = MatlabEngine.startMatlab();
//	        double[] a = {2.0 ,4.0, 6.0};
//	        double[] roots = eng.feval("sqrt", a);
//	        for (double e: roots) {
//	            System.out.println(e);
//	        }
//	        eng.close();
//	    }
//	}

}
