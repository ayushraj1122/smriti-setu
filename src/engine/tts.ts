import { AppLanguageCode } from '../types';

export class TextToSpeechService {
  private static synth: SpeechSynthesis | null = typeof window !== 'undefined' ? window.speechSynthesis : null;
  private static isSpeaking: boolean = false;

  private static langMap: Record<AppLanguageCode, string> = {
    en: 'en-IN',
    hi: 'hi-IN',
    as: 'as-IN',
    bn: 'bn-IN',
    mni: 'mni-IN',
    kha: 'kha-IN',
    lus: 'lus-IN',
    nag: 'en-IN'
  };

  public static speak(text: string, lang: AppLanguageCode = 'en', rate: number = 0.88): void {
    if (!this.synth) {
      console.warn('SpeechSynthesis is not supported in this browser environment.');
      return;
    }

    try {
      this.synth.cancel();

      // Clean speech text for screen reader clarity
      const cleanText = text.replace(/[\u{1F300}-\u{1F6FF}]/gu, '');
      const utterance = new SpeechSynthesisUtterance(cleanText);
      utterance.lang = this.langMap[lang] || 'en-IN';
      utterance.rate = rate; // Slower cadence for cognitive clarity
      utterance.pitch = 1.0;
      utterance.volume = 1.0;

      utterance.onstart = () => {
        this.isSpeaking = true;
      };

      utterance.onend = () => {
        this.isSpeaking = false;
      };

      utterance.onerror = (e) => {
        console.warn('SpeechSynthesis error:', e);
        this.isSpeaking = false;
      };

      this.synth.speak(utterance);
    } catch (err) {
      console.warn('TTS playback error:', err);
    }
  }

  public static stop(): void {
    if (this.synth) {
      this.synth.cancel();
      this.isSpeaking = false;
    }
  }

  public static getSpeakingStatus(): boolean {
    return this.isSpeaking;
  }
}
