export type ButtonVariant = 'default' | 'destructive' | 'outline' | 'secondary' | 'ghost' | 'link';

export type ButtonSize =
  'default' | 'xs' | 'sm' | 'lg' | 'icon' | 'icon-xs' | 'icon-sm' | 'icon-lg';

export type Image = {
  src: string;
  alt?: string;
};

export type NavLink = {
  label: string;
  href: string;
};

export type NavColumn = {
  title: string;
  links: NavLink[];
};

export type CtaLink = {
  label: string;
  href: string;
  variant?: ButtonVariant;
  size?: ButtonSize;
};

export type Brand = {
  title: string;
  href?: string;
  image?: Image;
};

export type HeaderProps = {
  brand?: Brand;
  columns?: NavColumn[];
};

export type HeroProps = {
  title?: string;
  description?: string;
  image?: Image;
  ctas?: CtaLink[];
  id?: string;
};

export type FeatureCardProps = {
  title: string;
  description: string;
  image?: Image;
};

export type FooterProps = {
  copyright?: string;
  columns?: NavColumn[];
  brand?: Brand;
};

export type LayoutProps = {
  title?: string;
  description?: string;
  bodyClass?: string;
};
