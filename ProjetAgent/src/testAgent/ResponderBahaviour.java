package testAgent;

import java.util.List;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.core.behaviours.SimpleBehaviour;

public class ResponderBahaviour extends SimpleBehaviour {

	private final static MessageTemplate mt = MessageTemplate
			.MatchPerformative(ACLMessage.REQUEST);

	public ResponderBahaviour(Agent agent) {
		super(agent);
	}

	public void action() {
		while (true) {
			ACLMessage aclMessage = myAgent.receive(mt);
			if (aclMessage != null) {
				try {
					String message = aclMessage.getContent();
					System.out.println(myAgent.getLocalName()
							+ ": I receive message\n" + aclMessage
							+ "\nwith content\n" + message);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else {
				this.block();
			}
		}
	}

	public boolean done() {
		return false;
	}
}
