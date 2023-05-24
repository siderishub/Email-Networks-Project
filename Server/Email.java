public class Email {
	public boolean isNew;
	public String sender;
	public String receiver;
	public String subject;
	public String mainbody;
	
	public Email(String sender ,String receiver ,String subject , String mainbody){
		isNew = true;
		this.sender=sender;
		this.receiver=receiver;
		this.subject=subject;
		this.mainbody=mainbody;
	}
}