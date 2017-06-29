package distrubutelock;

import static org.junit.Assert.*;

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
		new Thread(new ServerMainThread(50000, true)).start();
		new Thread(new ServerMainThread(50001, false)).start();
		new Thread(new ServerMainThread(50002, false)).start();

		//上锁
		Message message = new Message(Message.Status.TRY_LOCK, "Client 1", "1", false);
		try {
			Message backMessage = SocketUtil.sendMessage("127.0.0.1", 50000, message);
			assertEquals(backMessage.getStatus(), Message.Status.SUCC);
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		
		//同一个客户端对一服务器同一个锁继续上锁
		try {
			Message backMessage = SocketUtil.sendMessage("127.0.0.1", 50000, message);
			assertEquals(backMessage.getStatus(), Message.Status.FAILED);
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		
		//同一个客户端对不同服务器同一个锁继续上锁
		try {
			Message backMessage = SocketUtil.sendMessage("127.0.0.1", 50001, message);
			assertEquals(backMessage.getStatus(), Message.Status.FAILED);
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		
		//不同客户端对同一服务器同一个锁继续上锁
		Message message1 = new Message(Message.Status.TRY_LOCK, "Client 2", "1", false);
		try {
			Message backMessage = SocketUtil.sendMessage("127.0.0.1", 50000, message1);
			assertEquals(backMessage.getStatus(), Message.Status.FAILED);

		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		
		//不同客户端对不同服务器同一个锁继续上锁
		try {
			Message backMessage = SocketUtil.sendMessage("127.0.0.1", 50002, message1);
			assertEquals(backMessage.getStatus(), Message.Status.FAILED);

		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		

		
		
		//检查锁状态
		Message message2 = new Message(Message.Status.CHECK_STATUS, "Client 1", "1", false);
		try {
			Message backMessage = SocketUtil.sendMessage("127.0.0.1", 50000, message2);
			assertEquals(backMessage.getStatus(), Message.Status.SUCC);
			assertEquals(backMessage.getClientId(), "Client 1");
			assertEquals(backMessage.getLockKey(), "1");

		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		
		Message message3 = new Message(Message.Status.CHECK_STATUS, "Client 2", "1", false);
		try {
			Message backMessage = SocketUtil.sendMessage("127.0.0.1", 50001, message3);
			assertEquals(backMessage.getStatus(), Message.Status.SUCC);
			assertEquals(backMessage.getClientId(), "Client 1");
			assertEquals(backMessage.getLockKey(), "1");
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		Message message7 = new Message(Message.Status.CHECK_STATUS, "Client 2", "3", false);
		try {
			Message backMessage = SocketUtil.sendMessage("127.0.0.1", 50001, message7);
			assertEquals(backMessage.getStatus(), Message.Status.FAILED);

		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		
		//错误客户端释放锁
		Message message4 = new Message(Message.Status.RELEASE, "Client 2", "1", false);
		try {
			Message backMessage = SocketUtil.sendMessage("127.0.0.1", 50001, message4);
			assertEquals(backMessage.getStatus(), Message.Status.FAILED);
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		try {
			Message backMessage = SocketUtil.sendMessage("127.0.0.1", 50000, message4);
			assertEquals(backMessage.getStatus(), Message.Status.FAILED);
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		//正确客户端释放锁
		Message message5 = new Message(Message.Status.TRY_RELEASE, "Client 1", "1", false);
		try {
			Message backMessage = SocketUtil.sendMessage("127.0.0.1", 50000, message5);
			assertEquals(backMessage.getStatus(), Message.Status.SUCC);
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		//再次释放锁
		try {
			Message backMessage = SocketUtil.sendMessage("127.0.0.1", 50000, message5);
			assertEquals(backMessage.getStatus(), Message.Status.FAILED);
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		
		//检查锁状态
		Message message6 = new Message(Message.Status.CHECK_STATUS, "Client 1", "1", false);
		try {
			Message backMessage = SocketUtil.sendMessage("127.0.0.1", 50000, message6);
			assertEquals(backMessage.getStatus(), Message.Status.FAILED);

		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}

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
				System.out.println("与客户端连接成功！");
				// 为每个客户端连接开启一个线程
				new Thread(new ServerThread(client, isLeader)).start();
			}
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
