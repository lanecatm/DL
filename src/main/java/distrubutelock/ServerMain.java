package distrubutelock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

import distrubutelock.Message;
import distrubutelock.SocketUtil;

/**
 * @author xfhuang
 * @email lanecatm@sjtu.edu.cn
 * @date 2017年6月27日 下午4:08:12
 * @version Introduction
 */
public class ServerMain {

	public static ConcurrentHashMap<String, String> map = new ConcurrentHashMap<String, String>();

	public static void main(String[] args) throws Exception {
		new Thread(new ServerMainThread(50000, true)).start();
		new Thread(new ServerMainThread(50001, false)).start();
		new Thread(new ServerMainThread(50002, false)).start();	
	}
}
class ServerMainThread implements Runnable {

	private int port;
	private Boolean isLeader;
	public ServerMainThread(int port, Boolean isLeader){
		this.port = port;
		this.isLeader = isLeader;
		
	}
	public void run() {

		// 服务端在端口监听客户端请求的TCP连接
		ServerSocket server;
		try {
			server = new ServerSocket(port);
			Socket client = null;

			boolean f = true;
			while (f) {
				// 等待客户端的连接，如果没有获取连接
				client = server.accept();
				// System.out.println("与客户端连接成功！");
				// 为每个客户端连接开启一个线程
				new Thread(new ServerThread(client, isLeader)).start();
			}
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}

class ServerThread implements Runnable {

	private Socket client = null;
	private Boolean isLeader;

	public ServerThread(Socket client, Boolean isLeader) {
		this.client = client;
		this.isLeader = isLeader;
	}

	public void run() {
		try {
			// 接受消息
			Message message = SocketUtil.getMessage(client);
			// System.out.println("Receive message: " + message.toString());
			
			IServerEngine serverEngine;
			if (isLeader) {
				serverEngine = new LeaderServerEngine();
			} else {
				serverEngine = new FollowerServerEngine();
				// serverEngine = new LeaderServerEngine();
			}

			// 修改消息
			// 把消息放到ServerEngine里面处理
			Message returnMessage = serverEngine.handle(message);
			if (returnMessage != null) {
				// 发送消息
				SocketUtil.sendMessage(client, returnMessage);
			}
			// System.out.println("Receive message: " + message.toString());
            client.close();  

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
