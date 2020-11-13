package uk.co.compendiumdev.unit.crud;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import uk.co.compendiumdev.Environment;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.fail;

public class TodoCrudTest {
    // Todos Paths
    private static final String ALL_TODOS_PATH = "/todos";
    private static final String SPECIFIC_TODO_PATH = "/todos/{id}";
    private static final String CLEAR_PATH = "/admin/data/thingifier";
    private static final String TODOS = "todos";
    private static final String TODO= "todo";

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
    private static final String EMPTY_STRING = "";
    private static final String WHITESPACE_STRING = " ";
    private static final String TRUE = "true";
    private static final String FALSE = "false";
    private static final int SOME_INTEGER = 1;
    private static final boolean SOME_BOOLEAN = true;

    // Response Codes

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
    public int postCreatesWithFullBody(){

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, TEST_TITLE);
        givenBody.put(DESCRIPTION, TEST_DESCRIPTION);
        givenBody.put(DONE, true);

        String id = given().
            body(givenBody).
        when().
            post(ALL_TODOS_PATH)
        .then()
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
    

    @Test
    public void postTodoWithXML() throws ParserConfigurationException, TransformerException, TransformerConfigurationException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();

        Document body = documentBuilder.newDocument();
        Element root = body.createElement(TODO);
        body.appendChild(root);

        Element title = body.createElement(TITLE);
        title.appendChild(body.createTextNode(TEST_TITLE));
        root.appendChild(title);


        Element description = body.createElement(DESCRIPTION);
        description.appendChild(body.createTextNode(TEST_DESCRIPTION));
        root.appendChild(description);

        Element doneStatus = body.createElement(DONE);
        doneStatus.appendChild(body.createTextNode(TRUE));
        root.appendChild(doneStatus);

        

