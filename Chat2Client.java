import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Chat2Client extends Thread {

	static DataInputStream input;
	static DataOutputStream output;

	Socket client1;
	Socket client2;

	public Chat2Client(Socket client1, Socket client2) {
		this.client1 = client1;
		this.client2 = client2;
	}

	@Override
	public void run() {

		Thread ServerRead1 = new Thread() {
			String message = "";

			@Override
			public void run() {

				try {

					while (!message.equals("q")) {
						input = new DataInputStream(client1.getInputStream());
						message = input.readLine();

						output = new DataOutputStream(client2.getOutputStream());
						output.writeBytes(message + "\n");

					}
					if (message.equals("q")) {
						client1.close();
						client2.close();
					}
					// }

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		};

		ServerRead1.start();

		Thread ServerRead2 = new Thread() {
			String message = "";

			@Override
			public void run() {

				try {
					while (!message.equals("q")) {
						input = new DataInputStream(client2.getInputStream());
						message = input.readLine();
						// System.out.println(message);

						output = new DataOutputStream(client1.getOutputStream());
						output.writeBytes(message + "\n");

					}
					if (message.equals("q")) {
						client1.close();

						client2.close();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		};

		ServerRead2.start();
	}

}
