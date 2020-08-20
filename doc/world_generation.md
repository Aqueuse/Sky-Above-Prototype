#Architecture SWING


* Window
  * frame Menu
    * Panel Container (card layout)
      * Panel global (menu d'accueil)
      * Panel CreerNouveau
      * Panel LoadFile
      * Panel Options
           * Quitter (Confirm Dialog)
  *  frame Simulation


# Generation de mondes

##GenerateLandscapes

Il génère les ADN des zones. Une chaine de 200 nombres entiers précédée
de trois lettres : le type de zones.

Le resultat est stoké dans un fichier CSV dédié avec une id unique.

## caratypes.csv

Ils contiennnent les types de paysages générables et leurs caractéristiques (hauteur maximale, facteur zone (niveau de reliefs), nom du pack de textures). Cette variété de caractéristiques à pour but de permettre la création de zones très variées, du désert au canyon en passant par la montagne et la plaine.

## types.CSV / Oceans.CSV


Ils continennent seulement la liste de tous les types et permettant un brassage rapide lors de la génération du monde.

## A propos des oceans

Il y en a un nombre minimum et ils font une taille minimum. L'objectif est que le monde soit séparée en grosses zones distinctes. Les oceans ont également leurs types possibles et il est prévu à terme de pouvoir les explorer.

# le moteur à ARN (ARN engine)

## Principes généraux

Le moteur permet de décompresser l'ADN d'une zone pour recréer exactement la même zone que la dernière fois. Math.random et seed() sont appliqué à chaque chiffre jusqu'à obtention de la longueur de chaine désirée.

## Parse ARN

Il transforme la chaine de 200 chiffres entiers en une chaine de la taille de la zone. La taille de la zone est retrouvable aisément au début de chaque ADN.

Cet ARN est ensuite analysé pour trouver le nombre de reliefs et leur hauteur et position en fonction de critères précis. Il faut par exemple que chaque zone commence et finisse à l'altitude 0. La taille des reliefs ne doit pas non plus dépasser la hauteur maximale du type.

Une fois les reliefs et l'ARN connu, il est possible de peupler matrice Zone, qui contient toutes les coordonnées X et Y de chaque zone avec l'index de la tuile utilisée dans chaque cas en fonction de la texture choisie.