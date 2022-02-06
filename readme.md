# Тестовое задание на Backend-практикум 2022

Приложение, которое позволяет парсить произвольную HTML страницу и выдавать статистику по количеству уникальных слов.
## Используемые технологии
- Java 8
- PostgreSQL
- Jsoup 1.14.3
- JavaFX 11.0.2
- Log4j 2.17.0

## Установка

Клонируйте репозиторий с помощью команды  git clone <https://github.com/MaximSavelev/Parser.git/>

## Настройка
Для работы базы данных PostgreSQL в файле database.properties необходимо указать:
```
url = jdbc:postgresql://localhost:5432/ - host по умолчанию локальный.Используется стандартный номер порта PostgreSQL(5432).
database_name = [название базы данных]
username = [имя пользователь]
password = [пароль]
```
## Демонстрация работы
![Пример работы приложения](https://github.com/MaximSavelev/Parser/blob/master/example.gif "Demonstration")
