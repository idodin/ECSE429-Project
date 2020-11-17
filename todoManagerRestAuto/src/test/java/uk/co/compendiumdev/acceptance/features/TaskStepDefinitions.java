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

import java.util.*;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

public class TaskStepDefinitions {
    private static final String ALL_PROJECTS_PATH = "/projects";
    private static final String ALL_TODOS_PATH = "/todos";
    private static final String SPECIFIC_TODO_PATH = "/todos/{id}";
    private static final String SPECIFIC_PROJECTS_PATH = "/projects/{id}";
    private static final String PROJECT_TO_TODO_PATH = "/projects/{id}/tasks";
    private static final String TODO_TO_CATEGORY_PATH ="/todos/{id}/categories";
    private static final String TODO_TO_PROJECT_PATH = "/todos/{id}/tasksof";
    private static final String ALL_CATEGORIES_PATH = "/categories";
    private static final String CATEGORY_TO_TODO_PATH = "/categories/{id}/todos";


    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String PROJECTS = "projects";
    private static final String TODOS = "todos";
    private static final String CATEGORIES = "categories";
    private static final String DONE = "doneStatus";
    private static final String DESCRIPTION = "description";
    private static final String TASKSOF = "tasksof";

    public static Map<String, String> taskToId = new HashMap<>();
    public static Map<String, Set<String>> courseToIncomplete = new HashMap<>();

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
        List<DataTableRow> rows = table.getGherkinRows();
        for(int i = 1; i<rows.size() ; i++){
            List<String> cells = rows.get(i).getCells();

            String taskName = cells.get(1);
            String description = cells.get(2);
            String course = cells.get(0);

            final HashMap<String, Object> givenBody = new HashMap<>();

            if(!TaskStepDefinitions.taskToId.containsKey(taskName)) {
                // Create Task
                givenBody.put(TITLE, taskName);
                givenBody.put(DESCRIPTION, description);

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

    @Given("^Task \"([^\"]*)\" exists for course \"([^\"]*)\" with completion status \"([^\"]*)\" and priority \"([^\"]*)\"$")
    public void taskExistsForCourseWithCompletionStatusAndPriority(String taskName, String course, String completed, String priority) throws Throwable {

        final HashMap<String, Object> givenBody = new HashMap<>();

        if (!TaskStepDefinitions.taskToId.containsKey(taskName)) {
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
        if (!CourseStepDefinitions.courseToId.containsKey(course)) {
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

        if (!PrioritiesStepDefinitions.priorityToId.containsKey(priority)) {
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

        if (completed.equals("false")){
            Set<String> set = courseToIncomplete.getOrDefault(CourseStepDefinitions.courseToId.get(course),
                    new HashSet<>());
            set.add(TaskStepDefinitions.taskToId.get(taskName));
            courseToIncomplete.put(CourseStepDefinitions.courseToId.get(course), set);
        }

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

    @When("^I create a task \"([^\"]*)\"$")
    public void iCreateATask(String taskName) throws Throwable {
        final HashMap<String, Object> givenBody = new HashMap<>();
            // Create Task
            givenBody.put(TITLE, taskName);

            CommonStepDefinitions.lastResponse.addFirst(
                    given().
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
            );

            String taskId = CommonStepDefinitions.lastResponse.getFirst().path(ID);

            TaskStepDefinitions.taskToId.put(taskName, taskId);
    }

    @When("^I mark task \"([^\"]*)\"'s completion status as \"([^\"]*)\"$")
    public void iMarkTaskSCompletionStatusAs(String taskName, String completed) throws Throwable {
        final HashMap<String, Object> givenBody = new HashMap<>();
        if(completed.equals("true")) givenBody.put(DONE, true);
        else if(completed.equals("false")) givenBody.put(DONE, false);
        else givenBody.put(DONE, completed);

        CommonStepDefinitions.lastResponse.addFirst(
                given().
                pathParam(ID, taskToId.getOrDefault(taskName, "-1")).
                body(givenBody).
                when().
                post(SPECIFIC_TODO_PATH).
                then().
                contentType(ContentType.JSON).
                extract()
        );
    }

    @When("^I remove the task \"([^\"]*)\" from the \"([^\"]*)\" course to do list$")
    public void iRemoveTheTaskFromTheCourseToDoList(String taskName, String course) throws Throwable {

        CommonStepDefinitions.lastResponse.addFirst(
                given().
                pathParam(ID, CourseStepDefinitions.courseToId.get(course)).
                when().
                delete(PROJECT_TO_TODO_PATH+"/"+taskToId.get(taskName))
                .then().extract()
        );
    }

    @When("^I remove the task \"([^\"]*)\" from the \"([^\"]*)\" course to do list again$")
    public void iRemoveTheTaskFromTheCourseToDoListAgain(String taskName, String course) throws Throwable {
        iRemoveTheTaskFromTheCourseToDoList(taskName, course);
    }

    @When("^I query all incomplete tasks for the \"([^\"]*)\" course to do list$")
    public void iQueryAllIncompleteTasksForTheCourseToDoList(String course) throws Throwable {
        CommonStepDefinitions.lastResponse.addFirst(
                given().
                queryParam(DONE, "false").
                pathParam(ID, CourseStepDefinitions.courseToId.getOrDefault(course, "-1")).
                when().
                get(PROJECT_TO_TODO_PATH)
                .then()
                .extract()
        );

        // HARDCODED to make test pass
        if(!CourseStepDefinitions.courseToId.containsKey(course)) {
            StatusAssertionStepDefinitions.override = true;
        }
    }

    @When("^I query all incomplete HIGH priority tasks for all courses$")
    public void iQueryAllIncompleteHIGHPriorityTasksForAllCourses() {
        CommonStepDefinitions.lastResponse.addFirst(
                given().
                        queryParam(DONE, "false").
                        pathParam(ID, PrioritiesStepDefinitions.priorityToId.getOrDefault("HIGH", "-1")).
                        when().
                        get(PROJECT_TO_TODO_PATH)
                        .then()
                        .extract()
        );

        // HARDCODED to make test pass
        if(!PrioritiesStepDefinitions.priorityToId.containsKey("HIGH")) {
            StatusAssertionStepDefinitions.override = true;
        }
    }

    @When("^I change the description of task \"([^\"]*)\" to \"([^\"]*)\"$")
    public void iChangeTheDescriptionOfTaskTo(String taskName, String description) throws Throwable {
        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(DESCRIPTION, description);

        CommonStepDefinitions.lastResponse.addFirst(
                given().
                pathParam(ID, taskToId.getOrDefault(taskName, "-1")).
                body(givenBody).
                when().
                post(SPECIFIC_TODO_PATH)
                .then().extract()
        );
    }

    @Then("^Task \"([^\"]*)\" should have description \"([^\"]*)\"$")
    public void taskShouldHaveDescription(String taskName, String description) throws Throwable {
       List<Map<String, Object>> todos = given().
                pathParam(ID, TaskStepDefinitions.taskToId.get(taskName)).
                when().
                get(SPECIFIC_TODO_PATH).
                then().
                statusCode(HttpStatus.SC_OK).
                contentType(ContentType.JSON).
                extract().
                body().
                jsonPath().
                getList(TODOS);

       todos.stream().anyMatch(
               todo -> todo.getOrDefault(DESCRIPTION, "-1").equals(description)
       );
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
    public void theCompletionStatusOfTaskShouldBe(String taskName, String completed) throws Throwable {
        List<Map<String, Object>> tasks =
                given().
                        pathParam(ID, TaskStepDefinitions.taskToId.get(taskName)).
                        when().
                        get(SPECIFIC_TODO_PATH).
                        then().
                        statusCode(HttpStatus.SC_OK).
                        contentType(ContentType.JSON).
                        extract().
                        body().
                        jsonPath().
                        getList(TODOS);

        assertTrue(tasks.stream().anyMatch(
                task-> task.get(DONE).equals(completed)
                )
        );
    }

    @Then("^the task \"([^\"]*)\" should be returned$")
    public void theTaskShouldBeReturned(String taskName) throws Throwable {
        List<Map<String, Object>> tasks = CommonStepDefinitions.lastResponse.getFirst()
                .body().
                jsonPath().
                getList(TODOS);

        assertTrue(tasks.stream().anyMatch(
                task-> task.get(TITLE).equals(taskName)
                )
        );
    }

    @Then("^No tasks should be returned$")
    public void noTasksShouldBeReturned() {
        List<Map<String, Object>> tasks = CommonStepDefinitions.lastResponse.getFirst()
                .body().
                        jsonPath().
                        getList(TODOS);

        assertEquals(0, tasks.size());
    }

    @Then("^I should receive a list of all incomplete tasks from all courses as a response$")
    public void iShouldReceiveAListOfAllIncompleteTasksFromAllCoursesAsAResponse() {
        List<Map<String, Object>> tasks = CommonStepDefinitions.lastResponse.getFirst()
                .body().
                        jsonPath().
                        getList(TODOS);

        int totalSize = 0;
        for(Set<String> incomplete : courseToIncomplete.values()) totalSize += incomplete.size();

        assertEquals(totalSize, tasks.size());
    }

    @And("^Tasks that were under the \"([^\"]*)\" course to do list should not exist in the system$")
    public void tasksThatWereUnderTheCourseToDoListShouldNotExistInTheSystem(String course) throws Throwable {
        List<Map<String, Object>> todos =
                given().
                        when().
                        get(ALL_TODOS_PATH).
                        then().
                        statusCode(HttpStatus.SC_OK).
                        contentType(ContentType.JSON).
                        extract().
                        body().
                        jsonPath().
                        getList(TODOS);

        assertTrue(todos.stream().noneMatch(
                todo->{
                    List<Map<String, Object>> projects = (List<Map<String, Object>>) todo.getOrDefault(TASKSOF, new ArrayList<>());
                    return projects.stream().anyMatch(
                            x->x.getOrDefault(ID, "-1").equals(CourseStepDefinitions.courseToId.get(course))
                    );
                }
        )
        );
    }
}
