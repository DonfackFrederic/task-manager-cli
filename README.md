# Task Manager CLI

Gestionnaire de tâches en ligne de commande, écrit en Java (projet pédagogique).

> 🚧 Projet en cours de développement — voir la section [Avancement](#avancement).

## Fonctionnalités

- [ ] Ajouter une tâche
- [ ] Supprimer une tâche
- [ ] Marquer une tâche comme terminée
- [ ] Lister les tâches
- [ ] Sauvegarder / charger les tâches (JSON)

## Prérequis

- Java 17+
- Maven 3.9+

## Installation

```bash
git clone <url-du-depot>
cd task-manager-cli
mvn clean install
```

## Utilisation

```bash
mvn compile exec:java -Dexec.mainClass="com.frederic.taskmanager.App"
# ou, une fois packagé :
java -jar target/task-manager-cli.jar
```

(Les commandes exactes — `add`, `delete`, `done`, `list` — seront documentées au Sprint 3.)

## Lancer les tests

```bash
mvn test
```

## Couverture de tests (JaCoCo)

```bash
mvn test jacoco:report
# rapport généré dans target/site/jacoco/index.html
```

## Architecture

Voir le détail de l'architecture (MVC léger, packages, format JSON) dans la documentation de planification du projet.

## Avancement

Projet structuré selon 5 sprints. Voir le suivi dans le dépôt de planification associé.
