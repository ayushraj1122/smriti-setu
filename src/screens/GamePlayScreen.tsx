import React, { useState, useEffect, useRef } from 'react';
import {
  Volume2,
  Pause,
  Play,
  RotateCcw,
  ArrowRight,
  CheckCircle2,
  XCircle,
  HelpCircle,
  Sparkles,
  Home
} from 'lucide-react';
import {
  GameQuestion,
  MemoryCard,
  AppLanguageCode,
  FontSizeScale
} from '../types';
import { GameQuestionGenerator } from '../data/gameQuestions';
import { getGameById } from '../data/gameCatalog';
import { ScoringEngine, AdaptiveEngine } from '../engine/scoring';
import { TextToSpeechService } from '../engine/tts';
import { t } from '../i18n/translations';

interface GamePlayScreenProps {
  gameId: string;
  level: number;
  currentLanguage: AppLanguageCode;
  highContrast: boolean;
  fontSizeScale: FontSizeScale;
  largeButtons: boolean;
  voiceEnabled: boolean;
  onFinishGame: (result: {
    score: number;
    accuracy: number;
    avgTimeMs: number;
    correct: number;
    total: number;
    recommendation: string;
  }) => void;
  onQuit: () => void;
}

export const GamePlayScreen: React.FC<GamePlayScreenProps> = ({
  gameId,
  level,
  currentLanguage,
  highContrast,
  fontSizeScale,
  largeButtons,
  voiceEnabled,
  onFinishGame,
  onQuit
}) => {
  const isMemoryMatch = gameId === 'memory_match';
  const gameDef = getGameById(gameId);

  // General state
  const [isPaused, setIsPaused] = useState(false);
  const [startTime, setStartTime] = useState<number>(Date.now());

  // Multiple Choice Questions State
  const [questions, setQuestions] = useState<GameQuestion[]>([]);
  const [currentIndex, setCurrentIndex] = useState(0);
  const [selectedAnswerIndex, setSelectedAnswerIndex] = useState<number | null>(null);
  const [isAnswerRevealed, setIsAnswerRevealed] = useState(false);
  const [correctCount, setCorrectCount] = useState(0);
  const [responseTimes, setResponseTimes] = useState<number[]>([]);
  const questionStartRef = useRef<number>(Date.now());

  // Memory Match State
  const [cards, setCards] = useState<MemoryCard[]>([]);
  const [flippedCardIds, setFlippedCardIds] = useState<number[]>([]);
  const [matchedPairs, setMatchedPairs] = useState(0);
  const [flipCount, setFlipCount] = useState(0);

  // Initialize questions or cards on mount
  useEffect(() => {
    if (isMemoryMatch) {
      const generatedCards = GameQuestionGenerator.generateMemoryMatchCards(level);
      setCards(generatedCards);
      setStartTime(Date.now());
      if (voiceEnabled) {
        TextToSpeechService.speak(
          'Match the pairs of cards. Take all the time you need.',
          currentLanguage
        );
      }
    } else {
      const generatedQuestions = GameQuestionGenerator.generateQuestions(gameId, level);
      setQuestions(generatedQuestions);
      setCurrentIndex(0);
      setStartTime(Date.now());
      questionStartRef.current = Date.now();
      if (generatedQuestions.length > 0 && voiceEnabled) {
        TextToSpeechService.speak(
          generatedQuestions[0].promptTextFallback,
          currentLanguage
        );
      }
    }
  }, [gameId, level, isMemoryMatch]);

  // Read question prompt aloud whenever index changes
  useEffect(() => {
    if (!isMemoryMatch && questions.length > 0 && questions[currentIndex]) {
      questionStartRef.current = Date.now();
      setSelectedAnswerIndex(null);
      setIsAnswerRevealed(false);
      if (voiceEnabled) {
        TextToSpeechService.speak(
          questions[currentIndex].promptTextFallback,
          currentLanguage
        );
      }
    }
  }, [currentIndex, questions, isMemoryMatch, voiceEnabled]);

  // Handle standard option selection
  const handleSelectOption = (index: number) => {
    if (isAnswerRevealed) return;

    const timeSpent = Date.now() - questionStartRef.current;
    setResponseTimes((prev) => [...prev, timeSpent]);

    setSelectedAnswerIndex(index);
    setIsAnswerRevealed(true);

    const currentQ = questions[currentIndex];
    const isCorrect = index === currentQ.correctIndex;

    if (isCorrect) {
      setCorrectCount((prev) => prev + 1);
      if (voiceEnabled) {
        TextToSpeechService.speak(t('msg_good_job', currentLanguage), currentLanguage);
      }
    } else {
      if (voiceEnabled) {
        TextToSpeechService.speak(t('msg_try_again', currentLanguage), currentLanguage);
      }
    }

    // Gentle auto-advance after 1.8 seconds for smooth dementia pacing
    setTimeout(() => {
      if (currentIndex + 1 < questions.length) {
        setCurrentIndex((prev) => prev + 1);
      } else {
        // Complete game
        completeStandardGame(isCorrect ? correctCount + 1 : correctCount, [...responseTimes, timeSpent]);
      }
    }, 1800);
  };

  const completeStandardGame = (finalCorrect: number, times: number[]) => {
    const totalQ = questions.length;
    const avgTimeMs =
      times.length > 0
        ? Math.round(times.reduce((a, b) => a + b, 0) / times.length)
        : 3500;
    const { score, accuracyPercent } = ScoringEngine.calculateScore(
      finalCorrect,
      totalQ,
      avgTimeMs,
      level
    );
    const recommendation = AdaptiveEngine.getRecommendation(accuracyPercent, level);

    onFinishGame({
      score,
      accuracy: accuracyPercent,
      avgTimeMs,
      correct: finalCorrect,
      total: totalQ,
      recommendation
    });
  };

  // Handle Memory Card Click
  const handleCardClick = (clickedCard: MemoryCard) => {
    if (clickedCard.isMatched || clickedCard.isFaceUp || flippedCardIds.length >= 2) return;

    const newFlipped = [...flippedCardIds, clickedCard.id];
    setFlippedCardIds(newFlipped);
    setFlipCount((prev) => prev + 1);

    // Update card face up state
    setCards((prev) =>
      prev.map((c) => (c.id === clickedCard.id ? { ...c, isFaceUp: true } : c))
    );

    if (newFlipped.length === 2) {
      const card1 = cards.find((c) => c.id === newFlipped[0]);
      const card2 = clickedCard;

      if (card1 && card1.content === card2.content) {
        // Match found!
        setTimeout(() => {
          setCards((prev) =>
            prev.map((c) =>
              c.id === card1.id || c.id === card2.id ? { ...c, isMatched: true } : c
            )
          );
          setFlippedCardIds([]);
          const newMatched = matchedPairs + 1;
          setMatchedPairs(newMatched);

          if (voiceEnabled) {
            TextToSpeechService.speak('Matched!', currentLanguage);
          }

          const totalPairs = cards.length / 2;
          if (newMatched >= totalPairs) {
            // Memory Match complete!
            const totalElapsedMs = Date.now() - startTime;
            const accuracy = Math.min(100, Math.round((totalPairs / Math.max(1, (flipCount + 2) / 2)) * 100));
            const { score } = ScoringEngine.calculateScore(totalPairs, totalPairs, totalElapsedMs / totalPairs, level);
            const recommendation = AdaptiveEngine.getRecommendation(accuracy, level);

            setTimeout(() => {
              onFinishGame({
                score,
                accuracy,
                avgTimeMs: Math.round(totalElapsedMs / totalPairs),
                correct: totalPairs,
                total: totalPairs,
                recommendation
              });
            }, 1000);
          }
        }, 600);
      } else {
        // No match - turn back after a brief glance
        setTimeout(() => {
          setCards((prev) =>
            prev.map((c) =>
              c.id === newFlipped[0] || c.id === newFlipped[1]
                ? { ...c, isFaceUp: false }
                : c
            )
          );
          setFlippedCardIds([]);
        }, 1200);
      }
    }
  };

  const currentQ = questions[currentIndex];

  const cardStyle = highContrast
    ? 'bg-slate-900 border-2 border-yellow-400 text-white'
    : 'bg-white border border-slate-200 text-slate-900 shadow-sm';

  const fontClass =
    fontSizeScale === 'EXTRA_LARGE'
      ? 'text-2xl'
      : fontSizeScale === 'LARGE'
      ? 'text-xl'
      : 'text-lg';

  return (
    <div className={`min-h-[calc(100vh-60px)] px-4 py-6 flex flex-col justify-between ${
      highContrast ? 'bg-slate-950 text-white' : 'bg-slate-50 text-slate-900'
    }`}>
      <div className="max-w-3xl w-full mx-auto space-y-6">
        {/* Top Game Bar */}
        <div className="flex items-center justify-between gap-3">
          <div className="flex items-center gap-3">
            <button
              id="btn-pause-game"
              type="button"
              onClick={() => setIsPaused(true)}
              className={`p-2.5 rounded-xl border ${
                highContrast
                  ? 'bg-slate-800 border-yellow-400 text-yellow-400 hover:bg-slate-700'
                  : 'bg-white border-slate-300 text-slate-700 hover:bg-slate-100'
              }`}
              title="Pause Game"
              aria-label="Pause Game"
            >
              <Pause className="w-5 h-5" />
            </button>
            <div>
              <h2 className="text-base sm:text-lg font-extrabold tracking-tight flex items-center gap-2">
                <span>{gameDef ? t(gameDef.titleKey, currentLanguage) : 'Game'}</span>
                <span className={`text-xs px-2 py-0.5 rounded-full font-bold ${
                  highContrast ? 'bg-yellow-400 text-slate-950' : 'bg-teal-100 text-teal-800'
                }`}>
                  Level {level}
                </span>
              </h2>
            </div>
          </div>

          <div className="flex items-center gap-3">
            {!isMemoryMatch && questions.length > 0 && (
              <span className="text-xs sm:text-sm font-bold opacity-80">
                Question {currentIndex + 1} of {questions.length}
              </span>
            )}
            {isMemoryMatch && (
              <span className="text-xs sm:text-sm font-bold opacity-80">
                Pairs: {matchedPairs} / {cards.length / 2}
              </span>
            )}
          </div>
        </div>

        {/* Progress bar for standard question */}
        {!isMemoryMatch && questions.length > 0 && (
          <div className={`w-full h-2 rounded-full overflow-hidden ${highContrast ? 'bg-slate-800' : 'bg-slate-200'}`}>
            <div
              className={`h-full transition-all duration-300 ${highContrast ? 'bg-yellow-400' : 'bg-teal-600'}`}
              style={{ width: `${((currentIndex + 1) / questions.length) * 100}%` }}
            />
          </div>
        )}

        {/* CONTENT 1: MEMORY MATCH */}
        {isMemoryMatch && (
          <div className={`p-6 rounded-3xl ${cardStyle} space-y-6 text-center`}>
            <div>
              <h3 className="text-xl font-extrabold mb-1">Pair the Matching Cards</h3>
              <p className="text-xs sm:text-sm text-slate-500">
                Tap two cards to reveal them. Take your time.
              </p>
            </div>

            {/* Cards Grid */}
            <div className={`grid gap-3 max-w-md mx-auto ${
              cards.length <= 6 ? 'grid-cols-3' : cards.length <= 8 ? 'grid-cols-4' : 'grid-cols-4 sm:grid-cols-6'
            }`}>
              {cards.map((card) => (
                <button
                  key={card.id}
                  id={`memory-card-${card.id}`}
                  type="button"
                  disabled={card.isMatched}
                  onClick={() => handleCardClick(card)}
                  className={`aspect-square rounded-2xl flex items-center justify-center text-3xl sm:text-4xl transition-all duration-300 transform shadow-sm ${
                    card.isMatched
                      ? highContrast
                        ? 'bg-emerald-900/60 border-2 border-emerald-400 opacity-60'
                        : 'bg-emerald-100 border-2 border-emerald-400 opacity-60'
                      : card.isFaceUp
                      ? highContrast
                        ? 'bg-yellow-400 text-slate-950 font-bold border-2 border-white scale-105'
                        : 'bg-white border-2 border-teal-600 shadow-md scale-105'
                      : highContrast
                      ? 'bg-slate-800 border-2 border-yellow-400 hover:bg-slate-700'
                      : 'bg-teal-700 hover:bg-teal-800 text-white'
                  }`}
                >
                  {card.isFaceUp || card.isMatched ? card.content : '🌸'}
                </button>
              ))}
            </div>

            <div className="flex justify-center gap-6 text-xs sm:text-sm font-semibold text-slate-500">
              <span>Flips: {flipCount}</span>
              <span>Matched: {matchedPairs}</span>
            </div>
          </div>
        )}

        {/* CONTENT 2: STANDARD MULTIPLE CHOICE QUESTION */}
        {!isMemoryMatch && currentQ && (
          <div className={`p-6 sm:p-8 rounded-3xl ${cardStyle} space-y-6`}>
            {/* Visual Icon & Audio Button */}
            <div className="flex items-center justify-between gap-3">
              <div className="w-16 h-16 rounded-2xl bg-teal-100 dark:bg-slate-800 flex items-center justify-center text-3xl">
                {currentQ.visualEmoji}
              </div>

              {voiceEnabled && (
                <button
                  id="btn-read-prompt"
                  type="button"
                  onClick={() => TextToSpeechService.speak(currentQ.promptTextFallback, currentLanguage)}
                  className={`px-3.5 py-2 rounded-xl text-xs sm:text-sm font-bold flex items-center gap-1.5 transition-colors ${
                    highContrast
                      ? 'bg-yellow-400 text-slate-950 hover:bg-yellow-300'
                      : 'bg-teal-100 text-teal-900 hover:bg-teal-200'
                  }`}
                >
                  <Volume2 className="w-4 h-4" />
                  <span>{t('btn_listen', currentLanguage)}</span>
                </button>
              )}
            </div>

            {/* Prompt Question */}
            <h3 className={`font-extrabold tracking-tight leading-snug ${fontClass}`}>
              {currentQ.promptTextFallback}
            </h3>

            {/* Options List */}
            <div className="space-y-3 pt-2">
              {currentQ.options.map((option, idx) => {
                const isSelected = selectedAnswerIndex === idx;
                const isCorrect = idx === currentQ.correctIndex;

                let optionClass = highContrast
                  ? 'bg-slate-800 border-2 border-yellow-400 text-white hover:bg-slate-750'
                  : 'bg-white border-2 border-slate-200 hover:border-teal-500 text-slate-800 shadow-sm';

                if (isAnswerRevealed) {
                  if (isCorrect) {
                    optionClass = highContrast
                      ? 'bg-emerald-950 border-2 border-emerald-400 text-emerald-300'
                      : 'bg-emerald-50 border-2 border-emerald-500 text-emerald-900';
                  } else if (isSelected) {
                    optionClass = highContrast
                      ? 'bg-rose-950 border-2 border-rose-400 text-rose-300'
                      : 'bg-rose-50 border-2 border-rose-500 text-rose-900';
                  }
                }

                return (
                  <button
                    key={idx}
                    id={`option-btn-${idx}`}
                    type="button"
                    disabled={isAnswerRevealed}
                    onClick={() => handleSelectOption(idx)}
                    className={`w-full text-left rounded-2xl flex items-center justify-between gap-3 transition-all ${
                      largeButtons ? 'p-5 text-base sm:text-lg' : 'p-4 text-sm sm:text-base'
                    } ${optionClass}`}
                  >
                    <span className="font-bold">{option}</span>
                    {isAnswerRevealed && (
                      <div>
                        {isCorrect && (
                          <CheckCircle2 className="w-6 h-6 text-emerald-500 flex-shrink-0" />
                        )}
                        {isSelected && !isCorrect && (
                          <XCircle className="w-6 h-6 text-rose-500 flex-shrink-0" />
                        )}
                      </div>
                    )}
                  </button>
                );
              })}
            </div>

            {/* Supportive feedback banner when answered */}
            {isAnswerRevealed && (
              <div
                className={`p-3.5 rounded-xl text-xs sm:text-sm font-bold flex items-center gap-2 animate-fadeIn ${
                  selectedAnswerIndex === currentQ.correctIndex
                    ? 'bg-emerald-50 text-emerald-900 border border-emerald-200'
                    : 'bg-amber-50 text-amber-900 border border-amber-200'
                }`}
              >
                <Sparkles className="w-4 h-4 flex-shrink-0" />
                <span>
                  {selectedAnswerIndex === currentQ.correctIndex
                    ? t('msg_good_job', currentLanguage)
                    : t('msg_try_again', currentLanguage)}
                </span>
              </div>
            )}
          </div>
        )}
      </div>

      {/* Pause Modal */}
      {isPaused && (
        <div className="fixed inset-0 bg-black/70 z-50 flex items-center justify-center p-4">
          <div className={`w-full max-w-sm p-6 rounded-3xl ${cardStyle} space-y-4 text-center`}>
            <h3 className="text-xl font-extrabold">Game Paused</h3>
            <p className="text-xs text-slate-500">
              Take a comfortable breath. Resume whenever you are ready.
            </p>
            <div className="space-y-2 pt-2">
              <button
                type="button"
                onClick={() => setIsPaused(false)}
                className={`w-full py-3.5 rounded-xl font-extrabold text-sm flex items-center justify-center gap-2 ${
                  highContrast ? 'bg-yellow-400 text-slate-950' : 'bg-teal-700 text-white'
                }`}
              >
                <Play className="w-4 h-4 fill-current" />
                <span>Resume Game</span>
              </button>
              <button
                type="button"
                onClick={onQuit}
                className="w-full py-3 rounded-xl font-bold text-sm text-slate-500 hover:text-slate-900 dark:hover:text-white"
              >
                Exit to Games Catalog
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};
