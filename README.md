### Executando:
- Iniciar Kafka conforme [documentação oficial](https://kafka.apache.org/quickstart)
- Startando mongodb para serviço de sócios torcedores `sudo docker run -p 27018:27017 -d --name mongosociotorcedor mongo` ou, caso já tenha rodado o container `sudo docker start mongosociotorcedor`
- Startando serviços:
  - A partir do Diretório DesafioSeniorCampanhas/DesafioSeniorCampanhas execute no cmd/terminal: `docker-compose up -d`
  - A partir do Diretório DesafioSeniorCampanhas/DesafioSeniorSocioTorcedor execute no cmd/terminal: `mvn spring-boot:run`

### Endpoints:
- Campanhas
  - Cadastrar: `POST` em `http://localhost:8081/campanhas` com body no formato  
`JSON { "nome": "string", "dataFimVigencia": "yyyy-mm-dd", "idTimeDoCoracao"  }`
  - Alterar: `PATCH` em `http://localhost:8081/campanhas/{id}` com body no formato  
`JSON { "nome": "string", "dataFimVigencia": "yyyy-mm-dd", "idTimeDoCoracao"  }`
  - Excluir `DELETE` em `http://localhost:8081/campanhas/{id}`
  - Recuperar Todas as Campanhas `GET` em `/campanhas`
  - Recuperar vigentes `GET` em `http://localhost:8081/campanhas/vigentes`
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
- Fluxo de Cadastro/Alteração/Exclusão de Campanha (MS de Campanha)
  1. Controller recebe requisição REST;
  2. Service executa comando relativo a campanha invocando o Repository e dispara um Evento de cadastro/alteração/exlusão de campanha;
  3. Um Listener captura o evento e dispara producer com a mensagem e a ação efetuada para o Kafka.
- Fluxo de Cadastro/Alteração/Exclusão de Campanha (MS de Sócio Torcedor)
  1. Consumer recebe mensagem de nova campanha via Kafka e invoca o Service com a ação correspondente ao comando;
  2. Service executa ação em seu banco de dados com réplica da campanha invocando o Repository;
  3. Caso a ação seja de cadastro de campanha, o Service varre todos os sócio torcedores associados ao mesmo código de Time da campanha e associa a campanha aos Sócios Torcedores do Time.
- Desenho da Arquitetura proposta em um cenário real (Não o implementado nesse desafio)
![Diagrama de Arquitetura](https://github.com/viniciusvasti/desafio-senior/blob/master/Arquitetura.jpg)

### Devido a falta de tempo, para API do Socio Torcedor deixei de lado alguns padrões/práticas,  
que adotei na API das Campanhas:
- Testes de unidade (implementei, seguindo o TDD, apenas os testes de integração)

### Melhorias (Há algumas melhorias que tenho em mente mas o tempo está limitado)
- Composição com docker-compose para cada microserviço com seu banco da dados Mongo (o foco foi na arquitetura)
- Definir uma camada de Application entre a camada de Apresentação (Controllers) e a de Serviços de Domínio (Service)  
Nessa camada seriam feitas as conversões de entidades de domínio para DTO's e virce-versa, retirando essa responsabilidade da camada de domínio.
- No projeto do Sócio Torcedor, CampanhaService está em acoplamento com SocioTorcedorService. Eu disparia eventos relacionados a
operações sobre as Campanhas e esse eventos cuidariam de executar o que for necessário em relação aos Sócios Torcedores
- O item acima também vale para o SocioTorcedorService que está em acomplamento com CampanhaRepository. Essas duas alterações atenderiam ao princípio da responsabilidade única

### Problemas conhecidos
- O Serviço do Sócio Torcedor não inicia se não houver comunicação com o Kafka
- No fluxo de retry, há um bug ao cair no tópico de retenção e tentar reenviar para o tópico principal, por tanto, provisoriamente, ao ocorre falha no consumo, não está sendo enviada a mensagem para o tópico de retenção, mas diretamente para o de erro.
