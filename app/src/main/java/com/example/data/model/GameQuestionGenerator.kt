package com.example.data.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object GameQuestionGenerator {

    fun generateQuestions(gameId: String, level: Int): List<GameQuestion> {
        val calendar = java.util.Calendar.getInstance()
        val dayOfWeek = SimpleDateFormat("EEEE", Locale.ENGLISH).format(Date())
        val monthName = SimpleDateFormat("MMMM", Locale.ENGLISH).format(Date())
        val dayNumber = SimpleDateFormat("d", Locale.ENGLISH).format(Date())
        val hour = calendar.get(java.util.Calendar.HOUR_OF_DAY)
        val timePeriod = when (hour) {
            in 5..11 -> "Morning"
            in 12..16 -> "Afternoon"
            in 17..20 -> "Evening"
            else -> "Night"
        }

        return when (gameId) {
            "day_date" -> when (level) {
                1 -> listOf(
                    GameQuestion(
                        id = "dd_1_1",
                        promptKey = "What period of the day is it right now?",
                        promptTextFallback = "What period of the day is it right now?",
                        visualEmoji = if (timePeriod == "Night" || timePeriod == "Evening") "🌙" else "☀️",
                        options = listOf(timePeriod, if (timePeriod == "Morning") "Night" else "Morning"),
                        correctIndex = 0
                    ),
                    GameQuestion(
                        id = "dd_1_2",
                        promptKey = "Which day is it today?",
                        promptTextFallback = "Which day is it today?",
                        visualEmoji = "📅",
                        options = listOf(dayOfWeek, if (dayOfWeek == "Sunday") "Wednesday" else "Sunday"),
                        correctIndex = 0
                    )
                )
                2 -> listOf(
                    GameQuestion(
                        id = "dd_2_1",
                        promptKey = "Which month are we in currently?",
                        promptTextFallback = "Which month are we in currently?",
                        visualEmoji = "🗓️",
                        options = listOf(monthName, "January", "July").distinct().take(3).let { list ->
                            if (list.size < 3) list + listOf("December") else list
                        },
                        correctIndex = 0
                    ),
                    GameQuestion(
                        id = "dd_2_2",
                        promptKey = "What is today's day of the week?",
                        promptTextFallback = "What is today's day of the week?",
                        visualEmoji = "☀️",
                        options = listOf(dayOfWeek, "Friday", "Monday").distinct().take(3).let { list ->
                            if (list.size < 3) list + listOf("Sunday") else list
                        },
                        correctIndex = 0
                    ),
                    GameQuestion(
                        id = "dd_2_3",
                        promptKey = "What comes after today ($dayOfWeek)?",
                        promptTextFallback = "What comes after today ($dayOfWeek)?",
                        visualEmoji = "⏩",
                        options = run {
                            calendar.add(java.util.Calendar.DAY_OF_YEAR, 1)
                            val nextDay = SimpleDateFormat("EEEE", Locale.ENGLISH).format(calendar.time)
                            calendar.add(java.util.Calendar.DAY_OF_YEAR, -1)
                            listOf(nextDay, "Tuesday", "Saturday").distinct().take(3).let { list ->
                                if (list.size < 3) list + listOf("Sunday") else list
                            }
                        },
                        correctIndex = 0
                    )
                )
                else -> listOf(
                    GameQuestion(
                        id = "dd_3_1",
                        promptKey = "Identify today's date and month:",
                        promptTextFallback = "Identify today's date and month:",
                        visualEmoji = "📆",
                        options = listOf("$dayNumber $monthName", "15 January", "22 October", "1 May"),
                        correctIndex = 0
                    ),
                    GameQuestion(
                        id = "dd_3_2",
                        promptKey = "If today is $dayOfWeek, which day was yesterday?",
                        promptTextFallback = "If today is $dayOfWeek, which day was yesterday?",
                        visualEmoji = "⏪",
                        options = run {
                            calendar.add(java.util.Calendar.DAY_OF_YEAR, -1)
                            val prevDay = SimpleDateFormat("EEEE", Locale.ENGLISH).format(calendar.time)
                            calendar.add(java.util.Calendar.DAY_OF_YEAR, 1)
                            listOf(prevDay, "Monday", "Wednesday", "Friday").distinct().take(4).let { list ->
                                if (list.size < 4) list + listOf("Sunday") else list
                            }
                        },
                        correctIndex = 0
                    ),
                    GameQuestion(
                        id = "dd_3_3",
                        promptKey = "Which season brings warm monsoon rains to North East India?",
                        promptTextFallback = "Which season brings warm monsoon rains to North East India?",
                        visualEmoji = "🌧️",
                        options = listOf("Monsoon / Rainy", "Mid-Winter Snow", "Dry Desert Heat", "Spring Frost"),
                        correctIndex = 0
                    )
                )
            }

            "place_time" -> when (level) {
                1 -> listOf(
                    GameQuestion(
                        id = "pt_1_1",
                        promptKey = "Where in the house do we sleep and rest peacefully?",
                        promptTextFallback = "Where in the house do we sleep and rest peacefully?",
                        visualEmoji = "🛏️",
                        options = listOf("Cozy Bedroom", "Kitchen Stove"),
                        correctIndex = 0
                    ),
                    GameQuestion(
                        id = "pt_1_2",
                        promptKey = "Where do we prepare warm tea and food?",
                        promptTextFallback = "Where do we prepare warm tea and food?",
                        visualEmoji = "🫖",
                        options = listOf("Kitchen", "Bathroom"),
                        correctIndex = 0
                    )
                )
                2 -> listOf(
                    GameQuestion(
                        id = "pt_2_1",
                        promptKey = "In which North Eastern state is the Brahmaputra River and Kaziranga located?",
                        promptTextFallback = "In which North Eastern state is the Brahmaputra River and Kaziranga located?",
                        visualEmoji = "🦏",
                        options = listOf("Assam", "Goa", "Rajasthan"),
                        correctIndex = 0
                    ),
                    GameQuestion(
                        id = "pt_2_2",
                        promptKey = "Where are the famous Living Root Bridges and misty hills found?",
                        promptTextFallback = "Where are the famous Living Root Bridges and misty hills found?",
                        visualEmoji = "🌿",
                        options = listOf("Meghalaya", "Gujarat", "Haryana"),
                        correctIndex = 0
                    ),
                    GameQuestion(
                        id = "pt_2_3",
                        promptKey = "Where do we keep clean drinking water at home?",
                        promptTextFallback = "Where do we keep clean drinking water at home?",
                        visualEmoji = "💧",
                        options = listOf("Clean Water Filter/Jug", "Shoe Rack", "Laundry Basket"),
                        correctIndex = 0
                    )
                )
                else -> listOf(
                    GameQuestion(
                        id = "pt_3_1",
                        promptKey = "Loktak Lake, famous for floating Phumdis, is in which state?",
                        promptTextFallback = "Loktak Lake, famous for floating Phumdis, is in which state?",
                        visualEmoji = "🏞️",
                        options = listOf("Manipur", "Punjab", "Kerala", "Bihar"),
                        correctIndex = 0
                    ),
                    GameQuestion(
                        id = "pt_3_2",
                        promptKey = "The capital of Nagaland known for the Hornbill Festival is:",
                        promptTextFallback = "The capital of Nagaland known for the Hornbill Festival is:",
                        visualEmoji = "🦜",
                        options = listOf("Kohima", "Agartala", "Shillong", "Aizawl"),
                        correctIndex = 0
                    ),
                    GameQuestion(
                        id = "pt_3_3",
                        promptKey = "At 8:00 in the morning, which activity fits best?",
                        promptTextFallback = "At 8:00 in the morning, which activity fits best?",
                        visualEmoji = "🥣",
                        options = listOf("Eating breakfast & taking morning tea", "Midnight sleeping", "Turning off all lights", "Watching night stars"),
                        correctIndex = 0
                    )
                )
            }

            "remember_objects" -> when (level) {
                1 -> listOf(
                    GameQuestion(
                        id = "ro_1_1",
                        promptKey = "Which object did you just see?",
                        promptTextFallback = "Which object did you just see?",
                        visualEmoji = "🍵",
                        options = listOf("Tea Cup 🍵", "Umbrella ☂️"),
                        correctIndex = 0
                    ),
                    GameQuestion(
                        id = "ro_1_2",
                        promptKey = "Which musical instrument was displayed?",
                        promptTextFallback = "Which musical instrument was displayed?",
                        visualEmoji = "🔔",
                        options = listOf("Golden Bell 🔔", "Car Key 🔑"),
                        correctIndex = 0
                    )
                )
                2 -> listOf(
                    GameQuestion(
                        id = "ro_2_1",
                        promptKey = "Which fruit was shown in the memory set?",
                        promptTextFallback = "Which fruit was shown in the memory set?",
                        visualEmoji = "🍎",
                        options = listOf("Red Apple 🍎", "Chili Pepper 🌶️", "Screwdriver 🪛"),
                        correctIndex = 0
                    ),
                    GameQuestion(
                        id = "ro_2_2",
                        promptKey = "Which North East regional flower was shown?",
                        promptTextFallback = "Which North East regional flower was shown?",
                        visualEmoji = "🌸",
                        options = listOf("Pink Orchid 🌸", "Cactus 🌵", "Pinecone 🌲"),
                        correctIndex = 0
                    ),
                    GameQuestion(
                        id = "ro_2_3",
                        promptKey = "Which item keeps us warm in chilly mountain weather?",
                        promptTextFallback = "Which item keeps us warm in chilly mountain weather?",
                        visualEmoji = "🧣",
                        options = listOf("Warm Shawl / Scarf 🧣", "Swimsuit 🩱", "Plastic Spoon 🥄"),
                        correctIndex = 0
                    )
                )
                else -> listOf(
                    GameQuestion(
                        id = "ro_3_1",
                        promptKey = "Select the 2 objects that appeared on your screen:",
                        promptTextFallback = "Select the object that appeared on your screen:",
                        visualEmoji = "🧺",
                        options = listOf("Bamboo Basket 🧺", "Ice Cream 🍦", "Airplane ✈️", "Bicycle 🚲"),
                        correctIndex = 0
                    ),
                    GameQuestion(
                        id = "ro_3_2",
                        promptKey = "Which morning reading item was in the collection?",
                        promptTextFallback = "Which morning reading item was in the collection?",
                        visualEmoji = "📰",
                        options = listOf("Newspaper 📰", "Hammer 🔨", "Torch 🔦", "Tennis Racket 🎾"),
                        correctIndex = 0
                    ),
                    GameQuestion(
                        id = "ro_3_3",
                        promptKey = "Which bird of North East was in the memory cards?",
                        promptTextFallback = "Which bird of North East was in the memory cards?",
                        visualEmoji = "🦜",
                        options = listOf("Great Hornbill 🦜", "Penguin 🐧", "Ostrich 🦤", "Flamingo 🦩"),
                        correctIndex = 0
                    )
                )
            }

            "find_target" -> when (level) {
                1 -> listOf(
                    GameQuestion(
                        id = "ft_1_1",
                        promptKey = "Touch the bright shining Sun ☀️:",
                        promptTextFallback = "Touch the bright shining Sun ☀️:",
                        visualEmoji = "☀️",
                        options = listOf("☀️ Sun", "🌧️ Rain"),
                        correctIndex = 0
                    ),
                    GameQuestion(
                        id = "ft_1_2",
                        promptKey = "Find the fresh Green Leaf 🌿:",
                        promptTextFallback = "Find the fresh Green Leaf 🌿:",
                        visualEmoji = "🌿",
                        options = listOf("🌿 Leaf", "🔥 Flame"),
                        correctIndex = 0
                    )
                )
                2 -> listOf(
                    GameQuestion(
                        id = "ft_2_1",
                        promptKey = "Spot the majestic Rhinoceros 🦏:",
                        promptTextFallback = "Spot the majestic Rhinoceros 🦏:",
                        visualEmoji = "🦏",
                        options = listOf("🦏 Rhino", "🐘 Elephant", "🐅 Tiger"),
                        correctIndex = 0
                    ),
                    GameQuestion(
                        id = "ft_2_2",
                        promptKey = "Find the warm cup of Assam Tea 🍵:",
                        promptTextFallback = "Find the warm cup of Assam Tea 🍵:",
                        visualEmoji = "🍵",
                        options = listOf("🍵 Tea Cup", "🥤 Cold Soda", "🍼 Baby Bottle"),
                        correctIndex = 0
                    ),
                    GameQuestion(
                        id = "ft_2_3",
                        promptKey = "Spot the musical Bell 🔔:",
                        promptTextFallback = "Spot the musical Bell 🔔:",
                        visualEmoji = "🔔",
                        options = listOf("🔔 Bell", "🥁 Drum", "🎺 Horn"),
                        correctIndex = 0
                    )
                )
                else -> listOf(
                    GameQuestion(
                        id = "ft_3_1",
                        promptKey = "Find the Red Apple among the fruits:",
                        promptTextFallback = "Find the Red Apple among the fruits:",
                        visualEmoji = "🍎",
                        options = listOf("🍎 Apple", "🍌 Banana", "🍇 Grapes", "🍊 Orange"),
                        correctIndex = 0
                    ),
                    GameQuestion(
                        id = "ft_3_2",
                        promptKey = "Find the Hornbill bird 🦜 among these animals:",
                        promptTextFallback = "Find the Hornbill bird 🦜 among these animals:",
                        visualEmoji = "🦜",
                        options = listOf("🦜 Hornbill", "🦚 Peacock", "🦉 Owl", "🦆 Duck"),
                        correctIndex = 0
                    ),
                    GameQuestion(
                        id = "ft_3_3",
                        promptKey = "Identify the clock showing day time ⏰:",
                        promptTextFallback = "Identify the clock showing day time ⏰:",
                        visualEmoji = "⏰",
                        options = listOf("⏰ Alarm Clock", "📱 Mobile Screen", "🔦 Flashlight", "📻 Radio"),
                        correctIndex = 0
                    )
                )
            }

            "sequence_attention" -> when (level) {
                1 -> listOf(
                    GameQuestion(
                        id = "sa_1_1",
                        promptKey = "What comes next in this simple line? 🔴 🔵 🔴 ❓",
                        promptTextFallback = "What comes next in this simple line? 🔴 🔵 🔴 ❓",
                        visualEmoji = "🔴 🔵 🔴 ❓",
                        options = listOf("🔵 Blue Circle", "🟢 Green Circle"),
                        correctIndex = 0
                    ),
                    GameQuestion(
                        id = "sa_1_2",
                        promptKey = "What number follows? 1, 2, 3, ❓",
                        promptTextFallback = "What number follows? 1, 2, 3, ❓",
                        visualEmoji = "1️⃣ 2️⃣ 3️⃣ ❓",
                        options = listOf("4️⃣ Four", "1️⃣ One"),
                        correctIndex = 0
                    )
                )
                2 -> listOf(
                    GameQuestion(
                        id = "sa_2_1",
                        promptKey = "Which symbol completes the sequence? ⭐ 🌙 ⭐ 🌙 ❓",
                        promptTextFallback = "Which symbol completes the sequence? ⭐ 🌙 ⭐ 🌙 ❓",
                        visualEmoji = "⭐ 🌙 ⭐ 🌙 ❓",
                        options = listOf("⭐ Star", "☀️ Sun", "☁️ Cloud"),
                        correctIndex = 0
                    ),
                    GameQuestion(
                        id = "sa_2_2",
                        promptKey = "Counting upwards by twos: 2, 4, 6, ❓",
                        promptTextFallback = "Counting upwards by twos: 2, 4, 6, ❓",
                        visualEmoji = "2 ➔ 4 ➔ 6 ➔ ❓",
                        options = listOf("8", "7", "3"),
                        correctIndex = 0
                    ),
                    GameQuestion(
                        id = "sa_2_3",
                        promptKey = "Shape rhythm: 🔺 ⬛ 🔺 ⬛ ❓",
                        promptTextFallback = "Shape rhythm: 🔺 ⬛ 🔺 ⬛ ❓",
                        visualEmoji = "🔺 ⬛ 🔺 ⬛ ❓",
                        options = listOf("🔺 Triangle", "⚪ Circle", "🔷 Diamond"),
                        correctIndex = 0
                    )
                )
                else -> listOf(
                    GameQuestion(
                        id = "sa_3_1",
                        promptKey = "Notice the repeating trio: 🍎 🍌 🍇 🍎 🍌 ❓",
                        promptTextFallback = "Notice the repeating trio: 🍎 🍌 🍇 🍎 🍌 ❓",
                        visualEmoji = "🍎 🍌 🍇 🍎 🍌 ❓",
                        options = listOf("🍇 Grapes", "🍎 Apple", "🍉 Watermelon", "🍒 Cherry"),
                        correctIndex = 0
                    ),
                    GameQuestion(
                        id = "sa_3_2",
                        promptKey = "Sequence count: 10, 20, 30, ❓",
                        promptTextFallback = "Sequence count: 10, 20, 30, ❓",
                        visualEmoji = "10 ➔ 20 ➔ 30 ➔ ❓",
                        options = listOf("40", "35", "50", "25"),
                        correctIndex = 0
                    ),
                    GameQuestion(
                        id = "sa_3_3",
                        promptKey = "Arrow directions: ⬆️ ➡️ ⬆️ ➡️ ❓",
                        promptTextFallback = "Arrow directions: ⬆️ ➡️ ⬆️ ➡️ ❓",
                        visualEmoji = "⬆️ ➡️ ⬆️ ➡️ ❓",
                        options = listOf("⬆️ Up", "⬇️ Down", "⬅️ Left", "🔄 Turn"),
                        correctIndex = 0
                    )
                )
            }

            "simple_pattern" -> when (level) {
                1 -> listOf(
                    GameQuestion(
                        id = "sp_1_1",
                        promptKey = "Big and Small pattern: 🐘 🐁 🐘 ❓",
                        promptTextFallback = "Big and Small pattern: 🐘 🐁 🐘 ❓",
                        visualEmoji = "🐘 🐁 🐘 ❓",
                        options = listOf("🐁 Small Mouse", "🐘 Big Elephant"),
                        correctIndex = 0
                    ),
                    GameQuestion(
                        id = "sp_1_2",
                        promptKey = "Day and Night rhythm: ☀️ 🌙 ☀️ ❓",
                        promptTextFallback = "Day and Night rhythm: ☀️ 🌙 ☀️ ❓",
                        visualEmoji = "☀️ 🌙 ☀️ ❓",
                        options = listOf("🌙 Crescent Moon", "☀️ Morning Sun"),
                        correctIndex = 0
                    )
                )
                2 -> listOf(
                    GameQuestion(
                        id = "sp_2_1",
                        promptKey = "Plant growth pattern: 🌱 🌿 🌳 🌱 🌿 ❓",
                        promptTextFallback = "Plant growth pattern: 🌱 🌿 🌳 🌱 🌿 ❓",
                        visualEmoji = "🌱 🌿 🌳 🌱 🌿 ❓",
                        options = listOf("🌳 Big Tree", "🌱 Sprout", "🍂 Dry Leaf"),
                        correctIndex = 0
                    ),
                    GameQuestion(
                        id = "sp_2_2",
                        promptKey = "Color rhythm: 🟩 🟨 🟩 🟨 ❓",
                        promptTextFallback = "Color rhythm: 🟩 🟨 🟩 🟨 ❓",
                        visualEmoji = "🟩 🟨 🟩 🟨 ❓",
                        options = listOf("🟩 Green", "🟥 Red", "🟦 Blue"),
                        correctIndex = 0
                    ),
                    GameQuestion(
                        id = "sp_2_3",
                        promptKey = "Sound volume: 🔔 🔕 🔔 🔕 ❓",
                        promptTextFallback = "Sound volume: 🔔 🔕 🔔 🔕 ❓",
                        visualEmoji = "🔔 🔕 🔔 🔕 ❓",
                        options = listOf("🔔 Ringing Bell", "🔕 Mute", "📢 Loudspeaker"),
                        correctIndex = 0
                    )
                )
                else -> listOf(
                    GameQuestion(
                        id = "sp_3_1",
                        promptKey = "Which block fits the symmetry? 🟥 🟦 🟩 🟥 🟦 ❓",
                        promptTextFallback = "Which block fits the symmetry? 🟥 🟦 🟩 🟥 🟦 ❓",
                        visualEmoji = "🟥 🟦 🟩 🟥 🟦 ❓",
                        options = listOf("🟩 Green Block", "🟨 Yellow Block", "⬛ Black Block", "🟥 Red Block"),
                        correctIndex = 0
                    ),
                    GameQuestion(
                        id = "sp_3_2",
                        promptKey = "Moon phases: 🌑 🌓 🌕 🌑 🌓 ❓",
                        promptTextFallback = "Moon phases: 🌑 🌓 🌕 🌑 🌓 ❓",
                        visualEmoji = "🌑 🌓 🌕 🌑 🌓 ❓",
                        options = listOf("🌕 Full Moon", "🌑 New Moon", "⭐ Bright Star", "☀️ Sun"),
                        correctIndex = 0
                    ),
                    GameQuestion(
                        id = "sp_3_3",
                        promptKey = "Tea garden rhythm: 🍃 ☕ 🍃 ☕ ❓",
                        promptTextFallback = "Tea garden rhythm: 🍃 ☕ 🍃 ☕ ❓",
                        visualEmoji = "🍃 ☕ 🍃 ☕ ❓",
                        options = listOf("🍃 Fresh Leaf", "🫖 Big Kettle", "🍯 Honey Jar", "🥖 Bread"),
                        correctIndex = 0
                    )
                )
            }

            "everyday_choice" -> when (level) {
                1 -> listOf(
                    GameQuestion(
                        id = "ec_1_1",
                        promptKey = "It is raining outside before stepping out. What should you take?",
                        promptTextFallback = "It is raining outside before stepping out. What should you take?",
                        visualEmoji = "🌧️",
                        options = listOf("☔ Umbrella", "🕶️ Sunglasses"),
                        correctIndex = 0
                    ),
                    GameQuestion(
                        id = "ec_1_2",
                        promptKey = "Before sitting down for lunch, what is the best habit?",
                        promptTextFallback = "Before sitting down for lunch, what is the best habit?",
                        visualEmoji = "🧼",
                        options = listOf("Wash hands with soap 🧼", "Put on walking shoes 👟"),
                        correctIndex = 0
                    )
                )
                2 -> listOf(
                    GameQuestion(
                        id = "ec_2_1",
                        promptKey = "You feel thirsty in the warm afternoon. What should you drink?",
                        promptTextFallback = "You feel thirsty in the warm afternoon. What should you drink?",
                        visualEmoji = "💧",
                        options = listOf("Fresh Clean Water 💧", "Cooking Oil 🛢️", "Liquid Detergent 🧴"),
                        correctIndex = 0
                    ),
                    GameQuestion(
                        id = "ec_2_2",
                        promptKey = "The sun has set and it is dark in the room. What is the safest choice?",
                        promptTextFallback = "The sun has set and it is dark in the room. What is the safest choice?",
                        visualEmoji = "💡",
                        options = listOf("Turn on the room light 💡", "Walk blindly in dark", "Close all doors"),
                        correctIndex = 0
                    ),
                    GameQuestion(
                        id = "ec_2_3",
                        promptKey = "In the cool winter morning of Shillong or Kohima, what should you wear?",
                        promptTextFallback = "In the cool winter morning of Shillong or Kohima, what should you wear?",
                        visualEmoji = "🧥",
                        options = listOf("Warm Woolen Sweater / Shawl 🧥", "Thin Cotton Vest 🎽", "Swimming Trunks 🩲"),
                        correctIndex = 0
                    )
                )
                else -> listOf(
                    GameQuestion(
                        id = "ec_3_1",
                        promptKey = "You hear a knock at your front door. What is the safest first step?",
                        promptTextFallback = "You hear a knock at your front door. What is the safest first step?",
                        visualEmoji = "🚪",
                        options = listOf("Look through window or ask who it is", "Immediately run outside", "Hide quietly under bed", "Throw water at door"),
                        correctIndex = 0
                    ),
                    GameQuestion(
                        id = "ec_3_2",
                        promptKey = "Your doctor prescribed an afternoon pill with water. When should you take it?",
                        promptTextFallback = "Your doctor prescribed an afternoon pill with water. When should you take it?",
                        visualEmoji = "💊",
                        options = listOf("At the scheduled time with water", "Double the dose at night", "Never take medicine", "Give to neighbor"),
                        correctIndex = 0
                    ),
                    GameQuestion(
                        id = "ec_3_3",
                        promptKey = "After writing notes, where is the best place to keep your reading glasses?",
                        promptTextFallback = "After writing notes, where is the best place to keep your reading glasses?",
                        visualEmoji = "👓",
                        options = listOf("In its glasses case on desk", "On the floor near sofa", "Inside the refrigerator", "In garden grass"),
                        correctIndex = 0
                    )
                )
            }

            else -> listOf(
                GameQuestion(
                    id = "generic_1",
                    promptKey = "Which choice feels best?",
                    promptTextFallback = "Which choice feels best?",
                    visualEmoji = "✨",
                    options = listOf("Option 1", "Option 2"),
                    correctIndex = 0
                )
            )
        }
    }

    fun generateMemoryMatchCards(level: Int): List<MemoryCard> {
        val pool = listOf(
            "🌿", // Assam tea leaf
            "🦜", // Great Hornbill
            "🌸", // Rhododendron / Orchid
            "🦏", // One-horned Rhino
            "🍵", // Cup of tea
            "🔔", // Temple bell
            "🏔️", // Mountain peak
            "🏡"  // Traditional home
        )

        val pairsCount = when (level) {
            1 -> 2 // 4 cards
            2 -> 3 // 6 cards
            else -> 4 // 8 cards
        }

        val selectedEmojis = pool.take(pairsCount)
        val cards = mutableListOf<MemoryCard>()
        var idCounter = 0

        selectedEmojis.forEach { emoji ->
            cards.add(MemoryCard(id = idCounter++, content = emoji))
            cards.add(MemoryCard(id = idCounter++, content = emoji))
        }

        return cards.shuffled()
    }
}
