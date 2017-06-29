package distrubutelock;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.junit.Test;

/**
 * @author xfhuang
 * @email lanecatm@sjtu.edu.cn
 * @date 2017年6月29日 上午11:13:37
 * @version Introduction
 */
public class TestLock {

	@Test
	public void test() {
		// 在测试之前打开3个server
		new Thread(new ServerMainThread(50000)).start();
		new Thread(new ServerMainThread(50001)).start();
		new Thread(new ServerMainThread(50002)).start();
		Message message = new Message(Message.Status.TRY_LOCK, "Client 1", "1", false);
		try {
			SocketUtil.sendMessage("127.0.0.1", 50000, message);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Message message1 = new Message(Message.Status.TRY_LOCK, "Client 1", "1", false);
		try {
			SocketUtil.sendMessage("127.0.0.1", 50000, message1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			SocketUtil.sendMessage("127.0.0.1", 50001, message1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Message message2 = new Message(Message.Status.CHECK_STATUS, "Client 1", "1", false);
		try {
			SocketUtil.sendMessage("127.0.0.1", 50000, message2);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Message message3 = new Message(Message.Status.RELEASE, "Client 1", "1", false);
		try {
			SocketUtil.sendMessage("127.0.0.1", 50000, message3);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Message message4 = new Message(Message.Status.CHECK_STATUS, "Client 1", "1", false);
		try {
			SocketUtil.sendMessage("127.0.0.1", 50000, message4);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}

class ServerMainThread implements Runnable {

	private int port;
	public ServerMainThread(int port){
		this.port = port;
		
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
				System.out.println("与客户端连接成功！");
				// 为每个客户端连接开启一个线程
				// TODO 改这里
				Boolean isLeader = false;
				new Thread(new ServerThread(client, isLeader)).start();
			}
			server.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
