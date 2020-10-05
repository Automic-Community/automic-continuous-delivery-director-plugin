## Getting Started:

###### Description

    CA Continuous Delivery Director (CA CDD) helps you orchestrate a continuous delivery pipeline for quick and dependable application deployments. The CA Continuous Delivery Director Plugin allows you to import the Application model (applications, environments, etc.) from the integrated CA Continuous Delivery Automation (formerly known as CA Automic Release Automation) instance and start both Application and General Workflows directly from the CA Continuous Delivery Director user interface.
    Note: The version v3.0.0 of the plugin is not backward compatible. That means, if any older version was used in your environment, upgrading to version v3.0.0 of the CDD Plugin will require re-assignment/reconfiguration of CDA environments to CDD phases in CDD.

###### Supported CA Continuous Delivery Director versions:

1. CA Continuous Delivery Director V6.6 (validated with Plugin V1.0.0)
2. CA Continuous Delivery Director V6.7 (validated with Plugin V1.0.4)

###### Prerequisites:

1. ** Plugins-dto-2.21.jar inside the lib directory and the lib directory is parallel to the src directory.**
2. CA Continuous Delivery Automation 12.0.x or later
3. Plugin version 1.0.2 is compabible with CA Continuous Delivery Automation V12.0
4. Plugin versions >= 1.0.3 are compatible with CA Continuous Delivery Automation >= V12.1

###### Step to install CDD plugin:

  1. Download the Plug-in for CA CDD from the https://marketplace.automic.com/
	2. Extract the .war file to the webapps directory of the Tomcat server.
	3. Log in to CA Continuous Delivery Director.
	4. Click the Administration tab and select Plug-ins.
	5. Click Register Plug-in at the top-right corner. The REGISTER PLUG-IN dialog is displayed.
	6. Enter the manifest URL of the plug-in in the following format: http://pluginserver:port/plugin-name/manifest.json.
	7. Click Register to start the installation process.

###### Adding the CDA Endpoint

Integrating > CDA Plug-ins for Third-Party Products > Plug-in for CA CDD > Adding the CDA Endpoint

  > Adding the CDA Endpoint
      
      After the plug-in has been installed, an endpoint must be configured to gain access to the CDA instance from CA Continuous Delivery Director.

    - To Add the CDA Endpoint
    
    1. Click the Administration tab and select Endpoints.
    2. Click Add Endpoint at the top-right corner. The ADD ENDPOINT dialog is displayed.
    3. Enter the following information:
      1. Name of the endpoint
        - Example: Dev Instance
      2. Optionally, a description to better identify the endpoint.
      3. From the drop-down list, select one or more users you want to authorize for the endpoint.
      4. Select the ARA Endpoint plug-in.
      5. In the Input Parameters section:      
               1. Enter the URL of your CDA REST endpoint.               
                  Important! The port used in the URL is for the CDA application and not for the Web UI of the CDA application. Typically, the application is installed on IIS, which uses port 80. The Web UI to which you connect to CDA using a browser uses the Tomcat port 8080.			
                  Note: The URL can be specified as http or https.
                  Example:				
                    - URL to connect to the CDA application: http://myCDAhost:80/cda
                    - URL to connect to the CDA Web UI: http://myCDAhost:8080/awi
               2. Enter the credentials of a CDA user (username in the following format: client/name/department and password) with sufficient rights to execute an   Application deployment.
		
    4. Click Test connection to verify that you can connect to the CDA (ARA) endpoint.
    5. Click Add.
   
   > Importing Applications and Environments
   
      You can import Applications and Environments from your CDA (ARA) endpoint to be used in the release orchestration pipeline.
      Notes:
            Any changes to the Applications or Environments are to be made directly within your CDA instance.
            CA Continuous Delivery Director will convert any imported CDA Environments with the same name to a single shared environment.
      To Import Applications
            Click the Administration tab and select Applications and Environments.
            Click Add External Applications. The ADD EXTERNAL APPLICATIONS dialog is displayed.
            Select the CDA endpoint (ARA).
            Click Import.
            
   > Creating CDA Tasks
     
        As a Release Manager, you work with two types of automatic tasks (Workflows) associated with the release: Start General Workflow and Start Application Workflow.
        Note: For detailed information about how to design and create releases, refer to the official CA Continuous Delivery Director documentation.
        
        ###### To Create a CDA Task
          1. Click the Releases tab.
          2. Click Create a task... within the phase of your choice. The CREATE TASK dialog is displayed.
          3. Enter a task name.
          4. Optionally, enter a description to better identify the task.
          5. Assign one or more owners to the task.
          6. From the Task Type drop-down list, select one of the following CDA (ARA) task types: Start General Workflows or Start Application Workflow.
             Note:
             Unlike General Workflows, Application Workflows typically contain only one or more Component Workflows at their highest level. Each Component Workflow, however, contains objects and functions like a General Workflow. Component Workflows orchestrate the deployment of an individual component (or parts of it) on one or more Deployment Targets within an Environment.
             > Start General Workflow
                1. Select the endpoint created to reach the CDA instance.
                   Example: Dev Instance
                2. Enter the General Workflow you want to be executed.
                   Note: The workflow value can be:
                       1. Selected from the list displayed after typing "@" in the field
                          Example: Deploy Tomcat
                       2. Entered as a token (reusable placeholders used to create generic Workflows) by typing "%" in the field and selecting an option from the list.
                          Example: last_successful_build
                       3. Entered as free text (alone or together with tokens) 
                       
             > Start Application Workflow
                1. Select the endpoint created to reach the CDA instance .
                   Example: Dev Instance
                2. Enter the Application, Application Workflow, Deployment Package and Deployment Profile values.
                   Note: The values can be:
                   1. Selected from the list displayed after typing "@" in the field.
                      Example: Deploy Tomcat
                   2. Entered as a token (reusable placeholders used to create generic Workflows) by typing "%" in the field and selecting an option from the list.
                      Example: last_successful_build
                   3. Entered as free text (alone or together with tokens)
                3. Select an installation mode:
                    1. Overwrite Existing: to deploy the Package on every target and overwriting existing components.
                    2. Skip Existing: to only deploy the Application on targets where the deployment package has not yet been deployed.
          7. Optionally, in the CONTENT panel you can select the Application content you want to tie with the completion of the task to keep track of when and where the content is deployed.
          8. Click Create to create the task.    
        
		
###### Copyright and License: 

Broadcom does not support, maintain or warrant Solutions, Templates, Actions and any other content published on the Community and is subject to Broadcom Community Terms and Conditions (https://community.broadcom.com/termsandconditions).
