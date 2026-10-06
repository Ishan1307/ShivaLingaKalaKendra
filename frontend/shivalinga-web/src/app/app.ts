import { Component, OnDestroy, OnInit, signal } from '@angular/core';

@Component({
  selector: 'app-root',
  styleUrl: './app.scss',
  templateUrl: './app.html',
})
export class App implements OnInit, OnDestroy {
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
    if (this.heroImageInterval) {
      clearInterval(this.heroImageInterval);
    }
  }
}
