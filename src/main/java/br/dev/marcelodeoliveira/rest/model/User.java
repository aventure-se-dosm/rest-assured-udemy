package br.dev.marcelodeoliveira.rest.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

//	Anotação para serialização
//	com XML deve estar na declaração
//	da prória classe Model em questão

@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.FIELD)
public class User {

	/**
	 * O atributo 'id' vem dentro da abertura de '<user>'.
	 * 
	 * <user id = '42992358235'> // valor ilustrativo.
	 * 
	 * No log, o objeto retornado terá 'null' como valor de 'id'.
	 * 
	 * Neste caso precisamos marcaá-lo como atributo, através da tag
	 * '@XmlAttrivute' para desserialização da response.
	 */

	@XmlAttribute
	private Long id;
	private String name;
	private Integer age;
	private Float salary;

	public User() {
	};

	public User(String name, Integer age, Float salary) {
		super();
		this.name = name;
		this.age = age;
		this.salary = salary;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Integer getAge() {
		return age;
	}

	public Float getSalary() {
		return salary;
	}

}
