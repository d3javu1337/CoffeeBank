фронт для сервиса бизнес клиентов

1. auth

POST /auth/registration
```json
{
  "officialName": "string",
  "brand": "string",
  "email": "email",
  "password": "password"
}
```
ответ: просто статус

POST /auth/login
```json
{
  "email": "email", 
  "password": "password"
}
```
ответ: текстом access token, кукой refresh token

GET /auth/refresh
требует куку
ответ: текстом access token, кукой refresh token

2. business-client
GET /business-client
получение email из токена
ответ: 
```json
{
  "officialName": "String", 
  "brand": "String"
}
```

3. invoice
POST /invoice
```json
{
  "token": "uuid",
  "amount": 1.1
}
```
ответ: строка ссылка на оплату

GET /api/token
отдает токен существующий для создания счетов на оплату (своеобразный api ключ)

POST /api/token
создает и отдает токен для создания счетов на оплату


4. account
GET /account
получает email из access токена
отдает 
```json
{
  "id": 1, 
  "name": "String", 
  "deposit": 1.1
}
```

POST /account
получает email из access токена
отдает статус

5. payment
GET /payment
принимает paymentId query параметром
при его наличии отдает 
```json
{
  "paymentId": "UUID", 
  "amount": 1.1
}
```
при отсутствии
```json 
[
  {
    "paymentId": "UUID",
    "amount": 1.1
  }
]
```

GET /payment/check
требует paymentId query параметром
ответ булеан в строковом представлении. Означает был ли оплачен счет
вся авторизация обрабатывается мидварем, который проверяет токен с auth хедера

страницы:
1. / (главная/профиль пользователя (данные пользователя, payment account, переходник на страницу payments))
2. /auth (логин/регистрация)
3. /payment (payments + invoice)

запросы с помощью axios

/