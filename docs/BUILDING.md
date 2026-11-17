# Building REVIBE

Upstream `v0.10.1` does not build from a clean checkout today. Two of the blockers are already
fixed as commits here: a dead JitPack pin on NewPipeExtractor, and a licence check that rejected
GPL-3.0-or-later. The rest is toolchain you install yourself, listed below.

```bash
git clone https://github.com/revibe-music-code/REVIBE REVIBE
cd REVIBE
./gradlew assembleCoreDebug
```

No submodule bootstrap is needed: `ffMetadataEx`, `taglib` and `innertubex` are vendored source
trees committed to this repository. Do not run `git submodule update --init --recursive`; there
are no gitlinks to populate, including the TagLib sources nested under `taglib/src/main/cpp`.

## What you need

**JDK 21.** `:app` and the Kotlin library modules declare `jvmToolchain(17)`, so Gradle can
provision 17 for them, but `:taglib` declares no toolchain and compiles at Java 21, meaning the
Gradle daemon's own JDK has to be 21 or newer. On JDK 17 the build fails in `:taglib` with
`invalid source release: 21`. CI builds on Temurin 21.

| Needed | Why |
|---|---|
| `platforms;android-36` | `compileSdk = 36` |
| `ndk;29.0.13113456` | native module `taglib` (all flavours) and `ffMetadataEx` (`full` only) |
| `cmake;3.31.6` | native builds of `taglib` (all flavours) and `ffMetadataEx` (`full` only) |
| SDK licences accepted | `sdkmanager --licenses` |
| FFmpeg native libs in `ffMetadataEx/ffmpeg-android-maker/output/lib` | `full` only; see below |

## Flavours and build types

Flavours are `core` (default) and `full`, which adds the FFmpeg audio decoders for ALAC, APE,
WavPack and DSD along with the FFmpeg metadata tag extractor. Build types are `release`, `debug`,
`userdebug` and `preview`. Debug builds use the applicationId suffix `.debug`, so a debug and a release
build coexist on one device.

The manual `workflow_dispatch` release workflows build both flavours and attach them to a draft
GitHub release; they do not publish test APKs. The `full` variant needs extra setup for
`ffMetadataEx`: the FFmpeg binaries are an external native dependency (not a Git submodule) and
must be present under `ffMetadataEx/ffmpeg-android-maker/output/lib`. The workflows clone
`mikooomich/ffmpeg-android-maker-prebuilt` (branch `audio`) into `ffMetadataEx/ffmpeg-android-maker`
before the `full` build. For the general procedure, see
[the vendored ffMetadataEx README](../ffMetadataEx/README.md#building). `FullDebug` was compiled
successfully in a clean simulated checkout with those libraries present; installation/runtime was
not exercised. Release, preview and userdebug builds remain unverified in this delivery.

`CONTRIBUTING.md` lists the update checker as a `full`-only feature. That was true upstream; in
InterTune `UpdateChecker` sits in the main source set and both flavours have it.

## Signing a release build

Release builds are signed from a gitignored `keystore.properties` at the repo root:

```properties
storeFile=/path/to/your.jks
keyAlias=youralias
storePassword=...
keyPassword=...
```

Without that file the release variant has no signing config and will not assemble.

REVIBE installs alongside InterTune/OuterTune because its applicationId is `app.revibe.music`, not
upstream's `dev.skye.intertune` or `com.dd3boh.outertune`. Its releases are signed with a different
key as well, which is why a build you sign yourself cannot update an upstream release installed
from GitHub, and vice versa.

## Notes

On a memory-constrained machine, `--max-workers=2` avoids the OOM killer during the native
build.

Pull request and commit conventions are in [CONTRIBUTING.md](../CONTRIBUTING.md), which is still
upstream's document and describes upstream's process.
