package distrubutelock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.List;

import distrubutelock.Message.Status;

/**
 * @author xfhuang
 * @email lanecatm@sjtu.edu.cn
 * @date 2017年6月27日 下午4:09:10
 * @version Introduction
 */

public class Client {

	public static void start(int port, String clientId) throws IOException {
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("输入server port:");
		int serverId = Integer.parseInt(input.readLine());
		while (true) {
			// 获取键盘输入
			
			System.out.print("输入lock key:");
			String lockKey = input.readLine();
			System.out.print("输入操作id: [1]Lock [2]Release [3]Check ");
			int status = Integer.parseInt(input.readLine());
			Message message = new Message(status, clientId, lockKey, false);
			// TODO 从xml读入Server的列表，根据选择的id向对应的server发送请求
			Message backMessage = SocketUtil.sendMessage("127.0.0.1", serverId, message);
			
			switch (backMessage.getStatus()) {
			case Message.Status.SUCC:
				System.out.print("[Success] ");
				System.out.println(message.getStatus() == Status.CHECK_STATUS ? backMessage.getClientId() + " own the lock key No." + backMessage.getLockKey():"");
				break;
			case Message.Status.FAILED:
				System.out.print("[Fail] ");
				System.out.println(message.getStatus() == Status.CHECK_STATUS ? "Lock key No." + backMessage.getLockKey() + " is empty":"");
				break;
			}
		}
	}

}
