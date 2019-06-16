### Negócio
#### Não ficou claro se o ID da campanha não pode ser retornado.  
Já que existe possibilidade de alterar uma campanha, estou retornando o ID no GET

### Persistência
#### Utilizei um MongoDB "embedded" no serviço

### Padrões de Desenvolvimento
#### Apliquei TDD
#### Dividi a aplicação em trÊs camadas: Apresentação (Controllers que expõem os endpoins REST), Service (regras de negócio, entidades de domínio/models e eventos) e Infra (repositories que acessam/persistem os dados)

### Melhorias (Há algumas melhorias que tenho em mente mas o tempo está limitado)
#### Definir uma camada de Application entre a camada de Apresentação (Controllers) e a de Serviços de Domínio (Service)  
Nessa camada seriam feitas as conversões de entidades de domínio para DTO's e virce-versa, retirando essa responsabilidade da camada de domínio.

### Problemas conhecidos
#### Ás vezes algum teste falha com IOException. Provavelmente perda de conexão com o embedded mongo.
