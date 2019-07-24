package swing;

import java.util.EventListener;

public interface ToolbarListener extends EventListener {
	public void toolbarEventOccurred(ToolbarEvent ev);
}
