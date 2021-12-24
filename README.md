# Descrption
Java REST API built for user to calculate six degree of Kevin Bacon

# Setup


### Prerequisites

Latest version of Java  
- JDK Version 16.0.1  
- JRE Version 16.0.1  

Latest version of Neo4j Desktop  
Maven version 3.6.3
IntelliJ IDEA Community Edition(recommended IDE)
Postman(API testing)

### Setup
1. Clone the repo if needed
   ```sh
   git clone https://github.com/Tony-beeper/six-degree-kevin-bacon.git
   ```
2. Setup as a Maven project  
3. Run Configuration: Maven with command line exec:java
4. Make sure project structure on IDE is JDK Version 16.0.1  



# DATABASE REQUIREMENTS

Neo4j Required

- Username: neo4j
- Password: 123456

# DATABASE Setup

Default DB connection port: bolt://localhost:7687
Can be changed in .env file

# API call

### Put Requests  

- PUT addr:8080/api/v1/addRelationship  

- PUT addr:8080/api/v1/addActor  

- PUT addr:8080/api/v1/addMovie  

### Get Requests

- PUT addr:8080/api/v1/computeBaconPath  

- PUT addr:8080/api/v1/getMovie  

- PUT addr:8080/api/v1/hasRelationship  

- PUT addr:8080/api/v1/getActor  

- PUT addr:8080/api/v1/computeBaconNumber

- PUT addr:8080/api/v1/clearNodes  



# Neo4j sample picture
![alt text](https://github.com/Tony-beeper/six-degree-kevin-bacon/blob/main/Neo4j_Desktop_Pic.png?raw=true)
