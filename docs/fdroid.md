# Getting REVIBE into F-Droid

Packaging was first requested upstream in InterTune issue #14 (historical reference). For REVIBE, track the request at `https://github.com/revibe-music-code/REVIBE/issues`. This is what is needed, what is already true, and the two routes in the order worth taking them.

## Where the repository already stands

Checked on 12 September 2026, against the published 0.10.7 apk and the tree it was built from. Historical check: the application IDs and the credentials policy were updated in the sections below.

- **Licence.** GPL-3.0, with the full text in `LICENSE` and an SPDX header on every file this fork wrote.
- **No proprietary dependencies.** Nothing from Google Play services, Firebase, billing or crash reporting, in the gradle files or in the apk.
- **No prebuilt binaries in the source.** `media/` is ignored and untracked; the only jar in git is the gradle wrapper's. The one native library, `libtaglib.so`, is built by the NDK from the C++ in the `taglib` submodule.
- **The build needs no published secrets.** Credentials (Last.fm API key and poll endpoints) in `services.properties` are public placeholders only. Real values must be supplied locally through `local.properties` (git-ignored) or build variants, and must never be published. A build from source therefore omits those integrations rather than embedding a secret; see "What an F-Droid build will not have" below.
- **The screen shots carry nobody's library.** They must be taken on a second, clean install of `app.revibe.music` beside a clean `app.revibe.music.debug` install, whose library contains only well-known albums added by hand. No playlists, no recommendations, no listening history belonging to anyone. Older screenshots from the `dev.skye.intertune` era are historical and need to be retaken.
- **Store metadata is in the repository** at `fastlane/metadata/android/en-US`: title, short and full descriptions, an icon, seven phone screen shots, five tablet screen shots, and a changelog for the current version code.
- **Releases are tagged.** `v0.10.7` and the rest are real tags on the default branch, which is what a build recipe points at.
- **An F-Droid install already updates through F-Droid.** `utils/InstallSource.kt` recognises the F-Droid clients and Droidify and Neo Store; the in-app updater then stands aside and points at the F-Droid page instead of offering the GitHub apk.

The signing certificate of the GitHub builds, for anyone verifying an apk or pinning it:

```
SHA-256  69c6b5fbf1220a44206e8a3a098f41c78af7f94d2fa56eb3165ca40b9199a2a7
SHA-1    b2753483bb52715810360d1e08ce1ebbe22b1abc
CN=InterTune, OU=Development, O=skye.dev, C=FR
```

> This is the historical GitHub signing certificate of the upstream line. REVIBE's own release
> signing was **not validated in this delivery**; do not treat this fingerprint as REVIBE's until
> checked separately.

F-Droid's own repository signs with its own key, so an install from there cannot be updated over an install from GitHub, in either direction. Moving either way means uninstalling first, which takes the library with it unless somebody backs it up. The 0.11 release notes say so and point at Settings > Backup and restore, and that line should be repeated whenever the F-Droid listing actually lands.

## IzzyOnDroid is not a route, and here is why

