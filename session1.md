## Session 1 Notes

### Testers Name:
#### Farouk Arab 260806502
#### Ismail Ziouti 260807219


## Charter
- Identify capabilities and areas of potential instability of the “rest
api todo list manager”.

- Identify documented and undocumented “rest api todo list manager”
capabilities.

- For each capability create a script or small program to demonstrate
the capability.

- Exercise each capability identified with data typical to the intended
use of the application.



### GET /projects

```
curl --location --request GET 'localhost:4567/todos'
```
```json
{
  "id": "8",
  "title": "occaecat cupidatataa",
  "doneStatus": "true",
  "description": "qua. Ut enim ad mini"
}
```

Notes:
- Tester tried to request all todos from endpoint
- Observed that endpoint returns all todos in an array

Test Ideas:
- Test getting todos with both XML and JSON format
- Test getting todos when several todos exist

Areas of potential risk:
- Todos not correctly returning when no todos exist
- Todos not correctly returning when too many projects exist

Capabilities:
- Able to return all todos in both XML and JSON format
- Able to return empty array when no todos exist

### POST/todos

Using JSON format:
```
curl --location --request POST 'localhost:4567/todos' \
--data-raw '{
  "title": "create a todo",
  "doneStatus": true,
  "description": "new todo created"
}'
```
```json
{
  "id": "247",
  "title": "create a todo",
  "doneStatus": "true",
  "description": "new todo created"
}
```

Using XML format:

```
curl --location --request POST 'localhost:4567/todos' \
--data-raw '<todo>
  <doneStatus>true</doneStatus>
  <description>created a new todo with XML</description>
  <title>new todo xml</title>
</todo>'

```
```xml
{
  "id": "248",
  "title": "new todo xml",
  "doneStatus": "true",
  "description": "created a new todo with XML"
}
```
Notes:
- Teser decided to use different input format in the title as integer, String and boolean
- Observed boolean and integer titles are successfuly converted to string
- Tester decided to use white space and empty string as a title
- Observed an Error message specifies that the title can't be empty
- Tester decided to put different input format in the description as integer, string and boolean
- Observed boolean and integer description are converted to string

Areas of potential risks:
- length of the title and the description

Test Ideas:
- Test creating todos with integer, string and boolean titles
- Test creating todos in both XML and JSON format 
- Test creating todos with empty / whitespace titles

Capabilities: 
- Able to create a todo with full body
- Able to generate an ID for each todo created
- Able to create a todo with only a title as input

### DEL/todos

```
curl --location --request DELETE 'localhost:4567/todos'
```
```
curl --location --request DELETE 'localhost:4567/todos/8'
```

```json
Method not Allowed
```
Notes:
- Tester tried to delete all todos instances
- Observed request not allowed
- Tester tried to delete a specific todo using its id
- Observed that todo was successfully removed

Areas of potential risks:
- delete a todo using a non existent ID

Test Ideas:
- Test to delete all todos
- Test to delete a specific todo

### PUT/todos/:id

```
curl --location --request PUT 'localhost:4567/todos/1' \
--data-raw '{
    
    "title":"hello world!"
}'

```

```json
{
  "id": "1",
  "title": "hello world!",
  "doneStatus": "false",
  "description": ""
}
```

Notes:

- Tester decided to update the full body of a specific todo 
- Observed all the fields were successfuly updated
- Tester decided to update a specific todo's title with an integer and a boolean input 
- Observed the input is converted to string, thus the title was successfly updated 
- Tester decided to update title and doneStatus
- Observed the title and the doneStatus were succefully updated BUT the description has been erased
- Tester decided to only update the title and description
- Observed that doneStatus becomes false even though it was originally true 

Areas of potential risks:
- Updating one field and not updating the other fields

Test Ideas:
- Test updating todos with full body input
- Test updating todo's title with an integer 
- Test updating todo's title with a boolean
- Test updating todo's title and doneStatus without updating description
- Test updating todo's title and description without doneStatus

Capabilities: 
- Able to update a todo with full body
- Able to update a title, a description and doneStatus
- Able to create a todo with only a title as input








