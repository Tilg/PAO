package projetAgent;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.MessageTemplate;
import jade.tools.introspector.gui.MessageTableModel;

public class ClientBehavior extends Behaviour {
	private AID bestSeller;
	private int bestPrice;
	private int nbReply = 0;
	private MessageTemplate mt;
	private int step = 0;
	private ClientAgent agent;
	private String product;
	private AID[] sellerAgents;
	private AID[] supplierAgent;

	@Override
	public void action() {
		// TODO Auto-generated method stub
		
		switch(step){
		case 0 :
			break;
		}
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}

}
