package uk.co.compendiumdev.performance.categories;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.compendiumdev.Environment;

import java.util.HashMap;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.fail;
import static uk.co.compendiumdev.performance.Constants.EXPERIMENT_COUNT;
import static uk.co.compendiumdev.performance.Constants.SLEEP_TIME;

public class DeleteCategoryPerformanceTest {
    // Projects Paths
    private static final String ALL_CATEGORIES_PATH = "/categories";
    private static final String SPECIFIC_CATEGORIES_PATH = "/categories/{id}";
    private static final String CLEAR_PATH = "/admin/data/thingifier";
    private static final String CATEGORIES = "categories";

    // Project Fields
    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";

    // Test Fields
    private static final String TEST_TITLE = "Some category title";
    private static final String TEST_DESCRIPTION = "Some category description";

    private static long globalStartTime;


    @BeforeAll
    public static void startTimer(){
        globalStartTime = System.currentTimeMillis();
    }

    @BeforeEach
    public void clearDataFromEnv(){
        RestAssured.baseURI = Environment.getBaseUri();
        if(RestAssured.baseURI == null) fail("To Do Manager isn't running!");

        post(CLEAR_PATH);

        final JsonPath clearedData = when().get(ALL_CATEGORIES_PATH)
                .then().statusCode(200).extract().body().jsonPath();

        final int newNumberOfTodos = clearedData.getList(CATEGORIES).size();

        // Assume instead of
        Assumptions.assumeTrue(newNumberOfTodos == 0);
    }

    @Test
    public void createAndDeleteCategories() throws InterruptedException {
        System.out.println("Experiment Number, Global Start Time (ms), T1 Time (ms), T2 Time(ms)");
        for(int i = 1 ; i<=EXPERIMENT_COUNT; i++){
            createAndDeleteCategory(i);
            Thread.sleep(SLEEP_TIME);
        }
    }

    private static int createCategory(int experimentNumber){
        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, TEST_TITLE);
        givenBody.put(DESCRIPTION, TEST_DESCRIPTION);

        long t1StartTime = System.currentTimeMillis();

        long t2StartTime = System.currentTimeMillis();

        Response r = given().
                body(givenBody).
                when().
                post(ALL_CATEGORIES_PATH).
                thenReturn();
        long t2Time = System.currentTimeMillis() - t2StartTime;

        String id = r.then().
                contentType(ContentType.JSON).
                statusCode(HttpStatus.SC_CREATED).
                body(
                        TITLE, equalTo(TEST_TITLE),
                        DESCRIPTION, equalTo(TEST_DESCRIPTION)
                ).
                extract().
                path(ID);


        long t1Time = System.currentTimeMillis() - t1StartTime;
        long globalTime = System.currentTimeMillis() - globalStartTime;

        if(experimentNumber != -1) {
            System.out.printf("%d,%d,%d,%d\n",
                    experimentNumber, globalTime, t1Time, t2Time);
        }

        return Integer.parseInt(id);
    }

    public void createAndDeleteCategory(int experimentNumber){
        int id = createCategory(-1);

        long t1StartTime = System.currentTimeMillis();

        long t2StartTime = System.currentTimeMillis();

        Response r = given().
                pathParam(ID, id).
                when().
                delete(SPECIFIC_CATEGORIES_PATH)
                .thenReturn();

        long t2Time = System.currentTimeMillis() - t2StartTime;

        r.then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK);

        long t1Time = System.currentTimeMillis() - t1StartTime;
        long globalTime = System.currentTimeMillis() - globalStartTime;

        if(experimentNumber != -1) {
            System.out.printf("%d,%d,%d,%d\n",
                    experimentNumber, globalTime, t1Time, t2Time);
        }
        createCategory(-1);
    }

}
