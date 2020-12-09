Setup:
W keycloak w ustawieniach client -> magazyn:
Valid Redirect URIs: *
Web Origins: *
(oba ustawiamy na gwiazdkę)

Potrzebne zainstalowane są:
Node.js: https://nodejs.org/en/
Yarn: https://classic.yarnpkg.com/en/docs/install

W folderze głównym projektu wykonujemy komendę:
yarn install 

Serwer ze stroną uruchamiamy
yarn start

Strona defaultowo uruchamia się na localhost:3000

Aby się zalogować trzeba w keycloak'u utworzyć usera w realmie magazyn
