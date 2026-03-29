# Roadmap do Projeto Balança + Humor

## Visão geral

Este projeto nasce da evolução de uma aplicação Python com Tkinter que já fazia duas coisas ao mesmo tempo:

- lia a balança pela porta serial, a partir de um Arduino;
- registrava o contexto subjetivo do uso, especialmente o humor e o motivo.

A nova fase do projeto não é apenas uma reescrita visual. A ideia é transformar o que hoje está concentrado em uma interface desktop Python em uma aplicação web local, com frontend moderno, backend separado e persistência em banco relacional, preservando a lógica real de uso que já foi consolidada no código antigo.

O princípio central do sistema continua o mesmo:

- a balança mede um valor;
- esse valor é associado a um estado subjetivo seu no momento do uso;
- tudo isso é salvo de forma estruturada para permitir um histórico consistente;
- mas o valor medido não deve ser exibido na interface para não influenciar o comportamento do usuário.

Em outras palavras, o projeto não quer apenas registrar peso e humor. Ele quer registrar um momento de uso com contexto suficiente para que o dado faça sentido depois, sem contaminar esse próprio momento com a consciência do número medido.

## Objetivo da primeira versão

A primeira versão deve ser funcionalmente séria, não um protótipo simplificado. Ela precisa portar as regras de negócio relevantes que o código Python já possui hoje, incluindo:

- humor;
- motivo do uso;
- primeiro uso do dia;
- sono;
- corrida;
- lotes;
- leitura serial;
- exigência de estabilidade da medição;
- histórico de registros;
- ocultação do valor numérico da balança na UI.

Ao mesmo tempo, essa versão deve inaugurar uma arquitetura mais sustentável:

- frontend web local no navegador;
- backend local responsável apenas pelas regras de negócio, sem a serial já implementada(estou sem arduino);
- banco PostgreSQL local;
- autenticação básica;
- catálogos de opções editáveis desde a v1.

## Princípios de produto

### 1. O número existe, mas não aparece

O sistema precisa salvar o valor medido no banco, porque ele é parte essencial do registro. Mas a interface não deve mostrar esse valor em nenhum ponto da experiência principal.

Isso vale para:

- tela de registro;
- confirmações;
- status da medição;
- histórico;
- listagens;
- respostas da API consumidas pelo frontend.

Na UI, o usuário deve ver apenas o estado do processo, por exemplo:

- balança desconectada;
- aguardando leitura;
- leitura recebida;
- leitura estável;
- leitura instável;
- registro salvo.

O projeto assume que a exposição do número altera o comportamento de uso e contamina o próprio experimento subjetivo que está sendo registrado.

### 2. A interface deve refletir o momento de uso

O formulário não é fixo o tempo todo. Ele muda conforme o contexto do dia e o histórico já registrado naquele dia.

Esse comportamento já existe no Python e deve ser preservado:

- certas perguntas só aparecem no primeiro uso do dia;
- certas perguntas deixam de aparecer depois que já foram respondidas naquele dia;
- o texto de alguns campos muda conforme o tipo de registro;
- o sistema impede combinações inválidas.

### 3. O sistema precisa evoluir com o uso real

As opções de humor e motivo não devem mais ficar hardcoded no frontend. Elas precisam nascer com os catálogos atuais do Python, mas passar a ser gerenciáveis na própria aplicação.

Isso é importante porque você explicitou que quer continuar usando as opções já existentes e poder ajustar, adicionar ou modificar essas opções à medida que o uso real do sistema mostrar necessidade.

## Escopo funcional da v1

### Registro principal

Cada registro deve conter, no mínimo:

- data e hora do evento;
- medição capturada pela balança;
- humor selecionado;
- motivo do uso;
- texto complementar quando necessário;
- observações gerais;
- indicação de primeiro uso do dia;
- dados de sono, quando aplicável;
- informação sobre corrida no dia, quando aplicável;
- lote associado;
- metadados técnicos da leitura serial.

### Catálogos editáveis

