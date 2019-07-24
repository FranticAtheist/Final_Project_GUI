package swing;

import java.util.EventObject;

public class FormEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int typeCategory;
	private int mode;
	private boolean isCommand;
	

	private String parity;
	private String chatText;

	public FormEvent(Object source) {
		super(source);

	}

	public FormEvent(Object source,int mode, String chatText, boolean isCommand){
		super(source);
		this.mode = mode;
		this.chatText = chatText;
		this.isCommand = isCommand;
	}
	
	
	public int getTypeCategory() {
		return typeCategory;
	}

	public int getMode() {
		return mode;
	}

	public String getText() {
		return chatText;
	}

	public String getParity() {
		return parity;
	}

	public boolean isCommand() {
		return isCommand;
	}

}
