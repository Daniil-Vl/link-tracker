![Bot](https://github.com/sanyarnd/java-course-2023-backend-template/actions/workflows/bot.yml/badge.svg)
![Scrapper](https://github.com/sanyarnd/java-course-2023-backend-template/actions/workflows/scrapper.yml/badge.svg)

# Link Tracker

Приложение для отслеживания обновлений контента по ссылкам. Отслеживает обновления в репозиториях Github и вопросах на Stackoverflow.
При появлении новых событий отправляется уведомление в Telegram.

Проект состоит из 2-х приложений:
* Bot
* Scrapper

Сервис Bot предоставляет интерфейс взаимодействия с приложением через телеграм.
Приложение Scrapper отслеживает обновления выбранных ресурсов и высылает уведомления (в сервис Bot).

Приложения общаются с помощью HTTP запросов или с помощью сообщений в Kafka. Можно выбрать желаемый способ общения.

## Технологический стек
+ Java 21
+ Spring Boot 3
+ PostgreSQL
+ Liquibase
+ Kafka
+ [Pengrad Telegram Bot API](https://github.com/pengrad/java-telegram-bot-api)
+ Docker
+ JUnit 5, Mockito, WireMock, TestContainers
+ Prometheus
+ Grafana

## Схема приложения
![Link tracker architecture](https://github.com/Daniil-Vl/java-backend-2024/assets/68438400/77d32e0f-7cf8-4b96-b525-57111bcd55a2)
