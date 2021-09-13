### Employee and Departments Management System

##### To run system on your localhost yor need to install Docker and follow next comands
cd departments-management <br>
./mvnw install <br>
docker build --tag departments-management:1.0.0 . <br>
cd ..  <br>
cd employee-management <br>
./mvnw install <br>
docker build --tag employee-management:1.0.0 . <br>
cd .. <br>
cd db <br>
docker build --tag database:1.0.0 . <br>
cd .. <br>

docker-compose up -d db <br>
docker ps <br>

docker exec -it [CONTAINER ID] bash <br>
psql -U postgres <br>
postgres-# CREATE DATABASE departments; <br>
postgres-# CREATE DATABASE employees; <br>
postgres-# \q <br>
exit <br>

docker-compose down <br>
docker-compose up -d  <br>
