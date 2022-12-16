Auteurs : BRACQUIER Benjamin, SOLER Lilian
# Projet Java : Producteurs/Consommateurs 

# Buffers	de	production-consommation
Un buffer de communication de type producteur-consommateur permet à des threads
d’échanger des messages via un buffer borné, ce buffer étant généralement implanté avec
un tableau de taille fixe (comme illustré dans la figure ci-après).
Ce type de structure de données est très utilisé dans les applications concurrentes.
Notamment, les serveurs applicatifs tels que les serveurs Web multi-threadés utilisent un tel
buffer : les requêtes client arrivant du réseau sont déposées dans ce buffer, elles sont ensuite
consommées par des threads qui vont les traiter en parallèle.
Les threads qui déposent des messages sont appelés des producteurs, et ceux qui consomment
les messages des consommateurs. Le nombre de producteurs peut être très différent du
nombre de consommateurs. De manière générale, on a 4 schémas possibles : MPMC (multiple
producteurs et multiples consommateurs), SPMC (un producteur et de multiples
consommateurs), MPSC (de multiples producteurs et un seul consommateur), SPSC (un seul
producteur et un seul consommateur). On trouve des implantations de buffers de productionconsommation optimisées spécifiquement pour ces différents cas. Dans ce projet, on se place
dans le cas général MPMC.

La mise en œuvre d’un buffer producteurs-consommateurs doit être à la fois performante (du
point de vue tu temps d’exécution) et cohérente. Comme les threads producteurs et
consommateurs s'exécutent en parallèle, la manipulation du buffer de messages est donc
synchronisée. Cette synchronisation garantit les invariants suivants :
- Un message n’est consommé qu'une seule fois.
- Les messages sont consommés dans l’ordre dans lequel ils ont été produits.

# Objectif	général du	TP
Ce TP comprend deux objectifs :
- Programmer une classe ProdConsBuffer mettant en œuvre buffer de communication de
type producteur-consommateur.
- Programmer une application de test (classe TestProdCons) qui crée un ensemble de
threads de type producteurs et consommateurs qui utilisent un buffer de type
ProdConsBuffer, avec des variations dans les nombres de messages produits et dans les
temps de production/consommation des messages échangés.
Attention, vous devrez respecter les spécifications que l'on vous donne au niveau des classes
et interfaces, car votre programme doit être contrôlable par un automate programmé.
Vous trouverez ci-après l’interface du buffer de production-consommation à respecter
initialement. Vous êtes libres de définir Message comme une interface ou une classe. 

```java
public interface IProdConsBuffer {
/**
* Put the message m in the buffer
**/
public void put(Message m) throws InterruptedException;
/**
* Retrieve a message from the buffer,
* following a FIFO order (if M1 was put before M2, M1
* is retrieved before M2)
**
public Message get() throws InterruptedException;
/**
* Returns the number of messages currently available in
* the buffer
**/
public int nmsg();
/**
* Returns the total number of messages that have
* been put in the buffer since its creation
**/
public int totmsg();
}
```
Vous aurez à réaliser différentes implémentations de l’interface IProdConsBuffer, ciblant
différents objectifs. Pour chaque objectif, l’ensemble des classes seront placées dans un
package de nom prodcons.v<#>, # variant de 1 à k. Attention, quelque-soit l’objectif, votre
implémentation de la classe IProdConsBuffer doit utiliser un tableau de taille bornée (vous ne
devez pas utiliser une structure de type ArrayList ou équivalent). 

# L’application	de	test	(Classe	TestProdCons)
La classe TestProdCons est la classe principale pour l’application de test. Cette classe crée un
buffer de production-consommation, puis crée un ensemble de threads Producteurs et un
ensemble de threads Consommateurs qui vont manipuler ce buffer en parallèle. Vous
définirez le comportement des threads Producteur dans une classe dédiée (Producer), et de
même pour les consommateurs (Consumer).
Toutes les options de configuration d’une exécution seront obtenues en utilisant la classe
Properties de Java appliquée sur un fichier d’options au format XML stocké dans le package
prodcons. Ce fichier se nomme par défaut options.xml. Vous pourrez vous inspirer du code
suivant pour récupérer les options de configuration: 
```java
 Properties properties = new Properties();
properties.loadFromXML(
 TestProdCons.class.getClassLoader().getResourceAsStream(“options.xml”));
 int nProd = Integer.parseInt(properties.getProperty(“nProd”));
 int nCons = Integer.parseInt(properties.getProperty(“nCons”));
 ```
 
 Le paramètre nProd (resp. nCons) indique le nombre de threads producteurs (resp.
consommateurs). bufSz indique la taille du buffer de production-consommation (on rappelle
que celui-ci est implanté avec un tableau).
Chaque Consommateur devra passer son temps dans une boucle infinie dans laquelle il
consomme un message et le traite. Comme nous sommes dans une application de test, nous
allons simuler le traitement du message par un appel à sleep(). La durée moyenne de
consommation est donnée dans le paramètre consTime.
Chaque Producteur devra produire un nombre aléatoire de messages, compris entre minProd
et maxProd. Comme pour les consommateurs, un producteur suivra une durée moyenne pour
produire un message (prodTime).
Tout Producteur (respectivement tout Consommateur) produit (respectivement consomme) un
seul message à la fois.
Pour faciliter l’analyse de vos exécutions, essayez de placer dans un message les informations
permettant de vérifier les propriétés du buffer de production-consommation. En particulier, si
vous voulez conserver dans un message d’identité du thread producteur, pensez à utiliser la
méthode getId() définie sur la classe Thread, qui retourne un entier identifiant le thread
courant de manière unique dans la JVM.

