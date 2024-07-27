### Contribution Guide

Thank you for your interest in contributing to our project "How-Fast-Can-You-Parse-1-Billion-of-Weather-Data"! Here is a guide to help you participate effectively.

## Project Structure

### Main Files

- *src/*: Contains the Java code for processing CSV files.
  - *CityTemperature.java*: Main code for processing the large CSV file and calculating temperature statistics per city.

- *pom.xml*: Maven configuration file to manage project dependencies and configurations.

- *src/test/CityTemperatureTest.java*: Contains unit tests to verify that individual methods of the code work correctly.

- *src/test/CityTemperatureIntegrationTest.java*: Contains integration tests to verify that different parts of the code work together as expected.

- *.github/workflows/java.yml*: YAML file for continuous integration with GitHub Actions. This workflow compiles and runs the Java code, checks the files, and measures performance.

- *.gitignore*: Contains rules to ignore large files to prevent them from being included in commits.

## Branch Distribution

- *main*: Main branch containing the stable version of the code. All new features and bug fixes must go through this branch after being tested and validated.

## Libraries Used

- *java.io.BufferedReader*: To read CSV files efficiently using a buffer to minimize disk access.
- *java.io.FileReader*: To open and read CSV files.
- *java.util.HashMap*: To store temperatures per city, allowing quick access to the data.
- *java.util.Map*: Interface used to store key-value pairs representing temperatures per city.

### Why These Libraries?
- *BufferedReader and FileReader*: Provide a quick and simple way to read files line by line, which is essential for processing large CSV files.
- *HashMap*: Allows storing and updating temperatures efficiently, offering quick data lookup and updates.

## Testing

### Initial Validation

- *GitHub Actions*: Used to run unit, integration, and end-to-end tests automatically on pushes and pull requests to the main branch. This approach ensures that changes are validated before being merged into the main branch.

### Types of Tests

- *Unit Tests*: Verify that individual methods work correctly.
- *Integration Tests*: Ensure that different parts of the code work together as expected, especially the processing and validation of CSV files.

## Integrity

### Integration Process

- *CI/CD Validation*: GitHub Actions automatically validates changes. Tests must pass for changes to be merged into the main branch.
- *Code Review*: Pull Requests must be reviewed by team members to ensure code quality and consistency.

## How to Contribute

### 1. Fork the Repository
- Fork this repository by clicking the "Fork" button at the top of the GitHub page.

### 2. Clone Your Forked Repository
- Clone your forked repository to your local machine:
    sh
    git clone https://github.com/your-username/How-Fast-Can-You-Parse-1-Billion-of-Weather-Data.git
    cd How-Fast-Can-You-Parse-1-Billion-of-Weather-Data
    

### 3. Create a Branch
- Create a new branch for your changes:
    sh
    git checkout -b your-branch-name
    

### 4. Make Changes
- Make the necessary changes to the following files:
  - *pom.xml*: Modify dependencies or configurations if needed.
  - *src/main/CityTemperature.java*: Make improvements or fixes to the main code.
  - *src/test/CityTemperatureTest.java*: Add or modify unit tests to verify new features or fixes.
  - *src/test/CityTemperatureIntegrationTest.java*: Add or modify integration tests to verify the interaction between different parts of the code.
  - *.github/workflows/java.yml*: Modify the workflow if you need to adjust CI/CD steps or add new tests.

### 5. Test Your Changes
- Ensure all changes are properly tested. You can run the tests locally using Maven:
    sh
    mvn test
    

### 6. Commit and Push
- Commit your changes with a descriptive message:
    sh
    git add .
    git commit -m "Description of changes made"
    git push origin your-branch-name
    

### 7. Create a Pull Request
- Go to your forked repository on GitHub and open a Pull Request to the main branch of the original repository. Describe the changes you made and why they are important.
