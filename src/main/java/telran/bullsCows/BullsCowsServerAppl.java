package telran.bullsCows;

import telran.net.Protocol;
import telran.net.TcpServer;

public class BullsCowsServerAppl {
	private static final int PORT = 5000;
	public static void main(String[] args) {
		BullsCowsService bullsCowsService  = new BullsCowsMapImpl();
		Protocol protocol = new BullsCowsProtocol(bullsCowsService);
		TcpServer tcpServer = new TcpServer(PORT, protocol);
		tcpServer.run();
	}

}
