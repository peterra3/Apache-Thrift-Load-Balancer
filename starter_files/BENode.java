import java.net.InetAddress;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TFramedTransport;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransportFactory;

public class BENode {
	static Logger log;

	public static void main(String[] args) throws Exception {
		if (args.length != 3) {
			System.err.println("Usage: java BENode FE_host FE_port BE_port");
			System.exit(-1);
		}

		// initialize log4j
		BasicConfigurator.configure();
		log = Logger.getLogger(BENode.class.getName());

		String hostFE = args[0];
		int portFE = Integer.parseInt(args[1]);
		int portBE = Integer.parseInt(args[2]);
		log.info("Launching BE node on port " + portBE + " at host " + getHostName());

		// Ping FENode that an instance of a BENode connected
		pingFrontend(hostFE, portFE, portBE);

		// launch Thrift server
		BcryptService.Processor processor = new BcryptService.Processor<BcryptService.Iface>(
				new BcryptServiceHandler());
		TServerSocket socket = new TServerSocket(portBE);
		TSimpleServer.Args sargs = new TSimpleServer.Args(socket);
		sargs.protocolFactory(new TBinaryProtocol.Factory());
		sargs.transportFactory(new TFramedTransport.Factory());
		sargs.processorFactory(new TProcessorFactory(processor));
		// sargs.maxWorkerThreads(64);
		TSimpleServer server = new TSimpleServer(sargs);
		server.serve();
	}

	static String getHostName() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (Exception e) {
			return "localhost";
		}
	}

	static void pingFrontend(String host, int portFE, int portBE) {
		TTransport transport = null;

		try {
			// Setup client connection to FENode to let it know BENode is ready for work
			TSocket sock = new TSocket(host, portFE);
			transport = new TFramedTransport(sock);
			TProtocol protocol = new TBinaryProtocol(transport);
			BcryptService.Client client = new BcryptService.Client(protocol);
			transport.open();
			log.info("Pinging FENode port " + portFE + " from BENode port " + portBE);

			// Send ping 
			boolean temp = client.ping(host, portBE);
			log.info(temp);


		} catch(Exception e) {
			log.info("Failed to ping FENode port " + portFE + " from BENode port " + portBE + ". Exception: "  + e);
		} finally{
			transport.close();
		}
		// Close the socket?


	}
}
