## Objective

Create a standalone java application which allows users to manage their favourite recipes. It should
allow adding, updating, removing and fetching recipes. Additionally users should be able to filter
available recipes based on one or more of the following criteria:
1. Whether or not the dish is vegetarian
2. The number of servings
3. Specific ingredients (either include or exclude)
4. Text search within the instructions.


For example, the API should be able to handle the following search requests:
1. All vegetarian recipes
2. Recipes that can serve 4 persons and have “potatoes” as an ingredient
3. Recipes without “salmon” as an ingredient that has “oven” in the instructions.

## Requirements
Please ensure that we have some documentation about the architectural choices and also how to
run the application. The project is expected to be delivered as a GitHub (or any other public git
hosting) repository URL.

All these requirements needs to be satisfied:

1. It must be a REST application implemented using Java (use a framework of your choice)
2. Your code should be production-ready.
3. REST API must be documented
4. Data must be persisted in a database
5. Unit tests must be present
6. Integration tests must be present

-----------------------------------------

## Setup guide

#### Minimum Requirements

- Java 17
- Maven 3.x

Make sure you have java 17 and [Maven](https://maven.apache.org) installed

#### Building and running the application

Open the command line in the source code folder

Build project

  ```
  $ mvn package
  ```
Run the tests
  ```
  $ mvn test
  ```

Go to target folder run the project

  ```
  $ java -jar recipe-0.0.1-SNAPSHOT.jar
  ```

Open the swagger-ui with the link below

```text
http://localhost:8080/swagger-ui.html#/
```
Note: Alternatively, it can be build and run within IDE like intellij

-----------------------------------------
## My solution
I've tried to make it as much production ready as I could. 

I've added custom error handling mechanisms

I've decided to use simple relational db called H2 and made the relations between ingredients and recipes as many-to-many relationship, since many ingredients might be used in many recipes.

Finally, I've tried to cover as much as test cases I could, I might have added more, but I needed to finish the task for today 

## Completeness
Solution is complete in itself

All the requirement given in assignment are satisfied

### Example Request for search
{
"veg": true,
"serving": 2,
"instruction":"electric",
"ingredientFilter": {
"ingredients": [
"Potato"
],
"filter": "INCLUDE"
}
}

## Scope of improvements
Pagination and sorting can be implemented

For too many fields to query from, a generic query builder can be created to cover any/all fields

Solution can be containerized with other databases like mysql, postgres, Oracle etc

### Help
Solution should build and run as expected. In case of any difficulties please reach out to me at avindiit@gmail.com