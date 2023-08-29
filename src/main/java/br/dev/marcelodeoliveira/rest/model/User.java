package br.dev.marcelodeoliveira.rest.model;

import java.util.ArrayList;
import java.util.List;

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
	private Endereco endereco;
	private Float salary;
	private List<Filhos> filhos;

	public User() {
	};

	public User(String name, Integer age, Float salary, Endereco endereco, List<Filhos> filhos) {
		super();
		this.name = name;
		this.age = age;
		this.endereco = endereco;
		this.salary = salary;
		this.filhos = filhos;
	}
	public User(String name, Integer age, Float salary) {
		this(name, age, salary, new Endereco(),  new ArrayList<Filhos>());
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

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public List<Filhos> getFilhos() {
		return filhos;
	}

	public void setFilhos(List<Filhos> filhos) {
		this.filhos = filhos;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public void setSalary(Float salary) {
		this.salary = salary;
	}
	

}
