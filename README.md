# Feature toggle service

 [![CircleCI](https://circleci.com/gh/johnowl/owl-toggle-server.svg?style=svg)](https://circleci.com/gh/johnowl/owl-toggle-server)
 [![codecov](https://codecov.io/gh/johnowl/owl-toggle-server/branch/master/graph/badge.svg)](https://codecov.io/gh/johnowl/owl-toggle-server) 

## What is a feature toggle?

It's a way to turn on and off certains portion of your code. In real life it can be an `if` expression to check a flag, if it returns `true` you do something and if it returns `false` you do something else. This project is were you store your flags, but it's not just that, you can write complex rules based on variables that are stored here too!

## Managing feature toggles

Example of a simple feature toggle to enable or disable a feature called profile:

    {
        "toggleId": "should_show_profile",
        "enabled": true,
        "rules": ""
    }

There is a set of APIs to manage this rules:

| Verb | Path | Description | 
| --- | --- | --- |
| GET | /toggles/ | Get all toggles. |
| POST | /toggles/ | Save new toggle or return error if a toggle with the same Id already exists. |
| GET | /toggles/{toggleId} | Get a toggle by it's id. |
| PUT | /toggles/{toggleId} | Update a toggle or return error if the toggle doesn't exsit. |
| DELETE | /toggles/{toggleId} | Remove a toggle by its id |

## Variables

You can add variables to use in your rules and turn your toggles much smarter. You can turn some feature toggles enabled for certains versions of your client. For example:

    {
        "toggleId": "should_show_profile",
        "enabled": true,
        "rules": "Number(appVersion) > 3"
    }

To store your variables, there is a API for that

| Verb | Path | Description | 
| --- | --- | --- |
| POST | /variables/{userId} | Save variables associated to a user. |
| GET | /variables/{userId} | Get variables associated to a user or return error if it doesn't exist. |

A variable can be a number, a string or a list of strings or numbers (in this first version only integer numbers are supported), see some examples:

    {
      "appVersion": 1,
      "roles": ["beta", "admin"],
      "status": "active"
    }

## How to check if a feature toggle is enabled

Guess what? We have some APIs to do this too! 

| Verb | Path | Description | 
| --- | --- | --- |
| POST | /toggles/{toggleId}/check | In this API you have too send the variables in the body. It will return `true` ou `false` in the response body. |
| GET | /toggles/{toggleId}/check/{userId} | Here you need to have variables already stored. It will return `true` ou `false` in the response body. |

## API Documentation

https://app.swaggerhub.com/apis/johnowl/api-documentation/1.0
