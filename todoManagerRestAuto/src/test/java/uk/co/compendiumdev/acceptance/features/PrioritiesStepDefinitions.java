package uk.co.compendiumdev.acceptance.features;

import cucumber.api.DataTable;
import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import gherkin.formatter.model.DataTableRow;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.post;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PrioritiesStepDefinitions {
    private static final String ALL_CATEGORIES_PATH = "/categories";
    private static final String SPECIFIC_CATEGORIES_PATH = "/categories/{id}";
    private static final String CATEGORY_TO_TODO_PATH = "/categories/{id}/todos";
    private static final String TODO_TO_CATEGORY_PATH = "/todos/{id}/categories";


    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String TODOS = "todos";

    public static final Map<String, String> priorityToId = new HashMap<>();

    @Given("^Categories exist for the following priority levels:$")
    public void categoriesExistForTheFollowingPriorityLevels(DataTable table) {
        List<DataTableRow> rows = table.getGherkinRows();

        for(int i = 1; i<rows.size() ; i++){
            List<String> cells = rows.get(i).getCells();

            String priority = cells.get(0);

            final HashMap<String, Object> givenBody = new HashMap<>();
            givenBody.put(TITLE, priority);

            String id = given().
                            body(givenBody).
                        when().
                            post(ALL_CATEGORIES_PATH).
                        then().
                            contentType(ContentType.JSON).
                            statusCode(HttpStatus.SC_CREATED).
                            body(
                                TITLE, equalTo(priority)
                            ).
                            extract().
                            path(ID);

            priorityToId.put(priority, id);
        }
    }

    @Given("^No tasks exist for each priority level$")
    public void noTasksExistForEachPriorityLevel() {
        for(Map.Entry<String, String> entry : priorityToId.entrySet()){
            List<Map<String, Object>> tasks =
                    given().
                        pathParam(ID, entry.getValue()).
                    when().
                        get(CATEGORY_TO_TODO_PATH).
                    then().
                        statusCode(HttpStatus.SC_OK).
                        contentType(ContentType.JSON).
                        extract().
                        body().
                        jsonPath().
                        getList(TODOS);

            assertEquals(0, tasks.size());
        }
    }

    @When("^I categorize task \"([^\"]*)\" as \"([^\"]*)\" priority$")
    public void iCategorizeTaskAsPriority(String taskName, String priority) throws Throwable {
        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(ID, TaskStepDefinitions.taskToId.get(taskName));

        CommonStepDefinitions.lastResponse.addFirst(
                given().
                pathParam(ID, priorityToId.getOrDefault(priority, "-1")).
                body(givenBody).
                when().
                post(CATEGORY_TO_TODO_PATH)
                .then().extract()
        );
    }

    @Then("^Category \"([^\"]*)\" should contain task \"([^\"]*)\"$")
    public void categoryShouldContainTask(String priority, String taskName) throws Throwable {
        List<Map<String, Object>> tasks =
                given().
                        pathParam(ID, priorityToId.get(priority)).
                        when().
                        get(CATEGORY_TO_TODO_PATH).
                        then().
                        statusCode(HttpStatus.SC_OK).
                        contentType(ContentType.JSON).
                        extract().
                        body().
                        jsonPath().
                        getList(TODOS);

        assertTrue(tasks.stream().anyMatch(
                category-> category.get(TITLE).equals(taskName)
                )
        );
    }

    @Then("^Category \"([^\"]*)\" should still contain its original tasks$")
    public void categoryShouldStillContainItsOriginalTasks(String priority) throws Throwable {
        List<Map<String, Object>> tasks =
                given().
                        pathParam(ID, priorityToId.get(priority)).
                        when().
                        get(CATEGORY_TO_TODO_PATH).
                        then().
                        statusCode(HttpStatus.SC_OK).
                        contentType(ContentType.JSON).
                        extract().
                        body().
                        jsonPath().
                        getList(TODOS);

        assertTrue(tasks.size()>1, "Multiple tasks should exist for priority!");
    }

    @And("^Category \"([^\"]*)\" should not contain task \"([^\"]*)\"$")
    public void categoryShouldNotContainTask(String priority, String taskName) throws Throwable {
        List<Map<String, Object>> tasks =
                given().
                        pathParam(ID, priorityToId.get(priority)).
                        when().
                        get(CATEGORY_TO_TODO_PATH).
                        then().
                        statusCode(HttpStatus.SC_OK).
                        contentType(ContentType.JSON).
                        extract().
                        body().
                        jsonPath().
                        getList(TODOS);

        assertTrue(tasks.stream().anyMatch(
                category-> category.get(TITLE).equals(taskName)
                )
        );
    }
}
