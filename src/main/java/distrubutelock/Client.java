package distrubutelock;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.List;

/**
 * @author xfhuang
 * @email lanecatm@sjtu.edu.cn
 * @date 2017年6月27日 下午4:09:10
 * @version Introduction
 */

public class Client {
	

	

	public static void main(String[] args) throws IOException {
		// 获取键盘输入
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("输入client id:");
		String clientId = input.readLine();
		System.out.print("输入server id:");
		int serverId = Integer.parseInt(input.readLine());
		System.out.print("输入lock key:");
		String lockKey = input.readLine();
		System.out.print("输入操作id:");
		int status = Integer.parseInt(input.readLine());
		Message message = new Message(Message.Status.TRY_LOCK, clientId, lockKey, false);
		//TODO 从xml读入Server的列表，根据选择的id向对应的server发送请求
		sendMessage("127.0.0.1", 20006, message);
	}
	
	
	
	
	public static void sendMessage(String ip, int host, Message message) throws IOException {
		// 客户端请求与本机在20006端口建立TCP连接
		Socket client = new Socket(ip, host);
		client.setSoTimeout(10000);
		
		SocketUtil.sendMessage(client, message);
		try {
			// 从服务器端接收数据有个时间限制（系统自设，也可以自己设置），超过了这个时间，便会抛出该异常
			Message backMessage = SocketUtil.getMessage(client);
			System.out.println(backMessage);
		} catch (SocketTimeoutException e) {
			System.out.println("Time out, No response");
		}
		if (client != null) {
			// 如果构造函数建立起了连接，则关闭套接字，如果没有建立起连接，自然不用关闭
			client.close(); // 只关闭socket，其关联的输入输出流也会被关闭
		}
	}

}
