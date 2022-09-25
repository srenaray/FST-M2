package liveProject;

import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import static io.restassured.RestAssured.given;

public class RestAssured_P1 {
    RequestSpecification req;
    RequestSpecification req1;
    ResponseSpecification res;
    ResponseSpecification res1;
    ResponseSpecification res2;
    String tokenGenerated = "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";
    String sshKey="ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAAAgQCPzPzGcnXdwHV5bjUXt08evT7LIEbYEduLr8Jia+igXkgtBezTTwYXGbEQZy5BYWWe47DJXqIbJi1BcXvkT6Fj6TltQdPlz2HeDcu0yI1xroPssJRWxDljzSyZNp4Q0zIC4NthmXDEVxsEXu1O2ztfbtObh5rIsbeV6+Uvo3YTWQ== ";
    int sshId;



    @BeforeClass
    public void setRequest() {
        req = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri("https://api.github.com/").setBasePath("user/keys")
                .addHeader("Authorization", "token" +tokenGenerated)
                .build().log().all();
        req1 = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri("https://api.github.com/").setBasePath("user/keys")
                .build().log().all();
        res = new ResponseSpecBuilder()
                .expectStatusCode(201)
                .expectContentType("application/json")
                .build();
        res1 = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType("application/json")
                .build();
        res2 = new ResponseSpecBuilder()
                .expectStatusCode(204)
                .build();
    }

    @Test(priority=1)
    public void addSSHKey()
    {
        String reqBody = "{\"title\": \"TestAPIKey\", \"key\": \""+sshKey+"\"}";
        Response response = given().spec(req).auth().oauth2(tokenGenerated)
                .body(reqBody)
                .when().post();

        response.then().spec(res).log().all();
        sshId = response.jsonPath().get("id");
        sshKey = response.jsonPath().get("key");
        System.out.println("Id is.............."+sshId);
        System.out.println("Ssh key is.............."+sshKey);
    }
    @Test( priority=2)
    public void getSSHKey() {
        Response response = given().spec(req).auth().oauth2(tokenGenerated)// Use requestSpe
                .when().get(); // Send GET request

        // Print response
        System.out.println("SSH Key added to the account is..... "+response.asPrettyString());
        // Assertions
        response.then()
                .spec(res1); // Use responseSpec
        Reporter.log("SSH Key added to Account is.... "+response.toString());

    }
    @Test(priority=3)
    public void deleteSSHKey() {
        Response response = given().spec(req1).
                pathParam("keyID",sshId).auth().oauth2(tokenGenerated) // Use requestSpec
                .when().delete("/{keyID}"); // Send Delete Request

        // Assertions
        response.then().spec(res2);
        Reporter.log("SSH Key deleted from Account is.... "+response.toString());
    }


}