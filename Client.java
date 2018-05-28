import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

	static DataOutputStream output;
	static DataInputStream input;

	static String messageReeive = "";

	static String messageSend = "";

	public static void main(String[] args) throws UnknownHostException, IOException {

		Scanner sc = new Scanner(System.in);
		Socket socket = new Socket("127.0.0.1", 8090);

		Thread ClientWrite = new Thread() {
			@Override
			public void run() {
				try {
					
					while (!messageSend.equals("close")) {
						output = new DataOutputStream(socket.getOutputStream());
						messageSend = sc.nextLine();
						output.writeBytes(messageSend + "\n");
					}
					if(messageSend.equals("q"))
					 socket.close();
				}

				catch (Exception e) {
				}
			}
		};

		Thread ClientRead = new Thread() {
			@Override
			public void run() {
				try {
					
					while (!messageReeive.equals("close")) {
						input = new DataInputStream(socket.getInputStream());
						messageReeive = input.readLine();
						System.out.println(messageReeive);
					}
					if(messageSend.equals("q"))
						 socket.close();
				

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};

		ClientRead.start();
		ClientWrite.start();

	}

}
