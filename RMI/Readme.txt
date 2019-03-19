1) Compiler le projet en naviguant vers le dossier TP1.2. Ouvrir un terminal et exécuter les commandes « ant clean » puis « ant » pour compiler.
2) Se positionner dans le dossier "bin", lancer la commande « rmiregistry & ».
3) Retourner à la racine du projet TP1.2 puis lancer le « nameServer » avec le script « ./nameserver.sh ». Le « nameServer » affichera son IP si tout est correct.
4) Lancer un/les serveur(s) de calcul.Pour ce faire, naviguer à la racine du projet (TP1.2) et lancer un serveur en utilisant l'IP fourni par le « nameserver ». ex : ./server.sh -m 0 -q 4 -p 5002 IP_nameServer  avec :
-m le taux de malice en %
-q la capacité 
-p le port
5) Pour lancer le répartiteur, on doit se retrouver dans la racine du TP1.2 puis on utilise la commande  : « ./client.sh <operationfilename> (-u ou -s) » en l'absence des paramètres « u » ou « s », le mode sécurisé  est sélectionné par défaut.

********************************************************************************************************************************************

Test de performance mode sécurisé :
 
Reprendre les étapes 1 - 3 ;
A l'étape 4 pour chaque serveur, utiliser la commande :  «  ./server.sh -m 0 -q X -p Y IP_nameServer »    avec :
X = Capacité du serveur ( 2,3 ou 4 )
Y= Le numéro de port de serveur.
 A l'étape 5, utiliser la commande : «  ./client.sh <operationfilename>" –s » (ici il faut spécifier l’argument « –s » car il s’agit du mode sécurisé.
 
 
 ********************************************************************************************************************************************
Test de performance mode non sécurisé :

* Trois serveurs de bonne foi :

Reprendre les étapes 1 – 3.

A l'étape 4 pour chaque serveur, utiliser la commande : «  ./server.sh -m 0 -q 5 -p Y IP_nameServer »     avec :
	Y= Le numéro de port de serveur.
	
A l'étape 5, utiliser la commande : «  ./client.sh <operationfilename>" –u » (ici il faut spécifier l’argument « –u » car il s’agit du mode non sécurisé.

* Un serveur malicieux 40% du temps, deux autres serveurs de bonne foi.
Reprendre les étapes 1 – 3.

A l'étape 4 :
	Le premier serveur, utiliser la commande : «  ./server.sh -m 40 -q 5 -p Y IP_nameServer »   . 
	Les 2 autres serveurs utiliser la commande «  ./server.sh -m 0 -q 5 -p Y IP_nameServer » avec :
		Y= Le numéro de port de serveur.
		
A l'étape 5, utiliser la commande : «  ./client.sh <operationfilename>" –u » (ici il faut spécifier l’argument « –s » car il s’agit du mode non sécurisé.

* Un serveur malicieux 85% du temps, deux autres serveurs de bonne foi.

Reprendre les étapes 1 – 3.

A l'étape 4 : 
	Le premier serveur, utiliser la commande : «  ./server.sh -m 85 -q 5 -p Y IP_nameServer ». 
	Les 2 autres serveurs utiliser la commande «  ./server.sh -m 0 -q 5 -p Y IP_nameServer » avec :
		Y= Le numéro de port de serveur.
		
A l'étape 5, utiliser la commande : «  ./client.sh <operationfilename>" –u » (ici il faut spécifier l’argument « –s » car il s’agit du mode non sécurisé.

