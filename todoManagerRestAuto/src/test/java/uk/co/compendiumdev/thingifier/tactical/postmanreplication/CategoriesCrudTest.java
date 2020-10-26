package uk.co.compendiumdev.thingifier.tactical.postmanreplication;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

public class CategoriesCrudTest {
    // Projects Paths
    private static final String ALL_CATEGORIES_PATH = "/categories";
    private static final String SPECIFIC_CATEGORIES_PATH = "/categories/{id}";
    private static final String CLEAR_PATH = "/admin/data/thingifier";
    private static final String CATEGORIES = "categories";
    private static final String CATEGORY = "category";

    // Project Fields
    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";

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


    @BeforeEach
    public void clearDataFromEnv(){

        RestAssured.baseURI = Environment.getBaseUri();

        // Make post request to Clear Data Path
        post(CLEAR_PATH);

        // Gets all categories
        final JsonPath clearedData =
                when().
                        get(ALL_CATEGORIES_PATH).
                then().
                        statusCode(200).
                        extract().body().jsonPath();

        // Gets number of categories after clearing
        final int newNumberOfCategories = clearedData.getList(CATEGORIES).size();

        // Make sure that number of categories after clearing is 0
        Assumptions.assumeTrue(newNumberOfCategories == 0);
    }

    @Test
    public int postCreatesWithFullBody(){
        // Map contains key value pairs for JSON
        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, TEST_TITLE);
        givenBody.put(DESCRIPTION, TEST_DESCRIPTION);

        String id =
                // Sets up request
                given().
                        body(givenBody).
                // Executes request
                when().
                        post(ALL_CATEGORIES_PATH).
                // Checks output of the request
                then().
                        // CHECKS CONTENT TYPE
                        contentType(ContentType.JSON).
                        // CHECKS STATUS CODE
                        statusCode(HttpStatus.SC_CREATED).
                        // CHECKS BODY RESPONSE
                        body(
                            TITLE, equalTo(TEST_TITLE),
                            DESCRIPTION, equalTo(TEST_DESCRIPTION)
                        ).
                        extract().
                        path(ID);

