package br.dev.marcelodeoliveira.rest;

import static io.restassured.RestAssured.*;

import org.apache.http.HttpStatus;

import static org.hamcrest.Matchers.*;
import org.junit.Test;

import io.restassured.http.ContentType;;

public class HTML {

	@Test
	public void  deveFazerBuscaComHtml() {
		given().log().all()
		.when()
		.get("https://restapi.wcaquino.me/v2/users")
		.then().log().all()
			.contentType(ContentType.HTML)
			.statusCode(HttpStatus.SC_OK)
			.body("html.body.div.table.tbody.tr.size()", is(3))
			
			// indexation here is starts with 0 ending in n-1, for n > 0 elements fo row or colum

			.body("html.body.div.table.thead.tr.th[0]", is("Id"))
			.body("html.body.div.table.thead.tr.th[1]", is("Nome"))
			.body("html.body.div.table.thead.tr.th[2]", is("Idade"))
			
			.body("html.body.div.table.tbody.tr[0].td[0]", is("1"))
			.body("html.body.div.table.tbody.tr[0].td[1]", is("João da Silva"))
			.body("html.body.div.table.tbody.tr[0].td[2]", is("30"))
			
			.body("html.body.div.table.tbody.tr[1].td[0]", is("2"))
			.body("html.body.div.table.tbody.tr[1].td[1]", is("Maria Joaquina"))
			.body("html.body.div.table.tbody.tr[1].td[2]", is("25"))
			
			.body("html.body.div.table.tbody.tr[2].td[0]", is("3"))
			.body("html.body.div.table.tbody.tr[2].td[1]", is("Ana Júlia"))
			.body("html.body.div.table.tbody.tr[2].td[2]", is("20"))
			
//			.body("html.body.div.table.tbody.tr[1].td[2]", is(30)) //it doesn't work: String value 
		;
		
	}
	
	@Test
	public void  deveFazerBuscaComHtmlEXpath() {
		given().log().all()
		.when()
		.then().log().all()
		.contentType(ContentType.HTML)
		.statusCode(HttpStatus.SC_OK)
		
		// indexation here is starts with 0 ending in n-1, for n > 0 elements for row or colum
		
		.body(hasXPath("count(//table//theader)", is("1")))
		.body(hasXPath("count(//table//tr)", is("4")))
		.body("html.body.div.table.thead.tr.th[1]", is("Nome"))
		.body("html.body.div.table.thead.tr.th[2]", is("Idade"))
//		
		.body(hasXPath("//tr[1]/td[1]", is("1")))
		.body(hasXPath("//tr[1]/td[2]", is("João da Silva")))
		.body(hasXPath("//tr[1]/td[3]", is("30")))
		
		.body(hasXPath("//tr[2]/td[1]", is("2")))
		.body(hasXPath("//tr[2]/td[2]", is("Maria Joaquina")))
		.body(hasXPath("//tr[2]/td[3]", is("25")))
		
		.body(hasXPath("//tr[3]/td[1]", is("3")))
		.body(hasXPath("//tr[3]/td[2]", is("Ana Júlia")))
		.body(hasXPath("//tr[3]/td[3]", is("20")))
		
//			.body("html.body.div.table.tbody.tr[1].td[2]", is(30)) //it doesn't work: String value 
		;
	}
	
	@Test
	public void  deveFazerBuscaComHtmlEXpathClean() {
		given().log().all()
		.queryParams("format", "clean")
		.when()
		.then().log().all()
		.contentType(ContentType.HTML)
		.statusCode(HttpStatus.SC_OK)
		
		// indexation here is starts with 0 ending in n-1, for n > 0 elements for row or colum
		
		//.body(hasXPath("count(//table//theader)", is("1")))
		//.body(hasXPath("count(//table//tr)", is("4")))
//		.body("html.body.div.table.thead.tr.th[1]", is("Nome"))
//		.body("html.body.div.table.thead.tr.th[2]", is("Idade"))
//		
		.body(hasXPath("//tr[2]/td[1]", is("1")))
		.body(hasXPath("//tr[2]/td[2]", is("João da Silva")))
		.body(hasXPath("//tr[2]/td[3]", is("30")))
		
		.body(hasXPath("//tr[3]/td[1]", is("2")))
		.body(hasXPath("//tr[3]/td[2]", is("Maria Joaquina")))
		.body(hasXPath("//tr[3]/td[3]", is("25")))
		
		.body(hasXPath("//tr[4]/td[1]", is("3")))
		.body(hasXPath("//tr[4]/td[2]", is("Ana Júlia")))
		.body(hasXPath("//tr[4]/td[3]", is("20")))
		
//			.body("html.body.div.table.tbody.tr[1].td[2]", is(30)) //it doesn't work: String value 
		;
		
	}
}
