package com.trello.StepDefinitions;

import com.trello.Utils.ConfigurationReader;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StepDefinitions {
    String boardId, labelId, cardId;
    List<String> list = new ArrayList<>();

    @Before
    public static void before() {
        baseURI = ConfigurationReader.get("baseURI");
        basePath = ConfigurationReader.get("basePath");
    }


    @When("Create board.")
    public void createBoard() {
        Response response = given().accept(ContentType.JSON)
                .and().contentType(ContentType.JSON)
                .queryParam("key", ConfigurationReader.get("key"))
                .queryParam("token", ConfigurationReader.get("token"))
                .queryParam("name", "BoardTest").when().post("/boards");

        response.prettyPrint();
        assertEquals(200, response.statusCode());
        assertEquals("application/json; charset=utf-8", response.contentType());
        response.prettyPrint();
        JsonPath jsonPath = response.jsonPath();
        boardId = jsonPath.getString("id");
        System.out.println("BoardId: " + boardId);
    }

    @And("Create a list on board.")
    public void createAListOnBoard() {
        Response response = given().accept(ContentType.JSON)
                .and().contentType(ContentType.JSON)
                .pathParam("id", boardId)
                .queryParam("key", ConfigurationReader.get("key"))
                .queryParam("token", ConfigurationReader.get("token"))
                .queryParam("name", "BoardLabel").when().post("/boards/{id}/lists");

        response.prettyPrint();
        assertEquals(200, response.statusCode());
        assertEquals("application/json; charset=utf-8", response.contentType());
        JsonPath jsonPath = response.jsonPath();
        labelId = jsonPath.getString("id");
        System.out.println("LabelId: " + labelId);
    }

    @And("Create a new card.")
    public void createANewCard() {

        for (int i = 0; i < 2; i++) {
            Response response = given().accept(ContentType.JSON)
                    .and().contentType(ContentType.JSON)
                    .queryParam("key", ConfigurationReader.get("key"))
                    .queryParam("token", ConfigurationReader.get("token"))
                    .queryParam("name", "KartName" + i + " ")
                    .queryParam("idList", labelId).when().post("/cards");
            assertEquals(200, response.statusCode());
            assertEquals("application/json; charset=utf-8", response.contentType());
            response.prettyPrint();
            JsonPath jsonPath = response.jsonPath();
            list.add(jsonPath.getString("id"));
            cardId = list.get(0);
            System.out.println("cardId = " + cardId);
        }

        System.out.println("First card id = " + list.get(0));
        System.out.println("Second card id = " + list.get(1));
    }

    @And("Update card.")
    public void updateCard() {
        String expectedNamed = "updatedName";
        Response response = given().accept(ContentType.JSON)
                .and().contentType(ContentType.JSON)
                .pathParam("id", cardId)
                .queryParam("key", ConfigurationReader.get("key"))
                .queryParam("token", ConfigurationReader.get("token"))
                .queryParam("name", expectedNamed).when().put("/cards/{id}");
        assertEquals(200, response.statusCode());
        assertEquals("application/json; charset=utf-8", response.contentType());
        response.prettyPrint();
        JsonPath jsonPath = response.jsonPath();
        String actualName = jsonPath.getString("name");
        System.out.println("Actual name: " + actualName);
        assertEquals(expectedNamed, actualName);
    }

    @And("Delete card.")
    public void deleteCard() {
        Random rd = new Random();
        int randomProduct = rd.nextInt(list.size());
        System.out.println("All size = " +list.size());
        System.out.println("randomNumber = " + randomProduct);
        cardId= list.get(randomProduct);
        System.out.println("cardId = " + cardId);
        Response response = given().accept(ContentType.JSON)
                .and().contentType(ContentType.JSON)
                .pathParam("id", cardId)
                .queryParam("key", ConfigurationReader.get("key"))
                .queryParam("token", ConfigurationReader.get("token"))
                .when().delete("/cards/{id}");
        assertEquals(200, response.statusCode());
        assertEquals("application/json; charset=utf-8", response.contentType());
        response.prettyPrint();
        JsonPath jsonPath = response.jsonPath();
        String result = jsonPath.getString("limit");
        System.out.println("Card delete result: " + result);
        assertNull(result);
    }

    @And("Delete board.")
    public void deleteBoard() {
        Response response = given().accept(ContentType.JSON)
                .and().contentType(ContentType.JSON)
                .pathParam("id", boardId)
                .queryParam("key", ConfigurationReader.get("key"))
                .queryParam("token", ConfigurationReader.get("token"))
                .when().delete("/boards/{id}");
        assertEquals(200, response.statusCode());
        assertEquals("application/json; charset=utf-8", response.contentType());
        response.prettyPrint();
        JsonPath jsonPath = response.jsonPath();
        String result = jsonPath.getString("_value");
        System.out.println("Board delete result: " + result);
        assertNull(result);

    }
}
