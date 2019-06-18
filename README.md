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
- Definir uma camada de Application entre a camada de Apresentação (Controllers) e a de Serviços de Domínio (Service)  
Nessa camada seriam feitas as conversões de entidades de domínio para DTO's e virce-versa, retirando essa responsabilidade da camada de domínio.
- No projeto do Sócio Torcedor, CampanhaService está em acoplamento com SocioTorcedorService. Eu disparia eventos relacionados a
operações sobre as Campanhas e esse eventos cuidariam de executar o que for necessário em relação aos Sócios Torcedores
- O item acima também vale para o SocioTorcedorService que está em acomplamento com CampanhaRepository. Essas duas alterações atenderiam ao princípio da responsabilidade única

### Problemas conhecidos
#### Ás vezes algum teste falha com IOException. Provavelmente perda de conexão com o embedded mongo.
#### O Serviço do Sócio Torcedor pára a execução se não houver comunicação com o Kafka
