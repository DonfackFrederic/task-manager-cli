# Task Manager CLI

Gestionnaire de tâches en ligne de commande, développé en **Java 17** dans le cadre d'un projet pédagogique visant à mettre en pratique les principes de conception, de qualité logicielle, de tests et d'architecture d'une application Java.

> 🚧 **Projet en cours de développement** — voir la section [Avancement](#avancement).

## Fonctionnalités

* [x] Ajouter une tâche
* [x] Supprimer une tâche
* [x] Marquer une tâche comme terminée
* [x] Lister les tâches
* [x] Sauvegarder et charger les tâches au format JSON
* [x] Mode interactif
* [x] Mode one-shot
* [x] Journalisation avec Log4j2
* [x] Rotation automatique des fichiers de logs
* [x] Stockage indépendant du répertoire courant
* [x] Gestion des chemins adaptée à Linux, macOS et Windows
* [ ] ...

## Prérequis

* Java 17+
* Maven 3.9+

## Installation

Cloner le dépôt :

```bash
git clone <url-du-depot>
cd task-manager-cli
```

Compiler et installer le projet :

```bash
mvn clean install
```

La JAR générée est disponible dans :

```text
target/task-manager-cli.jar
```

## Utilisation

### Mode interactif

L'application peut être lancée sans argument :

```bash
java -jar target/task-manager-cli.jar
```

Une interface interactive permet ensuite d'exécuter les commandes :

```text
taskcli> add "Apprendre Spring Boot"
taskcli> list
taskcli> done 12345
taskcli> delete 12345
```

Utiliser `help` pour afficher la liste des commandes disponibles et `exit` pour quitter l'application.

### Mode one-shot

Les commandes peuvent également être exécutées directement depuis le terminal :

```bash
java -jar target/task-manager-cli.jar add "Apprendre Spring Boot"
java -jar target/task-manager-cli.jar list
java -jar target/task-manager-cli.jar done 12345
java -jar target/task-manager-cli.jar delete 12345
```

L'application peut également être installée comme commande système afin de pouvoir utiliser directement :

```bash
taskcli add "Apprendre Spring Boot"
taskcli list
taskcli done 12345
```

## Persistance des données

Les tâches sont sauvegardées au format JSON.

Le fichier de données n'est pas stocké dans le répertoire courant ni à côté de la JAR. Un répertoire utilisateur approprié est déterminé automatiquement selon le système d'exploitation.

| Système | Répertoire des données                   |
| ------- | ---------------------------------------- |
| Linux   | `~/.local/share/taskcli/`                |
| macOS   | `~/Library/Application Support/taskcli/` |
| Windows | `%LOCALAPPDATA%\taskcli\`                |

La résolution des chemins est centralisée dans la classe `AppPaths`.

Cette approche permet notamment d'exécuter `taskcli` depuis n'importe quel répertoire sans déplacer ou recréer les données de l'application.

## Logs

Les logs applicatifs sont gérés avec **Log4j2**.

* Niveau `INFO` et supérieur affiché en console
* Logs persistants dans un répertoire utilisateur dédié
* Rotation automatique par date
* Rotation supplémentaire lorsque le fichier dépasse 5 Mo
* Maximum de 7 fichiers de rotation conservés
* Fichiers de logs exclus du contrôle de version

Le répertoire des logs est déterminé automatiquement par `AppPaths` et transmis à Log4j2 via une propriété système.

| Système | Répertoire des logs            |
| ------- | ------------------------------ |
| Linux   | `~/.local/state/taskcli/`      |
| macOS   | `~/Library/Logs/taskcli/`      |
| Windows | `%LOCALAPPDATA%\taskcli\logs\` |

## Tests

Les tests automatisés sont exécutés avec Maven :

```bash
mvn test
```

Les tests couvrent notamment les principales fonctionnalités du service de gestion des tâches et de la persistance JSON.

## Couverture de tests

La couverture de code est analysée avec **JaCoCo** :

```bash
mvn test jacoco:report
```

Le rapport est généré dans :

```text
target/site/jacoco/index.html
```

## Architecture

Le projet utilise une architecture séparant les principales responsabilités de l'application :

```text
com.frederic.taskmanager
├── controller
├── service
├── repository
├── model
├── exception
└── view
```

Les principales responsabilités sont séparées entre :

* **Controller** : interprétation et gestion des commandes utilisateur
* **Service** : logique métier
* **Repository** : persistance et récupération des tâches
* **Model** : représentation des données
* **View** : affichage dans la console
* **Exception** : gestion des exceptions propres à l'application

La gestion des chemins est centralisée dans `AppPaths` afin de maintenir la portabilité entre les différents systèmes d'exploitation.

La documentation détaillée de l'architecture, des choix techniques et de l'évolution du projet est disponible dans la documentation de planification associée.

## Technologies utilisées

* **Java 17**
* **Maven**
* **Jackson** — sérialisation et désérialisation JSON
* **Log4j2** — journalisation
* **JUnit** — tests automatisés
* **JaCoCo** — couverture de code
* **AsciiTable** — affichage des tâches sous forme de tableau

## Qualité du code

Le projet met l'accent sur plusieurs pratiques de développement logiciel :

* Séparation des responsabilités
* Exceptions métier dédiées
* Tests automatisés
* Documentation Javadoc
* Journalisation structurée
* Persistance des données
* Gestion portable des chemins
* Contrôle de la qualité du code
* Utilisation de Maven pour automatiser la compilation et les tests

## Avancement

Le développement du projet est organisé en plusieurs sprints, chacun introduisant progressivement de nouvelles fonctionnalités et améliorations techniques.

Les objectifs, décisions techniques et travaux réalisés sont suivis dans la documentation de planification du projet.

## Licence

Projet pédagogique personnel.
