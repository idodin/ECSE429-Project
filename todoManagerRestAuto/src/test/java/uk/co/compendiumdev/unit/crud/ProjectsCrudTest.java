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

public class ProjectsCrudTest {
    // Projects Paths
    private static final String ALL_PROJECTS_PATH = "/projects";
    private static final String SPECIFIC_PROJECT_PATH = "/projects/{id}";
    private static final String CLEAR_PATH = "/admin/data/thingifier";
    private static final String PROJECTS = "projects";
    private static final String PROJECT = "project";

    // Project Fields
    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String COMPLETED = "completed";
    private static final String ACTIVE = "active";

    // Test Fields
    private static final String TEST_TITLE = "Some project title";
    private static final String TEST_DESCRIPTION = "Some project description";
    private static final String OTHER_TEST_TITLE = "Some other project title";
    private static final String OTHER_TEST_DESCRIPTION = "Some other project description";

    // Useful Constants
    private static final String EMPTY_STRING = "";
    private static final String WHITESPACE_STRING = " ";
    private static final String TRUE = "true";
    private static final String FALSE = "false";
    private static final String NULL = "null";
    private static final int SOME_INTEGER = 1;
    private static final boolean SOME_BOOLEAN = true;

    private static int id = -1;

    // Response Codes

    @BeforeEach
    public void clearDataFromEnv(){

        RestAssured.baseURI = Environment.getBaseUri();
        if(RestAssured.baseURI == null) fail("To Do Manager isn't running!");

        post(CLEAR_PATH);

        final JsonPath clearedData = when().get(ALL_PROJECTS_PATH)
                .then().statusCode(200).extract().body().jsonPath();

        final int newNumberOfProjects = clearedData.getList(PROJECTS).size();

        // Assume instead of
        Assumptions.assumeTrue(newNumberOfProjects == 0);
    }

    @Test
    public void postCreatesWithFullBody(){

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, TEST_TITLE);
        givenBody.put(DESCRIPTION, TEST_DESCRIPTION);
        givenBody.put(ACTIVE, true);
        givenBody.put(COMPLETED, false);

        String stringId = given().
            body(givenBody).
        when().
            post(ALL_PROJECTS_PATH)
        .then()
            .contentType(ContentType.JSON)
            .statusCode(HttpStatus.SC_CREATED)
            .body(
                    TITLE, equalTo(TEST_TITLE),
                    DESCRIPTION, equalTo(TEST_DESCRIPTION),
                    ACTIVE, equalTo(TRUE),
                    COMPLETED, equalTo(FALSE)
                )
                .extract()
                .path(ID);

