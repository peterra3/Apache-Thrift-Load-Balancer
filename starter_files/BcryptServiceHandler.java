import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TTransportFactory;

public class BcryptServiceHandler implements BcryptService.Iface {
	public List<String> hashPassword(List<String> passwords, short logRounds)
			throws IllegalArgument, org.apache.thrift.TException {
		if (logRounds < 4 || logRounds > 30) {
			throw new IllegalArgument("logRounds must be between 4 and 30.");
		}
		try {
			List<String> hashedPasswords = new ArrayList<>();
			for (String password : passwords) {
				hashedPasswords.add(BCrypt.hashpw(password, BCrypt.gensalt(logRounds)));
			}
			return hashedPasswords;
		} catch (Exception e) {
			throw new IllegalArgument(e.getMessage());
		}
	}

	public List<Boolean> checkPassword(List<String> passwords, List<String> hashes) throws IllegalArgument, TException {
		if (passwords.size() != hashes.size()) {
			throw new IllegalArgument("Passwords and hashes lists must be of the same length.");
		}
		try {
			List<Boolean> results = new ArrayList<>();
			for (int i = 0; i < passwords.size(); i++) {
				results.add(BCrypt.checkpw(passwords.get(i), hashes.get(i)));
			}
			return results;
		} catch (Exception e) {
			throw new IllegalArgument(e.getMessage());
		}

	}

	public boolean ping(String host, int portBE) throws org.apache.thrift.TException {
		// This ping will be sent from the BENode and received in the FENode.
		// Hence, the FENode will be executing the logic below

		// TSocket sock = new TSocket(host, portBE);
		// TTransport transport = new TFramedTransport(sock);
		// TProtocol protocol = new TBinaryProtocol(transport);
		// BcryptService.Client client = new BcryptService.Client(protocol);
		// transport.open();

		// Send ping
		System.out.println("test123");

		return true;
	}

}
