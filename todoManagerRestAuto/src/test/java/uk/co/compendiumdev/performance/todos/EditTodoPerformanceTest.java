package uk.co.compendiumdev.performance.todos;

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

public class EditTodoPerformanceTest {
    // Todos Paths
    private static final String ALL_TODOS_PATH = "/todos";
    private static final String SPECIFIC_TODO_PATH = "/todos/{id}";
    private static final String CLEAR_PATH = "/admin/data/thingifier";
    private static final String TODOS = "todos";

    // Todos Fields
    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String DONE = "doneStatus";

    // Test Fields
    private static final String TEST_TITLE = "Some todos title";
    private static final String TEST_DESCRIPTION = "Some todos description";
    private static final String OTHER_TEST_TITLE = "Some other todos title";
    private static final String OTHER_TEST_DESCRIPTION = "Some other todos description";

    // Useful Constants
    private static final String TRUE = "true";
    private static final String FALSE = "false";

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

        final JsonPath clearedData = when().get(ALL_TODOS_PATH)
                .then().statusCode(200).extract().body().jsonPath();

        final int newNumberOfTodos = clearedData.getList(TODOS).size();

        // Assume instead of
        Assumptions.assumeTrue(newNumberOfTodos == 0);
    }

    @Test
    public void editTodos() throws InterruptedException {
        System.out.println("Experiment Number, Global Start Time (ms), T1 Time (ms), T2 Time(ms)");
        for(int i = 1 ; i<=EXPERIMENT_COUNT; i++){
            createAndUpdateTodo(i);
            Thread.sleep(SLEEP_TIME);
        }
    }

    private static int createTodo(int experimentNumber){
        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, TEST_TITLE);
        givenBody.put(DESCRIPTION, TEST_DESCRIPTION);
        givenBody.put(DONE, true);

        Response r = given().
                body(givenBody).
                when().
                post(ALL_TODOS_PATH).thenReturn();

        String id = r.then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_CREATED)
                .body(
                        TITLE, equalTo(TEST_TITLE),
                        DESCRIPTION, equalTo(TEST_DESCRIPTION),
                        DONE, equalTo(TRUE)
                )
                .extract()
                .path(ID);

        return Integer.parseInt(id);
    }

    private static int createAndUpdateTodo(int experimentNumber){
        int id = createTodo(-1);

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, OTHER_TEST_TITLE);
        givenBody.put(DESCRIPTION, OTHER_TEST_DESCRIPTION);
        givenBody.put(DONE, false);

        long t1StartTime = System.currentTimeMillis();

        long t2StartTime = System.currentTimeMillis();
        Response r = given().
                pathParam(ID, id).
                body(givenBody).
                when().
                put(SPECIFIC_TODO_PATH)
                .thenReturn();

        long t2Time = System.currentTimeMillis() - t2StartTime;
                r.then().contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK)
                .body(
                        ID, equalTo(String.valueOf(id)),
                        TITLE, equalTo(OTHER_TEST_TITLE),
                        DESCRIPTION, equalTo(OTHER_TEST_DESCRIPTION),
                        DONE, equalTo(FALSE)

                );

        long t1Time = System.currentTimeMillis() - t1StartTime;


        long globalTime = System.currentTimeMillis() - globalStartTime;

        if(experimentNumber != -1) {
            System.out.printf("%d,%d,%d,%d\n",
                    experimentNumber, globalTime, t1Time, t2Time);
        }

        return id;
    }

}
