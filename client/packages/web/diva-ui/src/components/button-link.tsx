import type { ComponentProps, PropsWithChildren } from 'react';
import type { VariantProps } from 'class-variance-authority';
import { Button, type buttonVariants } from './ui/button';

type ButtonLinkProps = PropsWithChildren<VariantProps<typeof buttonVariants> & ComponentProps<'a'>>;

export function ButtonLink({
  href,
  variant,
  size,
  className,
  children,
  ...props
}: ButtonLinkProps) {
  return (
    <Button asChild variant={variant} size={size}>
      <a href={href} className={className} {...props}>
        {children}
      </a>
    </Button>
  );
}
