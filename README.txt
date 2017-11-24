#8 Point Solutions - Code Challenge (Setup and Execution)

## 1. Configure Tomcat Server

1.1 Download and install Tomcat 9.
1.2 Navigate to {install-folder}/tomcat/conf.
1.3 Open server.xml and take note of the port being used by the server (for example, 9556 below):

	<Connector connectionTimeout="20000" port="9556" protocol="HTTP/1.1" redirectPort="8443"/>

1.4 Open tomcat-users.xml and append the following roles and users to the end of the config file before </tomcat-users/>:

	<role rolename="manager-gui" />
	<user username="{admin-username}" password="{secure-password}" roles="manager-gui" />

1.5 Start tomcat service by executing the following command in {install-folder}/tomcat/bin:

	win cmd > catalina.bat run
	bash > ./catalina.bat run

1.6 This will start the server on http://localhost:{port} as set in server.xml.

## 2. Install mysql

2.1 Download and install mysql database server.
2.2 Take note of the port it is running on, and the username and password set up during installation.

## 3. Client Configuration

3.1 Navigate to ~/codechallenge/src/main/resources/ and open config.properties.
3.2 Provide database settings domain (no "http://"), port, user and pass, as in step 2.2.

## 4. Build Project from Maven

Assuming Maven is installed and PATH and aliases are set up... 

4.1 Navigate to ~/codechallenge and execute the following command in cmd/bash. This will run integration tests and create the WAR file required for deployment of the REST API and Management screens in ~/codechallenge/target. It will also create an empty database on MySql.

	> mvn clean install

4.2 Navigate to ~/codechallengetransactions and execute the same command.

	> mvn clean install

## 5. Deploy WAR to Tomcat

With tomcat service running...

5.1 Access server management screen at http://localhost:{port} as in step 1.6.
5.2 Go to "Manager App".
5.3 Enter credentials for user as set in step 1.4.
5.4 In "Deploy" > "WAR file to deploy" section, provide the WAR file generated in Step 4.1 (@ ~/codechallenge/target).
5.5 Click "Deploy".
5.6 Go to http://localhost:{port}/codechallenge. Supplier management screen (with no suppliers) should load successfully.

## 6. Run Transaction Generator

**Ensure that the management screen on http://localhost:{port}/codechallenge loads successfully before proceeding.**

Navigate to ~/codechallengetransactions/target and execute the newly created jar file (probably named "codechallengetransactions-1-jar-with-dependencies.jar"):

	> java -jar codechallengetransactions-1-jar-with-dependencies.jar {serverURL} {transaction-count}

...where:
	- serverURL: http://localhost:{port} as used above.
	- transaction-count: Number of transactions to generate.

This will create 2 suppliers (in the case that no suppliers have been added through the management screen) and assign {transaction-count} transactions at random between them. You can view the new transactions and suppliers through the management screen at http://localhost:{port}/codechallenge.