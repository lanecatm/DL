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
		SocketUtil.sendMessage("127.0.0.1", 20006, message);
	}
	
	
	
	
	

}
