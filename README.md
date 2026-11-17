# REVIBE

**Feel the music again.**

REVIBE é um fork Android em desenvolvimento, reconstruído sobre a base Kotlin/Jetpack Compose do [InterTune](https://github.com/ItzSkyeYT/InterTune). Não é um protótipo vazio: preserva a arquitetura e os recursos herdados de biblioteca, pesquisa, letras, fila, downloads, player Media3/ExoPlayer, MediaSession e reprodução em segundo plano. A interface está sendo reidentificada como REVIBE, com tema escuro, preto/grafite, violeta/lilás, superfícies Liquid Glass e marca própria.

> **Estado desta entrega:** as alterações de código ainda não puderam ser compiladas ou executadas. Não há APK Debug ou Release nesta pasta. Consulte [o relatório de testes e limitações](docs/TEST_REPORT.md) antes de instalar ou distribuir.

## Identidade e licença

- Nome: **REVIBE**; slogan: **Feel the music again.**
- `applicationId` release: `app.revibe.music`; debug e preview acrescentam `.debug` e `.preview`.
- Compatibilidade declarada na configuração herdada: `minSdk 24`, `compileSdk 36`, `targetSdk 36`.
- A base está licenciada sob **GPL-3.0-only**. O arquivo [LICENSE](LICENSE), avisos de copyright do código e créditos de componentes permanecem no projeto. Verifique a GPL-3.0 antes de redistribuir binários e forneça o código-fonte correspondente.
- Agradecimentos e o link do projeto original aparecem em **Configurações > Sobre** e na documentação técnica.

## Estrutura e recursos herdados

A aplicação usa Kotlin, Jetpack Compose/Material 3, Hilt, Room, DataStore, Coroutines/Flow, Coil e AndroidX Media3. A árvore mantém módulos de integração como `innertube`, `sabr`, `kugou`, `lrclib`, `lastfm`, `fingerprint`, `ffMetadataEx` e `taglib`. A disponibilidade e o comportamento de cada fonte dependem do serviço remoto, das configurações e das condições de acesso correspondentes; nada nesta entrega simula resultados ou reprodução.

As fontes de `ffMetadataEx`, `taglib` e `innertubex`, ausentes como conteúdo no ZIP fornecido, foram repostas nos commits referenciados pela árvore upstream `a3015d9d831abdb8f1165d715eb39e7ddf7b9eab`. Os identificadores dos commits estão registrados em [docs/REVIBE.md](docs/REVIBE.md).

## Alterações REVIBE

- Identidade visual própria, novo cabeçalho da Home, cores e ícones REVIBE; modo escuro continua sendo o padrão.
- Liquid Glass passou a ser a preferência padrão para instalações novas; o usuário ainda pode desligá-lo. Preferências já gravadas não são sobrescritas.
- Histórico local de busca pode ser mostrado enquanto as sugestões remotas carregam. Busca com resultados ganhou tratamento de falha/retry e paginação protegida contra pedidos concorrentes.
- Authorities da busca do sistema, identificador do app e ações de atalhos internos foram ajustados para o pacote REVIBE.
- A tela Sobre contém agradecimentos ao InterTune, InnerTune e OuterTune, com links para o upstream e para a licença.

A descrição detalhada, o comportamento dos guards herdados e as limitações estão em [docs/REVIBE.md](docs/REVIBE.md) e [docs/TEST_REPORT.md](docs/TEST_REPORT.md). O README original do upstream foi preservado como referência em [docs/INTERTUNE_README_UPSTREAM.md](docs/INTERTUNE_README_UPSTREAM.md).

## Build local

É necessário JDK 21 e Android SDK com `platforms;android-36`. A documentação upstream também exige `ndk;29.0.13113456` e `cmake;3.31.6` para os módulos nativos, e submódulos Git recursivos quando se parte de um clone. Abra a pasta raiz no Android Studio ou configure `local.properties` com `sdk.dir` apontando para o SDK instalado. Não inclua `local.properties`, keystore ou credenciais pessoais no repositório.

```bash
export JAVA_HOME=/caminho/para/jdk-21
export PATH="$JAVA_HOME/bin:$PATH"
./gradlew :app:assembleCoreDebug
./gradlew :app:testCoreDebugUnitTest
```

A criação do APK não foi concluída neste ambiente: faltam o Android SDK e as ferramentas de build compatíveis. Uma compilação bem-sucedida também não substitui testes de reprodução em um aparelho real.

## Estado e problemas pendentes

A busca recebeu correções estáticas, mas ainda falta compilar e testar consultas e paginação com e sem rede. O player e o pipeline Media3 foram preservados; a causa da mensagem “Erro desconhecido” não foi confirmada em runtime, portanto não se afirma que esse problema foi corrigido. Inicialização, reprodução, pausa, fila, notificação, tela bloqueada, persistência e compatibilidade com Galaxy S23 Ultra continuam pendentes de teste em dispositivo/emulador.
