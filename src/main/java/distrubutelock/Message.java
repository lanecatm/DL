package distrubutelock;
/** 
* @author xfhuang 
* @email lanecatm@sjtu.edu.cn
* @date 2017年6月27日 下午6:04:50
* @version 
* Introduction
*/
public class Message {
	
	public static class Status{
		
		//客户端发给服务器端
		public static final int TRY_LOCK = 1;
		public static final int TRY_RELEASE = 2;
		public static final int CHECK_STATUS = 3;
		
		//服务器发给服务器
		public static final int LOCK = 4;
		public static final int RELEASE = 5;
		public static final int BRODCAST = 6;
		
		//服务器发给客户
		public static final int SUCC = 7;
		public static final int FAILED = 8;
	}
	
	@Override
	public String toString() {
		return "Message [status=" + status + ", clientId=" + clientId + ", lockKey=" + lockKey + ", isServer="
				+ isServer + "]";
	}
	
	private int status;
	private String clientId;
	private String lockKey;
	private Boolean isServer;
	
	
	public Message(int status, String clientId, String lockKey, Boolean isServer) {
		super();
		this.status = status;
		this.clientId = clientId;
		this.lockKey = lockKey;
		this.isServer = isServer;
	}
	
	
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getLockKey() {
		return lockKey;
	}
	public void setLockKey(String lockKey) {
		this.lockKey = lockKey;
	}
	public Boolean getIsServer() {
		return isServer;
	}
	public void setIsServer(Boolean isServer) {
		this.isServer = isServer;
	}
	

	

}
