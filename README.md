# TowerDefence-09-2017

Backend проекта **TD** | Осень 2017
## Команда

- Даниил - [@Dnnd](https://github.com/Dnnd)
- Олег - [@oleggator](https://github.com/oleggator)
- Кирилл - [@DrMatters](https://github.com/DrMatters)

## Rest Api 

### Register User
Регистрация нового пользователя
#### URL
`/auth/signup`
#### Method
`POST`
#### Params
```
{
  "login": string [required],
  "email": string [required],
  "password": string [required],
}
```
#### Response
В случае успеха, возвращает данные созданного пользователя
```
{
  "id": int,
  "login": string,
  "email": string,
 }
 ```
### Signin
Авторизация
#### URL
`/auth/signin`
#### Method
`POST`
#### Params
```
{
  "email": string [required],
  "password": string [required],
}
```
#### Response
В случае успеха, возвращает данные авторизовавшегося пользователя
```
{
  "id": int,
  "login": string,
  "email": string,
 }
 ```
### Logout
Завершение пользовательской сессии
##### URL
`/logout`
#### Method
`POST`
#### Response
```
{
  "status":"Success"
}
 ``` 
### Get Current User
Получение пользователя текущей сессии
**URL**
`/user`
**Method**
`GET`
**Response**
В случае успеха, возвращает данные текущего пользователя
```
{
  "id": int,
  "login": string,
  "email": string,
 }
 ```
 ### Update User
Обновление пользователских данных
#### URL
`/user/edit`
#### Params
```
{
  "id": int[required],
  "login": string
  "email": string,
  "password": string,
}
```
#### Method
`POST`
#### Response
```
{
  "status":"Updated"
 }
 ```
 
 
