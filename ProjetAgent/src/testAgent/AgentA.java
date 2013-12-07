package testAgent;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class AgentA extends Agent {

	protected void setup() {
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.setContent("coucou");
		message.setOntology("caca");
		message.addReceiver(new AID("B", AID.ISLOCALNAME));
		this.send(message);
	}
}
