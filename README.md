# Задание #

## Условие ##

Веб приложение, цель которого - операции со счетами пользователей.
RESTful api:
* Перевод денег с одного счёта на другой
* Положить деньги на счёт
* Снять деньги со счёта

## Выбранный стек технологий ##
Spring Boot, H2 database, Hibernate, Spring MVC, Spring Data JPA, Bootstrap, Thymeleaf

## Выполнено ##
* Добавлены initial sql скрипты
* Админка, с основными операциями и валидацией
* Добавлена поддержка Swagger UI для тестирования
* Добавлены интеграционные тесты на основную логику с операциями со счетами пользователя, а также тесты для проверки правильности настройки JPA
* Основное RESTful api + дополнительно api для получения метаинформации по данным
* Для сборки использовать gradle build, исполняемый jar файл будет в projectDir\build\libs, для запуска используем java -jar youFile.jar

## Инструкция для разработчиков ##
Для создания проекта выполняем из projectDir gradle idea, открываем файл проекта через IDE Idea

## Elastic ##
Добавлена поддержка elastic search, необходимо проставить:
* elasticsearch.home - путь до elastic, местонахождение bin папки
* elasticsearch.cluster.name - имя кластера, по умолчанию elasticsearch
* elasticsearch.host - host(см. настройки)
* elasticsearch.port - port(см. настройки)

## Kubernetes ##
В планах добавить deployment в kubernetes, получил кучу проблем с установкой на WINDOWS, так как docker требует включенного Hyper-V, minikube поднимал так же на нем, тестировал на Windows 10
* Первое - заходим в "Пуск" -> "Установка и удаление программ" -> "Программы и компоненты" -> "Включение и отключение компонентов Windows" -> Убеждаемся что checkbox "Hyper-V" проставлен в true
* Второе - заходим в bios и убеждаемся что виртуализация доступна, у всех по разному советую загуглить
* Если не включена перезагружаем компьютер, после включение. После включения может быть недоступен интернет. Решение этой проблемы, заходим в доступные подключения и убираем с физического подкдлючения по кабелю в настройках галку "Hyper-V Extensible Virtual Switch", ок, ждем немного и проверяем интернет.
* Идем в заходим в "Пуск" -> "Диспетчер Hyper-V" -> "Диспетчер виртуальных коммутаторов" и создаем там новый "Виртуальный коммутатор" к внешней сети и выбираем нашу сетевую карточку с именем "Primary Virtual Switch".
* Далее устанавливаем через Power Shell(запусти через администратора) менеджер пакетов choco(гугли)
* choco install minikube
* `minikube start --cpus=4 --memory=4096 --vm-driver hyperv --hyperv-virtual-switch "Primary Virtual Switch"`, ждем
* `minikube addons enable ingress` установка ingress
* `minikube dashboard` если открылся dashboard в броузере по умолчания прыгаем от счастья

## Решение проблем с Kubernetes ##
Иногда его проще переустановить через заставить работать после гибернизации к примеру, решение проблем
* `minikube stop` остановка, не помогло? - `minikube ssh \ sudo poweroff`
* `minikube delete`, не удаляет ругается на config.json, нужно полностью все удалить, отключаем Hyper-V, так же как и включаем(смотри выше), перезапускаемся заходим в $UserHome удаляем .minikube, удаляем C:\ProgramData\Microsoft\Windows\Hyper-V - папку Virtual Machines, включаем Hyper-V и все настраиваем снова как выше

## Volumes ##
Для docker и kubernetes volumes paths прописываются в unix style, для того чтобы перевести в WINDOWS можно установить git bash и открыть к примеру в target folder, например E:\storage\psql == /e/storage/psql

## Exec ##
* `kubectl exec -it podId(kubectl get pods | grep name)` -- /bin/bash(depends from docker image)

## Quartz ##
* TODO

## OAuth2 ##
В application.properties

`security.protocol=oauth2`

* Получение access token-a

 ```
`curl -X POST \
   http://localhost:8080/oauth/token \
   -H 'authorization: Basic b2F1dGgyLWRlbW86b2F1dGgyLWRlbW8tcGFzc3dvcmQ=' \
   -F grant_type=password \
   -F username=admin \
   -F password=admin1234 \
   -F client_id=oauth2-demo`
 ```

