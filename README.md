# ContactsListView — TP Android (Kotlin & XML)

Application Android d'initiation à l'utilisation des **ListView**, des **ArrayAdapter** et du rafraîchissement d'interface via **notifyDataSetChanged()**.

## 📱 Fonctionnalités
- Affichage d'une liste de contacts prédéfinis via `ArrayAdapter` et `simple_list_item_activated_1`.
- Sélection d'un élément au clic, surlignage visuel et transfert dans le champ de saisie supérieur (`EditText`).
- Modification sécurisée du contact sélectionné avec validation d'entrée (champ non vide, contrôle de sélection active).
- Mise à jour instantanée de l'interface graphique grâce à `notifyDataSetChanged()`.
- Validation également possible par la touche **Terminé** (`IME_ACTION_DONE`) du clavier virtuel.

### 🌟 Bonus réalisés (« Pour aller plus loin ») :
1. **Bouton Ajouter** : Ajout dynamique de nouveaux contacts.
2. **Suppression sur appui long** : Boîte de dialogue `AlertDialog` avec confirmation.
3. **Contrôle d'unicité** : Interdiction des doublons.
4. **Tri alphabétique** : Réorganisation automatique après modification ou ajout.
5. **Survie à la rotation** : Sauvegarde et restauration de l'état dans `onSaveInstanceState`.

---

## 📂 Structure du Répertoire
```
ContactsListView/
├── app/
│   ├── src/main/
│   │   ├── AndroidManifest.xml
│   │   ├── java/com/example/contacts/
│   │   │   └── MainActivity.kt
│   │   └── res/
│   │       ├── layout/activity_main.xml
│   │       └── values/ (strings.xml, colors.xml, themes.xml)
│   └── build.gradle.kts
├── captures/
│   ├── 1_ecran_initial.png
│   ├── 2_selection_contact.png
│   ├── 3_contact_modifie.png
│   ├── 4_erreur_nom_vide.png
│   └── 5_toast_aucune_selection.png
├── compte-rendu/
│   ├── compte-rendu.md
│   └── compte-rendu.pdf
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

---

## 🚀 Compilation et Exécution

1. Ouvrir le projet dans **Android Studio**.
2. Laisser Gradle synchroniser les dépendances.
3. Exécuter avec `Shift + F10` ou en ligne de commande :
   ```bash
   ./gradlew assembleDebug
   ```

---

## 👨‍💻 Auteur
- **Adem Ouerhani**
- Dépôt : [https://github.com/ADEM2005373/ContactsListView](https://github.com/ADEM2005373/ContactsListView)
