<h3> Spring MVC приложение - Система управления задачами</h3>

<b>Стек технологий: </b>
- Java <br>
- Spring Boot, Spring Boot MVC, Spring Security, Spring Data JPA<br>
- HTML, Thymeleaf<br>
- Docker, Docker Compose<br>
- PostgreSQL<br>
- ПО установлено на одноплатном компьютере Raspberry PI и доступно из сети Интернет.<br>
<br>
Посмотреть на работу приложения можно по ссылке:<br>
https://yterinc.ru/
<br>
<br>
<b>Инструкция по работе:</b> <br>
Пользователь в приложении может работать в двух режимах:
<br>
1. У пользователя отсутствует начальник. Пользователь сам регистрируется в системе, создает себе задачи и решает их. Возможность редактирования данных о себе и о задачах отсутствует.
<br>
2. У пользователя имеется начальник. Пользователь также может все как в п.1. Задачи данному пользователю может также назначать начальник и контролировать их выполнение.
<br>
Любого пользователя можно назначить начальником одного или нескольких пользователей.
<br>
Admin системы задается при установке приложения. Admin обладает полным контролем над всеми пользователями/задачами и может назначать начальников.
<br>
Для демонстрации всех возможностей приложения необходимо использовать учетную запись admin.
<br>
<br>
<b>Для сборки и запуска приложения необходимо:</b> <br>
1. Клонировать репозиторий<br>
   git clone https://github.com/YTERINC/TaskAndGoalManSystem.git<br>
2. Выполнить команду на сервере с установленным Docker<br>
   docker compose up --build<br>
3. Проверить работу приложения можно с помощью WEB-браузера