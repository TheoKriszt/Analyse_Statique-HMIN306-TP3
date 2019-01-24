# HMIN306 : Evolution et Restructuration
## TP3 & TP4 : Analyse Statique du code, utilisation de Spoon et Identification de modules
------------------------------------------------------------------------------------------

#### Usage
``java -jar monJar.jar chemin_vers_les_sources <min-X>``
où ``min-X`` est utilisé dans la 11ème métrique calculée : *Les classes qui possèdent plus de ``X`` méthodes*


#### Ressources
L'affichage des graphes, via [JGraphX](https://github.com/jgraph/jgraphx), reste "brouillon" 
car il faut généralement replacer les éléments à la main pour obtenir un graphe compréhensible.
Pour aider un peu, j'ai laissé j'ai laissé d'une part un projet "exemple" [ici](lib/SimpleSample) pour tester le projet sur un programme simple.
D'autre part, j'ai laissé [là](res) les résultats réarrangés des rendus graphiques avec : 
+ Sur l'analyse de ce projet même
    + [Les relations d'appels entre les classes](res/callGraph%20introspection.PNG)
    + [Le dendrogramme (arbre de regroupement)](res/MEGA_DENDRO_clean.png)
   
+ Sur l'analyse du projet exemple
    + [Les relations d'appels entre les classes](res/callGraph%20simpleSample.PNG)
    
+ Sur des tests faits pour vérifier l'algorithme de clustering
    + [Un arbre connexe](res/dendroTestComplet.PNG)
    + [Deux arbres non connexes, car les clusters (A-B) et (C-D) présentent un meilleur couplage que s'ils étaient réunis](res/dendroTestDisjoint.PNG)

 
  
### I -  Analyse du code par l'AST Parser
Se réréfer à la classe [Main](src/fr/kriszt/theo/Main.java) 

+ Les métriques du code sont calculées et affichées dans la console.

### II - Graphe d'appel
Toujour avec la classe [Main](src/fr/kriszt/theo/Main.java)
+ Pendant l'analyse, des [Relations](src/fr/kriszt/theo/relations/Relation.java) sont crées.
    Les Relations associent deux classes et ont un poids (le nombre d'appels de méthodes entre les deux classes)  
+ Les différents graphes (Appels entre les classes, une autre vue complémentaire pour ces appels et le graphe de couplage) sont affichés ensuite 
### III - Spoon
Se réréfer à la classe [CallGraphBySpoon](src/fr/kriszt/theo/spoon/main/CallGraphBySpoon.java)

Le graphe d'appel est retrouvé cette fois grâce à [Spoon](http://spoon.gforge.inria.fr/)

### IV - Graphe de couplage entre classes
La construction du dendrogramme est effectuée par la classe [Dendrogram](src/fr/kriszt/theo/GraphX/Dendrogram.java)
Comme dit plus haut, elle utilise notamment des [Relations](src/fr/kriszt/theo/relations/Relation.java) 
d'où elle identifie les [ClassClusters](src/fr/kriszt/theo/relations/ClassCluster.java) les plus couplés et les lie deux à deux.

Le couplage est arrété si la moyenne des couplages des clusters d'origine est supérieure à la métrique de couplage qu'aurait le clusster généré par cette fusion.

**Exemple :** (A lire de haut en bas)

    (A) <--4--> (B) <--2--> (C)<--6-->(D)  # A et B s'appellent 4 fois, B et C 2 fois, C et D 6 fois
        (A+B; S=4) <--2--> (C+D; S=6)      # A et B fusionnent, leur couplage vaut 4, pour C et D c'est 6
                ~~(A+B; S=2)~~             # Si (A+B) et (C+D) fusionnaient, le cluster restant aurait un couplage de 2
                                           # Le clustering s'arrête donc à la ligne 2            

### V - Mini-Tutoriel sur Spoon 
J'ai proposé un scénario pour présenter l'utilité de Spoon comme outil de refactoring à travers la classe [SpoonTutorial](src/fr/kriszt/theo/spoon/main/SpoonTutorial.java)

Inspiré de [OW2con'18 Spoon: open source library to analyze, rewrite, transform, transpile Java source code](https://www.youtube.com/watch?v=ZZzdVTIu-OY)

Utilise le  [programme d'exemple](lib/SimpleSample) qu'il faut refactorer.

 
##### Scénario :
Lors d'une code review, vous vous rendez-compte qu'une faille dans le code permet à des classes non autorisées de modifier librement des personnes.

Après inspection, il s'avère que l'attribut statique ``allPersons`` de la classe [Person](lib/SimpleSample/company/src/com/company/Person.java) a été laissé public, malgré le fait qu'un getter ``getAllPersons()`` existe déjà.

On se rend compte au passage que le getter encapsule mal le champ et renvoie directement l'objet pointé par ``Person.allPersons`` (et permet donc d'obtenir une référence vers ``allPersons`` qui devrait être privé).

##### Objectifs :
 - Passer le champ ``Person.allPersons`` en private
 - Altérer ``Person.getAllPersons()`` pour qu'il retourne une **copie** de la liste privée de Personnes
 - Modifier dans la classe Main les accès en lecture du champ ``Person.allPersons`` pour appeler à la place le getter dorénavant sûr
 
##### Solution :
+ Avec Spoon et en utilisant les bonnes requêtes, le programme récupère successivement les références vers :
   + Les classes ``Person`` et ``Main`` 
   + La méthode ``getAllPersons()``
   + Le champ ``allPersons``
   
+ La visibilité de ``allPersons`` et passée à ``PRIVATE``
+ Le code de ``getAllPersons()`` est remplacé pour renvoyer ``Person.allPersons.clone()``
+ Les accès en lecture du champ ``allPersons`` au sein de la classe ``Main`` sont remplacés par l'invocation de la méthode ``getAllPersons()``
+ Un commentaire *"TODO : check this auto-generated patch !"* est ajouté à chaque remplacement
    
