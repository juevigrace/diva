import { getIconPaths } from '@lib/icons';

interface SidebarIconProps {
  icon: string;
  className?: string;
}

export default function SidebarIcon({ icon, className = 'h-4 w-4 shrink-0' }: SidebarIconProps) {
  const paths = getIconPaths(icon);
  return (
    <svg className={className} fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth="2">
      {paths.map((d) => (
        <path key={d} strokeLinecap="round" strokeLinejoin="round" d={d} />
      ))}
    </svg>
  );
}
