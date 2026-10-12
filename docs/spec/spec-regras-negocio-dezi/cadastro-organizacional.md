# CAP-2 Cadastro organizacional (fonte: `02-cadastro-organizacional.md`, prefixo RN-ORG)

## Empresa
- Toda leitura filtra pela empresa do usuário; ao gravar, contratante = o do usuário; nasce ATIVA; sem tela de cadastro (carga externa ⚠). (ORG-01, 02) [C]
- Só ADMINISTRADOR lista todas as empresas (todos os contratantes); lista "do contratante de um funcionário informado" não confere o contratante ⚠. (ORG-03) [C]
- Parâmetros da empresa: `feedforward` (sem efeito no mobile), `cbo` (2 empresas; 0 cargos com CBO), `label_gestores/subordinados/pares`, `host` (login por domínio), `versao_bi` (1 ou 2). (ORG-04) [C+B]
- 19 empresas ativas, 8 contratantes; inativar empresa não tem endpoint nem efeito sobre o login. (ORG-05) [B] ⚠

## Unidade
- Escopo = empresa. Nome único por empresa, sem caixa, com `trim` (só no código; 3 grupos duplicados já existem; **no novo, D46:** também no banco; validação olha só o 1º resultado). Nasce ATIVA. (ORG-06..08) [C+B]
- Hierarquia por `id_pai`; árvore desce só por filhos ativos; sem controle de ciclo. Hoje todas as 34 ativas são raiz (hierarquia sem uso). (ORG-09) [C+B]

## Departamento
- Escopo: ADM/GESTOR_RH/CONSULTOR_RH veem a empresa; GESTOR_AREA e FUNCIONARIO só o próprio. (ORG-10) [C]
- `idDepartamentoCliente` único na empresa (sem caixa) e `nome` único (banco garante só o nome). Mensagem de nome duplicado passa o código do cliente (defeito). (ORG-11) [C+B]
- Hierarquia até 6 níveis [B]. **Departamento inativo corta a árvore**: filhos/descendentes/ascendentes só consideram ativos; a subida do gestor para ali. Ascendentes repete o pai. Sem validação de ciclo/pai inativo. (ORG-12) [C+B]
- Nasce ATIVA; inativar não é impedido com funcionários/cargos/subdepartamentos ativos (8 funcionários ativos em depto inativo). (ORG-13) [C+B]

## Gestor (unidade × departamento)
- O gestor é uma linha de `unidade_departamento`, usada em `findAvaliador` e subordinados. (ORG-15) [C]
- **Mantido como efeito de salvar funcionário:** apaga a linha do par (unidade, depto) do funcionário e, se perfil GESTOR_AREA/GESTOR_RH, recria com ele; `departamento_gestor` apagada e recriada só para GESTOR_AREA (depto + descendentes ativos). Salvar funcionário comum provavelmente apaga o gestor sem recriar: 57 de 156 pares sem gestor. (ORG-16, 17) [C+B] ⚠
- Resíduos [B]: 5 linhas com gestor de perfil FUNCIONARIO; 9 com gestor fora do par; 3 pares duplicados; 6 gestores com vínculo duplo (`fetchOne` falharia). (ORG-17, 18)
- `departamento_gestor`: reprocessável pelo botão "Relacionar" (só ADM na tela; todos os GESTOR_AREA de todas as empresas); nenhum Java a lê (consumo provável pelo BI ⚠); defeito provável no laço dos descendentes. (ORG-19) [C+B]

## Subordinados (`obterSubordinados`)
1. Só devolve algo se o funcionário é gestor em `unidade_departamento` e o departamento dele é o do vínculo.
2. Entram: todos da unidade+departamento do vínculo, exceto ele, o nome literal "Administrador" e `DESLIGADO` (recesso entra).
3. Para cada departamento **filho direto ativo** (1 nível): se o filho tem gestor, entra **o gestor do filho** (sem filtro de situação); senão todos do filho na mesma unidade. (ORG-20, 21) [C] ⚠ conjunto desejado
- Usos: criação de avaliação (SUBORDINADO), mobile (só subordinados), `funcionariosParaAvaliacao` = todos ATIVA da empresa visível menos ADMINISTRADOR (recesso inclusive; bloqueio vem como pendência). (ORG-22, 23)

