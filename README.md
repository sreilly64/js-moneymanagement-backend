# Money Bee Backend

Backend API for a fintech money management application.

# Running locally

1. Clone project and navigate to root directory where the project was cloned.

```none
mkdir /Users/$(your_username)/Dev/
cd /Users/$(your-username)/Dev/
git clone https://github.com/sreilly64/js-moneymanagement-backend.git
cd js-moneymanagement-backend
```

2. Ensure Postgres database is running.

```none
psql
# CREATE DATABABASE jsmoney
```

3. Build and test.

```none
./mvnw clean package
```

4. Run project locally.

```none
export DB_URL=jdbc:postgresql://localhost:5432/jsmoney
export DB_UN=postgres
export DB_PW=password
./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-Dspring.profiles.active=cloud"
```

# Deployment 

##  Pre-Deployment

1. [Create heroku account](https://signup.heroku.com/)
2. [Download heroku cli](https://devcenter.heroku.com/articles/heroku-cli#download-and-install) and follow instructions on the page.
3. Create database service.
4. Connect to DB from local client (optional).

### Create heroku app

User the cli to create your cloud application

```
cd ~/js-moneymanagement-backend
$ heroku create
```

### Add a cloud database

Create a PostgreSQL database

```
heroku addons:create heroku-postgresql
```

Login to https://data.heroku.com/ to view your cloud database.
From there, select your database, navigate to the "Settings" tab and acquire your database credentials: connection URL, username, and password.


### Connect to DB from local client.

Use the credentials from the "Add a cloud database" section, to create a database connection using your favorite client.


## Deployment

1. Login to heroku cli

```
$ heroku login
```

2. From the project's root directory, issue the command to push the applications.

```
$ git push heroku master
```

3. Set environment variables.

Set environment variables using the following format:  
cf set-env APP_NAME ENV_VAR_NAME ENV_VAR_VALUE

```
heroku config:set DB_UN=<actual_user_name>
heroku config:set DB_PW=<actual_password>
heroku config:set DB_URL=<actual_url>
```

4. Start application.

```
web npm start
```

## Maintenance / management

### Heroku CLI usage
[Docs](https://devcenter.heroku.com/articles/using-the-cli)


## Troubleshooting

* Tail logs

```
heroku logs -t
```

* Validate app status

```
heroku apps:info
```

* ssh into container

```
heroku run bash -a <APPNAME>
```
