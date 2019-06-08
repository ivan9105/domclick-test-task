`docker-compose up -d`

DOCUMENT API

go to http://localhost:5601/app/kibana#/dev_tools/console?_g=()

create index 

```
PUT domclick
{
    "settings" : {
        "index" : {
            "number_of_shards" : 3, 
            "number_of_replicas" : 2 
        }
    }
}
```

get info about index

```
GET /domclick
```

put index mapping

```
PUT domclick/user_data/_mapping?include_type_name=true
{
  "user_data": {
    "properties": {
      "firstName": {
        "type": "text"
      },
      "lastName": {
        "type": "text"
      },
      "middleName": {
        "type": "text"
      },
      "tags": {
        "type": "nested"
      },
      "accounts": {
        "type": "nested"
      },
      "created_date": {
        "type":   "date",
        "format": "yyyy-MM-dd"
      }
    }
  }
}
```

get mapping info

```
GET /domclick/user_data/_mapping?include_type_name=true
```

put some data 

```
PUT /domclick/user_data/1
{
  "firstName": "Иван",
  "lastName": "Иванов",
  "middleName": "Иванович",
  "tags": [
    {
      "value": "пользователь"
    },
    {
      "value": "контент-менеджер"
    },
    {
      "value": "сервис-менеджер"
    }
  ],
  "accounts": [
    {
      "balance": 15000.43
    },
    {
      "balance": 345
    }
  ],
  "createdDate": "2018-01-01"
}
```

search data 

```
GET /domclick/user_data/_search
{
  "query": {
    "bool": {
      "must": [
        { "match": { "firstName": "Иван" }}
      ]
    }
  }
}

GET /domclick/user_data/_search
{
  "query": {
    "nested": {
      "path": "tags",
      "query": {
        "bool": {
          "must": [
            { "match": { "tags.value": "пользователь" }}
          ]
        }
      }
    }
  }
}

GET /domclick/user_data/_search
{
  "query": {
    "bool": {
      "must": [
        {
          "nested": {
            "path": "tags",
            "query": {
              "bool": {
                "must": [
                  {
                    "match": {
                      "tags.value": "пользователь"
                    }
                  }
                ]
              }
            }
          }
        },
        { "match": { "firstName": "Иван" }}
      ]
    }
  }
}

```

SEARCH API

You must "escape" russians symbols in request to api
I use https://www.freeformatter.com/java-dotnet-escape.html#ad-output

```
curl -X GET "http://localhost:9200/domclick/user_data/_search?q=firstName:\u0418\u0432\u0430\u043D"
```

with request body 

```
curl -X GET "http://localhost:9200/domclick/user_data/_search" -H "Content-Type: application/json" -d "{\"query\":{\"bool\":{\"must\":[{\"match\":{\"firstName\":\"\u0418\u0432\u0430\u043D\"}}]}}}"
```