A aplicação deve ter telas de administração para editar:

- humores;
- motivos do uso;
- texturas de lote;
- cheiros de lote.

Essas opções devem nascer com os valores herdados do Python, mas o sistema precisa permitir:

- criar novas opções;
- desativar opções;
- renomear opções;
- reordenar opções.

Como você quer preservar a fidelidade histórica, registros antigos devem continuar mostrando o texto original usado no momento do cadastro, mesmo que uma opção seja renomeada depois.

### Histórico

O histórico deve existir já na v1 e precisa ser útil. Você quer visualizar na UI os registros anteriores com contexto suficiente, sem exibir o peso.

Esse histórico deve mostrar, por exemplo:

- data e hora;
- humor;
- motivo;
- complemento do motivo, se houver;
- primeiro uso do dia;
- corrida;
- dados de sono do dia, quando fizer sentido;
- lote;
- status da medição;
- observações.

O valor numérico da balança continua oculto.

### Configuração e conexão

A conexão serial não deve ficar misturada com a tela principal de uso. Haverá uma tela separada de configuração/conexão para:

- selecionar porta serial;
- selecionar baud rate;
- conectar;
- desconectar;
- visualizar estado atual da comunicação.

## Regras de negócio obrigatórias

### Primeiro uso do dia

O sistema deve continuar com marcação automática do primeiro uso do dia.

Regra esperada:

- se ainda não houver registro naquele dia, o sistema pode marcar automaticamente o registro como primeiro uso do dia;
- se já existir um registro do dia marcado como primeiro uso, essa marcação não pode ser feita novamente até o dia seguinte;
- a UI deve refletir esse bloqueio de forma clara.

### Sono

Na nova versão, o bloco de sono continua existindo, mas com escopo reduzido.

Ele deve aparecer apenas:

- no primeiro uso do dia;
- se ainda não houver dados de sono registrados naquele dia.

Campos mantidos:

- qualidade do sono;
- quantidade de horas dormidas.

Campo removido em relação ao Python:

- texto livre sobre como foi a noite.

### Corrida

O campo `corri hoje` deve manter a regra de travamento diário.

Ou seja:

- se for marcado como verdadeiro em um registro do dia, essa informação fica consolidada;
- o sistema não deve continuar perguntando a mesma coisa repetidamente ao longo do dia;
- o comportamento segue o mesmo racional do Python.

### Motivo do uso

O campo continua com semântica dependente do contexto:

- no primeiro uso do dia: a pergunta representa o motivo principal;
- nos demais usos do dia: a pergunta representa um motivo adicional.

Além disso:

- a lista de motivos continua existindo;
- a opção `Outros` deve exigir complemento textual;
- os textos devem poder evoluir no futuro via catálogo editável.

### Lotes

Lotes seguem sendo parte da v1.

O sistema deve permitir:

- selecionar um lote atual;
- cadastrar um novo lote durante o fluxo de registro;
- salvar identificação, data, quantidade em gramas, valor do lote, textura, cheiro e notas;
- calcular automaticamente o valor por grama.

Os lotes precisam ser entidades de primeira classe no banco, não apenas texto solto no registro.

### Medição estável

A gravação do registro depende de leitura válida da balança.

Como regra padrão:

- o sistema deve trabalhar com o fluxo de captura estável;
- se a leitura serial falhar no momento do registro, o salvamento deve ser bloqueado;
- não haverá salvamento sem peso como exceção da v1.

## Arquitetura proposta

### Frontend

O frontend será uma aplicação web local rodando no navegador, construída em tecnologia moderna de frontend.

Responsabilidades do frontend:

- renderizar a tela de registro;
- renderizar histórico;
- renderizar telas administrativas e de configuração;
- consumir a API local;
- exibir estados do sistema sem revelar a medição.

O frontend não deve falar diretamente com a serial.

### Backend

O backend será o núcleo operacional do sistema.

Responsabilidades do backend:

