package com.example.ai

import com.example.domain.model.DescriptionGenRequest
import com.example.domain.model.GeneratedScript
import com.example.domain.model.HashtagGenRequest
import com.example.domain.model.ScriptGenRequest
import com.example.domain.model.ThumbnailTextGenRequest
import com.example.domain.model.TitleGenRequest
import com.example.domain.model.VideoIdeaGenRequest
import kotlinx.coroutines.delay

class MockAiService : AiService {

    override suspend fun generateTitles(request: TitleGenRequest): Result<List<String>> {
        delay(600) // Simulate processing
        val game = request.game.ifBlank { "Gaming" }
        val topic = request.topic.ifBlank { "Epic Gameplay" }
        val style = request.style

        val titles = when (style.lowercase()) {
            "boss fight" -> listOf(
                "$game: DEFEATING THE FINAL $topic! 😱🔥",
                "INSANE $topic Boss Fight Battle in $game!",
                "Can I Defeat The Ultimate $topic? (IMPOSSIBLE)",
                "$game - SECRET $topic Boss Destroyed With 1 HP!",
                "World First $topic Solo Win! ($game)"
            )
            "challenge" -> listOf(
                "I Tried The $topic Challenge in $game...",
                "100 Days Of $topic In $game (Hardcore Mode)",
                "Is $topic Actually Possible in $game?! 🤯",
                "Beating $game Doing Only $topic (NO UPGRADES)",
                "The Hardest $topic Challenge Ever Attempted!"
            )
            "funny" -> listOf(
                "$game But $topic Went Completely Wrong 😂",
                "When You Try $topic And Instant Regret Hits...",
                "Funniest $topic Moments & Glitches in $game",
                "Trolling Friends With $topic In $game!",
                "I Hired A Pro For $topic In $game (HILARIOUS)"
            )
            "tutorial" -> listOf(
                "$game: How To Master $topic (Beginner to Pro Guide)",
                "The SECRET Trick To $topic In $game You Didn't Know!",
                "5 Proven Tips To Conquer $topic in $game Fast",
                "Ultimate $topic Strategy Guide - $game 2026",
                "Never Fail $topic Again In $game With This Strategy!"
            )
            "update" -> listOf(
                "$game NEW UPDATE: $topic Changed EVERYTHING! 🚨",
                "Testing The Brand New $topic In $game!",
                "$game Just Added $topic & It's Completely Broken...",
                "Secret Hidden Features In The $topic Update ($game)",
                "Is The New $topic In $game Worth It? (Honest Review)"
            )
            else -> listOf(
                "$game $topic - INSANE Gameplay & Highlights! 🔥",
                "Can We Pull Off $topic in $game?!",
                "$game But Every Second $topic Gets Harder!",
                "The Most EPIC $topic Moment in $game History!",
                "$game: The Truth About $topic (You Won't Believe This)"
            )
        }
        return Result.success(titles)
    }

    override suspend fun generateDescription(request: DescriptionGenRequest): Result<String> {
        delay(700)
        val game = request.game.ifBlank { "Gaming" }
        val title = request.title.ifBlank { "Epic Gaming Session" }
        val topic = request.topic.ifBlank { "Ultimate Battle" }
        val keywords = if (request.keywords.isNotBlank()) "Keywords: ${request.keywords}\n" else ""

        val description = buildString {
            appendLine("🎮 $title")
            appendLine("Welcome back, Gaming Squad! In today's video, we are jumping into $game for $topic!")
            appendLine()
            appendLine("⚔️ WHAT HAPPENS IN THIS VIDEO:")
            appendLine("Watch as we push our skills to the absolute limit facing $topic in $game. From intense clutches to insane surprises, you don't want to miss a single second!")
            appendLine()
            appendLine("🔔 Don't forget to LIKE the video, SUBSCRIBE to the channel, and turn on NOTIFICATIONS so you never miss another upload!")
            appendLine()
            appendLine("📌 CHAPTERS & HIGHLIGHTS:")
            appendLine("00:00 - Epic Intro & Challenge Setup")
            appendLine("01:45 - The First Wave")
            appendLine("04:30 - The Secret Strategy Unlocked")
            appendLine("08:15 - Final $topic Showdown")
            appendLine("12:00 - Victory Celebration & Outro")
            appendLine()
            appendLine("💬 COMMUNITY QUESTION:")
            appendLine("What was your favorite moment from this run? Let me know down in the comments!")
            appendLine()
            if (keywords.isNotBlank()) {
                appendLine(keywords)
            }
            appendLine("⚠️ DISCLAIMER:")
            appendLine("This gameplay footage is recorded and edited for entertainment purposes. All in-game rights belong to their respective developers and publishers.")
            appendLine()
            val cleanGame = game.replace(" ", "")
            val cleanTopic = topic.replace(" ", "")
            appendLine("#$cleanGame #$cleanTopic #Gaming #Gameplay #GamingCreator")
        }
        return Result.success(description)
    }

