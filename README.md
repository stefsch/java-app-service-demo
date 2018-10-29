# Java on App Service for Linux (Demo)

This project is a standalone demonstration of Java on Azure App Service for Linux. This is meant to be used by Microsoft employees to highlight the core features of the product.

## Contents

- [Requirements](#requirements)
- [Preparation](#preparation)
- [Demonstration Steps](#demonstration-steps)
- [Pitching Notes](#pitching-notes)
- [Technical Notes](#technical-notes)

## Requirements

Before working through this demo, you must have the following technologies on your machine.

- The Azure CLI
- An Azure account and subscription
- Maven (version 3 or greater)

## Preparation

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

1. Export the follwing environment variables for your resources. Do not change these names. I suggest making these permanent environment variables (rather than confined to just the session).
    - RESOURCE_GROUP --> Name of your resource group
    - APP_SERVICE_NAME --> name of your App Service instance
    - DOCUMENTDB_URI --> URI of your Cosmos DB
    - DOCUMENTDB_KEY --> Primary key for Cosmos
    - DOCUMENTDB_DBNAME --> Name of your Cosmos DB

1. Deploy the application!
    - Package and deploy the application: `mvn clean package azure-webapp:deploy`
    - We want to have it deployed ahead of the demo because the cold-start can take a few minutes. During the demo we will _re_-deploy the application, which saves time.

## Demonstration Steps

1. Orient the audenience with the project
    - It's a simple To-Do List application powered by Spring Boot on the backend, and AngularJS on the front. 
    - The src directory is fairly lean. The controller class shows all the URL mappings.
    - Show Azure plugin in the POM and note the following:
        - The properties within the `<appSettings>` are set as environment variables when the app is deployed
        - Here they configure our connection to the Cosmos DB
1. Use the Maven plugin to deploy the application
    - `mvn azure-webapp:deploy`
    - While the deployment is happening, you can show the current version of the application in the browser
    - Create an entry or two
1. Navigate to the Portal
    - Under "Application Settings", show that the poperties from the POM plugin are now environment variables


## Pitching Notes

- Using the Maven plugin for deployment
    - This is the primary story we are pushing for deploying Java applications to App Service. Note that they only need to specify their app service name, a resource group, and a runtime. That's it! If the app does not already exist, the plugin will create it!
- They can deploy a container and use the Azul JDK
    - Add Git repo with images
- __Managed__ service for Java. No one else has this!!
- Upcoming feature to auto-update the JDK major/minor version, or stay at your current version

## Technical Notes

- Underlying Linux OS?
- 