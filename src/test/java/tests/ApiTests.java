package tests;
import io.qameta.allure.Owner;
import models.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static specs.Specs.*;


public class ApiTests {
    @Test
    @Owner("Sukhinin Dmitrii")
    @DisplayName("Checking user name after 2000 year")
    void checkUserWithGroovyTest() {

        step("Checking user name after 2000 year", () -> {
            given(baseRequestSpec)
                    .when()
                    .get("/unknown")
                    .then()
                    .spec(baseResponseSpecCode200)
                    .body("data.findAll{it.year>2000}.name.flatten()",
                            hasItems("aqua sky", "fuchsia rose", "true red", "tigerlily", "blue turquoise"));
        });
    }

    @Test
    @Owner("Sukhinin Dmitrii")
    @DisplayName("Checking user id and name")
    void checkIdNameTest() {

        step("Checking user id and name", () -> {
            UserData data = given(baseRequestSpec)
                    .when()
                    .get("/unknown/2")
                    .then()
                    .spec(baseResponseSpecCode200)
                    .extract().as(UserData.class);
            assertEquals(2, data.getData().getId());
            assertEquals("fuchsia rose", data.getData().getName());

        });
    }

    @Test
    @Owner("Sukhinin Dmitrii")
    @DisplayName("Creation and verification user")
    void createUserTest() {
        UserBodyModel createUser = new UserBodyModel();
        createUser.setName("morpheus");
        createUser.setJob("leader");

        step("Creation and verification user", () -> {
            UserResponseModel createResponse = given(baseRequestSpec)
                    .body(createUser)
                    .when()
                    .post("/users")
                    .then()
                    .spec(baseResponseSpecCode201)
                    .extract().as(UserResponseModel.class);
            assertThat(createResponse.getName()).isEqualTo("morpheus");
            assertThat(createResponse.getJob()).isEqualTo("leader");
        });
    }

    @Test
    @Owner("Sukhinin Dmitrii")
    @DisplayName("Update and verification user")
    void updateUserTest() {
        UserBodyModel updateUser = new UserBodyModel();
        updateUser.setName("morpheus");
        updateUser.setJob("zion resident");

        step("Update and verification user", () -> {
            UserResponseModel updateResponse = given(baseRequestSpec)
                    .body(updateUser)
                    .when()
                    .put("/users/2")
                    .then()
                    .spec(baseResponseSpecCode200)
                    .extract().as(UserResponseModel.class);
            assertThat(updateResponse.getName()).isEqualTo("morpheus");
            assertThat(updateResponse.getJob()).isEqualTo("zion resident");
        });
    }

    @Test
    @Owner("Sukhinin Dmitrii")
    @DisplayName("Register and verification user")
    void registerUserTest() {
        RegisterBodyModel registerBody = new RegisterBodyModel();
        registerBody.setEmail("eve.holt@reqres.in");
        registerBody.setPassword("pistol");

        step("Register and verification user", () -> {
            RegisterResponseModel registerResponse = given(baseRequestSpec)
                    .body(registerBody)
                    .when()
                    .post("/register")
                    .then()
                    .spec(baseResponseSpecCode200)
                    .extract().as(RegisterResponseModel.class);
            assertThat(registerResponse.getId()).isEqualTo(4);
            assertThat(registerResponse.getToken()).isNotNull();
        });
    }

    @Test
    @Owner("Sukhinin Dmitrii")
    @DisplayName("Verify deleted user")
    void checkDeletedUser() {

        step("Verify deleted user", () -> {
            given()
                    .spec(baseRequestSpec)
                    .when()
                    .delete("/users/2")
                    .then()
                    .spec(baseResponseSpecCode204);
        });
    }
}
