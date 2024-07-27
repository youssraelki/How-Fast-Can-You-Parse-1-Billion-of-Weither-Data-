# City Temperature Analysis

## Description
This project aims to measure the performance of parsing large CSV files containing weather data. The project is divided into several stages to process a file with 1 billion lines and derive temperature statistics by city.

## Project Structure
- *src/*: Contains Java code for processing CSV files.
  - *main/CityTemperature.java*: Processes the CSV file to extract and calculate the maximum, minimum, and average temperatures by city.
  - *test/CityTemperatureTest.java*: Contains unit tests for the temperature processing code.
  - *test/CityTemperatureIntegrationTest.java*: Contains integration tests to verify the interaction between different parts of the code.
- *.github/workflows/*: Contains YAML files for continuous integration with GitHub Actions.
  - *java.yml*: Workflow to compile and run Java code, as well as to check files and performance.
- *pom.xml*: Maven configuration file for managing project dependencies and configurations.
- *.gitignore*: Contains rules to ignore large files.

## Project Objective
This repository demonstrates how to efficiently parse a CSV file with 1 billion lines and obtain temperature statistics by city.

## Technologies Used
- *Java JDK 20*: Used to compile and run Java code. Version 20 was chosen for its performance improvements and new features.
- *Maven*: Used for dependency management and project configuration.
- *GitHub Actions*: Used for continuous integration and task automation. GitHub Actions facilitates managing CI/CD workflows by enabling efficient code testing and deployment.

## Optimization Strategy
- *Initial Validation*: Utilizes GitHub Actions to run integration tests whenever there is a push or pull request to the main branch.
- *Methodology*: Java code processes the file with 1 billion lines to extract temperature data.
- *GitHub Actions*: Employs GitHub Actions to compile and execute code, ensures the large file is ignored with .gitignore, and downloads the large file from Google Drive if necessary.
- *Performance*: Measures execution time and memory usage to optimize performance.

### Why GitHub Actions?
We chose GitHub Actions for the following reasons:
1. *Easy Integration*: GitHub Actions integrates seamlessly with GitHub repositories, simplifying CI/CD workflow setup without external tools.
2. *Automation*: Workflows can be automated to run on specific events like pushes and pull requests, ensuring code is always tested before merging.
3. *Scalability*: GitHub Actions provides execution environments that can be configured for different Java versions, crucial for testing code in various environments.
4. *Ease of Use*: Configuring workflows with YAML files is simple and flexible, allowing easy customization based on project needs.

## Results
- *Execution Time*: The average execution time for processing the 1 billion line file was measured at 3288437 ms.
- *Memory Consumption*: The average memory consumption was measured at 55795 KB.
- *Data Validity*: The processed data was successfully validated, showing high accuracy in calculating minimum, maximum, and average temperatures.

## Project Duration
This project took approximately 1 week to complete, including development, testing, and optimization.

## Usage
1. Clone the repository.
    sh
    git clone https://github.com/your-username/your-project.git
    
2. Navigate to the project directory.
    sh
    cd your-project
    
3. Compile and run the Java code.
    sh
    mvn compile
    mvn exec:java -Dexec.mainClass="CityTemperature"
    

## Branches
- *main*: Main branch containing the stable version of the code.

## Libraries Used
- *BufferedReader*: Used for efficiently reading CSV files line by line.
- *FileReader*: Used for opening and reading CSV files.
- *HashMap*: Used for fast storage and access to temperatures by city.

## Contributions
For more information on how to contribute to this project, please see CONTRIBUTING.md.

## License
This project is licensed under the MIT License. See the LICENSE file for details.
