package swing;

import java.util.EventObject;

public class CheckBoxEvent extends EventObject {
	private boolean editable;
	public CheckBoxEvent(Object source) {
		super(source);
		
	}
	public CheckBoxEvent(Object source, boolean editable) {
		super(source);
		this.editable = editable;
		
	}
	public Boolean isEditable() {
		return editable;
	}
	

}
