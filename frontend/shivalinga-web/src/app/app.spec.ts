import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { provideRouter, Router } from '@angular/router';
import { App } from './app';
import { routes } from './app.routes';
import { API_BASE_URL, DEFAULT_API_BASE_URL } from './core/api/api.config';
import { HomePage } from './web/home-page';
import { WebLogin } from './web/web-login/web-login';

describe('App', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [App, HomePage, WebLogin],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        { provide: API_BASE_URL, useValue: DEFAULT_API_BASE_URL },
        provideRouter(routes),
      ],
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
    await TestBed.inject(Router).navigateByUrl('/our-story');
    fixture.detectChanges();
    await fixture.whenStable();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('.about-hero h1')?.textContent).toContain('A story shaped');
    expect(compiled.querySelector('.purpose-cards')?.children.length).toBe(3);
    expect(compiled.querySelector('.founder-note-image img')?.getAttribute('src')).toBe('/founder-portrait.png');
    expect(compiled.querySelector('.why-list')?.children.length).toBe(4);
  });

  it('should scroll to the beginning section without leaving /our-story', async () => {
    const router = TestBed.inject(Router);
    const fixture = TestBed.createComponent(App);
    await router.navigateByUrl('/our-story');
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
    expect(router.url).toBe('/our-story');
  });

  it('should render the login form and a reset-password dialog without sending email', async () => {
    const router = TestBed.inject(Router);
    const fixture = TestBed.createComponent(App);
    await router.navigateByUrl('/login');
    fixture.detectChanges();
    await fixture.whenStable();
    const compiled = fixture.nativeElement as HTMLElement;

    expect(compiled.querySelector('input[type="email"][name="email"]')).toBeTruthy();
    expect(compiled.querySelector('input[type="password"][name="password"]')).toBeTruthy();
    expect(compiled.querySelector('a[href="/our-story"]')).toBeTruthy();
    expect(compiled.querySelector('a[href="/#contact"]')).toBeTruthy();

    compiled.querySelector<HTMLButtonElement>('.forgot-link')?.click();
    fixture.detectChanges();
    expect(compiled.querySelector('[role="dialog"]')).toBeTruthy();

    const resetEmail = compiled.querySelector<HTMLInputElement>('#reset-email');
    if (!resetEmail) {
      throw new Error('Reset email field was not rendered');
    }
    resetEmail.value = 'student@example.com';
    resetEmail.dispatchEvent(new Event('input', { bubbles: true }));
    fixture.detectChanges();
    compiled.querySelector<HTMLFormElement>('.reset-form')?.dispatchEvent(
      new Event('submit', { bubbles: true, cancelable: true }),
    );
    fixture.detectChanges();

    expect(compiled.querySelector('.form-message')?.textContent).toContain('No email has been sent');
    expect(router.url).toBe('/login');
  });

  it('should submit credentials and display backend login success and failure messages', async () => {
    const fixture = TestBed.createComponent(App);
    const httpTestingController = TestBed.inject(HttpTestingController);
    await TestBed.inject(Router).navigateByUrl('/login');
    fixture.detectChanges();
    await fixture.whenStable();

    const compiled = fixture.nativeElement as HTMLElement;
    const email = compiled.querySelector<HTMLInputElement>('#login-email');
    const password = compiled.querySelector<HTMLInputElement>('#login-password');
    const form = compiled.querySelector<HTMLFormElement>('.login-form');
    if (!email || !password || !form) {
      throw new Error('Login form controls were not rendered');
    }

    email.value = 'student@example.com';
    email.dispatchEvent(new Event('input', { bubbles: true }));
    password.value = 'correct-password';
    password.dispatchEvent(new Event('input', { bubbles: true }));
    fixture.detectChanges();
    await fixture.whenStable();
    form.dispatchEvent(new Event('submit', { bubbles: true, cancelable: true }));
    fixture.detectChanges();

    const loginRequest = httpTestingController.expectOne('http://localhost:8080/api/auth/login');
    expect(loginRequest.request.method).toBe('POST');
    expect(loginRequest.request.body).toEqual({
      email: 'student@example.com',
      password: 'correct-password',
    });
    loginRequest.flush({ message: 'Login successful' });
    await fixture.whenStable();
    fixture.detectChanges();
    const loginComponent = fixture.debugElement.query(By.directive(WebLogin)).componentInstance as WebLogin;
    expect(loginComponent.loginMessage).toBe('Login successful');
    expect(compiled.querySelector('[role="status"]')?.textContent).toContain('Login successful');

    password.value = 'wrong-password';
    password.dispatchEvent(new Event('input', { bubbles: true }));
    fixture.detectChanges();
    await fixture.whenStable();
    form.dispatchEvent(new Event('submit', { bubbles: true, cancelable: true }));
    fixture.detectChanges();

    const failedLoginRequest = httpTestingController.expectOne('http://localhost:8080/api/auth/login');
    failedLoginRequest.flush(
      { message: 'Invalid email or password' },
      { status: 401, statusText: 'Unauthorized' },
    );
    await fixture.whenStable();
    fixture.detectChanges();
    expect(loginComponent.loginMessage).toBe('Invalid email or password');
    expect(compiled.querySelector('[role="alert"]')?.textContent).toContain('Invalid email or password');
    httpTestingController.verify();
  });
});
