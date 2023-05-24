import java.net.*;
import java.util.ArrayList;
import java.io.*;

public class MailServer {
	public static ArrayList<Account> Accounts;
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        boolean listening = true;
        Accounts = new ArrayList<Account>();
        Accounts.add(new Account("anto@mail.gr","123"));
        Accounts.add(new Account("work@mail.gr","pass"));
        Accounts.get(0).mailbox.add(new Email("work@mail.gr","anto@mail.gr","long time no see","we havent talked in a long time let's meet"));
        Accounts.get(0).mailbox.add(new Email("work@mail.gr","anto@mail.gr","Weird weather","Dangerous weather tomorrow , Watch out!"));
        Accounts.get(0).mailbox.add(new Email("work@mail.gr","anto@mail.gr","Waiting payment","I need the money from the loan I gave you"));
        Accounts.get(1).mailbox.add(new Email("anto@mail.gr","work@mail.gr","Welcome to work","We hope you have a good start at the job"));
        Accounts.get(1).mailbox.add(new Email("anto@mail.gr","work@mail.gr","important","this is the email read fast"));
        Accounts.get(1).mailbox.add(new Email("anto@mail.gr","work@mail.gr","Intresting proposal","A new and refreshing idea in this email"));
        try {
            serverSocket = new ServerSocket(4444);
        } catch (IOException e) {
            System.err.println("Could not listen on port 4444.");
            System.exit(-1);
        }

        while (listening)
	    new MailServerThread(serverSocket.accept()).start();

        serverSocket.close();
    }
}
