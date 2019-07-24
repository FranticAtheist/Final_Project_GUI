package swing;

public class TypeCategory {
	private int id;
	private String text;
	public TypeCategory(int id, String text) {
	this.id = id;
	this.text = text;
	}
	public String toString() {
		return text;
	}
	public int getId() {
		return id;
	}
}
