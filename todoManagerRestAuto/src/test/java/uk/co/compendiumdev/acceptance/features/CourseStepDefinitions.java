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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CourseStepDefinitions {
    private static final String ALL_PROJECTS_PATH = "/projects";
    private static final String ALL_TODOS_PATH = "/todos";
    private static final String SPECIFIC_PROJECTS_PATH = "/projects/{id}";
    private static final String PROJECT_TO_TODO_PATH = "/projects/{id}/tasks";
    private static final String TODO_TO_PROJECT_PATH = "/todos/{id}/tasksof";


    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String TODOS = "todos";
    private static final String PROJECTS = "projects";
    private static final String ACTIVE = "active";
    private static final String COMPLETED = "completed";

    public static final Map<String, String> courseToId = new HashMap<>();

    @Given("^Projects exist for the following courses:$")
    public void projectsExistForTheFollowingCourses(DataTable table) {
        List<DataTableRow> rows = table.getGherkinRows();
        for(int i = 1; i<rows.size() ; i++){
            List<String> cells = rows.get(i).getCells();

            String course = cells.get(0);

            final HashMap<String, Object> givenBody = new HashMap<>();
            givenBody.put(TITLE, course);

            String id = given().
                    body(givenBody).
                    when().
                    post(ALL_PROJECTS_PATH).
                    then().
                    contentType(ContentType.JSON).
                    statusCode(HttpStatus.SC_CREATED).
                    body(
                            TITLE, equalTo(course)
                    ).
                    extract().
                    path(ID);

            courseToId.put(course, id);
        }
    }

    @Given("^The following tasks exist for their respective courses:$")
    public void theFollowingTasksExistForTheirRespectiveCourses(DataTable table) {
        List<DataTableRow> rows = table.getGherkinRows();
        for(int i = 1; i<rows.size() ; i++){
            List<String> cells = rows.get(i).getCells();

            String course = cells.get(0);
            String taskName = cells.get(1);

            final HashMap<String, Object> givenBody = new HashMap<>();
            givenBody.put(TITLE, taskName);

            String taskId = given().
                    body(givenBody).
                    when().
                    post(ALL_TODOS_PATH).
                    then().
                    contentType(ContentType.JSON).
                    statusCode(HttpStatus.SC_CREATED).
                    body(
                            TITLE, equalTo(taskName)
                    ).
                    extract()
                    .path(ID);

            TaskStepDefinitions.taskToId.put(taskName, taskId);

            givenBody.clear();
            givenBody.put(ID, String.valueOf(taskId));

            given().
                    pathParam(ID, courseToId.get(course)).
                    body(givenBody).
                    when().
                    post(PROJECT_TO_TODO_PATH)
                    .then()
                    .statusCode(HttpStatus.SC_CREATED);

            givenBody.clear();
            givenBody.put(ID, courseToId.get(course));

            given().
                    pathParam(ID, taskId).
                    body(givenBody).
                    when().
                    post(TODO_TO_PROJECT_PATH)
                    .then()
                    .statusCode(HttpStatus.SC_CREATED);
        }
    }

    @Given("^No tasks exist for each course$")
    public void noTasksExistForEachCourse() {
        for(Map.Entry<String, String> entry : courseToId.entrySet()){
            List<Map<String, Object>> tasks =
                    given().
                            pathParam(ID, entry.getValue()).
                            when().
                            get(PROJECT_TO_TODO_PATH).
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

    @When("^I add the task \"([^\"]*)\" to the \"([^\"]*)\" course to do list$")
    public void iAddTheTaskToTheCourseToDoList(String taskName, String course) throws Throwable {
        // Assign task to project
        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(ID, String.valueOf(TaskStepDefinitions.taskToId.get(taskName)));

        given().
                pathParam(ID, CourseStepDefinitions.courseToId.getOrDefault(course, "-1")).
                body(givenBody).
                when().
                post(PROJECT_TO_TODO_PATH);
    }

    @When("^I remove the \"([^\"]*)\" course to do list$")
    public void iRemoveTheCourseToDoList(String arg0) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @When("^I create a course to do list for course \"([^\"]*)\" with active status \"([^\"]*)\" and completion status \"([^\"]*)\"$")
    public void iCreateACourseToDoListForCourseWithActiveStatusAndCompletionStatus(String arg0, String arg1, String arg2) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @When("^I create a course to do list for course \"([^\"]*)\" with active status \"([^\"]*)\", completion status \"([^\"]*)\" and description \"([^\"]*)\"$")
    public void iCreateACourseToDoListForCourseWithActiveStatusCompletionStatusAndDescription(String arg0, String arg1, String arg2, String arg3) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^\"([^\"]*)\" course to do list should contain (\\d+) tasks$")
    public void courseToDoListShouldContainTasks(String course, int taskCount) throws Throwable {
        List<Map<String, Object>> tasks =
                given().
                        pathParam(ID, courseToId.get(course)).
                        when().
                        get(PROJECT_TO_TODO_PATH).
                        then().
                        statusCode(HttpStatus.SC_OK).
                        contentType(ContentType.JSON).
                        extract().
                        body().
                        jsonPath().
                        getList(TODOS);

        assertEquals(taskCount, tasks.size());
    }

    @Then("^Course to do list \"([^\"]*)\" should not exist in the system$")
    public void courseToDoListShouldNotExistInTheSystem(String course) throws Throwable {
        List<Map<String, Object>> projects =
                given().
                        when().
                        get(ALL_PROJECTS_PATH).
                        then().
                        statusCode(HttpStatus.SC_OK).
                        contentType(ContentType.JSON).
                        extract().
                        body().
                        jsonPath().
                        getList(PROJECTS);

        assertTrue(projects.stream().noneMatch(
                project-> project.get(TITLE).equals(course)
                )
        );
    }

    @Then("^\"([^\"]*)\" course to do list should contain task \"([^\"]*)\"$")
    public void courseToDoListShouldContainTask(String course, String taskName) throws Throwable {
        List<Map<String, Object>> tasks =
                given().
                        pathParam(ID, courseToId.get(course)).
                        when().
                        get(PROJECT_TO_TODO_PATH).
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

    @Then("^\"([^\"]*)\" course to do list should not contain task \"([^\"]*)\"$")
    public void courseToDoListShouldNotContainTask(String course, String taskName) throws Throwable {
        List<Map<String, Object>> tasks =
                given().
                        pathParam(ID, courseToId.get(course)).
                        when().
                        get(PROJECT_TO_TODO_PATH).
                        then().
                        statusCode(HttpStatus.SC_OK).
                        contentType(ContentType.JSON).
                        extract().
                        body().
                        jsonPath().
                        getList(TODOS);

        assertTrue(tasks.stream().noneMatch(
                category-> category.get(TITLE).equals(taskName)
                )
        );
    }

    @Then("^A course to do list for course \"([^\"]*)\" should exist$")
    public void aCourseToDoListForCourseShouldExist(String course) throws Throwable {
        List<Map<String, Object>> projects =
                given().
                        when().
                        get(ALL_PROJECTS_PATH).
                        then().
                        statusCode(HttpStatus.SC_OK).
                        contentType(ContentType.JSON).
                        extract().
                        body().
                        jsonPath().
                        getList(PROJECTS);

        assertTrue(projects.stream().anyMatch(
                project-> project.get(TITLE).equals(course)
                )
        );
    }

    @Then("^The course to do list for course \"([^\"]*)\" should have active status \"([^\"]*)\"$")
    public void theCourseToDoListForCourseShouldHaveActiveStatus(String course, String active) throws Throwable {
        List<Map<String, Object>> projects =
                given().
                        when().
                        get(ALL_PROJECTS_PATH).
                        then().
                        statusCode(HttpStatus.SC_OK).
                        contentType(ContentType.JSON).
                        extract().
                        body().
                        jsonPath().
                        getList(PROJECTS);

        assertEquals(active, (projects.stream().filter(
                project -> project.get(TITLE).equals(course)
        ).findFirst().orElse(new HashMap<String, Object>())).getOrDefault(ACTIVE, "-1"));
    }

    @Then("^The course to do list for course \"([^\"]*)\" should have completion status \"([^\"]*)\"$")
    public void theCourseToDoListForCourseShouldHaveCompletionStatus(String course, String completed) throws Throwable {
        List<Map<String, Object>> projects =
                given().
                        when().
                        get(ALL_PROJECTS_PATH).
                        then().
                        statusCode(HttpStatus.SC_OK).
                        contentType(ContentType.JSON).
                        extract().
                        body().
                        jsonPath().
                        getList(PROJECTS);

        assertEquals(completed, (projects.stream().filter(
                project -> project.get(TITLE).equals(course)
        ).findFirst().orElse(new HashMap<String, Object>())).getOrDefault(COMPLETED, "-1"));
    }
}
