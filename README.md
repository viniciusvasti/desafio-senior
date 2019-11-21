### Executando:
- Iniciar Kafka conforme [documentação oficial](https://kafka.apache.org/quickstart)
- Startando mongodb para serviço de campanhas `sudo docker run -p 27017:27017 -d --name mongocampanhas mongo` ou, caso já tenha rodado o container `sudo docker start mongocampanhas`
- Startando mongodb para serviço de sócios torcedores `sudo docker run -p 27018:27017 -d --name mongosociotorcedor mongo` ou, caso já tenha rodado o container `sudo docker start mongosociotorcedor`
- Startando serviço de campanhas:
  - A partir do Diretório DesafioSeniorCampanhas/DesafioSeniorCampanhas execute no cmd/terminal: `mvn spring-boot:run`
  - A partir do Diretório DesafioSeniorCampanhas/DesafioSeniorSocioTorcedor execute no cmd/terminal: `mvn spring-boot:run`

### Endpoints:
- Campanhas
  - Cadastrar: `POST` em `/campanhas` com body no formato  
`JSON { "nome": "string", "dataFimVigencia": "yyyy-mm-dd", "idTimeDoCoracao"  }`
  - Alterar: `PATCH` em `/campanhas/{id}` com body no formato  
`JSON { "nome": "string", "dataFimVigencia": "yyyy-mm-dd", "idTimeDoCoracao"  }`
  - Excluir `DELETE` em `/campanhas/{id}`
  - Recuperar Todas as Campanhas `GET` em `/campanhas`
  - Recuperar vigentes `GET` em `/campanhas/vigentes`
- Sócio Torcedor
  - Cadastrar: `POST` em `/socios-torcedores` com body no formato  
`JSON { "email": "string", "nome": "string", "dataNascimento": "yyyy-mm-dd", "idTimeDoCoracao"  }`
  - Recuperar todos `GET` em `/socios-torcedores`

### Negócio
- Não ficou claro se o ID da campanha não pode ser retornado.  
Já que existe possibilidade de alterar uma campanha, estou retornando o ID no GET

### Persistência
- Utilizei duas instâncias de MongoDB cujas portas podem ser verificadas em `src/main/resources/application.properties` de cada projeto

### Kafka
- Subi uma instância na minha máquina local cuja porta pode ser verificada em em `src/main/resources/application.properties` de um dos projetos
- Não implementei testes automatizados para o Kafka

### Padrões de Desenvolvimento e Arquitetura
- TDD;
- Dividi a aplicação em três camadas: Apresentação (Controllers que expõem os endpoins REST), Service (regras de negócio, entidades de domínio/models e eventos) e Infra (repositories que acessam/persistem os dados);
- Implementei dois microserviços: um para CRUD das campanhas e outro para cadastro de cliente;
- Implementei mensageria com Kafka, assim o serviço de campanhas produz mensagens informando cada alteração numa campanha e o serviço de cliente/sócio torcedor tem um consumer que consome essas mensagens e executa o que for necessário com elas.

### Devido a falta de tempo, para API do Socio Torcedor deixei de lado alguns padrões/práticas,  
que adotei na API das Campanhas:
- Testes de unidade (implementei, seguindo o TDD, apenas os testes de integração)

### Melhorias (Há algumas melhorias que tenho em mente mas o tempo está limitado)
- Definir uma camada de Application entre a camada de Apresentação (Controllers) e a de Serviços de Domínio (Service)  
Nessa camada seriam feitas as conversões de entidades de domínio para DTO's e virce-versa, retirando essa responsabilidade da camada de domínio.
- No projeto do Sócio Torcedor, CampanhaService está em acoplamento com SocioTorcedorService. Eu disparia eventos relacionados a
operações sobre as Campanhas e esse eventos cuidariam de executar o que for necessário em relação aos Sócios Torcedores
- O item acima também vale para o SocioTorcedorService que está em acomplamento com CampanhaRepository. Essas duas alterações atenderiam ao princípio da responsabilidade única

### Problemas conhecidos
- O Serviço do Sócio Torcedor não inicia se não houver comunicação com o Kafka
