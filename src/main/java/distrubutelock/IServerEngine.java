package distrubutelock;
/** 
* @author xfhuang 
* @email lanecatm@sjtu.edu.cn
* @date 2017年6月27日 下午4:10:10
* @version 
* Introduction
*/
public interface IServerEngine {
	
	public String handleTryLock(String clientId, String lockKey);
	
	public String handleTryRelease(String clientId, String lockKey);
	
	public String handleCheck(String clientId, String lockKey);
	
	

}
