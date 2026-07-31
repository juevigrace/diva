export function isActive(href: string, currentPath: string) {
  if (href === '/') return currentPath === '/';
  return currentPath.startsWith(href);
}

export function getUserInitials(username?: string, email?: string, displayName?: string) {
  if (displayName) {
    return displayName
    .split(' ')
    .map((n: string) => n[0])
    .join('')
    .toUpperCase()
    .slice(0, 2);
  }
  return (username || email || 'U').slice(0, 2).toUpperCase();
}

export function showStatus(
  setter: (s: string) => void,
  _setError: (e: boolean) => void,
  msg: string,
  isError: boolean
) {
  setter(msg);
  _setError(isError);
  setTimeout(() => setter(''), 3000);
}

export function buildPageArray(page: number, totalPages: number): (number | 'ellipsis')[] {
  const pages: (number | 'ellipsis')[] = [];
  if (totalPages > 1) {
    for (let i = 1; i <= totalPages; i++) {
      if (Math.abs(i - page) <= 2 || i === 1 || i === totalPages) {
        pages.push(i);
      } else if (pages[pages.length - 1] !== 'ellipsis') {
        pages.push('ellipsis');
      }
    }
  }
  return pages;
}
