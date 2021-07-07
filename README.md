[![Build Status](https://travis-ci.org/dmitriyermoshin19/job4j_pooh.svg?branch=main)](https://travis-ci.org/dmitriyermoshin19/job4j_pooh)
[![codecov](https://codecov.io/gh/dmitriyermoshin19/job4j_pooh/branch/main/graph/badge.svg)](https://codecov.io/gh/dmitriyermoshin19/job4j_pooh)
# job4j_pooh
### Описание проекта
Этот проект представляет применение ***многопоточности*** в Java. В качестве примера создан аналог асинхронной очереди RabbitMQ. Использована технология пула ExecutorService. Приложение запускает Socket и ждем клиентов.
Клиенты могут быть двух типов: отправители (publisher), получатели (subscriver).
В качестве протокола будет использован HTTP. Сообщения в формате JSON.
Существуют два режима: queue, topic.
#### Queue.
Отправитель посылает сообщение с указанием очереди.
Получатель читает первое сообщение и удаляет его из очереди.
Если приходят несколько получателей, то они читают из одной очереди.
Уникальное сообщение может быть прочитано, только одним получателем.
 Пример запросов:
```
POST /queue/{ "queue" : "weather", "text" : "temperature +18 C" }
GET /queue
```
#### Topic.
Отправить посылает сообщение с указанием темы.
Получатель читает первое сообщение и удаляет его из очереди.
Если приходят несколько получателей, то они читают отдельные очереди.
```
POST /topic/{ "topic" : "weather", "text" : "temperature +18 C" }
GET /topic
```
### Использованные технологии
- Java SE 14
- ExecutorService
- Maven as a build system
- Mockito
- Junit
- CI/CD Travis
- Checkstyle
- Jacoco
