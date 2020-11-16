package uk.co.compendiumdev.acceptance.features;

import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import gherkin.formatter.model.DataTableRow;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assumptions;
import uk.co.compendiumdev.Environment;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.post;
import static org.hamcrest.Matchers.equalTo;

public class CommonStepDefinitions {
    private static final String CLEAR_PATH = "/admin/data/thingifier";
    private static final String ALL_PROJECTS_PATH = "/projects";
    private static final String ALL_TODOS_PATH = "/todos";
    private static final String ALL_CATEGORIES_PATH = "/categories";
    private static final String CATEGORY_TO_TODO_PATH = "/categories/{id}/todos";
    private static final String SPECIFIC_PROJECTS_PATH = "/projects/{id}";
    private static final String PROJECT_TO_TODO_PATH = "/projects/{id}/tasks";

    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String TODOS = "todos";
    private static final String DONE = "doneStatus";

    public static LinkedList<ExtractableResponse<Response>> lastResponse = new LinkedList<>();

    @Given("^The following tasks exist with their respective statuses, courses and priority levels:$")
    public void theFollowingTasksExistWithTheirRespectiveStatusesCoursesAndPriorityLevels(DataTable table) {
        List<DataTableRow> rows = table.getGherkinRows();
        for(int i = 1; i<rows.size() ; i++){
            List<String> cells = rows.get(i).getCells();

            String taskName = cells.get(0);
            String completed = cells.get(1);
            String course = cells.get(2);
            String priority = cells.get(3);


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

            // Create Priority
            givenBody.clear();
            givenBody.put(TITLE, priority);

            if(!PrioritiesStepDefinitions.priorityToId.containsKey(priority)) {
                String priorityId = given().
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

                PrioritiesStepDefinitions.priorityToId.put(priority, priorityId);
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

            // Assign task to priority
            givenBody.clear();
            givenBody.put(ID, TaskStepDefinitions.taskToId.get(taskName));

                    given().
                            pathParam(ID, PrioritiesStepDefinitions.priorityToId.getOrDefault(priority, "-1")).
                            body(givenBody).
                            when().
                            post(CATEGORY_TO_TODO_PATH)
                            .then().extract();
        }
    }

    @Given("^The application is running$")
    public void theApplicationIsRunning() {
        RestAssured.baseURI = Environment.getBaseUri();
        Assumptions.assumeTrue(Environment.getBaseUri() != null);
        post(CLEAR_PATH);
        CourseStepDefinitions.courseToId.clear();
        TaskStepDefinitions.taskToId.clear();
        PrioritiesStepDefinitions.priorityToId.clear();
    }
}
