import java.io.*;
import java.net.*;

public class MailClient {
    public static void main(String[] args) throws IOException {

        Socket Socket = null;
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            Socket = new Socket("127.0.0.1", 4444);
            out = new PrintWriter(Socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(Socket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Can't Find host");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection ");
            System.exit(1);
        }

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String fromServer;
        String fromUser;

        while ((fromServer = in.readLine()) != null) {
        	
            System.out.println(fromServer);
            if (fromServer.equals("Exiting..."))
                break;
		    if(fromServer.equals("Waiting input")){
		    	fromUser = stdIn.readLine();
		    	out.println(fromUser);
		    }
	    }

        out.close();
        in.close();
        stdIn.close();
        Socket.close();
    }
}
