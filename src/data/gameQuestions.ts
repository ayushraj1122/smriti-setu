import { GameQuestion, MemoryCard } from '../types';

export class GameQuestionGenerator {
  /**
   * Generates questions for a specific game and level.
   */
  public static generateQuestions(gameId: string, level: number): GameQuestion[] {
    switch (gameId) {
      case 'day_date':
        return this.generateDayDateQuestions(level);
      case 'place_time':
        return this.generatePlaceTimeQuestions(level);
      case 'remember_objects':
        return this.generateRememberObjectsQuestions(level);
      case 'find_target':
        return this.generateFindTargetQuestions(level);
      case 'sequence_attention':
        return this.generateSequenceAttentionQuestions(level);
      case 'simple_pattern':
        return this.generateSimplePatternQuestions(level);
      case 'everyday_choice':
        return this.generateEverydayChoiceQuestions(level);
      default:
        return this.generateDayDateQuestions(level);
    }
  }

  /**
   * Generates memory match cards for Memory Match game.
   */
  public static generateMemoryMatchCards(level: number): MemoryCard[] {
    const northEastIcons = [
      '🦏', // Rhino
      '🌿', // Tea Leaf
      '🪶', // Hornbill Feather
      '🌸', // Orchid
      '🔔', // Temple Bell
      '🐘', // Elephant
      '🎋', // Bamboo
      '🍵'  // Assam Tea Cup
    ];

    // Level 1: 3 pairs (6 cards)
    // Level 2: 4 pairs (8 cards)
    // Level 3: 6 pairs (12 cards)
    const pairCount = level === 1 ? 3 : level === 2 ? 4 : 6;
    const selectedIcons = northEastIcons.slice(0, pairCount);
    
    const cardPool: MemoryCard[] = [];
    let idCounter = 1;

    selectedIcons.forEach((icon) => {
      cardPool.push({ id: idCounter++, content: icon, isFaceUp: false, isMatched: false });
      cardPool.push({ id: idCounter++, content: icon, isFaceUp: false, isMatched: false });
    });

    // Deterministic shuffle
    return cardPool.sort(() => Math.random() - 0.5);
  }

  private static generateDayDateQuestions(level: number): GameQuestion[] {
    const today = new Date();
    const days = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];
    const months = [
      'January', 'February', 'March', 'April', 'May', 'June',
      'July', 'August', 'September', 'October', 'November', 'December'
    ];

    const currentDayName = days[today.getDay()];
    const currentMonthName = months[today.getMonth()];
    const currentYear = today.getFullYear();

    // Determine current time period
    const hour = today.getHours();
    let currentPeriod = 'Morning';
    if (hour >= 12 && hour < 17) currentPeriod = 'Afternoon';
    else if (hour >= 17 && hour < 21) currentPeriod = 'Evening';
    else if (hour >= 21 || hour < 5) currentPeriod = 'Night';

    const questions: GameQuestion[] = [];

    // Question 1: What day is today?
    const otherDays = days.filter((d) => d !== currentDayName);
    const dayOptions = [currentDayName, otherDays[0], otherDays[1]].sort(() => Math.random() - 0.5);
    questions.push({
      id: 'dd_1',
      promptKey: 'q_which_day',
      promptTextFallback: 'Which day of the week is today?',
      visualEmoji: '☀️',
      options: dayOptions,
      correctIndex: dayOptions.indexOf(currentDayName)
    });

    // Question 2: What period of the day is it?
    const periods = ['Morning', 'Afternoon', 'Evening'];
    const periodOptions = [currentPeriod, ...periods.filter((p) => p !== currentPeriod).slice(0, 2)].sort(
      () => Math.random() - 0.5
    );
    questions.push({
      id: 'dd_2',
      promptKey: 'q_which_period',
      promptTextFallback: 'What part of the day is it right now?',
      visualEmoji: '⏰',
      options: periodOptions,
      correctIndex: periodOptions.indexOf(currentPeriod)
    });

    // Question 3: What month is this?
    const otherMonths = months.filter((m) => m !== currentMonthName);
    const monthOptions = [currentMonthName, otherMonths[0], otherMonths[1]].sort(() => Math.random() - 0.5);
    questions.push({
      id: 'dd_3',
      promptKey: 'q_which_month',
      promptTextFallback: 'Which month are we currently in?',
      visualEmoji: '🗓️',
      options: monthOptions,
      correctIndex: monthOptions.indexOf(currentMonthName)
    });

    if (level >= 2) {
      // Question 4: Current year
      const yearOptions = [String(currentYear), String(currentYear - 1), String(currentYear + 1)].sort(
        () => Math.random() - 0.5
      );
      questions.push({
        id: 'dd_4',
        promptKey: 'q_which_year',
        promptTextFallback: 'Which year is this?',
        visualEmoji: '📅',
        options: yearOptions,
        correctIndex: yearOptions.indexOf(String(currentYear))
      });
    }

