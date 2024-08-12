# City Temperature Analysis

## Description
This project aims to measure the performance of parsing large CSV files containing weather data. The project is divided into several stages to process a file with 1 billion lines and derive temperature statistics by city.

## Project Structure
- `src/`: Contains Java code for processing CSV files.
  - `main/CityTemperature.java`: Processes the CSV file to extract and calculate the maximum, minimum, and average temperatures by city.
  - `test/CityTemperatureTest.java`: Contains unit tests for the temperature processing code.
  - `test/CityTemperatureIntegrationTest.java`: Contains integration tests to verify the interaction between different parts of the code.
- `.github/workflows/`: Contains YAML files for continuous integration with GitHub Actions.
  - `java.yml`: Workflow to compile and run Java code, as well as to check files and performance.
- `pom.xml`: Maven configuration file for managing project dependencies and configurations.
- `.gitignore`: Contains rules to ignore large files.

## Project Objective
This repository demonstrates how to efficiently parse a CSV file with 1 billion lines and obtain temperature statistics by city.

## Technologies Used
- Java JDK 20: Used to compile and run Java code. Version 20 was chosen for its performance improvements and new features.
- Maven: Used for dependency management and project configuration.
- GitHub Actions: Used for continuous integration and task automation. GitHub Actions facilitates managing CI/CD workflows by enabling efficient code testing and deployment.

## Optimization Strategy
- Initial Validation: Utilizes GitHub Actions to run integration tests whenever there is a push or pull request to the main branch.
- Methodology: Java code processes the file with 1 billion lines to extract temperature data.
- GitHub Actions: Employs GitHub Actions to compile and execute code, ensures the large file is ignored with `.gitignore`, and downloads the large file from Google Drive if necessary.
- Performance: Measures execution time and memory usage to optimize performance.

### Why GitHub Actions?
We chose GitHub Actions for the following reasons:
1. **Easy Integration**: GitHub Actions integrates seamlessly with GitHub repositories, simplifying CI/CD workflow setup without external tools.
2. **Automation**: Workflows can be automated to run on specific events like pushes and pull requests, ensuring code is always tested before merging.
3. **Scalability**: GitHub Actions provides execution environments that can be configured for different Java versions, crucial for testing code in various environments.
4. **Ease of Use**: Configuring workflows with YAML files is simple and flexible, allowing easy customization based on project needs.

## Results
- **Execution Time**: The average execution time for processing the 1 billion line file was measured at 3288437 ms.
- **Memory Consumption**: The average memory consumption was measured at 55795 KB.
- **Data Validity**: The processed data was successfully validated, showing high accuracy in calculating minimum, maximum, and average temperatures.

## Project Duration
This project took approximately 1 week to complete, including development, testing, and optimization.

## Usage
1. Clone the repository.
    ```sh
    git clone https://github.com/your-username/your-project.git
    ```
2. Navigate to the project directory.
    ```sh
    cd your-project
    ```
3. Compile and run the Java code.
    ```sh
    mvn compile
    mvn exec:java -Dexec.mainClass="CityTemperature"
    ```

## Branches
- `main`: Main branch containing the stable version of the code.

## Libraries Used
- `BufferedReader`: Used for efficiently reading CSV files line by line.
- `FileReader`: Used for opening and reading CSV files.
- `HashMap`: Used for fast storage and access to temperatures by city.

## Contributions
For more information on how to contribute to this project, please see [CONTRIBUTING.md](CONTRIBUTING.md).

## License
This project is licensed under the MIT License. See the [LICENSE.md](LICENSE.md) file for details.

# Projet de Traitement de Données avec MapReduce

## Description
Ce projet implémente un pipeline de traitement de données en utilisant Java et MapReduce. Il comprend la génération, le traitement et la fusion de fichiers CSV.

## Fonctionnalités
- *Génération de Données* : Génère des fichiers CSV avec des données simulées.
- *Traitement MapReduce* : Utilise MapReduce pour traiter et agréger les données des fichiers CSV générés.
- *Fusion de CSV* : Fusionne plusieurs fichiers CSV en un seul fichier.

## Guide d'Installation

### Prérequis
- JDK 22
- Maven 3.8 ou version ultérieure
- Hadoop (pour les tâches MapReduce)

### Installation
1. *Cloner le dépôt* :
   - Clonez le dépôt Git à l'aide de l'URL fournie.