# Travail à réaliser
## Objectif	1 – Solution directe

### Enoncé : 
Réalisez la classe ProdConsBuffer qui implémente l’interface IProdConsBuffer en appliquant
dans un premier temps le principe de la solution directe vue en TD (wait/notify de java). Pour
cela, définissez les variables caractérisant le probléme (nfull, nempty), le tableau de gardesactions, et dérivez en la solution directe.

Réalisez l’application de test (classe TestProdCons et les classes associées – Producer,
Consumer, ..). Vous prendrez aussi soin de garantir que les processus commutent souvent afin
d'avoir une réelle concurrence. Vous mettrez en place des tests qui permettent de s’assurer des
propriétés attendues du programme.

### Travail realisé :
Ces 2 méthodes seront synchronisées pour éviter les problèmes de concurrence.
Pour le put (la production),on vérifie que le nombre de messages dans le buffer est inférieur à la taille du buffer, si c'est le cas on ajoute le message dans le buffer et on incrémente le nombre de messages dans le buffer, sinon on attend que le nombre de messages dans le buffer soit inférieur à la taille du buffer.

Pour le get (la consommation), on vérifie que le nombre de messages dans le buffer est supérieur à 0, si c'est le cas on récupère le message dans le buffer et on décrémente le nombre de messages dans le buffer, sinon on attend que le nombre de messages dans le buffer soit supérieur à 0.

Pour les tests, on a testé toutes les possibilités qui seront expliqué en annexe.

## Objectif	2 – Terminaison
### Enoncé :
Etendez la solution directe pour faire en sorte que l’application termine automatiquement
lorsque tous les messages produits ont été consommés et traités. Attention, pour satisfaire
cette condition de terminaison, vous devez réfléchir en termes de synchronisation entre vos
threads.

### Travail realisé :
La terminaison est faite dans le main en stoppant les producers puis en vérifiant que le buffer est vide et on stop les consumers ensuite .
On vérifie enfin qu'il n'y ait pas de message de perdu.

## Objectif	3 – Solution	basée	sémaphores
### Enoncé :
Réalisez une version de la classe ProdConsBuffer où vous utilisez la classe Semaphore
fournie par Java pour gérer la synchronisation des producteurs et des consommateurs. Vous
prendrez soin de favoriser le parallélisme au maximum entre les différents threads
(producteurs et consommateurs).

### Travail realisé :
On a crée 3 sémaphores : un pour vérifier que ce n'est pas plein,un pour vérifier que ce n'est pas vide et un dernier pour faire le verrou.
Ainsi pour put, on vérifie que ce n'est pas plein puis on active le verrou, on ajoute le message dans le buffer et on incrémente le nombre de message dans le buffer puis on déverouille le verrou et on libére le sémaphore pour dire que ce n'est pas vide.

Pour get, on vérifie que ce n'est pas vide puis on active le verrou, on récupère le message dans le buffer et on décrémente le nombre de message dans le buffer puis on déverouille le verrou et on libére le sémaphore pour dire que ce n'est pas plein.
### Objectif	4	– Solution	basée	sur	les	Locks	et	Conditions	de	Java [optionnel]
### Enoncé :
Réalisez une version de la classe ProdConsBuffer dans laquelle vous utilisez les sections
critiques conditionnelles de Java (Classe ReentrantLock et interface Condition, package
java.util.concurrent).

### Travail realisé :
Nous n'avons pas developpé cette partie.

## Objectif	5 – Multi-consommation
### Enoncé :
Etendez le comportement de la classe ProdConsBuffer telle que définie à l’objectif 1, de sorte
qu’un consommateur puisse retirer k messages consécutifs dans le buffer (k pouvant être
supérieur à la taille du buffer). Vous étendrez l’interface IProdConsBuffer comme montré cidessous et vous mettrez en oeuvre une solution simple qui fournisse cette fonctionnalité.
```java
public interface IProdConsBuffer {
/**
* Put the message m in the prodcons buffer
**/
public void put(Message m) throws InterruptedException;
/**
* Retrieve a message from the prodcons buffer, following a fifo order
**/
public Message get() throws InterruptedException;
/**
* Retrieve n consecutive messages from the prodcons buffer
**/
public Message[] get(int k) throws InterruptedException;
 …
}
 ```
 Vous utiliserez l’application de tests pour valider cette nouvelle fonctionnalité.

