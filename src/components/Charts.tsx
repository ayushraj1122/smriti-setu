import React from 'react';
import { GameAttemptEntity } from '../types';

interface ChartProps {
  attempts: GameAttemptEntity[];
  highContrast?: boolean;
}

export const AccuracyTrendChart: React.FC<ChartProps> = ({ attempts, highContrast }) => {
  if (attempts.length === 0) {
    return (
      <div className="py-8 text-center text-slate-400 text-sm font-medium">
        No completed game attempts recorded yet.
      </div>
    );
  }

  // Sort chronologically
  const sorted = [...attempts].sort((a, b) => a.timestamp - b.timestamp).slice(-10);
  const maxAccuracy = 100;
  const height = 180;
  const width = 480;
  const paddingX = 40;
  const paddingY = 24;

  const points = sorted.map((att, idx) => {
    const x = paddingX + (idx / Math.max(1, sorted.length - 1)) * (width - 2 * paddingX);
    const y = height - paddingY - (att.accuracyPercent / maxAccuracy) * (height - 2 * paddingY);
    return { x, y, accuracy: att.accuracyPercent, label: `Game ${idx + 1}` };
  });

  const pathD = points.reduce((acc, pt, i) => `${acc} ${i === 0 ? 'M' : 'L'} ${pt.x} ${pt.y}`, '');

  return (
    <div className="w-full overflow-x-auto">
      <svg
        viewBox={`0 0 ${width} ${height}`}
        className="w-full h-44 select-none"
        role="img"
        aria-label="Accuracy trend over recent sessions"
      >
        {/* Horizontal reference grid lines */}
        {[25, 50, 75, 100].map((val) => {
          const y = height - paddingY - (val / 100) * (height - 2 * paddingY);
          return (
            <g key={val}>
              <line
                x1={paddingX}
                y1={y}
                x2={width - paddingX}
                y2={y}
                stroke={highContrast ? '#334155' : '#e2e8f0'}
                strokeDasharray="3 3"
              />
              <text
                x={paddingX - 8}
                y={y + 4}
                textAnchor="end"
                fontSize="10"
                fill={highContrast ? '#94a3b8' : '#64748b'}
              >
                {val}%
              </text>
            </g>
          );
        })}

        {/* Path line */}
        <path
          d={pathD}
          fill="none"
          stroke={highContrast ? '#facc15' : '#0d9488'}
          strokeWidth="3"
          strokeLinecap="round"
          strokeLinejoin="round"
        />

        {/* Data points */}
        {points.map((pt, i) => (
          <g key={i} className="group cursor-pointer">
            <circle
              cx={pt.x}
              cy={pt.y}
              r="5"
              fill={highContrast ? '#facc15' : '#0f766e'}
              stroke={highContrast ? '#0f172a' : '#ffffff'}
              strokeWidth="2"
            />
            <text
              x={pt.x}
              y={pt.y - 10}
              textAnchor="middle"
              fontSize="10"
              fontWeight="bold"
              fill={highContrast ? '#ffffff' : '#0f172a'}
            >
              {pt.accuracy}%
            </text>
          </g>
        ))}
      </svg>
    </div>
  );
};

