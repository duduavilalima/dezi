# DEZI-QUEUE

Projeto que consome file do RabbitMQ, produzido pelo DEZI, e envia e-mail conforme dados da mensagem recebida.

## SpringBootRabbitMQ

### Tutorial
```html
https://www.rabbitmq.com/getstarted.html
```

### Download e Instalação
```html
https://www.rabbitmq.com/download.html
```

### Docker (Utilize docker-compose abaixo)

```sh
docker run -d --hostname my-rabbit --name some-rabbit -e RABBITMQ_DEFAULT_USER=user -e RABBITMQ_DEFAULT_PASS=password rabbitmq:3-management
```

- rabbitmq:3-management : com administrador habilitado.
- RABBITMQ_DEFAULT_USER : Usuário de acesso (admin e cliente)
- RABBITMQ_DEFAULT_PASS : Senha de acesso (admin e cliente)

Acesso página administrador: localhost:15672

Acesso produtor e consumidor: localhost:5672

### docker-compose.yml (Docker RabbitMQ da Fasolti)

```txt
version: '3'
services:
  fasolti-rabbitmq:
    image: "docker.fasolti.com.br/rabbitmq"
    hostname: fasolti-rabbitmq
    ports:
      - "15672:15672"
      - "5672:5672"
    volumes:
      - $PWD/rabbitmq:/var/lib/rabbitmq"
    environment:
     - RABBITMQ_DEFAULT_USER=admin
     - RABBITMQ_DEFAULT_PASS=admin
```

## Instalação

Apenas execute o IDE STS Spring e abra o projeto apontando para o código fonte.

## Deployment

Gerar com o seguinte comando:

```
mvn clean package -P prod
```

Dentro do `target` do projeto, executar em background (Linux):

```
nohup java -jar api.jar &
```

## Versionamento

Usamos [SemVer](http://semver.org/) para controle de versão. Para as versões disponíveis, veja as [tags neste repositório](http://git.fasolti.com.br/denis.oliveira/deziQueue/tags).

## Gerando o CHANGELOG do projeto
```sh
npm run release -- --release-as 0.0.1  --dry-run

npm run release -- --release-as patch --dry-run

npm run release -- --release-as minor  --dry-run

npm run release -- --release-as major  --dry-run
```

- --dry-run roda teste.
