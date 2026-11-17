# REVIBE — documentação técnica

**Estado:** trabalho em andamento; código não compilado nesta sessão. Atualizado em 17 de novembro de 2026.

## Origem, identidade e arquitetura

REVIBE deriva do código-fonte do [InterTune](https://github.com/ItzSkyeYT/InterTune), que por sua vez credita InnerTune e OuterTune. O projeto foi editado no lugar da base entregue; não foi criado um player vazio. A stack preservada inclui Kotlin, Jetpack Compose/Material 3, Hilt, Room, DataStore, Coroutines/Flow, Coil, AndroidX Media3/ExoPlayer e módulos de integração para pesquisa, letras, scrobbling, codecs e reconhecimento.

Identidade configurada: nome REVIBE, slogan “Feel the music again.”, escuro padrão, superfícies grafite/pretas, acento violeta, marca vetorial/ícones novos e cabeçalho próprio na Home. Liquid Glass fica ligado por padrão nos locais suportados, com controle para desligá-lo. O valor padrão só vale quando não há preferência gravada: instalações existentes mantêm o valor salvo.

Configuração Android herdada: `namespace=com.dd3boh.outertune`, `applicationId=app.revibe.music`, `minSdk=24`, `compileSdk=36`, `targetSdk=36`. O namespace de código Kotlin foi preservado para evitar uma refatoração ampla e arriscada que não altera o ID instalado. Variantes debug e preview usam sufixos `.debug` e `.preview`.

## Alterações realizadas nesta reconstrução

1. **Marca e tema:** nome e marca REVIBE, paleta violeta/preto, tema escuro padrão, cabeçalho da Home e recursos de ícone próprios.
2. **Liquid Glass:** defaults do player, dock, letras, boas-vindas e configuração alinhados em `true`; a preferência do usuário continua editável.
3. **Busca — sugestões:** o histórico local aparece enquanto a chamada de sugestões remotas está em curso. `flatMapLatest` continua trocando/cancelando a coleta da consulta anterior; as sugestões continuam sendo fornecidas pela integração existente, não fabricadas localmente.
4. **Busca — resultados:** há estado de falha/retry na primeira página e na continuação; pedidos `loadMore` concorrentes são bloqueados por filtro até a conclusão da chamada em andamento. O tratamento usa os tipos de erro existentes do app (`WhyStopped`).
5. **Integração Android:** applicationId alterado para `app.revibe.music`; authorities da busca do sistema conferidas para release/debug/preview; ações dos atalhos de downloads e reconhecimento namespaced sob `app.revibe.music.action.*`; teste de authority atualizado.
6. **Créditos:** a tela Sobre acrescenta “Agradecimentos aos criadores do InterTune”, nomes identificados na base e links para o repositório oficial e para a licença. Os nomes e links são os que constam na documentação upstream.
7. **Fontes vendored (antigos submódulos):** o ZIP recebido tinha as pastas `ffMetadataEx` e `taglib` vazias e não continha `innertubex`. Foram recuperadas do checkout upstream identificado pelo comentário do ZIP (commit `a3015d9d831abdb8f1165d715eb39e7ddf7b9eab`), sem copiar metadados `.git`. Hoje esses fontes estão incluídos como código vendored no repositório REVIBE (árvores de código comuns, não submódulos Git): não há gitlinks para popular e `git submodule update --init --recursive` não é necessário nem aplicável. Os hashes abaixo são histórico de origem upstream, não estado de checkout:
   - `ffMetadataEx`: `5374cd8b5eaf603c09ea8199aaf11311a3e60254`
   - `innertubex`: `b5f8b6f1bc2b73ee9e3f3ac95b7eaa255368864e`
   - `taglib`: `abf4cb2e421ff8ca2c97ae899f8dd915e91b5771`
   - fontes TagLib aninhados (antes submódulos recursivos de `taglib`): TagLib `7d86716194777e0294453bfdc9dd170bd033e1f4`, utfcpp `df857efc5bbc2aa84012d865f7d7e9cccdc08562`, ftest `c4ad4af0946b73ce1a40cbc72205d15d196c7e06`.

## Busca e reprodução: o que se pode afirmar

As mudanças de busca são correções no fluxo de estado e de falha; não houve execução contra uma fonte real nem medição de latência nesta sessão. Assim, são mudanças implementadas no código, não comprovação de que o serviço remoto esteja rápido ou disponível. Falhas de rede/serviço ainda podem impedir resultados.

A reprodução herdada usa o pipeline Media3/ExoPlayer e componentes de serviço/sessão já presentes na base. A auditoria desta sessão foi estática: não foi obtida uma causa raiz verificável para a mensagem “Erro desconhecido”, e não se alterou o pipeline para mascarar a mensagem. Não afirmar que a reprodução foi corrigida ou validada sem executar o fluxo em aparelho.

### Guard de serviços próprios do InterTune

`BuiltInKeys` compara o pacote/assinatura ao identificador e certificado do upstream. Esse código foi preservado deliberadamente. Em `app.revibe.music` e nas variantes `.debug`/`.preview`, o REVIBE não é reconhecido como build oficial do InterTune. Como consequência, não recebe as credenciais embutidas do upstream e não deve ativar serviços/canais que pertencem à equipe original (por exemplo, atualização do upstream, formulário/relay de report, suporte/comunidade, share page e lista de doadores, conforme os guards no código). Isso não desliga por si só player, biblioteca local, histórico, fila ou a infraestrutura genérica de pesquisa/letras. A ausência de credenciais próprias deixa os recursos que dependem delas indisponíveis, conforme o comportamento condicional da base.

## Licença e atribuição

O arquivo `LICENSE` entregue é a licença upstream **GNU GPL version 3 only** e foi preservado. Avisos de copyright nos arquivos-fonte, `Modules.md` e créditos de terceiros também foram mantidos; a cópia do README upstream está em `docs/INTERTUNE_README_UPSTREAM.md`. Esta reconstrução acrescenta documentação e mudanças REVIBE, sem reivindicar autoria do código upstream.

A seção Sobre agradece a ItzSkyeYT (InterTune), Zion Huang (InnerTune), Michael Zh e Davide Garberi (OuterTune), além dos demais colaboradores reconhecidos na base. Endereços oficiais utilizados:

- Projeto InterTune: <https://github.com/ItzSkyeYT/InterTune>
- Licença upstream: <https://github.com/ItzSkyeYT/InterTune/blob/main/LICENSE>

A redistribuição de binários deve observar a GPL-3.0 e a disponibilização do código-fonte correspondente. Este documento não substitui a licença.

## Build e signing

Os fontes de `ffMetadataEx`, `taglib` e `innertubex` são vendored neste repositório: não há submódulos Git para inicializar. A distinção entre as variantes é a dependência nativa de FFmpeg:

- `core`: JDK 21 como toolchain do Gradle (os módulos Kotlin declaram `jvmToolchain(17)`), Android SDK `platforms;android-36` e licenças aceitas. Não exige bibliotecas FFmpeg externas. O módulo nativo vendored `:taglib`, presente nas duas variantes, declara NDK `29.0.13113456` e CMake `3.31.6` em `taglib/build.gradle.kts`; portanto essas ferramentas também são necessárias para compilar `core`.
- `full`: tudo o que `core` exige, mais `:ffMetadataEx` (JDK 21, NDK `29.0.13113456` e CMake `3.31.6`) e as bibliotecas nativas do FFmpeg em `ffMetadataEx/ffmpeg-android-maker/output/lib`. Esses binários são uma dependência externa, resolvida separadamente conforme o [README do ffMetadataEx](../ffMetadataEx/README.md#building); os workflows de release clonam `mikooomich/ffmpeg-android-maker-prebuilt` (branch `audio`) para `ffMetadataEx/ffmpeg-android-maker` antes do build `full`.

Variantes incluem `core`/`full` e build types `debug`, `release`, `userdebug` e `preview`; confira `app/build.gradle.kts` antes de escolher uma variante. `CoreDebug` e `FullDebug` foram compilados com sucesso a partir de clone limpo simulado; `release`, `userdebug` e `preview` permanecem sem verificação de build nesta entrega. Detalhes em [TEST_REPORT.md](TEST_REPORT.md).

Com SDK compatível, a variante `core` pode ser verificada com:

```bash
./gradlew :app:assembleCoreDebug
./gradlew :app:testCoreDebugUnitTest
```

`keystore.properties` e arquivo de keystore não estavam presentes. Portanto não há configuração de assinatura própria disponível, e nenhum APK Release assinado foi produzido. Não reutilize uma chave de terceiros. A configuração do projeto deixa Release sem signing config quando não há keystore.

## Próximos passos recomendados

- Compilar todas as variantes necessárias em ambiente Android SDK/NDK suportado e corrigir erros efetivos do compilador.
- Executar testes unitários; acrescentar testes de fluxo de sugestões/paginação onde o desenho de dependências permita fakes determinísticos.
- Validar pesquisa em rede lenta, modo offline, mudança rápida de consulta, falha e retry.
- Testar em aparelho: busca real, abertura de item, stream autorizado, play/pause/skip/seek, notificação, lockscreen, segundo plano, retomada, Bluetooth/áudio e persistência.
- Diagnosticar “Erro desconhecido” com logs e cenário reproduzível sem esconder exceções.
- Conferir compatibilidade visual e desempenho num Samsung Galaxy S23 Ultra, inclusive consumo do efeito Liquid Glass e acessibilidade.
- Revisar qualquer integração de terceiros antes de publicar o fork e preparar informações de distribuição/código-fonte GPL.
