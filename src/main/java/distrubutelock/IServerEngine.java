package distrubutelock;
/** 
* @author xfhuang 
* @email lanecatm@sjtu.edu.cn
* @date 2017年6月27日 下午4:10:10
* @version 
* Introduction
*/
public interface IServerEngine {
	
	public Message handleTryLock(String clientId, String lockKey);
	
	public Message handleTryRelease(String clientId, String lockKey);
	
	public Message handleCheck(String clientId, String lockKey);
	
	
	public Message handle(Message message);
	
	

}
