sudo docker build --tag spring .
sudo docker run --name spring_server -p 80:81 -v $(pwd):/server -it spring
