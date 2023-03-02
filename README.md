# TasksSite
---------- RUS ----------
## Setting
Переименовать файл /src/main/resources/application.yml.origin на application.yml. В данном файле изменить следующие параметры:
- upload_path
- username
- password
- url

Пример как должно выглядеть:
``` 
upload_path: "D:/usersForum"
username: postgres
password: root
url: jdbc:postgresql://localhost/forum
```

## Installation
1. Установить [JDK](https://www.oracle.com/java/technologies/downloads/) v19.0.1+
2. Установить и настроить базу данных (Рекомендуется [PostgreSQL](https://www.postgresql.org/))
3. Открыть папку с проектом в командной строке и выполнить следующие команды
```
mvnw.cmd clean
mvnw.cmd package
cd target
java -jar forum-0.0.1-SNAPSHOT.jar
```
