# Tasks_Api

## 🔍 Sobre 
É uma Api Rest onde você pode criar, atualizar, modificar e excluir tarefas. Você poderá cadastrar um usuario e fazer login com ele para ter acesso as suas tarefas.

## 🤖 Tecnologias

- Java 17
- Spring Boot 3
- JPA / Hibernate
- Maven

## 📖 Documentação

A documentação foi feita utilizando a biblioteca SpringDoc, que contem o Swagger UI. Você pode usar o Swagger UI para testar os endpoints mencionados no tópico abaixo. Para acessá-la insira a URL: ``` /swagger-ui/index.html ```

## 👀 Endpoints

### /cadastro
Aqui você pode fazer a criação do seu usuário, que será usado para autenticar no sistema e criar a suas tarefas. Veja a forma com que a requisição deve ser montada:

**Metódo POST** 
```
{
	"nome": "Nome",
	"email": "qualquer.tasks@gmail.com",
	"senha": "1234"
}
```


### /login
Aqui é onde você irá se autenticar e receber um Token JWT, que será usado para criar e interagir com as tarefas. Veja a forma com que a requisição deve ser montada:

**Metódo POST** 
```
{
	"email": "qualquer.tasks@gmail.com",
	"senha": "1234"
}
```


### /tarefas
É neste endpoint que você poderá criar e modificar suas tarefas, também é onde utilizaremos o token no cabeçalho da requisiçao para a auntenticação. Veremos a seguir os metódos http que podem ser usados:

**Metódo POST**

Criação da tarefa.

```
{
	"titulo": "Estudar Lógica",
	"descricao": "Capitulo 1 e 2",
	"vencimento": "2023-09-11T20:00", // deve ser uma data futura 
	"prioridade": "MEDIANA"
}
```


**Metódo PUT**

É utilizado para atualizar a uma tarefa, onde o unico campo obrigatório a ser passado é o id de uma tarefa existente. Veja a forma com que a requisição deve ser montada:

```
{
	"id": 2,
	"prioridade": "BAIXA"
}
```
Mas você tambem pode atualizar dois ou mais campos se quiser:

```
{
	"id": 2,
        "nome": "Estudar Matemática",
	"prioridade": "BAIXA"
}
```


**Metódo DELETE**

Caso queira deletar uma tarefa, inclua o id da mesma após o endpoint, por exemplo: ``` tarefas/4 ```

**Metódo GET**

Uma requisição GET exibe todas as tarefas associadas ao usuário, filtrando-as com base no endereço de e-mail obtido a partir do token JWT. Dado que é uma requisição do tipo GET, não é necessário incluir um corpo na mesma.

Caso queira exibir apenas uma tarefa e não uma lista, inclua o id da tarefa após o endpoint, por exemplo: ``` tarefas/2 ```


### /usuarios

**Metódo GET**

No endpoint usuarios existe apenas o metódo GET para exibir um usuario pelo id, por exemplo: ``` usuarios/2 ```

## 🏃 Como executar o projeto


### 📍 Requisitos para execução

- Java 17
- 

*# clone o repositório*

```
git clone https://github.com/CrisDeniz/tasks_api
```

*# vá até a pasta do projeto*

```
cd tasks_api
```

*# execute o projeto*

```
mvn spring-boot:run
```

## Autor

Cristian Deniz

https://www.linkedin.com/in/cristian-deniz/
