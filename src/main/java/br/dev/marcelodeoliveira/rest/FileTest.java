package br.dev.marcelodeoliveira.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.lessThan;

import java.io.File;

import org.apache.http.HttpStatus;
import org.junit.Test;

public class FileTest {

	private final String UrlFileUpload = "https://restapi.wcaquino.me/upload";
	private final String PathUploadedFile = "src/main/resources/users.pdf";
	private final String PathTooMuchLargeUploadedFile = "src/main/resources/chromedriver_win32_111.zip";

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
		//.contentType("multipart/form-data") --> optional
		
		//	'Content-type' has an attribute called name.
		//	"arquivo" is set for this endpoint, standing for files to upload
		// which Content-type is 'multipart/form-data'
		given().log().all()
			.multiPart("arquivo", new File(PathUploadedFile))
		.when()
			.post(UrlFileUpload)
		.then().log().all()
				.statusCode(HttpStatus.SC_OK);
	}
	
	@Test
	public void naoDeveFazerUploadDeArquivoMuitoGrande() {
		given().log().all()
			.multiPart("arquivo", new File(PathTooMuchLargeUploadedFile))
		.when()
			.post(UrlFileUpload)
		.then().log().all()
			.statusCode(HttpStatus.SC_REQUEST_TOO_LONG);
	}

	@Test
	public void naoDeveFazerUploadDeArquivoTimeoutException() {
		given().log().all()
		.multiPart("arquivo", new File(PathTooMuchLargeUploadedFile))
		.when()
		.post(UrlFileUpload)
		.then().log().all()
		.time(lessThan(10000L))
		.statusCode(HttpStatus.SC_REQUEST_TOO_LONG);
	}
}