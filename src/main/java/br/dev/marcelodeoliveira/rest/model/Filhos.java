package br.dev.marcelodeoliveira.rest.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "user.filhos")
@XmlAccessorType(XmlAccessType.FIELD)
public class Filhos {
	String name;

	Filhos(){}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Filhos(String name) {
		this.name = name;
	}


}
