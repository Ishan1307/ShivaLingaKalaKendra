import { HttpErrorResponse } from '@angular/common/http';
import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { finalize } from 'rxjs';
import { AuthService } from '../../core/auth/auth.service';

@Component({
  selector: 'app-web-login',
  imports: [FormsModule, RouterLink],
  templateUrl: './web-login.html',
})
export class WebLogin {
  private readonly authService = inject(AuthService);
  private readonly changeDetector = inject(ChangeDetectorRef);

  email = '';
  password = '';
  resetEmail = '';
  loginMessage = '';
  loginError = false;
  isSubmitting = false;
  resetMessage = '';
  resetDialogOpen = false;

  submitLogin(): void {
    if (this.isSubmitting) {
      return;
    }

    this.loginMessage = '';
    this.loginError = false;
    this.isSubmitting = true;

    this.authService
      .login({ email: this.email, password: this.password })
      .pipe(
        finalize(() => {
          this.isSubmitting = false;
          this.changeDetector.markForCheck();
        }),
      )
      .subscribe({
        next: (response) => {
          this.loginMessage = response.message;
          this.changeDetector.markForCheck();
        },
        error: (error: unknown) => {
          this.loginError = true;
          this.loginMessage = this.getLoginErrorMessage(error);
          this.changeDetector.markForCheck();
        },
      });
  }

  openResetDialog(): void {
    this.resetEmail = '';
    this.resetMessage = '';
    this.resetDialogOpen = true;
  }

  closeResetDialog(): void {
    this.resetDialogOpen = false;
    this.resetMessage = '';
  }

  submitPasswordReset(): void {
    this.resetMessage = 'Password reset is not connected yet. No email has been sent.';
  }

  private getLoginErrorMessage(error: unknown): string {
    if (!(error instanceof HttpErrorResponse)) {
      return 'An unexpected error occurred. Please try again.';
    }

    if (error.status === 0) {
      return 'Unable to reach the academy server. Please try again in a moment.';
    }

    const body: unknown = error.error;
    if (
      body !== null &&
      typeof body === 'object' &&
      'message' in body &&
      typeof body.message === 'string' &&
      body.message.trim()
    ) {
      return body.message;
    }

    if (error.status === 401) {
      return 'The email or password is incorrect.';
    }

    return 'We could not sign you in right now. Please try again.';
  }
}
