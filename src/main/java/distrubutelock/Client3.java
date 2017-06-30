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

public class Client3 {
	

	

	public static void main(String[] args) throws IOException {
		System.out.println("Client 3, at port 50013");
		Client.start(50013, "Client 3");

	}
	
	
	


}
