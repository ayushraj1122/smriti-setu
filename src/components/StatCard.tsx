import React from 'react';

interface StatCardProps {
  id?: string;
  label: string;
  value: string | number;
  subtitle?: string;
  icon?: React.ReactNode;
  highContrast?: boolean;
}

export const StatCard: React.FC<StatCardProps> = ({
  id,
  label,
  value,
  subtitle,
  icon,
  highContrast
}) => {
  return (
    <div
      id={id}
      className={`rounded-2xl p-4 sm:p-5 flex flex-col justify-between transition-all duration-150 shadow-sm ${
        highContrast
          ? 'bg-slate-900 border-2 border-yellow-400 text-white'
          : 'bg-white border border-slate-200 text-slate-900 hover:border-teal-300'
      }`}
    >
      <div className="flex items-center justify-between mb-2">
        <span className={`text-xs sm:text-sm font-semibold uppercase tracking-wider ${
          highContrast ? 'text-yellow-400' : 'text-slate-500'
        }`}>
          {label}
        </span>
        {icon && (
          <div className={`p-2 rounded-xl ${
            highContrast ? 'bg-slate-800 text-yellow-400' : 'bg-teal-50 text-teal-700'
          }`}>
            {icon}
          </div>
        )}
      </div>

      <div>
        <div className={`text-2xl sm:text-3xl font-extrabold tracking-tight ${
          highContrast ? 'text-white' : 'text-slate-900'
        }`}>
          {value}
        </div>
        {subtitle && (
          <p className={`text-xs mt-1 font-medium ${
            highContrast ? 'text-slate-400' : 'text-slate-500'
          }`}>
            {subtitle}
          </p>
        )}
      </div>
    </div>
  );
};