    override suspend fun generateHashtags(request: HashtagGenRequest): Result<List<String>> {
        delay(500)
        val gameTag = request.game.replace(" ", "").replace("[^a-zA-Z0-9]".toRegex(), "")
        val topicTag = request.topic.replace(" ", "").replace("[^a-zA-Z0-9]".toRegex(), "")

        val tags = mutableListOf<String>()
        if (gameTag.isNotBlank()) tags.add("#$gameTag")
        if (topicTag.isNotBlank()) tags.add("#$topicTag")
        tags.addAll(
            listOf(
                "#Gaming",
                "#GamingVideos",
                "#Gameplay",
                "#Gamer",
                "#GamerLife",
                "#YouTuber",
                "#GamingCommunity",
                "#GamingClips",
                "#ProGamer",
                "#GamingSetup",
                "#LetsPlay",
                "#TrendingGaming"
            )
        )
        return Result.success(tags.distinct().take(12))
    }

    override suspend fun generateVideoIdeas(request: VideoIdeaGenRequest): Result<List<String>> {
        delay(600)
        val game = request.game.ifBlank { "Gaming" }
        val category = request.category.ifBlank { "Challenge" }
        val difficulty = request.difficulty.ifBlank { "Hard" }

        val ideas = listOf(
            "Beating $game on $difficulty Difficulty Without Taking Any Damage",
            "100 Days Hardcore Survival Challenge in $game",
            "I Tried Using Only Starter Gear To Win In $game",
            "Can You Beat $game Without Ever Jumping / Sprinting?",
            "Fighting Every Secret Boss in $game in One Single Run",
            "Top 10 Hidden Glitches and Easter Eggs in $game ($category Edition)",
            "Beating $game But The Game Gets 10% Faster Every Minute",
            "Solo vs Squad Clutch In High Rank Lobby ($game)",
            "Testing The Most Overpowered Weapon / Strat In $game",
            "Spending 24 Hours Straight Grinding In $game - Was It Worth It?"
        )
        return Result.success(ideas)
    }

    override suspend fun generateThumbnailText(request: ThumbnailTextGenRequest): Result<List<String>> {
        delay(400)
        val topic = request.topic.trim().uppercase()
        val textSnippets = if (topic.isNotBlank()) {
            listOf(
                "$topic!",
                "IMPOSSIBLE $topic",
                "FINAL $topic 😱",
                "$topic CLUTCH!",
                "SECRET $topic",
                "NO WAY!",
                "100% UNBEATEN",
                "HE'S BACK!"
            )
        } else {
            listOf(
                "FINAL BOSS! 😱",
                "IMPOSSIBLE!",
                "30 KILLS!",
                "HE'S BACK!",
                "INSANE WAR!",
                "NO WAY!",
                "WORLD RECORD?!",
                "1 HP CLUTCH!"
            )
        }
        return Result.success(textSnippets)
    }

    override suspend fun generateVideoScript(request: ScriptGenRequest): Result<GeneratedScript> {
        delay(800)
        val game = request.game.ifBlank { "Gaming" }
        val topic = request.topic.ifBlank { "Epic Boss Challenge" }
        val length = request.length.ifBlank { "10 Minutes" }

        val script = GeneratedScript(
            title = "$game: $topic ($length Run)",
            hook = "Stop scrolling! If you thought $topic in $game was impossible, wait until you see what happens at the final 30 seconds of this run!",
            intro = "What's going on gamers! Welcome back to the channel. Today we're taking on the challenge you've all been spamming my comments with: $topic in $game.",
            gameplayBeats = listOf(
                "Stage 1 - Setup & Gear Preparation: Explain rules, show our starting inventory, and preview what dangers lie ahead.",
                "Stage 2 - First Major Encounter: We face the early obstacle and almost lose everything due to a surprise attack.",
                "Stage 3 - Turning Point: We discover the golden timing or combo that allows us to bounce back.",
                "Stage 4 - The Climax / Boss Encounter: The final showdown where every input has to be frame-perfect.",
                "Stage 5 - Victory and Loot Reveal: We finally achieve the goal and showcase the rewards."
            ),
            commentaryNotes = listOf(
                "React with genuine excitement when the boss enters second phase.",
                "Mention your previous failed attempts to build anticipation and tension.",
                "Ask viewers in the chat or comments what their favorite strategy is for this section.",
                "Maintain high energy during the countdown and clutch moments."
            ),
            outro = "And that is how you conquer $topic in $game! If you enjoyed this video, hit that thumbs up button.",
            callToAction = "Subscribe for more gaming challenges every single week. Click the video on screen right now to see our previous insane adventure!"
        )
        return Result.success(script)
    }

    override suspend fun removeBackground(imageUri: String): Result<String> {
        delay(1000)
        // Note: For dev/offline mode, marks image as cutout mode so UI knows to apply edge contrast filter
        return Result.success(imageUri)
    }
}
