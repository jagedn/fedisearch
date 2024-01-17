## Fedisearch, a search of Mastodon Users 

- Micronaut
- langchain4j
- Postgresql + PgVector
- Fediverse (Mastodon, Pleroma, ...)


Example of Micronaut + Langchaing4j embedding text

Search every X minutes for users in the Fediverse, extract the bio and generate an embedding vector with it

You can find users using semantic search (i.e. "people how loves cats", "politics", "software python puaggg")

## Build

`./gradlew build`

## Run

`docker-compose up -d`

(will create a Postgresql database with PgVector extension enabled)

`./gradlew run`

wait a few seconds ... and open a browser:

http://localhost:8080


