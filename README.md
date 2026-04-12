Ce projet est une implémentation complète du jeu de dames internationales (plateau 10×10) en Java, avec une interface graphique réalisée en Swing. Le jeu gère les déplacements, les prises obligatoires, les rafles, la promotion en dame, l’historique des coups et la détection automatique de fin de partie.

Fonctionnalités :
Plateau officiel 10×10 avec cases noires/blanches.
Pions blancs et noirs, promotion automatique en dame.
Déplacements simples et déplacements multiples (rafles).
Prises obligatoires respectées automatiquement.
Historique des coups affiché en temps réel.
Détection du vainqueur et possibilité de relancer une partie.
Interface graphique simple et lisible.

Description des classes : 
"""

Main:
Point d’entrée du programme. Lance l’interface graphique via Swing.

Jeu:
Fenêtre principale du jeu.
Elle contient :

le plateau au centre,
l’historique des coups à droite,
les labels de lignes et colonnes.

Plateau:
La logique du jeu se trouve ici :
création du plateau et des cases,
placement initial des pions,
gestion des clics et des déplacements,
détection des prises et rafles,
promotion en dame,
changement de tour,
détection de fin de partie.

C’est la classe la plus importante du projet.

Case:
Représente une case du plateau.
Elle stocke :
sa position (ligne, colonne),
un pion éventuel,
son état (sélectionnée ou non),
son affichage graphique (pion, dame, contour).

Piece:
Représente un pion ou une dame.
Contient la couleur,
un booléen indiquant si la pièce est une dame.

HistoriquePanel:
Panneau latéral affichant la liste des coups joués.
Chaque déplacement ou prise y est ajouté automatiquement.

"""

Règles implémentées:

Déplacements diagonaux.
Les pions blancs montent, les noirs descendent.
Les dames se déplacent sur plusieurs cases.
Les prises sont obligatoires.
Les rafles sont gérées automatiquement.
Promotion en dame en atteignant la dernière ligne.
Fin de partie lorsqu’un joueur n’a plus de pions.