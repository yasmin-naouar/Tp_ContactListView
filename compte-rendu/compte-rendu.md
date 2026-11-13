# Compte Rendu — TP Android : ListView de Contacts Modifiable

**Étudiant :** Adem Ouerhani  
**Module :** Travaux Pratiques — Android Kotlin  
**Projet :** ContactsListView  
**Notions abordées :** ListView, ArrayAdapter, mutableListOf, setOnItemClickListener, notifyDataSetChanged()  
**Dépôt GitHub :** https://github.com/ADEM2005373/ContactsListView  

---

## 1. Objectifs du Travail Pratique

Ce travail pratique d'initiation au développement Android avec Kotlin a pour finalité de concevoir une application de gestion de liste modifiable :
1. Afficher une liste de contacts sous forme de chaînes simples dans un composant `ListView`.
2. Réagir à la sélection d'un contact en copiant son libellé dans un `EditText` situé en haut de l'écran.
3. Permettre la modification du contact sélectionné lors du clic sur le bouton **Modifier**, et actualiser l'interface grâce à `notifyDataSetChanged()`.
4. Approfondir la maîtrise du cycle de vie et enrichir les fonctionnalités (ajout, suppression par clic long, contrôle des doublons, tri automatique et survie à la rotation d'écran).

---

## 2. Conception de l'Interface Utilisateur (XML)

L'interface déclarée dans `res/layout/activity_main.xml` repose sur un `LinearLayout` vertical composé de deux sections :
* **Section supérieure (saisie et actions) :** Un `LinearLayout` horizontal comprenant :
  * Un champ de saisie `EditText` (`@+id/etContact`) avec `layout_width="0dp"` et `layout_weight="1"` pour occuper tout l'espace disponible.
  * Un bouton `Button` (`@+id/btnModifier`) pour valider la mise à jour.
  * Un bouton supplémentaire `Button` (`@+id/btnAjouter`) pour insérer de nouveaux contacts.
* **Section inférieure (affichage défilant) :** Un `ListView` (`@+id/lvContacts`) avec `layout_height="0dp"`, `layout_weight="1"` et `choiceMode="singleChoice"` pour mémoriser visuellement la sélection courante.

---

## 3. Implémentation Kotlin et Notions Clés

### 3.1 Gestion des données : `mutableListOf` vs `listOf`
* **`listOf(...)`** produit une collection en lecture seule (`List<T>`). Toute tentative d'affectation indexée (`contacts[i] = nouveauNom`) provoque une erreur de compilation.
* **`mutableListOf(...)`** instancie une `MutableList<T>`, autorisant les mutations en place (`add`, `set`, `removeAt`).

### 3.2 Le rôle central de l'adaptateur (`ArrayAdapter`)
La `ListView` ne peut pas interpréter directement les structures de données en mémoire. L'`ArrayAdapter` sert d'intermédiaire en créant et recyclant un `TextView` pour chaque élément :

```kotlin
adapter = ArrayAdapter(
    this,
    android.R.layout.simple_list_item_activated_1,
    contacts
)
lvContacts.adapter = adapter
```
Le layout `simple_list_item_activated_1` offre l'avantage de surligner automatiquement l'élément sélectionné lorsque `setItemChecked(position, true)` est invoqué.

### 3.3 Importance capitale de `notifyDataSetChanged()`
La modification de la liste sous-jacente (`contacts[positionSelectionnee] = nouveauNom`) met à jour la mémoire mais ne déclenche aucun rafraîchissement visuel. L'appel explicite à `adapter.notifyDataSetChanged()` notifie l'observateur interne de l'adaptateur pour redessiner immédiatement les vues de la `ListView`.

---

## 4. Exercices « Pour aller plus loin » Réalisés

1. **Bouton « Ajouter »** : Permet d'insérer le texte de l'EditText en queue de liste après validation.
2. **Suppression sur appui long (`setOnItemLongClickListener`)** : Déclenche une boîte de dialogue `AlertDialog` demandant confirmation avant de retirer le contact.
3. **Contrôle d'unicité (Doublons)** : Empêche la modification ou l'ajout si le nom existe déjà dans la liste (recherche insensible à la casse).
4. **Tri alphabétique automatique** : Exécution de `contacts.sort()` après chaque insertion ou modification.
5. **Survie à la rotation d'écran (`onSaveInstanceState`)** :
   * *Problématique :* Par défaut, la rotation détruit puis recrée l'activité, réinitialisant les variables membres à leur valeur d'origine.
   * *Solution appliquée :* Sauvegarde de la liste dynamique dans le `Bundle` (`putStringArrayList`) et restauration dans `onCreate`.

---

## 5. Scénarios de Test et Validation

| Test réalisé | Comportement attendu | Résultat obtenu | Statut |
| :--- | :--- | :--- | :---: |
| **Lancement** | 8 contacts affichés, aucun élément sélectionné | Affichage ordonné, champ vide | Conforme |
| **Clic sur « Sarra Trabelsi »** | Nom copié dans l'EditText, ligne surlignée | Texte copié, curseur positionné à la fin | Conforme |
| **Modification en « Sarra Ben Salah »** | Remplacement dans la liste et notification Toast | Contact mis à jour et liste re-triée | Conforme |
| **Champ vide + Modifier** | Icône d'erreur sur l'EditText | Indicateur d'erreur actif | Conforme |
| **Clic Modifier sans sélection** | Toast « Sélectionnez d'abord un contact » | Toast affiché, aucun crash | Conforme |
| **Ajout de contact** | Nouveau nom inséré et trié | Insertion et rafraîchissement validés | Conforme |
| **Suppression appui long** | Dialogue de confirmation et suppression | Retrait effectif après confirmation | Conforme |
| **Doublon** | Message d'avertissement et refus | Rejet validé | Conforme |

---

## 6. Captures d'écran

Les captures du dossier `captures/` attestent des tests :
* `1_ecran_initial.png` : Écran initial au lancement.
* `2_selection_contact.png` : Sélection d'un élément reporté dans le champ de saisie.
* `3_contact_modifie.png` : Confirmation de modification avec toast et mise à jour.
* `4_erreur_nom_vide.png` : Affichage de l'erreur en cas de saisie vide.
* `5_toast_aucune_selection.png` : Toast informatif si aucune ligne n'est sélectionnée.
