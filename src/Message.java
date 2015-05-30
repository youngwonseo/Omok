import java.io.Serializable;


public class Message implements Serializable{
	
	MessageType type;
	String chat;
	int x,y;
	
	public Message(MessageType type,String data){
		this.type = type;
		
		switch(type){
		case COORDINATE:
			String[] xy = data.trim().split(",");
			x = Integer.parseInt(xy[0]);
			y = Integer.parseInt(xy[1]);
			break;
		case CHAT:
			this.chat =  data;
			break;
		}
		
	}
	
	
	public MessageType getType(){
		return type;
	}
	
	
	
		
	
	
	
	
}
