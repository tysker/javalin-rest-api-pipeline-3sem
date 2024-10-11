# Træfik Setup Remote


### Install apache2-utils inside your droplet terminal

```bash
  sudo apt-get install apache2-utils
``` 

### Generate a traefik hashed dashboard login (droplet linux terminal )

```bash
  echo $(htpasswd -nb <your_username> <your_password>) | sed -e s/\\$/\\$\\$/g
```

<img src="./images/encrypted-password.png">


### Clone the repository into your droplet

- remember to push your code to your repository before cloning it into your droplet

### 7. Inside the project set permissions for the acme.json file and folder

```bash
  chmod 600 ./acme
  chmod 600 ./acme/acme.json
```

### 8. Create a .env file at the root of your project and add the following environment variables

```bash
# Lets-encrypt - Digital Ocean
PROVIDER=digitalocean
EMAIL=<your_email>
ACME_STORAGE=/etc/traefik/acme/acme.json
DO_AUTH_TOKEN=<your_digital_ocean_token> (see step 3)

# Traefik
TRAFIK_DOMAIN=traefik.<your_domain>
DASHBOARD_AUTH=<your_traefik_dashboard_login> (see step 4 and 5)

# API
API_DOMAIN=api.<your_domain_name>

# Postgres
POSTGRES_USER=<your_postgres_user>
POSTGRES_PASSWORD=<your_postgres_password>

# Watchtower
REPO_USER=<your_docker_hub_username>
REPO_PASS=<your_docker_hub_password>

# REST API SETUP
CONNECTION_STR=jdbc:postgresql://db:5432/
DB_USERNAME=<your_postgres_user>
DB_PASSWORD=<your_postgres_password>
DEPLOYED=TRUE
SECRET_KEY=super_secret_key (has to include at least 32 characters letters and numbers)
TOKEN_EXPIRE_TIME=1800000 (30 minutes)
ISSUER=<your_domain>

```

## Frontend Deployment

### 1. Create a new file called nginx.conf and add the following code

```bash
server {
    listen       80;
    listen  [::]:80;
    server_name  localhost;

    location / {
        root   /usr/share/nginx/html;
        index  index.html index.htm;
        try_files $uri $uri/ /index.html;
    }
}
```

### 2. Create a new file called Dockerfile and add the following code

```bash
# First stage: build the react app
# FROM tiangolo/node-frontend:10 as build-stage
FROM node:18 as build-stage
WORKDIR /app
COPY package*.json /app/
RUN npm install
COPY . .
RUN npm run build

# Second stage: use the build output from the first stage with nginx
FROM nginx:1.25
COPY --from=build-stage /app/dist/ /usr/share/nginx/html

# Copy the default nginx.conf to get the try-files directive to work with react router
COPY ./nginx.conf /etc/nginx/conf.d/default.conf
```
### 3. Add both files to your frontend root folder (same level as package.json) and push your code to your git repo

**The above requires that you have already set up a github workflow (pipeline) for your frontend project.**

### 4. Exchange the schoolhack image in the docker-compose file with your own image

```bash
  image: <your_docker_hub_username>/<your_docker_hub_repo_name>:<your_docker_hub_tag>
```

**Remember to replace everything related to schoolhack with your own project name**

## How to use it

###  Run Docker

```bash
  docker-compose up -d
```

### Stop Docker

```bash
  docker-compose down
```

### Access Traefik Dashboard through browser

```bash
  traefik.<your_domain>
```

### Access Your Rest Api

```bash
  <your_domain>/<your_api_path> (example: api.3sem.dk/api or restapi.3sem.dk/api)
```

### Reset DB data installation

(-v) // remove volumes
```bash
 docker-compose down -v 
```

```bash
 sudo  rm -rf ./data
```

### Connect your remote database to your local pgAdmin container

```bash
  <your_domain>:5432
```

### Testing the api

- use the http file inside the utility folder to test the api

***

<img src="images/3sem-setup-remote.drawio.png" alt="3 semester local environment setup">
