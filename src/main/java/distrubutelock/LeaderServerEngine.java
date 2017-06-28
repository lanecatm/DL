package distrubutelock;

import java.io.IOException;
import java.util.List;

public class LeaderServerEngine implements IServerEngine {
	private void broadcast(Message message) {
		List<ServerInfo> followers = ClusterInfo.getFollowers();
		for (ServerInfo element : followers) {
			try {
				Client.sendMessage(element.ip, element.port, message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Message handleTryLock(String clientId, String lockKey) {
		return lock(clientId, lockKey);
	}

	public Message handleTryRelease(String clientId, String lockKey) {
		return release(clientId, lockKey);
	}

	public Message handleCheck(String clientId, String lockKey) {
		Message returnMessage = new Message(Message.Status.FAILED, clientId, lockKey, true);
		String occupier = ServerMain.map.get(lockKey);
		
		if (occupier != null) {
			returnMessage.setClientId(occupier);
			returnMessage.setStatus(Message.Status.SUCC);
		}
		
		return returnMessage;
	}

	public Message lock(String clientId, String lockKey) {
		Message returnMessage = new Message(Message.Status.FAILED, clientId, lockKey, true);
		
		if (ServerMain.map.get(lockKey) == null) {
			ServerMain.map.put(lockKey, clientId);
			returnMessage.setStatus(Message.Status.SUCC);
			broadcast(new Message(Message.Status.LOCK, clientId, lockKey, true)); 
		}
		
		return returnMessage;

	}

	public Message release(String clientId, String lockKey) {
		Message returnMessage = new Message(Message.Status.FAILED, clientId, lockKey, true);
		String occupier = ServerMain.map.get(lockKey);
		
		if ( occupier != null && occupier.equals(clientId)) {
			ServerMain.map.remove(lockKey);
			returnMessage.setStatus(Message.Status.SUCC);
			broadcast(new Message(Message.Status.RELEASE, clientId, lockKey, true));
		}
		return returnMessage;
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
