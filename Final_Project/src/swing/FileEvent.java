package swing;

import java.util.EventObject;

public class FileEvent extends EventObject{

	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String text; 
	public FileEvent(Object source) {
		super(source);
	}
	public FileEvent(Object source, String text) {
		super(source);
		this.text = text;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	
}
