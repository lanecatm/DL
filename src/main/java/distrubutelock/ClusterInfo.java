package distrubutelock;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;


public class ClusterInfo {
	// private ServerInfo leader;
	// private List<ServerInfo> followers;
	
	public static ServerInfo getLeader() {
//		try {
//			//File xml = new File("servers.xml");
//			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//			DocumentBuilder builder = factory.newDocumentBuilder();
//			Document doc = builder.parse("servers.xml");
//			XPathFactory xPathfactory = XPathFactory.newInstance();
//			XPath xpath = xPathfactory.newXPath();
//			XPathExpression expr = xpath.compile("");
//		}catch (Exception e) {
//			e.printStackTrace();
//		}

		return new ServerInfo("127.0.0.1", 50000, true);
	}
	
	public static List<ServerInfo> getFollowers() {
		List<ServerInfo> followersList = new ArrayList<ServerInfo>();
		followersList.add(new ServerInfo("127.0.0.1", 50001, false));
		followersList.add(new ServerInfo("127.0.0.1", 50002, false));
		return followersList;
	}
}
