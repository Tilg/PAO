package testAgent;


import java.util.Iterator;


import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class SenderAgent extends Agent {
	
	String service = "";

	protected void setup() {
		System.out.println("Hello. My name is " + this.getLocalName());
		Object[] args = getArguments();
		String message = (String) args[0];
		AID aid = new AID();

		DFAgentDescription dfd = new DFAgentDescription();

		try {
			DFAgentDescription[] result = DFService.search(this, dfd);
			String out = "";
			int i = 0;
			String service = "";

			System.out.println("taille de result : " + result.length);
			while ((service.compareTo("receiver") != 0) && (i < result.length)) {
				System.out.println("coucou1");
				DFAgentDescription desc = (DFAgentDescription) result[i];
				Iterator iter2 = desc.getAllServices();
				while (iter2.hasNext()) {
					ServiceDescription sd = (ServiceDescription) iter2.next();
					service = sd.getName();
					if (service.compareTo("receiver") == 0) {
						aid = desc.getName();
						break;
					}
				}
				System.out.println(aid.getName());

				sendMessage(message, aid);
				System.out.println("coucou2");
				i++;
			}
		} catch (Exception fe) {
		}

	}

	private void sendMessage(String mess, AID id) {
		try {
			ACLMessage aclMessage = new ACLMessage(ACLMessage.REQUEST);
			aclMessage.addReceiver(id);

			aclMessage.setContent(mess);

			this.send(aclMessage);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void registerService() {
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(this.getAID());

		ServiceDescription sd = new ServiceDescription();
		sd.setType(service);
		sd.setName(service);

		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			System.err.println(getLocalName()
					+ " registration with DF unsucceeded. Reason: "
					+ e.getMessage());
			doDelete();
		}

	}

}
