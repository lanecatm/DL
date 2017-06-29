package distrubutelock;

import java.io.IOException;

public class FollowerServerEngine implements IServerEngine {

	private Message requestLeader(Message message) {
		ServerInfo leader = ClusterInfo.getLeader();
		try {
			Client.sendMessage(leader.ip, leader.port, message);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Message handleTryLock(String clientId, String lockKey) {
		return requestLeader(new Message(Message.Status.LOCK, clientId, lockKey, true));
	}

	public Message handleTryRelease(String clientId, String lockKey) {
		return requestLeader(new Message(Message.Status.RELEASE, clientId, lockKey, true));
	}

	public Message handleCheck(String clientId, String lockKey) {
		Message returnMessage = new Message(Message.Status.FAILED, "", lockKey, true);
		String occupier = ServerMain.map.get(lockKey);
		
		if (occupier != null) {
			returnMessage.setClientId(occupier);
			returnMessage.setStatus(Message.Status.SUCC);
		}
		
		return returnMessage;
	}
	
	public Message lock(String clientId, String lockKey) {
		
		ServerMain.map.put(lockKey, clientId);
		
		return null;

	}
	
	public Message release(String clientId, String lockKey) {
		ServerMain.map.remove(lockKey);
		
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
		case Message.Status.SUCC:
		case Message.Status.FAILED:
			returnMessage = null;
		default:
			System.out.println("Error, leader server receive message:" + message.toString());
			returnMessage = new Message(Message.Status.FAILED, "", "", true);
		}

		return returnMessage;
	}

}
