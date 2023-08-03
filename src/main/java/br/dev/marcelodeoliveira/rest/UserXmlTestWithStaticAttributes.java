package br.dev.marcelodeoliveira.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasXPath;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import java.util.ArrayList;

import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.hamcrest.xml.HasXPath;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.internal.path.xml.NodeImpl;

public class UserXmlTestWithStaticAttributes {

	private String RESOURCE_USERS_XML = "/usersXML";

	// static attributes - 23th lession
	// always static and public <--- using BeforeClass

	// desta vez os métodos lançam mão apenas do 'recurso' quando necessário
	// uma vez que definimos estaticamente a url

	@BeforeClass
	public static void setup() {
		RestAssured.baseURI = "https://restapi.wcaquino.me";
		RestAssured.basePath = "/usersXML/";
	}

	private String getUserUrlById(Integer index) {
		return (getUsersUrl() + "/" + index);
	}

	private String getUsersUrl() {
		return RESOURCE_USERS_XML;
	}

	@Test
	public void devoTrabalharComXml() {
		RestAssured.given().log().all().when()

	//				 --> Same as below
				.get("3").then()
				// .assertThat()
				.statusCode(HttpStatus.SC_OK);
	}

	@Test
	public void devoTrabalharComXmlNaRaiz() {

		given()
		.when()
			.get("3")
		.then().assertThat()
			.statusCode(HttpStatus.SC_OK).rootPath("user")

			.body("name", Matchers.is("Ana Julia")).body("@id", is("3")).body("@id", not(is(3)))
			.body("filhos.size()", is(1)).body("filhos.name.size()", is(2))
				// forma esdr�xula, mas possível
			.body("filhos", hasItems("ZezinhoLuizinho"))

				// redefinindo a raiz para 'user.filhos':
				.rootPath("user.filhos").body("size()", is(1)).body("name.size()", is(2))
				.body("name", hasItems("Zezinho", "Luizinho")).body("name", hasItem("Zezinho"))
				.body("name[0]", is("Zezinho")).body("name[1]", is("Luizinho"));
	}

	@Test
	public void devoFazerPesquisasAvancadasComXMLEJava() {

		ArrayList<NodeImpl> names = given()
				// RequestSpecification names = given()
				.when().get("").then().assertThat().statusCode(HttpStatus.SC_OK).extract()
				.path("users.user.name.findAll{it.toString().contains('n')}");

		Assert.assertEquals(2, names.size());
		Assert.assertTrue(names.get(1).toString().equalsIgnoreCase("Ana JULIA"));
	}

	@Test
	@Ignore
	public void devoFazerPesquisasAvancadasComXMLRequestSpecificationEJava() {

		// RequestSpecification names =
		given()
		.when()
			.get(getUsersUrl())
		.then().assertThat()
			.statusCode(HttpStatus.SC_OK)
			.extract()
			.path("users.user.name.findAll{it.toString().contains('n')}");
		// TODO: Assertions using <<RequestSpecification>> name worthly!
	}

	@Test
	public void devoFazerPesquisasAvancadasComXPath() {
		given()
		.when()
			.get()
		.then()// .assertThat()
			.statusCode(HttpStatus.SC_OK).body(hasXPath("count(/users/user)", is("3")))
			.body(hasXPath("count(/users/user)", is(not("2"))))
			.body(hasXPath("/users/user[@id='1']")) // full form of this element				
			.body(hasXPath("//user[@id='2']")) // shortened form of this element
			.body(hasXPath("//name[text()='Luizinho']/../../name", is("Ana Julia")))

			// It doesn't work: hasHpath expect as 2nd argument an Iterable<String>
			// .body(hasXPath("//name[text()='Ana Julia']/following-siblings::filhos", hasItems("Zezinho", "Luizinho")))
			.body(hasXPath(
					"//name[text()='Ana Julia']/following-sibling::filhos",
					allOf(containsString("Zezinho"), containsString("Luizinho"))))

				.body(hasXPath("/users/user[last()]/name", is("Ana Julia")))
				.body(hasXPath("/users/user/name", is("João da Silva"))).body(hasXPath("//name", is("João da Silva")))
				.body(hasXPath("//name['0']", is("João da Silva")))

				//	 .body(hasXPath("/users/user/name[0]", is("João da Silva"))) 
				//	it doesn't  work: index should be written surrounded with simple quotes
				//	 .body(hasXPath("//name[0]", is("João da Silva"))) //not work
				.body(hasXPath("//user/name", is("João da Silva")))
				.body(hasXPath("/users/user/name", is("João da Silva")))

				// First index
				.body(hasXPath("/users/user/name['0']", is("João da Silva")))
				.body(hasXPath("/users/user/name['0']", is("João da Silva")))
				.body(hasXPath("/users/user/name['any_string_7y88yggy8g']", is("João da Silva")))

				// element with a specific index
				.body(hasXPath("/users/user[1]/name", is("João da Silva")))
				.body(hasXPath("/users/user[2]/name", is("Maria Joaquina")))
				.body(hasXPath("/users/user[3]/name", is("Ana Julia")))

				// last index element
				.body(HasXPath.hasXPath("/users/user[last()]/name", is("Ana Julia")))
				.body(HasXPath.hasXPath("/users/user[last()]/name[last()]", is("Ana Julia")))

				// contains
				.body(hasXPath("count(/users/user/name[contains(., 'n')])", is("2")))

				// relational operations
				.body(hasXPath("//user[age < 24]/name", is("Ana Julia")))
				.body(hasXPath("//user[age > 20 and age  < 30]/name", is("Maria Joaquina")))
				.body(hasXPath("//user[age > 29][age < 31]/name", is("João da Silva")));
	}
}