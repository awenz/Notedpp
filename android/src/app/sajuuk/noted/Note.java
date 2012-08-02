package app.sajuuk.noted;

public class Note {
	private String text;
	
	public String getText(){
		return text;
	}
	
	public void setText(String Text){
		text=Text;
	}
	
	public Note(String Text){
		text=Text;
	}
}
