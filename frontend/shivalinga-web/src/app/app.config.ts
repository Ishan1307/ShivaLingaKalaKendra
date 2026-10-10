import { ApplicationConfig, provideBrowserGlobalErrorListeners } from '@angular/core';
import { provideHttpClient } from '@angular/common/http';
import { provideRouter, withInMemoryScrolling } from '@angular/router';
import { API_BASE_URL, DEFAULT_API_BASE_URL } from './core/api/api.config';
import { routes } from './app.routes';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideHttpClient(),
    { provide: API_BASE_URL, useValue: DEFAULT_API_BASE_URL },
    provideRouter(routes, withInMemoryScrolling({ anchorScrolling: 'enabled' })),
  ]
};
