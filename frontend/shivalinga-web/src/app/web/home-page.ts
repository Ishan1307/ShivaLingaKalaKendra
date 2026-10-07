import { AfterViewInit, Component, ElementRef, OnDestroy, OnInit, signal, ViewChild } from '@angular/core';

@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.html',
})
export class HomePage implements AfterViewInit, OnInit, OnDestroy {
  @ViewChild('homePage') private homePage?: ElementRef<HTMLElement>;

  readonly heroImages = [
    {
      src: 'https://images.unsplash.com/photo-1504609813442-a8924e83f76e?auto=format&fit=crop&w=2000&q=85',
      position: 'center 38%',
    },
    {
      src: 'https://images.unsplash.com/photo-1547153760-18fc86324498?auto=format&fit=crop&w=2000&q=85',
      position: 'center',
    },
    {
      src: 'https://images.unsplash.com/photo-1508700115892-45ecd05ae2ad?auto=format&fit=crop&w=2000&q=85',
      position: 'center',
    },
  ];
  readonly activeHeroImage = signal(0);

  private heroImageInterval?: ReturnType<typeof setInterval>;
  private revealObserver?: IntersectionObserver;

  ngAfterViewInit(): void {
    const revealElements = this.homePage?.nativeElement.querySelectorAll<HTMLElement>('.home-reveal');
    if (!revealElements) {
      return;
    }

    if (
      typeof IntersectionObserver === 'undefined' ||
      (typeof window.matchMedia === 'function' &&
        window.matchMedia('(prefers-reduced-motion: reduce)').matches)
    ) {
      revealElements.forEach((element) => element.classList.add('is-visible'));
      return;
    }

    this.revealObserver = new IntersectionObserver(
      (entries) => {
        entries.forEach((entry) => {
          if (entry.isIntersecting) {
            entry.target.classList.add('is-visible');
            this.revealObserver?.unobserve(entry.target);
          }
        });
      },
      { threshold: 0.14 },
    );

    revealElements.forEach((element) => this.revealObserver?.observe(element));
  }

  ngOnInit(): void {
    if (
      typeof window === 'undefined' ||
      (typeof window.matchMedia === 'function' &&
        window.matchMedia('(prefers-reduced-motion: reduce)').matches)
    ) {
      return;
    }

    this.heroImageInterval = setInterval(() => {
      this.activeHeroImage.update((activeImage) => (activeImage + 1) % this.heroImages.length);
    }, 10_000);
  }

  ngOnDestroy(): void {
    this.revealObserver?.disconnect();
    if (this.heroImageInterval) {
      clearInterval(this.heroImageInterval);
    }
  }
}
