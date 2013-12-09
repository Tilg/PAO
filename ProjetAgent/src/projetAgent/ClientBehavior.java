package projetAgent;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Cette classe represente les differents comportement de l'agent
 * @author Phongphet
 *
 */
public class ClientBehavior extends Behaviour {

	private static AID[] sellerAgents;
	private static AID[] supplierAgent;
	private static Agent clientAgent;

	/**
	 * Cette methode decrit l'action, on commence par recuperer les objets de la classe agent
	 */
	@Override
	public void action() {
		clientAgent = this.getAgent();
		sellerAgents = ClientAgent.getSellerAgents();
		supplierAgent = ClientAgent.getSupplierAgent();
	}

	/**
	 * Cette methode permet d'etablir une demande de prix avec le vendeur
	 * @param argument
	 */
	public void askProductPriceWithSeller(String argument) {
		if (!argument.equals("")) {
			ACLMessage cfp = new ACLMessage(ACLMessage.QUERY_REF);
			for (int i = 0; i < sellerAgents.length; ++i) {
				cfp.addReceiver(sellerAgents[i]);
			}
			cfp.setOntology("request-one-product");
			cfp.setContent(argument);
			clientAgent.send(cfp);
		}
	}

	/**
	 * Cette methode permet de recuperer la reponse du vendeur concernant le prix du produit
	 * @return
	 */
	public ArrayList<Message> receivePriceProductFromSeller() {
		ArrayList<Message> listeMessage = new ArrayList<>();
		ACLMessage replySeller = null;
		for (int i = 0; i < sellerAgents.length; i++) {
			while (replySeller == null) {
				replySeller = clientAgent.receive();
			}

			if (replySeller.getPerformative() == ACLMessage.CONFIRM
					&& replySeller.getOntology().equals("reply-one-product")) {
				String prix = replySeller.getContent();
				Message message = new Message();
				message.setPrix(prix);
				listeMessage.add(message);
			}
		}
		return listeMessage;
	}

	/**
	 * Cette methode permet d'etablir une demande de prix en precisant des criteres aupres des vendeurs
	 * @param argument
	 */
	public void askProductWithCriteria(String argument) {
		if (!argument.equals("")) {
			ACLMessage cfp = new ACLMessage(ACLMessage.QUERY_REF);
			for (int i = 0; i < sellerAgents.length; i++) {
				cfp.addReceiver(sellerAgents[i]);
			}
			cfp.setOntology("request-several-products");
			cfp.setContent(argument);
			clientAgent.send(cfp);
		}
	}

	/**
	 * Cette methode permet de recupere la liste de tous les produits proposes par le vendeur suite a la demande du client
	 * @return
	 */
	public ArrayList<ArrayList<Message>> receiveListOfProduct() {
		ArrayList<ArrayList<Message>> listeTotal = new ArrayList<>();
		for (int i = 0; i < sellerAgents.length; i++) {
			ArrayList<Message> listeMessage = new ArrayList<>();
			ACLMessage replySeller = null;
			while (replySeller == null) {
				replySeller = clientAgent.receive();
			}
			if (replySeller.getPerformative() == ACLMessage.CONFIRM
					&& replySeller.getOntology().equals(
							"reply-several-products")) {
				System.out.println(replySeller.getContent());
				StringTokenizer token1 = new StringTokenizer(
						replySeller.getContent(), ",");
				while (token1.hasMoreElements()) {
					String chaine = token1.nextToken();

					String[] listElement = chaine.split("/");
					Message mess = new Message();

					mess.setCategorie(listElement[0]);
					mess.setNom(listElement[1]);
					mess.setReference(listElement[2]);
					System.out.println(listElement[0]);
					System.out.println(listElement[1]);
					System.out.println(listElement[2]);
					listeMessage.add(mess);
				}
			}
			listeTotal.add(listeMessage);
		}
		return listeTotal;
	}

	/**
	 * Cette methode permet de demander le prix du produit avec le fournisseur
	 * @param argument
	 */
	public void askProductWithSupplier(String argument) {
		ACLMessage messageForSupplier = new ACLMessage(ACLMessage.QUERY_IF);
		for (int i = 0; i < supplierAgent.length; i++) {
			messageForSupplier.addReceiver(supplierAgent[i]);
		}
		messageForSupplier.setOntology("demandeClient");
		messageForSupplier.setContent(argument);
		clientAgent.send(messageForSupplier);
	}

	/**
	 * Cette methode permet de recupere le prix du produit du fournisseur
	 * @return
	 */
	public ArrayList<Message> receivePriceProductFromSupplier() {
		ArrayList<Message> listeMessage = new ArrayList<>();
		for (int i = 0; i < supplierAgent.length; i++) {
			ACLMessage replySupplier = null;
			while (replySupplier == null) {
				replySupplier = clientAgent.receive();
			}

			System.out.println(replySupplier.getOntology());
			if (replySupplier.getOntology().equals("prixFournisseur")) {
				String prix = replySupplier.getContent();
				Message message = new Message();
				message.setPrix(prix);
				listeMessage.add(message);
			}
		}
		return listeMessage;
	}

	/**
	 * Cette methode envoie la demande d'achat du produit selectionne aupres du fournisseur
	 * @param argument
	 */
	public void askToBuyProductWithSupplier(String argument) {
		ACLMessage messageForSupplier = new ACLMessage(ACLMessage.QUERY_REF);
		for (int i = 0; i < supplierAgent.length; ++i) {
			messageForSupplier.addReceiver(supplierAgent[i]);
		}
		messageForSupplier.setOntology("achatClient");
		messageForSupplier.setContent(argument);
		clientAgent.send(messageForSupplier);
	}
	
	/**
	 * Cette methode demande l'achat du produit selectionne aupres du vendeur
	 * @param argument
	 */
	public void askToBuyProductWithSeller(String argument) {
		ACLMessage messageForSupplier = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
		for (int i = 0; i < supplierAgent.length; ++i) {
			messageForSupplier.addReceiver(supplierAgent[i]);
		}
		messageForSupplier.setOntology("buy-product");
		messageForSupplier.setContent(argument);
		clientAgent.send(messageForSupplier);
	}

	/**
	 * Cette methode recoit la confirmation d'acaht du fournisseur
	 * @return
	 */
	public Message receiveResponseFromSupplier() {
		ACLMessage replySupplier = null;
		while (replySupplier == null) {
			replySupplier = clientAgent.receive();
		}

		System.out.println(replySupplier.getOntology());
		if (replySupplier.getOntology().equals("reponseFournisseur")) {
			String prix = replySupplier.getContent();
			Message message = new Message();
			message.setPrix(prix);
			return message;
		}

		return null;
	}
	
	/**
	 * Cette methode recoit la confirmation d'achat du vendeur
	 * @return
	 */
	public Message receiveResponseFromSeller() {
		ACLMessage replySupplier = null;
		while (replySupplier == null) {
			replySupplier = clientAgent.receive();
		}
		if (replySupplier.getOntology().equals("transaction-success")) {
			String confirm = replySupplier.getContent();
			Message message = new Message();
			message.setPrix(confirm);
			return message;
		}

		return null;
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}
}
