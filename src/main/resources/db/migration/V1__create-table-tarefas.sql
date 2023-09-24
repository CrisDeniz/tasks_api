create table tarefas(

    id bigint not null auto_increment,
    titulo varchar(40) not null,
    descricao varchar(100) not null,
    email varchar(25) not null,
    vencimento varchar(20) not null,
    prioridade varchar(10) not null,

    primary key(id)
)