### Travail realisé :
Pour avoir un multi consommation, on a crée une méthode get(int k) qui permet de récupérer k messages consécutifs dans le buffer.
Ainsi cette méthode vérifie que le nombre de messages dans le buffer est supérieur ou égal à k, si c'est le cas on récupère les k messages dans le buffer et on décrémente le nombre de messages dans le buffer de k , sinon on attend que le nombre de messages dans le buffer soit égal à k.

## Objectif	6 – Multi-production
### Enoncé :
Etendez le comportement de la classe ProdConsBuffer telle que définie à l’objectif 1, de sorte
que les producteurs puissent déposer un message en n exemplaires, et que la production et
consommation de ces exemplaires soient synchrones. Pour cela il faut respecter les règles
suivantes:
- un message déposé en n exemplaires doit être retiré par n consommateurs avant de
disparaître du buffer,
- un producteur ne peut poursuivre son activité que lorsque tous les exemplaires du
message ont été retirés.
- un consommateur ne peut poursuivre son activité que lorsque tous les exemplaires
du message ont été retirés.
Autrement dit, un producteur qui dépose un message en 3 exemplaires sera bloqué jusqu’à ce
que 3 consommateurs viennent consommer le message. Les deux premiers consommateurs
seront également bloqués, c’est le dernier consommateur qui vient consommer le message qui
engendrera le déblocage global.
Pour fournir cette fonctionnalité, vous étendrez l’interface IProdConsBuffer comme suit et
vous utiliserez l’application de tests pour valider cette nouvelle fonctionnalité. Si vous le
souhaitez, vous pouvez étendre la classe Message pour mettre en œuvre cette fonctionnalité.
```java
public interface IProdConsBuffer {
/**
* Put the message m in the prodcons buffer
**/
public void put(Message m) throws InterruptedException;
/**
* Put n instances of the message m in the prodcons buffer
* The current thread is blocked until all
* instances of the message have been consumed
* Any consumer of m is also blocked until all the instances of
* the message have been consumed
**/
public void put(Message m, int n) throws InterruptedException;
/**
* Retrieve a message from the prodcons buffer, following a fifo order
**/
public Message get() throws InterruptedException;
/**
* Retrieve n consecutive messages from the prodcons buffer
**/
public Message[] get(int n) throws InterruptedException;
…
}
```

### Travail realisé :
Nous n'avons pas developpé cette partie.

## Annexe : 

### Utils :
Ce package contient les classes Consumer/Producer, Message, IProdConsBuffer et Print(print prenant texte et un boolean pour savoir si on veut afficher ou non) ainsi que les fichiers options(short, normal, long).

### Main 
Ce programme est une application de producteur-consommateur qui utilise différentes implémentations de buffer. La méthode main charge des propriétés à partir d'un fichier XML, puis affiche ces propriétés. Ensuite, elle crée une instance de buffer en utilisant la classe spécifiée dans l'argument packageName.

Ensuite, la méthode main crée des producteurs et des consommateurs en utilisant les propriétés du fichier XML. Elle attend que les producteurs finissent, puis vérifie que le buffer est vide. Si le buffer n'est pas vide, la méthode attend pendant 50 ms et vérifie à nouveau jusqu'à ce que le buffer soit vide ou que le nombre maximal d'itérations ait été atteint. Ensuite, la méthode arrête les consommateurs et vérifie qu'il n'y a pas de messages manquants ou en double dans le buffer. Si tout est en ordre, la méthode renvoie 0, sinon elle renvoie 1.

### Test de la classe ProdConsBuffer

La classe TestProdConse exécute une suite de tests pour un système qui a plusieurs implémentations (prodcons.v1, prodcons.v2, prodcons.v3, prodcons.v5). La méthode de test exécute plusieurs tests pour chaque implémentation, en utilisant trois fichiers d'entrée (options-short, options, options-long). Le nombre de tests exécutés pour chaque fichier d'entrée est déterminé par NTESTS.

Pour chaque test, la méthode de test appelle Main.main avec le fichier d'entrée et le nom de l'implémentation en tant qu'arguments, et recueille le nombre d'erreurs renvoyées par Main.main. La méthode de test calcule ensuite le taux d'erreur et le taux d'erreur par test pour chaque fichier d'entrée, et affiche les résultats. Si un test a des erreurs, la méthode de test affiche le taux d'erreur moyen et le taux d'erreur moyen par test pour tous les fichiers d'entrée. Si tous les tests ont réussi, la méthode de test affiche un message indiquant que tous les tests ont réussi.

La méthode principale appelle la méthode de test avec les arguments de ligne de commande et sans nom d'implémentation. La méthode displayTime calcule et affiche le temps nécessaire pour exécuter tous les tests.

### Script launchNTests.sh

Le script launchNTests.sh exécute n fois le programme TestProdCons. Le script prend en argument le nombre de tests à exécuter. Tous les éxécution s'ouvre dans des tabs d'un gnome terminale.

Remarque: il faut modifier le lancement du programme TestProdCons selon votre environnement.

TODO: mettre en place un environnement de test sous docker.

