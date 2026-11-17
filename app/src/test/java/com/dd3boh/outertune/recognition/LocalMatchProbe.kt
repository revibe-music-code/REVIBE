/*
 * Copyright (C) 2026 InterTune
 *
 * SPDX-License-Identifier: GPL-3.0
 */

package com.dd3boh.outertune.recognition

import com.dd3boh.outertune.fingerprint.DecodedSignature
import com.dd3boh.outertune.fingerprint.SIGNATURE_SAMPLE_RATE_HZ
import com.dd3boh.outertune.fingerprint.SignatureGenerator
import org.junit.Assume.assumeTrue
import org.junit.Test
import java.io.File
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.file.Files
import kotlin.random.Random

/**
 * Whether room audio can be checked against a candidate recording on the phone, with no Shazam in
 * between: the "compare it to the actual recording" idea of 24 Sep, for telling which of several
 * uploads is the one playing.
 *
 * The app's fingerprints are Shazam's peaks: a time, a frequency and a magnitude per peak. Pairs of
 * nearby peaks make hashes that survive noise, and a window from the same recording lines its
 * hashes up at one time offset against the recording's, where another recording's scatter. That is
 * Wang's 2003 method, the one Shazam itself grew from. This measures, on deterministic synthetic
 * fixtures (or on a directory supplied by the caller), how far apart the same recording and a
 * different one score.
 *
 * Historic measurements from 25 Sep, on a local library that is not committed here, found the best
 * offset standing out by 1.7x to 41x for the same recording, 4.4x through simulated room noise, and
 * at the right place to 0.1 s; other recordings scored 1.0x to 1.5x, and 2 %/4 % tempo variants
 * scored 1.1x and 1.3x. The weakest case was a quiet passage landing near the ceiling of the other
 * recordings, which two windows agreeing on one offset would settle. The numbers are historical
 * context only; this fixture-based probe reports fresh scores for the current generator.
 *
 * No personal library paths or real track names are stored here. By default, deterministic WAV
 * fixtures are generated in a temporary directory. To use your own local files instead, set
 * LOCAL_MATCH_DIR to a directory containing the six WAV files named in [fixtureNames].
 *
 * Skipped unless LOCAL_MATCH=1, since it reads/decodes audio and invokes ffmpeg.
 *
 *     LOCAL_MATCH=1 ./gradlew :app:testCoreDebugUnitTest --tests "*LocalMatchProbe*" -i
 */
class LocalMatchProbe {

    private val dir: File by lazy {
        System.getenv("LOCAL_MATCH_DIR")?.let(::File) ?: createSyntheticFixtures()
    }

    @Test
    fun probe() {
        assumeTrue(System.getenv("LOCAL_MATCH") == "1")
        val referenceA = decode(File(dir, "reference-a.wav"))
        val referenceARip = decode(File(dir, "reference-a-rip.wav"))
        val referenceB = decode(File(dir, "reference-b.wav"))
        val referenceBEdit = decode(File(dir, "reference-b-edit.wav"))
        val referenceC = decode(File(dir, "reference-c.wav"))
        val referenceD = decode(File(dir, "reference-d.wav"))
        val references = listOf(
            "Reference A" to referenceA,
            "Reference B" to referenceB,
            "Reference C" to referenceC,
            "Reference D" to referenceD,
        ).map { (name, audio) -> name to Index(SignatureGenerator.makeSignature(audio)) }

        fun room(name: String, audio: ShortArray, fromS: Int, noiseDb: Double = -12.0) {
            val window = noisy(audio.copyOfRange(fromS * SIGNATURE_SAMPLE_RATE_HZ, (fromS + 12) * SIGNATURE_SAMPLE_RATE_HZ), noiseDb)
            val query = SignatureGenerator.makeSignature(window)
            val line = references.joinToString("  ") { (ref, index) ->
                val m = index.match(query)
                "%s %5.1fx @%.1fs".format(ref, m.score, m.offsetS)
            }
            println("LOCAL %-44s %s".format("$name @$fromS s", line))
        }
        room("A, another rip", referenceARip, 8)
        room("A, another rip", referenceARip, 20)
        room("A, another rip, louder room", referenceARip, 20, noiseDb = -3.0)
        room("B edit", referenceBEdit, 4)
        room("B edit", referenceBEdit, 14)
        room("B edit", referenceBEdit, 24)
        room("C", referenceC, 12)
        room("D", referenceD, 8)
        room("A through a room", room(referenceARip), 8)
        room("A through a room, noisy", room(referenceARip), 8, noiseDb = -6.0)
        room("C through a room, noisy", room(referenceC), 12, noiseDb = -6.0)
        room("B edit through a room, noisy", room(referenceBEdit), 14, noiseDb = -6.0)
        room("A 4% faster (another version)", tempo(referenceARip, 1.04), 8)
        room("A 2% faster", tempo(referenceARip, 1.02), 8)
    }

