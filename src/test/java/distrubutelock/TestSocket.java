package distrubutelock;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

/** 
* @author xfhuang 
* @email lanecatm@sjtu.edu.cn
* @date 2017年6月27日 下午4:44:33
* @version 
* Introduction
*/
public class TestSocket {

	
	@Test
	public void test() {
		new Thread(new ServerMainThread(50000, true)).start();
		Message message = new Message(Message.Status.TRY_LOCK, "client1", "1", false);
		try {
			Message backMessage = SocketUtil.sendMessage("127.0.0.1", 50000, message);
			System.out.println(backMessage);
			backMessage = SocketUtil.sendMessage("127.0.0.1", 50000, message);
			System.out.println(backMessage);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
	}

}
