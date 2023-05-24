import java.util.ArrayList;

public class Account {
	public String username;
	public String password;
	public ArrayList<Email> mailbox;
	
	public Account(String username,String password){
		this.username = username;
		this.password = password;
		mailbox = new ArrayList<Email>();
	}
}