, где -H authorization Basic (CLIENT_ID:CLIENT_SECRET in Base64 из таблицы OAUTH_CLIENT_DETAILS)


* Придет ответ в формате

 ```json
`{
   "access_token": "faea0851-f270-4999-9bcf-8cec742166f0",
   "token_type": "bearer",
   "refresh_token": "0086473b-4233-48aa-8b5f-40cd62b0dd02",
   "expires_in": 10730,
   "scope": "read write"
 }`
```

* Для получения информации используйте access_token в header-e authorization

 ```
`curl -X GET \
   http://localhost:8080/api/account/list \
   -H 'authorization: Bearer faea0851-f270-4999-9bcf-8cec742166f0'`
```

* Если access token is expired, можно обновить access token с помощью refresh token-a

 ```
`curl -X POST \
   http://localhost:8080/oauth/token \
   -H 'authorization: Basic b2F1dGgyLWRlbW86b2F1dGgyLWRlbW8tcGFzc3dvcmQ=' \
   -F refresh_token=0086473b-4233-48aa-8b5f-40cd62b0dd02 \
   -F grant_type=refresh_token`
 ```

## Security ##
Для тестирования использую зависимость
 ```
`testCompile("org.springframework.security:spring-security-test")`
 ```

В MockMvcSecurityConfigTest.kt пример того как можно использовать MockMvc


## LDAP ##
В application.properties

`security.protocol=ldap`

Для того чтобы сэмулировать хранилище
  ```
 `testCompile("org.springframework.security:spring-security-test")`
  ```

Доп. настройки:

Пользователи
`spring.ldap.embedded.ldif=classpath:local-storage.ldif`

Пример использования
`curl -X GET 'http://127.0.0.1:8080/api/account/list' -u ben:benspassword`

* Todo


## JWT ##

Jwt example

try to execute GET request:

  ```
curl -X GET "http://127.0.0.1:8080/api/company/list" -H  "accept: */*"
  ```

request will be returned:

  ```
{"timestamp":1565372887123,"status":403,"error":"Forbidden","message":"Access Denied","path":"/api/company/list"}
  ```

you need generate jwt token using credentials:

  ```
curl -X POST "http://127.0.0.1:8080/api/token/generate" -H  "accept: */*" -H  "Content-Type: application/json" -d "{  \"username\": \"ivan9105\",  \"password\": \"ivan9105\"}"
  ```

request will be returned:

  ```
{
  "username": "ivan9105",
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJpdmFuOTEwNSIsInNjb3BlcyI6W3siYXV0aG9yaXR5IjoiUk9MRV9BRE1JTiJ9XSwiaXNzIjoiaHR0cDovL2RvbWNsaWNrLmNvbSIsImlhdCI6MTU2NTM3MjgzOCwiZXhwIjoxNTY1MzcyODQxfQ.K0UmxPAgObSgDDcRpY4fQs1G2i3wfPuAVXVbyiPt6J8"
}
  ```

you need use "Authorization: Bearer $token" in you GET request:

  ```
curl -X GET "http://127.0.0.1:8080/api/company/list" -H  "accept: */*" -H  "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJpdmFuOTEwNSIsInNjb3BlcyI6W3siYXV0aG9yaXR5IjoiUk9MRV9BRE1JTiJ9XSwiaXNzIjoiaHR0cDovL2RvbWNsaWNrLmNvbSIsImlhdCI6MTU2NTM3MzE0OSwiZXhwIjoxNTY1Mzc2NzQ5fQ.dOEN4pwngO6fs7Xqf-2ZmbBQiGtWyTY_jy1kmZnkk9s"
  ```

You can use it for parse token and seeing details using this is resource:

  ```
https://jwt.io/
  ```

## ACL ##

В application.properties

`security.acl.enabled=true`

Структура бд

Security Identity
 ```
`INSERT INTO acl_sid (id, principal, sid) VALUES
(1, 1, 'PROJECT_MANAGER'),
(2, 1, 'DEVELOPER'),
(3, 0, 'TEXT_WRITER');`
 ```
* principal - признак является ли роль главной
* sid - наименование роли

Имя класса
 ```
`INSERT INTO acl_class (id, class) VALUES
 (1, 'com.domclick.entity.acl.AnswerEntity');`
 ```
