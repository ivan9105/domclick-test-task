`docker-compose up -d`

if you have problem with vm.max_map_count when "elasticsearch" was start
you can use command

```
sysctl -w vm.max_map_count=524288
```

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
      "createdDate": {
        "type":   "date",
        "format": "yyyy-MM-dd"
      },
      "role": {
        "type":  "keyword"
      },
      "lastLoginDateTime": {
        "type":   "date",
        "format": "yyyy-MM-dd HH:mm:ss"
      },
      "address": {
        "properties": {
          "unstructured": {
            "type": "text"
          },
          "region": {
            "type": "text"
          },
          "city": {
            "type": "text"
          },
          "cityType": {
            "type": "text"
          },          
          "street": {
            "type": "text"
          },
          "house": {
            "type": "integer"          
          },
          "houseType": {
            "type": "text"          
          },          
          "block": {
            "type": "text"
          },
          "flat": {
            "type": "integer"
          },
          "flatType": {
            "type": "text"
          }         
        }
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
  "createdDate": "2018-01-01",
  "role": "RCIK",
  "lastLoginDateTime": "2018-01-01 10:00:00",
  "address": {
    "unstructured": "Самара Ново-Садовая дом 44 строение 2",
    "region": "Самара",
    "city": "Самара",
    "cityType": "г",
    "street": "Ново-Садовая",
    "house": 4,
    "houseType": "д",
    "block": "строение 2",
    "flat": 12,
    "flatType": "кв"
  }
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

AGGREGATIONS

AVG

```
GET /domclick/user_data/_search?size=0
{
   "query":{
      "match":{
         "firstName":"Мария"
      }
   },
   "aggs":{
      "accounts":{
         "nested":{
            "path":"accounts"
         },
         "aggs":{
            "avg_accounts_balance":{
               "avg":{
                  "field":"accounts.balance"
               }
            }
         }
      }
   }
}
```

if `?size=0` you don't select matched document data, you get only aggregation result

AVG using `Script`

```
GET /domclick/user_data/_search?size=0
{
   "query":{
      "match":{
         "firstName":"Мария"
      }
   },
   "aggs":{
      "accounts":{
         "nested":{
            "path":"accounts"
         },
         "aggs":{
            "avg_accounts_balance":{
               "avg":{
                  "script":{
                     "lang": "painless",
                    "source": "doc['accounts.balance']"
                  }
               }
            }
         }
      }
   }
}
```

//Todo



