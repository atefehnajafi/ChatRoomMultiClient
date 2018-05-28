import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.WritableByteChannel;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.util.ArrayList;
import java.util.Scanner;

public class Server {

	static ServerSocket server;

	public static void main(String[] args) throws IOException {

		server = new ServerSocket(8090);

		ArrayList<User> userList = new ArrayList<>();
		ArrayList<User> onlineUserList = new ArrayList<>();
		ArrayList<User> busyUserList=new ArrayList<>();

		User a = new User("a", "1");
		User b = new User("m", "2");
		User c = new User("z", "3");
		User d = new User("w", "4");

		userList.add(a);
		userList.add(b);
		userList.add(c);
		userList.add(d);

		while (true) {

			Socket client = server.accept();
			User newUser = new User("test", "1", client);
			
			
			Thread ServerRead = new Thread() {

				@Override
				public void run() {
					try {
						int flag=0;
						User partner = null;
						User findUser=null;
				
						newUser.output.writeBytes("select one item from (login - list - connect - close)" + "\n");
						
					
						while (true) {				
								
							String message = newUser.input.readLine();

						

							if (message.equals("login")) {

								User onlineUser;
								newUser.output.writeBytes("username:" + "\n");
								String _newUsername = newUser.input.readLine();

								newUser.output.writeBytes("password:" + "\n");
								String _newPassword = newUser.input.readLine();

								onlineUser = checkUserPass(_newUsername, _newPassword, newUser, userList);

								
								newUser.setUsername(onlineUser.getUsername());
								newUser.setPassword(onlineUser.getPassword());


								onlineUserList.add(newUser);
								//flag=1;
								newUser.output.writeBytes("select one item from (list - connect - close)" + "\n");
								message = newUser.input.readLine();
							}

							if (message.equals("list")) {
								String onlineList = "";
								for (int i = 0; i < onlineUserList.size(); i++) {
									if (!(newUser.getUsername().equals(onlineUserList.get(i).getUsername()))) {
										onlineList = onlineUserList.get(i).getUsername() + "   " + onlineList;
									}
								}
								newUser.output.writeBytes(onlineList + "\n");

								newUser.output.writeBytes("select one item from (connect - close)" + "\n");
								message = newUser.input.readLine();
							}

							if (message.equals("connect")) {
								newUser.output.writeBytes("select your partner : " + "\n");
								String partnerName = newUser.input.readLine();

								partner = checkPartner(partnerName, onlineUserList, newUser);
								newUser.setPartner(partner);
								partner.setPartner(newUser);
								
								onlineUserList.remove(partner);
								onlineUserList.remove(newUser);
								busyUserList.add(partner);
								busyUserList.add(newUser);
							
							}

							else {							
								newUser.partner.output.writeBytes(message + "\n");	
								
								if(message.equals("close"))
								{
									newUser.setPartner(null);
									partner.setPartner(null);
									newUser.getSocket().close();
									partner.getSocket().close();
									busyUserList.remove(partner);
									busyUserList.remove(newUser);
								}
							
							}

						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};

			ServerRead.start();
		}

	}

	public static User checkUserPass(String _username, String _password, User newuser, ArrayList<User> users)
			throws IOException {
		User _onlineUser = null;
		loop: for (int i = 0; i < users.size(); i++) {
			if (users.get(i).getUsername().equals(_username) && users.get(i).getPassword().equals(_password)) {
				try {
					_onlineUser = users.get(i);
					newuser.output.writeBytes("Login successfully \n");
					break loop;
				} catch (Exception e) {
					// TODO: handle exception
				}

			} else {
				//newuser.output.writeBytes("the username is not exist");
			}
		}
		return _onlineUser;

	}

	public static User checkPartner(String _username, ArrayList<User> users, User _newUser) throws IOException {
		User partner = null;
		for (int i = 0; i < users.size(); i++) {
			if (users.get(i).getUsername().equals(_username)) {
				partner = users.get(i);
				return partner;
			} else{}
				//_newUser.output.writeBytes("partner is not exist ! ");
		}
		return partner;
	}

	
	public static User sertchUser(Socket client,ArrayList<User> onlineUSer,User user) {
		User findUser=null;
		for(int i=0;i<onlineUSer.size();i++)
		{
			if (onlineUSer.get(i).getSocket().equals(user.getSocket())) {
				findUser=onlineUSer.get(i);
			}
		}
		return findUser;
	}
	

	
	
}
