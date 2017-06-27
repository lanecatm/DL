package distrubutelock;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author xfhuang
 * @email lanecatm@sjtu.edu.cn
 * @date 2017年6月27日 下午4:08:12
 * @version Introduction
 */
public class ServerMain
{
	public static void main(String[] args) throws Exception {
		//TODO 从服务器中读出端口号
		int port = 20006;
		
		// 服务端在端口监听客户端请求的TCP连接
		ServerSocket server = new ServerSocket(port);
		Socket client = null;
		
		boolean f = true;
		while (f) {
			// 等待客户端的连接，如果没有获取连接
			client = server.accept();
			System.out.println("与客户端连接成功！");
			// 为每个客户端连接开启一个线程
			new Thread(new ServerThread(client)).start();
		}
		server.close();
	}
}

class ServerThread implements Runnable {

	private Socket client = null;

	public ServerThread(Socket client) {
		this.client = client;
	}

	@Override
	public void run() {
		try {
			//接受消息
			Message message = SocketUtil.getMessage(client);
			
			//修改消息
			//TODO 把消息放到ServerEngine里面处理
			//     下面2行删掉，改成new一个新回复消息
			message.setLockKey("server" + message.getLockKey());
			message.setClientId("server" + message.getClientId());
			
			//发送消息
			SocketUtil.sendMessage(client, message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
