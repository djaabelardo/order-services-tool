docker run -d -p 6033:3306 --name=docker-mysql --env="MYSQL_ROOT_PASSWORD=password" --env="MYSQL_PASSWORD=password" -

docker-compose up --build