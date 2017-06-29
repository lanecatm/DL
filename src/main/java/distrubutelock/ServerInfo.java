package distrubutelock;

public class ServerInfo {
	String ip;
	int port;
	Boolean isLeader;
	
	ServerInfo(String ip, int port, Boolean isLeader) {
		this.ip = ip;
		this.port = port;
		this.isLeader = isLeader;
	}
}