2. *Naviguer dans le répertoire du projet* :
   - Accédez au répertoire du projet cloné.

3. *Installer les dépendances* :
   - Utilisez Maven pour installer les dépendances nécessaires.

4. *Compiler le projet* :
   - Compilez le code source en utilisant Maven.

5. *Exécuter la génération de données* :
   - Les classes `GenerateCSV1`, `GenerateCSV2`, et `GenerateCSV3` sont responsables de la génération des fichiers CSV.

6. *Exécuter le traitement MapReduce* :
   - La classe `MapReduceMain` traite et agrège les données des fichiers CSV à l'aide de MapReduce.

7. *Fusionner les fichiers CSV* :
   - La classe `MergeCSVs` combine les fichiers CSV en un seul fichier.

## Exécution des Tâches MapReduce

Pour exécuter les tâches MapReduce, suivez les étapes suivantes :

1. Assurez-vous que Hadoop est installé et configuré sur votre machine.
2. Compilez le projet avec Maven :
    ```sh
    mvn clean install
    ```
3. Exécutez le job MapReduce :
    ```sh
    hadoop jar target/my-mapreduce-job.jar mapreduce.TemperatureAnalysis <input path> <output path>
    ```
   Les chemins `<input path>` et `<output path>` doivent être remplacés par les chemins appropriés dans votre système de fichiers Hadoop.

## Stratégie de MapReduce et Utilisation des Buffers

### Stratégie de MapReduce

Le modèle MapReduce est une technique efficace pour le traitement de grandes quantités de données en parallèle. Il est composé de deux phases principales : **Map** et **Reduce**.

1. **Phase Map** :
   - **Objectif** : Diviser le travail en sous-tâches et traiter chaque sous-tâche indépendamment.
   - **Comment** : Les données sont lues en morceaux ou "batches" (lots) pour être traitées en parallèle. Chaque batch est traité par un thread distinct, et les résultats partiels sont collectés. Dans notre implémentation, la méthode `processBatch()` est responsable de cette phase. Chaque ligne du fichier CSV est analysée pour extraire les informations pertinentes (comme les températures et les villes). Les résultats intermédiaires pour chaque ville sont ensuite stockés dans une structure de données `Map`.

2. **Phase Reduce** :
   - **Objectif** : Agréger les résultats intermédiaires pour produire les résultats finaux.
   - **Comment** : Les résultats intermédiaires produits par la phase Map sont combinés et consolidés. Les informations sur chaque ville sont fusionnées pour obtenir les valeurs finales telles que la température maximale, minimale et moyenne. La méthode `mergeResults()` effectue cette agrégation.

### Utilisation des Buffers

L'utilisation des buffers est cruciale pour optimiser la mémoire et les performances lorsque l'on travaille avec des ensembles de données volumineux. Dans notre implémentation :

1. **Batch Processing** :
   - Les lignes du fichier CSV sont lues par morceaux (`batches`). Chaque morceau contient un nombre fixe de lignes défini par la constante `BATCH_SIZE`. Ce découpage en batches permet de traiter les données en parallèle et réduit l'utilisation de la mémoire en limitant la quantité de données chargées en mémoire à un moment donné.
   - Lorsque un batch est complet, il est traité par un thread séparé. Les résultats de chaque thread sont collectés dans une liste de `Future` objets.

2. **Memory Optimization** :
   - **`List<String>` pour les batches** : Chaque batch est stocké dans une `ArrayList` pour être traité. Une fois le batch traité, il est effacé pour libérer de la mémoire.
   - **`ConcurrentHashMap` pour les résultats finaux** : Cette structure de données permet une gestion efficace de la mémoire en permettant des mises à jour concurrentes.

### Code Exemple

```java
private static Map<String, double[]> processBatch(List<String> batch) {
    Map<String, double[]> result = new HashMap<>();
    for (String line : batch) {
        String[] data = line.split(",");
        if (data.length < 3 || !isNumeric(data[2])) {
            continue;
        }
        String city = data[1];
        double temperature = Double.parseDouble(data[2]);
        result.merge(city, new double[]{temperature, temperature, temperature, 1},
            (existingTemps, newTemps) -> {
                existingTemps[0] = Math.max(existingTemps[0], newTemps[0]);
                existingTemps[1] = Math.min(existingTemps[1], newTemps[1]);
                existingTemps[2] += newTemps[2];
                existingTemps[3] += newTemps