    class Match(val score: Double, val offsetS: Double)

    /** A recording's hashes, each with where in it the hash starts. */
    class Index(signature: DecodedSignature) {
        private val table = HashMap<Long, MutableList<Int>>()
        init { hashes(signature).forEach { (hash, t) -> table.getOrPut(hash) { ArrayList() }.add(t) } }

        /**
         * How far the best offset stands out from the next best, and that offset. A different
         * recording's votes scatter, and so do those of the same song at another tempo, whose
         * offsets drift across the window: both come out near 1.
         */
        fun match(query: DecodedSignature): Match {
            val qs = hashes(query)
            if (qs.isEmpty()) return Match(0.0, 0.0)
            val votes = HashMap<Int, Int>()
            for ((hash, tq) in qs) table[hash]?.forEach { tr -> votes.merge((tr - tq) / 4, 1, Int::plus) }
            // Neighbouring offset buckets together, since a peak can land one pass either side.
            val best = votes.keys.maxByOrNull { k -> (votes[k] ?: 0) + (votes[k - 1] ?: 0) + (votes[k + 1] ?: 0) } ?: return Match(0.0, 0.0)
            fun around(k: Int) = (votes[k] ?: 0) + (votes[k - 1] ?: 0) + (votes[k + 1] ?: 0)
            val next = votes.keys.filter { kotlin.math.abs(it - best) > 2 }.maxOfOrNull(::around) ?: 0
            return Match(around(best).toDouble() / maxOf(1, next), best * 4 * PASS_S)
        }
    }

