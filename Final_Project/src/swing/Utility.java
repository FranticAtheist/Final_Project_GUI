package swing;

import com.fazecast.jSerialComm.SerialPort;

public class Utility {
	static int[] intBaudRates = { 2400, 9600, 19200, 38400 };
	static final String[] StringBoudRates = {"2400","9600","19200","38400" };
	static final String[] parityTypeString = { "none", "odd", "even" };
	static final String[] stopLengthsString = { "1", "2" };
	static final char ACK = 6;
	static final char NEG_ACK = 21;
	static final char NF = 13;
	static final char END_OF_TEXT = 3;
	static final int chatMode =0;
	static final int commandMode = 1;
	static final int fileMode = 2;
	static final int uartCommand = 2;
	static final int sendModeState = 0;
	static final int sendSizeState =1;
	static final int sendMessageState = 2;
	static enum mode{sleep,chat,command,file};
	static final int[] stopLengths = {SerialPort.ONE_STOP_BIT,SerialPort.TWO_STOP_BITS};
	static final int[] parityType = {SerialPort.NO_PARITY,SerialPort.ODD_PARITY,SerialPort.EVEN_PARITY};
	static final String[] modes = {"chat","command","file"};
	static final int MAX_AMOUNT_OF_ATTEPTS = 3;
	static final int MAX_SIZE_STRING = 5;
	static final String[] LedColors = {"clear rgb","set red","set green","set yellow","set blue","set purple","set light blue","set white"};
}
