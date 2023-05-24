import java.net.*;
import java.io.*;

public class MailServerThread extends Thread {
    private Socket socket = null;
    public String currentuser = null;
    public int currentuserid ;

    public MailServerThread(Socket socket) {
	super("MailServerThread");
	this.socket = socket;
    }

    public void run() {

	try {
	    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
	    BufferedReader in = new BufferedReader(
				    new InputStreamReader(
				    socket.getInputStream()));

	    String inputLine;
	    int id;
	    
	    out.println("Hello,");
	    
	    while (true) {
	    	out.println("what do you want to do?");
		    out.println("> Login");
		    out.println("> Register");
		    out.println("> Exit");
		    out.println("Waiting input");
	    	inputLine = in.readLine();
	    	if(inputLine.equalsIgnoreCase("Login")) {
	    		if(!login(in,out))
	    			out.println("Invalid user or password");
	    		
	    		else{
	    			out.println("Welcome,");
	    			while(true) {
		    	    	out.println("what do you want to do?");
		    		    out.println("> NewEmail");
		    		    out.println("> ShowEmails");
		    		    out.println("> ReadEmail");
		    		    out.println("> DeleteEmail");
		    		    out.println("> Logout");
		    		    out.println("> Exit");
		    		    out.println("Waiting input");
		    	    	inputLine = in.readLine();
		    		    if(inputLine.equalsIgnoreCase("NewEmail")) {
		    		    	newEmail(in, out);
		    		    }
		    		    else if(inputLine.equalsIgnoreCase("ShowEmails")) {
		    		    	showEmail(out);
		    		    }
		    		    else if(inputLine.equalsIgnoreCase("ReadEmail")) {
		    		    	out.println("give the id of the email");
			    		    out.println("Waiting input");
			    	    	inputLine = in.readLine();
			    	    	try {
			    	    		id = Integer.parseInt(inputLine);
			    	    	}
			    		    catch (NumberFormatException nfe) {
			    		    	id=Integer.MIN_VALUE;
			    		    }
		    		    	readEmail(id,out);
		    		    }
		    		    else if(inputLine.equalsIgnoreCase("DeleteEmail")) {
		    		    	out.println("give the id of the email");
			    		    out.println("Waiting input");
			    	    	inputLine = in.readLine();
			    	    	try {
			    	    		id = Integer.parseInt(inputLine);
			    	    	}
			    		    catch (NumberFormatException nfe) {
			    		    	id=Integer.MIN_VALUE;
			    		    }
			    	    	
		    		    	deleteEmail(id,out);
		    		    }
		    		    else if(inputLine.equalsIgnoreCase("logout")) {
		    		    	break;
		    		    }
		    		    else if(inputLine.equalsIgnoreCase("Exit")) {
		    		    	exit(in,out);
		    		    	return;
		    		    }
		    		    else {
		    		    	out.println("I don't understand , try again");
		    		    }
	    			}
	    		}
	    	}
	    	else if(inputLine.equalsIgnoreCase("Register")) {
	    		register(in,out);
	    	}
	    	else if(inputLine.equalsIgnoreCase("Exit")) {
		    	exit(in,out);
		    	return;
			}
	    	else {
	    		out.println("I don't understand , try again");
	    	}
	    }

	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
    
    public void register(BufferedReader in,PrintWriter out) {
    	String inputLine;
    	String username;
    	try {
			out.println("Write a Username");
		    out.println("Waiting input");
			inputLine = in.readLine();
			for(Account acc: MailServer.Accounts) {
				if(acc.username.equals(inputLine)) {
					out.println("Username taken");
					return;
				}
			}
			username = inputLine;
			out.println("Write a Password");
		    out.println("Waiting input");
    		inputLine = in.readLine();
    		MailServer.Accounts.add(new Account(username , inputLine));
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    }
    
    public boolean login(BufferedReader in,PrintWriter out) {
    	String inputLine;
    	String username;
    	try {
			out.println("Write your Username");
		    out.println("Waiting input");
			inputLine = in.readLine();
			for(Account acc: MailServer.Accounts) {
				if(acc.username.equals(inputLine)) {
					username = inputLine;
					out.println("Write your Password");
	    		    out.println("Waiting input");
					inputLine = in.readLine();
					if(!acc.password.equals(inputLine)) {
						return false;
					}
					currentuserid = MailServer.Accounts.indexOf(acc);
					currentuser = username;
					return true;
				}
			}
			return false;
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return false;
    }
    public void newEmail(BufferedReader in,PrintWriter out) throws IOException {
    	String inputLine;
    	String reciever;
    	String subject;
    	out.println("Write the Reciever");
	    out.println("Waiting input");
	    inputLine = in.readLine();
	    for(Account acc: MailServer.Accounts) {
			if(acc.username.equals(inputLine)) {
				reciever = inputLine;
		    	out.println("Write the Subject");
			    out.println("Waiting input");
			    inputLine = in.readLine();
			    subject = inputLine;
				out.println("Write the email");
			    out.println("Waiting input");
			    inputLine = in.readLine();
			    acc.mailbox.add(new Email(currentuser, reciever, subject, inputLine));
			    out.println("email sent");
			    return;
			}
	    }
	    out.println("reciever doesn't exist");
    }
    public void showEmail(PrintWriter out) {
    	
    	if(MailServer.Accounts.get(currentuserid).mailbox.size() == 0) {
    		out.println("empty mailbox");
    		return;
    	}
    		
    	int i=1;
		String newinfo= "    ";
		for(Email mail: MailServer.Accounts.get(currentuserid).mailbox) {	
			if(mail.isNew) {
				newinfo = "New!";
			}
			out.println(i+") "+newinfo+" "+mail.sender+" "+mail.subject);
			i++;
		}
    				
    }
    public void readEmail(int id,PrintWriter out){
    	if(id <= 0) {
    		out.println("Not a valid id");
    		return;
    	}
    	if(id-1 >= MailServer.Accounts.get(currentuserid).mailbox.size()) {
    		out.println("there is no email with that id");
    		return;
    	}
    	if(MailServer.Accounts.get(currentuserid).mailbox.get(id-1).isNew)
    		MailServer.Accounts.get(currentuserid).mailbox.get(id-1).isNew = false;
    	
    	out.println("Sender: "+MailServer.Accounts.get(currentuserid).mailbox.get(id-1).sender);
    	out.println("Subject: "+MailServer.Accounts.get(currentuserid).mailbox.get(id-1).subject);
    	out.println(MailServer.Accounts.get(currentuserid).mailbox.get(id-1).mainbody);
    	out.println(" ");
    }
    public void deleteEmail(int id,PrintWriter out){
    	if(id <= 0) {
    		out.println("Not a valid id");
    		return;
    	}
    	if(id-1 >= MailServer.Accounts.get(currentuserid).mailbox.size()) {
    		out.println("there is no email with that id");
    		return;
    	}
    	MailServer.Accounts.get(currentuserid).mailbox.remove(id-1);
    	out.println("email deleted");
    }
    public void exit(BufferedReader in,PrintWriter out) throws IOException {
		out.println("Exiting...");
	    out.close();
	    in.close();
	    socket.close();
    }
}
