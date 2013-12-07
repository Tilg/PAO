package testAgent;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class AgentA extends Agent {

	protected void setup() {

		System.out.println("---------------------------------------");
		System.out.println("----------------agent A----------------");
		System.out.println("---------------------------------------");

		FSMBehaviour agentA_beh = new FSMBehaviour();

		agentA_beh.registerFirstState(new attendreAgentB(), "attendreAgentB");
		agentA_beh.registerState(new envoiChiffre(), "envoiChiffre");
		agentA_beh.registerLastState(new fin(), "fin");

		agentA_beh.registerDefaultTransition("attendreAgentB", "envoiChiffre");
		agentA_beh.registerTransition("envoiChiffre", "attendreAgentB", 0);
		agentA_beh.registerTransition("envoiChiffre", "fin", 1);

		addBehaviour(agentA_beh);
	}

	private class attendreAgentB extends OneShotBehaviour {

		@Override
		public void action() {

			System.out.println("en attente de l agent B");
			block();
		}
	}

	/*****************************************************************/
	private class envoiChiffre extends OneShotBehaviour {

		int valeurRetour = 0;

		@Override
		public void action() {

			ACLMessage messageRecu = receive();
			if (messageRecu.getContent().equalsIgnoreCase("pret"))
				valeurRetour = 0;
			else
				valeurRetour = 1;

			int chiffre = (int) (Math.random() * 10);
			System.out.println("envoi de la valeur " + chiffre);
			ACLMessage message = new ACLMessage(ACLMessage.INFORM);
			message.addReceiver(messageRecu.getSender());
			message.setContent(chiffre + "");
			send(message);
		}

		public int onEnd() {
			return valeurRetour;
		}
	}

	/*****************************************************************/
	private class fin extends OneShotBehaviour {

		@Override
		public void action() {
			System.out.println("arret de l'agent");
			myAgent.doDelete();
		}
	}
}
