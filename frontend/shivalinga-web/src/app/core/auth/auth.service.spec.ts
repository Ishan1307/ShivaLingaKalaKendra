import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { API_BASE_URL, DEFAULT_API_BASE_URL } from '../api/api.config';
import { AuthService } from './auth.service';

describe('AuthService', () => {
  let authService: AuthService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        { provide: API_BASE_URL, useValue: DEFAULT_API_BASE_URL },
      ],
    });
    authService = TestBed.inject(AuthService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpTestingController.verify());

  it('posts login credentials to the configured authentication endpoint', () => {
    const credentials = { email: 'student@example.com', password: 'correct-password' };
    let responseMessage = '';

    authService.login(credentials).subscribe((response) => {
      responseMessage = response.message;
    });

    const request = httpTestingController.expectOne('http://localhost:8080/api/auth/login');
    expect(request.request.method).toBe('POST');
    expect(request.request.body).toEqual(credentials);
    request.flush({ message: 'Login successful' });

    expect(responseMessage).toBe('Login successful');
  });
});
