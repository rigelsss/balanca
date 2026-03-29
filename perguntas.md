# Perguntas para fechar a especificação da nova versão

Estas perguntas partem da análise do `app.py` antigo. A intenção aqui é preservar a regra de negócio existente e só decidir o que ainda está em aberto para a implementação da nova UI.

## Regras identificadas no código atual

- O registro salva leitura da balança, humor, motivo do uso, observações, dados da serial e versão do app.
- O valor numérico da medição é salvo no banco, mas não deve aparecer na UI.
- Existe regra de `primeiro uso do dia`:
  - se ainda não houve uso no dia, o app pode marcar automaticamente o primeiro registro como primeiro uso;
  - se já existir um registro com `first_of_day = 1`, essa opção fica desabilitada até o dia seguinte.
- O bloco de `sono` só aparece no primeiro uso do dia e só se ainda não houver dados de sono registrados naquele dia.
- O campo `corri hoje` é perguntado uma vez por dia:
  - se já foi marcado uma vez no dia, fica registrado e deixa de ser perguntado novamente.
- O campo `motivo do uso` muda semanticamente:
  - no primeiro uso: "O que está te levando a usar agora?"
  - nos demais: "O que está te levando a usar agora? Por que mais?"
- Existe regra de `lotes`:
  - é possível selecionar lote atual;
  - é possível cadastrar novo lote no momento do registro;
  - novo lote exige data, gramas, valor do lote e identificação;
  - o sistema calcula `valor_por_grama`.
- A gravação da medição pode exigir leitura estável (`[S] stable=YES`) para salvar.

## Perguntas de produto e UX

1. A nova aplicação deve continuar fazendo a marcação automática de `primeiro uso do dia` quando for o primeiro registro do dia, ou você quer que isso passe a ser sempre manual?
Marcacao automatica

2. No primeiro uso do dia, você quer manter exatamente os mesmos campos de sono?
   - qualidade do sono de 1 a 5
   - quantidade de horas dormidas
Pode tirar    - texto livre sobre como foi a noite

3. O campo `corri hoje` deve continuar travando o restante do dia assim que for marcado como `Sim`, exatamente como no Python, ou você quer permitir corrigir esse dado depois?
Travando o restante do dia

4. A seção de `lotes` deve aparecer já na v1 com a mesma lógica atual, inclusive:
   - seleção de lote atual
   - criação de novo lote durante o registro
   - campos de textura, cheiro e notas do lote
Sim

5. Os catálogos editáveis de `humor` e `motivo do uso` devem ter uma tela própria de configuração desde a v1, ou por enquanto você prefere editar isso diretamente no banco/arquivo de configuração e deixar a tela administrativa para depois?
Tela propria desde a v1

6. Quando um humor ou motivo for renomeado no futuro, você quer:
   - que registros antigos continuem exibindo o texto antigo;
   - ou que todo o histórico passe a refletir o novo nome?
Que registros antigos continuem exibindo o texto antigo

7. Sobre o histórico da aplicação: além de esconder o peso, você quer ver na UI os registros anteriores com campos como humor, motivo, lote, primeiro uso, corrida e sono, ou prefere um histórico mais mínimo?
Boa ideia, quero sim ver na UI os registros anteriores

8. Você quer que a UI tenha uma tela separada de `configuração/conexão` para escolher porta serial e baud rate, ou isso deve ficar visível na tela principal como era no Tkinter?
Tela separada de configuracao/conexao

9. Em caso de falha na serial no momento do registro, qual comportamento você prefere:
   - bloquear completamente o salvamento;
   - permitir salvar sem peso em casos excepcionais;
   - ou guardar um rascunho pendente para completar depois?
Bloquear completamente o salvamento

10. O banco definido anteriormente foi `Postgres local`. Você quer mesmo seguir com `Postgres` na primeira versão, ou prefere `SQLite` inicialmente para ficar mais simples de instalar e rodar localmente?
Postgres. Eu já o tenho instalado e rodando em outro projeto.

## Perguntas técnicas que afetam a implementação

11. A nova aplicação será usada só na sua máquina local ou você quer a possibilidade real de usar de outro dispositivo na rede local no futuro?
Só na máquina local

12. Você quer autenticação básica na aplicação, mesmo sendo local, para proteger o histórico e as telas de configuração?
Sim

13. Exportação de CSV continua sendo requisito da v1?
Não, registro é mais importante.

14. Você quer migração dos dados existentes do `balanca.db` antigo para o novo banco, ou a nova versão pode começar com banco limpo?
Pode começar com banco limpo

15. O frontend deve ser:
   - web local no navegador;
   - ou empacotado como app desktop desde o início?
web local no navegador

## Catálogos atuais identificados no `app.py`

### Humores

- Feliz
- Tranquilo
- Ansioso
- Com Raiva
- Triste
- Empolgado
- Cansado
- Desmotivado
- Caos Mental

### Motivos do uso

- Tédio
- Empolgado
- Triste
- Vontade
- Ansioso
- Querer Mais
- Caos Mental
- Outros

### Textura do lote

- Arenoso
- Muito seco, duro de manipular
- Manipulável
- Úmido, mas manipulável
- Muito úmido, de difícil manipulação

### Cheiro do lote

- Cheiroso
- Muito cheiroso
- Quase sem cheiro
- Sem cheiro
