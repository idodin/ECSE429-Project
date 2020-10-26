## Session 2 Notes

### GET /projects

```
curl --location --request GET 'localhost:4567/projects'
```

```json
{
    "projects": [
        {
            "id": "172",
            "title": "Some project title",
            "completed": "false",
            "active": "false",
            "description": "Some project description"
        }
    ]
}
```

Notes:
- Tester tried to to request all projects from endpoint 
- Observed that endpoint returns all projects in an array

Test Ideas:
- Test getting projects when no projects exist
- Test getting projects with both XML and JSON format
- Test getting projects when several projects exist
- Test limits of how many projects can be returned

Areas of potential risk:
- Projects not correctly returning when no projects exist
- Projects not correctly returning when too many projects exist

Capabilites discovered:
- Able to get all projects in both XML and JSON format


### POST /projects

Notes:
- Tester decided to input integer, string and boolean titles
- Observed that integer and boolean values for title are automatically converted into a string
- Tester decided to input integer, string and boolean descriptions
- Observed that integer and boolean values for description are automatically converted into a string
- Tester decided to try omitting active / completed boolean fields
- Observed that active / completed boolean fields default to false when omitted

Test Ideas:
- Test creating project with integer, string and boolean titles
- Test creating projects in both XML and JSON formt 
- Test creating proejcts with empty / whitespace titles
- Test creating projects with malformed JSON / XML

