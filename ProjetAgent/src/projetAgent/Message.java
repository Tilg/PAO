package projetAgent;

/**
 * Cette classe represente l'objet message qui est transfere entre client-fournisseur, client-vendeur
 * @author Phongphet
 *
 */
public class Message {
	
	private String reference;
	private String categorie;
	private String nom;
	private String prix;
	private String vendeur;
	private String type;
	
	public Message(){
		reference = "";
		categorie = "";
		prix = "";
		vendeur = "";
		type = "";		
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getCategorie() {
		return categorie;
	}

	public void setCategorie(String categorie) {
		this.categorie = categorie;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrix() {
		return prix;
	}

	public void setPrix(String prix) {
		this.prix = prix;
	}

	public String getVendeur() {
		return vendeur;
	}

	public void setVendeur(String vendeur) {
		this.vendeur = vendeur;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	

}
