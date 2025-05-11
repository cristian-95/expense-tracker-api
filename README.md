# expense-tracker-api

Uma API para gerenciar gastos, como proposto em: https://roadmap.sh/projects/expense-tracker-api

## Requisitos:

- Java 21
- PostgreSQL

## Execução:

- Baixe o codigo
- crie um arquivo .env ou exporte as variaveis de ambiente necessárias em application.yaml
- crie um banco de dados com nome expense_tracker_db (ou modifique o nome em application.yaml)
- no terminal:
 ```bash
  $ export $(xargs <.env)
  $ mvn spring-boot:run
 ```
- porta utilizada: 8080
- Ao executar, a documentação se encontra em 
  - localhost:8080/swagger-ui/index.html