* class - полное наименования класса

Идентификация объекта
 ```
`INSERT INTO acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES
 (1, 1, 1, NULL, 3, 0),
 (2, 1, 2, NULL, 3, 0),
 (3, 1, 3, NULL, 3, 0);`
 ```
* object_id_class - ссылка на таблицу с данными по классам acl_class table
* object_id_identity - первичный ключ целевого объекта (т.е. объекта бизнес логики - таблица для сущности Answer)
* parent_object - родительский объект (из этой же таблицы)
* owner_sid - владелец объекта, связь с acl_sid table
* entries_inheriting - записи этого объекта наследледуются от родительского (иерархия на основе таблицы acl_entry таблицы)

Записи (детальное описание прав)
 ```
`INSERT INTO acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES
 (1, 1, 1, 1, 1, 1, 1, 1),
 (2, 1, 2, 1, 2, 1, 1, 1),
 (3, 1, 3, 3, 1, 1, 1, 1),
 (4, 2, 1, 2, 1, 1, 1, 1),
 (5, 2, 2, 3, 1, 1, 1, 1),
 (6, 3, 1, 3, 1, 1, 1, 1),
 (7, 3, 2, 3, 2, 1, 1, 1);`
 ```
* acl_object_identity - ссылка на запись в acl_object_identity table
* ace_order - порядок текущей записи в списке acl_entry на основании acl_object_identity
* sid - ссылка на acl_sid, принадлежность к owner
* mask - READ == 1 и WRITE == 2
* granting - 0 запрещено, 1 разрешено
* audit_success и audit_failure для аудита

детальное описание

1 - разрешить PROJECT_MANAGER читать "Идентификацию объекта" с id == 1, которая ссылается на реальный объект Answer

2 - разрешить PROJECT_MANAGER редактировать "Идентификацию объекта" с id == 1, которая ссылается на реальный объект Answer

3 - разрешить TEXT_WRITER читать "Идентификацию объекта" с id == 1, которая ссылается на реальный объект Answer

4 - разрешить DEVELOPER читать "Идентификацию объекта" с id == 2, которая ссылается на реальный объект Answer

5 - разрешить TEXT_WRITER читать "Идентификацию объекта" с id == 2, которая ссылается на реальный объект Answer

6 - разрешить TEXT_WRITER читать "Идентификацию объекта" с id == 3, которая ссылается на реальный объект Answer

7 - разрешить TEXT_WRITER редактировать "Идентификацию объекта" с id == 2, которая ссылается на реальный объект Answer

## Docker ##

Сборка и создание образа

`gradle docker`

Запуск

`docker run -p 8080:8080 -d -t domclick`

Посмотреть логи

`docker logs (docker ps | grep domclick | awk '{print $1}')`

Остановить container

`docker stop (docker ps | grep domclick | awk '{print $1}') `

Скрипты (windows запускать из power shell) из корневой папки проекта

`.\deploy\build.sh`

`.\deploy\start.sh`

`.\deploy\stop.sh`

Postgres

Пример volume для windows

`docker run -p 5432:5432 -it -v /e/storage/psql:/var/lib/postgresql/data postgres:latest -e POSTGRES_USER=user POSTGRES_PASSWORD=password`

Для подключение внутри контейнера приложения к postgres, т.е. по факту подключение одного контейнера к другому используется не localhost а имя сервиса например db в (docker-compose.yml)

Image push to docker hub

`docker tag domclick $DOCKER_ID/domclick:$TAG`

`docker push $DOCKER_ID/$DOCKER_REPO_ID:$TAG`

## Docker compose ##

Решение проблем

No such image - нужно сделать docker-compose ps и docker-compose rm
Windows - если проект в root directory будет ошибка permission denied, нужно перенести проект в другую директорию

Volume
Для того чтобы создать volume нужно выполнить

` docker volume create pgdata`

использовать его можно так

```
services:
  serviceId:
    volumes:
      - pgdata:/var/lib/postgresql/data

volumes:
  pgdata:
    external: true
```

больше информации https://forums.docker.com/t/data-directory-var-lib-postgresql-data-pgdata-has-wrong-ownership/17963/23

## Профили ##

Use -Dspring.profiles.active=postgres in VM Options or java -jar -Dspring.profiles.active=dev ID.jar