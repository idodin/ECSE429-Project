package uk.co.compendiumdev.unit.crud;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.compendiumdev.Environment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.fail;

public class RelationshipTest {
    // Clear Data Path
    private static final String CLEAR_PATH = "/admin/data/thingifier";

    // Projects Paths
    private static final String ALL_PROJECTS_PATH = "/projects";
    private static final String PROJECTS = "projects";

    // Todos Paths
    private static final String ALL_TODOS_PATH = "/todos";
    private static final String TODOS = "todos";

    // Relationship Paths
    private static final String PROJECT_TO_TODO_PATH = "/projects/{id}/tasks";
    private static final String PROJECT_TO_SPECIFIC_TODO_PATH = "/projects/{projectId}/tasks/{todoId}";
    private static final String TODO_TO_PROJECT_PATH = "/todos/{id}/tasksof";
    private static final String TODO_TO_SPECIFIC_PROJECT_PATH = "/todos/{todoId}/tasksof/{projectId}";

    // Fields
    private static final String ID = "id";
    private static final String PROJECT_ID = "projectId";
    private static final String TODO_ID = "todoId";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String COMPLETED = "completed";
    private static final String ACTIVE = "active";
    private static final String DONE = "doneStatus";

    // Test Fields
    private static final String TEST_PROJECT_TITLE = "Some project title";
    private static final String TEST_PROJECT_DESCRIPTION = "Some project description";

    // Useful Constants
    private static final String TRUE = "true";
    private static final String FALSE = "false";

    // Test Fields
    private static final String TEST_TODO_TITLE = "Some todos title";
    private static final String TEST_TODO_DESCRIPTION = "Some todos description";

    private static int projectId = -1;
    private static int todoId = -1;

    @BeforeEach
    public void clearDataFromEnv(){

        RestAssured.baseURI = Environment.getBaseUri();
        if(RestAssured.baseURI == null) fail("To Do Manager isn't running!");

        post(CLEAR_PATH);

        final JsonPath clearedTodoData = when().get(ALL_TODOS_PATH)
                .then().statusCode(200).extract().body().jsonPath();

        final int newNumberOfTodos = clearedTodoData.getList(TODOS).size();

        final JsonPath clearedProjectsData = when().get(ALL_PROJECTS_PATH)
                .then().statusCode(200).extract().body().jsonPath();

        final int newNumberOfProjects = clearedProjectsData.getList(PROJECTS).size();

        Assumptions.assumeTrue(newNumberOfProjects == 0);
        Assumptions.assumeTrue(newNumberOfTodos == 0);
    }

    @BeforeEach
    public void constructTestData(){
        final HashMap<String, Object> givenProjectBody = new HashMap<>();
        givenProjectBody.put(TITLE, TEST_PROJECT_TITLE);
        givenProjectBody.put(DESCRIPTION, TEST_PROJECT_DESCRIPTION);
        givenProjectBody.put(ACTIVE, true);
        givenProjectBody.put(COMPLETED, false);

        String id = given().
                body(givenProjectBody).
                when().
                post(ALL_PROJECTS_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_CREATED)
                .body(
                        TITLE, equalTo(TEST_PROJECT_TITLE),
                        DESCRIPTION, equalTo(TEST_PROJECT_DESCRIPTION),
                        ACTIVE, equalTo(TRUE),
                        COMPLETED, equalTo(FALSE)
                ).extract()
                .path(ID);
        projectId = Integer.parseInt(id);

        final HashMap<String, Object> givenTodoBody = new HashMap<>();
        givenTodoBody.put(TITLE, TEST_TODO_TITLE);
        givenTodoBody.put(DESCRIPTION, TEST_TODO_DESCRIPTION);
        givenTodoBody.put(DONE, true);

        id = given().
                body(givenTodoBody).
                when().
                post(ALL_TODOS_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_CREATED)
                .body(
                        TITLE, equalTo(TEST_TODO_TITLE),
                        DESCRIPTION, equalTo(TEST_TODO_DESCRIPTION),
                        DONE, equalTo(TRUE)
                ).extract()
                .path(ID);
        todoId = Integer.parseInt(id);
    }

    @Test
    public void createProjectToTodo(){

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(ID, String.valueOf(todoId));

        given().
                pathParam(ID, projectId).
                body(givenBody).
                when().
                post(PROJECT_TO_TODO_PATH)
                .then()
                .statusCode(HttpStatus.SC_CREATED);
    }

    @Test
    public void createProjectToTodoXML(){

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(ID, String.valueOf(todoId));

        given().
                pathParam(ID, projectId).
                accept(ContentType.XML).
                body(givenBody).
                when().
                post(PROJECT_TO_TODO_PATH)
                .then()
                .statusCode(HttpStatus.SC_CREATED);
    }

