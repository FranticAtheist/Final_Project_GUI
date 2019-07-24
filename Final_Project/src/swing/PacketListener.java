package swing;
import com.fazecast.jSerialComm.*;


public class PacketListener  implements SerialPortPacketListener{
	
	byte[] buffer;
	@Override
	public int getListeningEvents() {
		return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
	}

	@Override
	public void serialEvent(SerialPortEvent arg0) {
		buffer = new byte[arg0.getSerialPort().bytesAvailable()];
		arg0.getSerialPort().readBytes(buffer, buffer.length);
		ReaderBuffer.parseByteArray(buffer);
	}

	@Override
	public int getPacketSize() {
		if (buffer != null) {
			return buffer.length; 
		}
		return 0;
	}

}
