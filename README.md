# Opis
V sklopu predmeta Principi programskih jezikov smo se naučili osnove ter naprednejše delo z programskim jezikom **Kotlin**. Implementirali smo grafični vmesnik s pomočjo ogrodja `Jetpack Compose`, nato smo tudi dodali povezavo z podatkovno bazo MySQL, ter napisali `Dao` modele za branje, pisanje, posodabljanje ter brisanje vnosov iz baze.

# Izgled
| ![image](https://github.com/mlukee/kotlin-desktop-app/assets/31586745/23ba6756-0f90-42b2-bc24-60062aa4dab5) |
|:--:|
|*About tab*|

| ![image](https://github.com/mlukee/kotlin-desktop-app/assets/31586745/ba2c1d89-e1e2-45b0-84ab-a0c0a0ce9d9d) |
|:--:|
|*Izpis izdelkov iz baze*|

Kadar uporabnik klikne `Koš` ikonico, se izbrani izdelek izbriše iz seznama ter iz baze. Uporabnik ima tudi možnost urejati izdelek ob kliku na `Svinčnik` ikonico, spremembe so vidne tudi v podatkovni bazi.

| ![image](https://github.com/mlukee/kotlin-desktop-app/assets/31586745/664f8546-6679-42a6-8aca-4aa1602dc282) |
|:--:|
|*Posodabljanje izdelka*|

# Podatkovna baza
Pri ustvarjanju podatkovne baze smo si pomagali z orodjem `MySQL Workbench`. Najprej smo si iz Kotlin razredov izdelali shemo podatkovne baze(EER diagram).
| ![image](https://github.com/mlukee/kotlin-desktop-app/assets/31586745/9a85edb0-3a2c-41c7-824b-46643644958e) |
|:--:|
|*EER diagram*|

Iz EER diagrama smo nato zgenerirali bazo. Pomagali smo si z `Database->Forward Engineer...`. Posameznim stolpcem smo tudi določili ustrezne podatkovne tipe (angl.
datatype) in omejitve.


