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


### POST /categories

```
curl --location --request POST 'localhost:4567/categories' \
--data-raw '{
    "title": "Clothes",
    "description": "fdsfsdafsadfas"
}'
```

```json
{
  "id": "4",
  "title": "Clothes",
  "description": "fdsfsdafsadfas"
}
```

```
curl --location --request POST 'localhost:4567/categories' \
--data-raw '{
    "title": "Clothes"
}'
```

```json
{
  "id": "5",
  "title": "Clothes",
  "description": ""
}

```

```
curl --location --request POST 'localhost:4567/categories' \
--data-raw '{
    "title": 2
}'
```

```json
{
  "id": "8",
  "title": "2.0",
  "description": ""
}
```

```
curl --location --request POST 'localhost:4567/categories' \
--data-raw '{
    "description": "sdfasdfsadf"
}'
```

```json
{
  "errorMessages": [
    "title : field is mandatory"
  ]
}
```

```
curl --location --request POST 'localhost:4567/categories' \
--data-raw '{
    "title": 2
}'
```

```json
{
  "id": "8",
  "title": "2.0",
  "description": ""
}
```

### GET /categories
```
curl --location --request GET 'localhost:4567/categories'
```

```json
{
  "categories": [
    {
      "id": "2",
      "title": "Home",
      "description": ""
    },
    {
      "id": "1",
      "title": "Office",
      "description": ""
    },
    {
      "id": "3",
      "title": "blahblah",
      "description": ""
    }
  ]
}
```


### DEL /categories

```
curl --location --request DELETE 'localhost:4567/categories'
```

```
method not allowed
```

### PUT /categories
```
curl --location --request PUT 'localhost:4567/categories' \
--data-raw '{
    "title": "adfasfasdf",
    "description": "asdfsafaff"
}'
```
```
method not allowed
```

### POST /categories/:id
```
curl --location --request POST 'localhost:4567/categories/2' \
--data-raw '{
    "title": "blah",
    "description" : "blah blah"
}'
```

```json
{
  "id": "2",
  "title": "blah",
  "description": "blah blah"
}
```

```
curl --location --request POST 'localhost:4567/categories/2' \
--data-raw '{
    "title": "bfdsafaslah"
}'
```
```json
{
  "id": "2",
  "title": "bfdsafaslah",
  "description": "blah blah"
}
```

```
curl --location --request POST 'localhost:4567/categories/2' \
--data-raw '{
    "description": "fdsfsadfa"
}'
```

```json
{
  "id": "2",
  "title": "bfdsafaslah",
  "description": "fdsfsadfa"
}
```

```
curl --location --request POST 'localhost:4567/categories/1001' \
--data-raw '{
    "title": "fdfasf",
    "description": "fdsafdsafaf"
}'
```

```json
{
  "errorMessages": [
    "No such category entity instance with GUID or ID 1001 found"
  ]
}
```

```
curl --location --request POST 'localhost:4567/categories/sdfsafasf' \
--data-raw '{
    "title": "fdfasf",
    "description": "fdsafdsafaf"
}'
```
```json
{
  "errorMessages": [
    "No such category entity instance with GUID or ID sdfsafasf found"
  ]
}
```

### GET /categories/:id
```
curl --location --request GET 'localhost:4567/categories/2'
```

```json
{
  "categories": [
    {
      "id": "2",
      "title": "Home",
      "description": ""
    }
  ]
}
```

```
curl --location --request GET 'localhost:4567/categories/1000'
```
```json
{
  "errorMessages": [
    "Could not find an instance with categories/1000"
  ]
}
```
```
curl --location --request GET 'localhost:4567/categories/adsfsfaf'
```
```json
{
  "errorMessages": [
    "Could not find an instance with categories/adsfsfaf"
  ]
}
```
### DEL /categories/:id

```
curl --location --request DELETE 'localhost:4567/categories/3'
```

```
No response body
```

```
curl --location --request DELETE 'localhost:4567/categories/10001'
```

```json
{
  "errorMessages": [
    "Could not find any instances with categories/10001"
  ]
}
```

```
curl --location --request DELETE 'localhost:4567/categories/asdfsfa'
```
```json
{
  "errorMessages": [
    "Could not find any instances with categories/asdfsfa"
  ]
}
```

### PUT /categories/:id
```
curl --location --request PUT 'localhost:4567/categories/3' \
--data-raw '{
    "title": "Title",
    "description": "adfasdfasfdas"
}'
```

```json
{
  "id": "3",
  "title": "Title",
  "description": "adfasdfasfdas"
}
```

```
curl --location --request PUT 'localhost:4567/categories/3' \
--data-raw '{
    "description": "adfasdfasfdas"
}'
```

```json
{
  "errorMessages": [
    "title : field is mandatory"
  ]
}
```

```
curl --location --request PUT 'localhost:4567/categories/3' \
--data-raw '{
    "title": "",
    "description": "adfasdfasfdas"
}'
```

```json
{
  "errorMessages": [
    "Failed Validation: title : can not be empty"
  ]
}
```

```
curl --location --request PUT 'localhost:4567/categories/3' \
--data-raw '{
    "title": "asdfasfasf"
}'
```

```json
{
  "id": "3",
  "title": "asdfasfasf",
  "description": ""
}
```

```
curl --location --request PUT 'localhost:4567/categories/3' \
--data-raw '{
    "title": "asdfasfasf",
    "description": ""
}'
```

```json
{
  "id": "3",
  "title": "asdfasfasf",
  "description": ""
}
```

