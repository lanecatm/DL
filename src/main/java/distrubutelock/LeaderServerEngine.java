package distrubutelock;

public class LeaderServerEngine implements IServerEngine {

	public Message handleTryLock(String clientId, String lockKey) {
		// TODO Auto-generated method stub
		return lock(clientId, lockKey);
	}

	public Message handleTryRelease(String clientId, String lockKey) {
		// TODO Auto-generated method stub
		return release(clientId, lockKey);
	}

	public Message handleCheck(String clientId, String lockKey) {
		// TODO Auto-generated method stub
		return null;
	}

	public Message lock(String clientId, String lockKey) {

		return null;

	}

	public Message release(String clientId, String lockKey) {

		return null;
	}

	public Message handle(Message message) {
		Message returnMessage = null;
		switch (message.getStatus()) {
		case Message.Status.TRY_LOCK:
			returnMessage = handleTryLock(message.getClientId(), message.getLockKey());
			break;
		case Message.Status.TRY_RELEASE:
			returnMessage = handleTryRelease(message.getClientId(), message.getLockKey());
			break;
		case Message.Status.CHECK_STATUS:
			returnMessage = handleCheck(message.getClientId(), message.getLockKey());
			break;
		case Message.Status.LOCK:
			if (message.getIsServer()) {
				returnMessage = lock(message.getClientId(), message.getLockKey());
			} else {
				System.out.println("Error, leader server receive client:" + message.toString());
				returnMessage = new Message(Message.Status.FAILED, "", "", true);
			}
			break;
		case Message.Status.RELEASE:
			if (message.getIsServer()) {
				returnMessage = release(message.getClientId(), message.getLockKey());
			} else {
				System.out.println("Error, leader server receive client:" + message.toString());
				returnMessage = new Message(Message.Status.FAILED, "", "", true);
			}
			break;
		default:
			System.out.println("Error, leader server receive message:" + message.toString());
			returnMessage = new Message(Message.Status.FAILED, "", "", true);
		}

		return returnMessage;
	}

}