Their [app inclusion policy](https://izzyondroid.org/docs/general/AppInclusionPolicy/) rejects apps created fully or in part by generative AI, rejects what it calls vibe-coded apps outright, and says that a lack of transparency about it can move a project to rejected. Using a language model to research, debug or look something up is allowed; its output ending up in the code is not.

A large part of this fork's recent work was written that way, in the open, with the maintainer directing it. That is exactly what the policy excludes. There are two honest options and neither is a submission as things stand:

1. Do not apply. The f-droid.org route below has no such rule.
2. Apply and say so in the request. Expect it to be rejected, and treat the answer as theirs to give rather than something to word around.

What is not an option is applying and staying quiet about it. The policy asks for transparency by name, and a repository that finds out afterwards is entitled to feel misled.

Everything else IzzyOnDroid asks for is already true here: an OSI licence, sources on GitHub, fastlane metadata in the repository, a release with one signed apk under thirty megabytes, no trackers, no proprietary libraries, and an in-app updater that is opt-in and off until somebody says yes.

## The f-droid.org route

> **Nota REVIBE:** o texto abaixo registra a experiência herdada do upstream com submódulos Git.
> Neste repositório `taglib` e `ffMetadataEx` são árvores de código vendored, sem gitlinks. A receita
> F-Droid precisa ser revalidada contra essa diferença antes de qualquer envio real.

Their [inclusion policy](https://f-droid.org/docs/Inclusion_Policy/) says nothing about how the code was written. It asks that the app be free software, that it build from source on their infrastructure with a free toolchain, that it carry no proprietary tracking or advertising libraries, and that anything which downloads an executable be opt-in and explained. All four hold.

Checked against the published 0.10.7 apk, by reading its dex directly: no Firebase, no Crashlytics, no Play services, no AdMob, no Facebook, Flurry, Adjust, AppsFlyer, Sentry, ACRA or Matomo, no Play billing and no install referrer. Three native libraries, two of them AndroidX and the third `libtaglib.so`, built from the C++ in the taglib submodule.

1. Open a request for packaging at <https://gitlab.com/fdroid/rfp/-/issues/new> with the template. The text below is ready to paste.
2. Offer, and better still open, a merge request to fdroiddata with the legacy template `docs/fdroid/dev.skye.intertune.yml` installed as `metadata/app.revibe.music.yml`. The file keeps its historical name in this repository; in fdroiddata it must be named after the real applicationId, `app.revibe.music`. Their submission queue says in as many words that packaging it yourself and opening a merge request saves everyone time, and that an issue on its own guarantees nothing. The recipe is a draft of `metadata/app.revibe.music.yml` in fdroiddata. It builds the `core` flavour, pulls submodules for taglib, and names an NDK.
3. The licence identifier is settled: `GPL-3.0-only`. The headers inherited from upstream say `GPL-3.0`, which SPDX deprecated because it does not say whether a later version may be used, and a fork cannot grant a permission its upstream never gave. The README says so in as many words. If OuterTune or InnerTune ever declare or-later, this follows them and the recipe changes with it.
4. Expect two questions. The native library, answered by it being built from source in the submodule. And the anti-feature, which is `NonFreeNet` because half of what the app does is talk to YouTube Music; declare it rather than argue.

Upstream OuterTune is already in fdroiddata, and its recipe is the best documentation there is for this build: it hits the same submodule on the same infrastructure. Two things it does that a first attempt would miss. It installs cmake by hand in a prebuild line, because taglib's CMakeLists demands 3.31.6 and the server does not carry it. And it keeps the taglib submodule out of the source scanner with `scandelete`. Both are copied here. It also declares `GPL-3.0-or-later` where this says only; that is worth raising with whoever reviews, since the parent reading or-later and the fork reading only is the kind of thing a packager will notice.

The NDK question is settled rather than left open. The taglib submodule pins `29.0.13113456`, which is new enough that the buildserver may not carry it, so the recipe names `28.2.13676358` (r28c) and rewrites the submodule's pin in a prebuild line. Tested on 12 September 2026: taglib built for all four ABIs, and the whole core release apk built with it, native library and all.

The `full` flavour's ffMetadataEx pins the same NDK. The recipe builds `core`, so it does not matter, but anybody switching the recipe to `full` has a second sed to write.

### The request, ready to paste

```
App name: REVIBE
Package ID: app.revibe.music
Source: https://github.com/revibe-music-code/REVIBE
Issue tracker: https://github.com/revibe-music-code/REVIBE/issues
Releases: https://github.com/revibe-music-code/REVIBE/releases (no releases are published yet; release builds not validated in this delivery)
License: GPL-3.0-only
Categories: Multimedia
Anti-Features: NonFreeNet

REVIBE is a local music player and YouTube Music client for Android. It is an
independent project derived from InterTune, which in turn comes from InnerTune
and OuterTune, and it credits that work. It plays local files (MP3, OGG, FLAC
and more, read with its own tag extractor), plays and downloads from YouTube
Music, shows synced lyrics, and keeps its recommendations on the device.

It builds with gradle from the tag and has no proprietary dependencies: no Play
services, no Firebase, no analytics library. Credentials in services.properties
are public placeholders; real values are supplied locally through local.properties
(git-ignored) or build variants and are never published, so a build from source
simply does not offer Last.fm login or in-app polls.

The one native library, libtaglib.so, is built by the NDK from the C++ in the
taglib submodule.

The in-app update check is opt-in, off until the user says yes during setup,
and it disables itself entirely when the app was installed by an F-Droid
client, pointing at the F-Droid page instead.

A build recipe is drafted at
https://github.com/revibe-music-code/REVIBE/blob/main/docs/fdroid/dev.skye.intertune.yml
(legacy filename; install it as metadata/app.revibe.music.yml)
```

## What happened to the first request, 12 September 2026

Posted as <https://gitlab.com/fdroid/rfp/-/work_items/4387> and closed by `fdroid-bot` six minutes later. The labels it left say why: `in-fdroiddata`, and `com.dd3boh.outertune`. That is upstream's package, already in F-Droid, and it is not what this app installs as. The bot read the `namespace` in `app/build.gradle.kts`, which the fork inherited along with the package structure, rather than the `applicationId` two lines below it (then `dev.skye.intertune`, now `app.revibe.music`).

The reply is in `docs/fdroid/rfp-reply.md`: the correction, the evidence from the published apk, and an offer to bring a merge request instead. Worth knowing for later: the namespace and the application id disagreeing will confuse more than one tool. Renaming the namespace touches every generated `R` and `BuildConfig` reference in the app, so it is a day's careful work rather than a line, but it is the real fix and it is worth doing before the next store or repository looks at this.

## Opening the merge request to fdroiddata

Their submission queue says it plainly: packaging it yourself and opening a merge request saves everyone a lot of time, and an issue on its own guarantees nothing. This is that, step by step. Nothing here needs anything from this REVIBE repository except the one file.

1. **Fork fdroiddata** at <https://gitlab.com/fdroid/fdroiddata> in the browser. It is large; a shallow clone is fine.

   ```bash
   git clone --depth 1 https://gitlab.com/<your-gitlab-user>/fdroiddata.git
   cd fdroiddata
   git checkout -b app.revibe.music
   ```

   They ask that the branch be named after the app id, which is what that is.

2. **Add the one file.** It goes in `metadata`, named after the application id exactly.

   ```bash
   cp <REPO_ROOT>/docs/fdroid/dev.skye.intertune.yml metadata/app.revibe.music.yml
   ```

   Drop the commented Binaries and AllowedAPKSigningKeys lines at the bottom, and the long comment header if it reads as noise beside the other files. The rest is meant to stay.

3. **Check it, if fdroidserver is installed.** `pipx install fdroidserver` gets it. None of this is required to open the merge request, and their CI runs the same checks, but finding a problem locally is faster than finding it in a pipeline.

   ```bash
   fdroid readmeta                      # does the file parse
   fdroid rewritemeta app.revibe.music # their own formatting, applied
   fdroid lint app.revibe.music       # warnings
   fdroid build -v -l app.revibe.music # the real test: does it build
   ```

   The build one is the whole point and takes a while. It needs their buildserver or a local android SDK with the NDK and cmake the recipe names.

4. **Commit and push.**

   ```bash
   git add metadata/app.revibe.music.yml
   git commit -m "New app: REVIBE"
   git push origin app.revibe.music
   ```

5. **Watch your fork's CI.** GitLab runs their pipeline on the push. If it fails, the log says which check, and the fix is usually one line of metadata.

6. **Open the merge request** against `fdroid/fdroiddata`, fill in their template, and link the request for packaging issue so the two are tied together. Then answer questions; commits get squashed when it is merged.

## What every release has to do from now on

- Bump `versionCode` and `versionName` in `app/build.gradle.kts`.
- Write `fastlane/metadata/android/en-US/changelogs/<versionCode>.txt`. F-Droid shows exactly this file, so it is the changelog people read.
- Tag it `vX.Y.Z` and let the workflow build it.
- Add a `Builds:` entry to the recipe draft, with the version name and code in the output path.

## What an F-Droid build will not have

Both are settings that hide themselves rather than failing, because the real values are never published:

- **Last.fm scrobbling**, which needs an API key supplied locally through `local.properties` or a build variant.
- **The questions**, which need the poll endpoints supplied the same way.

`services.properties` carries public placeholders only; no secret is committed. Everything else, including the local player, YouTube Music, downloads, lyrics, the recommendation engine and the home screen widget, is in the build from source.
