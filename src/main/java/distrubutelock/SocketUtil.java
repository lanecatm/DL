package distrubutelock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

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
		//out.close();
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

}