    companion object {
        /** One pass of the generator: 128 samples at 16 kHz. */
        const val PASS_S = 128.0 / SIGNATURE_SAMPLE_RATE_HZ

        /** Names expected in LOCAL_MATCH_DIR, and created by the synthetic fallback. */
        val fixtureNames = listOf(
            "reference-a.wav",
            "reference-a-rip.wav",
            "reference-b.wav",
            "reference-b-edit.wav",
            "reference-c.wav",
            "reference-d.wav",
        )

        private const val FIXTURE_SECONDS = 48

        /**
         * Deterministic, self-contained fixtures. No real music, backup paths or device-specific
         * data are embedded; the same seeds produce the same WAVs on every machine.
         */
        private fun createSyntheticFixtures(): File {
            val dir = Files.createTempDirectory("revibe-local-match-").toFile()
            dir.deleteOnExit()
            val fixtures = listOf(
                Triple("reference-a.wav", 7, 0.000),
                Triple("reference-a-rip.wav", 7, 0.002),
                Triple("reference-b.wav", 19, 0.000),
                Triple("reference-b-edit.wav", 19, 0.003),
                Triple("reference-c.wav", 31, 0.000),
                Triple("reference-d.wav", 43, 0.000),
            )
            fixtures.forEach { (name, seed, detune) ->
                val file = File(dir, name)
                writeWav(file, syntheticTrack(seed, detune, trimStartS = if (name == "reference-b-edit.wav") 4 else 0))
                file.deleteOnExit()
            }
            return dir
        }

        private fun syntheticTrack(
            seed: Int,
            detune: Double,
            trimStartS: Int,
            noiseLevel: Double = 0.06,
        ): ShortArray {
            val random = Random(seed)
            val phases = DoubleArray(5) { random.nextDouble() * 2 * Math.PI }
            val bases = doubleArrayOf(110.0, 220.0, 330.0, 550.0, 880.0)
            val length = FIXTURE_SECONDS * SIGNATURE_SAMPLE_RATE_HZ
            val startSample = trimStartS * SIGNATURE_SAMPLE_RATE_HZ
            return ShortArray(length) { i ->
                val t = (startSample + i).toDouble() / SIGNATURE_SAMPLE_RATE_HZ
                var value = 0.0
                for (k in bases.indices) {
                    val f = bases[k] * (1.0 + detune + 0.02 * kotlin.math.sin(t * (0.07 + 0.013 * k) + k))
                    value += kotlin.math.sin(2 * Math.PI * f * t + phases[k]) * (0.5 / (k + 1))
                }
                val envelope = 0.6 + 0.4 * kotlin.math.sin(2 * Math.PI * 0.13 * t + seed)
                val noise = (random.nextDouble() - 0.5) * noiseLevel
                ((value * envelope + noise) * 12000).coerceIn(-32768.0, 32767.0).toInt().toShort()
            }
        }

        private fun writeWav(file: File, samples: ShortArray) {
            val dataSize = samples.size * 2
            val buffer = ByteBuffer.allocate(44 + dataSize).order(ByteOrder.LITTLE_ENDIAN)
            fun ascii(value: String) = buffer.put(value.toByteArray(Charsets.US_ASCII))
            ascii("RIFF")
            buffer.putInt(36 + dataSize)
            ascii("WAVEfmt ")
            buffer.putInt(16)
            buffer.putShort(1)
            buffer.putShort(1)
            buffer.putInt(SIGNATURE_SAMPLE_RATE_HZ)
            buffer.putInt(SIGNATURE_SAMPLE_RATE_HZ * 2)
            buffer.putShort(2)
            buffer.putShort(16)
            ascii("data")
            buffer.putInt(dataSize)
            samples.forEach(buffer::putShort)
            Files.write(file.toPath(), buffer.array())
        }

        /**
         * Anchor and target peaks: frequency in pairs of bins (15.6 Hz), a target up to 0.8 s after
         * in steps of two passes, five per anchor. Twice as coarse as the generator's resolution
         * both ways scored best through a simulated room; finer lost matches to noise.
         */
        fun hashes(signature: DecodedSignature): List<Pair<Long, Int>> {
            val peaks = signature.peaksByBand.flatten()
                .map { it.fftPassNumber to it.correctedPeakFrequencyBin / 128 }
                .sortedWith(compareBy({ it.first }, { it.second }))
            val out = ArrayList<Pair<Long, Int>>()
            for (i in peaks.indices) {
                val (t1, f1) = peaks[i]
                var paired = 0
                var j = i + 1
                while (j < peaks.size && paired < 5) {
                    val (t2, f2) = peaks[j]
                    val dt = t2 - t1
                    if (dt > 100) break
                    if (dt >= 1 && kotlin.math.abs(f2 - f1) <= 200) {
                        out += ((f1.toLong() shl 20) or (f2.toLong() shl 8) or (dt / 2).toLong()) to t1
                        paired++
                    }
                    j++
                }
            }
            return out
        }

        /** White noise at [db] relative to the signal's own level, standing in for a room. */
        fun noisy(samples: ShortArray, db: Double): ShortArray {
            val rms = kotlin.math.sqrt(samples.sumOf { it.toDouble() * it } / samples.size)
            val sd = rms * Math.pow(10.0, db / 20)
            val random = Random(7)
            return ShortArray(samples.size) { i ->
                val gauss = kotlin.math.sqrt(-2 * kotlin.math.ln(random.nextDouble().coerceAtLeast(1e-12))) * kotlin.math.cos(2 * Math.PI * random.nextDouble())
                (samples[i] + gauss * sd).coerceIn(-32768.0, 32767.0).toInt().toShort()
            }
        }

        /** A phone's microphone across a room: no deep bass, no top end, and three early echoes. */
        fun room(samples: ShortArray): ShortArray = ffmpeg(samples, "highpass=f=150,lowpass=f=6000,aecho=0.8:0.5:35|70|110:0.35|0.25|0.15")

        /** Faster by [factor] without a change of pitch, as a remix at another tempo would be. */
        fun tempo(samples: ShortArray, factor: Double): ShortArray = ffmpeg(samples, "atempo=$factor")

        private fun ffmpeg(samples: ShortArray, filter: String): ShortArray {
            val bytes = ByteBuffer.allocate(samples.size * 2).order(ByteOrder.LITTLE_ENDIAN).apply { asShortBuffer().put(samples) }.array()
            val process = ProcessBuilder("ffmpeg", "-v", "error", "-f", "s16le", "-ar", "$SIGNATURE_SAMPLE_RATE_HZ", "-ac", "1", "-i", "-",
                "-af", filter, "-f", "s16le", "-ar", "$SIGNATURE_SAMPLE_RATE_HZ", "-ac", "1", "-").start()
            Thread { process.outputStream.use { it.write(bytes) } }.start()
            return pcm(process.inputStream.readBytes())
        }

        fun decode(file: File): ShortArray {
            val process = ProcessBuilder("ffmpeg", "-v", "error", "-i", file.absolutePath,
                "-f", "s16le", "-ac", "1", "-ar", "$SIGNATURE_SAMPLE_RATE_HZ", "-").start()
            return pcm(process.inputStream.readBytes())
        }

        private fun pcm(raw: ByteArray): ShortArray {
            val shorts = ShortArray(raw.size / 2)
            ByteBuffer.wrap(raw).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shorts)
            return shorts
        }
    }
}
