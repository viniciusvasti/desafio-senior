### Negócio
#### Não ficou claro se o ID da campanha não pode ser retornado.  
Já que existe possibilidade de alterar uma campanha, estou retornando o ID no GET

### Persistência
#### Utilizei um MongoDB "embedded" no serviço

### Padrões de Desenvolvimento
#### Apliquei TDD
#### Dividi a aplicação em trÊs camadas: Apresentação (Controllers que expõem os endpoins REST), Service (regras de negócio, entidades de domínio/models e eventos) e Infra (repositories que acessam/persistem os dados)
#### Devido a falta de tempo, para API do Socio Torcedor deixei de lado alguns padrões/práticas,  
que adotei na API das Campanhas:
- Testes de unidade (implementei, seguindo o TDD, apenas os testes de integração)
- Utilização de POJO's command para segregar objetos recebidos da camada de apresentação

### Melhorias (Há algumas melhorias que tenho em mente mas o tempo está limitado)
#### Definir uma camada de Application entre a camada de Apresentação (Controllers) e a de Serviços de Domínio (Service)  
Nessa camada seriam feitas as conversões de entidades de domínio para DTO's e virce-versa, retirando essa responsabilidade da camada de domínio.

### Problemas conhecidos
#### Ás vezes algum teste falha com IOException. Provavelmente perda de conexão com o embedded mongo.
#### O Serviço do Sócio Torcedor pára a execução se não houver comunicação com o Kafka
#### A regra de negócio `Dado um E-mail que já existe, informar que o cadastro já foi efetuado, porém, caso o cliente
não tenha nenhuma campanha associada, o serviço deverá enviar as novas campanhas como
resposta` não foi aplicada porque não sei se compreendi bem, mas achei conflitante com outra regra que diz que quando novas campanhas são cadastradas, os torcedores que estiverem associados ao time do coração das novas campanhas, devem ser associados a essas campanhas, automaticamente. Logo, exceto por questões de indisponibilidade de algum serviço, a regra geral é que os Sócio Torcedores sempre estarão associados
às campanhas vigentes do seu time do coração 
