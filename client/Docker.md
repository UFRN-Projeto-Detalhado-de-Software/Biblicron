sudo docker build --tag angular-client .
sudo docker run --name angular-client -p 4200:4200 -v $(pwd):/angular-client -it angular-client
