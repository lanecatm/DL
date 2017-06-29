package distrubutelock;

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
		Message message = new Message(Message.Status.TRY_LOCK, "client1", "1", false);
		try {
			SocketUtil.sendMessage("127.0.0.1", 20006, message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
