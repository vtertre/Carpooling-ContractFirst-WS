package iaws.covoiturage.domain;

import iaws.covoiturage.domain.nomenclature.Coordinate;
import iaws.covoiturage.domain.nomenclature.FirstName;
import iaws.covoiturage.domain.nomenclature.LastName;
import iaws.covoiturage.domain.nomenclature.Mail;
import iaws.covoiturage.domain.nomenclature.MailingAddress;

public class Teacher {
	
	private LastName lastName;
	
	private FirstName firstName;
	
	private Mail mail;
	
	private MailingAddress mailingAddress;
	
	private Coordinate coordinate;
	
	public Teacher(LastName lastName, FirstName firstName,
			Mail mail, MailingAddress mailingAddress, Coordinate coord) {
		this.lastName = lastName;
		this.firstName = firstName;
		this.mail = mail;
		this.mailingAddress = mailingAddress;
		this.coordinate = coord;
	}

	public LastName getLastName() {
		return lastName;
	}

	public void setLastName(LastName lastName) {
		this.lastName = lastName;
	}

	public FirstName getFirstName() {
		return firstName;
	}

	public void setFirstName(FirstName firstName) {
		this.firstName = firstName;
	}

	public Mail getMail() {
		return mail;
	}

	public void setMail(Mail mail) {
		this.mail = mail;
	}

	public MailingAddress getMailingAddress() {
		return mailingAddress;
	}

	public void setMailingAddress(MailingAddress mailingAddress) {
		this.mailingAddress = mailingAddress;
	}

	public Coordinate getCoordinates() {
		return coordinate;
	}

	public void setCoordinates(Coordinate coordinates) {
		this.coordinate = coordinates;
	}
}
