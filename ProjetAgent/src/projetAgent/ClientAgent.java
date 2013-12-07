package projetAgent;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ClientAgent extends Agent {

	private String product;
	private AID[] sellerAgents;
	private AID[] supplierAgent;

	protected void setup() {
		System.out.println("We are client agent");
		Object[] args = getArguments();
		if (args != null && args.length > 1) {
			product = (String) args[0];
			System.out.println("We are trying to by this product : " + args[0]);
			addBehaviour(new TickerBehaviour(this, 60000) {
				@Override
				protected void onTick() {
					sellerAgents = searchAgents("selling");
					supplierAgent = searchAgents("supplying");
				}
			});
		}
	}

	private AID[] searchAgents(String type) {
		AID[] listAgent = null;
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("selling");
		dfd.addServices(sd);

		try {
			DFAgentDescription[] result = DFService.search(this, dfd);
			sellerAgents = new AID[result.length];
			System.out.println("Search for sellor, there are : "
					+ result.length + " elements");
			for (int i = 0; i < result.length; i++) {
				sellerAgents[i] = result[i].getName();
			}
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		return listAgent;
	}

	public void askProductByRefWithSeller(String ref, AID id) {
		try {
			ACLMessage aclMessage = new ACLMessage(ACLMessage.QUERY_REF);
			aclMessage.setOntology("request-one-product");
			aclMessage.addReceiver(id);
			aclMessage.setContent(ref);

			this.send(aclMessage);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public String getProduct() {
		return product;
	}

	public AID[] getSellerAgents() {
		return sellerAgents;
	}

	public AID[] getSupplierAgent() {
		return supplierAgent;
	}

	private class ClientBehaviour extends Behaviour {

		private AID bestSeller;
		private int bestPrice;
		private int repliesCnt = 0;
		private MessageTemplate mt;
		private int step = 0;

		@Override
		public void action() {
			// TODO Auto-generated method stub
			switch (step) {
			case 0:
				// Send the cfp to all sellers
				ACLMessage cfp = new ACLMessage(ACLMessage.QUERY_REF);
				for (int i = 0; i < sellerAgents.length; ++i) {
					cfp.addReceiver(sellerAgents[i]);
				}
				cfp.setOntology("request-one-product");
				cfp.setContent(product);
				myAgent.send(cfp);
				step = 1;
				break;

			case 1:

				// Receive all proposals/refusals from seller agents
				ACLMessage replySeller = myAgent.receive();
				if (replySeller != null) {
					// Reply received
					if (replySeller.getPerformative() == ACLMessage.PROPOSE) {
						// This is an offer
						int price = Integer.parseInt(replySeller.getContent());
						if (bestSeller == null || price < bestPrice) {
							// This is the best offer at present
							bestPrice = price;
							bestSeller = replySeller.getSender();
						}
					}
					repliesCnt++;
					if (repliesCnt >= sellerAgents.length) {
						// We received all replies
						step = 2;
					}
				} else {
					block();
				}
				break;
				
			case 2 :
				//contact supplier to see if their price is better
				// Send the cfp to all sellers
				ACLMessage messageForSupplier = new ACLMessage(ACLMessage.QUERY_REF);
				for (int i = 0; i < supplierAgent.length; ++i) {
					messageForSupplier.addReceiver(supplierAgent[i]);
				}
				messageForSupplier.setOntology("");
				messageForSupplier.setContent("idproduit=" + product);
				myAgent.send(messageForSupplier);
				step = 3;
				break;
			
			case 3 : 
				//receive the proposals from the supplier
				// Receive all proposals/refusals from seller agents
				ACLMessage replySupplier = myAgent.receive();
				if (replySupplier != null) {
					// Reply received
					if (replySupplier.getPerformative() == ACLMessage.PROPOSE) {
						// This is an offer
						int price = Integer.parseInt(replySupplier.getContent());
						if (bestSeller == null || price < bestPrice) {
							// This is the best offer at present
							bestPrice = price;
							bestSeller = replySupplier.getSender();
						}
					}
					repliesCnt++;
					if (repliesCnt >= sellerAgents.length) {
						// We received all replies
						step = 2;
					}
				} else {
					block();
				}
				break;
			}
		}

		@Override
		public boolean done() {
			// TODO Auto-generated method stub
			return false;
		}

	}
}
