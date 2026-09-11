# Task Manager CLI

Gestionnaire de tâches en ligne de commande, écrit en Java (projet pédagogique).

> 🚧 Projet en cours de développement — voir la section [Avancement](#avancement).

## Fonctionnalités

- [x] Ajouter une tâche
- [x] Supprimer une tâche
- [x] Marquer une tâche comme terminée
- [x] Lister les tâches
- [x] Sauvegarder / charger les tâches (JSON)

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

## Logs

Les logs applicatifs sont gérés par Log4j2 :
- Niveau INFO et supérieur affiché en console pendant l'exécution
- Historique complet conservé dans `logs/app.log` (non versionné, voir `.gitignore`)
- Rotation automatique : nouveau fichier par jour ou au-delà de 5 Mo, 7 fichiers conservés

## Architecture

Voir le détail de l'architecture (MVC léger, packages, format JSON) dans la documentation de planification du projet.

## Avancement

Projet structuré selon 5 sprints. Voir le suivi dans le dépôt de planification associé.
