package testAgent;

import java.security.acl.Acl;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class AgentB extends Agent {

	int somme = 0;
	boolean stop = false;

	protected void setup() {
		System.out.println("----------------agent B----------------");
		FSMBehaviour agentB_beh = new FSMBehaviour();

		agentB_beh.registerFirstState(new attendrechiffre(), "attendrechiffre");
		agentB_beh.registerState(new afficher(), "afficher");
		agentB_beh.registerState(new fin(), "fin");

		agentB_beh.registerTransition("attendrechiffre", "afficher", 0);
		agentB_beh.registerTransition("attendrechiffre", "fin", 1);
		agentB_beh.registerDefaultTransition("afficher", "attendrechiffre");

		addBehaviour(agentB_beh);
	}

	private class attendrechiffre extends OneShotBehaviour {

		int valeurRetour = 0;

		public void action() {
			ACLMessage message = new ACLMessage(ACLMessage.INFORM);
			message.addReceiver(new AID("AgentA", AID.ISLOCALNAME));

			if (!stop) {
				message.setContent("pret");
				send(message);
				valeurRetour = 0;
				block();
			} else {
				message.setContent("arret");
				send(message);
				valeurRetour = 1;
			}
		}

		public int onEnd() {
			return valeurRetour;
		}
	}

	/**********************************************************************/
	private class afficher extends OneShotBehaviour {
		@Override
		public void action() {
			ACLMessage messageRecu = receive();
			somme += Integer.parseInt(messageRecu.getContent());
			System.out.println("message recu= " + messageRecu.getContent());
			System.out.println("la somme actuelle = " + somme);
			if (somme > 50)
				stop = true;
		}
	}

	/**********************************************************************/
	private class fin extends OneShotBehaviour {
		@Override
		public void action() {
			System.out.println("fin de l'agent");
			myAgent.doDelete();
		}
	}
}