    if (level >= 3) {
      // Question 5: Season in North East India
      const monthIdx = today.getMonth();
      let season = 'Spring / Pre-Monsoon';
      if (monthIdx >= 5 && monthIdx <= 8) season = 'Monsoon (Rainy)';
      else if (monthIdx >= 9 && monthIdx <= 10) season = 'Autumn';
      else if (monthIdx >= 11 || monthIdx <= 1) season = 'Winter';

      const seasonOptions = [season, 'Midsummer Heat', 'Desert Dryness'].sort(() => Math.random() - 0.5);
      questions.push({
        id: 'dd_5',
        promptKey: 'q_which_season',
        promptTextFallback: 'What season is typical for this time in our region?',
        visualEmoji: '🌦️',
        options: seasonOptions,
        correctIndex: seasonOptions.indexOf(season)
      });
    }

    return questions;
  }

  private static generatePlaceTimeQuestions(level: number): GameQuestion[] {
    return [
      {
        id: 'pt_1',
        promptKey: 'q_kaziranga_rhino',
        promptTextFallback: 'Which famous animal is protected in Assam’s Kaziranga National Park?',
        visualEmoji: '🦏',
        options: ['One-Horned Rhinoceros', 'Desert Camel', 'Polar Bear'],
        correctIndex: 0
      },
      {
        id: 'pt_2',
        promptKey: 'q_shillong_meghalaya',
        promptTextFallback: 'Which state in North East India is known as the "Abode of Clouds" (Meghalaya)?',
        visualEmoji: '☁️',
        options: ['Meghalaya (Shillong)', 'Rajasthan', 'Goa'],
        correctIndex: 0
      },
      {
        id: 'pt_3',
        promptKey: 'q_brahmaputra_river',
        promptTextFallback: 'What is the grand river that flows through the heart of Assam?',
        visualEmoji: '🌊',
        options: ['Brahmaputra River', 'Thames River', 'Nile River'],
        correctIndex: 0
      },
      ...(level >= 2
        ? [
            {
              id: 'pt_4',
              promptKey: 'q_living_root_bridges',
              promptTextFallback: 'Where are the world-famous bio-engineered Living Root Bridges located?',
              visualEmoji: '🌉',
              options: ['Meghalaya', 'Kerala', 'Gujarat'],
              correctIndex: 0
            },
            {
              id: 'pt_5',
              promptKey: 'q_loktak_lake',
              promptTextFallback: 'Which freshwater lake in Manipur is famous for circular floating phumdis?',
              visualEmoji: '🛶',
              options: ['Loktak Lake', 'Dal Lake', 'Chilika Lake'],
              correctIndex: 0
            }
          ]
        : [])
    ];
  }

  private static generateRememberObjectsQuestions(level: number): GameQuestion[] {
    return [
      {
        id: 'ro_1',
        promptKey: 'q_remember_tea_cup',
        promptTextFallback: 'Look closely at this warm morning beverage: 🍵 Tea cup. What was shown?',
        visualEmoji: '🍵',
        options: ['Warm Tea Cup', 'Iron Hammer', 'Wooden Wheelbarrow'],
        correctIndex: 0
      },
      {
        id: 'ro_2',
        promptKey: 'q_remember_orchid',
        promptTextFallback: 'Observe this blooming blossom: 🌸 Fox-tail Orchid. What flower was presented?',
        visualEmoji: '🌸',
        options: ['North East Orchid', 'Cactus Thorn', 'Pinecone'],
        correctIndex: 0
      },
      {
        id: 'ro_3',
        promptKey: 'q_remember_umbrella',
        promptTextFallback: 'During North East rains, we carry this: ☂️ Umbrella. What was displayed?',
        visualEmoji: '☂️',
        options: ['Rain Umbrella', 'Cricket Bat', 'Cooking Pan'],
        correctIndex: 0
      },
      ...(level >= 2
        ? [
            {
              id: 'ro_4',
              promptKey: 'q_remember_bell',
              promptTextFallback: 'A temple or prayer bell: 🔔 Brass Bell. Which item was on screen?',
              visualEmoji: '🔔',
              options: ['Brass Bell', 'Motor Key', 'Steel Lock'],
              correctIndex: 0
            }
          ]
        : [])
    ];
  }

  private static generateFindTargetQuestions(level: number): GameQuestion[] {
    return [
      {
        id: 'ft_1',
        promptKey: 'q_find_sun',
        promptTextFallback: 'Spot and tap the bright Sun among these items:',
        visualEmoji: '☀️',
        options: ['☀️ Sun', '🌧️ Cloud', '⭐ Star', '🌙 Moon'],
        correctIndex: 0
      },
      {
        id: 'ft_2',
        promptKey: 'q_find_leaf',
        promptTextFallback: 'Find the fresh green Assam Tea Leaf:',
        visualEmoji: '🌿',
        options: ['🪵 Wood', '🌿 Tea Leaf', '🪨 Pebble', '🌾 Straw'],
        correctIndex: 1
      },
      {
        id: 'ft_3',
        promptKey: 'q_find_bird',
        promptTextFallback: 'Identify the majestic bird (Great Hornbill):',
        visualEmoji: '🦅',
        options: ['🐟 Fish', '🦋 Butterfly', '🦅 Hornbill', '🐢 Turtle'],
        correctIndex: 2
      },
      ...(level >= 2
        ? [
            {
              id: 'ft_4',
              promptKey: 'q_find_circle',
              promptTextFallback: 'Select the smooth round Green Circle:',
              visualEmoji: '🟢',
              options: ['🟥 Red Square', '🔺 Triangle', '🟢 Green Circle', '⬛ Black Box'],
              correctIndex: 2
            }
          ]
        : [])
    ];
  }

  private static generateSequenceAttentionQuestions(level: number): GameQuestion[] {
    return [
      {
        id: 'sa_1',
        promptKey: 'q_sequence_numbers',
        promptTextFallback: 'Look at the counting order: 1 , 2 , 3 , __ ? What number comes next?',
        visualEmoji: '🔢',
        options: ['4', '6', '2'],
        correctIndex: 0
      },
      {
        id: 'sa_2',
        promptKey: 'q_sequence_fruits',
        promptTextFallback: 'Look at the line: 🍎 , 🍌 , 🍎 , 🍌 , __ ? What comes next?',
        visualEmoji: '🍎',
        options: ['🍎 Apple', '🥥 Coconut', '🥕 Carrot'],
        correctIndex: 0
      },
      {
        id: 'sa_3',
        promptKey: 'q_sequence_traffic',
        promptTextFallback: 'Look at the shapes: ⚪ , ⚫ , ⚪ , ⚫ , __ ? What comes next?',
        visualEmoji: '⚪',
        options: ['⚪ White Circle', '🟩 Green Tile', '🟨 Yellow Dot'],
        correctIndex: 0
      },
      ...(level >= 2
        ? [
            {
              id: 'sa_4',
              promptKey: 'q_sequence_tens',
              promptTextFallback: 'Counting by tens: 10 , 20 , 30 , __ ?',
              visualEmoji: '🔟',
              options: ['40', '55', '25'],
              correctIndex: 0
            }
          ]
        : [])
    ];
  }

  private static generateSimplePatternQuestions(level: number): GameQuestion[] {
    return [
      {
        id: 'sp_1',
        promptKey: 'q_pattern_colors',
        promptTextFallback: 'Look at the color pattern: 🔵 🔴 🔵 🔴 __ ? What fits next?',
        visualEmoji: '🔵',
        options: ['🔵 Blue Dot', '🟡 Yellow Dot', '🟢 Green Dot'],
        correctIndex: 0
      },
      {
        id: 'sp_2',
        promptKey: 'q_pattern_nature',
        promptTextFallback: 'Nature cycle: 🌞 Day , 🌙 Night , 🌞 Day , __ ?',
        visualEmoji: '🌙',
        options: ['🌙 Night', '⚡ Lightning', '❄️ Snow'],
        correctIndex: 0
      },
      {
        id: 'sp_3',
        promptKey: 'q_pattern_arrows',
        promptTextFallback: 'Direction rhythm: ⬆️ Up , ⬇️ Down , ⬆️ Up , __ ?',
        visualEmoji: '⬇️',
        options: ['⬇️ Down', '⬅️ Left', '➡️ Right'],
        correctIndex: 0
      },
      ...(level >= 2
        ? [
            {
              id: 'sp_4',
              promptKey: 'q_pattern_sizing',
              promptTextFallback: 'Size progression: 🔹 Small , 🔷 Big , 🔹 Small , __ ?',
              visualEmoji: '🔷',
              options: ['🔷 Big', '⚪ Circle', '◽ Tiny Dot'],
              correctIndex: 0
            }
          ]
        : [])
    ];
  }

  private static generateEverydayChoiceQuestions(level: number): GameQuestion[] {
    return [
      {
        id: 'ec_1',
        promptKey: 'q_choice_rain',
        promptTextFallback: 'It starts raining outside in Guwahati. What should you take when stepping out?',
        visualEmoji: '🌧️',
        options: ['Umbrella or Raincoat ☂️', 'Sunglasses 🕶️', 'Electric Iron 🔌'],
        correctIndex: 0
      },
      {
        id: 'ec_2',
        promptKey: 'q_choice_sleep',
        promptTextFallback: 'It is 10:00 PM at night and your eyes feel heavy. What is the best action?',
        visualEmoji: '🛌',
        options: ['Go to bed for restful sleep 😴', 'Start chopping firewood 🪵', 'Turn on loud speakers 📢'],
        correctIndex: 0
      },
      {
        id: 'ec_3',
        promptKey: 'q_choice_medicine',
        promptTextFallback: 'The morning reminder chimes for your prescribed medicine. What should you do?',
        visualEmoji: '💊',
        options: ['Take medicine with water 🥛', 'Hide the pills under rug', 'Ignore it completely'],
        correctIndex: 0
      },
      ...(level >= 2
        ? [
            {
              id: 'ec_4',
              promptKey: 'q_choice_reading',
              promptTextFallback: 'You want to read your favorite local Assamese / Hindi newspaper, but the print looks blurry. What helps?',
              visualEmoji: '👓',
              options: ['Wear reading spectacles 👓', 'Sit in darkness', 'Turn paper upside down'],
              correctIndex: 0
            }
          ]
        : [])
    ];
  }
}
