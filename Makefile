include .env
export $(shell sed 's/=.*//' .env)

Docker_path := ./Docker

# ...
