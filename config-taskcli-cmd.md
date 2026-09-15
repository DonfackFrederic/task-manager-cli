## Installation comme commande système

TaskCLI peut être utilisé directement comme une commande système, ce qui permet d'exécuter les commandes suivantes depuis n'importe quel répertoire :

```bash
taskcli add "Apprendre Spring Boot"
taskcli list
taskcli done 12345
taskcli delete 12345
```

### Linux

Sous Linux, il est possible de créer une commande `taskcli` accessible depuis le terminal.

1. Placer le fichier JAR dans un emplacement dédié, par exemple :

```text
/opt/taskcli/taskcli.jar
```

2. Créer un script `/usr/local/bin/taskcli` :

```bash
#!/bin/bash
exec java -jar /opt/taskcli/taskcli.jar "$@"
```

3. Rendre le script exécutable :

```bash
sudo chmod +x /usr/local/bin/taskcli
```

4. Vérifier que la commande est disponible :

```bash
taskcli
```

ou :

```bash
taskcli list
```

`/usr/local/bin` étant généralement présent dans le `PATH`, la commande peut ensuite être utilisée depuis n'importe quel répertoire.

### macOS

L'application peut également être installée comme commande système sur macOS.

Le principe est similaire à Linux : le JAR est placé dans un répertoire dédié et un script `taskcli` est ajouté à un répertoire présent dans le `PATH`, par exemple `/usr/local/bin`.

La procédure peut toutefois varier selon la configuration du système, notamment entre les Mac Intel et Apple Silicon.

### Windows

Sous Windows, le même principe est possible avec un script `.bat` ou `.cmd`.

Le JAR et le script peuvent être placés dans un répertoire tel que :

```text
C:\Program Files\TaskCLI\
```

Le répertoire doit ensuite être ajouté à la variable d'environnement `PATH`.

La configuration étant différente de Linux et macOS, les commandes d'installation doivent être adaptées à Windows.

> **Remarque :** les méthodes d'installation comme commande système sont spécifiques au système d'exploitation. Elles ne modifient pas le fonctionnement de TaskCLI lui-même : dans tous les cas, les arguments sont transmis à l'application Java de la même manière.
