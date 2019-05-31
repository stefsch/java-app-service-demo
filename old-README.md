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

1. Clone this project: `git clone https://github.com/JasonFreeberg/java-app-service-demo.git` 
1. Provision a Resource Group and Cosmos DB account

    You can create the resources from the portal or using the CLI. The database name must be lowercase. For CLI users...

    ```
    az group create -n <your-azure-group-name> -l <your-resource-group-region>
    az cosmosdb create --kind GlobalDocumentDB -g <your-azure-group-name> -n <your-azure-documentDB-name>
    ```

    Get your database's URI from the Portal or from the JSON response if you used the CLI.

    Lastly, get the `primaryMasterKey` connection key:

    ```
    az cosmosdb list-keys -g <your-azure-group-name> -n <your-azure-documentDB-name>
    ```
1. Create an App Service instance and set up the scaling rules
    1. Create an App Service instance in the Portal with the following settings:
        - __OS__: Linux
        - __Publish__ : Code
        - __Runtime Stack__: Tomcat 9.0 (JRE 8)
    1. Create a __new__ App Service plan with a P1v2
        - If you have other applications in your plan, the scaling rules may not work as planned
    1. Set up scaling rules
        - Navigate to "Scale out (App Service plan)"
        - Click "Enable autoscale"
        - Click "Add Rule", and set the following for "Criteria":

        | Field                | Memory Rule             | CPU Rule                |
        |----------------------|-------------------------|-------------------------|
        | Time Aggregation     | Minimum                 | Minimum                 |
        | Metric name          | Memory Percentage       | CPU Percentage          |
        | Time grain statistic | Average                 | Minimum                 |
        | Operator             | Greater than            | Greater than            |
        | Threshold            | 60                      | 60                      |
        | Duration             | 5                       | 5                       |
        | Operation            | Increase instance count | Increase instance count |
        | Instance count       | 1                       | 1                       |
        | Cool down (minutes)  | 5                       | 5                       |

1. Set the follwing environment variables on your local machine. Do not change these names. I suggest making these permanent environment variables (rather than the session current).
    - `RESOURCE_GROUP`: Name of your resource group
    - `APP_SERVICE_NAME`:  Name of your App Service instance
    - `DOCUMENTDB_URI`:  URI of your Cosmos DB
    - `DOCUMENTDB_KEY`: Primary key for Cosmos
    - `DOCUMENTDB_DBNAME`: Name of your Cosmos DB
1. Deploy the application!
    - Package and deploy the application: `mvn clean package azure-webapp:deploy`
    - We want to have it deployed ahead of the demo because the cold-start can take a few minutes. During the demo we will _re_-deploy the application, which saves time.

## Demonstration Steps

1. Orient the audience with the project
    - It's a simple To-Do List application powered by Spring Boot on the backend, and AngularJS on the front. 
    - The src directory is fairly lean. The controller class shows all the URL mappings.
    - Show the Azure plugin in the POM and note the following:
        - You __only__ need to include the app name, resource group, and 
        - The properties within the `<appSettings>` are set as environment variables when the app is deployed
1. Use the Maven plugin to deploy the application
    - `mvn azure-webapp:deploy`
    - While the deployment is happening, you can show the current version of the application in the browser
    - Create an entry or two
1. Navigate to the Portal
    - Under "Application Settings", show that the poperties from the POM plugin are now environment variables
    - Show the scaling rules in Portal
1. Test Cosmos and scaling rules
    - Make a record in Cosmos
    - Spam the buttons to stress-test the memory and CPU
1. Check on the instance count
    - In "Overview", we should see spikes for CPU and memory usage
    - Under "Scale out" the instance count should have increased
        - It may take a few minutes.
1. Finally, show the web SSH and log stream

## Pitching Notes

- Using the Maven plugin for deployment
    - This is the primary story we are pushing for deploying Java applications to App Service. Note that they only need to specify their app service name, a resource group, and a runtime. That's it! If the app does not already exist, the plugin will create it!
- They can deploy a container and use the Azul JDK
    - Add Git repo with images
- __Managed__ service for Java. 
    - Upcoming feature to auto-update the JDK major/minor version, or stay at your current version
- Things __not__ covered in this demo:
    - Site swaps
    - Jenkins CICD plugin
    - Application Insights

## Technical Notes

- Underlying Linux OS?
- To run the app locally, you must have Tomcat locally installed with an enviornment variable, `TOMCAT_HOME` pointing to the tomcat directory. Then you can deploy to your lcoal tomcat using Maven: `mvn clean package cargo:deploy`. 
    - Be sure to use the `clean` command if you are testing changes to the Javascript, otherwise your changes may not be packaged.
- Deploying with the WAR is faster because we can do hot deployments thanks to Tomcat
    - If you decide to package a JAR, it may be slower