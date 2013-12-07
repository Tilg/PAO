package testAgent;

import java.util.List;
import java.util.Iterator;

import jade.core.Agent;
import jade.domain.FIPAException;
import jade.domain.DFService; //Communicator
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;

public class SearchAgent extends Agent {

	protected void setup() {
		System.out.println("Hello. I am " + this.getLocalName() + ".");

		this.searchAgents();
	}

	private void searchAgents() {
		int i;
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd  = new ServiceDescription();
		sd.setType( "buyer" );
		dfd.addServices(sd);
		
		try {
			DFAgentDescription[] result = DFService.search(this, dfd);
			result = DFService.search(this, dfd);
			System.out.println("Search for BUYER: " + result.length + " elements" );
			for (i = 0; i < result.length; i++) {
				System.out.println((i + 1) + " : " + result[i].getName());
			}

		} catch (Exception fe) {
			System.err.println(getLocalName()
					+ " search with DF is not succeeded because of "
					+ fe.getMessage());
			doDelete();
		}
	}
}