- gerenciar conexão serial;
- interpretar respostas do Arduino;
- aplicar as regras de negócio;
- decidir quando mostrar ou ocultar campos;
- validar o fluxo de primeiro uso do dia;
- validar o fluxo de sono e corrida;
- controlar lotes;
- persistir dados no PostgreSQL;
- expor API para o frontend;
- garantir que o valor medido não seja devolvido para a UI.

### Banco de dados

O banco será PostgreSQL local.

A modelagem deve contemplar pelo menos:

- usuários;
- registros de uso;
- logs diários;
- lotes;
- catálogos de humor;
- catálogos de motivo;
- catálogos de textura;
- catálogos de cheiro;
- configurações relevantes.

Como regra de consistência histórica, registros devem guardar:

- a referência da opção escolhida;
- e também um snapshot textual do rótulo usado no momento do registro.

Isso evita que o histórico mude retroativamente se um nome for alterado no futuro.

## Estrutura funcional esperada da aplicação

### Tela de login

Como você quer autenticação básica mesmo em ambiente local, a aplicação começa com uma etapa simples de autenticação.

O objetivo aqui não é multiusuário complexo, mas proteção mínima para:

- histórico;
- configurações;
- catálogos;
- conexão serial.

### Tela principal de registro

Essa será a tela mais importante do projeto.

Ela deve priorizar:

- rapidez de uso;
- clareza de contexto;
- ausência de números da balança;
- fluxo confiável de registro.

É a partir dela que o usuário:

- escolhe o humor;
- informa o motivo;
- responde perguntas contextuais do dia;
- seleciona ou cria lote;
- confirma o registro;
- acompanha apenas o estado da medição.

### Tela de histórico

Essa tela deve permitir leitura retrospectiva do uso sem expor o peso.

O objetivo não é apenas listar entradas, mas permitir reconhecer padrões subjetivos e contextuais:

- como estava o humor;
- por que houve uso;
- se era primeiro uso do dia;
- se houve corrida;
- quais dados de sono estavam associados;
- qual lote estava em uso naquele momento.

### Tela de configuração/conexão

Separada da tela principal.

Serve para:

- configurar serial;
- testar conexão;
- observar status operacional;
- eventualmente ajustar preferências técnicas.

### Tela de administração de catálogos

Responsável por manter vivo o vocabulário do sistema.

Nela será possível:

- criar novos humores;
- criar novos motivos;
- ajustar opções de lote;
- desativar itens obsoletos;
- manter o sistema alinhado com seu uso real ao longo do tempo.

## Estratégia de implementação

## Status atual

Data de referência deste status: 29/03/2026.

Situação geral:

- a fase 1 pode ser considerada concluída;
- a base local de frontend, backend, autenticação e banco já está funcional;
- o projeto já avançou parcialmente sobre as fases seguintes ao implementar contexto da tela principal e salvamento de registro;
- a fase 2 fica adiada temporariamente porque neste momento não há Arduino disponível para teste real da serial.

O que já está entregue no repositório:

- frontend React/Vite com login e rotas protegidas;
- backend Spring Boot com autenticação por sessão;
- PostgreSQL com Flyway;
- schema inicial e expansão inicial das entidades centrais;
- endpoint `GET /api/register/context`;
- endpoint `POST /api/entries`;
- tela principal ligada ao backend;
- criação de lote inline no fluxo de registro;
- medição mockada no backend para destravar desenvolvimento sem expor peso na UI.

O que ainda não está concluído:

- serial real;
- histórico real na UI;
- CRUD administrativo de catálogos;
- tela de configuração/conexão serial funcional;
- testes de integração mais completos;
- refinamentos finais de UX.

### Fase 1. Fundação técnica

Objetivo:

- estruturar backend, frontend e banco;
- definir autenticação;
- configurar conexão com PostgreSQL;
- estabelecer comunicação API <-> frontend.

Entregas:

- projeto inicial do frontend;
- projeto inicial do backend;
- schema inicial do banco;
- autenticação básica;
- infraestrutura mínima para rodar localmente.

Status:

- concluída.

