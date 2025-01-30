# CurrencyExchanger

REST-api для обменных курсов валют

## Содержание

- [Стек](#стек)
- [Особенности](#особенности)
- [Установка](#установка)

## Стек

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Apache Maven](https://img.shields.io/badge/Apache%20Maven-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white)
![SQLite](https://img.shields.io/badge/sqlite-%2307405e.svg?style=for-the-badge&logo=sqlite&logoColor=white)
![Apache Tomcat](https://img.shields.io/badge/apache%20tomcat-%23F8DC75.svg?style=for-the-badge&logo=apache-tomcat&logoColor=black)

## Особенности

<h3>Валюты</h3>

GET `/currencies`    
Получение списка всех валют. Ответ:

```json
[
  {
    "id": 1,
    "name": "Australian dollar",
    "code": "AUD",
    "sign": "A$"
  },
  {
    "id": 2,
    "name": "United States dollar",
    "code": "USD",
    "sign": "$"
  },
  {
    "id": 3,
    "name": "Euro",
    "code": "EUR",
    "sign": "€"
  }
]
```

GET `/currency/GPB`    
Получение конкретной валюты. Ответ:

```json
{
  "id": 6,
  "name": "Pound Sterling",
  "code": "GBP",
  "sign": "£"
}
```

POST `/currencies`    
Добавление новой валюты в базу данных. Указываются `name`, `code`, `sign`. Ответ:

```json
{
  "id": 8,
  "name": "Test",
  "code": "VAL",
  "sign": "V"
}
```

<h3>Обменные курсы</h3>
GET `/exchangeRates`    
Получение списка всех обменных курсов. Ответ:

```json
[
  {
    "id": 1,
    "baseCurrency": {
      "id": 2,
      "name": "United States dollar",
      "code": "USD",
      "sign": "$"
    },
    "targetCurrency": {
      "id": 3,
      "name": "Euro",
      "code": "EUR",
      "sign": "€"
    },
    "rate": 106.1
  },
  {
    "id": 2,
    "baseCurrency": {
      "id": 7,
      "name": "Tenge",
      "code": "KZT",
      "sign": "₸"
    },
    "targetCurrency": {
      "id": 3,
      "name": "Euro",
      "code": "EUR",
      "sign": "€"
    },
    "rate": 102.81
  }
]
```

GET `/exchangeRate/USDEUR`    
Получение конкретного обменнего курса. Ответ:

```json
{
  "id": 1,
  "baseCurrency": {
    "id": 2,
    "name": "United States dollar",
    "code": "USD",
    "sign": "$"
  },
  "targetCurrency": {
    "id": 3,
    "name": "Euro",
    "code": "EUR",
    "sign": "€"
  },
  "rate": 106.1
}
```

POST `/exchangeRates`    
Добавление нового обменного курса в базу данных. Указываются `baseCurrencyCode`, `targetCurrencyCode`, `rate`. Ответ:

```json
{
  "id": 4,
  "baseCurrency": {
    "id": 4,
    "name": "Russian Ruble",
    "code": "RUB",
    "sign": "₽"
  },
  "targetCurrency": {
    "id": 1,
    "name": "Australian dollar",
    "code": "AUD",
    "sign": "A$"
  },
  "rate": 2.88
}
```

PATCH `/exchangeRate/RUBAUD`    
Обновление существующего обменного курса. Указывается `rate`. Ответ:

```json
{
  "id": 4,
  "baseCurrency": {
    "id": 4,
    "name": "Russian Ruble",
    "code": "RUB",
    "sign": "₽"
  },
  "targetCurrency": {
    "id": 1,
    "name": "Australian dollar",
    "code": "AUD",
    "sign": "A$"
  },
  "rate": 50
}
```

<h3>Обмен валюты</h3>
GET `/exchange?from=RUB&to=AUD&amount=12`    
Расчет перевода определенного количества средств из одной валюты в другую.Указывается код валюты из которой
выполняется конвертация -`from`, код валюты, в которую выполняется конвертация - `to`, количество средств - `amount`.
Ответ:

```json
[
  {
    "id": 1,
    "baseCurrency": {
      "id": 2,
      "name": "United States dollar",
      "code": "USD",
      "sign": "$"
    },
    "targetCurrency": {
      "id": 3,
      "name": "Euro",
      "code": "EUR",
      "sign": "€"
    },
    "rate": 106.1
  },
  {
    "id": 2,
    "baseCurrency": {
      "id": 7,
      "name": "Tenge",
      "code": "KZT",
      "sign": "₸"
    },
    "targetCurrency": {
      "id": 3,
      "name": "Euro",
      "code": "EUR",
      "sign": "€"
    },
    "rate": 102.81
  }
]
```

<h3>Ответ в случае ошибки</h3>

```json
{
  "message": "Failed to add exchange rate. Exchange rate already exists."
}
```
