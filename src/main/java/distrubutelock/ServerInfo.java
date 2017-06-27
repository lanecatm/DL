package distrubutelock;

public class ServerInfo {
	String ip;
	String port;
	Boolean isLeader;
	
	ServerInfo(String ip, String port, Boolean isLeader) {
		this.ip = ip;
		this.port = port;
		this.isLeader = isLeader;
	}
}