### Fase 2. Núcleo da serial e medição

Objetivo:

- portar a lógica de leitura serial do Python;
- encapsular parsing e estado da balança;
- bloquear exposição do valor ao frontend.

Entregas:

- serviço de serial;
- parser das mensagens do Arduino;
- fluxo de leitura estável;
- API de status da balança.

Status:

- adiada temporariamente por falta de Arduino para teste real;
- o backend usa provisoriamente uma medição mock estável para permitir evoluir o restante da aplicação.

### Fase 3. Regras de negócio do registro

Objetivo:

- portar corretamente o comportamento dinâmico do formulário.

Entregas:

- primeiro uso do dia;
- sono;
- corrida;
- motivo principal e motivo adicional;
- validação de `Outros`;
- bloqueio de registro sem medição válida.

Status:

- parcialmente concluída;
- regra de contexto diário e fluxo principal de salvamento já existem;
- ainda depende de revisão final quando a serial real entrar.

### Fase 4. Lotes

Objetivo:

- recriar o fluxo de seleção e cadastro de lote com consistência de dados.

Entregas:

- CRUD básico de lotes;
- vínculo de lote no registro;
- cálculo de valor por grama;
- catálogos de textura e cheiro.

Status:

- parcialmente concluída;
- já existe criação inline de novo lote no fluxo de registro;
- ainda falta CRUD dedicado de lotes fora do fluxo principal.

### Fase 5. Histórico e telas administrativas

Objetivo:

- dar visibilidade ao uso passado e permitir evolução das opções.

Entregas:

- histórico de registros;
- tela de configuração serial;
- tela de administração de catálogos;
- controle de ativação/desativação de opções.

Status:

- ainda não iniciada de forma funcional.

### Fase 6. Refino de UX e estabilização

Objetivo:

- melhorar fluidez, clareza e robustez.

Entregas:

- mensagens de erro melhores;
- estados de loading;
- confirmação de ações críticas;
- revisão de responsividade;
- revisão de segurança básica;
- testes finais.

Status:

- não iniciada.

## Próxima etapa prática

Como a serial real deve ficar para depois, a próxima etapa recomendada passa a ser:

### Fase 5 antecipada parcialmente: histórico e administração sem serial

Objetivo imediato:

- dar visibilidade aos registros já salvos;
- permitir evolução dos catálogos sem depender do Arduino;
- continuar validando o uso real do sistema no navegador.

Escopo recomendado do próximo ciclo:

- implementar `GET /api/entries`;
- ligar a tela de histórico ao backend;
- implementar `GET /api/catalogs/*`;
- implementar edição básica de humores, motivos, texturas e cheiros;
- deixar a tela de configuração serial apenas como placeholder até haver hardware para teste.

Motivo da prioridade:

- isso continua entregando valor real agora;
- permite validar os dados que já estão sendo gravados;
- evita travar o projeto em uma dependência de hardware indisponível no momento.

## O que fica explicitamente fora da v1

Para manter foco, algumas coisas não entram como prioridade inicial:

- exportação CSV;
- migração do banco SQLite antigo;
- uso em rede local por outros dispositivos;
- empacotamento como app desktop;
- analytics avançado;
- dashboards estatísticos sofisticados.

Esses pontos podem virar uma segunda etapa depois que o fluxo principal estiver sólido.

## Resultado esperado da v1

Ao final da primeira versão, o sistema deverá permitir que você use a balança em um fluxo real, já no navegador, preservando a lógica comportamental que existia no Python e adicionando uma base mais sustentável para evolução futura.

Essa v1 deve entregar:

- registro confiável;
- contexto subjetivo rico;
- ocultação do peso na experiência de uso;
- histórico navegável;
- catálogos vivos;
- arquitetura separada e extensível.

Se a implementação seguir esse roadmap, o projeto deixa de ser apenas um app utilitário local em Tkinter e passa a ser uma plataforma pessoal de registro comportamental mediado por hardware, com base sólida para amadurecer sem perder o que já funcionava na prática.
