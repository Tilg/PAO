package testAgent;


import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;

import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class ReceiverAgent extends Agent {

	private String service;

	protected void setup() {
		Object[] args = getArguments();
		service = (String) args[0];
		System.out.println("Hello. My name is " + this.getLocalName());
		registerService();

		//addBehaviour(new ResponderBahaviour(this));
		
		addBehaviour(new CyclicBehaviour(this) 
        {
             public void action() 
             {
                ACLMessage msg= receive();
                if (msg!=null)
                    System.out.println( " - " +
                       myAgent.getLocalName() + " <- " +
                       msg.getContent() );
                block();
             }
        });
		 
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