## Cargo
- Escopo: ADM/GESTOR_RH/CONSULTOR_RH empresa; GESTOR_AREA seu departamento; demais o próprio cargo. (ORG-24) [C]
- **Nasce INATIVA** (54% inativos); ativa-se ao associar competências (ver `modelo-competencias.md`). (ORG-25) [C+B] ⚠ intencional
- `idCargoCliente` e `nome` únicos por empresa (sem caixa no código; índices do banco); `idCargoCliente` nulo gera NPE. (ORG-26) [C+B]
- Cargo pai opcional, sem ciclo/pai-inativo validados; 18 com pai (uso residual). (ORG-27) [C+B]
- Inativar cargo inativa os vínculos cargo×tipo (e em cascata cargo×competência); não impede com funcionários ativos. (ORG-28) [C+B]
- **Cópia** de cargo: nome + " - Cópia" (numeração por `LIKE 'nome%'`), `idCargoCliente` = `<id>-<n>`; copia tipos, competências e detalhamentos com pesos; mantém departamento, pai e situação. Falha de clone não desfaz. (ORG-29) [C]
- Departamento do cargo (V2.0_0034) serve ao escopo do GESTOR_AREA; não é imposto ao funcionário (96 cargos com funcionários em outro depto). (ORG-30) [C+B] ⚠
- 33 cargos ativos sem tipo de competência geram pendência na avaliação. (ORG-32) [B]

## Funcionário
- Escopo de leitura: ADM/GESTOR_RH/CONSULTOR_RH empresa; GESTOR_AREA depto + filhos diretos; demais só ele; funcionário sem unidade (10 ADMINISTRADOR) some das consultas com projeção. (ORG-33) [C+B]
- Criação: empresa do usuário, ATIVA, senha temporária, `primeiroAcesso`, configuração pessoal, vínculo de gestor. (ORG-34) [C]
- **Validações ao gravar** (ORG-35) [C]: e-mail funcional único entre ATIVA da empresa (2 duplicados já existem; **no novo, D46:** garantido no banco, sem caixa); matrícula única (sem caixa); **inativar só `DESLIGADO`** (18 INATIVA em atividade existem; **no novo, D45:** situação única, ativo derivado); desligamento ≥ admissão; recesso exige início e fim, fim ≥ início; ADMINISTRADOR só editado por administrador.
- **Recesso** (10 situações): ao salvar situação não-recesso, as duas datas são zeradas; **não termina sozinho** e as datas não afetam nenhuma regra de avaliação (as 7 pessoas em recesso estão com data fim vencida). **No novo (D44):** volta sozinho à atividade na data fim. Quem está em recesso fica barrado como avaliado na liberação. (ORG-36, 37) [C+B]
- **Desligamento:** `DESLIGADO` → INATIVA e data de desligamento = hoje se vazia; outra situação apaga a data. `inactivate` força DESLIGADO; `activate` volta a EM_ATIVIDADE só se estava DESLIGADO. Nunca é apagado. (ORG-38) [C+B]
- Perfil: tela lista perfis menos ADMINISTRADOR; o perfil acumula acesso e papel de gestão. (ORG-39) [C+B]
- Campos informativos sem regra: `ponto_focal_rh` (131), `preenche_formacao_e_idiomas` (137), sexo (M/F), datas. (ORG-40) [B] ⚠
- Mudança de cargo/depto/unidade livre, sem histórico; a avaliação isola o impacto por snapshot. Unidade nula quebra o passo de gestor (NPE). (ORG-41) [C+B] ⚠
- Listas: por situação funcional; por perfil+empresa; `obterColaboradoresPDI`; participantes da avaliação com papéis para mensageria. (ORG-42, 43)

## Apontamento funcional
- Avaliador→avaliado+competência, data do servidor, `graduacaoVisual` obrigatória (escala 0–10; dados 2 a 10), texto (registro crítico) e feedforward. O mobile aceita avaliador do corpo ⚠. (ORG-44) [C]
- Leitura: ADM/GESTOR_RH veem a empresa; demais só os que lançaram. Defeito: no retorno mobile por avaliador+avaliado `registroCritico` recebe o feedforward. (ORG-45, 46) [C]
- 19 registros (2017–2023), uso baixo. (ORG-47) [B]

## Foto, formação e idiomas
- Foto em Base64 (`FOTO_PERFIL`, `FOTO_AVATAR`); avatar 80x80 gerado sob demanda e nunca refeito; **sem filtro de escopo**. Formação/idiomas sem validação de servidor; uso residual (19 e 6). (ORG-48, 49) [C+B] ⚠
