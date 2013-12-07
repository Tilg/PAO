package testAgent;

import jade.core.Agent;

public class HelloWorldAgentWithParameters extends Agent {

	private String service;

	protected void setup() {
		Object[] args = getArguments();
		service = (String) args[0];

		System.out.println("Hello World. My name is " + this.getLocalName()
				+ " (" + this.getAID() + ") and I provide " + service
				+ " service.");
	}

	/*
	 * public void setArguments(String[] args) { service = args[0]; }
	 */
}
