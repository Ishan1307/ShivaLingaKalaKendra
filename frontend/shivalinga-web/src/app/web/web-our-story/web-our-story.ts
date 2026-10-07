import { AfterViewInit, Component, ElementRef, OnDestroy, ViewChild } from '@angular/core';

@Component({
  selector: 'app-web-our-story',
  templateUrl: './web-our-story.html',
})
export class WebOurStory implements AfterViewInit, OnDestroy {
  @ViewChild('aboutPage') private aboutPage?: ElementRef<HTMLElement>;
  @ViewChild('founderStory') private founderStory?: ElementRef<HTMLElement>;

  private revealObserver?: IntersectionObserver;

  scrollToBeginning(event: MouseEvent): void {
    event.preventDefault();
    const prefersReducedMotion =
      typeof window !== 'undefined' &&
      typeof window.matchMedia === 'function' &&
      window.matchMedia('(prefers-reduced-motion: reduce)').matches;

    this.founderStory?.nativeElement.scrollIntoView({
      behavior: prefersReducedMotion ? 'auto' : 'smooth',
      block: 'start',
    });
  }

  ngAfterViewInit(): void {
    const revealElements = this.aboutPage?.nativeElement.querySelectorAll<HTMLElement>('.story-reveal');
    if (!revealElements) {
      return;
    }

    if (
      typeof IntersectionObserver === 'undefined' ||
      (typeof window !== 'undefined' &&
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
      { threshold: 0.16 },
    );

    revealElements.forEach((element) => this.revealObserver?.observe(element));
  }

  ngOnDestroy(): void {
    this.revealObserver?.disconnect();
  }
}