    @Test
    public void createProjectToTodoEnforcesRefIntegrity(){

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(ID, String.valueOf(todoId));

        given().
                pathParam(ID, projectId).
                body(givenBody).
                when().
                post(PROJECT_TO_TODO_PATH)
                .then()
                .statusCode(HttpStatus.SC_CREATED);

        final HashMap<String, Object> getAllBody = new HashMap<>();
        getAllBody.put(ID, String.valueOf(todoId));

        List<Map<String, Object>> todos = given().
                pathParam(ID, todoId).
                body(getAllBody).
                when().
                get(TODO_TO_PROJECT_PATH)
                .then()
                .statusCode(HttpStatus.SC_OK).
                        extract().body().jsonPath().getList(PROJECTS);

        Assertions.assertEquals(1, todos.size());
    }

    @Test
    public void deleteProjectToTodoSuccessfully(){
        createProjectToTodo();

        final HashMap<String, Object> givenBodyBefore = new HashMap<>();
        givenBodyBefore.put(ID, String.valueOf(todoId));

        List<Map<String, Object>> todosBefore = given().
                pathParam(ID, projectId).
                body(givenBodyBefore).
                when().
                get(PROJECT_TO_TODO_PATH)
                .then()
                .statusCode(HttpStatus.SC_OK).
                        extract().body().jsonPath().getList(TODOS);

        Assertions.assertEquals(1, todosBefore.size());

        given().
                pathParam(PROJECT_ID, projectId).
                pathParam(TODO_ID, todoId).
                when().
                delete(PROJECT_TO_SPECIFIC_TODO_PATH)
                .then()
                .statusCode(HttpStatus.SC_OK);

        final HashMap<String, Object> givenBodyAfter = new HashMap<>();
        givenBodyAfter.put(ID, String.valueOf(todoId));

        List<Map<String, Object>> todosAfter = given().
                pathParam(ID, projectId).
                body(givenBodyAfter).
                when().
                get(PROJECT_TO_TODO_PATH)
                .then()
                .statusCode(HttpStatus.SC_OK).
                        extract().body().jsonPath().getList(TODOS);

        Assertions.assertEquals(0, todosAfter.size());
    }