        Transformer transformer = (TransformerFactory.newInstance()).newTransformer();
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(body), new StreamResult(writer));

        String xmlBody = writer.getBuffer().toString();

        given().
                body(xmlBody).
                contentType(ContentType.XML).
                accept(ContentType.XML).
        when().
                post(ALL_TODOS_PATH).
        then().
                contentType(ContentType.XML).
                statusCode(HttpStatus.SC_CREATED).
                body(
                        TODO+"."+TITLE, equalTo(TEST_TITLE),
                        TODO+"."+DESCRIPTION, equalTo(TEST_DESCRIPTION),
                        TODO+"."+DONE, equalTo(TRUE)
                        
                );
    }
    
    
   
    @Test
    public void postCreatesWithoutDone(){

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, TEST_TITLE);
        givenBody.put(DESCRIPTION, TEST_DESCRIPTION);
       

        given().
                body(givenBody).
                when().
                post(ALL_TODOS_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_CREATED)
                .body(
                        TITLE, equalTo(TEST_TITLE),
                        DESCRIPTION, equalTo(TEST_DESCRIPTION),
                        DONE, equalTo(FALSE)
                        
                );
    }
    
    
    
    
    @Test
    public void postCreatesWithoutDescription(){

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, TEST_TITLE);
        givenBody.put(DONE, true);

        given().
                body(givenBody).
                when().
                post(ALL_TODOS_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_CREATED)
                .body(
                        TITLE, equalTo(TEST_TITLE),
                        DESCRIPTION, equalTo(EMPTY_STRING),
                        DONE, equalTo(TRUE)
                       
                );
    }
    
    @Test
    public void postCreatesWithIntegerTitleConverted(){

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, SOME_INTEGER);
        givenBody.put(DESCRIPTION, TEST_DESCRIPTION);
        givenBody.put(DONE, true);
       

        given().
                body(givenBody).
                when().
                post(ALL_TODOS_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_CREATED)
                .body(
                        TITLE, equalTo(Double.toString(SOME_INTEGER)),
                        DESCRIPTION, equalTo(TEST_DESCRIPTION),
                        DONE, equalTo(TRUE)
                );
    }
    @Test
    
    public void postCreatesWithBooleanTitleConverted(){

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, SOME_BOOLEAN);
        givenBody.put(DESCRIPTION, TEST_DESCRIPTION);
        givenBody.put(DONE, true);
       

        given().
                body(givenBody).
                when().
                post(ALL_TODOS_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_CREATED)
                .body(
                        TITLE, equalTo(Boolean.toString(SOME_BOOLEAN)),
                        DESCRIPTION, equalTo(TEST_DESCRIPTION),
                        DONE, equalTo(TRUE)
                       
                );
    }
    
    @Test
    public void postCreatesWithIntegerDescriptionConverted(){

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, TEST_TITLE);
        givenBody.put(DESCRIPTION, SOME_INTEGER);
        givenBody.put(DONE, true);
        

        given().
                body(givenBody).
                when().
                post(ALL_TODOS_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_CREATED)
                .body(
                        TITLE, equalTo(TEST_TITLE),
                        DESCRIPTION, equalTo(Double.toString(SOME_INTEGER)),
                        DONE, equalTo(TRUE)
                       
                );
    }
    
    @Test
    public void postCreatesWithBooleanDescriptionConverted(){

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, TEST_TITLE);
        givenBody.put(DESCRIPTION, SOME_BOOLEAN);
        givenBody.put(DONE, true);
        

        given().
                body(givenBody).
                when().
                post(ALL_TODOS_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_CREATED)
                .body(
                        TITLE, equalTo(TEST_TITLE),
                        DESCRIPTION, equalTo(Boolean.toString(SOME_BOOLEAN)),
                        DONE, equalTo(TRUE)
                      
                );
    }
    
    @Test
    public void postRejectsIntegerDone(){
        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, TEST_TITLE);
        givenBody.put(DESCRIPTION, TEST_DESCRIPTION);
        givenBody.put(DONE, SOME_INTEGER);
      

        given().
                body(givenBody).
                when().
                post(ALL_TODOS_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }
    
    // check if the title is mandatory
    @Test
    public void postWithEmptyTitle(){
        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, EMPTY_STRING);
        givenBody.put(DESCRIPTION, TEST_DESCRIPTION);
        givenBody.put(DONE, true);
      

        given().
                body(givenBody).
                when().
                post(ALL_TODOS_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }
    
    // handle title with white space(title white space rejected)
    @Test
    public void postWithWhiteSpaceTitle(){
        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, WHITESPACE_STRING);
        givenBody.put(DESCRIPTION, TEST_DESCRIPTION);
        givenBody.put(DONE, true);
      

        given().
                body(givenBody).
                when().
                post(ALL_TODOS_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }
    
    //GETS SECTION
    @Test
    public void getTodossReturnsAllTodos(){
        // Create Three Random projects
        postCreatesWithFullBody();
        postCreatesWithBooleanTitleConverted();
        postCreatesWithIntegerTitleConverted();

        List<Map<String, Object>> todos = when().
                get(ALL_TODOS_PATH).
                then().
                contentType(ContentType.JSON).
                statusCode(HttpStatus.SC_OK).
                extract().
                body().
                jsonPath().
                getList(TODOS);

        Assertions.assertTrue(
                todos.size() == 3 &&
                todos.stream().allMatch(
                        object -> object.get(TITLE).equals(TEST_TITLE) ||
                                object.get(TITLE).equals(Double.toString(SOME_INTEGER)) ||
                                object.get(TITLE).equals(Boolean.toString(SOME_BOOLEAN))
                )
        );
    }
 
    @Test
    public void getSpecificTodoReturnsBody() {
        int id = postCreatesWithFullBody();

        Map<String, Object> todo = (Map<String, Object>)given().
                pathParam(ID, id).
        when().
                get(SPECIFIC_TODO_PATH).
        then().
                statusCode(HttpStatus.SC_OK).
                contentType(ContentType.JSON).
                extract().
                body().
                jsonPath().
                getList(TODOS).
                get(0);

        Assertions.assertTrue(
                todo.get(ID).equals(String.valueOf(id)) &&
                todo.get(TITLE).equals(TEST_TITLE) &&
                todo.get(DESCRIPTION).equals(TEST_DESCRIPTION) &&
                todo.get(DONE).equals(TRUE)
        );

    }
    
    // check if can get an instance of todo with id that doesnt exist
    @Test
    public void getSpecificTodoReturnsNotFound() {

        given().
                pathParam(ID, 268351).
                when().
                get(SPECIFIC_TODO_PATH).
                then().
                statusCode(HttpStatus.SC_NOT_FOUND).
                contentType(ContentType.JSON);

    }
   
    @Test
    public void getSpecificTodoHandlesNonIntegerID() {

        given().
                pathParam(ID, "roukzzIsamilo").
                when().
                get(SPECIFIC_TODO_PATH).
                then().
                statusCode(HttpStatus.SC_NOT_FOUND).
                contentType(ContentType.JSON);

    }

    // PATCH SECTION 
    public void patchTodosNotAllowed(){

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, TEST_TITLE);
        givenBody.put(DESCRIPTION, TEST_DESCRIPTION);
        givenBody.put(DONE, true);

        given().
                body(givenBody).
                when().
                patch(ALL_TODOS_PATH)
                .then()
                .statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED);
    }

    //DELETE SECTION
    
    @Test
    public void deleteTodosNotAllowed(){
        when().
                patch(ALL_TODOS_PATH).
                then().
                statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED);
    }
    
   // PUT SECTION 
    @Test
    public void putSpecificUpdatesWithFullBody(){
        int id = postCreatesWithFullBody();

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, OTHER_TEST_TITLE);
        givenBody.put(DESCRIPTION, OTHER_TEST_DESCRIPTION);
        givenBody.put(DONE, false);
       

        given().
                pathParam(ID, id).
                body(givenBody).
                when().
                put(SPECIFIC_TODO_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK)
                .body(
                        ID, equalTo(String.valueOf(id)),
                        TITLE, equalTo(OTHER_TEST_TITLE),
                        DESCRIPTION, equalTo(OTHER_TEST_DESCRIPTION),
                        DONE, equalTo(FALSE)
                       
                );
    }
    
    @Test
    public void putSpecificUpdatesWithIntegerTitleConverted(){
        int id = postCreatesWithFullBody();

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, SOME_INTEGER);
        givenBody.put(DESCRIPTION, OTHER_TEST_DESCRIPTION);
        givenBody.put(DONE, false);
        

        given().
                pathParam(ID, id).
                body(givenBody).
                when().
                put(SPECIFIC_TODO_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK)
                .body(
                        ID, equalTo(String.valueOf(id)),
                        TITLE, equalTo(Double.toString(SOME_INTEGER)),
                        DESCRIPTION, equalTo(OTHER_TEST_DESCRIPTION),
                        DONE, equalTo(FALSE)
                        
                );
    }
    
    @Test
    public void putSpecificUpdatesWithBooleanTitleConverted(){
        int id = postCreatesWithFullBody();

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, SOME_BOOLEAN);
        givenBody.put(DESCRIPTION, OTHER_TEST_DESCRIPTION);
        givenBody.put(DONE, false);
        

        given().
                pathParam(ID, id).
                body(givenBody).
                when().
                put(SPECIFIC_TODO_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK)
                .body(
                        ID, equalTo(String.valueOf(id)),
                        TITLE, equalTo(Boolean.toString(SOME_BOOLEAN)),
                        DESCRIPTION, equalTo(OTHER_TEST_DESCRIPTION),
                        DONE, equalTo(FALSE)
                       
                );
    }
    
    @Test
    public void putSpecificUpdatesWithoutDescription(){
        int id = postCreatesWithFullBody();

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, OTHER_TEST_TITLE);
        givenBody.put(DONE, false);
     
        given().
                pathParam(ID, id).
                body(givenBody).
                when().
                put(SPECIFIC_TODO_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK)
                .body(
                        TITLE, equalTo(OTHER_TEST_TITLE),
                        DESCRIPTION, equalTo(EMPTY_STRING),
                        DONE, equalTo(FALSE)
                       
                );
    }
    
    @Test
    public void putSpecificUpdatesWithoutDone(){
        int id = postCreatesWithFullBody();

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, OTHER_TEST_TITLE);
        givenBody.put(DESCRIPTION, OTHER_TEST_DESCRIPTION);
      

        given().
                pathParam(ID, id).
                body(givenBody).
                when().
                put(SPECIFIC_TODO_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK)
                .body(
                        ID, equalTo(String.valueOf(id)),
                        TITLE, equalTo(OTHER_TEST_TITLE),
                        DESCRIPTION, equalTo(OTHER_TEST_DESCRIPTION),
                        DONE, equalTo(FALSE)
                     
                );
    }
    
    // the taskof tests go bellow
    
    // create a taskof
    
    //get a taskof
    
    // BUG: return a project when we use an non existent ID of a todo.


    
    
     
}    