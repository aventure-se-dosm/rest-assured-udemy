package br.dev.marcelodeoliveira.rest.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;

@XmlRootElement(name = "user.endereco")
@XmlAccessorType(XmlAccessType.FIELD)
public class Endereco {

	
	private String rua;
	
	private Integer numero;
	
	public Endereco() {}

	public Endereco(String rua, Integer numero) {
		
		this.rua = rua;
		this.numero = numero;
	}

	public String getRua() {
		return rua;
	}
	public void setRua(String Rua) {
		this.rua = Rua;
	}
	public Integer getNumero() {
		return numero;
	}
	public void setNumero(Integer numero) {
		this.numero = numero;
	}
}