    // BUG
    // CURRENTLY RETURNS 400
    @Test
    public void deleteProjectToTodoReturns400IfProjectDoesntExist(){
        createProjectToTodo();

        given().
                pathParam(PROJECT_ID, 100001).
                pathParam(TODO_ID, todoId).
                when().
                delete(PROJECT_TO_SPECIFIC_TODO_PATH)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    // SHOULD RETURN 404
    @Test
    public void deleteProjectToTodoDoesNotReturn404IfProjectDoesntExist(){
        createProjectToTodo();

        given().
                pathParam(PROJECT_ID, 100001).
                pathParam(TODO_ID, todoId).
                when().
                delete(PROJECT_TO_SPECIFIC_TODO_PATH)
                .then()
                .statusCode(not(equalTo(HttpStatus.SC_NOT_FOUND)));
    }

    @Test
    public void deleteProjectToTodoReturns404IfTodoDoesntExist(){
        createProjectToTodo();

        given().
                pathParam(PROJECT_ID, projectId).
                pathParam(TODO_ID, "100001").
                when().
                delete(PROJECT_TO_SPECIFIC_TODO_PATH)
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void rejectProjectToTodoWhenProjectDoesntExist(){

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(ID, "1001");

        given().
                pathParam(ID, projectId).
                body(givenBody).
                when().
                post(PROJECT_TO_TODO_PATH)
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void rejectProjectToTodoWhenNonIntegerIdIsPassed(){

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(ID, "adsfsaf");

        given().
                pathParam(ID, projectId).
                body(givenBody).
                when().
                post(PROJECT_TO_TODO_PATH)
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void getProjectToTodoReturnsAll(){
        createProjectToTodo();

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(ID, String.valueOf(todoId));

        List<Map<String, Object>> todos = given().
                pathParam(ID, projectId).
                body(givenBody).
                when().
                get(PROJECT_TO_TODO_PATH)
                .then()
                .statusCode(HttpStatus.SC_OK).
                extract().body().jsonPath().getList(TODOS);

        Assertions.assertEquals(1, todos.size());
    }

    @Test
    public void getProjectToTodoReturnsAllXML(){
        createProjectToTodo();

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(ID, String.valueOf(todoId));

        given().
                pathParam(ID, projectId).
                accept(ContentType.XML).
                body(givenBody).
                when().
                get(PROJECT_TO_TODO_PATH)
                .then()
                .statusCode(HttpStatus.SC_OK).
                        contentType(ContentType.XML);
    }

    @Test
    public void getProjectToTodoReturnsNone(){
        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(ID, String.valueOf(todoId));

        List<Map<String, Object>> todos = given().
                pathParam(ID, projectId).
                body(givenBody).
                when().
                get(PROJECT_TO_TODO_PATH)
                .then()
                .statusCode(HttpStatus.SC_OK).
                        extract().body().jsonPath().getList(TODOS);

        Assertions.assertEquals(0, todos.size());
    }

    @Test
    public void createTodoToProject(){

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(ID, String.valueOf(projectId));

        given().
                pathParam(ID, todoId).
                body(givenBody).
                when().
                post(TODO_TO_PROJECT_PATH)
                .then()
                .statusCode(HttpStatus.SC_CREATED);
    }

    @Test
    public void createTodoToProjectEnforcesRefIntegrity(){

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(ID, String.valueOf(projectId));

        given().
                pathParam(ID, todoId).
                body(givenBody).
                when().
                post(TODO_TO_PROJECT_PATH)
                .then()
                .statusCode(HttpStatus.SC_CREATED);

        final HashMap<String, Object> getAllBody = new HashMap<>();
        getAllBody.put(ID, String.valueOf(todoId));

        List<Map<String, Object>> todos = given().
                pathParam(ID, projectId).
                body(getAllBody).
                when().
                get(PROJECT_TO_TODO_PATH)
                .then()
                .statusCode(HttpStatus.SC_OK).
                        extract().body().jsonPath().getList(TODOS);

        Assertions.assertEquals(1,
                todos.size()
        );
    }


    @Test
    public void getToDoToProjectReturnsAll(){
        createProjectToTodo();

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(ID, String.valueOf(projectId));

        List<Map<String, Object>> todos = given().
                pathParam(ID, todoId).
                body(givenBody).
                when().
                get(TODO_TO_PROJECT_PATH)
                .then()
                .statusCode(HttpStatus.SC_OK).
                        extract().body().jsonPath().getList(PROJECTS);

        Assertions.assertEquals(1,todos.size());
    }

    @Test
    public void getToDoToProjectReturnsAllInXML(){
        createProjectToTodo();

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(ID, String.valueOf(projectId));

        given().
                pathParam(ID, todoId).
                accept(ContentType.XML).
                body(givenBody).
                when().
                get(TODO_TO_PROJECT_PATH)
                .then()
                .statusCode(HttpStatus.SC_OK);

    }

    @Test
    public void getToDoToProjectReturnsNone(){

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(ID, String.valueOf(projectId));

        List<Map<String, Object>> todos = given().
                pathParam(ID, todoId).
                body(givenBody).
                when().
                get(TODO_TO_PROJECT_PATH)
                .then()
                .statusCode(HttpStatus.SC_OK).
                        extract().body().jsonPath().getList(PROJECTS);

        Assertions.assertEquals(0, todos.size());
    }

    @Test
    public void deleteTodoToProjectSuccessfully(){
        createTodoToProject();

        final HashMap<String, Object> givenBodyBefore = new HashMap<>();
        givenBodyBefore.put(ID, String.valueOf(projectId));

        List<Map<String, Object>> projectsBefore = given().
                pathParam(ID, todoId).
                body(givenBodyBefore).
                when().
                get(TODO_TO_PROJECT_PATH)
                .then()
                .statusCode(HttpStatus.SC_OK).
                        extract().body().jsonPath().getList(PROJECTS);

        Assertions.assertEquals(1, projectsBefore.size());

        given().
                pathParam(PROJECT_ID, projectId).
                pathParam(TODO_ID, todoId).
                when().
                delete(TODO_TO_SPECIFIC_PROJECT_PATH)
                .then()
                .statusCode(HttpStatus.SC_OK);

        final HashMap<String, Object> givenBodyAfter = new HashMap<>();
        givenBodyAfter.put(ID, String.valueOf(projectId));

        List<Map<String, Object>> projectsAfter = given().
                pathParam(ID, todoId).
                body(givenBodyAfter).
                when().
                get(TODO_TO_PROJECT_PATH)
                .then()
                .statusCode(HttpStatus.SC_OK).
                        extract().body().jsonPath().getList(PROJECTS);

        Assertions.assertEquals(projectsAfter.size(), 0);
    }

    @Test
    public void deleteTodoToProjectReturns404IfProjectDoesntExist(){
        createProjectToTodo();

        given().
                pathParam(PROJECT_ID, 100001).
                pathParam(TODO_ID, todoId).
                when().
                delete(TODO_TO_SPECIFIC_PROJECT_PATH)
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void deleteTodoToProjectReturns404IfTodoDoesntExist(){
        createProjectToTodo();

        given().
                pathParam(PROJECT_ID, projectId).
                pathParam(TODO_ID, "100001").
                when().
                delete(PROJECT_TO_SPECIFIC_TODO_PATH)
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    // BUG
    // Currently returns projects for task that doesnt exist
    @Test
    public void getToDoToProjectReturnsProjectsForTaskThatDoesntExist(){
        createTodoToProject();

        List<Map<String, Object>> projects = given().
                pathParam(ID,  1001).
                when().
                get(TODO_TO_PROJECT_PATH)
                .then()
                .statusCode(HttpStatus.SC_OK).
                extract().body().jsonPath().getList(PROJECTS);

        Assertions.assertEquals(1, projects.size());
    }

    // Should return 404 if task doesnt exist
    @Test
    public void getToDoToSpecificProjectDoesNotReturn404WhenTaskDoesntExist(){
        createTodoToProject();

        given().
                pathParam(ID,  1001).
                when().
                get(TODO_TO_PROJECT_PATH)
                .then()
                .statusCode(not(equalTo(HttpStatus.SC_NOT_FOUND)));
    }
}
