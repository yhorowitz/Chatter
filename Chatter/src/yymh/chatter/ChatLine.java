package yymh.chatter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatLine implements Comparable{

	public static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

	String senderName;
	String message;
	Date timeStamp;
	
	ChatLine(String sender, String msg, Date timeStamp) {
		this.senderName = sender;
		this.message = msg;
		this.timeStamp = timeStamp;
	}
	
	@Override
	public int compareTo(Object o) {

		if (this.timeStamp.compareTo(((ChatLine)o).timeStamp) == -1) {
			return -1;
		}
		else if (this.timeStamp.compareTo(((ChatLine)o).timeStamp) == 1) {
			return 1;
		}
		
		return 0;
	}
	
	@Override
	public String toString() {
		
		String time = timeFormat.format(this.timeStamp);
		return time + " " + this.senderName + ": " + this.message;
	}
	
}
