# Recipes
* [Description](#description)
* [Technologies](#technologies)
* [Setup](#setup)
* [Endpoints](#endpoints)
    * [Register User Endpoint](#register-user-endpoint)
    * [Add Recipe Endpoint](#add-recipe-endpoint)
    * [Update Recipe Endpoint](#update-recipe-endpoint)
    * [Get Recipe Endpoint](#get-recipe-endpoint)

## Description

<details>
<summary>Click here to see general information about <b>Project</b>!</summary>
  
  
A multi-user web application that allows storing, retrieving, updating and deleting recipes. Each user can search for recipes by category or name. The serivce          supports registration process and allows users (after logon) to add their own recipes and to update, delete and search this recipes by ID.
  
  
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

## Endpoints

When you start the application a H2 database containing initial tables will be automaticly created. You can use [Postman](https://www.postman.com) or any similar program for testing existing endpoints.

### Register User Endpoint

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

Endpoint receives a recipe as a JSON object and returns a JSON object with one id field. This is a uniquely generated number by which we can identify and retrieve a recipe later. 

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

Endpoint  returns a recipe with a specified id as a JSON object (where {id} is the id of a recipe). The server should respond with the 200 (Ok) status code. If a recipe with a specified id does not exist, the server should respond with 404 (Not found).

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