        return Integer.parseInt(id);
    }

    @Test
    public void postCreatesWithXML() throws ParserConfigurationException, TransformerException, TransformerConfigurationException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();

        Document body = documentBuilder.newDocument();
        Element root = body.createElement(CATEGORY);
        body.appendChild(root);

        Element title = body.createElement(TITLE);
        title.appendChild(body.createTextNode(TEST_TITLE));
        root.appendChild(title);


        Element description = body.createElement(DESCRIPTION);
        description.appendChild(body.createTextNode(TEST_DESCRIPTION));
        root.appendChild(description);

        Transformer transformer = (TransformerFactory.newInstance()).newTransformer();
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(body), new StreamResult(writer));

        String xmlBody = writer.getBuffer().toString();

        given().
                body(xmlBody).
                // I GIVE XML
                contentType(ContentType.XML).
                // I EXPECT XML BACK
                accept(ContentType.XML).
        when().
                post(ALL_CATEGORIES_PATH).
        then().
                contentType(ContentType.XML).
                statusCode(HttpStatus.SC_CREATED).
                body(
                        CATEGORY+"."+TITLE, equalTo(TEST_TITLE),
                        CATEGORY+"."+DESCRIPTION, equalTo(TEST_DESCRIPTION)
                );
    }

    @Test
    public void postCreatesWithoutDescription(){

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, TEST_TITLE);

        given().
                body(givenBody).
                when().
                post(ALL_CATEGORIES_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_CREATED)
                .body(
                        TITLE, equalTo(TEST_TITLE),
                        DESCRIPTION, equalTo(EMPTY_STRING)
                );
    }

    @Test
    public void postCreatesWithIntegerTitleConverted(){

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, SOME_INTEGER);
        givenBody.put(DESCRIPTION, TEST_DESCRIPTION);

        given().
                body(givenBody).
                when().
                post(ALL_CATEGORIES_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_CREATED)
                .body(
                        TITLE, equalTo(Double.toString(SOME_INTEGER)),
                        DESCRIPTION, equalTo(TEST_DESCRIPTION)
                );
    }

    @Test
    public void postCreatesWithBooleanTitleConverted(){

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, SOME_BOOLEAN);
        givenBody.put(DESCRIPTION, TEST_DESCRIPTION);

        given().
                body(givenBody).
                when().
                post(ALL_CATEGORIES_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_CREATED)
                .body(
                        TITLE, equalTo(Boolean.toString(SOME_BOOLEAN)),
                        DESCRIPTION, equalTo(TEST_DESCRIPTION)
                );
    }

    @Test
    public void postCreatesWithIntegerDescriptionConverted(){

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, TEST_TITLE);
        givenBody.put(DESCRIPTION, SOME_INTEGER);

        given().
                body(givenBody).
                when().
                post(ALL_CATEGORIES_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_CREATED)
                .body(
                        TITLE, equalTo(TEST_TITLE),
                        DESCRIPTION, equalTo(Double.toString(SOME_INTEGER))
                );
    }

    @Test
    public void postCreatesWithBooleanDescriptionConverted(){

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, TEST_TITLE);
        givenBody.put(DESCRIPTION, SOME_BOOLEAN);

        given().
                body(givenBody).
                when().
                post(ALL_CATEGORIES_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_CREATED)
                .body(
                        TITLE, equalTo(TEST_TITLE),
                        DESCRIPTION, equalTo(Boolean.toString(SOME_BOOLEAN))
                );
    }

    @Test
    public void postWithMissingTitleDoesReject(){

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(DESCRIPTION, TEST_DESCRIPTION);

        given().
                body(givenBody).
                when().
                post(ALL_CATEGORIES_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(equalTo(HttpStatus.SC_BAD_REQUEST));
    }

    @Test
    public void postWithEmptyTitleDoesReject(){

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, EMPTY_STRING);
        givenBody.put(DESCRIPTION, TEST_DESCRIPTION);

        given().
                body(givenBody).
                when().
                post(ALL_CATEGORIES_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(equalTo(HttpStatus.SC_BAD_REQUEST));
    }

    @Test
    public void postWithWhitespaceTitleDoesReject(){

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, WHITESPACE_STRING);
        givenBody.put(DESCRIPTION, TEST_DESCRIPTION);

        given().
                body(givenBody).
                when().
                post(ALL_CATEGORIES_PATH).
                then().
                contentType(ContentType.JSON).
                statusCode(equalTo(HttpStatus.SC_BAD_REQUEST));
    }

    @Test
    public void getCategoriesReturnsAllCategories(){
        // Create Three Random projects
        postCreatesWithFullBody();
        postCreatesWithBooleanTitleConverted();
        postCreatesWithIntegerTitleConverted();

        List<Map<String, Object>> projects = when().
                get(ALL_CATEGORIES_PATH).
                then().
                contentType(ContentType.JSON).
                statusCode(HttpStatus.SC_OK).
                extract().
                body().
                jsonPath().
                getList(CATEGORIES);

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
    public void putCategoriesNotAllowed(){

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, TEST_TITLE);
        givenBody.put(DESCRIPTION, TEST_DESCRIPTION);

        given().
                body(givenBody).
                when().
                put(ALL_CATEGORIES_PATH)
                .then()
                .statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED);
    }

    @Test
    public void putCategoriesNotAllowedXML() throws ParserConfigurationException, TransformerException, TransformerConfigurationException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();

        Document body = documentBuilder.newDocument();
        Element root = body.createElement(CATEGORY);
        body.appendChild(root);

        Element title = body.createElement(TITLE);
        title.appendChild(body.createTextNode(TEST_TITLE));
        root.appendChild(title);


        Element description = body.createElement(DESCRIPTION);
        description.appendChild(body.createTextNode(TEST_DESCRIPTION));
        root.appendChild(description);


        Transformer transformer = (TransformerFactory.newInstance()).newTransformer();
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(body), new StreamResult(writer));

        String xmlBody = writer.getBuffer().toString();

        given().
                body(xmlBody).
                contentType(ContentType.XML).
                accept(ContentType.XML).
                when().
                put(ALL_CATEGORIES_PATH).
                then().
                statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED);
    }

    @Test
    public void patchCategoriesNotAllowed(){

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, TEST_TITLE);
        givenBody.put(DESCRIPTION, TEST_DESCRIPTION);

        given().
                body(givenBody).
                when().
                patch(ALL_CATEGORIES_PATH)
                .then()
                .statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED);
    }

    @Test
    public void patchCategoriesNotAllowedXML() throws ParserConfigurationException, TransformerException, TransformerConfigurationException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();

        Document body = documentBuilder.newDocument();
        Element root = body.createElement(CATEGORY);
        body.appendChild(root);

        Element title = body.createElement(TITLE);
        title.appendChild(body.createTextNode(TEST_TITLE));
        root.appendChild(title);


        Element description = body.createElement(DESCRIPTION);
        description.appendChild(body.createTextNode(TEST_DESCRIPTION));
        root.appendChild(description);


        Transformer transformer = (TransformerFactory.newInstance()).newTransformer();
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(body), new StreamResult(writer));

        String xmlBody = writer.getBuffer().toString();

        given().
                body(xmlBody).
                contentType(ContentType.XML).
                accept(ContentType.XML).
                when().
                patch(ALL_CATEGORIES_PATH).
                then().
                statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED);
    }

    @Test
    public void deleteCategoriesNotAllowed(){
        given().
                contentType(ContentType.JSON).
                accept(ContentType.JSON).
                when().
                delete(ALL_CATEGORIES_PATH).
                then().
                statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED);
    }

    @Test
    public void deleteCategoriesNotAllowedXML(){
        given().
                contentType(ContentType.XML).
                accept(ContentType.XML).
                when().
                delete(ALL_CATEGORIES_PATH).
                then().
                statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED);
    }

    @Test
    public void getSpecificCategoryReturnsBody() {
        int id = postCreatesWithFullBody();

        Map<String, Object> project = (Map<String, Object>)given().
                pathParam(ID, id).
                when().
                get(SPECIFIC_CATEGORIES_PATH).
                then().
                statusCode(HttpStatus.SC_OK).
                contentType(ContentType.JSON).
                extract().
                body().
                jsonPath().
                getList(CATEGORIES).
                get(0);

        Assertions.assertTrue(
                project.get(ID).equals(String.valueOf(id)) &&
                        project.get(TITLE).equals(TEST_TITLE) &&
                        project.get(DESCRIPTION).equals(TEST_DESCRIPTION)
        );

    }

    @Test
    public void getSpecificCategoryReturnsXMLBody() {
        int id = postCreatesWithFullBody();

        given().
                pathParam(ID, id).
                accept(ContentType.XML).
                when().
                get(SPECIFIC_CATEGORIES_PATH).
                then().
                statusCode(HttpStatus.SC_OK).
                contentType(ContentType.XML).
                body(
                        CATEGORIES+"."+CATEGORY+"."+TITLE, equalTo(TEST_TITLE),
                        CATEGORIES+"."+CATEGORY+"."+DESCRIPTION, equalTo(TEST_DESCRIPTION)
                );

    }

    @Test
    public void getSpecificCategoryReturnsNotFound() {

        given().
                pathParam(ID, 10001).
                when().
                get(SPECIFIC_CATEGORIES_PATH).
                then().
                statusCode(HttpStatus.SC_NOT_FOUND).
                contentType(ContentType.JSON);

    }

    @Test
    public void getSpecificCategoryHandlesNonIntegerID() {

        given().
                pathParam(ID, "fdsafa").
                when().
                get(SPECIFIC_CATEGORIES_PATH).
                then().
                statusCode(HttpStatus.SC_NOT_FOUND).
                contentType(ContentType.JSON);

    }

    @Test
    public void putSpecificUpdatesWithFullBody(){
        int id = postCreatesWithFullBody();

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, OTHER_TEST_TITLE);
        givenBody.put(DESCRIPTION, OTHER_TEST_DESCRIPTION);

        given().
                pathParam(ID, id).
                body(givenBody).
                when().
                put(SPECIFIC_CATEGORIES_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK)
                .body(
                        ID, equalTo(String.valueOf(id)),
                        TITLE, equalTo(OTHER_TEST_TITLE),
                        DESCRIPTION, equalTo(OTHER_TEST_DESCRIPTION)
                );
    }

    @Test
    public void putSpecificCreatesWithXML() throws ParserConfigurationException, TransformerException, TransformerConfigurationException {
        int id = postCreatesWithFullBody();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();

        Document body = documentBuilder.newDocument();
        Element root = body.createElement(CATEGORY);
        body.appendChild(root);

        Element title = body.createElement(TITLE);
        title.appendChild(body.createTextNode(TEST_TITLE));
        root.appendChild(title);


        Element description = body.createElement(DESCRIPTION);
        description.appendChild(body.createTextNode(TEST_DESCRIPTION));
        root.appendChild(description);


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
                put(SPECIFIC_CATEGORIES_PATH).
                then().
                contentType(ContentType.XML).
                statusCode(HttpStatus.SC_OK).
                body(
                        CATEGORY+"."+TITLE, equalTo(TEST_TITLE),
                        CATEGORY+"."+DESCRIPTION, equalTo(TEST_DESCRIPTION)
                );
    }

    @Test
    public void putSpecificUpdatesWithIntegerTitleConverted(){
        int id = postCreatesWithFullBody();

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, SOME_INTEGER);
        givenBody.put(DESCRIPTION, OTHER_TEST_DESCRIPTION);

        given().
                pathParam(ID, id).
                body(givenBody).
                when().
                put(SPECIFIC_CATEGORIES_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK)
                .body(
                        ID, equalTo(String.valueOf(id)),
                        TITLE, equalTo(Double.toString(SOME_INTEGER)),
                        DESCRIPTION, equalTo(OTHER_TEST_DESCRIPTION)
                );
    }

    @Test
    public void putSpecificUpdatesWithBooleanTitleConverted(){
        int id = postCreatesWithFullBody();

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, SOME_BOOLEAN);
        givenBody.put(DESCRIPTION, OTHER_TEST_DESCRIPTION);

        given().
                pathParam(ID, id).
                body(givenBody).
                when().
                put(SPECIFIC_CATEGORIES_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK)
                .body(
                        ID, equalTo(String.valueOf(id)),
                        TITLE, equalTo(Boolean.toString(SOME_BOOLEAN)),
                        DESCRIPTION, equalTo(OTHER_TEST_DESCRIPTION)
                );
    }

    @Test
    public void putSpecificUpdatesWithIntegerDescriptionConverted(){
        int id = postCreatesWithFullBody();

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, TEST_TITLE);
        givenBody.put(DESCRIPTION, SOME_INTEGER);

        given().
                pathParam(ID, id).
                body(givenBody).
                when().
                put(SPECIFIC_CATEGORIES_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK)
                .body(
                        ID, equalTo(String.valueOf(id)),
                        TITLE, equalTo(TEST_TITLE),
                        DESCRIPTION, equalTo(Double.toString(SOME_INTEGER))
                );
    }

    @Test
    public void putSpecificUpdatesWithBooleanDescriptionConverted(){
        int id = postCreatesWithFullBody();

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, TEST_TITLE);
        givenBody.put(DESCRIPTION, SOME_BOOLEAN);

        given().
                pathParam(ID, id).
                body(givenBody).
                when().
                put(SPECIFIC_CATEGORIES_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK)
                .body(
                        ID, equalTo(String.valueOf(id)),
                        TITLE, equalTo(TEST_TITLE),
                        DESCRIPTION, equalTo(Boolean.toString(SOME_BOOLEAN))
                );
    }

    @Test
    public void putSpecificUpdatesWithoutDescription(){
        int id = postCreatesWithFullBody();

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, OTHER_TEST_TITLE);

        given().
                pathParam(ID, id).
                body(givenBody).
                when().
                put(SPECIFIC_CATEGORIES_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK)
                .body(
                        ID, equalTo(String.valueOf(id)),
                        TITLE, equalTo(OTHER_TEST_TITLE),
                        DESCRIPTION, equalTo(EMPTY_STRING)
                );
    }

    @Test
    public void putSpecificUpdatesWithoutActive(){
        int id = postCreatesWithFullBody();

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, OTHER_TEST_TITLE);
        givenBody.put(DESCRIPTION, OTHER_TEST_DESCRIPTION);

        given().
                pathParam(ID, id).
                body(givenBody).
                when().
                put(SPECIFIC_CATEGORIES_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK)
                .body(
                        ID, equalTo(String.valueOf(id)),
                        TITLE, equalTo(OTHER_TEST_TITLE),
                        DESCRIPTION, equalTo(OTHER_TEST_DESCRIPTION)
                );
    }

    @Test
    public void putSpecificUpdatesWithoutCompleted(){
        int id = postCreatesWithFullBody();

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, OTHER_TEST_TITLE);
        givenBody.put(DESCRIPTION, OTHER_TEST_DESCRIPTION);

        given().
                pathParam(ID, id).
                body(givenBody).
                when().
                put(SPECIFIC_CATEGORIES_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK)
                .body(
                        ID, equalTo(String.valueOf(id)),
                        TITLE, equalTo(OTHER_TEST_TITLE),
                        DESCRIPTION, equalTo(OTHER_TEST_DESCRIPTION)
                );
    }

    @Test
    public void putSpecificWithEmptyTitleDoesReject(){
        int id = postCreatesWithFullBody();

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(DESCRIPTION, OTHER_TEST_DESCRIPTION);

        given().
                pathParam(ID, id).
                body(givenBody).
                when().
                put(SPECIFIC_CATEGORIES_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(equalTo(HttpStatus.SC_BAD_REQUEST));
    }

    @Test
    public void putSpecificWithWhitespaceTitleDoesReject(){
        int id = postCreatesWithFullBody();

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, WHITESPACE_STRING);
        givenBody.put(DESCRIPTION, OTHER_TEST_DESCRIPTION);

        given().
                pathParam(ID, id).
                body(givenBody).
                when().
                put(SPECIFIC_CATEGORIES_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(equalTo(HttpStatus.SC_BAD_REQUEST));
    }

    @Test
    public void postSpecificUpdatesWithFullBody(){
        int id = postCreatesWithFullBody();

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, OTHER_TEST_TITLE);
        givenBody.put(DESCRIPTION, OTHER_TEST_DESCRIPTION);

        given().
                pathParam(ID, id).
                body(givenBody).
                when().
                post(SPECIFIC_CATEGORIES_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK)
                .body(
                        ID, equalTo(String.valueOf(id)),
                        TITLE, equalTo(OTHER_TEST_TITLE),
                        DESCRIPTION, equalTo(OTHER_TEST_DESCRIPTION)
                );
    }

    @Test
    public void postSpecificCreatesWithXML() throws ParserConfigurationException, TransformerException, TransformerConfigurationException {
        int id = postCreatesWithFullBody();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();

        Document body = documentBuilder.newDocument();
        Element root = body.createElement(CATEGORY);
        body.appendChild(root);

        Element title = body.createElement(TITLE);
        title.appendChild(body.createTextNode(TEST_TITLE));
        root.appendChild(title);


        Element description = body.createElement(DESCRIPTION);
        description.appendChild(body.createTextNode(TEST_DESCRIPTION));
        root.appendChild(description);


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
                post(SPECIFIC_CATEGORIES_PATH).
                then().
                contentType(ContentType.XML).
                statusCode(HttpStatus.SC_OK).
                body(
                        CATEGORY+"."+TITLE, equalTo(TEST_TITLE),
                        CATEGORY+"."+DESCRIPTION, equalTo(TEST_DESCRIPTION)
                );
    }

    @Test
    public void postSpecificUpdatesWithIntegerTitleConverted(){
        int id = postCreatesWithFullBody();

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, SOME_INTEGER);
        givenBody.put(DESCRIPTION, OTHER_TEST_DESCRIPTION);

        given().
                pathParam(ID, id).
                body(givenBody).
                when().
                post(SPECIFIC_CATEGORIES_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK)
                .body(
                        ID, equalTo(String.valueOf(id)),
                        TITLE, equalTo(Double.toString(SOME_INTEGER)),
                        DESCRIPTION, equalTo(OTHER_TEST_DESCRIPTION)
                );
    }

    @Test
    public void postSpecificUpdatesWithBooleanTitleConverted(){
        int id = postCreatesWithFullBody();

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, SOME_BOOLEAN);
        givenBody.put(DESCRIPTION, OTHER_TEST_DESCRIPTION);

        given().
                pathParam(ID, id).
                body(givenBody).
                when().
                post(SPECIFIC_CATEGORIES_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK)
                .body(
                        ID, equalTo(String.valueOf(id)),
                        TITLE, equalTo(Boolean.toString(SOME_BOOLEAN)),
                        DESCRIPTION, equalTo(OTHER_TEST_DESCRIPTION)
                );
    }

    @Test
    public void postSpecificUpdatesWithoutTitle(){
        int id = postCreatesWithFullBody();

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(DESCRIPTION, OTHER_TEST_DESCRIPTION);

        given().
                pathParam(ID, id).
                body(givenBody).
                when().
                post(SPECIFIC_CATEGORIES_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK)
                .body(
                        ID, equalTo(String.valueOf(id)),
                        TITLE, equalTo(TEST_TITLE),
                        DESCRIPTION, equalTo(OTHER_TEST_DESCRIPTION)
                );
    }

    @Test
    public void postSpecificUpdatesWithoutDescription(){
        int id = postCreatesWithFullBody();

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, OTHER_TEST_TITLE);

        given().
                pathParam(ID, id).
                body(givenBody).
                when().
                post(SPECIFIC_CATEGORIES_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK)
                .body(
                        ID, equalTo(String.valueOf(id)),
                        TITLE, equalTo(OTHER_TEST_TITLE),
                        DESCRIPTION, equalTo(TEST_DESCRIPTION)
                );
    }

    @Test
    public void postSpecificUpdatesWithIntegerDescriptionConverted(){
        int id = postCreatesWithFullBody();

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, TEST_TITLE);
        givenBody.put(DESCRIPTION, SOME_INTEGER);

        given().
                pathParam(ID, id).
                body(givenBody).
                when().
                post(SPECIFIC_CATEGORIES_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK)
                .body(
                        ID, equalTo(String.valueOf(id)),
                        TITLE, equalTo(TEST_TITLE),
                        DESCRIPTION, equalTo(Double.toString(SOME_INTEGER))
                );
    }

    @Test
    public void postSpecificUpdatesWithBooleanDescriptionConverted(){
        int id = postCreatesWithFullBody();

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, TEST_TITLE);
        givenBody.put(DESCRIPTION, SOME_BOOLEAN);

        given().
                pathParam(ID, id).
                body(givenBody).
                when().
                post(SPECIFIC_CATEGORIES_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK)
                .body(
                        ID, equalTo(String.valueOf(id)),
                        TITLE, equalTo(TEST_TITLE),
                        DESCRIPTION, equalTo(Boolean.toString(SOME_BOOLEAN))
                );
    }

    @Test
    public void postSpecificWithEmptyTitle(){
        int id = postCreatesWithFullBody();

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(DESCRIPTION, OTHER_TEST_DESCRIPTION);

        given().
                pathParam(ID, id).
                body(givenBody).
                when().
                post(SPECIFIC_CATEGORIES_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK)
                .body(
                        ID, equalTo(String.valueOf(id)),
                        TITLE, equalTo(TEST_TITLE),
                        DESCRIPTION, equalTo(OTHER_TEST_DESCRIPTION)
                );
    }

    @Test
    public void postSpecificWithWhitespaceTitleDoesReject(){
        int id = postCreatesWithFullBody();

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, WHITESPACE_STRING);
        givenBody.put(DESCRIPTION, OTHER_TEST_DESCRIPTION);

        given().
                pathParam(ID, id).
                body(givenBody).
                when().
                post(SPECIFIC_CATEGORIES_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(equalTo(HttpStatus.SC_BAD_REQUEST));
    }

    @Test
    public void deleteSpecificDeletesCategory(){
        int id = postCreatesWithFullBody();

        List<Map<String, Object>> projectsBefore = when().
                get(ALL_CATEGORIES_PATH).
                then().
                contentType(ContentType.JSON).
                statusCode(HttpStatus.SC_OK).
                extract().
                body().
                jsonPath().
                getList(CATEGORIES);

        given().
                pathParam(ID, id).
                when().
                delete(SPECIFIC_CATEGORIES_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK);

        List<Map<String, Object>> projectsAfter = when().
                get(ALL_CATEGORIES_PATH).
                then().
                contentType(ContentType.JSON).
                statusCode(HttpStatus.SC_OK).
                extract().
                body().
                jsonPath().
                getList(CATEGORIES);

        Assertions.assertTrue(
                projectsAfter.size() == projectsBefore.size() -1 &&
                        projectsAfter.stream().noneMatch(
                                object -> object.get(ID).equals(String.valueOf(id))
                        )
        );
    }

    @Test
    public void deleteSpecificDeletesCategoryXML(){
        int id = postCreatesWithFullBody();

        List<Map<String, Object>> projectsBefore = when().
                get(ALL_CATEGORIES_PATH).
                then().
                contentType(ContentType.JSON).
                statusCode(HttpStatus.SC_OK).
                extract().
                body().
                jsonPath().
                getList(CATEGORIES);

        given().
                pathParam(ID, id).
                contentType(ContentType.XML).
                accept(ContentType.XML).
                when().
                delete(SPECIFIC_CATEGORIES_PATH)
                .then()
                .contentType(ContentType.XML)
                .statusCode(HttpStatus.SC_OK);

        List<Map<String, Object>> projectsAfter = when().
                get(ALL_CATEGORIES_PATH).
                then().
                contentType(ContentType.JSON).
                statusCode(HttpStatus.SC_OK).
                extract().
                body().
                jsonPath().
                getList(CATEGORIES);

        Assertions.assertTrue(
                projectsAfter.size() == projectsBefore.size() -1 &&
                        projectsAfter.stream().noneMatch(
                                object -> object.get(ID).equals(String.valueOf(id))
                        )
        );
    }

    @Test
    public void deleteSpecificTwiceRejects() {
        int id = postCreatesWithFullBody();

        List<Map<String, Object>> projectsBefore = when().
                get(ALL_CATEGORIES_PATH).
                then().
                contentType(ContentType.JSON).
                statusCode(HttpStatus.SC_OK).
                extract().
                body().
                jsonPath().
                getList(CATEGORIES);

        given().
                pathParam(ID, id).
                when().
                delete(SPECIFIC_CATEGORIES_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK);

        given().
                pathParam(ID, id).
                when().
                delete(SPECIFIC_CATEGORIES_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void deleteNotFoundRejects() {
        given().
                pathParam(ID, 10001).
                when().
                delete(SPECIFIC_CATEGORIES_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void deleteNonIntegerIDRejects() {
        given().
                pathParam(ID, "asefsadfa").
                when().
                delete(SPECIFIC_CATEGORIES_PATH)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void patchSpecificNotAllowed(){
        int id = postCreatesWithFullBody();

        final HashMap<String, Object> givenBody = new HashMap<>();
        givenBody.put(TITLE, OTHER_TEST_TITLE);
        givenBody.put(DESCRIPTION, OTHER_TEST_DESCRIPTION);

        given().
                pathParam(ID, id).
                body(givenBody).
                when().
                patch(SPECIFIC_CATEGORIES_PATH).
                then().
                statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED);
    }

    @Test
    public void patchSpecificNotAllowedXML() throws ParserConfigurationException, TransformerException, TransformerConfigurationException {
        int id = postCreatesWithFullBody();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();

        Document body = documentBuilder.newDocument();
        Element root = body.createElement(CATEGORY);
        body.appendChild(root);

        Element title = body.createElement(TITLE);
        title.appendChild(body.createTextNode(TEST_TITLE));
        root.appendChild(title);


        Element description = body.createElement(DESCRIPTION);
        description.appendChild(body.createTextNode(TEST_DESCRIPTION));
        root.appendChild(description);


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
                patch(SPECIFIC_CATEGORIES_PATH).
                then().
                statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED);
    }

}

