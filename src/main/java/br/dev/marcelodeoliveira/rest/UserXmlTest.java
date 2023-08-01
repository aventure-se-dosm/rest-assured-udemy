package br.dev.marcelodeoliveira.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import java.util.ArrayList;

import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import io.restassured.internal.path.xml.NodeImpl;
import io.restassured.specification.RequestSpecification;

public class UserXmlTest {

//	private static final Float MINIMUM_MONTHLY_REMUNERATION = 1320.00F;
	private String REQUEST_USER_INSECURE = "http://restapi.wcaquino.me/usersXML/";
	private String REQUEST_USER_SECURE = "https://restapi.wcaquino.me/usersXML/";
	private boolean secure = true;

	private String getUserUrlById(Integer index) {
		return getUsersUrl() + index;
	}

	private String getUsersUrl() {
		return isSecure() ? (REQUEST_USER_SECURE) : (REQUEST_USER_INSECURE);

	}

	private boolean isSecure() {
		return this.secure;
	}

		@Test
		public void devoTrabalharComXml() {
			given()
			.when()
				.get(getUserUrlById(3))
			.then().assertThat()
			.statusCode(HttpStatus.SC_OK)
			.body("user.name", is("Ana Julia"))
			.body("user.@id", is("3"))
			.body("user.@id", not(is(3)))
			.body("user.filhos.size()", is(1))
			.body("user.filhos.name.size()", is(2))
			
			//forma esdrúxula, mas possível
			.body("user.filhos", hasItems("ZezinhoLuizinho"))
			
			.body("user.filhos.name", hasItems("Zezinho", "Luizinho"))
			.body("user.filhos.name", hasItem("Zezinho"))
			.body("user.filhos.name[0]", is("Zezinho"))
			.body("user.filhos.name[1]", is("Luizinho"))
			;
		}
		
		@Test
		public void devoFazerPesquisasAvancadasComXMLEJava() {
			
			ArrayList<NodeImpl> names = given()
			//RequestSpecification names = given()
			.when()
				.get(getUsersUrl())
			.then().assertThat()
			.statusCode(HttpStatus.SC_OK)
			.extract().path("users.user.name.findAll{it.toString().contains('n')}");
			
			Assert.assertEquals(2, names.size());
			Assert.assertTrue(names.get(1).toString().equalsIgnoreCase("Ana JULIA"));
			
			
			
		}
		@Test @Ignore
		public void devoFazerPesquisasAvancadasComXMLRequestSpecificationEJava() {
			
			//ArrayList<NodeImpl> names = given()
					RequestSpecification names = given()
					.when()
					.get(getUsersUrl())
					.then().assertThat()
					.statusCode(HttpStatus.SC_OK)
					.extract().path("users.user.name.findAll{it.toString().contains('n')}");
			
					//TODO: Assertions using  <<RequestSpecification>> name worthly!
					
			//Assert.assertEquals(2, names.size());
			//Assert.assertTrue(names.get(1).toString().equalsIgnoreCase("Ana JULIA"));
			
			
			
		}
		
}