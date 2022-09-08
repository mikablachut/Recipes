# Recipes
* [Description](#description)
* [Technologies](#technologies)
* [Setup](#setup)
* [Endpoints](#endpoints)
    * [Register User Endpoint](#register-user-endpoint)
    * [Add Recipe Endpoint](#add-recipe-endpoint)
    * [Update Recipe Endpoint](#update-recipe-endpoint)
    * [Get Recipe Endpoint](#get-recipe-endpoint)
    * [Search Recipe Endpoint](#search-recipe-endpoint)
    * [Delete Recipe Endpoint](#delete-recipe-endpoint)

## Description

<details>
<summary>Click here to see general information about <b>Project</b>!</summary>
  
  
A multi-user web application that allows storing, retrieving, updating and deleting recipes. Each user can search for recipes by category or name. The serivce          supports registration process and allows users (after logon) to add their own recipes and to update, delete and search this recipes by ID.

The idea for project cames from Java Beckend Developer track in [JetBrains Academy](https://www.jetbrains.com/academy/).
  
</details>

## Technologies

<ul>
  <li>Java 17</li>
  <li>Spring Boot</li>
  <li>Spring Boot Security</li>
  <li>Spring Data</li>
  <li>Hibernate</li>
  <li>H2</li>
  <li>Gradle</li>
  <li>JUnit</li>
  <li>REST API</li>
  <li>Project Lombok</li>
</ul>

## Setup

Make sure you have [git](https://git-scm.com/) installed. The application uses H2 as the database by default. It is required to provide database connection URL and database user conditionals for an application to run.

### Gradle

<b>Java 17 is required for this step.</b>

Clone repository and enter its folder:

```
https://github.com/dominikablachut/Recipes.git
cd Recipes
```





## Endpoints

When you start the application a H2 database containing initial tables will be automaticly created. You can use [Postman](https://www.postman.com) or any similar program for testing existing endpoints.

### Register User Endpoint

Endpoint receives a JSON object with two fields: email (string), and password (string). If a user with a specified email does not exist, the program saves (registers) the user in a database and responds with 200 (Ok). If a user is already in the database, respond with the 400 (Bad Request) status code. Both fields are required and must be valid: email should contain @ and . symbols, password should contain at least 8 characters and shouldn't be blank. If the fields do not meet these restrictions, the service should respond with 400 (Bad Request). 

POST method

```POST /api/register```    request without authentication

Request body:

```json
{
   "email": "Cook_Programmer@somewhere.com",
   "password": "RecipeInBinary"
}
```
<details>
<summary><b>Sample response</b></summary>
  Stasus code: 200 (Ok)
</details>


### Add Recipe Endpoint

Endpoint receives a recipe as a JSON object and returns a JSON object with one id field. This is a uniquely generated number by which we can identify and retrieve a recipe later. The application accept only valid recipes – all fields are obligatory, name and description shouldn't be blank, and JSON arrays should contain at least one item. If a recipe doesn't meet these requirements, the service should respond with the 400 (Bad Request) status code. Only a register user can add a recipe. If a user is not register, but he try to carry out the action mentioned above, the service should respond with the 401 (Unauthorized) status code.

POST method

```POST /api/recipe/new```   request without authentication

Request body:

```json 
{
   "name": "Fresh Mint Tea",
   "category": "beverage",
   "description": "Light, aromatic and refreshing beverage, ...",
   "ingredients": ["boiled water", "honey", "fresh mint leaves"],
   "directions": ["Boil water", "Pour boiling hot water into a mug", "Add fresh mint leaves", "Mix and let the mint leaves seep for 3-5 minutes", "Add honey and mix again"]
}
```
<details>
<summary><b>Sample response</b></summary>
  Stasus code: 401 (Unauthorized)
</details>

```POST /api/recipe/new``` request with basic authentication; email (login): Cook_Programmer@somewhere.com, and password: RecipeInBinary.

Request body:

```json
{
   "name": "Mint Tea",
   "category": "beverage",
   "description": "Light, aromatic and refreshing beverage, ...",
   "ingredients": ["boiled water", "honey", "fresh mint leaves"],
   "directions": ["Boil water", "Pour boiling hot water into a mug", "Add fresh mint leaves", "Mix and let the mint leaves seep for 3-5 minutes", "Add honey and mix again"]
}
```

<details>
<summary><b>Sample response</b></summary>
<p>
  
```json 
{
   "id": 1
}
```
  
</p>
</details>

### Update Recipe Endpoint

Endpoint receives a recipe as a JSON object and updates a recipe with a specified id. The server should return the 204 (No Content) status code. If a recipe with a specified id does not exist, the server should return 404 (Not found). The server should respond with 400 (Bad Request) if a recipe doesn't follow the restrictions indicated above in section [Add Recipe Endpoint](#add-recipe-endpoint). Only an author of a recipe can update it. If a user is not the author of a recipe, but he try to carry out the action mentioned above, the service should respond with the 403 (Forbidden) status code.

PUT method

```PUT /api/recipe/1``` request with basic authentication; email (login): Cook_Programmer@somewhere.com, password: RecipeInBinary

Request body:

```json
{
   "name": "Fresh Mint Tea",
   "category": "beverage",
   "description": "Light, aromatic and refreshing beverage, ...",
   "ingredients": ["boiled water", "honey", "fresh mint leaves"],
   "directions": ["Boil water", "Pour boiling hot water into a mug", "Add fresh mint leaves", "Mix and let the mint leaves seep for 3-5 minutes", "Add honey and mix again"]
}
```

<details>
<summary><b>Sample response</b></summary>
  Stasus code: 204 (No Content)
</details>

### Get Recipe Endpoint

Endpoint  returns a recipe with a specified id as a JSON object (where {id} is the id of a recipe). The server should respond with the 200 (Ok) status code. If a recipe with a specified id does not exist, the server should respond with 404 (Not found). Only a register user can search a recipe. If a user is not register, but he try to carry out the action mentioned above, the service should respond with the 401 (Unauthorized) status code.

GET method

```GET /api/recipe/1``` request with basic authentication; email (login): Cook_Programmer@somewhere.com, password: RecipeInBinary

Path Variable:
```java
long id
```

<details>
<summary><b>Sample response</b></summary>
<p>
  
```json 
{
   "name": "Fresh Mint Tea",
   "category": "beverage",
   "date": "2020-01-02T12:11:25.034734",
   "description": "Light, aromatic and refreshing beverage, ...",
   "ingredients": ["boiled water", "honey", "fresh mint leaves"],
   "directions": ["Boil water", "Pour boiling hot water into a mug", "Add fresh mint leaves", "Mix and let the mint leaves seep for 3-5 minutes", "Add honey and mix again"]
}
```
  
</p>
</details>

### Search Recipe Endpoint

Endpoint takes one of the two mutually exclusive query parameters:
- category – if this parameter is specified, it returns a JSON array of all recipes of the specified category. Search is case-insensitive, the recipes are sorted by date(newest first).
- name – if this parameter is specified, it returns a JSON array of all recipes with the names that contain the specified parameter. Search is case-insensitive, the recipes are sorted by date(newest first). If no recipes are found, the program should return an empty JSON array. If 0 parameters were passed, or more than 1, the server should return 400 (Bad Request). The same response should follow if the specified parameters are not valid. If everything is correct, it should return 200 (Ok).
Only a register user can search a recipe. If a user is not register, but he try to carry out the action mentioned above, the service should respond with the 401 (Unauthorized) status code.

Request parameter:

```java
String category
```

or 

```java
String name
```

GET method

``` GET /api/recipe/search/?category=dessert ```

<details>
<summary><b>Sample response</b></summary>
<p>
  
```json 
[
   {
      "name": "Vegan Chocolate Ice Cream",
      "category": "dessert",
      "date": "2021-04-06T14:10:54.009345",
      ....
   },
   {
      "name": "vegan avocado ice cream",
      "category": "DESSERT",
      "date": "2020-01-06T13:10:53.011342",
      ....
   },
]
```
</p>
</details>

``` GET /api/recipe/search/?name=tea ```

<details>
<summary><b>Sample response</b></summary>
<p>
  
```json 
[
   {
      "name": "Fresh Mint Tea",
      "category": "beverage",
      "date": "2021-09-06T14:11:51.006787",
      ....
   },
   {
      "name": "warming ginger tea",
      "category": "beverage",
      "date": "2020-08-06T14:11:42.456321",
      ....
   },
   {
      "name": "Iced Tea Without Sugar",
      "category": "beverage",
      "date": "2019-07-06T17:12:32.546987",
      ....
   },
]
```
</p>
</details>

### Delete Recipe Endpoint

It deletes a recipe with a specified {id}. The server should respond with the 204 (No Content) status code. If a recipe with a specified id does not exist, the server should return 404 (Not found). Only an author of a recipe can delete it. If a user is not the author of a recipe, but they try to carry out the actions mentioned above, the service should respond with the 403 (Forbidden) status code.

DELETE method

``` DELETE /api/recipe/1 ```

<details>
<summary><b>Sample response</b></summary>
  Stasus code: 204 (No Content)
</details>
