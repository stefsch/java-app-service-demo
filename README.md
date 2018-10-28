# Java on App Service for Linux (Demo)

This project is a standalone demonstration of Java on Azure App Service for Linux. This is meant to be used by Microsoft employees to highlight the core features of the product. In this demo you will...

## Contents

- [Requirements](#requirements)
- [Preparation](#preparation)
- [Demonstration Steps](#demonstration-steps)
- [Pitching Notes](#pitching-notes)

### Requirements

Before working through this demo, you must have the following technologies on your machine.

- The Azure CLI
- An Azure account and subscription
- Maven (version 3 or greater)

### Preparation

Complete these steps __before__ meeting a customer or going to a conference.

1. Clone this project

    `git clone https://github.com/JasonFreeberg/java-app-service-demo.git` 

1. Provision a Resource Group and Cosmos DB account

    You can create the resources from the portal or using the CLI. The database name must be lowercase. For CLI users...

    ```bash
    az group create -n <your-azure-group-name> -l <your-resource-group-region>
    az cosmosdb create --kind GlobalDocumentDB -g <your-azure-group-name> -n <your-azure-documentDB-name>
    ```

    Get your database's URI from the Portal or from the JSON response if you used the CLI.

    Lastly, get the `primaryMasterKey` connection key by running this command:

    `az cosmosdb list-keys -g <your-azure-group-name> -n <your-azure-documentDB-name>`

1. Create an App Service instance and set up the scaling rules

    1. Create an App Service instance in the Portal with the following settings:
        - OS --> Linux
        - Publish --> Code
        - Runtime Stack --> Tomcat 9.0 (JRE 8)
        - Create a new App Service plan with ____ and no other apps in it
            - __TODO__: Figure out best tier

    1. Set up scaling rules
        - Navigate to "Scale out (App Service plan)"
        - Click "Enable autoscale"
        - Click "Add Rule", and set the following for "Criteria":
            - Time Aggregation --> Average
            - Metric name --> CPU Percentage
            - Timem grain statistic --> Average
            - Operater --> Greater than
            - Threshold --> 70
            - Duration --> 5 minutes
        - Set the following for "Action":
            - Operation --> Increase count by 
            - Instance count --> 1
            - Cool down (minutes) --> 2
        - __TODO__: Memory instructions

1. Configure the Maven POM.xml

    - Add the name of your App Service instance and the resource group to the pom.xml

1. Export environment variables for your document DB
    - 

### Demonstration Steps

1. 


### Pitching Notes