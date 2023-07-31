
package br.dev.marcelodeoliveira.rest;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.request;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.hasItemInArray;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;

import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class OlaMundo {

	private static final String URL_OLA_MUNDO = "http://restapi.wcaquino.me/ola";

	@Test
	public void olaMundoProlixo() {
		// Response response = RestAssured.request(Method.GET,
		// "http://restapi.wcaquino.me/ola");
		Response response = request(Method.GET, "https://restapi.wcaquino.me/ola");
		Assert.assertEquals(response.getBody().asString(), "Ola Mundo!");
		Assert.assertEquals(response.statusCode(), 200);
		Assert.assertFalse(response.statusCode() == 404);
		Assert.assertTrue("O Status code deve ser '200'", response.statusCode() == 200);
	}

	@Test
	public void devoConhecerOutrasFormasDeRestAssured() {
		Response response = RestAssured.request(Method.GET, URL_OLA_MUNDO);
		ValidatableResponse validacao = response.then();
		validacao.statusCode(200);
		// response.then().statusCode(200);
		// get("https://restapi.wcaquino.me/ola");
	}

	@Test
	public void olaMundoFormaPreferida() {
		given()
		.when()
			.get(URL_OLA_MUNDO)
		.then()
			.assertThat() // pratically semantical-purpouse //											// only;
			.statusCode(HttpStatus.SC_OK)
			.body(is("Ola Mundo!"))
			.body(containsString("Mundo"))
			.body(is(not("")));
	}

	@Test
	public void DevoConhecerMatchersComHamcrest() {

		// Seguindo a Maria Joaquina do Tutorial do Wagnão \_(ツ)_/
		Response response = RestAssured.request(Method.GET, URL_OLA_MUNDO);
		Integer requestStatus = response.getStatusCode();

		String aluno1 = "Cirilo da Silva Santos";
		String aluno2 = "Maria Joaquina Medici di Grimaldi-Habsburg";

		List<String> disciplinas = Arrays.asList("Matemática", "Português", "História", "Geografia", "Ciências",
				"Artes", "Educação Física");
		List<Integer> primosAte20 = Arrays.asList(2, 3, 5, 7, 11, 13, 17, 19);

		String[] horarios = { "manha", "tarde", "noite" };

		// Numerical comparision
		Assert.assertThat(requestStatus, Matchers.greaterThan(100));
		Assert.assertThat(requestStatus, Matchers.lessThan(HttpStatus.SC_NOT_FOUND));

		// Types - henceforth using static import examples
		Assert.assertThat(aluno2, isA(String.class));

		// sequential types - henceforth using static import examples

		Assert.assertThat(disciplinas, hasSize(7));
//		Assert.assertThat(disciplinas, contains());
		// Assert.assertThat(disciplinas, containsInAnyOrder("Matemática",
		// "Português"));
		// Assert.assertThat(disciplinas, containsInRelativeOrder("Artes", "Ciências",
		// "Matemática"));

		Assert.assertThat(primosAte20, contains(2, 3, 5, 7, 11, 13, 17, 19));
		Assert.assertThat(primosAte20, containsInAnyOrder(5, 3, 11, 13, 2, 19, 7, 17));

		Assert.assertThat(horarios, hasItemInArray("manha"));
		// Logical

		Assert.assertThat(aluno1, is("Cirilo da Silva Santos"));

		// Watch out! it's true because they're different objects.
		Assert.assertThat("Cirilo da Silva", is(not(aluno1)));
		Assert.assertThat(aluno2, allOf(startsWith("Mar"), endsWith("urg"), containsString("qui")));
	}

}
