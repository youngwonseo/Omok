import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class NetworkController {

	private Socket sock;
	private ObjectOutputStream osDataOutputStream;
	private ReceiveDataThread rt = null;
	private Message receivedMessage = null;
	private final OmokController oc;
	private int port;
	ServerSocket serverSock;
	ServerThread st;

	public NetworkController(OmokController oc) {
		this.oc = oc;
	}

	public boolean server(int port,OmokUI ou) {
		boolean result = false;
		st = new ServerThread(this,port,ou);

		this.port = port;

		try {
			st.start();
			
		} catch (Exception e) {

		}

		return result;

	}

	public void client(String serverIP, int port,OmokUI ou) {
		try {
			InetAddress serverAddr = InetAddress.getByName(serverIP);
			sock = new Socket(serverAddr.getHostName(), port);
			System.out.println("success");
			osDataOutputStream = new ObjectOutputStream(sock.getOutputStream());
			new ReceiveDataThread(this,ou).start();
			
		} catch (Exception e) {

		}
	}

	public void stopWaitClient() {
		try {
			serverSock.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean toSendMessage(Message message) {
		try {
			osDataOutputStream.writeObject(message);
			osDataOutputStream.flush();
			System.out.println("send!");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	
	
	public Message toReceiveMessage(Message message) {
		
		
		
		switch(message.type){
		case COORDINATE:
			oc.getEnemy(message.x, message.y);
			break;
		case CHAT:
			oc.getChat(message.chat);
			break;
		case WIN:
			oc.toLose();
		}
		
		return null;
	}
	
	
	
	
	class ServerThread extends Thread {
		int port;
		NetworkController nc;
		OmokUI ou;
		public ServerThread(NetworkController nc,int port,OmokUI ou) {
			this.nc = nc;
			this.port = port;
			this.ou = ou;
		}

		public synchronized void run() {
			try {
				serverSock = null;
				InetAddress serverAddr = InetAddress.getByName(null);
				serverSock = new ServerSocket(port, 1);
				System.out.println("wait..");
				sock = serverSock.accept();
				System.out.println("success");
				ou.start();
				osDataOutputStream = new ObjectOutputStream(
						sock.getOutputStream());
				new ReceiveDataThread(nc,ou).start();
				
			} catch (Exception e) {
				System.out.println("cancel..");
			}
		}
	}

	class ReceiveDataThread extends Thread {

		NetworkController nc;
		OmokUI ou;
		ObjectInputStream isDataInputStream;
		private boolean bWaitting = true;

		public ReceiveDataThread(NetworkController nc,OmokUI ou) {
			this.nc = nc;
			this.ou = ou;
			try {
				isDataInputStream = new ObjectInputStream(
						sock.getInputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public synchronized void run() {
			try {
				System.out.println("received Thread!");
				while (bWaitting) {
					System.out.println("ready to receive!");
					toReceiveMessage((Message) isDataInputStream.readObject());
					System.out.println("received!");
					ou.repaint();
				}

			} catch (Exception e) {

			} finally {

			}
		}

		public boolean stopToReceive() {
			bWaitting = false;
			return true;
		}

	}
	


}