        id = Integer.parseInt(stringId);
    }

    @Test
    public void postCreatesWithXML() throws ParserConfigurationException, TransformerException, TransformerConfigurationException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();

        Document body = documentBuilder.newDocument();
        Element root = body.createElement(PROJECT);
        body.appendChild(root);

        Element title = body.createElement(TITLE);
        title.appendChild(body.createTextNode(TEST_TITLE));
        root.appendChild(title);


        Element description = body.createElement(DESCRIPTION);
        description.appendChild(body.createTextNode(TEST_DESCRIPTION));
        root.appendChild(description);

        Element active = body.createElement(ACTIVE);
        active.appendChild(body.createTextNode(TRUE));
        root.appendChild(active);

        Element completed = body.createElement(COMPLETED);
        completed.appendChild(body.createTextNode(FALSE));
        root.appendChild(completed);

        Transformer transformer = (TransformerFactory.newInstance()).newTransformer();
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(body), new StreamResult(writer));

        String xmlBody = writer.getBuffer().toString();

        given().
                body(xmlBody).
                contentType(ContentType.XML).
                accept(ContentType.XML).
        when().
                post(ALL_PROJECTS_PATH).
        then().
                contentType(ContentType.XML).
                statusCode(HttpStatus.SC_CREATED).
                body(
                        PROJECT+"."+TITLE, equalTo(TEST_TITLE),
                        PROJECT+"."+DESCRIPTION, equalTo(TEST_DESCRIPTION),
                        PROJECT+"."+ACTIVE, equalTo(TRUE),
                        PROJECT+"."+COMPLETED, equalTo(FALSE)
                );
    }

    @Test
    public void postCreatesWithoutActive(){

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, TEST_TITLE);
        givenBody.put(DESCRIPTION, TEST_DESCRIPTION);
        givenBody.put(COMPLETED, false);

        given().
                body(givenBody).
                when().
                post(ALL_PROJECTS_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_CREATED)
                .body(
                        TITLE, equalTo(TEST_TITLE),
                        DESCRIPTION, equalTo(TEST_DESCRIPTION),
                        ACTIVE, equalTo(FALSE),
                        COMPLETED, equalTo(FALSE)
                );
    }

    @Test
    public void postCreatesWithoutCompleted(){

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, TEST_TITLE);
        givenBody.put(DESCRIPTION, TEST_DESCRIPTION);
        givenBody.put(ACTIVE, true);

        given().
                body(givenBody).
                when().
                post(ALL_PROJECTS_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_CREATED)
                .body(
                        TITLE, equalTo(TEST_TITLE),
                        DESCRIPTION, equalTo(TEST_DESCRIPTION),
                        ACTIVE, equalTo(TRUE),
                        COMPLETED, equalTo(FALSE)
                );
    }

    @Test
    public void postCreatesWithoutDescription(){

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, TEST_TITLE);
        givenBody.put(ACTIVE, true);
        givenBody.put(COMPLETED, false);

        given().
                body(givenBody).
                when().
                post(ALL_PROJECTS_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_CREATED)
                .body(
                        TITLE, equalTo(TEST_TITLE),
                        DESCRIPTION, equalTo(EMPTY_STRING),
                        ACTIVE, equalTo(TRUE),
                        COMPLETED, equalTo(FALSE)
                );
    }

    @Test
    public void postCreatesWithIntegerTitleConverted(){

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, SOME_INTEGER);
        givenBody.put(DESCRIPTION, TEST_DESCRIPTION);
        givenBody.put(ACTIVE, true);
        givenBody.put(COMPLETED, false);

        given().
                body(givenBody).
                when().
                post(ALL_PROJECTS_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_CREATED)
                .body(
                        TITLE, equalTo(Double.toString(SOME_INTEGER)),
                        DESCRIPTION, equalTo(TEST_DESCRIPTION),
                        ACTIVE, equalTo(TRUE),
                        COMPLETED, equalTo(FALSE)
                );
    }

    @Test
    public void postCreatesWithBooleanTitleConverted(){

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, SOME_BOOLEAN);
        givenBody.put(DESCRIPTION, TEST_DESCRIPTION);
        givenBody.put(ACTIVE, true);
        givenBody.put(COMPLETED, false);

        given().
                body(givenBody).
                when().
                post(ALL_PROJECTS_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_CREATED)
                .body(
                        TITLE, equalTo(Boolean.toString(SOME_BOOLEAN)),
                        DESCRIPTION, equalTo(TEST_DESCRIPTION),
                        ACTIVE, equalTo(TRUE),
                        COMPLETED, equalTo(FALSE)
                );
    }

    @Test
    public void postCreatesWithIntegerDescriptionConverted(){

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, TEST_TITLE);
        givenBody.put(DESCRIPTION, SOME_INTEGER);
        givenBody.put(ACTIVE, true);
        givenBody.put(COMPLETED, false);

        given().
                body(givenBody).
                when().
                post(ALL_PROJECTS_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_CREATED)
                .body(
                        TITLE, equalTo(TEST_TITLE),
                        DESCRIPTION, equalTo(Double.toString(SOME_INTEGER)),
                        ACTIVE, equalTo(TRUE),
                        COMPLETED, equalTo(FALSE)
                );
    }

    @Test
    public void postCreatesWithBooleanDescriptionConverted(){

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, TEST_TITLE);
        givenBody.put(DESCRIPTION, SOME_BOOLEAN);
        givenBody.put(ACTIVE, true);
        givenBody.put(COMPLETED, false);

        given().
                body(givenBody).
                when().
                post(ALL_PROJECTS_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_CREATED)
                .body(
                        TITLE, equalTo(TEST_TITLE),
                        DESCRIPTION, equalTo(Boolean.toString(SOME_BOOLEAN)),
                        ACTIVE, equalTo(TRUE),
                        COMPLETED, equalTo(FALSE)
                );
    }

    @Test
    public void postRejectsIntegerActive(){
        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, TEST_TITLE);
        givenBody.put(DESCRIPTION, TEST_DESCRIPTION);
        givenBody.put(ACTIVE, SOME_INTEGER);
        givenBody.put(COMPLETED, false);

        given().
                body(givenBody).
                when().
                post(ALL_PROJECTS_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void postRejectsIntegerCompleted(){
        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, TEST_TITLE);
        givenBody.put(DESCRIPTION, TEST_DESCRIPTION);
        givenBody.put(ACTIVE, true);
        givenBody.put(COMPLETED, SOME_INTEGER);

        given().
                body(givenBody).
                when().
                post(ALL_PROJECTS_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    // BUG - TITLE CAN BE MISSING
    // CURRENT BEHAVIOUR
    @Test
    public void postWithMissingTitleReturnsEmptyTitle(){

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(DESCRIPTION, TEST_DESCRIPTION);
        givenBody.put(ACTIVE, true);
        givenBody.put(COMPLETED, false);

        given().
                body(givenBody).
                when().
                post(ALL_PROJECTS_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_CREATED)
                .body(
                        TITLE, equalTo(EMPTY_STRING),
                        DESCRIPTION, equalTo(TEST_DESCRIPTION),
                        ACTIVE, equalTo(TRUE),
                        COMPLETED, equalTo(FALSE)
                );
    }

    // DOES NOT HAVE EXPECTED BEHAVIOUR
    @Test
    public void postWithMissingTitleDoesNotReject(){

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(DESCRIPTION, TEST_DESCRIPTION);
        givenBody.put(ACTIVE, true);
        givenBody.put(COMPLETED, false);

        given().
                body(givenBody).
                when().
                post(ALL_PROJECTS_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(not(equalTo(HttpStatus.SC_BAD_REQUEST)));
    }

    // BUG - TITLE CAN BE EMPTY STRING
    // CURRENT BEHAVIOUR
    @Test
    public void postWithEmptyTitleReturnsEmptyTitle(){

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, EMPTY_STRING);
        givenBody.put(DESCRIPTION, TEST_DESCRIPTION);
        givenBody.put(ACTIVE, true);
        givenBody.put(COMPLETED, false);

        given().
                body(givenBody).
                when().
                post(ALL_PROJECTS_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_CREATED)
                .body(
                        TITLE, equalTo(EMPTY_STRING),
                        DESCRIPTION, equalTo(TEST_DESCRIPTION),
                        ACTIVE, equalTo(TRUE),
                        COMPLETED, equalTo(FALSE)
                );
    }

    // DOES NOT HAVE EXPECTED BEHAVIOUR
    @Test
    public void postWithEmptyTitleDoesNotReject(){

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, EMPTY_STRING);
        givenBody.put(DESCRIPTION, TEST_DESCRIPTION);
        givenBody.put(ACTIVE, true);
        givenBody.put(COMPLETED, false);

        given().
                body(givenBody).
                when().
                post(ALL_PROJECTS_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(not(equalTo(HttpStatus.SC_BAD_REQUEST)));
    }

    // BUG - TITLE CAN BE WHITESPACE
    // CURRENT BEHAVIOUR
    @Test
    public void postWithWhitespaceTitleReturnsEmptyTitle(){

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, WHITESPACE_STRING);
        givenBody.put(DESCRIPTION, TEST_DESCRIPTION);
        givenBody.put(ACTIVE, true);
        givenBody.put(COMPLETED, false);

        given().
                body(givenBody).
                when().
                post(ALL_PROJECTS_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_CREATED)
                .body(
                        TITLE, equalTo(WHITESPACE_STRING),
                        DESCRIPTION, equalTo(TEST_DESCRIPTION),
                        ACTIVE, equalTo(TRUE),
                        COMPLETED, equalTo(FALSE)
                );
    }

    // DOES NOT HAVE EXPECTED BEHAVIOUR
    @Test
    public void postWithWhitespaceTitleDoesNotReject(){

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, WHITESPACE_STRING);
        givenBody.put(DESCRIPTION, TEST_DESCRIPTION);
        givenBody.put(ACTIVE, true);
        givenBody.put(COMPLETED, false);

        given().
                body(givenBody).
                when().
                post(ALL_PROJECTS_PATH).
                then().
                contentType(ContentType.JSON).
                statusCode(not(equalTo(HttpStatus.SC_BAD_REQUEST)));
    }

    @Test
    public void getProjectsReturnsAllProjects(){
        // Create Three Random projects
        postCreatesWithFullBody();
        postCreatesWithBooleanTitleConverted();
        postCreatesWithIntegerTitleConverted();

        List<Map<String, Object>> projects = when().
                get(ALL_PROJECTS_PATH).
                then().
                contentType(ContentType.JSON).
                statusCode(HttpStatus.SC_OK).
                extract().
                body().
                jsonPath().
                getList(PROJECTS);

        Assertions.assertTrue(
                projects.size() == 3 &&
                projects.stream().allMatch(
                        object -> object.get(TITLE).equals(TEST_TITLE) ||
                                object.get(TITLE).equals(Double.toString(SOME_INTEGER)) ||
                                object.get(TITLE).equals(Boolean.toString(SOME_BOOLEAN))
                )
        );
    }

    @Test
    public void putProjectsNotAllowed(){

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, TEST_TITLE);
        givenBody.put(DESCRIPTION, TEST_DESCRIPTION);
        givenBody.put(ACTIVE, true);
        givenBody.put(COMPLETED, false);

        given().
                body(givenBody).
                when().
                put(ALL_PROJECTS_PATH)
                .then()
                .statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED);
    }

    @Test
    public void putProjectsNotAllowedXML() throws ParserConfigurationException, TransformerException, TransformerConfigurationException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();

        Document body = documentBuilder.newDocument();
        Element root = body.createElement(PROJECT);
        body.appendChild(root);

        Element title = body.createElement(TITLE);
        title.appendChild(body.createTextNode(TEST_TITLE));
        root.appendChild(title);


        Element description = body.createElement(DESCRIPTION);
        description.appendChild(body.createTextNode(TEST_DESCRIPTION));
        root.appendChild(description);

        Element active = body.createElement(ACTIVE);
        active.appendChild(body.createTextNode(TRUE));
        root.appendChild(active);

        Element completed = body.createElement(COMPLETED);
        completed.appendChild(body.createTextNode(FALSE));
        root.appendChild(completed);

        Transformer transformer = (TransformerFactory.newInstance()).newTransformer();
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(body), new StreamResult(writer));

        String xmlBody = writer.getBuffer().toString();

        given().
                body(xmlBody).
                contentType(ContentType.XML).
                accept(ContentType.XML).
                when().
                put(ALL_PROJECTS_PATH).
                then().
                statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED);
    }

    @Test
    public void patchProjectsNotAllowed(){

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, TEST_TITLE);
        givenBody.put(DESCRIPTION, TEST_DESCRIPTION);
        givenBody.put(ACTIVE, true);
        givenBody.put(COMPLETED, false);

        given().
                body(givenBody).
                when().
                patch(ALL_PROJECTS_PATH)
                .then()
                .statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED);
    }

    @Test
    public void patchProjectsNotAllowedXML() throws ParserConfigurationException, TransformerException, TransformerConfigurationException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();

        Document body = documentBuilder.newDocument();
        Element root = body.createElement(PROJECT);
        body.appendChild(root);

        Element title = body.createElement(TITLE);
        title.appendChild(body.createTextNode(TEST_TITLE));
        root.appendChild(title);


        Element description = body.createElement(DESCRIPTION);
        description.appendChild(body.createTextNode(TEST_DESCRIPTION));
        root.appendChild(description);

        Element active = body.createElement(ACTIVE);
        active.appendChild(body.createTextNode(TRUE));
        root.appendChild(active);

        Element completed = body.createElement(COMPLETED);
        completed.appendChild(body.createTextNode(FALSE));
        root.appendChild(completed);

        Transformer transformer = (TransformerFactory.newInstance()).newTransformer();
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(body), new StreamResult(writer));

        String xmlBody = writer.getBuffer().toString();

        given().
                body(xmlBody).
                contentType(ContentType.XML).
                accept(ContentType.XML).
                when().
                patch(ALL_PROJECTS_PATH).
                then().
                statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED);
    }

    @Test
    public void deleteProjectsNotAllowed(){
        given().
                contentType(ContentType.JSON).
                accept(ContentType.JSON).
        when().
                delete(ALL_PROJECTS_PATH).
                then().
                statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED);
    }

    @Test
    public void deleteProjectsNotAllowedXML(){
        given().
                contentType(ContentType.XML).
                accept(ContentType.XML).
                when().
                delete(ALL_PROJECTS_PATH).
                then().
                statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED);
    }

    @Test
    public void getSpecificProjectReturnsBody() {
        postCreatesWithFullBody();

        Map<String, Object> project = (Map<String, Object>)given().
                pathParam(ID, id).
        when().
                get(SPECIFIC_PROJECT_PATH).
        then().
                statusCode(HttpStatus.SC_OK).
                contentType(ContentType.JSON).
                extract().
                body().
                jsonPath().
                getList(PROJECTS).
                get(0);

        Assertions.assertTrue(
                project.get(ID).equals(String.valueOf(id)) &&
                project.get(TITLE).equals(TEST_TITLE) &&
                project.get(DESCRIPTION).equals(TEST_DESCRIPTION) &&
                project.get(ACTIVE).equals(TRUE) &&
                project.get(COMPLETED).equals(FALSE)
        );

    }

    @Test
    public void getSpecificProjectReturnsXMLBody() {
        postCreatesWithFullBody();

                given().
                    pathParam(ID, id).
                    accept(ContentType.XML).
                when().
                    get(SPECIFIC_PROJECT_PATH).
                then().
                    statusCode(HttpStatus.SC_OK).
                    contentType(ContentType.XML).
                    body(
                            PROJECTS+"."+PROJECT+"."+TITLE, equalTo(TEST_TITLE),
                            PROJECTS+"."+PROJECT+"."+DESCRIPTION, equalTo(TEST_DESCRIPTION),
                            PROJECTS+"."+PROJECT+"."+ACTIVE, equalTo(TRUE),
                            PROJECTS+"."+PROJECT+"."+COMPLETED, equalTo(FALSE)
                    );

    }

    @Test
    public void getSpecificProjectReturnsNotFound() {

        given().
                pathParam(ID, 10001).
                when().
                get(SPECIFIC_PROJECT_PATH).
                then().
                statusCode(HttpStatus.SC_NOT_FOUND).
                contentType(ContentType.JSON);

    }

    @Test
    public void getSpecificProjectHandlesNonIntegerID() {

        given().
                pathParam(ID, "fdsafa").
                when().
                get(SPECIFIC_PROJECT_PATH).
                then().
                statusCode(HttpStatus.SC_NOT_FOUND).
                contentType(ContentType.JSON);

    }

    @Test
    public void putSpecificUpdatesWithFullBody(){
        postCreatesWithFullBody();

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, OTHER_TEST_TITLE);
        givenBody.put(DESCRIPTION, OTHER_TEST_DESCRIPTION);
        givenBody.put(ACTIVE, false);
        givenBody.put(COMPLETED, true);

        given().
                pathParam(ID, id).
                body(givenBody).
                when().
                put(SPECIFIC_PROJECT_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK)
                .body(
                        ID, equalTo(String.valueOf(id)),
                        TITLE, equalTo(OTHER_TEST_TITLE),
                        DESCRIPTION, equalTo(OTHER_TEST_DESCRIPTION),
                        ACTIVE, equalTo(FALSE),
                        COMPLETED, equalTo(TRUE)
                );
    }

    @Test
    public void putSpecificCreatesWithXML() throws ParserConfigurationException, TransformerException, TransformerConfigurationException {
        postCreatesWithFullBody();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();

        Document body = documentBuilder.newDocument();
        Element root = body.createElement(PROJECT);
        body.appendChild(root);

        Element title = body.createElement(TITLE);
        title.appendChild(body.createTextNode(TEST_TITLE));
        root.appendChild(title);


        Element description = body.createElement(DESCRIPTION);
        description.appendChild(body.createTextNode(TEST_DESCRIPTION));
        root.appendChild(description);

        Element active = body.createElement(ACTIVE);
        active.appendChild(body.createTextNode(TRUE));
        root.appendChild(active);

        Element completed = body.createElement(COMPLETED);
        completed.appendChild(body.createTextNode(FALSE));
        root.appendChild(completed);

        Transformer transformer = (TransformerFactory.newInstance()).newTransformer();
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(body), new StreamResult(writer));

        String xmlBody = writer.getBuffer().toString();

        given().
                pathParam(ID, id).
                body(xmlBody).
                contentType(ContentType.XML).
                accept(ContentType.XML).
                when().
                put(SPECIFIC_PROJECT_PATH).
                then().
                contentType(ContentType.XML).
                statusCode(HttpStatus.SC_OK).
                body(
                        PROJECT+"."+TITLE, equalTo(TEST_TITLE),
                        PROJECT+"."+DESCRIPTION, equalTo(TEST_DESCRIPTION),
                        PROJECT+"."+ACTIVE, equalTo(TRUE),
                        PROJECT+"."+COMPLETED, equalTo(FALSE)
                );
    }

    @Test
    public void putSpecificUpdatesWithIntegerTitleConverted(){
        postCreatesWithFullBody();

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, SOME_INTEGER);
        givenBody.put(DESCRIPTION, OTHER_TEST_DESCRIPTION);
        givenBody.put(ACTIVE, false);
        givenBody.put(COMPLETED, true);

        given().
                pathParam(ID, id).
                body(givenBody).
                when().
                put(SPECIFIC_PROJECT_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK)
                .body(
                        ID, equalTo(String.valueOf(id)),
                        TITLE, equalTo(Double.toString(SOME_INTEGER)),
                        DESCRIPTION, equalTo(OTHER_TEST_DESCRIPTION),
                        ACTIVE, equalTo(FALSE),
                        COMPLETED, equalTo(TRUE)
                );
    }

    @Test
    public void putSpecificUpdatesWithBooleanTitleConverted(){
        postCreatesWithFullBody();

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, SOME_BOOLEAN);
        givenBody.put(DESCRIPTION, OTHER_TEST_DESCRIPTION);
        givenBody.put(ACTIVE, false);
        givenBody.put(COMPLETED, true);

        given().
                pathParam(ID, id).
                body(givenBody).
                when().
                put(SPECIFIC_PROJECT_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK)
                .body(
                        ID, equalTo(String.valueOf(id)),
                        TITLE, equalTo(Boolean.toString(SOME_BOOLEAN)),
                        DESCRIPTION, equalTo(OTHER_TEST_DESCRIPTION),
                        ACTIVE, equalTo(FALSE),
                        COMPLETED, equalTo(TRUE)
                );
    }

    @Test
    public void putSpecificUpdatesWithIntegerDescriptionConverted(){
        postCreatesWithFullBody();

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, TEST_TITLE);
        givenBody.put(DESCRIPTION, SOME_INTEGER);
        givenBody.put(ACTIVE, false);
        givenBody.put(COMPLETED, true);

        given().
                pathParam(ID, id).
                body(givenBody).
                when().
                put(SPECIFIC_PROJECT_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK)
                .body(
                        ID, equalTo(String.valueOf(id)),
                        TITLE, equalTo(TEST_TITLE),
                        DESCRIPTION, equalTo(Double.toString(SOME_INTEGER)),
                        ACTIVE, equalTo(FALSE),
                        COMPLETED, equalTo(TRUE)
                );
    }

    @Test
    public void putSpecificUpdatesWithBooleanDescriptionConverted(){
        postCreatesWithFullBody();

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, TEST_TITLE);
        givenBody.put(DESCRIPTION, SOME_BOOLEAN);
        givenBody.put(ACTIVE, false);
        givenBody.put(COMPLETED, true);

        given().
                pathParam(ID, id).
                body(givenBody).
                when().
                put(SPECIFIC_PROJECT_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK)
                .body(
                        ID, equalTo(String.valueOf(id)),
                        TITLE, equalTo(TEST_TITLE),
                        DESCRIPTION, equalTo(Boolean.toString(SOME_BOOLEAN)),
                        ACTIVE, equalTo(FALSE),
                        COMPLETED, equalTo(TRUE)
                );
    }

    @Test
    public void putSpecificUpdatesWithoutDescription(){
        postCreatesWithFullBody();

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, OTHER_TEST_TITLE);
        givenBody.put(ACTIVE, false);
        givenBody.put(COMPLETED, true);

        given().
                pathParam(ID, id).
                body(givenBody).
                when().
                put(SPECIFIC_PROJECT_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK)
                .body(
                        ID, equalTo(String.valueOf(id)),
                        TITLE, equalTo(OTHER_TEST_TITLE),
                        DESCRIPTION, equalTo(EMPTY_STRING),
                        ACTIVE, equalTo(FALSE),
                        COMPLETED, equalTo(TRUE)
                );
    }

    @Test
    public void putSpecificUpdatesWithoutActive(){
        postCreatesWithFullBody();

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, OTHER_TEST_TITLE);
        givenBody.put(DESCRIPTION, OTHER_TEST_DESCRIPTION);
        givenBody.put(COMPLETED, true);

        given().
                pathParam(ID, id).
                body(givenBody).
                when().
                put(SPECIFIC_PROJECT_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK)
                .body(
                        ID, equalTo(String.valueOf(id)),
                        TITLE, equalTo(OTHER_TEST_TITLE),
                        DESCRIPTION, equalTo(OTHER_TEST_DESCRIPTION),
                        ACTIVE, equalTo(FALSE),
                        COMPLETED, equalTo(TRUE)
                );
    }

    @Test
    public void putSpecificUpdatesWithoutCompleted(){
        postCreatesWithFullBody();

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, OTHER_TEST_TITLE);
        givenBody.put(DESCRIPTION, OTHER_TEST_DESCRIPTION);
        givenBody.put(ACTIVE, false);

        given().
                pathParam(ID, id).
                body(givenBody).
                when().
                put(SPECIFIC_PROJECT_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK)
                .body(
                        ID, equalTo(String.valueOf(id)),
                        TITLE, equalTo(OTHER_TEST_TITLE),
                        DESCRIPTION, equalTo(OTHER_TEST_DESCRIPTION),
                        ACTIVE, equalTo(FALSE),
                        COMPLETED, equalTo(FALSE)
                );
    }

    // BUG - TITLE CAN BE EMPTY
    // CURRENT BEHAVIOUR
    @Test
    public void putSpecificWithEmptyTitleReturnsEmptyTitle(){
        postCreatesWithFullBody();

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(DESCRIPTION, OTHER_TEST_DESCRIPTION);
        givenBody.put(COMPLETED, true);
        givenBody.put(ACTIVE, false);

        given().
                pathParam(ID, id).
                body(givenBody).
                when().
                put(SPECIFIC_PROJECT_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK)
                .body(
                        ID, equalTo(String.valueOf(id)),
                        TITLE, equalTo(EMPTY_STRING),
                        DESCRIPTION, equalTo(OTHER_TEST_DESCRIPTION),
                        ACTIVE, equalTo(FALSE),
                        COMPLETED, equalTo(TRUE)
                );
    }

    //DOES NOT HAVE EXPECTED BEHAVIOUR
    @Test
    public void putSpecificWithEmptyTitleDoesNotReject(){
        postCreatesWithFullBody();

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(DESCRIPTION, OTHER_TEST_DESCRIPTION);
        givenBody.put(COMPLETED, true);
        givenBody.put(ACTIVE, false);

        given().
                pathParam(ID, id).
                body(givenBody).
                when().
                put(SPECIFIC_PROJECT_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(not(equalTo(HttpStatus.SC_BAD_REQUEST)));
    }

    // BUG - TITLE CAN BE WHITESPACE
    // CURRENT BEHAVIOUR
    @Test
    public void putSpecificWithWhitespaceTitleReturnsEmptyTitle(){
        postCreatesWithFullBody();

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, WHITESPACE_STRING);
        givenBody.put(DESCRIPTION, OTHER_TEST_DESCRIPTION);
        givenBody.put(COMPLETED, true);
        givenBody.put(ACTIVE, false);

        given().
                pathParam(ID, id).
                body(givenBody).
                when().
                put(SPECIFIC_PROJECT_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK)
                .body(
                        ID, equalTo(String.valueOf(id)),
                        TITLE, equalTo(WHITESPACE_STRING),
                        DESCRIPTION, equalTo(OTHER_TEST_DESCRIPTION),
                        ACTIVE, equalTo(FALSE),
                        COMPLETED, equalTo(TRUE)
                );
    }

    //DOES NOT HAVE EXPECTED BEHAVIOUR
    @Test
    public void putSpecificWithWhitespaceTitleDoesNotReject(){
        postCreatesWithFullBody();

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, WHITESPACE_STRING);
        givenBody.put(DESCRIPTION, OTHER_TEST_DESCRIPTION);
        givenBody.put(COMPLETED, true);
        givenBody.put(ACTIVE, false);

        given().
                pathParam(ID, id).
                body(givenBody).
                when().
                put(SPECIFIC_PROJECT_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(not(equalTo(HttpStatus.SC_BAD_REQUEST)));
    }

    @Test
    public void postSpecificUpdatesWithFullBody(){
        postCreatesWithFullBody();

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, OTHER_TEST_TITLE);
        givenBody.put(DESCRIPTION, OTHER_TEST_DESCRIPTION);
        givenBody.put(ACTIVE, false);
        givenBody.put(COMPLETED, true);

        given().
                pathParam(ID, id).
                body(givenBody).
                when().
                post(SPECIFIC_PROJECT_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK)
                .body(
                        ID, equalTo(String.valueOf(id)),
                        TITLE, equalTo(OTHER_TEST_TITLE),
                        DESCRIPTION, equalTo(OTHER_TEST_DESCRIPTION),
                        ACTIVE, equalTo(FALSE),
                        COMPLETED, equalTo(TRUE)
                );
    }

    @Test
    public void postSpecificCreatesWithXML() throws ParserConfigurationException, TransformerException, TransformerConfigurationException {
        postCreatesWithFullBody();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();

        Document body = documentBuilder.newDocument();
        Element root = body.createElement(PROJECT);
        body.appendChild(root);

        Element title = body.createElement(TITLE);
        title.appendChild(body.createTextNode(TEST_TITLE));
        root.appendChild(title);


        Element description = body.createElement(DESCRIPTION);
        description.appendChild(body.createTextNode(TEST_DESCRIPTION));
        root.appendChild(description);

        Element active = body.createElement(ACTIVE);
        active.appendChild(body.createTextNode(TRUE));
        root.appendChild(active);

        Element completed = body.createElement(COMPLETED);
        completed.appendChild(body.createTextNode(FALSE));
        root.appendChild(completed);

        Transformer transformer = (TransformerFactory.newInstance()).newTransformer();
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(body), new StreamResult(writer));

        String xmlBody = writer.getBuffer().toString();

        given().
                pathParam(ID, id).
                body(xmlBody).
                contentType(ContentType.XML).
                accept(ContentType.XML).
                when().
                post(SPECIFIC_PROJECT_PATH).
                then().
                contentType(ContentType.XML).
                statusCode(HttpStatus.SC_OK).
                body(
                        PROJECT+"."+TITLE, equalTo(TEST_TITLE),
                        PROJECT+"."+DESCRIPTION, equalTo(TEST_DESCRIPTION),
                        PROJECT+"."+ACTIVE, equalTo(TRUE),
                        PROJECT+"."+COMPLETED, equalTo(FALSE)
                );
    }

    @Test
    public void postSpecificUpdatesWithIntegerTitleConverted(){
        postCreatesWithFullBody();

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, SOME_INTEGER);
        givenBody.put(DESCRIPTION, OTHER_TEST_DESCRIPTION);
        givenBody.put(ACTIVE, false);
        givenBody.put(COMPLETED, true);

        given().
                pathParam(ID, id).
                body(givenBody).
                when().
                post(SPECIFIC_PROJECT_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK)
                .body(
                        ID, equalTo(String.valueOf(id)),
                        TITLE, equalTo(Double.toString(SOME_INTEGER)),
                        DESCRIPTION, equalTo(OTHER_TEST_DESCRIPTION),
                        ACTIVE, equalTo(FALSE),
                        COMPLETED, equalTo(TRUE)
                );
    }

    @Test
    public void postSpecificUpdatesWithBooleanTitleConverted(){
        postCreatesWithFullBody();

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, SOME_BOOLEAN);
        givenBody.put(DESCRIPTION, OTHER_TEST_DESCRIPTION);
        givenBody.put(ACTIVE, false);
        givenBody.put(COMPLETED, true);

        given().
                pathParam(ID, id).
                body(givenBody).
                when().
                post(SPECIFIC_PROJECT_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK)
                .body(
                        ID, equalTo(String.valueOf(id)),
                        TITLE, equalTo(Boolean.toString(SOME_BOOLEAN)),
                        DESCRIPTION, equalTo(OTHER_TEST_DESCRIPTION),
                        ACTIVE, equalTo(FALSE),
                        COMPLETED, equalTo(TRUE)
                );
    }

    @Test
    public void postSpecificUpdatesWithoutTitle(){
        postCreatesWithFullBody();

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(DESCRIPTION, OTHER_TEST_DESCRIPTION);
        givenBody.put(ACTIVE, false);
        givenBody.put(COMPLETED, true);

        given().
                pathParam(ID, id).
                body(givenBody).
                when().
                post(SPECIFIC_PROJECT_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK)
                .body(
                        ID, equalTo(String.valueOf(id)),
                        TITLE, equalTo(TEST_TITLE),
                        DESCRIPTION, equalTo(OTHER_TEST_DESCRIPTION),
                        ACTIVE, equalTo(FALSE),
                        COMPLETED, equalTo(TRUE)
                );
    }

    @Test
    public void postSpecificUpdatesWithoutDescription(){
        postCreatesWithFullBody();

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, OTHER_TEST_TITLE);
        givenBody.put(ACTIVE, false);
        givenBody.put(COMPLETED, true);

        given().
                pathParam(ID, id).
                body(givenBody).
                when().
                post(SPECIFIC_PROJECT_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK)
                .body(
                        ID, equalTo(String.valueOf(id)),
                        TITLE, equalTo(OTHER_TEST_TITLE),
                        DESCRIPTION, equalTo(TEST_DESCRIPTION),
                        ACTIVE, equalTo(FALSE),
                        COMPLETED, equalTo(TRUE)
                );
    }

    @Test
    public void postSpecificUpdatesWithoutActive(){
        postCreatesWithFullBody();

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, OTHER_TEST_TITLE);
        givenBody.put(DESCRIPTION, OTHER_TEST_DESCRIPTION);
        givenBody.put(COMPLETED, true);

        given().
                pathParam(ID, id).
                body(givenBody).
                when().
                post(SPECIFIC_PROJECT_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK)
                .body(
                        ID, equalTo(String.valueOf(id)),
                        ID, equalTo(String.valueOf(id)),
                        TITLE, equalTo(OTHER_TEST_TITLE),
                        DESCRIPTION, equalTo(OTHER_TEST_DESCRIPTION),
                        ACTIVE, equalTo(TRUE),
                        COMPLETED, equalTo(TRUE)
                );
    }

    @Test
    public void postSpecificUpdatesWithoutCompleted(){
        postCreatesWithFullBody();

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, OTHER_TEST_TITLE);
        givenBody.put(DESCRIPTION, OTHER_TEST_DESCRIPTION);
        givenBody.put(ACTIVE, false);

        given().
                pathParam(ID, id).
                body(givenBody).
                when().
            post(SPECIFIC_PROJECT_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK)
                .body(
                        ID, equalTo(String.valueOf(id)),
                        TITLE, equalTo(OTHER_TEST_TITLE),
                        DESCRIPTION, equalTo(OTHER_TEST_DESCRIPTION),
                        ACTIVE, equalTo(FALSE),
                        COMPLETED, equalTo(FALSE)
                );
    }

    @Test
    public void postSpecificUpdatesWithIntegerDescriptionConverted(){
        postCreatesWithFullBody();

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, TEST_TITLE);
        givenBody.put(DESCRIPTION, SOME_INTEGER);
        givenBody.put(ACTIVE, false);
        givenBody.put(COMPLETED, true);

        given().
                pathParam(ID, id).
                body(givenBody).
                when().
                post(SPECIFIC_PROJECT_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK)
                .body(
                        ID, equalTo(String.valueOf(id)),
                        TITLE, equalTo(TEST_TITLE),
                        DESCRIPTION, equalTo(Double.toString(SOME_INTEGER)),
                        ACTIVE, equalTo(FALSE),
                        COMPLETED, equalTo(TRUE)
                );
    }

    @Test
    public void postSpecificUpdatesWithBooleanDescriptionConverted(){
        postCreatesWithFullBody();

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, TEST_TITLE);
        givenBody.put(DESCRIPTION, SOME_BOOLEAN);
        givenBody.put(ACTIVE, false);
        givenBody.put(COMPLETED, true);

        given().
                pathParam(ID, id).
                body(givenBody).
                when().
                post(SPECIFIC_PROJECT_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK)
                .body(
                        ID, equalTo(String.valueOf(id)),
                        TITLE, equalTo(TEST_TITLE),
                        DESCRIPTION, equalTo(Boolean.toString(SOME_BOOLEAN)),
                        ACTIVE, equalTo(FALSE),
                        COMPLETED, equalTo(TRUE)
                );
    }

    @Test
    public void postSpecificWithEmptyTitle(){
        postCreatesWithFullBody();

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(DESCRIPTION, OTHER_TEST_DESCRIPTION);
        givenBody.put(COMPLETED, true);
        givenBody.put(ACTIVE, false);

        given().
                pathParam(ID, id).
                body(givenBody).
                when().
                post(SPECIFIC_PROJECT_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK)
                .body(
                        ID, equalTo(String.valueOf(id)),
                        TITLE, equalTo(TEST_TITLE),
                        DESCRIPTION, equalTo(OTHER_TEST_DESCRIPTION),
                        ACTIVE, equalTo(FALSE),
                        COMPLETED, equalTo(TRUE)
                );
    }

    // BUG - TITLE CAN BE WHITESPACE
    // CURRENT BEHAVIOUR
    @Test
    public void postSpecificWithWhitespaceTitleReturnsEmptyTitle(){
        postCreatesWithFullBody();

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, WHITESPACE_STRING);
        givenBody.put(DESCRIPTION, OTHER_TEST_DESCRIPTION);
        givenBody.put(COMPLETED, true);
        givenBody.put(ACTIVE, false);

        given().
                pathParam(ID, id).
                body(givenBody).
                when().
                post(SPECIFIC_PROJECT_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK)
                .body(
                        ID, equalTo(String.valueOf(id)),
                        TITLE, equalTo(WHITESPACE_STRING),
                        DESCRIPTION, equalTo(OTHER_TEST_DESCRIPTION),
                        ACTIVE, equalTo(FALSE),
                        COMPLETED, equalTo(TRUE)
                );
    }

    //DOES NOT HAVE EXPECTED BEHAVIOUR
    @Test
    public void postSpecificWithWhitespaceTitleDoesNotReject(){
        postCreatesWithFullBody();

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, WHITESPACE_STRING);
        givenBody.put(DESCRIPTION, OTHER_TEST_DESCRIPTION);
        givenBody.put(COMPLETED, true);
        givenBody.put(ACTIVE, false);

        given().
                pathParam(ID, id).
                body(givenBody).
                when().
                post(SPECIFIC_PROJECT_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(not(equalTo(HttpStatus.SC_BAD_REQUEST)));
    }

    @Test
    public void deleteSpecificDeletesProject(){
        postCreatesWithFullBody();

        List<Map<String, Object>> projectsBefore = when().
                get(ALL_PROJECTS_PATH).
                then().
                contentType(ContentType.JSON).
                statusCode(HttpStatus.SC_OK).
                extract().
                body().
                jsonPath().
                getList(PROJECTS);

        given().
                pathParam(ID, id).
                when().
                delete(SPECIFIC_PROJECT_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK);

        List<Map<String, Object>> projectsAfter = when().
                get(ALL_PROJECTS_PATH).
                then().
                contentType(ContentType.JSON).
                statusCode(HttpStatus.SC_OK).
                extract().
                body().
                jsonPath().
                getList(PROJECTS);

        Assertions.assertTrue(
                projectsAfter.size() == projectsBefore.size() -1 &&
                        projectsAfter.stream().noneMatch(
                                object -> object.get(ID).equals(String.valueOf(id))
                        )
        );
    }

    @Test
    public void deleteSpecificDeletesProjectXML(){
        postCreatesWithFullBody();

        List<Map<String, Object>> projectsBefore = when().
                get(ALL_PROJECTS_PATH).
                then().
                contentType(ContentType.JSON).
                statusCode(HttpStatus.SC_OK).
                extract().
                body().
                jsonPath().
                getList(PROJECTS);

        given().
                pathParam(ID, id).
                contentType(ContentType.XML).
                accept(ContentType.XML).
                when().
                delete(SPECIFIC_PROJECT_PATH)
                .then()
                .contentType(ContentType.XML)
                .statusCode(HttpStatus.SC_OK);

        List<Map<String, Object>> projectsAfter = when().
                get(ALL_PROJECTS_PATH).
                then().
                contentType(ContentType.JSON).
                statusCode(HttpStatus.SC_OK).
                extract().
                body().
                jsonPath().
                getList(PROJECTS);

        Assertions.assertTrue(
                projectsAfter.size() == projectsBefore.size() -1 &&
                        projectsAfter.stream().noneMatch(
                                object -> object.get(ID).equals(String.valueOf(id))
                        )
        );
    }

    @Test
    public void deleteSpecificTwiceRejects() {
        postCreatesWithFullBody();

        List<Map<String, Object>> projectsBefore = when().
                get(ALL_PROJECTS_PATH).
                then().
                contentType(ContentType.JSON).
                statusCode(HttpStatus.SC_OK).
                extract().
                body().
                jsonPath().
                getList(PROJECTS);

        given().
                pathParam(ID, id).
                when().
                delete(SPECIFIC_PROJECT_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK);

        given().
                pathParam(ID, id).
                when().
                delete(SPECIFIC_PROJECT_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void deleteNotFoundRejects() {
        given().
                pathParam(ID, 10001).
                when().
                delete(SPECIFIC_PROJECT_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void deleteNonIntegerIDRejects() {
        given().
                pathParam(ID, "asefsadfa").
                when().
                delete(SPECIFIC_PROJECT_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void patchSpecificNotAllowed(){
        postCreatesWithFullBody();

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, OTHER_TEST_TITLE);
        givenBody.put(DESCRIPTION, OTHER_TEST_DESCRIPTION);
        givenBody.put(ACTIVE, false);
        givenBody.put(COMPLETED, true);

        given().
                pathParam(ID, id).
                body(givenBody).
                when().
                patch(SPECIFIC_PROJECT_PATH).
                then().
                statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED);
    }

    @Test
    public void patchSpecificNotAllowedXML() throws ParserConfigurationException, TransformerException, TransformerConfigurationException {
        postCreatesWithFullBody();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();

        Document body = documentBuilder.newDocument();
        Element root = body.createElement(PROJECT);
        body.appendChild(root);

        Element title = body.createElement(TITLE);
        title.appendChild(body.createTextNode(TEST_TITLE));
        root.appendChild(title);


        Element description = body.createElement(DESCRIPTION);
        description.appendChild(body.createTextNode(TEST_DESCRIPTION));
        root.appendChild(description);

        Element active = body.createElement(ACTIVE);
        active.appendChild(body.createTextNode(TRUE));
        root.appendChild(active);

        Element completed = body.createElement(COMPLETED);
        completed.appendChild(body.createTextNode(FALSE));
        root.appendChild(completed);

        Transformer transformer = (TransformerFactory.newInstance()).newTransformer();
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(body), new StreamResult(writer));

        String xmlBody = writer.getBuffer().toString();

        given().
                pathParam(ID, id).
                body(xmlBody).
                contentType(ContentType.XML).
                accept(ContentType.XML).
                when().
                patch(SPECIFIC_PROJECT_PATH).
                then().
                statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED);
    }
}
