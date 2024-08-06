# Contribuer au Projet

Merci pour votre intérêt à contribuer à ce projet "How-Fast-Can-You-Parse-1-Billion-of-Weather-Data" ! Voici un guide pour vous aider à participer efficacement.

## Structure du Projet

### Fichiers Principaux

- src/: Contient le code Java pour le traitement des fichiers CSV.
  - CityTemperature.java: Code principal pour traiter le grand fichier CSV et calculer les statistiques de température par ville.

- pom.xml: Fichier de configuration Maven pour gérer les dépendances et configurations du projet.

- src/test/CityTemperatureTest.java: Contient des tests unitaires pour vérifier que les méthodes individuelles du code fonctionnent correctement.

- src/test/CityTemperatureIntegrationTest.java: Contient des tests d'intégration pour vérifier que différentes parties du code fonctionnent ensemble comme prévu.

- .github/workflows/java.yml: Fichier YAML pour l'intégration continue avec GitHub Actions. Ce workflow compile et exécute le code Java, vérifie les fichiers et mesure les performances.

- .gitignore: Contient des règles pour ignorer les fichiers volumineux afin qu'ils ne soient pas inclus dans les commits.

## Répartition des Branches

- main: Branche principale contenant la version stable du code. Toutes les nouvelles fonctionnalités et corrections de bogues doivent passer par cette branche après avoir été testées et validées.

## Bibliothèques Utilisées

- java.io.BufferedReader: Pour lire les fichiers CSV de manière efficace en utilisant un tampon afin de minimiser l'accès au disque.
- java.io.FileReader: Pour ouvrir et lire les fichiers CSV.
- java.util.HashMap: Pour stocker les températures par ville, permettant un accès rapide aux données.
- java.util.Map: Interface utilisée pour stocker des paires clé-valeur représentant les températures par ville.

### Pourquoi Ces Bibliothèques ?
- BufferedReader et FileReader: Fournissent un moyen rapide et simple de lire les fichiers ligne par ligne, ce qui est essentiel pour traiter de grands fichiers CSV.
- HashMap: Permet de stocker et de mettre à jour les températures de manière efficace, offrant une recherche et des mises à jour rapides des données.

## Tests

### Validation Initiale

- GitHub Actions: Utilisé pour exécuter automatiquement des tests unitaires, d'intégration et de bout en bout lors des pushs et des pull requests vers la branche principale. Cette approche garantit que les modifications sont validées avant d'être fusionnées dans la branche principale.

### Types de Tests

- Tests Unitaires: Vérifient que les méthodes individuelles fonctionnent correctement.
- Tests d'Intégration: Assurent que différentes parties du code fonctionnent ensemble comme prévu, en particulier le traitement et la validation des fichiers CSV.

## Intégrité

### Processus d'Intégration

- Validation CI/CD: GitHub Actions valide automatiquement les modifications. Les tests doivent réussir pour que les modifications soient fusionnées dans la branche principale.
- Revue de Code: Les Pull Requests doivent être examinées par des membres de l'équipe pour garantir la qualité et la cohérence du code.

## Comment Contribuer

### 1. Forker le Dépôt
- Forkez ce dépôt en cliquant sur le bouton "Fork" en haut de la page GitHub.

### 2. Cloner Votre Dépôt Forké
- Clonez votre dépôt forké sur votre machine locale :
    sh
    git clone https://github.com/your-username/How-Fast-Can-You-Parse-1-Billion-of-Weather-Data.git
    cd How-Fast-Can-You-Parse-1-Billion-of-Weather-Data
    

### 3. Créer une Branche
- Créez une nouvelle branche pour vos modifications :
    sh
    git checkout -b your-branch-name
    

### 4. Apporter des Modifications
- Apportez les modifications nécessaires aux fichiers suivants :
  - pom.xml: Modifiez les dépendances ou configurations si nécessaire.
  - src/main/CityTemperature.java: Apportez des améliorations ou corrections au code principal.
  - src/test/CityTemperatureTest.java: Ajoutez ou modifiez les tests unitaires pour vérifier les nouvelles fonctionnalités ou corrections.
  - src/test/CityTemperatureIntegrationTest.java: Ajoutez ou modifiez les tests d'intégration pour vérifier l'interaction entre différentes parties du code.
  - .github/workflows/java.yml: Modifiez le workflow si vous devez ajuster les étapes CI/CD ou ajouter de nouveaux tests.

### 5. Tester Vos Modifications
- Assurez-vous que toutes les modifications sont correctement testées. Vous pouvez exécuter les tests localement en utilisant Maven :
    sh
    mvn test
    

### 6. Committer et Pousser
- Committez vos modifications avec un message descriptif :
    sh
    git add .
    git commit -m "Description des modifications apportées"
    git push origin your-branch-name
    

### 7. Créer une Pull Request
- Allez sur votre dépôt forké sur GitHub et ouvrez une Pull Request vers la branche principale du dépôt d'origine. Décrivez les modifications que vous avez apportées et pourquoi elles sont importantes.

Merci pour votre contribution !