export const ResponseTimeChart: React.FC<ChartProps> = ({ attempts, highContrast }) => {
  if (attempts.length === 0) {
    return (
      <div className="py-8 text-center text-slate-400 text-sm font-medium">
        No completed game attempts recorded yet.
      </div>
    );
  }

  const sorted = [...attempts].sort((a, b) => a.timestamp - b.timestamp).slice(-10);
  const maxTime = Math.max(...sorted.map((a) => a.avgResponseTimeMs / 1000), 8);
  const height = 180;
  const width = 480;
  const paddingX = 40;
  const paddingY = 24;

  const points = sorted.map((att, idx) => {
    const sec = att.avgResponseTimeMs / 1000;
    const x = paddingX + (idx / Math.max(1, sorted.length - 1)) * (width - 2 * paddingX);
    const y = height - paddingY - (sec / maxTime) * (height - 2 * paddingY);
    return { x, y, sec: sec.toFixed(1) };
  });

  const pathD = points.reduce((acc, pt, i) => `${acc} ${i === 0 ? 'M' : 'L'} ${pt.x} ${pt.y}`, '');

  return (
    <div className="w-full overflow-x-auto">
      <svg
        viewBox={`0 0 ${width} ${height}`}
        className="w-full h-44 select-none"
        role="img"
        aria-label="Response time trend chart"
      >
        {/* Horizontal grid lines */}
        {[2, 4, 6, 8].map((sec) => {
          const y = height - paddingY - (sec / maxTime) * (height - 2 * paddingY);
          if (y < paddingY) return null;
          return (
            <g key={sec}>
              <line
                x1={paddingX}
                y1={y}
                x2={width - paddingX}
                y2={y}
                stroke={highContrast ? '#334155' : '#e2e8f0'}
                strokeDasharray="3 3"
              />
              <text
                x={paddingX - 8}
                y={y + 4}
                textAnchor="end"
                fontSize="10"
                fill={highContrast ? '#94a3b8' : '#64748b'}
              >
                {sec}s
              </text>
            </g>
          );
        })}

        {/* Line */}
        <path
          d={pathD}
          fill="none"
          stroke={highContrast ? '#38bdf8' : '#0284c7'}
          strokeWidth="3"
          strokeLinecap="round"
          strokeLinejoin="round"
        />

        {/* Points */}
        {points.map((pt, i) => (
          <g key={i}>
            <circle
              cx={pt.x}
              cy={pt.y}
              r="5"
              fill={highContrast ? '#38bdf8' : '#0369a1'}
              stroke={highContrast ? '#0f172a' : '#ffffff'}
              strokeWidth="2"
            />
            <text
              x={pt.x}
              y={pt.y - 10}
              textAnchor="middle"
              fontSize="10"
              fontWeight="bold"
              fill={highContrast ? '#ffffff' : '#0f172a'}
            >
              {pt.sec}s
            </text>
          </g>
        ))}
      </svg>
    </div>
  );
};

export const CategoryBreakdownChart: React.FC<ChartProps> = ({ attempts, highContrast }) => {
  const categories = [
    { key: 'orientation', name: 'Orientation', color: '#0d9488' },
    { key: 'memory', name: 'Memory', color: '#6366f1' },
    { key: 'attention', name: 'Attention', color: '#f59e0b' },
    { key: 'reasoning', name: 'Reasoning', color: '#ec4899' }
  ];

  const catData = categories.map((cat) => {
    const matching = attempts.filter((a) => a.gameCategory.toLowerCase() === cat.key);
    const count = matching.length;
    const avgAccuracy =
      count > 0 ? Math.round(matching.reduce((acc, curr) => acc + curr.accuracyPercent, 0) / count) : 0;
    return { ...cat, count, avgAccuracy };
  });

  return (
    <div className="space-y-3.5 py-2">
      {catData.map((cat) => (
        <div key={cat.key}>
          <div className="flex justify-between text-xs sm:text-sm font-semibold mb-1">
            <span className={highContrast ? 'text-white' : 'text-slate-800'}>{cat.name}</span>
            <span className={highContrast ? 'text-yellow-400' : 'text-slate-600'}>
              {cat.count > 0 ? `${cat.avgAccuracy}% (${cat.count} played)` : 'No plays yet'}
            </span>
          </div>
          <div className={`w-full h-3 rounded-full overflow-hidden ${highContrast ? 'bg-slate-800' : 'bg-slate-100'}`}>
            <div
              className="h-full rounded-full transition-all duration-500"
              style={{
                width: `${cat.count > 0 ? Math.max(5, cat.avgAccuracy) : 0}%`,
                backgroundColor: highContrast ? '#facc15' : cat.color
              }}
            />
          </div>
        </div>
      ))}
    </div>
  );
};
