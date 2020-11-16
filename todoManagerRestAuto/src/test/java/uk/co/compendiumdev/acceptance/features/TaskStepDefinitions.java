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
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TaskStepDefinitions {
    private static final String ALL_PROJECTS_PATH = "/projects";
    private static final String ALL_TODOS_PATH = "/todos";
    private static final String SPECIFIC_PROJECTS_PATH = "/projects/{id}";
    private static final String PROJECT_TO_TODO_PATH = "/projects/{id}/tasks";
    private static final String TODO_TO_CATEGORY_PATH ="/todos/{id}/categories";
    private static final String TODO_TO_PROJECT_PATH = "/todos/{id}/tasksof";

    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String PROJECTS = "projects";
    private static final String TODOS = "todos";
    private static final String CATEGORIES = "categories";
    private static final String DONE = "doneStatus";

    public static Map<String, String> taskToId = new HashMap<>();

    @Given("^The following tasks exist with their respective statuses and courses:$")
    public void theFollowingTasksExistWithTheirRespectiveStatusesAndCourses(DataTable table) {
        List<DataTableRow> rows = table.getGherkinRows();
        for(int i = 1; i<rows.size() ; i++){
            List<String> cells = rows.get(i).getCells();

            String taskName = cells.get(0);
            String completed = cells.get(1);
            String course = cells.get(2);

            final HashMap<String, Object> givenBody = new HashMap<>();

            if(!TaskStepDefinitions.taskToId.containsKey(taskName)) {
                // Create Task
                givenBody.put(TITLE, taskName);
                if (completed.equals("true")) givenBody.put(DONE, true);
                else if (completed.equals("false")) givenBody.put(DONE, false);
                else givenBody.put(DONE, completed);

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
            }

            // Create Course
            if(!CourseStepDefinitions.courseToId.containsKey(course)) {
                givenBody.clear();
                givenBody.put(TITLE, course);

                String courseId = given().
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

                CourseStepDefinitions.courseToId.put(course, courseId);
            }

            // Assign task to project
            givenBody.clear();
            givenBody.put(ID, String.valueOf(TaskStepDefinitions.taskToId.get(taskName)));

            given().
                    pathParam(ID, CourseStepDefinitions.courseToId.get(course)).
                    body(givenBody).
                    when().
                    post(PROJECT_TO_TODO_PATH)
                    .then()
                    .statusCode(HttpStatus.SC_CREATED);
        }
    }

    @Given("^The following tasks exist with their respective courses and descriptions:$")
    public void theFollowingTasksExistWithTheirRespectiveCoursesAndDescriptions(DataTable table) {
    }

    @Given("^Task \"([^\"]*)\" exists for course \"([^\"]*)\" with completion status \"([^\"]*)\" and priority \"([^\"]*)\"$")
    public void taskExistsForCourseWithCompletionStatusAndPriority(String arg0, String arg1, String arg2, String arg3) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @When("^I create a task \"([^\"]*)\"$")
    public void iCreateATask(String taskName) throws Throwable {
        final HashMap<String, Object> givenBody = new HashMap<>();

        if(!TaskStepDefinitions.taskToId.containsKey(taskName)) {
            // Create Task
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
        }
    }

    @When("^I mark task \"([^\"]*)\"'s completion status as \"([^\"]*)\"$")
    public void iMarkTaskSCompletionStatusAs(String arg0, String arg1) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @When("^I remove the task \"([^\"]*)\" from the \"([^\"]*)\" course to do list$")
    public void iRemoveTheTaskFromTheCourseToDoList(String arg0, String arg1) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @When("^I remove the task \"([^\"]*)\" from the \"([^\"]*)\" course to do list again$")
    public void iRemoveTheTaskFromTheCourseToDoListAgain(String arg0, String arg1) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @When("^I query all incomplete tasks for the \"([^\"]*)\" course to do list$")
    public void iQueryAllIncompleteTasksForTheCourseToDoList(String arg0) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @When("^I query all incomplete HIGH priority tasks for all courses$")
    public void iQueryAllIncompleteHIGHPriorityTasksForAllCourses() {
    }

    @When("^I change the description of task \"([^\"]*)\" to \"([^\"]*)\"$")
    public void iChangeTheDescriptionOfTaskTo(String arg0, String arg1) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^Task \"([^\"]*)\" should have description \"([^\"]*)\"$")
    public void taskShouldHaveDescription(String arg0, String arg1) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^Task \"([^\"]*)\" should not be under the \"([^\"]*)\" course to do list$")
    public void taskShouldNotBeUnderTheCourseToDoList(String taskName, String course) throws Throwable {
        List<Map<String, Object>> projects =
                given().
                        pathParam(ID, TaskStepDefinitions.taskToId.get(taskName)).
                        when().
                        get(TODO_TO_PROJECT_PATH).
                        then().
                        statusCode(HttpStatus.SC_OK).
                        contentType(ContentType.JSON).
                        extract().
                        body().
                        jsonPath().
                        getList(PROJECTS);

        assertTrue(projects.stream().noneMatch(
                category-> category.get(TITLE).equals(course)
                )
        );
    }

    @Then("^Task \"([^\"]*)\" should be under the \"([^\"]*)\" course to do list$")
    public void taskShouldBeUnderTheCourseToDoList(String taskName, String course) throws Throwable {
        List<Map<String, Object>> projects =
                given().
                        pathParam(ID, TaskStepDefinitions.taskToId.get(taskName)).
                        when().
                        get(TODO_TO_PROJECT_PATH).
                        then().
                        statusCode(HttpStatus.SC_OK).
                        contentType(ContentType.JSON).
                        extract().
                        body().
                        jsonPath().
                        getList(PROJECTS);

        assertTrue(projects.stream().anyMatch(
                category-> category.get(TITLE).equals(course)
                )
        );
    }

    @Then("^The completion status of task \"([^\"]*)\" should be \"([^\"]*)\"$")
    public void theCompletionStatusOfTaskShouldBe(String arg0, String arg1) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^The completion status of task \"([^\"]*)\" should be false$")
    public void theCompletionStatusOfTaskShouldBeFalse(String arg0) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^Tasks that were under the \"([^\"]*)\" course to do list should not exist in the system$")
    public void tasksThatWereUnderTheCourseToDoListShouldNotExistInTheSystem(String arg0) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^the task \"([^\"]*)\" should be returned$")
    public void theTaskShouldBeReturned(String arg0) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^No tasks should be returned$")
    public void noTasksShouldBeReturned() {
    }

    @Then("^I should receive a list of all incomplete tasks from all courses as a response$")
    public void iShouldReceiveAListOfAllIncompleteTasksFromAllCoursesAsAResponse() {
    }
}
