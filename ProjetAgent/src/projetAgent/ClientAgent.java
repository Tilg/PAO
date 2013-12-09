package projetAgent;

import java.util.Timer;
import java.util.TimerTask;

import interfaceGraphique.InterfaceRecherche;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 * Cette classe représente l'agent client 
 * @author Phongphet
 *
 */

public class ClientAgent extends Agent {

	private static AID[] sellerAgent;
	private static AID[] supplierAgent;
	private InterfaceRecherche interfaceR;

	protected void setup() {
		System.out.println("----- We are client agent -----");
		interfaceR = new InterfaceRecherche();
		interfaceR.lancerInterface();
		Timer timer = new Timer();
		/*On cherche les agents vendeurs et fournisseurs dans notre plateforme central, on renouvelle cette 
		 * operation tous les 2 secondes*/
		TimerTask task = new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				sellerAgent = searchAgents("business");
				supplierAgent = searchAgents("provider");
			}
		};
		
		 timer.scheduleAtFixedRate(task, 0, 3*1000);
		addBehaviour(new ClientBehavior());
	}

	/**
	 * Cette méthode permet de retrouver tous les agents en fonction du type
	 * @param type
	 * @return
	 */
	private AID[] searchAgents(String type) {
		AID[] listAgent = null;
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType(type);
		dfd.addServices(sd);

		try {
			DFAgentDescription[] result = DFService.search(this, dfd);
			listAgent = new AID[result.length];
			System.out.println("Search for "+ type +", there are : "
					+ result.length + " elements");
			for (int i = 0; i < result.length; i++) {
				listAgent[i] = result[i].getName();
			}
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		return listAgent;
	}

	public static AID[] getSellerAgents() {
		return sellerAgent;
	}

	public static AID[] getSupplierAgent() {
		return supplierAgent;
	}
}
