create schema desafio;

create table desafio.hospede (
                                 documento varchar(50) not null,
                                 nome varchar(50) not null,
                                 telefone varchar(9),
                                 primary KEY(documento)
);

create table desafio.checkin (
                                 documento varchar(50) not null,
                                 dataEntrada timestamp not null,
                                 dataSaida timestamp not null,
                                 adicionalVeiculo varchar(1),
                                 foreign key (documento) references Hospede(documento),
                                 constraint PK_CHECKIN primary key (documento, dataEntrada, dataSaida)
);
