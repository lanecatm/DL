package distrubutelock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * @author xfhuang
 * @email lanecatm@sjtu.edu.cn
 * @date 2017年6月27日 下午4:26:59
 * @version Introduction
 */
public class SocketUtil {
	public static void sendMessage(Socket client, Message message) throws IOException {
		// 获取Socket的输出流，用来向客户端发送数据
		PrintStream out = new PrintStream(client.getOutputStream());
		// 将接收到的字符串前面加上echo，发送到对应的客户端
		out.println(message.getClientId());
		out.println(message.getLockKey());
		out.println(message.getStatus());
		out.println(message.getIsServer());
		// out.close();
	}

	public static Message getMessage(Socket client) throws IOException {
		// 获取Socket的输入流，用来接收从客户端发送过来的数据
		BufferedReader buf = new BufferedReader(new InputStreamReader(client.getInputStream()));

		// 接收从客户端发送过来的数据
		String clientId = buf.readLine();

		String lockKey = buf.readLine();
		int status = Integer.parseInt(buf.readLine());
		Boolean isServer = Boolean.parseBoolean(buf.readLine());

		Message message = new Message(status, clientId, lockKey, isServer);
		return message;
	}

	public static Message sendMessage(String ip, int host, Message message) throws IOException {

		// 客户端请求与本机在20006端口建立TCP连接
		Socket client = new Socket(ip, host);
		client.setSoTimeout(10000);

		SocketUtil.sendMessage(client, message);
		Message backMessage = null;
		try {
			// 从服务器端接收数据有个时间限制（系统自设，也可以自己设置），超过了这个时间，便会抛出该异常
			backMessage = SocketUtil.getMessage(client);
			System.out.println(backMessage);
		} catch (SocketTimeoutException e) {
			System.out.println("Time out, No response");
		}
		if (client != null) {
			// 如果构造函数建立起了连接，则关闭套接字，如果没有建立起连接，自然不用关闭
			client.close(); // 只关闭socket，其关联的输入输出流也会被关闭
		}
		return backMessage;
	}
}
