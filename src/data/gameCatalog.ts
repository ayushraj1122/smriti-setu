import { GameDefinition, GameCategory } from '../types';

export const GAME_CATALOG: GameDefinition[] = [
  {
    id: 'day_date',
    titleKey: 'game_day_date',
    subtitleKey: 'game_day_date_desc',
    category: 'orientation',
    iconEmoji: '📅',
    totalLevels: 3
  },
  {
    id: 'place_time',
    titleKey: 'game_place_time',
    subtitleKey: 'game_place_time_desc',
    category: 'orientation',
    iconEmoji: '🏞️',
    totalLevels: 3
  },
  {
    id: 'memory_match',
    titleKey: 'game_memory_match',
    subtitleKey: 'game_memory_match_desc',
    category: 'memory',
    iconEmoji: '🃏',
    totalLevels: 3
  },
  {
    id: 'remember_objects',
    titleKey: 'game_remember_objects',
    subtitleKey: 'game_remember_objects_desc',
    category: 'memory',
    iconEmoji: '🧺',
    totalLevels: 3
  },
  {
    id: 'find_target',
    titleKey: 'game_find_target',
    subtitleKey: 'game_find_target_desc',
    category: 'attention',
    iconEmoji: '🎯',
    totalLevels: 3
  },
  {
    id: 'sequence_attention',
    titleKey: 'game_sequence_attention',
    subtitleKey: 'game_sequence_attention_desc',
    category: 'attention',
    iconEmoji: '🔢',
    totalLevels: 3
  },
  {
    id: 'simple_pattern',
    titleKey: 'game_simple_pattern',
    subtitleKey: 'game_simple_pattern_desc',
    category: 'reasoning',
    iconEmoji: '🧩',
    totalLevels: 3
  },
  {
    id: 'everyday_choice',
    titleKey: 'game_everyday_choice',
    subtitleKey: 'game_everyday_choice_desc',
    category: 'reasoning',
    iconEmoji: '💡',
    totalLevels: 3
  }
];

export function getGameById(id: string): GameDefinition | undefined {
  return GAME_CATALOG.find((g) => g.id === id);
}

export function getGamesByCategory(category: GameCategory): GameDefinition[] {
  if (category === 'all') return GAME_CATALOG;
  return GAME_CATALOG.filter((g) => g.category === category);
}
