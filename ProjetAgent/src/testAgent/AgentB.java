package testAgent;

import java.security.acl.Acl;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class AgentB extends Agent {


	protected void setup() {
		ACLMessage message= null;
		while(message == null){
			message = receive();
		}
		String contenu = message.getContent();
		String ontologie = message.getOntology();
	}
}
