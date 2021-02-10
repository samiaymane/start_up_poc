[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![MIT License][license-shield]][license-url]
[![LinkedIn][linkedin-shield]][linkedin-url]



<!-- PROJECT LOGO -->
<br />
<p align="center">
  <a href="https://github.com/samiaymane/start_up_poc">
    <img src="images/logo.png" alt="Logo" width="80" height="80">
  </a>

  <h3 align="center">STARTUP POC</h3>

  <p align="center">
    API de suivi de temps passé sur des projets d'entreprise.
    <br />
    <a href="https://localhost:8080/swagger"><strong>Explorer la doc »</strong></a>
  </p>
</p>



<!-- TABLE OF CONTENTS -->
<details open="open">
  <summary><h2 style="display: inline-block">Sommaire</h2></summary>
  <ol>
    <li>
      <a href="#about-the-project">A propos du projet</a>
      <ul>
        <li><a href="#built-with">Conçu avec</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting strated</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#contributing">Contributtion</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## A propos du projet

[![Product Name Screen Shot][product-screenshot]](https://example.com)

Cette application est une API développée sous Java. Elle permet le suivi de temps de travail des développeurs d'une entreprise.

Voici la liste des fonctionnalités implémentées :

* En tant qu’utilisateur, on peut se connecter via un Login / Password (les profils supportés sont Admin, Manager, User).
* En tant que User, on peut saisir ses temps, à la granularité de la seconde (choix projet + temps).
* En tant que User, on peut éditer un compte-rendu mensuel (export PDF).
* En tant que User, on peut consulter les temps saisis par les Users.
* En tatn que User, on peut supprimer une saisie de temps.
* En tant que Manager, on peut éditer les compte-rendus périodiques des Users qui lui sont rattachés (export PDF).
* En tant que Manager, on peut saisir des projets.
* En tant que Manager, on peut saisir de nouveaux Users qui lui seront rattachés.
* En tant qu’Admin, on peut changer le statut d’un User (Manager / User).
* En tant qu’Admin, on peut changer l’affectation d’un User (changement de Manager).
* En tant qu’utilisateur non authentifié je ne peux pas solliciter l’API (excepté pour m’authentifier).
* En tant qu'Admin ou Manager, on peut supprimer un projet.
* En tant qu'Admin, on peut supprimer un utilisateur.



### Built With

* [SpringBoot](https://spring.io/projects/spring-boot)


<!-- GETTING STARTED -->
## Getting Started

Pour avoir une copie locale du projet, veuillez suivre les étapes suivantes.

### Prerequisites

Il faut installer Java SDK sur votre machine, et avoir un IDE. Celui recommandé est IntelliJ.

### Installation

Clone the repo
   ```sh
   git clone https://github.com/samiaymane/start_up_poc
   ```


<!-- USAGE EXAMPLES -->
## Usage

Voici une liste d'actions pour tester l'api :

* Se connecter à l'api en tant que manager
   ```sh
   METHOD : POST
   BODY : x-www-form-urlencoded
   ```
   ```sh
   http://localhost:8080/login
   ```
   ```sh
   username : manager1@dev.dev
   password : manager1
   ```
* Récupérer l'utilisateur connecté à l'api
   ```sh
   METHOD : GET
   ```
   ```sh
   http://localhost:8080/hello
   ```
* Afficher la liste des users
   ```sh
   METHOD : GET
   ```
   ```sh
   http://localhost:8080/users/all
   ```
* Afficher la liste des projets
   ```sh
   METHOD : GET
   ```
   ```sh
   http://localhost:8080/projects
   ```
* Créer un utilisateur normal
   ```sh
   METHOD : POST
   BODY : JSON
   ```
   ```sh
   http://localhost:8080/users
   ```
   ```sh
   {
	"firstName": "Sami Aymane",
	"lastName": "Elouafkaoui",
	"email": "samiaymane98@gmail.com",
	"password": "sami_98"
   }
   ```
* Créer un projet
   ```sh
   METHOD : POST
   BODY : JSON
   ```
   ```sh
   http://localhost:8080/projects
   ```
   ```sh
   {
	"title": "Poject X",
	"description": "This project is confidential"
   }
   ```
* Affecter un utilisateur à un projet
   ```sh
   METHOD : PATCH
   BODY : JSON
   ```
   ```sh
   http://localhost:8080/projects/{{project_id}}/addUser
   ```
   ```sh
   { 
	"id": {users_id}

   }
   ```
* Se déconnecter
   ```sh
   METHOD : GET
   ```
   ```sh
   http://localhost:8080/logout
   ```
* Se connecter à l'api en tant qu'utilisateur normal
   ```sh
   METHOD : POST
   BODY : x-www-form-urlencoded
   ```
   ```sh
   http://localhost:8080/login
   ```
   ```sh
   username : samiaymane98@gmail.com
   password : sami_98
   ```
* Créer un log d'activité
   ```sh
   METHOD : POST
   BODY : JSON
   ```
   ```sh
   http://localhost:8080/projects/{{project_id}}/logs
   ```
   ```sh
   {
	"start" : "2021-02-05T08:00:00.000Z",
	"end" : "2021-02-09T12:30:00.000Z"
   }
   ```
   ```sh
   {
	"start" : "2021-02-08T14:30:00.000Z",
	"end" : "2021-02-09T17:00:00.000Z"
   }
   ```
   ```sh
   {
	"start" : "2021-02-09T13:30:00.000Z",
	"end" : "2021-02-09T18:00:00.000Z"
   }
   ```
* Exporter le rapport d'activité
   ```sh
   METHOD : GET
   BODY : JSON (optional)
   ```
   ```sh
   http://localhost:8080/exportPDF
   ```
   ```sh
   {
	"startDate" : "2021-02-08",
	"endDate" : "2021-02-12"
   }
   ```
* Se déconnecter
* Se connecter à l'api en tant que admin
   ```sh
   METHOD : POST
   BODY : x-www-form-urlencoded
   ```
   ```sh
   http://localhost:8080/login
   ```
   ```sh
   username : admin@dev.dev
   password : admin
   ```
* Changer le manager d'un user
   ```sh
   METHOD : PATCH
   BODY : JSON
   ```
   ```sh
   http://localhost:8080/users/{{user_id}}/changeRole
   ```
   ```sh
   {
	"action": "UPGRADE"
   }
   ```
* Downgrade un user
   ```sh
   METHOD : PATCH
   BODY : JSON
   ```
   ```sh
   http://localhost:8080/users/{{user_id}}/changeRole
   ```
   ```sh
   {
	"action": "DOWNGRADE"
   }
   ```
* Delete un project
   ```sh
   METHOD : DELETE
   ```
   ```sh
   http://localhost:8080/projects/{{project_id}}
   ```

<!-- CONTRIBUTING -->
## Contributing

The project is private and close to contributions.


<!-- LICENSE -->
## License

No license is attributed to this project.



<!-- CONTACT -->
## Contact

Sami Elouafkaoui - samiaymane98@gmail.com

Project Link: [https://github.com/samiaymane/start_up_poc](https://github.com/samiaymane/start_up_poc)



<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[contributors-shield]: https://img.shields.io/github/contributors/samiaymane/repo.svg?style=for-the-badge
[contributors-url]: https://github.com/samiaymane/repo/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/samiaymane/repo.svg?style=for-the-badge
[forks-url]: https://github.com/samiaymane/repo/network/members
[stars-shield]: https://img.shields.io/github/stars/samiaymane/repo.svg?style=for-the-badge
[stars-url]: https://github.com/samiaymane/repo/stargazers
[issues-shield]: https://img.shields.io/github/issues/samiaymane/repo.svg?style=for-the-badge
[issues-url]: https://github.com/samiaymane/repo/issues
[license-shield]: https://img.shields.io/github/license/samiaymane/repo.svg?style=for-the-badge
[license-url]: https://github.com/samiaymane/repo/blob/master/LICENSE.txt
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: https://linkedin.com/in/samiaymane
