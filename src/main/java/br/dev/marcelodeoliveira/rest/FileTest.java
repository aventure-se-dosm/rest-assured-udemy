package br.dev.marcelodeoliveira.rest;

import static io.restassured.RestAssured.given;

import java.io.File;

import org.apache.http.HttpStatus;
import org.junit.Test;

import io.restassured.http.ContentType;

public class FileTest {

	private final String UrlFileUpload = "https://restapi.wcaquino.me/upload";
	private final String PathUploadedFile = "src/main/resources/users.pdf";

	@Test
	public void deveObrigarEnvioArquivo() {
		given().log().all()
			//.contentType("multipart/form-data") --> optional
		.when()
			.post(UrlFileUpload)
		.then().log().all()
			.statusCode(HttpStatus.SC_NOT_FOUND);
	}

	@Test
	public void deveFazerEeArquivo() {
		given().log().all()
			//.contentType("multipart/form-data") --> optional
		
			//	'Content-type' has an attribute called name.
			//	"arquivo" is set for this endpoint, standing for files to upload
			// which Content-type is 'multipart/form-data'
			.multiPart("arquivo", new File(PathUploadedFile))
		.when()
			.post(UrlFileUpload)
		.then().log().all()
				.statusCode(HttpStatus.SC_OK);
	}
}