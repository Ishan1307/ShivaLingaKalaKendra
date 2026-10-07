import { TestBed } from '@angular/core/testing';
import { provideRouter, Router } from '@angular/router';
import { App } from './app';
import { routes } from './app.routes';
import { HomePage } from './web/home-page';

describe('App', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [App, HomePage],
      providers: [provideRouter(routes)],
    })
      .compileComponents();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(App);
    fixture.detectChanges();
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it('should render title', async () => {
    const fixture = TestBed.createComponent(HomePage);
    fixture.detectChanges();
    await fixture.whenStable();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('h1')?.textContent).toContain('Where tradition');
    expect(compiled.querySelector('.intro-copy.home-reveal.is-visible')).toBeTruthy();
    expect(compiled.querySelector('.moments-image.home-reveal')).toBeTruthy();
  });

  it('should render the story page at /about', async () => {
    const fixture = TestBed.createComponent(App);
    await TestBed.inject(Router).navigateByUrl('/about');
    fixture.detectChanges();
    await fixture.whenStable();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('.about-hero h1')?.textContent).toContain('A story shaped');
    expect(compiled.querySelector('.purpose-cards')?.children.length).toBe(3);
    expect(compiled.querySelector('.founder-note-image img')?.getAttribute('src')).toBe('/founder-portrait.png');
    expect(compiled.querySelector('.why-list')?.children.length).toBe(4);
  });

  it('should scroll to the beginning section without leaving /about', async () => {
    const router = TestBed.inject(Router);
    const fixture = TestBed.createComponent(App);
    await router.navigateByUrl('/about');
    fixture.detectChanges();
    await fixture.whenStable();
    const compiled = fixture.nativeElement as HTMLElement;
    const beginning = compiled.querySelector('#founder-story') as HTMLElement;
    let didScrollToBeginning = false;
    beginning.scrollIntoView = () => {
      didScrollToBeginning = true;
    };

    compiled.querySelector<HTMLAnchorElement>('.about-hero-copy a')?.dispatchEvent(
      new MouseEvent('click', { bubbles: true, cancelable: true }),
    );

    expect(didScrollToBeginning).toBe(true);
    expect(router.url).toBe('/about');
  });
});
