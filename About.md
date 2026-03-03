# Project 'springtimes'

A springboot backend for storing "time series data".

Specifically numeric (decimal) datapoints for specific instants tagged with a category (string).

Originally intended for both for (potential) personal use but mainly as a research prototype / point of comparison / portfolio thing.

## Base design - rationalesimplicity of bootstrapping:
- postgres for data storage - backend and db connected to and exposed by docker-compose.yaml
- auth via JWT and without granular access controls. 
- TODO: an easy way to create a valid JWT
- Config of secrets via ENV var (the docker-compose will have to pass them through)
- UUIDs or similar for database primary keys and such
- simple crud interfaces where appropriate but also 
- endpoints to get datapoints for chunked timeperiods (like: years, months)



